package pf.unit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestConfigTest {

    @Test
    public void testGetRootPath() throws Exception {
        String rootPath = TestConfig.getRootPath();
        assertEquals("/Users/ahmedhammad/Projects/FinanceWeb2", rootPath);
    }
}
