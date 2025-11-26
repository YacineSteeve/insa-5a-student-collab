package com.blyweertboukari.studentcollab.student.controller;

import com.blyweertboukari.studentcollab.student.dto.LoginDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentRegistrationDTO;
import com.blyweertboukari.studentcollab.student.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
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
}
