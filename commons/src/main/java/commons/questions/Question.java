package commons.questions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ComparativeQuestion.class),
    @JsonSubTypes.Type(value = EstimationQuestion.class),
})
public interface Question {
}
