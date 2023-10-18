package pro.sky.hogwarts.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.hogwarts.dto.*;
import pro.sky.hogwarts.service.*;

import java.util.List;

@RestController
@Tag(name = "Контроллер по работе со студентами")
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentDtoOut createStudent(@RequestBody StudentDtoIn studentDtoIn) {
        return studentService.createStudent(studentDtoIn);
    }

    @GetMapping("/{id}")
    public StudentDtoOut getStudent(@PathVariable("id") long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("/{id}")
    public StudentDtoOut update(@PathVariable("id") long id, @RequestBody StudentDtoIn studentDtoIn) {
        return studentService.updateStudent(id, studentDtoIn);
    }

    @DeleteMapping("/{id}")
    public StudentDtoOut deleteStudent(@PathVariable("id") long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping
    public List<StudentDtoOut> findAllStudentsByAge(@RequestParam(required = false) Integer age) {
        return studentService.findAllStudentsByAge(age);
    }

    @GetMapping("/filter")
    public List<StudentDtoOut> findStudentsByAgeBetween(@RequestParam int ageFrom, @RequestParam int ageTo) {
        return studentService.findStudentsByAgeBetween(ageFrom, ageTo);
    }

    @GetMapping("/{id}/faculty")
    public FacultyDtoOut findFaculty(@PathVariable("id") long id) {
        return studentService.findFaculty(id);
    }

    @PatchMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StudentDtoOut uploadAvatar(@PathVariable("id") long id,
                                      @RequestPart("avatar") MultipartFile multipartFile) {
        return studentService.uploadAvatarToStudent(id, multipartFile);
    }

    @GetMapping("/count")
    public int getCountOfStudents(){
        return studentService.getCountOfStudents();
    }

    @GetMapping("/averageAge")
    public double getAverageAge(){
        return studentService.getAverageAge();
    }

    @GetMapping("/lastStudents")
    public List<StudentDtoOut> getLastStudents
            (@RequestParam(value ="count", defaultValue = "5", required = false) int count){
        return studentService.getLastStudents(Math.abs(count)); // модуль числа
    }
}
