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
// This class was contributed by Dane Foster
// Dane S. Foster
// Equity Technology Group, Inc
// http://www.equitytg.com.
// 954.360.9800
// dfoster@equitytg.com

import javax.servlet.ServletOutputStream;
import java.io.*;

public class ServletOutputStreamSimulator extends ServletOutputStream
{
    private OutputStream outStream;
    /**
     * The default consturctor.<p>
     * When the default constructor is used all the content written
     * by <code>this</code> stream is sent to <code>java.lang.System.out</code>
     */
    public ServletOutputStreamSimulator()
    {
        this.outStream = System.out;
    }
    /**
     * @param   out The output of <code>this</code> ServletOutputStream goes here.
     */
    public ServletOutputStreamSimulator( OutputStream out )
    {
        this.outStream = out;
    }
    public void write( int b )
    {
        try
        {
            outStream.write( b );
        }
        catch( IOException io )
        {
            io.printStackTrace();
        }
    }
}
