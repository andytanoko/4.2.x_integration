/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BusinessDocument.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.common.model;

import java.io.Serializable;

/**
 * This class is a wrapper class for the user document, attachment, and the intermediate document.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class BusinessDocument implements Serializable
{
  /**
   * Serial Version
   */
  private static final long serialVersionUID = -5770945199681408281L;
  private byte[] _doc;
  private String _filename;
  private Boolean _isRequiredPack;
  
  public BusinessDocument() 
  {
  }
  
  public BusinessDocument(byte[] doc, String filename, Boolean isRequiredUnpack)
  {
    setDoc(doc);
    setFilename(filename);
    setRequiredPack(isRequiredUnpack);
  }

  public byte[] getDoc()
  {
    return _doc;
  }

  public void setDoc(byte[] _doc)
  {
    this._doc = _doc;
  }

  public String getFilename()
  {
    return _filename;
  }

  public void setFilename(String _filename)
  {
    this._filename = _filename;
  }

  public Boolean isRequiredPack()
  {
    return _isRequiredPack;
  }

  public void setRequiredPack(Boolean requiredUnpack)
  {
    _isRequiredPack = requiredUnpack;
  }
  
  public String toString()
  {
    return "BusinessDocument[filename: "+getFilename()+" isRequiredPack: "+isRequiredPack()+" doc byte length "+(getDoc() == null ? 0+"": getDoc().length+"") +"]";
  }
}
