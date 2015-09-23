package com.gridnode.pdip.base.security.mime.smime.helpers;

/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 10 Nov 2003    Zou Qingsong        Created. *
 */
import java.io.Serializable;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

public class mime_multipart  extends MimeMultipart implements Serializable
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2266977484708417005L;

	public mime_multipart(Multipart multipart) throws MessagingException
  {
    reconstructParts(multipart);
  }

  private void reconstructParts(Multipart multipart) throws MessagingException
  {
      contentType = multipart.getContentType();
      parent = multipart.getParent();
      int count = multipart.getCount();

      for (int i = 0; i < count; i++)
      {
        addBodyPart(multipart.getBodyPart(i));
      }
  }

  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }

  public synchronized void setParameter(String name, String value) throws MessagingException
  {
      ContentType contenttype = new ContentType(contentType);
      contenttype.setParameter(name, value);
      contentType = contenttype.toString();
  }

  public synchronized void removeParameter(String name) throws ParseException
  {
      ContentType contenttype = new ContentType(contentType);
      ParameterList pList = contenttype.getParameterList();
      pList.remove(name);
      contentType = (new ContentType(contenttype.getPrimaryType(), contenttype.getSubType(), pList)).toString();
  }

  public String getParameter(String name)
  {
    String value = null;
    try
    {
      ContentType contenttype = new ContentType(contentType);
      value = contenttype.getParameter(name);
    }
    catch (Exception ex)
    {
      value = null;
    }
    return value;
  }
}