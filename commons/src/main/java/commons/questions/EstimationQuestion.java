package commons.questions;

import commons.Activity;
import lombok.*;

@Data
/**
 * Class for estimation questions. A player is supposed to estimate which question uses similar
 * amount of energy
 */
public class EstimationQuestion implements Question {
    private Activity activity; // activity

    /**
     * no args constructor
     */
    public EstimationQuestion() {
    }

    /**
     * constructor with the activities
     * @param activity to add to question
     */
    public EstimationQuestion(Activity activity) {
        this.activity = activity;
    }
}
