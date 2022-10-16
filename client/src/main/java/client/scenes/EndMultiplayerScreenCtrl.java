package client.scenes;

import com.google.inject.Inject;
import commons.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Setter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EndMultiplayerScreenCtrl implements Initializable {

    private final MainCtrl mainCtrl;

    private ObservableList<Player> players;
    @Setter
    private String playerName;

    @FXML
    private Button goToHomeScreen;

    @FXML
    private Button playAgain;

    @FXML
    private TableView<Player> leaderboard;

    @FXML
    private TableColumn<Player, String> playerPosition;

    @FXML
    private TableColumn<Player, String> playerScore;

    @FXML
    private TableColumn<Player, String> playerUsername;

    @FXML
    private Label score;

    /**
     * Creates a new screen with injections
     * @param mainCtrl Main Controller
     */
    @Inject
    public EndMultiplayerScreenCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    void goToHomeScreen(ActionEvent event) {
        mainCtrl.showHomeScreen();
    }

    @FXML
    void playAgain(ActionEvent event) {
        Player player = new Player(playerName);
        Integer gameId = null;
        try {
            gameId = mainCtrl.getServer().addPlayerWaitingRoom(player);
            mainCtrl.setPlayer(player);
        } catch (Exception e){
            mainCtrl.showPopup(Alert.AlertType.ERROR, "Connection failed");
        }

        if(gameId == null){
            mainCtrl.setUsernameOriginScreen(2);
            mainCtrl.showUsernameScreen();
            mainCtrl.getUsernameScreenCtrl().getInputUsernameField().setText(player.getName());
            mainCtrl.getUsernameScreenCtrl().setUsernameButtonClicked();
        }
        else {
            mainCtrl.setGameID(gameId);
            mainCtrl.showWaitingRoom();
        }
    }

    /**
     * Sets the score label text
     * @param score int value
     */
    public void setScoreLabel(int score){
        this.score.setText(String.valueOf(score));
    }

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
