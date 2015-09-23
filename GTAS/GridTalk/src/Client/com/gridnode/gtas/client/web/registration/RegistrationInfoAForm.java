/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistrationInfoAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-27     Andrew Hill         Created
 * 2002-12-16     Andrew Hill         registrationTab
 * 2003-04-16     Andrew Hill         New fields for Node Lock changes
 */
package com.gridnode.gtas.client.web.registration;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.gridnode.CompanyProfileAForm;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class RegistrationInfoAForm extends CompanyProfileAForm
{
  private static final String LIC_FILE = "licFile"; //20030416AH

  private String _gridnodeId;
  private String _gridnodeName;
  private String _bizConnections;
  private String _category;
  private String _prodKeyF1;
  private String _prodKeyF2;
  private String _prodKeyF3;
  private String _prodKeyF4;
  private String _licStartDate;
  private String _licEndDate;
  private String _registrationState;
  private String _securityPassword;
  private String _confirmPassword;
  private String _registrationTab;
  private String _osName; //20030416AH
  private String _osVersion; //20030416AH
  private String _machineName; //20030416AH

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _securityPassword = null;
    _confirmPassword = null;
  }

  public void setRegistrationTab(String tab)
  { _registrationTab = tab; }

  public String getRegistrationTab()
  { return _registrationTab; }

  public String getGridnodeId()
  { return _gridnodeId; }

  public void setGridnodeId(String gridnodeId)
  { _gridnodeId=gridnodeId; }

  public String getGridnodeName()
  { return _gridnodeName; }

  public void setGridnodeName(String gridnodeName)
  { _gridnodeName=gridnodeName; }

  public String getBizConnections()
  { return _bizConnections; }

  public void setBizConnections(String bizConnections)
  { _bizConnections=bizConnections; }

  public String getCategory()
  { return _category; }

  public void setCategory(String category)
  { _category=category; }

  public String getProdKeyF1()
  { return _prodKeyF1; }

  public void setProdKeyF1(String prodKeyF1)
  { _prodKeyF1=prodKeyF1; }

  public String getProdKeyF2()
  { return _prodKeyF2; }

  public void setProdKeyF2(String prodKeyF2)
  { _prodKeyF2=prodKeyF2; }

  public String getProdKeyF3()
  { return _prodKeyF3; }

  public void setProdKeyF3(String prodKeyF3)
  { _prodKeyF3=prodKeyF3; }

  public String getProdKeyF4()
  { return _prodKeyF4; }

  public void setProdKeyF4(String prodKeyF4)
  { _prodKeyF4=prodKeyF4; }

  public String getLicStartDate()
  { return _licStartDate; }

  public void setLicStartDate(String licStartDate)
  { _licStartDate=licStartDate; }

  public String getLicEndDate()
  { return _licEndDate; }

  public void setLicEndDate(String licEndDate)
  { _licEndDate=licEndDate; }

  public String getRegistrationState()
  { return _registrationState; }

  public void setRegistrationState(String registrationState)
  { _registrationState=registrationState; }

  public String getSecurityPassword()
  { return _securityPassword; }

  public void setSecurityPassword(String securityPassword)
  { _securityPassword=securityPassword; }

  public String getConfirmPassword()
  { return _confirmPassword; }

  public void setConfirmPassword(String confirmPassword)
  { _confirmPassword=confirmPassword; }

  public boolean isProfileEditable()
  {
    Short regState = StaticUtils.shortValue( getRegistrationState() );
    if(IGTRegistrationInfoEntity.REG_STATE_NOT_REG.equals(regState))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  //20030416AH - licFile...
  public void setLicFile(String value)
  {
    addFileToField(LIC_FILE,value);
  }

  public String getLicFile()
  {
    FormFileElement ffe = getFormFileElement(LIC_FILE);
    return (ffe != null) ? ffe.getFileName() : "";
  }

  public void clearLicFile()
  { //20030417AH
    FormFileElement[] ffes = getFormFileElements(LIC_FILE,false);
    for(int i=0; i < ffes.length; i++)
    {
      removeFileFromField(LIC_FILE, ffes[i].getId() );
    }
  }

  public void setLicFileDelete(String[] deletions)
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField(LIC_FILE,deletions[i]);
    }
  }

  public void setLicFileUpload(FormFile file)
  {
    addFileToField(LIC_FILE,file);
  }
  //...............

  public void setOsName(String osName)
  { _osName = osName; } //20030416AH

  public String getOsName()
  { return _osName; } //20030416AH

  public void setOsVersion(String osVersion)
  { _osVersion = osVersion; } //20030416AH

  public String getOsVersion()
  { return _osVersion; } //20030416AH

  public void setMachineName(String machineName)
  { _machineName = machineName; } //20030416AH

  public String getMachineName()
  { return _machineName; } //20030416AH
}