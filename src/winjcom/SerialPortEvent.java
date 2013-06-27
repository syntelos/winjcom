package winjcom;

import java.util.*;

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
 * If you ever want to know what was the previous state of something just save it in your code.
 * If you ever want to know what is the current state of something use one of the
 * isXXXX() The whole idea is Keep It Simple !!!
 */
public class SerialPortEvent extends EventObject
  {
  public static final int DATA_AVAILABLE = 1;
  public static final int OUTPUT_BUFFER_EMPTY = 2;
  public static final int CTS = 3;
  public static final int DSR = 4;
  public static final int RI  = 5;
  public static final int CD  = 6;
  public static final int OE  = 7;
  public static final int PE  = 8;
  public static final int FE  = 9;
  public static final int BI  = 10;

  private int eventType;

  public SerialPortEvent( SerialPort srcport, int eventtype )
    {
    super(srcport);
    this.eventType = eventtype;
    }

  public int getEventType()
    {
    return eventType;
    }
    
  public String getEventTypeString ()
    {
    switch ( eventType )  
      {
      case DATA_AVAILABLE: return "RxData";
      case OUTPUT_BUFFER_EMPTY: return "TxEmpty";
      case CTS: return "CTS";
      case DSR: return "DSR";
      case RI:  return "RI";
      case CD:  return "CD";
      case OE:  return "OE";
      case PE:  return "PE";
      case FE:  return "FE";
      case BI:  return "BI";
      default:  return "Unknown";
      }
    }
    
  public String toString()
    {
    return super.toString()+" "+getEventTypeString();
    }
  }
