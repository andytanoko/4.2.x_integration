/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 * Jul 18 2003    Neo Sok Lay         Change EntityDescr.
 */
package com.gridnode.gtas.server.partnerprocess.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for ProcessMapping entity. A ProcessMapping is used to
 * define business process mapping to a trading partner. The mapping would
 * tell what business process the enterprise is having with its partner, and
 * what role is the enterprise playing in the process.<P>
 *
 * The Model:<BR><PRE>
 *   UId                  - UID for a ProcessMapping entity instance.
 *   CanDelete            - Whether the ProcessMapping can be deleted.
 *   Version              - Current version of the ProcessMapping instance.
 *   ProcessDef           - Name of the Process to map.
 *   IsInitiatingRole     - Whether the enterprise is playing the Initiating role in
 *                          the process.
 *   DocType              - If the enterprise is playing the responding role, what
 *                          type of document it will be responding to.
 *   SendChannelUID       - UID of the partner's channel that documents will be
 *                          sent via if the enterprise is playing the initiating
 *                          role.
 *   PartnerID            - ID of the trading partner.
 *   ProcessIndicatorCode - Process indicator code of the ProcessDef.
 *   ProcessVersionID     - Version identifier of the ProcessDef.
 *   PartnerRoleMapping   - UID of the RoleMapping for the partner's role.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0 I7
 */
public class ProcessMapping
       extends AbstractEntity
       implements IProcessMapping
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4255720561789105998L;
	protected boolean _isInitiatingRole = false;
  protected String  _processDef;
  protected String  _docType;
  protected String  _processIndicatorCode;
  protected String  _processVersionID;
  protected String  _partnerID;
  protected Long    _partnerRoleMapping;
  protected Long _sendChannelUID;

  public ProcessMapping()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityDescr()
  {
    String role = isInitiatingRole() ? "Initiator" : "Responder";

    return new StringBuffer().append(getProcessDef()).append('/').append(
                  getPartnerID()).append('/').append(role).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // *************** Getters for attributes *************************

  public Long getSendChannelUID()
  {
    return _sendChannelUID;
  }

  public String getProcessDef()
  {
    return _processDef;
  }

  public String getProcessIndicatorCode()
  {
    return _processIndicatorCode;
  }

  public String getDocumentType()
  {
    return _docType;
  }

  public Long getPartnerRoleMapping()
  {
    return _partnerRoleMapping;
  }

  public String getProcessVersionID()
  {
    return _processVersionID;
  }

  public String getPartnerID()
  {
    return _partnerID;
  }

  public boolean isInitiatingRole()
  {
    return _isInitiatingRole;
  }

  // *************** Setters for attributes *************************

  public void setSendChannelUID(Long sendChannelUID)
  {
    _sendChannelUID = sendChannelUID;
  }

  public void setProcessDef(String processDef)
  {
    _processDef = processDef;
  }

  public void setProcessIndicatorCode(String processIndicatorCode)
  {
    _processIndicatorCode = processIndicatorCode;
  }

  public void setDocumentType(String docType)
  {
    _docType = docType;
  }

  public void setPartnerRoleMapping(Long partnerRoleMapping)
  {
    _partnerRoleMapping = partnerRoleMapping;
  }

  public void setProcessVersionID(String processVersionID)
  {
    _processVersionID = processVersionID;
  }

  public void setPartnerID(String partnerID)
  {
    _partnerID = partnerID;
  }

  public void setInitiatingRole(boolean isInitiatingRole)
  {
    _isInitiatingRole = isInitiatingRole;
  }
}