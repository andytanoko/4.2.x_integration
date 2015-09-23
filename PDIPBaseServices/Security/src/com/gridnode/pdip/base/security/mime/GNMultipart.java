/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNMultipart.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 10 Aug  2001    Lim Soon Hsiung     Initial creation GT 1.1
 * 17 June 2002    Jagadeesh           GTAS2.0- Included to throw SecurityServiceException.
 * 09 Dec  2002    Jagadeesh           Modified - To include
 */

package com.gridnode.pdip.base.security.mime;

import java.io.Serializable;

import javax.activation.DataSource;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;

/**
 * Title:        GridNode Security
 * Description:  GridNode Security Module
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Lim Soon Hsiung
 * @version 1.1
 */

public class GNMultipart extends MimeMultipart implements Serializable  //implements IMimeExceptionValue
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1939286733868172696L;
	private String description;
//  ExceptionValue exV = new ExceptionValue("GNMultipart Exception");
  private boolean readOnly = false;

  public GNMultipart()
  {
    super();
  }

  public GNMultipart(String subType)
  {
    super(subType);
  }

  public GNMultipart(DataSource datasource) throws Exception
  {
    super(datasource);
  }

  public GNMultipart(MimeMultipart multipart) throws Exception
  {
    reconstructParts(multipart);
  }

  private void reconstructParts(MimeMultipart multipart) throws SecurityServiceException
  {
    try
    {
      contentType = multipart.getContentType();
      parent = multipart.getParent();
      int count = multipart.getCount();

      for (int i = 0; i < count; i++)
      {
        addBodyPart(multipart.getBodyPart(i));
      }

    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to construct Multipart mime object",ex);
      //GNException.throwEx(exV, "Unable to construct Multipart mime object", ex);
    }

  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

//  public String getContentType()
//  {
//    return contentType;
//  }

  public synchronized void setParameter(String name, String value) throws SecurityServiceException
  {
    try
    {
      ContentType contenttype = new ContentType(contentType);
      contenttype.setParameter(name, value);
      contentType = contenttype.toString();
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to set ContentType parameter!",ex);
      //GNException.throwEx(exV, "Unable to set ContentType parameter!", ex);
    }

  }

  public synchronized void removeParameter(String name) throws SecurityServiceException
  {
    try
    {
      ContentType contenttype = new ContentType(contentType);
      ParameterList pList = contenttype.getParameterList();
      pList.remove(name);
      contentType = (new ContentType(contenttype.getPrimaryType(), contenttype.getSubType(), pList)).toString();
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to set ContentType parameter!",ex);
      //GNException.throwEx(exV, "Unable to set ContentType parameter!", ex);
    }

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

  public boolean isMimeType(String string) throws SecurityServiceException
  {
    return isMimeType(this, string);
  }

  static boolean isMimeType(GNMultipart mimepart, String string)
    throws SecurityServiceException
  {
    try
    {
      ContentType contenttype = new ContentType(mimepart.getContentType());
      return contenttype.match(string);
    } catch (ParseException parseexception)
    {
        return mimepart.getContentType().equalsIgnoreCase(string);
    }
  }

  public static final GNMultipart createInstance(MimeMultipart mp) throws SecurityServiceException
  {
    GNMultipart gnMp = null;
    try
    {
      gnMp = new GNMultipart(mp);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to instantiate GNMultipart",ex);
      //GNException.throwEx(EX_GENERAL, "Unable to instantiate GNMultipart" , ex);
    }

    return gnMp;
  }

  public void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }

  public boolean isReadOnly()
  {
    return readOnly;
  }
}