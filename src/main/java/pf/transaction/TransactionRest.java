package pf.transaction;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pf.webmvc.RestLib;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/transactions")
public class TransactionRest {
	
	@Autowired
	TransactionService transactionService;
//	private final static Logger LOGGER = Logger.getLogger(TransactionRest.class.getName());


	@RequestMapping(method = RequestMethod.GET, path="/hello")
	public String index() {
		return "Greetings from transactions rest apis!";
	}

	@RequestMapping("/getTransactions.do")
	public String getTransactions(HttpServletRequest request, 
			@RequestParam("account") String account) {
		try {
			
			String userId = RestLib.getLoggedInUser(request);
			Map<String, Object> transactions = transactionService.getTransactionsMap(userId, account);
			return new Gson().toJson(transactions);
		} catch (Exception e) {
			return RestLib.getErrorString(e);
		}
	}
	
	@RequestMapping("/getYearList.do")
	public String getYearList(HttpServletRequest request) {
		String yearsString;
		try {
			
			String userId = RestLib.getLoggedInUser(request);
			List<String> years = transactionService.getYearList(userId);
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
	
//	public void configure() throws Exception {
//		StoreFactory storeFactory = new RestLib().getStoreFactory();
//		//load business objects
//		transactionService = storeFactory.createTransactionMgmt();
//	}
	
	@RequestMapping("/getYearTransactions.do")
	public String getYearTransactions(HttpServletRequest request, 
			@RequestParam("year") String year, @RequestParam("accountId") String accountId) {
		String jsonString;
		try {
//			
			String userId = RestLib.getLoggedInUser(request);
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
			
			String userId = RestLib.getLoggedInUser(request);
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
			
			String userId = RestLib.getLoggedInUser(request);
			
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
			
			String userId = RestLib.getLoggedInUser(request);
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
			
			String userId = RestLib.getLoggedInUser(request);
			transactionService.saveTransaction(userId, date, description, withdraw, deposit, amount);
			Map<String, Boolean>  map = new HashMap<> ();
			map.put("success", true);
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
			
			RestLib.getLoggedInUser(request); // you can delete transactionService that you does not know, not a big risk as the id itself is complex and secret
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
			
			RestLib.getLoggedInUser(request);// you can delete transactionService that you does not know, not a big risk as the id itself is complex and secret
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
