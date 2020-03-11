DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS library_content;
DROP TABLE IF EXISTS graphicnovel_author;
DROP TABLE IF EXISTS author;
DROP TABLE  IF EXISTS graphicnovel;
DROP TABLE IF EXISTS serie_category;
DROP TABLE IF EXISTS serie;
DROP TABLE  IF EXISTS category;
-- DROP TABLE IF EXISTS users_library;
DROP TABLE IF EXISTS library;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS role;
DROP SEQUENCE IF EXISTS author_id_seq;
DROP SEQUENCE IF EXISTS graphicnovel_id_seq;
DROP SEQUENCE IF EXISTS serie_id_seq;
DROP SEQUENCE IF EXISTS category_id_seq;
DROP SEQUENCE IF EXISTS role_id_seq;
DROP SEQUENCE IF EXISTS users_id_seq;
DROP SEQUENCE IF EXISTS library_id_seq;
DROP SEQUENCE IF EXISTS library_content_id_seq;
DROP SEQUENCE IF EXISTS review_id_seq;

-- ********************************** --
-- Authors
-- ********************************** --
CREATE TABLE author (
    id BIGSERIAL CONSTRAINT author_id_pk PRIMARY KEY,
    external_id VARCHAR(15),
    lastname VARCHAR(50),
    firstname VARCHAR(50),
    nickname VARCHAR(50),
    nationality VARCHAR(50),
    birthdate DATE,
    decease_date DATE,
    photo_url VARCHAR(255),
    site_url VARCHAR(255),
    biography VARCHAR(10000),
    is_scraped BOOLEAN,
    scrap_url VARCHAR(255),
    creation_date TIMESTAMP,
    creation_user VARCHAR(30),
    last_update_date TIMESTAMP,
    last_update_user VARCHAR(30)
);

-- ********************************** --
-- Series & Categories
-- ********************************** --
CREATE TABLE serie (
    id BIGSERIAL CONSTRAINT serie_id_pk PRIMARY KEY,
    external_id VARCHAR(15),
    title VARCHAR(100),
    status VARCHAR(30),
    origin VARCHAR(30),
    langage VARCHAR(30),
    site_url VARCHAR(255),
    page_url VARCHAR(255),
    page_thb_url VARCHAR(255),
    is_favorite BOOLEAN,
    is_scraped BOOLEAN,
    scrap_url VARCHAR(255),
    creation_date TIMESTAMP,
    creation_user VARCHAR(30),
    last_update_date TIMESTAMP,
    last_update_user VARCHAR(30)
);

CREATE TABLE category (
    id BIGSERIAL CONSTRAINT category_id_pk PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE serie_category (
    serie_id BIGINT CONSTRAINT serie_fk REFERENCES serie,
    category_id BIGINT CONSTRAINT category_fk REFERENCES category,
    PRIMARY KEY (serie_id, category_id)
);

-- ********************************** --
-- Graphic novels & Authors
-- ********************************** --
CREATE TABLE graphicnovel (
    id BIGSERIAL CONSTRAINT graphicnovel_id_pk PRIMARY KEY,
    external_id VARCHAR(15),
    tome VARCHAR(15),
    num_edition VARCHAR(15),
    title VARCHAR(255),
    publisher VARCHAR(50),
    cycle VARCHAR(50),
    publication_date DATE,
    release_date DATE,
    isbn VARCHAR(20),
    total_pages INT,
    format VARCHAR(20),
    is_original_edition BOOLEAN,
    is_integrale BOOLEAN,
    is_broche BOOLEAN,
    is_favorite BOOLEAN,
    info_edition VARCHAR(255),
    reedition_url VARCHAR(255),
    is_scraped BOOLEAN,
    scrap_url VARCHAR(255),
    creation_date TIMESTAMP,
    creation_user VARCHAR(30),
    last_update_date TIMESTAMP,
    last_update_user VARCHAR(30),
    serie_id BIGINT CONSTRAINT serie_id_fk REFERENCES serie
);

CREATE TABLE graphicnovel_author (
    graphicnovel_id BIGINT CONSTRAINT graphicnovel_fk REFERENCES graphicnovel,
    author_id BIGINT CONSTRAINT author_fk REFERENCES author,
    PRIMARY KEY (graphicnovel_id, author_id),
    role VARCHAR(50)
);

-- ********************************** --
-- Users & Roles
-- ********************************** --
CREATE TABLE role (
    id BIGSERIAL CONSTRAINT role_id_pk PRIMARY KEY,
    name VARCHAR(30)
);

CREATE TABLE users (
    id BIGSERIAL CONSTRAINT users_id_pk PRIMARY KEY,
    username VARCHAR(30),
    password VARCHAR(30),
    email VARCHAR(255),
    last_login_date TIMESTAMP,
    role_id BIGINT CONSTRAINT role_id_fk REFERENCES role
);

-- ********************************** --
-- Users & libraries
-- ********************************** --
CREATE TABLE library (
    id BIGSERIAL CONSTRAINT library_id_pk PRIMARY KEY,
    name VARCHAR(30),
    description VARCHAR(500),
    library_id BIGINT CONSTRAINT library_fk REFERENCES library
);

-- CREATE TABLE users_library (
--     users_id BIGINT CONSTRAINT users_fk REFERENCES users,
--     PRIMARY KEY (users_id, library_id)
-- );

CREATE TABLE library_content (
    id BIGSERIAL CONSTRAINT library_content_id_pk PRIMARY KEY,
    library_id BIGINT CONSTRAINT library_id_fk REFERENCES library,
    graphicnovel_id BIGINT CONSTRAINT graphicnovel_id_fk REFERENCES graphicnovel,
    is_physical BOOLEAN,
    is_numeric BOOLEAN,
    local_storage VARCHAR(255),
    creation_date TIMESTAMP,
    creation_user VARCHAR(30),
    last_update_date TIMESTAMP,
    last_update_user VARCHAR(30)
);

-- ********************************** --
-- Users, libraries & reviews
-- ********************************** --
CREATE TABLE review (
    id BIGSERIAL CONSTRAINT review_id_pk PRIMARY KEY,
    users_id BIGINT CONSTRAINT users_fk REFERENCES users,
    library_id BIGINT CONSTRAINT library_fk REFERENCES library,
    serie_id BIGINT CONSTRAINT serie_id_fk REFERENCES serie,
    graphicnovel_id BIGINT CONSTRAINT graphicnovel_id_fk REFERENCES graphicnovel,
    score INT,
    comment VARCHAR(1000)
);