package pf.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pf.service.BackupService;
import pf.transaction.TransactionEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import java.util.Date;
import java.util.List;
//import java.util.Map;

@Controller
public class WelcomeController {

	@Value("${application.message:Hello World}")
	private String message = "Hello World";

//	@GetMapping("/helloWorld")
//	public String welcome(Map<String, Object> model) {
//		model.put("time", new Date());
//		model.put("message", this.message);
//		return "welcome.jsp";
//	}


//	@GetMapping("/test4")
//	@ResponseBody
//	public String test(Model model) {
//		return "This is a test content";
//	}

	@GetMapping("/login4")
	public String login() {
		return "welcome.jsp";
	}

////////////////////////////////////////////
	@Autowired
	BackupService backupService;


	@GetMapping(value = {"/", "/login"})
	public String login(Model model) {
		return "pages/login.jsp";
	}

	@GetMapping("/transactions")
	public String trans(Model model) {
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


	@ResponseBody
	@RequestMapping(value="/exporting",  produces = "text/csv")
	public String exporting(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("userId");
		if(id == null || id.isEmpty()) {
			response.sendRedirect("/login"); //login.jsp view
			return "";
		}

		List<TransactionEntity> ts = backupService.backupUserData(String.valueOf(id));
		response.setContentType("text/csv");

		String fileName = backupService.getFileName();
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		StringBuilder buffer = new StringBuilder();

		buffer.append(backupService.getTitleFormatted()).append("\r\n");
		for(TransactionEntity t: ts) {
			buffer.append(backupService.getRowFormatted(t)).append("\r\n");
		}

		return buffer.toString();

	}


//FATAL ERROR: YOU CAN NOT MAP TO STATIC RESOURCES. ONLY JSP.



}
