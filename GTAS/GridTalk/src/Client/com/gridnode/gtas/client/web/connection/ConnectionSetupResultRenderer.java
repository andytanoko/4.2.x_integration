/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupResultRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-02     Andrew Hill         Created
 * 2002-11-14     Andrew Hill         Security Password Support
 * 2003-03-31     Andrew Hill         Render button labels carefully
 * 2003-11-05     Andrew Hill         noSecurity hack support for GNDB00016109
 */
package com.gridnode.gtas.client.web.connection;

import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.ctrl.IEntityConstraint;
import com.gridnode.gtas.client.ctrl.IGTConnectionSetupParamEntity;
import com.gridnode.gtas.client.ctrl.IGTConnectionSetupResultEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTGridNodeEntity;
import com.gridnode.gtas.client.ctrl.IGTJmsRouterEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.ColumnEntityAdapter;
import com.gridnode.gtas.client.web.renderers.ElvRenderer;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;

public class ConnectionSetupResultRenderer extends AbstractRenderer
{
  private boolean _edit;

  private static final Object[] _gridMasterColumns = {
    IGTGridNodeEntity.NAME,
    IGTGridNodeEntity.ID,
  };

  private static final Object[] _routerColumns = {
    IGTJmsRouterEntity.NAME,
    IGTJmsRouterEntity.IP_ADDRESS,
  };

  private static final Number[] _commonFields = {
    IGTConnectionSetupResultEntity.STATUS,
    IGTConnectionSetupResultEntity.FAILURE_REASON,
  };

  private static final Number[] _startFields = {
    IGTConnectionSetupParamEntity.CURRENT_LOCATION,
    IGTConnectionSetupParamEntity.SERVICING_ROUTER,
    /*IGTConnectionSetupParamEntity.ORIGINAL_LOCATION,
    IGTConnectionSetupParamEntity.ORIGINAL_SERVICING_ROUTER,*/
    IGTConnectionSetupParamEntity.SECURITY_PASSWORD,
  };
  private static final Number[] _endFields = {
    IGTConnectionSetupResultEntity.STATUS,
    IGTConnectionSetupResultEntity.FAILURE_REASON,
  };

  public ConnectionSetupResultRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      ConnectionSetupResultAForm form = (ConnectionSetupResultAForm)getActionForm();
      IGTConnectionSetupResultEntity result = (IGTConnectionSetupResultEntity)getEntity();
      IGTConnectionSetupParamEntity param = (IGTConnectionSetupParamEntity)result.getFieldValue(IGTConnectionSetupResultEntity.SETUP_PARAMETERS);
      renderCommonFormElements(result.getType(),_edit);

      Short status = form.getStatusShort();

      BindingFieldPropertyRenderer bfpr = renderFields(null,result,_commonFields);
      if(!IGTConnectionSetupResultEntity.STATUS_FAILURE.equals(status))
      {
        removeNode("failureReason_details",false);
      }

      if( IGTConnectionSetupResultEntity.STATUS_FAILURE.equals(status) || IGTConnectionSetupResultEntity.STATUS_NOT_DONE.equals(status) )
      {
        removeNode("done_section",false);
        renderFields(bfpr,param,_startFields);
        renderLabelCarefully("reset_link","connectionSetupParam.reset",false); //20030331AH
        renderLabelCarefully("processing_request_message","generic.communicatingWithServer.long",false); //20030331AH
        removeIdAttribute("processing_request_message",false); //So CCR cant find it ;-)
      }
      else if(IGTConnectionSetupResultEntity.STATUS_SUCCESS.equals(status) )
      {
        removeNode("notDone_section",false);
        renderFields(bfpr,result,_endFields);

        OperationContext opCon = rContext.getOperationContext();
        Collection availableGridMasters = (Collection)opCon.getAttribute(ConnectionSetupResultDispatchAction.GRIDMASTER_LIST);
        Collection availableRouters = (Collection)opCon.getAttribute(ConnectionSetupResultDispatchAction.ROUTER_LIST);

        renderFieldElv( rContext,
                        result,
                        form,
                        IGTConnectionSetupResultEntity.AVAILABLE_GRIDMASTERS,
                        availableGridMasters,
                        _gridMasterColumns);

        renderFieldElv( rContext,
                        result,
                        form,
                        IGTConnectionSetupResultEntity.AVAILABLE_ROUTERS,
                        availableRouters,
                        _routerColumns);

        renderLabelCarefully("ok","connectionSetupResult.edit.complete",false); //2003031AH
      }
      else
      {
        throw new java.lang.IllegalStateException("Illegal value for status:" + form.getStatus());
      }
      
      if( result.getSession().isNoSecurity() )
      { //20031105AH
        removeNode("securityPassword_details",false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering connectionSetupResult screen",t);
    }
  }

  private void renderFieldElv(RenderingContext rContext,
                              IGTEntity entity,
                              ActionForm form,
                              Number fieldId,
                              Collection value,
                              Object[] columns)
    throws RenderingException
  { //@todo: abstract even further and move to AbstractRenderer
    try
    {
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(fieldId);
      String fieldName = fmi.getFieldName();
      IEntityConstraint constraint = (IEntityConstraint)fmi.getConstraint();
      String entityType = constraint.getEntityType();
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns,manager,entityType);
      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
      String order = null;
      try
      {
        order = (String)PropertyUtils.getSimpleProperty(form,fieldName + "Order");
        if(order == null) order = "";
      }
      catch(Throwable t)
      {
        throw new RenderingException("Unable to get value for property '"
                                      + fieldName + "Order' from form",t);
      }
      listOptions.setColumnAdapter(adapter);
      listOptions.setCreateURL(null);
      listOptions.setDeleteURL(null);
      listOptions.setAllowsEdit(_edit);
      listOptions.setAllowsSelection(_edit);
      listOptions.setHeadingLabelKey(entity.getType() + "." + fieldName);
      listOptions.setViewURL(null);
      listOptions.setUpdateURL(null);
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                fieldName + "_details",
                                                listOptions,
                                                value);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(_edit);
      elvRenderer.setTableName(fieldName);
      elvRenderer.setOrder(  StaticUtils.explode( order, ",") );
      elvRenderer.render(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Unable to render the elv for fieldId" + fieldId,t);
    }
  }
}

