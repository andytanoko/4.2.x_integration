/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 04 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.mapper.entities.ejb;

import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * A GridTalkMappingRuleBean provides persistency services for
 * GridTalkMappingRule.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GridTalkMappingRuleBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3170618837186851251L;

	public String getEntityName()
  {
    return GridTalkMappingRule.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity gridTalkMappingRule)
    throws Exception
  {
    String gridTalkMappingRuleName =
      gridTalkMappingRule.getFieldValue(GridTalkMappingRule.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           GridTalkMappingRule.NAME,
                           filter.getEqualOperator(),
                           gridTalkMappingRuleName,
                           false);

    if (getDAO().findByFilter(filter).size() > 0)
    {
      String msg = "GridTalkMappingRule : "+gridTalkMappingRuleName+" already exist";
      throw new DuplicateEntityException(msg);
    }
  }

}