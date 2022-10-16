package commons;

import commons.questions.ComparativeQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComparativeQuestionTest {

    List<Activity> activities;

    /**
     * Run before each test
     * Creates a list of activities
     */
    @BeforeEach
    public void setup(){
        activities = List.of(
            new Activity("1", "image_a", "a", 1L, "a"),
            new Activity("2", "image_b","b", 2L, "b"),
            new Activity("3", "image_c","c", 3L, "c")
        );
    }

    @Test
    void noArgsConstructorTest(){
        ComparativeQuestion q = new ComparativeQuestion();
        assertNotNull(q);
    }

    /**
     * Tests if correct answer is generated with most == true
     */
    @Test
    void generateCorrectAnswerMost() {
        ComparativeQuestion q = new ComparativeQuestion(activities, true);
        assertEquals(2, q.getCorrect_answer());
    }

    /**
     * Tests if correct answer is generated with most == true
     * Reverses the order of activities to get more lines covered
     */
    @Test
    void generateCorrectAnswerMost2() {
        List<Activity> reverseActs = List.of(activities.get(2), activities.get(1), activities.get(0));
        ComparativeQuestion q = new ComparativeQuestion(reverseActs, true);
        assertEquals(0, q.getCorrect_answer());
    }

    /**
     * Tests if correct answer is generated with most == false
     */
    @Test
    void generateCorrectAnswerLeast() {
        ComparativeQuestion q = new ComparativeQuestion(activities, false);
        assertEquals(0, q.getCorrect_answer());
    }

    /**
     * Tests if correct answer is generated with activities being null
     */
    @Test
    void generateCorrectAnswerNull() {
        ComparativeQuestion q = new ComparativeQuestion(null, true);
        assertEquals(-1, q.getCorrect_answer());
    }

    /**
     * Tests if correct answer is generated with activities being empty
     */
    @Test
    void generateCorrectAnswerEmpty() {
        ComparativeQuestion q = new ComparativeQuestion(new ArrayList<Activity>(), true);
        assertEquals(-1, q.getCorrect_answer());
    }

    /**
     * Checks if correct answer is generated with 1 activity
     */
    @Test
    void generateCorrectAnswerOne() {
        ComparativeQuestion q = new ComparativeQuestion(List.of(activities.get(0)), true);
        assertEquals(0, q.getCorrect_answer());
    }
}