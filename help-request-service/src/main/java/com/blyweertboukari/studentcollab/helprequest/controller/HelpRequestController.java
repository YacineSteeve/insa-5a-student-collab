package com.blyweertboukari.studentcollab.helprequest.controller;

import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestCreationDTO;
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
@RequestMapping("/helprequests")
@Tag(name = "Help Requests", description = "Help Request API")
public class HelpRequestController {
    @Autowired
    private HelpRequestService helpRequestService;

    @PostMapping
    @Operation(summary = "Create Help Request")
    public ResponseEntity<HelpRequestCreationDTO> createHelpRequest(@Valid @RequestBody HelpRequestCreationDTO dto) {
        try {
            HelpRequestCreationDTO helpRequest = helpRequestService.createHelpRequest(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Help Request By ID")
    public ResponseEntity<HelpRequestCreationDTO> getHelpRequest(@PathVariable Long id) {
        try {
            HelpRequestCreationDTO helpRequest = helpRequestService.getHelpRequestById(id);
            return ResponseEntity.ok(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Help Request By ID")
    public ResponseEntity<HelpRequestCreationDTO> updateHelpRequest(@PathVariable Long id, @Valid @RequestBody HelpRequestCreationDTO dto) {
        try {
            HelpRequestCreationDTO helpRequest = helpRequestService.updateHelpRequest(id, dto);
            return ResponseEntity.ok(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Get Help Request Status By ID")
    public ResponseEntity<HelpRequestCreationDTO> updateStatus(@PathVariable Long id, @RequestParam HelpRequest.Status status) {
        try {
            HelpRequestCreationDTO helpRequest = helpRequestService.updateStatus(id, status);
            return ResponseEntity.ok(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Help Request By ID")
    public ResponseEntity<Void> deleteHelpRequest(@PathVariable Long id) {
        try {
            helpRequestService.deleteHelpRequest(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    @Operation(summary = "Get All Help Requests")
    public ResponseEntity<List<HelpRequestCreationDTO>> getAllHelpRequests() {
        List<HelpRequestCreationDTO> helpRequests = helpRequestService.getAllHelpRequests();
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get Help Request By Student ID")
    public ResponseEntity<List<HelpRequestCreationDTO>> getHelpRequestsByStudent(@PathVariable Long studentId) {
        List<HelpRequestCreationDTO> helpRequests = helpRequestService.getHelpRequestsByStudent(studentId);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get Help Requests By Status")
    public ResponseEntity<List<HelpRequestCreationDTO>> getHelpRequestsByStatus(@PathVariable HelpRequest.Status status) {
        List<HelpRequestCreationDTO> helpRequests = helpRequestService.getHelpRequestsByStatus(status);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/keyword/{keyword}")
    @Operation(summary = "Get Help Requests By Keyword")
    public ResponseEntity<List<HelpRequestCreationDTO>> getHelpRequestsByKeyword(@PathVariable String keyword) {
        List<HelpRequestCreationDTO> helpRequests = helpRequestService.getHelpRequestsByKeyword(keyword);
        return ResponseEntity.ok(helpRequests);
    }
}
