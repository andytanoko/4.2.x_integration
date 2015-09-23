/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistrationInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 10 2002    Neo Sok Lay         Created
 * Apr 03 2003    Koh Han Sing        Added nodeLock feature
 * Apr 11 2003    Koh Han Sing        Add in license state
 */
package com.gridnode.gtas.server.registration.model;

import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Date;

/**
 * This RegistrationInfo entity holds the details of a product registration
 * instance. These information are not persistent in the database as it is
 * presented here but rather will be separately persistent in the GridNode,
 * License, and CompanyProfile database.<p>
 * The data model:<PRE>
 * Gridnode ID         - The Gridnode ID to be registered.
 * Gridnode Name       - Name given to the GridNode.
 * Category            - The Gridnode Category. This is determined by the
 *                       Gridnode ID and product key.
 * BizConnections      - Number of Business Connections allowed.
 * Company Profile     - Company profile.
 * Product Key F1      - Field 1 of the Product key.
 * Product Key F2      - Field 2 of the Product key.
 * Product Key F3      - Field 3 of the Product key.
 * Product Key F4      - Field 4 of the Product key.
 * License Start Date  - Start date of the validated product key license.
 * License End Date    - End date of the validated product key license.
 * RegistrationState   - Current state of registration process.
 * OS Name             - The name of the operating system used.
 * OS Version          - The version of the operating system used.
 * Machine Name        - The name of the computer.
 * LicenseState        - The state of the license.
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class RegistrationInfo
  extends    AbstractEntity
  implements IRegistrationInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6965580734354487369L;
	private Integer         _nodeID;
  private String          _nodeName;
  private String          _prodKeyF1;
  private String          _prodKeyF2;
  private String          _prodKeyF3;
  private String          _prodKeyF4;
  private String          _category;
  private Integer         _bizConnections;
  private CompanyProfile  _profile;
  private Date            _licStartDate;
  private Date            _licEndDate;
  private short           _registrationState = STATE_NOT_REGISTERED;
  private String          _licFile;
  private String          _osName;
  private String          _osVersion;
  private String          _machineName;
  private short           _licenseState = STATE_LICENSE_NOT_COMMENCED;

  public RegistrationInfo()
  {
  }

  // ************** Methods from AbstractEntity ***************************

  public String getEntityDescr()
  {
    return getGridnodeID() + "-" + getGridnodeName();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return GRIDNODE_ID;
  }

  // ***************** Getters & Setters *********************************

  public void setGridnodeID(Integer nodeID)
  {
    _nodeID = nodeID;
  }

  public Integer getGridnodeID()
  {
    return _nodeID;
  }

  public void setGridnodeName(String nodeName)
  {
    _nodeName = nodeName;
  }

  public String getGridnodeName()
  {
    return _nodeName;
  }

  public void setProductKeyF1(String f1)
  {
    _prodKeyF1 = f1;
  }

  public String getProductKeyF1()
  {
    return _prodKeyF1;
  }

  public void setProductKeyF2(String f2)
  {
    _prodKeyF2 = f2;
  }

  public String getProductKeyF2()
  {
    return _prodKeyF2;
  }

  public void setProductKeyF3(String f3)
  {
    _prodKeyF3 = f3;
  }

  public String getProductKeyF3()
  {
    return _prodKeyF3;
  }

  public void setProductKeyF4(String f4)
  {
    _prodKeyF4 = f4;
  }

  public String getProductKeyF4()
  {
    return _prodKeyF4;
  }

  public void setCategory(String category)
  {
    _category = category;
  }

  public String getCategory()
  {
    return _category;
  }

  public void setBizConnections(Integer connections)
  {
    _bizConnections = connections;
  }

  public Integer getBizConnections()
  {
    return _bizConnections;
  }

  public void setCompanyProfile(CompanyProfile profile)
  {
    _profile = profile;
  }

  public CompanyProfile getCompanyProfile()
  {
    return _profile;
  }

  public void setLicenseStartDate(Date startDate)
  {
    _licStartDate = startDate;
  }

  public Date getLicenseStartDate()
  {
    return _licStartDate;
  }

  public void setLicenseEndDate(Date endDate)
  {
    _licEndDate = endDate;
  }

  public Date getLicenseEndDate()
  {
    return _licEndDate;
  }

  public void setRegistrationState(short regState)
  {
    _registrationState = regState;
  }

  public short getRegistrationState()
  {
    return _registrationState;
  }

  public void setLicenseFile(String licFile)
  {
    _licFile = licFile;
  }

  public String getLicenseFile()
  {
    return _licFile;
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

  public void setLicenseState(short licenseState)
  {
    _licenseState = licenseState;
  }

  public short getLicenseState()
  {
    return _licenseState;
  }
}