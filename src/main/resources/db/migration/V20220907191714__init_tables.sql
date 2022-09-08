CREATE TABLE user
(
    id       INT8 PRIMARY KEY,
    token    VARCHAR(36)  NOT NULL,
    username VARCHAR(32)  NOT NULL,
    email    VARCHAR(128) NOT NULL
);

CREATE TABLE monitored_endpoint
(
    id                 INT8 PRIMARY KEY AUTO_INCREMENT,
    url                VARCHAR(512) NOT NULL,
    monitored_interval INT          NOT NULL,
    date_of_creation   DATETIME     NOT NULL,
    date_of_last_check DATETIME,
    name               VARCHAR(256) NOT NULL,
    owner_id           INT8         NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE monitoring_result
(
    id                    INT8 PRIMARY KEY AUTO_INCREMENT,
    date_of_check         DATETIME NOT NULL,
    status                INT      NOT NULL,
    payload               TEXT,
    monitored_endpoint_id INT8     NOT NULL,
    FOREIGN KEY (monitored_endpoint_id) REFERENCES monitored_endpoint (id) ON DELETE CASCADE
)