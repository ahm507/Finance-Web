package pf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pf.account.DeepAccountLayersException;
import pf.account.NullAccountException;
import pf.backup.BackupService;
import pf.backup.CurrencyTransefereException;
import pf.backup.RestoreService;
import pf.charts.ChartService;
import pf.charts.WeeklyReport;
import pf.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

//import java.util.Date;
//import java.util.Map;

@Controller
public class ApplicationController extends WebMvcConfigurerAdapter {

	private final static Logger logger = Logger.getLogger(WeeklyReport.class.getName());

	BackupService backupService;
	UserRepository userRepository;
	RestoreService restoreService;
	ChartService chartService;

	ApplicationController(BackupService backupService, UserRepository userRepository, RestoreService restoreService,
	                      ChartService chartService) {
		this.backupService = backupService;
		this.userRepository = userRepository;
		this.restoreService = restoreService;
		this.chartService = chartService;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		String[][] mappings = new String[][] { 
			{ "/login", "pages/login.jsp" }, 
			{ "/", "pages/login.jsp" },
			{ "/contactus", "pages/contactus.jsp" }, 
			{ "/password-forget", "pages/password-forget.jsp" },
			{ "/password-reset", "pages/password-reset.jsp" }, 
			{ "/privacy", "pages/privacy.jsp" },
			{ "/register", "pages/register.jsp" }, 
			{ "/error2", "pages/error.jsp" },
			// authorized only
			{ "/transactions", "pages/transactions.jsp" }, 
			{ "/accounts", "pages/accounts.jsp" },
			{ "/export", "pages/export.jsp" }, 
			{ "/register-verify", "pages/register-verify.jsp" },
			{ "/settings", "pages/settings.jsp" },
			{ "/charts", "pages/charts.jsp" },
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
		// FIXME: just return results directly. No need to exporting.jsp
		return "pages/exporting.jsp";

	}

	@GetMapping("/chart-export") //as html for better copy and paste into excel
	@ResponseBody
	public String chartExport(HttpServletRequest request, HttpServletResponse response,
	                          @RequestParam("year") String year, @RequestParam("type") String type)
			throws Exception {
		String email = request.getRemoteUser();
		String userId = userRepository.findByEmail(email).getId();
		String csvResponse = chartService.getExpensesTrendHtml(year, type, userId, email);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		return csvResponse;
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
	public String test(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model)
			throws NullAccountException, DeepAccountLayersException, CurrencyTransefereException {

		return "User is:" + request.getRemoteUser() + "<br>";
	}

	@PostMapping("/upload")
	void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// FIXME: This is too much work, see:
		// https://www.mkyong.com/spring-boot/spring-boot-file-upload-example/
		// In one single parameter you can get byte array with the file. CSV
		// reader accept any kind of Reader, including StringReader.

		logger.info("Started inside upload() API");

		// String userId = getLoggedInUser(request);
		String userEmail = request.getRemoteUser();
		String userId = userRepository.findByEmail(userEmail).getId();

		final String path = request.getServletContext().getRealPath("/WEB-INF/upload-temp/");
		final Part filePart = request.getPart("file");
		final String fileName = getFileName(filePart);

		OutputStream out = null;
		response.setContentType("text/html;charset=UTF-8");
		final PrintWriter writer = response.getWriter();
		String fullFileName = path + File.separator + userId + "-" + fileName;
		out = new FileOutputStream(new File(fullFileName));
		InputStream fileContent = filePart.getInputStream();

		int read = 0;
		final byte[] bytes = new byte[1024];

		while ((read = fileContent.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		writer.println("<h2>" + fileName + " is uploaded successfully</h2>");
		logger.log(Level.INFO, "File{0}being uploaded to {1}", new Object[] { fileName, path });

		// Here, we start the import
		Vector<String> output = restoreService.importFile(userId, fullFileName);
		for (String str : output) {
			writer.println(str);
		}

		writer.println("<br>Import is completed successfully");

		out.close();
		if (null != fileContent)
			fileContent.close();

	}

	private String getFileName(final Part part) {
		if(part == null) {
			return "null";
		}
		final String partHeader = part.getHeader("content-disposition");
		logger.log(Level.INFO, "Part Header = {0}", partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return "null";
	}
	// FATAL ERROR: YOU CAN NOT MAP TO STATIC RESOURCES. ONLY JSP.

}
