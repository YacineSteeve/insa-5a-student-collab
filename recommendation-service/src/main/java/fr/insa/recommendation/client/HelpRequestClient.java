package fr.insa.recommendation.client;

import fr.insa.recommendation.dto.HelpRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "help-request-service")
public interface HelpRequestClient {
    
    @GetMapping("/api/help-requests/{id}")
    HelpRequestDTO getHelpRequestById(@PathVariable Long id);
}
