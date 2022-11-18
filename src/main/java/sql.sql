
drop database if exists my_oj;
create database my_oj;

use my_oj;

drop table if exists user;
create table user(
    id int primary key auto_increment,
    username varchar(96),
    password varchar(96),
    is_admin int default 0
);

insert into user(username, password, is_admin) values
    ("admin","1417.a", 1),
    ("细雨", "1417.a", 0);


drop table if exists user_submit_code;
create table user_submit_code(
    user_id int,
    problem_id int,
    submit_code varchar(4096),
    primary key(user_id, problem_id)
);

drop table if exists oj_question;
create table oj_question(
    id int primary key auto_increment,
    title varchar(96),
    level varchar(96),
    description varchar(4096),
    template_code varchar(4096),
    test_code varchar(4096),
    reference_code varchar(4096)
);