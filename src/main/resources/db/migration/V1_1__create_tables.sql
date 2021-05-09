CREATE TABLE faculties
(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE departments
(
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    faculty_id INT REFERENCES faculties(id)
);

CREATE TABLE groups
(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    department_id INT NOT NULL,
    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE auditoriums
(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    department_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE persons
(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(50)
);

CREATE TABLE students
(
    person_id INT NOT NULL UNIQUE,
    group_id INT NOT NULL,
    CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES persons(id),
    CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE teachers
(
    person_id INT NOT NULL UNIQUE,
    department_id INT NOT NULL,
    CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES persons(id),
    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE lectures
(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    num INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    start TIME NOT NULL,
    finish TIME NOT NULL);

CREATE TABLE subjects
(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE  timetable_items
(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    date DATE NOT NULL,
    lecture_id INT NOT NULL,
    teacher_id INT NOT NULL,
    subject_id INT NOT NULL,
    auditorium_id INT NOT NULL,
    CONSTRAINT fk_lecture FOREIGN KEY (lecture_id) REFERENCES lectures(id),
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(person_id),
    CONSTRAINT fk_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_auditorium FOREIGN KEY (auditorium_id) REFERENCES auditoriums(id)
);

CREATE TABLE timetable_items_groups
(
    timetable_item_id INT NOT NULL,
    group_id INT NOT NULL,
    CONSTRAINT key_timetable_item_group UNIQUE (timetable_item_id, group_id),
    CONSTRAINT fk_timetable_item FOREIGN KEY (timetable_item_id) REFERENCES timetable_items(id),
    CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE teacher_subjects
(
    teacher_id INT NOT NULL,
    subject_id INT NOT NULL,
    CONSTRAINT key_teacher_subject UNIQUE (teacher_id, subject_id),
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(person_id),
    CONSTRAINT fk_subject FOREIGN KEY (subject_id) REFERENCES subjects(id)
);
