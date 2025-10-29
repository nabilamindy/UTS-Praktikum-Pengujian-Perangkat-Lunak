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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentServiceStubTest {

    private EnrollmentService enrollmentService;
    private StudentRepositoryStub studentRepositoryStub;
    private CourseRepositoryStub courseRepositoryStub;
    private GradeCalculatorStub gradeCalculatorStub;
    private NotificationServiceStub notificationServiceStub;

    static class StudentRepositoryStub implements StudentRepository {
        private Student studentToReturn;

        @Override
        public Student findById(String studentId) {
            return studentToReturn;
        }

        @Override
        public void update(Student student) {}

        @Override
        public List<Course> getCompletedCourses(String studentId) {
            return Collections.emptyList();
        }

        public void setStudentToReturn(Student student) {
            this.studentToReturn = student;
        }
    }

    static class CourseRepositoryStub implements CourseRepository {
        private Course courseToReturn;
        private boolean prerequisiteMet = true;

        @Override
        public Course findByCourseCode(String courseCode) {
            return courseToReturn;
        }

        @Override
        public void update(Course course) {}

        @Override
        public boolean isPrerequisiteMet(String studentId, String courseCode) {
            return prerequisiteMet;
        }

        public void setCourseToReturn(Course course) {
            this.courseToReturn = course;
        }

        public void setPrerequisiteMet(boolean met) {
            this.prerequisiteMet = met;
        }
    }

    static class GradeCalculatorStub extends GradeCalculator {
        private int maxCreditsToReturn;

        @Override
        public int calculateMaxCredits(double gpa) {
            return maxCreditsToReturn;
        }

        public void setMaxCreditsToReturn(int maxCredits) {
            this.maxCreditsToReturn = maxCredits;
        }
    }

    static class NotificationServiceStub implements NotificationService {
        public String lastEmail;
        public String lastSubject;
        public String lastMessage;

        @Override
        public void sendEmail(String email, String subject, String message) {
            this.lastEmail = email;
            this.lastSubject = subject;
            this.lastMessage = message;
        }

        @Override
        public void sendSMS(String phone, String message) {
            // Implementasi untuk testing
        }
    }

    @BeforeEach
    void setUp() {
        studentRepositoryStub = new StudentRepositoryStub();
        courseRepositoryStub = new CourseRepositoryStub();
        gradeCalculatorStub = new GradeCalculatorStub();
        notificationServiceStub = new NotificationServiceStub();

        enrollmentService = new EnrollmentService(
                studentRepositoryStub,
                courseRepositoryStub,
                notificationServiceStub,
                gradeCalculatorStub
        );
    }

    @Test
    void enrollCourse_Success() {
        Student studentNabila = new Student("0909", "Nabila", "Nabila@email.com",
                "Computer Science", 3, 3.2, "ACTIVE");
        Course course = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");

        studentRepositoryStub.setStudentToReturn(studentNabila);
        courseRepositoryStub.setCourseToReturn(course);
        courseRepositoryStub.setPrerequisiteMet(true);

        Enrollment enrollment = enrollmentService.enrollCourse("0909", "CS101");

        assertNotNull(enrollment);
        assertEquals("0909", enrollment.getStudentId());
        assertEquals("CS101", enrollment.getCourseCode());
        assertEquals("APPROVED", enrollment.getStatus());
        assertEquals("Nabila@email.com", notificationServiceStub.lastEmail);
    }

    @Test
    void enrollCourse_StudentSuspended() {
        Student studentSeol = new Student("099", "Seol", "seol@email.com",
                "Computer Science", 3, 1.5, "SUSPENDED");

        studentRepositoryStub.setStudentToReturn(studentSeol);

        assertThrows(EnrollmentException.class, () ->
                enrollmentService.enrollCourse("099", "CS101")
        );
    }

    @Test
    void enrollCourse_PrerequisiteNotMet() {
        Student studentNabila = new Student("0909", "Nabila", "Nabila@email.com",
                "Computer Science", 3, 3.2, "ACTIVE");
        Course course = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");

        studentRepositoryStub.setStudentToReturn(studentNabila);
        courseRepositoryStub.setCourseToReturn(course);
        courseRepositoryStub.setPrerequisiteMet(false);

        assertThrows(PrerequisiteNotMetException.class, () ->
                enrollmentService.enrollCourse("0909", "CS101")
        );
    }

    @Test
    void enrollCourse_StudentNotFound() {
        studentRepositoryStub.setStudentToReturn(null);

        assertThrows(StudentNotFoundException.class, () ->
                enrollmentService.enrollCourse("999", "CS101")
        );
    }

    @Test
    void enrollCourse_CourseNotFound() {
        Student studentNabila = new Student("0909", "Nabila", "Nabila@email.com",
                "Computer Science", 3, 3.2, "ACTIVE");

        studentRepositoryStub.setStudentToReturn(studentNabila);
        courseRepositoryStub.setCourseToReturn(null);

        assertThrows(CourseNotFoundException.class, () ->
                enrollmentService.enrollCourse("0909", "CS101")
        );
    }

    @Test
    void enrollCourse_CourseFull() {
        Student studentNabila = new Student("0909", "Nabila", "Nabila@email.com",
                "Computer Science", 3, 3.2, "ACTIVE");
        Course fullCourse = new Course("CS101", "Programming", 3, 30, 30, "Dr. Smith");

        studentRepositoryStub.setStudentToReturn(studentNabila);
        courseRepositoryStub.setCourseToReturn(fullCourse);
        courseRepositoryStub.setPrerequisiteMet(true);

        assertThrows(CourseFullException.class, () ->
                enrollmentService.enrollCourse("0909", "CS101")
        );
    }

    @Test
    void enrollCourse_StudentInactiveStatus() {
        Student inactiveStudent = new Student("088", "Inactive", "inactive@email.com",
                "Computer Science", 3, 3.0, "INACTIVE");
        Course course = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");

        studentRepositoryStub.setStudentToReturn(inactiveStudent);
        courseRepositoryStub.setCourseToReturn(course);
        courseRepositoryStub.setPrerequisiteMet(true);

        Enrollment enrollment = enrollmentService.enrollCourse("088", "CS101");

        assertNotNull(enrollment);
        assertEquals("088", enrollment.getStudentId());
    }

    @Test
    void enrollCourse_StudentProbationStatus() {
        Student probationStudent = new Student("077", "Probation", "probation@email.com",
                "Computer Science", 3, 1.8, "PROBATION");
        Course course = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");

        studentRepositoryStub.setStudentToReturn(probationStudent);
        courseRepositoryStub.setCourseToReturn(course);
        courseRepositoryStub.setPrerequisiteMet(true);

        Enrollment enrollment = enrollmentService.enrollCourse("077", "CS101");

        assertNotNull(enrollment);
        assertEquals("077", enrollment.getStudentId());
    }

    @Test
    void validateCreditLimit_Success() {
        Student studentNabila = new Student("0909", "Nabila", "Nabila@email.com",
                "Computer Science", 3, 3.2, "ACTIVE");
        studentRepositoryStub.setStudentToReturn(studentNabila);
        gradeCalculatorStub.setMaxCreditsToReturn(24);

        boolean result = enrollmentService.validateCreditLimit("0909", 20);

        assertTrue(result);
    }

    @Test
    void validateCreditLimit_ExceedsLimit() {
        Student studentNabila = new Student("0909", "Nabila", "Nabila@email.com",
                "Computer Science", 3, 3.2, "ACTIVE");

        studentRepositoryStub.setStudentToReturn(studentNabila);
        gradeCalculatorStub.setMaxCreditsToReturn(18);

        boolean result = enrollmentService.validateCreditLimit("0909", 20);

        assertFalse(result);
    }

    @Test
    void validateCreditLimit_StudentNotFound() {
        studentRepositoryStub.setStudentToReturn(null);

        assertThrows(StudentNotFoundException.class, () ->
                enrollmentService.validateCreditLimit("999", 20)
        );
    }

    @Test
    void dropCourse_Success() {
        Student studentNabila = new Student("0909", "Nabila", "Nabila@email.com",
                "Computer Science", 3, 3.0, "ACTIVE");
        Course course = new Course("CS101", "Programming", 3, 30, 10, "Dr. Smith");

        studentRepositoryStub.setStudentToReturn(studentNabila);
        courseRepositoryStub.setCourseToReturn(course);

        enrollmentService.dropCourse("0909", "CS101");

        assertEquals("Nabila@email.com", notificationServiceStub.lastEmail);
        assertEquals("Course Drop Confirmation", notificationServiceStub.lastSubject);
    }

    @Test
    void dropCourse_StudentNotFound() {
        studentRepositoryStub.setStudentToReturn(null);

        assertThrows(StudentNotFoundException.class, () ->
                enrollmentService.dropCourse("999", "CS101")
        );
    }

    @Test
    void dropCourse_CourseNotFound() {
        Student studentNabila = new Student("0909", "Nabila", "Nabila@email.com",
                "Computer Science", 3, 3.2, "ACTIVE");

        studentRepositoryStub.setStudentToReturn(studentNabila);
        courseRepositoryStub.setCourseToReturn(null);

        assertThrows(CourseNotFoundException.class, () ->
                enrollmentService.dropCourse("0909", "CS101")
        );
    }
}

