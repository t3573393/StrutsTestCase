//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

package servletunit.struts;

import junit.framework.AssertionFailedError;
import org.apache.cactus.ServletTestCase;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

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
    boolean isInitialized = false;
    boolean actionServletIsInitialized = false;

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
    private void init() {
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
        try {
            if (actionServlet == null)
                actionServlet = new ActionServlet();
            ServletContext servletContext = new StrutsServletContextWrapper(this.config.getServletContext());

            this.config = new StrutsServletConfigWrapper(this.config);
            ((StrutsServletConfigWrapper) this.config).setServletContext(servletContext);
            this.request = new StrutsRequestWrapper(this.request);
            this.response = new StrutsResponseWrapper(this.response);
            isInitialized = true;
        } catch (Exception e) {
            throw new AssertionFailedError("Error trying to set up test fixture: " + e.getClass() + " - " + e.getMessage());
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
     * Returns an HttpServletResponse object that can be used in
     * this test.
     */
    public HttpServletResponse getResponse() {
        init();
        return this.response;
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
        ((StrutsRequestWrapper) this.request).addParameter(parameterName,parameterValue);
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
        ((StrutsRequestWrapper) this.request).addParameter(parameterName,parameterValues);
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
        this.setRequestPathInfo("",pathInfo);
    }


    /**
     * Sets the request path instructing the ActionServlet to used a
     * particual ActionMapping.  Also sets the ServletPath property
     * on the request.
     *
     * @param moduleName the name of the Struts sub-application with
     * which this request is associated, or null if it is the default
     * application.
     * @param pathInfo the request path to be processed.  This should
     * correspond to a particular action mapping, as would normally
     * appear in an HTML or JSP source file.  If this request is part
     * of a sub-application, the module name should not appear in the
     * request path.
     */
    public void setRequestPathInfo(String moduleName, String pathInfo) {
        init();
        ((StrutsRequestWrapper) this.request).setPathInfo(Common.stripActionPath(pathInfo));
	if (moduleName != null) {
	    if (!moduleName.equals("")) {
		if (!moduleName.startsWith("/"))
		    moduleName = "/" + moduleName;
		if (!moduleName.endsWith("/"))
		    moduleName = moduleName + "/";
	    }
            this.request.setAttribute(Common.INCLUDE_SERVLET_PATH, moduleName);
        }
    }

    /**
     * Sets an initialization parameter on the
     * ActionServlet.  Allows you to simulate an init parameter
     * that would normally have been found in web.xml.
     * @param key the name of the initialization parameter
     * @param value the value of the intialization parameter
     */
    public void setInitParameter(String key, String value){
        this.config.setInitParameter(key, value);
        this.actionServletIsInitialized = false;
    }

    /**
     * Sets the location of the Struts configuration file for the default module.
     * This method should only be called if the configuration file is different than
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
        setConfigFile(null,pathname);
    }

    /**
     * Sets the struts configuration file for a given sub-application.
     *
     * @param moduleName the name of the sub-application, or null if this is the default application
     * @param pathname the location of the configuration file for this sub-application
     */
    public void setConfigFile(String moduleName, String pathname) {
        init();
        // ugly hack to get this to play ball with Class.getResourceAsStream()
        if (!pathname.startsWith("/")) {
            String prefix = this.getClass().getPackage().getName().replace('.','/');
            pathname = "/" + prefix + "/" + pathname;
        }
        if (moduleName == null)
            this.config.setInitParameter("config",pathname);
        else
            this.config.setInitParameter("config/" + moduleName,pathname);
        actionServletIsInitialized = false;
    }

    /**
     * Returns the ActionServlet controller used in this
     * test.
     *
     */
    public ActionServlet getActionServlet() {
        init();
        try {
            if (!actionServletIsInitialized) {
                // the RequestProcessor holds on to the ServletContext, so
                // we need to ensure that it is replaced for each test.
                ServletContext context = config.getServletContext();
                String name = "org.apache.struts.action.REQUEST_PROCESSOR";

                Object obj = context.getAttribute(name);
                if (obj != null) {
                    config.getServletContext().setAttribute(name, null);
                }
                this.actionServlet.init(config);
                actionServletIsInitialized = true;
            }
            return this.actionServlet;
        } catch (ServletException e) {
            throw new AssertionFailedError(
                    "Error while initializing ActionServlet: " + e.getMessage());
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
        actionServletIsInitialized = false;
    }

    /**
     * Sets the behavior of this test when validating ActionForm instances.
     * Set to false if you do not want your test to fail if a form
     * does not pass validation.  By default, this is is set to true.
     *
     * @deprecated This method no longer affects the flow of control.
     */
    public void setFailIfInvalid(boolean flag) {
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
        try {
            HttpServletRequest request = this.request;
            HttpServletResponse response = this.response;
            // make sure errors are cleared from last test.
            request.removeAttribute(Action.ERROR_KEY);

            ActionServlet actionServlet = this.getActionServlet();
            actionServlet.doPost(request,response);

        } catch (ServletException se) {
            se.getRootCause().printStackTrace();
            fail("Error running action.perform(): " + se.getRootCause().getClass() + " - " + se.getRootCause().getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error running action.perform(): " + e.getClass() + " - " + e.getMessage());
        }
    }

    /**
     * Returns the forward sent to RequestDispatcher.
     */
    private String getActualForward() {

        if (response.containsHeader("Location")) {
            return Common.stripJSessionID(((StrutsResponseWrapper) response).getRedirectLocation());
        } else {
            String forward = ((StrutsServletContextWrapper) this.actionServlet.getServletContext()).getForward();
            if (forward == null)
                return null;
            else
                return request.getContextPath() + Common.stripJSessionID(forward);
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
        Common.verifyForwardPath(actionServlet,request.getPathInfo(),forwardName,getActualForward(),false,request,config.getServletContext(),config);
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
        forwardPath = request.getContextPath() + forwardPath;
        String actualForward = getActualForward();
        if (actualForward == null)
            throw new AssertionFailedError("Was expecting '" + forwardPath + "' but it appears the Action has tried to return an ActionForward that is not mapped correctly.");
        if (!(actualForward.equals(forwardPath)))
            throw new AssertionFailedError("was expecting '" + forwardPath + "' but received '" + getActualForward() + "'");
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
        Common.verifyForwardPath(actionServlet,request.getPathInfo(),null,getActualForward(),true,request,config.getServletContext(),config);
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

    /**
     * Returns the ActionForm instance stored in either the request or session.  Note
     * that no form will be returned if the Action being tested cleans up the form
     * instance.
     *
     * @ return the ActionForm instance used in this test, or null if it does not exist.
     */
    public ActionForm getActionForm() {
        init();
        return Common.getActionForm(actionServlet,request.getPathInfo(),request,config.getServletContext());
    }

    /**
     * Sets an ActionForm instance to be used in this test.  The given ActionForm instance
     * will be stored in the scope specified in the Struts configuration file (ie: request
     * or session).  Note that while this ActionForm instance is passed to the test, Struts
     * will still control how it is used.  In particular, it will call the ActionForm.reset()
     * method, so if you override this method in your ActionForm subclass, you could potentially
     * reset attributes in the form passed through this method.
     *
     * @param form the ActionForm instance to be used in this test.
     */
    public void setActionForm(ActionForm form) {
        init();
        // make sure ActionServlet is initialized.
        Common.setActionForm(form,request,request.getPathInfo(),config.getServletContext(),this.getActionServlet());
    }

}

