package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    /**
     * Checks if empty constructor creates a new object
     */
    @Test
    public void emptyConstructorTest() {
        Player player = new Player();
        assertNotNull(player);
    }

    /**
     * Checks if constructor with name creates a new object
     */
    @Test
    public void nameConstructorTest() {
        Player player = new Player("p1");
        assertNotNull(player);
    }

    /**
     * Checks if constructor with name and score creates a new object
     */
    @Test
    public void nameScoreConstructorTest() {
        Player player = new Player("p1", 1200);
        assertNotNull(player);
    }

    /**
     * Checks if constructor with id, name creates a new object
     */
    @Test
    public void idNameScoreConstructorTest() {
        Player player = new Player(1L, "p1", 1200);
        assertNotNull(player);
    }

    /**
     * Checks if object is equal to itself
     */
    @Test
    public void equalsTest1() {
        Player player = new Player("p1", 1200);
        assertEquals(player, player);
    }

    /**
     * Checks if object is equal to identical object
     */
    @Test
    public void equalsTest2() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player("p1", 1200);
        assertEquals(player1, player2);
    }

    /**
     * Checks if object is not equal to different name
     */
    @Test
    public void equalsTest3() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player("p2", 1200);
        assertNotEquals(player1, player2);
    }

    /**
     * Checks if object is not equal to different  score
     */

    /**
     * Checks if object is not equal to different name and score
     */
    @Test
    public void equalsTest5() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player("p2", 2000);
        assertNotEquals(player1, player2);
    }

    /**
     * Checks if object is not equal to different id name and score
     */
    @Test
    public void equalsTest6() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player(1L,"p1", 2000);
        assertNotEquals(player1, player2);
    }

    /**
     * Checks if object is not equal to different id name and score
     */
    @Test
    public void equalsTest7() {
        Player player1 = new Player(1L, "p1", 1200);
        Player player2 = new Player(2L, "p1", 2000);
        assertNotEquals(player1, player2);
    }

    /**
     * Checks if object is equal to same id name and score
     */
    @Test
    public void equalsTest8() {
        Player player1 = new Player(1L, "p1", 1200);
        Player player2 = new Player(1L,"p1", 1200);
        assertEquals(player1, player2);
    }

    /**
     * Checks if object is not equal to itself
     */
    @Test
    public void equalsTest9() {
        Player player = new Player(1L,"p1", 1200);
        assertEquals(player, player);
    }

    /**
     * Checks if object is equal to itself with only name
     */
    @Test
    public void equalsTest10() {
        Player player = new Player("p1");
        assertEquals(player, player);
    }

    /**
     * Checks if object is equal to itself with only name
     */
    @Test
    public void equalsTest11() {
        Player player = new Player();
        assertEquals(player, player);
    }


    /**
     * Checks if object is not equal to different object
     */
    @Test
    public void equalsTest13() {
        Player player1 = new Player("p1", 0);
        Player player2 = new Player("p1");
        assertEquals(player1, player2);
    }

    /**
     * test for getId
     */
    @Test
    public void getIdTest() {
        Player player = new Player(1L, "p1", 1200);
        assertEquals(1L, player.getId());
    }

    /**
     * test for setId
     */
    @Test
    public void setIdTest1() {
        Player player = new Player(1L, "p1", 1200);
        player.setId(4L);
        assertEquals(4L, player.getId());
    }

    /**
     * test for setId
     */
    @Test
    public void setIdTest2() {
        Player player = new Player("p1", 1200);
        player.setId(4L);
        assertEquals(4L, player.getId());
    }

    /**
     * test for getName
     */
    @Test
    public void getNameTest() {
        Player player = new Player("p1");
        assertEquals("p1", player.getName());
    }

    /**
     * test for setName
     */
    @Test
    public void setNameTest1() {
        Player player = new Player("p1");
        player.setName("player1");
        assertEquals("player1", player.getName());
    }

    /**
     * test for setName
     */
    @Test
    public void setNameTest2() {
        Player player = new Player("p1");
        assertThrows(IllegalArgumentException.class, () -> {
            player.setName("");
        }, "Name cannot be empty");
    }

    /**
     * test for getScore
     */
    @Test
    public void getScoreTest1() {
        Player player = new Player(1L, "p1", 1200);
        assertEquals(1200, player.getScore());
    }

    /**
     * test for getScore
     */
    @Test
    public void getScoreTest2() {
        Player player = new Player("p1");
        assertEquals(0, player.getScore());
    }

    /**
     * test for setScore
     */
    @Test
    public void setScoreTest1() {
        Player player = new Player(1L, "p1", 1200);
        player.setScore(4000);
        assertEquals(4000, player.getScore());
    }

    /**
     * test for setScore
     */
    @Test
    public void setScoreTest2() {
        Player player = new Player("p1");
        player.setScore(4000);
        assertEquals(4000, player.getScore());
    }

    /**
     * test for setScore
     */
    @Test
    public void setScoreTest3() {
        Player player = new Player("p1");
        assertThrows(IllegalArgumentException.class, () -> {
            player.setScore(-200);
        }, "Score cannot be negative");
    }

    /**
     * test for setScore
     */
    @Test
    public void setScoreTest4() {
        Player player = new Player("p1", 1200);
        assertThrows(IllegalArgumentException.class, () -> {
            player.setScore(-200);
        }, "Score cannot be negative");
    }

    /**
     * test for toString
     */
    @Test
    public void toStringTest1() {
        Player player = new Player(1L, "p1", 1200);
        assertEquals("Player(id=1, name=p1, score=1200, streak=0)", player.toString());
    }

    /**
     * test for toString
     */
    @Test
    public void toStringTest2() {
        Player player = new Player("p1", 1200);
        assertEquals("Player(id=null, name=p1, score=1200, streak=0)", player.toString());
    }

    /**
     * test for toString
     */
    @Test
    public void toStringTest3() {
        Player player = new Player("p1");
        assertEquals("Player(id=null, name=p1, score=0, streak=0)", player.toString());
    }

    /**
     * test for toString
     */
    @Test
    public void toStringTest4() {
        Player player = new Player();
        assertEquals("Player(id=null, name=null, score=null, streak=0)", player.toString());
    }

    @Test
    void compareToTest() {
        Player a = new Player(1L,"a",5);
        Player b = new Player(2L, "b", 6);
        Player c = new Player(3L, "c", 6);
        assertEquals(1,a.compareTo(b));
        assertEquals(-1,b.compareTo(a));
        assertEquals(0,b.compareTo(c));
        assertEquals(0,c.compareTo(b));
    }

    /**
     * Test for setStreak
     */
    @Test
    public void TestStreakSetter() {
        Player a = new Player(1L,"a",5);
        a.setStreak(3);
        assertEquals(3, a.getStreak());
    }

    /**
     * Test for getStreak
     */
    @Test
    public void TestStreakGetter() {
        Player a = new Player(1L,"a",5);
        assertEquals(0, a.getStreak());
        a.setStreak(3);
        assertEquals(3, a.getStreak());
    }

    /**
     * Tests the resetStreak method
     */
    @Test
    public void TestResetSteak() {
        Player a = new Player(1L,"a",5);
        a.setStreak(5);
        a.resetStreak();
        assertEquals(0, a.getStreak());
    }

    /**
     * Tests the incrementStreak method
     */
    @Test
    public void TestStreakIncrement() {
        Player a = new Player(1L,"a",5);
        for(int i = 0; i < 6; i++){
            a.incrementStreak();
        }
        assertEquals(6, a.getStreak());
    }

    /**
     * comparing to a non player class
     */
    @Test
    public void testEquals() {
        Player p = new Player("a");
        Object p1 = new Object();
        assertFalse(p.equals(p1));
    }

    /**
     * negative streak
     */
    @Test()
    public void testNegativeStreak() throws Exception{
        Player p = new Player("a");
        assertThrows(IllegalArgumentException.class, () -> {
            p.setStreak(-6);
        }, "Streak cannot be negative");
    }

    /**
     * negative score constructor
     */
    @Test()
    public void testNegativeScoreConstructor() {
        Player p = new Player("a", -10);
        assertEquals("a", p.getName());
        assertEquals(0, p.getScore());
    }

    /**
     * negative score constructor
     */
    @Test()
    public void testNegativeScoreConstructor1() {
        Player p = new Player(20L,"a", -10);
        assertEquals(20L, p.getId());
        assertEquals("a", p.getName());
        assertEquals(0, p.getScore());
    }
}
