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
        if (actionServlet == null)
            actionServlet = new ActionServlet();
        config = new ServletConfigSimulator();
        request = new HttpServletRequestSimulator(config.getServletContext());
        response = new HttpServletResponseSimulator();
        context = (ServletContextSimulator) config.getServletContext();
        requestWrapper = null;
        responseWrapper = null;
        isInitialized = true;
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
            if (wrapper.getRequest() == null)
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
            if (wrapper.getResponse() == null)
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
        return this.request.getSession(true);
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
                this.actionServlet.init(config);
                actionServletIsInitialized = true;
            }
        } catch (ServletException e) {
            throw new AssertionFailedError(e.getMessage());
        }
        return actionServlet;
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
            se.getRootCause().printStackTrace();
            fail("Error running action.perform(): " + se.getRootCause().getClass() + " - " + se.getRootCause().getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error running action.perform(): " + ex.getClass() + " - " + ex.getMessage());
        }
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
        this.request.addParameter(parameterName,parameterValue);
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
        this.request.addParameter(parameterName,parameterValues);
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
        init();
        config.setInitParameter(key, value);
        actionServletIsInitialized = false;
    }

    /**
     * Sets the context directory to be used with the getRealPath() methods in
     * the ServletContext and HttpServletRequest API.
     * @param contextDirectory a File object representing the root context directory
     * for this application.
     */
    public void setContextDirectory(File contextDirectory) {
        init();
        context.setContextDirectory(contextDirectory);
    }

    /**
     * Sets the location of the Struts configuration file for the default module.
     * This method can take either an absolute path, or a relative path.  If an
     * absolute path is supplied, the configuration file will be loaded from the
     * underlying filesystem; otherwise, the ServletContext loader will be used.
     */
    public void setConfigFile(String pathname) {
        init();
        setConfigFile(null,pathname);
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
        init();
        if (moduleName == null)
            this.config.setInitParameter("config",pathname);
        else
            this.config.setInitParameter("config/" + moduleName,pathname);
        actionServletIsInitialized = false;
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
        init();

        // pull in the appropriate parts of the
        // web.xml file -- first the init-parameters
        Digester digester = new Digester();
        digester.push(this.config);
        digester.setDebug(0);
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
        digester.setDebug(0);
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
    }

    /**
     * Returns the forward sent to RequestDispatcher.
     */
    private String getActualForward() {
        if (response.containsHeader("Location")) {
            return Common.stripJSessionID(response.getHeader("Location"));
        } else
            try  {
                return request.getContextPath() + Common.stripJSessionID(((ServletContextSimulator) config.getServletContext()).getRequestDispatcherSimulator().getForward());
            } catch (NullPointerException npe) {
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
        init();
        Common.verifyForwardPath(actionServlet,actionPath,forwardName,getActualForward(),false,request,config.getServletContext(),config);
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
        Common.verifyForwardPath(actionServlet,actionPath,null,getActualForward(),true,request,config.getServletContext(),config);
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
        Common.verifyActionMessages(request,errorNames,Action.ERROR_KEY,"error");
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
        Common.verifyNoActionMessages(request,Action.ERROR_KEY,"error");
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
        init();
        Common.verifyActionMessages(request,messageNames,Globals.MESSAGE_KEY,"action");
    }

    /**
     * Verifies that the ActionServlet controller sent no action messages upon
     * executing an Action object.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent any action messages after excecuting and Action object.
     */
    public void verifyNoActionMessages() {
        init();
        Common.verifyNoActionMessages(request,Globals.MESSAGE_KEY,"action");
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
        init();
        // make sure action servlet is intialized
        Common.setActionForm(form,request,actionPath,context,this.getActionServlet());
    }




}

