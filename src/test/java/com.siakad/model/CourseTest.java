package com.siakad.model;

import com.siakad.model.Course;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void testCourseConstructorWithSixParameters() {
        Course course = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");

        assertEquals("CS101", course.getCourseCode());
        assertEquals("Programming", course.getCourseName());
        assertEquals(3, course.getCredits());
        assertEquals(30, course.getCapacity());
        assertEquals(15, course.getEnrolledCount());
        assertEquals("Dr. Smith", course.getLecturer());
        assertNotNull(course.getPrerequisites());
        assertTrue(course.getPrerequisites().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Course course = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");

        course.setCourseCode("CS102");
        course.setCourseName("Data Structures");
        course.setCredits(4);
        course.setCapacity(40);
        course.setEnrolledCount(20);
        course.setLecturer("Dr. Johnson");

        assertEquals("CS102", course.getCourseCode());
        assertEquals("Data Structures", course.getCourseName());
        assertEquals(4, course.getCredits());
        assertEquals(40, course.getCapacity());
        assertEquals(20, course.getEnrolledCount());
        assertEquals("Dr. Johnson", course.getLecturer());
    }

    @Test
    void testPrerequisitesManagement() {
        Course course = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");

        // Test addPrerequisite
        course.addPrerequisite("CS100");
        course.addPrerequisite("MATH101");

        List<String> prerequisites = course.getPrerequisites();
        assertEquals(2, prerequisites.size());
        assertTrue(prerequisites.contains("CS100"));
        assertTrue(prerequisites.contains("MATH101"));

        // Test setPrerequisites
        List<String> newPrerequisites = Arrays.asList("PHY101", "CHEM101");
        course.setPrerequisites(newPrerequisites);

        assertEquals(2, course.getPrerequisites().size());
        assertTrue(course.getPrerequisites().contains("PHY101"));
    }

    @Test
    void testAddPrerequisiteWithNullList() {
        Course course = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");
        course.setPrerequisites(null);

        // Should handle null prerequisites list
        course.addPrerequisite("CS100");

        assertNotNull(course.getPrerequisites());
        assertEquals(1, course.getPrerequisites().size());
    }

    @Test
    void testDefaultValues() {
        Course course = new Course("CS101", "Programming", 3, 30, 15, "Dr. Smith");

        // Test default values after construction
        assertNotNull(course.getCourseCode());
        assertNotNull(course.getCourseName());
        assertNotNull(course.getLecturer());
        assertNotNull(course.getPrerequisites());
        assertTrue(course.getCredits() > 0);
        assertTrue(course.getCapacity() > 0);
    }
}
