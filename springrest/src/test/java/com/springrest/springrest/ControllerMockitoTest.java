package com.springrest.springrest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springrest.springrest.controller.MyController;
import com.springrest.springrest.entities.Course;
import com.springrest.springrest.services.CourseService;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = {ControllerMockitoTest.class})
public class ControllerMockitoTest {
//if I want to test the controller class by calling the methods through url we must start the springboot application
//without starting we cant test controller class's method through urls
	// and for this MockMVC comes into picture
	
	@Mock
	private CourseService courseService;
	
	@InjectMocks
	MyController  myController;
	
	List<Course> myCourses;
	Course myCourse;
	long cid;
	
	@BeforeEach
	public void setData() {
		myCourses=new ArrayList<>();
		myCourses.add(new Course(1, "hhd", "jhdj"));
		myCourses.add(new Course(2, "hhd", "jhdj"));
		
		myCourse= new Course(1, "hhd", "jhdj");
		
		cid=1;
	}
	
	@Test
	@Order(1)
	public void test_getCourses() {
	when(courseService.getCourses()).thenReturn(myCourses);
	assertEquals(2, myController.getCourses().size());
	}
	
	@Test
	@Order(2)
	public void test_getCourse() {
	when(courseService.getCourse(cid)).thenReturn(myCourse);
	assertEquals(1, myController.getCourse(cid).getId());
	}	
	
	@Test
	@Order(3)
	public void test_addCourse() {
	when(courseService.addCourse(myCourse)).thenReturn(myCourse);
	assertEquals(myCourse, myController.addCourse(myCourse));
	}	
	
	@Test
	@Order(4)
	public void test_updateCourse() {
	when(courseService.updateCourse(myCourse)).thenReturn(myCourse);
	assertEquals(myCourse, myController.updateCourse(myCourse));
	}	
	
	
	@Test
	@Order(5)
	public void test_deleteCourse() {
	
		ResponseEntity<HttpStatus> c=myController.deleteCourse(cid);
		assertEquals(HttpStatus.OK, c.getStatusCode());
		
		//myController.deleteCourse(cid);
			
		verify(courseService, times(1)).deleteCourse(cid);// this one is also not needed as we can just chk the status code
	}	
	
	
	
	
}
