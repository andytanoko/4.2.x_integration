/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RniftUtil.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15 2007			Alain Ah Ming					Created
 * 																				Copied and modified from 
 * 																					com.gridnode.testkit.inovis.http.RnifHttpClient
 */
package com.gridnode.gridtalk.tester.loopback.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.gridnode.gridtalk.tester.loopback.log.Logger;

/**
 * Utility class for RNIF transport purposes
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class RniftUtil
{
	private static final String HTTP_REQUEST_METHOD_POST = "POST";
	private static final String HTTP_REQUEST_METHOD_GET = "GET";
	private int _httpResponseCode = 0;
	private String _httpResponseMessage = null;
	
	public RniftUtil()
	{
		
	}
//  private final static String CONTENT_ID_PATTERN = "<{0}.{1}.GridNode.{2}@{3}>";
//  private final static String PREAMPLE_CID_PLACEHOLDER = "###_PREAMBLE_CID_###";
//  private final static String DELIVERY_CID_PLACEHOLDER = "###_DELIVERY_CID_###";
//  private final static String DELIVERY_TS_PLACEHOLDER = "###_DELIVERY_TS_###";
//  private final static String RECEIVER_DUNS_PLACEHOLDER = "###_RECEIVER_DUNS_###";
//  private final static String SENDER_DUNS_PLACEHOLDER = "###_SENDER_DUNS_###";
//  private final static String MSG_TRACKING_ID_PLACEHOLDER = "###_MSG_TRACKING_ID_###";
//  private final static String SERVICEH_CID_PLACEHOLDER = "###_SERVICEH_CID_###";
//  private final static String PIP_INST_ID_PLACEHOLDER = "###_PIP_INST_ID_###";
//  private final static String SERVICEC_CID_PLACEHOLDER = "###_SERVICEC_CID_###";
//  private final static String DOC_GEN_TS_PLACEHOLDER = "###_DOC_GEN_TS_###";
//  private final static String DOC_ID_PLACEHOLDER = "###_DOC_ID_###";
//  private final static String INITIATOR_DUNS_PLACEHOLDER = "###_INITIATOR_DUNS_###";
//  private final static String SRC_MSG_TRACKING_ID_PLACEHOLDER = "###_SRC_MSG_TRACKING_ID_###";
	
	/**
	 * Sends an RNIF/ACK message
	 * @param httpUrl The destination URL
	 * @param rnifRequest The RNIF message
	 * @param msgType The content type
	 * @param rnifVersion The RNIF version
	 * @param respType The response type
	 * @throws IOException 
	 */
	public boolean send(URL httpUrl, String rnifRequest, String msgType, String rnifVersion, String respType) throws IOException
	{
		String mn = "sendRnifMsg";
	  HttpURLConnection httpCon = null;
	
	  httpCon = (HttpURLConnection)httpUrl.openConnection();
	  byte[] payload = rnifRequest.getBytes();
	  
	  HttpURLConnection.setFollowRedirects(true);
	  httpCon.setRequestMethod(HTTP_REQUEST_METHOD_POST);
	  httpCon.setDoInput(true);
	  httpCon.setDoOutput(true);
	  httpCon.setUseCaches(false);
	  httpCon.setAllowUserInteraction(true);
	  payload = wrapContent(payload, msgType, httpCon);
	  httpCon.setRequestProperty("x-RN-Version", rnifVersion);
	  httpCon.setRequestProperty("x-RN-Response-Type", respType);
	  httpCon.setRequestProperty("mime-version", "1.0");
	  httpCon.setRequestProperty("Content-Length", payload.length+"");
//  System.out.println("payload length is "+payload.length);
  
	  OutputStream os = httpCon.getOutputStream();
	  os.write(payload);
	  
	  _httpResponseCode = httpCon.getResponseCode();
	  _httpResponseMessage = httpCon.getResponseMessage();
	  
	  Logger.debug(this.getClass().getSimpleName(), "sendRnif", "Response Code: "+_httpResponseCode);
	  Logger.debug(this.getClass().getSimpleName(), "sendRnif", "Response Code: "+_httpResponseMessage);
  
  		return (_httpResponseCode == HttpURLConnection.HTTP_ACCEPTED || _httpResponseCode == HttpURLConnection.HTTP_OK);
//  	throw new Exception("HTTP response code: "+respCode);
  
//  String respMsg = httpCon.getResponseMessage();
//  System.out.println("Return code="+respCode + ", msg="+respMsg);
//  String detailResp = readContent(httpCon);
//  System.out.println("Detail Response = "+detailResp);
}


  private static byte[] wrapContent(byte[] content, String type, HttpURLConnection conn)
  {
    String boundary = "----" + createNewMsgId();
    String startboundary = "--" + boundary;
    String endboundary = startboundary + "--";
    content = (startboundary + "\r\n" + new String(content) + "\r\n" + endboundary + "\r\n").getBytes();
    String contentType = "multipart/related; type=\"" + type + "\"; boundary=\"" + boundary + "\"";
    conn.setRequestProperty("Content-Type", contentType);
    
    return content;
  }
  
  private static String createNewMsgId()
  {
    Calendar cal = new GregorianCalendar();
    return "GNRNUID" + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH)
            + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND);
  }

	public int getHttpResponseCode()
	{
		return _httpResponseCode;
	}
	public String getHttpResponseMessage()
	{
		return _httpResponseMessage;
	}

//  private static String readContent(HttpURLConnection conn) throws IOException
//  {
//    int contentLength = conn.getContentLength();
//    if (contentLength > 0)
//    {
//      char[] content = new char[contentLength];
//      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//      reader.read(content);
//      return String.valueOf(content);
//    }
//    else
//      return "";
//  }  
//  
//  private static String findPipInstId(String content)
//  {
//    return findValue(content, ConfigMgr.getMainConfig().getGtOutboundRnifPipInstIdPath());
//  }
//  
//  private static String findReceiverDuns(String content)
//  {
//    String[] searchTokens = {
//        "<DeliveryHeader>", "</DeliveryHeader>",
//        "<messageReceiverIdentification>", "</messageReceiverIdentification>",
//        "<GlobalBusinessIdentifier>", "</GlobalBusinessIdentifier>",
//    };
//    return search(content, searchTokens);
//  }
//
//  private static String findMsgTrackingId(String content)
//  {
//    String[] searchTokens = {
//        "<DeliveryHeader>", "</DeliveryHeader>",
//        "<messageTrackingID>", "</messageTrackingID>",
//        "<InstanceIdentifier>", "</InstanceIdentifier>",
//    };
//    return search(content, searchTokens);
//  }
//
//  private static String findInitiatorDuns(String content)
//  {
//    String[] searchTokens = {
//        "<ServiceHeader>", "</ServiceHeader>",
//        "<KnownInitiatingPartner>", "</KnownInitiatingPartner>",
//        "<GlobalBusinessIdentifier>", "</GlobalBusinessIdentifier>",
//    };
//    return search(content, searchTokens);
//  }
//
//  public static String findValue(String content, String commaDelimitedTokenList)
//  {
//  	String[] tokenList = constructTokenList(commaDelimitedTokenList);
//  	String value = search(content, tokenList);
//  	return value;
//  }
//  
//  private static String[] constructTokenList(String commaDelimitedTokenList)
//  {
//  	StringTokenizer st = new StringTokenizer(commaDelimitedTokenList,", ");
//  	String[] tokenList = new String[st.countTokens()];
//  	int count = 0;
//  	while(st.hasMoreTokens())
//  	{
//  		tokenList[count++]=st.nextToken();
//  	}
//  	return tokenList;
//  }
//  
//  private static String search(String content, String[] searchTokens)
//  {
//    int start = 0;
//    int end = -1;
//    for (int i=0; i<searchTokens.length; i++)
//    {
//      int idx = content.indexOf(searchTokens[i], start);
//      if (idx < 0)
//      {
//        return null; //not found
//      }
//      if (i % 2 == 0) // start token 
//      {
//        start = idx + searchTokens[i].length();
//      }
//      else //end token
//      {
//        end = idx;
//      }
//    }
//    return content.substring(start, end);
//  }
  
//	public static boolean validateRnAckFromGt(String testId, String ackMessage)
//	{
//		throw new UnsupportedOperationException("TODO");
//	}
//	
//  public static byte[] generateActionMsg(String pipInstId)
//  {
//    String preambleContentID = genContentID();
//    String deliveryContentID = genContentID();
//    String serviceHeaderContentID = genContentID();
//    String serviceContentContentID = genContentID();
//    String deliverTs = genTs();
//    String senderDuns = ConfigMgr.getMainConfig().getDuns();
//    String receiverDuns = ConfigMgr.getRnifHubConfig().getDuns();
//    String initiatorDuns = senderDuns;
//    String msgTrackingID = "1";
////    String pipInstID = pip;
//    String docGenTs = genTs();
//    String docID = UUID.randomUUID().toString();
//    
//    try
//    {
//      String template = getContent(ConfigMgr.getRnifHubConfig().getActionTemplateFile());
//      
//      String content = template.replaceAll(PREAMPLE_CID_PLACEHOLDER, preambleContentID);
//      content = content.replaceAll(DELIVERY_CID_PLACEHOLDER, deliveryContentID);
//      content = content.replaceAll(SERVICEH_CID_PLACEHOLDER, serviceHeaderContentID);
//      content = content.replaceAll(SERVICEC_CID_PLACEHOLDER, serviceContentContentID);
//      content = content.replaceAll(DELIVERY_TS_PLACEHOLDER, deliverTs);
//      content = content.replaceAll(SENDER_DUNS_PLACEHOLDER, senderDuns);
//      content = content.replaceAll(RECEIVER_DUNS_PLACEHOLDER, receiverDuns);
//      content = content.replaceAll(INITIATOR_DUNS_PLACEHOLDER, initiatorDuns);
//      content = content.replaceAll(MSG_TRACKING_ID_PLACEHOLDER, msgTrackingID);
//      content = content.replaceAll(PIP_INST_ID_PLACEHOLDER, pipInstId);
//      content = content.replaceAll(DOC_GEN_TS_PLACEHOLDER, docGenTs);
//      content = content.replaceAll(DOC_ID_PLACEHOLDER, docID);
//      
//      Properties actionProp = ConfigMgr.getRnifHubConfig().getActionProperties();
//      Enumeration keys = actionProp.propertyNames();
//      while (keys.hasMoreElements())
//      {
//        String key = (String)keys.nextElement();
//        String val = actionProp.getProperty(key);
//        content = content.replaceAll(key, val);
//      }
//      return content.getBytes();
//    }
//    catch (IOException ex)
//    {
//      Logger.error("RnifUtil", "generateActionMsg", "Error reading action template "+ConfigMgr.getRnifHubConfig().getActionTemplate(), ex);
//    }
//    return null;
//  }
//  
//  public static byte[] generateAckMsg(String actionContent)
//  {
//    String pipInstId = findPipInstId(actionContent);
//    String receiverDuns = findReceiverDuns(actionContent);
//    String srcMsgTrackingId = findMsgTrackingId(actionContent);
//    String initiatorDuns = findInitiatorDuns(actionContent);
//    String preambleContentID = genContentID();
//    String deliveryContentID = genContentID();
//    String serviceHeaderContentID = genContentID();
//    String serviceContentContentID = genContentID();
//    String deliverTs = genTs();
//    String senderDuns = ConfigMgr.getRnifHubConfig().getDuns();
//    String msgTrackingID = "reply_"+srcMsgTrackingId;
//   
//    try
//    {
//      String template = getContent(ConfigMgr.getRnifHubConfig().getAckTemplateFile());
//      
//      String content = template.replaceAll(PREAMPLE_CID_PLACEHOLDER, preambleContentID);
//      content = content.replaceAll(DELIVERY_CID_PLACEHOLDER, deliveryContentID);
//      content = content.replaceAll(SERVICEH_CID_PLACEHOLDER, serviceHeaderContentID);
//      content = content.replaceAll(SERVICEC_CID_PLACEHOLDER, serviceContentContentID);
//      content = content.replaceAll(DELIVERY_TS_PLACEHOLDER, deliverTs);
//      content = content.replaceAll(SENDER_DUNS_PLACEHOLDER, receiverDuns);
//      content = content.replaceAll(RECEIVER_DUNS_PLACEHOLDER, senderDuns);
//      content = content.replaceAll(INITIATOR_DUNS_PLACEHOLDER, initiatorDuns);
//      content = content.replaceAll(MSG_TRACKING_ID_PLACEHOLDER, msgTrackingID);
//      content = content.replaceAll(PIP_INST_ID_PLACEHOLDER, pipInstId);
//      content = content.replaceAll(SRC_MSG_TRACKING_ID_PLACEHOLDER, srcMsgTrackingId);
//      
//      Properties ackProp = ConfigMgr.getRnifHubConfig().getAckProperties();
//      Enumeration keys = ackProp.propertyNames();
//      while (keys.hasMoreElements())
//      {
//        String key = (String)keys.nextElement();
//        String val = ackProp.getProperty(key);
//        content = content.replaceAll(key, val);
//      }
//      return content.getBytes();
//    }
//    catch (IOException ex)
//    {
//      Logger.error("RnifUtil",
//                   "generateAckMsg",
//                   "Error reading action template "+ConfigMgr.getRnifHubConfig().getAckTemplate(),
//                   ex);
//    }
//    return null;
//  }
    
//  private static String genContentID()
//  {
//    String param0 = String.valueOf(_random.nextInt(Integer.MAX_VALUE));
//    String param1 = String.valueOf(System.currentTimeMillis());
//    String param2 = System.getProperty("user.name");
//    String param3;
//    try
//    {
//      param3 = InetAddress.getLocalHost().getHostName();
//    }
//    catch (UnknownHostException ex)
//    {
//      param3 = "unknown.host";
//    }
//    return MessageFormat.format(CONTENT_ID_PATTERN, param0, param1, param2, param3);
//  }
//
//  private static String genTs()
//  {
//    SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd'T'hhmmss'.'SSS'Z'");
//    Date   currentTime_1 = new Date();
//    return formatter.format(currentTime_1);
//  }
//  
//  private static String getContent(File f) throws IOException
//  {
//    FileInputStream is = new FileInputStream(f);
//    byte[] buff = new byte[1024];
//    int readLen = -1;
//    StringBuffer content = new StringBuffer();
//    while ((readLen=is.read(buff))>0)
//    {
//      content.append(new String(buff, 0, readLen));
//    }    
//    return content.toString();
//  }
//  
}
