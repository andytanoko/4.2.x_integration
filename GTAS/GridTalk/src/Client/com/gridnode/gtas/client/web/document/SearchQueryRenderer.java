/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQueryRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 * 2005-03-15     Andrew Hill         Disable edit link for non-admins
 */
package com.gridnode.gtas.client.web.document;

import java.util.Collection;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IConstraint;
import com.gridnode.gtas.client.ctrl.IGTConditionEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSearchQueryEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.Filter;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.EntityFieldNameValueRetriever;
import com.gridnode.gtas.client.web.renderers.IOptionValueRetriever;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class SearchQueryRenderer extends AbstractRenderer
{
  //private static final String PARAM_UPDATE_ACTION = "updateAction";

  protected static final Number[] fields = new Number[]{IGTSearchQueryEntity.NAME,
                                                        IGTSearchQueryEntity.DESCRIPTION,
                                                        IGTSearchQueryEntity.CREATED_BY,
                                                        IGTSearchQueryEntity.IS_PUBLIC};

  private static Number[] _conditionFields =
  {
    IGTConditionEntity.TYPE,
    IGTConditionEntity.FIELD,
    IGTConditionEntity.XPATH,
    IGTConditionEntity.OPERATOR,
    IGTConditionEntity.VALUES,
  };
  
  private boolean _edit;

  public SearchQueryRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      renderCommonFormElements(IGTEntity.ENTITY_SEARCH_QUERY, _edit);
      renderFields(null, getEntity(), fields);
      
      // render conditions using stamping of html
      renderConditions();
      
      //20050315AH - Hide edit link if we arent an admin
      RenderingContext rContext = getRenderingContext();
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      
      /* TWX re-enable so that user can edit
      if( !gtasSession.isAdmin() )
      {
        removeNode("edit_button", false);
      }*/
      //...
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering searchQuery screen", t);
    }
  }
  
  protected void renderConditions() throws Throwable
  {
    RenderingContext rContext = getRenderingContext();
    IGTSearchQueryEntity searchQuery = (IGTSearchQueryEntity)getEntity();
    SearchQueryAForm searchQueryForm = (SearchQueryAForm)rContext.getOperationContext().getActionForm();

    renderLabel("condition.type_label",         "condition.type");
    renderLabel("condition.fieldOrXpath_label", "condition.fieldOrXpath");
    renderLabel("condition.operator_label",     "condition.operator");
    renderLabel("condition.values_label",       "condition.values");

    renderLabelCarefully("removeCondition",     "condition.removeCondition",  false);
    renderLabelCarefully("addCondition",        "condition.addCondition",     false);

    Node conditionRow = getElementById("condition_row");
    Node conditionRowParent = conditionRow.getParentNode();
    conditionRowParent.removeChild(conditionRow);
    Node controlRow = getElementById("control_row");
    
    // Note: meta-info states value class as ArrayList, but event returned Vector...
    Collection conditionsCollection = (Collection)searchQuery.getFieldValue(IGTSearchQueryEntity.CONDITIONS);
    IGTConditionEntity[] conditions = (IGTConditionEntity[])conditionsCollection.toArray(new IGTConditionEntity[conditionsCollection.size()]);
    ConditionAForm[] conditionForms = searchQueryForm.getConditions();
    for(int i = 0; i < conditionForms.length; i++)
    {
      try
      {
        Node clonedRow = conditionRow.cloneNode(true);
        conditionRowParent.insertBefore(clonedRow, controlRow);
        
        // Render condition
        IGTConditionEntity condition = conditions[i];
        ConditionAForm conditionForm = conditionForms[i];

        renderFields(null, condition, _conditionFields, conditionForm, "");

        // Make id unique for each row
        String idPrefix = "conditions[" + i + "].";

        if(_edit)
        {
          Element selectCol = getElementById("selected_value");
          selectCol.setAttribute("id", idPrefix + "selected_value");
          selectCol.setAttribute("name", idPrefix + "selected");

          if(conditionForm.isSelected())
          {
            selectCol.setAttribute("checked", "checked");
          }
        }
        
        Element typeCol = getElementById("type_value");
        typeCol.setAttribute("id", idPrefix + "type_value");
        typeCol.setAttribute("name", idPrefix + "type");
        
        Element fieldCol = getElementById("field_value");
        fieldCol.setAttribute("id", idPrefix + "field_value");
        fieldCol.setAttribute("name", idPrefix + "field");
  
        Element xpathCol = getElementById("xpath_value");
        xpathCol.setAttribute("id", idPrefix + "xpath_value");
        xpathCol.setAttribute("name", idPrefix + "xpath");
  
        Element operatorCol = getElementById("operator_value");
        operatorCol.setAttribute("id", idPrefix + "operator_value");
        operatorCol.setAttribute("name", idPrefix + "operator");
  
        Element valuesCol = getElementById("values_value");
        valuesCol.setAttribute("id", idPrefix + "values_value");
        valuesCol.setAttribute("name", idPrefix + "values");
       
        if(IGTConditionEntity.TYPE_GDOC.equals(conditionForm.getTypeAsShort()))
        {
          removeNode(xpathCol, false);
          
          // Special rendering for field 
          renderField(fieldCol, conditionForm.getField());
        }
        else
        {
          removeNode(fieldCol, false);
        }
      }
      catch(Throwable t)
      {
        throw new RenderingException("Error rendering condition, i=" + i, t);
      }
    }
  }

  private void renderField(Element fieldElement, String value) throws GTClientException
  {
    RenderingContext rContext = getRenderingContext();
    IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());

    IOptionValueRetriever fieldRetriever = new EntityFieldNameValueRetriever(
                                                          rContext.getResourceLookup(),
                                                          EntityFieldNameValueRetriever.SUBMIT_ID,
                                                          IGTEntity.ENTITY_GRID_DOCUMENT);
                                                                
    IFilter fieldFilter = new Filter()
    {
      public boolean allows(Object object, Object context) throws GTClientException
      {
        IGTFieldMetaInfo fmi = (IGTFieldMetaInfo)object;
        boolean allowedConstraint = true;
        switch(fmi.getConstraintType())
        {
          case IConstraint.TYPE_DYNAMIC_ENTITY:
          case IConstraint.TYPE_LOCAL_ENTITY:
          case IConstraint.TYPE_OTHER:
          case IConstraint.TYPE_UID:
            allowedConstraint = false;
        }         
        return allowedConstraint;
      }
    };                                                      
                                                              
    Collection gdocFields = EntityFieldNameValueRetriever.buildFmiCollection(
                                                          gtasSession,
                                                          IGTEntity.ENTITY_GRID_DOCUMENT,
                                                          false,
                                                          fieldFilter,
                                                          this);

    if(_edit)
    {
      // Render to a combo box
      renderSelectOptions(fieldElement, gdocFields, fieldRetriever, true, null);
      renderSelectedOptions(fieldElement, value);
    }
    else
    {
      // Render to a read-only text field 
      IGTManager manager = gtasSession.getManager(IGTEntity.ENTITY_GRID_DOCUMENT);
      IGTFieldMetaInfo fmi = manager.getSharedFieldMetaInfo(IGTEntity.ENTITY_GRID_DOCUMENT, new Integer(value));
      replaceText(fieldElement, fieldRetriever.getOptionText(fmi));
    }
  }
}