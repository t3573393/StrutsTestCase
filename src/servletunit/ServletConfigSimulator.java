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
    protected Hashtable parameters;
    private ServletContextSimulator context;

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
         * <p>The value of an initialization parameter is a single
         * <code>String</code>, which you must interpret.
         *
         *
         * @param name  a <code>String</code> containing the name
         *                      of the parameter whose value is requested
         *
         * @return              a <code>String</code> representing the value
         *                      of the parameter
         *
         */

        public String getInitParameter(String name)
        {
                String as[] = getParameterValues(name);
                if(as != null)
                 return as[0];
                else
                 return null;
        }
        /**
         * Returns the names of the servlet's initialization parameters
         * as an <code>Enumeration</code> of <code>String</code> objects,
         * or an empty <code>Enumeration</code> if the servlet has
         * no initialization parameters.
         *
         * @return              an <code>Enumeration</code> of <code>String</code>
         *                      objects containing the names of the servlet's
         *                      initialization parameters
         *
         *
         *
         */

        public Enumeration getInitParameterNames()
        {
                return parameters.keys();
        }
        public String[] getParameterValues(String s)
        {
          return (String[])parameters.get(s);
        }
        /**
         * Returns the {@link ServletContext} object that the server
         * has passed to this servlet. The <code>ServletContext</code>
         * object is part of the <code>ServletConfig</code> object this
         * interface defines.
         *
         *
         * @return              a {@link ServletContext} object, which
         *                      gives the servlet information about how
         *                      to interact with the server
         *
         * @see         ServletContext
         *
         */

        public ServletContext getServletContext()
        {
            return context;
        }
        public String getServletName()
        {
                return null;
        }
        public void setInitParameter(String key,String value)
        {
                String [] arr = {value};
                        parameters.put(key,arr);
        }
}
