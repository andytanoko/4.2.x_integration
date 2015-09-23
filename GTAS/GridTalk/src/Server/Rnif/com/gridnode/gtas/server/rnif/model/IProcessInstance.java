/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcessInstance.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-17     Daniel D'Cotta      Created
 * 2003-08-20     Guo Jianyu          added USER_TRACKING_ID
 */
package com.gridnode.gtas.server.rnif.model;

public interface IProcessInstance
{
  public static final String ENTITY_NAME= "ProcessInstance";
  public static final Number UID= new Integer(0); //Integer
  public static final Number PROCESS_INSTANCE_ID= new Integer(1); // String
  public static final Number PARTNER= new Integer(2); // String
  public static final Number STATE= new Integer(3); // String
  public static final Number START_TIME= new Integer(4); // Date
  public static final Number END_TIME= new Integer(5); // Date
  public static final Number RETRY_NUM= new Integer(6); // Integer
  public static final Number IS_FAILED= new Integer(7); // Boolean
  public static final Number FAIL_REASON= new Integer(8); // Integer
  public static final Number DETAIL_REASON= new Integer(9); // String
  public static final Number ASSOC_DOCS= new Integer(10); // ArrayList

  public static final Number PROCESS_DEF_NAME= new Integer(11); // String
  public static final Number ROLE_TYPE= new Integer(12); // String
  public static final Number USER_TRACKING_ID = new Integer(13); //String
  //  public static final String  FR_RETRY_OUT = "RetriesOut";
  //  public static final String  FR_ACT_OUT ="AckTimeOut";
  //  public static final String  FR_VALIDATE_EX="ValidateError";
  //  public static final String  FR_RECEIVE_EX ="ExceptReceived";
  //  public static final String FR_USER_CANCEL = "Cancelled";

  public static final int FR_RETRY_OUT= 1;
  public static final int FR_ACT_OUT= 2;
  public static final int FR_VALIDATE_EX= 3;
  public static final int FR_RECEIVE_EX= 4;
  public static final int FR_USER_CANCEL= 5;
}
