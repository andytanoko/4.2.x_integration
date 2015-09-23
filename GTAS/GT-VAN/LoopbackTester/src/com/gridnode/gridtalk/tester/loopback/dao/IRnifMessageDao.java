/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.dao;

import com.gridnode.gridtalk.tester.loopback.entity.RnifMessageEntity;

/**
 * DAO for RNIF message template
 * @author Alain Ah Ming
 *
 */
public interface IRnifMessageDao {
	/**
	 * Return the RNIF message template
	 * @param pipCode The PIP code of the message template
	 * @see RnifMessageEntity#DIRECTION_GT_INBOUND
	 * @see RnifMessageEntity#DIRECTION_GT_OUTBOUND
	 * @return A String representing the RNIF message template
	 * @throws MessageDaoException if any error occurs while retrieving
	 * the message template
	 */
	public RnifMessageEntity getRnifMessageTemplate(String pipCode)
	 throws MessageDaoException;
}
