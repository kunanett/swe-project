package ranks;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Player.class)
public interface PlayerDao {

    @SqlUpdate("""
            CREATE TABLE IF NOT EXISTS rankings (
                            nickname VARCHAR NOT NULL PRIMARY KEY ,
                            points INTEGER NOT NULL,
                            best_rank INTEGER NOT NULL 
                        )
            """
    )
    void createTable();

    @SqlUpdate("""
            insert into rankings (nickname, points, best_rank) 
            values (:nickname, :points, :points)
            """)
    void insertPlayer(@BindBean Player player);

    @SqlQuery("""
            select * from rankings
            order by points desc
            """)
    @RegisterBeanMapper(Player.class)
    List<Player> listRankings();

    @SqlQuery("""
            select * from rankings
            where nickname = :nickname
""")
    Optional<Player> findPlayerByNickname(@Bind("nickname") String nickname);

    @SqlUpdate("""
               update rankings
               set best_rank = :newBestRank
               where nickname = :nickname;
""")
    void updatePlayersRank(@Bind("nickname") String nickname, @Bind("newBestRank") long newBestRank);

    @SqlUpdate("""
               update rankings
               set points = points + :pointsAdded
               where nickname = : nickname
""")
    void updatePlayersPoints(@Bind("nickname") String nickname, @Bind("pointsAdded") long pointsAdded);
}
