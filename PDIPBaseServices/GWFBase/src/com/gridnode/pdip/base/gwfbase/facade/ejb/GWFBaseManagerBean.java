/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GWFBaseManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 6, 2008   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.gwfbase.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification;
import com.gridnode.pdip.base.gwfbase.bpss.model.IBpssProcessSpecification;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.UtilEntity;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class GWFBaseManagerBean implements SessionBean
{
  /**
   * 
   */
  private static final long serialVersionUID = -1966511653812057028L;
  private SessionContext _ctxt;
  
  public void ejbCreate() throws CreateException, RemoteException
  {
  }
  
  public void ejbActivate() throws EJBException, RemoteException
  {
  }

  public void ejbPassivate() throws EJBException, RemoteException
  {
  }

  public void ejbRemove() throws EJBException, RemoteException
  {
  }

  public void setSessionContext(SessionContext ctxt) throws EJBException,
                                                    RemoteException
  {
    _ctxt = ctxt;
  }
  
  /**
   * Retrieve the BpssProcessSpecification given the uuid and spec version.
   * @param uuid BpssProcessSpec uuid
   * @param specVersion BpssProcessSpec version
   * @return BpssProcessSpecification or null if cannot be found.
   * @throws SystemException throw if we failed to retrieve the entity from the underlying datasource.
   */
  public BpssProcessSpecification getBpssProcessSpec(String uuid, String specVersion) throws SystemException
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, IBpssProcessSpecification.UUID,filter.getEqualOperator(), uuid, false);
    filter.addSingleFilter(filter.getAndConnector(),IBpssProcessSpecification.VERSION,filter.getEqualOperator(),specVersion,false);
    Collection processSpecificationColl= null;
    try  
    {
      processSpecificationColl = UtilEntity.getEntityByFilter(filter,BpssProcessSpecification.ENTITY_NAME,true);
      if(processSpecificationColl == null || processSpecificationColl.size() == 0)
      {
        return null;
      }
      else
      {
        return (BpssProcessSpecification)processSpecificationColl.iterator().next();
      }
    }
    catch(Throwable th)
    {
      throw new SystemException("Error in getting the BpssProcessSpec with uuid:"+uuid+" specVersion: "+specVersion, th);
    }
  }
}
