package com.blyweertboukari.studentcollab.helprequest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "help_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "help_request_keywords", joinColumns = @JoinColumn(name = "help_request_id"))
    @Column(name = "keywords")
    private List<String> keywords = new ArrayList<>();

    @Column(nullable = false)
    private Long studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.WAITING;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant desiredDate;

    public enum Status {
        WAITING,
        IN_PROGRESS,
        DONE,
        ABANDONED,
        CLOSED
    }
}
