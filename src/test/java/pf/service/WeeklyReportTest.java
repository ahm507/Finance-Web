package pf.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.mail.search.DateTerm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import pf.account.DeepAccountLayersException;
import pf.account.NullAccountException;
import pf.backup.BackupService;
import pf.backup.CurrencyTransefereException;
import pf.charts.ChartService;
import pf.charts.WeeklyReport;
import pf.email.Mailer;
import pf.transaction.TransactionService;
import pf.user.UserRepository;
import pf.user.UserService;

import org.thymeleaf.context.Context;

@SpringBootTest
@RunWith(SpringRunner.class)

public class WeeklyReportTest {

	
	
	private static final Logger log = LoggerFactory.getLogger(WeeklyReportTest.class);

	public WeeklyReportTest() {

	}

//	@InjectMocks
//	WeeklyReport weeklyReport;

	@Autowired
	ChartService chartService;
	@Autowired
	TransactionService transactionService;
	@Autowired
	SpringTemplateEngine templateEngine;
	@Autowired
	UserRepository userRepository;

	@Mock
	Mailer mailerMock;


	@Value("${pf.service.weekly-report.folder}")
	String batchReportsFolder;
	
	
	@Test
	public void weeklyReport() throws Exception {
		//when(mailerMock.sendWeeklyReport(anyString(), anyString(), anyString())).thenReturn();
		WeeklyReport weeklyReport = new WeeklyReport(chartService, transactionService, userRepository, templateEngine, mailerMock, batchReportsFolder); 
		weeklyReport.process();
		assertEquals("test@test.test", weeklyReport.getUserEmail());
		assertEquals(570.0, weeklyReport.getCurrentYearAverageBalance().getExpenses(), 000.1);
	}

	@Autowired
	BackupService backupService;
	
	@Test
	public void periodicBackup() throws Exception {
		backupService.autoBackup();
		String fileName = backupService.getPeridicFileFullPath();
//		FileOutputStream file = new FileOutputStream(fileName);
		assertTrue(fileName.length() > 0);
		log.info(fileName);
	}


}
