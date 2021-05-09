INSERT INTO faculties (name) VALUES ('Computer science');
INSERT INTO departments (name, faculty_id) VALUES ('Programming', 1);
INSERT INTO departments (name, faculty_id) VALUES ('Security', 1);
INSERT INTO groups (name, department_id) values ('GR-1', 1);
INSERT INTO groups (name, department_id) values ('GR-2', 1);
INSERT INTO groups (name, department_id) values ('GR-3', 1);
INSERT INTO groups (name, department_id, deleted) values ('GR-DELETED', 1, true);
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Ivan', 'Ivanov', '2020-12-31', 'g. Ivanovo ul.Pushkina 1', '+71234567890', 'ivan@gmail.com');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Petr', 'Petrov', '2020-11-30', 'g. Habarovsk ul.Lermontova 1', '+72234567890', 'petr@mail.ru');
INSERT INTO persons (first_name, last_name, birthday, address, phone, email) VALUES ('Elena', 'Smirnova', '2020-10-19', 'g. Bobruysk ul.Nekrasova 4', '+73234567890', 'smirnova.e@yandex.ru');
INSERT INTO students (person_id, group_id, start_date) VALUES (1, 1, '2019-9-1');
INSERT INTO students (person_id, group_id, start_date) VALUES (2, 1, '2019-9-1');
INSERT INTO students (person_id, group_id, start_date, deleted) VALUES (3, 2, '2020-9-1', true);
