/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */
package se.metamatrix.mimedir;

import java.util.Date;

/**
 * The class ResDate is a subclass of the java.util.Date class that in 
 * addition to the specific instant in time also provides resolution
 * information. It is possible to determine if a ResDate only is
 * authoritative for the datum information and not the time information.
 */

public class ResDate extends Date
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2211483047707968608L;
		boolean timeResolution;

    public ResDate(Date date, boolean timeResolution)
    {
	super(date.getTime());
	this.timeResolution = timeResolution;
    }

    // those constructors are deprectated anyway so we might as well
    // remove them
    //private ResDate(int i, int j, int k) {}
    //private ResDate(int i, int j, int k, int l, int m) {}
    //private ResDate(int i, int j, int k, int l, int m, int n) {}
    //private ResDate(String s) {}
    
    public boolean getTimeResolution()
    {
	return this.timeResolution;
    }

    // todo: override all relevant methods in Date
}

