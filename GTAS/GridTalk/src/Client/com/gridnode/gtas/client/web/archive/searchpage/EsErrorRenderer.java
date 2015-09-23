/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EsErrorRenderer.java
 *
 ****************************************************************************
 * Date           	Author                        	Changes
 ****************************************************************************
 * 30 Nov 2005		  Sumedh Chalermkanjana			Created
 */
package com.gridnode.gtas.client.web.archive.searchpage;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.pdip.framework.util.AssertUtil;

/**
 * If there is an error (specified by errorMessage) for any invalid input field values of EsPi search page or EsDoc search page, 
 * this class does error rendering on EsPi search page or EsDoc search page.
 */
public class EsErrorRenderer extends AbstractRenderer
{
	private String errorMessage;

	public EsErrorRenderer(RenderingContext rContext, String errorMessage)
	{
		super(rContext);
		AssertUtil.assertTrue(StaticUtils.stringNotEmpty(errorMessage));
		this.errorMessage = errorMessage;
	}

	public void render()
	{
		Element parent = getElementById("errorMessage");
		Text text = _target.createTextNode(errorMessage);
		parent.appendChild(text);
	}
}
