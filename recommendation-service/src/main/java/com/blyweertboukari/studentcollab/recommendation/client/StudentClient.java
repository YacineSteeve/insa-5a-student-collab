package com.blyweertboukari.studentcollab.recommendation.client;

import com.blyweertboukari.studentcollab.recommendation.dto.StudentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "student-service")
public interface StudentClient {
    
    @GetMapping("/api/students")
    List<StudentDTO> getAllStudents();
    
    @GetMapping("/api/students/{id}")
    StudentDTO getStudentById(@PathVariable Long id);
    
    @GetMapping("/api/students/competence/{competence}")
    List<StudentDTO> getStudentsByCompetence(@PathVariable String competence);
}
