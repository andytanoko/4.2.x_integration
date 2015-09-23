/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDependencyChecker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.backend.helpers;

import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS Backend module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * Port - dependent on Rfc
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDependencyChecker
{

  /**
   * Constructor for EntityDependencyChecker.
   */
  public EntityDependencyChecker()
  {

  }


  /**
   * Checks whether there are dependent Ports on the specified Rfc.
   * 
   * @param rfcUid The UID of the Rfc.
   * @return A Set of Port entities that are dependent on the Rfc, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPortsForRfc(Long rfcUid)
  {
    Set dependents = null;
    try
    {
      dependents = getPortListByRfc(rfcUid);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentPortsForRfc] Error", t);
    }
    
    return dependents;
  }

  /**
   * Get the list of Ports that have the specified Rfc.
   * 
   * @param rfcUid The UID of the Rfc.
   * @return A Set of Port entities that are associated to the
   * specified Rfc.
   * @throws Throwable Error in retrieving the associations from BackendServiceManager.
   */  
  private Set getPortListByRfc(Long rfcUid) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Port.RFC,
      filter.getEqualOperator(), rfcUid, false);

    Set set = new HashSet();

    Collection portList = ActionHelper.getManager().findPorts(filter);

    if (portList != null)
    {
      set.addAll(portList);
    }

    return set;    
  }
      
}
