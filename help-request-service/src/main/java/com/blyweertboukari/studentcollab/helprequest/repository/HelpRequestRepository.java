package com.blyweertboukari.studentcollab.helprequest.repository;

import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    List<HelpRequest> findByStudentId(Long studentId);
    List<HelpRequest> findByStatus(HelpRequest.Status status);
    List<HelpRequest> findByKeywordsContaining(String keyword);
}
