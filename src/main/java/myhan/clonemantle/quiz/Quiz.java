package myhan.clonemantle.quiz;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "quiz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word")
    @Size(max = 10)
    @NotNull
    private String word;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @Column
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1536)
    @NotNull
    private float[] embedding;
}
