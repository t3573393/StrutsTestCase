package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;
import org.apache.cactus.WebResponse;

public class TestProcessResults extends CactusStrutsTestCase {

    public void testSuccessfulLogin() {
        processRequest(true);
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
	    verifyForwardPath("/main/success.jsp");
        assertEquals("deryl", getSession().getAttribute("authentication"));
        verifyNoActionErrors();
    }

    public void endSuccessfulLogin(WebResponse response) {
        String html = "<h2>You have successfully logged in!</h2>\n\n<a href=\"/test/login/login.jsp\">Go back</a>";
        assertEquals("unexpected response code",200,response.getStatusCode());
        assertEquals("unexpected text",html,response.getText());
    }
}
