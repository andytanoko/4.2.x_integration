/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchRegistryQueryRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 * 2003-09-28     Daniel D'Cotta      Modified to use FakeEnumeration instead of
 *                                    OptionSource which after two days of trying
 *                                    still refused to work
 * 2003-10-03     Neo Sok Lay         Display UUID in results column. Re-order
 *                                    results column fields.
 */
package com.gridnode.gtas.client.web.be;

import java.util.Collection;

import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.FakeEnumeratedConstraint;
import com.gridnode.gtas.client.web.strutsbase.FakeForeignEntityConstraint;

public class SearchRegistryQueryRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected final static Number[] _queryFields = {
    IGTSearchRegistryQueryEntity.SEARCH_ID,
    IGTSearchRegistryQueryEntity.DT_SUBMITTED,
    IGTSearchRegistryQueryEntity.DT_RESPONDED,
    IGTSearchRegistryQueryEntity.EXCEPTION_STR,
  };

  protected final static Number[] _criteriaFields = {
    IGTSearchRegistryCriteriaEntity.BUS_ENTITY_DESC,
    IGTSearchRegistryCriteriaEntity.DUNS,
    //IGTSearchRegistryCriteriaEntity.MESSAGING_STANDARDS, // render separately
    IGTSearchRegistryCriteriaEntity.MATCH,
    //IGTSearchRegistryCriteriaEntity.QUERY_URL, // render separately
  };

  private final static Object[] _resultColumns = {
    IGTSearchedBusinessEntityEntity.UUID,
    IGTSearchedBusinessEntityEntity.BE_STATE,
    IGTSearchedBusinessEntityEntity.ID,
    IGTSearchedBusinessEntityEntity.ENTERPRISE_ID,
    IGTSearchedBusinessEntityEntity.DESCRIPTION,
  };

  public SearchRegistryQueryRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTSearchRegistryQueryEntity query = (IGTSearchRegistryQueryEntity)getEntity();
      IGTSearchRegistryCriteriaEntity criteria = (IGTSearchRegistryCriteriaEntity)query.getFieldValue(IGTSearchRegistryQueryEntity.CRITERIA);
      //SearchRegistryQueryAForm form = (SearchRegistryQueryAForm)getActionForm();
      ActionErrors errors = rContext.getActionErrors();

      renderCommonFormElements(query.getType(),_edit);

      BindingFieldPropertyRenderer bfpr = renderFields(null, query,_queryFields);

      if(query.getFieldValue(IGTSearchRegistryQueryEntity.DT_RESPONDED) != null)
      {
        Collection results = (Collection)query.getFieldEntities(IGTSearchRegistryQueryEntity.RESULTS);
        if(results != null)
        {
          renderFieldElv(rContext, query, IGTSearchRegistryQueryEntity.RESULTS, results, _resultColumns);
        }
      }
      
      // Render embeded entity
      renderLabel("criteria_heading", "searchRegistryQuery.criteria.heading", false);
      bfpr = renderFields(bfpr, criteria, _criteriaFields);

      // Render MessagingStandards' options from event
      bfpr.reset();
      bfpr.setBindings(criteria, IGTSearchRegistryCriteriaEntity.MESSAGING_STANDARDS, errors);
//      bfpr.setOptionSource(this);
      IGTSearchRegistryQueryManager queryMgr = (IGTSearchRegistryQueryManager)query.getSession().getManager(IGTManager.MANAGER_SEARCH_REGISTRY_QUERY);
      String[] _messagingStandardsValues = StaticUtils.getStringArray(queryMgr.getMessagingStandards());
      IEnumeratedConstraint _messagingStandardsConstraint 
        = new FakeEnumeratedConstraint(_messagingStandardsValues, _messagingStandardsValues, false); // this is user input and thus does not need to be i18n
      bfpr.setConstraint(_messagingStandardsConstraint);
      bfpr.render(_target);


      // Render QueryUrl as a FakeForeignEntity instead of a string when editing
      bfpr.reset();
      bfpr.setBindings(criteria, IGTSearchRegistryCriteriaEntity.QUERY_URL, errors);
      if(_edit)
      { 
        IForeignEntityConstraint _queryUrlConstraint 
          = new FakeForeignEntityConstraint(IGTEntity.ENTITY_REGISTRY_CONNECT_INFO,
                                            "queryUrl",
                                            "queryUrl");
        bfpr.setConstraint(_queryUrlConstraint);
      }
      bfpr.render(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering searchRegistryQuery screen", t);
    }
  }
  
  private void renderFieldElv(RenderingContext rContext,
                              IGTEntity entity,
                              Number fieldId,
                              Collection value,
                              Object[] columns)
    throws RenderingException
  {
    try
    {
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(fieldId);
      String fieldName = fmi.getFieldName();
      IEntityConstraint constraint = (IEntityConstraint)fmi.getConstraint();
      String entityType = constraint.getEntityType();
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns, manager, entityType);

      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
      listOptions.setColumnAdapter(adapter);
      listOptions.setCreateURL(null);
      listOptions.setDeleteURL(null);
      listOptions.setAllowsEdit(false);
      listOptions.setAllowsSelection(false);
      listOptions.setHeadingLabelKey(entity.getType() + "." + fieldName);
      listOptions.setViewURL("divertViewSearchedBusinessEntity");
      listOptions.setUpdateURL(null);
      
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                fieldName + "_details",
                                                listOptions,
                                                value);
      SearchedBusinessEntityListRenderer sbelr = new SearchedBusinessEntityListRenderer(rContext);
      elvRenderer.setRenderer(sbelr);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(false);
      elvRenderer.setIndexModeDefault(true);
      elvRenderer.setTableName(fieldName);
      elvRenderer.render(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Unable to render the elv for fieldId" + fieldId, t);
    }
  }
}