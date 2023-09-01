package pro.sky.hogwarts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SchoolExceptionHandler {
    @ExceptionHandler({FacultyNotFoundException.class, StudentNotFoundException.class, AvatarNotFoundException.class})
    public ResponseEntity<?> handlerNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AvatarProcessingException.class)
    public ResponseEntity<?> handleInternalServerError() {
        return ResponseEntity.internalServerError().build();
    }
}
