package servletunit.tests;

import junit.framework.TestCase;
import servletunit.HttpServletRequestSimulator;

public class TestSession extends TestCase {

    HttpServletRequestSimulator request;

    public TestSession(String testName) {
        super(testName);
    }

    public void setUp() {
        this.request = new HttpServletRequestSimulator(null);
    }

    public void testGetSession() {
        assertNotNull(request.getSession());
    }

    public void testGetSessionTrue() {
        assertNotNull(request.getSession(true));
    }

    public void testGetSessionFalse() {
        assertNull(request.getSession(false));
    }

    public void testGetSessionFalseSessionExists() {
        request.getSession();
        assertNotNull(request.getSession(false));
    }

    public void testGetSessionInvalid() {
        request.getSession().invalidate();
        assertNotNull(request.getSession(true));
    }

    public void testGetSessionInvalidFalse() {
        request.getSession().invalidate();
        assertNull(request.getSession(false));
    }


}
