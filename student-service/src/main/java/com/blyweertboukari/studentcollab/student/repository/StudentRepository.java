package com.blyweertboukari.studentcollab.student.repository;

import com.blyweertboukari.studentcollab.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Student> findBySkillsContaining(String skill);
    List<Student> findByMajor(Student.Major major);
}
