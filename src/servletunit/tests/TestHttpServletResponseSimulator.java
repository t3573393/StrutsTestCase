package servletunit.tests;

import junit.framework.TestCase;
import junit.framework.*;
import servletunit.HttpServletResponseSimulator;

/**
 * A Junit based test of the HttpServletResponseSimulator class
 * @author Sean Pritchard
 * @version 1.0
 */

public class TestHttpServletResponseSimulator extends TestCase {

    public TestHttpServletResponseSimulator(String testCase) {
        super(testCase);
    }

    /**
     * Ensures the value returned by encodeURL contains the
     * original url.
     */
    public void testEncodeURL(){
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        String url = "http://sourceforge.net";
        assertTrue(response.encodeURL(url).indexOf(url)!=-1);
    }

    /**
     * Ensures the value returned by encodeUrl contains the
     * original url.
     */
    public void testEncodeUrl(){
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        String url = "http://sourceforge.net";
        assertTrue(response.encodeUrl(url).indexOf(url)!=-1);
    }

    /**
     * Ensures the value returned by encodeRedirectURL contains the
     * original url.
     */
    public void testEncodeRedirectURL(){
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        String url = "http://sourceforge.net";
        assertTrue(response.encodeRedirectURL(url).indexOf(url)!=-1);
    }

    /**
     * Ensures the value returned by encodeRedirectUrl contains the
     * original url.
     */
    public void testEncodeRedirectUrl(){
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        String url = "http://sourceforge.net";
        assertTrue(response.encodeRedirectUrl(url).indexOf(url)!=-1);
    }

    public void testSetHeader() {
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        response.setHeader("TestName", "testValue");
        assertTrue(response.containsHeader("TestName"));
    }

    public void testSetIntHeader() {
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        response.setIntHeader("TestName", 5);
        assertTrue(response.containsHeader("TestName"));
    }


    public static Test suite() {
        return new TestSuite(TestHttpServletResponseSimulator.class);
    }

    public void testSendRedirect() throws Exception{
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        response.sendRedirect("http://sourceforge.net");
        response.containsHeader("Location");
    }

}

