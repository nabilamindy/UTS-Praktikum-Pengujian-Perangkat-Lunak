package com.siakad.service;

import com.siakad.service.EnrollmentService;
import com.siakad.service.GradeCalculator;
import com.siakad.service.NotificationService;
import com.siakad.exception.*;
import com.siakad.model.Course;
import com.siakad.model.Enrollment;
import com.siakad.model.Student;
import com.siakad.repository.CourseRepository;
import com.siakad.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceMockTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private GradeCalculator gradeCalculator;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Student studentNabila;
    private Student studentSeol;
    private Course courseProgramming;

    @BeforeEach
    void setUp() {
        studentNabila = new Student("0909", "Nabila", "Nabila@email.com",
                "Computer Science", 3, 3.2, "ACTIVE");
        studentSeol = new Student("099", "Seol", "seol@email.com",
                "Computer Science", 3, 1.5, "SUSPENDED");
        courseProgramming = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");
    }

    @Test
    void enrollCourse_Success() {
        when(studentRepository.findById("0909")).thenReturn(studentNabila);
        when(courseRepository.findByCourseCode("CS101")).thenReturn(courseProgramming);
        when(courseRepository.isPrerequisiteMet("0909", "CS101")).thenReturn(true);

        Enrollment enrollment = enrollmentService.enrollCourse("0909", "CS101");

        assertNotNull(enrollment);
        assertEquals("0909", enrollment.getStudentId());
        assertEquals("CS101", enrollment.getCourseCode());
        assertEquals("APPROVED", enrollment.getStatus());
        verify(courseRepository).update(any(Course.class));
        verify(notificationService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void enrollCourse_StudentSuspended() {
        when(studentRepository.findById("099")).thenReturn(studentSeol);

        EnrollmentException exception = assertThrows(EnrollmentException.class, () -> {
            enrollmentService.enrollCourse("099", "CS101");
        });
        assertEquals("Student is suspended", exception.getMessage());
    }

    @Test
    void enrollCourse_PrerequisiteNotMet() {
        when(studentRepository.findById("0909")).thenReturn(studentNabila);
        when(courseRepository.findByCourseCode("CS101")).thenReturn(courseProgramming);
        when(courseRepository.isPrerequisiteMet("0909", "CS101")).thenReturn(false);

        PrerequisiteNotMetException exception = assertThrows(PrerequisiteNotMetException.class, () -> {
            enrollmentService.enrollCourse("0909", "CS101");
        });
        assertEquals("Prerequisites not met", exception.getMessage());
    }

    @Test
    void enrollCourse_StudentNotFound() {
        when(studentRepository.findById("999")).thenReturn(null);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.enrollCourse("999", "CS101");
        });
        assertEquals("Student not found: 999", exception.getMessage());
    }

    @Test
    void enrollCourse_CourseNotFound() {
        when(studentRepository.findById("0909")).thenReturn(studentNabila);
        when(courseRepository.findByCourseCode("UNKNOWN")).thenReturn(null);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> {
            enrollmentService.enrollCourse("0909", "UNKNOWN");
        });
        assertEquals("Course not found: UNKNOWN", exception.getMessage());
    }

    @Test
    void enrollCourse_CourseFull() {
        Course fullCourse = new Course("CS101", "Programming", 3, 30, 30, "Dr. Smith");

        when(studentRepository.findById("0909")).thenReturn(studentNabila);
        when(courseRepository.findByCourseCode("CS101")).thenReturn(fullCourse);

        CourseFullException exception = assertThrows(CourseFullException.class, () -> {
            enrollmentService.enrollCourse("0909", "CS101");
        });
        assertEquals("Course is full", exception.getMessage());
    }

    @Test
    void enrollCourse_StudentInactiveStatus() {
        Student inactiveStudent = new Student("088", "Inactive", "inactive@email.com",
                "Computer Science", 3, 3.0, "INACTIVE");

        when(studentRepository.findById("088")).thenReturn(inactiveStudent);
        when(courseRepository.findByCourseCode("CS101")).thenReturn(courseProgramming);
        when(courseRepository.isPrerequisiteMet("088", "CS101")).thenReturn(true);

        Enrollment enrollment = enrollmentService.enrollCourse("088", "CS101");

        assertNotNull(enrollment);
        assertEquals("088", enrollment.getStudentId());
    }

    @Test
    void enrollCourse_StudentProbationStatus() {
        Student probationStudent = new Student("077", "Probation", "probation@email.com",
                "Computer Science", 3, 1.8, "PROBATION");

        when(studentRepository.findById("077")).thenReturn(probationStudent);
        when(courseRepository.findByCourseCode("CS101")).thenReturn(courseProgramming);
        when(courseRepository.isPrerequisiteMet("077", "CS101")).thenReturn(true);

        Enrollment enrollment = enrollmentService.enrollCourse("077", "CS101");

        assertNotNull(enrollment);
        assertEquals("077", enrollment.getStudentId());
    }

    @Test
    void validateCreditLimit_Success() {
        when(studentRepository.findById("0909")).thenReturn(studentNabila);
        when(gradeCalculator.calculateMaxCredits(3.2)).thenReturn(24);

        boolean result = enrollmentService.validateCreditLimit("0909", 20);

        assertTrue(result);
    }

    @Test
    void validateCreditLimit_ExceedsLimit() {
        when(studentRepository.findById("0909")).thenReturn(studentNabila);
        when(gradeCalculator.calculateMaxCredits(3.2)).thenReturn(18);

        boolean result = enrollmentService.validateCreditLimit("0909", 20);

        assertFalse(result);
    }

    @Test
    void validateCreditLimit_ExactlyAtLimit() {
        when(studentRepository.findById("0909")).thenReturn(studentNabila);
        when(gradeCalculator.calculateMaxCredits(3.2)).thenReturn(20);

        boolean result = enrollmentService.validateCreditLimit("0909", 20);

        assertTrue(result);
    }

    @Test
    void validateCreditLimit_StudentNotFound() {
        when(studentRepository.findById("999")).thenReturn(null);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.validateCreditLimit("999", 20);
        });
        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void dropCourse_Success() {
        when(studentRepository.findById("0909")).thenReturn(studentNabila);
        when(courseRepository.findByCourseCode("CS101")).thenReturn(courseProgramming);

        enrollmentService.dropCourse("0909", "CS101");

        verify(courseRepository).update(any(Course.class));
        verify(notificationService).sendEmail(eq("Nabila@email.com"), anyString(), anyString());
    }

    @Test
    void dropCourse_StudentNotFound() {
        when(studentRepository.findById("999")).thenReturn(null);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.dropCourse("999", "CS101");
        });
        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void dropCourse_CourseNotFound() {
        when(studentRepository.findById("0909")).thenReturn(studentNabila);
        when(courseRepository.findByCourseCode("UNKNOWN")).thenReturn(null);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> {
            enrollmentService.dropCourse("0909", "UNKNOWN");
        });
        assertEquals("Course not found", exception.getMessage());
    }
}