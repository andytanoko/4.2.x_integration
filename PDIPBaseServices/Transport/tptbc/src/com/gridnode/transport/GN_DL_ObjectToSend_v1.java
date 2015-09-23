/**
 * Title: GridTalk Software
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:GridNode Pte Ltd
 * @author Yang Yue Xiang
 * @version 1.0
 */

package com.gridnode.transport;

import java.io.Serializable;

/**
 *The first version of GN_DL_ObjectToSend.
 *This class only contains one field - the serializable object to be sent.
 */
public class GN_DL_ObjectToSend_v1 extends GN_DL_ObjectToSend
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3002847064085719895L;
		Serializable o = null;
    /**
     *@param o the object to be sent.
     */
    public GN_DL_ObjectToSend_v1(Serializable o)
    {
	super(GN_DL_ObjectToSend.VERSION_1);
	this.o = o;
    }
    /**
     *@return the serializable object wrapped in this GN_DL_ObjectToSend.
     */
    public Serializable getObject()
    {
	return this.o;
    }
}
