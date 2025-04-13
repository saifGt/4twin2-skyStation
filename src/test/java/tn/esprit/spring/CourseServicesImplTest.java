package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.services.CourseServicesImpl;

@SpringBootTest
public class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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

        Course saved = courseServices.addCourse(course);

        assertNotNull(saved);
        assertEquals(course.getNumCourse(), saved.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveAllCourses() {
        List<Course> courses = Arrays.asList(course);
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseServices.retrieveAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updated = courseServices.updateCourse(course);

        assertNotNull(updated);
        assertEquals(course.getNumCourse(), updated.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course retrieved = courseServices.retrieveCourse(1L);

        assertNotNull(retrieved);
        assertEquals(1L, retrieved.getNumCourse());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            courseServices.retrieveCourse(99L);
        });

        assertEquals("Cours introuvable !", exception.getMessage());
        verify(courseRepository, times(1)).findById(99L);
    }
}
