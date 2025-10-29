package com.siakad.exception;

import com.siakad.exception.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionClassesTest {

    @Test
    void testCourseFullException() {
        // Test constructor with message
        CourseFullException exception1 = new CourseFullException("Course is full");
        assertEquals("Course is full", exception1.getMessage());

        // Test constructor with message and cause
        Throwable cause = new RuntimeException("Underlying error");
        CourseFullException exception2 = new CourseFullException("Course is full", cause);
        assertEquals("Course is full", exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    void testCourseNotFoundException() {
        // Test constructor with message
        CourseNotFoundException exception1 = new CourseNotFoundException("Course not found: CS101");
        assertEquals("Course not found: CS101", exception1.getMessage());

        // Test constructor with message and cause
        Throwable cause = new RuntimeException("Database error");
        CourseNotFoundException exception2 = new CourseNotFoundException("Course not found", cause);
        assertEquals("Course not found", exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    void testEnrollmentException() {
        // Test constructor with message
        EnrollmentException exception1 = new EnrollmentException("Enrollment failed");
        assertEquals("Enrollment failed", exception1.getMessage());

        // Test constructor with message and cause
        Throwable cause = new IllegalArgumentException("Invalid input");
        EnrollmentException exception2 = new EnrollmentException("Enrollment failed", cause);
        assertEquals("Enrollment failed", exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    void testPrerequisiteNotMetException() {
        // Test constructor with message
        PrerequisiteNotMetException exception1 = new PrerequisiteNotMetException("Prerequisites not met");
        assertEquals("Prerequisites not met", exception1.getMessage());

        // Test constructor with message and cause
        Throwable cause = new RuntimeException("Prerequisite check failed");
        PrerequisiteNotMetException exception2 = new PrerequisiteNotMetException("Prerequisites not met", cause);
        assertEquals("Prerequisites not met", exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    void testStudentNotFoundException() {
        // Test constructor with message
        StudentNotFoundException exception1 = new StudentNotFoundException("Student not found: 0909");
        assertEquals("Student not found: 0909", exception1.getMessage());

        // Test constructor with message and cause
        Throwable cause = new RuntimeException("Database connection failed");
        StudentNotFoundException exception2 = new StudentNotFoundException("Student not found", cause);
        assertEquals("Student not found", exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }
}

