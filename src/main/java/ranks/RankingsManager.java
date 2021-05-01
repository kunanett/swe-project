package ranks;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that manages database operations.
 * Other classes can access the database only through this class.
 */
public class RankingsManager {
    private static  Jdbi jdbi;

    private static final RankingsManager instance = new RankingsManager();

    private RankingsManager(){
        jdbi = Jdbi.create("jdbc:h2:file:~/testdb").setSqlLogger(new Slf4JSqlLogger()).installPlugin(new SqlObjectPlugin());
    }

    /**
     * Returns the instance of this class.
     *
     * @return the only instance of {@code RankingsManager} class
     */
    public static RankingsManager getInstance(){
        return instance;
    }

    /**
     * Creates the rankings table in the database.
     * The table will not be created if it already exists.
     */
    public void createTable(){
        jdbi.useExtension(PlayerDao.class, PlayerDao::createTable);
    }

    /**
     * Inserts a player in the database rankings table.
     *
     * @param player a {@code Player} object to be inserted in the database
     */
    public void insertPlayer(Player player){
        jdbi.useExtension(PlayerDao.class, dao -> dao.insertPlayer(player));
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
     * @return
     */
    public boolean playerExists(String nickname) {
        Optional<Player> player = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname(nickname));
        return player.isPresent();
    }

    public long getPlayersPoints(String nickname){
        Optional<Player> p = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname(nickname));
        if (p.isPresent()){
            return p.get().getPoints();
        }
        return 0;
    }

    public long getPlayersBestRank(String nickname){
        Optional<Player> p = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname(nickname));
        if (p.isPresent()){
            return p.get().getBestRank();
        }
        return 0;
    }

    public long getPlayersCurrentRank(String nickname){
        List<Player> ranking = getRankings().stream().sorted(Comparator.comparingLong(Player::getPoints).reversed()).collect(Collectors.toList());
        long i = 1;
        for (Player p : ranking){
            if (p.getNickname().equals(nickname)){
                return i;
            }
            i++;
        }
        return ranking.size() + 1;
    }

    public void updatePlayer(String nickname, boolean winner){
        long addedPoints;
        if (winner){
            addedPoints = 1;
        }else addedPoints = 0;
        if (!playerExists(nickname)){
            insertPlayer(Player.builder()
                    .nickname(nickname)
                    .points(addedPoints)
                    .bestRank(getRankings().size() + 1)
                    .build());
        }else{
             jdbi.useExtension(PlayerDao.class, dao -> dao.updatePlayersPoints(nickname, addedPoints));
        }
    }

    public void updateRankings(){
        List<Player> players = getRankings();
        for (Player p : players){
            long currentRank = getPlayersCurrentRank(p.getNickname());
            if ( currentRank < p.getBestRank()){
                jdbi.useExtension(PlayerDao.class, dao -> dao.updatePlayersRank(p.getNickname(), currentRank));
            }
        }
    }

}
