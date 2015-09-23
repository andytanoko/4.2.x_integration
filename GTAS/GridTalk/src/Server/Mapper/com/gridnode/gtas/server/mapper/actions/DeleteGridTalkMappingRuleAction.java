/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteGridTalkMappingRuleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 03 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Extend from AbstractDeleteEntityAction
 */
package com.gridnode.gtas.server.mapper.actions;

import com.gridnode.gtas.events.mapper.DeleteGridTalkMappingRuleEvent;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerHome;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerObj;
import com.gridnode.gtas.server.mapper.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;

/**
 * This Action class handles the deletion of a GridTalkMappingRule.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteGridTalkMappingRuleAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6847774998236651086L;
	private static final String ACTION_NAME = "DeleteGridTalkMappingRuleAction";

  private IGridTalkMappingManagerObj getGTMappingManager()
    throws ServiceLookupException
  {
    return (IGridTalkMappingManagerObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IGridTalkMappingManagerHome.class.getName(),
        IGridTalkMappingManagerHome.class,
        new Object[0]);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    return ((GridTalkMappingRule) entity).canDelete();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity, Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    MappingRule mappingRule =
      (MappingRule) entity.getFieldValue(GridTalkMappingRule.MAPPING_RULE);

    getGTMappingManager().deleteGridTalkMappingRule(
      (Long) entity.getFieldValue(keyId));
    ServiceLookupHelper.getManager().deleteMappingRule(
      (Long) mappingRule.getFieldValue(MappingRule.UID));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return getGTMappingManager().findGridTalkMappingRulesKeys(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return getGTMappingManager().findGridTalkMappingRules(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getActionName()
   * @since GT 2.2 I1
   */
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getExpectedEventClass()
   * @since GT 2.2 I1
   */
  protected Class getExpectedEventClass()
  {
    return DeleteGridTalkMappingRuleEvent.class;
  }

}