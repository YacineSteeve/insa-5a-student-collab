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
    private String lastName;
    private String firstName;
    private String email;
    private String establishment;
    private Major major;
    private List<String> skills;
    private List<String> availabilities;
    private Double averageReview;


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
