package com.blyweertboukari.studentcollab.student.service;

import com.blyweertboukari.studentcollab.student.dto.StudentDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentUpdateDTO;
import com.blyweertboukari.studentcollab.student.dto.StudentsFilters;
import com.blyweertboukari.studentcollab.student.exceptions.NotFoundException;
import com.blyweertboukari.studentcollab.student.model.Student;
import com.blyweertboukari.studentcollab.student.repository.StudentRepository;
import com.blyweertboukari.studentcollab.student.repository.StudentSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));
        return toDTO(student);
    }

    public StudentDTO updateStudent(Long id, StudentUpdateDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        if (dto.getLastName() != null)
            student.setLastName(dto.getLastName());

        if (dto.getFirstName() != null)
            student.setFirstName(dto.getFirstName());

        if (dto.getEstablishment() != null)
            student.setEstablishment(dto.getEstablishment());

        if (dto.getMajor() != null)
            student.setMajor(dto.getMajor());

        if (dto.getSkills() != null)
            student.setSkills(dto.getSkills());

        if (dto.getAvailabilities() != null)
            student.setAvailabilities(dto.getAvailabilities());

        student = studentRepository.save(student);
        return toDTO(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException("Student not found");
        }
        studentRepository.deleteById(id);
    }

    public List<StudentDTO> getAllStudents(StudentsFilters filters) {
        Specification<Student> spec = (root, query, cb) -> cb.conjunction();

        if (filters.getSkills() != null && !filters.getSkills().isEmpty()) {
            spec = spec.and(StudentSpecifications.hasAnySkills(filters.getSkills()));
        }

        if (filters.getMajors() != null && !filters.getMajors().isEmpty()) {
            spec = spec.and(StudentSpecifications.hasMajors(filters.getMajors()));
        }

        return studentRepository.findAll(spec)
                .stream()
                .map(this::toDTO)
                .toList();
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
