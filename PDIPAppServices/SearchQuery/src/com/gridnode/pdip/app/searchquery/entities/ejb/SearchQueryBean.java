/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQueryBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.searchquery.entities.ejb;

import com.gridnode.pdip.app.searchquery.model.SearchQuery;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * A SearchQueryBean provides persistency services for SearchQuery.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class SearchQueryBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4263664319151336588L;

	public String getEntityName()
  {
    return SearchQuery.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity searchQuery)
    throws Exception
  {
    String queryName = searchQuery.getFieldValue(SearchQuery.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SearchQuery.NAME, filter.getEqualOperator(),
      queryName, false);

    if (getDAO().findByFilter(filter).size() > 0)
    {
      String msg = "SearchQuery : "+queryName+" already exist";
      throw new DuplicateEntityException(msg);
    }
  }

}