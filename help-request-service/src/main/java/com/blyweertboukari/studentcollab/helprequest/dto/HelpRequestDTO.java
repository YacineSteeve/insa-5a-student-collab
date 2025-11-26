package com.blyweertboukari.studentcollab.helprequest.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequestDTO {
    private Long id;

    @NotBlank(message = "The title is required")
    private String titre;

    @NotBlank(message = "The description is required")
    private String description;

    @UniqueElements(message = "Each keyword must be unique")
    @NotEmpty(message = "Keywords must not be empty")
    private List<String> keywords = new ArrayList<>();
}
