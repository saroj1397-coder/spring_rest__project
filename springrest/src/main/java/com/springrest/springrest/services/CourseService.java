package com.springrest.springrest.services;

import java.util.List;
import java.util.Optional;

import com.springrest.springrest.entities.Course;

public interface CourseService {

	public List<Course> getCourses();

//	public Optional<Course> getCourse(Long id);
	public Course getCourse(Long id);


	public Course addCourse(Course course);

	public Course updateCourse(Course course);

	public void deleteCourse(Long id);
}
