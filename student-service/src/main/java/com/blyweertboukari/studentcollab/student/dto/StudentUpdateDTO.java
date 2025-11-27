package com.blyweertboukari.studentcollab.student.dto;

import com.blyweertboukari.studentcollab.student.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDTO {
    private String lastName;

    private String firstName;

    private String establishment;

    private Student.Major major;

    @UniqueElements(message = "Each skill must be unique")
    private List<String> skills;

    @UniqueElements(message = "Each availability must be unique")
    private List<String> availabilities;
}
