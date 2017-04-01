package pf.backup;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pf.RestLib;
///rest/backup//backup.do
@RestController
@RequestMapping("/rest/backup")

public class BackupRest {
	
	private static final Logger log = LoggerFactory.getLogger(BackupRest.class);

	BackupService backupService;
	public BackupRest(BackupService backupService) {
		this.backupService = backupService;
	}
	
	//This call actually used for testing and can be used by UI to let specific user trigger a backup
	@RequestMapping("/backup-email.do")
	public String backup(HttpServletRequest request) throws Exception {

		String userEmail = request.getRemoteUser();
		backupService.autoBackupForUser(userEmail); //Sent by email
		
		return "{message: \"Seems ok, check your ~/Server/pf-batch-reports/*.zip and check email to get the zip backup.\"}";
		
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String handleError(HttpServletRequest req, Exception ex) {
		log.error("Request: " + req.getRequestURL() + " raised " + ex);
		return RestLib.getErrorString(ex);

	}

}
