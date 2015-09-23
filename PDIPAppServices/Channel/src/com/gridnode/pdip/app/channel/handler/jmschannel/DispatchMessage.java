/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DespatchMessage.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 20 2002    Jagadeesh             Created.
 * Jan 17 2003    Goh Kan Mun           Modified the name to DispatchMessage from
 *                                      JMSChannelInfo.
 */

package com.gridnode.pdip.app.channel.handler.jmschannel;

import com.gridnode.pdip.app.channel.model.ChannelInfo;

import java.io.File;
import java.io.Serializable;

public class DispatchMessage implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3087662563125869576L;
	private ChannelInfo _channelInfo;
  private String _data[];
  private File _file[];
  private String _header[];

  public DispatchMessage()
  {
  }

  public void setChannleInfo(ChannelInfo channelInfo)
  {
    _channelInfo = channelInfo;
  }

  public void setData(String data[])
  {
    _data = data;
  }

  public void setFile(File file[])
  {
    _file = file;
  }

  public void setHeader(String header[])
  {
    _header = header;
  }

  public ChannelInfo getChannelInfo()
  {
    return _channelInfo;
  }

  public String[] getData()
  {
    return _data;
  }

  public File[] getFile()
  {
    return _file;
  }

  public String[] getHeader()
  {
    return _header;
  }

  public String toString()
  {
    return _header + "  " + _channelInfo.toString();
  }

}