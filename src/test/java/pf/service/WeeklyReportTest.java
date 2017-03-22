package pf.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.when;

import javax.annotation.Resource;
import javax.mail.search.DateTerm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import pf.email.Mailer;
import pf.transaction.TransactionService;
import pf.user.UserRepository;
import pf.user.UserService;

import org.thymeleaf.context.Context;

@SpringBootTest
@RunWith(SpringRunner.class)

public class WeeklyReportTest {

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
		// String userId = "4f680c93-838d-4329-9aba-7bedca232a89", userEmail =
		// "test@test.test";
//		 when(mailerMock.sendWeeklyReport(anyString(), anyString(), anyString())).thenReturn();

		WeeklyReport weeklyReport = new WeeklyReport(chartService, transactionService, userRepository, templateEngine, mailerMock, batchReportsFolder); 
		weeklyReport.process();
		assertEquals("test@test.test", weeklyReport.getUserEmail());
	
	}


	


}
