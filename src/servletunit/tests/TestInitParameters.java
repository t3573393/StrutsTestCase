package servletunit.tests;

import junit.framework.TestCase;
import servletunit.*;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public class TestInitParameters extends TestCase {

    ServletConfigSimulator config;

    public TestInitParameters(String testName) {
        super(testName);
    }

    public void setUp() {
        config = new ServletConfigSimulator();
    }

    public void testSetInitParameter() {
        config.setInitParameter("test","testValue");
        assertEquals("testValue",config.getInitParameter("test"));
    }

    public void testNoParameter() {
        assertNull(config.getInitParameter("badValue"));
    }

    public void testParameterFromContext() {
        ServletContext context = config.getServletContext();
        config.setInitParameter("test","testValue");
        assertEquals("testValue",context.getInitParameter("test"));
        config.setInitParameter("test","newValue");
        assertEquals("newValue",context.getInitParameter("test"));
    }

    public void testGetInitParameterNames() {
        config.setInitParameter("test","testValue");
        config.setInitParameter("another","anotherValue");
        assertEquals("testValue",config.getInitParameter("test"));
        assertEquals("anotherValue",config.getInitParameter("another"));
        Enumeration names = config.getInitParameterNames();
        boolean fail = true;
        while (names.hasMoreElements()) {
            fail = true;
            String name = (String) names.nextElement();
            if ((name.equals("test")) || (name.equals("another")))
                fail = false;
        }
        if (fail)
            fail();
    }

    public void testGetInitParameterNamesFromContext() {
        ServletContext context = config.getServletContext();
        config.setInitParameter("test","testValue");
        config.setInitParameter("another","anotherValue");
        assertEquals("testValue",context.getInitParameter("test"));
        assertEquals("anotherValue",context.getInitParameter("another"));
        Enumeration names = context.getInitParameterNames();
        boolean fail = true;
        while (names.hasMoreElements()) {
            fail = true;
            String name = (String) names.nextElement();
            if ((name.equals("test")) || (name.equals("another")))
                fail = false;
        }
        if (fail)
            fail();
    }


}


