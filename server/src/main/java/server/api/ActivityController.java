package server.api;

import commons.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Config;
import server.database.ActivityRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Activity endpoints go in this controller
 */

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityRepository repo;

    /**
     * Creates new ActivityController object
     * Sets repository to repo
     * @param repo repository to use
     */
    @Autowired
    public ActivityController(ActivityRepository repo) {
        this.repo = repo;
    }

    /**
     * API GET ALL ACTIVITIES ENDPOINT
     * @return list of all activities in the database
     */
    @GetMapping(path = {"", "/"})
    public List<Activity> getAll() {
        return repo.findAll();
    }

    /**
     * API GET ACTIVITY BY ID ENDPOINT
     * Initializes the image for the activity
     * @param id id of activity to be returned
     * @return activity with specified id. Bad request response entity if invalid id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable("id") String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Activity activity = repo.findById(id).get();
        activity.initializeImage(new File(Config.defaultImagePath + activity.getImage_path()));
        return ResponseEntity.ok(activity);
    }

    /**
     * Adds a list of activities to the database
     * @param activities
     * @return number of activities added
     */
    @PostMapping(path = {"/add", "/add/"})
    public ResponseEntity<Long> addManyActivities(@RequestBody List<Activity> activities) {
        if (activities == null) {
            return ResponseEntity.badRequest().build();
        }

        long count = 0L;
        for(Activity a : activities){
            if(!addActivity(a).equals(ResponseEntity.badRequest().build())){ // tries to add the activity and checks if it succeeded
                count++; // adds to count only if object was successfully added
            }
        }
        return ResponseEntity.ok(count);
    }

    /**
     * adds an activity to the database
     * @param activity
     * @return the activity which was added, bad request if invalid activity
     */
    @PostMapping(path = {"/add-one", "/add-one/"})
    public ResponseEntity<Activity> addActivity(@RequestBody Activity activity) {


        if (invalidActivity(activity)){
            return ResponseEntity.badRequest().build();
        }

        Activity act = repo.save(activity); // saves activity to the database
        return ResponseEntity.ok(act); // returns the same object if everything ok
    }


    /**
     * API UPDATE ACTIVITY BY ID
     * @param activity Activity object with updated parameters
     * @param id ID of activity to be updated
     * @return updated activity if successful, bad request if invalid update activity or invalid id
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<Activity> updateActivity(@RequestBody Activity activity, @PathVariable("id") String id) {

        Optional<Activity> dbActivityOpt = repo.findById(id); // get the player from the database

        if (dbActivityOpt.isEmpty() || invalidUpdate(activity)) {
            return ResponseEntity.badRequest().build();
        }
        Activity dbActivity = dbActivityOpt.get();

        if (!isNullOrEmpty(activity.getTitle())) { // check if a new title is specified
            dbActivity.setTitle(activity.getTitle());
        }
        if (!isNullOrEmpty(activity.getConsumption_in_wh())) { // check if a new consumption is specified
            dbActivity.setConsumption_in_wh(activity.getConsumption_in_wh());
        }
        if (!isNullOrEmpty(activity.getSource())) { // check if a new source is specified
            dbActivity.setSource(activity.getSource());
        }
        if (!isNullOrEmpty(activity.getImage_path())) { // check if a new image_path is specified
            dbActivity.setImage_path(activity.getImage_path());
        }

        Activity saved = repo.save(dbActivity); // update the player
        return ResponseEntity.ok(saved); // for some reason I can't return dbPlayer, it throws an internal server error
    }

    /**
     * API GET RANDOM ACTIVITY
     * @return 1 random activity from the database
     */
    @GetMapping("/random")
    public ResponseEntity<Activity> getRandomActivity(){
        if (repo.count()==0){ // checks if the repository is empty
            return ResponseEntity.badRequest().build();
        }
        List<Activity> act = repo.getRandomActivities(1).get();
        return ResponseEntity.ok(act.get(0));
    }

    /**
     * API DELETE ENDPOINT
     * @param id identifier of the activity to be deleted
     * @return  returns a response entity with either a
     *          200 OK status when deletion is successful
     *          or a bad request output when it fails
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Activity> deleteActivity(@PathVariable("id") String id) {
        // check if the activity with this id exists in the database
        if(!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        // if it exists, first save it, so it can be
        // returned through the response body after a successful deletion
        Activity deleted = repo.findById(id).get();

        repo.deleteById(id);    // delete it
        return ResponseEntity.ok(deleted);
    }

    /**
     * Checks if activity is invalid for updating
     * invalid if:
     *  - is null
     *  - title, consumption, source and image path are all null or empty
     *
     * @param activity
     * @return true if activity is null or has no updatable fields specified
     */
    private boolean invalidUpdate(Activity activity){
        return activity == null // bad request if editable id doesn't exist or no new values given
            || isNullOrEmpty(activity.getTitle())
            && isNullOrEmpty(activity.getConsumption_in_wh())
            && isNullOrEmpty(activity.getSource())
            && isNullOrEmpty(activity.getImage_path());
    }

    /**
     * Checks if the activity is valid
     * invalid if:
     *  - is null
     *  - title is null or empty
     *  - id is null or empty
     *  - consumption is null
     *  - source is null or empty
     *  - title length > 255 chars
     *  - consumption > Long.MAX_VALUE
     *  - source length > 255 chars
     *  - id length > 255 chars
     *  - image path length > 255 chars
     *
     * @param activity
     * @return true if activity is invalid, false otherwise
     */
    private boolean invalidActivity(Activity activity){
        return activity == null
            || isNullOrEmpty(activity.getTitle())
            || isNullOrEmpty(activity.getId())
            || isNullOrEmpty(activity.getConsumption_in_wh())
            || isNullOrEmpty(activity.getSource())
            || activity.getTitle().length() > 255
            || activity.getConsumption_in_wh() > Long.MAX_VALUE
            || activity.getSource().length() > 255
            || activity.getId().length() > 255
            || activity.getImage_path().length() > 255;
    }

    /**
     * Checks if object is null
     * If object is string also checks if string is empty
     *
     * @param o Object to check if is null or empty
     * @return true if object is null or if object is string and is empty, else false
     */
    private static boolean isNullOrEmpty(Object o) { // checks if an object is null or empty
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            String s = (String) o;
            return s.isEmpty();
        }
        return false;
    }

}
