package com.blyweertboukari.studentcollab.recommendation.service;

import com.blyweertboukari.studentcollab.recommendation.dto.HelpRequestDTO;
import com.blyweertboukari.studentcollab.recommendation.dto.RecommendationDTO;
import com.blyweertboukari.studentcollab.recommendation.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private HelpRequestService helpRequestService;
    
    public List<RecommendationDTO> recommendStudentsForHelpRequest(Long helpRequestId) {
        HelpRequestDTO helpRequest = helpRequestService.getHelpRequestById(helpRequestId);
        List<StudentDTO> allStudents = studentService.getAllStudents();
        
        List<RecommendationDTO> recommendations = new ArrayList<>();
        
        for (StudentDTO student : allStudents) {
            if (student.getId().equals(helpRequest.getStudentId())) {
                continue;
            }
            
            double score = calculateMatchScore(helpRequest, student);
            String reason = generateReason(helpRequest, student, score);
            
            RecommendationDTO recommendation = new RecommendationDTO();
            recommendation.setStudent(student);
            recommendation.setScore(score);
            recommendation.setReason(reason);
            
            recommendations.add(recommendation);
        }
        
        return recommendations.stream()
                .sorted(Comparator.comparingDouble(RecommendationDTO::getScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
    
    private double calculateMatchScore(HelpRequestDTO helpRequest, StudentDTO student) {
        double score = 0.0;
        
        if (helpRequest.getMotsCles() != null && student.getSkills() != null) {
            long matchingCompetences = helpRequest.getMotsCles().stream()
                    .filter(keyword -> student.getSkills().stream()
                            .anyMatch(comp -> comp.toLowerCase().contains(keyword.toLowerCase())))
                    .count();
            
            if (!helpRequest.getMotsCles().isEmpty()) {
                score += (matchingCompetences * 40.0) / helpRequest.getMotsCles().size();
            }
        }
        
        if (student.getAvailabilities() != null && !student.getAvailabilities().isEmpty()) {
            score += 20.0;
        }
        
        if (student.getAverageReview() != null && student.getAverageReview() > 0) {
            score += student.getAverageReview() * 8.0;
        }
        
        return Math.min(score, 100.0);
    }
    
    private String generateReason(HelpRequestDTO helpRequest, StudentDTO student, double score) {
        List<String> reasons = new ArrayList<>();
        
        if (helpRequest.getMotsCles() != null && student.getSkills() != null) {
            long matchingCompetences = helpRequest.getMotsCles().stream()
                    .filter(keyword -> student.getSkills().stream()
                            .anyMatch(comp -> comp.toLowerCase().contains(keyword.toLowerCase())))
                    .count();
            
            if (matchingCompetences > 0) {
                reasons.add("Compétences correspondantes: " + matchingCompetences);
            }
        }
        
        if (student.getAvailabilities() != null && !student.getAvailabilities().isEmpty()) {
            reasons.add("Disponible");
        }
        
        if (student.getAverageReview() != null && student.getAverageReview() > 3.5) {
            reasons.add("Bonne réputation (note: " + String.format("%.1f", student.getAverageReview()) + "/5)");
        }
        
        if (reasons.isEmpty()) {
            return "Étudiant potentiellement intéressé";
        }
        
        return String.join(", ", reasons);
    }
}
