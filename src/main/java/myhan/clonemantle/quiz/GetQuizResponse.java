package myhan.clonemantle.quiz;

import java.time.ZonedDateTime;
import java.util.List;

public record GetQuizResponse(
        Long id,
        String word,
        ZonedDateTime completedAt,
        float[] embedding) {
    public static GetQuizResponse from(Quiz quiz) {
        return new GetQuizResponse(
                quiz.getId(),
                quiz.getWord(),
                quiz.getCompletedAt(),
                quiz.getEmbedding());
    }
}
