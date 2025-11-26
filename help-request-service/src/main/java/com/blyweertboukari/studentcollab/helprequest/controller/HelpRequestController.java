package com.blyweertboukari.studentcollab.helprequest.controller;

import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestCreationDTO;
import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
import com.blyweertboukari.studentcollab.helprequest.service.HelpRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/helprequests")
public class HelpRequestController {
    @Autowired
    private HelpRequestService helpRequestService;

    @PostMapping
    public ResponseEntity<HelpRequestCreationDTO> createHelpRequest(@Valid @RequestBody HelpRequestCreationDTO dto) {
        try {
            HelpRequestCreationDTO helpRequest = helpRequestService.createHelpRequest(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HelpRequestCreationDTO> getHelpRequest(@PathVariable Long id) {
        try {
            HelpRequestCreationDTO helpRequest = helpRequestService.getHelpRequestById(id);
            return ResponseEntity.ok(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HelpRequestCreationDTO> updateHelpRequest(@PathVariable Long id, @Valid @RequestBody HelpRequestCreationDTO dto) {
        try {
            HelpRequestCreationDTO helpRequest = helpRequestService.updateHelpRequest(id, dto);
            return ResponseEntity.ok(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<HelpRequestCreationDTO> updateStatus(@PathVariable Long id, @RequestParam HelpRequest.Status status) {
        try {
            HelpRequestCreationDTO helpRequest = helpRequestService.updateStatus(id, status);
            return ResponseEntity.ok(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHelpRequest(@PathVariable Long id) {
        try {
            helpRequestService.deleteHelpRequest(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<HelpRequestCreationDTO>> getAllHelpRequests() {
        List<HelpRequestCreationDTO> helpRequests = helpRequestService.getAllHelpRequests();
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<HelpRequestCreationDTO>> getHelpRequestsByStudent(@PathVariable Long studentId) {
        List<HelpRequestCreationDTO> helpRequests = helpRequestService.getHelpRequestsByStudent(studentId);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<HelpRequestCreationDTO>> getHelpRequestsByStatus(@PathVariable HelpRequest.Status status) {
        List<HelpRequestCreationDTO> helpRequests = helpRequestService.getHelpRequestsByStatus(status);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<List<HelpRequestCreationDTO>> getHelpRequestsByKeyword(@PathVariable String keyword) {
        List<HelpRequestCreationDTO> helpRequests = helpRequestService.getHelpRequestsByKeyword(keyword);
        return ResponseEntity.ok(helpRequests);
    }
}
