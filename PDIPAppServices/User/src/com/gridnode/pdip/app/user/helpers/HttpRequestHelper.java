/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpRequestHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 12 2002    Neo Sok Lay         Created
 * Apr 02 2003    Neo Sok Lay         GNDB00013390: getUrl() from config file
 *                                    instead of jndi lookup.
 */
package com.gridnode.pdip.app.user.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.gridnode.pdip.app.user.login.ISignOnKeys;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * This helper class helps to post Http requests.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class HttpRequestHelper
  implements ISignOnKeys
{
  /*030402NSL - change to url property key
  private static final String SIGNON_URL = "java:comp/env/param/url/SignOnUrl";
  private static final String SIGNOFF_URL = "java:comp/env/param/url/SignOffUrl";
  */
  private static final String SIGNON_URL = "url.signon";
  private static final String SIGNOFF_URL = "url.signoff";
  private static final String URL_CONFIG  = "login.url";
  
  private static final String DEF_URL_ENCODING = "UTF-8";

  /**
   * Post a SignOn Http request
   *
   * @param application The application to signon to.
   * @param session SessionID of the session to signon.
   * @param userID UserID of the user to signon.
   * @param password Password provided by user to signon, encrypted using
   * <CODE>PasswordMask</CODE>
   *
   * @see com.gridnode.pdip.framework.util.PasswordMask
   */
  public static void postSignOnRequest(
    String application, String session, String userID, String password)
    throws SystemException, ApplicationException
  {
    StringBuffer urlStr = new StringBuffer();
    urlStr.append(getUrl(SIGNON_URL));
    urlStr.append("?").append(SIGNON_APPLICATION).append("=").append(encodeStr(application));
    urlStr.append("&").append(SIGNON_USER_ID).append("=").append(encodeStr(userID));
    urlStr.append("&").append(SIGNON_PASSWORD).append("=").append(encodeStr(password));
    urlStr.append("&").append(SIGNON_SESSION).append("=").append(encodeStr(session));

    postRequest(urlStr.toString());
  }

  /**
   * Post a SignOff Http request
   *
   * @param session SessionID of the session to signoff.
   *
   * @see com.gridnode.pdip.framework.util.PasswordMask
   */
  public static void postSignOffRequest(String session)
    throws SystemException, ApplicationException
  {
    StringBuffer urlStr = new StringBuffer();
    urlStr.append(getUrl(SIGNOFF_URL));
    urlStr.append("?").append(SIGNON_SESSION).append("=").append(encodeStr(session));

    postRequest(urlStr.toString());
  }

  /**
   * Post a Http request
   *
   * @param urlStr The Url of the request. This url has to contain the
   * HTTP protocol.
   *
   * @exception ApplicationException Application type of exception occurred
   * at the web service site.
   * @exception SystemException System type of exception occurred at the web
   * service site.
   */
  private static void postRequest(String urlStr)
    throws SystemException, ApplicationException
  {
    HttpURLConnection conn = null;
    try
    {
      URL url = new URL(urlStr.toString());
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("POST");
      HttpURLConnection.setFollowRedirects(true);
      conn.setInstanceFollowRedirects(true);
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setAllowUserInteraction(false);
      Logger.debug("[HttpRequestHelper.postRequest] URL " + urlStr +
        ", Response: " + conn.getResponseCode() +" "+ conn.getResponseMessage());
    }
    catch(Throwable ex)
    {
      Logger.warn("[HttpRequestHelper.postRequest] Error posting HttpRequest '" +
        urlStr + "'");

      if (conn != null)
        conn.disconnect();

      throw new SystemException(ex);
    }

    try
    {
      analyseError(conn);
    }
    catch (IOException ex)
    {
      throw new SystemException(ex);
    }
    finally
    {
      if (conn != null)
        conn.disconnect();
    }
  }

  /**
   * Analyse for any error returned from a Http URL connection.
   * <P>The ERR_CODE has to be returned in the Http header field and the
   * exception trace in the content as plain text.
   * <P>Throws exception if error is returned, otherwise returns silently.
   *
   * @param conn The Http URL connection
   * @exception ApplicationException if ERR_CODE is ERR_AUTH_FAIL or
   * ERR_BAD_REQUEST.
   * @exception SystemException if ERR_CODE is ERR_SERVICE_FAIL.
   *
   * @see com.gridnode.pdip.app.user.login.ISignOnKeys
   * @since 2.0
   */
  private static void analyseError(HttpURLConnection conn)
    throws SystemException, ApplicationException, IOException
  {
    int errCode = conn.getHeaderFieldInt(ERR_CODE, ERR_NO_ERROR);
    String errContent = readContent(conn);
    Logger.debug("[HttpRequestHelper.analyseError] ErrCode,Content="+errCode + ","+
      errContent);

    switch (errCode)
    {
      case ERR_NO_ERROR :
           break;
      case ERR_AUTH_FAIL :
      case ERR_BAD_REQUEST :
           throw new ApplicationException(errContent);
      case ERR_SERVICE_FAIL :
      default :
           throw new SystemException(errContent, null);
    }
  }

  /**
   * Reads the content return from the Http url connection.
   *
   * @param conn The Http URL connection.
   * @return The content as string.
   * @exception IOException Error reading from the input stream of the connection.
   *
   * @since 2.0
   */
  private static String readContent(HttpURLConnection conn)
    throws IOException
  {
    int contentLength = conn.getContentLength();
    if (contentLength > 0)
    {
      char[] content = new char[contentLength];
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      reader.read(content);
      return String.valueOf(content);
    }
    else
      return "";
  }

  /**
   * Get a Url connection string from the login.url configuration.
   *
   * @param urlKey Jndi name of the entry.
   * @return The Url connection string returned from config file, or <B>null</B>
   * if none found.
   *
   * @since 2.0
   */
  private static String getUrl(String urlKey)
  {
    /*030402NSL - get from ConfigurationManager instead
    // do the lookup
    try
    {
      return (String)ServiceLocator.instance(
               ServiceLocator.CLIENT_CONTEXT).getHome(
               urlKey, String.class);
    }
    catch (ServiceLookupException ex)
    {
      Logger.err("[HttpRequestHelper.getUrl] Error ", ex);
    }
    */

    try
    {
      return ConfigurationManager.getInstance().getConfig(URL_CONFIG).getString(urlKey, null);
    }
    catch (Exception ex)
    {
      Logger.warn("[HttpRequestHelper.getUrl] Error ", ex);
    }

    return null;
  }

  private static String encodeStr(String val)
  {
  	try
  	{
  		return URLEncoder.encode(val, DEF_URL_ENCODING);
  	}
  	catch (Exception e)
  	{
  		Logger.warn("HttpRequestHelper.encodeStr] Error", e);
  		return val;
  	}
  }
}