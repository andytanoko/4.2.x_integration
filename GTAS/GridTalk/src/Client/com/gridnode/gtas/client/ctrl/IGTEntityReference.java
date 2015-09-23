/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEntityReference.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-29     Andrew Hill         Created 
 */
package com.gridnode.gtas.client.ctrl;

/*
 * Interface to a bean that provides sufficient information to deference an entity
 * or collection of entities that have the same key value.
 */
public interface IGTEntityReference
{
  /*
   * Returns the (pTier) entity type string.
   * @return entityType
   */
  public String getType();
  
  /*
   * Returns the numeric field identifier for the field whose value keys the entity bein
   * referenced. (For example the UID field)
   * @return keyFieldId
   */
  public Number getKeyFieldId();
  
  /*
   * Returns the the value of the key field that will identify the entitie(s)
   * (For example the uid value)
   * @return keyValue
   */
  public Object getKeyValue();
}
