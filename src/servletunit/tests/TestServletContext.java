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

package servletunit.tests;

import junit.framework.TestCase;
import servletunit.ServletContextSimulator;

import java.util.Enumeration;
import java.io.File;

public class TestServletContext extends TestCase {

    ServletContextSimulator context;

    public TestServletContext(String testName) {
        super(testName);
    }

    public void setUp() {
        context = new ServletContextSimulator();
    }

    public void testSetAttribute() {
        context.setAttribute("test","testValue");
        assertEquals("testValue",context.getAttribute("test"));
    }

    public void testNoAttribute() {
        assertNull(context.getAttribute("badValue"));
    }

    public void testGetAttributeNames() {
        context.setAttribute("test","testValue");
        context.setAttribute("another","anotherValue");
        assertEquals("testValue",context.getAttribute("test"));
        assertEquals("anotherValue",context.getAttribute("another"));
        Enumeration names = context.getAttributeNames();
        boolean fail = true;
        while (names.hasMoreElements()) {
            fail = true;
            String name = (String) names.nextElement();
            if ((name.equals("test")) || (name.equals("another")))
                fail = false;
        }
        if (fail)
            fail();
    }

    public void testGetRealPath() {
        File file = new File("c:/develop/projects/strutstestcase");
        context.setContextDirectory(file);
        assertEquals(new File(file,"test.html").getAbsolutePath(),context.getRealPath("/test.html"));
    }

    public void testGetRealPathNotSet() {
        context.setContextDirectory(null);
        assertNull(context.getRealPath("/test.html"));
    }

}
