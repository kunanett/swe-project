package ranks;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.List;

public class RankingsManager {
    private static final Jdbi jdbi = Jdbi.create("jdbc:h2:file:~/testdb").setSqlLogger(new Slf4JSqlLogger()).installPlugin(new SqlObjectPlugin());

    public static List<Player> getRankings(){
        return jdbi.withExtension(PlayerDao.class, PlayerDao::listRankings);
    }
}
