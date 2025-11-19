package com.blyweertboukari.studentcollab.student.service;

import com.blyweertboukari.studentcollab.student.dto.LoginDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentRegistrationDTO;
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
    
    public StudentDTO register(StudentRegistrationDTO registrationDTO) {
        if (studentRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Un étudiant avec cet email existe déjà");
        }
        
        Student student = new Student();
        student.setNom(registrationDTO.getNom());
        student.setEmail(registrationDTO.getEmail());
        student.setPassword(registrationDTO.getPassword());
        student.setEtablissement(registrationDTO.getEtablissement());
        student.setFiliere(registrationDTO.getFiliere());
        student.setCompetences(registrationDTO.getCompetences());
        student.setDisponibilites(registrationDTO.getDisponibilites());
        
        student = studentRepository.save(student);
        return toDTO(student);
    }
    
    public StudentDTO login(LoginDTO loginDTO) {
        Student student = studentRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));
        
        if (!student.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
        
        return toDTO(student);
    }
    
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return toDTO(student);
    }
    
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        student.setNom(studentDTO.getNom());
        student.setEtablissement(studentDTO.getEtablissement());
        student.setFiliere(studentDTO.getFiliere());
        student.setCompetences(studentDTO.getCompetences());
        student.setDisponibilites(studentDTO.getDisponibilites());
        
        student = studentRepository.save(student);
        return toDTO(student);
    }
    
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Étudiant non trouvé");
        }
        studentRepository.deleteById(id);
    }
    
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<StudentDTO> getStudentsByCompetence(String competence) {
        return studentRepository.findByCompetencesContaining(competence).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<StudentDTO> getStudentsByFiliere(String filiere) {
        return studentRepository.findByFiliere(filiere).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setNom(student.getNom());
        dto.setEmail(student.getEmail());
        dto.setEtablissement(student.getEtablissement());
        dto.setFiliere(student.getFiliere());
        dto.setCompetences(student.getCompetences());
        dto.setDisponibilites(student.getDisponibilites());
        dto.setMoyenneAvis(student.getMoyenneAvis());
        return dto;
    }
}
