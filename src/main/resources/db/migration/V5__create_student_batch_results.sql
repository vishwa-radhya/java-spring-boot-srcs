CREATE TABLE student_batch_results (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL,
    student_name VARCHAR(255),
    processed_name VARCHAR(255)
);