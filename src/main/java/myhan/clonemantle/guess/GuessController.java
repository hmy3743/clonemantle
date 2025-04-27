package myhan.clonemantle.guess;

import myhan.clonemantle.common.CursorPageRequest;
import myhan.clonemantle.common.CursorPageResponse;
import myhan.clonemantle.quiz.Quiz;
import myhan.clonemantle.quiz.QuizRepository;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/guess")
public class GuessController {
    private final QuizRepository quizRepository;
    private final GuessRepository guessRepository;
    private final EmbeddingModel embeddingModel;

    @Autowired
    GuessController(QuizRepository quizRepository, GuessRepository guessRepository, EmbeddingModel embeddingModel) {
        this.quizRepository = quizRepository;
        this.guessRepository = guessRepository;
        this.embeddingModel = embeddingModel;
    }

    @GetMapping
    public ResponseEntity<CursorPageResponse<GetGuessesItem>> getGuesses(@Validated CursorPageRequest cursorPageRequest) {
        long id = Long.MAX_VALUE;
        try {
            id = Long.parseLong(cursorPageRequest.after());
        } catch (RuntimeException ignored) {}

        Optional<Quiz> quizOptional = quizRepository.findFirstByCompletedAtIsNullOrderById();

        if(quizOptional.isEmpty()) {
            return ResponseEntity.status(503).build();
        }
        Quiz quiz = quizOptional.get();

        List<Guess> guesses = guessRepository.findByQuizIdAndIdLessThanOrderByIdDesc(quiz.getId(), id, PageRequest.ofSize(10));

        boolean hasNext = false;
        String nextCursor = null;
        if(guesses.size() == cursorPageRequest.first() + 1) {
            hasNext = true;
            guesses.removeLast();
            nextCursor = guesses.getLast().getId().toString();
        }

        List<GetGuessesItem> items = guesses.stream().map(guess -> new GetGuessesItem(guess.getId(), guess.getWord(), guess.getSimilarity())).toList();

        return ResponseEntity.ok().body(new CursorPageResponse<>(items, nextCursor, hasNext));
    }

    @PostMapping("")
    @Transactional
    public ResponseEntity<PostGuessResponse> postGuess(@Validated @RequestBody PostGuessRequest postGuessRequest) {
        Optional<Quiz> quizOptional = quizRepository.findFirstByCompletedAtIsNullOrderById();

        if(quizOptional.isEmpty()) {
            return ResponseEntity.status(503).build();
        }
        Quiz quiz = quizOptional.get();

        if(quiz.getWord().equals(postGuessRequest.word())) {
            quiz.setCompletedAt(ZonedDateTime.now());
            Guess guess = guessRepository.save(new Guess(null, quiz.getId(), postGuessRequest.word(), 1));
            return ResponseEntity.ok().body(PostGuessResponse.of(guess));
        }

        float[] quizEmbedding = quiz.getEmbedding();
        float[] guessEmbedding = embeddingModel.embed(postGuessRequest.word());

        double similarity = cosineSimilarity(quizEmbedding, guessEmbedding);

        Guess guess = guessRepository.save(new Guess(null, quiz.getId(), postGuessRequest.word(), similarity));

        return ResponseEntity.ok().body(PostGuessResponse.of(guess));
    }

    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        INDArray v1 = Nd4j.create(vectorA);
        INDArray v2 = Nd4j.create(vectorB);
        return Transforms.cosineSim(v1, v2);
    }
}
