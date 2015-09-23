/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefFileAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.upload.FormFile;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;


public class ProcedureDefFileAForm extends GTActionFormBase
{
  public final String FILE_FIELDNAME = "fileName";

  private String _name;
  private String _description;
  private String _type;

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getDescription()
  { return _description; }

  public void setDescription(String description)
  { _description=description; }

  public String getType()
  { return _type; }

  public Integer getTypeInteger()
  {
    return StaticUtils.integerValue(_type);
  }

  public void setType(String type)
  { _type=type; }

  public void setFileName(String value)
  {
    addFileToField(FILE_FIELDNAME,value);
  }

  public String getFileName()
  {
    FormFileElement ffe = getFormFileElement(FILE_FIELDNAME);
    return (ffe != null) ? ffe.getFileName() : "";
  }

  public void setFileNameDelete(String[] deletions)
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField(FILE_FIELDNAME,deletions[i]);
    }
  }

  public void setFileNameUpload(FormFile file)
  {
    addFileToField(FILE_FIELDNAME,file);
  }
}