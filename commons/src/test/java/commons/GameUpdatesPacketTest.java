package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameUpdatesPacketTest {

    @Test
    void emptyConstructorTest() {
        GameUpdatesPacket g = new GameUpdatesPacket();
        assertNotNull(g);
    }

    @Test
    void normalConstructorTest() {
        GameUpdatesPacket g = new GameUpdatesPacket(1, "LOADING SCREEN", 3);
        assertNotNull(g);
    }
}