package ranks;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Player.class)
public interface PlayerDao {

    @SqlUpdate("""
        CREATE TABLE rankings (
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
    List<Player> listRankings();
}
