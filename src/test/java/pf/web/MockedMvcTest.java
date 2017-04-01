package pf.web;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
//import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import pf.email.Zoho;
import pf.user.UserEntity;
import pf.user.UserRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MockedMvcTest {

	// private static final Logger log =
	// LoggerFactory.getLogger(MockedMvcTest.class);

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
		mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf()).accept(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "test@test.test").param("password", "test")).andExpect(status().is(302))// Found

				// .andExpect(content().contentType("text/html"))
				.andExpect(content().string("")) // forward to /transactions
				.andExpect(MockMvcResultMatchers.redirectedUrl("/transactions"));

	}

	@Test
	public void logout() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/logout").with(csrf()).accept(MediaType.APPLICATION_FORM_URLENCODED))
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

	@MockBean
	private Zoho zohoMail;

	@Test
	public void registerAndVerify() throws Exception {
		// register new user

		// when(zohoMail.sendZohoMail(anyString(), anyString(), anyString(),
		// anyString(), anyString(), anyString(),
		// anyString())).thenReturn(true);

		String email = "test2@test.test";
		mockMvc.perform(MockMvcRequestBuilders.get("/rest/users/register.do").param("email", email)
				.param("password", "2016").param("password2", "2016"))
				// .andExpect(MockMvcResultMatchers.content().string("{"status":"success"}"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("success")));

		UserEntity user = userRepository.findByEmail(email);
		assertNotNull(user);

		// Verify the email
		mockMvc.perform(MockMvcRequestBuilders.get("/rest/users/verifyEmail.do").param("email", email).param("code",
				user.getVerification_key()))
				// .andExpect(MockMvcResultMatchers.content().string("{"status":"success"}"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("success")));

		mockMvc.perform(MockMvcRequestBuilders.get("/rest/users/resendVerifyEmail.do").param("email", email))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("success")));

	}

	//This test case succeed but it throw null pointer exception!! At least I am sure it basiclly works from security point of view.
	@Test
	@WithMockUser(username = "test@test.test", password = "test", roles = { "USER", "ADMIN" })
	public void upload() throws Exception {
		 
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain",
				"Spring Framework".getBytes());
		mockMvc.perform(fileUpload("/upload").file(multipartFile).with(csrf())).andExpect(status().is(200));

	}

}
