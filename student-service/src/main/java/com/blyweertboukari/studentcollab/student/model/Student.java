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
    private String lastName;

    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String establishment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Major major;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skills")
    private List<String> skills = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_availabilities", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "availabilities")
    private List<String> availabilities = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    
    @Transient
    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public enum Major {
        COMPUTER_SCIENCE,
        MATHEMATICS,
        PHYSICS,
        CHEMISTRY,
        BIOLOGY,
        ECONOMICS,
        BUSINESS,
        LITERATURE,
        HISTORY,
        ARTS
    }
}
