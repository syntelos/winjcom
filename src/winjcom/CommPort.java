package winjcom;


import java.io.*;

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
 * The main difference are the method read and write.
 * They are needed since it may happen that a port returns less bytes than wanted or writes less
 * bytes that you are expected to write. read and write tells you this.
 * If you want the standard interface just use PortInputStream and PortOutputStream
 * NOTE: Basically all methods return either IOexception or UnsupportedEsception, this
 * is really by design since IO si a messy business and you MUST NOT assume things go smooth and nice.
 * methods may fail for a multitude of reasons, loking, ownership, timeout, BUGS, you HAVE
 * to take care of exception either in the form or reopen or in user notificatino.
 */
public abstract class CommPort 
  {
  protected final String portName;
  
  /**
   * Constructor, needed since portName is final.
   * @param portName
   */
  public CommPort ( String portName )
    {
    this.portName = portName;
    }
    
  /**
   * Accessor for portName
   * @return the name of the port as set in costructor
   */
  public String getName()
    {
    return portName;
    }

  public abstract void open() throws IOException;
  
  public abstract void close();

  /**
   * Port instances are allowed to read less chars than requested at their wish.
   * Expect this to throw exception on timeout or alternatively to return zero bytes.
   * @param buff
   * @param offset
   * @param len 
   * @return the number of bytes read, only -1 means EOF, zero is a valid return
   * @throws IOException
   */
  public abstract int read ( byte[] buff, int offset, int len) throws IOException;  

  /**
   * Port instances are allowed to write less chars than requested.
   * Expect this to throw exception on timeout or alternatively to return zero bytes.
   * @param buff
   * @param offset
   * @param len
   * @return the number of bytes written, only -1 means EOF, Zero is a valid return
   * @throws IOException
   */
  public abstract int write ( byte[]buff, int offset, int len ) throws IOException;

  /**
   * Return the number of bytes available on read.
   * @return the number of bytes available to read
   * @throws IOException
   */
  public abstract int available() throws IOException;
  
  /**
   * This THROWS AWAY any char that is in the input or output queue !!!!
   * @throws IOException
   */
  public abstract void flush() throws IOException;






  public abstract void enableReceiveFraming(int f) throws IOException;

  public abstract void disableReceiveFraming() throws IOException;

  public abstract boolean isReceiveFramingEnabled() throws IOException;

  public abstract int getReceiveFramingByte() throws IOException;

  public abstract void disableReceiveTimeout() throws IOException;

  public abstract void enableReceiveTimeout(int timeoutMilli) throws IOException;

  public abstract boolean isReceiveTimeoutEnabled() throws IOException;

  public abstract int getReceiveTimeout() throws IOException;

  public abstract void enableReceiveThreshold(int thresh) throws IOException;

  public abstract void disableReceiveThreshold() throws IOException;

  public abstract int getReceiveThreshold() throws IOException;

  public abstract boolean isReceiveThresholdEnabled() throws IOException;

  public abstract void setInputBufferSize(int size) throws IOException;

  public abstract int getInputBufferSize() throws IOException;

  public abstract void setOutputBufferSize(int size) throws IOException;

  public abstract int getOutputBufferSize() throws IOException;

  public String toString()
    {
    return portName;
    }
  }
