package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {

    /**
     * Gets a number of random activities
     * @param limit how many distinct activities do you want
     * @return Optional of an Activity list
     */
    @Query(
        nativeQuery=true,
        value="SELECT * FROM Activity ORDER BY random() LIMIT ?1")
    Optional<List<Activity>> getRandomActivities(int limit);

    /**
     * Calculates the total number of distinct values all the Activities have for Consumption
     * @return The number of distinct values for Consumption
     */
    @Query(
            nativeQuery=true,
            value="SELECT COUNT(DISTINCT CONSUMPTION_IN_WH) FROM Activity")
    int numberDistinctConsumptions();

    /**
     * Randomly selects from the database a certain number of activities, such that
     * they all have a different value for their consumption, which is in the interval (floor, ceil).
     * None of the activities should have the same consumption as the pivot.
     * If there aren't enough activities, which fulfill those requirements, a shorter list than needed is returned.
     *
     * @param size A limit for the number of IDs to be returned
     * @param floor Minimum consumption the activities, whose IDs are returned, should have
     * @param ceil Maximum consumption the activities, whose IDs are returned, should have
     * @param pivotConsumption Consumption of the pivot none of the returned activities should have
     * @return The IDs of the needed number of activities
     */
    @Query(
            nativeQuery=true,
            value="SELECT ID FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY CONSUMPTION_IN_WH) AS temp, FROM (SELECT * FROM Activity ORDER BY random() )) WHERE temp = 1 AND CONSUMPTION_IN_WH < ?3 AND CONSUMPTION_IN_WH > ?2 AND CONSUMPTION_IN_WH != ?4 LIMIT ?1")
    Optional<List<String>> activitiesWithSpecifiedConsumption(int size, int floor, int ceil, long pivotConsumption);

    /**
     * Gets a list of activities in random order which have another activity with the same consumption
     * @param limit Upper limit to number of activities returned
     * @return List of activities which don't have a unique consumption
     */
    @Query(
            nativeQuery = true,
            value="SELECT a.id, a.consumption_in_wh, a.image_path, a.source, a.title FROM Activity as a, Activity as b WHERE a.consumption_in_wh = b.consumption_in_wh AND a.id != b.id ORDER BY RANDOM() LIMIT ?1")
    Optional<List<Activity>> nonUniqueActivities(int limit);

    /**
     * Gets a list of activities in random order which have the same consumption but not the same as ID as specified
     * @param consumption The consumption the activities should have
     * @param id The ID the activities shouldn't have
     * @param limit Upper limit to number of activities returned
     * @return List of activities
     */
    @Query(
        nativeQuery = true,
        value="SELECT * FROM Activity WHERE consumption_in_wh = ?1 AND id != ?2 ORDER BY RANDOM() LIMIT ?3")
    Optional<List<Activity>> sameConsumptionActivities(long consumption, String id, int limit);
}
