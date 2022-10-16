package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Controller used as an intermediate between controllers for all
 * scenes/windows
 * <p>
 * CURRENTLY, USED FOR HOME SCREEN/SCENE ONLY
 */
public class HomeScreenCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    public boolean isLightMode;

    @Getter
    public int usernameOriginScreen;

    /**
     * Creates a new screen with injections
     *
     * @param server   ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isLightMode = true;
        this.usernameOriginScreen = 0; //still on home screen
    }

    /**
     * Sets the origin of the username selection screen
     *
     * @param usernameOriginScreen 1 if going to single player
     *                             2 if going to multiplayer
     */
    public void setUsernameOriginScreen(int usernameOriginScreen) {
        if (usernameOriginScreen == 0 ||
                usernameOriginScreen == 1 ||
                usernameOriginScreen == 2)
            this.usernameOriginScreen = usernameOriginScreen;
    }


    /**
     * This method transfers the user to the settings screen
     * where he/she can switch to dark mode, read the help page,
     * enter the room URL or go to admin panel
     *
     * @param event
     */
    @FXML
    void goToSettings(ActionEvent event) {
        mainCtrl.showSettingsScreen();
    }


    /**
     * Tries to get a question from the server
     * If succeeds connect create a new singlePlayerGame and go to the username screen
     * <p>
     * TODO: popup
     */
    @FXML
    public void showUsernameScreenSingle() {
        mainCtrl.newSinglePlayerGame();
    }

    /**
     * Run when pressed multiplayer
     * Sends you to the username selection screen
     * Gives an error popup when connection to the server not possible
     */
    @FXML
    public void showUsernameScreenMulti() {
        try {
            mainCtrl.getServer().getRandomActivity();
            mainCtrl.setUsernameOriginScreen(2);
            mainCtrl.showUsernameScreen();
        } catch (Exception e) {
            e.printStackTrace();
            mainCtrl.showPopup(Alert.AlertType.ERROR, "Connection to the server failed");
        }
    }

    @FXML
    void exitApp(ActionEvent event) {
        // to fully terminate the client process
        mainCtrl.stopListening();
        Platform.exit();
        System.exit(0);
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

        // To not allow users to resize column widths
        // or reorder columns (switch column places)
        playerPosition.setReorderable(false);
        playerPosition.setResizable(false);

        playerUsername.setReorderable(false);
        playerUsername.setResizable(false);

        playerScore.setReorderable(false);
        playerScore.setResizable(false);

    }

    /**
     * Used to refresh the leaderboard entries
     */
    @FXML
    public void refresh() {
        try {
            // because of the getLeaderPlayers(10) method, the
            // leaderboard needs no sorting, as the list of players
            // is returned already sorted through the query
            List<Player> playerList = mainCtrl.getServer().getLeaderPlayers(10);
            players = FXCollections.observableList(playerList);
            leaderboard.setItems(players);
        } catch (Exception e) {
            System.err.println("Failed to connect to the server");
        }
    }

}
