package client;


import commons.Player;
import commons.questions.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Single player game class
 * Used for handling a single-player game
 */
@Data
public class SinglePlayerGame {

    private List<Question> questions;
    private Player player;
    private int questionNumber = 1;
    private int maxQuestions;

    /**
     * Empty constructor
     * Used by Jackson to initialize object from JSON
     */
    public SinglePlayerGame() {}

    /**
     * Creates a new game with specified amount of questions
     * @param maxQuestions Number of questions in the game
     */
    public SinglePlayerGame(int maxQuestions) {
        this.player = new Player();
        this.questions = new ArrayList<>();
        this.maxQuestions = maxQuestions;
    }

    /**
     * Creates a new game with specified amount of questions and a username
     * @param maxQuestions Number of questions in the game
     * @param username Creates new player with given username
     */
    public SinglePlayerGame(int maxQuestions, String username) {
        this.player = new Player(username);
        this.questions = new ArrayList<>();
        this.maxQuestions = maxQuestions;
    }

    /**
     * Creates a new game with specific player
     * @param player
     */
    public SinglePlayerGame(Player player) {
        this.player = player;
        this.questions = new ArrayList<>();
    }

    /**
     * Creates a new game with specific player and a list of questions
     * @param player
     * @param questions
     */
    public SinglePlayerGame(Player player, List<Question> questions) {
        this.player = player;
        this.questions = questions;
    }

    /**
     * Adds a question to the list. Checks for duplicates
     * @param question to be added
     * @return true if question was added, false otherwise
     */
    public boolean addQuestion(Question question){
        // TODO: Make this comparison actually do something, right now it uses Object's equals method. Probably should use a set or something
        for(int i = 0; i < questions.size(); i++) {
            if (question.equals(questions.get(i))) return false;
        }
        questions.add(question);
        return true;
    }

    /**
     * This method adds points to a score of a player
     * A particular formula for the points has been developed.
     * parsing an int equal to -1 will be qualified as answering the question wrongly
     * @param timeWhenAnswered time in seconds how long it took a user to answer a question
     * @param guessQuestionRate for every other question than a guess question this will be set to 1.0
     *                          for the guess question this will be set to a percentage how good the guess was
     */
    public int addPoints(int timeWhenAnswered, double guessQuestionRate){
        if(timeWhenAnswered == -1){
            player.resetStreak();
            nextQuestion();
            return 0;
        }
        player.incrementStreak();
        int currentScore = getPlayer().getScore();
        int pointsToBeAdded = (int)Math.round(guessQuestionRate * getPointsToBeAdded(timeWhenAnswered));
        player.setScore( currentScore + pointsToBeAdded );
        nextQuestion();
        return pointsToBeAdded;
    }

    /**
     * The points achievable for a single question are from 950 to 1050 depending on the number of seconds it took the user to select an answer
     * Each second subtracts 5 points from the added score
     * The score is then multiplied by (100 + streak)% and added to the score of a player
     * @param time
     * @return
     */
    public int getPointsToBeAdded(int time) {
        double streakFactor = (100.0 + player.getStreak()) / 100.0;
        var points = Math.round(streakFactor * (1050 - 5 * time));
        return (int)points;
    }

    /**
     * Increments questionNumber
     */
    public void nextQuestion(){ questionNumber++;}

}
