INSERT INTO faculties (name) values ('Faculty of Engineering');
INSERT INTO faculties (name) values ('Faculty of Physics');
INSERT INTO faculties (name) values ('Faculty of Information Technology');
INSERT INTO faculties (name, deleted) values ('Deleted faculty', 'true');

INSERT INTO departments (name, faculty_id) values ('Department of automotive industry', 1);
INSERT INTO departments (name, faculty_id) values ('Department of space development ', 1);
INSERT INTO departments (name, faculty_id) values ('Department of electrophysics', 2);
INSERT INTO departments (name, faculty_id) values ('Department of molecular physics', 2);
INSERT INTO departments (name, faculty_id) values ('Department of software development', 3);
INSERT INTO departments (name, faculty_id) values ('Department of system administration', 3);
INSERT INTO departments (name, faculty_id, deleted) values ('Deleted department', 3, 'true');

INSERT INTO groups (name, department_id) values ('GR-1', 1);
INSERT INTO groups (name, department_id) values ('GR-2', 1);
INSERT INTO groups (name, department_id) values ('AUTO-1', 1);
INSERT INTO groups (name, department_id) values ('AUTO-2', 1);
INSERT INTO groups (name, department_id) values ('SPACE-1', 2);
INSERT INTO groups (name, department_id) values ('SPACE-2', 2);
INSERT INTO groups (name, department_id) values ('MPH-1', 3);
INSERT INTO groups (name, department_id) values ('MPH-2', 3);
INSERT INTO groups (name, department_id) values ('SW-1', 4);
INSERT INTO groups (name, department_id) values ('SW-2', 4);
INSERT INTO groups (name, department_id) values ('SA-1', 5);
INSERT INTO groups (name, department_id) values ('SA-2', 5);

INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Ivan', 'Ivanov', '2020-12-31', 'g. Ivanovo ul.Pushkina 1', '+71234567890', 'ivan@gmail.com');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Petr', 'Petrov', '2020-11-30', 'g. Habarovsk ul.Lermontova 1', '+72234567890', 'petr@mail.ru');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Elena', 'Smirnova', '2020-10-19', 'g. Bobruysk ul.Nekrasova 4', '+73234567890', 'smirnova.e@yandex.ru');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Kung', 'Lao', '1992-08-09', 'g. Mortal kombat 2', '+1321321321', 'kunglao@mk.com');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Liu', 'Kang', '1992-08-09', 'g. Mortal kombat 2', '+1654654654', 'liukang@mk.com');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Sonya', 'Blade', '1992-08-09', 'g. Mortal kombat 2', '+198989898', 'sonya@mk.com');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Johny', 'Cage', '1992-08-09', 'g. Mortal kombat 2', '+1333666999', 'jcage@mk.com');
INSERT INTO students (person_id, group_id, start_date) VALUES (1, 1, '2019-9-1');
INSERT INTO students (person_id, group_id, start_date) VALUES (2, 1, '2019-9-1');
INSERT INTO students (person_id, group_id, start_date) VALUES (3, 2, '2020-9-1');
INSERT INTO students (person_id, group_id, start_date) VALUES (4, 3, '2020-9-1');
INSERT INTO students (person_id, group_id, start_date) VALUES (5, 3, '2020-9-1');
INSERT INTO students (person_id, group_id, start_date) VALUES (6, 4, '2020-9-1');
INSERT INTO students (person_id, group_id, start_date) VALUES (7, 4, '2020-9-1');

INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Ferdinand', 'Porsche', '1875-09-03', 'Stuttgart Meinstrasse 1', '+4912345678', 'porsche@gmail.com');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Elon', 'Musk', '1971-06-28', 'California Silicon valley 174', '+1111222333', 'musk_elon.e@paypal.com');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Nikola', 'Tesla', '1856-07-10', 'New York Manhattan 12', '+1999888777', 'tesla_nikola@mail.com');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Bill', 'Gates', '1955-10-28', 'Redmond IT park', '+1888777555', 'b.gates@microsoft.com');
INSERT INTO teachers (person_id, department_id, degree) VALUES (8, 1, 'PROFESSOR');
INSERT INTO teachers (person_id, department_id, degree) VALUES (9, 2, 'TEACHER');
INSERT INTO teachers (person_id, department_id, degree) VALUES (10, 3, 'ACADEMIC');
INSERT INTO teachers (person_id, department_id, degree) VALUES (11, 5, 'LECTURER');

INSERT INTO lectures (num, name, start, finish) values (1, 'First lecture',  '08:00:00', '09:00:00');
INSERT INTO lectures (num, name, start, finish) values (2, 'Second lecture', '09:20:00', '10:20:00');
INSERT INTO lectures (num, name, start, finish) values (3, 'Third lecture',  '10:40:00', '11:40:00');
INSERT INTO lectures (num, name, start, finish) values (4, 'Forth lecture',  '12:00:00', '13:00:00');

INSERT INTO subjects (name) values ('Engine creating');
INSERT INTO subjects (name) values ('Energy efficiency');
INSERT INTO subjects (name) values ('Electric cars developing');
INSERT INTO subjects (name) values ('General physics');
INSERT INTO subjects (name) values ('Electrophysics');
INSERT INTO subjects (name) values ('Object oriented programming');
INSERT INTO subjects (name) values ('Web programming');
INSERT INTO subjects (name) values ('Mobile programming');
INSERT INTO subjects (name) values ('Android operation system');
INSERT INTO subjects (name) values ('Windows operation system');

INSERT INTO auditoriums (name, department_id) values ('Auditorium-1', 1);
INSERT INTO auditoriums (name, department_id) values ('Auditorium-2', 1);
INSERT INTO auditoriums (name, department_id) values ('Auditorium-3', 1);
INSERT INTO auditoriums (name, department_id) values ('Auditorium-21', 2);
INSERT INTO auditoriums (name, department_id) values ('Auditorium-22', 2);
INSERT INTO auditoriums (name, department_id) values ('Auditorium-23', 2);
INSERT INTO auditoriums (name, department_id) values ('Auditorium-31', 3);
INSERT INTO auditoriums (name, department_id) values ('Auditorium-32', 3);
INSERT INTO auditoriums (name, department_id) values ('Auditorium-33', 3);

INSERT INTO teacher_subjects (teacher_id, subject_id) values (8, 1);
INSERT INTO teacher_subjects (teacher_id, subject_id) values (8, 2);
INSERT INTO teacher_subjects (teacher_id, subject_id) values (9, 3);
INSERT INTO teacher_subjects (teacher_id, subject_id) values (10, 4);
INSERT INTO teacher_subjects (teacher_id, subject_id) values (10, 5);
INSERT INTO teacher_subjects (teacher_id, subject_id) values (11, 7);
INSERT INTO teacher_subjects (teacher_id, subject_id) values (11, 10);
