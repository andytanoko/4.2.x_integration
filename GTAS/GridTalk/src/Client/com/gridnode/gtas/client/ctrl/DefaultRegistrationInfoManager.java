/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultRegistrationInfoManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-26     Andrew Hill         Created
 * 2002-12-02     Andrew Hill         Keep GlobalContext relatively up to date for isRegistered
 * 2003-04-16     Andrew Hill         Nodelock changes
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.NotApplicableException;
import com.gridnode.gtas.events.registration.CancelRegistrationInfoEvent;
import com.gridnode.gtas.events.registration.ConfirmRegistrationInfoEvent;
import com.gridnode.gtas.events.registration.GetRegistrationInfoEvent;
import com.gridnode.gtas.events.registration.UpgradeLicenseEvent;
import com.gridnode.gtas.events.registration.ValidateRegistrationInfoEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultRegistrationInfoManager extends DefaultAbstractManager
  implements IGTRegistrationInfoManager
{

  DefaultRegistrationInfoManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_REGISTRATION_INFO, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTRegistrationInfoEntity rego = (IGTRegistrationInfoEntity)entity;
      Short state = (Short)rego.getFieldValue(IGTRegistrationInfoEntity.REG_STATE);
      if(state == null)
      {
        throw new java.lang.NullPointerException("registrationState field is null");
      }
      else if(state.equals(IGTRegistrationInfoEntity.REG_STATE_REG) || state.equals(IGTRegistrationInfoEntity.REG_STATE_EXPIRED))
      { //20030416AH
        upgradeRegistration(rego);
      }
      else if(state.equals(IGTRegistrationInfoEntity.REG_STATE_NOT_REG))
      {
        startRegistration(rego);
      }
      else if(state.equals(IGTRegistrationInfoEntity.REG_STATE_IN_PROGRESS))
      {
        completeRegistration(rego);
      }
      else
      {
        throw new java.lang.IllegalStateException("Unrecognised registrationState:" + state);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating registrationInfo",t);
    }
  }

  private void startRegistration(IGTRegistrationInfoEntity rego)
    throws GTClientException
  {
    try
    {
      DefaultCompanyProfileManager profileMgr = (DefaultCompanyProfileManager)
                                                _session.getManager(
                                                IGTManager.MANAGER_COMPANY_PROFILE);

      Integer gnId = (Integer)rego.getFieldValue(IGTRegistrationInfoEntity.GRIDNODE_ID);
      String gnName = rego.getFieldString(IGTRegistrationInfoEntity.GRIDNODE_NAME);
      String licFile = rego.getFieldString(IGTRegistrationInfoEntity.LICENSE_FILE); //20030416AH
      IGTCompanyProfileEntity profile = (IGTCompanyProfileEntity)rego.getFieldValue(IGTRegistrationInfoEntity.COMPANY_PROFILE);
      Map profileMap = profileMgr.getProfileMap(profile);

      ValidateRegistrationInfoEvent event = new ValidateRegistrationInfoEvent(gnId,
                                                                              gnName,
                                                                              licFile,
                                                                              profileMap); //20030416AH
      handleUpdateEvent(event, (AbstractGTEntity)rego);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error starting registration process",t);
    }
  }

  private void upgradeRegistration(IGTRegistrationInfoEntity rego)
    throws GTClientException
  { //20030416AH
    try
    {
      String licFile = rego.getFieldString(IGTRegistrationInfoEntity.LICENSE_FILE);
      UpgradeLicenseEvent event = new UpgradeLicenseEvent(licFile);
      handleUpdateEvent(event, (AbstractGTEntity)rego);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error upgrading license",t);
    }
  }

  private void completeRegistration(IGTRegistrationInfoEntity rego)
    throws GTClientException
  {
    try
    {
      String secPw = rego.getFieldString(IGTRegistrationInfoEntity.SECURITY_PASSWORD);
      ConfirmRegistrationInfoEvent event = new ConfirmRegistrationInfoEvent(secPw);
      handleUpdateEvent(event, (AbstractGTEntity)rego);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error completing registration process",t);
    }
  }

  public void cancelRegistration(IGTRegistrationInfoEntity rego)
    throws GTClientException
  {
    try
    {
      CancelRegistrationInfoEvent event = new CancelRegistrationInfoEvent();
      handleUpdateEvent(event, (AbstractGTEntity)rego);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error cancelling registration process",t);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new NotApplicableException("Create is not applicable to RegistrationInfo");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_REGISTRATION_INFO;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_REGISTRATION_INFO;
  }

  public IGTRegistrationInfoEntity getRegistrationInfo() throws GTClientException
  {
    try
    {
      IGTRegistrationInfoEntity rInfo = (IGTRegistrationInfoEntity)handleGetEvent(getGetEvent(null));
      boolean isRegistered = IGTRegistrationInfoEntity.REG_STATE_REG.equals(rInfo.getFieldValue(IGTRegistrationInfoEntity.REG_STATE));
      ((DefaultGTSession)_session).getContext().setRegistered(isRegistered); //20021202AH
      return rInfo;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving registrationInfo entity",t);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(uid != null)
    {
      throw new java.lang.IllegalArgumentException("RegistrationInfo may not be referenced by uid");
    }
    return new GetRegistrationInfoEvent();
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new NotApplicableException("RegistrationInfo does not have associated list events");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new UnsupportedOperationException("RegistrationInfo entity may not be deleted");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_REGISTRATION_INFO.equals(entityType))
    {
      return new DefaultRegistrationInfoEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[2];
    sharedVfmi[0] = new VirtualSharedFMI( "registrationInfo.securityPassword",
                                          IGTRegistrationInfoEntity.SECURITY_PASSWORD);
    sharedVfmi[1] = new VirtualSharedFMI( "registrationInfo.confirmPassword",
                                          IGTRegistrationInfoEntity.CONFIRM_PASSWORD);
    sharedVfmi[0].setMandatoryUpdate(true);
    sharedVfmi[1].setMandatoryUpdate(true);
    return sharedVfmi;
  }
}