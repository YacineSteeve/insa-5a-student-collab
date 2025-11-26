package com.blyweertboukari.studentcollab.helprequest.controller;

import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestCreationDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestDTO;
import com.blyweertboukari.studentcollab.helprequest.exceptions.NotFoundException;
import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
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

    @PostMapping
    @Operation(summary = "Create Help Request")
    public ResponseEntity<HelpRequestDTO> createHelpRequest(@Valid @RequestBody HelpRequestCreationDTO dto) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.createHelpRequest(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Help Request By ID")
    public ResponseEntity<HelpRequestDTO> getHelpRequest(@PathVariable Long id) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.getHelpRequestById(id);
            return ResponseEntity.ok(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Help Request By ID")
    public ResponseEntity<HelpRequestDTO> updateHelpRequest(@PathVariable Long id, @Valid @RequestBody HelpRequestCreationDTO dto) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.updateHelpRequest(id, dto);
            return ResponseEntity.ok(helpRequest);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Help Request By ID")
    public ResponseEntity<Void> deleteHelpRequest(@PathVariable Long id) {
        try {
            helpRequestService.deleteHelpRequest(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    @Operation(summary = "Get All Help Requests")
    public ResponseEntity<List<HelpRequestDTO>> getAllHelpRequests() {
        List<HelpRequestDTO> helpRequests = helpRequestService.getAllHelpRequests();
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/me")
    @Operation(summary = "Get Help Request For Authenticated User")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsForAuthenticatedUser() {
        List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsForAuthenticatedUser();
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get Help Requests By Status")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsByStatus(@PathVariable HelpRequest.Status status) {
        List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsByStatus(status);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/keyword/{keyword}")
    @Operation(summary = "Get Help Requests By Keyword")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsByKeyword(@PathVariable String keyword) {
        List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsByKeyword(keyword);
        return ResponseEntity.ok(helpRequests);
    }
}
