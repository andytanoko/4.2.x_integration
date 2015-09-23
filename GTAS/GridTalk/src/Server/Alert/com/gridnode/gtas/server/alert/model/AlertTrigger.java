/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTrigger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17 2002    Neo Sok Lay         Created
 * Jul 18 2003    Neo Sok Lay         Change EntityDescr.
 */
package com.gridnode.gtas.server.alert.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * This is an object model for Trigger entity. A Trigger is used to
 * determine which alert to raise based on
 * the document type, partner id, partner type, partner group.<P>
 *
 * The Model:<BR><PRE>
 *   UID          - UID for a AlertTrigger entity instance.
 *   CanDelete    - Whether the AlertTrigger can be deleted.
 *   Version      - Version of the AlertTrigger entity instance.
 *   Level        - The level of the trigger.
 *   AlertType    - Type of Alert
 *   Alert        - The alert to raise
 *   DocType      - Document type, criterion for triggering alert of level 1-4
 *   PartnerType  - Partner type, criterion for triggering alert of level 2-3
 *   PartnerGroup - Partner group, criterion for triggering alert of level 3
 *   PartnerID    - Partner Id, criterion for triggering alert of level 4
 *   Enabled      - Whether the AlertTrigger is to be enabled i.e. take effect.
 *                  If the criteria match for a lowel level trigger, e.g. level 4,
 *                  but the trigger is not enabled, the alert will not be raised.
 *   AttachDoc    - Whether to attach the related document as email attachment,
 *                  applicable for email alert actions.
 *   Recipients   - Dynamic recipients based on alert type, document, or partner.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.1
 */
public class AlertTrigger
       extends AbstractEntity
       implements IAlertTrigger
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 934273330747469034L;
	private static final Hashtable _LEVEL_NULL_FIELDS_MAP = new Hashtable();
  private static final Hashtable _LEVEL_COND_FIELDS_MAP = new Hashtable();
  static
  {
    _LEVEL_NULL_FIELDS_MAP.put(AlertTrigger.LEVEL_0, AlertTrigger.LEVEL_0_NULL_FIELDS);
    _LEVEL_NULL_FIELDS_MAP.put(AlertTrigger.LEVEL_1, AlertTrigger.LEVEL_1_NULL_FIELDS);
    _LEVEL_NULL_FIELDS_MAP.put(AlertTrigger.LEVEL_2, AlertTrigger.LEVEL_2_NULL_FIELDS);
    _LEVEL_NULL_FIELDS_MAP.put(AlertTrigger.LEVEL_3, AlertTrigger.LEVEL_3_NULL_FIELDS);
    _LEVEL_NULL_FIELDS_MAP.put(AlertTrigger.LEVEL_4, AlertTrigger.LEVEL_4_NULL_FIELDS);

    _LEVEL_COND_FIELDS_MAP.put(AlertTrigger.LEVEL_0, AlertTrigger.LEVEL_0_COND_FIELDS);
    _LEVEL_COND_FIELDS_MAP.put(AlertTrigger.LEVEL_1, AlertTrigger.LEVEL_1_COND_FIELDS);
    _LEVEL_COND_FIELDS_MAP.put(AlertTrigger.LEVEL_2, AlertTrigger.LEVEL_2_COND_FIELDS);
    _LEVEL_COND_FIELDS_MAP.put(AlertTrigger.LEVEL_3, AlertTrigger.LEVEL_3_COND_FIELDS);
    _LEVEL_COND_FIELDS_MAP.put(AlertTrigger.LEVEL_4, AlertTrigger.LEVEL_4_COND_FIELDS);
  }

  protected int     _level;
  protected Long    _alertUID;
  protected String  _docType;
  protected String  _partnerType;
  protected String  _partnerGroup;
  protected String  _partnerId;
  protected String _alertType;
  protected boolean _enabled        = true;
  protected boolean _attachDoc      = false;
  protected List    _recipients     = new ArrayList();

  public AlertTrigger()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityDescr()
  {
    StringBuffer criteria = new StringBuffer();
    int level = getLevel();
    if (level > LEVEL_0.intValue())
      criteria.append("/Doc Type ").append(getDocumentType());
    if (level > LEVEL_1.intValue())
    {
      if (level < LEVEL_4.intValue())
      {
        criteria.append("/Partner Type ").append(getPartnerType());
        if (level == LEVEL_3.intValue())
          criteria.append("/Partner Group ").append(getPartnerGroup());
      }
      else
        criteria.append("/Partner ID ").append(getPartnerId());
    }      

    StringBuffer buff = new StringBuffer();
    buff.append("Level ").append(getLevel());
    buff.append("/Alert Type ").append(getAlertType());
    buff.append(criteria.toString());
    return buff.toString();
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

  public int getLevel()
  {
    return _level;
  }

  public Long getAlertUID()
  {
    return _alertUID;
  }

  public String getAlertType()
  {
    return _alertType;
  }

  public String getDocumentType()
  {
    return _docType;
  }

  public String getPartnerGroup()
  {
    return _partnerGroup;
  }

  public String getPartnerType()
  {
    return _partnerType;
  }

  public String getPartnerId()
  {
    return _partnerId;
  }

  public boolean isEnabled()
  {
    return _enabled;
  }

  public boolean isAttachDoc()
  {
    return _attachDoc;
  }

  public List getRecipients()
  {
    return _recipients;
  }

  // *************** Setters for attributes *************************

  public void setLevel(int level)
  {
    _level = level;
  }

  public void setAlertUID(Long alertUID)
  {
    _alertUID = alertUID;
  }

  public void setAlertType(String alertType)
  {
    _alertType = alertType;
  }

  public void setDocumentType(String docType)
  {
    _docType = docType;
  }

  public void setPartnerGroup(String partnerGroup)
  {
    _partnerGroup = partnerGroup;
  }

  public void setPartnerType(String partnerType)
  {
    _partnerType = partnerType;
  }

  public void setPartnerId(String partnerId)
  {
    _partnerId = partnerId;
  }

  public void setEnabled(boolean enabled)
  {
    _enabled = enabled;
  }

  public void setAttachDoc(boolean attachDoc)
  {
    _attachDoc = attachDoc;
  }

  public void setRecipients(List recipients)
  {
    _recipients = recipients;
  }

  public Number[] getLevelNullFields()
  {
    return (Number[])_LEVEL_NULL_FIELDS_MAP.get(getFieldValue(LEVEL));
  }

  public Number[] getLevelCondFields()
  {
    return (Number[])_LEVEL_COND_FIELDS_MAP.get(getFieldValue(LEVEL));
  }

  public void nullifyByLevel()
  {
    Number[] nullFields = getLevelNullFields();

    if (nullFields != null)
    {
      for (int i=0; i<nullFields.length; i++)
        setFieldValue(nullFields[i], null);
    }
    else
      throw new java.lang.RuntimeException("Invalid Trigger Level: "+getLevel());
  }

  public boolean validate()
  {
    boolean valid = true;
    Number[] condFields = getLevelCondFields();

    if (condFields != null)
    {
      for (int i=0; i<condFields.length && valid; i++)
      {
        String val = (String)getFieldValue(condFields[i]);
        valid = (val != null && val.trim().length()>0);
      }
    }
    else
      throw new java.lang.RuntimeException("Invalid Trigger Level: "+getLevel());
    return valid;
  }
}