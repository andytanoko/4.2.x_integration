/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcedureDefFile.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 31 2002    Jagadeesh              Created
 */

package com.gridnode.pdip.base.userprocedure.model;
/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in UserProcedureDefinitionFile entity.
 *
 * @author Jagadeesh.
 *
 * @version 2.0
 * @since 2.0
 */

public interface IProcedureDefFile
{

  public static final String ENTITY_NAME="ProcedureDefFile";

  public static final Number UID = new Integer(0);

  public static final Number NAME = new Integer(1);

  public static final Number DESCRIPTION = new Integer(2);

  public static final Number TYPE = new Integer(3);

  public static final Number FILE_NAME = new Integer(4);

  public static final Number FILE_PATH = new Integer(5);

  /**
   * FieldId for Whether-the-PartnerFunction-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(6); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION       = new Integer(7); //Double

}