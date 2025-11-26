package com.blyweertboukari.studentcollab.student.repository;

import com.blyweertboukari.studentcollab.student.model.Student;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.util.List;

public class StudentSpecifications {
    public static Specification<Student> hasMajors(List<Student.Major> majors) {
        return (root, query, builder) -> {
            if (majors == null || majors.isEmpty()) {
                return null;
            }
            return root.get("major").in(majors);
        };
    }

    public static Specification<Student> hasAnySkills(List<String> skills) {
        return (root, query, builder) -> {
            if (skills == null || skills.isEmpty()) {
                return null;
            }
            Join<Student, String> skillsJoin = root.join("skills");
            return skillsJoin.in(skills);
        };
    }
}
