create table if not exists author
(
    id          uuid,
    author_name varchar(255),
    created_at  timestamp,
    updated_at  timestamp
) partition by hash (author_name);
create UNIQUE INDEX author_name_idx on author (author_name);
CREATE INDEX author_id on author (id);
CREATE TABLE author_0 PARTITION OF author FOR VALUES WITH (MODULUS 4, REMAINDER 0);
CREATE TABLE author_1 PARTITION OF author FOR VALUES WITH (MODULUS 4, REMAINDER 1);
CREATE TABLE author_2 PARTITION OF author FOR VALUES WITH (MODULUS 4, REMAINDER 2);
CREATE TABLE author_3 PARTITION OF author FOR VALUES WITH (MODULUS 4, REMAINDER 3);