package pro.sky.hogwarts.service;

import io.micrometer.common.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.hogwarts.dto.FacultyDtoOut;
import pro.sky.hogwarts.dto.StudentDtoIn;
import pro.sky.hogwarts.dto.StudentDtoOut;
import pro.sky.hogwarts.entity.Avatar;
import pro.sky.hogwarts.entity.Student;
import pro.sky.hogwarts.exception.FacultyNotFoundException;
import pro.sky.hogwarts.exception.StudentNotFoundException;
import pro.sky.hogwarts.mapper.FacultyMapper;
import pro.sky.hogwarts.mapper.StudentMapper;
import pro.sky.hogwarts.repository.FacultyRepository;
import pro.sky.hogwarts.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;
    private final AvatarService avatarService;

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository,
                          StudentMapper studentMapper, FacultyMapper facultyMapper, AvatarService avatarService) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
        this.avatarService = avatarService;
    }

    public StudentDtoOut createStudent(StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method CREATE with parameter");
        return studentMapper.toDto(
                studentRepository.save(
                        studentMapper.toEntity(studentDtoIn)));
    }

    public StudentDtoOut updateStudent(long id, StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method UPDATE with id= {}", id);
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(studentDtoIn.getName());
                    oldStudent.setAge(studentDtoIn.getAge());
                    Optional.ofNullable(studentDtoIn.getId())
                            .ifPresent(facultyId ->
                                    oldStudent.setFaculty(
                                            facultyRepository.findById(facultyId)
                                                    .orElseThrow(() -> new FacultyNotFoundException(facultyId))
                                    )
                            );
                    return studentMapper.toDto(studentRepository.save(oldStudent));
                })
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public StudentDtoOut deleteStudent(long id) {
        LOG.info("Was invoked method DELETE with id= {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }

    public StudentDtoOut getStudent(long id) {
        LOG.info("Was invoked method GET with id= {}", id);
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public List<StudentDtoOut> findAllStudentsByAge(@Nullable Integer age) {
        LOG.info("Was invoked method FIND_ALL_STUDENTS_BY_AGE");
        return Optional.ofNullable(age)
                .map(studentRepository::findAllStudentsByAge)
                .orElseGet(studentRepository::findAll).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findStudentsByAgeBetween(int ageFrom, int ageTo) {
        LOG.info("Was invoked method FIND_STUDENTS_BY_AGE_BETWEEN");
        return studentRepository.findStudentsByAgeBetween(ageFrom, ageTo).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public FacultyDtoOut findFaculty(long id) {
        LOG.info("Was invoked method FIND_FACULTY");
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public StudentDtoOut uploadAvatarToStudent(long id, MultipartFile multipartFile) {
        LOG.info("Was invoked method UPLOAD_AVATAR_TO_STUDENT");
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        avatarService.create(student, multipartFile);
        StudentDtoOut studentDtoOut = studentMapper.toDto(student);
        return studentDtoOut;
    }

    public int getCountOfStudents() {
        LOG.info("Was invoked method GET_COUNT_OF_STUDENTS");
        return studentRepository.getCountOfStudents();
    }

    public double getAverageAge() {
        LOG.info("Was invoked method GET_AVERAGE_AGE");
        return studentRepository.getAverageAge();
    }

    //вариант-1 (с проекцией)
//    public List<StudentDtoOut> getLastStudents(int count) {
//        return studentRepository.getLastStudents(Pageable.ofSize(count));
//    }

    //вариант-2 (без проекции, с маппингом)
    @Transactional(readOnly = true)
    public List<StudentDtoOut> getLastStudents(int count) {
        LOG.info("Was invoked method GET_LAST_STUDENTS");
        return studentRepository.getLastStudents(Pageable.ofSize(count)).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    //вариант-3 (нативный запрос)
//    public List<StudentDtoOut> getLastStudents(int count) {
//        return studentRepository.getLastStudents(count).stream()
//                .map(studentMapper :: toDto)
//                .collect(Collectors.toList());
//    }

    public List<String> getNamesStartsWithA(){
        LOG.info("Was invoked method GET_NAMES_STARTS_WITH_A");
        return studentRepository.findAll().stream()
                .map(student -> student.getName().toUpperCase())
                .filter(name -> name.startsWith("A"))
                //.filter(name -> name.startsWith("A") || name.startsWith("a"))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    public List<String> getNamesStartsWith(String letter){
        LOG.info("Was invoked method GET_NAMES_STARTS_WITH");
        return studentRepository.findAll().stream()
                .map(student -> student.getName().toUpperCase())
                .filter(name -> name.startsWith(letter))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    public double getAvgAgeByStreams() {
        LOG.info("Was invoked method GET_AVG_AGE_BY_STREAMS");

        //Вар-1:
//        List<Student> students = studentRepository.findAll();
//        int sumAge = students.stream()
//                .map(Student::getAge)
//                .reduce(Integer::sum)
//                .get();
//        return (double) sumAge / students.size();
//

        //Вар-2:
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .getAsDouble();

    }



}
