/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IParamDef.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 31 2002    Jagadeesh              Created
 * Feb  10 2003    Jagadeesh              Added: A Vector of ParamDef - insted of
 *                                        HashMap of Key-Value Pairs.
 */


package com.gridnode.pdip.base.userprocedure.model;

import java.util.HashMap;
import java.util.Vector;

public class ProcedureHandlerInfo
{
  private ProcedureDef  _procedureDef;
  private HashMap       _paramMap =null; //Depreciated As on 100203.
  private String        _fullPath = null;
  private String        _fileName = null;
  private Vector        _paramDef = null;

  public ProcedureHandlerInfo(      //Depreciated As on 100203.
    ProcedureDef procedureDef,
    HashMap paramMap,
    String fullPath,
    String filename)
  {
    _procedureDef = procedureDef;
    _paramMap = paramMap;
    _fullPath = fullPath;
    _fileName = filename;
  }

  public ProcedureHandlerInfo(
    ProcedureDef procedureDef,
    Vector paramDef,
    String fullPath,
    String filename)
  {
    _procedureDef = procedureDef;
    _paramDef = paramDef;
    _fullPath = fullPath;
    _fileName = filename;
  }


  public ProcedureDef getProcedureDef()
  {
    return _procedureDef;
  }

  public HashMap getParamMap()
  {
    return _paramMap;
  }

  public String getFullPath()
  {
    return _fullPath;
  }

  public String getFileName()
  {
    return _fileName;
  }

  public int getProcedureType()
  {
    return _procedureDef.getType();
  }

  public Vector getParamDef()
  {
    return _paramDef;
  }
}