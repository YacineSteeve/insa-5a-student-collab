package com.blyweertboukari.studentcollab.student.controller;

import com.blyweertboukari.studentcollab.student.dto.StudentDTO;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        StudentDTO currentStudent = (StudentDTO) authentication.getPrincipal();

        return ResponseEntity.ok(currentStudent);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Student By ID")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        try {
            StudentDTO student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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

    @GetMapping("/skill/{skill}")
    @Operation(summary = "Get Students By Skill")
    public ResponseEntity<List<StudentDTO>> getStudentsBySkill(@PathVariable String skill) {
        List<StudentDTO> students = studentService.getStudentsBySkill(skill);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/major/{major}")
    @Operation(summary = "Get Students By Major")
    public ResponseEntity<List<StudentDTO>> getStudentsByMajor(@PathVariable String major) {
        List<StudentDTO> students = studentService.getStudentsByMajor(major);
        return ResponseEntity.ok(students);
    }
}
