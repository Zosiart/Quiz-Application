package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SettingsScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public boolean isLightMode;

    /**
     * Creates a new screen with injections
     *
     * @param server   ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public SettingsScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    private Button darkMode;

    @FXML
    private TextField inputServerURLField;

    @FXML
    void toggleDarkMode() {
        isLightMode = !isLightMode;
        if (isLightMode)
            darkMode.setText("Light Mode");
        else {
            darkMode.setText("Dark Mode");
        }
        mainCtrl.checkDarkMode();
    }

    @FXML
    void connectToServer() {
        mainCtrl.setServerURL(getServerURL());
    }

    /**
     * Shows the help screen
     */
    @FXML
    public void showHelpScreen() {
        mainCtrl.showHelpScreen();
    }

    /**
     * Get dark mode
     *
     * @return true if is in light mode
     */
    public boolean getDarkMode() {
        return isLightMode;
    }

    /**
     * exit back to home screen
     */
    public void exit() {
        mainCtrl.showHomeScreen();
    }

    /**
     * Show admin screen
     */
    public void showAdminScreen() {
        mainCtrl.showAdminScreen();
    }

    /**
     * Gets the server url from the input field
     *
     * @return String server url
     */
    public String getServerURL() {
        return inputServerURLField.getText();
    }
}
