package commons;

import commons.questions.EstimationQuestion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstimationQuestionTest {

    @Test
    void noArgsConstructorTest() {
        EstimationQuestion q = new EstimationQuestion();
        assertNotNull(q);
    }

    @Test
    void ConstructorTest(){
        EstimationQuestion q = new EstimationQuestion(new Activity());
        assertNotNull(q);
    }
}