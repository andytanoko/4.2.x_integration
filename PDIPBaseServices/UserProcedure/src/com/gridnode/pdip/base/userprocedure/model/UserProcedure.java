/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedure.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 30 2002    Jagadeesh               Created
 * Jul 08 2003    Koh Han Sing            Add in GridDocument field
 * Jul 18 2003    Neo Sok Lay             Change EntityDescr.
 */
package com.gridnode.pdip.base.userprocedure.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import java.util.Vector;

/**
 * This is an object model for ProcedureDef entity. A ProcedureDef keeps the inforamtion about
 * a Procedure Definition configured by the user.<P>
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a UserProcedure entity instance.
 *   Name             - Name of this UserProcedure entity.
 *   Description      - Description of this UserProcedure entity.
 *   IsSynchronous    - IsSynchronous - specify Sync or Async.
 *   ProcType         - Type of Procedure used.
 *   ProcDefFile      - Procedure Definition File information
 *   ProcDef          - Procedure Definition.
 *   ProcParamList    - List of ParamDef.
 *   ReturnDataType   - Return Data Type for this Procedure.
 *   DefAction        - Default Action.
 *   DefAlert         - Default Alert.
 *   ProcReturnLis    - List of ReturnDef.
 *   GridDocField     - GridDocument field to store return value
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Jagadeesh.
 *
 * @version GT 2.2 I1
 * @since 2.0
 *
 */
public class UserProcedure extends AbstractEntity
  implements IUserProcedure, IProcedureType, IAction, IDataType
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2614829425434523811L;
	private String  _name          = null;
  private String  _description   = null;
  private boolean _isSynchronous = true;
  private int     _procType      = IProcedureType.PROC_TYPE_EXEC;

  // procedure definition
  private ProcedureDef     _procDef     = null;
  private ProcedureDefFile _procDefFile = null;

  // procedure parameters
  private Vector _procParamList = null;

  // procedure returns
  private int     _returnDataType = 0;
  private int     _defAction      = 0;
  private Long    _defAlert       = null;
  private Vector  _procReturnList = null;
  private Integer _gridDocField   = null;

  public UserProcedure()
  {
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  public String getName()
  {
    return _name;
  }

  public String getDescription()
  {
    return _description;
  }

  public boolean isSynchronous()
  {
    return _isSynchronous;
  }

  public int getProcedureType()
  {
    return _procType;
  }

  public ProcedureDefFile getProcedureDefFile()
  {
    return _procDefFile;
  }

  public ProcedureDef getProcedureDef()
  {
    return _procDef;
  }

  public Vector getProcedureParamList()
  {
    return _procParamList;
  }

  public int getReturnDataType()
  {
    return _returnDataType;
  }

  public int getProcedureDefAction()
  {
    return _defAction;
  }

  public Long getProcedureDefAlert()
  {
    return _defAlert;
  }

  public Vector getProcedureReturnList()
  {
    return _procReturnList;
  }

  public Integer getGridDocField()
  {
    return _gridDocField;
  }

  public String getEntityDescr()
  {
    return new StringBuffer().append(getName()).append('/').append(getDescription()).toString();
  }

  public void setName(String name)
  {
   _name = name;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setIsSynchronous(boolean isSynchronous)
  {
     _isSynchronous = isSynchronous;
  }

  public void setProcedureType(int procedureType)
  {
    _procType = procedureType;
  }

  public void setProcedureDefFile(ProcedureDefFile procDefFile)
  {
    _procDefFile = procDefFile;
  }

  public void setProcedureDef(ProcedureDef procDef)
  {
    _procDef = procDef;
  }

  public void setProcedureParamList(Vector procedureParamList)
  {
    _procParamList = procedureParamList;
  }

  public void setReturnDataType(int returnDataType)
  {
    _returnDataType = returnDataType;
  }

  public void setProcedureDefAction(int procDefAction)
  {
    _defAction = procDefAction;
  }

  public void setProcedureDefAlert(Long procDefAlert)
  {
    _defAlert = procDefAlert;
  }

  public void setProcedureReturnList(Vector procReturnList)
  {
    _procReturnList = procReturnList;
  }

  public void setGridDocField(Integer gridDocField)
  {
    _gridDocField = gridDocField;
  }

}
