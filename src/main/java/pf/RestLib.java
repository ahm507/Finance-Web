package pf;

import com.google.gson.Gson;
import pf.user.UserRest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RestLib {
	private final static Logger LOGGER = Logger.getLogger(UserRest.class.getName());

	public static String getErrorInfo(Throwable exception) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);

		Throwable cause = exception.getCause();
		if(cause != null)	{
			printWriter.print("CAUSE IS:");
			cause.printStackTrace(printWriter);
		}

		String str = stringWriter.toString();
		str = str.replace("\n", "<br>");
		str = str.replace("\t", "    ");
		
		//because of json encoding, the html tags has no effect
		//str = "---------------------------------------------------------------------------------------" + str;
		//str = str.replace(System.getProperty("line.separator"), "<br/>\n");		
	
		return str;
	}

	public String getWebInfPath() {
		URL url = this.getClass().getResource("/");
		String path = url.getPath();
		return path.substring(0, path.lastIndexOf("/")); //remove trailing slash
	}

//	public static String getLoggedInUser(HttpServletRequest request) throws Exception {
//		HttpSession session = request.getSession();
//		String userId = (String)session.getAttribute("userId");
//		if(userId == null) {
//			throw new AuthenticationRequiredException("");
//		}
//		return userId;
//	}

	public static String getErrorString(Exception exp) {
		Map<String, String> map = new HashMap<>();
		map.put("status", "fail");
		map.put("error", exp.getMessage());
		map.put("details", getErrorInfo(exp));
		map.put("msg", "Exception" + getErrorInfo(exp));
	    LOGGER.severe(getErrorInfo(exp));
		return new Gson().toJson(map);
	}


	

}
