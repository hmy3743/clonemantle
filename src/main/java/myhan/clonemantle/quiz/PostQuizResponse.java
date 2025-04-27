package myhan.clonemantle.quiz;

import java.util.List;

public record PostQuizResponse(
        Long id,
        float[] embedding
) {
    public static PostQuizResponse of(Quiz quiz) {
        return new PostQuizResponse(quiz.getId(), quiz.getEmbedding());
    }
}
