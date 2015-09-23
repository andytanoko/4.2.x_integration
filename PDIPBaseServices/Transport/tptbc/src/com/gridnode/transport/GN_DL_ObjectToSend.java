
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
 *This class describes the object to be sent by this layer (DL layer).
 *Also, this class acts as an interface between this layer and the lower layer.
 */
public class GN_DL_ObjectToSend implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 423540463833825084L;

		/**
     *Due to this class is an interface between this layer and the lower layer, this class
     *is a superclass which defines a serial of versions of its subclass.
     */
    public static final int VERSION_1 = 1;

    int version;

    /**
     *The constructor method
     *@param version the version of the instance to be created.
     */
    public GN_DL_ObjectToSend(int version)
    {
	this.version = version;
    }
    /**
     *set the version of this object
     *@param v the version to be set.
     */
    public void setVersion(int v)
    {
	this.version = v;
    }
    /**
     *@return the version of this instance.
     */
    public int getVersion()
    {
	return this.version;
    }
}
