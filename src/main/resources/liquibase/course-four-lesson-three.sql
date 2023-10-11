--liquibase formatted sql

--changeset TSve:1
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM pg_catalog.pg_tables t INNER JOIN pg_indexes i ON i.tablename = t.tablename WHERE t.tablename = 'students' AND i.indexname = 'IDX_students_name';
CREATE INDEX IDX_students_name ON students(name);

--changeset TSve:2
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM pg_catalog.pg_tables t INNER JOIN pg_indexes i ON i.tablename = t.tablename WHERE t.tablename = 'faculties' AND i.indexname = 'IDX_faculties_name_color';
CREATE INDEX IDX_faculties_name_color ON faculties(name, color);