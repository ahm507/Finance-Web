package pf.account;

import com.google.gson.Gson;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pf.user.UserRepository;
import pf.user.UserRest;
import pf.RestLib;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rest/accounts")
public class AccountRest {

	private final static Logger logger = Logger.getLogger(UserRest.class.getName());

	AccountService accountService;
	UserRepository userRepository;

	public AccountRest(AccountService accountService, UserRepository userRepository) {
		this.accountService = accountService;
		this.userRepository = userRepository;
	}

	// private final static Logger LOGGER =
	// Logger.getLogger(AccountRest.class.getName());

	// @POST //to match the easyui tree grid way
	// method = RequestMethod.POST,
	@RequestMapping(value = "/getAccounts.do")
	public List<Account> getAccounts(HttpServletRequest request) throws Exception {

		return accountService.getAccountsTree(request.getRemoteUser());// , type
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveAccount.do")
	public Map<String, Boolean> saveAccount(HttpServletRequest request, @RequestParam("text") String text,
			@RequestParam("description") String description, @RequestParam("type") String type,
			@RequestParam("currency") String currency) throws Exception {

		String userId = userRepository.findByEmail(request.getRemoteUser()).getId();
		accountService.create(userId, text, description, type, currency);
		return getSuccessStatus();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/removeAccount.do")
	public Map<String, Boolean> removeAccount(HttpServletRequest request, @RequestParam("id") String id)
			throws Exception {

		String userId = userRepository.findByEmail(request.getRemoteUser()).getId();
		accountService.removeAccount(userId, id);
		return getSuccessStatus();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateAccount.do")
	public String updateAccount(HttpServletRequest request, @RequestParam("id") String id,
			@RequestParam("text") String text, @RequestParam("description") String description,
			@RequestParam("type") String type, @RequestParam("currency") String currency) throws Exception {
		String json;
		String userId = userRepository.findByEmail(request.getRemoteUser()).getId();
		accountService.updateAccount(userId, id, text, description, type, currency);
		Map<String, Boolean> map = new HashMap<>();
		map.put("success", true);
		json = new Gson().toJson(map);
		return json;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getAccount.do")
	public Account getAccount(HttpServletRequest request, @RequestParam("id") String id) throws Exception {
		// FIXME: You can send user entity directly to service layer
		String userId = userRepository.findByEmail(request.getRemoteUser()).getId();
		return accountService.getAccount(userId, id);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String handleError(HttpServletRequest req, Exception ex) {
		logger.severe("Request: " + req.getRequestURL() + " raised " + ex);
		return RestLib.getErrorString(ex);

	}

	private Map<String, Boolean> getSuccessStatus() {
		Map<String, Boolean> map = new HashMap<>();
		map.put("success", true);
		return map;
	}

}
