package com.siakad.model;

import com.siakad.model.CourseGrade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseGradeTest {

    @Test
    void testDefaultConstructor() {
        CourseGrade courseGrade = new CourseGrade();

        assertNull(courseGrade.getCourseCode());
        assertEquals(0, courseGrade.getCredits());
        assertEquals(0.0, courseGrade.getGradePoint());
    }

    @Test
    void testParameterizedConstructor() {
        CourseGrade courseGrade = new CourseGrade("CS101", 3, 3.5);

        assertEquals("CS101", courseGrade.getCourseCode());
        assertEquals(3, courseGrade.getCredits());
        assertEquals(3.5, courseGrade.getGradePoint());
    }

    @Test
    void testSettersAndGetters() {
        CourseGrade courseGrade = new CourseGrade();

        courseGrade.setCourseCode("CS102");
        courseGrade.setCredits(4);
        courseGrade.setGradePoint(4.0);

        assertEquals("CS102", courseGrade.getCourseCode());
        assertEquals(4, courseGrade.getCredits());
        assertEquals(4.0, courseGrade.getGradePoint());
    }

    @Test
    void testBoundaryValues() {
        CourseGrade courseGrade = new CourseGrade("CS101", 0, 0.0);

        assertEquals(0, courseGrade.getCredits());
        assertEquals(0.0, courseGrade.getGradePoint());

        courseGrade.setGradePoint(4.0);
        assertEquals(4.0, courseGrade.getGradePoint());
    }
}
