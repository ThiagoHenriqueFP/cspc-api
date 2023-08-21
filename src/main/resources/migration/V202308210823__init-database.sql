-- roles
CREATE TABLE roles (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   CONSTRAINT pk_roles PRIMARY KEY (id)
);

-- user
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT NOT NULL,
   first_name VARCHAR(255) NULL,
   last_name VARCHAR(255) NULL,
   email VARCHAR(255) NULL,
   password VARCHAR(255) NULL,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles (
  role_id BIGINT NOT NULL,
   user_id BIGINT NOT NULL
);

ALTER TABLE users ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users_roles ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE users_roles ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);

-- coordinator

CREATE TABLE coordinators (
  id BIGINT AUTO_INCREMENT NOT NULL,
   user_id BIGINT NULL,
   CONSTRAINT pk_coordinators PRIMARY KEY (id)
);

ALTER TABLE coordinators ADD CONSTRAINT FK_COORDINATORS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- classroom

CREATE TABLE classrooms (
  id BIGINT AUTO_INCREMENT NOT NULL,
   title VARCHAR(255) NULL,
   coordinator_id BIGINT NULL,
   CONSTRAINT pk_classrooms PRIMARY KEY (id)
);

ALTER TABLE classrooms ADD CONSTRAINT FK_CLASSROOMS_ON_COORDINATOR FOREIGN KEY (coordinator_id) REFERENCES coordinators (id);

-- instructor

CREATE TABLE instructors (
  id BIGINT AUTO_INCREMENT NOT NULL,
   user_id BIGINT NULL,
   classroom_id BIGINT NULL,
   CONSTRAINT pk_instructors PRIMARY KEY (id)
);

ALTER TABLE instructors ADD CONSTRAINT FK_INSTRUCTORS_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classrooms (id);

ALTER TABLE instructors ADD CONSTRAINT FK_INSTRUCTORS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- sm

CREATE TABLE scrum_masters (
  id BIGINT AUTO_INCREMENT NOT NULL,
   user_id BIGINT NULL,
   classroom_id BIGINT NULL,
   CONSTRAINT pk_scrum_masters PRIMARY KEY (id)
);

ALTER TABLE scrum_masters ADD CONSTRAINT FK_SCRUM_MASTERS_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classrooms (id);

ALTER TABLE scrum_masters ADD CONSTRAINT FK_SCRUM_MASTERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- grade

CREATE TABLE grades (
  id BIGINT AUTO_INCREMENT NOT NULL,
   communication DOUBLE NULL,
   collaboration DOUBLE NULL,
   autonomy DOUBLE NULL,
   quiz DOUBLE NULL,
   individual_challenge DOUBLE NULL,
   squad_challenge DOUBLE NULL,
   final_grade DOUBLE NULL,
   CONSTRAINT pk_grades PRIMARY KEY (id)
);

-- squad

CREATE TABLE squads (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   classroom_id BIGINT NULL,
   CONSTRAINT pk_squads PRIMARY KEY (id)
);

ALTER TABLE squads ADD CONSTRAINT FK_SQUADS_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classrooms (id);


-- student

CREATE TABLE students (
  id BIGINT AUTO_INCREMENT NOT NULL,
   grades_id BIGINT NULL,
   user_id BIGINT NULL,
   squad_id BIGINT NULL,
   classroom_id BIGINT NULL,
   CONSTRAINT pk_students PRIMARY KEY (id)
);

ALTER TABLE students ADD CONSTRAINT FK_STUDENTS_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classrooms (id);

ALTER TABLE students ADD CONSTRAINT FK_STUDENTS_ON_GRADES FOREIGN KEY (grades_id) REFERENCES grades (id);

ALTER TABLE students ADD CONSTRAINT FK_STUDENTS_ON_SQUAD FOREIGN KEY (squad_id) REFERENCES squads (id);

ALTER TABLE students ADD CONSTRAINT FK_STUDENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);