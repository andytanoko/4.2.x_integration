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
package com.gridnode.gtas.server.acl.helpers;

import com.gridnode.pdip.app.user.model.UserAccount;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS Acl module. 
 * The following dependencies are currently checked:<p>
 * <PRE>
 * UserAccount - dependent on Role (managed in SubjectRole)
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
   * Checks whether there are dependent users on the specified role.
   * 
   * @param roleUid The UID of the Role to be deleted.
   * @return A Set of UserAccount entities that are dependent on the Role, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentUsersForRole(Long roleUid)
  {
    Set dependentUsers = null;
    try
    {
      dependentUsers = getUserList(roleUid);
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("RoleDependencyChecker", "checkDependentUsersForRole", "Error", t);
    }
    
    return dependentUsers;
  }

  /**
   * Get the list of Users that have the specified Role.
   * 
   * @param roleUId The UID of the Role.
   * @return A Set of UserAccount entities that are associated to the
   * specified Role.
   * @throws Throwable Error in retrieving the associations from ACLManager.
   */  
  private Set getUserList(Long roleUId) throws Throwable
  {
    Collection userUIdList = ActionHelper.getACLManager().getSubjectUIdsForRole(
                               roleUId,
                               UserAccount.ENTITY_NAME);

    Set set = new HashSet();
    if (userUIdList != null)
    {
      Long userUId = null;
      for (Iterator i=new HashSet(userUIdList).iterator(); i.hasNext(); )
      {
        try
        {
          userUId = (Long) i.next();
          set.add(ActionHelper.verifyUserAccount(userUId));
        }
        catch (Exception ex)
        {
          //skip if user account not found
        }
      }
    }

    return set;
  }
  
}
