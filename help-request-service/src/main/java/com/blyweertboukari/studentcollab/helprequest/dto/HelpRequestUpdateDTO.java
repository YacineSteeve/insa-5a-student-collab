package com.blyweertboukari.studentcollab.helprequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequestUpdateDTO {
    private String title;

    private String description;

    private Long assigneeId;

    private Date desiredDate;

    private List<String> keywords = new ArrayList<>();
}
