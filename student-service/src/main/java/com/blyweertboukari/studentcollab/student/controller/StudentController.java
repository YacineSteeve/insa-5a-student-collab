package com.blyweertboukari.studentcollab.student.controller;

import com.blyweertboukari.studentcollab.student.dto.StudentDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentUpdateDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentsFilters;
import com.blyweertboukari.studentcollab.student.exceptions.NotFoundException;
import com.blyweertboukari.studentcollab.student.model.Student;
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

    private Student getAuthenticatedStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (Student) authentication.getPrincipal();
    }

    @GetMapping
    @Operation(summary = "Get All Students")
    public ResponseEntity<List<StudentDTO>> getAllStudents(@Valid @ModelAttribute StudentsFilters filters) {
        List<StudentDTO> students = studentService.getAllStudents(filters);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
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

    @GetMapping("/me")
    @Operation(summary = "Get authenticated Student")
    public ResponseEntity<StudentDTO> authenticatedStudent() {
        try {
            Student currentStudent = getAuthenticatedStudent();

            StudentDTO student = studentService.getStudentById(currentStudent.getId());

            return ResponseEntity.ok(student);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/me")
    @Operation(summary = "Update authenticated student profile")
    public ResponseEntity<StudentDTO> updateStudent(@Valid @RequestBody StudentUpdateDTO studentUpdateDTO) {
        try {
            Student currentStudent = getAuthenticatedStudent();

            StudentDTO updatedStudent = studentService.updateStudent(currentStudent.getId(), studentUpdateDTO);

            return ResponseEntity.ok(updatedStudent);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete authenticated student")
    public ResponseEntity<Void> deleteStudent() {
        try {
            Student currentStudent = getAuthenticatedStudent();

            studentService.deleteStudent(currentStudent.getId());

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
