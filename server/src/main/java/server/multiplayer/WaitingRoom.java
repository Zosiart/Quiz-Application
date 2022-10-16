package server.multiplayer;

import commons.MultiPlayerGame;
import commons.Player;
import commons.questions.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A Waiting room class that will store the players who are waiting to enter a game. The mechanics of this
 * class can be described by a toilet analogy. All the players once the multiplayer is pressed and dumped into a
 * so-called toilet and once a player presses start a MultiplayerGame class is generated using the attributes
 * of the Waiting room and a multiplayer game is started.
 */
@Data
public class WaitingRoom {
    private int multiplayerGameID;      // a waiting room id which is incremented every time a new game is started (flush count)
    private List<Player> players;       // list of players
    private List<Question> questions;   // list of questions
    private int maxNumberOfQuestions;   // maximal number of questions
    /**
     * no args constructor
     */
    public WaitingRoom(){}

    /**
     * constructor with a set id (it should be set to zero)
     * @param maxNumberOfQuestions maximal number of questions
     * @param players list of players
     * @param questions list of questions generated for multiplayer games
     */
    public WaitingRoom(List<Player> players,  List<Question> questions, int maxNumberOfQuestions){
        this.players = players;
        this.questions = questions;
        this.multiplayerGameID = 0;
        this.maxNumberOfQuestions = maxNumberOfQuestions; // make min number of questions 5, max number 25
    }

    /**
     * @param maxNumberOfQuestions maximal number of questions
     * @param questions list of questions generated for multiplayer games
     */
    public WaitingRoom(List<Question> questions, int maxNumberOfQuestions){
        this.players = new ArrayList<>();
        this.questions = questions;
        this.multiplayerGameID = 0;
        this.maxNumberOfQuestions = maxNumberOfQuestions; // make min number of questions 5, max number 25
    }

    /**
     * Set the ID of the next multiplayer game
     * @param multiplayerGameID the ID that will be set
     *               this parameter cannot be negative
     */
    public void setMultiplayerGameID(int multiplayerGameID) {
        this.multiplayerGameID = Math.max(multiplayerGameID, 0);
    }

    /**
     * Adds a question to the list. Checks for duplicates
     * @param question to be added
     * @return true if question was added, false otherwise
     */
    public boolean addQuestion(Question question){
        if(questions.contains(question) || question == null)
            return false;
        return questions.add(question);
    }

    /**
     * Adding a player to a waiting room
     * @return true if a players name is unique else returns false
     * @param player player to be added
     */
    public boolean addPlayerToWaitingRoom(Player player){
        if(player == null
            || player.getName() == null
            || player.getName().isEmpty()
            || this.players.contains(player)) return false;
        return this.players.add(player);
    }

    /**
     * This method is used in case a player leaves the waiting room
     * and the player list has to reflect this change
     * @param player player to be removed
     * @return true if the player was removed successfully
     *          false if the player could not be removed or
     *          the player was not in the player list
     */
    public boolean removePlayerFromWaitingRoom(Player player) {
        return this.players.remove(player);
    }

    /**
     * Gets the number of players in the waiting room
     * @return the size of the list of players
     */
    public int getNumberOfPlayers() {
        return this.players.size();
    }

    /**
     * Flushing a WaitingRoom. This will transfer all the players to a MultiplayerGame class
     * and a ll the players
     * @return new Multiplayer game with an ID and ist of players and question
     */
    public MultiPlayerGame flushWaitingRoom(){
        MultiPlayerGame game = new MultiPlayerGame(multiplayerGameID, players, questions);
        questions = new ArrayList<>();
        players = new ArrayList<>();
        multiplayerGameID++;
        return game;
    }

}
