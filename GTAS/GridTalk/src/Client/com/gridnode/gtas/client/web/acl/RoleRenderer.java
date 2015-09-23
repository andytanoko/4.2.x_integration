/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RoleRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 * 2002-07-05     Andrew Hill         Modify to use BFPR
 * 2002-08-01     Andrew Hill         Refactored
 */
package com.gridnode.gtas.client.web.acl;

import java.util.Collection;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;

public class RoleRenderer extends AbstractRenderer
{
  private boolean _edit;
  private Collection _accessRights;

  protected static final Number[] fields = new Number[] { IGTRoleEntity.ROLE,
                                                          IGTRoleEntity.DESCRIPTION,  };

  public RoleRenderer( RenderingContext rContext, boolean edit, Collection accessRights )
  {
    super(rContext);
    _edit = edit;
    _accessRights = accessRights;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTRoleEntity entity = (IGTRoleEntity)getEntity();

      // Render the Role form details
      renderCommonFormElements("role",_edit);
      renderFields(null, entity, fields );

      // Render the embedded AccessRight listview
      if(entity.isNewEntity())
      {
        removeNode("accessRights_details",false);
      }
      else
      {
        addAccessRightsElv(rContext);
        renderLabel("elv_create","role.accessRights.create",false);
        renderLabel("elv_delete","role.accessRights.delete",false);
      }
    }
    catch(Exception e)
    {
      if(e instanceof RenderingException)
        throw (RenderingException)e;
      else
        throw new RenderingException("Error rendering role screen",e);
    }
  }

  private void addAccessRightsElv(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      Number[] columns = {  IGTAccessRightEntity.DESCRIPTION,
                            IGTAccessRightEntity.FEATURE,
                            IGTAccessRightEntity.ACTION,
                            IGTAccessRightEntity.DATA_TYPE, };

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      String entityType = IGTEntity.ENTITY_ACCESS_RIGHT;
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns,manager,entityType);
      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
      if(_edit)
      {
        listOptions.setDeleteURL("divertDeleteAccessRight");
        listOptions.setCreateURL("divertUpdateAccessRight");
      }
      else
      {
        listOptions.setDeleteURL(null);
        listOptions.setCreateURL(null);
      }
      listOptions.setColumnAdapter(adapter);
      listOptions.setHeadingLabelKey("role.accessRights");
      listOptions.setUpdateURL("divertUpdateAccessRight");
      listOptions.setViewURL("divertViewAccessRight");
      listOptions.setCreateLabelKey("role.accessRights.create");
      listOptions.setDeleteLabelKey("role.accessRights.delete");
      listOptions.setAllowsEdit(_edit);
      listOptions.setAllowsSelection(_edit);
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                "accessRights_details",
                                                listOptions,
                                                _accessRights);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(false);
      elvRenderer.render(_target);
    }
    catch(Exception e)
    {
      throw new RenderingException("Unable to render the AccessRight table for the role",e);
    }
  }
}