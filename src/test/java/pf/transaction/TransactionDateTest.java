package pf.transaction;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionDateTest {

	@Test
	public void testDateTime() {
		String date = "2017-12-01";
//		String time = new SimpleDateFormat (" hh:mm:ss").format(new Date());
		date += new SimpleDateFormat(" HH:mm:ss").format(new Date());
//
//		date += time;
		System.out.print(date);
	}

}
