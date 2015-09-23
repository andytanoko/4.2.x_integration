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
package com.gridnode.pdip.base.userprocedure.helpers;

import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.ReturnDef;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * Base UserProcedure module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * UserProcedure - dependent on Alert
 *               - dependent on Alert via ReturnDef
 * UserProcedure - dependent on ProcedureDefFile
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
   * Checks whether there are dependent UserProcedures on the specified Alert.
   * 
   * @param alertUid The UID of the Alert.
   * @return A Set of UserProcedure entities that are dependent on the Alert, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentUserProceduresForAlert(Long alertUid)
  {
    Set dependents = null;
    try
    {
      dependents = getUserProcedureListByAlert(alertUid);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentUserProceduresForAlert] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent UserProcedures on the specified ProcedureDefFile.
   * 
   * @param defFileUid The UID of the ProcedureDefFile.
   * @return A Set of UserProcedure entities that are dependent on the ProcedureDefFile, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentUserProceduresForProcedureDefFile(Long defFileUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, UserProcedure.PROC_DEF_FILE,
        filter.getEqualOperator(), defFileUid, false);
        
      dependents = getUserProcedureList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentUserProceduresForProcedureDefFile] Error", t);
    }
    
    return dependents;
  }

  /**
   * Get the list of UserProcedures that satisfy the specified
   * filter condition.
   * 
   * @param filter The filter condition.
   * @return A Set of UserProcedures entities that satisfied the filter
   * condition.
   * @throws Throwable Error in retrieving the UserProcedures from UserProcedureManager.
   */  
  private Set getUserProcedureList(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection procList = getUserProcManager().getUserProcedure(filter);

    if (procList != null)
    {
      set.addAll(procList); 
    }
    
    return set;
  }
  
  /**
   * Get the list of UserProcedures that have associations with the specified Alert.
   * 
   * @param alertUid The UID of the Alert.
   * @return A Set of UserProcedures entities that are associated to the
   * specified Alert.
   * @throws Throwable Error in retrieving the associations from UserProcedureManager.
   */  
  private Set getUserProcedureListByAlert(Long alertUid) throws Throwable
  {
    DataFilterImpl filter = null;

    Set set = new HashSet();

    Collection procList = getUserProcManager().getUserProcedure(filter);

    if (procList != null)
    {
      UserProcedure proc;
      ReturnDef returnDef;
      for (Iterator i=procList.iterator(); i.hasNext(); )
      {
        proc = (UserProcedure)i.next();
        if (alertUid.equals(proc.getProcedureDefAlert()))
        {
          set.add(proc);
        }
        else
        {
          Collection returnDefList = proc.getProcedureReturnList();
          for (Iterator j=returnDefList.iterator(); j.hasNext(); )
          {
            returnDef = (ReturnDef)j.next();
            if (alertUid.equals(returnDef.getAlert()))
            {
              set.add(proc);
              break;
            }
          }
        }        
      }
    }

    return set;    
  }

  private static IUserProcedureManagerObj getUserProcManager()
      throws Exception
  {
    return (IUserProcedureManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IUserProcedureManagerHome.class.getName(),
      IUserProcedureManagerHome.class,
      new Object[0]);
  }

}
