//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2001 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//
//  You may view the full text here: http://www.gnu.org/copyleft/lesser.txt
//
//  If you have any questions or suggestions, you may contact me at
//  this address: Deryl Seale - deryl@acm.org

package servletunit.struts;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.cactus.ServletTestCase;
import junit.framework.AssertionFailedError;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;

/**
 * CactusStrutsTestCase is an extension of the Cactus ServletTestCase 
 * base class that provides additional methods to aid in testing 
 * Struts Action objects.  It uses an in-container approach to run 
 * the servlet container, and tests the execution of Action objects as they
 * are actually run through the Struts ActionServlet.  CactusStrutsTestCase
 * provides methods that set up the request path, request parameters
 * for ActionForm subclasses, as well as methods that can verify
 * that the correct ActionForward was used and that the proper
 * ActionError messages were supplied.
 * <br>
 * <br>
 * <b>Please note</b> that this class is meant to run in the Cactus
 * framework, and you must configure your test environment
 * accordingly.  Please see <a href="http://jakarta.apache.org/cactus">http://jakarta.apache.org/cactus</a>
 * for more details.
 *
 */
public class CactusStrutsTestCase extends ServletTestCase {

    ActionServlet actionServlet;
    String actionPath;
    ActionForward forward;
    ActionForm actionForm;
    HashMap parameters = new HashMap();
    HttpServletRequestWrapper requestWrapper;
    HttpServletResponseWrapper responseWrapper;
    boolean isInitialized = false;
    boolean failIfFormInvalid = true;

    /**
     * Default constructor.
     */
    public CactusStrutsTestCase(String testName) {
        super(testName);
    }

    /**
     * A check that every method should run to ensure that the
     * base class setUp method has been called.
     */
    protected void init() {
	if (!isInitialized) {
	    throw new AssertionFailedError("You are overriding the setUp() method without calling super.setUp().  You must call the superclass setUp() method in your TestCase subclass to ensure proper initialization.");
	}
    }
    
    /**
     * Sets up the test fixture for this test.  This method creates
     * an instance of the ActionServlet, initializes it to validate
     * forms and turn off debugging, and clears all other parameters.
     */
    public void setUp() throws Exception {
	if (!isInitialized) {
	    try {
		parameters.clear();
		forward = null;
		actionForm = null;
		if (actionServlet == null)
		    actionServlet = new ActionServlet();
		requestWrapper = null;
		responseWrapper = null;
		isInitialized = true;
	    } catch (Exception e) {
		throw new AssertionFailedError("\n" + e.getClass() + " - " + e.getMessage());
	    }
	}
    }

    /**
     * Tears down the test fixture upon completion.  This method calls
     * the destroy method on the ActionServlet method used in this test,
     * and <b>must</b> be called if this method is overridden in a subclass.
     */
    public void tearDown() throws Exception {
	init();
        try {
            actionServlet.destroy();
        } catch (Exception e) {
            throw new AssertionFailedError("\n" + e.getClass() + " - " + e.getMessage());
        }
    }

    /**
     * Returns an HttpServletRequest object that can be used in
     * this test.
     */
    public HttpServletRequest getRequest() {
	init();
        return this.request;
    }

    /**
     * Returns a HttpServletRequestWrapper object that can be used
     * in this test. Note that if {@link #setRequestWrapper} has not been
     * called, this method will return an instance of 
     * javax.servlet.http.HttpServletRequestWrapper.
     */
    public HttpServletRequestWrapper getRequestWrapper() {
	init();
	if (requestWrapper == null)
	    return new HttpServletRequestWrapper(this.request);
	else
	    return requestWrapper;
    }

    /**
     * Set this TestCase to use a given HttpServletRequestWrapper
     * class when calling Action.perform().  Note that if this
     * method is not called, then the normal HttpServletRequest
     * object is used.
     *
     * @param wrapper an HttpServletRequestWrapper object to be
     * used when calling Action.perform().
     */
    public void setRequestWrapper(HttpServletRequestWrapper wrapper) {
	init();
	if (wrapper == null)
	    throw new IllegalArgumentException("wrapper class cannot be null!");
	else {
	    wrapper.setRequest(this.request);
	    this.requestWrapper = wrapper;
	}
    }

    /**
     * Returns an HttpServletResponse object that can be used in
     * this test.
     */
    public HttpServletResponse getResponse() {
	init();
        return this.response;
    }

    /**
     * Returns an HttpServletResponseWrapper object that can be used in
     * this test.  Note that if {@link #setResponseWrapper} has not been
     * called, this method will return an instance of 
     * javax.servlet.http.HttpServletResponseWrapper.
     */
    public HttpServletResponseWrapper getResponseWrapper() {
	init();
	if (responseWrapper == null)
	    return new HttpServletResponseWrapper(this.response);
	else
	    return responseWrapper;
    }

    /**
     * Set this TestCase to use a given HttpServletResponseWrapper
     * class when calling Action.perform().  Note that if this
     * method is not called, then the normal HttpServletResponse
     * object is used.
     *
     * @param wrapper an HttpServletResponseWrapper object to be
     * used when calling Action.perform().
     */
    public void setResponseWrapper(HttpServletResponseWrapper wrapper) {
	init();
	if (wrapper == null)
	    throw new IllegalArgumentException("wrapper class cannot be null!");
	else {
	    wrapper.setResponse(this.response);
	    this.responseWrapper = wrapper;
	}
    }

    /**
     * Returns an HttpSession object that can be used in this
     * test.
     */
    public HttpSession getSession() {
	init();
        return this.session;
    }

    /**
     * Adds an HttpServletRequest parameter to be used in setting up the
     * ActionForm instance to be used in this test.  Each parameter added
     * should correspond to an attribute in the ActionForm instance used
     * by the Action instance being tested.
     */
    public void addRequestParameter(String parameterName, String parameterValue)
    {
	init();
        parameters.put(parameterName,parameterValue);
    }

    /**
     * Adds an HttpServletRequest parameter that is an array of String values
     * to be used in setting up the ActionForm instance to be used in this test.
     * Each parameter added should correspond to an attribute in the ActionForm 
     * instance used by the Action instance being tested.
     */
    public void addRequestParameter(String parameterName, String[] parameterValues)
    {
	init();
        parameters.put(parameterName,parameterValues);
    }

    /**
     * Sets the request path instructing the ActionServlet to used a
     * particual ActionMapping.
     *
     * @param pathInfo the request path to be processed.  This should
     * correspond to a particular action mapping, as would normally
     * appear in an HTML or JSP source file.
     */
    public void setRequestPathInfo(String pathInfo) {
	init();
        this.actionPath = Common.stripActionPath(pathInfo);
    }

    /**
     * Sets the location of the Struts configuration file.  This method
     * should only be called if the configuration file is different than
     * the default value of /WEB-INF/struts-config.xml.<br><br>
     * The rules for searching for the configuration files are the same
     * as the rules associated with calling Class.getResourceAsStream().
     * Briefly, this method delegates the call to its class loader, after
     * making these changes to the resource name: if the resource name starts
     * with "/", it is unchanged; otherwise, the package name is prepended
     * to the resource name after converting "." to "/". <br><br>
     * To be on the safe side, always make sure the pathname refers to a
     * file in the same directory as your test, or make sure it begins
     * with a "/" character.
     */
    public void setConfigFile(String pathname) {
	init();
        // ugly hack to get this to play ball with Class.getResourceAsStream()
        if (!pathname.startsWith("/")) {
            String prefix = this.getClass().getPackage().getName().replace('.','/');
            pathname = "/" + prefix + "/" + pathname;
        }
        this.config.setInitParameter("config",pathname);
    }

    /**
     * Sets the ActionForm instance to be used with the
     * Action to be tested.  In most cases, you do <b>not</b>
     * need to use this method, as CactusStrutsTestCase sets
     * up the ActionForm for you.  If, however, you need to
     * use an ActionForm instance from a previous test (eg:
     * to test nested properties) then you can use this method
     * to set the ActionForm instance.
     */
    public void setActionForm(ActionForm form) {
	init();
        this.actionForm = form;
    }

    /**
     * Returns the ActionForm instance used in testing an Action
     * instance.  This method should only be used in conjunction
     * with the setActionForm() method to keep and use ActionForm
     * instances between test execution.
     *
     * @return any ActionForm instance saved in the request or
     * session scope, or null if no such ActionForm instance
     * is saved (or is cleaned up by the Action.perform() method).
     *
     * @see setActionForm(ActionForm form)
     */
    public ActionForm getActionForm() {
	init();
        return (this.actionForm);
    }

    /**
     * Returns the ActionServlet controller used in this
     * test.
     *
     */
    public ActionServlet getActionServlet() {
	init();
        try {
            this.actionServlet.init(config);
            return this.actionServlet;
        } catch (ServletException e) {
	    throw new AssertionFailedError("Error while initializing ActionServlet: " + e.getMessage());
        }
    }

    /**
     * Sets the ActionServlet to be used in this test execution.  This
     * method should only be used if you plan to use a customized
     * version different from that provided in the Struts distribution.
     */
    public void setActionServlet(ActionServlet servlet) {
	init();
        if (servlet == null)
            throw new AssertionFailedError("Cannot set ActionServlet to null");
        this.actionServlet = servlet;
    }

    /**
     * Sets the behavior of this test when validating ActionForm instances.
     * Set to false if you do not want your test to fail if a form
     * does not pass validation.  By default, this is is set to true.
     */
    public void setFailIfFormInvalid(boolean flag) {
	this.failIfFormInvalid = flag;
    }

    /**
     * Executes the Action instance to be tested.  This method initializes
     * the ActionServlet, sets up and optionally validates the ActionForm
     * bean associated with the Action to be tested, and then calls the
     * Action.perform() method.  Results are stored for further validation.
     *
     * @exception AssertionFailedError if there are any execution
     * errors while calling Action.perform() or ActionForm.validate().
     *
     */
    public void actionPerform() {
	init();
	HttpServletRequest request = this.request;
	HttpServletResponse response = this.response;
	// make sure errors are cleared from last test.
	request.removeAttribute(Action.ERROR_KEY); 
	
	if (this.requestWrapper != null)
	    request = this.requestWrapper;
	if (this.responseWrapper != null)
	    response = this.responseWrapper;

	ActionServlet actionServlet = this.getActionServlet();

        // set up the ActionMapping
        ActionMapping mapping = actionServlet.findMapping(this.actionPath);
        if (mapping == null)
            throw new AssertionFailedError("Undefined mapping '" + this.actionPath + "'");

        // set up the ActionForm
        ActionForm form = null;
        if (mapping.getAttribute() != null) {
            String formType = actionServlet.findFormBean(mapping.getAttribute()).getType();
            try {
                form = getActionForm();
                if (form == null) {
                    form = (ActionForm) Class.forName(formType).newInstance();
                    form.reset(mapping,request);
                }
                BeanUtils.populate(form,parameters);
            } catch (Exception e) {
                throw new AssertionFailedError("Error trying to set up ActionForm: " + e.getMessage());
            }

            // Store the newly created bean in the appropriate scope
	    String scope = mapping.getScope();
	    String attribute = mapping.getAttribute();
            if ("request".equals(scope))
                getRequest().setAttribute(attribute, form);
            else if ("session".equals(scope))
                getSession().setAttribute(attribute, form);


            if (mapping.getValidate()) {
                ActionErrors errors = form.validate(mapping,request);
                if (((errors != null) && (!errors.empty())) && (failIfFormInvalid)) {
                    StringBuffer errorText = new StringBuffer();
                    Iterator iterator = errors.get();
                    while (iterator.hasNext()) {
                        errorText.append(" \"");
                        errorText.append(((ActionError) iterator.next()).getKey());
                        errorText.append("\"");
                    }
                    throw new AssertionFailedError("Error while validating ActionForm: " + errorText.toString());
                }
            }
        }

	// Check to see if this is a simple forward.  If so,
	// try it out to see if it works and return.  Errors
	// should be reported as test failure.
	String forward = mapping.getForward();
        if ( forward != null ) {
	    try {
		actionServlet.getServletContext().getRequestDispatcher(forward).forward(request, response);
		return;
	    } catch (Exception e) {
		throw new AssertionFailedError("Error while validating forward '" + forward + "' : " + e.getClass() + " - " + e.getMessage());
	    }
	}

        // set up Action
        Action action = null;
        try {
            action = (Action) Class.forName(mapping.getType()).newInstance();
            action.setServlet(actionServlet);
        } catch (Exception e) {
            throw new AssertionFailedError("Error trying to set up Action: " + e.getMessage());
        }

        try {
            this.forward = action.perform(mapping,form,request,response);
	    if (mapping.getAttribute() != null) {
		ActionForm retForm = null;
		String scope = mapping.getScope();
		if ("request".equals(scope)) {
		    retForm = (ActionForm)getRequest().getAttribute(mapping.getAttribute());
		} else if ("session".equals(scope)) {
		    retForm = (ActionForm)getSession().getAttribute(mapping.getAttribute());
		}
		setActionForm(retForm);
	    }
        } catch (ServletException e) {
            e.printStackTrace();
            throw new AssertionFailedError("Error running action.perform(): " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionFailedError("Error running action.perform(): " + e.getMessage());
        }
    }

    /**
     * Verifies if the ActionServlet controller used this forward.
     *
     * @param forwardName the logical name of a forward, as defined
     * in the Struts configuration file.  This can either refer to a
     * global forward, or one local to the ActionMapping.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward than <code>forwardName</code> after
     * executing an Action object.
     */
    public void verifyForward(String forwardName) throws AssertionFailedError {
	init();
        Common.verifyForwardPath(actionServlet,actionPath,forwardName,forward.getPath(),false);
    }

    /**
     * Verifies if the ActionServlet controller used this actual path
     * as a forward.
     *
     * @param forwardPath an absolute pathname to which the request
     * is to be forwarded.
     * 
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward path than <code>forwardPath</code> after
     * executing an Action object.
     */
    public void verifyForwardPath(String forwardPath) throws AssertionFailedError {
	init();
	if (!Common.stripJSessionID(forward.getPath()).equals(forwardPath))
	    throw new AssertionFailedError("was expecting '" + forwardPath + "' but received '" + forward.getPath() + "'");
    }

    /**
     * Verifies if the ActionServlet controller forwarded to the defined
     * input path.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward than the defined input path after
     * executing an Action object.
     */
    public void verifyInputForward() {
	init();
        String inputPath = actionServlet.findMapping(actionPath).getInput();
        Common.verifyForwardPath(actionServlet,actionPath,inputPath,forward.getPath(),true);
    }

    /**
     * Verifies if the ActionServlet controller sent these error messages.
     * There must be an exact match between the provided error messages, and
     * those sent by the controller, in both name and number.
     *
     * @param errorNames a String array containing the error message keys
     * to be verified, as defined in the application resource properties
     * file.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent different error messages than those in <code>errorNames</code>
     * after executing an Action object.
     */

    public void verifyActionErrors(String[] errorNames) {
	init();
        Common.verifyActionErrors(request,errorNames);
    }

    /**
     * Verifies that the ActionServlet controller sent no error messages upon
     * executing an Action object.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent any error messages after excecuting and Action object.
     */
    public void verifyNoActionErrors() {
	init();
        Common.verifyNoActionErrors(request);
    }

}

