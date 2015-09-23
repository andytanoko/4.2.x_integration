/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateSwappingMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 24, 2007   Yee Wah, Wong       Created
 * Sep 28, 2009   Tam Wei Xiang       #1032: reload the cert again to prevent
 *                                    concurrent modification on the cert entity
 */

package com.gridnode.pdip.base.certificate.facade.ejb;


import java.util.Collection;

import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;
import com.gridnode.pdip.base.certificate.exceptions.CertificateException;
import com.gridnode.pdip.base.certificate.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.base.certificate.helpers.RNIFConfiguration;
import com.gridnode.pdip.base.certificate.helpers.CertificateEntityHandler;
import com.gridnode.pdip.base.certificate.helpers.CertificateLogger;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;

public class CertificateSwappingMDBean
extends TimeInvokeMDBean
{
	private static final long serialVersionUID = -5267330383215610187L;
	
	protected void invoke(AlarmInfo info)
	{	
		long threshold = 0;
		
		try
		{
			DataFilterImpl filter = new DataFilterImpl();
	        filter.addSingleFilter(null,Certificate.UID,filter.getEqualOperator(),info.getTaskId(),false);
	        Collection col = getCertManager().getCertificate(filter);
	        
	       if(col!=null && col.size()>0)
	       {
	        	Certificate cert = (Certificate)col.iterator().next();
	        	
	        	if(cert.isPartner())
	        	{
	        		threshold = -1;
	        	}
	        	else
	        	{
	        		threshold = RNIFConfiguration.getSignCertTakeOverPeriod();
	        	}
	        		 	
	        	Certificate pendingCert = getEntityHandler().getCert(cert, threshold, true);
	        	
            
	          	try
	          	{
                //TWX 20090928 #1032 reload the entity again to prevent concurrent modification on the Cert entity
                cert = (Certificate)getEntityHandler().getEntityByKey(Long.valueOf(info.getTaskId()));
                cert.setReplacementCertUid((Long)pendingCert.getKey());
                
	          		CertificateLogger.log("[CertificateSwappingMDBean] Set replacement cert. Replacement cert uid for certificate "+cert.getCertName()+" will be "+pendingCert.getKey());
	          		cert.setIsForcedGetReplacementCert(true); //WYW 20080730
	          		getEntityHandler().update(cert);
	          	}
	          	catch(Throwable th)
	          	{
	          		throw new CertificateException("[CertificateSwappingMDBean] Error in updating the replacement cert UID "+pendingCert.getKey()+" for cert with UID "+cert.getKey(), th);
	          	}
	        	
	          	
	          	//cancel(delete) the iCalAlarm after the alarm kicked start successfully
	          	DataFilterImpl iCalFilter = new DataFilterImpl();
	          	iCalFilter.addSingleFilter(null,iCalAlarm.UID,iCalFilter.getEqualOperator(),info.getAlarmUid(),false);
	          	
	          	getICalManager().cancelAlarmByFilter(iCalFilter);
	       }
	             
		}catch(Exception ex)
		{
			CertificateLogger.error(ILogErrorCodes.CERTTIFICATE_SWAPPING_INVOKE, "[CertificateSwappig.invoke] Problem of swapping certificate: "+ex.getMessage(), ex);
		}
	}
	
	private ICertificateManagerObj getCertManager()
    throws ServiceLookupException
    {
	    return (ICertificateManagerObj)ServiceLocator.instance(
	             ServiceLocator.CLIENT_CONTEXT).getObj(
	               ICertificateManagerHome.class.getName(),
	               ICertificateManagerHome.class,
	               new Object[0]);
    }
	
	public static IiCalTimeMgrObj getICalManager() throws ServiceLookupException
    {
      return (IiCalTimeMgrObj) ServiceLocator
        .instance(ServiceLocator.CLIENT_CONTEXT)
        .getObj(
          IiCalTimeMgrHome.class.getName(),
          IiCalTimeMgrHome.class,
          new Object[0]);
    }
	
	
	public CertificateEntityHandler getEntityHandler()
	{
	      return CertificateEntityHandler.getInstance();
	}
	
	 
	

}
