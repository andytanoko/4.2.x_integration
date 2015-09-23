/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractBpssModel.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 5, 2005    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.deploy.manager.bpss;

import java.io.Serializable;

import com.gridnode.xml.adapters.GNAttribute;
import com.gridnode.xml.adapters.GNElement;

/**
 * The abstract class for the Bpss Obj.
 * 
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public abstract class AbstractBpssModel
	implements Serializable
{
	private AbstractBpssModel _parent;
	
	public  final String _ATTR_DEFAULT_REQUIRED = "required";
	public  final String _ATTR_DEFAULT_IMPLIED = "implied";
	
	/**
	 * Unmarshal the GNElement into AbstractBpssModel obj.
	 * @param element
	 * @return
	 */
	public abstract AbstractBpssModel buildModel(GNElement element);
	
	/**
	 * Marshal the AbstractBpssModel obj to GNElement
	 * @return
	 */
	public abstract GNElement buildElement();
	
	/**
	 * Get the parent of the current AbstractBpssModel.
	 * @return
	 */
	public AbstractBpssModel getParent()
	{
		return _parent;
	}
	
	/**
	 * Set the parent of the current AbstractBpssModel.
	 * @param parent
	 */
	public void setParent(AbstractBpssModel parent)
	{
		_parent = parent;
	}
	
	/**
	 * Set the value of a GNAttribute.
	 * @param target the target attribute that we are going to set the value.
	 * @param source the attribute that we retrieve the value.
	 */
	public void setAttributeValue(GNAttribute target, GNAttribute source)
	{
		if(source != null)
		{
			target.setValue(source.getValue());
		}
	}
	
	/**
	 * Add the attribute to the element we pass in. 
	 * @param elem
	 * @param attDefault can be Required, Implied or none of them.
	 * @param attValue
	 */
	public void addAttributeToElement(GNElement elem, String attDefault, GNAttribute attValue)
	{
		if(attDefault.equals(_ATTR_DEFAULT_IMPLIED))
		{
			if(attValue.getValue().compareTo("")!=0)
			{
				elem.addAttribute(attValue);
			}
		}
		else if(attDefault.equals(_ATTR_DEFAULT_REQUIRED))
		{
			elem.addAttribute(attValue);
		}
		else
		{
			if(attValue.getValue().compareTo("")!=0)
			{
				elem.addAttribute(attValue);
			}
		}
	}
}
