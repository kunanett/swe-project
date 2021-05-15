package results;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that manages database operations.
 * Other classes can access the database only through this class. The classes only instance can either be the standard instance that manages the application's database, or the test instance which is used during JUnit testing.
 */
public class RankingsManager {
    private static Jdbi jdbi;

    private static RankingsManager instance;

    private RankingsManager(String db) {
        jdbi = Jdbi.create(db).setSqlLogger(new Slf4JSqlLogger()).installPlugin(new SqlObjectPlugin());
    }

    /**
     * Returns the instance of this class.
     *
     * @return the only instance of {@code RankingsManager} class
     */
    public static RankingsManager getInstance() {
        instance = new RankingsManager("jdbc:h2:file:~/testdb");
        return instance;
    }

    /**
     * Returns the instance of this class that is used for JUnit testing.
     *
     * This instance accesses a test database.
     *
     * @return the test instance of {@code RankingsManager} class
     */
    public static RankingsManager getTestInstance() {
        instance = new RankingsManager("jdbc:h2:file:~/test");
        return instance;
    }

    /**
     * Creates the rankings table in the database.
     * The table will not be created if it already exists.
     */
    public void createTable() {
        jdbi.useExtension(PlayerDao.class, PlayerDao::createTable);
    }

    /**
     * Inserts a player in the database rankings table.
     *
     * @param player a {@code Player} object to be inserted in the database
     */
    public void insertPlayer(Player player) {
        if (!playerExists(player.getNickname())) {
            jdbi.useExtension(PlayerDao.class, dao -> dao.insertPlayer(player));
        }
    }

    /**
     * Returns information about all the {@code Player} objects in the database.
     *
     * @return a {@code List} containing all the {@code Player} objects from the database
     */
    public List<Player> getRankings() {
        return jdbi.withExtension(PlayerDao.class, PlayerDao::listRankings);
    }

    /**
     * Checks if a {@code Player} exists in the database.
     * The {@code Player} is being searched by their nickname.
     *
     * @param nickname - a {@code String} containing the {@code Player} object's nickname
     * @return {@code true} if the {@code Player} exists in the database, {@code false} otherwise
     */
    private boolean playerExists(String nickname) {
        Optional<Player> player = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname(nickname));
        return player.isPresent();
    }

    /**
     * Retrieves the points of a {@code Player}.
     *
     * @param nickname the {@code String} containing the nickname of the {@code Player}
     * @return the {@code long} value of the {@code Player}'s points or 0 if the player is not in the database
     */
    public long getPlayersPoints(String nickname) {
        Optional<Player> p = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname(nickname));
        if (p.isPresent()) {
            return p.get().getPoints();
        }
        return 0;
    }

    /**
     * Returns the {@code Player}'s current rank on the leaderboard.
     *
     * @param nickname the {@code String} containing the nickname of the {@code Player}
     * @return the {@code long} value of the current rank of the {@code Player}
     */
    private long getPlayersCurrentRank(String nickname) {
        List<Player> ranking = getRankings().stream().sorted(Comparator.comparingLong(Player::getPoints).reversed()).collect(Collectors.toList());
        long i = 1;
        for (Player p : ranking) {
            if (p.getNickname().equals(nickname)) {
                return i;
            }
            i++;
        }
        return ranking.size() + 1;
    }

    /**
     * Updates a {@code Player} in the database.
     * <p>
     * If the {@code Player} is new, then it will be inserted in the rankings table, otherwise their points will be updated if they were the winner of a particular match.
     *
     * @param nickname the {@code String} containing the nickname of the {@code Player}
     * @param winner   {@code boolean} that is {@code true} if the {@code Player} has won a recent match, {@code false} otherwise
     */
    public void updatePlayer(String nickname, boolean winner) {
        long addedPoints;
        if (winner) {
            addedPoints = 1;
        } else addedPoints = 0;
        if (!playerExists(nickname)) {
            insertPlayer(Player.builder()
                    .nickname(nickname)
                    .points(addedPoints)
                    .bestRank(getRankings().size() + 1)
                    .build());
        } else if (winner) {
            jdbi.useExtension(PlayerDao.class, dao -> dao.updatePlayersPoints(nickname, addedPoints));
        }
    }

    /**
     * Updates the best rank column in the rankings table.
     * <p>
     * If a {@code Player}'s rank is higher than their best rank, then their current rank will become their best rank ever.
     */
    public void updateRankings() {
        List<Player> players = getRankings();
        for (Player p : players) {
            long currentRank = getPlayersCurrentRank(p.getNickname());
            if (currentRank < p.getBestRank()) {
                jdbi.useExtension(PlayerDao.class, dao -> dao.updatePlayersRank(p.getNickname(), currentRank));
            }
        }
    }

}
