//import org.junit.*;
//import static org.junit.Assert.*;
//
//public class UserTests {
//
//    UserManager um;
//
//    @Before
//    public void setUp() {
//        um = new UserManager();
//        um.createAdminUser("John", "john123", "hello");
//        um.createTradingUser("Kate", "kate230", "dolphin");
//    }
//
//    @After
//    public void tearDown() {
//        um = null;
//    }
//
//    @Test()
//    public void testThreshold() {
//        um.setThreshold(1);
//        assertEquals(1, um.getThreshold());
//    }
//
//    @Test()
//    public void testLogin() {
//        assertTrue(um.login("kate230", "dolphin", UserTypes.TRADING));
//        assertFalse(um.login("kate230", "dolphin", UserTypes.ADMIN));
//        assertTrue(um.login("john123", "hello", UserTypes.ADMIN));
//        assertFalse(um.login("john123", "dolphin", UserTypes.ADMIN));
//        assertFalse(um.login("lara22", "dolphin", UserTypes.TRADING));
//    }
//
//    @Test()
//    public void testCreateExistingUser() {
//        assertFalse(um.createAdminUser("John", "john123", "whale"));
//        assertFalse(um.createTradingUser("John", "john123", "whale"));
//        assertEquals(1, um.adminUsers.size());
//    }
//
//    @Test()
//    public void testSetPasswordByUsername() {
//        um.setPasswordByUsername("john123", "hi", UserTypes.ADMIN);
//        assertTrue(um.login("john123", "hi", UserTypes.ADMIN));
//    }
//
//    @Test()
//    public void testUpdateCreditByUsername() {
//        um.updateCreditByUsername("kate230", true);
//        assertEquals(1, um.tradingUsers.get("kate230").getCredit());
//    }
//
//}