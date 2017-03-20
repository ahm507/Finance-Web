package pf.webmvc;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import pf.service.RestoreService;
import pf.user.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This servlet is responsible on uploading files, typically the csv file to restore backup from.
 * @author Ahmed Hammad
 *
 */


@Controller
public class FileUploadController {
	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(FileUploadController.class.getName());

	UserRepository userRepository;
	RestoreService restoreService;

	public FileUploadController(UserRepository userRepository, RestoreService restoreService) {
		this.userRepository = userRepository;
		this.restoreService = restoreService;
	}
	
	

	@PostMapping("/upload")
	void upload(HttpServletRequest request,
	            HttpServletResponse response
	            ) throws IOException, ServletException {


//FIXME: This is too much work, see:
//https://www.mkyong.com/spring-boot/spring-boot-file-upload-example/
//In one single parameter you can get byte array with the file. CSV reader acceot any kind of Reader, including StringReader.

		LOGGER.info("Started inside upload() API");

//		String userId = getLoggedInUser(request);
		String userEmail = request.getRemoteUser();
		String userId = userRepository.findByEmail(userEmail).getId();

		final String path = request.getServletContext().getRealPath("/WEB-INF/upload-temp/");
	    final Part filePart = request.getPart("file");
	    final String fileName = getFileName(filePart);

	    OutputStream out = null;
	    InputStream filecontent = null;
		response.setContentType("text/html;charset=UTF-8");
	    final PrintWriter writer = response.getWriter();
	    String fullFileName = path + File.separator + userId + "-" + fileName;
	    try {
	    	out = new FileOutputStream(new File(fullFileName));
	        filecontent = filePart.getInputStream();

	        int read = 0;
	        final byte[] bytes = new byte[1024];

	        while ((read = filecontent.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
	        writer.println("<h2>" + fileName + " is uploaded successfully</h2>");
	        LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", new Object[]{fileName, path});
	    } catch (FileNotFoundException fne) {

	    	writer.println("You either did not specify a file to upload or are "
	                + "trying to upload a file to a protected or nonexistent "
	                + "location.");
	        writer.println("<br/> ERROR: " + fne.getMessage());

	        LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
	                new Object[]{fne.getMessage()});

	   } catch (Exception e) {
	    		LOGGER.severe(getErrorInfo(e));
	    		//e.printStackTrace();
		} finally {

		        if (out != null) {
		            out.close();
		        }
		        if (filecontent != null) {
		            filecontent.close();
		        }
	    }
        
        //Here, we start the import
		try {
	        Vector<String> output = restoreService.importFile(userId, fullFileName);
	        for(String str : output) {
	        	writer.println(str);
	        }
	        
			writer.println("<br>Import is completed successfully");
		} catch (Exception e) {
			request.setAttribute("javax.servlet.jsp.jspException", e);
//			RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
//			dispatcher.forward(request, response);
			response.sendRedirect("/error");
		}
	}	
	
	private String getFileName(final Part part) {
	    final String partHeader = part.getHeader("content-disposition");
	    LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
	    for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(
	                    content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}
		
//	private String getLoggedInUser(HttpServletRequest request) {
//		HttpSession session = request.getSession();
//		String userId = (String)session.getAttribute("userId");
//		//String userId = String.valueOf(intId);
//		return userId;
//	}

	public String getErrorInfo(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		if(e != null) {
			Throwable c = e.getCause();
			if(c != null)	{
				pw.print("CAUSE IS:");
				c.printStackTrace(pw);
			}
		}
		String str = sw.toString();
		//because of json encoding, the html tags has no effect
		//str = "---------------------------------------------------------------------------------------" + str;
		//str = str.replace(System.getProperty("line.separator"), "<br/>\n");		
	
		return str;
	}

}
