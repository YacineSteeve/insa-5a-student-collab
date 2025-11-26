package com.blyweertboukari.studentcollab.student.controller;

import com.blyweertboukari.studentcollab.student.dto.LoginDTO;
import com.blyweertboukari.studentcollab.student.dto.LoginResponse;
import com.blyweertboukari.studentcollab.student.dto.RegistrationDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentDTO;
import com.blyweertboukari.studentcollab.student.model.Student;
import com.blyweertboukari.studentcollab.student.service.AuthenticationService;
import com.blyweertboukari.studentcollab.student.service.JwtService;
import com.blyweertboukari.studentcollab.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private StudentService studentService;

    @PostMapping("/signup")
    @Operation(summary = "Register student")
    public ResponseEntity<StudentDTO> register(@RequestBody RegistrationDTO registrationDTO) {
        Student student = authenticationService.signup(registrationDTO);

        StudentDTO studentDTO = studentService.toDTO(student);

        return ResponseEntity.ok(studentDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "Log student in")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDTO loginDTO) {
        Student authenticatedStudent = authenticationService.authenticate(loginDTO);

        String jwtToken = jwtService.generateToken(authenticatedStudent);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
