--liquibase formatted sql

--changeset TSve:1
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM pg_catalog.pg_tables t INNER JOIN pg_indexes i ON i.tablename = t.tablename WHERE t.tablename = 'students' AND i.indexname = 'idx_students_name';
--rollback DROP INDEX idx_students_name
CREATE INDEX idx_students_name ON students(name);


--changeset TSve:2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM pg_catalog.pg_tables t INNER JOIN pg_indexes i ON i.tablename = t.tablename WHERE t.tablename = 'faculties' AND i.indexname = 'idx_faculties_name_color';
--rollback DROP INDEX idx_faculties_name_color
CREATE INDEX idx_faculties_name_color ON faculties(name, color);