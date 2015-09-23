/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June  27 2002	MAHESH              Created
 */
package com.gridnode.pdip.base.data.entities.ejb;

import com.gridnode.pdip.base.data.entities.model.StringData;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
 
public class StringDataBean extends AbstractEntityBean {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3823399241040519075L;

	//Abstract methods of AbstractEntityBean
	public String getEntityName(){
		return StringData.ENTITY_NAME;
	}
}