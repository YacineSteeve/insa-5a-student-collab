package com.blyweertboukari.studentcollab.helprequest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "help_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long studentId;
    
    @Column(nullable = false)
    private String titre;
    
    @Column(length = 2000, nullable = false)
    private String description;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "help_request_keywords", joinColumns = @JoinColumn(name = "help_request_id"))
    @Column(name = "keyword")
    private List<String> motsCles = new ArrayList<>();
    
    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDemande statut = StatutDemande.EN_ATTENTE;
    
    @Column(nullable = false)
    private TypeDemande type;
    
    public enum StatutDemande {
        EN_ATTENTE,
        EN_COURS,
        TERMINEE,
        ANNULEE
    }
    
    public enum TypeDemande {
        DEMANDE_AIDE,
        OFFRE_AIDE
    }
}
