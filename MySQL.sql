DROP DATABASE IF EXISTS soundgood_music_school;
CREATE DATABASE soundgood_music_school;
USE soundgood_music_school;

set sql_mode=' ';

CREATE TABLE charge (
 charge_id VARCHAR(10) NOT NULL,
 charge_date TIMESTAMP(6) NOT NULL
);


ALTER TABLE charge ADD CONSTRAINT PK_Charge PRIMARY KEY (charge_id);


CREATE TABLE email (
 email_id VARCHAR(10) NOT NULL,
 email_Address VARCHAR(100) NOT NULL
);

ALTER TABLE email ADD CONSTRAINT PK_Email PRIMARY KEY (email_id);


CREATE TABLE phone (
 phone_id INT NOT NULL,
 phone_number VARCHAR(500) NOT NULL
);

ALTER TABLE Phone ADD CONSTRAINT PK_Phone PRIMARY KEY (phone_id);



CREATE TABLE sibling_discount (
 amount VARCHAR(100),
 number_of_attending_siblings INT
);


CREATE TABLE student (
 student_id VARCHAR (10),
 first_name VARCHAR(100) NOT NULL,
 last_name VARCHAR(100) NOT NULL,
 age INTEGER,
 person_number VARCHAR(20) NOT NULL,
 attending_siblings INTEGER,
 street VARCHAR(100) NOT NULL,
 city VARCHAR(100),
 zip VARCHAR(100) NOT NULL,
 
 PRIMARY KEY (student_id)
);


CREATE TABLE student_email (
 email_id VARCHAR(10) NOT NULL,
 student_id VARCHAR(10) NOT NULL
);

ALTER TABLE student_email ADD CONSTRAINT PK_Student_email PRIMARY KEY (email_id,student_id);


CREATE TABLE student_phone (
 phone_id INT NOT NULL,
 student_id VARCHAR(10) NOT NULL
);

ALTER TABLE student_phone ADD CONSTRAINT PK_Student_phone PRIMARY KEY (phone_id,student_id);


CREATE TABLE instructor (
 instructors_id INT NOT NULL,
 student_id VARCHAR(10) NOT NULL,
 first_name VARCHAR(100) NOT NULL,
 last_name VARCHAR(100) NOT NULL,
 age INT,
 person_number VARCHAR(20) NOT NULL,
 FOREIGN KEY (student_id) REFERENCES Student (student_id)
);


ALTER TABLE instructor ADD CONSTRAINT PK_Instructor PRIMARY KEY (instructors_id,student_id);




CREATE TABLE audition (
 instructors_id INT NOT NULL,
 student_id VARCHAR(10) NOT NULL,
 acceptance_status VARCHAR(20) NOT NULL
);

ALTER TABLE audition ADD CONSTRAINT PK_Audition PRIMARY KEY (instructors_id,student_id);


CREATE TABLE instructor_phone (
 phone_id INT NOT NULL,
 instructors_id INT NOT NULL,
 student_id VARCHAR(10) NOT NULL
);

ALTER TABLE Instructor_phone ADD CONSTRAINT PK_Instructor_phone PRIMARY KEY (phone_id,instructors_id,student_id);


CREATE TABLE instructors_email (
 email_id VARCHAR(10) NOT NULL,
 instructors_id INT NOT NULL,
 student_id VARCHAR(10) NOT NULL
);

ALTER TABLE Instructors_email ADD CONSTRAINT PK_Instructors_email PRIMARY KEY (email_id,instructors_id,student_id);


CREATE TABLE parent (
 parental_id INT NOT NULL,
 student_id VARCHAR(10) NOT NULL,
 first_name VARCHAR(200),
 last_name VARCHAR(200),
 attending_children  INT
);

ALTER TABLE Parent ADD CONSTRAINT PK_Parent PRIMARY KEY (parental_id,student_id);


CREATE TABLE parent_phone (
 phone_id INT NOT NULL,
 parental_id INT NOT NULL,
 student_id VARCHAR(10) NOT NULL
);

ALTER TABLE parent_phone ADD CONSTRAINT PK_Parent_phone PRIMARY KEY (phone_id,parental_id,student_id);


CREATE TABLE payment (
 payment_id INT NOT NULL,
 student_id VARCHAR(10) NOT NULL,
 charge_id VARCHAR(10) NOT NULL,
 accumulated_payment VARCHAR(100) NOT NULL
);

ALTER TABLE payment ADD CONSTRAINT PK_Payment PRIMARY KEY (payment_id,student_id,charge_id);


CREATE TABLE school (
 student_id VARCHAR(10),
 school_name VARCHAR(100) NOT NULL,
 street VARCHAR(100) NOT NULL,
 city VARCHAR(100),
 zip VARCHAR(100) NOT NULL
);

 ALTER TABLE school ADD CONSTRAINT PK_School PRIMARY KEY (student_id);




CREATE TABLE waiting_list (
 student_id VARCHAR(10) NOT NULL,
 callback_acceptance_status VARCHAR(15) NOT NULL
);

ALTER TABLE waiting_list ADD CONSTRAINT PK_Waiting_list PRIMARY KEY (student_id);


CREATE TABLE application (
 student_id VARCHAR(10) NOT NULL,
 status_of_application VARCHAR(20) NOT NULL,
 skill_level VARCHAR(20)
);


ALTER TABLE application ADD CONSTRAINT PK_Application PRIMARY KEY (student_id);


CREATE TABLE ensemble (
 student_id VARCHAR(10) NOT NULL,
 ensemble_id CHAR(10) NOT NULL,
 charge_id VARCHAR(10) NOT NULL,
 difficulty_level VARCHAR(20),
 genre VARCHAR(20), 
 date_of_ensemble DATE,
 instructors_id INT NOT NULL,
 max_capacity_seats VARCHAR(30) NOT NULL,
 occupied_seats VARCHAR (30),
 
 FOREIGN KEY(instructors_id) REFERENCES Instructor (instructors_id)
);



ALTER TABLE ensemble ADD CONSTRAINT PK_Ensemble PRIMARY KEY (student_id,ensemble_id,charge_id);



CREATE TABLE group_lesson (
 student_id VARCHAR(10) NOT NULL,
 group_lesson_id CHAR(10) NOT NULL,
 charge_id VARCHAR(10) NOT NULL,
 difficulty_level VARCHAR(10),
 genre CHAR(10),
 date_of_group_lesson DATE,
 instructors_id INT NOT NULL,
 instrument VARCHAR(10),
 FOREIGN KEY(instructors_id) REFERENCES Instructor (instructors_id)
);



ALTER TABLE group_lesson ADD CONSTRAINT PK_Group_lesson PRIMARY KEY (student_id,group_lesson_id,charge_id);


CREATE TABLE individual_lesson (
 student_id VARCHAR(10) NOT NULL,
 individual_lesson_id INT (10) NOT NULL,
 charge_id VARCHAR(10) NOT NULL,
 difficulty_level VARCHAR(10),
 date_of_lesson DATE,
 instructors_id INT NOT NULL,
 FOREIGN KEY(instructors_id) REFERENCES Instructor (instructors_id)
);


ALTER TABLE individual_lesson ADD CONSTRAINT PK_Individual_lesson PRIMARY KEY (student_id,individual_lesson_id,charge_id);



CREATE TABLE instrument_catalog (
 instrument_id VARCHAR(10),
 leased date,
 price VARCHAR(10) NOT NULL,
 instrument_name VARCHAR(10),
 instrument_brand VARCHAR(20),
 monthly_rental_fee VARCHAR(10) NOT NULL,
 rented VARCHAR(10),
 returned date,
CONSTRAINT serial PRIMARY KEY (instrument_id)


 
);





CREATE TABLE lease (
 instrument_id VARCHAR(10),
 student_id VARCHAR(10),
 time_period date, 
 rental_status VARCHAR(20),
 CONSTRAINT serial PRIMARY KEY (instrument_id, student_id),
 FOREIGN KEY (instrument_id) REFERENCES Instrument_catalog (instrument_id),
 FOREIGN KEY (student_id) REFERENCES Student (student_id)

);






CREATE TABLE stock (
 availability VARCHAR(10) NOT NULL,
 instrument_id VARCHAR(10) NOT NULL,
 student_id VARCHAR(10),
 CONSTRAINT serial PRIMARY KEY (instrument_id, student_id),
 FOREIGN KEY (instrument_id) REFERENCES instrument_catalog (instrument_id),
 FOREIGN KEY (student_id) REFERENCES Student (student_id)
);



ALTER TABLE student_email ADD CONSTRAINT FK_Student_email_0 FOREIGN KEY (email_id) REFERENCES Email (email_id);
ALTER TABLE student_email ADD CONSTRAINT FK_Student_email_1 FOREIGN KEY (student_id) REFERENCES Student (student_id);


ALTER TABLE student_phone ADD CONSTRAINT FK_Student_phone_0 FOREIGN KEY (phone_id) REFERENCES Phone (phone_id);
ALTER TABLE student_phone ADD CONSTRAINT FK_Student_phone_1 FOREIGN KEY (student_id) REFERENCES Student (student_id);


ALTER TABLE instructor ADD CONSTRAINT FK_Instructor_0 FOREIGN KEY (student_id) REFERENCES Student (student_id);



ALTER TABLE audition ADD CONSTRAINT FK_Audition_0 FOREIGN KEY (instructors_id,student_id) REFERENCES Instructor (instructors_id,student_id);


ALTER TABLE instructor_phone ADD CONSTRAINT FK_Instructor_phone_0 FOREIGN KEY (phone_id) REFERENCES Phone (phone_id);
ALTER TABLE instructor_phone ADD CONSTRAINT FK_Instructor_phone_1 FOREIGN KEY (instructors_id,student_id) REFERENCES Instructor (instructors_id,student_id);


ALTER TABLE instructors_email ADD CONSTRAINT FK_Instructors_email_0 FOREIGN KEY (email_id) REFERENCES Email (email_id);
ALTER TABLE instructors_email ADD CONSTRAINT FK_Instructors_email_1 FOREIGN KEY (instructors_id,student_id) REFERENCES Instructor (instructors_id,student_id);


ALTER TABLE parent ADD CONSTRAINT FK_Parent_0 FOREIGN KEY (student_id) REFERENCES Student (student_id);


ALTER TABLE parent_phone ADD CONSTRAINT FK_Parent_phone_0 FOREIGN KEY (phone_id) REFERENCES Phone (phone_id);
ALTER TABLE parent_phone ADD CONSTRAINT FK_Parent_phone_1 FOREIGN KEY (parental_id,student_id) REFERENCES Parent (parental_id,student_id);


ALTER TABLE payment ADD CONSTRAINT FK_Payment_0 FOREIGN KEY (student_id) REFERENCES Student (student_id);
ALTER TABLE payment ADD CONSTRAINT FK_Payment_1 FOREIGN KEY (charge_id) REFERENCES Charge (charge_id);


ALTER TABLE school ADD CONSTRAINT FK_School_0 FOREIGN KEY (student_id) REFERENCES Student (student_id);


ALTER TABLE waiting_list ADD CONSTRAINT FK_Waiting_list_0 FOREIGN KEY (student_id) REFERENCES School (student_id);


ALTER TABLE application ADD CONSTRAINT FK_Application_0 FOREIGN KEY (student_id) REFERENCES School (student_id);


ALTER TABLE ensemble ADD CONSTRAINT FK_Ensemble_0 FOREIGN KEY (student_id) REFERENCES Application (student_id);
ALTER TABLE ensemble ADD CONSTRAINT FK_Ensemble_1 FOREIGN KEY (charge_id) REFERENCES Charge (charge_id);





ALTER TABLE group_lesson ADD CONSTRAINT FK_Group_lesson_0 FOREIGN KEY (student_id) REFERENCES Application (student_id);
ALTER TABLE group_lesson ADD CONSTRAINT FK_Group_lesson_1 FOREIGN KEY (charge_id) REFERENCES Charge (charge_id);


ALTER TABLE individual_lesson ADD CONSTRAINT FK_Individual_lesson_0 FOREIGN KEY (student_id) REFERENCES Application (student_id);
ALTER TABLE individual_lesson ADD CONSTRAINT FK_Individual_lesson_1 FOREIGN KEY (charge_id) REFERENCES Charge (charge_id);



