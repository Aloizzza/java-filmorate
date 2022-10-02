--DROP TABLE FILMS, FILM_GENRE_LINK, FRIENDS, GENRES, RATING, MPA_RATING, USERS;

create table if not exists USERS
(
    ID_USER  integer auto_increment,
    NAME     varchar,
    LOGIN    varchar not null,
    EMAIL    varchar not null,
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (ID_USER)
);

create table if not exists mpa_rating
(
    id_rate integer,
    name varchar,
    constraint MPA_RATING_PK
        primary key (id_rate)
);

create table if not exists films
(
    id_film integer,
    name varchar not null,
    description varchar not null,
    releasedate date,
    duration int,
	rate integer,
    mpa long references mpa_rating(id_rate),
    constraint FILMS_PK
        primary key (id_film)
);

create table if not exists genres
(
    id_genre integer,
    name varchar,
    constraint GENRES_PK
        primary key (id_genre)
);

create table if not exists film_genre_link
(
    id_genre      integer not null references GENRES(ID_GENRE),
    id_film       integer not null references films(id_film),
    constraint FILM_GENRE_LINK_PK
        primary key (id_film,id_genre)
);



create table if not exists rating
(
    id_user integer references USERS(ID_USER),
	id_film integer references FILMS(ID_FILM),
    constraint RATING_PK
        primary key (id_user,id_film)
);

create table if not exists friends
(
    id_user integer references USERS(ID_USER) on delete cascade ,
    id_friend integer,
    status integer,
    constraint FRIENDS_PK
        primary key (id_user,id_friend)
)

