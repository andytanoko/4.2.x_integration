/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchGridNodeQueryRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.activation;

import java.util.Collection;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;

public class SearchGridNodeQueryRenderer extends AbstractRenderer
{
  private boolean _edit;

  private static final Number[] _queryFields = {
    IGTSearchGridNodeQueryEntity.SEARCH_ID,
    IGTSearchGridNodeQueryEntity.DT_SUBMITTED,
    IGTSearchGridNodeQueryEntity.DT_RESPONDED,
  };

  private static final Number[] _commonCriteriaFields = {
    IGTSearchGridNodeCriteriaEntity.CRITERIA_TYPE,
    IGTSearchGridNodeCriteriaEntity.MATCH_TYPE,
  };

  private static final Number[] _gridNodeCriteriaFields = {
    IGTSearchGridNodeCriteriaEntity.GRIDNODE_ID,
    IGTSearchGridNodeCriteriaEntity.GRIDNODE_NAME,
    IGTSearchGridNodeCriteriaEntity.CATEGORY,
  };

  private static final Number[] _whitepageCriteriaFields = {
    IGTSearchGridNodeCriteriaEntity.BUSINESS_DESC,
    IGTSearchGridNodeCriteriaEntity.DUNS,
    IGTSearchGridNodeCriteriaEntity.CONTACT_PERSON,
    IGTSearchGridNodeCriteriaEntity.EMAIL,
    IGTSearchGridNodeCriteriaEntity.WEBSITE,
    IGTSearchGridNodeCriteriaEntity.TEL,
    IGTSearchGridNodeCriteriaEntity.FAX,
    IGTSearchGridNodeCriteriaEntity.COUNTRY,
  };

  private static final Object[] _resultColumns = {
    IGTSearchedGridNodeEntity.GRIDNODE_ID,
    IGTSearchedGridNodeEntity.GRIDNODE_NAME,
    IGTSearchedGridNodeEntity.STATE,
  };

  public SearchGridNodeQueryRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTSearchGridNodeQueryEntity query = (IGTSearchGridNodeQueryEntity)getEntity();
      IGTSearchGridNodeCriteriaEntity criteria = (IGTSearchGridNodeCriteriaEntity)query.getFieldValue(IGTSearchGridNodeQueryEntity.CRITERIA);
      SearchGridNodeQueryAForm form = (SearchGridNodeQueryAForm)getActionForm();
      Short criteriaType = StaticUtils.shortValue( form.getCriteriaType() );

      renderCommonFormElements(query.getType(),_edit);

      BindingFieldPropertyRenderer bfpr = renderFields(null, query,_queryFields);
      renderFields(bfpr, criteria, _commonCriteriaFields);
      if( IGTSearchGridNodeCriteriaEntity.CRITERIA_TYPE_BY_GRIDNODE.equals(criteriaType) )
      {
        renderFields(bfpr, criteria, _gridNodeCriteriaFields);
        removeNode("byWhitepageInfo_details",false);
        renderLabel("byGridNodeInfo_label","searchGridNodeCriteria.byGridNodeInfo",false);
      }
      else if( IGTSearchGridNodeCriteriaEntity.CRITERIA_TYPE_BY_WHITEPAGE.equals(criteriaType) )
      {
        renderFields(bfpr, criteria, _whitepageCriteriaFields);
        removeNode("byGridNodeInfo_details",false);
        renderLabel("byWhitePageInfo_label","searchGridNodeCriteria.byWhitePageInfo",false);
      }
      else
      {
        //Should I be hiding this when there is no criteria?
        removeNode("matchType_details",false);
        removeNode("byGridNodeInfo_details",false);
        removeNode("byWhitepageInfo_details",false);
      }

      if(query.getFieldValue(IGTSearchGridNodeQueryEntity.DT_RESPONDED) != null)
      {
        Collection results = (Collection)query.getFieldEntities(IGTSearchGridNodeQueryEntity.RESULTS);
        if(results != null)
        {
          renderFieldElv(rContext,query,IGTSearchGridNodeQueryEntity.RESULTS,results,_resultColumns);
        }
        renderLabel("cancel","searchGridNodeQuery.view.done",false);

      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering searchGridNodeQuery screen",t);
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
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns,manager,entityType);
      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();

      listOptions.setColumnAdapter(adapter);
      listOptions.setCreateURL(null);
      listOptions.setDeleteURL(null);
      listOptions.setAllowsEdit(false);
      listOptions.setAllowsSelection(false);
      listOptions.setHeadingLabelKey(entity.getType() + "." + fieldName);
      listOptions.setViewURL("divertCreateActivationRecord");
      listOptions.setUpdateURL(null);
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                fieldName + "_details",
                                                listOptions,
                                                value);
      SearchedGridNodeListRenderer sgnlr = new SearchedGridNodeListRenderer(rContext);
      elvRenderer.setRenderer(sgnlr);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(false);
      elvRenderer.setIndexModeDefault(true);
      elvRenderer.setTableName(fieldName);
      elvRenderer.render(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Unable to render the elv for fieldId" + fieldId,t);
    }
  }

}