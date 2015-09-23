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
 
public class BpssBusinessDocument
extends AbstractEntity
implements IBpssBusinessDocument {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1586108226787018166L;
	//Attributes
	protected String _name;
	protected String _conditionExpr;
        protected String _expressionLanguage;
	protected String _specElement;
	protected String _specLocation;

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

	public String getName()
	{
		return _name;
	}

	public String getConditionExpr()
	{
		return _conditionExpr;
	}

	public String getExpressionLanguage()
	{
		return _expressionLanguage;
	}

	public String getSpecElement()
	{
		return _specElement;
	}

	public String getSpecLocation()
	{
		return _specLocation;
	}


	// ******************** Setters for attributes ***************************

	public void setName(String name)
	{
		_name=name;
	}

	public void setExpressionLanguage(String expressionLanguage)
	{
		_expressionLanguage=expressionLanguage;
	}

	public void setConditionExpr(String conditionExpr)
	{
		_conditionExpr=conditionExpr;
	}

	public void setSpecElement(String specElement)
	{
		_specElement=specElement;
	}

	public void setSpecLocation(String specLocation)
	{
		_specLocation=specLocation;
	}

}