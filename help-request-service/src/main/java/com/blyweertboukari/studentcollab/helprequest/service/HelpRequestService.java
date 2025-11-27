package com.blyweertboukari.studentcollab.helprequest.service;

import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestCreationDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestUpdateDTO;
import com.blyweertboukari.studentcollab.helprequest.dto.HelpRequestsFilters;
import com.blyweertboukari.studentcollab.helprequest.exceptions.ForbiddenException;
import com.blyweertboukari.studentcollab.helprequest.exceptions.NotFoundException;
import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
import com.blyweertboukari.studentcollab.helprequest.repository.HelpRequestRepository;
import com.blyweertboukari.studentcollab.helprequest.repository.HelpRequestSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HelpRequestService {
    @Autowired
    private HelpRequestRepository helpRequestRepository;

    public HelpRequestDTO createHelpRequest(Long userId, HelpRequestCreationDTO dto) {
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setAuthorId(userId);
        helpRequest.setTitle(dto.getTitle());
        helpRequest.setDescription(dto.getDescription());
        helpRequest.setKeywords(dto.getKeywords());
        helpRequest.setDesiredDate(dto.getDesiredDate());
        helpRequest.setStatus(HelpRequest.Status.WAITING);

        helpRequest = helpRequestRepository.save(helpRequest);
        return toDTO(helpRequest);
    }

    public HelpRequestDTO getHelpRequestById(Long helpRequestId) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new NotFoundException("Help request not found"));
        return toDTO(helpRequest);
    }

    public HelpRequestDTO updateHelpRequest(Long userId, Long helpRequestId, HelpRequestUpdateDTO dto) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new NotFoundException("Help request not found"));

        if (!helpRequest.getAuthorId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to update this help request");
        }

        if (dto.getTitle() != null) helpRequest.setTitle(dto.getTitle());

        if (dto.getDescription() != null) helpRequest.setDescription(dto.getDescription());

        if (dto.getKeywords() != null) helpRequest.setKeywords(dto.getKeywords());

        if (dto.getDesiredDate() != null) helpRequest.setDesiredDate(dto.getDesiredDate());

        helpRequest = helpRequestRepository.save(helpRequest);

        return toDTO(helpRequest);
    }

    public HelpRequestDTO changeHelpRequestAssignee(Long userId, Long helpRequestId, Long assigneeId) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new NotFoundException("Help request not found"));

        if (helpRequest.getAuthorId().equals(userId) && assigneeId != null) {
            helpRequest.setAssigneeId(assigneeId);
        } else if (helpRequest.getAssigneeId().equals(userId) && assigneeId == null) {
            helpRequest.setAssigneeId(null);
        }

        helpRequest = helpRequestRepository.save(helpRequest);

        return toDTO(helpRequest);
    }

    public void deleteHelpRequest(Long userId, Long helpRequestId) {
        HelpRequest helpRequest = helpRequestRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Help request not found"));

        if (!helpRequest.getAuthorId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to delete this help request");
        }

        helpRequestRepository.deleteById(helpRequestId);
    }

    public List<HelpRequestDTO> getAllHelpRequests(HelpRequestsFilters filters) {
        Specification<HelpRequest> spec = buildSpecification(filters);

        return helpRequestRepository.findAll(spec)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<HelpRequestDTO> getHelpRequestsForUser(Long userId, HelpRequestsFilters filters) {
        Specification<HelpRequest> spec = buildSpecification(filters);

        spec = spec.and(HelpRequestSpecifications.hasAuthor(userId));

        return helpRequestRepository.findAll(spec)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<HelpRequestDTO> getHelpRequestsAssignedToUser(Long userId, HelpRequestsFilters filters) {
        Specification<HelpRequest> spec = buildSpecification(filters);

        spec = spec.and(HelpRequestSpecifications.hasAssignee(userId));

        return helpRequestRepository.findAll(spec)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private Specification<HelpRequest> buildSpecification(HelpRequestsFilters filters) {
        Specification<HelpRequest> spec = (root, query, cb) -> cb.conjunction();

        if (filters.getKeywords() != null && !filters.getKeywords().isEmpty()) {
            spec = spec.and(HelpRequestSpecifications.hasAllKeywords(filters.getKeywords()));
        }

        if (filters.getStatuses() != null && !filters.getStatuses().isEmpty()) {
            spec = spec.and(HelpRequestSpecifications.hasStatuses(filters.getStatuses()));
        }

        if (filters.getDesiredDateFrom() != null) {
            spec = spec.and(HelpRequestSpecifications.desiredDateFrom(filters.getDesiredDateFrom()));
        }

        if (filters.getDesiredDateTo() != null) {
            spec = spec.and(HelpRequestSpecifications.desiredDateTo(filters.getDesiredDateTo()));
        }

        return spec;
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
