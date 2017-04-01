package pf.web;
/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Basic integration tests for demo application.
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class WebServerTests {

//	private final static Logger logger = Logger.getLogger(WebServerTests.class.getName());
	
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	
	@Test
	public void testPublicPartAreAccessible() {
		String body = restTemplate.getForObject("/login", String.class);
		assertThat(body).contains("<title>Login</title>");

		body = restTemplate.getForObject("/login", String.class);
		assertThat(body).contains("<title>Login</title>");
				
		body = restTemplate.getForObject("/contactus", String.class);
		assertThat(body).contains("<html");

		body = restTemplate.getForObject("/password-forget", String.class);
		assertThat(body).contains("<html");

		body = restTemplate.getForObject("/password-reset", String.class);
		assertThat(body).contains("<html");

		body = restTemplate.getForObject("/privacy", String.class);
		assertThat(body).contains("<html");

		body = restTemplate.getForObject("/register", String.class);
		assertThat(body).contains("<html");
	}

//	@Test
//	public void testNonPublicPartAreNotAccessable() {
//		String body = restTemplate.getForObject("/transactions", String.class);
//		assertThat(body).isNull();
//
//		body = restTemplate.getForObject("/accounts", String.class);
//		assertThat(body).isNull();
//
//		body = restTemplate.getForObject("/export", String.class);
//		assertThat(body).isNull();
//
//		body = restTemplate.getForObject("/register-verify", String.class);
//		assertThat(body).isNull();
//
//		body = restTemplate.getForObject("/settings", String.class);
//		assertThat(body).isNull();
//
//		body = restTemplate.getForObject("/charts", String.class);
//		assertThat(body).isNull();
//
//		body = restTemplate.getForObject("/import", String.class);
//		assertThat(body).isNull();
//
//		body = restTemplate.getForObject("/admin", String.class);
//		assertThat(body).isNull();
//		
//		
//	}
	

	



	@Test
	public void testLogin() throws Exception {
		HttpHeaders headers = getCSRFHeaders();
//		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "test@test.test");
		form.set("password", "test");
		ResponseEntity<String> entity = this.restTemplate.exchange("/login",
				HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(form, headers),
				String.class);
		
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(entity.getHeaders().getLocation().toString())
				.endsWith(this.port + "/transactions");
		assertThat(entity.getHeaders().get("Set-Cookie")).isNotNull();
	}	
	
	private HttpHeaders getCSRFHeaders() {
		HttpHeaders headers = new HttpHeaders();
		//CSRF requires session id and CSRF token
		ResponseEntity<String> page = this.restTemplate.getForEntity("/login",
				String.class);
		assertThat(page.getStatusCode()).isEqualTo(HttpStatus.OK);
		String cookie = page.getHeaders().getFirst("Set-Cookie");
		headers.set("Cookie", cookie);
		//The below code is for CSRF
		Pattern pattern = Pattern.compile("(?s).*name=\"_csrf\".*?value=\"([^\"]+).*");
		Matcher matcher = pattern.matcher(page.getBody());
		assertThat(matcher.matches()).as(page.getBody()).isTrue();
		headers.set("X-CSRF-TOKEN", matcher.group(1));
		return headers;
	}	
	
//	@Test
//	public void testHome() throws Exception {
//		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
//		ResponseEntity<String> entity = this.restTemplate.exchange("/", HttpMethod.GET,
//				new HttpEntity<Void>(headers), String.class);
//		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
//		assertThat(entity.getHeaders().getLocation().toString())
//				.endsWith(this.port + "/login");
//	}

	

//	@Test
//	public void testCss() throws Exception {
//		ResponseEntity<String> entity = this.restTemplate
//				.getForEntity("/css/bootstrap.min.css", String.class);
//		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(entity.getBody()).contains("body");
//	}

}
