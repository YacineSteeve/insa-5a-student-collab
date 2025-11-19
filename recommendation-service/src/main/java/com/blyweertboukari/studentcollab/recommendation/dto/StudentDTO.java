package com.blyweertboukari.studentcollab.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String nom;
    private String email;
    private String etablissement;
    private String filiere;
    private List<String> competences;
    private List<String> disponibilites;
    private Double moyenneAvis;
}
