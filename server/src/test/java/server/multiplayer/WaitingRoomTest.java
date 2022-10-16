package server.multiplayer;


import commons.*;
import commons.questions.ComparativeQuestion;
import commons.questions.EstimationQuestion;
import commons.questions.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WaitingRoomTest {

    WaitingRoom waitingRoom;
    List<Question> questions;
    List<Player> players;
    Activity a1;
    Activity a2;

    /**
     * Code to be run before each test, create a list of questions and players
     */
    @BeforeEach
    public void setup() {
        questions = new ArrayList<>();
        players = new ArrayList<>();
        waitingRoom = new WaitingRoom(players, questions, 5);
        a1 = new Activity("1L", "image_a", "a", 1L, "a");
        a2 = new Activity("2L", "image_b", "b", 2L, "b");
    }

    /**
     * No arguments constructor test
     */
    @Test
    public void EmptyConstructorTest() {
        WaitingRoom w = new WaitingRoom();

        assertNotNull(w);
    }

    /**
     * Constructor with player list, question list
     * and max number of questions
     */
    @Test
    void ConstructorWith3ArgumentsTest() {
        WaitingRoom w = new WaitingRoom(questions, 5);

        assertNotNull(w);
    }

    /**
     * Constructor with question list and max number
     * of questions
     */
    @Test
    void ConstructorWith2ArgumentsTest() {
        WaitingRoom w = new WaitingRoom(questions, 5);

        assertNotNull(w);
    }

    /**
     * Test setup constructor
     */
    @Test
    void SetupConstructorTest() {
        assertNotNull(waitingRoom);
    }

    /**
     * Test constructor properly initializing max number of questions
     * to have a minimum of 5 and maximum of 25
     */
    @Test
    void MaxNumberOfQuestionsConstructorTest() {
        WaitingRoom w1 = new WaitingRoom(players, questions, 5);

        assertEquals(5, w1.getMaxNumberOfQuestions());

        WaitingRoom w2 = new WaitingRoom(players, questions, 10);

        assertEquals(10, w2.getMaxNumberOfQuestions());

        WaitingRoom w3 = new WaitingRoom(players, questions, 25);

        assertEquals(25, w3.getMaxNumberOfQuestions());
    }

    /**
     * Adding players which are not in the list and their names are not null
     */
    @Test
    public void SuccessfulAddPlayerTest(){
        Player p1 = new Player("a");

        assertTrue(waitingRoom.addPlayerToWaitingRoom(p1));

        Player p2 = new Player("b");

        assertTrue(waitingRoom.addPlayerToWaitingRoom(p2));

        assertEquals(2, waitingRoom.getNumberOfPlayers());
    }

    /**
     * Adding a player without name
     */
    @Test
    public void AddPlayerWithoutNameTest(){
        Player player = new Player();

        assertFalse(waitingRoom.addPlayerToWaitingRoom(player));
    }

    /**
     * Adding a player with a blank name
     */
    @Test
    public void AddPlayerWithEmptyNameTest(){
        Player player = new Player("");

        assertFalse(waitingRoom.addPlayerToWaitingRoom(player));
    }

    /**
     * Adding a null player
     */
    @Test
    public void AddNullPlayerTest(){
        assertFalse(waitingRoom.addPlayerToWaitingRoom(null));
    }

    /**
     * Adding a player who is already in the waiting room
     */
    @Test
    public void AddAnExistingPlayerTest(){
        Player player1 = new Player("a");
        waitingRoom.addPlayerToWaitingRoom(player1);

        assertFalse(waitingRoom.addPlayerToWaitingRoom(player1));

        assertEquals(1, waitingRoom.getNumberOfPlayers());

        Player player2 = new Player("a"); // this player is the exact same as player1, just diff variable

        assertFalse(waitingRoom.addPlayerToWaitingRoom(player2));

        assertEquals(1, waitingRoom.getNumberOfPlayers());
    }

    /**
     * Remove a player from waiting room
     */
    @Test
    public void RemoveExistingPlayerTest(){
        Player p1 = new Player("a");

        assertEquals(0, waitingRoom.getNumberOfPlayers());

        waitingRoom.addPlayerToWaitingRoom(p1);

        assertEquals(1, waitingRoom.getNumberOfPlayers());

        assertTrue(waitingRoom.removePlayerFromWaitingRoom(p1));

        assertEquals(0, waitingRoom.getNumberOfPlayers());

    }

    /**
     * remove player which is not in waiting room
     */
    @Test
    public void RemoveNonExistingPlayerTest(){
        Player p1 = new Player("a");

        assertFalse(waitingRoom.removePlayerFromWaitingRoom(p1));

        Player p2 = new Player("b");
        waitingRoom.addPlayerToWaitingRoom(p1);

        assertEquals(1, waitingRoom.getNumberOfPlayers());

        assertFalse(waitingRoom.removePlayerFromWaitingRoom(p2));

        assertTrue(waitingRoom.removePlayerFromWaitingRoom(p1));

        assertEquals(0, waitingRoom.getNumberOfPlayers());
    }

    /**
     * Tests if a question is added successfully
     */
    @Test
    public void TestAddQuestion() {
        assertTrue(waitingRoom.addQuestion(new ComparativeQuestion(null, false)));

        assertEquals(1, waitingRoom.getQuestions().size());

        Question q = new ComparativeQuestion(null, true);
        assertTrue(waitingRoom.addQuestion(q));
        assertEquals(2, waitingRoom.getQuestions().size());
    }

    /**
     * Tests if many questions are added successfully
     * in a row
     */
    @Test
    public void AddManyQuestions() {
        waitingRoom.addQuestion(new ComparativeQuestion(null, true));
        waitingRoom.addQuestion(new EstimationQuestion(a1));
        waitingRoom.addQuestion(new ComparativeQuestion(null, false));
        waitingRoom.addQuestion(new EstimationQuestion(a2));

        assertEquals(4, waitingRoom.getQuestions().size());

        Question q1 = new ComparativeQuestion(List.of(a1, a2), false);
        assertTrue(waitingRoom.addQuestion(q1));

        assertEquals(5, waitingRoom.getQuestions().size());

        assertFalse(waitingRoom.addQuestion(q1));

        assertEquals(5, waitingRoom.getQuestions().size());

        assertFalse(waitingRoom.addQuestion(null));

        assertEquals(5, waitingRoom.getQuestions().size());
    }

    /**
     * Test if the flush waiting room returns
     * a non-null multiplayer game
     */
    @Test
    void FlushWaitingRoomTest1(){
        MultiPlayerGame m = waitingRoom.flushWaitingRoom();

        assertNotNull(m);
    }

    /**
     * Test if the multiplayer game ID is calculated correctly
     */
    @Test
    void FlushWaitingRoomTest2(){
        MultiPlayerGame m = waitingRoom.flushWaitingRoom();

        assertEquals(0, m.getGameID());

        assertEquals(1, waitingRoom.getMultiplayerGameID());
    }

    /**
     * Test if flush waiting room method works correctly
     */
    @Test
    void FlushWaitingRoomTest3(){
        MultiPlayerGame m1 = waitingRoom.flushWaitingRoom();

        assertEquals(0, m1.getGameID());
        assertEquals(new ArrayList<>(), m1.getPlayers());
        assertEquals(new ArrayList<>(), m1.getQuestions());

        Player a = new Player("a", 200);
        Player b = new Player("b", 100);
        Player c = new Player("c");
        Player d = new Player("d", 100);
        players.addAll(List.of(a, b, c, d));

        waitingRoom.addPlayerToWaitingRoom(a);
        waitingRoom.addPlayerToWaitingRoom(b);
        waitingRoom.addPlayerToWaitingRoom(c);
        waitingRoom.addPlayerToWaitingRoom(d);

        ComparativeQuestion q1 = new ComparativeQuestion(List.of(a1, a2), true);
        EstimationQuestion q2 = new EstimationQuestion(a2);
        ComparativeQuestion q3 = new ComparativeQuestion(null, false);
        questions.addAll(List.of(q1, q2, q3));

        waitingRoom.addQuestion(q1);
        waitingRoom.addQuestion(q2);
        waitingRoom.addQuestion(q3);

        MultiPlayerGame m2 = waitingRoom.flushWaitingRoom();

        assertEquals(1, m2.getGameID());
        assertEquals(players, m2.getPlayers());
        assertEquals(questions, m2.getQuestions());

        waitingRoom.flushWaitingRoom();
        waitingRoom.flushWaitingRoom();
        MultiPlayerGame m3 = waitingRoom.flushWaitingRoom();

        assertEquals(4, m3.getGameID());
        assertEquals(new ArrayList<>(), m3.getPlayers());
        assertEquals(new ArrayList<>(), m3.getQuestions());

        assertEquals(5, waitingRoom.getMultiplayerGameID());
    }

    /**
     * Test the getter for the multiplayer game id
     */
    @Test
    public void TestMultiPlayerGameIDGetter() {
        assertEquals(0, waitingRoom.getMultiplayerGameID());

        waitingRoom.flushWaitingRoom();

        assertEquals(1, waitingRoom.getMultiplayerGameID());

        for(int i = 0; i < 10; i++)
            waitingRoom.flushWaitingRoom();

        assertEquals(11, waitingRoom.getMultiplayerGameID());
    }

    /**
     * Test the getter for the player list
     */
    @Test
    public void TestPlayerListGetter() {
        assertEquals(new ArrayList<>(), waitingRoom.getPlayers());

        Player a = new Player("a", 200);
        Player b = new Player("b", 100);
        Player c = new Player("c");
        Player d = new Player("d", 100);
        players.addAll(List.of(a, b, c, d));

        WaitingRoom w = new WaitingRoom(players, questions, 15);

        assertEquals(players, w.getPlayers());
    }

    /**
     * Test the getter for the question list
     */
    @Test
    public void TestQuestionListGetter() {
        assertEquals(new ArrayList<>(), waitingRoom.getQuestions());

        ComparativeQuestion q1 = new ComparativeQuestion(List.of(a1, a2), true);
        EstimationQuestion q2 = new EstimationQuestion(a2);
        ComparativeQuestion q3 = new ComparativeQuestion(null, false);
        questions.addAll(List.of(q1, q2, q3));

        WaitingRoom w = new WaitingRoom(players, questions, 15);

        assertEquals(questions, w.getQuestions());
    }

    /**
     * Test the getter for the max number of questions field
     */
    @Test
    public void TestMaxNumberOfQuestionsGetter() {
        assertEquals(5, waitingRoom.getMaxNumberOfQuestions());
    }

    /**
     * Test the setter for the multiplayer game id
     */
    @Test
    public void TestMultiPlayerGameIDSetter() {
        assertEquals(0, waitingRoom.getMultiplayerGameID());

        waitingRoom.setMultiplayerGameID(14);

        assertEquals(14, waitingRoom.getMultiplayerGameID());

        waitingRoom.flushWaitingRoom();

        assertEquals(15, waitingRoom.getMultiplayerGameID());

        for(int i = 0; i < 10; i++)
            waitingRoom.flushWaitingRoom();

        assertEquals(25, waitingRoom.getMultiplayerGameID());

        waitingRoom.setMultiplayerGameID(2);

        assertEquals(2, waitingRoom.getMultiplayerGameID());

        waitingRoom.setMultiplayerGameID(-14);

        assertEquals(0, waitingRoom.getMultiplayerGameID());

        waitingRoom.setMultiplayerGameID(Integer.MIN_VALUE);

        assertEquals(0, waitingRoom.getMultiplayerGameID());

        waitingRoom.setMultiplayerGameID(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, waitingRoom.getMultiplayerGameID());
    }

    /**
     * Test the setter for the player list
     */
    @Test
    public void TestPlayerListSetter() {
        assertEquals(new ArrayList<>(), waitingRoom.getPlayers());

        Player a = new Player("a", 200);
        Player b = new Player("b", 100);
        Player c = new Player("c");
        Player d = new Player("d", 100);
        players.addAll(List.of(a, b, c, d));
        waitingRoom.setPlayers(players);

        assertEquals(players, waitingRoom.getPlayers());
    }

    /**
     * Test the setter for the question list
     */
    @Test
    public void TestQuestionListSetter() {
        assertEquals(new ArrayList<>(), waitingRoom.getQuestions());

        ComparativeQuestion q1 = new ComparativeQuestion(List.of(a1, a2), true);
        EstimationQuestion q2 = new EstimationQuestion(a2);
        ComparativeQuestion q3 = new ComparativeQuestion(null, false);
        questions.addAll(List.of(q1, q2, q3));

        waitingRoom.setQuestions(questions);

        assertEquals(questions, waitingRoom.getQuestions());
    }

    /**
     * Test the setter for the max number of questions field
     */
    @Test
    public void TestMaxNumberOfQuestionsSetter() {
        assertEquals(5, waitingRoom.getMaxNumberOfQuestions());

        waitingRoom.setMaxNumberOfQuestions(7);

        assertEquals(7, waitingRoom.getMaxNumberOfQuestions());

        waitingRoom.setMaxNumberOfQuestions(10);

        assertEquals(10, waitingRoom.getMaxNumberOfQuestions());

        waitingRoom.setMaxNumberOfQuestions(25);

        assertEquals(25, waitingRoom.getMaxNumberOfQuestions());
    }

}