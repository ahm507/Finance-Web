package pf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import pf.account.DeepAccountLayersException;
import pf.account.NullAccountException;
import pf.service.BackupService;
import pf.service.CurrencyTransefereException;
import pf.service.WeeklyReport;
import pf.transaction.TransactionEntity;
import pf.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//import java.util.Date;
//import java.util.Map;

@Controller
public class ApplicationController extends WebMvcConfigurerAdapter {

	private final static Logger logger = Logger.getLogger(WeeklyReport.class.getName());

	BackupService backupService;
	UserRepository userRepository;

	ApplicationController(BackupService backupService, UserRepository userRepository) {
		this.backupService = backupService;
		this.userRepository = userRepository;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		String[][] mappings = new String[][] { { "/login", "pages/login.jsp" }, { "/", "pages/login.jsp" },
				{ "/contactus", "pages/contactus.jsp" }, { "/password-forget", "pages/password-forget.jsp" },
				{ "/password-reset", "pages/password-reset.jsp" }, { "/privacy", "pages/privacy.jsp" },
				{ "/register", "pages/register.jsp" }, { "/error2", "pages/error.jsp" },
				// authorized only
				{ "/transactions", "pages/transactions.jsp" }, { "/accounts", "pages/accounts.jsp" },
				{ "/export", "pages/export.jsp" }, { "/register-verify", "pages/register-verify.jsp" },
				{ "/settings", "pages/settings.jsp" }, { "/charts", "pages/charts.jsp" },
				{ "/import", "pages/import.jsp" }

		};

		for (String[] line : mappings) {
			registry.addViewController(line[0]).setViewName(line[1]);
		}

	}

	@GetMapping("/exporting")
	public String exporting(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model)
			throws NullAccountException, DeepAccountLayersException, CurrencyTransefereException {
		String userEmail = request.getRemoteUser();
		String userId = userRepository.findByEmail(userEmail).getId();

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=" + backupService.getFileName());

		model.put("csvFileName", backupService.getFileName());
		model.put("csvContents", backupService.getExportContents(userId));

		return "pages/exporting.jsp";

	}

	@GetMapping("/exception")
	public String exception() throws Exception {
		throw new Exception("This is a test exception!");
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		logger.severe("Request: " + req.getRequestURL() + " raised " + ex);

		ModelAndView modelView = new ModelAndView();
		modelView.addObject("exception", ex);
		modelView.addObject("url", req.getRequestURL());
		modelView.setViewName("pages/error.jsp");
		return modelView;
	}

		
	 @GetMapping("/test")
	 @ResponseBody
	 public String test(HttpServletRequest request, HttpServletResponse
	 response, Map<String, Object> model)
	 throws NullAccountException, DeepAccountLayersException,
	 CurrencyTransefereException {
	
	 StringBuilder buffer = new StringBuilder();
	 
	 buffer.append("User is:" + request.getRemoteUser() + "<br>");

	 return buffer.toString();
	
	 }

	// FATAL ERROR: YOU CAN NOT MAP TO STATIC RESOURCES. ONLY JSP.

}
