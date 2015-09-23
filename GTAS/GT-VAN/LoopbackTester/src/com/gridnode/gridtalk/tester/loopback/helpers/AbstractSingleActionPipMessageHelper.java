/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractSingleActionPipMessageHelper.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 21, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.helpers;

import com.gridnode.gridtalk.tester.loopback.dao.MessageDaoException;
import com.gridnode.gridtalk.tester.loopback.entity.RnifMessageEntity;

/**
 * Abstract super class for single-action PIP message helpers
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public abstract class AbstractSingleActionPipMessageHelper extends AbstractPipMessageHelper
{
	public abstract String generatePipMessage(String senderDuns, String receiverDuns, String pipInstId, String documentId)
		throws MessageHelperException;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gridnode.gridtalk.tester.loopback.helpers.IPipMessageHelper#retrieveDocumentIdFromMessage(java.lang.String)
	 */
	public String retrieveDocumentIdFromMessage(String rnifMessage) throws MessageHelperException
	{
		try
		{
			RnifMessageEntity rnifMsgTemplate = getRnifDao()
					.getRnifMessageTemplate(getPipCode());
			return findValue(rnifMessage, rnifMsgTemplate
			         				.getPath(RnifMessageEntity.PATH_DOC_ID));
		}
		catch (MessageDaoException e)
		{
			throw new MessageHelperException("Error in DAO.", e);
		}

	}


}
