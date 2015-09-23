/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocChannelInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 26 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

public class DocChannelInfo
{
  private Long   channelUid = null;
  private String channelName = "";
  private String channelProtocol = "";

  public Long getChannelUid()
  {
    return channelUid;
  }

  public String getChannelName()
  {
    return channelName;
  }

  public String getChannelProtocol()
  {
    return channelProtocol;
  }

  public void setChannelUid(Long channelUid)
  {
    this.channelUid = channelUid;
  }

  public void setChannelName(String channelName)
  {
    this.channelName = channelName;
  }

  public void setChannelProtocol(String channelProtocol)
  {
    this.channelProtocol = channelProtocol;
  }

}