/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizCertMappingDAOHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 * Aug 01 2008	  Wong Yee Wah		  #38  Added method: getEntityByFilter()
 * 									  Added method: load()
 * 									  Added method: getCertificateDAOHelper()	
 */
package com.gridnode.gtas.server.partnerprocess.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.base.certificate.helpers.CertificateDAOHelper;
 
/**
 * Helper for DAO level checking.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class BizCertMappingDAOHelper
{
  private static BizCertMappingDAOHelper _self = null;
  private static Object                  _lock = new Object();

  private BizCertMappingDAOHelper()
  {

  }

  public static BizCertMappingDAOHelper getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new BizCertMappingDAOHelper();
      }
    }
    return _self;
  }

  /**
   * Check if the specified BizCertMapping will result in duplicate when
   * created or updated.
   *
   * @param mapping The BizCertMapping to check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the mapping itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * BizCertMapping will result in duplicates.
   */
  public void checkDuplicate(
    BizCertMapping mapping, boolean checkKey) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BizCertMapping.PARTNER_ID,
      filter.getEqualOperator(), mapping.getPartnerID(), false);

    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), BizCertMapping.UID,
        filter.getNotEqualOperator(), mapping.getKey(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException(
        "BizCert Mapping ["+ mapping.getEntityDescr() + "] already exists!");
  }

  /**
   * Check whether a BizCertMapping can be deleted.
   *
   * @param mapping The BizCertMapping to check.
   *
   * @exception ApplicationException The BizCertMapping is not allowed to be
   * deleted.
   */
  public void checkCanDelete(BizCertMapping mapping) throws Exception
  {
    if (!mapping.canDelete())
      throw new ApplicationException("BizCertMapping not allowed to be deleted!");
  }
  
  public Collection getEntityByFilter(IDataFilter filter)
  throws Exception
  {
	  BizCertMapping bizCertMapping;
	  Certificate pCert;
	  Certificate oCert;
	  Collection BCMCollection = getDAO().getEntityByFilter(filter);
	  for (Iterator i=BCMCollection.iterator(); i.hasNext(); )
	  {
		bizCertMapping = (BizCertMapping)i.next();
		
		pCert = bizCertMapping.getPartnerCert();
		getCertificateDAOHelper().loadCertificateSwapping(pCert);
	    
	    oCert = bizCertMapping.getOwnCert();
	    getCertificateDAOHelper().loadCertificateSwapping(oCert);
	  }
	  return BCMCollection;
  }
 
  public IEntity load(Long certUID)
  throws Exception
  {
    BizCertMapping bizCertMapping = (BizCertMapping)getDAO().load(certUID);
    
    if(bizCertMapping!=null)
    {
    	getCertificateDAOHelper().loadCertificateSwapping(bizCertMapping.getPartnerCert());
    	getCertificateDAOHelper().loadCertificateSwapping(bizCertMapping.getOwnCert());
    }
    
    return bizCertMapping;
  }

  private IEntityDAO getDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(BizCertMapping.ENTITY_NAME);
  }
  
  public IEntityDAO getICalAlarmDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(iCalAlarm.class.getName());
  }
  
  public CertificateDAOHelper getCertificateDAOHelper()
  {
	  return CertificateDAOHelper.getInstance();
  }


}