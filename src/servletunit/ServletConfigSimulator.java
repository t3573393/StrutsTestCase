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
// Additions by:
//
// Deryl Seale (deryl@acm.org) 10/31/2001

import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Hashtable;

/**
 * This class simulates a ServletConfig.
 */

public class ServletConfigSimulator implements ServletConfig
{

    private Hashtable parameters;
    private ServletContext context;

    public ServletConfigSimulator()
    {
        parameters=new Hashtable();
        context = new ServletContextSimulator();
    }

    /**
     * Returns a <code>String</code> containing the value of the
     * named initialization parameter, or <code>null</code> if
     * the parameter does not exist.
     *
     * @param name      a <code>String</code> specifying the name
     *                  of the initialization parameter
     *
     * @return          a <code>String</code> containing the value
     *                  of the initialization parameter
     *
     */
    public String getInitParameter(String name)
    {
        return (String) parameters.get(name);
    }

    /**
     * Returns the names of the servlet's initialization parameters
     * as an <code>Enumeration</code> of <code>String</code> objects,
     * or an empty <code>Enumeration</code> if the servlet has
     * no initialization parameters.
     *
     * @return          an <code>Enumeration</code> of <code>String</code>
     *                  objects containing the names of the servlet's
     *                  initialization parameters
     *
     *
     *
     */
    public Enumeration getInitParameterNames()
    {
        return parameters.keys();
    }

    /**
     * Returns a reference to the {@link ServletContext} in which the caller
     * is executing.
     *
     *
     * @return          a {@link ServletContext} object, used
     *                  by the caller to interact with its servlet
     *                  container
     *
     * @see             ServletContext
     *
     */
    public ServletContext getServletContext()
    {
        return context;
    }

    /**
     * Returns the name of this servlet instance.
     * The name may be provided via server administration, assigned in the
     * web application deployment descriptor, or for an unregistered (and thus
     * unnamed) servlet instance it will be the servlet's class name.
     *
     * @return          the String "ActionServlet"
     *
     *
     *
     */
    public String getServletName()
    {
        return "ActionServlet";
    }

    /**
     * Sets a named initialization parameter with the supplied
     * <code>String</code> value.
     *
     * @param name      a <code>String</code> specifying the name
     *                  of the initialization parameter
     *
     * @param value     a <code>String</code> value for this initialization
     *                  parameter
     *
     */
    public void setInitParameter(String key,String value)
    {
        parameters.put(key,value);
    }

}

