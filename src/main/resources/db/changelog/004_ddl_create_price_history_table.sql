CREATE TABLE price_history
(
    id      SERIAL PRIMARY KEY,
    before  BIGINT not null,
    after   BIGINT not null,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    post_id INT not null REFERENCES auto_post (id)
);