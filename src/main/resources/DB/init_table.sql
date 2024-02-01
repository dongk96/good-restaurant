create table users (
    userId bigint not null auto_increment,
    email varchar(255),
    password varchar(255),
    role varchar(255) check (role in ('USER','ADMIN')),
    primary key (userId)
);

create table Follow (
    followId bigint not null auto_increment,
    follower_id bigint not null,
    host_id bigint not null,
    state boolean,
    primary key (followId)
);

create table places (
    delicious_place_id bigint not null auto_increment,
    user_id bigint not null,
    place_kakao_id bigint,
    place_name varchar(255),
    place_address varchar(255),
    place_url varchar(255),
    primary key (delicious_place_id)
)