/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DisplayTag.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 27 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.taglib.display;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.JspException;
import javax.servlet.http.*;

import java.util.ArrayList;
import java.util.HashMap;


/** Tag library Class that handles display of records into different frames so that the when
 *  the number of records goes large the end-user is able to see a subset of records and navigate
 *  between these subsets
 *
 *
 */
public final class DisplayTag implements Tag {

	private PageContext pc = null;

	ArrayList alFinalReturn = new ArrayList();
	ArrayList alContent = new ArrayList();

	private String strStart = null;
	private String strRange = null;

	int intEnd = 0;

	int intContentSize = 0;

	public void setPageContext(PageContext p) {
		pc = p;
	}

	public void setParent(Tag t) {}

	public Tag getParent() { return null; }

	public void setStart(String s) {
		strStart = s;
		if(strStart.equals(""))
			strStart = "0";
	}

	public String getStart() {
		return strStart;
	}

	public void setRange(String s) {
		strRange = s;
	}

	public String getRange() {
		return strRange;
	}

	public void setContent(ArrayList alContent)
	{
		this.alContent = alContent;
	}

	public ArrayList getContent()
	{
		return alContent;
	}

	public int doStartTag() throws JspException
	{
		int intStart = Integer.parseInt(strStart);
		int intRange = Integer.parseInt(strRange);

		intEnd = intStart+intRange;

		intContentSize = alContent.size();

		if(alContent.size() == 0)
		{
			try
			{
				pc.getOut().write("<br><br>");
				pc.getOut().write("<center>");
				pc.getOut().write("<h3>No Records to Display</h3>");
				pc.getOut().write("</center>");

			}
			catch(Exception e)
			{
			}
			return SKIP_BODY;
		}


		if(intEnd > intContentSize)
		{
			intEnd = intContentSize;
		}

		for(int i = intStart; i < intEnd; i++)
		{
			alFinalReturn.add((HashMap)alContent.get(i));
		}

		pc.setAttribute("alReturn", alFinalReturn);

		return EVAL_BODY_INCLUDE;

	}

	public int doEndTag() throws JspException
	{
		if(intContentSize > 0)
		{
			int intStart = Integer.parseInt(strStart);
			int intRange = Integer.parseInt(strRange);

			try
			{
				pc.getOut().write("<table width=\"100%\">");
				pc.getOut().write("	  <tr>");
				pc.getOut().write("			<td align=\"left\">");
				pc.getOut().write("				<font face=\"Arial\" size=\"2\"><b>");
				pc.getOut().write("					RECORDS " + (intStart + 1) + " ");
				pc.getOut().write("					TO " + " " + intEnd + " ");
				pc.getOut().write("					OF " + " " + intContentSize);
				pc.getOut().write("				</b></font>");
				pc.getOut().write("			</td>");

				pc.getOut().write("			<td align=\"right\">");

				if(intStart != 0)
				{
					pc.getOut().write("<a style=\"text-decoration:none\" href=\"javascript:next('"+(0)+"')\">");
					pc.getOut().write("		<font face=\"Arial\" size=\"2\">");
					pc.getOut().write("			<b>START</b>");
					pc.getOut().write("		</font>");
					pc.getOut().write("</a>&nbsp;");
				}

				if((intStart + intRange) < intContentSize)
				{
					pc.getOut().write("<a style=\"text-decoration:none\" href=\"javascript:next('"+(intStart + intRange)+"')\">");
					pc.getOut().write("		<font face=\"Arial\" size=\"2\">");
					pc.getOut().write("			<b>NEXT</b>");
					pc.getOut().write("		</font>");
					pc.getOut().write("</a>&nbsp;");

				}

				if(intStart != 0)
				{
				        pc.getOut().write("&nbsp;<a style=\"text-decoration:none\" href=\"javascript:next('"+(intStart - intRange)+"')\">");
					pc.getOut().write("		<font face=\"Arial\" size=\"2\">");
					pc.getOut().write("			<b>PREVIOUS</b>");
					pc.getOut().write("		</font>");
					pc.getOut().write("</a>");

				}

				if((intStart + intRange) < intContentSize)
				{
					pc.getOut().write("&nbsp;<a style=\"text-decoration:none\" href=\"javascript:next('"+(intContentSize - (intContentSize%intRange>0?intContentSize%intRange:intRange))+"')\">");
					pc.getOut().write("		<font face=\"Arial\" size=\"2\">");
					pc.getOut().write("			<b>END</b>");
					pc.getOut().write("		</font>");
					pc.getOut().write("</a>");

				}


				pc.getOut().write("			</td>");

				pc.getOut().write("	  </tr>");
				pc.getOut().write("</table>");
			}
			catch(Exception e)
			{
			}
		}
		return EVAL_PAGE;
	}

	public void release() {
		pc = null;
	}
}
