package winjcom;

import java.io.*;
import java.util.Iterator;
import java.util.List;

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
 * Simple class that can list system ports and allow IO
 */
public class PortTester 
  {
  public static void main(String[] args) 
    {
    PortTester portTester = new PortTester();
    portTester.StartTest();
    }


  private CommPortIdentifier portIdentifier;
  private SerialPort serport;

  private void report ( String msg )
    {
    System.out.println(msg);
    }
    
  private void StartTest()
    {
    portIdentifier = new WinjcomIdentifier(0);
    listComPorts();
    selectComport();
    new Thread(new PortReader()).start();
    typeSendBytes();
    }

  private void typeSendBytes()
    {  
    try
      {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      String aStr = "";
      while (aStr != null) 
        {
        System.out.print("type something to send> ");
        aStr = in.readLine();
        aStr += "\r\n";
        // WARNING: be careful, you shoulod select the encoding !
        // This will timeout if you have FLOW CONTROL and theline is stuck !
        byte []buf = aStr.getBytes();
        serport.write(buf,0,buf.length );
        }
      }
    catch ( Exception exc )
      {
      exc.printStackTrace();
      }
    }
    
  private void listComPorts ()
    {
    List portlist = portIdentifier.getCommPortList();
    for (Iterator iter=portlist.iterator(); iter.hasNext(); )
      {
      CommPort identifier = (CommPort)iter.next();
      System.out.println("Available port name="+identifier.getName());
      }
    }

  private SerialPort openPort ( String portName )
    {
    try 
      {
      CommPort aPort = portIdentifier.getCommPort(portName);
      aPort.open();
      return (SerialPort)aPort;
      }
    catch ( Exception exc )
      {
      report("exc="+exc);
      exc.printStackTrace();
      }
      
    return null;
    }
    
  private void selectComport ()
    {
    try 
      {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      System.out.print("type port name> ");
      String portname = in.readLine();
      serport = openPort(portname);
      serport.setSerialPortParams(9600,8, SerialPort.STOPBITS_2, SerialPort.PARITY_NONE);
      serport.enableReceiveTimeout(20000);
      serport.setEventListener(new EventListener());

      serport.notifyOnDSR(true);
      serport.notifyOnCarrierDetect(true);
      serport.notifyOnCTS(true);
//      serport.notifyOnOutputEmpty(true);
      } 
    catch (IOException exc) 
      {
      report("Exc="+exc);
      exc.printStackTrace();
      }
    }


private final class EventListener implements SerialPortEventListener
  {
  public void serialEvent(SerialPortEvent ev)
    {
    System.out.println("Got event="+ev);
    }
  }


private final class PortReader implements Runnable
  {
  public void run()
    {
    try
      {
      // This will timeout if nothing is received in the specified time.
      byte []buff = new byte[1];
      while (  serport.read(buff,0,buff.length) > 0 )
        {
        // NOTE: you should be checking the encoding !
        System.out.print(new String(buff));
        }
      }
    catch ( Exception exc )
      {
      exc.printStackTrace();
      }
    }
  }


}
