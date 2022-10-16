package commons.questions;

import commons.Activity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EqualityQuestionTest {

    @Test
    void noArgsConstructorTest(){
        EqualityQuestion e = new EqualityQuestion();
        assertNotNull(e);
    }

    @Test
    void ConstructorTest(){
        List<Activity> activityList = List.of(new Activity(), new Activity());
        EqualityQuestion e = new EqualityQuestion(new Activity(), new Activity(), activityList, 0);
        assertNotNull(e);
    }
}