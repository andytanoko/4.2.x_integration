/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CleanXpdlDefinition.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * May 11, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.deploy.manager.ejb;

import java.util.Collection;
import java.util.Iterator;

import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.UtilEntity;

public class CleanXpdlDefinition
{
  public static void undeployMarkDeleted() throws Throwable
  {
    IGWFDeployMgrObj deploymanager =(IGWFDeployMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IGWFDeployMgrHome.class.getName(),IGWFDeployMgrHome.class,new Object[]{});
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null,XpdlPackage.STATE,filter.getEqualOperator(),new Integer(XpdlPackage.STATE_DELETED),false);
    Collection xpdlPackageColl = UtilEntity.getEntityByFilter(filter,XpdlPackage.ENTITY_NAME,false);
    for(Iterator i = xpdlPackageColl.iterator();i.hasNext();)
    {
      XpdlPackage xpdlPackage =(XpdlPackage)i.next();
      System.out.println("Undeploying PackageId="+xpdlPackage.getPackageId()+", VersionId="+xpdlPackage.getVersionId());
      deploymanager.undeployXpdl(xpdlPackage.getPackageId(),xpdlPackage.getVersionId());
    }
  }
  
  public static void main(String args[])
  {
    try
    {
      undeployMarkDeleted();
    }
    catch(Throwable th)
    {
      th.printStackTrace();
    }
    
  }
}
