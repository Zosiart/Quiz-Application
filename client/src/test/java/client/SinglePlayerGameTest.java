package client;

import commons.Activity;
import commons.questions.ComparativeQuestion;
import commons.Player;
import commons.questions.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SinglePlayerGameTest {

    SinglePlayerGame singlePlayerGame;

    /**
     * Creates a new singleplayergame with an empty list of questions
     * Run before each test
     */
    @BeforeEach
    public void setup(){
        List<Question> questions = new ArrayList<>();
        singlePlayerGame = new SinglePlayerGame(new Player("a"), questions);
    }

    /**
     * Tests if the empty constructor creates an object
     */
    @Test
    public void testEmptyConstructor() {
        singlePlayerGame = new SinglePlayerGame();
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if the constructor with Player creates a new object
     */
    @Test
    public void testConstructorPlayer() {
        singlePlayerGame = new SinglePlayerGame(new Player("a"));
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if the constructor with maxQuestions creates a new object
     */
    @Test
    public void testConstructorMaxQuestions() {
        singlePlayerGame = new SinglePlayerGame(1);
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if the constructor with maxQuestions and username creates a new object
     */
    @Test
    public void testConstructorMaxQuestionsUserName() {
        singlePlayerGame = new SinglePlayerGame(1, "username");
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if the constructor in setup creates a new object
     */
    @Test
    public void testConstructor() {
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if a question is added successfully
     */
    @Test
    public void testAddQuestion(){
        singlePlayerGame.addQuestion(new ComparativeQuestion());
        assertEquals(1, singlePlayerGame.getQuestions().size());
    }

    /**
     * Tests if the points to be added calculation is correct
     */
    @Test
    public void testGetPointsToBeAdded() {
        singlePlayerGame.getPlayer().setStreak(5);
        assertEquals(1050, singlePlayerGame.getPointsToBeAdded(10));
        singlePlayerGame.getPlayer().setStreak(1);
        assertEquals(1010, singlePlayerGame.getPointsToBeAdded(10));
        assertEquals(1015, singlePlayerGame.getPointsToBeAdded(9));
        singlePlayerGame.getPlayer().setStreak(3);
        assertEquals(1056, singlePlayerGame.getPointsToBeAdded(5));
    }

    /**
     * Tests if the points to be added for a regular question is correct
     */
    @Test
    public void testAddPointsRegularQuestion() {
        singlePlayerGame.getPlayer().setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        singlePlayerGame.addPoints(5, 1.0);
        assertEquals(1066, singlePlayerGame.getPlayer().getScore());
    }

    /**
     * Tests if the points to be added for a wrong answer is correct
     */
    @Test
    public void testAddPointsWrongAnswer() {
        int scoreBefore = singlePlayerGame.getPlayer().getScore();
        singlePlayerGame.addPoints(-1, 1.0);
        assertEquals(scoreBefore, singlePlayerGame.getPlayer().getScore());
    }

    /**
     * Tests if the points to be added for an estimation question is correct
     */
    @Test
    public void testAddPointsGuessQuestion() {
        singlePlayerGame.getPlayer().setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        singlePlayerGame.addPoints(7, 0.49);
        assertEquals(517, singlePlayerGame.getPlayer().getScore());
    }


    /**
     * Tests the nextQuestion method
     */
    @Test
    public void testNextQuestion() {
        for(int i = 0; i < 5; i++){
            singlePlayerGame.nextQuestion();
        }
        assertEquals(6, singlePlayerGame.getQuestionNumber());
    }


    /**
     * Tests if the streak is reset after a wrong answer
     */
    @Test
    public void testStreakAfterWrongAnswer() {
        singlePlayerGame.addPoints(-1, 1.0);
        assertEquals(0, singlePlayerGame.getPlayer().getStreak());
    }

    /**
     * Test for getPlayer
     */
    @Test
    public void testPlayerGetter() {
        Player p = new Player("a", 420);
        singlePlayerGame.getPlayer().setScore(420);
        assertEquals(p, singlePlayerGame.getPlayer());
    }


    /**
     * Tests if question gets added
     */
    @Test
    public void testQuestionGetter() {
        singlePlayerGame.addQuestion(new ComparativeQuestion());
        singlePlayerGame.addQuestion(new ComparativeQuestion(new ArrayList<Activity>(), true));
        assertEquals(2, singlePlayerGame.getQuestions().size());
    }

    /**
     * Test if question added was not null
     */
    @Test
    public void testQuestionGetterNotNull() {
        singlePlayerGame.addQuestion(new ComparativeQuestion());
        singlePlayerGame.addQuestion(new ComparativeQuestion());
        assertNotNull(singlePlayerGame.getQuestions().get(0));
    }

    /**
     * Test for setQuestion
     */
    @Test
    public void testQuestionSetter() {
        List<Question> questions = new ArrayList<>();
        questions.add(new ComparativeQuestion());
        questions.add(new ComparativeQuestion());
        singlePlayerGame.setQuestions(questions);
        assertEquals(2, singlePlayerGame.getQuestions().size());
    }

    /**
     * Test for getQuestionNumber
     */
    @Test
    public void testQuestionNumberGetter() {
        singlePlayerGame.nextQuestion();
        singlePlayerGame.nextQuestion();
        singlePlayerGame.nextQuestion();
        assertEquals(4, singlePlayerGame.getQuestionNumber());
    }

    /**
     * Test for setPlayer
     */
    @Test
    public void testPlayerSetter() {
        Player p = new Player("b", 420);
        singlePlayerGame.setPlayer(p);
        assertEquals(p, singlePlayerGame.getPlayer());
    }


    /**
     * Test for setQuestionNumber
     */
    @Test
    public void testQuestionNumberSetter() {
        singlePlayerGame.setQuestionNumber(3);
        assertEquals(3, singlePlayerGame.getQuestionNumber());
    }
}
