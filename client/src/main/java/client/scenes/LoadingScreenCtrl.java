package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Setter @Getter
    private boolean multiplayer;

    private Timer timer = new Timer();

    @FXML
    private Button back;

    @Getter
    @Setter
    @FXML
    private Label counter;

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public LoadingScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    void back(ActionEvent event) {
        mainCtrl.resetUserText();
        mainCtrl.showHomeScreen();
        timer.cancel();
        timer = new Timer();
        counter.setText("3");
        multiplayer = false;
    }

    /**
     * Starts the timer for starting the game
     * Lasts for 3 seconds
     * Displays seconds on the screen
     */
    public void countdown() {
        if(!multiplayer){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        mainCtrl.getSinglePlayerGame().addQuestion(mainCtrl.getServer().getRandomQuestion());
                    } catch (Exception e){
                        mainCtrl.showPopup(Alert.AlertType.ERROR, "Connection failed");
                        mainCtrl.showHomeScreen();
                    }
                }
            }).start();
        }

        TimerTask task = new TimerTask() {
            int second = 2;

            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(second == 0) {
                            cancel();
                            if(!multiplayer){
                                mainCtrl.nextQuestionScreen();
                                counter.setText("3");
                            }
                        } else {
                            counter.setText(String.valueOf(second--));
                        }
                    }
                });
            }
        };

        timer.schedule(task, 1000, 1000);
    }

}

