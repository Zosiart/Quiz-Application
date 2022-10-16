package commons.questions;

import commons.Activity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
/**
 * Class for MC Question. A player is supposed to choose how much energy does an activity waste
 */
public class MCQuestion implements Question {
    private Activity activity;
    private List<Long> options;
    private int correct_answer;

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public MCQuestion() {
    }

    /**
     * constructor with an activity
     *
     * @param activity to add to question
     */
    public MCQuestion(Activity activity, List<Long> options) {
        this.activity = activity;

        this.options = new ArrayList<>(options);
        this.options.add(activity.getConsumption_in_wh());
        Collections.sort(this.options);

        this.correct_answer = this.options.indexOf(activity.getConsumption_in_wh());
    }
}
