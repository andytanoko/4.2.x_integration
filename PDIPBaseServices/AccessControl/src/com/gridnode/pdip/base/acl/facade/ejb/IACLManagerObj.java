/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IACLManagerObj.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * May 21 2002    Goh Kan Mun             Added methods for the Feature.
 * May 22 2002    Neo Sok Lay             Add Service methods for AccessRight.
 * May 31 2002    Neo Sok Lay             Add method for retrieving RolePrincipals
 *                                        for a Subject.
 *                                        Change exception handling.
 * Jul 14 2003    Neo Sok Lay             Add method: getRoleKeysByFilter(IDataFilter)
 * Oct 20 2005    Neo Sok Lay             Business methods of the remote interface must throw java.rmi.RemoteException
 *                                        - The business method removeAccessRightsForFeature does not throw java.rmi.RemoteException
 *                                        - The business method getAccessRights does not throw java.rmi.RemoteException
 *                                        - The business method getAccessRightsKeys does not throw java.rmi.RemoteException
 *                                        - The business method getAccessRightsFor does not throw java.rmi.RemoteException
 *                                        - The business method getAccessRightsFor does not throw java.rmi.RemoteException
 *                                        - The business method getPrincipalsForSubject does not throw java.rmi.RemoteException
 */
package com.gridnode.pdip.base.acl.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.pdip.base.acl.model.AccessRight;
import com.gridnode.pdip.base.acl.model.Feature;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.model.SubjectRole;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * This interface defines the services defined in the ACLManagerNean
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IACLManagerObj extends EJBObject
{
 /**
   * To create a new <code>Role</code> entity.
   */
  public Long createRole(Role role)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * To update changes to an existing <code>Role</code> entity.
   */
  public void updateRole(Role role)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * To remove an existing <code>Role</code> entity.
   */
  public void deleteRole(Long roleUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * To retrieve an existing <code>Role</code> entity with the specified name.
   */
  public Role getRoleByRoleName(String roleName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve an existing <code>Role</code> entity based on uId.
   */
  public Role getRoleByUId(Long uId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a <code>Collection</code> of <code>Role</code> entities based on the
   * specified filter.
   */
  public Collection getRolesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a <code>Collection</code> of UIDs of <code>Role</code> entities based on the
   * specified filter.
   */
  public Collection getRoleKeysByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To create Subject-Role relationship.
   */
  public void assignRoleToSubject(Long roleUId, Long subjectUId, String subjectType)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * To remove Subject-Role relationship.
   */
  public void unassignRoleFromSubject(Long roleUId, Long subjectUId, String subjectType)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of <code>Subject</code> UIds that have relationship with the
   * specified subjectType and roleUId.
   */
  public Collection getSubjectUIdsForRole(Long roleUId, String subjectType)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of <code>Role</code> entities that have relationship with the
   * specified subjectType and subjectUId.
   */
  public Collection getRolesForSubject(Long subjectUId, String subjectType)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To remove all Subject-Role relationships base on the specified subjectType and
   * subjectUId.
   */
  public void removeSubjectRoleForSubject(Long subjectUId, String subjectType)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * To retrieve Subject-Role relationship.
   */
  public SubjectRole getSubjectRole(Long subjectUId, String subjectType, Long roleUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a <code>Collection</code> of Subject-Role relationship with the
   * specified subjectType and subjectUId.
   */
  public Collection getSubjectRolesForSubject(Long subjectUId, String subjectType)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To create a <code>Feature</code> entity.
   */
  public void createFeature(Feature feature)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * To update a <code>Feature</code> entity.
   */
  public void updateFeature(Feature feature)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * To remove a <code>Feature</code> entity with the specified uId.
   */
  public void deleteFeature(Long featureUId)
    throws DeleteEntityException, SystemException, RemoteException;

 /**
   * To retrieve a <code>Collection</code> of all <code>Feature</code> entities.
   */
  public Collection getAllFeatures()
    throws FindEntityException, SystemException, RemoteException;

 /**
   * To retrieve a <code>Feature</code> entity by specified name.
   */
  public Feature getFeatureByFeatureName(String featureName)
    throws FindEntityException, SystemException, RemoteException;

 /**
   * To retrieve a <code>Feature</code> entity by specified uId.
   */
  public Feature getFeatureByFeatureUId(Long featureUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Create a new AccessRight record.
   *
   * @param accessRight The AccessRight entity.
   * @return The UID of the created AccessRight record.
   *
   * @exception CreateEntityException, SystemException
   */
  public Long createAccessRight(AccessRight accessRight)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * To remove an existing AccessRight definition from the storage.
   *
   * @param uId the UId of the AccessRight to be deleted.
   *
   * @exception DeleteEntityException
   * @exception SystemException
   *
   * @since 2.0
   * @version 2.0
   */
  public void removeAccessRight(Long accessRightUID)
    throws    DeleteEntityException, SystemException, RemoteException;

  /**
   * To remove an all AccessRight definitions from the storage for a particular
   * Role.
   *
   * @param roleUID the UID of the Role whose access rights are to be deleted.
   *
   * @exception DeleteEntityException
   * @exception SystemException
   *
   * @since 2.0
   * @version 2.0
   */
  public void removeAccessRightsFromRole(Long roleUID)
    throws    DeleteEntityException, SystemException, RemoteException;

  /**
   * To remove an all AccessRight definitions from the storage for a particular
   * Role.
   *
   * @param roleUID the UID of the Role whose access rights are to be deleted.
   *
   * @exception DeleteEntityException
   * @exception SystemException
   *
   * @since 2.0
   * @version 2.0
   */
  public void removeAccessRightsForFeature(String feature)
    throws    DeleteEntityException, SystemException, RemoteException;

  /**
   * Retrieve an AccessRight record with the specified UID.
   *
   * @param accessRightUID The UID of the AccessRight record.
   * @return The retrieve AccessRight record, or <B>null</B> if none found.
   *
   * @exception FindEntityException
   * @exception SystemException
   */
  public AccessRight getAccessRight(Long accessRightUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Retrieve the AccessRights for a Role.
   *
   * @param roleUID The UID of the Role.
   * @return A Collection of AccessRights retrieved.
   *
   * @exception FindEntityException
   * @exception SystemException
   */
  public Collection getAccessRightsForRole(Long roleUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Retrieve the AccessRights that satisfy a condition.
   *
   * @param filter The retrieval condition.
   * @return A Collection of AccessRights retrieved.
   *
   * @exception FindEntityException
   * @exception SystemException
   */
  public Collection getAccessRights(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Retrieve the UIDs of the AccessRights that satisfy a condition.
   *
   * @param filter The retrieval condition.
   * @return A Collection of UIDs (Long) of the AccessRights retrieved.
   *
   * @exception FindEntityException
   * @exception SystemException
   */
  public Collection getAccessRightsKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To update changes to an existing AccessRight in the storage.
   *
   * @param  accessRight The modified AccessRight to be save into the storage.
   *
   * @exception UpdateEntityException
   * @exception SystemException
   *
   * @since 2.0
   * @version 2.0
   */
  public void updateAccessRight(AccessRight accessRight)
    throws    UpdateEntityException, SystemException, RemoteException;

  /**
   * Retrieve the AccessRights for a Role and a particular feature.
   *
   * @param roleUID The UID of the Role.
   * @param feature The feature name.
   * @return A Collection of AccessRights retrieved.
   *
   * @exception FindEntityException
   * @exception SystemException
   */
  public Collection getAccessRightsFor(Long roleUID, String feature)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Retrieve the AccessRights for a Role, feature and action.
   *
   * @param roleUID The UID of the Role.
   * @param feature The feature name.
   * @oaram action The action.
   * @return A Collection of AccessRights retrieved.
   *
   * @exception FindEntityException
   * @exception SystemException
   */
  public Collection getAccessRightsFor(Long roleUID, String feature, String action)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the RolePrincipals for a subject.
   *
   * @param subjectUId UID of the subject.
   * @param subjectType Type of the subject.
   * @return A collection of RolePrincipal objects representing the authorized
   * roles of the subject.
   *
   * @exception FindEntityException Subject not found
   * @exception SystemException Unexpected error while retrieving the roles
   * or constructing the principals.
   */
  public Collection getPrincipalsForSubject(Long subjectUId, String subjectType)
    throws TypedException, RemoteException;

}