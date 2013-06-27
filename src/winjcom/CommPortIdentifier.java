package winjcom;

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
 * Devices are obtained from concrete instance of this class.
 * A real device driver provides you with a class that extends/implements this abstract that provides with a 
 * means to obtain a port. As an example a concrete class for this abstrac class can be WinjcomIdentifier,
 * other driver writes may create WinparallelIdentifier and so on so forth.
 * Of course you end up with many differnt Class names, but this would have happened anyway since the development
 * of this is not centralized. SOmeone could always end up with a GlobalportIdentifier that is able to 
 * scan all ports and do automagic things, then good, we will use that, then :-)
 * When you new this class the native library is loaded and initialized.
 */
public abstract class CommPortIdentifier
  {
  /**
   * Given a port name will return the CommPort that you can use to do IO.
   * Note that the given CommPort is NOT open, yet, and only when you try to open it
   * you can discover if it is really present or if somebody else has it already opened.
   * NOTE: This must NOT rely on the fact that Java has "discovered" the list of ports since
   * the discovery mechanism may fail and there should be a way for the user to open the port anyway !
   * This is the preferred way to get a CommPort, but you may be able to get it in other ways
   * if the implementation allows you.
   * NOTE: The returned CommPort is most likely always a new port, in other words do NOT rely
   * on the fact that you get the same object given the same name.
   * @param portName
   * @return a CommPort that you can then try to open
   */
  public abstract CommPort getCommPort(String portName);

  /**
   * Each time you call this one the driver will try to scan the system again for the 
   * ports that are attached. This is so you can rediscover ports that have been attached
   * at runtime. Note however that the discovery mechanism may as well fail (not find an available port)
   * and you can always use the getCommPort to get such " hidden" ports.
   * @return a List of CommPort objects present in the system.
   */
  public abstract List getCommPortList();
  }

