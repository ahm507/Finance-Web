package pf.service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import pf.email.Mailer;
import pf.transaction.TransactionService;
import pf.user.UserEntity;
import pf.user.UserRepository;

@Service
public class WeeklyReport {

	// Dependency Definition
	private ChartService chartService;
	private TransactionService transactionService;
	private SpringTemplateEngine templateEngine;
	private UserRepository userRepository;
	private Mailer mailer;
	private String batchReportsFolder;

	@Autowired
	public WeeklyReport(ChartService chartService, TransactionService transactionService, UserRepository userRepository,
			SpringTemplateEngine templateEngine, Mailer mailer,
			@Value("${pf.service.weekly-report.folder:/Users/Macpro/Server/pf-batch-reports/}") String batchReportsFolder) {
		this.chartService = chartService;
		this.transactionService = transactionService;
		this.userRepository = userRepository;
		this.templateEngine = templateEngine;
		this.mailer = mailer;
		this.batchReportsFolder = batchReportsFolder;
	}

	private final static Logger logger = Logger.getLogger(WeeklyReport.class.getName());

	// Internal Data
	private List<Balance> currentYearBalance = new ArrayList<>();
	private Balance currentMonth = new Balance("", 0, 0, 0, 0);
	private Balance currentYearTotalBalance = new Balance("", 0, 0, 0, 0);
	private Balance currentYearAverageBalance = new Balance("", 0, 0, 0, 0);
	private String userEmail;

	public String getUserEmail() {
		return userEmail;
	}

	// @Scheduled(cron="0 0 0 1,5,10,15,20,25,30 * *") //Every 5 days
	@Scheduled(cron = "0 0 0 * * *") // Every day as a testing period
	public void process() throws Exception {
		Iterable<UserEntity> allUsers = userRepository.findAll();// OrderByEmail
		for (UserEntity user : allUsers) {
			this.userEmail = user.getEmail();
			String yearString = new SimpleDateFormat("yyyy").format(new Date());
			retrieveData(user.getId(), yearString);
			doAnalysis();
			String to = user.getEmail();
			String emailBody = renderEmailTemplate(to);

			String dateStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String subject = String.format("Weekly Financial Report as of %s ", dateStamp);

			mailer.sendWeeklyReport(to, subject, emailBody);
			String fullPath = String.format("%s%s-%s.html", batchReportsFolder, to, dateStamp);
			storeToFile(emailBody, fullPath);
		}
	}

	private void storeToFile(String emailBody, String fullPath) throws FileNotFoundException {
		// Uses OutputStreamWriter with default encoding
		try (PrintWriter out = new PrintWriter(fullPath)) {
			out.write(emailBody);
			logger.info(String.format("Email message is stored at %s", fullPath));
		}
	}

	private void retrieveData(String userId, String currentYear) throws Exception {
		List<Map<String, Object>> data = chartService.getExpensesTrend(currentYear, "totals", userId, userEmail);

		for (Map<String, Object> element : data) {
			// [{"Month":"Jan","Expenses":21279.0,"Assets":269613.95999999996,"Liabilities":-468.5600000000006,"Income":23210.91}
			// double expenses, double income, double liabilities, double assets
			currentYearBalance.add(new Balance((String) element.get("Month"), (Double) element.get("Expenses"),
					(Double) element.get("Income"), (Double) element.get("Liabilities"),
					(Double) element.get("Assets")));
		}
	}

	public void doAnalysis() throws Exception {
		// this.userEmail = userEmail;

		// List<String> years = transactionService.getYearList(userEmail);

		// String currentYear =
		// String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		int currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH);
		currentMonth = currentYearBalance.get(currentMonthIndex);
		// if( ! years.contains(yearString)) {
		//
		// return ;
		// }

		// I assume last element is current year
		// compute all data analysis
		// currentYearTotal
		// currentYearAverage
		int months = currentYearBalance.size();
		for (Balance b : currentYearBalance) {
			currentYearTotalBalance.setIncome(currentYearTotalBalance.getIncome() + b.getIncome());
			currentYearTotalBalance.setExpenses(currentYearTotalBalance.getExpenses() + b.getExpenses());
		}
		currentYearAverageBalance.setExpenses(currentYearTotalBalance.getExpenses() / months);
		currentYearAverageBalance.setIncome(currentYearTotalBalance.getIncome() / months);

		// balance types not summed or averaged, but last one is used
		currentYearTotalBalance.setAssets(currentYearBalance.get(months - 1).getAssets());
		currentYearTotalBalance.setLiabilities(currentYearBalance.get(months - 1).getLiabilities());
		currentYearAverageBalance.setAssets(currentYearTotalBalance.getAssets());
		currentYearAverageBalance.setLiabilities(currentYearTotalBalance.getLiabilities());

	}

	public String renderEmailTemplate(String email) {

		//
		Context thymeleafCtx = new Context();// locale!
		thymeleafCtx.setVariable("USER_NAME", email);

		thymeleafCtx.setVariable("MONTH_NAME", currentMonth.getMonth());
		thymeleafCtx.setVariable("MONTH_INCOME", currentMonth.getIncomeFormatted());
		thymeleafCtx.setVariable("MONTH_EXPENSES", currentMonth.getExpensesFormatted());
		thymeleafCtx.setVariable("MONTH_ASSETS", currentMonth.getAssetsFormatted());
		thymeleafCtx.setVariable("MONTH_LIABILITIES", currentMonth.getLiabilitiesFormatted());

		thymeleafCtx.setVariable("AVERAGE_INCOME", currentYearAverageBalance.getIncomeFormatted());
		thymeleafCtx.setVariable("AVERAGE_EXPENSES", currentYearAverageBalance.getExpensesFormatted());

		thymeleafCtx.setVariable("TOTAL_INCOME", currentYearTotalBalance.getIncomeFormatted());
		thymeleafCtx.setVariable("TOTAL_EXPENSES", currentYearTotalBalance.getExpensesFormatted());

		// settings add ".html" and uses /resources/templates
		String htmlContent = templateEngine.process("email-template-weekly", thymeleafCtx);

		logger.info(String.format("email-template-weekly.html template is populated successfully for user %s.", email));

		return htmlContent;
	}

}
