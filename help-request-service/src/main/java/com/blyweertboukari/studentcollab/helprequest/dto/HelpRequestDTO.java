package com.blyweertboukari.studentcollab.helprequest.dto;

import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequestDTO {
    private Long id;
    private Long studentId;
    private String title;
    private String description;
    private List<String> keywords = new ArrayList<>();
    private Instant createdAt;
    private Instant desiredDate;
    private HelpRequest.Status status;
}
