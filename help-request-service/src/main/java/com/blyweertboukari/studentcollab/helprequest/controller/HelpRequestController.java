package com.blyweertboukari.studentcollab.helprequest.controller;

import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestCreationDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestsFilters;
import com.blyweertboukari.studentcollab.helprequest.exceptions.NotFoundException;
import com.blyweertboukari.studentcollab.helprequest.service.HelpRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/help-requests")
@Tag(name = "Help Requests", description = "Help Request API")
public class HelpRequestController {
    @Autowired
    private HelpRequestService helpRequestService;

    @GetMapping
    @Operation(summary = "Get All Help Requests")
    public ResponseEntity<List<HelpRequestDTO>> getAllHelpRequests(
            @Valid @ModelAttribute HelpRequestsFilters filters
    ) {
        List<HelpRequestDTO> helpRequests = helpRequestService.getAllHelpRequests(filters);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/{helpRequestId}")
    @Operation(summary = "Get Help Request By ID")
    public ResponseEntity<HelpRequestDTO> getHelpRequest(@PathVariable Long helpRequestId) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.getHelpRequestById(helpRequestId);
            return ResponseEntity.ok(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Get Help Requests For Authenticated User")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsForAuthenticatedUser(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @ModelAttribute HelpRequestsFilters filters
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            long authorId = Long.parseLong(userId);
            List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsForUser(authorId, filters);
            return ResponseEntity.ok(helpRequests);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping
    @Operation(summary = "Create Help Request")
    public ResponseEntity<HelpRequestDTO> createHelpRequest(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody HelpRequestCreationDTO helpRequestCreationDTO
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            long authorId = Long.parseLong(userId);
            HelpRequestDTO helpRequest = helpRequestService.createHelpRequest(authorId, helpRequestCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{helpRequestId}")
    @Operation(summary = "Update Help Request By ID")
    public ResponseEntity<HelpRequestDTO> updateHelpRequest(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long helpRequestId,
            @Valid @RequestBody HelpRequestCreationDTO helpRequestCreationDTO
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long authorId = Long.parseLong(userId);
            HelpRequestDTO helpRequest = helpRequestService.updateHelpRequest(authorId, helpRequestId, helpRequestCreationDTO);
            return ResponseEntity.ok(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{helpRequestId}")
    @Operation(summary = "Delete Help Request By ID")
    public ResponseEntity<Void> deleteHelpRequest(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long helpRequestId
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long authorId = Long.parseLong(userId);
            helpRequestService.deleteHelpRequest(authorId, helpRequestId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
