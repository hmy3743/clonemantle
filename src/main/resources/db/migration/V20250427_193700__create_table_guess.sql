CREATE TABLE
    guess (
        id SERIAL PRIMARY KEY,
        quiz_id INTEGER NOT NULL,
        word VARCHAR(50) NOT NULL,
        similarity DOUBLE PRECISION NOT NULL,
        inserted_at TIMESTAMPTZ,
        CONSTRAINT fk_quiz
            FOREIGN KEY (quiz_id)
            REFERENCES quiz(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
    );

CREATE INDEX idx_guess_quiz_id_id_desc ON guess (quiz_id, id DESC);