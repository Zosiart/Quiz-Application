package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.questions.EstimationQuestion;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Data
public class EstimationQuestionCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private EstimationQuestion question;

    // for how long to show question and answer
    private double questionTime = 15.0;
    private double answerTime = 4.0;

    private int timeWhenAnswered = -1;
    private double guessAccuracy = 1.0;
    private int currentTime = (int) questionTime;
    private int pointsGainedForQuestion = 0;
    private double additionalPoints = 1.0; // if joker "double points" is used, it is set to 2.0

    // Timeline objects used for animating the progressbar
    // Global objects because they need to be accessed from different methods
    private Timeline questionTimer;
    private Timeline answerTimer;
    private boolean multiplayer;


    @FXML
    private TextField answerField;

    @FXML
    private ImageView image;

    @FXML
    private Label questionLabel;

    @FXML
    private Button exit;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button joker1;

    @FXML
    private Button joker2;

    @FXML
    private Button joker3;

    @FXML
    private HBox questionBox;

    @FXML
    private Label jokerMessage;

    @FXML
    private Label QuestionNumber;


    /**
     * Constructor for the Estimation Question Controller
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public EstimationQuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * sets multiplayer flag
     *
     * @param v multiplayer bool
     */
    public void setMultiplayer(boolean v) {
        this.multiplayer = v;
    }

    /**
     * Exiting the screen
     */
    public void exit() {
        mainCtrl.showHomeScreen();
        stopTimers();
        mainCtrl.resetQuestionScreens();
    }

    private void setQuestionNumber(){
        if (!multiplayer){
            QuestionNumber.setText("Question:  " + mainCtrl.getSinglePlayerGame().getQuestionNumber() +"/"+mainCtrl.getSinglePlayerGameQuestions());
        } else {
            QuestionNumber.setText("Question:  " + (mainCtrl.getMultiPlayerGame().getQuestionNumber()+1)+"/"+mainCtrl.getMultiPlayerGame().getQuestions().size());
        }
    }


    /**
     * Function for creating a countdown and a progress bar
     */
    public void countdown() {

        // set the progressbar value to be 0 at the beginning of the animation
        KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0));

        // set the keyframe at the end of the animation
        KeyFrame qEnd = new KeyFrame(Duration.seconds(questionTime), e -> { // whatever is in e -> {} will be run when this keyframe is reached
            showAnswers(); // show answers when the animation is done
        }, new KeyValue(progressBar.progressProperty(), 1)); // set the progressbar value to be 1

        // initialize the timeline with the 2 keyframes
        questionTimer = new Timeline(start, qEnd);
        // set timeline to only run once (can also be made to loop indefinitely)
        questionTimer.setCycleCount(1);

        // starts the timeline
        questionTimer.play();
    }

    /**
     * Sets the question object for this screen
     * Also sets the question label and answer text field
     *
     * @param question
     */
    public void setQuestion(EstimationQuestion question) {
        this.question = question;
        setQuestionText();
        // Sets a formatter for the input field to only accept numbers
        answerField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("[0-9]*") ? change : null));
        setImage();
        setJokers();
        setQuestionNumber();
        if(multiplayer) {
            joker1.setVisible(false);
        } else {
            joker1.setVisible(true);
        }
    }

    /**
     * Set textual representation of the question
     */
    private void setQuestionText() {
        String questionText = "How much energy does " + this.question.getActivity().getTitle() + " use?";
        this.questionLabel.setText(questionText);
    }

    /**
     * Checking the correctness of the answer
     *
     * @param answer
     */
    public void checkAnswer(Long answer) {
        Long correctAnswer = question.getActivity().getConsumption_in_wh();
        double upperBound = correctAnswer * 1.8;
        double lowerBound = correctAnswer * 0.4;

        if (answer > upperBound || answer < lowerBound) {
            guessAccuracy = 0;
            timeWhenAnswered = -1;
            return;
        }

        guessAccuracy = (double) answer / correctAnswer;
        if (guessAccuracy > 1)
            guessAccuracy = 2 - guessAccuracy;
        timeWhenAnswered = (int) (progressBar.getProgress() * questionTime);
    }

    private void showAnswers() {

        // This creates another timeline for the timer for the answerTime. See countdown() for a more in-depth breakdown
        KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0));
        KeyFrame aEnd = new KeyFrame(Duration.seconds(answerTime), e -> {
            if (multiplayer) {
                resetEstimationQuestion();
            } else {
                endQuestion(); // end the question when the animation is done
            }
        }, new KeyValue(progressBar.progressProperty(), 1));
        answerTimer = new Timeline(start, aEnd);
        answerTimer.setCycleCount(1);
        answerTimer.play();

        answerField.setDisable(true);
        // disable joker buttons, so they can't be clicked while
        // answers are being shown
        joker1.setMouseTransparent(true);
        joker2.setMouseTransparent(true);
        joker3.setMouseTransparent(true);

        questionLabel.setText(questionLabel.getText() + " - " + question.getActivity().getConsumption_in_wh() + " Wh");

        if (multiplayer) {
            mainCtrl.addScoreMultiplayer(timeWhenAnswered, additionalPoints * guessAccuracy);
        } else {
            pointsGainedForQuestion = mainCtrl.getSinglePlayerGame().addPoints(timeWhenAnswered, additionalPoints * guessAccuracy);
        }
        additionalPoints = 1.0;
    }

    /**
     * Resets attributes to default after each question
     *
     * To be used only during game!
     * After the end of the game, finalResetEstimationQuestion() should be used.
     * The difference is that this method leaves the jokers disabled (if they have been clicked previously)
     */
    public void resetEstimationQuestion() {
        timeWhenAnswered = -1;

        // re-enable jokers
        setJokers();

        // reset answerField text and status
        answerField.setText("");
        answerField.setDisable(false);

        jokerMessage.setText("");

        stopTimers();
    }

    private void endQuestion() {
        resetEstimationQuestion();
        mainCtrl.showScoreChangeScreen(pointsGainedForQuestion);
    }

    @FXML
    void answerTextChanged(KeyEvent event) {
        // try-catch block needed to check for errors when parsing string
        try {
            Long answer = Long.parseLong(answerField.getText());
            checkAnswer(answer);
        } catch (NumberFormatException e) {
        }
    }

    @FXML
    private void joker1() {
        if (!multiplayer) {
            joker1.setDisable(true);
            mainCtrl.useJokerAdditionalQuestion();

            stopTimers();
            /* even if the correct answer was selected before the question was changed, 0 points will be added
             * the method addPoints() is used just to increment the number of the current question in the list
             * streak is reset to 0
             */
            pointsGainedForQuestion = mainCtrl.getSinglePlayerGame().addPoints(-1, 0.0);
            endQuestion();
        }
    }

    @FXML
    private void joker2() {
        // if there is no answer input, don't use joker
        if (answerField.getText().equals("")) {
            jokerMessage.setText("Input an answer to use this joker!");
            return;
        }

        joker2.setDisable(true); // disable button
        mainCtrl.useJokerRemoveOneAnswer();

        /* calculate the points the player would win for this question
         * the same way they are calculated in addPoints(), but without actually adding them
         */
        Long answer = Long.parseLong(answerField.getText());
        checkAnswer(answer);

        int pointsToBeAdded = 0;
        if(multiplayer) {
            pointsToBeAdded = (int) Math.round(guessAccuracy * additionalPoints * mainCtrl.getPointsToBeAdded(timeWhenAnswered));
        } else {
            pointsToBeAdded = (int) Math.round(guessAccuracy * additionalPoints * mainCtrl.getSinglePlayerGame().getPointsToBeAdded(timeWhenAnswered));
        }

        if (pointsToBeAdded > 0) {
            jokerMessage.setText("Close enough! You will get some points for this answer.");
        } else {
            Long correctAnswer = question.getActivity().getConsumption_in_wh();
            if (answer > correctAnswer)
                jokerMessage.setText("You guess is too far from the actual answer! Try a lower value.");
            else jokerMessage.setText("You guess is too far from the actual answer! Try a higher value.");
        }
    }

    @FXML
    private void joker3 () {
        joker3.setDisable(true); // disable button
        mainCtrl.useJokerDoublePoints();

        additionalPoints = 2.0; // points will be double only for the current question
    }

    /**
     * Reset the states of the jokers. Enable all jokers and set their usage to false.
     */
    private void resetJokers () {
        joker1.setDisable(false);
        joker2.setDisable(false);
        joker3.setDisable(false);
        joker1.setMouseTransparent(false);
        joker2.setMouseTransparent(false);
        joker3.setMouseTransparent(false);
        mainCtrl.resetJokers();
    }

    /**
     * Reset an estimation question
     */
    public void finalResetEstimationQuestion () {
        stopTimers();
        resetEstimationQuestion();
        resetJokers();
    }

    private void stopTimers () {
        if (questionTimer != null) {
            questionTimer.stop();
            questionTimer = null;
        }
        if (answerTimer != null) {
            answerTimer.stop();
            answerTimer = null;
        }
    }

    /**
     * Sets the images to the ones stored in the activities.
     * Also sets the images to be the same width as the question
     */
    private void setImage () {
        if (question.getActivity().getImage() != null) {
            InputStream inputStream = new ByteArrayInputStream(question.getActivity().getImage());
            if (inputStream != null) {
                image.setImage(new Image(inputStream));
            }
        }
        image.fitHeightProperty().bind(questionBox.heightProperty());
    }

    /**
     * Updates the disabling of the buttons used for jokers, in case
     * a joker has been used on another screen.
     * Resets the mouse-transparency, used when answers are being shown
     */
    private void setJokers () {
        if (mainCtrl.jokerAdditionalQuestionIsUsed()) {
            joker1.setDisable(true);
        } else {
            joker1.setMouseTransparent(false);
        }

        if (mainCtrl.jokerRemoveOneAnswerIsUsed()) {
            joker2.setDisable(true);
        } else {
            joker2.setMouseTransparent(false);
        }

        if (mainCtrl.jokerDoublePointsIsUsed()) {
            joker3.setDisable(true);
        } else {
            joker3.setMouseTransparent(false);
        }
    }

    /**
     * Add tooltips for the jokers
     */
    public void addTooltips () {
        Tooltip skipQuestion = new Tooltip();
        skipQuestion.setText("Skip this question!");
        skipQuestion.setShowDelay(Duration.ZERO);
        joker1.setTooltip(skipQuestion);

        Tooltip cutAnswer = new Tooltip();
        cutAnswer.setText("Reveal a wrong answer!");
        cutAnswer.setShowDelay(Duration.ZERO);
        joker2.setTooltip(cutAnswer);

        Tooltip doublePoints = new Tooltip();
        doublePoints.setText("Double score intake for this question!");
        doublePoints.setShowDelay(Duration.ZERO);
        joker3.setTooltip(doublePoints);
    }

}