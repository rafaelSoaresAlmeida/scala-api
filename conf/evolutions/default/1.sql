# --- !Ups

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO users (name, email) VALUES ('João Silva', 'joao.silva@exemplo.com');
INSERT INTO users (name, email) VALUES ('Maria Oliveira', 'maria.oliveira@exemplo.com');

# --- !Downs

DROP TABLE IF EXISTS users;