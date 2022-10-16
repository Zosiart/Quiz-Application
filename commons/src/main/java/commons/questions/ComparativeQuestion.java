package commons.questions;

import commons.Activity;
import lombok.Data;

import java.util.List;

/**
 * Class for storing Comparative questions in the format:
 *      Which activity uses the most/least energy?
 *          Activity 1
 *          Activity 2
 *          Activity 3
 */
@Data
public class ComparativeQuestion implements Question{

    private boolean isMost; // true if the question asks for most, false if least energy
    private List<Activity> activities; // list of activities
    private int correct_answer; // index of the correct answer in the list

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public ComparativeQuestion(){} // needed to be able to build object from json

    /**
     * Constructor which specifies a list of activities and if question isMost
     * @param activities List of activities (usually 3)
     * @param isMost If question asks for the most energy consumption
     */
    public ComparativeQuestion(List<Activity> activities, boolean isMost){
        this.isMost = isMost;
        this.activities = activities;
        this.correct_answer = generateCorrectAnswer();
    }

    private int generateCorrectAnswer() {
        if(activities == null || activities.size() <= 0){
            return -1;
        }
        int min = 0; // index of minimum
        int max = 0; // index of maximum
        for(int i = 1; i < activities.size(); i++){

            Activity current = activities.get(i);
            if(current.getConsumption_in_wh() < activities.get(min).getConsumption_in_wh() ){
                min = i; // check if current has smaller consumption than min
            }
            if(current.getConsumption_in_wh() > activities.get(max).getConsumption_in_wh() ){
                max = i; // checks if current has bigger consumption than max
            }
        }
        if(isMost){
            return max;
        } else {
            return min;
        }
    }
}
