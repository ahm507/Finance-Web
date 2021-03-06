package pf;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;

import pf.user.UserRest;

public class RestLib {
	private final static Logger LOGGER = Logger.getLogger(UserRest.class.getName());

	private static String getErrorInfo(Throwable exception) {
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
