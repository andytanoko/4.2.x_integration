/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceChangeListenerMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 31 2003    Neo Sok Lay         Created
 * May 08 2006    Neo Sok Lay         GNDB00027090: when cert is revoked, replace
 *                                    link with replacement cert.
 * Aug 25 2006    Tam Wei Xiang       Modified the way we retrieve the pendingCert.
 *                                    Alter method handleCertificateUpdated(...)
 * Aug 01 2008	  Wong Yee Wah		  #38  Modified method: handleCertificateUpdated()  
 * 									  Modified method: getReplacementCert() Ticket #71                              
 */
package com.gridnode.gtas.server.partnerprocess.facade.ejb;

import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.helpers.Logger;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.entity.EntityEvent;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;


import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

/**
 * This is a message-driven bean that, on receiving a message of EntityEvent
 * topic, will handle the events by to ensure that dependent entities 
 * that are changed are synchronized to the PartnerProcess entity data models.
 *
 * @author Neo Sok Lay
 * @version GT 4.0
 */
public class EntityChangeListenerMDBean
  implements MessageDrivenBean, MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3209233118943547603L;
	private MessageDrivenContext _mdx = null;

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove() 
  {
    _mdx = null;
  }

  public void ejbCreate()
  {
  }

  /**
   * On message received, assumes that the message is an ObjectMessage containing
   * an EntityEvent object. Otherwise, the message is ignored.<p>
   *
   * @param msg The received message.
   */
  public void onMessage(Message msg)
  {
    FacadeLogger logger = Logger.getEntityChangeFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {msg};

    try
    {
      logger.logEntry(methodName, params);
      
      EntityEvent event = (EntityEvent)((ObjectMessage)msg).getObject();

      IEntity entity = event.getEntity();
      handleChange(event.getEventType(), entity);
    }
    catch (Throwable ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
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
    FacadeLogger logger = Logger.getEntityChangeFacadeLogger();
    String methodName   = "handleChange";
    Object[] params     = new Object[]
                          {
                            new Integer(eventType),
                            entity
                          };

    try
    {
      logger.logEntry(methodName, params);

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
      logger.logMessage(methodName, null, ex.getMessage());
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
    finally
    {
      logger.logExit(methodName, params);
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

  // ****************** Handlers **************************************

  /**
   * Handling method for the event when a ProcessDef has been updated.
   * The ProcessVersionId and ProcessIndicatorCode of ProcessMapping needs to
   * be synchronized with the updated ProcessDef.
   * 
   * @param entity The ProcessDef updated.
   * @throws UpdateEntityException
   * @throws SystemException
   */
  protected void handleProcessDefUpdated(IEntity entity)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getEntityChangeFacadeLogger();
    String methodName   = "handleProcessDefUpdated";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);

      ProcessDef processDef = (ProcessDef)entity;
      
      Collection processMappings = findProcessMappings(processDef.getDefName());
      if (processMappings != null && !processMappings.isEmpty())
      {
        String processIndicatorCode = processDef.getGProcessIndicatorCode();
        String processVersionId = processDef.getVersionIdentifier();
        
        for (Iterator i=processMappings.iterator(); i.hasNext(); )
        {
          try
          {
            ProcessMapping mapping = (ProcessMapping)i.next();
            mapping.setProcessIndicatorCode(processIndicatorCode);
            mapping.setProcessVersionID(processVersionId);
            ActionHelper.getManager().updateProcessMapping(mapping);
          }
          catch (Exception e)
          {
            logger.logMessage(methodName, params, "Update ProcessMapping for change in ProcessDef "+processDef.getDefName() +
              ", Error: "+e.getMessage());
          }
        }
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Find ProcessMapping(s) based on the specified ProcessDef name.
   * 
   * @param processDef The ProcessDef name.
   * @return Collection of ProcessMapping(s) that are linked to the specified process def.
   * @throws Exception Error in retrieving ProcessMapping(s).
   */
  private Collection findProcessMappings(String processDef)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ProcessMapping.PROCESS_DEF,
      filter.getEqualOperator(), processDef, false);
    
    return ActionHelper.getManager().findProcessMappingByFilter(
             filter);
                            
  }
  
  /**
   * Handle the event when a certificate is updated in the db.
   * 
   * @param entity The certificate with updated contents
   * @throws UpdateEntityException Unable to update the affected links in BizCertMapping
   * @throws SystemException
   */
  protected void handleCertificateUpdated(IEntity entity)
  throws UpdateEntityException, SystemException
  {
  	FacadeLogger logger = Logger.getEntityChangeFacadeLogger();
  	String methodName   = "handleCertificateUpdated";
  	Object[] params     = new Object[] {entity};
  	
  	try
  	{
  		logger.logEntry(methodName, params);
  		
  		Certificate cert = (Certificate)entity;
  		
  		if (cert.getRevokeId()!=0 || cert.getIsForcedGetReplacementCert()) //cert is revoked
  		{
  			//Certificate newCert = getPendingCert(new Long(cert.getUId()));
  			
  			//TWX 25082006 
  			Certificate newCert = getReplacementCert(cert); 
  			if (newCert != null) //only need to change the mappings if there is new cert for it
  			{
  				Collection certMappings = findCertMappings(cert.getUId());
  				if (certMappings != null && !certMappings.isEmpty())
  				{
  					for (Iterator i=certMappings.iterator(); i.hasNext(); )
  					{
  						try
  						{
  							BizCertMapping mapping = (BizCertMapping)i.next();
  							if (cert.isPartner())
  							{
  								mapping.setPartnerCert(newCert);
  							}
  							else
  							{
  								mapping.setOwnCert(newCert);
  							}
  							ActionHelper.getManager().updateBizCertMapping(mapping);
  						}
  						catch (Exception e)
  						{
  							logger.logMessage(methodName, params, "Update BizCertMapping for change in Certificate "+cert.getEntityDescr() +
  							                  ", Error: "+e.getMessage());
  						}
  					}
  					
  				}
  			}
  		}
  	}
  	catch (Throwable ex)
  	{
  		logger.logUpdateError(methodName, params, ex);
  	}
  	finally
  	{
  		logger.logExit(methodName, params);
  	}
  }
  
  /**
   * Find the BizCertMapping(s) that reference the specified certificate
   * @param certUid The UID of the certificate
   * @return Collection of BizCertMapping(s) with OwnCert or PartnerCert pointing to the specified certificate.
   * @throws Exception
   */
  private Collection findCertMappings(Long certUid)
  throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.addSingleFilter(null, BizCertMapping.OWN_CERT,
  	                       filter.getEqualOperator(), certUid, false);
  	filter.addSingleFilter(filter.getOrConnector(), BizCertMapping.PARTNER_CERT, filter.getEqualOperator(), certUid, false);
  	
  	return ActionHelper.getManager().findBizCertMappingByFilter(filter);
  }

  /**
   * Retrieve the pending cert for the revoked cert. Note: this assumes that there is only one pending cert configured for
   * the revoked cert. (this is a design issue - the related cert uid should be configured at the expiring cert instead of new cert to
   * maintain one pending cert for one expiring cert.)
   * @param oldCertUid The UID of the revoked cert
   * @return The pending cert configured for the revoked cert, if any. 
   * @throws Exception
   */
  private Certificate getPendingCert(Long oldCertUid)
  	throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.addSingleFilter(null, Certificate.RELATED_CERT_UID, filter.getEqualOperator(), oldCertUid, false);
  	filter.addSingleFilter(filter.getAndConnector(), Certificate.REVOKEID, filter.getEqualOperator(), new Integer(0), false);
  	Collection pendingCert = ActionHelper.getCertManager().getCertificate(filter);
  	if (pendingCert != null && !pendingCert.isEmpty())
  	{
  		return (Certificate)pendingCert.iterator().next();
  	}
  	return null;
  }
  
  /**
   * TWX 25082006 Get the replacement cert for the oldCert .
   * A new design. A direct relationship from old cert to the replacement cert has been initialized
   * through replacementCertUid. 
   * @param oldCert Certificate that has revoked.
   * @return the replacement cert (must be able to locate one)
   * @throws Exception
   */
  private Certificate getReplacementCert(Certificate oldCert)
  	throws Exception
  {  
  	IDataFilter filter = new DataFilterImpl();
  	filter.addSingleFilter(null, Certificate.UID, filter.getEqualOperator(), oldCert.getReplacementCertUid(), false);
  	filter.addSingleFilter(filter.getAndConnector(), Certificate.REVOKEID, filter.getEqualOperator(), new Integer(0), false);//WYW 01082008 Ticket #71
  	Collection replacementCerts = ActionHelper.getCertManager().getCertificate(filter);
  	if(replacementCerts != null && replacementCerts.size() > 0)
  	{
  		return (Certificate)replacementCerts.iterator().next();
  	}
  	else
  	{
  		FacadeLogger logger = Logger.getEntityChangeFacadeLogger();
  		logger.logMessage("getReplacementCert", null, "Cannot get replacementCert with UID: "+oldCert.getReplacementCertUid()+" for cert with key: "+oldCert.getKey());
  		return null;
  	}
  }
}
