/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTBusinessEntityManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-06     Andrew Hill         Created
 * 2003-01-15     Andrew Hill         send(beUids,gnUids)
 * 2003-07-18     Andrew Hill         Remove deprecated send method
 * 2004-01-02     Daniel D'Cotta      Added DomainIdentifiers
 * 2008-07-17	  Teh Yu Phei		  Add activate (Ticket 31)
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;

import com.gridnode.gtas.client.GTClientException;

public interface IGTBusinessEntityManager extends IGTManager
{
  /*20030718AH - co: public void send(Collection beUids, String toEnterpriseId, Long viaChannelUid)
    throws GTClientException; */

  public void send(Collection beUids, Collection gnUids) throws GTClientException;
    
  public IGTDomainIdentifierEntity newDomainIdentifier() throws GTClientException;

  public void activate(long[] bizEntities) throws GTClientException;
}