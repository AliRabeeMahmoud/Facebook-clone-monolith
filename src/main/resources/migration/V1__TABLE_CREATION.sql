-- Create the Country table
CREATE TABLE country (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE,
    country_code VARCHAR(3),
    calling_code VARCHAR(4),
    date_created DATETIME,
    date_last_modified DATETIME
);

-- Create the User table
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    intro VARCHAR(100),
    gender VARCHAR(16),
    hometown VARCHAR(128),
    current_city VARCHAR(128),
    edu_institution VARCHAR(128),
    workplace VARCHAR(128),
    profile_photo VARCHAR(256),
    cover_photo VARCHAR(256),
    role VARCHAR(32) NOT NULL,
    follower_count INT,
    following_count INT,
    enabled BOOLEAN,
    account_verified BOOLEAN,
    email_verified BOOLEAN,
    birth_date DATETIME,
    join_date DATETIME,
    date_last_modified DATETIME,
    country_id BIGINT,
    FOREIGN KEY (country_id) REFERENCES country(id)
);

-- Create the Post table
CREATE TABLE post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(4096),
    post_photo VARCHAR(256),
    like_count INT,
    comment_count INT,
    share_count INT,
    is_type_share BOOLEAN NOT NULL,
    date_created DATETIME,
    date_last_modified DATETIME,
    author_id BIGINT NOT NULL,
    shared_post_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES user(id),
    FOREIGN KEY (shared_post_id) REFERENCES post(id)
);

-- Create the Comment table
CREATE TABLE comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(1024),
    like_count INT,
    date_created DATETIME,
    date_last_modified DATETIME,
    author_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES user(id),
    FOREIGN KEY (post_id) REFERENCES post(id)
);

-- Create the Notification table
CREATE TABLE notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    receiver_id BIGINT,
    sender_id BIGINT,
    owning_post_id BIGINT,
    owning_comment_id BIGINT,
    is_seen BOOLEAN,
    is_read BOOLEAN,
    date_created DATETIME,
    date_updated DATETIME,
    date_last_modified DATETIME,
    FOREIGN KEY (receiver_id) REFERENCES user(id),
    FOREIGN KEY (sender_id) REFERENCES user(id),
    FOREIGN KEY (owning_post_id) REFERENCES post(id),
    FOREIGN KEY (owning_comment_id) REFERENCES comment(id)
);

-- Create the Tag table
CREATE TABLE tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE,
    tag_use_counter INT,
    date_created DATETIME,
    date_last_modified DATETIME
);

-- Create the post_likes join table
CREATE TABLE post_likes (
    post_id BIGINT NOT NULL,
    liker_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, liker_id),
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (liker_id) REFERENCES user(id)
);

-- Create the comment_likes join table
CREATE TABLE comment_likes (
    comment_id BIGINT NOT NULL,
    liker_id BIGINT NOT NULL,
    PRIMARY KEY (comment_id, liker_id),
    FOREIGN KEY (comment_id) REFERENCES comment(id),
    FOREIGN KEY (liker_id) REFERENCES user(id)
);

-- Create the post_tags join table
CREATE TABLE post_tags (
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (tag_id) REFERENCES tag(id)
);

-- Create the follow_users join table
CREATE TABLE follow_users (
    followed_id BIGINT NOT NULL,
    follower_id BIGINT NOT NULL,
    PRIMARY KEY (followed_id, follower_id),
    FOREIGN KEY (followed_id) REFERENCES user(id),
    FOREIGN KEY (follower_id) REFERENCES user(id)
);