/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: License.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 10 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.license.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Date;

/**
 * This License entity holds the license details of a product.<p>
 * The data model:<PRE>
 * UID                 - UID of the License entity.
 * Product Key         - The Product key of the licensed product.
 * Product Name        - The name of the licensed product.
 * Product Version     - The version of the licensed product.
 * Start Date          - The starting date when the license becomes valid.
 * End Date            - The ending date when the license ceased to be valid.
 * State               - The state of the license: Valid, NotCommenced, Expired
 *                       or Revoked.
 * Version             - Version of the License entity.
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class License
  extends    AbstractEntity
  implements ILicense
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3738649150042981484L;
	private String          _prodKey;
  private String          _prodName;
  private String          _prodVersion;
  private short           _state        = STATE_NOT_COMMENCED;
  private Date            _startDate;
  private Date            _endDate;

  public License()
  {
  }

  // ************** Methods from AbstractEntity ***************************

  public String getEntityDescr()
  {
    return getProductKey() +
           "[" + getProductName() +
           "," + getProductVersion() +
           "]";
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters & Setters *********************************

  public void setProductKey(String prodKey)
  {
    _prodKey = prodKey;
  }

  public String getProductKey()
  {
    return _prodKey;
  }

  public void setProductName(String prodName)
  {
    _prodName = prodName;
  }

  public String getProductName()
  {
    return _prodName;
  }

  public void setProductVersion(String prodVersion)
  {
    _prodVersion = prodVersion;
  }

  public String getProductVersion()
  {
    return _prodVersion;
  }

  public void setState(short state)
  {
    _state = state;
  }

  public short getState()
  {
    return _state;
  }

  public void setStartDate(Date startDate)
  {
    _startDate = startDate;
  }

  public Date getStartDate()
  {
    return _startDate;
  }

  public void setEndDate(Date endDate)
  {
    _endDate = endDate;
  }

  public Date getEndDate()
  {
    return _endDate;
  }

  public boolean isLicenseValid()
  {
    return getState() == STATE_VALID;
  }
}