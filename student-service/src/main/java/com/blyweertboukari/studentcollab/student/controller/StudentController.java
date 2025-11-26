package com.blyweertboukari.studentcollab.student.controller;

import com.blyweertboukari.studentcollab.student.dto.StudentDTO;
import com.blyweertboukari.studentcollab.student.exceptions.NotFoundException;
import com.blyweertboukari.studentcollab.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@Tag(name = "Students", description = "Student API")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/me")
    @Operation(summary = "Get authenticated Student")
    public ResponseEntity<StudentDTO> authenticatedStudent() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            StudentDTO currentStudent = (StudentDTO) authentication.getPrincipal();

            return ResponseEntity.ok(currentStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Student By ID")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        try {
            StudentDTO student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Student By ID")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        try {
            StudentDTO student = studentService.updateStudent(id, studentDTO);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Student By ID")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    @Operation(summary = "Get All Students")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
}
