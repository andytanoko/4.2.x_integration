/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExpiryCheckerDelegate.java
 *
 ****************************************************************************
 * Date            Author              Changes
 ****************************************************************************
 * Aug 17, 2006    Tam Wei Xiang       Created
 * Dec 20, 2006    Tam Wei Xiang       Added new attr _trigerAlertAfterExpiredDay.
 *                                     This allow user to specify the number of day
 *                                     to trigger the cert expired alert start counting
 *                                     from the date the cert expired
 * Dec 21, 2006    Neo Sok Lay         Change input parameter names and sequence. 
 * Jul 18, 2009    Tam Wei Xiang       #560 - Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib                                             
 */
package com.gridnode.gtas.server.certificate.helpers;

import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.X509Principal;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.certificate.model.CertExpiryData;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerHome;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.alert.providers.DefaultProviderList;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.certificate.model.ICertificate;
import com.gridnode.pdip.base.certificate.model.IX500Name;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.TimeUtil;
//import com.rsa.certj.cert.X500Name;
//import com.rsa.certj.cert.X509Certificate;

/**
 * Check for both the expiring days and expiry date of certificate.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ExpiryCheckerDelegate
{
	private int _daysBefore;
	private String _alertName;
	private Integer _daysAfter;
  
	public ExpiryCheckerDelegate()
	{
		
	}
	
	public ExpiryCheckerDelegate(Integer daysBefore, Integer daysAfter, String alertName)
	{
		_daysBefore = daysBefore;
		_alertName = alertName;
    _daysAfter = daysAfter;
	}
	
	public void execute()
		throws Exception
	{ 
		CertificateLogger.log("[ExpiryCheckerDelegate] executing ... alertName is "+_alertName);
		Collection<Certificate> nonRevokedCerts = retrieveCertificates();
		Iterator<Certificate> i = nonRevokedCerts.iterator();
		Date currentDate = new Date();
		
		while(i.hasNext())
		{
			Certificate cert = i.next();
			if(isCertExpired(cert, currentDate))
			{
        if(! isValidAlertDayToRemind (_daysAfter))
        {
          CertificateLogger.log("[ExpiryCheckerDelegate] Not raising alert. The day to trigger alert after the cert expired date is "+_daysAfter);
          continue;
        }
        
        if(getExpiredDays(currentDate, getActualDate(cert.getEndDate())) <= _daysAfter)
        {
          CertificateLogger.log("[ExpiryCheckerDelegate] raising expired alert.");
          raiseAlert(_alertName, cert, null);
        }
			}
			else if(isCertExpiring(cert, _daysBefore, currentDate))
			{
				CertificateLogger.log("[ExpiryCheckerDelegate] raising expiring alert. ");
				
				int certExpiringDays = getExpiringDays(currentDate, getActualDate(cert.getEndDate()));
				raiseAlert(_alertName, cert, certExpiringDays);
			}
		}
	}
	
	/**
	 * Retrieve a collection of non-revoked certificates
	 * @return
	 */
	@SuppressWarnings("unchecked") 
	private Collection<Certificate> retrieveCertificates()
		throws Exception
	{
		IDataFilter filter = new DataFilterImpl();
		filter.addSingleFilter(null, ICertificate.REVOKEID, 
		                           filter.getEqualOperator(), new Integer(0), false);
		ICertificateManagerObj certManager = getCertManager();
		return certManager.getCertificate(filter);
	}
	
	/**
	 * Check if the certificate is expired.
	 * @param cert
	 * @param currentDate
	 * @return
	 */
	private boolean isCertExpired(Certificate cert, Date currentDate)
	{
		Date expiryDate = getActualDate(cert.getEndDate());
		return currentDate.getTime() > expiryDate.getTime(); 
	}
	
	/**
	 * Check if the cert is expiring.
	 * @param cert
	 * @param daysToRemind
	 * @param currentDate
	 * @return
	 */
	private boolean isCertExpiring(Certificate cert, int daysToRemind, Date currentDate)
	{
		long currentTime = currentDate.getTime();
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(currentTime);
		c.add(Calendar.DAY_OF_MONTH, daysToRemind);
		long expiringDate = c.getTimeInMillis();
		
		Date expiryDate = getActualDate(cert.getEndDate());
		long certExpiryDate = expiryDate.getTime();
		
		return certExpiryDate > currentTime && certExpiryDate <= expiringDate;
	}
	
	/**
	 * Get the total days of a certificate that will be expired given the currentDate
	 * @param currentDate
	 * @param certExpiryDate
	 * @return the day different between currentDate and certExpiryDate.
	 */
	private int getExpiringDays(Date currentDate, Date certExpiryDate)
	{
		long oneDayInMilliSeconds = 24 * 60 * 60 * 1000;
		long expiringDaysInMilliSeconds = certExpiryDate.getTime() - currentDate.getTime();
		
		return  new Long(expiringDaysInMilliSeconds/ oneDayInMilliSeconds).intValue(); //if result is 1.2, return will be 1. If 1.9, return will also be 1
	}
	
	/**
	 * Get the total expired start counting from currentDate to certExpiryDate.
	 * While comparing the currentDate will be adjusted to capture till hour unit. This 
	 * can help to ensure that the alert will be at least be triggered once.
	 * @param currentDate
	 * @param certExpiryDate
	 * @return the day different between currentDate and certExpiryDate. 
	 */
	private double getExpiredDays(Date currentDate, Date certExpiryDate)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		currentDate = c.getTime();
		
		double oneDayInMilliSeconds = 24 * 60 * 60 * 1000;
		double expiredDaysInMilliSeconds = currentDate.getTime() - certExpiryDate.getTime();
		
		return expiredDaysInMilliSeconds / oneDayInMilliSeconds;
	}
	
	/**
	 * Raise the cert expired/expiring alert.
	 * @param alertName
	 * @param cert the certificate that has been expired or will be expiring.
	 * @param certExpiringDays if the cert has expired, the value will be set to null.
	 */
	private void raiseAlert(String alertName, Certificate cert, Integer certExpiringDays)
	{
		try
		{
            DefaultProviderList providerList = new DefaultProviderList();
            providerList.addProvider(getCertProvider(cert, certExpiringDays));
			IAlertManagerObj alertMgr = getAlertManager();
			alertMgr.triggerAlert(alertName, providerList, (String)null);
		}
		catch(Exception ex)
		{
			CertificateLogger.error(ILogErrorCodes.GT_EXPIRY_CHECKER_DELEGATE,
			                        "[ExpiryCheckerDelegate.raiseAlert] Error in sending alert :"+alertName+" Error: ", ex);
		}
	}
	
	/**
	 * Create the Certificate Expiry Provider
	 * @param cert
	 * @param certExpiringDays
	 * @return
	 */
	private CertExpiryData getCertProvider(Certificate cert, Integer certExpiringDays) throws Exception
	{
		X509Certificate x509Cert = getX509Cert(cert);
        X509Principal issuerName = GridCertUtilities.getX509IssuerPrincipal(x509Cert);
		Hashtable displayIssuerName = GridCertUtilities.getX500Constants(issuerName);
		
		String subjectCommonName = (String)displayIssuerName.get(IX500Name.COMMAN_NAME);
		String orgUnit = (String)displayIssuerName.get(IX500Name.ORGANIZATIONAL_UNIT);
		String organization = (String)displayIssuerName.get(IX500Name.ORGANIZATION);
		String country = (String)displayIssuerName.get(IX500Name.COUNTRY);
		
		String serialNumber = convertByteToHex(GridCertUtilities.decode(cert.getSerialNumber()));
		String issuerN = new String(GridCertUtilities.decode(cert.getIssuerName()));
		
		Date validFrom = getActualDate(cert.getStartDate()); //make it current system timezone
		Date validTo = getActualDate(cert.getEndDate()); //make it current system timezone
		
		String category = cert.isPartner() ? "P" : "O";
		
		return new CertExpiryData(category, cert.getCertName(), issuerN, serialNumber,
		                          subjectCommonName,orgUnit,organization,country, validFrom, validTo, certExpiringDays);
	}
	
	private X509Certificate getX509Cert(Certificate cert)
	{
		return GridCertUtilities.loadCertificateFromString(cert.getCertificate());
	}
	
	private ICertificateManagerObj getCertManager() throws ServiceLookupException
	{
		return (ICertificateManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
		                                                               ICertificateManagerHome.class.getName(),
		                                                               ICertificateManagerHome.class,
		                                                               new Object[0]);
	}
	
	private IAlertManagerObj getAlertManager() throws ServiceLookupException
	{
		return (IAlertManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
                                                                   IAlertManagerHome.class.getName(),
                                                                   IAlertManagerHome.class,
                                                                   new Object[0]
		                                                        );
	}
	
	private String convertByteToHex(byte[] byteArray)
  {
  	StringBuilder serialNumInHex = new StringBuilder();
  	for(int i = 0; byteArray != null && byteArray.length > i; i++)
  	{
  		serialNumInHex.append(GridCertUtilities.hexEncode(byteArray[i]));
  		if(! ((i+1) > byteArray.length) )
  		{
  			serialNumInHex.append(" ");
  		}
  	}
  	return serialNumInHex.toString();
  }
	
	/**
	 * Due to the date we store in DB is a fake UTC time, while do comparing with the current date,
	 * we need to convert back to the local time.
	 * @param d
	 * @return
	 */
	private Date getActualDate(Date d)
	{
		return new Date(TimeUtil.utcToLocal(d.getTime()));
	}
	
  /**
   * User can specify the total day to remind after the cert already expired. But user may insert invalid
   * value like 0 or negative value.
   * @param trigerAlertAfterExpiredDay
   * @return false if the trigerAlertAfterExpiredDay is less than 0 or equal to null. true otherwise
   */
  private boolean isValidAlertDayToRemind(Integer trigerAlertAfterExpiredDay)
  {
    if(trigerAlertAfterExpiredDay == null || trigerAlertAfterExpiredDay <= 0)
    {
      return false;
    }
    else
    {
      return true;
    }
  }
  
	public String getAlertName()
	{
		return _alertName;
	}

	public void setAlertName(String name)
	{
		_alertName = name;
	}

	public Integer getDaysToRemind()
	{
		return _daysBefore;
	}

	public void setDaysToRemind(Integer toRemind)
	{
		_daysBefore = toRemind;
	}
  
	/**
	 * @param args
	 */
	/*
	public static void main(String[] args)
	{
		Calendar ca = Calendar.getInstance();
		Date currentDate = new Date(ca.getTimeInMillis());
		
		ca.add(Calendar.DAY_OF_MONTH, -1);
		//ca.add(Calendar.HOUR_OF_DAY,-1);
		//ca.add(Calendar.MINUTE, -20);
		
		ExpiryCheckerDelegate delegate = new ExpiryCheckerDelegate(10,"haha");
		Certificate c = new Certificate();
		c.setEndDate(new Date(TimeUtil.localToUtc(ca.getTimeInMillis())));
		
		boolean isExpired = delegate.isCertExpired(c, currentDate);
		System.out.println("is cert expired "+isExpired);
		
		boolean isExpring = delegate.isCertExpiring(c, 10, currentDate);
		System.out.println("is cert expring "+ isExpring);
		
		double expiredDay = delegate.getExpiredDays(currentDate, delegate.getActualDate(c.getEndDate()));
		System.out.println("expired day is "+expiredDay);
		
		
		int expiringDay = delegate.getExpiringDays(currentDate, delegate.getActualDate(c.getEndDate()));
		System.out.println("expiring day is "+expiringDay);
		//System.out.println("expiring in days "+delegate.getExpiringDays(currentDate, expiryDate));

		
	} */
}
