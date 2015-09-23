/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.enterprise.ISearchRegistryCriteria;

public interface IGTSearchRegistryCriteriaEntity extends IGTEntity
{
  //States
  public static final Short MATCH_ALL             = new Short(ISearchRegistryCriteria.MATCH_ALL);
  public static final Short MATCH_ANY             = new Short(ISearchRegistryCriteria.MATCH_ANY);
  
  //Fields
//  public static final Number UID                  = ISearchRegistryCriteria.UID;
  public static final Number BUS_ENTITY_DESC      = ISearchRegistryCriteria.BUS_ENTITY_DESC;
  public static final Number MESSAGING_STANDARDS  = ISearchRegistryCriteria.MESSAGING_STANDARDS;
  public static final Number DUNS                 = ISearchRegistryCriteria.DUNS;
  public static final Number QUERY_URL            = ISearchRegistryCriteria.QUERY_URL;
  public static final Number MATCH                = ISearchRegistryCriteria.MATCH;
}
