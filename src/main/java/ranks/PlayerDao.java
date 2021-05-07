package ranks;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

/**
 * Interface for providing Data Access Object of {@code Player} class.
 *
 * The interface provides SQL queries and updates for managing the game's database. Implementing it will be done by {@code Jdbi} class.
 */
@RegisterBeanMapper(Player.class)
public interface PlayerDao {

    /**
     * Maps to an {@code Sql Update} that creates the rankings database table if it does not already exist.
     */
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
            drop table rankings
""")
    void deleteTable();

    /**
     * Maps to an {@code Sql Query} that inserts a {@code Player} object into the database.
     *
     * @param player the {@code Player} object to be inserted
     */
    @SqlUpdate("""
            insert into rankings (nickname, points, best_rank) 
            values (:nickname, :points, :bestRank)
            """)
    void insertPlayer(@BindBean Player player);

    /**
     * Maps to an {@code Sql Query} that returns the rankings of the game.
     *
     * @return a {@code List} of all {@code Player} objects in the database
     */
    @SqlQuery("""
            select * from rankings
            order by points desc
            """)
    @RegisterBeanMapper(Player.class)
    List<Player> listRankings();

    /**
     * Maps to an {@code Sql Query} that finds a {@code Player} object by its nickname in the database.
     *
     * @param nickname the {@code String} the {@code Player} object is searched by
     * @return an {@code Optional} object that may or may not wrap a {@code Player} object, or it could wrap a {@code null}
     */
    @SqlQuery("""
            select * from rankings
            where nickname = :nickname
""")
    Optional<Player> findPlayerByNickname(@Bind("nickname") String nickname);

    /**
     * Maps to an {@code Sql Update} that updates a {@code Player} objects record in the database, giving it a new best rank.
     *
     * @param nickname the name of the player that is to be updated
     * @param newBestRank the {@code long} value of the new best rank
     */
    @SqlUpdate("""
               update rankings
               set best_rank = :newBestRank
               where nickname = :nickname;
""")
    void updatePlayersRank(@Bind("nickname") String nickname, @Bind("newBestRank") long newBestRank);

    /**
     * Maps to an {@code Sql Update} that updates a {@code Player} objects record in the database, modifying their points.
     *
     * @param nickname the name of the player that is to be updated
     * @param pointsAdded the {@code long} value of points added to sum of points already in the database
     */
    @SqlUpdate("""
               update rankings
               set points = points + :pointsAdded
               where nickname = :nickname;
""")
    void updatePlayersPoints(@Bind("nickname") String nickname, @Bind("pointsAdded") long pointsAdded);
}
