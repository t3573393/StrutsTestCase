package servletunit.struts;

import org.apache.cactus.server.ServletConfigWrapper;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

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
