/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPostOfficeObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 06 2002    Neo Sok Lay         Created
 * Jan 23 2003    Neo Sok Lay         Add getMyGridNodeCommInfo().
 */
package com.gridnode.gtas.server.enterprise.post.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.gridnode.gtas.server.enterprise.exceptions.PostingException;
import com.gridnode.gtas.server.enterprise.post.PostInstruction;
import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * LocalObject for PostOfficeBean. In this Post Office context, Open = Online, thus a GridMaster Post Office that is opened
 * is currently online and thus Post Instructions can be dropped in for Postman to deliver a message to the GridMaster.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IPostOfficeObj
  extends        EJBObject
{
  /**
   * Check if the GridMaster Post Office is opened so that the Post Instruction can be dropped in.
   *
   * @return <b>true</b> if opened, <b>false</b> otherwise.
   */
  public boolean isGridMasterPostOfficeOpened()
    throws RemoteException;

  /**
   * Drop a Post Instruction to the GridMaster Post Office.
   *
   * @param instruction The Post Instruction
   * @throws PostingException Error processing the Post Instruction
   */
  public void dropToGridMasterPostOffice(PostInstruction instruction)
    throws PostingException, SystemException, RemoteException;

  /**
   * Check whether any of the Postman is working currently i.e. making delivery now.
   * @return <b>true</b> if working, <b>false</b> otherwise.
   */
  public boolean isPostmanWorkingNow()
    throws RemoteException;

  /**
   * Get the identifier for the GridMaster Post Office that is opened currently.
   *
   * @return GridNodeID of the GridMaster Post Office that is currently opened, <b>null</b> if none is opened.
   */
  public String getOpenedGridMasterPostOfficeID()
    throws RemoteException;

  /**
   * Get current GridNodeCommInfo (GNCI) for this GridTalk.
   *
   * @return Current GNCI for this GridTalk. <b>null</b> if this GridTalk is
   * currently offline.
   */
  public String getMyGridNodeCommInfo()
    throws RemoteException;
}