package pf.account;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pf.user.UserRepository;
import pf.web.RestLib;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/accounts")
public class AccountRest {
	

	AccountService accountService;
	UserRepository userRepository;

	public AccountRest(AccountService accountService, UserRepository userRepository) {
		this.accountService = accountService;
		this.userRepository = userRepository;
	}
		
	
//	private final static Logger LOGGER = Logger.getLogger(AccountRest.class.getName()); 

//	@POST //to match the easyui tree grid way
//	method = RequestMethod.POST,
	@RequestMapping(value = "/getAccounts.do")
	public String getAccounts(HttpServletRequest request) {
		String json;
		try {
			json = new Gson().toJson(accountService.getAccountsTree(request.getRemoteUser()));//, type
		} catch (Exception exp) {
			json = RestLib.getErrorString(exp);
		}
		
		//FIXME: You do not need to convert to json yourself. It is already done for you. You can also remove the mvn lib used.
		return json;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveAccount.do")
	public String saveAccount(HttpServletRequest request,
	                          @RequestParam("text") String text,
							  @RequestParam("description") String description, @RequestParam("type") String type,
							  @RequestParam("currency") String currency) {
		String json;
		try {
			String userId = userRepository.findByEmail(request.getRemoteUser()).getId();
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
			String userId = userRepository.findByEmail(request.getRemoteUser()).getId();
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
			String userId = userRepository.findByEmail(request.getRemoteUser()).getId();
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
			//FIXME: You can send user entity directly to service layer
			String userId = userRepository.findByEmail(request.getRemoteUser()).getId();
			AccountEntity accountentity = accountService.getAccount(userId, id);
			json = new Gson().toJson(accountentity);
		} catch (Exception exp) {
			json = RestLib.getErrorString(exp);
		}
		return json;
	}
}
