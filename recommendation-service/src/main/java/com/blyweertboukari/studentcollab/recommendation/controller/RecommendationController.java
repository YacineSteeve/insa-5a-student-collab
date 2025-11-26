package com.blyweertboukari.studentcollab.recommendation.controller;

import com.blyweertboukari.studentcollab.recommendation.dto.RecommendationDTO;
import com.blyweertboukari.studentcollab.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Recommendations", description = "Recommendation API")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @GetMapping("/recommendations")
    @Operation(summary = "Get Recommendations For Help Request")
    public ResponseEntity<List<RecommendationDTO>> getRecommendations(@RequestParam Long helpRequestId) {
        try {
            List<RecommendationDTO> recommendations = recommendationService.recommendStudentsForHelpRequest(helpRequestId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
