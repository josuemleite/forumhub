create table users
(
    id       bigserial primary key,
    name     varchar(100) not null,
    email    varchar(100) not null unique,
    password varchar(100) not null
);

create table profiles
(
    id   bigserial primary key,
    name varchar(100) not null
);

create table courses
(
    id       bigserial primary key,
    name     varchar(100) not null,
    category varchar(100) not null
);

create table topics
(
    id            bigserial primary key,
    title         varchar(255) not null,
    message       text         not null,
    creation_date timestamp    not null,
    status        boolean      not null,
    author_id     bigint       not null references users (id),
    course_id     bigint       not null references courses (id)
);

create table answers
(
    id            bigserial primary key,
    message       text      not null,
    topic_id      bigint    not null references topics (id),
    creation_date timestamp not null,
    author_id     bigint    not null references users (id),
    solution      varchar(255)
);

create table user_profiles
(
    user_id    bigint not null references users (id),
    profile_id bigint not null references profiles (id),
    primary key (user_id, profile_id)
);
