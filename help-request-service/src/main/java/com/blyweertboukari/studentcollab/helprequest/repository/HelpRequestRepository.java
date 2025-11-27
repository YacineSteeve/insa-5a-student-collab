package com.blyweertboukari.studentcollab.helprequest.repository;

import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long>, JpaSpecificationExecutor<HelpRequest> {
    List<HelpRequest> findByAuthorId(Long authorId);
    List<HelpRequest> findByStatus(HelpRequest.Status status);
    List<HelpRequest> findByKeywordsContaining(String keyword);
}
