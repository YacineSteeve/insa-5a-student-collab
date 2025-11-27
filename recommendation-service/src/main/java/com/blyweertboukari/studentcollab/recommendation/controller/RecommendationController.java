package com.blyweertboukari.studentcollab.recommendation.controller;

import com.blyweertboukari.studentcollab.recommendation.dto.RecommendationDTO;
import com.blyweertboukari.studentcollab.recommendation.exceptions.ForbiddenException;
import com.blyweertboukari.studentcollab.recommendation.exceptions.NotFoundException;
import com.blyweertboukari.studentcollab.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<RecommendationDTO>> getRecommendations(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestParam Long helpRequestId
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<RecommendationDTO> recommendations = recommendationService.recommendStudentsForHelpRequest(
                    Long.parseLong(userId),
                    helpRequestId
            );
            return ResponseEntity.ok(recommendations);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
