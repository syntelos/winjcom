package winjcom;

import java.io.*;
import java.util.ArrayList;


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


public final class WinjcomPort extends SerialPort
  {
  private static final String classname="WinjcomPort.";

  private final int debugMask;

  // they are volatile since there is the monitor thread that uses them.
  private volatile SerialPortEventListener portListener;
  private volatile int portFd = -1;


  /**
   * Constructor.
   * It is package private so you have to use the getCommPort method of 
   * WinjportIdentifier to get the port.
   * @param portName
   */
  WinjcomPort( String portName, int debugMask )
    {
    super(portName);
    this.debugMask = debugMask;
    }


  private native int nativeOpen(String name) throws IOException;

  /**
   * Opens the port to be used for work.
   * If you want to receive events right at port opening, you should set the listener
   * before opening theport.
   * @throws IOException
   */
  public void open () throws IOException
    {
    portFd = nativeOpen(portName);

    // You need to set at least one event...
    nativeNotifyEnable(portFd,EV_RING,true);

    if (mustLog(WinjcomIdentifier.DEBUG_OPEN_PORT) ) report (classname+"open() openFd="+portFd);
    
    Thread monitorThread = new Thread(new MonitorThread());
    monitorThread.setName(classname+"Monitor("+portName+")");
    monitorThread.setDaemon(true);
    monitorThread.start();
    }


  private boolean mustLog ( int mask )
    {
    return (debugMask & mask) != 0;
    }

  private void report ( String msg )
    {
    System.out.println(msg);
    }

  private native void nativeSetSerialPortParams (
    int portFd,
    int baudrate, 
    int dataBits, 
    int stopBits, 
    int parity) throws IOException;


  public synchronized void setSerialPortParams(
    int baudrate, 
    int databits, 
    int stopbits, 
    int parity) throws IOException
    {
    nativeSetSerialPortParams(portFd,baudrate,databits,stopbits,parity);
    }


  /**
   * Native params are all accessed using a single native call.
   * param type is defined here as a constant.
   * @param paramType
   * @return
   */
  private static final int DCB_BAUDRATE    = 1;   
  private static final int DCB_DATABITS    = 2;
  private static final int DCB_STOPBITS    = 3;
  private static final int DCB_PARITY      = 4;
  private static final int DCB_FLOWCONTROL = 5;

  private native int nativeGetPortParam (int portFd, int paramType)throws IOException;
  
  public int getBaudRate()throws IOException
    {
    return nativeGetPortParam(portFd, DCB_BAUDRATE);
    }


  public int getDataBits()throws IOException
    {
    return nativeGetPortParam(portFd, DCB_DATABITS);
    }

  public int getStopBits()throws IOException
    {
    return nativeGetPortParam(portFd, DCB_STOPBITS);
    }

  public int getParity()throws IOException
    {
    return nativeGetPortParam(portFd, DCB_PARITY);
    }

  public int getFlowControlMode()throws IOException
    {
    return nativeGetPortParam(portFd, DCB_FLOWCONTROL);
    }


  /**
   * Note that what to tinker with is one of DCB_ defined in here
   * @param portFd
   * @param what    one of DCB_ defined in here
   * @param value   depends on what you want to set...
   */
  private native void nativeSetPortParam ( int portFd, int what, int value )throws IOException;

  /**
   * The param is one of FLOWCONTROL_ defined in SerialPort
   * @param flowcontrol
   */
  public void setFlowControlMode(int flowcontrol)throws IOException
    {
    nativeSetPortParam(portFd,DCB_FLOWCONTROL, flowcontrol);
    }

  public void enableReceiveFraming(int f) throws IOException
    {
    throw new UnsupportedException("Unsupported");
    }

  public void disableReceiveFraming()throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }

  public boolean isReceiveFramingEnabled()throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }

  public int getReceiveFramingByte()throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }


  /**
   * ALl values are in milliseconds.
   * @param portFd
   * @param reInTi
   * @param reToTiMu
   * @param reToTiCo
   * @param wrToTiMu
   * @param wrToTiCo
   */
  private native void nativeSetCommTimeouts ( 
    int portFd, 
    int reInTi, 
    int reToTiMu, 
    int reToTiCo,
    int wrToTiMu,
    int wrToTiCo )throws IOException;

  public void disableReceiveTimeout()throws IOException
    {
    nativeSetCommTimeouts(portFd, 0,0,0, 0,0);
    }

  public void enableReceiveTimeout(int timeoutMilli)throws IOException
    {
    // This sets a timeout between chars as much as a total timeout
    // it also sets the SAME timeout for both read and write.
    nativeSetCommTimeouts(portFd, timeoutMilli,0,timeoutMilli, 0,timeoutMilli);
    }

  private native int nativeGetReceiveTotalTimeoutConstant ( int portFd )throws IOException;
  
  public boolean isReceiveTimeoutEnabled()throws IOException
    {
    return nativeGetReceiveTotalTimeoutConstant ( portFd ) != 0;
    }

  public int getReceiveTimeout()throws IOException
    {    
    return nativeGetReceiveTotalTimeoutConstant ( portFd );
    }

  public void enableReceiveThreshold(int thresh)throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }

  public void disableReceiveThreshold()throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }

  public int getReceiveThreshold()throws IOException
    {    
    throw new UnsupportedException("Not implemented");
    }

  public boolean isReceiveThresholdEnabled()throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }


  public void setInputBufferSize(int size)throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }

  public int getInputBufferSize()throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }

  public void setOutputBufferSize(int size)throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }

  public int getOutputBufferSize()throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }

  public boolean isDTR()throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }


  /**
   * The params are taken directly from Win 
   * @param portFd
   * @param what
   */
  private native void nativeSendEscape ( int portFd, int what )throws IOException;
  
  private static final int ESC_SETRTS=3;  // As from windows !
  private static final int ESC_CLRRTS=4;  // As from windows !
  private static final int ESC_SETDTR=5;  // As from windows !
  private static final int ESC_CLRDTR=6;  // As from windows !
  private static final int ESC_SETBREAK=8;  // As from windows !
  private static final int ESC_CLRBREAk=9;  // As from windows !

  public void  setDTR(boolean state)throws IOException
    {
    int what = state ? ESC_SETDTR : ESC_CLRDTR;
    
    nativeSendEscape (portFd, what);
    }

  public void  setRTS(boolean state)throws IOException
    {
    int what = state ? ESC_SETRTS : ESC_CLRRTS;
    
    nativeSendEscape (portFd, what);
    }

  public  void sendBreak(int duration) throws InterruptedException, IOException
    {
    nativeSendEscape (portFd, ESC_SETBREAK);
    Thread.currentThread().sleep(duration);
    nativeSendEscape (portFd, ESC_CLRBREAk);
    }


  private static final int MS_CTS_ON=16;
  private static final int MS_DSR_ON=32;
  private static final int MS_RING_ON=64;
  private static final int MS_RLSD_ON=128;
  
  private native int nativeGetModemStatus ( int fd ) throws IOException;

  public  boolean isCTS()throws IOException
    {
    return (nativeGetModemStatus(portFd) & MS_CTS_ON) != 0;
    }

  public  boolean isDSR()throws IOException
    {
    return (nativeGetModemStatus(portFd) & MS_DSR_ON) != 0;
    }

  public  boolean isCD()throws IOException
    {
    return (nativeGetModemStatus(portFd) & MS_RLSD_ON) != 0;
    }
    
  public  boolean isRI()throws IOException
    {
    return (nativeGetModemStatus(portFd) & MS_RING_ON) != 0;
    }

  public  boolean isRTS() throws IOException
    {
    throw new UnsupportedException("Not implemented");
    }

  /**
   * Set the listener to be used, if null then it just clears the listener.
   * @param portListener
   */
  public void setEventListener(SerialPortEventListener portListener)
    {
    this.portListener = portListener;
    }


  private static final int EV_RXCHAR  = 0x0001;  // Any Character received
  private static final int EV_CTS     = 0x0008;  // CTS received
  private static final int EV_TXEMPTY = 0x0004;  // Notify on output empty
  private static final int EV_DSR     = 0x0010;  // DSR changed state
  private static final int EV_BREAK   = 0x0040;  // BREAK received
  private static final int EV_ERR     = 0x0080;  // Line status error occurred
  private static final int EV_RING    = 0x0100;  // Ring signal detected
  private static final int EV_RLSD    = 0x0020;  // The RLSD (receive-line-signal-detect) signal changed state


  /**
   * Enable or disable notification.
   * You MUST call this one with dummy params at the beginning to setup things properly.
   * @param portFd
   * @param what    a value as defined in SetCommMask, keep them the same...
   * @param enable
   * @throws IOException
   */
  private native void nativeNotifyEnable ( int portFd, int what, boolean enable );

  public void notifyOnDataAvailable(boolean enable)   
    {   
    nativeNotifyEnable(portFd,EV_RXCHAR,enable);   
    }
    
  public void notifyOnOutputEmpty(boolean enable)     
    {   
    nativeNotifyEnable(portFd,EV_TXEMPTY,enable);  
    }

  public void notifyOnCTS(boolean enable)             
    {   
    nativeNotifyEnable(portFd,EV_CTS,enable);      
    }
    
  public void notifyOnDSR(boolean enable)             
    {   
    nativeNotifyEnable(portFd,EV_DSR,enable);      
    }
    
  public void notifyOnRingIndicator(boolean enable)   
    {   
    nativeNotifyEnable(portFd,EV_RING,enable);     
    }
    
  public void notifyOnCarrierDetect(boolean enable)   
    {   
    nativeNotifyEnable(portFd,EV_RLSD,enable);     
    }
    
  public void notifyOnOverrunError(boolean enable)    
    {   
    nativeNotifyEnable(portFd,EV_ERR,enable);      
    }
    
  public void notifyOnParityError(boolean enable)     
    {   
    nativeNotifyEnable(portFd,EV_ERR,enable);      
    }
    
  public void notifyOnFramingError(boolean enable)    
    {   
    nativeNotifyEnable(portFd,EV_ERR,enable);      
    }
  
  public void notifyOnBreakInterrupt(boolean enable)  
    {   
    nativeNotifyEnable(portFd,EV_BREAK,enable);    
    }


  private native int nativeWriteArray ( int portFd, byte[]buff, int offset, int len ) throws IOException;
  
  private native void nativePurgeComm ( int portFd ) throws IOException;

  public int write ( byte[]buff, int offset, int len ) throws IOException
    {
    return nativeWriteArray(portFd,buff,offset,len);
    }
    
  public void flush() throws IOException
    {
    nativePurgeComm(portFd);  
    }
    



  private static final int COMSTAT_cbInQue = 1;   // Number of bytes received but not yet read

  private native int nativeClearCommError( int portFd, int what ) throws IOException;;

  private native int nativeReadArray (int portFd, byte[] buff, int offset, int len) throws IOException;

  public int read ( byte[] buff, int offset, int len) throws IOException
    {
    return nativeReadArray(portFd,buff,offset,len);
    }

  public int available () throws IOException
    {
    return nativeClearCommError(portFd, COMSTAT_cbInQue);
    }





  private native void nativeClose(int portFd) throws IOException;

  /**
   * Close does not throw exception since there is no receovery from it.
   * But I print it out, just in case someone wants to notice...
   */
  public synchronized void close() 
    {
    try
      {
      nativeClose(portFd);
      // the event thread just dies since I have closed the data stream.
      }
    catch ( Exception exc )
      {
      if (mustLog(WinjcomIdentifier.DEBUG_CLOSE_PORT) )        
        {
        report(classname+"close() Exc="+exc);
        exc.printStackTrace();
        }
      }
      
    portFd = -1;
    }


  /**
   * Ideally the program MUST call close, but if it does not this is a chance to recover.
   * Of course you never know when GC is run...
   */
  public void finalize()
    {
    if (portFd < 0) return;
    
    report("finalize:calling close()");
  
    close();
    }

  /**
   * What is probably useful is to have the port this driver is bound to.
   * @return human readable information for this object
   */
  public String toString()
    {
    return classname+" "+portName;
    }

  
private native int nativeGetCommEvent (int portFd) throws IOException;

private class MonitorThread implements Runnable
    {
    private void waitForEvent () throws IOException
      {
      int eventMask = nativeGetCommEvent(portFd);

      if ( portListener == null ) return;
      
      if ( (eventMask & EV_CTS) != 0 ) 
        portListener.serialEvent(new SerialPortEvent(WinjcomPort.this, SerialPortEvent.CTS));
        
      if ( (eventMask & EV_RING) != 0 ) 
        portListener.serialEvent(new SerialPortEvent(WinjcomPort.this, SerialPortEvent.RI));
        
      if ( (eventMask & EV_DSR) != 0 ) 
        portListener.serialEvent(new SerialPortEvent(WinjcomPort.this, SerialPortEvent.DSR));
        
      if ( (eventMask & EV_RLSD) != 0 ) 
        portListener.serialEvent(new SerialPortEvent(WinjcomPort.this, SerialPortEvent.CD));

      if ( (eventMask & EV_TXEMPTY) != 0 ) 
        portListener.serialEvent(new SerialPortEvent(WinjcomPort.this, SerialPortEvent.OUTPUT_BUFFER_EMPTY));

      if ( (eventMask & EV_RXCHAR) != 0 ) 
        portListener.serialEvent(new SerialPortEvent(WinjcomPort.this, SerialPortEvent.DATA_AVAILABLE));
      }

    public void run()
      {
      if (mustLog(WinjcomIdentifier.DEBUG_COMMEVENT) ) report("MonitorThread: port("+portName+") start");

      try
        {
        for(;;) waitForEvent();
        }
      catch ( Exception exc )
        {
        // Monitor thread exit when the port is closed
        // The result is an exception that I could catch or just ignore...
        if (mustLog(WinjcomIdentifier.DEBUG_COMMEVENT) )
          report("MonitorThread: port("+portName+") Exception="+exc);
        }

      if (mustLog(WinjcomIdentifier.DEBUG_COMMEVENT) )
        report("MonitorThread: port("+portName+") exit");
      }

    }


}
