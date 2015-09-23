/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGetEntityListAction2.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ??? ?? 2003    Ang Meng Hua        Created
 * Oct 16 2003    Neo Sok Lay         Extend from AbstractGetEntityListAction.
 *                                    Sort the entity list after retrieve by keys
 *                                    -- to maintain the original sort order.
 */
package com.gridnode.gtas.server.rdm.ejb.actions;

import com.gridnode.pdip.framework.db.cursor.ICursorObj;
import com.gridnode.pdip.framework.db.entity.EntityOrderComparator;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This is an abstract GetList action class that provides the the generic
 * cursor handling methods for the concrete GetList actions. The difference
 * of AbstractGetEntityListAction2 from AbstractGetEntityListAction is that
 * only the entity keys will be kept in the cursor. Only on request to getNextList
 * will the actual entity objects be retrieved. This is extremely optimized for
 * huge entity objects to reduce the amount of caching. 
 *
 * @author Ang Meng Hua
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public abstract class AbstractGetEntityListAction2 extends AbstractGetEntityListAction
{
  //private static long _lastListID;

  // *************** AbstractGridTalkAction methods ***************************


  // ************************ Own methods **************************************
  
  /**
   * Get the fieldId of the identity field of this entity.
   *
   * @return The FieldId of the key field of the entity  
   */
  protected abstract Number getEntityKeyID();  

  /**
   * Retrieves a collection of the primary keys of the entities based on a 
   * filtering condition.
   *
   * @param filter the filtering condition.
   * @return A Collection of primary keys (Long).
   */
  protected abstract Collection retrieveEntityKeys(IDataFilter filter)
    throws Exception;

  /**
   * Get the next list of entities to be returned, as specified by the
   * getEvent.
   *
   * @param getEvent The GetEntityListEvent which indicates the parameters for
   * returning the next list of entities.
   * @return The event response data containing the next list of entities.
   *
   * @exception FindEntityException Unable to retrieve the CursorBean.
   * @exception ServiceLookupException Unable to locate the CursorBean home.
   * @exception javax.ejb.CreateException Unable to create the CursorBean.
   */
  /*031016NSL
  protected EntityListResponseData getNextList(GetEntityListEvent getEvent)
    throws Exception
  {
    String listID = getEvent.getListID();
    ICursorObj cursorObj = null;
    Collection entityKeys = null;
	Collection entityList = new ArrayList();    

    if (listID != null)
    {
      cursorObj = retrieveCursor(listID);
    }
    else
    {
      // retrieve from db
      //entityList = retrieveEntityList(getEvent.getFilter());
	  entityKeys = retrieveEntityKeys(getEvent.getFilter());      

      // create cursor for empty result set?

      //new list ID
      listID = getNewListID();
      cursorObj = createNewCursor(entityKeys, listID);
    }

    int count = getEvent.getMaxRows();
    if (count == 0)
      count = cursorObj.size();

    int start = getEvent.getStartRow();
    if (start >= cursorObj.size())
      start = 0;

    entityKeys = cursorObj.get(start, count);

	IDataFilter filter = null;    
    if (entityKeys!=null && !entityKeys.isEmpty())
    {    
	  filter = new DataFilterImpl();   
	  filter.addDomainFilter(
	    null,
		getEntityKeyID(),
		entityKeys,
		false);
		
	  entityList = retrieveEntityList(filter);		
    }    
 
    return createResponseData(entityList, start,
             cursorObj.size()-start-entityList.size(),
             listID);
  } 
  */
  
  /** 
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction#getListDataForCursor(com.gridnode.pdip.framework.db.filter.IDataFilter)
   */
  protected Collection getListDataForCursor(IDataFilter filter)
    throws Exception
  {
    return retrieveEntityKeys(filter);
  }

  /** 
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction#getEntityListFromCursor(com.gridnode.pdip.framework.db.cursor.ICursorObj, int, int)
   */
  protected Collection getEntityListFromCursor(
    ICursorObj cursor,
    int start,
    int count)
    throws Exception
  {
    Collection entityKeys = cursor.get(start, count);
    ArrayList entityList = new ArrayList();
    
    IDataFilter filter = null;    
    if (entityKeys!=null && !entityKeys.isEmpty())
    {    
      filter = new DataFilterImpl();   
      filter.addDomainFilter(
        null,
        getEntityKeyID(),
        entityKeys,
        false);
    
      entityList.addAll(retrieveEntityList(filter));    
      
      //sort by entity key order to previous the original retrieve order from cursor
      Collections.sort(entityList, new EntityOrderComparator(getEntityKeyID(), entityKeys));
    }    

    return entityList;
  }

}