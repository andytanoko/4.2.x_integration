/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkLicense.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 04 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This GridTalkLicense entity holds the license details for GridTalk.<p>
 * The data model:<PRE>
 * UID                 - UID of the License entity.
 * License UID         - The UID of the license tied to this GridTalkLicense.
 * OS Name             - Encrpyted OS Name of the GridTalk server.
 * OS Version          - Encrpyted OS Version of the GridTalk server.
 * Machine Name        - Encrpyted Machine Name of the GridTalk server.
 * Start Date          - Encrpyted Start Date of the license.
 * End Date            - Encrpyted End Date of the license.
 * Version             - Version of the License entity.
 * </PRE>
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class GridTalkLicense
  extends    AbstractEntity
  implements IGridTalkLicense
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2045940854919507657L;
	private Long            _licenseUid;
  private String          _osName;
  private String          _osVersion;
  private String          _machineName;
  private String          _startDate;
  private String          _endDate;

  public GridTalkLicense()
  {
  }

  // ************** Methods from AbstractEntity ***************************

  public String getEntityDescr()
  {
    return getLicenseUid()+"-"+getOsName()+"-"+getOsVersion()+"-"+getMachineName();
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

  public void setLicenseUid(Long licenseUid)
  {
    _licenseUid = licenseUid;
  }

  public Long getLicenseUid()
  {
    return _licenseUid;
  }

  public void setOsName(String osName)
  {
    _osName = osName;
  }

  public String getOsName()
  {
    return _osName;
  }

  public void setOsVersion(String osVersion)
  {
    _osVersion = osVersion;
  }

  public String getOsVersion()
  {
    return _osVersion;
  }

  public void setMachineName(String machineName)
  {
    _machineName = machineName;
  }

  public String getMachineName()
  {
    return _machineName;
  }

  public void setStartDate(String startDate)
  {
    _startDate = startDate;
  }

  public String getStartDate()
  {
    return _startDate;
  }

  public void setEndDate(String endDate)
  {
    _endDate = endDate;
  }

  public String getEndDate()
  {
    return _endDate;
  }

}