package fr.insa.recommendation.controller;

import fr.insa.recommendation.dto.RecommendationDTO;
import fr.insa.recommendation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @GetMapping("/help-request/{helpRequestId}")
    public ResponseEntity<List<RecommendationDTO>> getRecommendations(@PathVariable Long helpRequestId) {
        try {
            List<RecommendationDTO> recommendations = recommendationService.recommendStudentsForHelpRequest(helpRequestId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
