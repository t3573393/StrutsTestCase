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

package servletunit.struts.tests;

import servletunit.struts.MockStrutsTestCase;

public class TestTilesForward extends MockStrutsTestCase {

    public TestTilesForward(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        // For some reason, I have to pass these values in here; the plugin
        // properties don't seem to work.
        setInitParameter("definitions-config","/WEB-INF/tiles-config.xml");
        setInitParameter("definitions-debug","0");
        setConfigFile("tiles","/WEB-INF/struts-config-tiles.xml");
        setConfigFile("/WEB-INF/struts-config.xml");
    }


    public void testTilesForward() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("tiles","/tilesForward.do");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/layouts/pageLayout.jsp");
    }


}
