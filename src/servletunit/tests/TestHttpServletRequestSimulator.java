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
import servletunit.HttpServletRequestSimulator;
import servletunit.ServletContextSimulator;

/**
 * A Junit based test of the HttpServletResponseSimulator class
 * @author Sean Pritchard
 * @version 1.0
 */

public class TestHttpServletRequestSimulator extends TestCase {

    HttpServletRequestSimulator request;

    public TestHttpServletRequestSimulator(String testCase) {
        super(testCase);
    }

    public void setUp() {
	request = new HttpServletRequestSimulator(new ServletContextSimulator());
    }

    public void testAddParameterArray() {
	String[] values = { "value1", "value2" };
	request.addParameter("name1",values);
	String[] result = request.getParameterValues("name1");
	if (result.length != 2) 
	    fail();
	if (!((result[0].equals("value1")) && (result[1].equals("value2"))))
	    fail();
    }

    public void testGetParameterValuesSingle() {
	request.addParameter("name1","value1");
	String[] result = request.getParameterValues("name1");
	if (result.length != 1) 
	    fail();
	if (!(result[0].equals("value1")))
	    fail();
    }
    
    public void testGetParameterWithArray() {
	String[] values = { "value1", "value2" };
	request.addParameter("name1",values);
	String result = request.getParameter("name1");
	if (!(result.equals("value1")))
	    fail();
    }
    

}

