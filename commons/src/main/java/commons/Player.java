package commons;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

/**
 * Activity class that is stored in the database
 *
 * Stores an activities
 */
@ToString
@Entity
public class Player implements Comparable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;

    @Getter
    private String name;
    @Getter
    private Integer score;
    @Getter
    @Transient
    private int streak = 0; // streak field for getting more points when you answer questions correctly in a row


    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public Player(){ }

    /**
     * Creates new player with name
     * @param name
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    /**
     * Creates new player with name and score
     * @param name
     * @param score
     */
    public Player(String name, Integer score) {
        this.name = name;
        this.score = score >= 0 ? score : 0; // check if score is smaller than 0
    }

    /**
     * Creates new player with id, name and score
     * @param id
     * @param name
     * @param score
     */
    public Player(Long id, String name, Integer score) {
        this.id = id;
        this.name = name;
        this.score = score >= 0 ? score : 0; // check if score is smaller than 0
    }

    /**
     * Sets name
     * @param name
     * @throws IllegalArgumentException if name is empty
     */
    public void setName(String name) {
        this.name = name;
        if(name.length() > 0)
            this.name = name;
        else throw new IllegalArgumentException("Name cannot be empty");
    }

    /**
     * Sets score
     * @param score
     * @throws IllegalArgumentException if score is negative
     */
    public void setScore(Integer score) {
        this.score = score;
        if(score >= 0)
            this.score = score;
        else throw new IllegalArgumentException("Score cannot be negative");
    }

    /**
     * Comparator for PLayer in descending order
     * @param otherPlayer
     */
    @Override
    public int compareTo(Object otherPlayer) {
        int compareScore = ((Player) otherPlayer).getScore();
        return compareScore - this.score;
    }

    /**
     * Sets streak
     * @param streak
     * @throws IllegalArgumentException if streak is negative
     */
    public void setStreak(int streak) {
        this.streak = streak;
        if(streak >= 0)
            this.streak = streak;
        else throw new IllegalArgumentException("Streak cannot be negative");
    }

    /**
     * resets streak to 0
     */
    public void resetStreak() { streak = 0;}

    /**
     * Increments streak
     */
    public void incrementStreak() { streak++;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) && Objects.equals(name, player.name) && Objects.equals(score, player.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, score);
    }
}

