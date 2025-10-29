package com.siakad.service;

import com.siakad.service.GradeCalculator;
import com.siakad.model.CourseGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {

    private GradeCalculator gradeCalculator;

    @BeforeEach
    void setUp() {
        gradeCalculator = new GradeCalculator();
    }

    @Test
    void calculateGPA_WithValidGrades_ReturnsCorrectGPA() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 4.0),
                new CourseGrade("CS102", 3, 3.0),
                new CourseGrade("CS103", 2, 2.0)
        );

        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(3.13, result, 0.01);
    }

    @Test
    void calculateGPA_WithEmptyList_ReturnsZero() {
        double result = gradeCalculator.calculateGPA(Collections.emptyList());
        assertEquals(0.0, result);
    }

    @Test
    void calculateGPA_WithNullList_ReturnsZero() {
        double result = gradeCalculator.calculateGPA(null);
        assertEquals(0.0, result);
    }

    @Test
    void calculateGPA_WithZeroCredits() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 0, 4.0),
                new CourseGrade("CS102", 0, 3.0)
        );

        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(0.0, result);
    }

    @Test
    void calculateGPA_WithSingleCourse() {
        List<CourseGrade> grades = Collections.singletonList(
                new CourseGrade("CS101", 3, 3.0)
        );

        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(3.0, result);
    }

    @Test
    void calculateGPA_WithMixedGradesAndCredits() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 4, 2.5),
                new CourseGrade("CS102", 2, 4.0),
                new CourseGrade("CS103", 3, 3.0)
        );

        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(3.0, result, 0.01);
    }

    @Test
    void calculateGPA_AllPerfectScores() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 4.0),
                new CourseGrade("CS102", 4, 4.0),
                new CourseGrade("CS103", 2, 4.0)
        );

        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(4.0, result);
    }

    @Test
    void calculateGPA_AllFailedScores() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 0.0),
                new CourseGrade("CS102", 2, 0.0),
                new CourseGrade("CS103", 4, 0.0)
        );

        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(0.0, result);
    }

    @Test
    void calculateGPA_WithInvalidGradePoint_Negative() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, -1.0),
                new CourseGrade("CS102", 2, 2.0)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.calculateGPA(grades);
        });
        assertTrue(exception.getMessage().contains("Invalid grade point"));
    }

    @Test
    void calculateGPA_WithInvalidGradePoint_AboveFour() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 4.5),
                new CourseGrade("CS102", 2, 2.0)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.calculateGPA(grades);
        });
        assertTrue(exception.getMessage().contains("Invalid grade point"));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2.0, ACTIVE",
            "1, 1.9, PROBATION",
            "2, 2.0, ACTIVE",
            "2, 1.9, PROBATION",
            "3, 2.25, ACTIVE",
            "3, 2.0, PROBATION",
            "3, 1.9, SUSPENDED",
            "4, 2.25, ACTIVE",
            "4, 2.0, PROBATION",
            "4, 1.9, SUSPENDED",
            "5, 2.5, ACTIVE",
            "5, 2.0, PROBATION",
            "5, 1.9, SUSPENDED",
            "6, 2.5, ACTIVE",
            "6, 2.0, PROBATION",
            "6, 1.9, SUSPENDED"
    })
    void determineAcademicStatus_WithVariousInputs_ReturnsCorrectStatus(int semester, double gpa, String expectedStatus) {
        String result = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expectedStatus, result);
    }

    @Test
    void determineAcademicStatus_InvalidGPA_Negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.determineAcademicStatus(-1.0, 3);
        });
        assertEquals("GPA must be between 0 and 4.0", exception.getMessage());
    }

    @Test
    void determineAcademicStatus_InvalidGPA_AboveFour() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.determineAcademicStatus(4.5, 3);
        });
        assertEquals("GPA must be between 0 and 4.0", exception.getMessage());
    }

    @Test
    void determineAcademicStatus_InvalidSemester() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.determineAcademicStatus(3.0, 0);
        });
        assertEquals("Semester must be positive", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "3.5, 24",
            "3.0, 24",
            "2.8, 21",
            "2.5, 21",
            "2.3, 18",
            "2.0, 18",
            "1.9, 15",
            "0.0, 15"
    })
    void calculateMaxCredits_WithVariousGPA_ReturnsCorrectCredits(double gpa, int expectedCredits) {
        int result = gradeCalculator.calculateMaxCredits(gpa);
        assertEquals(expectedCredits, result);
    }

    @Test
    void calculateMaxCredits_InvalidGPA_Negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.calculateMaxCredits(-1.0);
        });
        assertEquals("GPA must be between 0 and 4.0", exception.getMessage());
    }

    @Test
    void calculateMaxCredits_InvalidGPA_AboveFour() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.calculateMaxCredits(4.5);
        });
        assertEquals("GPA must be between 0 and 4.0", exception.getMessage());
    }

    @Test
    void calculateMaxCredits_BoundaryValue_ExactlyTwoPointFive() {
        int result = gradeCalculator.calculateMaxCredits(2.5);
        assertEquals(21, result);
    }

    @Test
    void calculateMaxCredits_BoundaryValue_ExactlyThree() {
        int result = gradeCalculator.calculateMaxCredits(3.0);
        assertEquals(24, result);
    }

    @Test
    void calculateMaxCredits_BoundaryValue_ExactlyTwo() {
        int result = gradeCalculator.calculateMaxCredits(2.0);
        assertEquals(18, result);
    }

    @Test
    void calculateMaxCredits_BoundaryValue_JustBelowThree() {
        int result = gradeCalculator.calculateMaxCredits(2.999);
        assertEquals(21, result);
    }

    @Test
    void calculateMaxCredits_BoundaryValue_JustBelowTwoPointFive() {
        int result = gradeCalculator.calculateMaxCredits(2.499);
        assertEquals(18, result);
    }

    @Test
    void calculateMaxCredits_BoundaryValue_JustBelowTwo() {
        int result = gradeCalculator.calculateMaxCredits(1.999);
        assertEquals(15, result);
    }
}

