package ranks;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RankingsManagerTest {

    RankingsManager rankingsManager;

    Jdbi jdbi;

    @BeforeEach
    void init(){
        rankingsManager = RankingsManager.getTestInstance();
        jdbi = Jdbi.create("jdbc:h2:file:~/test").setSqlLogger(new Slf4JSqlLogger()).installPlugin(new SqlObjectPlugin());
        jdbi.useExtension(PlayerDao.class, PlayerDao::createTable);

    }

    @AfterEach
    void tearDown(){
        jdbi.useExtension(PlayerDao.class, PlayerDao::deleteTable);
    }

    void assertRankings(List<Player> expected, List<Player> actual){
        assertAll(
                () -> assertTrue(actual.containsAll(expected)),
                () -> assertTrue(expected.containsAll(actual))
        );
    }


    @Test
    void insertPlayer() {
        Player testAnett = Player.builder().nickname("Anett").points(56).bestRank(100).build();
        rankingsManager.insertPlayer(testAnett);
        Optional<Player> anotherAnett = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname("Anett"));
        assertEquals(testAnett, anotherAnett.get());

        Player otherAnett = Player.builder().nickname("Anett").points(12).bestRank(34).build();
        rankingsManager.insertPlayer(otherAnett);

        anotherAnett = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname("Anett"));
        assertEquals(testAnett, anotherAnett.get());
    }


    @Test
    void getRankings() {
        Player testAnett = Player.builder().nickname("Anett").points(20).bestRank(100).build();
        rankingsManager.insertPlayer(testAnett);
        Player testKretya = Player.builder().nickname("Kristóf").points(33).bestRank(100).build();
        rankingsManager.insertPlayer(testKretya);
        Player testZsuzsi = Player.builder().nickname("Zsuzsi").points(7).bestRank(100).build();
        rankingsManager.insertPlayer(testZsuzsi);
        List<Player> expectedRankings = new ArrayList<>();
        expectedRankings.add(testAnett);
        expectedRankings.add(testZsuzsi);
        expectedRankings.add(testKretya);

        List<Player> actualRankings = rankingsManager.getRankings();
        assertRankings(expectedRankings, actualRankings);
    }

    @Test
    void getPlayersPoints() {
        Player testAnett = Player.builder().nickname("Anett").points(56).bestRank(100).build();
        rankingsManager.insertPlayer(testAnett);

        assertEquals(56, rankingsManager.getPlayersPoints("Anett"));

        jdbi.useExtension(PlayerDao.class, dao -> {
           dao.updatePlayersPoints("Anett", 4);
        });

        assertEquals(60, rankingsManager.getPlayersPoints("Anett"));
        assertEquals(0, rankingsManager.getPlayersPoints("Zsuzsi"));
    }

    @Test
    void updatePlayer() {
        Player testAnett = Player.builder().nickname("Anett").points(56).bestRank(100).build();
        rankingsManager.insertPlayer(testAnett);
        rankingsManager.updatePlayer("Anett", true);
        assertEquals(57, rankingsManager.getPlayersPoints("Anett"));
        rankingsManager.updatePlayer("Anett", false);
        assertEquals(57, rankingsManager.getPlayersPoints("Anett"));

        rankingsManager.updatePlayer("Zsuzsi", true);
        Player zsuzsi = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname("Zsuzsi")).get();
        assertEquals(1, zsuzsi.getPoints());
        assertEquals(2, zsuzsi.getBestRank());

        rankingsManager.updatePlayer("Kristóf", false);
        Player kristof = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname("Kristóf")).get();
        assertEquals(0, kristof.getPoints());
        assertEquals(3, kristof.getBestRank());
    }

    @Test
    void updateRankings() {
        Player testAnett = Player.builder().nickname("Anett").points(20).bestRank(100).build();
        rankingsManager.insertPlayer(testAnett);
        Player testKretya = Player.builder().nickname("Kristóf").points(33).bestRank(100).build();
        rankingsManager.insertPlayer(testKretya);
        Player testZsuzsi = Player.builder().nickname("Zsuzsi").points(7).bestRank(100).build();
        rankingsManager.insertPlayer(testZsuzsi);

        rankingsManager.updateRankings();

        Player zsuzsi = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname("Zsuzsi")).get();
        Player kristof = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname("Kristóf")).get();
        Player anett = jdbi.withExtension(PlayerDao.class, dao -> dao.findPlayerByNickname("Anett")).get();

        assertAll(
                () -> assertEquals(1, kristof.getBestRank()),
                () -> assertEquals(2, anett.getBestRank()),
                () -> assertEquals(3, zsuzsi.getBestRank())
        );

    }
}