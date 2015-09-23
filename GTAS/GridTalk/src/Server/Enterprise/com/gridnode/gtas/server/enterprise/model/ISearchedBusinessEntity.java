/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchedBusinessEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.model;

/**
 * This interface defines the the properties and FieldIds for accessing fields
 * in SearchedBusinessEntity entity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public interface ISearchedBusinessEntity
{
  /**
   * Name of the SearchedBusinessEntity entity.
   */
  public static final String ENTITY_NAME = "SearchedBusinessEntity";

  /**
   * FieldId for UUID of the SearchedBusinessEntity instance of a particular search.
   */
  public static final Number UUID = new Integer(0);
  
  /**
   * FieldId for EnterpriseId of a SearchedBusinessEntity entity instance. A String.
   */
  public static final Number ENTERPRISE_ID        = new Integer(1);  //String

  /**
   * FieldId for BusinessEntityId of a SearchedBusinessEntity entity instance. A String.
   */
  public static final Number ID                   = new Integer(2);  //string(4)

  /**
   * FieldId for BusinessEntityDescription of a SearchedBusinessEntity entity instance.
   * A String.
   */
  public static final Number DESCRIPTION          = new Integer(3);  //string(80)

  /**
   * FieldId for State of a SearchedBusinessEntity entity instance. An integer.
   */
  public static final Number STATE              = new Integer(4);  //integer
  
  /**
   * FieldId for WhitePage of a SearchedBusinessEntity entity instance. A
   * {@link WhitePage}.
   */
  public static final Number WHITE_PAGE           = new Integer(5);  //WhitePage

  /**
   * FieldId for Channels. A Collection of ChannelInfo (which do not contain UIDs).
   */
  public static final Number CHANNELS           = new Integer(6);  //Collection(ChannelInfos)


  // Values for STATE
  /**
   * State to indicate that the SearchBusinessEntity does not belong to
   * a existing known partner in this GridTalk. Value = 0.
   */
  public static final int STATE_NEW_BE  = 0;
  
  /**
   * State to indicate that the SearchBusinessEntity belongs to
   * this GridTalk's own GridNode. Value = 1.
   */
  public static final int STATE_MY_BE  = 1;

  /**
   * State to indicate that the SearchBusinessEntity belongs to
   * an existing known partner in this GridTalk. Value = 2.
   */
  public static final int STATE_PARTNER_BE  = 2;

}
