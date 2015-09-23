/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DisplayTagTEI.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 27 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.taglib.display;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagExtraInfo;

public class DisplayTagTEI extends TagExtraInfo {

	public VariableInfo[] getVariableInfo(TagData data) {
		return new VariableInfo[] {
			new VariableInfo("alReturn", "java.util.ArrayList",
				true, VariableInfo.NESTED)
		};
	}
}
