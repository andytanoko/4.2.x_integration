/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public class GridTalkMappingRuleAForm extends GTActionFormBase
{
  // These fields are directly maintained by the GridTalkMappingRule entity
  private String _name;
  private String _description;
  private String _sourceDocType;
  private String _targetDocType;
  private String _sourceDocFileType;
  private String _targetDocFileType;
  private String _headerTransformation;
  private String _transformWithHeader;
  private String _transformWithSource;

  // These fields are maintained in the embedded MappingRule entity
  private String _mappingRuleType;
  private String _mappingFile;
  private String _refDocUid;
  private String _xpath;
  private String _paramName;
  private String _transformRefDoc;
  private String _keepOriginal;
  private String _mappingClass;

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

  public void setSourceDocType(String value)
  {
    _sourceDocType = value;
  }

  public String getSourceDocType()
  {
    return _sourceDocType;
  }

  public void setTargetDocType(String value)
  {
    _targetDocType = value;
  }

  public String getTargetDocType()
  {
    return _targetDocType;
  }

  public void setSourceDocFileType(String value)
  {
    _sourceDocFileType = value;
  }

  public String getSourceDocFileType()
  {
    return _sourceDocFileType;
  }

  public void setTargetDocFileType(String value)
  {
    _targetDocFileType = value;
  }

  public String getTargetDocFileType()
  {
    return _targetDocFileType;
  }

  public void setHeaderTransformation(String value)
  {
    _headerTransformation = value;
  }

  public String getHeaderTransformation()
  {
    return _headerTransformation;
  }

  public void setTransformWithHeader(String value)
  {
    _transformWithHeader = value;
  }

  public String getTransformWithHeader()
  {
    return _transformWithHeader;
  }

  public void setTransformWithSource(String value)
  {
    _transformWithSource = value;
  }

  public String getTransformWithSource()
  {
    return _transformWithSource;
  }

  // Fields in the embedded MappingRule

  public void setType(String value)
  {
    _mappingRuleType = value;
  }

  public String getType()
  {
    return _mappingRuleType;
  }

  public void setMappingFile(String uid)
  {
    _mappingFile = uid;
  }

  public String getMappingFile()
  {
    return _mappingFile;
  }

  public void setRefDocUid(String uid)
  {
    _refDocUid = uid;
  }

  public String getRefDocUid()
  {
    return _refDocUid;
  }

  public void setXpath(String value)
  {
    _xpath = value;
  }

  public String getXpath()
  {
    return _xpath;
  }

  public void setParamName(String value)
  {
    _paramName = value;
  }

  public String getParamName()
  {
    return _paramName;
  }

  public void setTransformRefDoc(String value)
  {
    _transformRefDoc = value;
  }

  public String getTransformRefDoc()
  {
    return _transformRefDoc;
  }

  public void setKeepOriginal(String value)
  {
    _keepOriginal = value;
  }

  public String getKeepOriginal()
  {
    return _keepOriginal;
  }
  
  public void setMappingClass(String mappingClass)
  {
    _mappingClass = mappingClass;
  }
  
  public String getMappingClass()
  {
    return _mappingClass;
  }

  public void doReset(ActionMapping p0, HttpServletRequest p1)
  {
    // Fields for checkboxes must be reset due to dumb way html checkboxes are(nt) submitted
    _headerTransformation = null;
    _transformWithHeader = null;
    _transformWithSource = null;
    _transformRefDoc = null;
    _keepOriginal = null;
  }
}