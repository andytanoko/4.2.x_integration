/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-15     Andrew Hill         Created
 * 2002-10-03     Daniel D'Cotta      Modified to use FormFileElement
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;

public class MappingFileAForm extends GTActionFormBase
{
  public final String FILE_FIELDNAME = "filename";

  private String _name;
  private String _description;
  private String _type;
  private String _subPath;

  public void setName(String value)
  {
    _name = value;
  }

  public String getName()
  {
    return _name;
  }

  public void setDescription(String value)
  {
    _description = value;
  }

  public String getDescription()
  {
    return _description;
  }

  public void setFilename(String value)
  {
    addFileToField(FILE_FIELDNAME,value);
  }

  public String getFilename()
  {
    FormFileElement ffe = getFormFileElement(FILE_FIELDNAME);
    return (ffe != null) ? ffe.getFileName() : "";
  }

  public void setFilenameDelete(String[] deletions) // 03102002 DDJ
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField(FILE_FIELDNAME,deletions[i]);
    }
  }

  public void setFilenameUpload(FormFile file)      // 03102002 DDJ
  {
    addFileToField(FILE_FIELDNAME,file);
  }

  public void setType(String value)
  {
    _type = value;
  }

  public String getType()
  {
    return _type;
  }
  
  public void setSubPath(String value)
  {
    _subPath = value;
  }

  public String getSubPath()
  {
    return _subPath;
  }

  public void doReset(ActionMapping p0, HttpServletRequest p1)
  {
//    _file = null;
  }
}