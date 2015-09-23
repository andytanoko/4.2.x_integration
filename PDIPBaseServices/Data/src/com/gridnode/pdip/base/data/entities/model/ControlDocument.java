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
package com.gridnode.pdip.base.data.entities.model;

import com.gridnode.pdip.framework.db.entity.*;
import java.util.*;

public class ControlDocument
extends AbstractEntity
implements IControlDocument {
 
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1298928883092125515L;
				//Attributes
        protected String _name;
        protected String _valueLocation;

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

	public String getValueLocation()
	{
		return _valueLocation;
	}

	// ******************** Setters for attributes ***************************

	public void setName(String name)
	{
		_name=name;
	}

	public void setValueLocation(String valueLocation)
	{
		_valueLocation=valueLocation;
	}

        public Map getDataLocationKeys()
        {
            Map dataLocationKeys=new HashMap();
            dataLocationKeys.put("VALUE_LOCATION",getValueLocation());
            //dataLocationKeys.put("SPEC_ELEMENT",getSpecElement());
            return dataLocationKeys;
        }


}