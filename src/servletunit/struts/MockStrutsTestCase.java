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
import junit.framework.TestCase;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.Action;
import org.apache.struts.Globals;
import servletunit.HttpServletRequestSimulator;
import servletunit.HttpServletResponseSimulator;
import servletunit.ServletConfigSimulator;
import servletunit.ServletContextSimulator;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.InputStream;
import java.io.File;

/**
 * MockStrutsTestCase is an extension of the base JUnit testcase that
 * provides additional methods to aid in testing Struts Action
 * objects.  It uses a mock object approach to simulate a servlet
 * container, and tests the execution of Action objects as they
 * are actually run through the Struts ActionServlet.  MockStrutsTestCase
 * provides methods that set up the request path, request parameters
 * for ActionForm subclasses, as well as methods that can verify
 * that the correct ActionForward was used and that the proper
 * ActionError messages were supplied.
 *
 *<br><br>
 *<b>NOTE:</b> By default, the Struts ActionServlet will look for the
 * file <code>WEB-INF/struts-config.xml</code>, so you must place
 * the directory that <i>contains</i> WEB-INF in your CLASSPATH.  If
 * you would like to use an alternate configuration file, please see
 * the setConfigFile() method for details on how this file is located.
 */
public class MockStrutsTestCase extends TestCase {

    ActionServlet actionServlet;
    HttpServletRequestSimulator request;
    HttpServletResponseSimulator response;
    HttpServletRequestWrapper requestWrapper;
    HttpServletResponseWrapper responseWrapper;
    ServletContextSimulator context;
    ServletConfigSimulator config;
    String actionPath;
    boolean isInitialized = false;
    boolean actionServletIsInitialized = false;

    protected static Log logger = LogFactory.getLog(MockStrutsTestCase.class);

    /**
     * Default constructor.
     */
    public MockStrutsTestCase(String testName) {
        super(testName);
    }

    /**
     * A check that every method should run to ensure that the
     * base class setUp method has been called.
     */
    private void init() {
        if (!isInitialized)
            throw new AssertionFailedError("You are overriding the setUp() method without calling super.setUp().  You must call the superclass setUp() method in your TestCase subclass to ensure proper initialization.");
    }

    /**
     * Sets up the test fixture for this test.  This method creates
     * an instance of the ActionServlet, initializes it to validate
     * forms and turn off debugging, and creates a mock HttpServletRequest
     * and HttpServletResponse object to use in this test.
     */
    public void setUp() throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setUp()");
        if (actionServlet == null)
            actionServlet = new ActionServlet();
        config = new ServletConfigSimulator();
        request = new HttpServletRequestSimulator(config.getServletContext());
        response = new HttpServletResponseSimulator();
        context = (ServletContextSimulator) config.getServletContext();
        requestWrapper = null;
        responseWrapper = null;
        isInitialized = true;
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setUp()");

        logger.debug("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        logger.fatal("fatal");
    }

    /**
     * Returns an HttpServletRequest object that can be used in
     * this test.
     */
    public HttpServletRequest getRequest() {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.getRequest()");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getRequest()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.getRequestWrapper()");
        init();
        if (requestWrapper == null) {
            if (logger.isDebugEnabled())
                logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getRequestWrapper()");
            return new HttpServletRequestWrapper(this.request);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("servletunit.struts.MockStrutsTestCase.getRequestWrapper() : wrapper class is '" + requestWrapper.getClass() + "'");
            }
            if (logger.isDebugEnabled())
                logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getRequestWrapper()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setRequestWrapper() : wrapper = " + wrapper);
        init();
        if (wrapper == null)
            throw new IllegalArgumentException("wrapper class cannot be null!");
        else {
            if (wrapper.getRequest() == null)
                wrapper.setRequest(this.request);
            this.requestWrapper = wrapper;
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setRequestWrapper()");
    }

    /**
     * Returns an HttpServletResponse object that can be used in
     * this test.
     */
    public HttpServletResponse getResponse() {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.getResponse()");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getResponse()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.getResponseWrapper()");
        init();
        if (responseWrapper == null) {
            if (logger.isDebugEnabled())
                logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getResponseWrapper()");
            return new HttpServletResponseWrapper(this.response);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("servletunit.struts.MockStrutsTestCase.getRequestWrapper() : wrapper class is '" + responseWrapper.getClass() + "'");
            }
            if (logger.isDebugEnabled())
                logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getResponseWrapper()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setResponseWrapper() : wrapper = " + wrapper);
        init();
        if (wrapper == null)
            throw new IllegalArgumentException("wrapper class cannot be null!");
        else {
            if (wrapper.getResponse() == null)
                wrapper.setResponse(this.response);
            this.responseWrapper = wrapper;
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setResponseWrapper()");
    }

    /**
     * Returns an HttpSession object that can be used in this
     * test.
     */
    public HttpSession getSession() {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.getSession()");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getSession()");
        return this.request.getSession(true);
    }

    /**
     * Returns the ActionServlet controller used in this
     * test.
     *
     */
    public ActionServlet getActionServlet() {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.getActionServlet()");
        init();
        try {
            if (!actionServletIsInitialized) {
                this.actionServlet.init(config);
                actionServletIsInitialized = true;
            }
        } catch (ServletException e) {
            if (logger.isDebugEnabled())
                logger.debug("Exiting!servletunit.struts.MockStrutsTestCase.getActionServlet()",e.getRootCause());
            throw new AssertionFailedError(e.getMessage());
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getActionServlet()");
        return actionServlet;
    }

    /**
     * Sets the ActionServlet to be used in this test execution.  This
     * method should only be used if you plan to use a customized
     * version different from that provided in the Struts distribution.
     */
    public void setActionServlet(ActionServlet servlet) {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setActionServlet() : servlet = " + servlet);
        init();
        if (servlet == null)
            throw new AssertionFailedError("Cannot set ActionServlet to null");
        this.actionServlet = servlet;
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setActionServlet()");
        actionServletIsInitialized = false;
    }

    /**
     * Executes the Action instance to be tested.  This method
     * calls the ActionServlet.doPost() method to execute the
     * Action instance to be tested, passing along any parameters
     * set in the HttpServletRequest object.  It stores any results
     * for further validation.
     *
     * @exception AssertionFailedError if there are any execution
     * errors while calling Action.perform()
     *
     */
    public void actionPerform() {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.actionPerform()");
        init();
        HttpServletRequest request = this.request;
        HttpServletResponse response = this.response;
        if (this.requestWrapper != null)
            request = this.requestWrapper;
        if (this.responseWrapper != null)
            response = this.responseWrapper;

        try {
            this.getActionServlet().doPost(request,response);
        } catch (ServletException se) {
            if (logger.isDebugEnabled())
                logger.debug("Exiting!servletunit.struts.MockStrutsTestCase.actionPerform()",se.getRootCause());
            fail("Error running action.perform(): " + se.getRootCause().getClass() + " - " + se.getRootCause().getMessage());
        } catch (Exception ex) {
            if (logger.isDebugEnabled())
                logger.debug("Exiting!servletunit.struts.MockStrutsTestCase.actionPerform()",ex);
            fail("Error running action.perform(): " + ex.getClass() + " - " + ex.getMessage());
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.actionPerform()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.addRequestParameter() : parameterName = " + parameterName + ", parameterValue = " + parameterValue);
        init();
        this.request.addParameter(parameterName,parameterValue);
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.addRequestParameter()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.addRequestParameter() : parameterName = " + parameterName + ", parameteValue = " + parameterValues);
        init();
        this.request.addParameter(parameterName,parameterValues);
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.addRequestParameter()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setRequestPathInfo() : pathInfo = " + pathInfo);
        init();
        this.setRequestPathInfo("",pathInfo);
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setRequestPathInfo()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setRequestPathInfo() : moduleName = " + moduleName + ", pathInfo = " + pathInfo);
        init();
        this.actionPath = Common.stripActionPath(pathInfo);
        if (moduleName != null) {
            if (!moduleName.equals("")) {
                if (!moduleName.startsWith("/"))
                    moduleName = "/" + moduleName;
                if (!moduleName.endsWith("/"))
                    moduleName = moduleName + "/";
            }
            this.request.setAttribute(Common.INCLUDE_SERVLET_PATH, moduleName);
        }
        this.request.setPathInfo(actionPath);
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setRequestPathInfo()");
    }

    /**
     * Sets an initialization parameter on the
     * ActionServlet.  Allows you to simulate an init parameter
     * that would normally have been found in web.xml,
     * but is not available while testing with mock objects.
     * @param key the name of the initialization parameter
     * @param value the value of the intialization parameter
     */
    public void setInitParameter(String key, String value){
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setInitParameter() : key = " + key + ", value = " + value);
        init();
        config.setInitParameter(key, value);
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setInitParameter()");
    }

    /**
     * Sets the context directory to be used with the getRealPath() methods in
     * the ServletContext and HttpServletRequest API.
     * @param contextDirectory a File object representing the root context directory
     * for this application.
     */
    public void setContextDirectory(File contextDirectory) {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setContextDirectory() : contextDirectory = " + contextDirectory);
        init();
        context.setContextDirectory(contextDirectory);
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setContextDirectory()");
    }

    /**
     * Sets the location of the Struts configuration file for the default module.
     * This method can take either an absolute path, or a relative path.  If an
     * absolute path is supplied, the configuration file will be loaded from the
     * underlying filesystem; otherwise, the ServletContext loader will be used.
     */
    public void setConfigFile(String pathname) {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setConfigFile() : pathName = " + pathname);
        init();
        setConfigFile(null,pathname);
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setConfigFile()");
    }

    /**
     * Sets the struts configuration file for a given sub-application. This method
     * can take either an absolute path, or a relative path.  If an absolute path
     * is supplied, the configuration file will be loaded from the underlying
     * filesystem; otherwise, the ServletContext loader will be used.
     *
     * @param moduleName the name of the sub-application, or null if this is the default application
     * @param pathname the location of the configuration file for this sub-application
     */
    public void setConfigFile(String moduleName, String pathname) {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setConfigFile() : moduleName = " + moduleName + ", pathname =" + pathname);
        init();
        if (moduleName == null)
            this.config.setInitParameter("config",pathname);
        else
            this.config.setInitParameter("config/" + moduleName,pathname);
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setConfigFile()");
    }

    /**
     * Sets the location of the web.xml configuration file to be used
     * to set up the servlet context and configuration for this test.
     * This method supports both init-param and context-param tags,
     * setting the ServletConfig and ServletContext appropriately.
     * This method can take either an absolute path, or a relative path.  If an
     * absolute path is supplied, the configuration file will be loaded from the
     * underlying filesystem; otherwise, the ServletContext loader will be used.
     */
    public void setServletConfigFile(String pathname) {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setServletConfigFile() : pathname = " + pathname);
        init();

        // pull in the appropriate parts of the
        // web.xml file -- first the init-parameters
        Digester digester = new Digester();
        digester.push(this.config);
        digester.setValidating(false);
        digester.addCallMethod("web-app/servlet/init-param", "setInitParameter", 2);
        digester.addCallParam("web-app/servlet/init-param/param-name", 0);
        digester.addCallParam("web-app/servlet/init-param/param-value", 1);
        try {
            InputStream input = context.getResourceAsStream(pathname);
            if(input==null)
                throw new AssertionFailedError("Invalid pathname: " + pathname);
            digester.parse(input);
            input.close();
        } catch (Exception e) {
            throw new AssertionFailedError("Received an exception while loading web.xml - " + e.getClass() + " : " + e.getMessage());
        }

        // now the context parameters..
        digester = new Digester();
        digester.setValidating(false);
        digester.push(this.context);
        digester.addCallMethod("web-app/context-param", "setInitParameter", 2);
        digester.addCallParam("web-app/context-param/param-name", 0);
        digester.addCallParam("web-app/context-param/param-value", 1);
        try {
            InputStream input = context.getResourceAsStream(pathname);
            if(input==null)
                throw new AssertionFailedError("Invalid pathname: " + pathname);
            digester.parse(input);
            input.close();
        } catch (Exception e) {
            throw new AssertionFailedError("Received an exception while loading web.xml - " + e.getClass() + " : " + e.getMessage());
        }
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setServletConfigFile()");
    }

    /**
     * Returns the forward sent to RequestDispatcher.
     */
    private String getActualForward() {
        if (logger.isDebugEnabled())
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.getActualForward()");
        if (response.containsHeader("Location")) {
            return Common.stripJSessionID(response.getHeader("Location"));
        } else
            try  {

                String strippedForward = request.getContextPath() + Common.stripJSessionID(((ServletContextSimulator) config.getServletContext()).getRequestDispatcherSimulator().getForward());
                if (logger.isDebugEnabled()) {
                    logger.debug("servletunit.struts.MockStrutsTestCase.getActualForward() : stripped forward and added context path - " + strippedForward);
                }
                if (logger.isDebugEnabled())
                    logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getActualForward()");
                return strippedForward;
            } catch (NullPointerException npe) {
                if (logger.isDebugEnabled()) {
                    logger.debug("servletunit.struts.MockStrutsTestCase.getActualForward() : caught NullPointerException - returning null",npe);
                }
                return null;
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.verifyForward() : forwardName = " + forwardName);
        init();
        Common.verifyForwardPath(actionServlet,actionPath,forwardName,getActualForward(),false,request,config.getServletContext(),config);
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.verifyForward()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.verifyForwardPath() : forwardPath = " + forwardPath);
        init();
        forwardPath = request.getContextPath() + forwardPath;

        String actualForward = getActualForward();
        if (actualForward == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("servletunit.struts.MockStrutsTestCase.verifyForwardPath() : actualForward is null - this usually means it is not mapped properly.");
            }
            throw new AssertionFailedError("Was expecting '" + forwardPath + "' but it appears the Action has tried to return an ActionForward that is not mapped correctly.");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("servletunit.struts.MockStrutsTestCase.verifyForwardPath() : expected forward = '" + forwardPath + "' - actual forward = '" + actualForward + "'");
        }
        if (!(actualForward.equals(forwardPath)))
            throw new AssertionFailedError("was expecting '" + forwardPath + "' but received '" + actualForward + "'");
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.verifyForwardPath()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.verifyInputForward()");
        init();
        Common.verifyForwardPath(actionServlet,actionPath,null,getActualForward(),true,request,config.getServletContext(),config);
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.verifyInputForward()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.verifyActionErrors() : errorNames = " + errorNames);
        init();
        Common.verifyActionMessages(request,errorNames,Action.ERROR_KEY,"error");
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.verifyActionErrors()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.verifyNoActionErrors()");
        init();
        Common.verifyNoActionMessages(request,Action.ERROR_KEY,"error");
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.verifyNoActionErrors()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.verifyActionMessages() : messageNames = " + messageNames);
        init();
        Common.verifyActionMessages(request,messageNames,Globals.MESSAGE_KEY,"action");
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.verifyActionMessages()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.verifyNoActionMessages()");
        init();
        Common.verifyNoActionMessages(request,Globals.MESSAGE_KEY,"action");
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.verifyNoActionMessages()");
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.getActionForm()");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.getActionForm()");
        return Common.getActionForm(actionServlet,actionPath,request,context);
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
            logger.debug("Entering+servletunit.struts.MockStrutsTestCase.setActionForm() : form = " + form);
        init();
        // make sure action servlet is intialized
        Common.setActionForm(form,request,actionPath,context,this.getActionServlet());
        if (logger.isDebugEnabled())
            logger.debug("Exiting-servletunit.struts.MockStrutsTestCase.setActionForm()");
    }




}

