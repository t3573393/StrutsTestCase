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

package servletunit.struts.tests;

import javax.servlet.*;
import javax.servlet.http.*;
import servletunit.struts.MockStrutsTestCase;
import java.io.IOException;
import org.apache.struts.action.Action;
import org.apache.struts.taglib.html.Constants;
import junit.framework.*;

public class TestRedirectAction extends MockStrutsTestCase {

    public TestRedirectAction(String testName) {
        super(testName);
    }

    public void testVerifyRedirect() {
        setRequestPathInfo("/testRedirect");
        actionPerform();
	verifyForward("redirect");
	verifyForwardPath("http://www.yahoo.com");
        verifyNoActionErrors();
    }
    
    /**
     * Confirms verifyForward works correctly when the redirect path
     * is a relative (not absolute) URL
     */
    public void testRelativeRedirect() {
        setRequestPathInfo("/testRelativeRedirect");
        actionPerform();
	verifyForward("redirect");
	verifyForwardPath("/main/success.jsp");
        verifyNoActionErrors();
    }

    public void testVerifyRedirectFail() {
	try {
	    setRequestPathInfo("/testRedirect");
	    actionPerform();
	    verifyForward("login");
	    verifyNoActionErrors();
	} catch (AssertionFailedError e) {
	    return;
	}
	fail("We are apparently getting the same redirects, when they should be different.");
    }
    

}
