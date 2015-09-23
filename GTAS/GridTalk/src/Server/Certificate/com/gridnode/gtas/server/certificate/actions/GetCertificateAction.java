/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCertificateAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 21 2003    Jagadeesh          Created
 * Jul 26 2006    Tam Wei Xiang      Convert the serial num from base64 to hex
 *                                   format.
 */

package com.gridnode.gtas.server.certificate.actions;

import java.util.Map;

import com.gridnode.gtas.events.certificate.GetCertificateEvent;
import com.gridnode.gtas.model.certificate.CertificateEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class GetCertificateAction extends AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3665484157475761240L;
	public static final String ACTION_NAME = "GetCertificateAction";

  public GetCertificateAction()
  {
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return Certificate.convertToMap(entity,
                                    CertificateEntityFieldID.getEntityFieldID(),
                                    null);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetCertificateEvent getEvent = (GetCertificateEvent)event;
    Certificate cert = getManager().findCertificateByUID(getEvent.getCertificateUID());
    cert.setSerialNumber(convertBase64ToHex(cert.getSerialNumber()));
    return cert;

  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetCertificateEvent getEvent = (GetCertificateEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getCertificateUID()),
           };
  }

  private String convertBase64ToHex(String strInBase64)
  {
  	byte[] serialNumInBase64 = GridCertUtilities.decode(strInBase64);
  	StringBuilder serialNumInHex = new StringBuilder();
  	for(int i = 0; serialNumInBase64 != null && serialNumInBase64.length > i; i++)
  	{
  		serialNumInHex.append(GridCertUtilities.hexEncode(serialNumInBase64[i]));
  		if(! ((i+1) > serialNumInBase64.length) )
  		{
  			serialNumInHex.append(" ");
  		}
  	}
  	return serialNumInHex.toString();
  }
  
  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetCertificateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  private ICertificateManagerObj getManager() throws ServiceLookupException
  {
    return (ICertificateManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
           ICertificateManagerHome.class.getName(),
           ICertificateManagerHome.class,
           new Object[0]);
  }

}


