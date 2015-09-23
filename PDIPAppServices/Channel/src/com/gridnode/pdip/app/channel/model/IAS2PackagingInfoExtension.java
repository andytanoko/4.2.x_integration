/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAS2PackagingInfoExtension.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Oct 30 2003      Guo Jianyu              Created
 */
package com.gridnode.pdip.app.channel.model;

public interface IAS2PackagingInfoExtension
{
  public static final String ENTITY_NAME = "AS2PackagingInfoExtension";

  public static final Number IS_ACK_REQ = new Integer(0); //boolean

  public static final Number IS_ACK_SIGNED = new Integer(1); //boolean

  public static final Number IS_NRR_REQ = new Integer(2); //boolean

  public static final Number IS_ACK_SYN = new Integer(3); //boolean

  public static final Number RETURN_URL = new Integer(4); //String

}