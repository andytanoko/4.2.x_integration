/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNTransportPayload.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 20 2002    Kan Mun                 Created
 * Dec 03 2002    Kan Mun                 Modified - Remove the backward compatible
 *                                                   dependency.
 * Dec 05 2002    Jagadeesh               Modified - Converted Header in String[] to
 *                                                   Hashtable.
 * Oct 05 2010    Tam Wei Xiang           #1897 - Add additional Contrustor and payloadFile instance
 *                                        variable.
 */

package com.gridnode.pdip.base.transport.helpers;

import java.io.File;
import java.util.Hashtable;



public class GNTransportPayload implements java.io.Serializable
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7051853016527856679L;
	private byte[] _fileContent = null;
  private String[] _data = null;
  private Hashtable _header = null;
  private File _payloadFile = null;

  /** @todo to be removed */
  public GNTransportPayload(Hashtable header, String[] data, byte[] fileContent)
  {
    setHeader(header);
    setData(data);
    setFileContent(fileContent);
//    TptLogger.debugLog("GNTransportPayload", "init", this.toString());
  }
  
  public GNTransportPayload(Hashtable header, String[] data, File fileContent)
  {
    setHeader(header);
    setData(data);
    setPayloadFile(fileContent);
  }

 //**************** Setter Methods for Data Object ***************************
  public void setFileContent(byte[] fileContent)
  {
    _fileContent = fileContent;
  }

  public void setData(String data[])
  {
    _data = data;
  }

  public void setHeader(Hashtable header)
  {
    _header = header;
  }

  //****************** Accessor Methods **************************************

  public Hashtable getHeader()
  {
    return _header;
  }

  public String[] getData()
  {
    return _data;
  }

  public byte[] getFileContent()
  {
    return _fileContent;
  }

  public File getPayloadFile()
  {
    return _payloadFile;
  }

  public void setPayloadFile(File file)
  {
    _payloadFile = file;
  }

  public String toString()
  {
    StringBuffer str = new StringBuffer();
    str.append("[GNTransportPayload: ");

    str.append("Header=[");
    str.append(_header.toString());
    str.append("]");
    return str.toString();
  }

}

