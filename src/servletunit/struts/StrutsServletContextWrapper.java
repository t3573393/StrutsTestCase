package servletunit.struts;

import org.apache.cactus.server.ServletContextWrapper;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import servletunit.RequestDispatcherSimulator;

public class StrutsServletContextWrapper extends ServletContextWrapper {

    private String dispatchedResource;

    public StrutsServletContextWrapper(ServletContext context) {
	super(context);
    }

    public RequestDispatcher getRequestDispatcher(String path) {
	System.out.println("\n\n\nMY CONTEXT WAS CALLED!!!!!\n\n\n");
	dispatchedResource = path;
	return new RequestDispatcherSimulator(path);
    }

    public String getForward() {
	return dispatchedResource;
    }

}
	
