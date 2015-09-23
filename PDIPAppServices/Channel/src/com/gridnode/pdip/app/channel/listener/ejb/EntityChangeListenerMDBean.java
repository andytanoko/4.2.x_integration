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
 * Aug 25,2006    Tam Wei Xiang      Modified the way we retrieve the pendingCert UID.
 *                                   Alter method handleCertificateUpdated(...)
 * Aug 01 2008	  Wong Yee Wah		 #38  Modified Method: handleCertificateUpdated(..)
 */

package com.gridnode.pdip.app.channel.listener.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.entity.EntityEvent;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.util.ServiceLocator;

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
      ChannelLogger.debugLog("EntityChangeListenerMDBean", "onMessage", "enter");

      EntityEvent event = (EntityEvent)((ObjectMessage)msg).getObject();

      IEntity entity = event.getEntity();
      handleChange(event.getEventType(), entity);
    }
    catch (Throwable ex)
    {
      ChannelLogger.debugLog("EntityChangeListenerMDBean", "onMessage", "error", ex);
    }
    finally
    {
      ChannelLogger.debugLog("EntityChangeListenerMDBean", "onMessage", "exit");
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
      ChannelLogger.debugLog("EntityChangeListenerMDBean", "handleChange", "enter");

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
      ChannelLogger.debugLog("EntityChangeListenerMDBean", "handleChange", "no handler defined: "+ex.getMessage());
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
    finally
    {
      ChannelLogger.debugLog("EntityChangeListenerMDBean", "handleChange", "exit");
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
   * Handle the event when certificate is updated
   * @param entity The certificate entity with updated event
   * @throws UpdateEntityException
   * @throws SystemException
   */
  protected void handleCertificateUpdated(IEntity entity)
  	throws UpdateEntityException, SystemException
  {
  	try
  	{
  		ChannelLogger.debugLog("EntityChangeListenerMDBean", "handleCertificateUpdated", "enter");
  		
  		Certificate cert = (Certificate)entity;
  		//WYW 01082008
  		if (cert.getRevokeId()!=0 || cert.getIsForcedGetReplacementCert()) //cert is revoked or is forced replace cert
  		{
  			//Long newCertUid = getPendingCert(new Long(cert.getUId()));
  			
  			//TWX 25082006 A direct link from the old cert to the replacementCert
  			Long newCertUid = cert.getReplacementCertUid();
  			
  			if (newCertUid != null)
  			{
  				Collection secInfos = findSecurityInfos(cert.getUId());
  				if (secInfos != null && !secInfos.isEmpty())
  				{
  					for (Iterator i=secInfos.iterator(); i.hasNext(); )
  					{
  						try
  						{
  							SecurityInfo secInfo = (SecurityInfo)i.next();
  							if (secInfo.isPartner())
  							{
  								if (cert.isPartner()) //if partner cert, update the encryption cert
  								{
  									secInfo.setEncryptionCertificateID(newCertUid);
  								}
  								else //own cert, update the signature cert
  								{
  									secInfo.setSignatureEncryptionCertificateID(newCertUid);
  								}
  							}
  							else //own secInfo
  							{
  								secInfo.setEncryptionCertificateID(newCertUid);
  							}
  							updateSecurityInfo(secInfo);
  						}
  						catch (Exception e)
  						{
  							ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_CHANNEL_UPDATE,
  							                       "EntityChangeListenerMDBean", "handleCertificateUpdated", "Update SecurityInfo for change in Certificate "+cert.getEntityDescr() +
  							                       ", Error: "+e.getMessage());
  						}
  					}
  				}    			
    		}
  		}
  	}
  	catch (Throwable ex)
  	{
  		ChannelLogger.debugLog("EntityChangeListenerMDBean", "handleCertificateUpdated", "error", ex);
  	}
  	finally
  	{
  		ChannelLogger.debugLog("EntityChangeListenerMDBean", "handleCertificateUpdated", "exit");
  	}

  }

  /**
   * Retrieve the pending cert for the revoked cert. Note: this assumes that there is only one pending cert configured for
   * the revoked cert. (this is a design issue - the related cert uid should be configured at the expiring cert instead of new cert to
   * maintain one pending cert for one expiring cert.)
   * @param oldCertUid The UID of the revoked cert
   * @return The pending cert configured for the revoked cert, if any. 
   * @throws Exception
   */
  private Long getPendingCert(Long oldCertUid)
  	throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.addSingleFilter(null, Certificate.RELATED_CERT_UID, filter.getEqualOperator(), oldCertUid, false);
  	filter.addSingleFilter(filter.getAndConnector(), Certificate.REVOKEID, filter.getEqualOperator(), new Integer(0), false);
  	Collection pendingCert = getCertificateManager().getCertificateKeys(filter);
  	if (pendingCert != null && !pendingCert.isEmpty())
  	{
  		return (Long)pendingCert.iterator().next();
  	}
  	return null;
  }
  
  private Collection findSecurityInfos(Long certUid)
    throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.addSingleFilter(null, SecurityInfo.ENCRYPTION_CERTIFICATE_ID, filter.getEqualOperator(), certUid, false);
  	filter.addSingleFilter(filter.getOrConnector(), SecurityInfo.SIGNATURE_ENCRYPTION_CERTIFICATE_ID, filter.getEqualOperator(), certUid, false);
  	
  	return getChannelManager().getSecurityInfo(filter);
  }
  
  private void updateSecurityInfo(SecurityInfo secInfo)
  	throws Exception
  {
  	getChannelManager().updateSecurityInfo(secInfo);
  }
  
  private IChannelManagerObj getChannelManager()
  	throws Exception
  {
  	return (IChannelManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IChannelManagerHome.class.getName(),
  	                                                            IChannelManagerHome.class,
  	                                                            new Object[0]);
  }

  private ICertificateManagerObj getCertificateManager()
	throws Exception
{
	return (ICertificateManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(ICertificateManagerHome.class.getName(),
	                                                                ICertificateManagerHome.class,
	                                                                new Object[0]);
}

}

