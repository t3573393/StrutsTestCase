package servletunit.struts;

import org.apache.cactus.server.ServletContextWrapper;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;

/**
 * A wrapper for the ServletContext class.  This is used in
 * CactusStrutsTestCase so that we can retrieve the forward
 * processed by the ActionServlet.  This allows us to to use
 * the ActionServlet as a black box, rather than mimic its
 * behavior as was previously the case.
 */
public class StrutsServletContextWrapper extends ServletContextWrapper {

    private String dispatchedResource;

    public StrutsServletContextWrapper(ServletContext context) {
	super(context);
    }

    public RequestDispatcher getRequestDispatcher(String path) {
	dispatchedResource = path;
	return super.getRequestDispatcher(path);
    }

    public String getForward() {
	return dispatchedResource;
    }

}
	
