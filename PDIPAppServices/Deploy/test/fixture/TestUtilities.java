// %1023962283332:fixture%
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


package fixture;

import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.framework.db.filter.*;

import java.util.*;

public class TestUtilities
  extends java.lang.Object
{

  /**
   * DOCUMENT ME!
   */
  public void setUp()
  {
  }

  /**
   * DOCUMENT ME!
   */
  public void tearDown()
  {
  }

  /**
   * DOCUMENT ME!
   * 
   * @param specUUId DOCUMENT ME!
   * @param specVersion DOCUMENT ME!
   * @param entryName DOCUMENT ME!
   * @param entryType DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public static BpssProcessSpecEntry getSpec(String specUUId, 
                                             String specVersion, 
                                             String entryName, 
                                             String entryType)
  {
    Collection bpssProcSpecColl = null;
    BpssProcessSpecEntry bpssProcSpecEntry = null;
    try
    {
      AbstractEntityHandler handler = null;
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IBpssProcessSpecification.UUID, 
                             filter.getEqualOperator(), specUUId, false);
      filter.addSingleFilter(filter.getAndConnector(), 
                             IBpssProcessSpecification.VERSION, 
                             filter.getEqualOperator(), specVersion, false);
      handler = EntityHandlerFactory.getHandlerFor(
                    BpssProcessSpecification.ENTITY_NAME, false);
      bpssProcSpecColl = handler.getEntityByFilter(filter);
      if (bpssProcSpecColl.size() > 1)
      {
        return null;
      }
      else if (bpssProcSpecColl.isEmpty())
      {
        return null;
      }
      else
      {
        Iterator it = bpssProcSpecColl.iterator();
        BpssProcessSpecification bpssProcSpec = (BpssProcessSpecification)it.next();
        filter = new DataFilterImpl();
        filter.addSingleFilter(null, IBpssProcessSpecEntry.SPEC_UID, 
                               filter.getEqualOperator(), 
                               new Long(bpssProcSpec.getUId()), false);
        filter.addSingleFilter(filter.getAndConnector(), 
                               IBpssProcessSpecEntry.ENTRY_NAME, 
                               filter.getEqualOperator(), entryName, false);
        filter.addSingleFilter(filter.getAndConnector(), 
                               IBpssProcessSpecEntry.ENTRY_TYPE, 
                               filter.getEqualOperator(), entryType, false);
        handler = EntityHandlerFactory.getHandlerFor(
                      BpssProcessSpecEntry.ENTITY_NAME, false);
        Collection bpssProcSpecEntryColl = handler.getEntityByFilter(filter);
        if (bpssProcSpecEntryColl.size() > 1)
        {
          return null;
        }
        else if (bpssProcSpecEntryColl.isEmpty())
        {
          return null;
        }
        else
        {
          it = bpssProcSpecEntryColl.iterator();
          bpssProcSpecEntry = (BpssProcessSpecEntry)it.next();
        }
      }
    }
    catch (Throwable th)
    {
      th.printStackTrace();
      System.exit(1);
    }
    return bpssProcSpecEntry;
  }
}