package server.api;

import commons.GameUpdatesPacket;
import commons.MultiPlayerGame;
import org.junit.jupiter.api.Test;
import server.Config;
import server.multiplayer.WaitingRoom;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigTest {

    private Config config = new Config();

    @Test
    void gamePacketTest() {
        GameUpdatesPacket gameUpdatesPacket = new GameUpdatesPacket( 0 ,"WAITINGROOM", -1);
        assertEquals(gameUpdatesPacket, config.getGameUpdatesPacket());
    }

    @Test
    void getRandomTest() {
        Random random = config.getRandom();

        assertTrue(random instanceof Random);
    }

    @Test
    void getWaitingRoomTest() {
        WaitingRoom waitingRoom = new WaitingRoom(new ArrayList<>(), new ArrayList<>(), config.numberOfQuestions);
        assertEquals(waitingRoom, config.getWaitingRoom());
    }

    @Test
    void multiPlayerGameTest() {
        MultiPlayerGame multiPlayerGame = new MultiPlayerGame(1, new ArrayList<>(),new ArrayList<>());
        assertEquals(multiPlayerGame, config.getMultiplayerGame());
    }
}
