package com.blyweertboukari.studentcollab.recommendation.service;

import com.blyweertboukari.studentcollab.recommendation.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String STUDENT_SERVICE_URI = "lb://student-service";

    public List<StudentDTO> getAllStudents() {
        return webClientBuilder.build()
                .get()
                .uri(STUDENT_SERVICE_URI + "/students")
                .retrieve()
                .bodyToFlux(StudentDTO.class)
                .collectList()
                .block();
    }
}