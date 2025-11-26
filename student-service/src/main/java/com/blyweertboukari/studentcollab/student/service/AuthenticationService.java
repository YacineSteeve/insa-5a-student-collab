package com.blyweertboukari.studentcollab.student.service;

import com.blyweertboukari.studentcollab.student.dto.LoginDTO;
import com.blyweertboukari.studentcollab.student.dto.RegistrationDTO;
import com.blyweertboukari.studentcollab.student.exceptions.ConflictException;
import com.blyweertboukari.studentcollab.student.model.Student;
import com.blyweertboukari.studentcollab.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public Student signup(RegistrationDTO registrationDTO) {
        if (studentRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new ConflictException("A student with this email already exists");
        }

        Student student = new Student();
        student.setEmail(registrationDTO.getEmail());
        student.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        student.setFirstName(registrationDTO.getFirstName());
        student.setLastName(registrationDTO.getLastName());
        student.setEstablishment(registrationDTO.getEstablishment());
        student.setMajor(registrationDTO.getMajor());
        student.setSkills(registrationDTO.getSkills());
        student.setAvailabilities(registrationDTO.getAvailabilities());

        return studentRepository.save(student);
    }

    public Student authenticate(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        return studentRepository.findByEmail(loginDTO.getEmail()).orElseThrow();
    }
}
