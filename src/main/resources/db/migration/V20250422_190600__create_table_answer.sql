CREATE TABLE
    quiz (
        id SERIAL PRIMARY KEY,
        word VARCHAR(50) NOT NULL,
        completed_at TIMESTAMPTZ
    );

CREATE INDEX idx_quiz_completed_at_null_id_desc ON quiz (id DESC)
WHERE
    completed_at IS NULL;