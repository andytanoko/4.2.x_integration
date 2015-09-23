/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessPermission.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002    Neo Sok Lay         Created
 * Oct 17 2005    Neo Sok Lay         Change DataAccess._dataAccessCri from
 *                                    private to protected access to improve perf.
 */
package com.gridnode.pdip.base.acl.auth;


import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.security.*;

import java.util.*;

/**
 * This class is for feature access permissions.
 * <P>
 * The name is the name of the feature (e.g. "feature_a.subfeature_b", "feature_c",
 * etc). The naming convention follows the  hierarchical feature naming
 * convention. Also, an asterisk may appear at the end of the name,
 * following a ".", or by itself, to signify a wildcard match.
 * For example: "feature_a.*" or "*" is valid, "*feature_a" or "feature_a*b"
 * is not valid.
 * <P>
 * <P>
 * The actions to be granted are in a form of a Map object with each granted
 * action as the key and a DataAccess object as the value. A DataAccess object
 * specifies one or more data access criteria for the action. A criterion
 * gives more restrictions in granting a permission in such way that the piece
 * of data to be accessed when the action is being performed is being inspected
 * to ensure that it matches the criterion. A wildcard may appear as an action
 * by itself to signify a wildcard match.
 * <P>
 * A DataAccess object is a Data type and Criteria pair. Data type indicates the
 * type of the data object and Criteria indicates the condition for granting
 * the access. A wildcard may appear as a Data type by itself to signify a
 * wildcard match. The Criteria is optional.
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class AccessPermission extends BasicPermission
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2785216658263586900L;
	private SortedMap      _actions;
  private boolean        _ignoreDataAccess = false;

  /**
   * Construct an AccessPermission object with an initial action and data access
   * criteria.
   *
   * @param featureName The name of the feature (permission).
   * @param action The action to grant.
   * @param dataType The dataType to grant. A null is treated as "*".
   * @param criteria The criteria indicating the condition of the data to grant.
   */
  public AccessPermission(
    String featureName, String action, String dataType, IDataFilter criteria)
  {
    super(featureName);
    _actions = Collections.synchronizedSortedMap(new TreeMap());
    _actions.put(action, new DataAccess(dataType, criteria));
  }

  /**
   * Construct an AccessPermission object with an initial action. No data access
   * criteria is relevant for this action. An initial DataAccess is created
   * with a wildcard for the DataType.
   *
   * @param featureName The name of the feature (permission).
   * @param action The action to grant.
   */
  public AccessPermission(
    String featureName, String action)
  {
    this(featureName, action, null, null);
    _ignoreDataAccess = true;
  }

  /**
   * Construct an AccessPermission object from a specified action access map.
   *
   * @param featureName The name of the feature (permission).
   * @param accessMap The action access map containing Action-DataAcess pairs.
   */
  public AccessPermission(String featureName, Map accessMap)
  {
    super(featureName);
    _actions = Collections.synchronizedSortedMap(new TreeMap(accessMap));
  }

  /**
   * Returns a new PermissionCollection object for storing
   * AccessPermission objects.
   * <p>
   *
   * @return a new PermissionCollection object suitable for storing
   * AccessPermissions.
   */
  public PermissionCollection newPermissionCollection()
  {
    return new AccessPermissionCollection();
  }

  /**
   * Returns the hash code value for this object.
   * The hash code used is the hash code of this permissions name, that is,
   * <code>getName().hashCode()</code>, where <code>getName</code> is
   * from the Permission superclass.
   *
   * @return a hash code value for this object.
   */
  public int hashCode()
  {
    return getName().hashCode();
  }

  /**
   * Checks two AccessPermission objects for equality. Checks that <i>obj</i> is
   * a AccessPermission, and has the same name and actionAccessMap as this object.
   * <P>
   * @param obj the object we are testing for equality with this object.
   * @return true if obj is an AccessPermission, and has the same name and
   * actionAccessMap as this AccessPermission object.
   */
  public boolean equals(Object obj)
  {
    if (obj == this)
      return true;

    if (! (obj instanceof AccessPermission))
      return false;

    AccessPermission that = (AccessPermission) obj;

    return (this.getActionsAccessMap().equals(that.getActionsAccessMap())) &&
        (this.getName().equals(that.getName()));
  }

  /**
   * Checks if this AccessPermission object "implies" the specified
   * permission.
   * <P>
   * More specifically, this method returns true if:<p>
   * <ul>
   * <li> <i>p</i> is an instanceof AccessPermission,<p>
   * <li> <i>p</i>'s actions are a proper subset of this
   * object's actions, and <p>
   * <li> <i>p</i>'s name is implied by this object's
   *      name. For example, "user_mgmt.*" implies "user_mgmt.admin".
   * </ul>
   * @param p the permission to check against.
   *
   * @return true if the specified permission is implied by this object,
   * false if not.
   */
  public boolean implies(Permission p)
  {
    boolean implies = super.implies(p);
    if (implies)
    {
      implies = false;
      AccessPermission ap = (AccessPermission)p;
      SortedMap otherMap = ap.getActionsAccessMap();

      Set otherActions = otherMap.keySet();
      synchronized (otherMap)
      {
        //all actions and data types must be implied
        for (Iterator i=otherActions.iterator(); i.hasNext() && !implies; )
        {
          String action = (String)i.next();
          DataAccess access = (DataAccess)otherMap.get(action);

          implies = ap._ignoreDataAccess ?
                    implies(_actions, action) :
                    implies(_actions, action, access);
        }
      }
    }

    //ACLLogger.debugLog("AccessPermission", "implies", this.toString() + " vs " +p.toString()+"="+implies);
    return implies;
  }

  /**
   * Checks whether an action is implied by the Action access map.
   *
   * @param base The Action access map to check against.
   * @param actionToCheck The action to check.
   * @return <B>true</B> if the action is implied by the Action access map,
   * <B>false</B> otherwise.
   *
   * @since 2.0
   */
  private boolean implies(SortedMap base, String actionToCheck)
  {
    return base.containsKey(actionToCheck) || base.containsKey("*");
  }

  /**
   * Checks whether an action is implied by the Action access map.
   *
   * @param base The Action access map to check against.
   * @param actionToCheck The action to check.
   * @param dAToCheck The DataAccess for the action. This will be checked against
   * the DA of the corresponding action to make sure that the data types and
   * criteria are implied.
   * @return <B>true</B> if the action is implied by the Action access map,
   * <B>false</B> otherwise.
   *
   * @since 2.0
   */
  private boolean implies(SortedMap base, String actionToCheck, DataAccess dAToCheck)
  {
    boolean implies = false;

    //access criteria explicitly defined or wildcard defined
    String action = base.containsKey(actionToCheck) ? actionToCheck : "*";
    if (base.containsKey(action))
    {
      DataAccess existingDA = (DataAccess)base.get(action);

      Set dataTypes = dAToCheck.getDataTypes();
      synchronized (dAToCheck._dataAccessCri)
      {
        for (Iterator iter=dataTypes.iterator(); iter.hasNext() && !implies; )
        {
          String dataType = (String)iter.next();
          //found a matching datatype or a datatype wildcard
          /**@todo check criteria ??? */
          implies = (existingDA.isDefined(dataType) ||
                     existingDA.isDefined("*"));
        }
      }
    }
    return implies;
  }

  /**
   * Return the canonical string representation of the actions.
   *
   * @return the canonical string representation of the actions.
   */
  public String getActions()
  {
    StringBuffer actionsStr = new StringBuffer();
    Set actions = _actions.keySet();
    synchronized (_actions)
    {
      for (Iterator i=actions.iterator(); i.hasNext(); )
      {
        String action = (String)i.next();
        DataAccess access = (DataAccess)_actions.get(action);
        actionsStr.append(action).append(":").append(access);
        if (i.hasNext())
          actionsStr.append(',');
      }
    }
    return actionsStr.toString();
  }

  /**
   * Get the Action access map for this permission.
   */
  public SortedMap getActionsAccessMap()
  {
    return Collections.unmodifiableSortedMap(_actions);
  }

  /**
   * Union the actions in two action access maps.
   *
   * @param oneMap One action access map.
   * @param otherMap The other action access map.
   *
   * @return the Unioned action access map.
   * @exception CloneNotSupportedException The contents of the input map cannot
   * be cloned.
   */
  public static SortedMap unionActions(SortedMap oneMap, SortedMap otherMap)
    throws CloneNotSupportedException
  {
    SortedMap unionedMap = Collections.synchronizedSortedMap(new TreeMap(oneMap));

    Set otherActions = otherMap.keySet();
    synchronized(otherMap)
    {
      for (Iterator i=otherActions.iterator(); i.hasNext(); )
      {
        String action = (String)i.next();
        DataAccess access = new DataAccess((DataAccess)otherMap.get(action));

        if (oneMap.containsKey(action)) //same action defined
        {
          DataAccess existingDA = new DataAccess((DataAccess)oneMap.get(action));

          unionDataAccess(existingDA, access);
          unionedMap.put(action, existingDA);
        }
        else
        {
          unionedMap.put(action, access);
        }
      }
    }

    return unionedMap;
  }

  /**
   * Union two DataAccess objects. All data types will be combined and the
   * criteria for the same data type are "ORed" together.
   *
   * @param base The base DataAccess object. This will contained the unioned
   * set after this operation.
   * @param other The other DataAccess object of the operation.
   */
  private static void unionDataAccess(DataAccess base, DataAccess other)
  {
    Set dataTypes = other.getDataTypes();

    //union the Data access criteria
    synchronized (other._dataAccessCri)
    {
      for (Iterator iter=dataTypes.iterator(); iter.hasNext(); )
      {
        String dataType = (String)iter.next();
        IDataFilter newCriteria = other.getCriteria(dataType);
        IDataFilter oldCriteria = base.getCriteria(dataType);
        IDataFilter criteria = newCriteria;
        if (oldCriteria != null)
        {
          criteria = (criteria == null)?
                       oldCriteria :
                       new DataFilterImpl(
                         oldCriteria, criteria.getOrConnector(), newCriteria);
        }
        base.addDataAccess(dataType, criteria);
      }
    }
  }

  /**
   * This class keeps a Map of DataType-Criteria pairs.
   * <P>
   */
  private static class DataAccess implements java.io.Serializable
  {
    /**
		 * Serial Version UID
		 */
		private static final long serialVersionUID = -2813261123157346212L;
		protected Map _dataAccessCri;

    /**
     * Create a DataAccess object with an empty Map.
     */
    DataAccess()
    {
      _dataAccessCri = (Map)Collections.synchronizedMap(new HashMap());
    }

    /**
     * Create a DataAccess object with an initial pair of DataType-Criteria
     * in the Map.
     *
     * @param dataType The dataType. Treated as "*" if null.
     * @param criteria The criteria. If null, it means allow access to all
     * data of the specified dataType.
     */
    DataAccess(String dataType, IDataFilter criteria)
    {
      this();
      addDataAccess(dataType, criteria);
    }

    /**
     * Create a DataAccess object from another DataAccess object.
     * This is done by cloning the DataType-Criteria map.
     *
     * @param dA The DataAccess object to create from.
     * @exception CloneNotSupportedException DataType-Criteria map cannot be
     * cloned.
     */
    DataAccess(DataAccess dA) throws CloneNotSupportedException
    {
      _dataAccessCri = Collections.synchronizedMap(new HashMap(dA._dataAccessCri));
    }

    /**
     * Checks two DataAccess objects whether their DataType-Criteria maps
     * are equal.
     *
     * @param other The other DataAccess object to compare with.
     * @return <B>true</B> if <CODE>other</CODE> is a DataAccess object, and
     * its DaTaype-Criteria map contents are the same as that of this
     * DataAccess object.
     */
    public boolean equals(Object other)
    {
      boolean isEquals = (other != null) &&
                         (other instanceof DataAccess);
      if (isEquals)
      {
        DataAccess o = (DataAccess)other;
        isEquals = _dataAccessCri.equals(o._dataAccessCri);
      }

      return isEquals;
    }

    /**
     * Add a DataType-Criteria pair
     *
     * @param dataType The dataType
     * @param criteria The criteria.
     */
    void addDataAccess(String dataType, IDataFilter criteria)
    {
      if (dataType == null)
        dataType = "*";

      _dataAccessCri.put(dataType, criteria);
    }

    /**
     * Remove a DataType-Criteria pair
     *
     * @param dataType The key to the pair.
     */
    void removeDataAccess(String dataType)
    {
      if (dataType == null)
        dataType = "*";

      _dataAccessCri.remove(dataType);
    }

    /**
     * Get the keys of existing pairs.
     */
    Set getDataTypes()
    {
      return _dataAccessCri.keySet();
    }

    /**
     * Get the Criteria of a pair.
     * @param dataType The key to the pair.
     */
    IDataFilter getCriteria(String dataType)
    {
      if (dataType == null)
        dataType = "*";

      return (IDataFilter)_dataAccessCri.get(dataType);
    }

    /**
     * Checks whether a dataType is defined in this DataAccess object.
     *
     * @param dataType The dataType to check.
     * @return <B>true</B> if defined, <B>false</B> otherwise.
     */
    boolean isDefined(String dataType)
    {
      if (dataType == null)
        dataType = "*";

      return _dataAccessCri.containsKey(dataType);
    }

    public String toString()
    {
      return getDataTypes().toString();
    }
  }
}


