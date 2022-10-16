package server.database;

import commons.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Gets a number of top players by score
     * @param limit how many distinct players do you want
     * @return Optional of a Player list
     */
    @Query(
            nativeQuery=true,
            value="SELECT * FROM Player a ORDER BY score DESC LIMIT ?1")
    Optional<List<Player>> getTopPlayers(int limit);
}
