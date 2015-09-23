/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportConfigAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-29     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.exportConfig;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.ctrl.IGTMheReference;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ExportConfigAForm extends GTActionFormBase
{
  private String _exportFile;
  private String[] _selectedEntities;
  private IGTMheReference _exportableEntities; 
 
  public ExportConfigAForm()
  {
    
  }
  
  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _selectedEntities = StaticUtils.EMPTY_STRING_ARRAY;
  }

  public IGTMheReference getExportableEntities()
  {
    return _exportableEntities;
  }

  public String getExportFile()
  {
    return _exportFile;
  }

  public String[] getSelectedEntities()
  {
    return _selectedEntities;
  }

  public void setExportableEntities(IGTMheReference exportableEntities)
  {
    _exportableEntities = exportableEntities;
  }

  public void setExportFile(String exportFile)
  {
    _exportFile = exportFile;
  }

  public void setSelectedEntities(String[] selectedEntities)
  {
    _selectedEntities = selectedEntities;
  }

}