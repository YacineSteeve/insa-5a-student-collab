package com.blyweertboukari.studentcollab.student.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreationDTO {
    @Valid
    @Max(value = 5, message = "Rating must be between 1 and 5")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @NotNull(message = "Rating is required")
    private Integer rating;

    @Valid
    @NotBlank(message = "Comment is required")
    @NotNull(message = "Help request id is required")
    private String comment;

    @Valid
    @NotNull(message = "Help request id is required")
    private Long helpRequestId;
}
