package servletunit.tests;

import junit.framework.TestCase;
import servletunit.*;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import javax.servlet.http.Cookie;

public class TestCookies extends TestCase {

    HttpServletRequestSimulator request;

    public TestCookies(String testName) {
        super(testName);
    }

    public void setUp() {
    this.request = new HttpServletRequestSimulator();
    }

    public void testNoCookies() {
    assertNull(request.getCookies());
    }

    public void testAddCookie() {
    request.addCookie(new Cookie("test","testValue"));
    Cookie[] cookies = request.getCookies();
    boolean assertion = false;
    for (int i = 0; i < cookies.length; i++) {
        if ((cookies[i].getName().equals("test")) && (cookies[i].getValue().equals("testValue")))
        assertion = true;
    }
    assertTrue(assertion);
    }

    public void testSetCookies() {
    Cookie[] cookies = new Cookie[2];
    cookies[0] = new Cookie("test","testValue");
    cookies[1] = new Cookie("test2","testValue2");
    boolean assert1 = false;
    boolean assert2 = false;
    request.setCookies(cookies);
    Cookie[] resultCookies = request.getCookies();
    for (int i = 0; i < resultCookies.length; i++) {
        if ((resultCookies[i].getName().equals("test")) && (resultCookies[i].getValue().equals("testValue")))
        assert1 = true;
        if ((resultCookies[i].getName().equals("test2")) && (resultCookies[i].getValue().equals("testValue2")))
        assert2 = true;

    }
    assertTrue(assert1 && assert2);
    }

    public void testCheckForWrongCookie() {
    request.addCookie(new Cookie("test","testValue"));
    Cookie[] cookies = request.getCookies();
    boolean assertion = true;
    for (int i = 0; i < cookies.length; i++) {
        if ((cookies[i].getName().equals("badValue")) && (cookies[i].getValue().equals("dummyValue")))
        assertion = false;
    }
    assertTrue(assertion);
    }




}
