package winjcom;

import java.io.IOException;
import java.io.OutputStream;

/*
  WinJcom is a native interface to serial ports in java.
  Copyright 2007 by Damiano Bolla, Jcomm@engidea.com

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Library General Public
  License as published by the Free Software Foundation; either
  version 2 of the License, or (at your option) any later version.
  This can be used with commercial products and you are not obliged
  to share your work with anybody.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Library General Public License for more details.

  You should have received a copy of the GNU Library General Public
  License along with this library; if not, write to the Free
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

/**
 * If you want to have an output stream instead of write you can just
 * new PortOutputStream(commPort) and you can have an output stream.
 */
public class PortOutputStream extends OutputStream
  {
  private final CommPort commPort;
  
  public PortOutputStream (CommPort commPort)
    {
    this.commPort = commPort;
    }
    
  public void write(int val) throws IOException
    {
    byte []value = new byte[1];

    value[0] = (byte)val;
    
    write(value);
    }

  public void write(byte[] buff) throws IOException
    {
    write(buff, 0, buff.length);
    }

  public void write(byte[] buff, int offset, int len) throws IOException
    {
    int written = commPort.write( buff, offset, len);
    
    if ( written != len )
      throw new TimeoutException("Timeout len="+len+" written="+written);
    }

  public void flush() throws IOException
    {
    commPort.flush();
    }
  }
