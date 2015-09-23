/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RenderUtil.java
 *
 ****************************************************************************
 * Date           	Author                        	Changes
 ****************************************************************************
 * 30 Nov 2005		  Sumedh Chalermkanjana			Created
 */
package com.gridnode.gtas.client.web.archive.searchpage;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

/**
 * This class contains utility methods used by EsPiValidationAction.java and EsDocValidationAction.java
 */
public class RenderUtil
{
	public static void renderError(ActionContext actionContext,
                           RenderingContext rContext,
                           IRenderingPipeline rPipe)
  {
  	HttpServletRequest request = actionContext.getRequest();
  	String errorMessage = (String) request.getAttribute(Constants.ERROR_MESSAGE);
  	request.removeAttribute(Constants.ERROR_MESSAGE);
  	if (StaticUtils.stringNotEmpty(errorMessage))
  	{
	  	EsErrorRenderer renderer = new EsErrorRenderer(rContext, errorMessage);
	  	rPipe.addRenderer(renderer);
  	}
  }
}
