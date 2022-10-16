package commons.questions;

import commons.Activity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for creating Equality Question
 */
@Data
public class EqualityQuestion implements Question {

    private List<Activity> activities;
    private Activity chosen;
    private int correct_answer;

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public EqualityQuestion() {
    }

    /**
     * Constructor which sets up the question with specific parameters
     * @param chosen The activity chosen to be in the question text
     * @param correct An activity with consumption equal to chosen
     * @param activities List of activities (wrong answers)
     * @param correct_answer index where correct should be added to in activities
     */
    public EqualityQuestion(Activity chosen, Activity correct, List<Activity> activities, int correct_answer) {
        this.chosen = chosen;
        this.correct_answer = correct_answer;
        this.activities = new ArrayList<>(activities);
        this.activities.add(correct_answer, correct);
    }

}
