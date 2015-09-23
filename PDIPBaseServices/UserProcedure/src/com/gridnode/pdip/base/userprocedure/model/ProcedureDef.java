/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDef.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Sep 14 2002    Ang Meng Hua           Created
 */
package com.gridnode.pdip.base.userprocedure.model;

import com.gridnode.pdip.framework.db.entity.*;

/**
 * This is an object model for ProcedureDef entity. A ProcedureDef keeps the
 * information about a Procedure Definition configured by the user.<P>
 *
 * The Model:<BR><PRE>
 *   Type                    - Type of Procedures.
 *   Command Line Expression - Command Line Expression used by the Procedure.
 *
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Ang Meng Hua.
 *
 * @version 2.0
 * @since 2.0
 *
 */

public abstract class ProcedureDef extends AbstractEntity
{
  private int _type = 0;
  private String _cmdLineExpr = null;

  public ProcedureDef()
  {
  }

  public int getType()
  {
    return _type;
  }

  public String getCmdLineExpr()
  {
    return _cmdLineExpr;
  }

  public void setType(int type)
  {
    _type = type;
  }

  public void setCmdLineExpr(String cmdLineExpr)
  {
    _cmdLineExpr = cmdLineExpr;
  }
}
