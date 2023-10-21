package pro.sky.hogwarts.service;

import io.micrometer.common.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.hogwarts.dto.FacultyDtoIn;
import pro.sky.hogwarts.dto.FacultyDtoOut;
import pro.sky.hogwarts.dto.StudentDtoOut;
import pro.sky.hogwarts.entity.Faculty;
import pro.sky.hogwarts.exception.FacultyNotFoundException;
import pro.sky.hogwarts.mapper.FacultyMapper;
import pro.sky.hogwarts.mapper.StudentMapper;
import pro.sky.hogwarts.repository.FacultyRepository;
import pro.sky.hogwarts.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FacultyService {
    private static final Logger LOG = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository,
                          FacultyMapper facultyMapper, StudentMapper studentMapper) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.facultyMapper = facultyMapper;
        this.studentMapper = studentMapper;
    }

    public FacultyDtoOut create(FacultyDtoIn facultyDtoIn) {
        LOG.info("Was invoked method CREATE with parameter");
        return facultyMapper.toDto(
                facultyRepository.save(
                        facultyMapper.toEntity(facultyDtoIn)));
    }

    public FacultyDtoOut update(long id, FacultyDtoIn facultyDtoIn) {
        LOG.info("Was invoked method UPDATE with id= {}", id);
        return facultyRepository.findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setColor(facultyDtoIn.getColor());
                    oldFaculty.setName(facultyDtoIn.getName());
                    return facultyMapper.toDto(facultyRepository.save(oldFaculty));
                })
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public FacultyDtoOut delete(long id) {
        LOG.info("Was invoked method DELETE with id= {}", id);
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }

    public FacultyDtoOut get(long id) {
        LOG.info("Was invoked method GET with id= {}", id);
        return facultyRepository.findById(id)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public List<FacultyDtoOut> findAll(@Nullable String color) {
        LOG.info("Was invoked method FIND_ALL_FACULTIES_BY_COLOR");
        return Optional.ofNullable(color)
                .map(facultyRepository::findAllByColor)
                .orElseGet(facultyRepository::findAll).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FacultyDtoOut> findByColorOrName(String colorOrName) {
        LOG.info("Was invoked method FIND_ALL_FACULTIES_BY_COLOR_OR_NAME");
        return facultyRepository.findByColorContainingIgnoreCaseOrNameContainingIgnoreCase(colorOrName, colorOrName)
                .stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findStudents(long id) {
        LOG.info("Was invoked method FIND_STUDENTS");
        return studentRepository.findAllStudentsByFacultyId(id).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public String getLongestFacultyName(){
        LOG.info("Was invoked method GET_LONGEST_FACULTY_NAME");
        return  facultyRepository.findAll().stream()
                .map(faculty -> faculty.getName())
                .max(Comparator.comparing(String::length))
                .get();
    }

    public Integer sum(){
        LOG.info("Was invoked method SUM");
        long time = System.currentTimeMillis();
        int res = Stream
                .iterate(1, a -> a +1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b );
        System.out.println("original: " + (System.currentTimeMillis() - time));
        return  res;
    }
    public Integer sumImproved1(){
        LOG.info("Was invoked method SUM_IMPROVED_1");
        long time = System.currentTimeMillis();
        int res = Stream
                .iterate(1, a -> a +1)
                //.parallel()
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b );

        System.out.println("improved_1: " + (System.currentTimeMillis() - time));
        return res;
    }
    public Integer sumImproved2(){
        LOG.info("Was invoked method SUM_IMPROVED_2");
        long time = System.currentTimeMillis();
        int res = Stream
                .iterate(1, a -> a +1)
                //.parallel()
                .limit(1_000_000)
                .reduce(0, Integer::sum);

        System.out.println("improved_2: " + (System.currentTimeMillis() - time));
        return res;
    }
}
