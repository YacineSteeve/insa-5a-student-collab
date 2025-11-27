package com.blyweertboukari.studentcollab.helprequest.controller;

import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestCreationDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestUpdateDTO;
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
    @Operation(summary = "Get all help requests")
    public ResponseEntity<List<HelpRequestDTO>> getAllHelpRequests(
            @Valid @ModelAttribute HelpRequestsFilters filters
    ) {
        List<HelpRequestDTO> helpRequests = helpRequestService.getAllHelpRequests(filters);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/{helpRequestId}")
    @Operation(summary = "Get help request by ID")
    public ResponseEntity<HelpRequestDTO> getHelpRequest(@PathVariable Long helpRequestId) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.getHelpRequestById(helpRequestId);
            return ResponseEntity.ok(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/by-me")
    @Operation(summary = "Get help requests authored by authenticated user")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsForAuthenticatedUser(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @ModelAttribute HelpRequestsFilters filters
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsForUser(Long.parseLong(userId), filters);
            return ResponseEntity.ok(helpRequests);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/for-me")
    @Operation(summary = "Get help requests assigned to authenticated user")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsAssignedToAuthenticatedUser(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @ModelAttribute HelpRequestsFilters filters
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsAssignedToUser(Long.parseLong(userId), filters);
            return ResponseEntity.ok(helpRequests);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping
    @Operation(summary = "Create help request")
    public ResponseEntity<HelpRequestDTO> createHelpRequest(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody HelpRequestCreationDTO helpRequestCreationDTO
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            HelpRequestDTO helpRequest = helpRequestService.createHelpRequest(Long.parseLong(userId), helpRequestCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{helpRequestId}")
    @Operation(summary = "Update help request by ID")
    public ResponseEntity<HelpRequestDTO> updateHelpRequest(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long helpRequestId,
            @Valid @RequestBody HelpRequestUpdateDTO helpRequestUpdateDTO
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            HelpRequestDTO helpRequest = helpRequestService.updateHelpRequest(Long.parseLong(userId), helpRequestId, helpRequestUpdateDTO);
            return ResponseEntity.ok(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/{helpRequestId}/assignee")
    @Operation(summary = "Change help request assignee")
    public ResponseEntity<HelpRequestDTO> removeAssignee(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long helpRequestId,
            @RequestParam Long assigneeId
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            HelpRequestDTO helpRequest = helpRequestService.changeHelpRequestAssignee(Long.parseLong(userId), helpRequestId, assigneeId);
            return ResponseEntity.ok(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{helpRequestId}")
    @Operation(summary = "Delete help request by ID")
    public ResponseEntity<Void> deleteHelpRequest(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long helpRequestId
    ) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            helpRequestService.deleteHelpRequest(Long.parseLong(userId), helpRequestId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
