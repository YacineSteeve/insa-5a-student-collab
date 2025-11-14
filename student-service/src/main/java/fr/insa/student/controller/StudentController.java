package fr.insa.student.controller;

import fr.insa.student.dto.LoginDTO;
import fr.insa.student.dto.StudentDTO;
import fr.insa.student.dto.StudentRegistrationDTO;
import fr.insa.student.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @PostMapping("/register")
    public ResponseEntity<StudentDTO> register(@Valid @RequestBody StudentRegistrationDTO registrationDTO) {
        try {
            StudentDTO student = studentService.register(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<StudentDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            StudentDTO student = studentService.login(loginDTO);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        try {
            StudentDTO student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        try {
            StudentDTO student = studentService.updateStudent(id, studentDTO);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/competence/{competence}")
    public ResponseEntity<List<StudentDTO>> getStudentsByCompetence(@PathVariable String competence) {
        List<StudentDTO> students = studentService.getStudentsByCompetence(competence);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/filiere/{filiere}")
    public ResponseEntity<List<StudentDTO>> getStudentsByFiliere(@PathVariable String filiere) {
        List<StudentDTO> students = studentService.getStudentsByFiliere(filiere);
        return ResponseEntity.ok(students);
    }
}
