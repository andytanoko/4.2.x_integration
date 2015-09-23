/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseStateNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28, 2006   i00107             Created
 */

package com.gridnode.gtas.server.notify;

import java.util.Date;

/**
 * @author i00107
 * Notification data to notify other modules of the change in the GridTalk License state.
 */
public class LicenseStateNotification extends AbstractNotification
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -3323101591797693335L;
	private String _productKey;
	private Integer _nodeID;
	private String _category;
	private Integer _numBizConnections;
	private short _state;
	private Date _validityStart;
	private Date _validityEnd;
	
  /**
   * Possible Value for STATE field. This indicates that the License is
   * currently valid.
   */
  public static final short STATE_LICENSE_VALID         = 0;

  /**
   * Possible value for STATE field. This indicates that the License will not
   * be valid until the START_DATE.
   */
  public static final short STATE_LICENSE_NOT_COMMENCED = 1;

  /**
   * Possible value for STATE field. This indicates that the License has expired.
   */
  public static final short STATE_LICENSE_EXPIRED       = 2;

  /**
   * Possible value for STATE field. This indicates that the License is revoked
   * when it has not expired.
   */
  public static final short STATE_LICENSE_REVOKED       = 3;

	/**
	 * 
	 */
	public LicenseStateNotification(String productKey, Integer nodeID, String category, Integer numBizConnections, 
	                                short state, 
	                                Date validityStart, Date validityEnd)
	{
		_productKey = productKey;
		_nodeID = nodeID;
		_category = category;
		_numBizConnections = numBizConnections;
		_state = state;
		_validityStart = validityStart;
		_validityEnd = validityEnd;
	}

	/**
	 * @see com.gridnode.pdip.framework.notification.INotification#getNotificationID()
	 */
	public String getNotificationID()
	{
		return "LicenseState";
	}

	/**
	 * @return Returns the category.
	 */
	public String getCategory()
	{
		return _category;
	}

	/**
	 * @return Returns the nodeID.
	 */
	public Integer getNodeID()
	{
		return _nodeID;
	}

	/**
	 * @return Returns the numBizConnections.
	 */
	public Integer getNumBizConnections()
	{
		return _numBizConnections;
	}

	/**
	 * @return Returns the productKey.
	 */
	public String getProductKey()
	{
		return _productKey;
	}

	/**
	 * @return Returns the state.
	 */
	public short getState()
	{
		return _state;
	}

	/**
	 * @return Returns the validityEnd.
	 */
	public Date getValidityEnd()
	{
		return _validityEnd;
	}

	/**
	 * @return Returns the validityStart.
	 */
	public Date getValidityStart()
	{
		return _validityStart;
	}

	public String toString()
	{
		return getNotificationID() + ": NodeID["+getNodeID()+"], Category["+getCategory()+"], ProductKey["+getProductKey()+"], State["+getState()+
			"], BizConnections["+getNumBizConnections()+"],Validity["+getValidityStart()+" to "+getValidityEnd()+"]";
	}
}
