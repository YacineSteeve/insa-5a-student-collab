package com.blyweertboukari.studentcollab.student.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String etablissement;
    
    @Column(nullable = false)
    private String filiere;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_competences", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "competence")
    private List<String> competences = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_disponibilites", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "disponibilite")
    private List<String> disponibilites = new ArrayList<>();
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avis> avis = new ArrayList<>();
    
    @Transient
    public Double getMoyenneAvis() {
        if (avis == null || avis.isEmpty()) {
            return 0.0;
        }
        return avis.stream()
                .mapToInt(Avis::getNote)
                .average()
                .orElse(0.0);
    }
}
