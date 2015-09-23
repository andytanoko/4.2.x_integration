/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTBizCertMappingManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-14     Andrew Hill         Created
 * 2003-01-15     Andrew Hill         getApplicablePartnerCertList(partnerId)
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;

import com.gridnode.gtas.events.partnerprocess.FilterPartnerListEvent;

import com.gridnode.gtas.client.GTClientException;

public interface IGTBizCertMappingManager extends IGTManager
{
  public static final Integer CONDITION_HAVE_CERT_MAPPING = FilterPartnerListEvent.HAVE_CERT_MAPPING;
  public static final Integer CONDITION_NO_CERT_MAPPING = FilterPartnerListEvent.NO_CERT_MAPPING;

  public Collection getApplicableOwnCertList()
    throws GTClientException;

  public Collection getApplicablePartnerCertList(IGTPartnerEntity partner)
    throws GTClientException;

  public Collection getApplicablePartnerCertList(Long partnerUid)
    throws GTClientException;

  public Collection getApplicablePartnerList(Integer condition)
    throws GTClientException;

  public Collection getApplicablePartnerCertList(String partnerId)
    throws GTClientException; //20030115AH
}