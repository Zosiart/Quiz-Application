package server.api;

import commons.Activity;
import commons.questions.ComparativeQuestion;
import commons.questions.EqualityQuestion;
import commons.questions.EstimationQuestion;
import commons.questions.MCQuestion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.dependencies.TestActivityRepository;
import server.api.dependencies.TestRandom;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionControllerTest {

    private TestRandom random;
    private TestActivityRepository repo;

    private List<Activity> activities;
    private QuestionController que;

    /**
     * Sets up a new question controller with testing dependencies
     * Runs before every test
     */
    @BeforeEach
    public void setup(){
        random = new TestRandom();
        repo = new TestActivityRepository();
        que = new QuestionController(random, repo);

        activities = List.of(
            new Activity("1", "image_a","a", 1L, "a"),
            new Activity("2", "image_b","b", 2L, "b"),
            new Activity("3", "image_c","c", 3L, "c"),
            new Activity("4", "image_d","d", 4L, "d"),
            new Activity("5", "image_e","e", 5L, "e"),
            new Activity("6", "image_f","f", 6L, "f"),
            new Activity("7", "image_g","g", 7L, "g")
        );
    }

    @AfterEach
    void cleanUp(){
        repo.activities = new ArrayList<>();
        random.setCount(0);
    }

    /**
     * Test for getting a random comparative question
     * Uses TestRandom implementation so random is predictable
     */
    @Test //repo and que have different TestRandom objects!
    void getRandomComparativeTest() {
        random.setCount(0);
        repo.activities.addAll(activities);

        List<Activity> e1_list = List.of(activities.get(1), activities.get(2), activities.get(3));
        ComparativeQuestion expected1 = new ComparativeQuestion(e1_list, true);
        assertEquals(expected1, que.getRandomComparative().getBody());
    }

    @Test
    void getRandomComparativeTestNoActivities() {
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), que.getRandomComparative());
    }

    @Test
    void getRandomEstimationTest() {
        repo.activities.addAll(activities);

        EstimationQuestion expected = new EstimationQuestion(activities.get(0));
        assertEquals(expected, que.getRandomEstimation().getBody());
    }

    @Test //repo and que have different TestRandom objects!
    void getRandomMCTest() {
        repo.activities.addAll(activities);

        List<Long> e1_list = List.of(activities.get(2).getConsumption_in_wh(), activities.get(3).getConsumption_in_wh());
        MCQuestion expected1 = new MCQuestion(activities.get(1), e1_list);
        assertEquals(expected1, que.getRandomMCQuestion().getBody());
    }

    /**
     * Returns an error because all activities have unique consumptions
     */
    @Test
    void getRandomEqualityTestUnique() {
        repo.activities.addAll(activities);
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), que.getRandomEquality());
    }

    @Test
    void getRandomEqualityTestEmpty() {
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), que.getRandomEquality());
    }

    @Test
    void getRandomEqualityTest() {
        random.setCount(0);

        Activity similar = new Activity("8", "image_h","h", 1L, "h");
        repo.activities.addAll(activities);
        repo.activities.add(similar);

        List<Activity> expectedList = List.of(activities.get(1), activities.get(2));
        EqualityQuestion expected = new EqualityQuestion(activities.get(0), similar, expectedList, 0);

        assertEquals(expected, que.getRandomEquality().getBody());
    }

    @Test
    void getRandomMCTestNoActivities() {
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), que.getRandomMCQuestion());
    }

    @Test
    void getRandomEstimationTestNoActivities() {
        repo.activities.add(activities.get(0));
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), que.getRandomEstimation());
    }

    @Test
    void getRandomQuestionTestComparative() {
        random.setCount(0); // sets the random to start from 0
        repo.activities.addAll(activities);

        ComparativeQuestion expected = new ComparativeQuestion(List.of(activities.get(1), activities.get(2), activities.get(3)), false);

        assertEquals(expected, que.getRandomQuestion().getBody());
    }

    @Test
    void getRandomQuestionTestEstimation() {
        repo.activities.addAll(activities);
        random.setCount(1); // sets the random to start from 1

        EstimationQuestion expected = new EstimationQuestion(activities.get(0));

        assertEquals(expected, que.getRandomQuestion().getBody());
    }

    @Test
    void getRandomQuestionTestMC() {
        random.setCount(2); // sets the random to start from 2
        repo.activities.addAll(activities);

        List<Long> e1_list = List.of(activities.get(2).getConsumption_in_wh(), activities.get(3).getConsumption_in_wh());
        MCQuestion expected = new MCQuestion(activities.get(1), e1_list);

        assertEquals(expected, que.getRandomQuestion().getBody());
    }

    @Test
    void getRandomQuestionTestEquality() {
        random.setCount(3); // sets the random to start from 3

        Activity similar = new Activity("8", "image_h","h", 1L, "h");
        repo.activities.addAll(activities);
        repo.activities.add(similar);

        List<Activity> expectedList = List.of(activities.get(1), activities.get(2));
        EqualityQuestion expected = new EqualityQuestion(activities.get(0), similar, expectedList, 1);

        assertEquals(expected, que.getRandomQuestion().getBody());
    }
}