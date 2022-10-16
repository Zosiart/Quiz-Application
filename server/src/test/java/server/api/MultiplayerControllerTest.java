package server.api;

import commons.Activity;
import commons.GameUpdatesPacket;
import commons.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import server.SomeController;
import server.api.dependencies.TestActivityRepository;
import server.api.dependencies.TestRandom;
import server.multiplayer.WaitingRoom;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiplayerControllerTest {
    private MultiplayerController lpc;
    private ArrayList<Player> players;
    private TestActivityRepository tar;

    /**
     * Sets up environment before each test
     */
    @BeforeEach
    public void setup(){
        players = new ArrayList<>();
        players.addAll(List.of(
                new Player(1L,"a", 1),
                new Player(2L,"b", 2),
                new Player(3L,"c", 3)
        ));
        tar = new TestActivityRepository();
        tar.activities.addAll(List.of(
                new Activity("1", "image_a","a", 1L, "a"),
                new Activity("2", "image_b","b", 2L, "b"),
                new Activity("3", "image_c","c", 3L, "c"),
                new Activity("4", "image_d","d", 4L, "d"),
                new Activity("5", "image_e","e", 5L, "e"),
                new Activity("6", "image_f","f", 6L, "f"),
                new Activity("7", "image_g","g", 7L, "g")
        ));
        lpc = new MultiplayerController(new WaitingRoom(new ArrayList<>(), new ArrayList<>(), 0), new TestRandom(), tar);
        lpc.postPlayerToWaitingRoom(players.get(0));
        lpc.startGame();
    }

    /**
     * Test for getting list of all players
     */
    @Test
    void getPlayersTest() {
        assertEquals(List.of(players.get(0)), lpc.getPlayers(0).getBody());
    }

    /**
     * Test for updating score of player
     * Needs Mockito for proper testing
     */
    @Test
    void updateScoreTest() {
        Player newScore = new Player(1L,"a", 5);
        assertEquals(newScore, lpc.updateScore(0, newScore).getBody());
        assertEquals(ResponseEntity.badRequest().build(), lpc.updateScore(0, new Player(5L,"bob", 99)));
    }
    /**
     * Test for getting the update packet
     * Needs Mockito for proper testing
     */
    @Test
    void getUpdateTest() throws InterruptedException {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<GameUpdatesPacket>>(5000L,noContent);
        var res2 = lpc.getUpdate(0);
        Thread.sleep(5500);
        assertEquals(res.getClass(), res2.getClass());
    }

    /**
     * test for getting players to waiting room
     *
     */
    @Test
    void getWaitingRoomPlayersTest() {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        lpc.postPlayerToWaitingRoom(p1);
        lpc.postPlayerToWaitingRoom(p2);

        assertEquals(players, lpc.getWaitingRoomPlayers().getBody());
    }

    /**
     * test for checking validity of username
     */
    @Test
    void isValidUsernameTest() {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        lpc.postPlayerToWaitingRoom(p1);
        lpc.postPlayerToWaitingRoom(p2);

        assertFalse(lpc.isValidUsername("").getBody());
        assertFalse(lpc.isValidUsername(null).getBody());
        assertFalse(lpc.isValidUsername(p1.getName()).getBody());
        assertTrue(lpc.isValidUsername("p3").getBody());
    }

    /**
     * test for removing players from waiting room
     */
    @Test
    void removePlayerFromWaitingRoom() {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        lpc.postPlayerToWaitingRoom(p1);
        lpc.postPlayerToWaitingRoom(p2);
        assertEquals(2, lpc.getWaitingRoomPlayers().getBody().size());
        lpc.removePlayerFromWaitingRoom(p1);

        assertEquals(1, lpc.getWaitingRoomPlayers().getBody().size());
        assertTrue(lpc.removePlayerFromWaitingRoom(p2).getBody());
        assertFalse(lpc.removePlayerFromWaitingRoom(new Player("p3")).getBody());
    }

    /**
     * idk we can just delete it later
     */
    @Test
    void someControllerTest() {
        SomeController someController = new SomeController();
        assertEquals("Hello there General Kenobi!", someController.index());
    }


    /**
     * Test for getting list of all players in waiting room which is empty
     * Should return empty list
     */
    @Test
    void getWaitingRoomPlayersEmptyTest() {
        assertEquals(List.of(), lpc.getWaitingRoomPlayers().getBody());
    }

    /**
     * Test for getting list of all players in waiting room
     */
    @Test
    void removePlayerFromWaitingRoomTest() {
        lpc.postPlayerToWaitingRoom(players.get(0));
        assertEquals(List.of(players.get(0)), lpc.getWaitingRoomPlayers().getBody());
        assertEquals(true, lpc.removePlayerFromWaitingRoom(players.get(0)).getBody());
        assertEquals(List.of(), lpc.getWaitingRoomPlayers().getBody());
        assertEquals(false, lpc.removePlayerFromWaitingRoom(players.get(0)).getBody());
        assertEquals(List.of(), lpc.getWaitingRoomPlayers().getBody());
    }


    /**
     * Tests adding players to waiting room but player is empty
     */
    @Test
    void postPlayerToWaitingRoomNullTest() {
        assertNull(lpc.postPlayerToWaitingRoom(null).getBody());
        lpc.postPlayerToWaitingRoom(players.get(0));
        assertNull(lpc.postPlayerToWaitingRoom(players.get(0)).getBody());
    }
    

    /**
     * Tests getting game
     * should be null before game starts
     */
    @Test
    void getGamesTest() {
        assertNull(lpc.getGame(1).getBody());
    }

}
