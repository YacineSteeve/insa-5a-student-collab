package com.blyweertboukari.studentcollab.helprequest.dto;

import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequestsFilters {
    @Valid
    private List<@NotBlank String> keywords;

    @Valid
    private List<HelpRequest.Status> statuses;

    @Valid
    private Instant desiredDateFrom;

    @Valid
    private Instant desiredDateTo;
}
