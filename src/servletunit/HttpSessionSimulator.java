package servletunit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.Hashtable;

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
// Dane S. Foster
// Equity Technology Group, Inc
// http://www.equitytg.com.
// 954.360.9800
// dfoster@equitytg.com
//
// Deryl Seale (deryl@acm.org) 10/31/2001

/**
 * This class simulates an HttpSession object. You can actually work with sessions. The simulation is done using a static
 * session object inside HttpServletRequest.
 */
public class HttpSessionSimulator implements HttpSession
{
    private Hashtable values;
    private boolean valid = true;
    private ServletContext context;

    public HttpSessionSimulator(ServletContext context)
    {
	this.context = context;
        values = new Hashtable();
    }

    public Object getAttribute(String s) throws IllegalStateException
    {
        checkValid();
        return values.get(s);
    }

    public Enumeration getAttributeNames() throws IllegalStateException
    {
        checkValid();
        return values.keys();
    }

    public long getCreationTime() throws IllegalStateException
    {
        checkValid();
        return -1;
    }

    public String getId()
    {
        return "-9999";
    }

    public long getLastAccessedTime()
    {
        return -1;
    }

    public int getMaxInactiveInterval() throws IllegalStateException
    {
        checkValid();
        return -1;
    }

    /**
     * This method is not supported.
     */
    public HttpSessionContext getSessionContext()
    {
        throw new UnsupportedOperationException("getSessionContext not supported!");
    }

    public Object getValue(String s) throws IllegalStateException
    {
        checkValid();
        return values.get(s);
    }

    public String[] getValueNames() throws IllegalStateException
    {
        checkValid();
        return (String[]) values.keySet().toArray();
    }

    public void invalidate() throws IllegalStateException
    {
        checkValid();
        this.valid = false;
    }

    public boolean isNew() throws IllegalStateException
    {
        checkValid();
        return false;
    }

    public void putValue(String s, Object obj) throws IllegalStateException
    {
        checkValid();
        values.put(s,obj);
    }

    public void removeAttribute(String s) throws IllegalStateException
    {
        checkValid();
        values.remove(s);
    }

    public void removeValue(String s) throws IllegalStateException
    {
        checkValid();
        values.remove(s);
    }

    public void setAttribute(String s, Object obj) throws IllegalStateException
    {
        checkValid();
        values.put(s,obj);
    }

    public void setMaxInactiveInterval(int i)
    {
    }

    public ServletContext getServletContext() {
	return this.context;
    }

    private void checkValid() throws IllegalStateException {
        if (!valid)
            throw new IllegalStateException("session has been invalidated!");
    }
}
