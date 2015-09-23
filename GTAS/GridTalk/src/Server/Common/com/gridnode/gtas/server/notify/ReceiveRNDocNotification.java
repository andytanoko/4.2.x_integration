/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveRNDocNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 13 2002    Koh Han Sing        Created
 * Jan 17 2003    Neo Sok Lay         Add header && dataReceived arrays
 * Jan 26 2004    Neo Sok Lay         Add isTransformationReq flag, ReceivedGdoc.
 * Mar 15 2004    Jagadeesh           Add from_route, to identify route from GM or GT1_GM.
 * Jan 25 2005    Mahesh              Add sigVerifyExMsg 
 */
package com.gridnode.gtas.server.notify;

import java.io.File;

/**
 * Notification message for the sending of a RosettaNet document.
 *
 */
public class ReceiveRNDocNotification
  extends    AbstractNotification
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8877710406434112252L;
	private File[] _filesReceived;
  private String[] _dataReceived;
  private String[] _header;
  private boolean _isTransformationReq;
  private Object _receivedGdoc;
  private int _from_route = -1;
  private String _sigVerifyExMsg;

  public ReceiveRNDocNotification(
    String[] header, String[] dataReceived, File[] filesReceived)
  {
    _filesReceived = filesReceived;
    _dataReceived = dataReceived;
    _header = header;
  }

  public ReceiveRNDocNotification(
    String[] header, String[] dataReceived, File[] filesReceived,
    boolean isTransformationReq, Object receivedGdoc)
  {
    this(header, dataReceived, filesReceived);
    _isTransformationReq = isTransformationReq;
    _receivedGdoc = receivedGdoc;
  }

  public ReceiveRNDocNotification(
    String[] header, String[] dataReceived, File[] filesReceived,
    boolean isTransformationReq, Object receivedGdoc,String sigVerifyExMsg)
  {
    this(header, dataReceived, filesReceived);
    _isTransformationReq = isTransformationReq;
    _receivedGdoc = receivedGdoc;
    _sigVerifyExMsg=sigVerifyExMsg;
  }
  
  public ReceiveRNDocNotification(
    String[] header, String[] dataReceived, File[] filesReceived,
    int from_route, Object receivedGdoc)
  {
    this(header, dataReceived, filesReceived);
    _from_route = from_route;
    _receivedGdoc = receivedGdoc;
  }


  public File[] getFilesReceived()
  {
    return _filesReceived;
  }

  public String[] getDataReceived()
  {
    return _dataReceived;
  }

  public String[] getHeader()
  {
    return _header;
  }

  public boolean isTransformationRequired()
  {
    return _isTransformationReq;
  }

  public Object getReceivedGdoc()
  {
    return _receivedGdoc;
  }

  public String getNotificationID()
  {
    return "ReceiveRNDoc";
  }

  public int getFromRoute()
  {
    return _from_route;
  }

//  public String toString()
//  {
//    StringBuffer buff = new StringBuffer(getNotificationID());
//    buff.append(" - Node[").append(getPartnerNode()).append("], State[").append(
//      getState()).append("]");
//
//    return buff.toString();
//  }

  public String getSigVerifyExMsg()
  {
    return _sigVerifyExMsg;
  }
}