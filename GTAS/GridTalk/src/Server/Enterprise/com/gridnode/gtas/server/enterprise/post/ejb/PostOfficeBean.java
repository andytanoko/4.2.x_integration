/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PostOfficeBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 06 2002    Neo Sok Lay         Created
 * Jan 23 2003    Neo Sok Lay         Add getMyGridNodeCommInfo().
 */
package com.gridnode.gtas.server.enterprise.post.ejb;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.enterprise.exceptions.PostingException;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.post.PostInstruction;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This bean provides services to for posting messages out of the enterprise.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class PostOfficeBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6889503539731121242L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  /**
   * @see IPostOffice#isGridMasterPostOfficeOpened
   */
  public boolean isGridMasterPostOfficeOpened()
  {
    return GridMasterState.getInstance().isOnline();
  }

  /**
   * @see IPostOffice#isPostmanWorkingNow
   */
  public boolean isPostmanWorkingNow()
  {
    return GridMasterState.getInstance().isLocked();
  }

  /**
   * @see IPostOffice#getOpenedGridMasterPostOfficeID
   */
  public String getOpenedGridMasterPostOfficeID()
  {
    return GridMasterState.getInstance().getGmNodeID();
  }

  /**
   * @see IPostOffice#isGridMasterPostOfficeOpened
   */
  public void dropToGridMasterPostOffice(PostInstruction instruction)
    throws PostingException, SystemException
  {
    FacadeLogger logger = Logger.getPostOfficeFacadeLogger();
    String methodName   = "dropToGridMasterPostOffice";
    Object[] params     = new Object[] {instruction};

    try
    {
      logger.logEntry(methodName, params);

      if (isGridMasterPostOfficeOpened())
      {
        //set recipient to Gm if not specified.
        if (instruction.getRecipientNodeID() == null ||
           instruction.getRecipientNodeID().trim().length()==0)
          instruction.setRecipientNodeID(getOpenedGridMasterPostOfficeID());

        GridMasterPostOffice.getInstance().post(instruction);
      }
      else
        throw new PostingException("GridMasterPostOffice is not opened. Message cannot sent.");
    }
    catch (PostingException ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      logger.logWarn(methodName, params, ex);
      throw new PostingException(
                "Fail to drop PostInstruction to GridMasterPostOffice", ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * @see IPostOffice#getMyGridNodeCommInfo
   */
  public String getMyGridNodeCommInfo()
  {
    return GridMasterState.getInstance().getMyGNCI();
  }
}