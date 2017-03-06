package pf.user;import com.google.gson.Gson;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestParam;import org.springframework.web.bind.annotation.RestController;import pf.account.AccountService;import pf.email.Mailer;import pf.webmvc.RestLib;import javax.servlet.http.HttpServletRequest;import java.util.HashMap;import java.util.Map;@RestController@RequestMapping("/rest/users")public class UserRest {	@Autowired	UserService userService;	@Autowired	AccountService accountService;	@Autowired	UserRepository userRepository;//	@RequestMapping("/login.do")//	public String login(HttpServletRequest request,//						@RequestParam("email") String email, @RequestParam("password") String password) {//		try {//			HttpSession session = request.getSession();//			Map<String, String> map = new HashMap<>();//			email = email.toLowerCase();//			UserEntity user = userService.login(email, password);//			session.setAttribute("email", email);//			session.setAttribute("userId", user.getId());//			map.put("status", "success");//			return new Gson().toJson(map);//		} catch (Exception exp) {//			return RestLib.getErrorString(exp);//		}//	}	//	@RequestMapping("/logout.do")//	public String logout(HttpServletRequest request, UserEntity currentUser) {//		try {//			HttpSession session = request.getSession();//			Map<String, String> map = new HashMap<>();//			session.setAttribute("email", null);//			session.setAttribute("userId", null);//			map.put("status", "success");//			return new Gson().toJson(map);//		} catch (Exception exp) {//			return RestLib.getErrorString(exp);//		}//	}	@RequestMapping("/register.do")	public String register( 			@RequestParam("email") String email,			@RequestParam("password") String password, @RequestParam("password2") String password2) {		try {			Mailer mailer = new Mailer();			double usdRate = 1.0;			double sarRate = 1.0;			userService.registerUser(email, password, password2, mailer, accountService, usdRate, sarRate);			Map<String, String> map = new HashMap<>();			map.put("status", "success");			return new Gson().toJson(map);		} catch (Exception exp) {			return RestLib.getErrorString(exp);		}	}	@RequestMapping("/resendVerifyEmail.do")	public String resendVerifyEmail(									@RequestParam("email") String email) {		try {			userService.resendVerifyEmail(email);			Map<String, String> map = new HashMap<>();			map.put("status", "success");			return new Gson().toJson(map);		} catch (Exception exp) {			return RestLib.getErrorString(exp);		}	}	@RequestMapping("/changeSettings.do")	public String changeSettings(								 @RequestParam("email") String email,								 @RequestParam("oldPassword") String oldPassword,								 @RequestParam("password") String password,								 @RequestParam("password2") String password2,								 @RequestParam("usd_rate") String usdRate,								 @RequestParam("sar_rate") String sarRate) {		try {//			RestLib.getLoggedInUser(request); 			//FIXME: Ensure the same user is changing his own settings only. Replace request parameter email by UserEntity			userService.updateSettings(email, oldPassword, password, password2, Double.parseDouble(usdRate),					Double.parseDouble(sarRate));			Map<String, String> map = new HashMap<>();			map.put("status", "success");			return new Gson().toJson(map);		} catch (Exception exp) {			return RestLib.getErrorString(exp);		}	}	@RequestMapping("/sendResetEMail.do")	public String sendResetEMail(								 @RequestParam("email") String email) {		try {			userService.sendResetEmail(email);			Map<String, String> map = new HashMap<>();			map.put("status", "success");			return new Gson().toJson(map);		} catch (Exception exp) {			return RestLib.getErrorString(exp);		}	}		@RequestMapping("/resetPassword.do")	public String resetPassword(@RequestParam("email") String email,			@RequestParam("code") String code, @RequestParam("password") String password,			@RequestParam("password2") String password2) {		try {//			configure();			//fixme: ensure email is from the authenticated user only			userService.resetPassword(email, code, password, password2);			Map<String, String> map = new HashMap<>();			map.put("status", "success");			return new Gson().toJson(map);		} catch (Exception exp) {			return RestLib.getErrorString(exp);		}	}	@RequestMapping("/contactus.do")	public String contactus(							@RequestParam("email") String email,							@RequestParam("name") String name,							@RequestParam("title") String title,							@RequestParam("comments") String comments) {		try {//			configure();			if(name == null) {				name = "";  //optional			}			if(title == null){				title = "";//optional			}			userService.sendFeedbackEmail(email, name, title, comments);						Map<String, String> map = new HashMap<>();			map.put("status", "success");			return new Gson().toJson(map);		} catch (Exception exp) {			return RestLib.getErrorString(exp);		}	}	@RequestMapping("/getAccountsRoot.do")	public String getAccountsRoot(HttpServletRequest request) {		try {//			HttpSession session = request.getSession();//			String userId = (String)session.getAttribute("userId");			//String userId = RestLib.getLoggedInUser(request);//			String userEmail = (String)request.getSession().getAttribute("email");			String userEmail = request.getRemoteUser();//currentUser.getId();//RestLib.getLoggedInUser(request);			String userId = userRepository.findByEmail(userEmail).getId();//currentUser.getEmail();//(String)request.getSession().getAttribute("email");			//fixme: rest layer must not have logic or details.			return new Gson().toJson(accountService.getAccountsTreeWithOneRoot(userId, userEmail));		} catch (Exception exp) {			return RestLib.getErrorString(exp);		}	}	@RequestMapping("/getExchangeRates.do")	public String getExchangeRates(HttpServletRequest request) {		try {//			configure();//			HttpSession session = request.getSession();//			String userEmail = currentUser.getEmail();//(String);\request.getSession().getAttribute("email");			String userEmail = request.getRemoteUser();			double usdRate = accountService.getUsdRate(userEmail);			double sarRate = accountService.getSarRate(userEmail);			Map<String, Double> map = new HashMap<>();			map.put("usd_rate", usdRate);			map.put("sar_rate", sarRate);			return new Gson().toJson(map);		} catch (Exception exp) {			return RestLib.getErrorString(exp);		}	}		}