/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.license.entities.ejb;

import com.gridnode.pdip.app.license.model.License;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;


/**
 * A LicenseBean provides persistency services for License(s).
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class LicenseBean extends AbstractEntityBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8661930830287773978L;

	public String getEntityName()
  {
    return License.ENTITY_NAME;
  }

  //called during ejbCreate()
  protected void checkDuplicate(IEntity entity) throws java.lang.Exception
  {
//    LicenseDAOHelper.getInstance().checkDuplicate(
//      (License)entity, false); // don't check key
  }


}