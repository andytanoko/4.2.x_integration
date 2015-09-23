/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessRight.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21 2002    Neo Sok Lay         Created
 * Jul 24 2003    Neo Sok Lay         Change EntityDescr
 */
package com.gridnode.pdip.base.acl.model;

import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This entity defines the single right for a role to perform a certain action
 * on a particular feature in the application.<P>
 * The right can be optionally subjected to certain criteria based on the
 * type of entity to be accessed.
 *
 * <P>The Model:<BR><PRE>
 *   UId          - UID for a AccessRight entity instance.
 *   RoleUID      - The UID of the Role that this access right is defined for.
 *   Feature      - The Feature that this AccessRight grants access to.
 *   Description  - Description of the AccessRight.
 *   Action       - The Action on the Feature that this AccessRight grants access
 *                  to.
 *   DataType     - The DataType that this AccessRight grants access to.
 *   Criteria     - The Criteria for the data the this AccessRight grants access
 *                  to.
 *   CanDelete    - Flag indicating whether this data can be deleted.
 *   Version      - Version of this AccessRight record.
 * </PRE>
 * <P>

 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class AccessRight
  extends    AbstractEntity
  implements IAccessRight
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1964808718297733157L;
	protected String _descr;
  protected String _feature;
  protected Long   _roleUID;
  protected String _action;
  protected String _dataType;
  protected IDataFilter _criteria;
  protected boolean _canDelete = true;

  public AccessRight()
  {
  }

  // *********************** Abstract Entity methods *************************
  public String getEntityDescr()
  {
    return new StringBuffer().append(getDescr()).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // *********************** Getters ***************************************

  public String getDescr()
  {
    return _descr;
  }

  public String getFeature()
  {
    return _feature;
  }

  public Long getRoleUID()
  {
    return _roleUID;
  }

  public String getAction()
  {
    return _action;
  }

  public String getDataType()
  {
    return _dataType;
  }

  public IDataFilter getCriteria()
  {
    return _criteria;
  }

  public boolean canDelete()
  {
    return _canDelete;
  }

  // ************************ Setters **************************************

  public void setDescr(String descr)
  {
    _descr = descr;
  }

  public void setFeature(String feature)
  {
    _feature = feature;
  }

  public void setRoleUID(Long roleUID)
  {
    _roleUID = roleUID;
  }

  public void setAction(String action)
  {
    _action = action;
  }

  public void setDataType(String dataType)
  {
    _dataType = dataType;
  }

  public void setCriteria(IDataFilter criteria)
  {
    _criteria = criteria;
  }

  public void setCanDelete(boolean canDelete)
  {
    _canDelete = canDelete;
  }

}