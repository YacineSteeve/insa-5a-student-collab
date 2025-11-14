package fr.insa.helprequest.repository;

import fr.insa.helprequest.model.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    List<HelpRequest> findByStudentId(Long studentId);
    List<HelpRequest> findByStatut(HelpRequest.StatutDemande statut);
    List<HelpRequest> findByType(HelpRequest.TypeDemande type);
    List<HelpRequest> findByMotsClesContaining(String keyword);
}
