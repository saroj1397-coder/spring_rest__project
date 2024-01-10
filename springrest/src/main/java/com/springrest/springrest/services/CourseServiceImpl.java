package com.springrest.springrest.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springrest.springrest.dao.CourseDao;
import com.springrest.springrest.entities.Course;



@Service
public class CourseServiceImpl implements CourseService  {
	
	@Autowired
	private CourseDao courseDao;

//	List<Course> list;
//	
//	public CourseServiceImpl() {
//		
//		list=new ArrayList<>();
//		list.add(new Course(1, "hhd", "jhdj"));
//	}
	
	@Override
	public List<Course> getCourses() {
		
		return courseDao.findAll();
	}

	@Override
	public Course getCourse(Long id) {
//		for(Course c: list) {
//			if(c.getId()==id) {
//				return c;
//			}
//		}
		
		return courseDao.getById(id);
	}
//	@Override
//	public Course getCourse(Long id) {
//		return courseDao.findById(id).get();
//	}
//	
	
	@Override
	public Course addCourse(Course course) {
	
		//list.add(course);
		courseDao.save(course);
		return course;
	}

	@Override
	public Course updateCourse(Course course) {
	
//		for(Course c: list) {
//			if(c.getId()==course.getId()) {
//				c.setDesc(course.getDesc());
//				c.setTitle(course.getTitle());
//				
//				return c;
//			}
//			
//		}
		
		courseDao.save(course);
		return course;
	}

	@Override
	public void deleteCourse(Long id) {
		
//		for(Course c: list) {
//			if(c.getId()==id) {
//				list.remove(c);
//				break;				
//			}
//		}
		
		courseDao.deleteById(id);
	}
	

}
