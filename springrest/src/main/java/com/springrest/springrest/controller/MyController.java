package com.springrest.springrest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springrest.springrest.entities.Course;
import com.springrest.springrest.services.CourseService;

@RestController
public class MyController {

	@Autowired
	private CourseService courseService;
	
	//get all courses
	@GetMapping("/courses")
	public List<Course> getCourses(){
		System.out.println("chetna");
		
		return courseService.getCourses();
	}
	
//	@GetMapping("/courses/{id}")
//	public Optional<Course> getCourse(@PathVariable Long id){
//		
//		return courseService.getCourse(id);
//	}
	@GetMapping("/courses/{id}")
	public Course getCourse(@PathVariable Long id){
		
		return courseService.getCourse(id);
	}
	
	@PostMapping("/courses")
	public Course addCourse(@RequestBody Course course){
		
		return courseService.addCourse(course);
	}
	
	  @PutMapping("/courses")
	    public ResponseEntity<Course> updateCourse(@RequestBody Course updatedCourse) {
		  Course existingCourse=null;
		  try {
		   existingCourse = courseService.getCourse(updatedCourse.getId());
		   System.out.println("-------------------");
			  System.out.println("existingCourse" + existingCourse);
			  System.out.println("-------------------");
		  
		  }
		  catch(Exception e) {
			  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		  }

		    Course updated = courseService.updateCourse(updatedCourse);
		    
		//    return (ResponseEntity<Course>) ResponseEntity.notFound();
		  return ResponseEntity.ok(updated);
	    }
	
	@DeleteMapping("/courses/{id}")
	public  ResponseEntity<HttpStatus> deleteCourse(@PathVariable Long id){
		try {
			courseService.deleteCourse(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		 
	}
	
	
}
