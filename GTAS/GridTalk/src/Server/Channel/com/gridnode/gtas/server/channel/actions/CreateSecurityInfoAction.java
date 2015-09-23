package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.CreateSecurityInfoEvent;
import com.gridnode.gtas.model.channel.ISecurityInfo;
import com.gridnode.gtas.model.channel.SecurityInfoEntityFieldId;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class CreateSecurityInfoAction extends AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5574747301773566119L;
	public static final String ACTION_NAME = "CreateSecurityInfoAction";
	

  protected Map convertToMap(AbstractEntity entity)
	{
		return SecurityInfo.convertToMap(entity,SecurityInfoEntityFieldId.getEntityFieldID(),null);
	}


	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		return getManager().createSecurityInfo((SecurityInfo)entity);
	}


	protected Object[] getErrorMessageParams(IEvent event)
	{
		return new Object[] {SecurityInfo.ENTITY_NAME};
	}


	protected AbstractEntity prepareCreationData(IEvent event)
	{
		CreateSecurityInfoEvent _event = (CreateSecurityInfoEvent)event;
		SecurityInfo securityInfo = new SecurityInfo();
		securityInfo.setName(_event.getName());
		securityInfo.setDescription(_event.getDescription());
		securityInfo.setDigestAlgorithm(_event.getDigestAlgorithm());
		securityInfo.setEncryptionCertificateID(_event.getEncyptionCertificateID());
		if (_event.getEncryptionLevel() != null)
			securityInfo.setEncryptionLevel((_event.getEncryptionLevel()).intValue());
		securityInfo.setEncryptionType(_event.getEncryptionType());
		securityInfo.setSignatureType(_event.getSignatureType());
		securityInfo.setSignatureEncryptionCertificateID(_event.getSignatureEncyptCertificateID());
		securityInfo.setIsPartner((_event.isPartner()).booleanValue());
		securityInfo.setCompressionType(_event.getCompressionType());
		securityInfo.setCompressionMethod(_event.getCompressionMethod());
		if (_event.getCompressionLevel() != null)
			securityInfo.setCompressionLevel((_event.getCompressionLevel()).intValue());
		securityInfo.setSequence(_event.getSequence());
		securityInfo.setEncryptionAlgorithm(_event.getEncryptionAlgorithm());
		
		if (_event.isPartner().booleanValue())
			// can only create for partner of "Other" category.
			securityInfo.setPartnerCategory(ISecurityInfo.CATEGORY_OTHERS);
		else
			securityInfo.setReferenceId(getEnterpriseID());
		
		return securityInfo;
		
	}


	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().getSecurityInfo(key);
	}


	protected String getActionName()
	{
		return ACTION_NAME;
	}


	protected Class getExpectedEventClass()
	{
		return CreateSecurityInfoEvent.class;
	}

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    //_event = (CreateSecurityInfoEvent) event;
//    if (_event.getDestType() != CreateJMSCommInfoEvent.QUEUE &&
//       _event.getDestType() != CreateJMSCommInfoEvent.TOPIC)
//      throw new EventException("Destination type not defined: " + _event.getDestType());
  }

  private IChannelManagerObj getManager()
    throws ServiceLookupException
  {
    return (IChannelManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IChannelManagerHome.class.getName(),
               IChannelManagerHome.class,
               new Object[0]);
  }




}


