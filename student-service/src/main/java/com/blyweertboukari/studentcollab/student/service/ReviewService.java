package com.blyweertboukari.studentcollab.student.service;

import com.blyweertboukari.studentcollab.student.dto.ReviewCreationDTO;
import com.blyweertboukari.studentcollab.student.dto.ReviewDTO;
import com.blyweertboukari.studentcollab.student.exceptions.NotFoundException;
import com.blyweertboukari.studentcollab.student.model.Review;
import com.blyweertboukari.studentcollab.student.model.Student;
import com.blyweertboukari.studentcollab.student.repository.ReviewRepository;
import com.blyweertboukari.studentcollab.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private StudentRepository studentRepository;

    public ReviewDTO createReview(Long studentId, ReviewCreationDTO dto) {
        Review review = new Review();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        review.setStudent(student);
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setHelpRequestId(dto.getHelpRequestId());

        review = reviewRepository.save(review);

        return toDTO(review);
    }

    public List<ReviewDTO> getAllForStudent(Long studentId, Long helpRequestId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        if (helpRequestId != null) {
            return reviewRepository.findAllByStudentIdAndHelpRequestId(student.getId(), helpRequestId)
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }

        return reviewRepository.findAllByStudentId(student.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setHelpRequestId(review.getHelpRequestId());
        return dto;
    }
}
