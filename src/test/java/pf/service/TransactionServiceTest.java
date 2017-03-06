package pf.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pf.transaction.TransactionService;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest

public class TransactionServiceTest {

    @Autowired
    TransactionService trasnService;



    @Test
    public void getYearList() throws Exception {

        List<String> years = trasnService.getYearList("Not Exist ID");
        assertEquals(0, years.size());


    }


}
