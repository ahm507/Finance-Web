package pf.transaction;

import com.google.gson.Gson;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pf.service.WeeklyReport;
import pf.user.UserRepository;
import pf.web.RestLib;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rest/transactions")
public class TransactionRest {

	private final static Logger logger = Logger.getLogger(TransactionRest.class.getName());

	TransactionService transactionService;
	UserRepository userRepository;

	public TransactionRest(TransactionService transactionService, UserRepository userRepository) {
		this.transactionService = transactionService;
		this.userRepository = userRepository;
	}

	@RequestMapping("/getTransactions.do")
	public Map<String, Object> getTransactions(HttpServletRequest request, @RequestParam("account") String accountId)
			throws Exception {

		// String userId =
		// currentUser.getId();//RestLib.getLoggedInUser(request);
		// String userEmail = request.getRemoteUser();

		//FIXME: Use SecurityContextHolder instead of gertRemoteUser
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("Logged in user email: " + userEmail);
		String userId = userRepository.findByEmail(userEmail).getId();

		return transactionService.getTransactionsMap(userId, accountId);

	}

	@RequestMapping("/getYearList.do")
	public String getYearList(HttpServletRequest request) throws Exception {
		String yearsString;

		logger.info("Logged in User Id request.getRemoteUser(): " + request.getRemoteUser());
		List<String> years = transactionService.getYearList(request.getRemoteUser());
		if (null == years || years.size() == 0) {
			yearsString = new Gson().toJson("NO-DATA");
		} else {
			yearsString = new Gson().toJson(years);
		}
		return yearsString;
	}

	@RequestMapping("/getYearTransactions.do")
	public String getYearTransactions(HttpServletRequest request, @RequestParam("year") String year,
			@RequestParam("accountId") String accountId) throws Exception {
		String jsonString;

		String userEmail = request.getRemoteUser();
		logger.info("Logged in user email: " + userEmail);
		String userId = userRepository.findByEmail(userEmail).getId();

		if (year.equals("all")) {
			jsonString = new Gson().toJson(transactionService.getTransactionsMap(userId, accountId));
		} else {
			jsonString = new Gson().toJson(transactionService.getYearTransactions(userId, accountId, year));
		}

		return jsonString;
	}

	@RequestMapping("/getUpToMonthTransactions.do")
	public List<TransactionDTO> getUpToMonthTransactions(HttpServletRequest request, @RequestParam("year") String year,
			@RequestParam("accountId") String accountId, @RequestParam("month") String month) throws Exception {

		String userEmail = request.getRemoteUser();
		logger.info("Logged in user email: " + userEmail);
		String userId = userRepository.findByEmail(userEmail).getId();
		return transactionService.getUpToMonthTransactions(userId, accountId, year, month);

	}

	@RequestMapping("/getUpToYearTransactions.do")
	public String getUpToYearTransactions(HttpServletRequest request, @RequestParam("year") String year,
			@RequestParam("accountId") String accountId) throws Exception {
		String jsonString;
		String userEmail = request.getRemoteUser();
		logger.info("Logged in user email: " + userEmail);
		String userId = userRepository.findByEmail(userEmail).getId();
		if (year.equals("all")) {
			jsonString = new Gson().toJson(transactionService.getTransactionsMap(userId, accountId));
		} else {
			jsonString = new Gson().toJson(transactionService.getUpToYearTransactions(userId, accountId, year));
		}

		return jsonString;
	}

	@RequestMapping("/getMonthTransactions.do")
	public List<TransactionDTO> getMonthTransactions(HttpServletRequest request, @RequestParam("year") String year,
			@RequestParam("accountId") String accountId, @RequestParam("month") String month) throws Exception {

		String userEmail = request.getRemoteUser();
		logger.info("Logged in user email: " + userEmail);
		String userId = userRepository.findByEmail(userEmail).getId();

		return transactionService.getMonthTransactions(userId, accountId, year, month);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveTransaction.do")
	public Map<String, Boolean> saveTransaction(HttpServletRequest request, @RequestParam("amount") String amount,
			@RequestParam("date") String date, @RequestParam("description") String description,
			@RequestParam("withdraw") String withdraw, @RequestParam("deposit") String deposit) throws Exception {

		// TODO: Simply send the whole user object
		String userEmail = request.getRemoteUser();
		logger.info("Logged in user email: " + userEmail);
		String userId = userRepository.findByEmail(userEmail).getId();
		// String userId =
		// currentUser.getId();//RestLib.getLoggedInUser(request);
		transactionService.saveTransaction(userId, date, description, withdraw, deposit, amount);
		Map<String, Boolean> map = new HashMap<>();
		map.put("success", true);

		return getSuccessStatus();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateTransaction.do")
	public Map<String, Boolean> updateTransaction(HttpServletRequest request, @RequestParam("amount") String amount,
			@RequestParam("date") String date, @RequestParam("description") String description,
			@RequestParam("withdraw") String withdraw, @RequestParam("deposit") String deposit,
			/* Note that, id is query parameter */@RequestParam("id") String id) throws Exception {
		// We are happy, because we are sure the user is authenticated
		// already
		// RestLib.getLoggedInUser(request); // you can delete
		// transactionService that you does not know, not a big risk as the
		// id itself is complex and secret
		// FIXME: transaction should include UserID to assure no cross
		// account hacking could be done.
		transactionService.updateTransaction(id, date, description, withdraw, deposit, amount);
		Map<String, Boolean> map = new HashMap<>();
		map.put("success", true);

		return getSuccessStatus();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/removeTransaction.do")
	public Map<String, Boolean> removeTransaction(HttpServletRequest request, @RequestParam("id") String id)
			throws Exception {

		// RestLib.getLoggedInUser(request);// you can delete
		// transactionService that you does not know, not a big risk as the
		// id itself is complex and secret
		// FIXME: transaction should include UserID to assure no cross
		// account hacking could be done.
		transactionService.removeTransaction(id);
		return getSuccessStatus();
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String handleError(HttpServletRequest req, Exception ex) {
		logger.severe("Request: " + req.getRequestURL() + " raised " + ex);
		return RestLib.getErrorString(ex);

	}

	@RequestMapping("/exception.do")
	public String exception(HttpServletRequest request) throws Exception {
		throw new Exception("This is a test exception from REST!");
	}

	private Map<String, Boolean> getSuccessStatus() {
		Map<String, Boolean> map = new HashMap<>();
		map.put("success", true);
		return map;
	}

}
