package com.blyweertboukari.studentcollab.student.dto;

import com.blyweertboukari.studentcollab.student.model.Student;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentsFilters {
    @Valid
    private List<@NotBlank String> skills;

    @Valid
    private List<Student.Major> majors;
}
