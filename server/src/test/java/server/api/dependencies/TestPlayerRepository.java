package server.api.dependencies;

import commons.Player;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.PlayerRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * ActivityRepository implementation for testing
 * Stores Activities in an ArrayList
 */
public class TestPlayerRepository implements PlayerRepository {

    public List<Player> players = new ArrayList<>();


    @Override
    public List<Player> findAll() {
        return players;
    }

    @Override
    public List<Player> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Player> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Player> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return players.size();
    }

    @Override
    public void deleteById(Long id) {
        players.remove(findById(id).get());
    }

    @Override
    public void delete(Player entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Player> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Player> S save(S player) {
        if(player.getId() == null){
            player.setId((long) players.size() + 1);
            players.add(player);
            return player;
        }

        Optional<Player> dbPlayer = findById(player.getId());
        if(dbPlayer.isPresent()){
            Player dbP = dbPlayer.get();
            dbP.setScore(player.getScore());
            dbP.setName(player.getName());

            return (S) dbP;
        }
        players.add(player);
        return player;
    }

    @Override
    public <S extends Player> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Player> findById(Long id) {
        for(Player p : players){
            if(p.getId() == id){
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Player> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Player> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Player> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Player getOne(Long aLong) {
        return null;
    }

    @Override
    public Player getById(Long id) {
        return null;
    }

    @Override
    public <S extends Player> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Player> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Player> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Player> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Player, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Optional<List<Player>> getTopPlayers(int limit) {
        List<Player> result = new ArrayList<>(players);
        Collections.sort(result);
        if (limit > result.size()){
            return Optional.of(result);
        }
        List<Player> resultLimit = new ArrayList<>();
        for(int i = 0; i < limit; i++){
            resultLimit.add(result.get(i));
        }
        return Optional.of(resultLimit);
    }
}
