/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTMheReference.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-29     Andrew Hill         Created 
 */
package com.gridnode.gtas.client.ctrl;

import java.util.List;

import com.gridnode.gtas.client.GTClientException;

/*
 * Interface to an object that defines references to multiple hetrogenous entities
 */
public interface IGTMheReference
{
  /*
   * Get a List of all the references.
   * If there are none will return an empty list.
   * @return List of IGTEntiyReference
   * @throws GTClientException
   */
  public List getAll() throws GTClientException;
  
  /*
   * Get a list of all the references of a particular entity type.
   * If there are none will return an empty list.
   * @param entityType the (ptier) entity type name
   * @return List of IGTEntityReference
   * @throws GTClientException
   */
  public List get(String type) throws GTClientException;
  
  
  public int size() throws GTClientException;
  public int size(String entityType) throws GTClientException;
  public String[] getReferencedTypes() throws GTClientException;
}
