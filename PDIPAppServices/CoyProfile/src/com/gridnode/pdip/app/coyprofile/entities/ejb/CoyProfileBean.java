/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CoyProfileBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.coyprofile.entities.ejb;

import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
 

/**
 * A CoyProfileBean provides persistency services for CompanyProfile(s).
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class CoyProfileBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6405119316649470223L;

	public String getEntityName()
  {
    return CompanyProfile.ENTITY_NAME;
  }

  //called during ejbCreate()
  protected void checkDuplicate(IEntity entity) throws java.lang.Exception
  {
//    CoyProfileDAOHelper.getInstance().checkDuplicate(
//      (CompanyProfile)entity, false); // don't check key
  }


}