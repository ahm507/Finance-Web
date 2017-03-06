//package pf.rest;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
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
//public class TransactionRestTest {
//
//	
//	private MockMvc mockMvc;
//	
//	@Autowired
//	private WebApplicationContext webApplicationContext;
//
//	
////	@Before
////	public void setup() throws Exception {
////		this.mockMvc = webAppContextSetup(webApplicationContext).build();
////
////		//perform login
////		mockMvc.perform(get("/rest/users/login.do?email=test@test.test&password=test"))
////				.andExpect(status().isOk())
////				.andExpect(jsonPath("$.status", is("success")));
////
////	}
//
//	
////FIXME: enable this test. 
////Measure test coverage. 
////	@Test
////	public void transactionList() throws Exception {
////		String request = "/rest/transactions/getUpToYearTransactions.do?accountId=2100ba44-4d4d-49dd-a358-8ceb43ff2714&year=2016";
////		mockMvc.perform(get(request))
////				.andExpect(status().isOk())
////				.andExpect(jsonPath("status", is("fail")));
////	}
//
//
//
//
//
//	
//}
