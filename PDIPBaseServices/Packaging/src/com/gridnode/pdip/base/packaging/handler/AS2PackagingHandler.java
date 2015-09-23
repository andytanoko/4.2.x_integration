/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AS2PackagingHandler.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Oct 29 2003      Guo Jianyu              Created
 * Aug 30 2006      Neo Sok Lay             Add PAYLOAD_FILENAME
 */
package com.gridnode.pdip.base.packaging.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gridnode.pdip.base.packaging.exceptions.PackagingException;
import com.gridnode.pdip.base.packaging.helper.PackagingInfo;
import com.gridnode.pdip.base.packaging.helper.PackagingLogger;
import com.gridnode.pdip.framework.messaging.IAS2Headers;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;

public class AS2PackagingHandler extends AbstractPackagingHandler implements IAS2Headers
{
  public static final String AS2_EVENT_ID = "8888"; //event ID for receiving AS2 messages

  public AS2PackagingHandler()
  {
  }

  public Message pack(PackagingInfo packagingInfo, Message message)
    throws PackagingException
  {
    Map AS2Headers = message.getMessageHeaders();

    if ((AS2Headers == null) || AS2Headers.isEmpty())
      throw new PackagingException("No AS2 headers specified!");

    Hashtable headers = new Hashtable();
    headers.put(MESSAGE_ID, AS2Headers.get(MESSAGE_ID));
    headers.put(AS2_VERSION, "1.1");  //supports compression
    headers.put(AS2_FROM, convertAS2Name((String)AS2Headers.get(AS2_FROM)));
    headers.put(AS2_TO, convertAS2Name((String)AS2Headers.get(AS2_TO)));
    if ("true".equalsIgnoreCase((String)AS2Headers.get(IS_ACK_REQ))) // ack required
      headers.put(DISPOSITION_NOTIFICATION_TO, "unused@unused.com");
    if ("true".equalsIgnoreCase((String)AS2Headers.get(IS_ACK_SIGNED))) // ack to be signed
      headers.put(DISPOSITION_NOTIFICATION_OPTIONS, "signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional,");
    if ("true".equalsIgnoreCase((String)AS2Headers.get(IS_ACK_REQ)) &&
      "false".equalsIgnoreCase((String)AS2Headers.get(IS_ACK_SYNC))) // asynchronous ack
      headers.put(RECEIPT_DELIVERY_OPTION, AS2Headers.get(RETURN_URL)); //return URL

    headers.put(CONTENT_TYPE, AS2Headers.get(CONTENT_TYPE)); //content-type
    headers.put(SUBJECT, AS2Headers.get(SUBJECT)); //subject

    headers.put("Connection", "close");

    Date now = new Date();
    SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss z");
    df.setTimeZone(TimeZone.getTimeZone("GMT")); // AS2 should use GMT

    headers.put(DATE, df.format(now));

    headers.put(PAYLOAD_FILENAME, AS2Headers.get(PAYLOAD_FILENAME)); //NSL20060830 payload filename, to retain for the MIME part filename
    
    PackagingLogger.debugLog("AS2PackagingHandler", "packageAndEnvelope",
      "headers are" + headers.toString());
    message.setMessageHeaders(headers);

    //No change to payload
    return message;
  }

  public Message unPack(PackagingInfo packagingInfo, Message message)
    throws PackagingException
  { /* nothing is done in this method. The bulk of the unpackaging will actually be
     * be done by AS2MessageHandler.
     */

    try
    {
      //set default header. Only the event-id is indeed needed.
      Map commonHeaders = message.getCommonHeaders();
      commonHeaders.put(ICommonHeaders.MSG_EVENT_ID, AS2_EVENT_ID);

      File[] files = new File[1];
      files[0] = File.createTempFile("AS2IncomingFile", null);

      FileOutputStream fos = new FileOutputStream(files[0]);

      Hashtable mimeHeader =  new Hashtable(message.getMessageHeaders());
      Enumeration keys = mimeHeader.keys();
      while (keys.hasMoreElements())
      {
        String key = (String)keys.nextElement();
        String line = key + ":" + mimeHeader.get(key) + "\r\n";
        fos.write(line.getBytes());
      }
      fos.write("\r\n".getBytes());

      fos.write(message.getPayLoadData());
      fos.close();

      message.setPayLoad(files);

      return message;
    }
    catch(Throwable t)
    {
      throw new PackagingException(t);
    }
  }

  /**
   * This method examines the input AS2 name and wraps it with a pair of double quotes
   * if the name contains characters which are not "!", and not in the range of 35-91
   * and not in the range of 93-126, inclusive.
   */
  private String convertAS2Name(String name)
  {
    char[] charArray = name.toCharArray();
    for (int i=0; i<charArray.length; i++)
    {
      if (charArray[i] == '!')
        continue;
      if (charArray[i] >= '#' && charArray[i] <= '[')
        continue;
      if (charArray[i] >= ']' && charArray[i] <= '~')
        continue;
      name = "\"" + name + "\"";
      return name;
    }

    return name;
  }
}