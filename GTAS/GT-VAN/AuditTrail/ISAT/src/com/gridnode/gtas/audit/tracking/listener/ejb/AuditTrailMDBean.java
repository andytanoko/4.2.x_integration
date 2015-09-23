/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Jan 09, 2007    Tam Wei Xiang       Support for the ByteMessage(From 
 *                                     HTTPBC component)
 * Mar 07 2007		Alain Ah Ming				Catch illegal argument exception, 
 * 																			log warning message and do nothing  
 * Mar 17 2007    Neo Sok Lay         Change to use Castor to deserialize from XML. 
 * Apr 05 2007    Tam Wei Xiang       Directly process those AuditTrail Data that
 *                                    its independent flag is set to true  
 * May 04 2007    Tam Wei Xiang       In case the AuditTrailData has failed to be
 *                                    processed
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 * Jul 30, 2008   Tam Wei Xiang       #69:1) Remove explicitly checked for redeliverd msg and dropped that msg.
 *                                        2) Handle the redelivered JMS msg
 */
package com.gridnode.gtas.audit.tracking.listener.ejb;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.naming.NamingException;

import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerHome;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.gridnode.util.xml.XMLBeanUtil;

/**
 * 
 * Listens the AuditTrailData obj from the GT-AT plugin (OTC).
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class AuditTrailMDBean implements MessageListener, MessageDrivenBean
{
  private MessageDrivenContext _ctx;
  private static final String CLASS_NAME = "AuditTrailMDBean";
  private static final String AUDIT_TRAIL_MGR_JNDI_NAME = IAuditTrailManagerHome.class.getName();
  private Logger _logger = null;
  
  public void onMessage(Message message)
  {
    String method = "onMessage";
    
    _logger.logEntry(method, null);
    AuditTrailData trailData = null;
    
      try
      {        
        if(message.getJMSRedelivered())
        {
          _logger.logMessage(method, null, "Redelivered msg found. Message: "+message);
        }
        
        if(message instanceof ObjectMessage)
        {
          ObjectMessage objMsg = (ObjectMessage)message;
          trailData = (AuditTrailData)objMsg.getObject();
        }
        else if(message instanceof StreamMessage)
        {
          trailData = deserializeToAuditTrailData((StreamMessage)message);
        }
        else
        {
          throw new IllegalArgumentException("["+CLASS_NAME+".onMessage] The message "+message+" is not supported !!");
        }
          IAuditTrailManagerObj trailObj = getAuditTrailObj();
          
          if(trailData == null)
          {
            throw new NullPointerException("The given AuditTrailData is null.");
          }
          
          
          if(message.getJMSRedelivered()) //#69 TWX Handle the redelivered of JMS msg.
          {
            trailObj.handleRedeliveredAuditTrailData(trailData);
          }
          else
          {
            trailObj.handleAuditTrailData(trailData);
          }
          
          System.out.println("GT Trans End mdbean processing ....");
      }
      catch(IllegalArgumentException e)
      {
      	_logger.logWarn(method, null,"Illegal argument: " + e.getMessage(), e);
      }
      catch(JMSException e)
      {
        _logger.logError(ILogErrorCodes.AT_AUDITTRAIL_MDB_ONMESSAGE_ERROR,
                         method, null, "Failed to read request: "+e.getMessage(), e);      	
      }
			catch (NamingException e)
			{
	      _logger.logError(ILogErrorCodes.AT_AUDITTRAIL_MDB_ONMESSAGE_ERROR,
	                       method, null, "Failed to process audit trail: JNDIFinder initialisation failure", e);
			}
			catch (RemoteException e)
			{
	      _logger.logError(ILogErrorCodes.AT_AUDITTRAIL_MDB_ONMESSAGE_ERROR,
	                       method, null, "Failed to process audit trail: EJB invocation failure", e);
			}
			catch (CreateException e)
			{
	      _logger.logError(ILogErrorCodes.AT_AUDITTRAIL_MDB_ONMESSAGE_ERROR,
	                       method, null, "Failed to process audit trail: EJB creation failure", e);
			}
      catch(AuditTrailTrackingException e)
      {
        _logger.logError(ILogErrorCodes.AT_AUDITTRAIL_MDB_ONMESSAGE_ERROR,
                         method, null, "Failed to process audit trail. General Error: "+e.getMessage(), e);
      }
	    catch(Throwable t)
	    {
	      _logger.logError(ILogErrorCodes.AT_AUDITTRAIL_MDB_ONMESSAGE_ERROR,
	                       method, null, "Failed to process audit trail. Unexpected Error: "+t.getMessage(), t);
	    }
      finally
      {
        _logger.logExit(method, null);
      }
  }
  
  private IAuditTrailManagerObj getAuditTrailObj() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditTrailManagerHome trailHome = (IAuditTrailManagerHome)finder.lookup(AUDIT_TRAIL_MGR_JNDI_NAME, IAuditTrailManagerHome.class);
    return trailHome.create();
  }
  
  public void ejbCreate()
  {
    _logger = getLogger();
  }
  
  public void ejbRemove()
  {

  }

  public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException
  {
    _ctx = context;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  private AuditTrailData deserializeToAuditTrailData(StreamMessage byteMsg) throws JMSException
  {
    String beanXML = byteMsg.readString();
    //AuditTrailData trailData = new AuditTrailData();
    //XMLBeanUtil.xmlToBean(beanXML, trailData);
    //NSL20070317 Change to use Castor
    AuditTrailData trailData = (AuditTrailData)XMLBeanUtil.xmlToObj(beanXML, AuditTrailData.class);
    return trailData;
  }
  
  public static void main(String[] args)
  {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    c.set(Calendar.DATE, c.get(Calendar.DATE)-7);
    
    System.out.println(c.getTime()+" "+c.get(Calendar.WEEK_OF_MONTH));
  }
}
