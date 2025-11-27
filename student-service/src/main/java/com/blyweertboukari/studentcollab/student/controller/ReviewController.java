package com.blyweertboukari.studentcollab.student.controller;

import com.blyweertboukari.studentcollab.student.dto.*;
import com.blyweertboukari.studentcollab.student.exceptions.NotFoundException;
import com.blyweertboukari.studentcollab.student.service.HelpRequestService;
import com.blyweertboukari.studentcollab.student.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@Tag(name = "reviews", description = "Review API")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HelpRequestService helpRequestService;

    @PostMapping
    @Operation(summary = "Review assignee student of a request")
    public ResponseEntity<ReviewDTO> getAuthenticatedStudent(@RequestHeader(value = "X-User-Id", required = false) String userId, @Valid @RequestBody ReviewCreationDTO reviewCreationDTO) {

        if (userId == null)  {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            HelpRequestDTO helpRequestDTO = helpRequestService.getHelpRequestById(reviewCreationDTO.getHelpRequestId());

            if (!helpRequestDTO.getAuthorId().equals(Long.parseLong(userId))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ReviewDTO reviewDTO = reviewService.createReview(helpRequestDTO.getAssigneeId(), reviewCreationDTO);
            return ResponseEntity.ok(reviewDTO);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
