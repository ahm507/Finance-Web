package pf.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

//import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)

public class PagesTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

//    @Test
//    public void testHome1() throws Exception {
//        mockMvc.perform(get("/finance/"))
//                .andExpect(status().isOk())
//                .andReturn();
//    }

    @Test
    public void testHome2() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    public void testPaths2() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
//                .andExpect(content().string(startsWith("pages/login.jsp")))
                .andReturn();

//        String co = content().toString();
//        int in = 123;

    }


//    @Test
//    public void mobileLogin() throws Exception {
//        mockMvc.perform(get("/mobile-login"))
//                .andExpect(status().isOk())
////                .andExpect(content().string(startsWith("mobile/mobile-login.html")))
//                .andReturn();
//
//
////                        .andExpect(content().string("{\"fieldErrors\":[{\"path\":\"title\",\"message\":\"The title cannot be empty.\"}]}"));
//
//    }





}
