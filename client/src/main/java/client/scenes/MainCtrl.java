package client.scenes;

import client.SinglePlayerGame;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.GameUpdatesPacket;
import commons.MultiPlayerGame;
import commons.Player;
import commons.questions.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MainCtrl {

    @Getter
    private final ServerUtils server;

    @Getter
    private Stage primaryStage;

    private HomeScreenCtrl homeScreenCtrl;
    @Getter
    private Parent homeScreenParent;

    private WaitingRoomCtrl waitingRoomCtrl;
    private Parent waitingRoomParent;

    private LoadingScreenCtrl loadingScreenCtrl;
    private Parent loadingScreenParent;

    private ComparativeQuestionScreenCtrl comparativeQuestionScreenCtrl;
    private Parent comparativeQuestionScreenParent;

    @Getter
    private UsernameScreenCtrl usernameScreenCtrl;
    private Parent usernameScreenParent;

    private EndScreenCtrl endScreenCtrl;
    private Parent endScreenParent;

    private HelpScreenCtrl helpScreenCtrl;
    private Parent helpScreenParent;

    private ScoreChangeScreenCtrl scoreChangeScreenCtrl;
    private Parent scoreChangeScreenParent;

    private EstimationQuestionCtrl estimationScreenCtrl;
    private Parent estimationQuestionParent;

    private SettingsScreenCtrl settingsScreenCtrl;
    private Parent settingsScreenParent;

    private ScoreChangeMultiplayerCtrl scoreChangeMultiplayerCtrl;
    private Parent scoreChangeMultiplayerParent;

    private AdminScreenCtrl adminScreenCtrl;
    private Parent adminScreenParent;

    private EndMultiplayerScreenCtrl endMultiplayerScreenCtrl;
    private Parent endMultiplayerScreenParent;

    private boolean jokerAdditionalQuestion = false;
    private boolean jokerRemoveOneAnswer = false;
    private boolean jokerDoublePoints = false;

    // single player variables
    @Getter
    private SinglePlayerGame singlePlayerGame;
    @Getter
    private int singlePlayerGameQuestions = 20;

    /**
     * Creates a new MainCtrl with server
     *
     * @param server ServerUtils object
     */
    @Inject
    public MainCtrl(ServerUtils server) {
        this.server = server;
    }

    /**
     * Initializes the screen
     *
     * @param primaryStage              The primary stage to use (window)
     * @param homeScreen                Screens that main controller can communicate with
     * @param waitingRoom
     * @param loadingScreen
     * @param comparativeQuestionScreen
     * @param usernameScreen
     * @param endScreen
     * @param helpScreen
     * @param scoreChangeScreen
     * @param scoreChangeMultiplayer
     * @param adminScreen
     */
    public void initialize(
            Stage primaryStage,
            Pair<HomeScreenCtrl, Parent> homeScreen,
            Pair<WaitingRoomCtrl, Parent> waitingRoom,
            Pair<LoadingScreenCtrl, Parent> loadingScreen,
            Pair<ComparativeQuestionScreenCtrl, Parent> comparativeQuestionScreen,
            Pair<UsernameScreenCtrl, Parent> usernameScreen,
            Pair<EndScreenCtrl, Parent> endScreen,
            Pair<HelpScreenCtrl, Parent> helpScreen,
            Pair<ScoreChangeScreenCtrl, Parent> scoreChangeScreen,
            Pair<SettingsScreenCtrl, Parent> settingsScreen,
            Pair<EstimationQuestionCtrl, Parent> estimationQuestion,
            Pair<ScoreChangeMultiplayerCtrl, Parent> scoreChangeMultiplayer,
            Pair<AdminScreenCtrl, Parent> adminScreen,
            Pair<EndMultiplayerScreenCtrl, Parent> endMultiplayerScreen
    ) {
        this.primaryStage = primaryStage;

        this.homeScreenCtrl = homeScreen.getKey();
        this.homeScreenParent = homeScreen.getValue();

        this.waitingRoomCtrl = waitingRoom.getKey();
        this.waitingRoomParent = waitingRoom.getValue();

        this.loadingScreenCtrl = loadingScreen.getKey();
        this.loadingScreenParent = loadingScreen.getValue();

        this.comparativeQuestionScreenCtrl = comparativeQuestionScreen.getKey();
        this.comparativeQuestionScreenParent = comparativeQuestionScreen.getValue();

        this.usernameScreenCtrl = usernameScreen.getKey();
        this.usernameScreenParent = usernameScreen.getValue();

        this.endScreenCtrl = endScreen.getKey();
        this.endScreenParent = endScreen.getValue();

        this.helpScreenCtrl = helpScreen.getKey();
        this.helpScreenParent = helpScreen.getValue();

        this.scoreChangeScreenCtrl = scoreChangeScreen.getKey();
        this.scoreChangeScreenParent = scoreChangeScreen.getValue();


        this.estimationScreenCtrl = estimationQuestion.getKey();
        this.estimationQuestionParent = estimationQuestion.getValue();

        this.settingsScreenCtrl = settingsScreen.getKey();
        this.settingsScreenParent = settingsScreen.getValue();

        this.scoreChangeMultiplayerCtrl = scoreChangeMultiplayer.getKey();
        this.scoreChangeMultiplayerParent = scoreChangeMultiplayer.getValue();

        this.adminScreenCtrl = adminScreen.getKey();
        this.adminScreenParent = adminScreen.getValue();

        this.endMultiplayerScreenCtrl = endMultiplayerScreen.getKey();
        this.endMultiplayerScreenParent = endMultiplayerScreen.getValue();


        // uncomment to disable the fullscreen popup
        //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.setTitle("Quizzzz!");
        primaryStage.setScene(new Scene(homeScreenParent));
        primaryStage.show();
        primaryStage.setFullScreen(true);
        homeScreenCtrl.refresh();
        checkDarkMode();

        // Sets proper exit code to window close request
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                stopListening();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /**
     * method for showing the home screen
     */
    public void showHomeScreen() {
        stopListening();
        primaryStage.getScene().setRoot(homeScreenParent);
        homeScreenCtrl.refresh();
        checkDarkMode();
    }

    /**
     * method for showing the settings screen
     */
    public void showSettingsScreen() {
        primaryStage.getScene().setRoot(settingsScreenParent);
        checkDarkMode();
    }

    /**
     * method for showing the laoding screen
     */
    public void showLoadingScreen(boolean multiPlayer) {
        primaryStage.getScene().setRoot(loadingScreenParent);
        checkDarkMode();
        loadingScreenCtrl.setMultiplayer(multiPlayer);
        loadingScreenCtrl.getCounter().setText("3");
        loadingScreenCtrl.countdown();
    }

    /**
     * method for showing the username screen
     */
    public void showUsernameScreen() {
        primaryStage.getScene().setRoot(usernameScreenParent);
        usernameScreenCtrl.setButtonText();
        checkDarkMode();
    }

    /**
     * method for showing the comparative question
     */
    public void showComparativeQuestionScreen(boolean multiplayer) {
        comparativeQuestionScreenCtrl.addTooltips();
        comparativeQuestionScreenCtrl.setMultiplayer(multiplayer);
        comparativeQuestionScreenCtrl.resetComparativeQuestionScreen();
        comparativeQuestionScreenCtrl.countdown();
        primaryStage.getScene().setRoot(comparativeQuestionScreenParent);
        checkDarkMode();
    }

    /**
     * method for showing an Estimation question
     */
    public void showEstimationQuestionScreen(boolean multiplayer) {
        estimationScreenCtrl.addTooltips();
        estimationScreenCtrl.setMultiplayer(multiplayer);
        estimationScreenCtrl.resetEstimationQuestion();
        estimationScreenCtrl.countdown();
        primaryStage.getScene().setRoot(estimationQuestionParent);
        checkDarkMode();
    }


    /**
     * method for showing the end screen
     */
    public void showEndScreen() {
        primaryStage.getScene().setRoot(endScreenParent);
        checkDarkMode();
    }

    /**
     * method for showing the help screen
     */
    public void showHelpScreen() {
        ((StackPane) primaryStage.getScene().getRoot()).getChildren().add(helpScreenParent);
        checkDarkMode();
    }

    /**
     * method for hiding the help screen
     */
    public void hideHelpScreen() {
        ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(helpScreenParent);
        checkDarkMode();
    }

    /**
     * method for showing the score change screen
     */
    public void showScoreChangeScreen(int pointsGained) {
        primaryStage.getScene().setRoot(scoreChangeScreenParent);
        checkDarkMode();
        showScore(pointsGained);
        scoreChangeScreenCtrl.countdown();
    }

    /**
     * method for showing admin screen
     */
    public void showAdminScreen() {
        primaryStage.getScene().setRoot(adminScreenParent);
        adminScreenCtrl.refresh();
        checkDarkMode();
    }


    /**
     * method for changing mode to opposite colour
     */
    public void checkDarkMode() {
        if (settingsScreenCtrl.getDarkMode()) {
            primaryStage.getScene().getRoot().setBlendMode(BlendMode.DIFFERENCE);
        } else {
            primaryStage.getScene().getRoot().setBlendMode(null);
        }
    }

    /**
     * Gets the origin of usernamescreen
     *
     * @return 1 - Singleplayer, 2 - Multiplayer
     */
    public int getUsernameOriginScreen() {
        return homeScreenCtrl.getUsernameOriginScreen();
    }

    /**
     * Sets the usernameOriginScreen
     *
     * @param usernameOriginScreen value
     *                             1 - Singleplayer
     *                             2 - Multiplayer
     */
    public void setUsernameOriginScreen(int usernameOriginScreen) {
        homeScreenCtrl.setUsernameOriginScreen(usernameOriginScreen);
    }

    /**
     * Resets the username text in usernamescreen
     */
    public void resetUserText() {
        usernameScreenCtrl.resetUserText();
    }

    /**
     * Checks for connection
     * Creates a new game with some number of questions
     */
    public void newSinglePlayerGame() {
        try {
            server.getRandomActivity();
            singlePlayerGame = new SinglePlayerGame(singlePlayerGameQuestions);
            //singlePlayerGame.addQuestion(question);

            setUsernameOriginScreen(1);
            showUsernameScreen();
        } catch (Exception e) {
            e.printStackTrace();
            showPopup(Alert.AlertType.ERROR, "Connection failed");
        }
    }

    /**
     * Similar to newSinglePlayerGame(), but requires a username
     *
     * @param username The username, used in the previous game
     */
    public void consecutiveSinglePlayerGame(String username) {
        try {
            server.getRandomActivity();

            singlePlayerGame = new SinglePlayerGame(singlePlayerGameQuestions, username);

            //skipping over the part where we ask for username
            showLoadingScreen(false);
        } catch (Exception e) {
            e.printStackTrace();
            showPopup(Alert.AlertType.ERROR, "Connection failed");
        }

    }

    /**
     * Shows the correct question screen based on the next question
     * <p>
     * Shows the end screen if next question isn't defined
     */
    public void nextQuestionScreen() {
        // check if there's a next question to show
        if (singlePlayerGame != null
                && singlePlayerGame.getQuestionNumber() <= singlePlayerGame.getMaxQuestions()
                + additionalQuestion()) {

            try {
                // get next question from the server
                Question newQuestion = server.getRandomQuestion();
                // loop until new question is not already in the list
                while (!singlePlayerGame.addQuestion(newQuestion)) {
                    newQuestion = server.getRandomQuestion();
                }

                Question question = singlePlayerGame.getQuestions().get(singlePlayerGame.getQuestionNumber() - 1);
                // check the question type
                if (question instanceof ComparativeQuestion
                        || question instanceof MCQuestion
                        || question instanceof EqualityQuestion) {

                    showComparativeQuestionScreen(false);
                    comparativeQuestionScreenCtrl.setQuestion(question);
                } else if (question instanceof EstimationQuestion) {
                    showEstimationQuestionScreen(false);
                    estimationScreenCtrl.setQuestion((EstimationQuestion) question);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showPopup(Alert.AlertType.ERROR, "Connection failed");
                showHomeScreen();
            }

        } else { // if no question to show display end screen
            endSinglePlayerGame();
        }
    }


    /**
     * Called to end the single player game
     * Shows the end screen and sends score to the server
     */
    public void endSinglePlayerGame() {
        //show End screen with score
        endScreenCtrl.setScoreLabel(singlePlayerGame.getPlayer().getScore());
        showEndScreen();

        //reset Question screen to prepare it for a new game
        comparativeQuestionScreenCtrl.finalResetComparativeQuestionScreen();
        estimationScreenCtrl.finalResetEstimationQuestion();

        //store player's end score
        try {
            server.postPlayer(singlePlayerGame.getPlayer());
        } catch (Exception e) {
            e.printStackTrace();
            showPopup(Alert.AlertType.ERROR, "Connection failed");
        }
    }

    /**
     * Shows the current score on the score change screen
     *
     * @param pointsGained number of points to be added to the score
     */
    public void showScore(int pointsGained) {
        int gained = pointsGained;
        int total = singlePlayerGame.getPlayer().getScore();
        int streak = singlePlayerGame.getPlayer().getStreak();
        scoreChangeScreenCtrl.setScoreLabels(gained, total, streak);
    }

    /**
     * Gets the username
     *
     * @return Username of current player of singlePlayerGame
     */
    public String getCurrentUsername() {
        return this.singlePlayerGame.getPlayer().getName();
    }

    /**
     * Gets the server url from the settings screen
     *
     * @return
     */
    public String getServerURL() {
        return this.settingsScreenCtrl.getServerURL();
    }

    /**
     * Shows an error popup message
     *
     * @param message to be shown on the popup
     */
    public void showPopup(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.initOwner(primaryStage);
        alert.setHeaderText(message);
        alert.show();
    }

    /**
     * Sets the server url and tests connection
     * @param URL Server URL
     */
    public void setServerURL(String URL){
        server.setServerURL(URL);
        try {
            server.getRandomActivity();
            showPopup(Alert.AlertType.INFORMATION, "Connected to " + server.getServerURL());
        } catch (Exception e) {
            showPopup(Alert.AlertType.ERROR, "Failed to connect to " + server.getServerURL());
        }
    }

    /**
     * Resets all attributes on the question screens
     *
     * Used when the game is left unfinished, because leave button on one screen
     * cannot use the method, which resets the other one
     */
    public void resetQuestionScreens() {
        estimationScreenCtrl.finalResetEstimationQuestion();
        comparativeQuestionScreenCtrl.finalResetComparativeQuestionScreen();
    }

    /**
     * @return true if jokerAdditionalQuestion has been used, false otherwise
     */
    public boolean jokerAdditionalQuestionIsUsed() {
        return jokerAdditionalQuestion;
    }

    /**
     * @return true if jokerRemoveOneAnswer has been used, false otherwise
     */
    public boolean jokerRemoveOneAnswerIsUsed() {
        return jokerRemoveOneAnswer;
    }

    /**
     * @return true if jokerDoublePoints has been used, false otherwise
     */
    public boolean jokerDoublePointsIsUsed() {
        return jokerDoublePoints;
    }

    /**
     * Sets the status of the AdditionalQuestion joker to "used"
     */
    public void useJokerAdditionalQuestion() {
        jokerAdditionalQuestion = true;
    }

    /**
     * Sets the status of the RemoveOneAnswer joker to "used"
     */
    public void useJokerRemoveOneAnswer() {
        jokerRemoveOneAnswer= true;
    }

    /**
     * Sets the status of the DoublePoints joker to "used"
     */
    public void useJokerDoublePoints() {
        jokerDoublePoints = true;
    }

    /**
     * Sets the status of all jokers to "unused"
     * To be used only when a game ends (or is left before the end)
     */
    public void resetJokers() {
        jokerAdditionalQuestion = false;
        jokerRemoveOneAnswer= false;
        jokerDoublePoints = false;
    }

    /**
     * Adds question to game maxquestions (because joker skips a question)
     * @return 1 - If the joker "Change current question" is used,
     *         in order to add a question to the maximum number of questions in the game;
     *         0 - Otherwise.
     */
    public int additionalQuestion() {
        if(jokerAdditionalQuestionIsUsed()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * ----------------------------------------- MULTIPLAYER CODE AHEAD ------------------------------------------------
     */

    /**
     * multiplayer variables
     */
    @Getter @Setter
    private MultiPlayerGame multiPlayerGame;
    private boolean MultiplayerStarted;
    @Getter @Setter
    private GameUpdatesPacket packet;
    @Getter @Setter
    private Player player;
    private int pointsGained;
    @Getter @Setter
    private int gameID = -1;

    /**
     * method for showing the waiting room
     */
    public void showWaitingRoom() {
        primaryStage.getScene().setRoot(waitingRoomParent);
        try {
            startListening(gameID);
        } catch (Exception e){
            showPopup(Alert.AlertType.ERROR, "Connection failed");
            showHomeScreen();
        }
        MultiplayerStarted = false;
        multiPlayerGame = null;
        packet = new GameUpdatesPacket();
        waitingRoomCtrl.refresh();
        checkDarkMode();
    }

    /**
     * start listening for updates
     * if questionnumber is wrong it updates it
     * if current screen is wrong is forces the player to the correct screen
     */
    public void startListening(int id) {
        server.registerUpdates(id, c -> {
            Platform.runLater(() -> {
                if (packet != null) {
                    if (packet.getHashListPlayers() != c.getHashListPlayers()) {
                        updatePlayerList();
                    }
                    // check if multiplayer has started and the screen or question number has changed
                    if (MultiplayerStarted && (c.getCurrentScreen() != packet.getCurrentScreen() || c.getQuestionNumber() != packet.getQuestionNumber())) {
                        multiPlayerGame.setQuestionNumber(c.getQuestionNumber());
                        changeScreenMultiplayer(c);
                    }
                    // Check if you are in waiting room and game has been started
                    if (primaryStage.getScene().getRoot().equals(waitingRoomParent) && !MultiplayerStarted && !"WAITINGROOM".equals(c.getCurrentScreen())) {
                        MultiplayerStarted = true;
                        changeScreenMultiplayer(c);
                        try {
                            multiPlayerGame = server.getMultiplayerGame(gameID);
                        } catch (Exception e) {
                            showPopup(Alert.AlertType.ERROR, "Connection failed");
                            showHomeScreen();
                        }
                    }
                    packet = c;
                }
            });
        });
    }

    private void updatePlayerList(){
        try {
            if(!MultiplayerStarted){
                waitingRoomCtrl.refresh();
            } else {
                List<Player> playas = server.getPlayersMultiplayer(gameID);
                multiPlayerGame.setPlayers(playas);
            }
        } catch (Exception e) {
            showPopup(Alert.AlertType.ERROR, "Connection failed");
            showHomeScreen();
        }
    }

    /**
     * stops the thread used for long polling
     */
    public void stopListening(){
        try {
            if(!MultiplayerStarted){
                server.removePlayerWaitingRoom(player);
            }
        } catch (Exception e){
            // do nothing if can't remove player, means there's nothing to remove
        }
        try {
            server.stop();
        } catch (Exception e){
            // do nothing if can't stop process, means there's nothing to stop
        }

        MultiplayerStarted = false;
        multiPlayerGame = null;
        packet = null;
        player = null;
        gameID = -1;

        resetQuestionScreens();
    }

    /**
     * starts the multiplayer game
     */
    public void startMultiplayer() {
        try {
            boolean started = server.startMultiplayer();
            if(!started){
                showPopup(Alert.AlertType.ERROR, "Server is still generating questions, try again in a moment");
            }
        } catch(Exception e){
            showPopup(Alert.AlertType.ERROR, "Connection failed");
            showHomeScreen();
        }
    }

    /**
     * updates the screens
     * needs to be its own function because of cyclomatic complectity
     *
     * @param packet the packet with the updates
     */
    public void changeScreenMultiplayer(GameUpdatesPacket packet) {

        comparativeQuestionScreenCtrl.setMultiplayer(true);
        comparativeQuestionScreenCtrl.resetComparativeQuestionScreen();
        estimationScreenCtrl.setMultiplayer(true);
        estimationScreenCtrl.resetEstimationQuestion();

        if (packet.getCurrentScreen().equals("QUESTION")) {
            showQuestionMultiplayer(packet);
        } else if (packet.getCurrentScreen().equals("LEADERBOARD")) {
            scoreChangeMultiplayerCtrl.setTableLeaderboard(multiPlayerGame.getPlayers());
            scoreChangeMultiplayerCtrl.setScoreLabels(pointsGained, player.getScore(), player.getStreak());
            showLeaderBoard();
        } else if (packet.getCurrentScreen().equals("ENDSCREEN")) {
            endMultiplayerScreenCtrl.setTableLeaderboard(multiPlayerGame.getPlayers());
            endMultiplayerScreenCtrl.setScoreLabel(player.getScore());
            endMultiplayerScreenCtrl.setPlayerName(player.getName());
            showEndMultiplayerScreen();
        } else if (packet.getCurrentScreen().equals("LOADING SCREEN")){
            showLoadingScreen(true);
        }
    }

    /**
     *
     */
    public void showQuestionMultiplayer(GameUpdatesPacket packet) {
        Question question = multiPlayerGame.getQuestions().get(packet.getQuestionNumber());
        // check the question type
        if (question instanceof ComparativeQuestion
                || question instanceof MCQuestion
                || question instanceof EqualityQuestion) {

            showComparativeQuestionScreen(true);
            comparativeQuestionScreenCtrl.setQuestion(question);
        } else if (question instanceof EstimationQuestion) {
            showEstimationQuestionScreen(true);
            estimationScreenCtrl.setQuestion((EstimationQuestion) question);
        }

    }

    /**
     * Adds score to the multiplayer player
     * @param timeWhenAnswered
     * @param guessQuestionRate
     */
    public void addScoreMultiplayer(int timeWhenAnswered, double guessQuestionRate){
        pointsGained = multiPlayerGame.addPointsForPlayer(timeWhenAnswered, guessQuestionRate, player);
        if(pointsGained > 0){
            try {
                server.postScore(player, gameID);
            } catch (Exception e) {
                showPopup(Alert.AlertType.ERROR, "Connection failed");
                showHomeScreen();
            }
        }
    }

    /**
     * Checks how many points the player will get without adding them
     * @param timeWhenAnswered
     * @return
     */
    public int getPointsToBeAdded(int timeWhenAnswered) {
        return multiPlayerGame.getPointsToBeAdded(timeWhenAnswered, this.player);
    }

    /**
     * Shows the leaderboard after each question
     */
    public void showLeaderBoard() {
        primaryStage.getScene().setRoot(scoreChangeMultiplayerParent);
        checkDarkMode();
    }

    /**
     * method for showing the multiplayer end screen
     */
    public void showEndMultiplayerScreen() {
        primaryStage.getScene().setRoot(endMultiplayerScreenParent);
        checkDarkMode();
        stopListening();
    }
}

