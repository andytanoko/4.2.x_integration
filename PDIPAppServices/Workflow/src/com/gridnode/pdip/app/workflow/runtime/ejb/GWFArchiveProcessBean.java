/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GWFArchiveProcessBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.workflow.runtime.ejb;

import java.rmi.RemoteException;
import javax.ejb.EntityContext;


import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.RemoveException;

import com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class GWFArchiveProcessBean extends AbstractEntityBean
{

  @Override
  public String getEntityName()
  {
    // TODO Auto-generated method stub
    return GWFArchiveProcess.ENTITY_NAME;
  }


}
