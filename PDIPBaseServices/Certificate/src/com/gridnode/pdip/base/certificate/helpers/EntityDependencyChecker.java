/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDependencyChecker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 25, 2006    Tam Wei Xiang       Created
 * Feb 09 2007		Alain Ah Ming				Log error codes
 */
package com.gridnode.pdip.base.certificate.helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.gridnode.pdip.base.certificate.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This checker perform dependency checking on the Base Certificate module.
 * 
 * @author Tam Wei Xiang
 * @since GT 4.0
 * @version GT 4.0 VAN
 */
public class EntityDependencyChecker
{
	public EntityDependencyChecker() {}
	
	/**
	 * Check is there any certificate(s) that treat the cert represent by the
	 * uid as its(their) related cert.
	 * @param uid
	 * @return a Set of Certificate entities(the replacement cert) that depend on the certificate identified by the 
	 *         uid or return null if such a dependency doesn't exist.
	 */
	public Set checkDependentReplacementUsage(Long uid)
	{
		IDataFilter filter = new DataFilterImpl();
		filter.addSingleFilter(null, Certificate.RELATED_CERT_UID, filter.getEqualOperator(), uid,false);
		
		Collection c = null;
		try
		{
			c = CertificateEntityHandler.getInstance().getCertificate(filter);
		}
		catch(Throwable th)
		{
			CertificateLogger.warn("[EntityDependencyChecker.checkDependentRelatedCertUsage] Error getting cert correspond replacement cert list", th);
		}
		
		Set<Certificate> s = new HashSet<Certificate>();
		if(c!= null)
		{
			s.addAll(c);
		}
		return s;
	}
}
