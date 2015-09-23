/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingServiceBean
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 16-OCT-2002    Jagadeesh           Created.
 */
package com.gridnode.pdip.base.packaging.facade.ejb;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.packaging.exceptions.PackagingException;
import com.gridnode.pdip.base.packaging.handler.IPackagingHandler;
import com.gridnode.pdip.base.packaging.handler.PackagingHandlerFactory;
import com.gridnode.pdip.base.packaging.helper.PackagingInfo;
import com.gridnode.pdip.base.packaging.helper.PackagingLogger;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.messaging.Message;

public class PackagingServiceBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2394561892837895590L;

	private SessionContext _ctx;
  
  private static final String CLASS_NAME = "PackagingServiceBean";
   
  public Message pack(PackagingInfo info, Message message)
    throws PackagingException, SystemException
  {
    final String METHOD_NAME = "pack()";
    try
    {
      if (info != null)
      {
        IPackagingHandler handler = 
          PackagingHandlerFactory.getPackagingHandler(info.getEnvelopeType());
        if (handler != null)
          message = handler.pack(info,message);   
      }
    }
    catch (Exception ex)
    {
      PackagingLogger.warnLog(
        CLASS_NAME,
        METHOD_NAME,
        "Exception packaging payload in message");
        
      throw new PackagingException(
        "Exception packaging payload in message",
        ex);
    }
    catch (Throwable te)
    {
      PackagingLogger.warnLog(
        CLASS_NAME,
        METHOD_NAME,
        "Exception packaging payload in message");
      throw new SystemException("Exception packaging payload in message ", te);
    }
    return message;
  }
  
  public Message unPack(PackagingInfo info, Message message)
    throws PackagingException, SystemException
  {
    final String METHOD_NAME = "unPack";
    try
    {
      if (info != null)
      { 
        IPackagingHandler handler = null;
        //String envelopeType = info.getEnvelopeType();
        handler =
          PackagingHandlerFactory.getPackagingHandler(info.getEnvelopeType());
        return handler.unPack(info,message); 
      }
    }
    catch (Exception ex)
    {
      PackagingLogger.warnLog(
        CLASS_NAME,
        METHOD_NAME,
        "Exception unpackaging payload in message");
      throw new PackagingException(
        "Exception unpackaging payload in message",
        ex);
    }
    catch (Throwable te)
    {
      PackagingLogger.warnLog(
        CLASS_NAME,
        METHOD_NAME,
        "Exception unpackaging payload in message");
      throw new SystemException(
        "Exception unpackaging payload in message", 
        te);
    }
    return null;
  }
  
  /** Life Cycle Method's for Enterprise Bean **/
  public void setSessionContext(SessionContext ctx)
  {
    _ctx = ctx;
  }
  public void ejbCreate()
  {
  }
  public void ejbRemove()
  {
  }
  public void ejbActivate()
  {
  }
  public void ejbPassivate()
  {
  }
}