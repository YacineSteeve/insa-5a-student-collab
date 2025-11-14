package fr.insa.student.service;

import fr.insa.student.dto.LoginDTO;
import fr.insa.student.dto.StudentDTO;
import fr.insa.student.dto.StudentRegistrationDTO;
import fr.insa.student.model.Student;
import fr.insa.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private StudentRegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setNom("Jean Dupont");
        testStudent.setEmail("jean@insa.fr");
        testStudent.setPassword("password123");
        testStudent.setEtablissement("INSA Lyon");
        testStudent.setFiliere("Informatique");
        testStudent.setCompetences(Arrays.asList("Java", "Spring Boot"));
        testStudent.setDisponibilites(Arrays.asList("Lundi 14h-16h"));
        testStudent.setAvis(new ArrayList<>());

        registrationDTO = new StudentRegistrationDTO();
        registrationDTO.setNom("Jean Dupont");
        registrationDTO.setEmail("jean@insa.fr");
        registrationDTO.setPassword("password123");
        registrationDTO.setEtablissement("INSA Lyon");
        registrationDTO.setFiliere("Informatique");
        registrationDTO.setCompetences(Arrays.asList("Java", "Spring Boot"));
        registrationDTO.setDisponibilites(Arrays.asList("Lundi 14h-16h"));
    }

    @Test
    void testRegisterSuccess() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        StudentDTO result = studentService.register(registrationDTO);

        assertNotNull(result);
        assertEquals("Jean Dupont", result.getNom());
        assertEquals("jean@insa.fr", result.getEmail());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> studentService.register(registrationDTO));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testLoginSuccess() {
        LoginDTO loginDTO = new LoginDTO("jean@insa.fr", "password123");
        when(studentRepository.findByEmail("jean@insa.fr")).thenReturn(Optional.of(testStudent));

        StudentDTO result = studentService.login(loginDTO);

        assertNotNull(result);
        assertEquals("jean@insa.fr", result.getEmail());
    }

    @Test
    void testLoginInvalidPassword() {
        LoginDTO loginDTO = new LoginDTO("jean@insa.fr", "wrongpassword");
        when(studentRepository.findByEmail("jean@insa.fr")).thenReturn(Optional.of(testStudent));

        assertThrows(RuntimeException.class, () -> studentService.login(loginDTO));
    }

    @Test
    void testLoginUserNotFound() {
        LoginDTO loginDTO = new LoginDTO("unknown@insa.fr", "password");
        when(studentRepository.findByEmail("unknown@insa.fr")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.login(loginDTO));
    }

    @Test
    void testGetStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        StudentDTO result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jean Dupont", result.getNom());
    }

    @Test
    void testUpdateStudent() {
        StudentDTO updateDTO = new StudentDTO();
        updateDTO.setNom("Jean Updated");
        updateDTO.setEmail("jean@insa.fr");
        updateDTO.setEtablissement("INSA Lyon");
        updateDTO.setFiliere("Informatique");
        updateDTO.setCompetences(Arrays.asList("Java", "Python"));
        updateDTO.setDisponibilites(Arrays.asList("Mardi 10h-12h"));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        StudentDTO result = studentService.updateStudent(1L, updateDTO);

        assertNotNull(result);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDTO> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jean Dupont", result.get(0).getNom());
    }
}
