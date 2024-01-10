package com.springrest.springrest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.springrest.springrest.dao.CourseDao;
import com.springrest.springrest.entities.Course;
import com.springrest.springrest.services.CourseServiceImpl;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes= {ServiceMockitoTest.class})
public class ServiceMockitoTest {
	
	@Mock
	private CourseDao courseDao;
	
	@InjectMocks
	private CourseServiceImpl courseService;
	
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
	public void test_getCourses(){
//		List<Course> myCourses=new ArrayList<>();
//		myCourses.add(new Course(1, "hhd", "jhdj"));
//		myCourses.add(new Course(2, "hhd", "jhdj"));
		
		when(courseDao.findAll()).thenReturn(myCourses);//mocking
		 assertEquals(2,courseService.getCourses().size());
	}
	
	@Test
	@Order(2)
	public void test_getCourse() {
	//Course myCourse= new Course(1, "hhd", "jhdj");
	//	long cId=1;

		when(courseDao.getById(cid)).thenReturn(myCourse);
		 assertEquals(cid,courseService.getCourse(cid).getId());
	}
	
	@Test
	@Order(3)
	public void test_addCourse() {
	//Course myCourse= new Course(1, "hhd", "jhdj");
	
		when(courseDao.save(myCourse)).thenReturn(myCourse);
		 assertEquals(myCourse,courseService.addCourse(myCourse));
	}
	
	@Test
	@Order(4)
	public void test_updateCourse() {
	//Course myCourse= new Course(1, "hhd", "jhdj");
	
		when(courseDao.save(myCourse)).thenReturn(myCourse);
		 assertEquals(myCourse,courseService.addCourse(myCourse));
	}
	
	@Test
	@Order(5)
	public void test_deleteCourse() {
	//Course myCourse= new Course(1, "hhd", "jhdj");
	//long cid=1;
	//	when(courseDao.deleteById(cid)).thenReturn(myCourse);
	//(when and then we can use only if that method is returning something)
		 courseService.deleteCourse(cid);
		 verify(courseDao, times(1)).deleteById(cid);//mocking when we r not returning anything
	}
	
}
