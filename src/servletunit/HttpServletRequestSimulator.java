package servletunit;

import javax.servlet.http.*;
import java.util.*;
import javax.servlet.*;
import java.io.*;
import java.security.Principal;


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
 * This class simulates a HttpServletRequest. Use this to provide the request object in your doGet() or doPost() methods.
 * You can add parameters using addParameter. You can also use setAttribute() to set an attribute. The code that you
 * write in doGet() or doPost() should not change, and should work ideally.
 */
public class HttpServletRequestSimulator implements HttpServletRequest
{
    protected Hashtable attributes;
    protected String scheme;
    protected String protocol;
    protected String requestURI;
    protected String contextPath;
    protected String lookupPath;
    protected String servletPath;
    protected String pathInfo;
    protected String queryString;
    protected String method;
    protected int contentLength;
    protected String contentType;
    protected String charEncoding;
    protected String authType;
    protected String remoteUser;
    protected String reqSessionId;

    protected Hashtable parameters;
    protected Hashtable headers;
    protected Hashtable requestDispatchers;

    protected HttpSession session;
    public final static int GET = 0;
    public final static int POST = 1;
    public final static int PUT = 2;

    public HttpServletRequestSimulator()
    {
        scheme = "http";
        attributes = new Hashtable();
        parameters = new Hashtable();
        headers = new Hashtable();
        requestDispatchers = new Hashtable();
        //if (getHeader("Accept")==null)
        //setHeader("Accept","dummy accept");
        contentLength = -1;
        contentType = "";
        if( session == null )
            session = new HttpSessionSimulator();
    }

    /**
     * Add a <code>javax.servlet.RequestDispatcher</code> to a list of possible dispatchers
     *
     * @param   url     The url of the resource.
     * @param   dispatcher  The resource
     */
    public void addDispatcher( String url, Object dispatcher )
    {
        this.requestDispatchers.put( url, dispatcher );
    }
    /**
     * Adds a parameter to this object's list of parameters
     *
     * @param   key     The name of the parameter
     * @param   value   The value of the parameter
     */
    public void addParameter( String key, String value )
    {
        this.parameters.put( key, value );
    }

    public Object getAttribute(String s)
    {
        return attributes.get(s);
    }

    public Enumeration getAttributeNames()
    {
          return attributes.keys();
    }

    public String getAuthType()
    {
        return authType;
    }

    public String getCharacterEncoding()
    {
        return charEncoding;
    }

    public int getContentLength()
    {
        return contentLength;
    }

    public String getContentType()
    {
        return contentType;
    }

    public String getContextPath()
    {
        return null;
    }

    public Cookie [] getCookies()
    {
        return null;
    }

    public long getDateHeader(String s)
    {
        return 0;
    }

    public String getHeader(String s)
    {
        return (String)headers.get(s);
    }

    public Enumeration getHeaderNames()
    {
        return headers.keys();
    }

    public Enumeration getHeaders(String s)
    {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException { return null; }

    public int getIntHeader(String s)
    {
        return 0;
    }

    public  Locale getLocale()
    {
        return null;
    }

    public Enumeration getLocales()
    {
        return null;
    }

    public String getMethod()
    {
        return method;
    }

    /**
     * @param   name    The name of the parameter
     * @return  The value of the parameter.  If there was no parameter associated with the <code>name</code>
     *          <code>null</code> is returned.  Please note, if the parameter is a list (i.e. check box items)
     *          you must use <code>getParamterValues( java.lang.String )</code> instead
     */
    public String getParameter( String s )
    {
        Object param = parameters.get( s );
        if( null == param )
            return null;
        if( param.getClass().isArray() )
            return null;
        return (String)param;
    }

    public Enumeration getParameterNames()
    {
        return parameters.keys();
    }

    /**
     * Returns the list of values for a parameter as an array of Strings
     *
     * @param   s   The name of the parameter
     */
    public String[] getParameterValues( String s )
    {
        Object param = parameters.get( s );
        if( null == param )
            return null;
        else {
            if (param.getClass().isArray()) {
                return (String[])param;
            } else {
                return new String[] {(String) param};
            }
        }
    }

    public String getPathInfo()
    {
        return pathInfo;
    }

    public String getPathTranslated()
    {
        return null;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public String getQueryString()
    {
        return queryString;
    }

    public BufferedReader getReader() throws IOException { return null;}
    /**
     * @deprecated
     */
    public String getRealPath(String s)
    {
        return null;
    }
    public String getRemoteAddr() { return null;}
    public String getRemoteHost() { return null;}
    public String getRemoteUser()
    {
        return remoteUser;
    }

    /**
     * @param   url The URL of the requested <code>javax.servlet.RequestDispatcher</code> object
     * @return  The <code>javax.servlet.RequestDispatcher</code> associated with the specified URL
     */
    public  RequestDispatcher getRequestDispatcher( String url )
    {
        return (RequestDispatcher)requestDispatchers.get( url );
    }

    public String getRequestedSessionId()
    {
        return reqSessionId;
    }

    public String getRequestURI()
    {
        return requestURI;
    }

    public StringBuffer getRequestURL()
    {
        return null;
    }

    public String getScheme()
    {
        return scheme;
    }

    public String getServerName() { return null; }
    public int getServerPort() { return 0;}
    public String getServletPath()
    {
        return servletPath;
    }

    public HttpSession getSession()
    {
        return session;
    }

    /**
     * Returns the <code>javax.servlet.http.HttpSession</code> object associated with <code>this</code>
     * request.
     *
     * @param b   If <code>true</code> return the current <code>HttpSession</code> object, otherwise create a new one
     * @return    A <code>javax.servlet.http.HttpSession</code> object
     * $Modified: 7-28-01
     * $Modifier: Dane S. Foster <mailto:dfoster@equitytg.com/>
     */
    public HttpSession getSession(boolean b)
    {
	if ((session == null) && (b))
            this.session = new HttpSessionSimulator();
        return this.session;
    }

    public Principal getUserPrincipal()
    {
        return null;
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        return true;
    }

    /**
     * @deprecated
     */
    public boolean isRequestedSessionIdFromUrl()
    {
        return isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    public boolean isRequestedSessionIdValid()
    {
        return true;
    }

    public boolean isSecure()
    {
        return false;
    }

    public boolean isUserInRole(String s)
    {
        return false;
    }

    public void removeAttribute(String s)
    {
        attributes.remove(s);
    }

    public void setAttribute(String s, Object obj)
    {
        attributes.put(s, obj);
    }

    public void setAuthType(String s)
    {
        authType = s;
    }

    public void setCharacterEncoding(String s)
    {
        charEncoding = s;
    }

    public void setHeader(String key, String value)
    {
        headers.put(key,value);
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/7/2001 8:07:20 PM)
     * @param methodType int
     */
    public void setMethod(int methodType)
    {
        switch (methodType)
            {
            case GET:method="GET";break;
            case PUT:method="PUT";break;
            case POST:method="POST";break;
            default:method="NULL";
            }
    }

    public void setParameterValue( String key, String[] value )
    {
        parameters.put( key, value );
    }

    public void setPathInfo(String s)
    {
        pathInfo = s;
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/7/2001 8:19:27 PM)
     * @param remoteUser java.lang.String
     */
    public void setRemoteUser(String remoteUser)
    {
        this.remoteUser = remoteUser;
    }

    void setRequestedSessionId(String s)
    {
        reqSessionId = s;
    }

    public void setRequestURI(String requestURI)
    {
        this.requestURI = requestURI;
    }

    public void setScheme(String s)
    {
        scheme = s;
    }

    void setServletPath(String s)
    {
        servletPath = s;
    }
}
