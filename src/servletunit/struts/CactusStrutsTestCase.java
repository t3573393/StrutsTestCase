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
import org.apache.struts.Globals;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    HttpServletRequestWrapper requestWrapper;
    HttpServletResponseWrapper responseWrapper;
    boolean isInitialized = false;
    boolean actionServletIsInitialized = false;
    String moduleName;

    protected static Log logger = LogFactory.getLog(CactusStrutsTestCase.class);

    /**
     * Default constructor.
     */
    public CactusStrutsTestCase() {
        super();
    }

    /**
     * Constructor that takes test name parameter, for backwards compatibility with older versions on JUnit.
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
        if (logger.isDebugEnabled())
            logger.debug("Entering setUp()");
        try {
            if (actionServlet == null)
                actionServlet = new ActionServlet();
            requestWrapper = null;
            responseWrapper = null;
            ServletContext servletContext = new StrutsServletContextWrapper(this.config.getServletContext());

            this.config = new StrutsServletConfigWrapper(this.config);
            ((StrutsServletConfigWrapper) this.config).setServletContext(servletContext);
            this.request = new StrutsRequestWrapper(this.request);
            this.response = new StrutsResponseWrapper(this.response);
            isInitialized = true;
            if (logger.isDebugEnabled())
                logger.debug("Exiting setUp()");
        } catch (Exception e) {
            if (logger.isDebugEnabled())
                logger.debug("Error in setUp()",e);
            throw new AssertionFailedError("Error trying to set up test fixture: " + e.getClass() + " - " + e.getMessage());
        }
    }

    /**
     * Returns an HttpServletRequest object that can be used in
     * this test.
     */
    public HttpServletRequest getRequest() {
        if (logger.isDebugEnabled())
            logger.debug("Entering getRequest()");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting getRequest()");
        return this.request;
    }

    /**
     * Returns a HttpServletRequestWrapper object that can be used
     * in this test. Note that if {@link #setRequestWrapper} has not been
     * called, this method will return an instance of
     * javax.servlet.http.HttpServletRequestWrapper.
     */
    public HttpServletRequestWrapper getRequestWrapper() {
        if (logger.isDebugEnabled())
            logger.debug("Entering getRequestWrapper()");
        init();
        if (requestWrapper == null) {
            if (logger.isDebugEnabled())
                logger.debug("Exiting getRequestWrapper()");
            return new HttpServletRequestWrapper(this.request);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("getRequestWrapper() : wrapper class is '" + requestWrapper.getClass() + "'");
            }
            if (logger.isDebugEnabled())
                logger.debug("Exiting getRequestWrapper()");
            return requestWrapper;
        }
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
        if (logger.isDebugEnabled())
            logger.debug("Entering setRequestWrapper() : wrapper = " + wrapper);
        init();
        if (wrapper == null) {
            throw new IllegalArgumentException("wrapper class cannot be null!");
        } else {
            if (wrapper.getRequest() == null)
                wrapper.setRequest(this.request);
            this.requestWrapper = wrapper;
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting setRequestWrapper()");
    }

    /**
     * Returns an HttpServletResponse object that can be used in
     * this test.
     */
    public HttpServletResponse getResponse() {
        if (logger.isDebugEnabled())
            logger.debug("Entering getResponse()");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting getResponse()");
        return this.response;
    }

    /**
     * Returns an HttpServletResponseWrapper object that can be used in
     * this test.  Note that if {@link #setResponseWrapper} has not been
     * called, this method will return an instance of
     * javax.servlet.http.HttpServletResponseWrapper.
     */
    public HttpServletResponseWrapper getResponseWrapper() {
        if (logger.isDebugEnabled())
            logger.debug("Entering getResponseWrapper()");
        init();
        if (responseWrapper == null) {
            if (logger.isDebugEnabled())
                logger.debug("Exiting getResponseWrapper()");
            return new HttpServletResponseWrapper(this.response);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("getRequestWrapper() : wrapper class is '" + responseWrapper.getClass() + "'");
            }
            if (logger.isDebugEnabled())
                logger.debug("Exiting getResponseWrapper()");
            return responseWrapper;
        }
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
        if (logger.isDebugEnabled())
            logger.debug("Entering setResponseWrapper() : wrapper = " + wrapper.getClass());
        init();
        if (wrapper == null)
            throw new IllegalArgumentException("wrapper class cannot be null!");
        else {
            if (wrapper.getResponse() == null)
                wrapper.setResponse(this.response);
            if (logger.isDebugEnabled())
                logger.debug("Exiting setResponseWrapper()");
            this.responseWrapper = wrapper;
        }
    }

    /**
     * Returns an HttpSession object that can be used in this
     * test.
     */
    public HttpSession getSession() {
        if (logger.isDebugEnabled())
            logger.debug("Entering getSession()");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting getSession()");
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
        if (logger.isDebugEnabled())
            logger.debug("Entering addRequestParameter() : paramaterName = " + parameterName + ", parameterValue = " + parameterValue);
        init();
        ((StrutsRequestWrapper) this.request).addParameter(parameterName,parameterValue);
        if (logger.isDebugEnabled())
            logger.debug("Exiting addRequestParameter()");
    }

    /**
     * Adds an HttpServletRequest parameter that is an array of String values
     * to be used in setting up the ActionForm instance to be used in this test.
     * Each parameter added should correspond to an attribute in the ActionForm
     * instance used by the Action instance being tested.
     */
    public void addRequestParameter(String parameterName, String[] parameterValues)
    {
        if (logger.isDebugEnabled())
            logger.debug("Entering addRequestParameter() : paramaterName = " + parameterName + ", parameterValue = " + parameterValues);
        init();
        ((StrutsRequestWrapper) this.request).addParameter(parameterName,parameterValues);
        if (logger.isDebugEnabled())
            logger.debug("Exiting addRequestParameter()");
    }

    /**
     * Clears all request parameters previously set.  NOTE: This will <strong>note</strong> clear
     * parameters set using Cactus beginXXX methods!
     */
    public void clearRequestParameters() {
        init();
        ((StrutsRequestWrapper) request).clearRequestParameters();
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
        if (logger.isDebugEnabled())
            logger.debug("Entering setRequestPathInfo() : pathInfo = " + pathInfo);
        init();
        this.setRequestPathInfo("",pathInfo);
        if (logger.isDebugEnabled())
            logger.debug("Exiting setRequestPathInfo()");
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
        if (logger.isDebugEnabled())
            logger.debug("Entering setRequestPathInfo() : moduleName = " + moduleName + ", pathInfo = " + pathInfo);
        init();
        ((StrutsRequestWrapper) this.request).setPathInfo(Common.stripActionPath(pathInfo));
        if (moduleName != null) {
            if (!moduleName.equals("")) {
                if (!moduleName.startsWith("/"))
                    moduleName = "/" + moduleName;
                if (!moduleName.endsWith("/"))
                    moduleName = moduleName + "/";
            }
            if (logger.isDebugEnabled()) {
                logger.debug("setRequestPathInfo() : setting request attribute - name = " + Common.INCLUDE_SERVLET_PATH + ", value = " + moduleName);
            }
            this.request.setAttribute(Common.INCLUDE_SERVLET_PATH, moduleName);
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting setRequestPathInfo()");
    }

    /**
     * Sets an initialization parameter on the
     * ActionServlet.  Allows you to simulate an init parameter
     * that would normally have been found in web.xml.
     * @param key the name of the initialization parameter
     * @param value the value of the intialization parameter
     */
    public void setInitParameter(String key, String value){
        if (logger.isDebugEnabled())
            logger.debug("Entering setInitParameter() : key = " + key + ", value = " + value);
        this.config.setInitParameter(key, value);
        this.actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting setInitParameter()");
    }

    /**
     * Sets the location of the Struts configuration file for the default module.
     * This method can take either an absolute path, or a relative path.  If an
     * absolute path is supplied, the configuration file will be loaded from the
     * underlying filesystem; otherwise, the ServletContext loader will be used.
     */
    public void setConfigFile(String pathname) {
        if (logger.isDebugEnabled())
            logger.debug("Entering setConfigFile() : pathname = " + pathname);
        init();
        setConfigFile(null,pathname);
        if (logger.isDebugEnabled())
            logger.debug("Exiting setConfigFile()");
    }

    /**
     * Sets the struts configuration file for a given sub-application.
     * This method can take either an absolute path, or a relative path.  If an
     * absolute path is supplied, the configuration file will be loaded from the
     * underlying filesystem; otherwise, the ServletContext loader will be used.
     *
     * @param moduleName the name of the sub-application, or null if this is the default application
     * @param pathname the location of the configuration file for this sub-application
     */
    public void setConfigFile(String moduleName, String pathname) {
        if (logger.isDebugEnabled())
            logger.debug("Entering setConfigFile() : moduleName = " + moduleName + ", pathname = " + pathname);
        init();
        if (moduleName == null)
            this.config.setInitParameter("config",pathname);
        else
            this.config.setInitParameter("config/" + moduleName,pathname);
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting setConfigFile()");
    }

    /**
     * Returns the ActionServlet controller used in this
     * test.
     *
     */
    public ActionServlet getActionServlet() {
        if (logger.isDebugEnabled())
            logger.debug("Entering getActionServlet()");
        init();
        try {
            if (!actionServletIsInitialized) {
                // the RequestProcessor holds on to the ServletContext, so
                // we need to ensure that it is replaced for each test.
                ServletContext context = config.getServletContext();
                String name = "org.apache.struts.action.REQUEST_PROCESSOR";

                // remove request processor for default module
                Object obj = context.getAttribute(name);
                if (obj != null) {
                    config.getServletContext().setAttribute(name, null);
                }

                // remove request processor for sub-applications, if used.
                // todo: this seems pretty redundant.. may want to make this cleaner.
                String moduleName = (String) request.getAttribute(Common.INCLUDE_SERVLET_PATH);
                if (moduleName.endsWith("/"))
                    moduleName = moduleName.substring(0,moduleName.lastIndexOf("/"));

                obj = context.getAttribute(name + moduleName);
                if (obj != null) {
                    config.getServletContext().setAttribute(name + moduleName, null);
                }

                this.actionServlet.init(config);
                actionServletIsInitialized = true;
            }
            if (logger.isDebugEnabled())
                logger.debug("Exiting getActionServlet()");
            return this.actionServlet;
        } catch (ServletException e) {
            if (logger.isDebugEnabled())
                logger.debug("Error in getActionServlet()",e.getRootCause());
            throw new AssertionFailedError("Error while initializing ActionServlet: " + e.getMessage());
        }
    }

    /**
     * Sets the ActionServlet to be used in this test execution.  This
     * method should only be used if you plan to use a customized
     * version different from that provided in the Struts distribution.
     */
    public void setActionServlet(ActionServlet servlet) {
        if (logger.isDebugEnabled())
            logger.debug("Entering setActionServlet() : servlet = " + servlet);
        init();
        if (servlet == null)
            throw new AssertionFailedError("Cannot set ActionServlet to null");
        this.actionServlet = servlet;
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting setActionServlet()");
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
        if (logger.isDebugEnabled())
            logger.debug("Entering actionPerform()");
        init();
        try {
            HttpServletRequest request = this.request;
            HttpServletResponse response = this.response;
            // make sure errors are cleared from last test.
            request.removeAttribute(Action.ERROR_KEY);
            request.removeAttribute(Globals.MESSAGE_KEY);

            if (this.requestWrapper != null)
                request = this.requestWrapper;
            if (this.responseWrapper != null)
                response = this.responseWrapper;

            ActionServlet actionServlet = this.getActionServlet();
            actionServlet.doPost(request,response);
            if (logger.isDebugEnabled())
                logger.debug("Exiting actionPerform()");
        } catch (ServletException se) {
            if (logger.isDebugEnabled())
                logger.debug("Error in actionPerform()",se.getRootCause());
            fail("Error running action.perform(): " + se.getRootCause().getClass() + " - " + se.getRootCause().getMessage());
        } catch (IOException e) {
            if (logger.isDebugEnabled())
                logger.debug("Error in actionPerform()",e);
            fail("Error running action.perform(): " + e.getClass() + " - " + e.getMessage());
        }
    }

    /**
     * Returns the forward sent to RequestDispatcher.
     */
    private String getActualForward() {
        if (logger.isDebugEnabled())
            logger.debug("Entering getActualForward()");
        if (response.containsHeader("Location")) {
            return Common.stripJSessionID(((StrutsResponseWrapper) response).getRedirectLocation());
        } else {
            String forward = ((StrutsServletContextWrapper) this.actionServlet.getServletContext()).getForward();
            if (logger.isDebugEnabled()) {
                logger.debug("getActualForward() : actual forward = " + forward);
            }
            if (forward == null) {
                if (logger.isDebugEnabled())
                    logger.debug("Exiting getActualForward()");
                return null;
            } else {
                String strippedForward = request.getContextPath() + Common.stripJSessionID(forward);
                if (logger.isDebugEnabled()) {
                    logger.debug("getActualForward() : stripped forward and added context path = " + strippedForward);
                }
                if (logger.isDebugEnabled())
                    logger.debug("Exiting getActualForward()");
                return strippedForward;
            }
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
        if (logger.isDebugEnabled())
            logger.debug("Entering verifyForward() : forwardName = " + forwardName);
        init();
        Common.verifyForwardPath(actionServlet,request.getPathInfo(),forwardName,getActualForward(),false,request,config.getServletContext(),config);
        if (logger.isDebugEnabled())
            logger.debug("Exiting verifyForward()");
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
        if (logger.isDebugEnabled())
            logger.debug("Entering verifyForwardPath() : forwardPath = " + forwardPath);
        init();
        String actualForward = getActualForward();

        if ((actualForward == null) && (forwardPath == null)) {
            // actions can return null forwards.
            return;
        }

        forwardPath = request.getContextPath() + forwardPath;

        if (actualForward == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("verifyForwardPath() : actualForward is null - this usually means it is not mapped properly.");
            }
            throw new AssertionFailedError("Was expecting '" + forwardPath + "' but it appears the Action has tried to return an ActionForward that is not mapped correctly.");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("verifyForwardPath() : expected forward = '" + forwardPath + "' - actual forward = '" + actualForward + "'");
        }
        if (!(actualForward.equals(forwardPath)))
            throw new AssertionFailedError("was expecting '" + forwardPath + "' but received '" + actualForward + "'");
        if (logger.isDebugEnabled())
            logger.debug("Exiting verifyForwardPath()");
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
        if (logger.isDebugEnabled())
            logger.debug("Entering verifyInputForward()");
        init();
        Common.verifyForwardPath(actionServlet,request.getPathInfo(),null,getActualForward(),true,request,config.getServletContext(),config);
        if (logger.isDebugEnabled())
            logger.debug("Exiting verifyInputForward()");
    }

    /**
     * Verifies that the ActionServlet controller used this forward and Tiles definition.
     *
     * @param forwardName the logical name of a forward, as defined
     * in the Struts configuration file.  This can either refer to a
     * global forward, or one local to the ActionMapping.
     *
     * @param definitionName the name of a Tiles definition, as defined
     * in the Tiles configuration file.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward or tiles definition than those given after
     * executing an Action object.
     */
    public void verifyTilesForward(String forwardName, String definitionName) {
        init();
        Common.verifyTilesForward(actionServlet,request.getPathInfo(),forwardName,definitionName,false,request,config.getServletContext(),config);
    }

    /**
     * Verifies that the ActionServlet controller forwarded to the defined
     * input Tiles definition.
     *
     * @param definitionName the name of a Tiles definition, as defined
     * in the Tiles configuration file.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward than the defined input path after
     * executing an Action object.
     */
    public void verifyInputTilesForward(String definitionName) {
        init();
        Common.verifyTilesForward(actionServlet,request.getPathInfo(),null,definitionName,true,request,config.getServletContext(),config);
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
        if (logger.isDebugEnabled())
            logger.debug("Entering verifyActionErrors() : errorNames = " + errorNames);
        init();
        Common.verifyActionMessages(request,errorNames,Action.ERROR_KEY,"error");
        if (logger.isDebugEnabled())
            logger.debug("Exiting verifyActionErrors()");
    }

    /**
     * Verifies that the ActionServlet controller sent no error messages upon
     * executing an Action object.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent any error messages after excecuting and Action object.
     */
    public void verifyNoActionErrors() {
        if (logger.isDebugEnabled())
            logger.debug("Entering verifyNoActionErrors()");
        init();
        Common.verifyNoActionMessages(request,Action.ERROR_KEY,"error");
        if (logger.isDebugEnabled())
            logger.debug("Exiting verifyNoActionErrors()");
    }

    /**
     * Verifies if the ActionServlet controller sent these action messages.
     * There must be an exact match between the provided action messages, and
     * those sent by the controller, in both name and number.
     *
     * @param messageNames a String array containing the action message keys
     * to be verified, as defined in the application resource properties
     * file.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent different action messages than those in <code>messageNames</code>
     * after executing an Action object.
     */
    public void verifyActionMessages(String[] messageNames) {
        if (logger.isDebugEnabled())
            logger.debug("Entering verifyActionMessages() : messageNames = " + messageNames);
        init();
        Common.verifyActionMessages(request,messageNames,Globals.MESSAGE_KEY,"action");
        if (logger.isDebugEnabled())
            logger.debug("Exiting verifyActionMessages()");
    }

    /**
     * Verifies that the ActionServlet controller sent no action messages upon
     * executing an Action object.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent any action messages after excecuting and Action object.
     */
    public void verifyNoActionMessages() {
        if (logger.isDebugEnabled())
            logger.debug("Entering verifyNoActionMessages()");
        init();
        Common.verifyNoActionMessages(request,Globals.MESSAGE_KEY,"action");
        if (logger.isDebugEnabled())
            logger.debug("Exiting verifyNoActionMessages()");
    }

    /**
     * Returns the ActionForm instance stored in either the request or session.  Note
     * that no form will be returned if the Action being tested cleans up the form
     * instance.
     *
     * @ return the ActionForm instance used in this test, or null if it does not exist.
     */
    public ActionForm getActionForm() {
        if (logger.isDebugEnabled())
            logger.debug("Entering getActionForm()");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting getActionForm()");
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
        if (logger.isDebugEnabled())
            logger.debug("Entering setActionForm() : form = " + form);
        init();
        // make sure ActionServlet is initialized.
        Common.setActionForm(form,request,request.getPathInfo(),config.getServletContext(),this.getActionServlet());
        if (logger.isDebugEnabled())
            logger.debug("Exiting setActionForm()");
    }

}

