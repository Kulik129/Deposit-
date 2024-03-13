CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    uuid UUID,
    full_name VARCHAR(150)
);

CREATE TABLE account
(
    id SERIAL PRIMARY KEY,
    uuid UUID,
    user_id INT REFERENCES users(id) ON DELETE SET NULL,
    balance INTEGER
);