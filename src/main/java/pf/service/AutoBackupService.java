package pf.service;//package pf.service;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.web.context.annotation.SessionScope;
//
////import pf.db.AccountStoreJdbc;
//
////Called from JSP
////It returns CSV file and should be clean
//
////@ApplicationScope
//@SessionScope
//@Service
////Default is keeping the variable
//public class AutoBackupService {
//
//	private int count = 0;
//
////	@Scheduled(cron="1 * * * *")
//
////	public Future<String> autoBackupDatabase() {
//
////	@Async //caller will return immediately
//	@Scheduled(fixedRate = 3000) //5 seconds
//	public void autoBackupDatabase() {
//		int num = count;
//		count++;
//
//		System.out.println(">>Before Backup: " + num);
////		Thread.sleep(3000);
//		System.out.println(">>After backup: " + num);
//
//
////		Thread.wait(1000); //the backup itself
////		Future<String> value = new Future<>();
////		return new AsyncResult<>(">>This is the autobackup");
//	}
//
//}
