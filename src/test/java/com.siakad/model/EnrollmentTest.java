package com.siakad.model;

import com.siakad.model.Enrollment;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class EnrollmentTest {

    @Test
    void testDefaultConstructor() {
        Enrollment enrollment = new Enrollment();

        assertNull(enrollment.getEnrollmentId());
        assertNull(enrollment.getStudentId());
        assertNull(enrollment.getCourseCode());
        assertNull(enrollment.getEnrollmentDate());
        assertNull(enrollment.getStatus());
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Enrollment enrollment = new Enrollment("ENR001", "STU001", "CS101", now, "APPROVED");

        assertEquals("ENR001", enrollment.getEnrollmentId());
        assertEquals("STU001", enrollment.getStudentId());
        assertEquals("CS101", enrollment.getCourseCode());
        assertEquals(now, enrollment.getEnrollmentDate());
        assertEquals("APPROVED", enrollment.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        Enrollment enrollment = new Enrollment();
        LocalDateTime now = LocalDateTime.now();

        enrollment.setEnrollmentId("ENR002");
        enrollment.setStudentId("STU002");
        enrollment.setCourseCode("CS102");
        enrollment.setEnrollmentDate(now);
        enrollment.setStatus("PENDING");

        assertEquals("ENR002", enrollment.getEnrollmentId());
        assertEquals("STU002", enrollment.getStudentId());
        assertEquals("CS102", enrollment.getCourseCode());
        assertEquals(now, enrollment.getEnrollmentDate());
        assertEquals("PENDING", enrollment.getStatus());
    }

    @Test
    void testDifferentStatusValues() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStatus("APPROVED");
        assertEquals("APPROVED", enrollment.getStatus());

        enrollment.setStatus("REJECTED");
        assertEquals("REJECTED", enrollment.getStatus());

        enrollment.setStatus("PENDING");
        assertEquals("PENDING", enrollment.getStatus());
    }
}

