package myhan.clonemantle.quiz;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/quiz")
public class QuizController {
    private final QuizRepository quizRepository;
    private final EmbeddingModel embeddingModel;

    @Autowired
    QuizController(QuizRepository quizRepository, EmbeddingModel embeddingModel) {
        this.quizRepository = quizRepository;
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/{id}")
    public GetQuizResponse getQuiz(@PathVariable Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isPresent()) {
            return GetQuizResponse.from(quiz.get());
        }
        return null;
    }

    @PostMapping("")
    public PostQuizResponse postQuiz(@Validated @RequestBody PostQuizRequest postQuizRequest) {
        float[] embedding = embeddingModel.embed(postQuizRequest.word());

        Quiz quiz = quizRepository.save(new Quiz(null, postQuizRequest.word(), null, embedding));

        return PostQuizResponse.of(quiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if(quiz.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        quizRepository.delete(quiz.get());
        return ResponseEntity.ok().build();
    }
}
