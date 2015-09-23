/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-11-05    Wong Yee Wah         Created
 */

package com.gridnode.gtas.server.document.helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;
import com.gridnode.pdip.app.partner.helpers.Logger;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class EntityDependencyChecker
{

  public Set checkDependentAS2DocTypeMappingForPartner(String partnerId)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, AS2DocTypeMapping.PARTNER_ID, filter.getEqualOperator(),
      partnerId, false);
        
      dependents = getAS2DocTypeMappingList(filter);
    }
    catch (Throwable t)
    {
      Logger.err("[EntityDependencyChecker.checkDependentAS2DocTypeMappingForPartner] Error", t);
    }
    
    return dependents;   
  }
  
  public Set checkDependentAS2DocTypeMappingForDocumentType(String docName)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, AS2DocTypeMapping.AS2_DOC_TYPE, filter.getEqualOperator(),
      docName, false);
      filter.addSingleFilter(filter.getOrConnector(), AS2DocTypeMapping.DOC_TYPE, filter.getEqualOperator(),
      docName, false);
        
      dependents = getAS2DocTypeMappingList(filter);
    }
    catch (Throwable t)
    {
      Logger.err("[EntityDependencyChecker.checkDependentAS2DocTypeMappingForDocumentType] Error", t);
    }
    
    return dependents;   
  }
  

  private Set getAS2DocTypeMappingList(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection mappingList = getDocumentManager().findAS2DocTypeMappingByFilter(filter);

    if (mappingList != null)
    {
      set.addAll(mappingList);
    }

    return set;    
  }
  
  
  
  private IDocumentManagerObj getDocumentManager() throws Exception
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
        IDocumentManagerHome.class.getName(),
        IDocumentManagerHome.class,
        new Object[0]);
  }
  
  
 
  
}
