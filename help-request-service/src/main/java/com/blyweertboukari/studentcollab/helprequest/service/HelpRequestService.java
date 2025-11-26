package com.blyweertboukari.studentcollab.helprequest.service;

import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestCreationDTO;
import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
import com.blyweertboukari.studentcollab.helprequest.repository.HelpRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HelpRequestService {
    @Autowired
    private HelpRequestRepository helpRequestRepository;

    public HelpRequestCreationDTO createHelpRequest(HelpRequestCreationDTO dto) {
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setStudentId(dto.getStudentId());
        helpRequest.setTitle(dto.getTitle());
        helpRequest.setDescription(dto.getDescription());
        helpRequest.setKeywords(dto.getKeywords());
        helpRequest.setDesiredDate(dto.getDesiredDate());

        helpRequest = helpRequestRepository.save(helpRequest);
        return toDTO(helpRequest);
    }

    public HelpRequestCreationDTO getHelpRequestById(Long id) {
        HelpRequest helpRequest = helpRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Help request not found"));
        return toDTO(helpRequest);
    }

    public HelpRequestCreationDTO updateHelpRequest(Long id, HelpRequestCreationDTO dto) {
        HelpRequest helpRequest = helpRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Help request not found"));

        helpRequest.setTitle(dto.getTitle());
        helpRequest.setDescription(dto.getDescription());
        helpRequest.setKeywords(dto.getKeywords());
        helpRequest.setDesiredDate(dto.getDesiredDate());

        helpRequest = helpRequestRepository.save(helpRequest);
        return toDTO(helpRequest);
    }

    public HelpRequestCreationDTO updateStatus(Long id, HelpRequest.Status status) {
        HelpRequest helpRequest = helpRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Help request not found"));

        helpRequest.setStatus(status);
        helpRequest = helpRequestRepository.save(helpRequest);
        return toDTO(helpRequest);
    }

    public void deleteHelpRequest(Long id) {
        if (!helpRequestRepository.existsById(id)) {
            throw new RuntimeException("Help request not found");
        }
        helpRequestRepository.deleteById(id);
    }

    public List<HelpRequestCreationDTO> getAllHelpRequests() {
        return helpRequestRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HelpRequestCreationDTO> getHelpRequestsByStudent(Long studentId) {
        return helpRequestRepository.findByStudentId(studentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HelpRequestCreationDTO> getHelpRequestsByStatus(HelpRequest.Status status) {
        return helpRequestRepository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HelpRequestCreationDTO> getHelpRequestsByKeyword(String keyword) {
        return helpRequestRepository.findByKeywordsContaining(keyword).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private HelpRequestCreationDTO toDTO(HelpRequest helpRequest) {
        HelpRequestCreationDTO dto = new HelpRequestCreationDTO();
        dto.setId(helpRequest.getId());
        dto.setStudentId(helpRequest.getStudentId());
        dto.setTitle(helpRequest.getTitle());
        dto.setDescription(helpRequest.getDescription());
        dto.setKeywords(helpRequest.getKeywords());
        dto.setDesiredDate(helpRequest.getDesiredDate());
        return dto;
    }
}
