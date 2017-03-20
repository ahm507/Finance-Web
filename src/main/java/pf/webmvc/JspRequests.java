package pf.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pf.user.UserEntity;
import pf.user.UserService;

/**
 * This class encapsulate all requests that come directly from JSP files, i.e. not using Ajax requests.
 *
 */

@Controller
public class JspRequests {
	
    UserService userService;
    public JspRequests(UserService userService) {
    	this.userService = userService;
	}
	
	//Called directly from JSP
//	public static void processIfNotLoggedUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		try {
//			HttpSession session = request.getSession();
//			String id = (String)session.getAttribute("userId");
//			if(id == null) {
//				response.sendRedirect("login"); //.jsp
//			}
//		} catch (IOException e) {
//			request.setAttribute("javax.servlet.jsp.jspException", e);
//			RequestDispatcher dispatcher = request.getRequestDispatcher("error"); //.jsp
//			dispatcher.forward(request, response);
//		}
//	}

	public boolean verifyUserEmail(String appRootPath, String email, String code) throws Exception {
		String emailLower = email.toLowerCase();
		UserEntity user = userService.verifyEmail(emailLower, code);
		return (user != null);
	}

	
}
