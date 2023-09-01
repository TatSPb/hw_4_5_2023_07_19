package pro.sky.hogwarts.mapper;

import org.springframework.stereotype.Component;
import pro.sky.hogwarts.dto.FacultyDtoIn;
import pro.sky.hogwarts.dto.FacultyDtoOut;
import pro.sky.hogwarts.entity.Faculty;

@Component
public class FacultyMapper {

    public FacultyDtoOut toDto(Faculty faculty) {
        FacultyDtoOut facultyDtoOut = new FacultyDtoOut();
        facultyDtoOut.setId(faculty.getId());
        facultyDtoOut.setName(faculty.getName());
        facultyDtoOut.setColor(faculty.getColor());
        return facultyDtoOut;
    }

    public Faculty toEntity(FacultyDtoIn facultyDtoIn) {
        Faculty faculty = new Faculty();
        faculty.setName(faculty.getName());
        faculty.setColor(faculty.getColor());
        return faculty;
    }
}
