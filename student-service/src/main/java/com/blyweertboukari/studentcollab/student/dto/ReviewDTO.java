package com.blyweertboukari.studentcollab.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;

    private Integer rating;

    private String comment;

    private Long helpRequestId;
}
