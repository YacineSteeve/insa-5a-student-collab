package com.blyweertboukari.studentcollab.student.dto;

import com.blyweertboukari.studentcollab.student.model.Student;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Establishment is required")
    private String establishment;

    @NotBlank(message = "Major is required")
    private Student.Major major;

    @UniqueElements(message = "Each skill must be unique")
    @Size(min = 1, message = "At least one skill is required")
    private List<String> skills;

    @UniqueElements(message = "Each availability must be unique")
    @Size(min = 1, message = "At least one availability is required")
    private List<String> availabilities;
}
