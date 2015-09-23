// %1023949935108:com.gridnode.pdip.base.gwfbase.xpdl.helpers%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002   Mahesh	      Created
 * Jun 13 2002   Mathew         Repackaged
 */

package com.gridnode.pdip.base.gwfbase.xpdl.helpers;

public class XpdlConstants
{
  // used in place of null
  public static final String NA = "_NA_";
  //PublicationStatus
  public static final String PS_UNDER_REVISION = "UNDER_REVISION";
  public static final String PS_RELEASED = "RELEASED";
  public static final String PS_UNDER_TEST = "UNDER_TEST";
  //GraphConformance
  public static final String GC_FULL_BLOCKED = "FULL_BLOCKED";
  public static final String GC_LOOP_BLOCKED = "LOOP_BLOCKED";
  public static final String GC_NON_BLOCKED = "NON_BLOCKED";
  //ParticipantType
  public static final String PT_RESOURCE_SET = "RESOURCE_SET";
  public static final String PT_RESOURCE = "RESOURCE";
  public static final String PT_ROLE = "ROLE";
  public static final String PT_ORGANIZATIONAL_UNIT = "ORGANIZATIONAL_UNIT";
  public static final String PT_HUMAN = "HUMAN";
  public static final String PT_SYSTEM = "SYSTEM";
  //FormalParameter
  public static final String FP_IN = "IN";
  public static final String FP_OUT = "OUT";
  public static final String FP_INOUT = "INOUT";
  //Implementation Activity type
  public static final String IT_NO = "No";
  public static final String IT_TOOL = "Tool";
  public static final String IT_SUBFLOW = "SubFlow";
  public static final String IT_LOOP = "Loop";
  //Subflow Execution
  public static final String SE_ASYNCHR = "ASYNCHR";
  public static final String SE_SYNCHR = "SYNCHR";
  //Loop Kind
  public static final String LK_WHILE = "WHILE";
  public static final String LK_REPEAT_UNTIL = "REPEAT_UNTIL";
  //Tool Type
  public static final String TT_APPLICATION = "APPLICATION";
  public static final String TT_PROCEDURE = "PROCEDURE";
  //Mode
  public static final String AUTOMATIC_MODE = "Automatic";
  public static final String MANUAL_MODE = "Manual";
  //SimulationInformation Instantiation
  public static final String SI_ONCE = "ONCE";
  public static final String SI_MULTIPLE = "MULTIPLE";
  //Combination Type
  public static final String CT_AND = "AND";
  public static final String CT_XOR = "XOR";
  //Transition Loop
  public static final String TL_NOLOOP = "NOLOOP";
  public static final String TL_FROMLOOP = "FROMLOOP";
  public static final String TL_TOLOOP = "TOLOOP";
  //Transition Condition
  public static final String TC_CONDITION = "CONDITION";
  public static final String TC_OTHERWISE = "OTHERWISE";
  public static final String TC_EXCEPTION = "EXCEPTION";
  public static final String TC_DEFAULTEXCEPTION = "DEFAULTEXCEPTION";


  //BOOLEAN | UNIT | PERFORMER
  //STRING | FLOAT | INTEGER | REFERENCE | DATETIME
  //RecordType | UnionType | EnumerationType | ArrayType | ListType | BasicType | PlainType | DeclaredType
  public static final String DT_BOOLEAN = "BOOLEAN";
  public static final String DT_UNIT = "UNIT";
  public static final String DT_PERFORMER = "PERFORMER";
  public static final String DT_STRING = "STRING";
  public static final String DT_FLOAT = "FLOAT";
  public static final String DT_INTEGER = "INTEGER";
  public static final String DT_REFERENCE = "REFERENCE";
  public static final String DT_DATETIME = "DATETIME";
  public static final String DT_RECORD_TYPE = "RecordType";
  public static final String DT_UNION_TYPE = "UnionType";
  public static final String DT_ENUMERATION_TYPE = "EnumerationType";
  public static final String DT_ARRAY_TYPE = "ArrayType";
  public static final String DT_LIST_TYPE = "ListType";
  public static final String DT_BASIC_TYPE = "BasicType";
  public static final String DT_PLAIN_TYPE = "PlainType";
  public static final String DT_DECLARED_TYPE = "DeclaredType";

  public static final String START_ACTIVITYID = "START";
  public static final String END_ACTIVITYID = "END";
}