//  FilterChainSimulator - A mock object for the servlet 2.3 FilterChain
//  Interface.
//  Copyright (C) 2002 Sean Pritchard
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
//  this address: Sean Pritchard smpritchard@yahoo.com


package servletunit;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *  A unit testing tool for simulating a FilterChain <p>
 *
 *
 * @author     Sean Pritchard
 * May 11, 2002
 * @version    1.0
 */

public class FilterChainSimulator implements FilterChain {

    private ServletRequest request = null;
    private ServletResponse response = null;
    private boolean doFilterCalled = false;


    /**
     *  Constructor for the FilterChainSimulator object
     */
    public FilterChainSimulator() { }


    /**
     *  Description of the Method
     *
     * @param  parm1    The request
     * @param  parm2    The response
     * @exception  javax.servlet.ServletException
     * @exception  java.io.IOException             Description of the Exception
     */
    public void doFilter(ServletRequest parm1, ServletResponse parm2) throws javax.servlet.ServletException, java.io.IOException {
        request = parm1;
        response = parm2;
        doFilterCalled = true;
    }


    /**
     *  Gets the request passed in as a parameter of the doFilter call.
     *
     * @return    The request value
     */
    public ServletRequest getRequest() {
        return request;
    }


    /**
     *  *  Gets the response passed in as a parameter of the doFilter call.
     *
     * @return    The response value
     */
    public ServletResponse getResponse() {
        return response;
    }


    /**
     *  Indicates whether doFilter has been called.
     *
     * @return    true if doFilter has been called
     */
    public boolean doFilterCalled() {
        return doFilterCalled;
    }
}
