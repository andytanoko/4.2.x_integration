/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FormFileElement.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-23     Andrew Hill         Created
 * 2002-10-03     Daniel D'Cotta      Modified to keep track of files in B-Tier
 * 2003-01-21     Andrew Hill         toString()
 * 2003-08-01     Andrew Hill         transferAs property
 */
package com.gridnode.gtas.client.web.strutsbase;

import org.apache.struts.upload.FormFile;

public class FormFileElement
{
  // String id number to enable identifying this file in the list
  private String _id;

  // The actual file reference object created by struts when the file is uploaded from the browser
  private FormFile _formFile;

  // The filename of an existing file stored in the B-Tier // 03102002 DDJ
  private String _filename;                       // 03102002 DDJ

  // The fullpath of a file that has just been uploaded to the B-Tier  // 08102002 DDJ
  private String _uploadedFilename;                                    // 08102002 DDJ

  // Is this file to be uploaded to GTAS from the P-Tier or not?
  private boolean _isToUpload = true;

  // Is this a file existing in GTAS already that needs to be deleted?
  private boolean _isToDelete = false;
  
  private String _transferAs; //20030801AH

  public FormFileElement(String id, FormFile formFile)
  {
    if(id == null) throw new java.lang.NullPointerException("null id");
    if(formFile == null) throw new java.lang.NullPointerException("null formFile");
    _id = id;
    _formFile = formFile;
    _filename = formFile.getFileName(); //20030801AH
    _transferAs = _filename; //20030801AH
  }

  public FormFileElement(String id, String filename)                                // 03102002 DDJ
  {                                                                                 // 03102002 DDJ
    if(id == null) throw new java.lang.NullPointerException("null id");             // 03102002 DDJ
    if(filename == null) throw new java.lang.NullPointerException("null filename"); // 03102002 DDJ
    _id = id;                                                                       // 03102002 DDJ
    _filename = filename;
    _transferAs = filename; //20030801AH                                            // 03102002 DDJ
    _isToUpload = false;                                                            // 03102002 DDJ
  }                                                                                 // 03102002 DDJ

  void setToDelete(boolean flag)
  { _isToDelete = flag; }

  public boolean isToDelete()
  { return _isToDelete; }

  void setToUpload(boolean flag)
  { _isToUpload = flag; }

  public boolean isToUpload()
  { return _isToUpload; }

  void setId(String value)
  { _id = value; }

  public String getId()
  { return _id; }

  void setFormFile(FormFile formFile)
  { _formFile = formFile; }

  public FormFile getFormFile()
  { return _formFile; }

  public String getFileName()
  {
    /*if(_isToUpload)     // 03102002 DDJ
      return _formFile.getFileName();
    else                // 03102002 DDJ
      return _filename; // 03102002 DDJ*/
    return _filename; //20030801AH
  }

  public String toString()
  { //20030121AH
    //...look I even used a StringBuffer. Aren't I a good boy? ;-)
    StringBuffer buffer = new StringBuffer();
    buffer.append("FormFileElement[id=");
    buffer.append(getId());
    buffer.append(",fileName=");
    buffer.append(getFileName());
    buffer.append(",isToUpload=");
    buffer.append(isToUpload());
    buffer.append(",isToDelete=");
    buffer.append(isToDelete());
    buffer.append("]");
    return buffer.toString();
  }

  void setUploadedFilename(String value) //20030731AH - Made public
  {
    if(!isToUpload()) throw new java.lang.IllegalStateException("Cannot set uploadedFilename when isToUpload is false");
    _uploadedFilename = value;
  }

  public String getUploadedFilename()
  {
    if(!isToUpload()) throw new java.lang.IllegalStateException("Cannot get uploadedFilename when isToUpload is false");
    return _uploadedFilename;
  }
  
  public String getTransferAs()
  {
    return _transferAs;
  }

  public void setTransferAs(String string)
  {
    _transferAs = string;
  }

}