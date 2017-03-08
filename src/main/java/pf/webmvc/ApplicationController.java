package pf.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import pf.account.DeepAccountLayersException;
import pf.account.NullAccountException;
import pf.service.BackupService;
import pf.service.CurrencyTransefereException;
import pf.transaction.TransactionEntity;
import pf.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import java.util.Date;
//import java.util.Map;

@Controller
public class ApplicationController  extends WebMvcConfigurerAdapter {

	
	
	
	@Autowired
	BackupService backupService;

	@Autowired
	UserRepository userRepository;

	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		String[][] mappings = new String[][] {
			{"/login",				"pages/login.jsp"},
			{"/", 					"pages/login.jsp"},
			{"/contactus", 			"pages/contactus.jsp"},
			{"/password-forget", 	"pages/password-forget.jsp"},
			{"/password-reset", 	"pages/password-reset.jsp"},
			{"/privacy", 			"pages/privacy.jsp"},
			{"/register", 			"pages/register.jsp"},			
			//authorized only 
			{"/transactions", 		"pages/transactions.jsp"},
			{"/accounts", 			"pages/accounts.jsp"},
			{"/export", 			"pages/export.jsp"},
			{"/register-verify", 	"pages/register-verify.jsp"},
			{"/settings", 			"pages/settings.jsp"},
			{"/charts", 			"pages/charts.jsp"},
			{"/import", 			"pages/import.jsp"}
			
		};
		
		for(String[] line : mappings) {
			registry.addViewController(line[0]).setViewName(line[1]);
		}
		
	}	

	
	//@ResponseBody
//	@RequestMapping(value="/exporting",  produces = "text/csv")
	@GetMapping("/exporting")
	public String exporting(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
		try {
			String userEmail = request.getRemoteUser();
			String userId = userRepository.findByEmail(userEmail).getId();

			response.setContentType("text/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=" + backupService.getFileName());
			
			model.put("csvFileName", backupService.getFileName());
			model.put("csvContents", backupService.getExportContents(userId));
			
			return "pages/exporting.jsp";
			
		} catch (Exception exp) {
			//HOW TO TEST AND FORCE ERROR
			//FIXME: Write full error details in tomcat log file
			
			Exception ex = new Exception ("Unable to export current contents", exp);
			request.setAttribute("exception", ex);
						
			return "pages/error.jsp";
			
		}		
		
	}

		

	//The following API and code is for testing only.
//	@Value("${application.message:Hello World}")
//	private String message = "Hello World";
//
//	@GetMapping("/helloWorld")
//	public String welcome(Map<String, Object> model) {
//		model.put("time", new Date());
//		model.put("message", this.message);
//		return "welcome.jsp";
//	}


	//for 403 access denied page
//	@RequestMapping(value = "/403", method = RequestMethod.GET)
//	public ModelAndView accesssDenied() {
//
//		ModelAndView model = new ModelAndView();
//		
//		//check if user is login
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (!(auth instanceof AnonymousAuthenticationToken)) {
//			UserDetails userDetail = (UserDetails) auth.getPrincipal();			
//			model.addObject("username", userDetail.getUsername());
//		}
//		
//		model.setViewName("403");
//		return model;
//
//	}
	
//	@GetMapping("/test4")
//	@ResponseBody
//	public String test(Model model) {
//		return "This is a test content";
//	}

//	@GetMapping("/login4")
//	public String login() {
//		return "welcome.jsp";
//	}


//FATAL ERROR: YOU CAN NOT MAP TO STATIC RESOURCES. ONLY JSP.



}
