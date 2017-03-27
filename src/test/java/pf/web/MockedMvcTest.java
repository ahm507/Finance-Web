package pf.web;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import pf.user.UserEntity;
import pf.user.UserRepository;

import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import javax.validation.constraints.AssertFalse;
import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MockedMvcTest {

	private static final Logger log = LoggerFactory.getLogger(MockedMvcTest.class);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	UserRepository userRepository;

	// @Autowired
	// private WebApplicationContext context;

	// @Before
	// public void setup() throws Exception {
	//
	// mockMvc = MockMvcBuilders
	// .webAppContextSetup(context)
	// .alwaysDo(print())
	// .apply(SecurityMockMvcConfigurers.springSecurity())
	// .build();
	// }

	@Test
	public void transactions() throws Exception {
		mockMvc.perform(get("/transactions")).andExpect(unauthenticated());
	}

	@Test
	@WithMockUser
	public void admin() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/admin")).andExpect(SecurityMockMvcResultMatchers.authenticated())
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser

	public void testLogin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").accept(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "test@test.test").param("password", "test")).andExpect(status().is(302))// 302
																											// Found
				// .andExpect(content().contentType("text/html"))
				.andExpect(content().string("")) // forward to /transactions
				.andExpect(MockMvcResultMatchers.redirectedUrl("/transactions"));

	}

	@Test
	public void logout() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/logout").accept(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is(302))// 302 Found
				.andExpect(MockMvcResultMatchers.redirectedUrl("/login?msg=logout"));

	}

	@Test
	@WithMockUser(username = "test@test.test", password = "test", roles = { "USER", "ADMIN" })

	public void yearList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/rest/transactions/getYearList.do"))
				.andExpect(MockMvcResultMatchers.status().is(200))// 200 OK
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]", CoreMatchers.is("2016")));

	}

	@Test
	@WithMockUser(username = "test@test.test", password = "test", roles = { "USER", "ADMIN" })
	public void accountTransactions() throws Exception {

		String accountId = "d2cedb8e-b049-464a-8142-c92814036047";
		mockMvc.perform(MockMvcRequestBuilders.get("/rest/transactions/getTransactions.do").param("account", accountId))
				.andExpect(MockMvcResultMatchers.status().is(200))// 200 OK
				.andExpect(MockMvcResultMatchers.jsonPath("$.total", CoreMatchers.is("1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].id",
						CoreMatchers.is("6d5a184b-7342-48e8-ac4d-2c5e3c7afb73")))// transactionId
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].amount", CoreMatchers.is(9000.0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].description", CoreMatchers.is("salary")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].amountFormated", CoreMatchers.is("9,000.00")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].balance", CoreMatchers.is(9000.0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].balanceFormated", CoreMatchers.is("9,000.00")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].withdrawId",
						CoreMatchers.is("d2cedb8e-b049-464a-8142-c92814036047")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].depositId",
						CoreMatchers.is("52e2a234-498a-44da-abc2-5c95807ba531")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].withdraw", CoreMatchers.is("Bonus")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].deposit", CoreMatchers.is("Bank")))

		;

	}

	@Test
	@WithMockUser(username = "test@test.test", password = "test", roles = { "USER", "ADMIN" })

	public void yearTransactions() throws Exception {

		String accountId = "d2cedb8e-b049-464a-8142-c92814036047";
		mockMvc.perform(MockMvcRequestBuilders.get("/rest/transactions/getTransactions.do").param("account", accountId)
				.param("year", "2016")).andExpect(MockMvcResultMatchers.jsonPath("$.total", CoreMatchers.is("1")));
	}

	@Test
	public void registerAndVerify() throws Exception {
		// register new user
		String email = "test2@test.test";
		mockMvc.perform(MockMvcRequestBuilders.get("/rest/users/register.do").param("email", email)
				.param("password", "2016").param("password2", "2016"))
				// .andExpect(MockMvcResultMatchers.content().string("{"status":"success"}"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("success")));

		UserEntity user = userRepository.findByEmail(email);

		assertNotNull(user);

		// Verify the email
		mockMvc.perform(MockMvcRequestBuilders.get("/rest/users/verifyEmail.do").param("email", email)
				.param("code", user.getVerification_key()))
				// .andExpect(MockMvcResultMatchers.content().string("{"status":"success"}"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("success")));

		mockMvc.perform(MockMvcRequestBuilders.get("/rest/users/resendVerifyEmail.do").param("email", email))
		.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("fail"))); //should not be able to send email to a fake email

		
	}
	
	

}
