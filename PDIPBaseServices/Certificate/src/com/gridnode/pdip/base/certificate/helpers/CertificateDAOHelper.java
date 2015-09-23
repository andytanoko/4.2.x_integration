/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateDAOHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 15 2008    Yee Wah,Wong       #38   Created
 * Aug 09 2008    Tam Wei Xiang      #38   Modified method findICalAlarmByCertId(...)
 *                                         to avoid ORA-01722: invalid number
 */

package com.gridnode.pdip.base.certificate.helpers;


import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.certificate.model.*;

import com.gridnode.pdip.framework.db.dao.*;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.*;


import java.util.Collection;
import java.util.Iterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This is a EntityDAO implementation for UserBean. It takes care of
 * the Certificate entity as well as its dependent entity: CertificateSwapping.
 *
 * @author Yee Wah, Wong
 *
 * @version GT4.1.3
 * @since GT4.1.3
 */

public class CertificateDAOHelper implements IEntityDAO
{

  private static CertificateDAOHelper _self;
  
  private CertificateDAOHelper()
  {
    
  }
  
  /**
   * Get the singleton instance of this DAO.
   */
  public static CertificateDAOHelper getInstance()
  {
    if (_self == null)
      _self = new CertificateDAOHelper();

    return _self;
  }
  
//******************* Start Implement methods in IEntityDAO ****************

  /**@todo check return key, throw specific exception */
  public Long create(IEntity entity)
    throws Exception
  {
    Certificate certificate = (Certificate)entity;
    Long certKey = getCertificateDAO().create(certificate);
 
    return certKey;
  }
  
  

  public IEntity load(Long certUID)
  throws Exception
  {
    Certificate cert = (Certificate)getCertificateDAO().load(certUID);
    loadCertificateSwapping(cert);
    
    return cert;
  }
  
  public void store(IEntity certificate)
  throws Exception
  {
    getCertificateDAO().store(certificate);
  }
  
  public void remove(Long certUID)
    throws Exception
  {
	  
	removeICalAlarmByCertUID(certUID);
   
	Certificate cert = getTargetPendingCertByCertUID(certUID);
	if(cert != null)
		removeICalAlarmByCertUID(cert.getUId());
    
    getCertificateDAO().remove(certUID);
  }
  
  public Long findByPrimaryKey(Long primaryKey) throws Exception
  {
    return getCertificateDAO().findByPrimaryKey(primaryKey);
  }
  
  public Collection findByFilter(IDataFilter filter)
    throws Exception
  {
    return getCertificateDAO().findByFilter(filter);
  }
  
  public Collection getEntityByFilter(IDataFilter filter)
    throws Exception
  {
    Collection certs = getCertificateDAO().getEntityByFilter(filter);
    for (Iterator i=certs.iterator(); i.hasNext(); )
    {
      Certificate cert = (Certificate)i.next();
      loadCertificateSwapping(cert);
    }
    return certs;
  }
  
  public int getEntityCount(IDataFilter filter)
    throws Exception
  {
    return getCertificateDAO().getEntityCount(filter);
  }
  
  /**
   * @see com.gridnode.pdip.framework.db.dao.IEntityDAO#getFieldValuesByFilter(java.lang.Number, com.gridnode.pdip.framework.db.filter.IDataFilter)
   */
  public Collection getFieldValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
  {
    return getCertificateDAO().getFieldValuesByFilter(fieldId, filter);
  }

// ******************* Ends Implement methods in IEntityDAO ****************
  
  

  /**
   * Loads the Certificate Swapping record for a certificate.
   *
   * @param Certificate the certificate entity.
   */
  
  public void loadCertificateSwapping(Certificate certificate)
    throws Exception
  {
 	  
  	DateFormat formatter ;   
	String swapTime = "";
	CertificateSwapping certSwap = new CertificateSwapping();
	
	if(findICalAlarmByCertId(certificate.getUId())!=null)
	{
		  iCalAlarm iAlarm = findICalAlarmByCertId(certificate.getUId());
		  formatter = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
		  
		  certSwap.setAlarmUID(iAlarm.getUId());
	      swapTime = (formatter.format(iAlarm.getStartDt())).substring(11, 16);
	      
	      certSwap.setSwapTime(swapTime);
	      certSwap.setSwapDate(iAlarm.getStartDt());
		  
	      certificate.setCertificateSwapping(certSwap);
	}else
	{
		 certSwap.setAlarmUID(0);
		 certSwap.setSwapTime("");
		 certSwap.setSwapDate(null);
	}
	  
	certificate.setCertificateSwapping(certSwap);
	
  }

  /**
   * Search for ical_alarm record by cert uid
   *
   * @param certUId for certificate
   * @return iCalalarm Entity.
   *
   */
  
  private iCalAlarm findICalAlarmByCertId(Long certId)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();

    
    filter.addSingleFilter(null, iCalAlarm.TASK_ID, filter.getEqualOperator(), (certId==null? certId : certId.toString()), false);//TWX 09082008 Require convert to string to avoid 
    Collection result = getICalAlarmDAO().getEntityByFilter(filter);                                                                             //ORA-01722: invalid number
  
    if(result == null || result.isEmpty())
    {
      return null;
    }
    
    return (iCalAlarm) result.iterator().next();
  }
  
  private void removeICalAlarmByCertUID(Long certId) throws Exception
  {
	  	
	  iCalAlarm ical = findICalAlarmByCertId(certId);
	  
	  if(ical != null)
		  getICalAlarmDAO().remove((Long)ical.getKey());
	  
  }
  
  private Certificate getTargetPendingCertByCertUID(Long certUID) throws Exception
  {
	DataFilterImpl Filter = new DataFilterImpl();
	Filter.addSingleFilter(null,Certificate.REPLACEMENT_CERT_UID,Filter.getEqualOperator(),certUID,false);
    Collection certCollec = getCertificateDAO().getEntityByFilter(Filter);
    
    if(certCollec != null && !certCollec.isEmpty())
    {
    	return (Certificate)certCollec.iterator().next();  
    }
    
    return null;
  
  }

  /**
   * Get the data access object for the UserAccount entity.
   *
   * @return the IEntityDAO for UserAccount entity.
   */
  public IEntityDAO getCertificateDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(Certificate.ENTITY_NAME);
  }

  /**
   * Get the data access object for the UserAccountState entity.
   *
   * @return the IEntityDAO for UserAccountState entity.
   */
  public IEntityDAO getCertificateSwappingDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(CertificateSwapping.ENTITY_NAME);
  }
  
  /**
   * Get the data access object for the ICalAlarm entity.
   *
   * @return the IEntityDAO for ICalAlarm entity.
   */
  public IEntityDAO getICalAlarmDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(iCalAlarm.class.getName());
  }
  
  /* (non-Javadoc)
   * @see com.gridnode.pdip.framework.db.dao.IEntityDAO#create(com.gridnode.pdip.framework.db.entity.IEntity, boolean)
   */
  public Long create(IEntity entity, boolean useUID) throws Exception {
    throw new Exception("[CertificateDAOHelper.create(IEntity entity, boolean useUID)] Not Supported");
  }  
  
  public Collection getMinValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
  {
    return getCertificateDAO().getMinValuesByFilter(fieldId, filter);
  }
  
  public Collection getMaxValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
  {
    return getCertificateDAO().getMaxValuesByFilter(fieldId, filter);
  }
  
}
