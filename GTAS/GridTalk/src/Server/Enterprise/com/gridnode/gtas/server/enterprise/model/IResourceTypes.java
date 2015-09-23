/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IResourceTypes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.model;

/**
 * This interface keeps all resource types that the EnterpriseHierarchyManagerBean
 * manages.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IResourceTypes
{

  static final String BUSINESS_ENTITY = "BusinessEntity";
  static final String CHANNEL         = "Channel";
  static final String PARTNER         = "Partner";
  static final String USER            = "User";
}