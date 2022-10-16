package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.questions.ComparativeQuestion;
import commons.questions.EqualityQuestion;
import commons.questions.MCQuestion;
import commons.questions.Question;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class ComparativeQuestionScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private boolean multiplayer;

    /**
     * Integer to set the question mode
     * 0 - ComparativeQuestion
     * 1 - MCQuestion
     * 2 - EqualityQuestion
     */
    private int questionMode = 0;

    private ComparativeQuestion question;
    private MCQuestion mcQuestion;
    private EqualityQuestion equalityQuestion;

    // for how long to show question and answer
    private double questionTime = 15.0;
    private double answerTime = 4.0;

    private int timeWhenAnswered = -1;
    private int currentTime = (int) questionTime;
    private int pointsGainedForQuestion = 0;
    private double additionalPoints = 1.0; // if joker "double points" is used, it is set to 2.0

    // Timeline objects used for animating the progressbar
    // Global objects because they need to be accessed from different methods
    private Timeline questionTimer;
    private Timeline answerTimer;

    @FXML
    private Label questionLabel;

    @FXML
    private Label QuestionNumber;

    @FXML
    private Button answer1;

    @FXML
    private Button answer2;

    @FXML
    private Button answer3;

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
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private ImageView image3;

    @FXML
    private VBox questionBox1;

    @FXML
    private VBox questionBox2;

    @FXML
    private VBox questionBox3;

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public ComparativeQuestionScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * sets multiplayer flag
     * @param v multiplayer bool
     */
    public void setMultiplayer(boolean v){
        this.multiplayer = v;
    }

    /**
     * Runs when answer option 1 is clicked
     * Sets color of the clicked button to yellow, others to default
     * (unless it is disabled and marked red from joker 2)
     */
    public void answer1Clicked(){
        checkAnswer(0);
        if(!answer1.isDisabled()) answer1.setStyle("-fx-background-color: #fccf03;");
        if(!answer2.isDisabled()) answer2.setStyle("");
        if(!answer3.isDisabled()) answer3.setStyle("");
    }

    /**
     * Runs when answer option 2 is clicked
     * Sets color of the clicked button to yellow, others to default
     * (unless it is disabled and marked red from joker 2)
     */
    public void answer2Clicked(){
        checkAnswer(1);
        if(!answer1.isDisabled()) answer1.setStyle("");
        if(!answer2.isDisabled()) answer2.setStyle("-fx-background-color: #fccf03;");
        if(!answer3.isDisabled()) answer3.setStyle("");
    }

    /**
     * Runs when answer option 3 is clicked
     * Sets color of the clicked button to yellow, others to default
     * (unless it is disabled and marked red from joker 2)
     */
    public void answer3Clicked(){
        checkAnswer(2);
        if(!answer1.isDisabled()) answer1.setStyle("");
        if(!answer2.isDisabled()) answer2.setStyle("");
        if(!answer3.isDisabled()) answer3.setStyle("-fx-background-color: #fccf03;");
    }

    private void setQuestionNumber(){
        if (!multiplayer){
            QuestionNumber.setText("Question:  " + (mainCtrl.getSinglePlayerGame().getQuestionNumber() - mainCtrl.additionalQuestion()) +"/"+mainCtrl.getSinglePlayerGameQuestions());
        } else {
            QuestionNumber.setText("Question:  " + (mainCtrl.getMultiPlayerGame().getQuestionNumber()+1)+"/"+mainCtrl.getMultiPlayerGame().getQuestions().size());
        }
    }


    private void checkAnswer(int answer){
        int correctAnswer = -1;
        if(questionMode == 0){
            correctAnswer = question.getCorrect_answer();
        } else if(questionMode == 1){
            correctAnswer = mcQuestion.getCorrect_answer();
        } else if(questionMode == 2){
            correctAnswer = equalityQuestion.getCorrect_answer();
        }

        if(answer != correctAnswer){
            timeWhenAnswered = -1;
        } else {
            timeWhenAnswered = (int) (progressBar.getProgress() * questionTime);
        }
    }

    /**
     * Sets the question object for this screen
     * Also sets the question label and answer button texts
     * @param question
     */
    public void setQuestion(Question question) {
        if(question instanceof ComparativeQuestion){
            this.question = (ComparativeQuestion) question;
            this.questionMode = 0;
            setQuestionText();
            setAnswerTexts();
            setImages();
        } else if(question instanceof MCQuestion){
            this.mcQuestion = (MCQuestion) question;
            this.questionMode = 1;
            setMCQuestionText();
            setMCAnswerTexts();
            setMCImages();
        } else if(question instanceof EqualityQuestion){
            this.equalityQuestion = (EqualityQuestion) question;
            this.questionMode = 2;
            setEqualityText();
            setEqualityAnswerTexts();
            setEqualityImages();
        }
        setJokers();
        setQuestionNumber();
        if(multiplayer) {
            joker1.setVisible(false);
        } else {
            joker1.setVisible(true);
        }
    }

    private void setQuestionText(){
        // Strings used to construct the question text
        String mostOrLeast;
        if(this.question.isMost()){
            mostOrLeast = "most";
        } else {
            mostOrLeast = "least";
        }
        String questionText = "Which activity uses the " + mostOrLeast + " amount of energy?";
        this.questionLabel.setText(questionText);
    }

    private void setAnswerTexts(){
        answer1.setText(this.question.getActivities().get(0).getTitle());
        answer2.setText(this.question.getActivities().get(1).getTitle());
        answer3.setText(this.question.getActivities().get(2).getTitle());
    }

    /**
     * Sets the images to the ones stored in the activities.
     * Also sets the images to be the same width as the question
     */
    private void setImages(){
        // Adding the images to a list to avoid duplicate code
        List<ImageView> images = List.of(image1, image2, image3);
        // This loops through every activity, gets the image and sets the image in the UI
        for(int i = 0; i < question.getActivities().size(); i++){
            if(question.getActivities().get(i).getImage() != null){
                InputStream inputStream = new ByteArrayInputStream(question.getActivities().get(i).getImage());
                if(inputStream != null){
                    images.get(i).setImage(new Image(inputStream));
                }
            }
        }
        // It's dumb that we have to set the images to be the width of the vbox here
        // A true javafx moment
        image1.fitWidthProperty().bind(questionBox1.widthProperty());
        image2.fitWidthProperty().bind(questionBox2.widthProperty());
        image3.fitWidthProperty().bind(questionBox3.widthProperty());
    }

    private void setMCQuestionText(){
        String questionText = "How much energy does " + this.mcQuestion.getActivity().getTitle() + " use?";
        this.questionLabel.setText(questionText);
    }

    private void setMCAnswerTexts(){
        answer1.setText(this.mcQuestion.getOptions().get(0) + " Wh");
        answer2.setText(this.mcQuestion.getOptions().get(1) + " Wh");
        answer3.setText(this.mcQuestion.getOptions().get(2) + " Wh");
    }

    /**
     * Sets the image to the one stored in the activity.
     * Also sets the image to be the same width as the question
     */
    private void setMCImages(){
        // This loops through every activity, gets the image and sets the image in the UI
        if(mcQuestion.getActivity().getImage() != null){
            InputStream inputStream = new ByteArrayInputStream(mcQuestion.getActivity().getImage());
            if(inputStream != null){
                image2.setImage(new Image(inputStream));
            }
        }
        // set the images to be the width of the vbox
        image2.fitWidthProperty().bind(questionBox2.widthProperty());
    }

    private void setEqualityText(){
        String questionText = "Instead of '" + equalityQuestion.getChosen().getTitle() + "' you could be...";
        this.questionLabel.setText(questionText);
    }

    private void setEqualityAnswerTexts(){
        answer1.setText(this.equalityQuestion.getActivities().get(0).getTitle());
        answer2.setText(this.equalityQuestion.getActivities().get(1).getTitle());
        answer3.setText(this.equalityQuestion.getActivities().get(2).getTitle());
    }

    /**
     * Sets the images to the ones stored in the activities.
     * Also sets the images to be the same width as the question
     */
    private void setEqualityImages(){
        // Adding the images to a list to avoid duplicate code
        List<ImageView> images = List.of(image1, image2, image3);
        // This loops through every activity, gets the image and sets the image in the UI
        for(int i = 0; i < equalityQuestion.getActivities().size(); i++){
            if(equalityQuestion.getActivities().get(i).getImage() != null){
                InputStream inputStream = new ByteArrayInputStream(equalityQuestion.getActivities().get(i).getImage());
                if(inputStream != null){
                    images.get(i).setImage(new Image(inputStream));
                }
            }
        }
        // It's dumb that we have to set the images to be the width of the vbox here
        // A true javafx moment
        image1.fitWidthProperty().bind(questionBox1.widthProperty());
        image2.fitWidthProperty().bind(questionBox2.widthProperty());
        image3.fitWidthProperty().bind(questionBox3.widthProperty());
    }

    /**
     * Exits the screen. Goes back to the home screen
     */
    public void exit() {
        mainCtrl.showHomeScreen();
        stopTimers();
        mainCtrl.resetQuestionScreens();
    }

    /**
     * Uses a Timeline object to create the progress bar and timer
     * Timeline is like animation, it uses KeyFrame objects which set at which point in what should the scene look like
     * Keyframes can also run code by adding a lambda function in them
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

    private void showAnswers(){

        // This creates another timeline for the timer for the answerTime. See countdown() for a more in-depth breakdown
        KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0));
        KeyFrame aEnd = new KeyFrame(Duration.seconds(answerTime), e -> {
            if(multiplayer){
                resetComparativeQuestionScreen();
            } else {
                endQuestion(); // end the question when the animation is done
            }
        }, new KeyValue(progressBar.progressProperty(), 1));
        answerTimer = new Timeline(start, aEnd);
        answerTimer.setCycleCount(1);
        answerTimer.play();

        // disable answer buttons, so they can't be clicked while
        // answers are being shown
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);

        // disable joker buttons, so they can't be clicked while
        // answers are being shown
        joker1.setMouseTransparent(true);
        joker2.setMouseTransparent(true);
        joker3.setMouseTransparent(true);

        int correctAnswer = -1;
        if(questionMode == 0){
            correctAnswer = question.getCorrect_answer();
        } else if(questionMode == 1){
            correctAnswer = mcQuestion.getCorrect_answer();
        } else if(questionMode == 2){
            correctAnswer = equalityQuestion.getCorrect_answer();
        }

        if (multiplayer) {
            mainCtrl.addScoreMultiplayer(timeWhenAnswered, additionalPoints);
        } else {
            pointsGainedForQuestion = mainCtrl.getSinglePlayerGame().addPoints(timeWhenAnswered, additionalPoints);
        }
        additionalPoints = 1.0;

        // highlight correct answer
        if(correctAnswer == 0){
            answer1.setStyle("-fx-background-color: #00ff00;");
        } else if(correctAnswer == 1){
            answer2.setStyle("-fx-background-color: #00ff00;");
        } else {
            answer3.setStyle("-fx-background-color: #00ff00;");
        }

        // make it so that answers also show the respective consumptions
        if(questionMode == 0){
            answer1.setText(
                this.question.getActivities().get(0).getTitle()
                    + " - " + this.question.getActivities().get(0).getConsumption_in_wh()
                    + " Wh");
            answer2.setText(
                this.question.getActivities().get(1).getTitle()
                    + " - " + this.question.getActivities().get(1).getConsumption_in_wh()
                    + " Wh");
            answer3.setText(
                this.question.getActivities().get(2).getTitle()
                    + " - " + this.question.getActivities().get(2).getConsumption_in_wh()
                    + " Wh");
        } else if(questionMode == 2){
            answer1.setText(
                this.equalityQuestion.getActivities().get(0).getTitle()
                    + " - " + this.equalityQuestion.getActivities().get(0).getConsumption_in_wh()
                    + " Wh");
            answer2.setText(
                this.equalityQuestion.getActivities().get(1).getTitle()
                    + " - " + this.equalityQuestion.getActivities().get(1).getConsumption_in_wh()
                    + " Wh");
            answer3.setText(
                this.equalityQuestion.getActivities().get(2).getTitle()
                    + " - " + this.equalityQuestion.getActivities().get(2).getConsumption_in_wh()
                    + " Wh");
        }
    }

    /**
     * Resets attributes to default after each question
     *
     * To be used only during game!
     * After the end of the game, finalResetComparativeQuestionScreen() should be used
     * The difference is that this method leaves the jokers disabled (if they have been clicked previously)
     */
    public void resetComparativeQuestionScreen(){
        timeWhenAnswered = -1;
        answer1.setStyle("");
        answer2.setStyle("");
        answer3.setStyle("");

        // re-enable answers
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);

        // re-enable jokers
        setJokers();

        // reset images
        image1.setImage(null);
        image2.setImage(null);
        image3.setImage(null);
        this.questionMode = 0;

        stopTimers();
    }

    private void endQuestion(){
        resetComparativeQuestionScreen();
        mainCtrl.showScoreChangeScreen(pointsGainedForQuestion);
    }

    /**
     * Skips the question and adds one more to the total number of questions
     */
    @FXML
    private void joker1() {
        if(!multiplayer) {
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

    /**
     * Disables one of the incorrect answer options
     */
    @FXML
    private void joker2() {
        joker2.setDisable(true); // disable button
        mainCtrl.useJokerRemoveOneAnswer();

        int correctAnswer = -1;
        if (questionMode == 0) {
            correctAnswer = question.getCorrect_answer();
        } else if (questionMode == 1) {
            correctAnswer = mcQuestion.getCorrect_answer();
        } else if (questionMode == 2) {
            correctAnswer = equalityQuestion.getCorrect_answer();
        }

        Random random = new Random();
        int x = Math.abs(random.nextInt() % 2); // get a 0 or 1 randomly
        int disableOption = (correctAnswer + x + 1) % 3; // get one of the incorrect answers
        if (disableOption == 0) {
            answer1.setDisable(true);
            answer1.setStyle("-fx-background-color: #fc1c45;");
        } else if (disableOption == 1) {
            answer2.setDisable(true);
            answer2.setStyle("-fx-background-color: #fc1c45;");
        } else if (disableOption == 2) {
            answer3.setDisable(true);
            answer3.setStyle("-fx-background-color: #fc1c45;");
        }
    }

    /**
     * Doubles the amount of points for the current question
     */
    @FXML
    private void joker3() {
        joker3.setDisable(true); // disable button
        mainCtrl.useJokerDoublePoints();

        additionalPoints = 2.0; // points will be double only for the current question
    }

    /**
     * Enables the use of the jokers again for the next game
     *
     * Intentionally a separate method and not included in resetComparativeQuestionScreen(),
     * because it is used to reset the 3 answer options after every question, but
     * jokers should remain disabled until the end of the game
     */
    private void resetJokers() {
        joker1.setDisable(false);
        joker2.setDisable(false);
        joker3.setDisable(false);
        joker1.setMouseTransparent(false);
        joker2.setMouseTransparent(false);
        joker3.setMouseTransparent(false);
        mainCtrl.resetJokers();
    }

    /**
     * Resets the comparative question screen
     */
    public void finalResetComparativeQuestionScreen() {
        stopTimers();
        resetComparativeQuestionScreen();
        resetJokers();
    }

    /**
     * Method which stops the timeline animations.
     * This needs to be done when leaving the screen before the animation is finished,
     * otherwise the code at the end will still be run
     */
    private void stopTimers(){
        if(questionTimer != null){
            questionTimer.stop();
            questionTimer = null;
        }
        if(answerTimer != null){
            answerTimer.stop();
            answerTimer = null;
        }
    }

    /**
     * Updates the disabling of the buttons used for jokers, in case
     * a joker has been used on another screen.
     * Resets the mouse-transparency, used when answers are being shown
     */
    private void setJokers() {
        if(mainCtrl.jokerAdditionalQuestionIsUsed()) {
            joker1.setDisable(true);
        } else {
            joker1.setMouseTransparent(false);
        }

        if(mainCtrl.jokerRemoveOneAnswerIsUsed()) {
            joker2.setDisable(true);
        } else {
            joker2.setMouseTransparent(false);
        }

        if(mainCtrl.jokerDoublePointsIsUsed()) {
            joker3.setDisable(true);
        } else {
            joker3.setMouseTransparent(false);
        }
    }

    /**
     * Add tooltips for the jokers
     */
    public void addTooltips() {
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
