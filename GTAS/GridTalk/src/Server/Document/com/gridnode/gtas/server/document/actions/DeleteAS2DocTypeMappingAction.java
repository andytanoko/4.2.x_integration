/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Collection;

import com.gridnode.gtas.events.document.DeleteAS2DocTypeMappingEvent;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;


/**
 * This Action class handles the deletion of a AS2DocTypemapping.
 *
 * @author Wong Yee Wah
 *
 * @version GT 4.0.2
 * @since 4.0.2
 */

public class DeleteAS2DocTypeMappingAction extends AbstractDeleteEntityAction
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -4466962848718770698L;
  private static final String ACTION_NAME = "DeleteAS2DocTypeMappingAction";
  
  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    return ((AS2DocTypeMapping) entity).canDelete();
  }
  
  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity, Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    ActionHelper.getManager().deleteAS2DocTypeMapping(
      (Long) entity.getFieldValue(keyId));
  }
  
  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findAS2DocTypeMappingKeys(filter);
  }
  
  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.getManager().findAS2DocTypeMappingByFilter(filter);
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
    return DeleteAS2DocTypeMappingEvent.class;
  }

}
