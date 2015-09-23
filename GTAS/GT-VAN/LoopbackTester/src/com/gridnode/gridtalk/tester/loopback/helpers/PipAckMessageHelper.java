/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PipAckMessageHelper.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 21, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.helpers;

import com.gridnode.gridtalk.tester.loopback.dao.IRnifMessageDao;
import com.gridnode.gridtalk.tester.loopback.dao.MessageDaoException;
import com.gridnode.gridtalk.tester.loopback.entity.RnifMessageEntity;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * RN ACK message helper class
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class PipAckMessageHelper extends AbstractPipMessageHelper
{
	private IPipMessageHelper _refMessageHelper = null;
	private IRnifMessageDao _dao = null;
	/**
	 * 
	 */
	private PipAckMessageHelper()
	{
        this(null, null);
	}

    /**
     * 
     * @param dao
     * @param refMessageHelper
     */
    public PipAckMessageHelper(IRnifMessageDao dao)
    {       
        this(dao, null);
    }

	
	public PipAckMessageHelper(IRnifMessageDao dao, IPipMessageHelper refMessageHelper)
	{		
		_dao = dao;
		_refMessageHelper = refMessageHelper;
	}


	/**
     * @param refMessageHelper The refMessageHelper to set.
     */
    public void setRefMessageHelper(IPipMessageHelper refMessageHelper) {
        _refMessageHelper = refMessageHelper;
    }

    @Override
	protected IRnifMessageDao getRnifDao()
	{
		return _dao;
	}

	public String getPipCode()
	{
		// TODO Auto-generated method stub
		return RnifMessageEntity.PIP_ACK;
	}
	
	public String generateAckMessage(String message) throws MessageHelperException
	{
	String mn = "generateAckMessage";
    try
    {
    	RnifMessageEntity rnifAckTemplate = getRnifDao().getRnifMessageTemplate(RnifMessageEntity.PIP_ACK);
      String pipInstId = _refMessageHelper.findPipInstId(message);
      String senderDuns = _refMessageHelper.findReceiverDuns(message);
      String srcMsgTrackingId = _refMessageHelper.findMsgTrackingId(message);
      String initiatorDuns = _refMessageHelper.findInitiatorDuns(message);
      String preambleContentID = genContentID();
      String deliveryContentID = genContentID();
      String serviceHeaderContentID = genContentID();
      String serviceContentContentID = genContentID();
      String deliverTs = genTs();
      String receiverDuns = _refMessageHelper.findSenderDuns(message);
      String msgTrackingID = "reply_"+srcMsgTrackingId;
      String pipCode = _refMessageHelper.getPipCode();
      
      String template = getContent(rnifAckTemplate.getTemplateFile());
      
      String content = template.replaceAll(PREAMPLE_CID_PLACEHOLDER, preambleContentID);
      content = content.replaceAll(DELIVERY_CID_PLACEHOLDER, deliveryContentID);
      content = content.replaceAll(SERVICEH_CID_PLACEHOLDER, serviceHeaderContentID);
      content = content.replaceAll(SERVICEC_CID_PLACEHOLDER, serviceContentContentID);
      content = content.replaceAll(DELIVERY_TS_PLACEHOLDER, deliverTs);
      content = content.replaceAll(SENDER_DUNS_PLACEHOLDER, senderDuns);
      content = content.replaceAll(RECEIVER_DUNS_PLACEHOLDER, receiverDuns);
      content = content.replaceAll(INITIATOR_DUNS_PLACEHOLDER, initiatorDuns);
      content = content.replaceAll(MSG_TRACKING_ID_PLACEHOLDER, msgTrackingID);
      content = content.replaceAll(PIP_INST_ID_PLACEHOLDER, pipInstId);
      content = content.replaceAll(SRC_MSG_TRACKING_ID_PLACEHOLDER, srcMsgTrackingId);
      content = content.replaceAll(PIP_CODE_PLACEHOLDER, pipCode);
      
      Properties ackProp = rnifAckTemplate.getParams();
      Enumeration keys = ackProp.propertyNames();
      while (keys.hasMoreElements())
      {
        String key = (String)keys.nextElement();
        String val = ackProp.getProperty(key);
        content = content.replaceAll(key, val);
      }

//      Logger.debug(this.getClass().getSimpleName(), 
//    		  mn,
//    		  "Payload string is\n"+content);
      return content;
    }
    catch (IOException ex)
    {
      throw new MessageHelperException("Error reading template ", ex);
    }
		catch (MessageDaoException e)
		{
      throw new MessageHelperException("Error in DAO", e);
		}
		
	}
}
