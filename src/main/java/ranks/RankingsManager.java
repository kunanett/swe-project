package ranks;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RankingsManager {
    private static  Jdbi jdbi;

    private static final RankingsManager instance = new RankingsManager();

    private RankingsManager(){
        jdbi = Jdbi.create("jdbc:h2:file:~/testdb").setSqlLogger(new Slf4JSqlLogger()).installPlugin(new SqlObjectPlugin());
    }

    public static RankingsManager getInstance(){
        return instance;
    }

    public void createTable(){
        jdbi.useExtension(PlayerDao.class, PlayerDao::createTable);
    }

    public void insertPlayer(Player player){
        jdbi.useExtension(PlayerDao.class, dao -> dao.insertPlayer(player));
    }

    public List<Player> getRankings() {
        return jdbi.withExtension(PlayerDao.class, PlayerDao::listRankings);
    }


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
