package com.gridnode.pdip.base.transport.helpers;
import com.gridnode.pdip.base.transport.comminfo.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BCSecurityUtil.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 31 March 2003  Qingsong            Initial creation for GTAS 2.0
 */

public class GNTransportInfo
{
  ICommInfo _commInfo;
  GNTransportPayload _payload;
  public GNTransportInfo()
  {
  }

  public GNTransportInfo(ICommInfo commInfo, GNTransportPayload payload)
  {
    _commInfo = commInfo;
    _payload = payload;
  }
  static public  GNTransportInfo load(byte[] buffer)
  {
    try
    {
      ByteArrayInputStream input = new ByteArrayInputStream(buffer);
      ObjectInputStream  obinput = new ObjectInputStream(input);
      GNTransportInfo transportino =  new GNTransportInfo();
      transportino._commInfo = (ICommInfo)obinput.readObject();
      transportino._payload = (GNTransportPayload)obinput.readObject();
      obinput.close();
      input.close();
      return transportino;

    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public  byte[] save() throws IOException
  {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      ObjectOutputStream  obout = new ObjectOutputStream(output);
      obout.writeObject(_commInfo);
      obout.writeObject(_payload);
      obout.close();
      output.close();
      return output.toByteArray();
  }
  public ICommInfo getCommInfo()
  {
    return _commInfo;
  }
  public GNTransportPayload getPayload()
  {
    return _payload;
  }
}