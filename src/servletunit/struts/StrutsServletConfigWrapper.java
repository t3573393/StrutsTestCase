package servletunit.struts;

import org.apache.cactus.server.ServletConfigWrapper;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * A wrapper for the ServletConfig class.  This is used in
 * CactusStrutsTestCase so that we can use out own ServletContext
 * wrapper class.  This allows us to to use the ActionServlet
 * as a black box, rather than mimic its behavior as was previously
 * the case.
 */
public class StrutsServletConfigWrapper extends ServletConfigWrapper {

    private ServletContext context;
    
    public StrutsServletConfigWrapper(ServletConfig config) {
	super(config);
    }

    public ServletContext getServletContext() {
	return this.context;
    }

    public void setServletContext(ServletContext context) {
	this.context = context;
    }

}
