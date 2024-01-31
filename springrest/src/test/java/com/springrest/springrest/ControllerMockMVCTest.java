package com.springrest.springrest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springrest.springrest.controller.MyController;
import com.springrest.springrest.entities.Course;
import com.springrest.springrest.services.CourseService;

@TestMethodOrder(OrderAnnotation.class)
@ComponentScan(basePackages = "com.springrest.springrest")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = {ControllerMockMVCTest.class})
public class ControllerMockMVCTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Mock
	CourseService courseService;

	@InjectMocks
	MyController myController;
	
	List<Course> myCourses;
	Course myCourse;
	long cid;
	
	
	
	@BeforeEach
	public void setUp() {//this method needs to be executed before every test methos starts
		mockMvc=MockMvcBuilders.standaloneSetup(myController).build();
		
		myCourses=new ArrayList<>();
		myCourses.add(new Course(1, "hhd", "jhdj"));
		myCourses.add(new Course(2, "hhd", "jhdj"));
		
		myCourse= new Course(1, "hhd", "jhdj");
		
		cid=1;
	}
	
	@Test
	@Order(1)
	public void test_getCourses() throws Exception {
		
		when(courseService.getCourses()).thenReturn(myCourses);
		//now we hav done the mocking of the dependency but now we want to call those controller
		//method using the url
		
//		
//		Yes, it's absolutely okay to write tests focusing on the HTTP status code, especially if your 
		//controller doesn't explicitly handle or set the status code. 
		//Checking the HTTP status code is a fundamental aspect of testing the behavior of your API.
//		By verifying the status code, you are ensuring that your endpoint is returning the 
		//expected outcome. For example, a status code of 200 (OK) indicates a successful response, 
		//while other status codes may represent different scenarios (e.g., 404 for not found, 500 for server error).
//		As your application evolves, and if you start handling status codes in your controller logic, you can update your 
		//tests accordingly to include more specific assertions. For now, testing the status code is a good practice, and it
		//provides a basic level of confidence in the correctness of your API endpoints.
		
		this.mockMvc.perform(get("/courses"))//so we r testing without calling controller methods through url only
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@Order(2)
	public void test_getCourse() throws Exception {
		
		when(courseService.getCourse(cid)).thenReturn(myCourse);
		
		this.mockMvc.perform(get("/courses/{id}", cid))
			.andExpect(status().isOk())
//			 .andDo(result -> {
//				 String responseBody = result.getResponse().getContentAsString();
//				    System.out.println("Response Body: " + responseBody);
//				    assertThat(responseBody).isNotEmpty();
//				    assertThat(responseBody).contains("\"id\":" + myCourse.getId(),
//	                        "\"title\":\"" + myCourse.getTitle() + "\"",
//	                        "\"description\":\"" + myCourse.getDescription() + "\"");
//	            		        })
			.andExpect(MockMvcResultMatchers.jsonPath(".id").value(1)) // // mockMVC return the object in JSON format not in object for that we hav special method MockMvcResultMatchers
			.andExpect(MockMvcResultMatchers.jsonPath(".title").value("hhd")) // for jsonPath(".id") we hav nice extension
			.andExpect(MockMvcResultMatchers.jsonPath(".description").value("jhdj"))
			.andDo(print());
	}
	
	@Test
	@Order(3)
	public void test_addCourse() throws Exception {
		
		when(courseService.addCourse(myCourse)).thenReturn(myCourse);
		
		//for sending data we will hav to convert the object into json format for that we hav special class ObjectMapper
		//becoz mockMVC will accept only JSON response not the java object
		
		ObjectMapper mapper= new ObjectMapper();
		String jsonbody=mapper.writeValueAsString(myCourse); //inside string we hav json data only
		
		this.mockMvc.perform(post("/courses")
					.content(jsonbody)
					.contentType(MediaType.APPLICATION_JSON)
					)
			.andExpect(status().isOk())
			.andDo(print());
					 
			
		
	}
	
/*	@Test
	@Order(4)
	public void test_updateCourse() throws Exception {
	
		System.out.println(myCourse.getId());
		System.out.println(myCourse.getTitle());
		System.out.println(myCourse.getDescription());
		
		when(courseService.updateCourse(myCourse)).thenReturn(myCourse);
		
		ObjectMapper mapper=new ObjectMapper();
		String jsonbody= mapper.writeValueAsString(myCourse);
		
		this.mockMvc.perform(put("/courses")
					.content(jsonbody)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("UTF-8")
					)
			.andExpect(status().isOk())
//			 .andDo(result -> {
//				 String responseBody = result.getResponse().getContentAsString();
//				    System.out.println("Response Body: " + responseBody);
//				    assertThat(responseBody).isNotEmpty();
//				    assertThat(responseBody).contains("\"id\":" + myCourse.getId(),
//	                        "\"title\":\"" + myCourse.getTitle() + "\"",
//	                        "\"description\":\"" + myCourse.getDescription() + "\"");
//	            		        })
//			//.andDo(print());
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(1)) 
			.andExpect(MockMvcResultMatchers.jsonPath("title").value("hhd"))
			.andExpect(MockMvcResultMatchers.jsonPath("description").value("jhdj"))
			// Add assertions to verify the response body
//			.andExpect(jsonPath("$.id").value(1))
//			.andExpect(jsonPath("$.title").value("hhd"))
//			.andExpect(jsonPath("$.description").value("jhdj"))
			
//			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
//			.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("hhd"))
//			.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("jhdj"))

			.andDo(print());
		
//		
//		java.lang.AssertionError: No value at JSON path "id"
//		at org.springframework.test.util.JsonPathExpectationsHelper.evaluateJsonPath(JsonPathExpectationsHelper.java:302)
//		at org.springframework.test.util.JsonPathExpectationsHelper.assertValue(JsonPathExpectationsHelper.java:99)
//		at org.springframework.test.web.servlet.result.JsonPathResultMatchers.lambda$value$2(JsonPathResultMatchers.java:111)
//		at org.springframework.test.web.servlet.MockMvc$1.andExpect(MockMvc.java:214)
//		at com.springrest.springrest.ControllerMockMVCTest.test_updateCourse(ControllerMockMVCTest.java:142)
//		at java.base/java.lang.reflect.Method.invoke(Method.java:568)
//		at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
//		at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
//	Caused by: java.lang.IllegalArgumentException: json can not be null or empty
//		at com.jayway.jsonpath.internal.Utils.notEmpty(Utils.java:401)
//		at com.jayway.jsonpath.JsonPath.read(JsonPath.java:390)
//		at com.jayway.jsonpath.JsonPath.read(JsonPath.java:377)
//		at org.springframework.test.util.JsonPathExpectationsHelper.evaluateJsonPath(JsonPathExpectationsHelper.java:299)
//		... 7 more	
		
	}
*/	
	@Test
	@Order(5)
	public void test_deleteCourse() throws Exception {
		
	this.mockMvc.perform(delete("/courses/{id}",cid))
				.andExpect(status().isOk())
				.andDo(print());
		
	}
	
}
