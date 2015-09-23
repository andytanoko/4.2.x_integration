/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 27 2002	 MAHESH              Created
 */
package com.gridnode.pdip.base.gwfbase.bpss.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
 
public class BpssDocumentation
extends AbstractEntity
implements IBpssDocumentation {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1093415147160843352L;
	//Attributes
	protected String _uri;
	protected String _documentation;

	//Abstract methods of AbstractEntity
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public Number getKeyId() {
		return UID;
	}

	public String getEntityDescr() {
		return ENTITY_NAME+":"+_uId;
	}

	// ******************** Getters for attributes ***************************

	public String getUri()
	{
		return _uri;
	}

	public String getDocumentation()
	{
		return _documentation;
	}

	// ******************** Setters for attributes ***************************

	public void setUri(String uri)
	{
		_uri=uri;
	}

	public void setDocumentation(String documentation)
	{
		_documentation=documentation;
	}

}