/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */
package se.metamatrix.mimedir;

/**
 * This is the default fallback errorhandler that does absolutely nothing.
 */

public class DefaultErrorHandler implements ErrorHandler
{
    public void error(int level, int code, String details)
    {

    }
}
