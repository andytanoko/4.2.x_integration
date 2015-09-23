/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: FTPFolderInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16, 2010   Tam Wei Xiang       Created
 * Oct 20, 2010   Tam Wei Xiang       #1830 - Add xpath for retrieving DocType
 */
package com.gridnode.ftp.model;

/**
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class FTPFolderInfo
{
  private String _be;
  private String _docType;
  private String _tp;
  private String _folder;
  private int _numFetch;
  private boolean _isMappingRequired;
  private String _xPath;
  private String _mappingRule;
  private String _docTypeXPath;
  
  public static final String DOCTYPE= ".docType";
  public static final String BE = ".be";
  public static final String PARTNER = ".partner";
  public static final String FOLDER = ".folder";
  public static final String NUM_FILE = ".num.fetch";
  public static final String IS_MAPPING_REQUIRED = ".isMappingRequired";
  
  public static final String X_PATH = ".xPath"; //xpath for retrieving partner id
  public static final String DOC_TYPE_XPATH = ".doctype.xPath";
  
  public static final String MAPPING_RULE = ".mappingRule";
  
  public FTPFolderInfo(String be, String tp, String docType, String folder, int numFetch, 
    boolean isMappingRequired, String xPath, String mappingRule)
  {
    setBe(be);
    setTp(tp);
    setDocType(docType);
    setFolder(folder);
    setNumFetch(numFetch);
    setIsMappingRequired(isMappingRequired);
    setXPath(xPath);
    setMappingRule(mappingRule);
  }
  
  public String toString()
  {
    return "be: "+getBe()+" tp: "+getTp()+" docType: "+getDocType()+" folder:"+getFolder()+" numFetch:"+getNumFetch() + 
      " isMappingRequired:"+getIsMappingRequired() + " xPath:"+getXPath() + " mappingRule:"+getMappingRule()+" docTypeXpath:"+getDocTypeXPath();
  }
  
  public int getNumFetch()
  {
    return _numFetch;
  }

  public void setNumFetch(int fetch)
  {
    _numFetch = fetch;
  }

  public String getFolder()
  {
    return _folder;
  }

  public void setFolder(String _folder)
  {
    this._folder = _folder;
  }

  public String getBe()
  {
    return _be;
  }

  public void setBe(String _be)
  {
    this._be = _be;
  }

  public String getDocType()
  {
    return _docType;
  }

  public void setDocType(String type)
  {
    _docType = type;
  }

  public String getTp()
  {
    return _tp;
  }

  public void setTp(String _tp)
  {
    this._tp = _tp;
  }
  
  public boolean getIsMappingRequired()
  {
    return _isMappingRequired;
  }
  
  public void setIsMappingRequired(boolean isMappingRequired)
  {
      
    _isMappingRequired = isMappingRequired;
  }
  
  public String getXPath()
  {
    return _xPath;
  }
  
  public void setXPath(String xPath)
  {
    _xPath = xPath; 
  }
  
  public String getMappingRule()
  {
    return _mappingRule;    
  }
  
  public void setMappingRule(String mappingRule)
  {
    _mappingRule = mappingRule;
  }

  public String getDocTypeXPath()
  {
    return _docTypeXPath;
  }

  public void setDocTypeXPath(String typeXPath)
  {
    _docTypeXPath = typeXPath;
  }
  
}