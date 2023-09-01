package pro.sky.hogwarts.mapper;

import org.springframework.stereotype.Component;
import pro.sky.hogwarts.dto.StudentDtoIn;
import pro.sky.hogwarts.dto.StudentDtoOut;
import pro.sky.hogwarts.entity.Student;
import pro.sky.hogwarts.exception.FacultyNotFoundException;
import pro.sky.hogwarts.repository.FacultyRepository;

import java.util.Optional;

@Component
public class StudentMapper {

    private final FacultyMapper facultyMapper;
    private final FacultyRepository facultyRepository;

    public StudentMapper(FacultyMapper facultyMapper, FacultyRepository facultyRepository) {
        this.facultyMapper = facultyMapper;
        this.facultyRepository = facultyRepository;
    }


    public StudentDtoOut toDto(Student student) {
        StudentDtoOut studentDtoOut = new StudentDtoOut();
        studentDtoOut.setId(student.getId());
        studentDtoOut.setName(student.getName());
        studentDtoOut.setAge(student.getAge());
        Optional.ofNullable(student.getFaculty())
                .ifPresent(faculty -> studentDtoOut.setFaculty((facultyMapper.toDto(faculty))));
        return studentDtoOut;
    }

    public Student toEntity(StudentDtoIn studentDtoIn) {
        Student student = new Student();
        student.setAge(studentDtoIn.getAge());
        student.setName(studentDtoIn.getName());
        Optional.ofNullable(studentDtoIn.getFacultyId())
                .ifPresent(facultyId ->
                        facultyRepository.findById(facultyId)
                                .orElseThrow(() -> new FacultyNotFoundException(facultyId))
                );
        return student;
    }
}
