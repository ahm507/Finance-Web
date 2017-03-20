package pf.transaction;

import com.google.gson.Gson;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pf.user.UserRepository;
import pf.webmvc.RestLib;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rest/transactions")
public class TransactionRest {
	
	TransactionService transactionService;
	UserRepository userRepository;
	public TransactionRest(TransactionService transactionService, UserRepository userRepository) {
		this.transactionService = transactionService;
		this.userRepository = userRepository;
	}

	private final static Logger LOGGER = Logger.getLogger(TransactionRest.class.getName());


	@RequestMapping(method = RequestMethod.GET, path="/hello")
	public String index() {
		return "Greetings from transactions rest apis!";
	}

	@RequestMapping("/getTransactions.do")
	public String getTransactions(HttpServletRequest request,
			@RequestParam("account") String accountId) {
		try {
			
//			String userId = currentUser.getId();//RestLib.getLoggedInUser(request);
//			String userEmail = request.getRemoteUser();
			String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			LOGGER.info("Logged in user email: " + userEmail);
			String userId = userRepository.findByEmail(userEmail).getId();

			Map<String, Object> transactions = transactionService.getTransactionsMap(userId, accountId);
			return new Gson().toJson(transactions);
		} catch (Exception e) {
			return RestLib.getErrorString(e);
		}
	}
	
	//FIXME: request is no longer used after the new authentication.
	
	@RequestMapping("/getYearList.do")
	public String getYearList(HttpServletRequest request) {
		String yearsString;
		try {
			LOGGER.info("Logged in User Id request.getRemoteUser(): " + request.getRemoteUser());
			List<String> years = transactionService.getYearList(request.getRemoteUser());
			if(null == years || years.size() == 0){
				yearsString = new Gson().toJson("NO-DATA");
			}
			else {
				yearsString = new Gson().toJson(years);
			}
		} catch (Exception e) {
			yearsString = RestLib.getErrorString(e);
		}
		return yearsString;
	}
		
	@RequestMapping("/getYearTransactions.do")
	public String getYearTransactions(HttpServletRequest request,
			@RequestParam("year") String year, @RequestParam("accountId") String accountId) {
		String jsonString;
		try {
			String userEmail = request.getRemoteUser();
			LOGGER.info("Logged in user email: " + userEmail);
			String userId = userRepository.findByEmail(userEmail).getId();

			if(year.equals("all")) {
				jsonString = new Gson().toJson(transactionService.getTransactionsMap(userId, accountId));
			} else {
				jsonString = new Gson().toJson(transactionService.getYearTransactions(userId, accountId, year));
			}
		} catch (Exception e) {
			jsonString = RestLib.getErrorString(e);
		}
		
		return jsonString;
	}

	@RequestMapping("/getUpToMonthTransactions.do")
	public String getUpToMonthTransactions(HttpServletRequest request,
			@RequestParam("year") String year, @RequestParam("accountId") String accountId, 
			@RequestParam("month") String month) {
		String jsonString;
		try {
			String userEmail = request.getRemoteUser();
			LOGGER.info("Logged in user email: " + userEmail);
			String userId = userRepository.findByEmail(userEmail).getId();
			jsonString = new Gson().toJson(transactionService.getUpToMonthTransactions(userId, accountId, year, month));
		} catch (Exception e) {
			jsonString = RestLib.getErrorString(e);
		}
		return jsonString;
	}

	@RequestMapping("/getUpToYearTransactions.do")
	public String getUpToYearTransactions(HttpServletRequest request,
			@RequestParam("year") String year, @RequestParam("accountId") String accountId) {
		String jsonString;
		try {
			String userEmail = request.getRemoteUser();
			LOGGER.info("Logged in user email: " + userEmail);
			String userId = userRepository.findByEmail(userEmail).getId();
			if(year.equals("all")) {
				jsonString = new Gson().toJson(transactionService.getTransactionsMap(userId, accountId));
			} else {
				jsonString = new Gson().toJson(transactionService.getUpToYearTransactions(userId, accountId, year));
			}
			
		} catch (Exception e) {
			jsonString = RestLib.getErrorString(e);
		}
		return jsonString;
	}

	@RequestMapping("/getMonthTransactions.do")
	public String getMonthTransactions(HttpServletRequest request,
			@RequestParam("year") String year, @RequestParam("accountId") String accountId, @RequestParam("month") String month) {
		String jsonString;
		try {
			
			String userEmail = request.getRemoteUser();
			LOGGER.info("Logged in user email: " + userEmail);
			String userId = userRepository.findByEmail(userEmail).getId();

			jsonString = new Gson().toJson(transactionService.getMonthTransactions(userId, accountId, year, month));
		} catch (Exception e) {
			jsonString = RestLib.getErrorString(e);
		}
		return jsonString;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveTransaction.do")
	public String saveTransaction(HttpServletRequest request,
			@RequestParam("amount") String amount, @RequestParam("date") String date, @RequestParam("description") String description, 
			@RequestParam("withdraw") String withdraw, @RequestParam("deposit") String deposit) {
		String jsonString;
		try {
			//TODO: Simply send the whole user object
			String userEmail = request.getRemoteUser();
			LOGGER.info("Logged in user email: " + userEmail);
			String userId = userRepository.findByEmail(userEmail).getId();
//			String userId = currentUser.getId();//RestLib.getLoggedInUser(request);
			transactionService.saveTransaction(userId, date, description, withdraw, deposit, amount);
			Map<String, Boolean>  map = new HashMap<> ();
			map.put("success", true);
			//fixme: do not directely convert to json. Even if the framewrok does do it for you, it a cross-cutting
			//behaviour that must be done in Filter or base class.
			jsonString = new Gson().toJson(map);
			
		} catch (Exception e) {
			jsonString = RestLib.getErrorString(e);
		}
		return jsonString;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateTransaction.do")
	public String updateTransaction(HttpServletRequest request,
			@RequestParam("amount") String amount, @RequestParam("date") String date, @RequestParam("description") String description, 
			@RequestParam("withdraw") String withdraw, @RequestParam("deposit") String deposit, 
			/*Note that, id is query parameter*/@RequestParam("id") String id) { 
		String jsonString;
		try {
			//We are happy, because we are sure the user is authenticated already
//			RestLib.getLoggedInUser(request); // you can delete transactionService that you does not know, not a big risk as the id itself is complex and secret
			//FIXME: transaction should include UserID to assure no cross account hacking could be done. 
			transactionService.updateTransaction(id, date, description, withdraw, deposit, amount);
			Map<String, Boolean>  map = new HashMap<> ();
			map.put("success", true);
			jsonString = new Gson().toJson(map);
		} catch (Exception e) {
			jsonString = RestLib.getErrorString(e);
		}
		return jsonString;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/removeTransaction.do")
	public String removeTransaction(HttpServletRequest request, @RequestParam("id") String id) {
		String jsonString;
		try {			
//			RestLib.getLoggedInUser(request);// you can delete transactionService that you does not know, not a big risk as the id itself is complex and secret
			//FIXME: transaction should include UserID to assure no cross account hacking could be done.
			transactionService.removeTransaction(id);
			Map<String, Boolean>  map = new HashMap<> ();
			map.put("success", true);
			jsonString = new Gson().toJson(map);
		} catch (Exception e) {
			jsonString = RestLib.getErrorString(e);
		}
		return jsonString;
	}

}
