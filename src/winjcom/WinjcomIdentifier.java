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
import java.util.*;

/**
 * To get a port you instantiate this class and then call the getCommPort()
 * Once you have a port you can open it and then you can read and write.
 * 
 * When the native library is unavailable, the constructor for
 * instances of this class will throw the exception thrown by the
 * system load library call (security or unsatisfied link exceptions).
 * 
 * @author Damiano Bolla
 * @author John Pritchard 
 */
public class WinjcomIdentifier extends CommPortIdentifier
{
    private static final String classname = "WinjcomIdentifier.";

    public static final String  jcomNativeLibrary = "winjcom";
  
    // this debug mask is used even in the native part of the driver
    // if you change the values you MUST recreate the dll.
    public static final int DEBUG_INIT       = 0x000000001;
    public static final int DEBUG_TEST_PORT	 = 0x000000002;
    public static final int DEBUG_ERRORS	 = 0x000000004;    
    public static final int DEBUG_OPEN_PORT  = 0x000000008;   
    public static final int DEBUG_WRITE      = 0x000000010;   
    public static final int DEBUG_READ       = 0x000000020;   
    public static final int DEBUG_COMMEVENT  = 0x000000040;   
    public static final int DEBUG_POPARAMS   = 0x000000080;   
    public static final int DEBUG_CLOSE_PORT = 0x000000100;

    private int debugMask;
  
    /**
     * You start with this one
     * You can enable debugging mask if you wish so.
     * @param debugMask a debug mask taken from the above constants. Zero means no debug
     */
    public WinjcomIdentifier (int debugMask)
    {
        this.debugMask = debugMask;

        if (mustLog(DEBUG_INIT) ) report (classname+"initialize native lib=" + jcomNativeLibrary);

        // load native library
        System.loadLibrary(jcomNativeLibrary);
    
        // Init the native part of the library
        nativeInitialize(debugMask);
    }
    
    private boolean mustLog ( int mask )
    {
        return (debugMask & mask) != 0;
    }
    
    private void report ( String msg )
    {
        System.out.println(msg);
    }

    private native void nativeInitialize(int debugMaks);

    /**
     * Note that his one does NOT leave the port "open"
     * If test is OK then it returns true, false + exception othervise
     * @param dev
     * @throws IOException
     */
    private native boolean nativeIsPortPresent(String dev) throws IOException;

    private boolean isPortPresent( String portName )
    {
        try {
            // this will throw exception if this port is not available.
            return nativeIsPortPresent(portName);
        }
        catch ( Exception exc ){
            if (mustLog(DEBUG_TEST_PORT)) report (classname+"isPortPresent() port="+portName+" exc="+exc);
            return false;
        }
    }


    /**
     * See the API description for documentation.
     * @param portName
     * @return usable com port attached to the given name
     */
    public CommPort getCommPort(String portName)
    {
        return new WinjcomPort(portName,debugMask);
    }

    /**
     * See at API description for documentation.
     * Windows com port are named COM1 .... COM9 for the first 9 ports then Billgates
     * decided that it was too easy and the following ones will be named \\.\com10
     * Do not ask me why, but this is how it is
     * @return a List of CommPort present on the system.
     */
    public List<CommPort> getCommPortList()
    {
        ArrayList<CommPort> risul = new ArrayList<CommPort>();
    
        for (int index=1; index<10; index++){
            String portName = "COM"+index;
      
            if ( !isPortPresent(portName)) continue;
      
            risul.add(getCommPort(portName));
        }
    
        return risul;
    }
}
