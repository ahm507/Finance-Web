//package pf.rest;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.embedded.LocalServerPort;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//

//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
////import static org.hamcrest.core.StringStartsWith.startsWith;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
////import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
//import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
//
////@SpringBootTest
////@RunWith(SpringJUnit4ClassRunner.class)
//
//@RunWith(SpringJUnit4ClassRunner.class) 
//@ContextConfiguration 
//@WebAppConfiguration
//
//public class MockedMvcTest {
//
//	@Autowired
//    private WebApplicationContext context;
//
////	@LocalServerPort
////	private int port;
//
//
//    private MockMvc mockMvc;
//
// 
//	
//    @Before
//    public void setup() throws Exception {
////        this.mockMvc = webAppContextSetup(webApplicationContext).build();
////    	this.mvc = MockMvcBuilders
////        		.webAppContextSetup(context) 
////        			.apply(springSecurity())
////        			.build();
//    	
//    	this.mockMvc = MockMvcBuilders
//                // replace standaloneSetup with line below
//                .webAppContextSetup(context)
//                .alwaysDo(print())
//                .apply(SecurityMockMvcConfigurers.springSecurity())
//                .build();    	
//    }
//
//    
////  @Test
////  public void testExport() throws Exception {
////	  
////	  mockMvc.perform(get("/transactions").with(user("test@test.test")))
////	  	.andExpect(authenticated());
//////	  .withUsername("test@test.test")
////	  
////	  
//////	  content()).contains("<title>Transactions Management</title>")
////              
////  }    
//    
////    @Test
////    public void testHome1() throws Exception {
////        mockMvc.perform(get("/finance/"))
////                .andExpect(status().isOk())
////                .andReturn();
////    }
//
////    @Test
////    public void testHome2() throws Exception {
////        mockMvc.perform(get("/"))
////                .andExpect(status().isOk())
////                .andReturn();
////    }
//
////
//    @Test
//    public void testPaths2() throws Exception {
//        mockMvc.perform(get("/login"))
//                .andExpect(status().isOk())
////                .andExpect(content().string(startsWith("pages/login.jsp")))
//                .andReturn();
//
////        String co = content().toString();
////        int in = 123;
//
//    }
//
//
////    @Test
////    public void mobileLogin() throws Exception {
////        mockMvc.perform(get("/mobile-login"))
////                .andExpect(status().isOk())
//////                .andExpect(content().string(startsWith("mobile/mobile-login.html")))
////                .andReturn();
////
////
//////                        .andExpect(content().string("{\"fieldErrors\":[{\"path\":\"title\",\"message\":\"The title cannot be empty.\"}]}"));
////
////    }
//
//
//
//
//
//}
//
//
//
////
////public class RestTest {
////
////	
////	private MockMvc mockMvc;
////	
////	@Autowired
////	private WebApplicationContext webApplicationContext;
////
////	
////	@Before
////	public void setup() throws Exception {
////		this.mockMvc = webAppContextSetup(webApplicationContext).build();
////	}
////
////	//FIXME: find a way to test web layer and login
//////	@Test
//////	public void restLogin() throws Exception {
//////		mockMvc.perform(get("/rest/users/login.do?email=test@test.test&password=test"))
//////				.andExpect(status().isOk())
//////				.andExpect(jsonPath("$.status", is("success")));
//////	}
////	
//////	@Test
//////	public void restLoginFail() throws Exception {
//////		mockMvc.perform(get("/rest/users/login.do?email=test@test.test&password=wrong"))
//////				.andExpect(status().isOk())
//////				.andExpect(jsonPath("status", is("fail")));
//////	}
////
//////	@Test
//////	public void wrongEmail() throws Exception {
//////		mockMvc.perform(get("/rest/users/login.do?email=wrong&password=wrong"))
//////				.andExpect(status().isOk())
//////				.andExpect(jsonPath("status", is("fail")));
//////	}
////
//////
//////	@Test
//////	public void transactionList() throws Exception {
//////		mockMvc.perform(get("/rest/users/login.do?email=wrong&password=wrong"))
//////				.andExpect(status().isOk())
//////				.andExpect(jsonPath("status", is("fail")));
//////	}
////
////
////	
//////	@LocalServerPort
//////	private int port;
////
//////	@Autowired
//////	private TestRestTemplate restTemplate;
////
//////	@Test
//////	public void greetingShouldReturnDefaultMessage() throws Exception {
////
//////		String page = restTemplate.getForObject("http://localhost:" + port + "/login",
//////				String.class);
//////		assertThat(page).contains("<title>Login</title>");
//////	}
////
//
//
//
////@Before
////public void setup() throws Exception {
////	this.mockMvc = webAppContextSetup(webApplicationContext).build();
////
////	//perform login
////	mockMvc.perform(get("/rest/users/login.do?email=test@test.test&password=test"))
////			.andExpect(status().isOk())
////			.andExpect(jsonPath("$.status", is("success")));
////
////}
//
//
////FIXME: enable this test. 
////Measure test coverage. 
////@Test
////public void transactionList() throws Exception {
////	String request = "/rest/transactions/getUpToYearTransactions.do?accountId=2100ba44-4d4d-49dd-a358-8ceb43ff2714&year=2016";
////	mockMvc.perform(get(request))
////			.andExpect(status().isOk())
////			.andExpect(jsonPath("status", is("fail")));
////}
//
//
//
//
