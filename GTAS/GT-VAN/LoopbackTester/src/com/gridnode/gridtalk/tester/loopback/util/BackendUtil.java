/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendUtil.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.util;

import com.gridnode.gridtalk.tester.loopback.cfg.ConfigMgr;
import com.gridnode.gridtalk.tester.loopback.helpers.BackendMessageHelper;
import com.gridnode.gridtalk.tester.loopback.log.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class BackendUtil
{
  private static final String HTTP_POST = "POST";
  private static final String REQ_PROP_SENDER = "sender";
  private static final String REQ_PROP_RECIPIENT = "recipient";
  private final static String REQ_PROP_DOCTYPE = "doctype";
//  private final static String REQ_PROP_FILENAME = "filename";

	public static boolean validateBackendMessageFromGt(String testId, String backendMessage) throws IOException
	{
		BackendMessageHelper msgHelper = new BackendMessageHelper();
		String docId = msgHelper.findDocId(backendMessage);
		return docId != null && testId != null && docId.equals(testId);
	}
	
	  
	  
	  public static void send(URL httpUrl, byte[] payload, String contentType) throws Exception
	  {
	  	String mn = "send";
	  	Logger.debug(BackendUtil.class.getSimpleName(), mn, "Destination: "+httpUrl);
	    HttpURLConnection httpCon = null;
      httpCon = (HttpURLConnection)httpUrl.openConnection();
      
      HttpURLConnection.setFollowRedirects(true);
      httpCon.setRequestMethod(HTTP_POST);
      httpCon.setDoInput(true);
      httpCon.setDoOutput(true);
      httpCon.setUseCaches(false);
      httpCon.setAllowUserInteraction(true);
      
      httpCon.setRequestProperty("mime-version", "1.0");
      httpCon.setRequestProperty("Content-Length", payload.length+"");
      httpCon.setRequestProperty("Content-Type", contentType);

      httpCon.setRequestProperty(REQ_PROP_RECIPIENT, ConfigMgr.getPartnerConfig().getPartnerId());
      httpCon.setRequestProperty(REQ_PROP_SENDER, ConfigMgr.getBackendConfig().getBeId());
      httpCon.setRequestProperty(REQ_PROP_DOCTYPE, ConfigMgr.getBackendConfig().getDocType());
      
//      httpCon.setRequestProperty(REQ_PROP_FILENAME, )

//      Log("payload length is "+payload.length);
      
//      Logger.debug("BackendUtil", mn, "Payload: " + new String(payload));
      OutputStream os = httpCon.getOutputStream();
      os.write(payload);
      
      int respCode = httpCon.getResponseCode();
      String responseMsg = httpCon.getResponseMessage();
      Logger.debug("BackendUtil", mn, "ResponseCode: " + respCode);
      Logger.debug("BackendUtil", mn, "ResponseMsg: " + responseMsg);
      if(respCode != HttpURLConnection.HTTP_ACCEPTED && respCode != HttpURLConnection.HTTP_OK)
      	throw new Exception("HTTP response code: "+respCode);
	  }	
}
