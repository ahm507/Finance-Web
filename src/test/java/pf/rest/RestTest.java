//package pf.rest;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.embedded.LocalServerPort;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
//
////@RunWith(SpringJUnit4ClassRunner.class)
////@SpringApplicationConfiguration(classes = PfApplication.class)
////@WebAppConfiguration
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//
//public class RestTest {
//
//	
//	private MockMvc mockMvc;
//	
//	@Autowired
//	private WebApplicationContext webApplicationContext;
//
//	
//	@Before
//	public void setup() throws Exception {
//		this.mockMvc = webAppContextSetup(webApplicationContext).build();
//	}
//
//	//FIXME: find a way to test web layer and login
////	@Test
////	public void restLogin() throws Exception {
////		mockMvc.perform(get("/rest/users/login.do?email=test@test.test&password=test"))
////				.andExpect(status().isOk())
////				.andExpect(jsonPath("$.status", is("success")));
////	}
//	
////	@Test
////	public void restLoginFail() throws Exception {
////		mockMvc.perform(get("/rest/users/login.do?email=test@test.test&password=wrong"))
////				.andExpect(status().isOk())
////				.andExpect(jsonPath("status", is("fail")));
////	}
//
////	@Test
////	public void wrongEmail() throws Exception {
////		mockMvc.perform(get("/rest/users/login.do?email=wrong&password=wrong"))
////				.andExpect(status().isOk())
////				.andExpect(jsonPath("status", is("fail")));
////	}
//
////
////	@Test
////	public void transactionList() throws Exception {
////		mockMvc.perform(get("/rest/users/login.do?email=wrong&password=wrong"))
////				.andExpect(status().isOk())
////				.andExpect(jsonPath("status", is("fail")));
////	}
//
//
//	
////	@LocalServerPort
////	private int port;
//
////	@Autowired
////	private TestRestTemplate restTemplate;
//
////	@Test
////	public void greetingShouldReturnDefaultMessage() throws Exception {
//
////		String page = restTemplate.getForObject("http://localhost:" + port + "/login",
////				String.class);
////		assertThat(page).contains("<title>Login</title>");
////	}
//
//
//
//
//
//	
//}
