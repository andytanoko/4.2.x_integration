/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Port.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002    Koh Han Sing        Created
 * May 26 2003    Jagadeesh           Added Fileds for Port Enhancement.
 * Aug 23 2005    Tam Wei Xiang       i)Method getAttachmentDir() and 
 *                                    setAttachmentDir() have been removed.
 *                                    ii)Instance variable _attachmentDir has been
 *                                    removed
 *                                    GDOC will be put in the same folder as UDOC
 * March 01 2006  Tam Wei Xiang       Added new field 'fileGrouping'. This allows user
 *                                    specify how they organize the udoc, gdoc, and 
 *                                    attachment in a particular folder.                                    
 */

package com.gridnode.gtas.server.backend.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for Port entity. A Port defines a logical port
 * for importing and exporting of documents.<P>
 *
 * The Model:<BR><PRE>
 *   UId            - UID for a Port entity instance.
 *   Name           - Name of the Port.
 *   Desc           - Description of the Port.
 *   IsRfc          - Whether to use Rfc for import/export.
 *   Rfc            - Rfc to use.
 *   HostDir        - Directory on host to get/put documents.
 *   IsDiffFileName - Whether to use a different filename for imported/exported
 *                    documents.
 *   IsOverwrite    - Whether to overwrite existing documents with same names.
 *   FileName       - Filename to use for the imported/exported documents.
 *   IsAddFileExt   - Whether to append a file extension to the exported
 *                    document
 *   FileExtType    - The type of file extension to append, whether it is a
 *                    value from the Gdoc or a datetimestamp.
 *   AttachmentDir  - Directory to put the attachments
 *   IsExportGdoc   - Whether to export the Gdoc as a attachment.
 *   FileGrouping   - This allows user specify how they organize the udoc, gdoc, and 
 *                    attachment in a particular folder.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class Port
  extends    AbstractEntity
  implements IPort
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5552192972512466330L;
	protected String      _name;
  protected String      _description;
  protected Boolean     _isRfc           = Boolean.FALSE;
  protected Rfc         _rfc;
  protected String      _hostDir;
  protected Boolean     _isDiffFileName  = Boolean.TRUE;
  protected Boolean     _isOverwrite     = Boolean.TRUE;
  protected String      _fileName;
  protected Boolean     _isAddFileExt;
  protected Integer     _fileExtType;
  protected String      _fileExtValue;
  // commented by weixiang protected String      _attachmentDir;
  protected Boolean     _isExportGdoc    = Boolean.FALSE;
  protected Integer     _startNum;
  protected Integer     _rolloverNum;
  protected Integer     _nextNum;
  protected Boolean     _isPadded        = Boolean.FALSE;
  protected Integer     _fixNumLength;
  protected Integer     _fileGrouping;


  public Port()
  {
  }

  // ******************* Methods from AbstractEntity ************************

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

  // ****************** Getters for attributes *****************************

  public String getName()
  {
    return _name;
  }

  public String getDescription()
  {
    return _description;
  }

  public Boolean isRfc()
  {
    return _isRfc;
  }

  public Rfc getRfc()
  {
    return _rfc;
  }

  public String getHostDir()
  {
    return _hostDir;
  }

  public String getFileName()
  {
    return _fileName;
  }

  public Boolean isDiffFileName()
  {
    return _isDiffFileName;
  }

  public Boolean isOverwrite()
  {
    return _isOverwrite;
  }

  public Integer getFileExtType()
  {
    return _fileExtType;
  }

  public String getFileExtValue()
  {
    return _fileExtValue;
  }

  public Boolean isAddFileExt()
  {
    return _isAddFileExt;
  }
  
  /* commented by Wei Xiang	
  public String getAttachmentDir()
  {
    return _attachmentDir;
  } */

  public Boolean isExportGdoc()
  {
    return _isExportGdoc;
  }

  public Integer getStartNumber()
  {
    return _startNum;
  }

  public Integer getRolloverNumber()
  {
    return _rolloverNum;
  }

  public Integer getNextNumber()
  {
    return _nextNum;
  }

  public Boolean isPadded()
  {
    return _isPadded;
  }

  public Integer getFixedNumberLen()
  {
    return _fixNumLength;
  }
  
  public Integer getFileGrouping()
  {
  	return _fileGrouping;
  }
  
  // *************************** Setters for attributes *********************

  public void setName(String name)
  {
    _name = name;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setIsRfc(Boolean isRfc)
  {
    _isRfc = isRfc;
  }

  public void setRfc(Rfc rfc)
  {
    _rfc = rfc;
  }

  public void setHostDir(String hostDir)
  {
    _hostDir = hostDir;
  }

  public void setFileName(String filename)
  {
    _fileName = filename;
  }

  public void setOverwrite(Boolean overwrite)
  {
    _isOverwrite = overwrite;
  }

  public void setIsDiffFileName(Boolean isDiffFileName)
  {
    _isDiffFileName = isDiffFileName;
  }

  public void setFileExtType(Integer fileExtType)
  {
    _fileExtType = fileExtType;
  }

  public void setFileExtValue(String fileExtValue)
  {
    _fileExtValue = fileExtValue;
  }

  public void setIsAddFileExt(Boolean isAddFileExt)
  {
    _isAddFileExt = isAddFileExt;
  }
  
  /* commented by Wei Xiang	
  public void setAttachmentDir(String attachmentDir)
  {
    _attachmentDir = attachmentDir;
  } */

  public void setIsExportGdoc(Boolean isExportGdoc)
  {
    _isExportGdoc = isExportGdoc;
  }

  public void setStartNumber(Integer startNum)
  {
    _startNum = startNum;
  }

  public void setRolloverNumber(Integer rolloverNum)
  {
    _rolloverNum = rolloverNum;
  }

  public void setNextNumber(Integer nextNum)
  {
    _nextNum = nextNum;
  }

  public void setIsPadded(Boolean isPadded)
  {
    _isPadded = isPadded;
  }

  public void setFixedNumberLen(Integer fixedNumLen)
  {
    _fixNumLength = fixedNumLen;
  }
  
  public void setFileGrouping(Integer fileGrouping)
  {
  	_fileGrouping = fileGrouping;
  }
}
