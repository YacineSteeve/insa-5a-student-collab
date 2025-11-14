package fr.insa.recommendation.service;

import fr.insa.recommendation.client.HelpRequestClient;
import fr.insa.recommendation.client.StudentClient;
import fr.insa.recommendation.dto.HelpRequestDTO;
import fr.insa.recommendation.dto.RecommendationDTO;
import fr.insa.recommendation.dto.StudentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private StudentClient studentClient;

    @Mock
    private HelpRequestClient helpRequestClient;

    @InjectMocks
    private RecommendationService recommendationService;

    private HelpRequestDTO testHelpRequest;
    private StudentDTO student1;
    private StudentDTO student2;
    private StudentDTO student3;

    @BeforeEach
    void setUp() {
        testHelpRequest = new HelpRequestDTO();
        testHelpRequest.setId(1L);
        testHelpRequest.setStudentId(1L);
        testHelpRequest.setTitre("Besoin d'aide en Java");
        testHelpRequest.setDescription("Je cherche de l'aide pour comprendre les streams");
        testHelpRequest.setMotsCles(Arrays.asList("Java", "Spring Boot"));
        testHelpRequest.setDateCreation(LocalDateTime.now());
        testHelpRequest.setStatut("EN_ATTENTE");
        testHelpRequest.setType("DEMANDE_AIDE");

        student1 = new StudentDTO();
        student1.setId(1L);
        student1.setNom("Étudiant Demandeur");
        student1.setEmail("demandeur@insa.fr");
        student1.setCompetences(Arrays.asList("Python", "Django"));
        student1.setDisponibilites(Arrays.asList("Lundi 14h-16h"));
        student1.setMoyenneAvis(4.0);

        student2 = new StudentDTO();
        student2.setId(2L);
        student2.setNom("Expert Java");
        student2.setEmail("expert@insa.fr");
        student2.setCompetences(Arrays.asList("Java", "Spring Boot", "Hibernate"));
        student2.setDisponibilites(Arrays.asList("Mardi 10h-12h"));
        student2.setMoyenneAvis(4.5);

        student3 = new StudentDTO();
        student3.setId(3L);
        student3.setNom("Débutant Java");
        student3.setEmail("debutant@insa.fr");
        student3.setCompetences(Arrays.asList("Java"));
        student3.setDisponibilites(Arrays.asList());
        student3.setMoyenneAvis(3.0);
    }

    @Test
    void testRecommendStudentsForHelpRequest() {
        when(helpRequestClient.getHelpRequestById(1L)).thenReturn(testHelpRequest);
        when(studentClient.getAllStudents()).thenReturn(Arrays.asList(student1, student2, student3));

        List<RecommendationDTO> recommendations = recommendationService.recommendStudentsForHelpRequest(1L);

        assertNotNull(recommendations);
        assertTrue(recommendations.size() <= 10);
        
        // Student 1 (demandeur) should be excluded
        assertFalse(recommendations.stream().anyMatch(r -> r.getStudent().getId().equals(1L)));
        
        // Student 2 should be first (best match)
        assertEquals(2L, recommendations.get(0).getStudent().getId());
        assertTrue(recommendations.get(0).getScore() > recommendations.get(1).getScore());
    }

    @Test
    void testRecommendationScoreCalculation() {
        when(helpRequestClient.getHelpRequestById(1L)).thenReturn(testHelpRequest);
        when(studentClient.getAllStudents()).thenReturn(Arrays.asList(student2, student3));

        List<RecommendationDTO> recommendations = recommendationService.recommendStudentsForHelpRequest(1L);

        assertNotNull(recommendations);
        assertEquals(2, recommendations.size());
        
        // Student 2 (Expert Java) should have higher score than Student 3
        RecommendationDTO expertReco = recommendations.stream()
                .filter(r -> r.getStudent().getId().equals(2L))
                .findFirst()
                .orElse(null);
        
        RecommendationDTO beginnerReco = recommendations.stream()
                .filter(r -> r.getStudent().getId().equals(3L))
                .findFirst()
                .orElse(null);
        
        assertNotNull(expertReco);
        assertNotNull(beginnerReco);
        assertTrue(expertReco.getScore() > beginnerReco.getScore());
    }

    @Test
    void testRecommendationReason() {
        when(helpRequestClient.getHelpRequestById(1L)).thenReturn(testHelpRequest);
        when(studentClient.getAllStudents()).thenReturn(Arrays.asList(student2));

        List<RecommendationDTO> recommendations = recommendationService.recommendStudentsForHelpRequest(1L);

        assertNotNull(recommendations);
        assertEquals(1, recommendations.size());
        
        RecommendationDTO recommendation = recommendations.get(0);
        assertNotNull(recommendation.getRaison());
        assertTrue(recommendation.getRaison().contains("Compétences correspondantes") || 
                   recommendation.getRaison().contains("Disponible") ||
                   recommendation.getRaison().contains("Bonne réputation"));
    }

    @Test
    void testNoRecommendationsWhenOnlyRequester() {
        when(helpRequestClient.getHelpRequestById(1L)).thenReturn(testHelpRequest);
        when(studentClient.getAllStudents()).thenReturn(Arrays.asList(student1));

        List<RecommendationDTO> recommendations = recommendationService.recommendStudentsForHelpRequest(1L);

        assertNotNull(recommendations);
        assertEquals(0, recommendations.size());
    }

    @Test
    void testLimitToTop10Recommendations() {
        List<StudentDTO> manyStudents = Arrays.asList(
            student2, student3,
            createStudent(4L, "S4", Arrays.asList("Java")),
            createStudent(5L, "S5", Arrays.asList("Java")),
            createStudent(6L, "S6", Arrays.asList("Spring Boot")),
            createStudent(7L, "S7", Arrays.asList("Java")),
            createStudent(8L, "S8", Arrays.asList("Spring Boot")),
            createStudent(9L, "S9", Arrays.asList("Java")),
            createStudent(10L, "S10", Arrays.asList("Spring Boot")),
            createStudent(11L, "S11", Arrays.asList("Java")),
            createStudent(12L, "S12", Arrays.asList("Spring Boot")),
            createStudent(13L, "S13", Arrays.asList("Java"))
        );

        when(helpRequestClient.getHelpRequestById(1L)).thenReturn(testHelpRequest);
        when(studentClient.getAllStudents()).thenReturn(manyStudents);

        List<RecommendationDTO> recommendations = recommendationService.recommendStudentsForHelpRequest(1L);

        assertNotNull(recommendations);
        assertEquals(10, recommendations.size());
    }

    private StudentDTO createStudent(Long id, String nom, List<String> competences) {
        StudentDTO student = new StudentDTO();
        student.setId(id);
        student.setNom(nom);
        student.setEmail(nom.toLowerCase() + "@insa.fr");
        student.setCompetences(competences);
        student.setDisponibilites(Arrays.asList("Lundi 14h-16h"));
        student.setMoyenneAvis(3.5);
        return student;
    }
}
