/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */
package com.gridnode.gtas.model.document;
/**
 * This interface defines the properties and FieldIds for accessing fields
 * in AS2DocTypeMapping entity.
 *
 * @author Wong Yee Wah
 *
 * @version 4.0.2 
 * @since 4.0.2 
 */
public interface IAS2DocTypeMapping
{
  /**
   * Name of ProcessMapping entity.
   */
  public static final String  ENTITY_NAME = "AS2DocTypeMapping";
  
  /**
   * FieldId for the UID for a ProcessMapping entity instance. A Long.
   */
  public static final Number UID = new Integer(0); //Long
  
  /**
   * FieldId for AS2Doc Type. A String.
   */
  public static final Number AS2_DOC_TYPE = new Integer(1); //String(30)

  /**
   * FieldId for Doc Type. A String.
   */
  public static final Number DOC_TYPE = new Integer(2); //String(30)
  
  /**
   * FieldId for Partner ID. A String.
   */
  public static final Number PARTNER_ID = new Integer(3); //String(20)
  
  /**
   * FieldId for Whether-the-AS2DocTypeMapping-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(4); //Boolean
  
}

