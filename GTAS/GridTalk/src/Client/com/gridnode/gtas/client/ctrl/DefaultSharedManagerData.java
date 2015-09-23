/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultSharedManagerData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-24     Andrew Hill         Created
 * 2003-06-23     Andrew Hill         Corrected error where vfmi incorrectly set to point to fmi table
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;

/**
 * Manager Data that may be shared globally - this is mostly processed field metaInfo
 * This will be stored in GlobalContext
 */
class DefaultSharedManagerData
{
  //@todo: check code see if safe to make these HashMaps (as *hopefully* they are only modified
  //once when created and then read only after that?)
  private Hashtable _fieldMetaInfo;
  private Hashtable _virtualFieldMetaInfo;
  private Hashtable _fieldMetaInfoLookup;
  private Hashtable _fieldIdLookup;

  DefaultSharedManagerData( Hashtable fieldMetaInfo,
                            Hashtable virtualFieldMetaInfo,
                            Hashtable fieldMetaInfoLookup,
                            Hashtable fieldIdLookup)
  {
    _fieldMetaInfo = fieldMetaInfo;
    //20030623AH - A very nasty bug on this line! _virtualFieldMetaInfo = fieldMetaInfo;
    _virtualFieldMetaInfo = virtualFieldMetaInfo; //20030623AH
    _fieldMetaInfoLookup = fieldMetaInfoLookup;
    _fieldIdLookup = fieldIdLookup;
  }

  Hashtable getFieldMetaInfo()
  {
    return _fieldMetaInfo;
  }

  Hashtable getVirtualFieldMetaInfo()
  {
    return _virtualFieldMetaInfo;
  }

  Hashtable getFieldMetaInfoLookup()
  {
    return _fieldMetaInfoLookup;
  }

  Hashtable getFieldIdLookup()
  {
    return _fieldIdLookup;
  }
}