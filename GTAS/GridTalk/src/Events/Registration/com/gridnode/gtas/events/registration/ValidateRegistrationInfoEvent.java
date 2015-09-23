/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValidateRegistrationInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 10 2002    Neo Sok Lay         Created
 * Mar 25 2003    Koh Han Sing        Changed to support node-lock licensing
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "SYSTEM"
 */
package com.gridnode.gtas.events.registration;

import java.util.Map;
import com.gridnode.gtas.model.gridnode.ICompanyProfile;
import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data to be validated as Product RegistrationInfo.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I5
 */
public class ValidateRegistrationInfoEvent
  extends    EventSupport
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1670288675013370872L;
	static final String GUARDED_FEATURE = "SYSTEM";
  static final String GUARDED_ACTION  = "ValidateRegistrationInfo";

  static final String GRIDNODE_ID     = "Gridnode ID";
  static final String GRIDNODE_NAME   = "Gridnode Name";
//  static final String PROD_KEY_F1     = "Prod Key F1";    //KHS20030325
//  static final String PROD_KEY_F2     = "Prod Key F2";    //KHS20030325
//  static final String PROD_KEY_F3     = "Prod Key F3";    //KHS20030325
//  static final String PROD_KEY_F4     = "Prod Key F4";    //KHS20030325
  static final String LICENSE_FILE    = "License File";
  static final String COMPANY_PROFILE = "Company Profile";


//KHS20030325
//  public ValidateRegistrationInfoEvent(
//    Integer gridnodeID, String gridnodeName,
//    String prodKeyF1, String prodKeyF2, String prodKeyF3,
//    String prodKeyF4, Map companyProfile)
//    throws EventException
  public ValidateRegistrationInfoEvent(Integer gridnodeID, String gridnodeName,
    String licenseFile, Map companyProfile)
    throws EventException
  {
    checkSetInteger(GRIDNODE_ID, gridnodeID);
    checkSetString(GRIDNODE_NAME, gridnodeName);
//    checkSetString(PROD_KEY_F1, prodKeyF1);    //KHS20030325
//    checkSetString(PROD_KEY_F2, prodKeyF2);    //KHS20030325
//    checkSetString(PROD_KEY_F3, prodKeyF3);    //KHS20030325
//    checkSetString(PROD_KEY_F4, prodKeyF4);    //KHS20030325
    checkSetString(LICENSE_FILE, licenseFile);   //KHS20030325
    checkObject(COMPANY_PROFILE, companyProfile, Map.class);
    checkString("Company Name", companyProfile.get(ICompanyProfile.COY_NAME));
    checkString("Country", companyProfile.get(ICompanyProfile.COUNTRY));
    checkString("Language", companyProfile.get(ICompanyProfile.LANGUAGE));
    setEventData(COMPANY_PROFILE, companyProfile);
  }

  public Integer getGridnodeID()
  {
    return (Integer)getEventData(GRIDNODE_ID);
  }

  public String getGridnodeName()
  {
    return (String)getEventData(GRIDNODE_NAME);
  }

//KHS20030325
//  public String getProdKeyF1()
//  {
//    return (String)getEventData(PROD_KEY_F1);
//  }

//KHS20030325
//  public String getProdKeyF2()
//  {
//    return (String)getEventData(PROD_KEY_F2);
//  }

//KHS20030325
//  public String getProdKeyF3()
//  {
//    return (String)getEventData(PROD_KEY_F3);
//  }

//KHS20030325
//  public String getProdKeyF4()
//  {
//    return (String)getEventData(PROD_KEY_F4);
//  }

  //KHS20030325
  public String getLicenseFile()
  {
    return (String)getEventData(LICENSE_FILE);
  }

  public Map getCompanyProfile()
  {
    return (Map)getEventData(COMPANY_PROFILE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ValidateRegistrationInfoEvent";
  }

  // ************ Methods from IGuardedEvent ***********************
  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }


}