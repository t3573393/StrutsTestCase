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
import junit.framework.TestCase;
import junit.framework.AssertionFailedError;
import servletunit.HttpServletRequestSimulator;
import servletunit.HttpServletResponseSimulator;
import servletunit.ServletContextSimulator;
import servletunit.ServletConfigSimulator;
import servletunit.RequestDispatcherSimulator;
import java.util.Iterator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

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
    ServletContextSimulator context;
    ServletConfigSimulator config;

    /**
     * Default constructor.
     */
    public MockStrutsTestCase(String testName) {
        super(testName);
    }

    /**
     * Sets up the test fixture for this test.  This method creates
     * and instance of the ActionServlet, initializes it to validate
     * forms and turn off debugging, and creates a mock HttpServletRequest
     * and HttpServletResponse object to use in this test.
     */
    public void setUp() {
        try {
            actionServlet = new ActionServlet();
            config = new ServletConfigSimulator();
            config.setInitParameter("debug","0");
            config.setInitParameter("detail","0");
            config.setInitParameter("validate","true");
            request = new HttpServletRequestSimulator();
            response = new HttpServletResponseSimulator();
        } catch (Exception e) {
            throw new AssertionFailedError("\n" + e.getClass() + " - " + e.getMessage());
        }
    }

    /**
     * Tears down the test fixture upon completion.  This method calls
     * the destroy method on the ActionServlet method used in this test.
     */
    public void tearDown() {
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
        return this.request;
    }

    /**
     * Returns an HttpServletResponse object that can be used in
     * this test.
     */
    public HttpServletResponse getResponse() {
        return this.response;
    }

    /**
     * Returns an HttpSession object that can be used in this
     * test.
     */
    public HttpSession getSession() {
        return this.request.getSession(true);
    }

    /**
     * Returns an ActionServlet controller to be used in this
     * test.
     *
     * @deprecated use actionPerform() instead
     */
    public ActionServlet getActionServlet() {
        try {
            this.actionServlet.init(config);
            return this.actionServlet;
        } catch (ServletException e) {
            throw new AssertionFailedError(e.getMessage());
        }
    }

    /**
     * Executes the Action instance to be tested.  This method
     * calls the ActionServlet.doPost() method to execute the
     * Action instance to be tested, passing along any parameters
     * set in the HttpServletRequest object.  It stores any results
     * for further verification.
     *
     * @exception AssertionFailedError if there are any execution
     * errors while calling Action.perform()
     *
     */
    public void actionPerform() {
        try {
            this.getActionServlet().doPost(request,response);
        } catch (ServletException e) {
            throw new AssertionFailedError("ServletException: " + e.getMessage());
        } catch (IOException e) {
            throw new AssertionFailedError("IOException: " + e.getMessage());
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
        this.request.addParameter(parameterName,parameterValue);
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
        this.request.setPathInfo(pathInfo);
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
        // ugly hack to get this to play ball with Class.getResourceAsStream()
        if (!pathname.startsWith("/")) {
            String prefix = this.getClass().getPackage().getName().replace('.','/');
            pathname = "/" + prefix + "/" + pathname;
        }
        this.config.setInitParameter("config",pathname);
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
        RequestDispatcherSimulator dispatcher = ((ServletContextSimulator) config.getServletContext()).getRequestDispatcherSimulator();
        String path = request.getPathInfo();
        int slash = path.lastIndexOf("/");
        int period = path.lastIndexOf(".");
        if ((period >= 0) && (period > slash))
            path = path.substring(0, period);
        ActionForward forward = actionServlet.findMapping(path).findForward(forwardName);
        if (forward == null)
            forward = actionServlet.findForward(forwardName);
        if (forward == null)
            throw new AssertionFailedError("cannot find forward '" + forwardName + "'");
        String expectedName = forward.getPath();
        if (!dispatcher.getForward().equals(expectedName))
            throw new AssertionFailedError("was expecting '" + expectedName + "' but received '" + dispatcher.getForward() + "'");
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

        int actualLength = 0;

        ActionErrors errors = (ActionErrors) request.getAttribute(Action.ERROR_KEY);
        if (errors == null) {
            throw new AssertionFailedError("was expecting some error messages, but received none.");
        } else {
            Iterator iterator = errors.get();
            while (iterator.hasNext()) {
                actualLength++;
                boolean throwError = true;
                ActionError error = (ActionError) iterator.next();
                for (int x = 0; x < errorNames.length; x++) {
                    if (error.getKey().equals(errorNames[x]))
                        throwError = false;
                }
                if (throwError) {
                    throw new AssertionFailedError("received unexpected error '" + error.getKey() + "'");
                }
            }
            if (actualLength != errorNames.length) {
                throw new AssertionFailedError("was expecting " + errorNames.length + " error(s), but received " + actualLength + " error(s)");
            }
        }
    }

    /**
     * Verifies that the ActionServlet controller sent no error messages upon
     * executing an Action object.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent any error messages after excecuting and Action object.
     */
    public void verifyNoActionErrors() {
        ActionErrors errors = (ActionErrors) request.getAttribute(Action.ERROR_KEY);
        if (errors != null) {
            Iterator iterator = errors.get();
            if (iterator.hasNext()) {
                throw new AssertionFailedError("was expecting no error messages, but received some.");
            }
        }
    }

}

