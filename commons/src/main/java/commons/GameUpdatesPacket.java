package commons;

import lombok.Data;

@Data
public class GameUpdatesPacket {

    int hashListPlayers;
    String currentScreen;
    int questionNumber;

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public GameUpdatesPacket() {
    }

    /**
     * Constructor with all parameters
     * @param hashListPlayers Hashcode of the list of players in the game / waiting area
     * @param currentScreen The name of the current screen
     * @param questionNumber The question number, which has been displayed lastly
     */
    public GameUpdatesPacket(int hashListPlayers, String currentScreen, int questionNumber) {
        this.hashListPlayers = hashListPlayers;
        this.currentScreen = currentScreen;
        this.questionNumber = questionNumber;
    }
}
