package servletunit.struts;

import org.apache.cactus.server.HttpServletRequestWrapper;
import org.apache.cactus.ServletURL;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A wrapper for the HttpServletRequest class.  This is used in
 * CactusStrutsTestCase so that we can add our own request parameters
 * outside of the beginXXX and endXXX methods.  This allows us to
 * to use the ActionServlet as a black box, rather than mimic its
 * behavior as was previously the case.
 */
public class StrutsRequestWrapper extends HttpServletRequestWrapper {

    private String pathInfo;
    private Map parameters;
    
    public StrutsRequestWrapper(HttpServletRequestWrapper request) {
	super(request,new ServletURL(request.getServerName(),request.getContextPath(),request.getServletPath(),request.getPathInfo(),request.getQueryString()));
	parameters = new HashMap();
    }
    
    public void setPathInfo(String pathInfo) {
	this.pathInfo = pathInfo;
    }

    public String getPathInfo() {
	if (this.pathInfo == null)
	    return super.getPathInfo();
	else
	    return this.pathInfo;
    }

    public String getParameter(String name) {
	String[] result = getParameterValues(name);
	if ((result != null) && (result.length > 0)) {
	    return result[0];
	} else
	    return null;
    }

    public String[] getParameterValues(String name) {
	Object result = super.getParameterValues(name);
	if ((result == null) && (parameters.containsKey(name))) {
	    result = parameters.get(name);
	    if (!(result instanceof String[])) {
		String[] resultArray = { result.toString() };
		result = resultArray;
	    }
	}
	return (String[]) result;
    }

    public Enumeration getParameterNames() {
	Enumeration superNames = super.getParameterNames();
	List nameList = new ArrayList(parameters.keySet());
	while (superNames.hasMoreElements()) {
	    nameList.add((String) superNames.nextElement());
	}
	return Collections.enumeration(nameList);
    }

    public void addParameter(String name, String value) {
	if (super.getParameter(name) == null)
	    parameters.put(name,value);
    }

    public void addParameter(String name, String[] values) {
	if (super.getParameter(name) == null)
	    parameters.put(name,values);
    }

}
		   
	      
