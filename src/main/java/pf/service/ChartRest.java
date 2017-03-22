package pf.service;

import com.google.gson.Gson;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pf.user.UserRepository;
import pf.user.UserRest;
import pf.web.RestLib;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

	ChartRest(ChartService chartService, UserRepository userRepository) {
		this.chartService = chartService;
		this.userRepository = userRepository;
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
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String handleError(HttpServletRequest req, Exception ex) {
		logger.severe("Request: " + req.getRequestURL() + " raised " + ex);
		return RestLib.getErrorString(ex);

	}


}
