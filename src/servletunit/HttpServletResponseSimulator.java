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
// Dane S. Foster
// Equity Technology Group, Inc
// http://www.equitytg.com.
// 954.360.9800
// dfoster@equitytg.com

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import java.io.*;
import java.util.Locale;
import javax.servlet.http.Cookie;
import junit.framework.AssertionFailedError;


/**
 * This class captures the response from the servlet. Use this to pass the response object to doGet() or doPost().
 * Important : We can only capture the output when it is written using a PrintWriter. In order to examine the output
 * of the doGet or the doPost methods, pick up the string buffer from this response object by using getWriterBuffer().
 */
public class HttpServletResponseSimulator implements HttpServletResponse
{
    protected OutputStream servOStream;       // The non-default javax.servlet.ServletOutputStream

    protected boolean calledGetWriter, calledGetOutputStream;

    protected StringWriter stringWriter=null;
        protected PrintWriter printWriter=null;
        public static final int SC_CONTINUE = 100;


        public static final int SC_SWITCHING_PROTOCOLS = 101;

        public static final int SC_OK = 200;

        public static final int SC_CREATED = 201;

        public static final int SC_ACCEPTED = 202;

        public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;

        public static final int SC_NO_CONTENT = 204;

        public static final int SC_RESET_CONTENT = 205;

        public static final int SC_PARTIAL_CONTENT = 206;

        public static final int SC_MULTIPLE_CHOICES = 300;

        public static final int SC_MOVED_PERMANENTLY = 301;

        public static final int SC_MOVED_TEMPORARILY = 302;

        public static final int SC_SEE_OTHER = 303;

        public static final int SC_NOT_MODIFIED = 304;

        public static final int SC_USE_PROXY = 305;

        public static final int SC_BAD_REQUEST = 400;

        public static final int SC_UNAUTHORIZED = 401;

        public static final int SC_PAYMENT_REQUIRED = 402;

        public static final int SC_FORBIDDEN = 403;

        public static final int SC_NOT_FOUND = 404;

        public static final int SC_METHOD_NOT_ALLOWED = 405;

        public static final int SC_NOT_ACCEPTABLE = 406;

        public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;

        public static final int SC_REQUEST_TIMEOUT = 408;

        public static final int SC_CONFLICT = 409;

        public static final int SC_GONE = 410;

        public static final int SC_LENGTH_REQUIRED = 411;

        public static final int SC_PRECONDITION_FAILED = 412;

        public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;

        public static final int SC_REQUEST_URI_TOO_LONG = 414;

        public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;

        public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

        public static final int SC_EXPECTATION_FAILED = 417;

        public static final int SC_INTERNAL_SERVER_ERROR = 500;

        public static final int SC_NOT_IMPLEMENTED = 501;

        public static final int SC_BAD_GATEWAY = 502;

        public static final int SC_SERVICE_UNAVAILABLE = 503;

        public static final int SC_GATEWAY_TIMEOUT = 504;

        public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
        public void addCookie(Cookie cookie)
        {
        }
        /**
         *
         * Adds a response header with the given name and
         * date-value.  The date is specified in terms of
         * milliseconds since the epoch.  This method allows response headers
         * to have multiple values.
         *
         * @param       name    the name of the header to set
         * @param       value   the additional date value
         *
         * @see #setDateHeader
         */

        public void addDateHeader(String name, long date)
        {
        }
        /**
         * Adds a response header with the given name and value.
         * This method allows response headers to have multiple values.
         *
         * @param       name    the name of the header
         * @param       value   the additional header value
         *
         * @see #setHeader
         */

        public void addHeader(String name, String value)
        {
        }
        /**
         * Adds a response header with the given name and
         * integer value.  This method allows response headers to have multiple
         * values.
         *
         * @param       name    the name of the header
         * @param       value   the assigned integer value
         *
         * @see #setIntHeader
         */

        public void addIntHeader(String name, int value)
        {
        }
        /**
         * Returns a boolean indicating whether the named response header
         * has already been set.
         *
         * @param       name    the header name
         * @return              <code>true</code> if the named response header
         *                      has already been set;
         *                      <code>false</code> otherwise
         */

        public boolean containsHeader(String name)
        {
                return false;
        }
        /**
         * @deprecated  As of version 2.1, use
         *                      encodeRedirectURL(String url) instead
         *
         * @param       url     the url to be encoded.
         * @return              the encoded URL if encoding is needed;
         *                      the unchanged URL otherwise.
         */

        public String encodeRedirectUrl(String url)
        {
                return null;
        }
        /**
         * Encodes the specified URL for use in the
         * <code>sendRedirect</code> method or, if encoding is not needed,
         * returns the URL unchanged.  The implementation of this method
         * includes the logic to determine whether the session ID
         * needs to be encoded in the URL.  Because the rules for making
         * this determination can differ from those used to decide whether to
         * encode a normal link, this method is seperate from the
         * <code>encodeURL</code> method.
         *
         * <p>All URLs sent to the <code>HttpServletResponse.sendRedirect</code>
         * method should be run through this method.  Otherwise, URL
         * rewriting cannot be used with browsers which do not support
         * cookies.
         *
         * @param       url     the url to be encoded.
         * @return              the encoded URL if encoding is needed;
         *                      the unchanged URL otherwise.
         *
         * @see #sendRedirect
         * @see #encodeUrl
         */

        public String encodeRedirectURL(String url)
        {
                return null;
        }
        /**
         * @deprecated  As of version 2.1, use encodeURL(String url) instead
         *
         * @param       url     the url to be encoded.
         * @return              the encoded URL if encoding is needed;
         *                      the unchanged URL otherwise.
         */

        public String encodeUrl(String url)
        {
                return null;
        }
        /**
         * Encodes the specified URL by including the session ID in it,
         * or, if encoding is not needed, returns the URL unchanged.
         * The implementation of this method includes the logic to
         * determine whether the session ID needs to be encoded in the URL.
         * For example, if the browser supports cookies, or session
         * tracking is turned off, URL encoding is unnecessary.
         *
         * <p>For robust session tracking, all URLs emitted by a servlet
         * should be run through this
         * method.  Otherwise, URL rewriting cannot be used with browsers
         * which do not support cookies.
         *
         * @param       url     the url to be encoded.
         * @return              the encoded URL if encoding is needed;
         *                      the unchanged URL otherwise.
         */

        public String encodeURL(String url)
        {
                return null;
        }
        /**
         * Forces any content in the buffer to be written to the client.  A call
         * to this method automatically commits the response, meaning the status
         * code and headers will be written.
         *
         * @see                 #setBufferSize
         * @see                 #getBufferSize
         * @see                 #isCommitted
         * @see                 #reset
         *
         */

        public void flushBuffer() throws IOException
        {
                throw new IOException();
        }
        /**
         * Returns the actual buffer size used for the response.  If no buffering
         * is used, this method returns 0.
         *
         * @return              the actual buffer size used
         *
         * @see                 #setBufferSize
         * @see                 #flushBuffer
         * @see                 #isCommitted
         * @see                 #reset
         *
         */

        public int getBufferSize()
                {
                        return -1;
                }
           /**
         * Returns the name of the charset used for
         * the MIME body sent in this response.
         *
         * <p>If no charset has been assigned, it is implicitly
         * set to <code>ISO-8859-1</code> (<code>Latin-1</code>).
         *
         * <p>See RFC 2047 (http://ds.internic.net/rfc/rfc2045.txt)
         * for more information about character encoding and MIME.
         *
         * @return              a <code>String</code> specifying the
         *                      name of the charset, for
         *                      example, <code>ISO-8859-1</code>
         *
         */

        public String getCharacterEncoding()
        {
                return null;
        }
        /**
         * Returns the locale assigned to the response.
         *
         *
         * @see                 #setLocale
         *
         */

        public Locale getLocale()
        {
                return null;
        }
        /**
         * Returns a {@link ServletOutputStream} suitable for writing binary
         * data in the response. The servlet container does not encode the
         * binary data.  Either this method or {@link #getWriter} may
         * be called to write the body, not both.
         *
         * @return                              a {@link ServletOutputStream} for writing binary data
         *
         * @exception IllegalStateException if the <code>getWriter</code> method
         *                                      has been called on this response
         *
         * @exception IOException               if an input or output exception occurred
         *
         * @see                                 #getWriter
         *
         */

        public ServletOutputStream getOutputStream() throws IOException
    {
        if( this.calledGetWriter )
            throw new IllegalStateException( "The getWriter method has already been called" );

        ServletOutputStream oStream = null;
        if( null == this.servOStream )
            oStream = new ServletOutputStreamSimulator();
        else
            oStream = new ServletOutputStreamSimulator( this.servOStream );
        // resets the status of servOStream to prevent us from possible using a closed stream
        this.servOStream = null;
        this.calledGetOutputStream = true;
        return oStream;
    }
        /**
         * Returns a <code>PrintWriter</code> object that
         * can send character text to the client.
         * The character encoding used is the one specified
         * in the <code>charset=</code> property of the
         * {@link #setContentType} method, which must be called
         * <i>before</i> calling this method for the charset to take effect.
         *
         * <p>If necessary, the MIME type of the response is
         * modified to reflect the character encoding used.
         *
         * <p>Either this method or {@link #getOutputStream} may be called
         * to write the body, not both.
         *
         *
         * @return                              a <code>PrintWriter</code> object that
         *                                      can return character data to the client
         *
         * @exception UnsupportedEncodingException  if the charset specified in
         *                                              <code>setContentType</code> cannot be
         *                                              used
         *
         * @exception IllegalStateException     if the <code>getOutputStream</code>
         *                                              method has already been called for this
         *                                              response object
         *
         * @exception IOException               if an input or output exception occurred
         *
         * @see                                         #getOutputStream
         * @see                                         #setContentType
         *
         */

        public PrintWriter getWriter() throws IOException
        {
        if( this.calledGetOutputStream )
            throw new IllegalStateException( "The getOutputStream method has already been called" );

        if( stringWriter == null )
            stringWriter = new StringWriter();
        if( printWriter == null )
            printWriter = new PrintWriter( stringWriter );

        this.calledGetWriter = true;
                return printWriter;
        }
        /**
         * Use this method to pick up the string buffer which will hold the contents of the string buffer. You can then
         * write your test case to examine the contents of this buffer and match it against an expected output.
         */
        public StringBuffer getWriterBuffer()
        {
                if (stringWriter==null) return null;
                return stringWriter.getBuffer();
        }
        /**
         * Returns a boolean indicating if the response has been
         * committed.  A commited response has already had its status
         * code and headers written.
         *
         * @return              a boolean indicating if the response has been
         *              committed
         *
         * @see                 #setBufferSize
         * @see                 #getBufferSize
         * @see                 #flushBuffer
         * @see                 #reset
         *
         */

        public boolean isCommitted()
        {
                return false;
        }
        /**
         * Clears any data that exists in the buffer as well as the status code and
         * headers.  If the response has been committed, this method throws an
         * <code>IllegalStateException</code>.
         *
         * @exception IllegalStateException  if the response has already been
         *                                   committed
         *
         * @see                 #setBufferSize
         * @see                 #getBufferSize
         * @see                 #flushBuffer
         * @see                 #isCommitted
         *
         */

        public void reset()
        {
        }
                public void resetBuffer()
                {
                }
        /**
         * Sends an error response to the client using the specified
         * status.  The server generally creates the
         * response to look like a normal server error page.
         *
         * <p>If the response has already been committed, this method throws
         * an IllegalStateException.
         * After using this method, the response should be considered
         * to be committed and should not be written to.
         *
         * @param       sc      the error status code
         * @exception   IOException     If an input or output exception occurs
         * @exception   IllegalStateException   If the response was committed
         */

        public void sendError(int sc) throws IOException
        {
            throw new AssertionFailedError("received error: " + sc);
        }
        /**
         * Sends an error response to the client using the specified status
         * code and descriptive message.  The server generally creates the
         * response to look like a normal server error page.
         *
         * <p>If the response has already been committed, this method throws
         * an IllegalStateException.
         * After using this method, the response should be considered
         * to be committed and should not be written to.
         *
         * @param       sc      the error status code
         * @param       msg     the descriptive message
         * @exception   IOException     If an input or output exception occurs
         * @exception   IllegalStateException   If the response was committed
         *                                              before this method call
         */

        public void sendError(int sc, String msg) throws IOException
        {
            throw new AssertionFailedError("recieved error " + sc + " : " + msg);
        }
        /**
         * Sends a temporary redirect response to the client using the
         * specified redirect location URL.  This method can accept relative URLs;
         * the servlet container will convert the relative URL to an absolute URL
         * before sending the response to the client.
         *
         * <p>If the response has already been committed, this method throws
         * an IllegalStateException.
         * After using this method, the response should be considered
         * to be committed and should not be written to.
         *
         * @param               location        the redirect location URL
         * @exception   IOException     If an input or output exception occurs
         * @exception   IllegalStateException   If the response was committed
         */

        public void sendRedirect(String location) throws IOException
        {
                throw new IOException();
        }
        /**
         * Sets the preferred buffer size for the body of the response.
         * The servlet container will use a buffer at least as large as
         * the size requested.  The actual buffer size used can be found
         * using <code>getBufferSize</code>.
         *
         * <p>A larger buffer allows more content to be written before anything is
         * actually sent, thus providing the servlet with more time to set
         * appropriate status codes and headers.  A smaller buffer decreases
         * server memory load and allows the client to start receiving data more
         * quickly.
         *
         * <p>This method must be called before any response body content is
         * written; if content has been written, this method throws an
         * <code>IllegalStateException</code>.
         *
         * @param size  the preferred buffer size
         *
         * @exception  IllegalStateException    if this method is called after
         *                                              content has been written
         *
         * @see                 #getBufferSize
         * @see                 #flushBuffer
         * @see                 #isCommitted
         * @see                 #reset
         *
         */

        public void setBufferSize(int size)
        {
        }
        /**
         * Sets the length of the content body in the response
         * In HTTP servlets, this method sets the HTTP Content-Length header.
         *
         *
         * @param len   an integer specifying the length of the
         *                      content being returned to the client; sets
         *                      the Content-Length header
         *
         */

        public void setContentLength(int len)
        {

        }
        /**
         * Sets the content type of the response being sent to
         * the client. The content type may include the type of character
         * encoding used, for example, <code>text/html; charset=ISO-8859-4</code>.
         *
         * <p>If obtaining a <code>PrintWriter</code>, this method should be
         * called first.
         *
         *
         * @param type  a <code>String</code> specifying the MIME
         *                      type of the content
         *
         * @see                 #getOutputStream
         * @see                 #getWriter
         *
         */

        public void setContentType(String type)
        {

        }
        /**
         *
         * Sets a response header with the given name and
         * date-value.  The date is specified in terms of
         * milliseconds since the epoch.  If the header had already
         * been set, the new value overwrites the previous one.  The
         * <code>containsHeader</code> method can be used to test for the
         * presence of a header before setting its value.
         *
         * @param       name    the name of the header to set
         * @param       value   the assigned date value
         *
         * @see #containsHeader
         * @see #addDateHeader
         */

        public void setDateHeader(String name, long date)
        {
        }
        /**
         *
         * Sets a response header with the given name and value.
         * If the header had already been set, the new value overwrites the
         * previous one.  The <code>containsHeader</code> method can be
         * used to test for the presence of a header before setting its
         * value.
         *
         * @param       name    the name of the header
         * @param       value   the header value
         *
         * @see #containsHeader
         * @see #addHeader
         */

        public void setHeader(String name, String value)
        {
        }
        /**
         * Sets a response header with the given name and
         * integer value.  If the header had already been set, the new value
         * overwrites the previous one.  The <code>containsHeader</code>
         * method can be used to test for the presence of a header before
         * setting its value.
         *
         * @param       name    the name of the header
         * @param       value   the assigned integer value
         *
         * @see #containsHeader
         * @see #addIntHeader
         */

        public void setIntHeader(String name, int value)
        {
        }
        /**
         * Sets the locale of the response, setting the headers (including the
         * Content-Type's charset) as appropriate.  This method should be called
         * before a call to {@link #getWriter}.  By default, the response locale
         * is the default locale for the server.
         *
         * @param loc  the locale of the response
         *
         * @see                 #getLocale
         *
         */

        public void setLocale(Locale loc)
        {
        }
    /**
     * The default action of calling the <code>getOutputStream</code> method is to return a
     * <code>javax.servlet.ServletOutputStream</code> object that sends the data to <code>
     * System.out</code>.  If you don't want the output sent to <code>System.out</code>
     * you can use this method to set where the output will go.  Please note, subsequent
     * calls to <code>getOutputStream</code> will reset the output path to <code>System.out</code>.
     * This prevents the OutputStream returned by calling getOutputStream from writing to a closed
     * stream
     *
     * @param   out     The <code>java.io.OutputStream</code> that represents the real path of
     *                  the output.
     */
    public void setOutputStream( OutputStream out )
    {
        this.servOStream = out;
    }
        /**
         * Sets the status code for this response.  This method is used to
         * set the return status code when there is no error (for example,
         * for the status codes SC_OK or SC_MOVED_TEMPORARILY).  If there
         * is an error, the <code>sendError</code> method should be used
         * instead.
         *
         * @param       sc      the status code
         *
         * @see #sendError
         */

        public void setStatus(int sc)
        {
        }
        /**
         * @deprecated As of version 2.1, due to ambiguous meaning of the
         * message parameter. To set a status code
         * use <code>setStatus(int)</code>, to send an error with a description
         * use <code>sendError(int, String)</code>.
         *
         * Sets the status code and message for this response.
         *
         * @param       sc      the status code
         * @param       sm      the status message
         */

        public void setStatus(int sc, String sm)
        {
        }
}
