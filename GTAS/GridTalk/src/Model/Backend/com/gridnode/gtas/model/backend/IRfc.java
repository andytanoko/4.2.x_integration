/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRfc.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2001    Koh Han Sing        Created
 */
package com.gridnode.gtas.model.backend;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in Rfc entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IRfc
{
   /**
    * Name for Rfc entity.
    */
   public static final String ENTITY_NAME = "Rfc";

   /**
    * FieldId for UId. A Number.
    */
   public static final Number UID = new Integer(0);  //Integer

  /**
   * FieldId for CanDelete flag. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(1);  //Boolean

   /**
    * FieldId for Name. A String.
    */
   public static final Number NAME = new Integer(3);  //String(18)

   /**
    * FieldId for Desc. A String.
    */
   public static final Number DESCRIPTION = new Integer(4);  //String(80)

   /**
    * FieldId for Host. A String.
    */
   public static final Number HOST = new Integer(6);  //String(80)

   /**
    * FieldId for PortNumber. A Number.
    */
   public static final Number PORT_NUMBER = new Integer(7);  //Integer

   /**
    * FieldId for UseCommandFile. A Boolean.
    */
   public static final Number USE_COMMAND_FILE = new Integer(8);  //Boolean

   /**
    * FieldId for CommandFileDir. A String.
    */
   public static final Number COMMAND_FILE_DIR = new Integer(9);  //String(120)

   /**
    * FieldId for CommandFile. A String.
    */
   public static final Number COMMAND_FILE = new Integer(10);  //String(80)

   /**
    * FieldId for CommandLine. A String.
    */
   public static final Number COMMAND_LINE = new Integer(11);  //String(120)

   //CONNECTION TYPE
   /**
    * ConnectionType for TCP/IP connection.
    */
   public static final String TCPIP  = "T";    //tcpip
}
