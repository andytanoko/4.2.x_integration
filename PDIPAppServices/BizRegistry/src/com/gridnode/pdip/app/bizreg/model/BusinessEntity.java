/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BusinessEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 * Aug 22 2002    Neo Sok Lay         Add isDeleted() and setDeleted() accessor
 *                                    methods.
 * Sep 30 2002    Neo Sok Lay         Add PartnerCategory field.
 * Jul 18 2003    Neo Sok Lay         Change EntityDescr.
 * Dec 22 2003    Neo Sok Lay         Add DomainIdentifiers field.
 * Mar 01 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.bizreg.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for BusinessEntity entity. A BusinessEntity keeps
 * tracks of a business unit of a company.<P>
 *
 * The Model:<BR><PRE>
 *   UId             - UID for a BusinessEntity entity instance.
 *   BusEntId        - ID of this BusinessEntity.
 *   EnterpriseId    - ID of the Enterprise owning this BusinessEntity.
 *   IsPartner       - Whether this BusinessEntity belongs to a Partner.
 *   PartnerCategory - Partner category type if the BusinessEntity belongs to a
 *                     partner.
 *   IsPublishable   - Whether this BusinessEntity is publishable to the UDDI
 *                     public registry.
 *   IsSyncToServer  - Whether this BusinessEntity is synchronized with the
 *                     UDDI public registry.
 *   Description     - Description of the BusinessEntity.
 *   State           - The state of this BusinessEntity.
 *   WhitePage       - WhitePage of the BusinessEntity.
 *   CanDelete       - Whether this BusinessEntity can be deleted by the user.
 *   Version         - The version of this BusinessEntity.
 *   DomainIdentifiers - The Collection of DomainIdentifier(s) that indicate the
 *                       identities of the BusinessEntity under different domains.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0
 * @since 2.0 I4
 */
public class BusinessEntity
  extends    AbstractEntity
  implements IBusinessEntity
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7835598154562534518L;
	protected String  _enterpriseId;
  protected String  _busEntId;
  protected String  _desc;
  protected boolean _isPartner        = false;
  protected boolean _isPublishable    = true;
  protected boolean _isSync           = true;
  protected WhitePage _whitePage;
  protected int     _state            = STATE_NORMAL;
  protected Short   _partnerCat;
  protected Set<DomainIdentifier> _domainIdentifiers = new HashSet<DomainIdentifier>();

  public BusinessEntity()
  {
  }

  // *************** Methods from AbstractEntity ***************************
  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityDescr()
  {
    return new StringBuffer(_busEntId).append('/').append(_desc).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ************************* Getters for attributes ***********************

  public String getEnterpriseId()
  {
    return _enterpriseId;
  }

  public String getBusEntId()
  {
    return _busEntId;
  }

  public String getDescription()
  {
    return _desc;
  }

  public boolean isPartner()
  {
    return _isPartner;
  }

  public boolean isPublishable()
  {
    return _isPublishable;
  }

  public boolean isSyncToServer()
  {
    return _isSync;
  }

  public WhitePage getWhitePage()
  {
    return _whitePage;
  }

  public int getState()
  {
    return _state;
  }

  public boolean isDeleted()
  {
    return _state == STATE_DELETED;
  }

  public Short getPartnerCategory()
  {
    return _partnerCat;
  }

  public Collection<DomainIdentifier> getDomainIdentifiers()
  {
    return _domainIdentifiers;
  }
  
  // ********************** Setters for attributes ***************************

  public void setEnterpriseId(String enterpriseId)
  {
    _enterpriseId = enterpriseId;
  }

  public void setBusEntId(String busEntId)
  {
    _busEntId = busEntId;
  }

  public void setDescription(String desc)
  {
    _desc = desc;
  }

  public void setPartner(boolean isPartner)
  {
    _isPartner = isPartner;
  }

  public void setPublishable(boolean isPublishable)
  {
    _isPublishable = isPublishable;
  }

  public void setSyncToServer(boolean isSync)
  {
    _isSync = isSync;
  }

  public void setWhitePage(WhitePage wp)
  {
    _whitePage = wp;
  }

  public void setState(int state)
  {
    _state = state;
  }

  public void setDeleted(boolean deleted)
  {
    _state = (deleted? STATE_DELETED : STATE_NORMAL);
  }

  public void setPartnerCategory(Short partnerCat)
  {
    _partnerCat = partnerCat;
  }
  
  public void setDomainIdentifiers(Collection<DomainIdentifier> domainIdentifiers)
  {
    _domainIdentifiers.clear();
    if (domainIdentifiers != null)
    {
      Long beUid = new Long(getUId());
      for (DomainIdentifier i : domainIdentifiers)
      {
        addDomainIdentifier(beUid, i);
      }
    }
  }
  
  private void addDomainIdentifier(Long beUid, DomainIdentifier domainIdentifier)
  {
    domainIdentifier.setBeUid(beUid);
    _domainIdentifiers.add(domainIdentifier);
  }
}
