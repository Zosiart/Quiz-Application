package server.api;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.api.dependencies.TestActivityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for activity controller
 */
class ActivityControllerTest {

    private TestActivityRepository repo;
    private Random random;
    private ActivityController act;

    private List<Activity> activities;

    /**
     * Sets up a new activity controller with testing dependencies
     * Runs before every test
     */
    @BeforeEach
    public void setup(){
        repo = new TestActivityRepository();
        act = new ActivityController(repo);

        activities = List.of(
                new Activity("1", "image_a","a", 1L, "a"),
                new Activity("2", "image_b","b", 2L, "b"),
                new Activity("3", "image_c","c", 3L, "c")
        );
    }

    /**
     * Test for getAll with many activities
     */
    @Test
    public void getAllTestMany(){
        repo.activities.addAll(activities);
        assertEquals(activities, act.getAll());
    }

    /**
     * Test for getAll with no activities
     */
    @Test
    public void getAllTestEmpty(){
        List<Activity> expected = new ArrayList<>();
        assertEquals(expected, act.getAll());
    }

    /**
     * Test for getActivityById
     */
    @Test
    public void getActivityByIdTest(){
        repo.activities.addAll(activities);
        assertEquals(activities.get(0), act.getActivityById("1").getBody());
        assertEquals(activities.get(1), act.getActivityById("2").getBody());
        assertEquals(activities.get(2), act.getActivityById("3").getBody());
    }

    /**
     * Test for getActivityById with invalid id
     */
    @Test
    public void getActivityByIdTestInvalidId(){
        assertEquals(ResponseEntity.badRequest().build(), act.getActivityById("8"));
    }

    /**
     * Test for Add activity with invalid title
     */
    @Test
    public void addActivityTestTitle(){
        Activity added = new Activity("1","image_a",null, 1L, "a");
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added));

        Activity added2 = new Activity("1","image_a","", 1L, "a");
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added2));

        String title = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" // 50 characters per line
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // total 300 characters
        Activity added3 = new Activity("1","image_a",title, 1L, "a");
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added3));
    }

    /**
     * Test for Add activity with invalid id
     */
    @Test
    public void addActivityTestId(){
        Activity added = new Activity(null,"image_a","a", 1L, "a");
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added));

        Activity added2 = new Activity("","image_a","a", 1L, "a");
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added2));

        String id = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" // 50 characters per line
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // total 300 characters
        Activity added3 = new Activity(id,"image_a","title", 1L, "a");
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added3));
    }

    /**
     * Test for Add activity with invalid consumption
     */
    @Test
    public void addActivityTestConsumption(){
        Activity added = new Activity("1","image_a","a", null, "a");
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added));
    }

    /**
     * Test for Add activity with invalid source
     */
    @Test
    public void addActivityTestSource(){
        Activity added = new Activity("1","image_a","a", 1L, null);
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added));

        Activity added2 = new Activity("1","image_a","a", 1L, "");
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added2));

        String source = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" // 50 characters per line
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // total 300 characters
        Activity added3 = new Activity("id","image_a","title", 1L, source);
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added3));
    }

    /**
     * Test for Add activity with invalid image path
     */
    @Test
    public void addActivityTestImagePath(){
        String path = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" // 50 characters per line
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // total 300 characters
        Activity added3 = new Activity("id", path,"title", 1L, "source");
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(added3));
    }

    /**
     * Test for Adding an activity
     */
    @Test
    public void addActivityTest(){
        Activity added = new Activity("1","image_a","a", 1L, "a");
        Activity expected = new Activity("1", "image_a","a", 1L, "a");
        assertEquals(expected, act.addActivity(added).getBody());
        assertEquals(expected, repo.activities.get(0));
    }

    /**
     * Test for Add activity with invalid title
     */
    @Test
    public void addManyActivitiesTest(){

        List<Activity> added = new ArrayList<>(activities);

        assertEquals(activities.size(), act.addManyActivities(added).getBody());
        assertEquals(activities, repo.activities);
    }

    /**
     * Test for adding many activities with activities = null
     */
    @Test
    public void addManyActivitiesTestNull(){
        assertEquals(ResponseEntity.badRequest().build(), act.addManyActivities(null));
    }

    /**
     * Test for adding activity with activity = null
     */
    @Test
    public void addActivityTestNull(){
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(null));
    }

    /**
     * Test for getting a random activity
     * Uses a TestRandom implementation for random the number is predictable
     */
    @Test
    public void getRandomActivityTest(){
        repo.activities.addAll(activities);
        assertEquals(activities.get(0), act.getRandomActivity().getBody());
        assertEquals(activities.get(1), act.getRandomActivity().getBody());
        assertEquals(activities.get(2), act.getRandomActivity().getBody());
    }

    /**
     * Test for getting a random activity with empty activity list
     */
    @Test
    public void getRandomActivityTestEmpty(){
        assertEquals(ResponseEntity.badRequest().build(), act.getRandomActivity());
    }

    /**
     * Test for deleteActivity
     */
    @Test
    public void deleteActivityTest(){
        repo.activities.addAll(activities);
        assertEquals(activities.get(1), act.deleteActivity("2").getBody());
        assertEquals(List.of(activities.get(0), activities.get(2)), repo.activities);
    }

    /**
     * Test for deleteActivity with invalid ID
     */
    @Test
    public void deleteActivityTestInvalidID(){
        repo.activities.addAll(activities);
        assertEquals(ResponseEntity.badRequest().build(), act.deleteActivity("8"));
    }

    /**
     * Test for updating activity title
     */
    @Test
    public void updateActivityTestTitle(){
        repo.activities.addAll(activities);

        Activity update = new Activity();
        update.setTitle("new");
        Activity expected = new Activity("1", "image_a","new", 1L, "a");
        assertEquals(expected, act.updateActivity(update, "1").getBody());
    }

    /**
     * Test for updating activity consumption
     */
    @Test
    public void updateActivityTestConsumption(){
        repo.activities.addAll(activities);

        Activity update = new Activity();
        update.setConsumption_in_wh(10L);
        Activity expected = new Activity("1", "image_a","a", 10L, "a");
        assertEquals(expected, act.updateActivity(update, "1").getBody());
    }

    /**
     * Test for updating activity source
     */
    @Test
    public void updateActivityTestSource(){
        repo.activities.addAll(activities);

        Activity update = new Activity();
        update.setSource("new");
        Activity expected = new Activity("1", "image_a","a", 1L, "new");
        assertEquals(expected, act.updateActivity(update, "1").getBody());
    }

    /**
     * Test for updating activity image path
     */
    @Test
    public void updateActivityTestImage_path(){
        repo.activities.addAll(activities);

        Activity update = new Activity();
        update.setImage_path("new");
        Activity expected = new Activity("1", "new","a", 1L, "a");
        assertEquals(expected, act.updateActivity(update, "1").getBody());
    }

    /**
     * Test for updating activity with invalid id
     */
    @Test
    public void updateActivityTestInvalidId(){
        repo.activities.addAll(activities);

        Activity update = new Activity("1", "image_a","new", 1L, "a");
        assertEquals(ResponseEntity.badRequest().build(), act.updateActivity(update, "8"));
    }

    /**
     * Test for updating activity with invalid id
     */
    @Test
    public void updateActivityTestInvalidUpdateActivity(){
        repo.activities.addAll(activities);

        Activity update = new Activity();
        assertEquals(ResponseEntity.badRequest().build(), act.updateActivity(update, "1"));
    }
}