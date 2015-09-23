/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractInfoHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Jan 08, 2007    Tam Wei Xiang       Change the PK from Long to String
 */
package com.gridnode.gtas.audit.tracking.helpers;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import com.gridnode.gtas.audit.model.BizDocument;
import com.gridnode.gtas.audit.model.BizEntityGroupMapping;
import com.gridnode.gtas.audit.model.IAuditTrailEntity;
import com.gridnode.gtas.audit.model.helpers.MimeConverter;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.dao.AuditTrailEntityDAO;
import com.gridnode.gtas.audit.dao.exception.AuditTrailDBServiceException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public abstract class AbstractInfoHelper
{
  private final String CLASS_NAME = "AbstractInfoHelper";
  //private AuditTrailEntityDAO _dao = null;
  
  /*
  public AbstractInfoHelper(AuditTrailEntityDAO dao) 
  {
    _dao = dao;
  }*/
  
  public AbstractInfoHelper() {}
  
  /**
   * Persist the AuditTrail entity into DB.
   * @param obj
   * @return
   * @throws AuditTrailTrackingException
   */
  
  public String persistAuditTrailEntity(IAuditTrailEntity obj) throws AuditTrailTrackingException
  {
    try
    {
      return getDAO().insertAuditTrailEntity(obj);
    }
    catch(Exception ex)
    {    	
      throw new AuditTrailTrackingException("Unexpected error in persisting AuditTrailEntity. Error: "+ex.getMessage()+": "+obj, ex);
    }
  }
  
  /**
   * Update the AuditTrail entity
   * @param obj
   */
  
  public void updateAuditTrailEntity(IAuditTrailEntity obj)
  {
    getDAO().update(obj);
  } 
  
  /**
   * Retrieve the groupName given the customer's beID
   * @param beID
   * @return
   * @throws AuditTrailTrackingException
   */
  public String getGroupName(String beID) throws AuditTrailTrackingException
  {
    return GroupMappingHelper.getInstance().getGroupMapping(beID, getDAO());
  }

  /**
   * It will convert the businessDocs into a single MimeMsg.
   * 
   * If the businessDocs contain more than one, we will package them into mime msg. Else we will based on 
   * isRequiredPack flag in the BusinessDocument to determine whether to pack it into mime format.
   * 
   * For the case audit file, since it is already in mime format, so no require to package into mime.
   * 
   * @param businessDocs an array of BusinessDocument instance
   * @return a BizDocument instance which contain the MimeMsg in byte[] form, and some bizDoc info. Null if the
   *         businessDocs contain 0 BusinessDocument instance or the actual payload (the content) in BusinessDocument
   *         is null.
   */
  public BizDocument createBizDocument(BusinessDocument[] businessDocs, String groupName) throws AuditTrailTrackingException
  {
    try
    {
      boolean isRequiredUnPack = false;
      if(businessDocs != null && businessDocs.length > 0)
      {
        byte[] doc = null;
        if(businessDocs.length == 1)
        {
          if(businessDocs[0] == null)
          {
            throw new NullPointerException("BizDoc[0] is null");
          }
          if(businessDocs[0].isRequiredPack())
          {
            doc = MimeConverter.getInstance().convertToBase64Mime(businessDocs);
            isRequiredUnPack = true;
          }
          else
          {
            doc = businessDocs[0].getDoc();
          }
        }
        else
        {
          doc = MimeConverter.getInstance().convertToBase64Mime(businessDocs);
          isRequiredUnPack = true; //doc more than one, imply require unpack
        }
        
        if(doc != null)
        {
          //Perform zip on the byte to save space
          doc = ByteArrayCompresser.zipFile(doc, null);
          return new BizDocument(groupName, doc, isRequiredUnPack, true);
        }
      }
      return null;
    }
    catch(Exception ex)
    {
      throw new AuditTrailTrackingException("Base64MIME Conversion Error in creating the BizDocument. Error: "+ex.getMessage(), ex);
    }
  }
  
  protected abstract AuditTrailEntityDAO getDAO();
}
