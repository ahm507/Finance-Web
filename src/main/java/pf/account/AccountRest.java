package pf.account;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pf.webmvc.RestLib;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/accounts")
public class AccountRest {
	
	@Autowired
	AccountService accountService;
//	private final static Logger LOGGER = Logger.getLogger(AccountRest.class.getName()); 

//	public void configure() throws Exception {
//		StoreFactory storeFactory = new RestLib().getStoreFactory();
//		accountService = storeFactory.createAccountMgmt();
//	}

//	@POST //to match the easyui tree grid way
	@RequestMapping(method = RequestMethod.POST, value = "/getAccounts.do")
	public String getAccounts(HttpServletRequest request){
		String json;
		try {
//			configure();
			String userId = RestLib.getLoggedInUser(request);
			String userEmail = (String)request.getSession().getAttribute("email");
			json = new Gson().toJson(accountService.getAccountsTree(userId, userEmail));//, type
		} catch (Exception exp) {
			json = RestLib.getErrorString(exp);
		}
		return json;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveAccount.do")
	public String saveAccount(HttpServletRequest request, @RequestParam("text") String text,
							  @RequestParam("description") String description, @RequestParam("type") String type,
							  @RequestParam("currency") String currency) {
		String json;
		try {
//			configure();
			String userId = RestLib.getLoggedInUser(request);
			accountService.create(userId, text, description, type, currency);
			Map<String, Boolean> map = new HashMap<>();
			map.put("success", true);
			json = new Gson().toJson(map);
		} catch (Exception exp) {
			json = RestLib.getErrorString(exp);
		}
		return json;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/removeAccount.do")
	public String removeAccount(HttpServletRequest request, @RequestParam("id") String id) {
		String json;
		try {
//			configure();
			String userId = RestLib.getLoggedInUser(request);
			accountService.removeAccount(userId, id);
			Map<String, Boolean> map = new HashMap<>();
			map.put("success", true);
			json = new Gson().toJson(map);
		} catch (Exception exp) {
			json = RestLib.getErrorString(exp);
		}
		return json;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/updateAccount.do")
	public String updateAccount(HttpServletRequest request,
								@RequestParam("id") String id,
								@RequestParam("text") String text,
								@RequestParam("description") String description,
								@RequestParam("type") String type,
								@RequestParam("currency") String currency) {
		String json;
		try {
//			configure();
			String userId = RestLib.getLoggedInUser(request);
			accountService.updateAccount(userId, id, text, description, type, currency);
			Map<String, Boolean> map = new HashMap<>();
			map.put("success", true);
			json = new Gson().toJson(map);
		} catch (Exception exp) {
			json = RestLib.getErrorString(exp);
		}
		return json;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/getAccount.do")
	public String getAccount(HttpServletRequest request, @RequestParam("id") String id) {
		String json;
		try {
//			configure();
			String userId = RestLib.getLoggedInUser(request);
			 AccountEntity accountentity = accountService.getAccount(userId, id);
			json = new Gson().toJson(accountentity);
		} catch (Exception exp) {
			json = RestLib.getErrorString(exp);
		}
		return json;
	}
}
