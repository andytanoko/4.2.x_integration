/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizCertMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 * Jul 18 2003    Neo Sok Lay         Change EntityDescr.
 */
package com.gridnode.gtas.server.partnerprocess.model;

import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for BizCertMapping entity. A BizCertMapping is used to
 * define certificate pair for the enterprise and its partner when they are
 * engaged in a business process. The mapping would tell which of partner's
 * certificate will be used for message encryption and which of the enterprise's
 * certificate will be used for signature encryption when sending a business
 * document to the partner. The same pair of certificates will be used for
 * verification and decryption respectively when the enterprise receives a
 * business document from the partner. <P>
 *
 * The Model:<BR><PRE>
 *   UId         - UID for a BizCertMapping entity instance.
 *   CanDelete   - Whether the BizCertMapping can be deleted.
 *   Version     - Current version of the BizCertMapping instance.
 *   PartnerId   - ID of the Partner for whom the mapping is defined.
 *   PartnerCert - Certificate of the Partner to be used for encryption and
 *                 verification.
 *   OwnCert     - Certificate of the enterprise to be used for signing and
 *                 decryption.
 * </PRE>
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class BizCertMapping
  extends    AbstractEntity
  implements IBizCertMapping
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8829227740230384994L;
	protected String  _partnerID;
  protected Certificate _partnerCert;
  protected Certificate _ownCert;

  public BizCertMapping()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityDescr()
  {
    StringBuffer buff = new StringBuffer("Mapping Partner ");
    buff.append(getPartnerID()).append(" Cert: ");
    if (getPartnerCert() == null)
      buff.append("(null)");
    else
      buff.append(getPartnerCert().getEntityDescr());

    buff.append(" to Own Cert: ");

    if (getOwnCert() == null)
      buff.append("(null)");
    else
      buff.append(getOwnCert().getEntityDescr());

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

  public Certificate getPartnerCert()
  {
    return _partnerCert;
  }

  public String getPartnerID()
  {
    return _partnerID;
  }

  public Certificate getOwnCert()
  {
    return _ownCert;
  }

  // *************** Setters for attributes *************************

  public void setOwnCert(Certificate ownCert)
  {
    _ownCert = ownCert;
  }

  public void setPartnerCert(Certificate partnerCert)
  {
    _partnerCert = partnerCert;
  }

  public void setPartnerID(String partnerID)
  {
    _partnerID = partnerID;
  }

}