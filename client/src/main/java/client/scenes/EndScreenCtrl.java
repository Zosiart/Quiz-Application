package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EndScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Button playAgain;
    @FXML
    private Button goToHomeScreen;
    @FXML
    private Label scoreLabel;

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public EndScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Returns to the home screen
     */
    public void goToHomeScreen() {
        mainCtrl.showHomeScreen();
    }

    /**
     * Runs when play again is pressed
     * Starts a new single player game with old username
     */
    public void playAgain() {
        if (mainCtrl.getUsernameOriginScreen() == 1) {
            mainCtrl.consecutiveSinglePlayerGame(mainCtrl.getCurrentUsername());
        } else {
            mainCtrl.showWaitingRoom();
        }
    }

    /**
     * Sets the score label text
     * @param score int value
     */
    public void setScoreLabel(int score){
        scoreLabel.setText("Your score is " + score);
    }
}
