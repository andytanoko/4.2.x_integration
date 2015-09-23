/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CursorBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 03 2002    Neo Sok Lay         Bug in get(index,count).
 */
package com.gridnode.pdip.framework.db.cursor;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
 
public class CursorBean implements SessionBean{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2721949356629613686L;
	private SessionContext ctx;
	private ArrayList list;
	private int index;
	private int min=-1,max=-1;

	public void setSessionContext(SessionContext ctx) {
		this.ctx=ctx;
	}
	public void ejbActivate() {

	}
	public void ejbPassivate() {

	}
	public void ejbRemove() {

	}
	public void ejbCreate() throws CreateException {
		this.list=null;
		index=min;
	}

	public void ejbCreate(Collection col) throws CreateException {
		setData(col);
	}

	/**
	 * This method sets collection to the cursor
	 */
	public void setData(Collection col){
		this.list=new ArrayList(col);
		this.index=min;
		if(list!=null)
			max=list.size();
	}

	/**
	 * This method returns size of the cursor
	 */
	public int size(){
		return max;
	}

	/**
	 * This method returns element at that given index
	 */
	public Object get(int  index){
		return (index>min && index< max)?list.get(index):null;
	}

	public Collection get(int index,int count){
		ArrayList retList=new ArrayList();
		index=(index<0)?0:index;
		for(int loop=index;loop-index<count && loop<max;loop++)
			retList.add(list.get(loop));
		return retList;
	}

	/**
	 * This method moves the cursor index to the first element
	 */
	public void first(){
		index=min;
	}

	/**
	 * This method moves the cursor index to the last element
	 */
	public void last(){
		index=max;
	}


	/**
	 * This method moves the cursor index to the next element
	 */
	public Object next(){
		index=(++index<list.size())?index:max-1;
		return get(index);
	}

	/**
	 * This method returns collection of elements from the current
	 * position to next "n" elements specified by the parameter
	 */
	public Collection next(int count){
		ArrayList retList=new ArrayList();
		Object obj=null;
		while(retList.size()<count && (obj=next())!=null)
			retList.add(obj);
		return retList;
	}

	/**
	 * This method moves the cursor index to the previous element
	 */
	public Object previous(){
		index=(--index>=min)?index:min;
		return get(index);
	}

	/**
	 * This method returns collection of elements from the current
	 * position to previous "n" elements specified by the parameter
	 */
	public Collection previous(int count){
		ArrayList retList=new ArrayList();
		Object obj=null;
		while(retList.size()<count && (obj=previous())!=null)
			retList.add(obj);
		return retList;
	}
}