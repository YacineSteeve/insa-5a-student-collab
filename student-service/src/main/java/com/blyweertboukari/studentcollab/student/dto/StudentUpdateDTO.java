package com.blyweertboukari.studentcollab.student.dto;

import com.blyweertboukari.studentcollab.student.model.Student;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDTO {
    @Valid
    private String lastName;

    @Valid
    private String firstName;

    @Valid
    @Email(message = "Email should be valid")
    private String email;

    @Valid
    private String establishment;

    @Valid
    private Student.Major major;

    @Valid
    @UniqueElements(message = "Each skill must be unique")
    private List<String> skills;

    @Valid
    @UniqueElements(message = "Each availability must be unique")
    private List<String> availabilities;
}
