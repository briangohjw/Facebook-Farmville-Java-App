DROP DATABASE IF EXISTS oop;
CREATE DATABASE oop;

USE oop;

DROP TABLE IF EXISTS rank; 
DROP TABLE IF EXISTS steal_plot; 
DROP TABLE IF EXISTS plot; 
DROP TABLE IF EXISTS user_crop; 
DROP TABLE IF EXISTS gift; 
DROP TABLE IF EXISTS crop; 
DROP TABLE IF EXISTS reaction; 
DROP TABLE IF EXISTS comment; 
DROP TABLE IF EXISTS thread_tagged; 
DROP TABLE IF EXISTS thread; 
DROP TABLE IF EXISTS friend_requests; 
DROP TABLE IF EXISTS friends; 
DROP TABLE IF EXISTS user; 

CREATE TABLE user 
  ( 
     username VARCHAR(255) NOT NULL PRIMARY KEY, 
     fullname VARCHAR(255) NOT NULL, 
     password VARCHAR(255) NOT NULL, 
     rank     VARCHAR(25) DEFAULT 'Novice' NOT NULL, 
     xp       INT DEFAULT 0 NOT NULL, 
     gold     INT DEFAULT 50 NOT NULL 
  ); 

CREATE TABLE friends 
  ( 
     username1 VARCHAR(255) NOT NULL, 
     username2 VARCHAR(255) NOT NULL, 
     CONSTRAINT friends_pk PRIMARY KEY(username1, username2), 
     CONSTRAINT friends_fk1 FOREIGN KEY (username1) REFERENCES user(username) ON DELETE CASCADE, 
     CONSTRAINT friends_fk2 FOREIGN KEY (username2) REFERENCES user(username) ON DELETE CASCADE 
  ); 

CREATE TABLE friend_requests 
  ( 
     requestor_username VARCHAR(255) NOT NULL, 
     receiver_username VARCHAR(255) NOT NULL, 
     CONSTRAINT friend_requests_pk PRIMARY KEY(requestor_username, 
     receiver_username), 
     CONSTRAINT friend_requests_fk1 FOREIGN KEY(requestor_username) REFERENCES 
     user(username), 
     CONSTRAINT friend_requests_fk2 FOREIGN KEY(receiver_username) REFERENCES 
     user(username) 
  ); 

CREATE TABLE thread 
  ( 
     sender_username   VARCHAR(255) NOT NULL, 
     receiver_username VARCHAR(255), 
     time_stamp        TIMESTAMP(6) NOT NULL, 
     text              VARCHAR(63000), 
     CONSTRAINT thread_pk PRIMARY KEY(sender_username, time_stamp), 
     CONSTRAINT thread_fk1 FOREIGN KEY(sender_username) REFERENCES user(username 
     )  ON DELETE CASCADE, 
     CONSTRAINT thread_fk2 FOREIGN KEY(receiver_username) REFERENCES user( 
     username)  ON DELETE CASCADE
  ); 

CREATE TABLE thread_tagged 
  ( 
     sender_username VARCHAR(255) NOT NULL, 
     tagged_username VARCHAR(255) NOT NULL, 
     time_stamp      TIMESTAMP(6) NOT NULL, 
     CONSTRAINT thread_tagged_pk PRIMARY KEY(sender_username, tagged_username, 
     time_stamp), 
     CONSTRAINT thread_tagged_fk1 FOREIGN KEY(sender_username, time_stamp) 
     REFERENCES thread(sender_username, time_stamp) ON DELETE CASCADE, 
     CONSTRAINT thread_tagged_fk2 FOREIGN KEY(tagged_username) REFERENCES user( 
     username)  ON DELETE CASCADE
  ); 

CREATE TABLE comment 
  ( 
     threader_username  VARCHAR(255) NOT NULL, 
     thread_time_stamp  TIMESTAMP(6) NOT NULL, 
     commenter_username VARCHAR(255) NOT NULL, 
     comment_time_stamp TIMESTAMP(6) NOT NULL,
     text               VARCHAR(63000) NOT NULL, 
     CONSTRAINT comment_pk PRIMARY KEY(commenter_username, comment_time_stamp, threader_username, 
     thread_time_stamp), 
     CONSTRAINT comment_fk1 FOREIGN KEY(threader_username, thread_time_stamp) 
     REFERENCES thread(sender_username, time_stamp) ON DELETE CASCADE, 
     CONSTRAINT comment_fk2 FOREIGN KEY(commenter_username) REFERENCES user( 
     username) ON DELETE CASCADE
  ); 

CREATE TABLE reaction 
  ( 
     threader_username VARCHAR(255) NOT NULL, 
     thread_time_stamp TIMESTAMP(6) NOT NULL, 
     reaction_type     INT NOT NULL, 
     reactor_username  VARCHAR(255) NOT NULL, 
     reaction_time_stamp TIMESTAMP(6) NOT NULL,
     CONSTRAINT reaction_pk PRIMARY KEY(reactor_username, reaction_time_stamp, threader_username, 
     thread_time_stamp), 
     CONSTRAINT reaction_fk1 FOREIGN KEY(threader_username, thread_time_stamp) 
     REFERENCES thread(sender_username, time_stamp) ON DELETE CASCADE, 
     CONSTRAINT reaction_fk2 FOREIGN KEY(reactor_username) REFERENCES user( 
     username) ON DELETE CASCADE
  ); 

CREATE TABLE crop 
  ( 
     crop_name     VARCHAR(25) NOT NULL, 
     cost          INT NOT NULL, 
     mature_time   INT NOT NULL, 
     xp            INT NOT NULL, 
     min_yield     INT NOT NULL, 
     max_yield     INT NOT NULL, 
     selling_price INT NOT NULL, 
     CONSTRAINT crop_pk PRIMARY KEY(crop_name) 
  ); 

CREATE TABLE gift 
  ( 
     sender_username VARCHAR(255) NOT NULL, 
     receiver_username VARCHAR(255) NOT NULL,
     time_stamp      TIMESTAMP(6) NOT NULL, 
     crop_name       VARCHAR(25) NOT NULL, 
     accepted        INT DEFAULT 0 NOT NULL,
     CONSTRAINT gift_pk PRIMARY KEY(sender_username, time_stamp), 
     CONSTRAINT gift_fk1 FOREIGN KEY(sender_username, time_stamp) REFERENCES 
     thread(sender_username, time_stamp), 
     CONSTRAINT gift_fk2 FOREIGN KEY(crop_name) REFERENCES crop(crop_name),
     CONSTRAINT gift_fk3 FOREIGN KEY(receiver_username) REFERENCES user(username) ON DELETE CASCADE
  ); 

CREATE TABLE user_crop 
  ( 
     username  VARCHAR(255) NOT NULL, 
     crop_name VARCHAR(25) NOT NULL, 
     num_crops INT NOT NULL, 
     CONSTRAINT user_crop_pk PRIMARY KEY(username, crop_name), 
     CONSTRAINT user_crop_fk1 FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE, 
     CONSTRAINT user_crop_fk2 FOREIGN KEY(crop_name) REFERENCES crop(crop_name) 
  ); 

CREATE TABLE plot 
  ( 
  		plot_id	INT AUTO_INCREMENT NOT NULL,
     username             VARCHAR(255) NOT NULL, 
     time_crop_planted         TIMESTAMP(6) DEFAULT NULL, 
     remaining_percentage INT DEFAULT -1, 
     crop_name            VARCHAR(25) DEFAULT NULL, 	
     original_yield INT DEFAULT -1,
     CONSTRAINT plot_pk PRIMARY KEY(plot_id), 
     CONSTRAINT plot_fk1 FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE, 
     CONSTRAINT plot_fk2 FOREIGN KEY(crop_name) REFERENCES crop(crop_name) 
  ); 

CREATE TABLE steal_plot 
  ( 
     plot_id         INT NOT NULL, 
     thief_username  VARCHAR(255) NOT NULL, 
     CONSTRAINT steal_plot_pk PRIMARY KEY(plot_id, thief_username), 
     CONSTRAINT steal_plot_fk1 FOREIGN KEY(plot_id) REFERENCES plot(plot_id), 
     CONSTRAINT steal_plot_fk2 FOREIGN KEY(thief_username) REFERENCES user(username) ON DELETE CASCADE
  ); 
  
CREATE TABLE rank 
  ( 
     rank_name         VARCHAR(25) NOT NULL, 
     xp  INT NOT NULL, 
     num_plots INT NOT NULL,
     CONSTRAINT rank_pk PRIMARY KEY(rank_name)
  ); 

INSERT INTO crop (crop_name, cost, mature_time, xp, min_yield, max_yield, selling_price)
VALUES  
('Papaya', 20, 30, 8, 75, 100, 15),
('Pumpkin', 30, 60, 5, 5, 200, 20),
('Sunflower', 40, 120, 20, 15, 20, 40),
('Watermelon', 50, 240, 1, 5, 800, 10);
            

INSERT INTO rank (rank_name, xp, num_plots)
VALUES
('Novice',0,5),
('Apprentice',1000,6),
('Journeyman',2500,7),
('Grandmaster',5000,8),
('Legendary',12000,9);