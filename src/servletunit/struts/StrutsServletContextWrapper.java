//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
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

import org.apache.cactus.server.ServletContextWrapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import servletunit.RequestDispatcherSimulator;

/**
 * A wrapper for the ServletContext class.  This is used in
 * CactusStrutsTestCase so that we can retrieve the forward
 * processed by the ActionServlet.  This allows us to to use
 * the ActionServlet as a black box, rather than mimic its
 * behavior as was previously the case.
 */
public class StrutsServletContextWrapper extends ServletContextWrapper {

    private String dispatchedResource;

    public StrutsServletContextWrapper(ServletContext context) {
        super(context);
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        dispatchedResource = path;
        return new RequestDispatcherSimulator(path);
        //return super.getRequestDispatcher(path);
    }

    public String getForward() {
        return dispatchedResource;
    }

}

