package fr.insa.helprequest.service;

import fr.insa.helprequest.dto.HelpRequestDTO;
import fr.insa.helprequest.model.HelpRequest;
import fr.insa.helprequest.repository.HelpRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HelpRequestService {
    
    @Autowired
    private HelpRequestRepository helpRequestRepository;
    
    public HelpRequestDTO createHelpRequest(HelpRequestDTO dto) {
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setStudentId(dto.getStudentId());
        helpRequest.setTitre(dto.getTitre());
        helpRequest.setDescription(dto.getDescription());
        helpRequest.setMotsCles(dto.getMotsCles());
        helpRequest.setType(dto.getType());
        helpRequest.setDateCreation(LocalDateTime.now());
        helpRequest.setStatut(HelpRequest.StatutDemande.EN_ATTENTE);
        
        helpRequest = helpRequestRepository.save(helpRequest);
        return toDTO(helpRequest);
    }
    
    public HelpRequestDTO getHelpRequestById(Long id) {
        HelpRequest helpRequest = helpRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        return toDTO(helpRequest);
    }
    
    public HelpRequestDTO updateHelpRequest(Long id, HelpRequestDTO dto) {
        HelpRequest helpRequest = helpRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        
        helpRequest.setTitre(dto.getTitre());
        helpRequest.setDescription(dto.getDescription());
        helpRequest.setMotsCles(dto.getMotsCles());
        
        helpRequest = helpRequestRepository.save(helpRequest);
        return toDTO(helpRequest);
    }
    
    public HelpRequestDTO updateStatus(Long id, HelpRequest.StatutDemande statut) {
        HelpRequest helpRequest = helpRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        
        helpRequest.setStatut(statut);
        helpRequest = helpRequestRepository.save(helpRequest);
        return toDTO(helpRequest);
    }
    
    public void deleteHelpRequest(Long id) {
        if (!helpRequestRepository.existsById(id)) {
            throw new RuntimeException("Demande non trouvée");
        }
        helpRequestRepository.deleteById(id);
    }
    
    public List<HelpRequestDTO> getAllHelpRequests() {
        return helpRequestRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<HelpRequestDTO> getHelpRequestsByStudent(Long studentId) {
        return helpRequestRepository.findByStudentId(studentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<HelpRequestDTO> getHelpRequestsByStatus(HelpRequest.StatutDemande statut) {
        return helpRequestRepository.findByStatut(statut).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<HelpRequestDTO> getHelpRequestsByType(HelpRequest.TypeDemande type) {
        return helpRequestRepository.findByType(type).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<HelpRequestDTO> getHelpRequestsByKeyword(String keyword) {
        return helpRequestRepository.findByMotsClesContaining(keyword).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private HelpRequestDTO toDTO(HelpRequest helpRequest) {
        HelpRequestDTO dto = new HelpRequestDTO();
        dto.setId(helpRequest.getId());
        dto.setStudentId(helpRequest.getStudentId());
        dto.setTitre(helpRequest.getTitre());
        dto.setDescription(helpRequest.getDescription());
        dto.setMotsCles(helpRequest.getMotsCles());
        dto.setDateCreation(helpRequest.getDateCreation());
        dto.setStatut(helpRequest.getStatut());
        dto.setType(helpRequest.getType());
        return dto;
    }
}
