package pf.unit;

public final class TestConfig {
	
	private TestConfig() {
	}
	
	//FIXME: Remove hard-coded paths, it will break for all other developers and in other contexts
	static public String getRootPath() {
//		URL url = new String().getClass().getResource("/");
//		String path = new String().getClass().getResource("/").getPath();
//		return path.substring(0, path.length()-1);
		return "/Users/ahmedhammad/Projects/FinanceWeb2";
	}
	
	static public String getTestRootPath() {
		return getRootPath() + "/test";
	}


//	@Test
//	public void testGetPath() {
//		URL url = new String().getClass().getResource("/");
//		String str = new String().getClass().getResource("/").getPath();
//		System.out.println(url);
//		System.out.println(str);
//
//
//
//	}
	
}
