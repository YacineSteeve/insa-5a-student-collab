package com.blyweertboukari.studentcollab.student.dto;

import com.blyweertboukari.studentcollab.student.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Establishment is required")
    private String establishment;

    @NotNull(message = "Major is required")
    private Student.Major major;

    private List<String> skills;
    private List<String> availabilities;
    private Double averageRating;

}
