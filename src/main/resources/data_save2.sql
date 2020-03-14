drop index if exists external_id_index;
drop index if exists author_external_id_index;
drop index if exists serie_external_id_index;

-- **************************************************************** --
-- Delete constraints
-- **************************************************************** --
alter table if exists graphicnovel
    drop constraint if exists serie_id;
alter table if exists graphicnovel_author
    drop constraint if exists author_id;
alter table if exists graphicnovel_author
    drop constraint if exists graphicnovel_id;
alter table if exists library_content
    drop constraint if exists graphicnovel_id;
alter table if exists library_content
    drop constraint if exists library_id;
alter table if exists library_serie_content
    drop constraint if exists library_id;
alter table if exists library_serie_content
    drop constraint if exists serie_id;
alter table if exists review
    drop constraint if exists library_content_id;
alter table if exists review
    drop constraint if exists library_serie_content_id;
--alter table if exists serie_category
--    drop constraint if exists category_id;
--alter table if exists serie_category
--    drop constraint if exists serie_id;
alter table if exists users
    drop constraint if exists role_id;
alter table if exists users_library
    drop constraint if exists library_id;
alter table if exists users_library
    drop constraint if exists users_id;

-- **************************************************************** --
-- Delete tables
-- **************************************************************** --
drop table if exists author cascade;
--drop table if exists category cascade;
drop table if exists graphicnovel cascade;
drop table if exists graphicnovel_author cascade;
drop table if exists library cascade;
drop table if exists library_content cascade;
drop table if exists library_serie_content cascade;
drop table if exists review cascade;
drop table if exists role cascade;
drop table if exists serie cascade;
drop table if exists serie_category cascade;
drop table if exists users cascade;
drop table if exists users_library cascade;

-- **************************************************************** --
-- Delete sequences
-- **************************************************************** --
drop sequence if exists author_id_seq;
--drop sequence if exists category_id_seq;
drop sequence if exists graphicnovel_id_seq;
drop sequence if exists library_content_id_seq;
drop sequence if exists library_id_seq;
drop sequence if exists library_serie_content_id_seq;
drop sequence if exists review_id_seq;
drop sequence if exists role_id_seq;
drop sequence if exists serie_id_seq;
drop sequence if exists users_id_seq;

-- **************************************************************** --
-- Create sequences
-- **************************************************************** --
create sequence author_id_seq start 1 increment 1;
--create sequence category_id_seq start 1 increment 1;
create sequence graphicnovel_id_seq start 1 increment 1;
create sequence library_content_id_seq start 1 increment 1;
create sequence library_id_seq start 1 increment 1;
create sequence library_serie_content_id_seq start 1 increment 1;
create sequence review_id_seq start 1 increment 1;
create sequence role_id_seq start 1 increment 1;
create sequence serie_id_seq start 1 increment 1;
create sequence users_id_seq start 1 increment 1;

-- **************************************************************** --
-- Create tables
-- **************************************************************** --
CREATE TABLE author
(
    id               INT8 NOT NULL,
    external_id      VARCHAR(15),
    lastname         VARCHAR(255),
    firstname        VARCHAR(255),
    nickname         VARCHAR(255),
    nationality      VARCHAR(255),
    birthdate        DATE,
    decease_date     DATE,
    photo_url        VARCHAR(255),
    site_url         VARCHAR(255),
    is_scraped       BOOLEAN,
    scrap_url        VARCHAR(255),
    biography        TEXT,
    creation_date    TIMESTAMP,
    creation_user    VARCHAR(30),
    last_update_date TIMESTAMP,
    last_update_user VARCHAR(30),
    PRIMARY KEY (id)
);

-- CREATE TABLE category
-- (
--     id   INT8 NOT NULL,
--     name VARCHAR(255),
--     PRIMARY KEY (id)
-- );

CREATE TABLE graphicnovel
(
    id                  INT8 NOT NULL,
    external_id         VARCHAR(15),
    tome                VARCHAR(15),
    num_edition         VARCHAR(15),
    title               VARCHAR(255),
    publisher           VARCHAR(50),
    collection          VARCHAR(50),
    publication_date    DATE,
    release_date        DATE,
    isbn                VARCHAR(20),
    total_pages         INT,
    book_format         VARCHAR(20),
    is_original_edition BOOLEAN,
    is_integrale        BOOLEAN,
    is_broche           BOOLEAN,
    info_edition        TEXT,
    reedition_url       VARCHAR(255),
    external_id_original_publication VARCHAR(15),
    is_scraped          BOOLEAN,
    scrap_url           VARCHAR(255),
    graphic_novel_url   VARCHAR(255),
    cover_picture_url   VARCHAR(255),
    cover_thumbnail_url VARCHAR(255),
    back_cover_picture_url VARCHAR(255),
    back_cover_thumbnail_url VARCHAR(255),
    page_url            VARCHAR(255),
    page_thumbnail_url  VARCHAR(255),
    creation_date       TIMESTAMP,
    creation_user       VARCHAR(30),
    last_update_date    TIMESTAMP,
    last_update_user    VARCHAR(30),
    serie_id            BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE graphicnovel_author
(
    graphicnovel_id INT8        NOT NULL,
    author_id       BIGINT      NOT NULL,
    role            VARCHAR(50) NOT NULL,
    PRIMARY KEY (graphicnovel_id, author_id, role)
);

CREATE TABLE library
(
    id          INT8 NOT NULL,
    name        VARCHAR(30),
    description VARCHAR(500),
    PRIMARY KEY (id)
);

CREATE TABLE library_content
(
    id               INT8 NOT NULL,
    library_id       INT8,
    graphicnovel_id  INT8,
    is_favorite      BOOLEAN,
    is_numeric       BOOLEAN,
    is_physical      BOOLEAN,
    local_storage    VARCHAR(255),
    creation_date    TIMESTAMP,
    creation_user    VARCHAR(255),
    last_update_date TIMESTAMP,
    last_update_user VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE library_serie_content
(
    id               INT8 NOT NULL,
    library_id       INT8,
    serie_id         INT8,
    is_favorite      BOOLEAN,
    creation_date    TIMESTAMP,
    creation_user    VARCHAR(255),
    last_update_date TIMESTAMP,
    last_update_user VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE review
(
    id                       INT8 NOT NULL,
    library_content_id       INT8,
    library_serie_content_id INT8,
    score                    INT4,
    comment                  TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE role
(
    id   INT8 NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE serie
(
    id               INT8         NOT NULL,
    external_id      VARCHAR(15),
    title            VARCHAR(255) NOT NULL,
    status           VARCHAR(20),
    origin           VARCHAR(20),
    categories       TEXT,
    langage          VARCHAR(30),
    synopsys         TEXT,
    page_thb_url     VARCHAR(255),
    page_url         VARCHAR(255),
    is_scraped       BOOLEAN,
    scrap_url        VARCHAR(255),
    site_url         VARCHAR(255),
    creation_date    TIMESTAMP,
    creation_user    VARCHAR(30),
    last_update_date TIMESTAMP,
    last_update_user VARCHAR(30),
    PRIMARY KEY (id)
);

-- CREATE TABLE serie_category
-- (
--     serie_id    INT8 NOT NULL,
--     category_id INT8 NOT NULL,
--     PRIMARY KEY (serie_id, category_id)
-- );

CREATE TABLE users
(
    id              INT8 NOT NULL,
    username        VARCHAR(30),
    password        VARCHAR(30),
    role_id         INT8,
    email           VARCHAR(255),
    last_login_date TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE users_library
(
    users_id   INT8 NOT NULL,
    library_id INT8 NOT NULL,
    PRIMARY KEY (users_id, library_id)
);


-- **************************************************************** --
-- Add foreign keys
-- **************************************************************** --
ALTER TABLE IF EXISTS graphicnovel
    ADD CONSTRAINT serie_id FOREIGN KEY (serie_id) REFERENCES serie;

ALTER TABLE IF EXISTS graphicnovel_author
    ADD CONSTRAINT author_id FOREIGN KEY (author_id) REFERENCES author;

ALTER TABLE IF EXISTS graphicnovel_author
    ADD CONSTRAINT graphicnovel_id FOREIGN KEY (graphicnovel_id) REFERENCES graphicnovel;

ALTER TABLE IF EXISTS library_content
    ADD CONSTRAINT graphicnovel_id FOREIGN KEY (graphicnovel_id) REFERENCES graphicnovel;

ALTER TABLE IF EXISTS library_content
    ADD CONSTRAINT library_id FOREIGN KEY (library_id) REFERENCES library;

ALTER TABLE IF EXISTS library_serie_content
    ADD CONSTRAINT library_id FOREIGN KEY (library_id) REFERENCES library;

ALTER TABLE IF EXISTS library_serie_content
    ADD CONSTRAINT serie_id FOREIGN KEY (serie_id) REFERENCES serie;

ALTER TABLE IF EXISTS review
    ADD CONSTRAINT library_content_id FOREIGN KEY (library_content_id) REFERENCES library_content;

ALTER TABLE IF EXISTS review
    ADD CONSTRAINT library_serie_content_id FOREIGN KEY (library_serie_content_id) REFERENCES library_serie_content;

--ALTER TABLE IF EXISTS serie_category
--    ADD CONSTRAINT category_id FOREIGN KEY (category_id) REFERENCES category;

-- ALTER TABLE IF EXISTS serie_category
--     ADD CONSTRAINT serie_id FOREIGN KEY (serie_id) REFERENCES serie;

ALTER TABLE IF EXISTS users
    ADD CONSTRAINT role_id FOREIGN KEY (role_id) REFERENCES role;

ALTER TABLE IF EXISTS users_library
    ADD CONSTRAINT library_id FOREIGN KEY (library_id) REFERENCES library;

ALTER TABLE IF EXISTS users_library
    ADD CONSTRAINT users_id FOREIGN KEY (users_id) REFERENCES users;

-- **************************************************************** --
-- Set tables id auto-incrementables
-- **************************************************************** --
alter table author alter column id set default nextval('public.author_id_seq');
alter sequence author_id_seq owned by author.id;

--alter table category alter column id set default nextval('public.category_id_seq');
--alter sequence category_id_seq owned by category.id;

alter table graphicnovel alter column id set default nextval('public.graphicnovel_id_seq');
alter sequence graphicnovel_id_seq owned by graphicnovel.id;

alter table library alter column id set default nextval('public.library_id_seq');
alter sequence library_id_seq owned by library.id;

alter table library_content alter column id set default nextval('public.library_content_id_seq');
alter sequence library_content_id_seq owned by library_content.id;

alter table library_serie_content alter column id set default nextval('public.library_serie_content_id_seq');
alter sequence library_serie_content_id_seq owned by library_serie_content.id;

alter table review alter column id set default nextval('public.review_id_seq');
alter sequence review_id_seq owned by review.id;

alter table role alter column id set default nextval('public.role_id_seq');
alter sequence role_id_seq owned by role.id;

alter table serie alter column id set default nextval('public.serie_id_seq');
alter sequence serie_id_seq owned by serie.id;

alter table users alter column id set default nextval('public.users_id_seq');
alter sequence users_id_seq owned by users.id;

-- Create index
create index external_id_index on graphicnovel (external_id);
create index author_external_id_index on author(external_id);
create index serie_external_id_index on serie(external_id);