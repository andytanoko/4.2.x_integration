/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefFile.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 31 2002    Jagadeesh              Created
 * Jul 18 2003    Neo Sok Lay             Change EntityDescr.
 */
package com.gridnode.pdip.base.userprocedure.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for ProcedureDefFile entity. A ProcedureDef keeps the inforamtion about
 * a Procedure Definition configured by the user.<P>
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a ProcedureDef entity instance.
 *   Name             - Name of this ProcedureDef entity.
 *   Description      - Description of this ProcedureDef entity.
 *   Type             - Type of File Definition.
 *   File Name        - File Name of Procedure.
 *   File Path        - File Path of Procedure.
 *
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
 */
public class ProcedureDefFile extends AbstractEntity
                              implements IProcedureDefFile
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9099595776426643301L;
	private String _name = null;
  private String _description = null;
  private int _type=0;
  private String _fileName = null;
  private String _filePath = null;

  public ProcedureDefFile()
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

  public int getType()
  {
    return _type;
  }

  public String getFileName()
  {
    return _fileName;
  }

  public String getFilePath()
  {
    return _filePath;
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

  public void setType(int type)
  {
    _type = type;
  }

  public void setFileName(String fileName)
  {
    _fileName = fileName;
  }

  public void setFilePath(String filePath)
  {
    _filePath = filePath;
  }
}
