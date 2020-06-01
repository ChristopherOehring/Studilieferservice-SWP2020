//package com.studilieferservice.usermanager;
//
//import com.studilieferservice.usermanager.userService.User;
//import com.studilieferservice.usermanager.userService.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.event.annotation.BeforeTestClass;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//
//@SpringBootTest(classes = UserManagerApplication.class)
//class DemoApplicationTests {
//
//	@Autowired
//	private UserService userService;
//    @BeforeTestClass
//	public void initDb(){
//
//		User newUser = new User("Test","test@test.com","testpassword");
//		userService.createUser(newUser);
//		User user = userService.findOne("test@test.com");
//	}
//
// @Test
//	public void testUser(){
//
//	 User user = userService.findOne("test@test.com");
//	 assertNotNull(user);
// }
//}
