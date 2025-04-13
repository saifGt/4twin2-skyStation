package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(2);
        course.setSupport(Support.SKI);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setPrice(150.0f);
        course.setTimeSlot(2);
    }

    @Test
    void testAddCourse() {

        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseServices.addCourse(course);

        assertNotNull(result);
        assertEquals(course.getNumCourse(), result.getNumCourse());
        assertEquals(course.getPrice(), result.getPrice());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveAllCourses() {
        // Given
        Course course2 = new Course();
        course2.setNumCourse(2L);
        course2.setLevel(1);
        course2.setSupport(Support.SNOWBOARD);
        course2.setTypeCourse(TypeCourse.INDIVIDUAL);
        course2.setPrice(200.0f);
        course2.setTimeSlot(1);

        List<Course> courses = Arrays.asList(course, course2);
        when(courseRepository.findAll()).thenReturn(courses);
        // When
        List<Course> result = courseServices.retrieveAllCourses();
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(course));
        assertTrue(result.contains(course2));
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCourse() {
        // Given
        course.setPrice(180.0f);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        // When
        Course result = courseServices.updateCourse(course);
        // Then
        assertNotNull(result);
        assertEquals(180.0f, result.getPrice());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveCourse() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        // When
        Course result = courseServices.retrieveCourse(1L);
        // Then
        assertNotNull(result);
        assertEquals(course.getNumCourse(), result.getNumCourse());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveCourse_NotFound() {
        // Given
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        // Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            courseServices.retrieveCourse(99L);
        });
        assertEquals("Cours introuvable !", exception.getMessage());
        verify(courseRepository, times(1)).findById(99L);
    }


}
