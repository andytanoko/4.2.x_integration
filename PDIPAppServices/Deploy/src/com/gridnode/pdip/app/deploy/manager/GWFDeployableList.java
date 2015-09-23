// %1023962282973:com.gridnode.pdip.app.deployment%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: 
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002   Mathew	        Created
 * Jun 13 2002   Mathew         Repackaged
 */


package com.gridnode.pdip.app.deploy.manager;


/**
 * <p>Title: Deployment Manager</p>
 * <p>Description: Deploy from XML specifications to database</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Gridnode Pte Ltd</p>
 * @author Mathew Yap
 * @version 1.0
 */
import com.gridnode.pdip.app.deploy.manager.util.Utilities;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;

public class GWFDeployableList
  extends java.util.ArrayList
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 41398285390029493L;

	/**
   * Creates a new GWFDeployableList object.
   */
  public GWFDeployableList()
  {
  }

  /**
   * DOCUMENT ME!
   * 
   * @param specUId DOCUMENT ME!
   * @param incomplete DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public int deploy(long specUId, java.util.Hashtable incomplete)
  {
    int finalResult = 0;
    for (int j = 0; j < size(); j++)
    {
      int result = 0;
      try
      {
        result = ((IGWFDeployable)get(j)).deploy(specUId, incomplete);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      finalResult = Utilities.combineResult(finalResult, result);
    }
    return finalResult;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param specUId DOCUMENT ME!
   * @param parentEntryUId DOCUMENT ME!
   */
  public void setParentEntryUId(long specUId, long parentEntryUId)
  {
    for (int j = 0; j < size(); j++)
    {
      //int result = 0;
      try
      {
        ((IGWFDeployable)get(j)).setParentEntryUId(specUId, parentEntryUId);
      }
      catch (GWFException ex)
      {
        ex.printStackTrace();
      }
    }
  }
}