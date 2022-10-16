package commons;

import commons.questions.ComparativeQuestion;
import commons.questions.EstimationQuestion;
import commons.questions.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class MultiPlayerGameTest {

    MultiPlayerGame multiPlayerGame;
    Activity a1;
    Activity a2;

    /**
     * Creates a new multiplayer game with an empty list of questions
     * Run before each test
     */
    @BeforeEach
    public void setup(){
        List<Question> questions = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        multiPlayerGame = new MultiPlayerGame(1, players, questions);
        a1 = new Activity("1L", "image_a", "a", 1L, "a");
        a2 = new Activity("2L", "image_b", "b", 2L, "b");
    }

    /**
     * Tests an empty constructor
     */
    @Test
    public void emptyConstructorTest() {
        MultiPlayerGame m = new MultiPlayerGame();

        assertNotNull(m);
    }

    /**
     * Tests a constructor with player list
     * and a question list
     */
    @Test
    public void playersQuestionsConstructor() {
        assertNotNull(multiPlayerGame);

        MultiPlayerGame m = new MultiPlayerGame(14, new ArrayList<>(), new ArrayList<>());

        assertNotNull(m);

        Player a = new Player(1L,"a",5);
        Player b = new Player(2L,"b",5);
        Player c = new Player(3L,"c",5);
        ArrayList<Player> players = new ArrayList<>();
        players.add(a);
        players.add(b);
        players.add(c);

        ComparativeQuestion q1 = new ComparativeQuestion(List.of(a1, a2), true);
        EstimationQuestion q2 = new EstimationQuestion(a2);
        ComparativeQuestion q3 = new ComparativeQuestion(null, false);
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);

        MultiPlayerGame m2 = new MultiPlayerGame(50, players, questions);
        assertNotNull(m2);

        MultiPlayerGame m3 = new MultiPlayerGame(5, null, null);
        assertNotNull(m3);
    }

    /**
     * Tests if a question is added successfully
     */
    @Test
    public void testAddQuestion() {
        assertTrue(multiPlayerGame.addQuestion(new ComparativeQuestion(null, false)));

        assertEquals(1, multiPlayerGame.getQuestions().size());

        Question q = new ComparativeQuestion(null, true);
        assertTrue(multiPlayerGame.addQuestion(q));

        assertEquals(2, multiPlayerGame.getQuestions().size());
    }

    /**
     * Tests if many questions are added successfully
     * in a row
     */
    @Test
    public void addManyQuestions() {
        multiPlayerGame.addQuestion(new ComparativeQuestion(null, true));
        multiPlayerGame.addQuestion(new EstimationQuestion(a1));
        multiPlayerGame.addQuestion(new ComparativeQuestion(null, false));
        multiPlayerGame.addQuestion(new EstimationQuestion(a2));

        assertEquals(4, multiPlayerGame.getQuestions().size());

        Question q1 = new ComparativeQuestion(List.of(a1, a2), false);
        assertTrue(multiPlayerGame.addQuestion(q1));

        assertEquals(5, multiPlayerGame.getQuestions().size());

        assertFalse(multiPlayerGame.addQuestion(q1));

        assertEquals(5, multiPlayerGame.getQuestions().size());

        assertFalse(multiPlayerGame.addQuestion(null));

        assertEquals(5, multiPlayerGame.getQuestions().size());
    }

    /**
     * Tests if a player is successfully removed from
     * the player list
     */
    @Test
    public void removePlayerTest() {
        Player a = new Player(1L,"a",5);
        Player b = new Player(2L,"b",5);
        Player c = new Player(3L,"c",5);
        ArrayList<Player> players = new ArrayList<>();
        players.add(a);
        players.add(b);
        players.add(c);
        multiPlayerGame.setPlayers(players);
        assertEquals(3, multiPlayerGame.getPlayers().size());

        assertTrue(multiPlayerGame.removePlayer(b));

        assertEquals(2, multiPlayerGame.getPlayers().size());

        players.add(b);
        multiPlayerGame.setPlayers(players);
        assertEquals(3, multiPlayerGame.getPlayers().size());

        assertTrue(multiPlayerGame.removePlayer(b));
        assertTrue(multiPlayerGame.removePlayer(c));

        assertEquals(1, multiPlayerGame.getPlayers().size());

        assertFalse(multiPlayerGame.removePlayer(b));   // remove already removed player
        assertFalse(multiPlayerGame.removePlayer(c));   // remove already removed player

        Player d = new Player(4L,"d",4350);
        assertFalse(multiPlayerGame.removePlayer(d));   // remove player that was never in the list

        assertEquals(1, multiPlayerGame.getPlayers().size());

        assertTrue(multiPlayerGame.removePlayer(a));

        assertEquals(0, multiPlayerGame.getPlayers().size());
    }

    /**
     * Tests if the points to be added calculation is correct
     */
    @Test
    public void TestGetPointsToBeAdded() {
        Player a = new Player("a");
        Player b = new Player("b");
        multiPlayerGame.setPlayers(List.of(a, b));
        a.setStreak(5);

        assertEquals(1050, multiPlayerGame.getPointsToBeAdded(10, a));

        b.setStreak(1);

        assertEquals(1010, multiPlayerGame.getPointsToBeAdded(10, b));
        assertEquals(1015, multiPlayerGame.getPointsToBeAdded(9, b));

        a.setStreak(3);

        assertEquals(1056, multiPlayerGame.getPointsToBeAdded(5, a));
    }

    /**
     * Tests if the points to be added for a regular question
     * for one player is correct
     */
    @Test
    public void TestAddPointsRegularQuestionOnePlayer() {
        Player a = new Player("a");
        multiPlayerGame.setPlayers(List.of(a));
        a.setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        multiPlayerGame.addPointsForPlayer(5, 1.0, a);

        assertEquals(1066, a.getScore());
    }

    /**
     * Tests if the points to be added for a wrong answer for
     * one player is correct
     */
    @Test
    public void TestAddPointsWrongAnswerOnePlayer() {
        Player a = new Player("a");
        multiPlayerGame.setPlayers(List.of(a));
        int scoreBefore = a.getScore();
        multiPlayerGame.addPointsForPlayer(-1, 1.0, a);

        assertEquals(scoreBefore, a.getScore());
    }

    /**
     * Tests if the points to be added for an estimation question
     * for one player is correct
     */
    @Test
    public void TestAddPointsEstimationQuestionOnePlayer() {
        Player a = new Player("a");
        multiPlayerGame.setPlayers(List.of(a));
        multiPlayerGame.getPlayers().get(0).setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        multiPlayerGame.addPointsForPlayer(7, 0.49, a);

        assertEquals(517, a.getScore());
    }

    /**
     * Tests if the points to be added for a regular question
     * for many players is correct
     */
    @Test
    public void TestAddPointsRegularQuestionManyPlayers() {
        Player a = new Player("a");
        Player b = new Player("b");
        Player c = new Player("c");
        multiPlayerGame.setPlayers(List.of(a, b, c));
        a.setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        b.setStreak(3); // same thing
        c.setStreak(3); // same thing
        multiPlayerGame.addPointsForEveryone(List.of(5, 5, 5), List.of(1.0, 1.0, 1.0));

        assertEquals(1066, multiPlayerGame.getPlayers().get(0).getScore());
        assertEquals(1066, multiPlayerGame.getPlayers().get(1).getScore());
        assertEquals(1066, multiPlayerGame.getPlayers().get(2).getScore());
        // of course we can use "a", "b", "c" instead of
        // multiPlayerGame.getPlayers().get(x) but I think that writing it like this
        // feels more natural/obvious for this test
    }

    /**
     * Tests if the points to be added for all wrong answer for
     * many players is correct
     */
    @Test
    public void TestAddPointsAllWrongAnswerManyPlayers() {
        Player a = new Player("a");
        Player b = new Player("b");
        Player c = new Player("c");
        multiPlayerGame.setPlayers(List.of(a, b, c));
        int scoreBefore1 = a.getScore();
        int scoreBefore2 = b.getScore();
        int scoreBefore3 = c.getScore();
        multiPlayerGame.addPointsForEveryone(List.of(-1, -1, -1), List.of(1.0, 1.0, 1.0));

        assertEquals(scoreBefore1, multiPlayerGame.getPlayers().get(0).getScore());
        assertEquals(scoreBefore2, multiPlayerGame.getPlayers().get(1).getScore());
        assertEquals(scoreBefore3, multiPlayerGame.getPlayers().get(2).getScore());
    }

    /**
     * Tests if the points to be added for all wrong answer for
     * many players is correct
     */
    @Test
    public void TestAddPointsMixedAnswersManyPlayers() {
        Player a = new Player("a", 250);
        Player b = new Player("b", 500);
        Player c = new Player("c", 400);
        multiPlayerGame.setPlayers(List.of(a, b, c));
        int scoreBefore1 = a.getScore();
        b.setStreak(3);
        int scoreBefore3 = c.getScore();
        multiPlayerGame.addPointsForEveryone(List.of(-1, 5, -1), List.of(1.0, 1.0, 1.0));

        assertEquals(scoreBefore1, multiPlayerGame.getPlayers().get(0).getScore());
        assertEquals(1566, multiPlayerGame.getPlayers().get(1).getScore());
        assertEquals(scoreBefore3, multiPlayerGame.getPlayers().get(2).getScore());
    }

    /**
     * Tests if the points to be added for an estimation question
     * for one player is correct
     */
    @Test
    public void TestAddPointsEstimationQuestionManyPlayers() {
        Player a = new Player("a", 200);
        Player b = new Player("b", 100);
        Player c = new Player("c");
        Player d = new Player("d", 100);
        multiPlayerGame.setPlayers(List.of(a, b, c, d));
        a.setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        b.setStreak(3); // same thing
        c.setStreak(3); // same thing
        multiPlayerGame.addPointsForEveryone(List.of(7, 7, 7, -1), List.of(0.49, 0.49, 0.49, 0.0));

        assertEquals(717, multiPlayerGame.getPlayers().get(0).getScore());
        assertEquals(617, multiPlayerGame.getPlayers().get(1).getScore());
        assertEquals(517, multiPlayerGame.getPlayers().get(2).getScore());
        assertEquals(100, multiPlayerGame.getPlayers().get(3).getScore());
    }

    /**
     * Tests the nextQuestion method
     */
    @Test
    public void TestNextQuestion() {
        assertEquals(-1, multiPlayerGame.getQuestionNumber());

        for(int i = 0; i < 5; i++){
            multiPlayerGame.nextQuestion();
        }

        assertEquals(4, multiPlayerGame.getQuestionNumber());
    }

    /**
     * Test the player list hasher method
     */
    @Test
    public void TestPlayerListHasher() {
        assertEquals(Objects.hash(multiPlayerGame.getPlayers()), multiPlayerGame.playerListHash());

        Player a = new Player("a", 200);
        Player b = new Player("b", 100);
        Player c = new Player("c");
        Player d = new Player("d", 100);
        List<Player> players = new ArrayList<>();
        players.add(a);
        players.add(b);
        players.add(c);
        players.add(d);
        multiPlayerGame.setPlayers(players);

        assertEquals(Objects.hash(multiPlayerGame.getPlayers()), multiPlayerGame.playerListHash());

        multiPlayerGame.removePlayer(a);

        assertEquals(Objects.hash(multiPlayerGame.getPlayers()), multiPlayerGame.playerListHash());

        multiPlayerGame.removePlayer(b);
        multiPlayerGame.removePlayer(c);

        assertEquals(Objects.hash(multiPlayerGame.getPlayers()), multiPlayerGame.playerListHash());
    }

    /**
     * Test the getter for the game ID
     */
    @Test
    public void TestGameIDGetter() {
        assertEquals(1, multiPlayerGame.getGameID());
    }

    /**
     * Test the getter for the player list
     */
    @Test
    public void TestPlayerListGetter() {
        assertEquals(new ArrayList<>(), multiPlayerGame.getPlayers());
    }

    /**
     * Test the getter for the question list
     */
    @Test
    public void TestQuestionListGetter() {
        assertEquals(new ArrayList<>(), multiPlayerGame.getQuestions());
    }

    /**
     * Test the getter for the question number
     */
    @Test
    public void TestQuestionNumberGetter() {
        assertEquals(-1, multiPlayerGame.getQuestionNumber());
        multiPlayerGame.nextQuestion();
        assertEquals(0, multiPlayerGame.getQuestionNumber());
    }

    /**
     * Test the getter for the current screen
     */
    @Test
    public void TestCurrentScreenGetter() {
        assertEquals("LOADING SCREEN", multiPlayerGame.getCurrentScreen());
    }

    /**
     * Test the setter for the game ID
     */
    @Test
    public void TestGameIDSetter() {
        assertEquals(1, multiPlayerGame.getGameID());

        multiPlayerGame.setGameID(10);
        assertEquals(10, multiPlayerGame.getGameID());

        multiPlayerGame.setGameID(5000);
        assertEquals(5000, multiPlayerGame.getGameID());

        multiPlayerGame.setGameID(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, multiPlayerGame.getGameID());

        multiPlayerGame.setGameID(5000);
        multiPlayerGame.setGameID(132);
        multiPlayerGame.setGameID(7);
        assertEquals(7, multiPlayerGame.getGameID());

        multiPlayerGame.setGameID(-1);
        assertEquals(0, multiPlayerGame.getGameID());

        multiPlayerGame.setGameID(0);
        assertEquals(0, multiPlayerGame.getGameID());

        multiPlayerGame.setGameID(Integer.MIN_VALUE);
        assertEquals(0, multiPlayerGame.getGameID());
    }

    /**
     * Test the setter for the player list
     */
    @Test
    public void TestPlayerListSetter() {
        Player a = new Player("a", 200);
        Player b = new Player("b", 100);
        Player c = new Player("c");
        Player d = new Player("d", 100);
        List<Player> players = List.of(a, b, c, d);
        multiPlayerGame.setPlayers(players);

        assertEquals(players, multiPlayerGame.getPlayers());
    }

    /**
     * Test the setter for the question list
     */
    @Test
    public void TestQuestionListSetter() {
        List<Question> questions = List.of(new EstimationQuestion(a1), new EstimationQuestion(a2), new ComparativeQuestion(List.of(a1, a2), true));
        multiPlayerGame.setQuestions(questions);

        assertEquals(questions, multiPlayerGame.getQuestions());
    }

    /**
     * Test the setter for the current screen
     */
    @Test
    public void TestCurrentScreenSetter() {
        multiPlayerGame.setCurrentScreen("QUESTION");
        assertEquals("QUESTION", multiPlayerGame.getCurrentScreen());

        multiPlayerGame.setCurrentScreen("LEADERBOARD");
        assertEquals("LEADERBOARD", multiPlayerGame.getCurrentScreen());

        multiPlayerGame.setCurrentScreen("ENDSCREEN");
        assertEquals("ENDSCREEN", multiPlayerGame.getCurrentScreen());

        assertThrows(IllegalArgumentException.class, () -> multiPlayerGame.setCurrentScreen("LITERALLY ANYTHING OTHER THAN THOSE"), "Not a valid screen");
    }

    /**
     * Test the getter of a GameUpdatesPacket
     */
    @Test
    public void getGameStatusTest() {
        Player a = new Player("a");
        Player b = new Player("b");
        List<Player> players = new ArrayList<>();
        players.add(a);
        players.add(b);
        multiPlayerGame.setPlayers(players);
        multiPlayerGame.setCurrentScreen("QUESTION");

        GameUpdatesPacket updates = new GameUpdatesPacket(Objects.hash(players), "QUESTION", -1);
        assertEquals(multiPlayerGame.getGameStatus(), updates);

        multiPlayerGame.removePlayer(a);
        assertNotEquals(multiPlayerGame.getGameStatus(), updates);
    }

}
