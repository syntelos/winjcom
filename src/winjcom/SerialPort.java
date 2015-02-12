/*
 * WinJcom is a native interface to serial ports in java.
 * Copyright 2007 by Damiano Bolla, Jcomm@engidea.com
 * Copyright 2015 John Pritchard, jdp@syntelos.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This can be used with commercial products and you are not obliged 
 * to share your work with anybody.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package winjcom;

import java.io.IOException;

/**
 * Defines a generic serial port, concrete implementation may have more methods
 * but this should be fairly general.
 * 
 * @author Damiano Bolla
 * @author John Pritchard 
 */
public abstract class SerialPort extends CommPort
{

    /**
     * COnstructor needed since portName is final
     * @param portName
     */
    public SerialPort ( String portName )
    {
        super(portName);
    }
    
    public static final int DATABITS_5 = 5;
    public static final int DATABITS_6 = 6;
    public static final int DATABITS_7 = 7;
    public static final int DATABITS_8 = 8;

    public static final int PARITY_NONE = 0;
    public static final int PARITY_ODD = 1;
    public static final int PARITY_EVEN = 2;
    public static final int PARITY_MARK = 3;
    public static final int PARITY_SPACE = 4;

    public static final int STOPBITS_1 = 1;
    public static final int STOPBITS_2 = 2;
    public static final int STOPBITS_1_5 = 3;
  
  
    public static final int FLOWCONTROL_NONE        = 0;
    public static final int FLOWCONTROL_RTSCTS_IN   = 1;
    public static final int FLOWCONTROL_RTSCTS_OUT  = 2;
    public static final int FLOWCONTROL_XONXOFF_IN  = 4;
    public static final int FLOWCONTROL_XONXOFF_OUT = 8;

    public abstract void setSerialPortParams
        (int baudrate, int databits, int stopbits, int parity) throws IOException;

    public abstract int getBaudRate() throws IOException;

    public abstract int getDataBits() throws IOException;

    public abstract int getStopBits() throws IOException;

    public abstract int getParity() throws IOException;

    public abstract void setFlowControlMode(int flowcontrol) throws IOException;

    public abstract int getFlowControlMode() throws IOException;

    public abstract boolean isDTR() throws IOException;

    public abstract void setDTR(boolean state) throws IOException;

    public abstract void setRTS(boolean state) throws IOException;

    public abstract boolean isCTS() throws IOException;

    public abstract boolean isDSR() throws IOException;

    public abstract boolean isCD() throws IOException ;

    public abstract boolean isRI() throws IOException;

    public abstract boolean isRTS() throws IOException;

    public abstract void sendBreak(int duration) throws InterruptedException, IOException;

    public abstract void setEventListener(SerialPortEventListener lsnr);

    public abstract void notifyOnDataAvailable(boolean enable)throws IOException;

    public abstract void notifyOnOutputEmpty(boolean enable)throws IOException;

    public abstract void notifyOnCTS(boolean enable)throws IOException;

    public abstract void notifyOnDSR(boolean enable)throws IOException;

    public abstract void notifyOnRingIndicator(boolean enable)throws IOException;

    public abstract void notifyOnCarrierDetect(boolean enable)throws IOException;

    public abstract void notifyOnOverrunError(boolean enable)throws IOException;

    public abstract void notifyOnParityError(boolean enable)throws IOException;

    public abstract void notifyOnFramingError(boolean enable)throws IOException;

    public abstract void notifyOnBreakInterrupt(boolean enable)throws IOException;
}
