package com.blyweertboukari.studentcollab.student.repository;

import com.blyweertboukari.studentcollab.student.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> allReviewsByStudentId(Long studentId);
    Review findByHelpRequestId(Long helpRequestId);
}
