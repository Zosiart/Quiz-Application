package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreChangeMultiplayerCtrl implements Initializable {

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

    /**
     * Creates a new screen with injections
     *
     * @param server   ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public ScoreChangeMultiplayerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Sets score change labels
     *
     * @param gained Number of points gained
     * @param total  Number of total points
     * @param streak How many questions in a row were correct
     */
    public void setScoreLabels(int gained, int total, int streak) {
        scoreGained.setText("+" + gained);
        scoreTotal.setText("Score: " + total);
        scoreStreak.setText("Streak: " + streak);
    }

    /**
     * Goes back to the home screen
     */
    public void exit() {
        mainCtrl.showHomeScreen();
    }

    private ObservableList<Player> players;

    @FXML
    private TableView<Player> leaderboard;
    @FXML
    private TableColumn<Player, String> playerPosition;
    @FXML
    private TableColumn<Player, String> playerUsername;
    @FXML
    private TableColumn<Player, String> playerScore;

    /**
     * Used to refresh the leaderboard entries
     */
    public void setTableLeaderboard(List<Player> playerList) {
        players = FXCollections.observableList(playerList);
        leaderboard.setItems(players);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playerUsername.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
        playerUsername.setCellFactory(e -> new TableCell<Player, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else
                    setText(item);
                switch (getIndex()) {
                    case 0:
                        this.setStyle("-fx-background-color: gold;");
                        break;
                    case 1:
                        this.setStyle("-fx-background-color: silver;");
                        break;
                    case 2:
                        this.setStyle("-fx-background-color: CD7F32;");
                        break;
                    default:
                }
            }
        });

        playerScore.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getScore().toString()));
        playerScore.setCellFactory(e -> new TableCell<Player, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else
                    setText(item);
                switch (getIndex()) {
                    case 0:
                        this.setStyle("-fx-background-color: gold;");
                        break;
                    case 1:
                        this.setStyle("-fx-background-color: silver;");
                        break;
                    case 2:
                        this.setStyle("-fx-background-color: CD7F32;");
                        break;
                    default:
                }
            }
        });

        playerPosition.setCellFactory(position -> new TableCell<Player, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(String.valueOf(getIndex() + 1));
                switch (getIndex()) {
                    case 0:
                        this.setStyle("-fx-background-color: gold;");
                        break;
                    case 1:
                        this.setStyle("-fx-background-color: silver;");
                        break;
                    case 2:
                        this.setStyle("-fx-background-color: CD7F32;");
                        break;
                    default:
                }
            }
        });
    }
}
