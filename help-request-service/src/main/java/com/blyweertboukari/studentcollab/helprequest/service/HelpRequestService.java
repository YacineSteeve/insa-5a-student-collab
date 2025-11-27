package com.blyweertboukari.studentcollab.helprequest.service;

import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestCreationDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestsFilters;
import com.blyweertboukari.studentcollab.helprequest.exceptions.NotFoundException;
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

    public HelpRequestDTO createHelpRequest(long authorId, HelpRequestCreationDTO dto) {
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setAuthorId(authorId);
        helpRequest.setTitle(dto.getTitle());
        helpRequest.setDescription(dto.getDescription());
        helpRequest.setKeywords(dto.getKeywords());
        helpRequest.setDesiredDate(dto.getDesiredDate());
        helpRequest.setStatus(HelpRequest.Status.WAITING);

        helpRequest = helpRequestRepository.save(helpRequest);
        return toDTO(helpRequest);
    }

    public HelpRequestDTO getHelpRequestById(Long id) {
        HelpRequest helpRequest = helpRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Help request not found"));
        return toDTO(helpRequest);
    }

    public HelpRequestDTO updateHelpRequest(Long authorId, Long helpRequestId, HelpRequestCreationDTO dto) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new NotFoundException("Help request not found"));

        helpRequest.setTitle(dto.getTitle());
        helpRequest.setDescription(dto.getDescription());
        helpRequest.setKeywords(dto.getKeywords());
        helpRequest.setDesiredDate(dto.getDesiredDate());

        helpRequest = helpRequestRepository.save(helpRequest);
        return toDTO(helpRequest);
    }

    public void deleteHelpRequest(Long authorId, Long id) {
        if (!helpRequestRepository.existsById(id)) {
            throw new NotFoundException("Help request not found");
        }
        helpRequestRepository.deleteById(id);
    }

    public List<HelpRequestDTO> getAllHelpRequests(HelpRequestsFilters filters) {
        return helpRequestRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HelpRequestDTO> getHelpRequestsForUser(long authorId, HelpRequestsFilters filters) {
        return helpRequestRepository.findByAuthorId(authorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HelpRequestDTO> getHelpRequestsByStatus(HelpRequest.Status status) {
        return helpRequestRepository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HelpRequestDTO> getHelpRequestsByKeyword(String keyword) {
        return helpRequestRepository.findByKeywordsContaining(keyword).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private HelpRequestDTO toDTO(HelpRequest helpRequest) {
        HelpRequestDTO dto = new HelpRequestDTO();
        dto.setId(helpRequest.getId());
        dto.setStatus(helpRequest.getStatus());
        dto.setAssigneeId(helpRequest.getAssigneeId());
        dto.setAuthorId(helpRequest.getAuthorId());
        dto.setTitle(helpRequest.getTitle());
        dto.setDescription(helpRequest.getDescription());
        dto.setKeywords(helpRequest.getKeywords());
        dto.setDesiredDate(helpRequest.getDesiredDate());
        dto.setCreatedAt(helpRequest.getCreatedAt());
        dto.setUpdatedAt(helpRequest.getUpdatedAt());
        return dto;
    }
}
