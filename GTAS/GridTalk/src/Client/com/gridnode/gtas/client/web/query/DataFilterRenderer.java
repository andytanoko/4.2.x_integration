/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.query;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;
import org.w3c.dom.*;
import java.util.*;

import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.web.*;
import com.gridnode.gtas.client.*;

public class DataFilterRenderer extends AbstractRenderer
{
  //This is used as the colspan for a datafilter tables control row
  private static String VALUE_FILTER_COLUMNS = "5";
  
  //Limit the size of text entry fields for appearances sake
  private static int MAX_FIELD_CHARS = 25;
  
  //Urls of icons
  private static String ADD_VALUE_FILTER_ICON = "images/actions/filter.gif";
  private static String REMOVE_VALUE_FILTERS_ICON = "images/actions/delete.gif";
  
  //Key under which we cache collection of fieldMetaInfo for gdoc fields in opCon
  private static final String GDOC_FIELDS_KEY = "GDOC_FIELDS_COLLECTION"; 
    
  private boolean _edit;
  private String _fieldName;
  private String _entityType;
  private IGTManager _manager;
  private IGTSession _gtasSession;
  private DateFieldRenderer _dfr;
  
  public DataFilterRenderer(RenderingContext rContext,
                            boolean edit,
                            String fieldName,
                            String entityType)
  {
    super(rContext);
    _edit = edit;
    if (fieldName == null)
      throw new NullPointerException("fieldName is null");
    _fieldName = fieldName;
    if (entityType == null)
      throw new NullPointerException("entityType is null");
    _entityType = entityType;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      Element filterNode = getElementById( _fieldName + "_value",false);
      if(filterNode != null)
      {
        includeJavaScript(IGlobals.JS_DATA_FILTER_UTILS);
        includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS); //yeh right - like its not already ;->
        
        removeAllChildren(filterNode);
        _dfr = new DateFieldRenderer(rContext, _edit);
        _gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
        _manager = _gtasSession.getManager(_entityType);
        if (_manager == null)
          throw new NullPointerException("_manager is null");        
        Element filterTable = _target.createElement("table");
        filterTable.setAttribute("border","0");
        filterNode.appendChild(filterTable);       
        DataFilterAForm filterForm = getFilterForm();
        renderDataFilterErrors(rContext, filterTable);
        renderValueFilters(rContext, filterForm, filterTable);
        Element filterControlRow = getDataFilterControlRow(rContext, filterTable);
        filterTable.appendChild(filterControlRow);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering DataFilter",t);
    }
  }
  
  private void renderDataFilterErrors(RenderingContext rContext, Element filterTable)
    throws RenderingException
  {
    try
    {
      ActionError error = MessageUtils.getFirstError(rContext.getActionErrors(), _fieldName);
      if(error != null)
      {
        Element errorRow = _target.createElement("tr");
        Element errorCell = _target.createElement("td");
        errorRow.appendChild(errorCell);
        errorCell.setAttribute("colspan", VALUE_FILTER_COLUMNS);
        errorCell.setAttribute("class", "errortext");
        errorCell.setAttribute("nowrap", "nowrap");
        String errorMsg = rContext.getResourceLookup().getMessage(error.getKey());
        replaceText(errorCell, errorMsg);
        filterTable.appendChild(errorRow);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering data filter errors",t);
    }
  }
  
  private void renderValueFilters(RenderingContext rContext,
                                  DataFilterAForm filterForm,
                                  Element filterTable)
    throws RenderingException
  {
    try
    {
      Object[] valueFilterForms = filterForm.getValueFilters();
      if(valueFilterForms != null)
      {
        for(int i=0; i < valueFilterForms.length; i++)
        {
          ValueFilterAForm valueFilterForm = (ValueFilterAForm)valueFilterForms[i];
          if (valueFilterForm == null)
            throw new NullPointerException("valueFilterForm is null");
          Element filterRow = _target.createElement("tr");
          filterTable.appendChild(filterRow);
          renderValueFilter(rContext, valueFilterForm, filterRow, i);
        }
      }
      else
      {
        throw new NullPointerException("No filterforms");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering ValueFilters",t);
    }
  }
  
  private void renderValueFilter( RenderingContext rContext,
                                  ValueFilterAForm filterForm, 
                                  Element filterRow,
                                  int rowIndex)
    throws RenderingException
  {
    try
    {
      Element selectorCell = getSelectorCell(rContext, filterForm, rowIndex);
      filterRow.appendChild(selectorCell);
      
      Element connectorCell = getConnectorCell(rContext, filterForm, rowIndex);
      filterRow.appendChild(connectorCell);
            
      Element fieldCell = getFieldCell(rContext, filterForm, rowIndex);
      filterRow.appendChild(fieldCell);
      
      Element operatorCell = getOperatorCell(rContext, filterForm, rowIndex);
      filterRow.appendChild(operatorCell);
      
      Element paramsCell = getParamsCell(rContext, filterForm, rowIndex);
      filterRow.appendChild(paramsCell);  
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error rendering ValueFilter details",t); 
    }
  }
  
  private Element getFieldCell(RenderingContext rContext, ValueFilterAForm valueFilterForm, int rowIndex)
    throws RenderingException
  {
    try
    {
      Element fieldCell = makeCell();
      fieldCell.removeAttribute("nowrap"); //Allow extra params to go to new line if not enough space
      Element fieldSelect = _target.createElement("select");
      String name = _fieldName + "." + "valueFilters[" + rowIndex + "].field";
      fieldSelect.setAttribute("name",name);
      fieldSelect.setAttribute("onchange","serverRefresh();");
      fieldCell.appendChild(fieldSelect);
      
      
      IOptionValueRetriever fieldRetriever = new EntityFieldNameValueRetriever(
                                                            rContext.getResourceLookup(),
                                                            EntityFieldNameValueRetriever.SUBMIT_ID,
                                                            IGTEntity.ENTITY_GRID_DOCUMENT);
            
      OperationContext opCon = rContext.getOperationContext();
      if (opCon == null)
        throw new NullPointerException("opCon is null");
      Collection gdocFields = (Collection)opCon.getAttribute(GDOC_FIELDS_KEY);
      if(gdocFields == null)
      {        
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
                                                              
        gdocFields = EntityFieldNameValueRetriever.buildFmiCollection(_gtasSession,
                                                             IGTEntity.ENTITY_GRID_DOCUMENT,
                                                             false,
                                                             fieldFilter,
                                                             this);
        opCon.setAttribute(GDOC_FIELDS_KEY, gdocFields);
      }
      
      if( StaticUtils.stringEmpty( valueFilterForm.getField() ) )
      { // If we havent selected a field yet, then autoselect the first field
        // as other parts of the renderer that will come later will need it
        // (such as rendering the params).
        IGTFieldMetaInfo firstField = (IGTFieldMetaInfo)StaticUtils.getFirst(gdocFields);
        if(firstField != null)
        {
          String fieldIdString = fieldRetriever.getOptionValue(firstField);
          valueFilterForm.setField(fieldIdString);
        }
      }
                                                             
      renderSelectOptions(fieldSelect,gdocFields,fieldRetriever,true,null);
      renderSelectedOptions(fieldSelect, valueFilterForm.getField() );
      
      highlightAnyErrors(rContext, fieldSelect, name);
      return fieldCell;      
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating field cell",t);
    }
  }
  
  private Element getConnectorCell(RenderingContext rContext, ValueFilterAForm valueFilterForm, int rowIndex)
    throws RenderingException
  {
    try
    {
      Element connectorCell = makeCell();
      if(rowIndex == 0)
      {
        Element br = _target.createElement("br");
        connectorCell.appendChild(br);
      }
      else
      {
        Element connectorSelect = _target.createElement("select");
        String name = _fieldName + "." + "valueFilters[" + rowIndex + "].connector";
        connectorSelect.setAttribute("name",name);
        connectorCell.appendChild(connectorSelect);
        
        if( StaticUtils.stringEmpty(valueFilterForm.getConnector()) )
        { //If no connector selected yet then explicitly set the first one into form
          String connector = QueryUtils._connectorConstraint.getValue(0);
          valueFilterForm.setConnector(connector);
        }
        
        renderSelectOptions(connectorSelect,QueryUtils._connectorConstraint,true,null);
        renderSelectedOptions(connectorSelect, valueFilterForm.getConnector() );
        highlightAnyErrors(rContext, connectorSelect, name);
      }
      return connectorCell;      
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating connector cell",t);
    }
  }
  
  private Element getOperatorCell(RenderingContext rContext, ValueFilterAForm valueFilterForm, int rowIndex)
    throws RenderingException
  {
    try
    {
      boolean isBinary = false;
      Number fieldId = StaticUtils.integerValue(valueFilterForm.getField());
      if(fieldId != null)
      {
        IGTFieldMetaInfo fmi = _manager.getSharedFieldMetaInfo(_entityType, fieldId);
        isBinary = ("java.lang.Boolean".equals(fmi.getValueClass()));
        if( isBinary && valueFilterForm.getOperator().equals(QueryUtils.OPERATOR_BETWEEN) )
        { //This is to avoid having two param fields when switch from a non binary
          //field to one thats binary and the operator was between
          valueFilterForm.setOperator(null);
        }
        int constraintType = fmi.getConstraintType();
        switch(constraintType)
        {
          case IConstraint.TYPE_FOREIGN_ENTITY:
            isBinary = true;
            break;
          
          case IConstraint.TYPE_LOCAL_ENTITY:
          case IConstraint.TYPE_DYNAMIC_ENTITY:
          case IConstraint.TYPE_OTHER:
            throw new IllegalStateException("Bad constraint type:" + constraintType);
        }       
      }
            
      IEnumeratedConstraint operatorConstraint
        = isBinary ? QueryUtils._binaryOperatorConstraint : QueryUtils._stdOperatorConstraint; 
      
      Element operatorCell = makeCell();
      Element operatorSelect = _target.createElement("select");
      String name = _fieldName + "." + "valueFilters[" + rowIndex + "].operator";
      operatorSelect.setAttribute("name",name);
      if(!isBinary)
      {
        operatorSelect.setAttribute("onchange","serverRefresh();");
      }
      operatorCell.appendChild(operatorSelect);
      
      String operator = valueFilterForm.getOperator();
      if( StaticUtils.stringEmpty(operator) 
          || "".equals(operatorConstraint.getLabel(operator)) )
      { //Explicitly set first operator in form if none selected (ie: in a new criteria row)
        //or if the one selected is no longer valid (ie: we switched the field and the new
        //field uses different set of operators)
        operator = operatorConstraint.getValue(0);
        valueFilterForm.setOperator(operator);
      }
      
      renderSelectOptions(operatorSelect,operatorConstraint,true,null);
      renderSelectedOptions(operatorSelect, valueFilterForm.getOperator() );
      highlightAnyErrors(rContext, operatorSelect, name);
      return operatorCell;      
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating operator cell",t);
    }
  }
  
  private Element getParamsCell(RenderingContext rContext, ValueFilterAForm valueFilterForm, int rowIndex)
    throws RenderingException
  {
    try
    {
      int numParams = 1;      
      if(QueryUtils.OPERATOR_BETWEEN.equals(valueFilterForm.getOperator()))
      {
        numParams = 2;
      }
      String[] params = valueFilterForm.getParams();
      //Ensure the number of elements in tha params array matches what it should
      //be for this operator type
      params = StaticUtils.reDim(params, numParams);
      valueFilterForm.setParams(params);
 
      Element paramsCell = makeCell();
      Element paramFieldStamp = _target.createElement("input");
      String name = _fieldName + "." + "valueFilters[" + rowIndex + "].params";
      paramFieldStamp.setAttribute("name",name);
      
      for(int i=0; i < numParams; i++)
      {
        Element paramField = (Element)getParamFieldElement(rContext, valueFilterForm, rowIndex, i);
        paramsCell.appendChild(paramField);
      }
      return paramsCell;      
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating params cell",t);
    }
  }
  
  private Element getParamFieldElement( RenderingContext rContext,
                                        ValueFilterAForm valueFilterForm,
                                        int rowIndex,
                                        int paramIndex)
    throws RenderingException
  {
    try
    {
      Number fieldId = StaticUtils.integerValue(valueFilterForm.getField());
      if(fieldId != null)
      {
        IGTFieldMetaInfo fmi = _manager.getSharedFieldMetaInfo(_entityType, fieldId);
        if (fmi == null)
          throw new NullPointerException("fmi is null");
        int constraintType = fmi.getConstraintType();
        String[] params = valueFilterForm.getParams();
        if(params == null) params = StaticUtils.EMPTY_STRING_ARRAY;
        String name = _fieldName + "." + "valueFilters[" + rowIndex + "].params";
        String value = params[paramIndex]; //Dont check bounds as caller must ensure element exists
        try
        {
          Element field = null;
          
          switch(constraintType)
          {
            
            case IConstraint.TYPE_TEXT:
            {
              ITextConstraint constraint = (ITextConstraint)fmi.getConstraint();
              field = _target.createElement("input");
              field.setAttribute("name",name);
              field.setAttribute("type","text");
              field.setAttribute("value",value);
              int length = constraint.getMaxLength();
              if(length > 0)
              {
                if(length > MAX_FIELD_CHARS) length = MAX_FIELD_CHARS; 
                field.setAttribute("size","" + length);
                field.setAttribute("maxlength","" + constraint.getMaxLength());
              }
            }
            break;
            
            case IConstraint.TYPE_RANGE:
            case IConstraint.TYPE_FILE:
            case IConstraint.TYPE_UID:
            {
              field = _target.createElement("input");
              field.setAttribute("name",name);
              field.setAttribute("type","text");
              field.setAttribute("value",value);
              field.setAttribute("size","" + MAX_FIELD_CHARS);
            }
            break;
            
            case IConstraint.TYPE_ENUMERATED:
            {
              IEnumeratedConstraint constraint = (IEnumeratedConstraint)fmi.getConstraint();
              field = _target.createElement("select");
              field.setAttribute("name",name);
              field.setAttribute("size","1");
              renderSelectOptions(field, constraint, true, "generic.empty");
              renderSelectedOptions(field, value);
            }
            break;
            
            case IConstraint.TYPE_FOREIGN_ENTITY:
            {
              IForeignEntityConstraint constraint = (IForeignEntityConstraint)fmi.getConstraint();
              field = _target.createElement("select");
              field.setAttribute("name",name);
              field.setAttribute("size","1");
              IForeignEntityConstraint foreignConstraint = (IForeignEntityConstraint)constraint;
              String foreignType = foreignConstraint.getEntityType();
              IGTManager manager = _gtasSession.getManager(foreignType);  
              Collection foreignEntities = manager.getAll();           
              String valueFieldName = foreignConstraint.getKeyFieldName();
              Number valueFieldId = manager.getFieldId(foreignType,valueFieldName);
              String textFieldName = foreignConstraint.getDisplayFieldName();
              Number textFieldId = manager.getFieldId(foreignType,textFieldName);
              EntityOptionValueRetriever retriever = new EntityOptionValueRetriever(textFieldId,valueFieldId);
              String[] additionalFields = foreignConstraint.getAdditionalDisplayFieldNames();
              retriever.setAdditionalDisplayFieldNames( additionalFields );
              retriever.setMaxDisplayLength(MAX_FIELD_CHARS);
              renderSelectOptions(field,foreignEntities,retriever,true,"generic.empty");
              renderSelectedOptions(field,value);
            }
            break;
            
            case IConstraint.TYPE_TIME:
            {
              ITimeConstraint constraint = (ITimeConstraint)fmi.getConstraint();
              Locale locale = StaticWebUtils.getLocale(rContext.getRequest());
              TimeZone timeZone = StaticWebUtils.getTimeZone(rContext.getRequest()); 
              field = _target.createElement("div");
              _dfr.reset();
              _dfr.setConstraint(constraint);
              _dfr.setLocale(locale);
              _dfr.setTimeZone(timeZone);
              _dfr.setInsertElement(field);
              _dfr.setFieldName(name);
              _dfr.setParamIndex(paramIndex);
              _dfr.setValue(value);
              _dfr.render(_target);
            }
            break;
            
            case IConstraint.TYPE_LOCAL_ENTITY:
            case IConstraint.TYPE_DYNAMIC_ENTITY:
            case IConstraint.TYPE_OTHER:
            default:
            {
              throw new IllegalStateException("Bad constraint type:" + constraintType);
            }
          }
          if (field == null)
            throw new NullPointerException("field is null");
          highlightAnyErrors(rContext, field, name + "[" + paramIndex + "]");
          return field;  
        }
        catch(Throwable t)
        {
          throw new RenderingException("Error getting a param field element for field "
          + fieldId
          + " ("
          + fmi.getFieldName()
          + ") "
          + " with constraint type "
          + fmi.getConstraintType(),t);
        }
      }
      else
      {
        return _target.createElement("br");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error getting paramFieldElement",t);
    }
  }
  
  
  private Element getSelectorCell( RenderingContext rContext,
                                      ValueFilterAForm filterForm,
                                      int rowIndex)
    throws RenderingException
  {
    try
    {
      Element selectorCell = makeCell();
      Element input = _target.createElement("input");
      String name = _fieldName + "." + "valueFilters[" + rowIndex + "].selected";
      input.setAttribute("type","checkbox");
      input.setAttribute("value","true");
      input.setAttribute("class","checkbox");
      input.setAttribute("name",name);
      if(filterForm.isSelected())
      {
        input.setAttribute("checked", "checked");
      }
      selectorCell.appendChild(input);
      return selectorCell;    
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating selector cell",t);
    }
  }
  
  private Element getDataFilterControlRow( RenderingContext rContext,
                                              Element filterTable)
  throws RenderingException
  {
    try
    {
      Element controlRow = _target.createElement("tr");
      Element controlCell = makeCell();
      controlCell.setAttribute("colspan",VALUE_FILTER_COLUMNS);
      controlCell.setAttribute("align","right");
      {
        String name = _fieldName + "_removeValueFilters";
        String action = "doFilterUpdateAction('" 
                        + _fieldName + "','" 
                        + QueryUtils.UPDATE_ACTION_REMOVE_VALUE_FILTERS
                        +"');";
        Element removeValueFiltersButton = createButtonLink(name,
                                                        "dataFilter.removeValueFilters",
                                                        REMOVE_VALUE_FILTERS_ICON,
                                                        action,
                                                        true);
        controlCell.appendChild(removeValueFiltersButton);
      }
      { 
        String name = _fieldName + "_addValueFilter";
        String action = "doFilterUpdateAction('" 
                        + _fieldName + "','" 
                        + QueryUtils.UPDATE_ACTION_ADD_VALUE_FILTER
                        +"');";
        Element addValueFilterButton = createButtonLink(name,
                                                        "dataFilter.addValueFilter",
                                                        ADD_VALUE_FILTER_ICON,
                                                        action,
                                                        true);
        controlCell.appendChild(addValueFilterButton);
      }
      Element updateActionInput = _target.createElement("input");
      updateActionInput.setAttribute("type","hidden");
      updateActionInput.setAttribute("name", _fieldName + ".updateAction");
      updateActionInput.setAttribute("value","");
      controlCell.appendChild(updateActionInput);
      
      controlRow.appendChild(controlCell);
      return controlRow;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating filter control row",t);
    }
  }
  
  private DataFilterAForm getFilterForm()
    throws RenderingException
  {
    try
    {
      ActionForm form = getActionForm();
      if (form == null)
        throw new NullPointerException("form is null");
      Object filterForm = PropertyUtils.getSimpleProperty(form, _fieldName);
      if (filterForm == null)
        throw new NullPointerException("filterForm in field " + _fieldName + " is null");
      if(filterForm instanceof DataFilterAForm)
      {
        return (DataFilterAForm)filterForm;
      }
      else
      {
        String valueType = StaticUtils.getObjectClassName(filterForm);
        throw new IllegalStateException("Expecting DataFilterAForm for property"
                                        + _fieldName
                                        + " of ActionForm but found "
                                        + valueType); 
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error getting filterForm from actionForm",t);
    }
  }
  
  private Element makeCell()
    throws RenderingException
  {
    try
    {
      Element td = _target.createElement("td");
      td.setAttribute("nowrap","nowrap");
      td.setAttribute("valign","top");
      return td;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating table cell",t);
    }
  }
  
  private boolean highlightAnyErrors(RenderingContext rContext, Element field, String name)
    throws RenderingException
  {
    try
    {
      ActionError error = MessageUtils.getFirstError(rContext.getActionErrors(), name);
      if(error != null)
      {
        field.setAttribute("class","badfield");
        return true;
      }
      else
      {
        return false;
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error in highlightAnyFields() where name=" + name,t);
    }
  }
}