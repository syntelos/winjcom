package winjcom;

import java.io.IOException;
import java.io.InputStream;

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
 * If you want to have an input stream instead of read you can just
 * new PortInputStream(commPort) and you can have an input stream.
 */
public class PortInputStream extends InputStream
  {
  private final CommPort commPort;
  
  public PortInputStream ( CommPort commPort )
    {
    this.commPort = commPort;
    }
    
  public int read() throws IOException
    {
    byte []risul = new byte[1];

    int letti = read(risul);

    if ( letti == 1 ) return risul[0] & 0x00FF;
    
    return letti;
    }

  public int read(byte[] buff) throws IOException
    {
    return read(buff, 0, buff.length );
    }

  public int read(byte[] buff, int offset, int len) throws IOException
    { 
    int letti = commPort.read( buff, offset, len );
    
    if ( letti != len )
      throw new TimeoutException("Timeout len="+len+" letti="+letti);
    
    return letti;
    }

  public synchronized int available() throws IOException
    {   
    return commPort.available();
    }
  }
