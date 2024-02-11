DROP TABLE IF EXISTS authentication_credentials;

CREATE TABLE authentication_credentials
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    jti           VARCHAR(36)  NOT NULL, -- UUID 길이로 설정
    access_token  VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    UNIQUE KEY uk_authentication_credentials_jti (jti)
);