package pf.user;//package pf.user;

import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest //initintiate web layer only
public class UserRestMockTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Test
//	public void shouldReturnDefaultMessage() throws Exception {
//		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
//				.andExpect(content().string(containsString("Hello World")));
//	}
//
//
//

	@Test
	public void testLog() throws IOException {
//		FileHandler handler = new FileHandler("pf2.txt");
//		handler.setFormatter(new XMLFormatter());
		Logger logger = Logger.getLogger("LoggerTest");
//		logger.addHandler(handler);
		logger.severe(">> This is a warning from me");
		logger.warning(">> This is a warning from me");
		logger.warning(">> This is a warning from me");


	}

}


