package pf.unit;//package pf.test;
//
//import jdepend.framework.JDepend;
//import jdepend.framework.JavaPackage;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//import static org.junit.Assert.assertFalse;
//
//public class ArchitecturalRulesTest {
//
//	Collection packages;
//
//   @Before
//    public  void setUp() throws Exception {
//		JDepend jdepend;
//		jdepend = new JDepend();
//		jdepend.addDirectory(pf.unit.TestConfig.getRootPath() + "/binary");///war/WEB-INF/classes
//		packages = jdepend.analyze();
//    }
//
//	@Test
//	public void testLayerWeb() throws Exception {
//
//		//web should depend only on mgmt
//		//assertFalse( isViolationExist("pf.web", "pf.email"));
//		assertFalse( isViolationExist("pf.web", "pf.jdbc"));
//		assertFalse( isViolationExist("pf.web", "pf.jpa"));
//
//	}
//
//	@Test
//	public void testLayerEmail() throws Exception {
//		//email should not depend on anything
//		assertFalse( isViolationExist("pf.email", "pf.web"));
//		assertFalse( isViolationExist("pf.email", "pf.mgmt"));
//		assertFalse( isViolationExist("pf.email", "pf.email"));
//	}
//
//	@Test
//	public void testLayerBuss() throws Exception {
//		//email should not depend on anything
//		String violation  = "pf.db";
//		assertFalse( isViolationExist("pf.buss.user", violation));
//		assertFalse( isViolationExist("pf.buss.account", violation));
//		assertFalse( isViolationExist("pf.buss.transaction", violation));
//		assertFalse( isViolationExist("pf.buss.chart", violation));
//		//independent of web
//		violation  = "pf.web";
//		assertFalse( isViolationExist("pf.buss.user", violation));
//		assertFalse( isViolationExist("pf.buss.account", violation));
//		assertFalse( isViolationExist("pf.buss.transaction", violation));
//		assertFalse( isViolationExist("pf.buss.chart", violation));
////		//independent of email
////		violation  = "pf.email";
////		assertFalse( isViolationExist("pf.buss.user", violation));
////		assertFalse( isViolationExist("pf.buss.account", violation));
////		assertFalse( isViolationExist("pf.buss.transaction", violation));
////		assertFalse( isViolationExist("pf.buss.chart", violation));
////		//independent of email
////		violation  = "pf.email";
////		assertFalse( isViolationExist("pf.buss.user", violation));
////		assertFalse( isViolationExist("pf.buss.account", violation));
////		assertFalse( isViolationExist("pf.buss.transaction", violation));
////		assertFalse( isViolationExist("pf.buss.chart", violation));
//	}
//
//
//	@Test
//	public void testLayerDb() throws Exception {
//		//db should not depend on anything else
//		assertFalse( isViolationExist("pf.db.jdbc", "pf.web"));
//		assertFalse( isViolationExist("pf.db.jdbc", "pf.mgmt"));
//		assertFalse( isViolationExist("pf.db.jdbc", "pf.email"));
//	}
//
//	@Test
//	public void testLayerMgmt() throws Exception {
//		//mgmt should not depend on web
//		assertFalse( isViolationExist("pf.buss.account", "pf.web"));
//		assertFalse( isViolationExist("pf.buss.user", "pf.web"));
//		assertFalse( isViolationExist("pf.buss.transaction", "pf.web"));
//
//	}
//
//	private boolean isViolationExist(String layer, String violation) throws Exception {
//		JavaPackage jPackage = getPackage(layer, packages);
//		if(jPackage == null) {
//			throw new Exception (layer + " does not exist!");
//		}
//		Collection efferents = jPackage.getEfferents();
//		Iterator efferentItor = efferents.iterator();
//		while (efferentItor.hasNext()) {
//			JavaPackage efferentPackage = (JavaPackage) efferentItor.next();
//			String efferentPackageName = efferentPackage.getName();
//			if(violation.equals(efferentPackageName)) {
//				throw new Exception (layer + " should not depend upon " + efferentPackageName);
//				//break;
//			}
//		}
//		return false;
//	}
//
//	private JavaPackage getPackage(String name, Collection packages) {
//		Iterator itor = packages.iterator();
//		while (itor.hasNext()) {
//			JavaPackage jPackage = (JavaPackage) itor.next();
//			String analyzedPackageName = jPackage.getName();
//			if(analyzedPackageName.equals(name)) {
//				return jPackage;
//			}
//		}
//		return null;
//	}
//
//
//}
