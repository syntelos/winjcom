package winjcom;
import java.util.*;

/**
 * If an Object wants to listen to serial events it shouldimplement
 * this interface and then attash itseld to the port using the 
 * public abstract void setEventListener(SerialPortEventListener lsnr)
 */
public interface SerialPortEventListener extends EventListener
  {
  public abstract void serialEvent( SerialPortEvent ev );
  }
