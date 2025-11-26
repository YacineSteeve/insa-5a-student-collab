package com.blyweertboukari.studentcollab.recommendation.service;

import com.blyweertboukari.studentcollab.recommendation.dto.HelpRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HelpRequestService {
    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String HELP_REQUEST_SERVICE_URI = "lb://help-request-service";

    public HelpRequestDTO getHelpRequestById(Long id) {
        return webClientBuilder.build()
                .get()
                .uri(HELP_REQUEST_SERVICE_URI + "/helprequests/" + id)
                .retrieve()
                .bodyToMono(HelpRequestDTO.class)
                .block();
    }
}