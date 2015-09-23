/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */
package se.metamatrix.mimedir;

/**
 * An interface a client can implement if it wants to be notified of
 * errors and other problems during parsing.
 */
public interface ErrorHandler
{
    int DEBUG = 0;
    int NOTICE = 1;
    int WARNING = 2;
    int ERROR = 3;
    int FATAL = 4;

    /**
     * This method is called by the parser if it wants to notify the
     * client of some contdition.
     * 
     * @param level   indicates the severity of the problem. Higher
     *                value means bigger problems. Clients can use the
     *                constants defined in this class to determine the
     *                severity of the problem.
     * @param code    an integer that is a numeric representation of the
     *                exact problem. It is intended for machine
     *                readability. 
     * @param details a cleartext description of the problem.
     */
    void error(int level, int code, String details);
}
