create database codewithfriends;

create table users(
	user_login varchar(255)primary key not null,
	user_name varchar(255) not null,
	user_last_name varchar(255) not null,
	user_password varchar(255) not null,
	user_pic blob );
	
create table posts(
	post_id int auto_increment not null primary key,
	user_login varchar(255) not null,
	post_date not null,
	post_title varchar(255) not null,
	post_content longtext not null,
	post_rate enum('1','2','3') not null,
	post_visibility enum('0','1') not null,
	post_pic blob,
        FOREIGN KEY (user_login) REFERENCES users(user_login)
);
