package com.blyweertboukari.studentcollab.helprequest.service;

import com.blyweertboukari.studentcollab.helprequest.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StudentService {
    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String STUDENT_SERVICE_URL = "lb://student-service";

    public Mono<StudentDTO> getStudentById(Long id) {
        return webClientBuilder.build()
                .get()
                .uri(STUDENT_SERVICE_URL + "/students/" + id)
                .retrieve()
                .bodyToMono(StudentDTO.class);
    }
}
