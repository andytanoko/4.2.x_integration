/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLPolicy.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002    Neo Sok Lay         Created
 * Oct 14 2005    Neo Sok Lay         Implement java.security.ACLPolicy
 * Nov 11 2005    Neo Sok Lay         Implement hierarchical policy delegation
 * Jun 11 2008    Tam Wei Xiang       #55: Stack overflow issue due to the unexpected 
 *                                    caching on the Root Policy, and the recursion call
 *                                    to get the "Permission" from the Root Policy.
 */
package com.gridnode.pdip.base.acl.auth;

import java.security.*;
import java.util.Collection;
import java.util.Enumeration;

import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerHome;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.helpers.ACLLogger;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This is a customized implementation of the javax.security.auth.Policy.
 * <P>
 * This policy currently is only interested in the RolePrincipals of the
 * Subject and would only return the AccessPermissions based on the access rights
 * given to each Role.
 * <P>
 * The CodeSource is ignored.
 * <P>
 * Applications that wish to implement access control based on Subject Roles
 * should install this policy by:
 * <PRE>
 *      <CODE>Policy.setPolicy(new ACLPolicy());</CODE>
 * </PRE>
 * <P>Checking of permission should be done subsequently by:
 * <PRE>
 *      <CODE>
 *      try
 *      {
 *        actionResult = Subject.doAsPrivileged(subject,
 *          new PrivilegedAction()
 *          {
 *            public Object run()
 *            {
 *              AccessController.checkPermission(perm);
 *
 *              ... perform the action authorized ...
 *
 *              return actionResult;
 *            }
 *          }, null);
 *     }
 *     catch (SecurityException ex)
 *     {
 *       //Permission is not granted
 *       :
 *     }
 *     </CODE>
 * </PRE>
 * <P>Or
 * <PRE>
 *      <CODE>
 *      try
 *      {
 *        actionResult = Subject.doAsPrivileged(subject,
 *          new PrivilegedExceptionAction()
 *          {
 *            public Object run() throws MyActionException
 *            {
 *              AccessController.checkPermission(perm);
 *
 *              ... perform the action authorized ...
 *
 *              //encounter error,
 *              if (error)
 *                throw new MyActionException("Problem performing the action");
 *              return actionResult;
 *            }
 *          }, null);
 *     }
 *     catch (PrivilegedActionException ex)
 *     {
 *       //error in performing action
 *       :
 *       throw (MyActionException)ex.getException();
 *     }
 *     catch (SecurityException ex)
 *     {
 *       //Permission is not granted
 *       :
 *     }
 *     </CODE>
 * </PRE>
 * <P>
 * IMPORTANT: A <B>null</B> must be passed For the AccessControlContext.
 * Passing null will indicate the caller context is not taken into consideration
 * for checking permission. Permissions should be solely based on the Roles
 * associated with the subject.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class ACLPolicy extends Policy
{
	private static Policy _rootPolicy = Policy.getPolicy();
	
  public ACLPolicy()
  {
	//#55 The caching should be done in the class variable which we can make use of the RootPolicy to perform permission assignment.
	//    See the GridTalkClientControllerBean.initSecurity() which will set the 
	//    custom policy into the Policy (for example Policy.setPolicy(new ACLPolicy());).
	//
	//    This will chain up the previous RootPolicy (DefaultRootPolicy <-- ACLPolicy 1 <-- ACLPolicy 2 ... <-- ACLPolicy n) 
	//    whenever we login into GT. This will cause the infinite recursion call (see getPermissions(ProtectionDomain domain))
	//    and caused the stack overflow exception.
  	//_rootPolicy = Policy.getPolicy();
  }

  /**
   * Refreshes/reloads the policy configuration. The behavior of this method
   * depends on the implementation. For example, calling <code>refresh</code>
   * on a file-based policy will cause the file to be re-read.
    *
   * @exception java.lang.SecurityException if the current thread does not
   *            have permission to refresh this Policy object.
   */
  public void refresh()
  {
  	_rootPolicy.refresh();
  }

  /**
   * Retrieve the Permissions granted to the Principals associated with
   * the specified Subject.
   *
   * @param subject the Subject whose associated Principals, in conjunction
   * with the provided CodeSource, determines the Permissions returned by
   * this method. This parameter may be null.
   * @param cs - the code specified by its CodeSource that determines,
   * in conjunction with the provided Subject, the Permissions returned by
   * this method. This parameter may be null.
   * @return the Collection of Permissions granted to all the Subject and
   * code specified in the provided subject and cs parameters
   */ /*051014NSL Replaced by getPermisssions(ProtectionDomain) and getPermissions(CodeSource)
  public PermissionCollection getPermissions(Subject subject, CodeSource cs)
  {
    ACLLogger.debugLog("ACLPolicy", "getPermissions", "subject: "+subject);
    //for now, ignores the codesource

    //retrieve feature access permissions based on roles.
    AccessPermissionCollection permColl = new AccessPermissionCollection();
    Collection rolePrincipals = subject.getPrincipals(RolePrincipal.class);

    ACLLogger.debugLog("ACLPolicy", "getPermissions", "#RolePrincipals: "+rolePrincipals.size());
    try
    {
      IACLManagerObj mgr = getACLManager();
      for (Iterator i=rolePrincipals.iterator(); i.hasNext(); )
      {
        RolePrincipal rp = (RolePrincipal)i.next();

        Collection acrs = mgr.getAccessRightsForRole(rp.getUID());
      ACLLogger.debugLog("ACLPolicy", "getPermissions", "#AccessRights: "+acrs.size());
        AuthorizationHelper.constructPermissions(acrs, permColl);
      }

      ACLLogger.debugLog("ACLPolicy", "getPermissions", "Permissions: "+permColl);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    //ACLLogger.debugLog("ACLPolicy", "getPermissions", "Exit ");
    return permColl;
  } */

  /**
   * Retrieve the Permissions granted to the Principals associated with
   * the specified ProtectionDomain.
   *
   * @param domain the ProtectionDomain whose associated Principals determines the Permissions returned by
   * this method. 
   * @return the Collection of Permissions granted to all the RolePrincipal(s) specified in the provided 
   * domain
   */
  public PermissionCollection getPermissions(ProtectionDomain domain)
	{
    ACLLogger.debugLog("ACLPolicy", "getPermissions", "by ProtectionDomain");
    Permissions allPerms = new Permissions();
    
    //retrieve feature access permissions based on roles.
    //AccessPermissionCollection permColl = new AccessPermissionCollection();
    Principal[] rolePrincipals = domain.getPrincipals();

    ACLLogger.debugLog("ACLPolicy", "getPermissions", "#Principals: "+rolePrincipals.length);
    try
    {
      IACLManagerObj mgr = null;
      for (int i=0; i<rolePrincipals.length; i++)
      {
      	if (rolePrincipals[i] instanceof RolePrincipal)
      	{
      		if (mgr == null)
      		{
      			mgr = getACLManager();
      		}
      		RolePrincipal rp = (RolePrincipal)rolePrincipals[i];

      		Collection acrs = mgr.getAccessRightsForRole(rp.getUID());
      		ACLLogger.debugLog("ACLPolicy", "getPermissions", "#AccessRights: "+acrs.size());
      		//AuthorizationHelper.constructPermissions(acrs, permColl);
      		AuthorizationHelper.constructPermissions(acrs, allPerms);
      	}
      }

      ACLLogger.debugLog("ACLPolicy", "getPermissions", "AccessPermissions: "+allPerms);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    
    PermissionCollection rootPerms = _rootPolicy.getPermissions(domain);
    if (rootPerms != null)
    {
    	for (Enumeration e = rootPerms.elements(); e.hasMoreElements(); )
    	{
    		allPerms.add((Permission)e.nextElement());
    	}
    }

    //ACLLogger.debugLog("ACLPolicy", "getPermissions", "Exit ");
    return allPerms;
	}

	public PermissionCollection getPermissions(CodeSource codesource)
	{
		ACLLogger.debugLog("ACLPolicy", "getPermissions", "by codesource: "+codesource);
    /*NSL20051111 Since we don't limit the codesource access, will just delegate to the root policy to 
     * handle the codesource access permissions
		//TODO temp implementation to allow all permissions
		AllPermission allperm = new AllPermission();
		PermissionCollection coll = allperm.newPermissionCollection();
		coll.add(allperm);

		return coll;
		*/
		return _rootPolicy.getPermissions(codesource);
	}

	private IACLManagerObj getACLManager()
    throws ServiceLookupException
  {
    return (IACLManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
             IACLManagerHome.class.getName(),
             IACLManagerHome.class,
             new Object[0]);
  }
}