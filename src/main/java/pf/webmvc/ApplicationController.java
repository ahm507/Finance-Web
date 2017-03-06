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
import pf.service.BackupService;
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

	
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
//		registry.addViewController("/").setViewName("pages/transactions.jsp");
//		registry.addViewController("/index").setViewName("pages/index.jsp");

		registry.addViewController("/login").setViewName("pages/login.jsp");

	}	
	
	
	@Autowired
	BackupService backupService;

	@Autowired
	UserRepository userRepository;


//	@GetMapping(value = {"/login"})
//	public String login(@RequestParam(value = "error", required = false) String error,
//			@RequestParam(value = "logout", required = false) String logout, Model model) {
//
//		if (error != null) {
//			model.addAttribute("error", "Invalid username and password!");
//		}
//
//		if (logout != null) {
//			model.addAttribute("msg", "You've been logged out successfully.");
//		}
//		
//		return "pages/login.jsp";
//	}

	
	@GetMapping(value = {"/"})//, "/index"
	public String index() {

		return "pages/login.jsp";
	}	
	
	@GetMapping("/transactions")
	public String trans(HttpServletRequest request, HttpServletResponse response, Model model) {
//		try {
//			if(notLoggedIn(request, response)) return "";	
//		} catch (IOException exp) {
//			return "pages/error.jsp";
//		}
//		
//        return "redirect:/todo-list";

		return "pages/transactions.jsp";
	}

	@GetMapping("/accounts")
	public String accounts(Model model) {
		return "pages/accounts.jsp";
	}

	@GetMapping("/contactus")
	public String contactus(Model model) {
		return "pages/contactus.jsp";
	}

	@GetMapping("/error")
	public String error(Model model) {
		return "pages/error.jsp";
	}

	@GetMapping("/export")
	public String export(Model model) {
		return "pages/export.jsp";
	}

	@GetMapping("/password-forget")
	public String password_forget(Model model) {
		return "pages/password-forget.jsp";
	}

	@GetMapping("/password-reset")
	public String password_reset(Model model) {
		return "pages/password-reset.jsp";
	}

	@GetMapping("/privacy")
	public String privacy(Model model) {
		return "pages/privacy.jsp";
	}

	@GetMapping("/register")
	public String register(Model model) {
		return "pages/register.jsp";
	}

	@GetMapping("/register-verify")
	public String register_verify(Model model) {
		return "pages/register-verify.jsp";
	}

	@GetMapping("/settings")
	public String settings(Model model) {
		return "pages/settings.jsp";
	}

	@GetMapping("/import")
	public String import2(Model model) {
		return "pages/import.jsp";
	}

	@GetMapping("/charts")
	public String charts(Model model) {
		return "pages/charts.jsp";
	}

	//FIXME: move all test classes under unit.
	
	//@ResponseBody
//	@RequestMapping(value="/exporting",  produces = "text/csv")
	@GetMapping("/exporting")
	public String exporting(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
		try {
			
//			HttpSession session = request.getSession();
//			String id = (String)session.getAttribute("userId");		
//			if(id == null || id.isEmpty()) {
//
//				return "pages/login.jsp";
//			}		

			String userEmail = request.getRemoteUser();

			//TODO: Extract to external function

			String userId = userRepository.findByEmail(userEmail).getId();
			List<TransactionEntity> ts = backupService.backupUserData(String.valueOf(userId));

			response.setContentType("text/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=" + backupService.getFileName());
			
			model.put("csvFileName", backupService.getFileName());
			StringBuilder buffer = new StringBuilder();
	
			buffer.append(backupService.getTitleFormatted()).append("\r\n");
			for(TransactionEntity t: ts) {
				buffer.append(backupService.getRowFormatted(t)).append("\r\n");
			}
			String contents = buffer.toString();




			model.put("csvContents", contents);
			
			return "pages/exporting.jsp";
			
		} catch (Exception exp) {
			//HOW TO TEST AND FORCE ERROR
			//FIXME: Write full error details in tomcat log file
			
			Exception ex = new Exception ("This is some error");
			request.setAttribute("exception", exp);
						
			return "pages/error.jsp";
			
		}		
		
	}
		

	//The following API and code is for testing only.
	@Value("${application.message:Hello World}")
	private String message = "Hello World";

	@GetMapping("/helloWorld")
	public String welcome(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		return "welcome.jsp";
	}


	
	//for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {

		ModelAndView model = new ModelAndView();
		
		//check if user is login
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();			
			model.addObject("username", userDetail.getUsername());
		}
		
		model.setViewName("403");
		return model;

	}
	
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
