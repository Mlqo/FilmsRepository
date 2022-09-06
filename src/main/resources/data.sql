create table users(
    userid int not null auto_increment,
    username varchar_ignorecase(50) not null UNIQUE,
    password varchar_ignorecase(500) not null,
    role varchar(50) not null,
    enabled boolean not null,
    primary key (userid)
);





create table user_movie(
                           userid int not null,
                           movieid int not null,
                           favorite boolean,
                           personal_rating int,
                           notes varchar(255),
    foreign key (userid) references users(userid)
    );




INSERT INTO `users` (`username`,`password`,`role`,`enabled`)
VALUES ('user',
'$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu',
'ROLE_USER', 1);
INSERT INTO `users` (`username`,`password`,`role`,`enabled`)
VALUES ('admin',
'$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu',
'ROLE_ADMIN', 1);