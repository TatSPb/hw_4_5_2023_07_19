package pro.sky.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hogwarts.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);
    List<Student> findByAgeBetween(int ageFrom, int ageTo);
    List<Student> findAllByFaculty_Id(long facultyId);
}
