package myhan.clonemantle.guess;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuessRepository extends JpaRepository<Guess, Long> {
    List<Guess> findByQuizIdAndIdLessThanOrderByIdDesc(long quizId, long id, Pageable pageable);
}
