package com.blyweertboukari.studentcollab.student.service;

import com.blyweertboukari.studentcollab.student.dto.LoginDTO;
import com.blyweertboukari.studentcollab.student.dto.RegistrationDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentDTO;
import com.blyweertboukari.studentcollab.student.model.Student;
import com.blyweertboukari.studentcollab.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public StudentDTO register(RegistrationDTO registrationDTO) {
        if (studentRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("A student with this email already exists");
        }

        Student student = new Student();
        student.setLastName(registrationDTO.getLastName());
        student.setFirstName(registrationDTO.getFirstName());
        student.setEmail(registrationDTO.getEmail());
        student.setPassword(registrationDTO.getPassword());
        student.setEstablishment(registrationDTO.getEstablishment());
        student.setMajor(registrationDTO.getMajor());
        student.setSkills(registrationDTO.getSkills());
        student.setAvailabilities(registrationDTO.getAvailabilities());

        student = studentRepository.save(student);
        return toDTO(student);
    }

    public StudentDTO login(LoginDTO loginDTO) {
        Student student = studentRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!student.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return toDTO(student);
    }

    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return toDTO(student);
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setLastName(studentDTO.getLastName());
        student.setFirstName(studentDTO.getFirstName());
        student.setEstablishment(studentDTO.getEstablishment());
        student.setMajor(studentDTO.getMajor());
        student.setSkills(studentDTO.getSkills());
        student.setAvailabilities(studentDTO.getAvailabilities());

        student = studentRepository.save(student);
        return toDTO(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found");
        }
        studentRepository.deleteById(id);
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<StudentDTO> getStudentsByCompetence(String competence) {
        return studentRepository.findBySkillsContaining(competence).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<StudentDTO> getStudentsByFiliere(String filiere) {
        // Attempt to map string to enum; fallback to all if invalid
        try {
            Student.Major major = Student.Major.valueOf(filiere.toUpperCase());
            return studentRepository.findByMajor(major).stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            return List.of();
        }
    }

    public StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setLastName(student.getLastName());
        dto.setFirstName(student.getFirstName());
        dto.setEmail(student.getEmail());
        dto.setEstablishment(student.getEstablishment());
        dto.setMajor(student.getMajor());
        dto.setSkills(student.getSkills());
        dto.setAvailabilities(student.getAvailabilities());
        dto.setAverageRating(student.getAverageRating());
        return dto;
    }
}
