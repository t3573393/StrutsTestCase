package servletunit.tests;

import junit.framework.TestCase;
import servletunit.HttpServletRequestSimulator;
import servletunit.ServletContextSimulator;

/**
 * A Junit based test of the HttpServletResponseSimulator class
 * @author Sean Pritchard
 * @version 1.0
 */

public class TestHttpServletRequestSimulator extends TestCase {

    HttpServletRequestSimulator request;

    public TestHttpServletRequestSimulator(String testCase) {
        super(testCase);
    }

    public void setUp() {
	request = new HttpServletRequestSimulator(new ServletContextSimulator());
    }

    public void testAddParameterArray() {
	String[] values = { "value1", "value2" };
	request.addParameter("name1",values);
	String[] result = request.getParameterValues("name1");
	if (result.length != 2) 
	    fail();
	if (!((result[0].equals("value1")) && (result[1].equals("value2"))))
	    fail();
    }

    public void testGetParameterValuesSingle() {
	request.addParameter("name1","value1");
	String[] result = request.getParameterValues("name1");
	if (result.length != 1) 
	    fail();
	if (!(result[0].equals("value1")))
	    fail();
    }
    
    public void testGetParameterWithArray() {
	String[] values = { "value1", "value2" };
	request.addParameter("name1",values);
	String result = request.getParameter("name1");
	if (!(result.equals("value1")))
	    fail();
    }
    

}

