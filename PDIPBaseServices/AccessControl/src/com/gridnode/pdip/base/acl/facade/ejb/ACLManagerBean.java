/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLManagerBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * May 21 2002    Goh Kan Mun             Added methods for the Feature.
 * May 21 2002    Neo Sok Lay             Add Service methods for AccessRight.
 * May 31 2002    Neo Sok Lay             Add method for retrieving RolePrincipals
 *                                        for a Subject.
 *                                        Change exception handling.
 * Jun 05 2002    Goh Kan Mun             Added methods for retrieving Subject uids based on
 *                                        Role and SubjectType.
 * Jun 19 2002    Neo Sok Lay             Return UID for createRole().
 * Jul 14 2003    Neo Sok Lay             Add method: getRoleKeysByFilter(IDataFilter)
 * Feb 08 2007		Alain Ah Ming						Changed to warning log
 */
package com.gridnode.pdip.base.acl.facade.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.acl.auth.AuthorizationHelper;
import com.gridnode.pdip.base.acl.entities.ejb.IAccessRightLocalObj;
import com.gridnode.pdip.base.acl.entities.ejb.IRoleLocalObj;
import com.gridnode.pdip.base.acl.helpers.*;
import com.gridnode.pdip.base.acl.model.AccessRight;
import com.gridnode.pdip.base.acl.model.Feature;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.model.SubjectRole;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * This beans manages the Role.
 *
 * @author Goh Kan Mun
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */

public class ACLManagerBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1716250377733537182L;
	transient private SessionContext _sessionContext = null;

  public ACLManagerBean()
  {
  }

  /* *****************************SessionBean Interfaces ***************************** */

  public void setSessionContext(SessionContext sessionContext) //throws EJBException, RemoteException
  {
    _sessionContext = sessionContext;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()// throws EJBException, RemoteException
  {
  }

  public void ejbActivate() //throws EJBException, RemoteException
  {
  }

  public void ejbPassivate() //throws EJBException, RemoteException
  {
  }

  /* *****************************Service provide by this class ***************************** */

  // *************************** Role ****************************************

  /**
   * To create a new <code>Role</code> entity.
   *
   * @param           role      The new <code>Role</code> entity.
   * @return The UID of the created Role.
   * @exception       Thrown when the create operation fails.
   *
   * @since 2.0
   * @version GT 4.0 VAN
   */
  public Long createRole(Role role)
    throws    CreateEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "createRole", "Enter");

      IRoleLocalObj obj = (IRoleLocalObj)RoleEntityHandler.getInstance().create(role);
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "createRole",
                         "BL Exception ", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "createRole", "Error ", t);
      throw new SystemException(
        "ACLManagerBean.createRole(Role) Error ", t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "createRole",
        "Exit ");
    }
  }

  /**
   * To update changes to an existing <code>Role</code> entity.
   *
   * @param           role    The modified <code>Role</code> entity.
   *
   * @exception       Thrown when the update operation fails.
   *
   * @since 2.0
   * @version GT 4.0 VAN
   */
  public void updateRole(Role role)
    throws    UpdateEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "updateRole", "Enter");

      RoleEntityHandler.getInstance().update(role);
    }
    catch (EntityModifiedException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "updateRole", "BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "updateRole", "Error ", t);
      throw new SystemException(
        "ACLManagerBean.updateRole(Role) Error ", t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "updateRole", "Exit ");
    }
  }

  /**
   * To remove an existing <code>Role</code> entity.
   *
   * @param           roleUId   the uId of the <code>Role</code> entity to be deleted.
   *
   * @exception       Thrown when the delete operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void deleteRole(Long roleUId)
    throws    DeleteEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "deleteRole", "roleUId: "+roleUId);

      RoleEntityHandler.getInstance().remove(roleUId);
      SubjectRoleEntityHandler.getInstance().removeSubjectRoleForRole(roleUId);
      getAccessRightHandler().removeByRoleUID(roleUId);
    }
    catch (RemoveException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "deleteRole", "BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "deleteRole", "Error ", t);
      throw new SystemException(
        "ACLManagerBean.deleteRole(roleUId) Error ", t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "deleteRole", "Exit ");
    }
  }

  /**
   * To retrieve an existing <code>Role</code> entity with the specified name.
   *
   * @param           roleName   the name of the <code>Role</code> entity.
   *
   * @return          The <code>Role<code> entity.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Role getRoleByRoleName(String roleName)
    throws    FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getRoleByRoleName",
                         "roleName: " + roleName);

      return RoleEntityHandler.getInstance().getRoleByRoleName(roleName);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRoleByRoleName",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRoleByRoleName",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRoleByRoleName",
        "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getRoleByRoleName(roleName) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getRoleByRoleName", "Exit ");
    }
  }

  /**
   * To retrieve an existing <code>Role</code> entity based on uId.
   *
   * @param           uId   the uId of the <code>Role</code> entity.
   *
   * @return          The <code>Role</code> entity.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Role getRoleByUId(Long uId)
    throws    FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getRoleByUId", "uId: " + uId);

      return (Role) RoleEntityHandler.getInstance().getEntityByKeyForReadOnly(uId);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRoleByUId", "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRoleByUId",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRoleByUId", "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getRoleByUId(uId) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getRoleByUId", "Exit ");
    }
  }

  /**
   * To retrieve a <code>Collection</code> of <code>Role</code> entities based on the
   * specified filter.
   *
   * @param           filter  the filter for  of the role.
   *
   * @return          a <code>Collection</code> of  <code>Role</code> entities.
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getRolesByFilter(IDataFilter filter)
    throws          FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getRolesByFilter", "filter: " +
        (filter==null?"null" : filter.getFilterExpr()));

      return RoleEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRolesByFilter", "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRolesByFilter",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRolesByFilter", "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getRolesByFilter(filter) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getRolesByFilter", "Exit ");
    }
  }

  /**
   * To retrieve a <code>Collection</code> of UIDs of <code>Role</code> entities based on the
   * specified filter.
   * 
   * @param filter The filter condition.
   * @return Collection of UIDs of the Role entities that satisfy the
   * specified filter condition.
   * 
   * @since GT 2.2 I1
   */
  public Collection getRoleKeysByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getRoleKeysByFilter", "filter: " +
        (filter==null?"null" : filter.getFilterExpr()));

      return RoleEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRoleKeysByFilter", "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRoleKeysByFilter",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRoleKeysByFilter", "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getRoleKeysByFilter(filter) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getRoleKeysByFilter", "Exit ");
    }
  }

  // *************************** Subject Role *********************************

  /**
   * To create Subject-Role relationship.
   *
   * @param           roleUId       the uId of a <code>Role</code> entity.
   * @param           subjectUId    the uId of a subject.
   * @param           subjectType   the subject type.
   *
   * @exception       thrown when an error occurs creating the new relatonship.
   *
   * @since 2.0
   * @version 2.0
   */
  public void assignRoleToSubject(Long roleUId, Long subjectUId, String subjectType)
         throws CreateEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "assignRoleToSubject",
                         "roleUId: " + roleUId +
                         ", subjectUId: " + subjectUId +
                         ", subjectType: " + subjectType);
      SubjectRoleEntityHandler.getInstance().assignRoleToSubject(
        subjectUId, subjectType, roleUId);
    }
    catch (CreateException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "assignRoleToSubject",
                         "BL Exception ", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "assignRoleToSubject", "Error ", t);
      throw new SystemException(
        "ACLManagerBean.assignRoleToSubject(roleUId,subjectUId,subjectType) Error ",
        t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "assignRoleToSubject", "Exit ");
    }
  }

  /**
   * To remove Subject-Role relationship.
   *
   * @param           roleUId       the uId of the <code>Role</code> entity.
   * @param           subjectUId    the uId of a subject.
   * @param           subjectType   the subject type.
   *
   * @exception       thrown when an error occurs removing the relatonship.
   *
   * @since 2.0
   * @version 2.0
   */
  public void unassignRoleFromSubject(Long roleUId, Long subjectUId, String subjectType)
    throws    DeleteEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "unassignRoleFromSubject",
                         "roleUId: " + roleUId +
                         ", subjectUId: " + subjectUId +
                         ", subjectType: " + subjectType);
      SubjectRoleEntityHandler.getInstance().unassignRoleToSubject(
        subjectType, roleUId, subjectUId);
    }
    catch (RemoveException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "unassignRoleFromSubject",
        "BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "unassignRoleFromSubject",
                         "Error ", t);
      throw new SystemException(
        "ACLManagerBean.unassignRoleFromSubject(roleUId, subjectUid,subjectType) Error ",
        t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "unassignRoleFromSubject",
        "Exit ");
    }
  }

  /**
   * To retrieve a collection of <code>Subject</code> UIds that have relationship with the
   * specified subjectType and roleUId.
   *
   * @param           roleUId          the uId of the role.
   * @param           subjectType      the subject type.
   *
   * @return          the <code>Collection</code> containing UId of <code>Subject</code> entities
   *                  that has relationship with the specified subjectType and roleUId.
   *
   * @exception       thrown when an error occurs retrieving the relatonships.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getSubjectUIdsForRole(Long roleUId, String subjectType)
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getSubjectUIdsForRole",
                         "roleUId: " + roleUId +
                         ", subjectType: " + subjectType);

      return SubjectRoleEntityHandler.getInstance().getSubjectUIdsForRole(
               subjectType, roleUId);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getSubjectUIdsForRole",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getSubjectUIdsForRole",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getSubjectUIdsForRole",
        "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getSubjectUIdsForRole(subjectUId,subjectType) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getSubjectUIdsForRole", "Exit ");
    }
  }

  /**
   * To retrieve a collection of <code>Role</code> entities that have relationship with the
   * specified subjectType and subjectUId.
   *
   * @param           subjectUId       the uId of the subject.
   * @param           subjectType      the subject type.
   *
   * @return          the <code>Collection</code> containing <code>Roles</code> entities
   *                  that has relationship with the specified subjectType and subjectUId.
   *
   * @exception       thrown when an error occurs retrieving the relatonships.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getRolesForSubject(Long subjectUId, String subjectType)
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getRolesForSubject",
                         "subjectUId: " + subjectUId +
                         ", subjectType: " + subjectType);

      return SubjectRoleEntityHandler.getInstance().getRolesForSubject(
               subjectUId, subjectType);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRolesForSubject",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRolesForSubject",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getRolesForSubject",
        "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getRolesForSubject(subjectUId,subjectType) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getRolesForSubject", "Exit ");
    }
  }

  /**
   * To remove all Subject-Role relationships base on the specified subjectType and
   * subjectUId.
   *
   * @param           subjectUId    the uId of the subject.
   * @param           subjectType   the type of subject.
   *
   * @exception       thrown when an error occurs removing the relatonships.
   *
   * @since 2.0
   * @version 2.0
   */
  public void removeSubjectRoleForSubject(Long subjectUId, String subjectType)
    throws    DeleteEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "removeSubjectRoleForSubject",
                         "subjectUId: " + subjectUId +
                         ", subjectType: " + subjectType);
      SubjectRoleEntityHandler.getInstance().removeSubjectRoleForSubject(
        subjectUId, subjectType);
    }
    catch (RemoveException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "removeSubjectRoleForSubject",
        "BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "removeSubjectRoleForSubject",
                         "Error ", t);
      throw new SystemException(
        "ACLManagerBean.removeSubjectRoleForSubject(subjectUId,subjectType) Error ",
        t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "removeSubjectRoleForSubject",
        "Exit ");
    }
  }

  /**
   * To retrieve Subject-Role relationship.
   *
   * @param           roleUId       the uId of the <code>Role</code> entity.
   * @param           subjectUId    the uId of the subject.
   * @param           subjectType   the type of subject.
   *
   * @return          the required <code>SubjectRole</code> entity.
   *
   * @exception       thrown when an error occurs retrieving the relatonship.
   *
   * @since 2.0
   * @version 2.0
   */
  public SubjectRole getSubjectRole(Long subjectUId, String subjectType, Long roleUId)
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getSubjectRole",
                         "roleUId: " +roleUId +
                         ", subjectUId: " + subjectUId +
                         ", subjectType: " + subjectType);

      return SubjectRoleEntityHandler.getInstance().getSubjectRole(
               subjectUId, subjectType, roleUId);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getSubjectRole",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getSubjectRole",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getSubjectRole", "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getSubjectRole(subjectUId,subjectType,roleUId) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getSubjectRole", "Exit ");
    }
  }

  /**
   * To retrieve a <code>Collection</code> of Subject-Role relationship with the
   * specified subjectType and subjectUId.
   *
   * @param           subjectUId    the uId of the subject.
   * @param           subjectType   the type of subject.
   *
   * @return          a <code>Collection</code> of <code>SubjectRole</code> entities.
   *
   * @exception       thrown when an error occurs retrieving the relatonships.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getSubjectRolesForSubject(Long subjectUId, String subjectType)
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getSubjectRolesForSubject",
                         "subjectUId: " + subjectUId +
                         ", subjectType: " + subjectType);

      return SubjectRoleEntityHandler.getInstance().getSubjectRoleForSubject(
               subjectUId, subjectType);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getSubjectRolesForSubject",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getSubjectRolesForSubject",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getSubjectRolesForSubject",
        "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getSubjectRolesForSubject(subjectUId,subjectType Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getSubjectRolesForSubject", "Exit ");
    }
  }

  // ****************************** Feature *********************************

  /**
   * To create a <code>Feature</code> entity.
   *
   * @param           feature    the <code>Feature</code> entity to be added.
   *
   * @exception       thrown when an error occurs adding the <code>Feature</code> entity.
   *
   * @since 2.0
   * @version 2.0
   */
  public void createFeature(Feature feature)
    throws    CreateEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "createFeature", "Enter");

      FeatureEntityHandler.getInstance().create(feature);
    }
    catch (CreateException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "createFeature",
                         "BL Exception ", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "createFeature", "Error ", t);
      throw new SystemException(
        "ACLManagerBean.createFeature(Feature) Error ", t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "createFeature", "Exit ");
    }
  }

  /**
   * To update a <code>Feature</code> entity.
   *
   * @param           feature    the modified <code>Feature</code> entity.
   *
   * @exception       thrown when an error occurs updating the <code>Feature</code> entity.
   *
   * @since 2.0
   * @version 2.0
   */
  public void updateFeature(Feature feature)
    throws    UpdateEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "updateFeature", "Enter");

      FeatureEntityHandler.getInstance().update(feature);
    }
    catch (EntityModifiedException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "updateFeature", "BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "updateFeature", "Error ", t);
      throw new SystemException(
        "ACLManagerBean.updateFeature(Feature) Error ", t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "updateFeature", "Exit ");
    }
 }

  /**
   * To remove a <code>Feature</code> entity with the specified uId.
   *
   * @param           featureUId    the uId of the <code>Feature</code> entity.
   *
   * @exception       thrown when an error occurs removing the <code>Feature</code> entity.
   *
   * @since 2.0
   * @version 2.0
   */
  public void deleteFeature(Long featureUId)
    throws    DeleteEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "deleteFeature",
        "featureUId: "+featureUId);

      FeatureEntityHandler.getInstance().remove(featureUId);
    }
    catch (RemoveException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "deleteFeature", "BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "deleteFeature", "Error ", t);
      throw new SystemException(
        "ACLManagerBean.deleteFeature(featureUId) Error ", t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "deleteFeature", "Exit ");
    }
  }

 /**
   * To retrieve a <code>Collection</code> of all <code>Feature</code> entities.
   *
   * @return          a <code>Collection</code> of all <code>Feature</code> entities.
   *
   * @exception       thrown when an error occurs retrieving the <code>Feature</code> entities.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getAllFeatures()
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getAllFeatures", "Enter");

      return FeatureEntityHandler.getInstance().getAllFeatures();
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAllFeatures", "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAllFeatures",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAllFeatures", "Error ", ex);
      throw new SystemException("ACLManagerBean.getAllFeatures() Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getAllFeatures", "Exit ");
    }
  }

 /**
   * To retrieve a <code>Feature</code> entity by specified name.
   *
   * @param           featureName     the name of the <code>Feature</code> entity.
   *
   * @return          the <code>Feature</code> entity with the specidied name.
   *
   * @exception       thrown when an error occurs retrieving the <code>Feature</code> entity.
   *
   * @since 2.0
   * @version 2.0
   */
  public Feature getFeatureByFeatureName(String featureName)
    throws       FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getFeatureByFeatureName",
        "featureName: "+featureName);

      return FeatureEntityHandler.getInstance().getFeatureByFeatureName(featureName);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getFeatureByFeatureName",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getFeatureByFeatureName",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getFeatureByFeatureName",
        "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getFeatureByFeatureName(featureName) Error ",
        ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getFeatureByFeatureName", "Exit ");
    }
  }

 /**
   * To retrieve a <code>Feature</code> entity by specified uId.
   *
   * @param           featureUId      the uId of the <code>Feature</code> entity.
   *
   * @return          the <code>Feature</code> entity with the specified uId.
   *
   * @exception       thrown when an error occurs retrieving the <code>Feature</code> entity.
   *
   * @since 2.0
   * @version 2.0
   */
  public Feature getFeatureByFeatureUId(Long featureUId)
    throws       FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getFeatureByFeatureUId",
                         "featureUId: " + featureUId);

      return (Feature)FeatureEntityHandler.getInstance().getEntityByKeyForReadOnly(featureUId);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getFeatureByFeatureUId",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getFeatureByFeatureUId",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getFeatureByFeatureUId",
        "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getFeatureByFeatureUId(featureUId) Error ",
        ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getFeatureByFeatureUId", "Exit ");
    }
  }

  // ********************************* AccessRight ****************************

  /**
   * Create a new AccessRight record.
   *
   * @param accessRight The AccessRight entity.
   * @return The UID of the created AccessRight record.
   *
   * @exception CreateEntityException, SystemException
   */
  public Long createAccessRight(AccessRight accessRight)
    throws CreateEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "createAccessRight", "Enter ");
      //RoleEntityHandler.getInstance().getRoleByUId()
      getAccessRightHandler().checkDuplicate(accessRight, false);
      IAccessRightLocalObj obj = (IAccessRightLocalObj)getAccessRightHandler().create(
                              accessRight);
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "createAccessRight",
                         "BL Exception ", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "createAccessRight",
                         "BL Exception ", ex);
      throw ex;
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "createAccessRight", "Error ", t);
      throw new SystemException(
        "ACLManagerBean.createAccessRight(AccessRight) Error ", t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "createAccessRight", "Exit ");
    }
  }

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
    throws    UpdateEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "updateAccessRight", "Enter ");
      getAccessRightHandler().checkDuplicate(accessRight, true);
      getAccessRightHandler().update(accessRight);
    }
    catch (EntityModifiedException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "updateAccessRight",
                         "BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "updateAccessRight",
                         "BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "updateAccessRight",
                         "Error ", t);
      throw new SystemException(
        "ACLManagerBean.updateAccessRight(AccessRight) Error ", t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "updateAccessRight", "Exit ");
    }
  }

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
    throws    DeleteEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "removeAccessRight",
        "accessRightUID: "+accessRightUID);

      getAccessRightHandler().remove(accessRightUID);
    }
    catch (RemoveException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "removeAccessRight",
        "BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "removeAccessRight",
                         "Error ", t);
      throw new SystemException(
        "ACLManagerBean.removeAccessRight(accessRightUID) Error ", t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "removeAccessRight", "Exit ");
    }
  }

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
    throws    DeleteEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "removeAccessRightsFromRole",
        "roleUID: "+roleUID);

      getAccessRightHandler().removeByRoleUID(roleUID);
    }
    catch (RemoveException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "removeAccessRightsFromRole",
        "BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "removeAccessRightsFromRole",
                         "Error ", t);
      throw new SystemException(
        "ACLManagerBean.removeAccessRightsFromRole(roleUID) Error ",
        t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "removeAccessRightsFromRole",
        "Exit ");
    }
  }

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
    throws    DeleteEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "removeAccessRightsForFeature",
        "feature: "+feature);

      getAccessRightHandler().removeByFeature(feature);
    }
    catch (RemoveException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "removeAccessRightsForFeature",
        "BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "removeAccessRightsForFeature",
                         "Error ", t);
      throw new SystemException(
        "ACLManagerBean.removeAccessRightsForFeature(roleUID) Error ",
        t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "removeAccessRightsForFeature",
        "Exit ");
    }
  }

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
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRightsForRole",
        "roleUID: "+roleUID);

      return getAccessRightHandler().getAccessRightsByRoleUId(roleUID);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsForRole",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsForRole",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsForRole",
        "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getAccessRightsForRole(roleUID) Error ",
        ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRightsForRole",
        "Exit ");
    }
  }

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
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRights",
        "filter: "+(filter==null?"null":filter.getFilterExpr()));

      return getAccessRightHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRights",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRights",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRights",
        "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getAccessRights(filter) Error ",
        ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRights",
        "Exit ");
    }
  }

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
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRightsKeys",
        "filter: " + (filter==null?"null":filter.getFilterExpr()));

      return getAccessRightHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsKeys",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsKeys",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsKeys", "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getAccessRightsKeys(filter) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRightsKeys", "Exit ");
    }
  }

  /**
   * Retrieve an AccessRight record with the specified UID.
   *
   * @param accessRightUID The UID of the AccessRight record.
   * @return The retrieve AccessRight record
   *
   * @exception FindEntityException If record not found.
   * @exception SystemException
   */
  public AccessRight getAccessRight(Long accessRightUID)
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRight",
        "accessRightUID: "+accessRightUID);

      return (AccessRight)getAccessRightHandler().getEntityByKeyForReadOnly(
               accessRightUID);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRight",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRight",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRight", "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getAccessRight(accessRightUID) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRight", "Exit ");
    }
  }

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
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRightsFor",
        "roleUID: "+roleUID + ", feature: "+feature);

      return getAccessRightHandler().getAccessRightsBy(roleUID, feature);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsFor",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsFor",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsFor", "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getAccessRightsFor(roleUID,feature) Error ", ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRightsFor", "Exit ");
    }
  }

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
    throws FindEntityException, SystemException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRightsFor",
        "roleUID: "+roleUID + ", feature: "+feature + ", action: "+action);

      return getAccessRightHandler().getAccessRightsBy(roleUID, feature, action);
    }
    catch (ApplicationException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsFor",
        "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsFor",
        "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getAccessRightsFor", "Error ", ex);
      throw new SystemException(
        "ACLManagerBean.getAccessRightsFor(roleUID,feature,action) Error ",
        ex);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getAccessRightsFor", "Exit ");
    }
  }

  // ************************** Authorization *********************************

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
    throws TypedException
  {
    try
    {
      ACLLogger.debugLog("ACLManagerBean", "getPrincipalsForSubject",
        "subjectUId: "+subjectUId + ", subjectType: "+subjectType);

      Collection roles = getRolesForSubject(subjectUId, subjectType);
      return AuthorizationHelper.constructPrincipals(roles);
    }
    catch (TypedException ex)
    {
      ACLLogger.warnLog("ACLManagerBean", "getPrincipalsForSubject",
        "Unable to get Roles for subject ");
      throw ex;
    }
    catch (Throwable t)
    {
      ACLLogger.warnLog("ACLManagerBean", "getPrincipalsForSubject",
        "Error ", t);
      throw new SystemException(
        "ACLManagerBean.getPrincipalsForSubject(subjectUId,subjectType) Error ",
        t);
    }
    finally
    {
      ACLLogger.debugLog("ACLManagerBean", "getPrincipalsForSubject", "Exit ");
    }
  }

  // ************* Helper methods *************************
  private AccessRightEntityHandler getAccessRightHandler()
  {
    return AccessRightEntityHandler.getInstance();
  }
}