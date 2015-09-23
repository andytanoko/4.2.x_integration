/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSCommInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ??? ?? ????                        Created
 * Oct 17 2003    Neo Sok Lay         Removed Get/Set TptImplVersion.
 *                                    Override parseAndSetURL() for JMS specific
 *                                    parsing.
 * Dec 04 2003    Neo Sok Lay         Remove constructor JMSCommInfo(URL).
 *                                    - replace by setURL(URL).
 */
package com.gridnode.pdip.base.transport.comminfo;

import java.util.HashMap;
import java.util.StringTokenizer;

public class JMSCommInfo extends AbstractCommInfo
                        implements IJMSCommInfo
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4891305348820570938L;
	private static final String DESTINATION ="destination";
  private static final String DESTINATION_TYPE = "destinationtype";
  private static final String DEFAULT_JMS_PROTOCOL = "smq";
  private static final String DEFAULT_JMS_DESTINATION_TYPE = "TOPIC";
  private static final String UNKNOWN_DESTINATION="UNKNOWNDESTINATION";

  private static final String URI_DELIMETER = "?"; //Can be moved up to ICommInfo
  private int _destinationType;
  private String _destination=null;
  //private String _destTypeName=null;
  private String _tptImplVersion = null;
  //private String[] keys = {};
  //private HashMap values = new HashMap();
  //private Vector values = new Vector();

  public JMSCommInfo()
  {

  }

  /*031204NSL
  public JMSCommInfo(String url)
  {
    super(url);
    parseJMSURL(url);
  }
  */

   public String getProtocolType()
   {
     return IJMSCommInfo.JMS;
   }

  /**
   * This getter returns the version of the CommInfo
   *
   * @return  the version of the CommInfo
   *
   * @since 1.0
   */
  /*031017NSL
  public String getTptImplVersion()
  {
    return _tptImplVersion;
  }
  */

  /**
   * This getter returns the version of the protocol used
   *
   * @return  the protocol version
   *
   * @since 1.0
   */
  public String getProtocolVersion()
  {
    return IJMSCommInfo.JMS_VERSION;
  }

  /**
   * This getter returns the destination(topic or queue) name.
   *
   * @return the topic/queue name
   *
   * @since 1.0
   */
  public String getDestination()
  {
     //return (String)values.get(1);
    return _destination;
  }

  /**
   * This getter returns the destination type, i.e. topic or queue.
   * The return value can be either TOPIC or QUEUE, as defined in this interface
   *
   * @return  the destination type
   *
   * @since 1.0
   */
  public int getDestType()
  {
    //return getDestTypeByName((String)values.get(0));
    return _destinationType;
  }


  private void parseJMSURL(String url)
  {
    String uri = url.substring(url.lastIndexOf("/")+1);
    System.out.println("URI  "+uri);
    HashMap values = new HashMap();
    StringTokenizer stk1 = new StringTokenizer(uri,URI_DELIMETER);
    while(stk1.hasMoreTokens())
    {
      String token = stk1.nextToken();
      int i = token.indexOf("=");
      String key   = i<0 ? null : token.substring(0,i);
      if (key != null)
      {
        String value = token.substring(i+1);
        //values.add(value);
        System.out.println("[Key="+key+"][Value="+value+"]");
        if (values == null)
          values = new HashMap();
        if (value != null)
          values.put(key.toLowerCase(), value);
      }
    }
    setDestType(getDestTypeByName((String)values.get(DESTINATION_TYPE)));
    setDestination((String)values.get(DESTINATION));
  }

  /***** Setter Methods ******************/
  /*031017NSL
  public void setTptImplVersion(String tptImplVersion)
  {
    _tptImplVersion = tptImplVersion;
  }
  */

  public void setDestType(int destinationType)
  {
    _destinationType = destinationType;
  }

//  public void setDestTypeName(String destTypeName)
//  {
//    _destTypeName = destTypeName.toUpperCase();
//  }

  public void setDestination(String destination)
  {
    _destination = destination;
  }

  private String getDestTypeName(int destType)
  {
    String name = DEFAULT_JMS_DESTINATION_TYPE;
    switch (destType)
    {
      case TOPIC : name = "TOPIC";
                   break;
      case QUEUE : name = "QUEUE";
                   break;
    }

    return name;
  }

  private int getDestTypeByName(String name)
  {
    int destinationType = IJMSCommInfo.TOPIC;
    if (name != null)
    {
      if ("TOPIC".equals(name.toUpperCase()))
      {
        destinationType = IJMSCommInfo.TOPIC;
      }
      else if( "QUEUE".equals(name.toUpperCase()) )
      {
        destinationType = IJMSCommInfo.QUEUE;
      }
    }
    return destinationType;
  }

//  public String getDestTypeName()
//  {
//    return _destTypeName;
//  }

/*  public String getURL()
  {
    _url = super.getURL();
    return _url+getJMSURL();
  }
*/

  protected String parseAndGetURL()
  {
    StringBuffer sb = new StringBuffer();
    sb.append((getProtocol() == null ? DEFAULT_JMS_PROTOCOL : getProtocol()));
    sb.append("://");
    if(getUser() != null)
    {
      sb.append(getUser());
      if(!checkNullEmpty(getPassword()))
      {
        sb.append(DELIMETER);
        sb.append(getPassword());
      }
      sb.append("@");
    }
    sb.append(checkNullEmpty(getHost()) ? UNKNOWN_HOST : getHost());
    sb.append(DELIMETER);
    sb.append(getPort());
    return sb.toString();
  }

/*
  private String getJMSURL()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("/");
    for(int i=0;i<keys.length;i++)
    {
      sb.append(keys[i]);
      sb.append("=");
      sb.append(values.get(i));
      sb.append(URI_DELIMETER);
    }
    sb.deleteCharAt(sb.length()-1);
    return sb.toString();
  }
*/
  public String toString()
  {
     String str = "[{ User="+getUser()+","+"Password="+getPassword()+","+
     "Destination="+getDestination()+","+"}]";
     return str;
  }


  public String toURL()
  {
    String paramURL = parseAndGetURL();
    StringBuffer sb = new StringBuffer(checkNullEmpty(paramURL) ? "" : paramURL);
    sb.append("/");
    sb.append(DESTINATION_TYPE);
    sb.append("=");
    sb.append(getDestTypeName(getDestType()));
    //checkNullEmpty(_destTypeName) ? DEFAULT_JMS_DESTINATION_TYPE: _destTypeName );

    sb.append(URI_DELIMETER);
    sb.append(DESTINATION);
    sb.append("=");
    sb.append(checkNullEmpty(_destination) ? UNKNOWN_DESTINATION : _destination);
    return sb.toString();
  }

  private boolean checkNullEmpty(String param)
  {
   return(param == null || param.trim().equals(""));
  }


  public static void main(String args[]) throws Exception
  {
//    SJMSCommInfo info = new SJMSCommInfo("smqr://www.localhost.com:8080/destinationtype=Topic?destination=abc");
//    SJMSCommInfo info = new SJMSCommInfo("jms://www.localhost.com:443/destinationtype=Topic?destination=testtopic");
//    info.setTptImplVersion("abc");
//    System.out.println("Protocol :"+info.getProtocol());
//    System.out.println("Host :"+info.getHost());
//    System.out.println("Port :"+info.getPort());
//    System.out.println("Destination Type :"+info.getDestType());
//    System.out.println("Destination  :"+info.getDestination());
//    System.out.println("Destination  :"+info.getURL());
//    if(info instanceof IJMSCommInfo)
//    System.out.println("Yes it is ");
      JMSCommInfo info1 = new JMSCommInfo();
      info1.setURL("smq://gtpublic:gridtalk@192.168.213.127:443/destinationtype=QUEUE?destination=gtpublic");
      //JMSCommInfo info1 = new JMSCommInfo("smq://gtpublic:gridtalk@192.168.213.127:443/destinationtype=TOPIC?destination=gtpublic");
      //JMSCommInfo info1 = new JMSCommInfo("smq://gtpublic@192.168.213.127:443/destinationtype=TOPIC?destination=gtpublic");
      //info.setProtocol("smq");
//      info.setHost("localhost");
//      info.setPort(443);
//      info.setUserName("abc");
    //  info.setPassword("xx");
      //info.setDestination("fgfdg");
      //info.setDestinationType("topic");
     //System.out.println(info.toURL());
      System.out.println(info1.getPort());
      System.out.println(info1.getProtocol());
      System.out.println(info1.getUser());
      System.out.println(info1.getPassword());
      System.out.println(info1.getDestTypeName(info1.getDestType()));
      System.out.println(info1.getDestination());
      System.out.println(info1.toURL());
      info1.setHost("123.132.133.31");
      info1.setDestination("911");
    System.out.println(info1.toURL());
      
  }

  /**
   * @see com.gridnode.pdip.base.transport.comminfo.AbstractCommInfo#parseAndSetURL(java.lang.String)
   */
  protected void parseAndSetURL(String url1)
  {
    super.parseAndSetURL(url1);
    parseJMSURL(url1);
  }
}