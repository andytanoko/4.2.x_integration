/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileType.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 18 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for FileType entity. A FileType is a
 * classification of a type of file. Example XML, PDF, GIF, etc<P>
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a FileType entity instance.
 *   FileType         - Name of the FileType(the extension of the filetype).
 *   Description      - Description of the FileType.
 *   ProgramName      - Name of the program used to open this filetype
 *   ProgramPath      - Path to the program
 *   Parameters       - Parameters needed by the program to open the filetype
 *   WorkingDirectory - Working directory of the program
 *   CanDelete        - Whether the FileType can be deleted.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class FileType
  extends    AbstractEntity
  implements IFileType
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -601493824797179938L;
	protected String  _fileType;
  protected String  _description;
  protected String  _programName;
  protected String  _programPath;
  protected String  _parameters;
  protected String  _workingDirectory;
 
  public FileType()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getName() + "/" + getDescription();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getName()
  {
    return _fileType;
  }

  public String getDescription()
  {
    return _description;
  }

  public String getProgramName()
  {
    return _programName;
  }

  public String getProgramPath()
  {
    return _programPath;
  }

  public String getParameters()
  {
    return _parameters;
  }

  public String getWorkingDirectory()
  {
    return _workingDirectory;
  }

  // *************** Setters for attributes *************************

  public void setName(String fileType)
  {
    _fileType = fileType;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setProgramName(String programName)
  {
    _programName = programName;
  }

  public void setProgramPath(String programPath)
  {
    _programPath = programPath;
  }

  public void setParameters(String parameters)
  {
    _parameters = parameters;
  }

  public void setWorkingDirectory(String workingDirectory)
  {
    _workingDirectory = workingDirectory;
  }
}