package myhan.clonemantle.guess;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostGuessRequest(
        @NotBlank(message = "word는 비어있을 수 없습니다")
        @Size(max = 10, message = "word의 길이는 10을 넘을 수 없습니다")
        String word
) {
}
