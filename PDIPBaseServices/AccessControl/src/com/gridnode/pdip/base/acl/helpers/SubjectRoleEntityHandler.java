/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubjectRoleEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 16 2002    Goh Kan Mun             Created
 * Jun 05 2002    Goh Kan Mun             Added in method to retrieve subjectUIds based on
 *                                        roleUId and subjectType.
 * Jul 09 2002    Neo Sok Lay             Return empty collection instead of null
 *                                        for no results.
 * Jul 25 2002    Neo Sok Lay             Remove dependency on ejbEntityMap.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */

package com.gridnode.pdip.base.acl.helpers;

import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.model.SubjectRole;
import com.gridnode.pdip.base.acl.entities.ejb.ISubjectRoleLocalHome;
import com.gridnode.pdip.base.acl.entities.ejb.ISubjectRoleLocalObj;

import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the SubjectRoleBean.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class SubjectRoleEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = SubjectRole.ENTITY_NAME;
  private static final Object lock = new Object();

  public SubjectRoleEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of SubjectRoleEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  public static SubjectRoleEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new SubjectRoleEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of RoleEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  private static SubjectRoleEntityHandler getFromEntityHandlerFactory()
  {
    return (SubjectRoleEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      ISubjectRoleLocalHome.class.getName(),
      ISubjectRoleLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return ISubjectRoleLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return ISubjectRoleLocalObj.class;
  }

  // ************************** Own Methods ********************************
  /**
   * Create a new Subject-Role relationship.
   *
   * @param roleUId       The uId of the Role entity.
   * @param subjectUId    The uId of the Subject entity.
   * @param subjectType   The type of Subject entity.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public void assignRoleToSubject(Long subjectUId, String subjectType, Long roleUId) throws Throwable
  {
    SubjectRole subjectRole = new SubjectRole();
    subjectRole.setRole(roleUId.longValue());
    subjectRole.setSubject(subjectUId.longValue());
    subjectRole.setSubjectType(subjectType);
    createEntity(subjectRole);
  }

  /**
   * Remove a Subject-Role relationship.
   *
   * @param roleUId       The uId of the Role entity.
   * @param subjectUId    The uId of the Subject entity.
   * @param subjectType   The type of Subject entity.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public void unassignRoleToSubject(String subjectType, Long roleUId, Long subjectUId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SubjectRole.SUBJECT_TYPE, filter.getEqualOperator(),
      subjectType, false);
    filter.addSingleFilter(filter.getAndConnector(), SubjectRole.SUBJECT, filter.getEqualOperator(),
      subjectUId, false);
    filter.addSingleFilter(filter.getAndConnector(), SubjectRole.ROLE, filter.getEqualOperator(),
      roleUId, false);

    removeByFilter(filter);
    /*
    Collection result = getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty())
      throw new ApplicationException("Unable to locate data!");
    remove(result);
    */
  }

  /**
   * Remove a collection of Subject-Role relationships.
   *
   * @param subjectRoleCollection          The collection of the Subject-Role relation to be removed.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   *//*
  private void remove(Collection subjectRoleCollection) throws Throwable
  {
    Iterator iter = subjectRoleCollection.iterator();
    SubjectRole subjectRole = null;
    while (iter.hasNext())
    {
      subjectRole = (SubjectRole) iter.next();
      remove(new Long(subjectRole.getUId()));
    }
  }*/

  /**
   * Find a <code>Collection</code> of Role entity with the specified subject type and
   * the specified uId of Subject entity.
   *
   * @param subjectUId    The uId of the Subject entity.
   * @param subjectType   The type of Subject entity.
   *
   * @return a Collection of Role entities, or <B>null</B> if no relationship found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection getRolesForSubject(Long subjectUId, String subjectType) throws Throwable
  {
    Collection result = getSubjectRoleForSubject(subjectUId, subjectType);
    /*
    if (result == null || result.isEmpty())
      return null;
    */
    return getRolesFromSubjectRoles(result);
  }

  /**
   * Find a <code>Collection</code> of Role entity with the specified subject type and
   * the specified uId of Subject entity.
   *
   * @param subjectUId    The uId of the Subject entity.
   * @param subjectType   The type of Subject entity.
   *
   * @return a Collection of Role entities, or empty collection if no relationship found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection getSubjectRoleForSubject(Long subjectUId, String subjectType) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SubjectRole.SUBJECT_TYPE, filter.getEqualOperator(),
      subjectType, false);
    filter.addSingleFilter(filter.getAndConnector(), SubjectRole.SUBJECT, filter.getEqualOperator(),
      subjectUId, false);

    return getEntityByFilterForReadOnly(filter);
    /*
    Collection result = getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty())
      return null;
    return result;
    */
  }

  /**
   * Retrieve a <code>Collection</code> of Role entities in the specified collection of
   * Subject-Role relationships.
   *
   * @param subjectRoleCollection          Collection of the Subject-Role relation.
   *
   * @return a Collection of Role entities, or empty collection if the specified
   * collection is null or empty.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  private Collection getRolesFromSubjectRoles(Collection subjectRoleCollection) throws Throwable
  {
    /*
    if (subjectRoleCollection == null || subjectRoleCollection.isEmpty())
       return null;
    */
    //Iterator iter = subjectRoleCollection.iterator();
    SubjectRole subjectRole = null;
    //Role role = null;
    Collection roleVector = new Vector();
    if (subjectRoleCollection != null && !subjectRoleCollection.isEmpty())
    {
      for (Iterator iter=subjectRoleCollection.iterator(); iter.hasNext(); )
      {
        subjectRole = (SubjectRole) iter.next();
        roleVector.add(new Long(subjectRole.getRole()));
      }
    }
    if (!roleVector.isEmpty())
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addDomainFilter(null, Role.UID, roleVector, false);
      roleVector = RoleEntityHandler.getInstance().getEntityByFilter(filter);
    }

    return roleVector;
  }

  /**
   * Remove all Subject-Role relationship of the specified Subject entity.
   *
   * @param subjectUId    The uId of the Subject entity.
   * @param subjectType   The type of Subject entity.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public void removeSubjectRoleForSubject(Long subjectUId, String subjectType) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SubjectRole.SUBJECT_TYPE, filter.getEqualOperator(),
      subjectType, false);
    filter.addSingleFilter(filter.getAndConnector(), SubjectRole.SUBJECT, filter.getEqualOperator(),
      subjectUId, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty())
      throw new ApplicationException("Unable to locate data!");

    Iterator iter = result.iterator();
    SubjectRole subjectRole = null;
    while (iter.hasNext())
    {
      subjectRole = (SubjectRole) iter.next();
      remove(new Long(subjectRole.getUId()));
    }
  }

  /**
   * To retrieve a Subject-Role relationship.
   *
   * @param roleUId       The uId of the Role entity.
   * @param subjectUId    The uId of the Subject entity.
   * @param subjectType   The type of Subject entity.
   *
   * @return the required SubjectRole entity.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   *
   */
  public SubjectRole getSubjectRole(Long subjectUId, String subjectType, Long roleUId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SubjectRole.SUBJECT_TYPE, filter.getEqualOperator(),
      subjectType, false);
    filter.addSingleFilter(filter.getAndConnector(), SubjectRole.SUBJECT, filter.getEqualOperator(),
      subjectUId, false);
    filter.addSingleFilter(filter.getAndConnector(), SubjectRole.ROLE, filter.getEqualOperator(),
      roleUId, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty())
      return null;
    return (SubjectRole) result.iterator().next();
  }

  /**
   * Remove all Subject-Role relationship of the specified Role entity.
   *
   * @param roleUId    The uId of the Role entity.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public void removeSubjectRoleForRole(Long roleUID) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SubjectRole.ROLE, filter.getEqualOperator(),
      roleUID, false);

    removeByFilter(filter);
  }

  /**
   * Find a <code>Collection</code> of Subject UIds with the specified uId of Role entity
   * and the specified subject type.
   *
   * @param roleUId    The uId of the Role entity.
   * @param subjectType   The type of subject that has relationship with the Role Entity.
   *
   * @return a Collection of Subject UId, or empty collection if no relationship found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection getSubjectUIdsForRole(String subjectType, Long roleUId) throws Throwable
  {
    Collection result = getSubjectRoleForRole(subjectType, roleUId);
    /*if (result == null || result.isEmpty())
      return null;
    */
    return getSubjectUIdsFromSubjectRoles(result);
  }

  /**
   * Find a <code>Collection</code> of SubjectRole entity with the specified uId of Role entity
   * and the specified subject type.
   *
   * @param roleUId       The uId of the Role entity.
   * @param subjectType   The type of subject that has relationship with the Role Entity.
   *
   * @return a Collection of Subject entities, or empty collection if no relationship found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection getSubjectRoleForRole(String subjectType, Long roleUId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SubjectRole.ROLE, filter.getEqualOperator(),
      roleUId, false);
    filter.addSingleFilter(filter.getAndConnector(), SubjectRole.SUBJECT_TYPE,
      filter.getEqualOperator(), subjectType, false);

    return getEntityByFilterForReadOnly(filter);
    /*
    Collection result = getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty())
      return null;
    return result;
    */
  }

  /**
   * Retrieve a collection of Subject UIds in the specified collection of Subject-Role relationships.
   *
   * @param subjectRoleCollection          Collection of the Subject-Role relation.
   *
   * @return a Collection of Subject UIds, or empty collection if specified collection
   * is null or empty.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  private Collection getSubjectUIdsFromSubjectRoles(Collection subjectRoleCollection) throws Throwable
  {
    /*
    if (subjectRoleCollection == null || subjectRoleCollection.isEmpty())
       return null;
    */
    //Iterator iter = subjectRoleCollection.iterator();
    SubjectRole subjectRole = null;
    Vector subjectVector = new Vector();
    if (subjectRoleCollection != null && !subjectRoleCollection.isEmpty())
    {
      for (Iterator iter=subjectRoleCollection.iterator(); iter.hasNext(); )
      {
        subjectRole = (SubjectRole) iter.next();
        subjectVector.add(new Long(subjectRole.getSubject()));
      }
    }
    return subjectVector;
  }

}