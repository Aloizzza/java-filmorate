--DROP TABLE films, film_genre_link, friends, genres, rating, mpa_rating, users;

CREATE TABLE IF NOT EXISTS users
(
    id_user  INTEGER AUTO_INCREMENT,
    name     VARCHAR,
    login    VARCHAR NOT NULL,
    email    VARCHAR NOT NULL,
    birthday DATE,
    CONSTRAINT users_PK
        PRIMARY KEY (id_user)
);

CREATE TABLE IF NOT EXISTS mpa_rating
(
    id_rate INTEGER,
    name    VARCHAR,
    CONSTRAINT mpa_rating_PK
        PRIMARY KEY (id_rate)
);

CREATE TABLE IF NOT EXISTS films
(
    id_film      BIGINT,
    name         VARCHAR NOT NULL,
    description  VARCHAR NOT NULL,
    release_date DATE,
    duration     INTEGER,
    rate         INTEGER,
    mpa          INTEGER REFERENCES mpa_rating(id_rate),
    CONSTRAINT films_PK
        PRIMARY KEY (id_film)
);

CREATE TABLE IF NOT EXISTS genres
(
    id_genre INTEGER,
    name     VARCHAR,
    CONSTRAINT GENRES_PK
        PRIMARY KEY (id_genre)
);

CREATE TABLE IF NOT EXISTS film_genre_link
(
    id_genre INTEGER NOT NULL REFERENCES genres(id_genre),
    id_film  INTEGER NOT NULL REFERENCES films(id_film),
    CONSTRAINT film_genre_link_PK
        PRIMARY KEY (id_film,id_genre)
);



CREATE TABLE IF NOT EXISTS rating
(
    id_user INTEGER REFERENCES users(id_user),
    id_film INTEGER REFERENCES films(id_film),
    CONSTRAINT rating_PK
        PRIMARY KEY (id_user,id_film)
);

CREATE TABLE IF NOT EXISTS friends
(
    id_user   INTEGER REFERENCES users(id_user) ON DELETE CASCADE,
    id_friend INTEGER,
    status    INTEGER,
    CONSTRAINT friends_PK
        PRIMARY KEY (id_user,id_friend)
)