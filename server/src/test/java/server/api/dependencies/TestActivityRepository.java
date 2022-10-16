package server.api.dependencies;

import commons.Activity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ActivityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ActivityRepository implementation for testing
 * Stores Activities in an ArrayList
 */
public class TestActivityRepository implements ActivityRepository {

    public List<Activity> activities = new ArrayList<>();
    TestRandom random = new TestRandom();

    @Override
    public List<Activity> findAll() {
        return activities;
    }

    @Override
    public List<Activity> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Activity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Activity> findAllById(Iterable<String> longs) {
        return null;
    }

    @Override
    public long count() {
        return activities.size();
    }

    @Override
    public void deleteById(String id) {
        activities.remove(findById(id).get());
    }

    @Override
    public void delete(Activity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Activity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Activity> S save(S activity) {
        activities.add(activity);
        return activity;
    }

    @Override
    public <S extends Activity> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Activity> findById(String id) {
        for(Activity a : activities){
            if(a.getId().equals(id)){
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Activity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Activity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Activity getOne(String aLong) {
        return null;
    }

    @Override
    public Activity getById(String aLong) {
        return null;
    }

    @Override
    public <S extends Activity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Activity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Activity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Activity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Activity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Optional<List<Activity>> getRandomActivities(int limit) {
        List<Activity> result = new ArrayList<>();
        for(int i = 0; i < limit; i++){
            result.add(findById(String.valueOf( random.nextLong() % activities.size() + 1 )).get());
        }
        return Optional.of(result);
    }

    @Override
    public int numberDistinctConsumptions() {
        List<Long> distinctConsumptions = activities
                                        .stream()
                                        .map(Activity::getConsumption_in_wh)
                                        .distinct()
                                        .collect(Collectors.toList());
        return distinctConsumptions.size();
    }

    @Override
    public Optional<List<String>> activitiesWithSpecifiedConsumption(int size, int floor, int ceil, long pivotConsumption) {
        // filter only the activities, whose consumption is within the specified range
        List<Activity> activitiesSuitableConsumptions = activities
                .stream()
                .filter(activity -> activity.getConsumption_in_wh() > floor && activity.getConsumption_in_wh() < ceil && activity.getConsumption_in_wh() != pivotConsumption)
                .collect(Collectors.toList());

        if(activitiesSuitableConsumptions.isEmpty()) return Optional.empty();
        List<String> result = new ArrayList<>();
        for(Activity a : activitiesSuitableConsumptions){
            result.add(a.getId());
        }
        return Optional.of(result);
    }

    @Override
    public Optional<List<Activity>> nonUniqueActivities(int limit){
        List<Activity> result = new ArrayList<>();
        for(Activity a : activities){
            for(Activity b : activities){
                if(!a.equals(b)){
                    result.add(a);
                }
                if(result.size() >= limit){
                    break;
                }
            }
            if(result.size() >= limit){
                break;
            }
        }
        return Optional.of(result);
    }

    @Override
    public Optional<List<Activity>> sameConsumptionActivities(long consumption, String id, int limit){
        List<Activity> result = new ArrayList<>();
        for(Activity a : activities){
            if(a.getConsumption_in_wh() == consumption && !a.getId().equals(id)){
                result.add(a);
            }
            if(result.size() >= limit){
                break;
            }
        }
        return Optional.of(result);
    }
}
