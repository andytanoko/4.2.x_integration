/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingRuleBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 01 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.entities.ejb;

import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * A MappingRuleBean provides persistency services for
 * MappingRule.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class MappingRuleBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1432889794878442603L;

	public String getEntityName()
  {
    return MappingRule.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity mappingRule)
    throws Exception
  {
    String mappingRuleName =
      mappingRule.getFieldValue(MappingRule.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           MappingRule.NAME,
                           filter.getEqualOperator(),
                           mappingRuleName,
                           false);

    if (getDAO().findByFilter(filter).size() > 0)
    {
      String msg = "MappingRule : "+mappingRuleName+" already exist";
      throw new DuplicateEntityException(msg);
    }
  }

}