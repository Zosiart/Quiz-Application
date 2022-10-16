package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class HelpScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public HelpScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Hides the help screen
     */
    public void back() {
        mainCtrl.hideHelpScreen();
    }
}
