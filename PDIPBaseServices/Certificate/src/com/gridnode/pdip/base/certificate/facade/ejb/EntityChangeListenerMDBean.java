/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityChangeListenerMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 8, 2006    i00107             Created
 * Jul 18, 2009   Tam Wei Xiang       #560 - Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */

package com.gridnode.pdip.base.certificate.facade.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.cert.X509Certificate;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.base.certificate.helpers.CertificateLogger;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.helpers.SecurityDB;
import com.gridnode.pdip.base.certificate.helpers.SecurityDBManager;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.entity.EntityEvent;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
//import com.rsa.certj.cert.X509Certificate;

/**
 * @author i00107
 * To listen for update changes to dependent entities.
 */
public class EntityChangeListenerMDBean	implements
																				MessageDrivenBean,
																				MessageListener
{

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 4529134146380684405L;
	private MessageDrivenContext _mdx = null;

	/**
	 * @see javax.ejb.MessageDrivenBean#ejbRemove()
	 */
	public void ejbRemove()
	{
		_mdx = null;
	}

  public void ejbCreate()
  {
  }

	/**
	 * @see javax.ejb.MessageDrivenBean#setMessageDrivenContext(javax.ejb.MessageDrivenContext)
	 */
	public void setMessageDrivenContext(MessageDrivenContext arg0) throws EJBException
	{
		_mdx = arg0;
	}

	/**
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message msg)
	{
    try
    {
      CertificateLogger.debug("[EntityChangeListenerMDBean.onMessage] enter");

      EntityEvent event = (EntityEvent)((ObjectMessage)msg).getObject();

      IEntity entity = event.getEntity();
      handleChange(event.getEventType(), entity);
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn("[EntityChangeListenerMDBean.onMessage] error", ex);
    }
    finally
    {
      CertificateLogger.debug("[EntityChangeListenerMDBean.onMessage] exit");
    }
	}

  /**
   * Handles an entity event. Reflection is used to invoke the concrete method
   * to handle the entity event.
   *
   * @param eventType The type of the event.
   * @param entity The entity that is manipulateed.
   * @throws Throwable
   */
  private void handleChange(int eventType, IEntity entity)
    throws Throwable
  {
    try
    {
      CertificateLogger.debug("[EntityChangeListenerMDBean.handleChange] enter");

      String opType = getOpType(eventType);
      String entityType = entity.getEntityName();
      String handlerMethod = "handle"+entityType+opType;
      Method method = getClass().getDeclaredMethod(
                        handlerMethod,
                        new Class[] {IEntity.class});
      method.invoke(this, new Object[] {entity});
    }
    catch (NoSuchMethodException ex)
    {
      //no method defined to handle change in resource
      CertificateLogger.warn("[EntityChangeListenerMDBean.handleChange] no handler defined: "+ex.getMessage());
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
    finally
    {
      CertificateLogger.debug("[EntityChangeListenerMDBean.handleChange] exit");
    }
  }

  /**
   * Get the operation type based on the event type.
   * @param eventType The type of the event.
   * @return The operation type determined from the event type. Only interested
   * event types are returned with valid operation types. Other event types are
   * returned with "Unknown".
   */
  private String getOpType(int eventType)
  {
    String type = "Unknown";

    switch (eventType)
    {
      case EntityEvent.CREATED: type = "Created"; break;
      case EntityEvent.DELETED: type = "Deleted"; break;
      case EntityEvent.UPDATED: type = "Updated"; break;
    }

    return type;
  }

  /**
   * Handle the event when certificate is created
   * @param entity The certificate entity with created event
   * @throws UpdateEntityException
   * @throws SystemException
   */
  protected void handleCertificateCreated(IEntity entity)
  	throws UpdateEntityException, SystemException
  {
  	try
  	{
      CertificateLogger.debug("[EntityChangeListenerMDBean.handleCertificateCreated] enter");
  		
  		Certificate cert = (Certificate)entity;

      X509Certificate newCert = GridCertUtilities.loadX509CertificateByString(cert.getCertificate());
//      SecurityDB secDB = SecurityDBManager.getInstance().getSecurityDB();
//      secDB.addTrustedCertificates(new com.rsa.certj.cert.Certificate[] { newCert });
  	}
  	catch (Throwable ex)
  	{
      CertificateLogger.warn("[EntityChangeListenerMDBean.handleCertificateCreated] Error synchronizing trusted certs. Security operations may fail until application is restarted.", ex);
  	}
  	finally
  	{
      CertificateLogger.debug("[EntityChangeListenerMDBean.handleCertificateCreated] exit");
  	}

  }

}

