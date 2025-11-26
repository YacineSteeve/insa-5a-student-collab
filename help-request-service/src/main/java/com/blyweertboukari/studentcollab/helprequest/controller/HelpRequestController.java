package com.blyweertboukari.studentcollab.helprequest.controller;

import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestDTO;
import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
import com.blyweertboukari.studentcollab.helprequest.service.HelpRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HelpRequestController {
    @Autowired
    private HelpRequestService helpRequestService;

    @PostMapping
    public ResponseEntity<HelpRequestDTO> createHelpRequest(@Valid @RequestBody HelpRequestDTO dto) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.createHelpRequest(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HelpRequestDTO> getHelpRequest(@PathVariable Long id) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.getHelpRequestById(id);
            return ResponseEntity.ok(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HelpRequestDTO> updateHelpRequest(@PathVariable Long id, @Valid @RequestBody HelpRequestDTO dto) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.updateHelpRequest(id, dto);
            return ResponseEntity.ok(helpRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<HelpRequestDTO> updateStatus(@PathVariable Long id, @RequestParam HelpRequest.StatutDemande statut) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.updateStatus(id, statut);
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
    public ResponseEntity<List<HelpRequestDTO>> getAllHelpRequests() {
        List<HelpRequestDTO> helpRequests = helpRequestService.getAllHelpRequests();
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsByStudent(@PathVariable Long studentId) {
        List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsByStudent(studentId);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/status/{statut}")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsByStatus(@PathVariable HelpRequest.StatutDemande statut) {
        List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsByStatus(statut);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsByType(@PathVariable HelpRequest.TypeDemande type) {
        List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsByType(type);
        return ResponseEntity.ok(helpRequests);
    }

    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<List<HelpRequestDTO>> getHelpRequestsByKeyword(@PathVariable String keyword) {
        List<HelpRequestDTO> helpRequests = helpRequestService.getHelpRequestsByKeyword(keyword);
        return ResponseEntity.ok(helpRequests);
    }
}
