package fr.insa.helprequest.service;

import fr.insa.helprequest.dto.HelpRequestDTO;
import fr.insa.helprequest.model.HelpRequest;
import fr.insa.helprequest.repository.HelpRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelpRequestServiceTest {

    @Mock
    private HelpRequestRepository helpRequestRepository;

    @InjectMocks
    private HelpRequestService helpRequestService;

    private HelpRequest testHelpRequest;
    private HelpRequestDTO testDTO;

    @BeforeEach
    void setUp() {
        testHelpRequest = new HelpRequest();
        testHelpRequest.setId(1L);
        testHelpRequest.setStudentId(1L);
        testHelpRequest.setTitre("Besoin d'aide en Java");
        testHelpRequest.setDescription("Je cherche de l'aide pour comprendre les streams");
        testHelpRequest.setMotsCles(Arrays.asList("Java", "Streams"));
        testHelpRequest.setDateCreation(LocalDateTime.now());
        testHelpRequest.setStatut(HelpRequest.StatutDemande.EN_ATTENTE);
        testHelpRequest.setType(HelpRequest.TypeDemande.DEMANDE_AIDE);

        testDTO = new HelpRequestDTO();
        testDTO.setStudentId(1L);
        testDTO.setTitre("Besoin d'aide en Java");
        testDTO.setDescription("Je cherche de l'aide pour comprendre les streams");
        testDTO.setMotsCles(Arrays.asList("Java", "Streams"));
        testDTO.setType(HelpRequest.TypeDemande.DEMANDE_AIDE);
    }

    @Test
    void testCreateHelpRequest() {
        when(helpRequestRepository.save(any(HelpRequest.class))).thenReturn(testHelpRequest);

        HelpRequestDTO result = helpRequestService.createHelpRequest(testDTO);

        assertNotNull(result);
        assertEquals("Besoin d'aide en Java", result.getTitre());
        assertEquals(HelpRequest.StatutDemande.EN_ATTENTE, result.getStatut());
        verify(helpRequestRepository, times(1)).save(any(HelpRequest.class));
    }

    @Test
    void testGetHelpRequestById() {
        when(helpRequestRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));

        HelpRequestDTO result = helpRequestService.getHelpRequestById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Besoin d'aide en Java", result.getTitre());
    }

    @Test
    void testGetHelpRequestByIdNotFound() {
        when(helpRequestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> helpRequestService.getHelpRequestById(999L));
    }

    @Test
    void testUpdateHelpRequest() {
        HelpRequestDTO updateDTO = new HelpRequestDTO();
        updateDTO.setStudentId(1L);
        updateDTO.setTitre("Titre modifié");
        updateDTO.setDescription("Description modifiée");
        updateDTO.setMotsCles(Arrays.asList("Java", "Spring"));
        updateDTO.setType(HelpRequest.TypeDemande.DEMANDE_AIDE);

        when(helpRequestRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));
        when(helpRequestRepository.save(any(HelpRequest.class))).thenReturn(testHelpRequest);

        HelpRequestDTO result = helpRequestService.updateHelpRequest(1L, updateDTO);

        assertNotNull(result);
        verify(helpRequestRepository, times(1)).save(any(HelpRequest.class));
    }

    @Test
    void testUpdateStatus() {
        when(helpRequestRepository.findById(1L)).thenReturn(Optional.of(testHelpRequest));
        when(helpRequestRepository.save(any(HelpRequest.class))).thenReturn(testHelpRequest);

        HelpRequestDTO result = helpRequestService.updateStatus(1L, HelpRequest.StatutDemande.EN_COURS);

        assertNotNull(result);
        verify(helpRequestRepository, times(1)).save(any(HelpRequest.class));
    }

    @Test
    void testGetAllHelpRequests() {
        List<HelpRequest> helpRequests = Arrays.asList(testHelpRequest);
        when(helpRequestRepository.findAll()).thenReturn(helpRequests);

        List<HelpRequestDTO> result = helpRequestService.getAllHelpRequests();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Besoin d'aide en Java", result.get(0).getTitre());
    }

    @Test
    void testGetHelpRequestsByStudent() {
        List<HelpRequest> helpRequests = Arrays.asList(testHelpRequest);
        when(helpRequestRepository.findByStudentId(1L)).thenReturn(helpRequests);

        List<HelpRequestDTO> result = helpRequestService.getHelpRequestsByStudent(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStudentId());
    }

    @Test
    void testDeleteHelpRequest() {
        when(helpRequestRepository.existsById(1L)).thenReturn(true);
        doNothing().when(helpRequestRepository).deleteById(1L);

        helpRequestService.deleteHelpRequest(1L);

        verify(helpRequestRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteHelpRequestNotFound() {
        when(helpRequestRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> helpRequestService.deleteHelpRequest(999L));
        verify(helpRequestRepository, never()).deleteById(any());
    }
}
