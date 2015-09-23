/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.base.rolemap.entities.ejb;

import com.gridnode.pdip.base.rolemap.entities.model.*;
import com.gridnode.pdip.framework.db.ejb.*;

public class RoleMapEntityBean extends AbstractEntityBean {

  /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -4282742579277791130L;

		//Abstract methods of AbstractEntityBean
    public String getEntityName(){
        return RoleMapEntity.ENTITY_NAME;
    }
}