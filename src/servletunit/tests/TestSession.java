package servletunit.tests;

import junit.framework.TestCase;
import servletunit.*;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public class TestSession extends TestCase {

    HttpServletRequestSimulator request;
    
    public TestSession(String testName) {
	super(testName);
    }

    public void setUp() {
	this.request = new HttpServletRequestSimulator();
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
}
