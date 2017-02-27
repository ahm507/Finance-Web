package pf.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pf.webmvc.RestLib;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * This is a thin layer REST class for the ChartApi class. Just a wrapper to provide
 * system APIs as rest. API.
 */

@RestController
@RequestMapping("/rest/charts")
public class ChartRest {

	@Autowired
	ChartService chartService;


	@RequestMapping("/getExpensesTrend.do")
	public String getExpensesTrend(HttpServletRequest request,
								   @RequestParam("year") String year,
								   @RequestParam("type") String type) throws Exception {

		String jsonString;

		try {
			String userId = RestLib.getLoggedInUser(request);
			String email = (String)request.getSession().getAttribute("email");

			List<Map<String, Object>> out2 = chartService.getExpensesTrend(year, type, userId, email);

			jsonString = new Gson().toJson(out2);

		} catch (Exception e) {
			jsonString = RestLib.getErrorString(e);
		}
		return jsonString;
	}

	@RequestMapping("/getChartDataFields.do")
	public String getChartDataFields(HttpServletRequest request,
									 @RequestParam("type") String type) {

		String jsonString;
		try {
			String userId = RestLib.getLoggedInUser(request);

			List<Map<String, String>> out = chartService.getChartFields(type, userId);

			jsonString = new Gson().toJson(out);

		} catch (Exception e) {
			jsonString = RestLib.getErrorString(e);
		}
		return jsonString;
	}

}
