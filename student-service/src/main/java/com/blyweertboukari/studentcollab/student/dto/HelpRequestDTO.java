package com.blyweertboukari.studentcollab.student.dto;

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
    private Long assigneeId;
    private Long authorId;
    private String title;
    private String description;
    private List<String> keywords = new ArrayList<>();
    private Instant createdAt;
    private Instant updatedAt;
    private Instant desiredDate;
    private Status status;

    public enum Status {
        WAITING,
        IN_PROGRESS,
        DONE,
        ABANDONED,
        CLOSED
    }
}
