package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

public class ScoreChangeScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label scoreGained;
    @FXML
    private Label scoreTotal;
    @FXML
    private Label scoreStreak;
    @FXML
    private Button leave;

    private Timer timer = new Timer();

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public ScoreChangeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Sets score change labels
     * @param gained Number of points gained
     * @param total Number of total points
     * @param streak How many questions in a row were correct
     */
    public void setScoreLabels(int gained, int total, int streak ){
        scoreGained.setText("+" + gained);
        scoreTotal.setText("Score: " + total);
        scoreStreak.setText("Streak: " + streak);
    }

    /**
     * Goes back to the home screen
     */
    public void exit() {
        mainCtrl.showHomeScreen();
        timer.cancel();
        timer = new Timer();
        mainCtrl.resetQuestionScreens();
    }

    /**
     * Starts the countdown to move to the next screen
     */
    public void countdown() {

        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                        mainCtrl.nextQuestionScreen();
                    }
                });
            }
        };

        timer.schedule(task, 3000);
    }


}
