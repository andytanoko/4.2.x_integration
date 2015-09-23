/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.helpers;

import com.gridnode.gridtalk.tester.loopback.dao.IRnifMessageDao;
import com.gridnode.gridtalk.tester.loopback.dao.MessageDaoException;
import com.gridnode.gridtalk.tester.loopback.entity.RnifMessageEntity;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Helper class to handle RNIF messages logic.
 * @author Alain Ah Ming
 * 
 */
public class PipMessageHelper extends  AbstractSingleActionPipMessageHelper
{
	private IRnifMessageDao _rnifDao = null;

	private String _pipCode = null;
	
	
	private PipMessageHelper() {
//		this(new RnifMessageDao());
	}

	public PipMessageHelper(String pipCode, IRnifMessageDao actionDao)
	{
		_pipCode = pipCode;
		_rnifDao = actionDao;
	}
	
	/**
	 * Generates an RNIF message from the specified PIP instance Id,
	 * document Id and document template
	 * @param pipInstId The PIP instance ID for which to create a message
	 * @param documentId The document Id to inject into the message
	 * @return The RNIF message generated
	 * @throws MessageHelperException 
	 * @throws IOException 
	 */
	public String generatePipMessage(String senderDuns, String receiverDuns, String pipInstId, String documentId) 
		throws MessageHelperException 
	{
		String mn = "generateRnifMessage";
		logDebug(mn, "Start.PIP inst. Id.:"+pipInstId);
		String preambleContentID = genContentID();
		String deliveryContentID = genContentID();
		String serviceHeaderContentID = genContentID();
		String serviceContentContentID = genContentID();
		String deliverTs = genTs();
		String initiatorDuns = senderDuns;
		String msgTrackingID = "1";
		String docGenTs = genTs();

		String template = null;
		try
		{
			RnifMessageEntity rnifMsg = _rnifDao.getRnifMessageTemplate(getPipCode());
			template = getContent(rnifMsg.getTemplateFile());
			String content = 
				template.replaceAll(PREAMPLE_CID_PLACEHOLDER, preambleContentID);
			content = content.replaceAll(DELIVERY_CID_PLACEHOLDER, deliveryContentID);
			content = content.replaceAll(SERVICEH_CID_PLACEHOLDER, serviceHeaderContentID);
			content = content.replaceAll(SERVICEC_CID_PLACEHOLDER, serviceContentContentID);
			content = content.replaceAll(DELIVERY_TS_PLACEHOLDER, deliverTs);
			content = content.replaceAll(SENDER_DUNS_PLACEHOLDER, senderDuns);
			content = content.replaceAll(RECEIVER_DUNS_PLACEHOLDER, receiverDuns);
			content = content.replaceAll(INITIATOR_DUNS_PLACEHOLDER, initiatorDuns);
			content = content.replaceAll(MSG_TRACKING_ID_PLACEHOLDER, msgTrackingID);
			content = content.replaceAll(PIP_INST_ID_PLACEHOLDER, pipInstId);
			content = content.replaceAll(DOC_GEN_TS_PLACEHOLDER, docGenTs);
			content = content.replaceAll(DOC_ID_PLACEHOLDER, documentId);
			
			// Get action-specific variables (?)
			Properties actionProp = rnifMsg.getParams();
			Enumeration keys = actionProp.propertyNames();
			while (keys.hasMoreElements()) 
			{
				String key = (String) keys.nextElement();
				String val = actionProp.getProperty(key);
				content = content.replaceAll(key, val);
			}
//			logDebug(mn, "Content:\n"+content);
			return content;
		}
		catch (IOException e) 
		{
			throw new MessageHelperException("IO Error reading template", e);
		}
		catch (MessageDaoException e)
		{
			throw new MessageHelperException("Error in DAO", e);
		}
	}

//	public String findPipInstId(String content)
//	  {
//	    return 
//	    findValue(content, 
//	    		_rnifDao.getRnifMessageTemplate(RnifMessageEntity.PIP_0C2).
//	    		getPath(RnifMessageEntity.PATH_PIP_INSTANCE_ID));
//	  }
//	  
//	public String findReceiverDuns(String content)
//	  {
//	  	return findValue(content, 
//    		_rnifDao.getRnifMessageTemplate(RnifMessageEntity.PIP_0C2).
//    		getPath(RnifMessageEntity.PATH_RECEIVER_DUNS));
//	  }
//
//	public String findSenderDuns(String content)
//	  {
//	  	return findValue(content, 
//    		_rnifDao.getRnifMessageTemplate(RnifMessageEntity.PIP_0C2).
//    		getPath(RnifMessageEntity.PATH_SENDER_DUNS));
//	  }
//
//	public String findMsgTrackingId(String content)
//	  {
//	    return findValue(content,
//	                 		_rnifDao.getRnifMessageTemplate(RnifMessageEntity.PIP_0C2).
//	                		getPath(RnifMessageEntity.PATH_MESSAGE_TRACKING_ID));
//	  }
//
//	public String findInitiatorDuns(String content)
//	  {
//	    return findValue(content,
//		                 		_rnifDao.getRnifMessageTemplate(RnifMessageEntity.PIP_0C2).
//		                		getPath(RnifMessageEntity.PATH_INITIATOR_DUNS));
//	  }

//	public String generateActionAckMsg(String actionContent)
//	  {
//		String mn = "generateActionAckMsg";
//		RnifMessageEntity rnifAckTemplate = _rnifDao.getRnifMessageTemplate(RnifMessageEntity.PIP_ACK);
//	    String pipInstId = findPipInstId(actionContent);
//	    String senderDuns = findReceiverDuns(actionContent);
//	    String srcMsgTrackingId = findMsgTrackingId(actionContent);
//	    String initiatorDuns = findInitiatorDuns(actionContent);
//	    String preambleContentID = genContentID();
//	    String deliveryContentID = genContentID();
//	    String serviceHeaderContentID = genContentID();
//	    String serviceContentContentID = genContentID();
//	    String deliverTs = genTs();
//	    String receiverDuns = findSenderDuns(actionContent);
//	    String msgTrackingID = "reply_"+srcMsgTrackingId;
//	   
//	    try
//	    {
//	      String template = getContent(rnifAckTemplate.getTemplateFile());
//	      
//	      String content = template.replaceAll(PREAMPLE_CID_PLACEHOLDER, preambleContentID);
//	      content = content.replaceAll(DELIVERY_CID_PLACEHOLDER, deliveryContentID);
//	      content = content.replaceAll(SERVICEH_CID_PLACEHOLDER, serviceHeaderContentID);
//	      content = content.replaceAll(SERVICEC_CID_PLACEHOLDER, serviceContentContentID);
//	      content = content.replaceAll(DELIVERY_TS_PLACEHOLDER, deliverTs);
//	      content = content.replaceAll(SENDER_DUNS_PLACEHOLDER, senderDuns);
//	      content = content.replaceAll(RECEIVER_DUNS_PLACEHOLDER, receiverDuns);
//	      content = content.replaceAll(INITIATOR_DUNS_PLACEHOLDER, initiatorDuns);
//	      content = content.replaceAll(MSG_TRACKING_ID_PLACEHOLDER, msgTrackingID);
//	      content = content.replaceAll(PIP_INST_ID_PLACEHOLDER, pipInstId);
//	      content = content.replaceAll(SRC_MSG_TRACKING_ID_PLACEHOLDER, srcMsgTrackingId);
//	      
//	      Properties ackProp = rnifAckTemplate.getParams();
//	      Enumeration keys = ackProp.propertyNames();
//	      while (keys.hasMoreElements())
//	      {
//	        String key = (String)keys.nextElement();
//	        String val = ackProp.getProperty(key);
//	        content = content.replaceAll(key, val);
//	      }
//
//	      Logger.debug(this.getClass().getSimpleName(), 
//	    		  mn,
//	    		  "Payload string is "+content.length());
//	      return content;
//	    }
//	    catch (IOException ex)
//	    {
//	      Logger.error(this.getClass().getSimpleName(), 
//	    		  mn, 
//	    		  "Error reading action template "+rnifAckTemplate.getTemplateFile(),
//	    		  ex);
//		    return null;
//	    }
//	  }

	  public String retrieveDocumentIdFromMessage(String rnifMessage) throws MessageHelperException
	  {
		  RnifMessageEntity rnifMsgTemplate;
			try
			{
				rnifMsgTemplate = _rnifDao.getRnifMessageTemplate(getPipCode());
		  	return findValue(rnifMessage, 
                         rnifMsgTemplate.getPath(RnifMessageEntity.PATH_DOC_ID));
			}
			catch (MessageDaoException e)
			{
				throw new MessageHelperException("Error in DAO", e);
			}
		  
	  }	  


		public String getPipCode()
		{
			// TODO Auto-generated method stub
			return _pipCode;
		}

		@Override
		protected IRnifMessageDao getRnifDao()
		{
			return _rnifDao;
		}
}

