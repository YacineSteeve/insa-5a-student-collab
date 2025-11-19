package com.blyweertboukari.studentcollab.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequestDTO {
    private Long id;
    private Long studentId;
    private String titre;
    private String description;
    private List<String> motsCles;
    private LocalDateTime dateCreation;
    private String statut;
    private String type;
}
