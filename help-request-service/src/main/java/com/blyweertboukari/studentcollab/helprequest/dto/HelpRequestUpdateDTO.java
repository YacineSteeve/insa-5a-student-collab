package com.blyweertboukari.studentcollab.helprequest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequestUpdateDTO {
    private String title;

    private String description;

    private Instant desiredDate;

    private List<String> keywords = new ArrayList<>();
}
