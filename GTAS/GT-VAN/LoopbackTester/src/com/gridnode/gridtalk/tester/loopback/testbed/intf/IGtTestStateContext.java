/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGtTestStateContext.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf;

import java.io.Serializable;

/**
 * Interface for a GridTalk test
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public interface IGtTestStateContext extends Serializable
{
	/**
	 * Return the identifier of this GridTalk test
	 * @return The <code>testId</code> of a GridTalk test object.
	 */
	public abstract String getTestId();
}
