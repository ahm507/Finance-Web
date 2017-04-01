package pf.charts;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pf.RestLib;
import pf.user.UserRepository;
import pf.user.UserRest;

/**
 * This is a thin layer REST class for the ChartApi class. Just a wrapper to
 * provide system APIs as rest. API.
 */

@RestController
@RequestMapping("/rest/charts")
public class ChartRest {

	private final static Logger logger = Logger.getLogger(UserRest.class.getName());

	ChartService chartService;
	UserRepository userRepository;
	WeeklyReport weeklyReport;
	ChartRest(ChartService chartService, UserRepository userRepository, WeeklyReport weeklyReport) {
		this.chartService = chartService;
		this.userRepository = userRepository;
		this.weeklyReport = weeklyReport;
	}

	@RequestMapping("/getExpensesTrend.do")
	public List<Map<String, Object>> getExpensesTrend(HttpServletRequest request, @RequestParam("year") String year,
			@RequestParam("type") String type) throws Exception {

		String userEmail = request.getRemoteUser();
		String userId = userRepository.findByEmail(userEmail).getId();
		return chartService.getExpensesTrend(year, type, userId, userEmail);
	}

	@RequestMapping("/getChartDataFields.do")
	public List<Map<String, String>> getChartDataFields(HttpServletRequest request, @RequestParam("type") String type)
			throws Exception {

		String userEmail = request.getRemoteUser();// currentUser.getId();//RestLib.getLoggedInUser(request);
		String userId = userRepository.findByEmail(userEmail).getId();// currentUser.getEmail();//(String)request.getSession().getAttribute("email");

		return chartService.getChartFields(type, userId);

	}

	@RequestMapping("/weekly-report")
	public String weeklyReport(HttpServletRequest request) throws FileNotFoundException, Exception {
		String userEmail = request.getRemoteUser();
		weeklyReport.processForUser(userRepository.findByEmail(userEmail));
		return "Seems OK, please check your inbox";
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String handleError(HttpServletRequest req, Exception ex) {
		logger.severe("Request: " + req.getRequestURL() + " raised " + ex);
		return RestLib.getErrorString(ex);

	}


}
