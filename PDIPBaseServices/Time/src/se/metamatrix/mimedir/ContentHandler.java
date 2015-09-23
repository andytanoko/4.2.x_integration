/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */

package se.metamatrix.mimedir;


public interface ContentHandler
{
    void contentLine(String name, Parameter param[], Object value,
		     String group);
}
