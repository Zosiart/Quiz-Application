package commons;

import commons.questions.MCQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MCQuestionTest {

    List<Activity> activities;

    /**
     * Run before each test
     * Creates a list of activities
     */
    @BeforeEach
    public void setup() {
        activities = List.of(
                new Activity("1", "image_a", "a", 1L, "a"),
                new Activity("2", "image_b", "b", 2L, "b"),
                new Activity("3", "image_c", "c", 3L, "c")
        );
    }

    @Test
    void noArgsConstructorTest() {
        MCQuestion q = new MCQuestion();
        assertNotNull(q);
    }

    @Test
    void ConstructorTest() {
        MCQuestion q = new MCQuestion(activities.get(0), List.of(activities.get(1).getConsumption_in_wh(), activities.get(2).getConsumption_in_wh()));
        assertNotNull(q);
    }
}
