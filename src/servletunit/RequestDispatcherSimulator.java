package servletunit;

// ServletUnit Library v1.2 - A java-based testing framework for servlets
// Copyright (C) June 1, 2001 Somik Raha
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// For any questions or suggestions, you can write to me at :
// Email : somik@kizna.com
//
// Postal Address :
// Somik Raha
// R&D Team
// Kizna Corporation
// 2-1-17-6F, Sakamoto Bldg., Moto Azabu, Minato ku, Tokyo, 106 0046, JAPAN
//
// This class was contributed by :
// Dane S. Foster
// Email: dfoster@equitytg.com
//
// Additions by:
// Deryl Seale (deryl@acm.org) 10/31/2001

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Simulates a <code>javax.servlet.RequestDispatcher</code> object.
 */
public class RequestDispatcherSimulator implements RequestDispatcher
{
    private Object dispatchedResource;
    /**
     *@param    dispatchedResource      The <code>dispatchedResource</code> object represents the resource that
     *                                  <code>this</code> <code>javax.servlet.RequestDispatcher</code> is tied to.
     *                                  Currently this class only supports <code>javax.servlet.Servlet</code> objects
     *                                  and <code>java.lang.String</code> objects.  If the parameter passed in is not
     *                                  a <code>javax.servlet.Servlet</code> object when forward or include is called
     *                                  the parameter's toString method is called and sent to <code>System.out</code>.
     *                                  Otherwise, the appropriate service method is called.
     */
    public RequestDispatcherSimulator( Object dispatchedResource )
    {
        this.dispatchedResource = dispatchedResource;
    }
    /**
     * Simulates the forward method of the <code>javax.servlet.RequestDispatcher</code> interface
     */
    public void forward( ServletRequest request, ServletResponse response ) throws ServletException, IOException
    {
        if( dispatchedResource instanceof Servlet )
            ((Servlet)dispatchedResource).service( request, response );
    }
    public void include( ServletRequest request, ServletResponse response ) throws ServletException, IOException
    {
        System.out.println( dispatchedResource.toString() );
    }

    public String getForward() {
        if (dispatchedResource instanceof String)
            return (String) dispatchedResource;
        else
            return dispatchedResource.getClass().toString();
    }
}
