package servletunit.struts.tests;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import servletunit.struts.MockStrutsTestCase;

public class TestCustomActionMapping extends MockStrutsTestCase {

    static Log logger = LogFactory.getLog(TestCustomActionMapping.class);

    /**
     * Sets up the test fixture for this test.  This method creates
     * an instance of the ActionServlet, initializes it to validate
     * forms and turn off debugging, and creates a mock HttpServletRequest
     * and HttpServletResponse object to use in this test.
     */
    protected void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    public void testUsesCustomActionMappingWhenSet() {
        setRequestPathInfo("test","/testCustomMapping");
        actionPerform();
        verifyForward("success");
    }
}

// $Log$
// Revision 1.1  2004/06/10 17:34:43  deryl
// verified bug 955188
//
