package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WaitingRoomCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private VBox listOfPlayers;

    @FXML
    private Button back;

    @FXML
    private Button startGame;
    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public WaitingRoomCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Resets the username screen text
     * Goes back to the home screen
     */
    public void back() {
        mainCtrl.resetUserText();
        mainCtrl.showHomeScreen();
    }

    private ObservableList<Player> players;

    @FXML
    private TableView<Player> playerTable;
    @FXML
    private TableColumn<Player, String> position;
    @FXML
    private TableColumn<Player, String> name;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
        name.setCellFactory(e -> new TableCell<Player, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null || empty)
                    setText(null);
                else
                    setText(item);
            }
        });

        position.setCellFactory(position -> new TableCell<Player, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(String.valueOf(getIndex() + 1));
            }
        });

        // To not allow users to resize column widths
        // or reorder columns (switch column places)
        position.setReorderable(false);
        position.setResizable(false);

        name.setReorderable(false);
        name.setResizable(false);
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
            List<Player> playerList = mainCtrl.getServer().getPlayersInWaitingRoom();
            players = FXCollections.observableList(playerList);
            playerTable.setItems(players);
        } catch (Exception e) {
            mainCtrl.showPopup(Alert.AlertType.ERROR, "Lost connection to the server");
            mainCtrl.showHomeScreen();
        }
    }

    /**
     * Starts the gam when you click start
     */
    public void startGame() {
        mainCtrl.startMultiplayer();
    }
}
