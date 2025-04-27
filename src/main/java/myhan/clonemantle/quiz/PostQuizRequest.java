package myhan.clonemantle.quiz;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PostQuizRequest(
        @NotEmpty(message = "word는 비어있을 수 없습니다")
        @Size(max = 10, message = "word는 최대 길이 10 을 넘을 수 없습니다")
        String word
) {
}
