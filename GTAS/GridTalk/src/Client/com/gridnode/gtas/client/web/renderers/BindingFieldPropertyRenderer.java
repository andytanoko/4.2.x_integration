/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-26     Andrew Hill         Created
 * 2002-0?-??+    Andrew Hill         Added another 99.9% of the functionality..... ;-)
 * 2002-09-03     Andrew Hill         SetBindings no longer calls reset()
 * 2002-??-??     Andrew Hill         Various tweaks to code
 * 2002-12-27     Andrew Hill         Basic support for FER additional display fields
 *                                    (Refactored method for getting display value for fers)
 * 2003-01-10     Andrew Hill         TimeConstraint fields special handling
 * 2003-01-14     Andrew Hill         OptionSource hack (FER only)
 * 2003-01-17     Andrew Hill         string[] values proccesing doesnt destroy referenced data
 * 2003-01-22     Andrew Hill         Allow actionForm used by this renderer to be set and a
 *                                    idPrefix for node ids to be set. This is experimental. Later
 *                                    want to refactor for full nested support!
 * 2003-03-31     Andrew Hill         Fix logic error for FERs where lookup unnecessary
 * 2003-04-14     Andrew Hill         Added alwaysUseMultifiles attribute
 * 2003-06-25     Andrew Hill         Sort enum options alphabetically
 * 2003-07-17     Andrew Hill         Support for selection tables
 * 2003-07-25     Daniel D'Cotta      GridForm integration
 * 2003-08-26     Andrew Hill         Automated Diversion Rendering (Version 1 ;-))
 * 2003-08-27     Andrew Hill         Allow FER primary display value to come from local entity
 * 2004-02-17     Neo Sok Lay         Construct date formats from TimeConstraint pattern, if specified.
 * 2006-04-10     Tam Wei Xiang       To support the rendering of ActionError's param array
 * 2006-11-14     Regina Zeng         To support the rendering of GDOC DETAIL page multiFiles link
 */
package com.gridnode.gtas.client.web.renderers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.FieldDivPath;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.pdip.framework.util.TimeUtil;

public class BindingFieldPropertyRenderer extends AbstractRenderer implements IFilter
{
  public static final String DIVERSION_ICON_URL = "images/actions/diversion.gif"; //20030826AH
    
  private String _trueKey = "generic.yes";
  private String _falseKey = "generic.no";

  private String _seperator = ";"; //seperator used when cannot use elements when doing mv fields
  private String _elementalSeperator = "br";

  private Element _detailElement = null;
  private Element _labelElement = null;
  private Element _valueElement = null;
  private Element _errorElement = null;
  private String  _labelId = null;
  private String  _valueId = null;
  private String  _errorId = null;
  private String  _detailId = null;
  private String  _baseId = null;
  private boolean _bindingDefined = false;
  private boolean _bindingPerformed = false;
  private boolean _verifyIds = false;

  private int _detailElementType = INodeTypes.NONE;
  private int _valueElementType = INodeTypes.NONE;
  private int _labelElementType = INodeTypes.NONE;
  private int _errorElementType = INodeTypes.NONE;

  private String _labelKey = null;
  private Object _value = null;
  private String _errorKey = null;

  private boolean _visible = false;
  private boolean _editable = false;
  private boolean _mandatory = false;
  private boolean _collection = false;
  private int     _maxLength = 0;

  private int _constraintType = IConstraint.TYPE_OTHER;
  private IConstraint _constraint = null;

  private boolean _clearLinkOpCons = false;

  private boolean _readOnlyIsText = false;

  //private Document _boundTarget = null;

  private ISimpleResourceLookup _rLookup; // Keep another ref to rLookup to save me some typing ;-)

  private IGTEntity _entity;
  private IGTSession _session;
  private Number _fieldId;

  private FormFileElement[] _formFileElements = null;

  private IFilter _constraintFilter;

  private boolean _isSupportAdditionalForeignDisplayFields = true;

  private String[] _sesa = new String[1]; //?!u.know:I!>u ;->
  private java.util.Date _scratchDate = null; //scratch date used in date processing (like sesa)
  private DateFormat _dateFormat = null;
  private DateFormat _dateTimeFormat = null;
  private DateFormat _timeFormat = null;

  private Locale _locale; //20030110AH
  private TimeZone _timeZone; //20030110AH

  private ActionForm _overrideForm = null;
  private String _idPrefix = "";

  private IBFPROptionSource _optionSource; //20030114AH

  private boolean _alwaysUseMultifiles; //20030414AH
  private boolean _valueRendered; //20030414AH
  private MultifilesRenderer _multifilesRenderer = null; //20030414AH
  private SelectionTableRenderer _selectionTableRenderer = null; //20030717AH
  
  private boolean _fbdEnabled = true; //20030826AH
  
  private Object[] _actionErrParams = null; //20060410 TWX
  
  public BindingFieldPropertyRenderer(RenderingContext rContext)
  {
    super(rContext);
    reset();
    _rLookup = rContext.getResourceLookup();
    _constraintFilter = this;
    _locale = StaticWebUtils.getLocale(rContext.getRequest()); //20030110AH
    _timeZone = StaticWebUtils.getTimeZone(rContext.getRequest()); //20030110AH
  }

  public void reset()
  {
    _alwaysUseMultifiles = false; //20030414AH
    _valueRendered = false; //20030414AH

    _detailElement = null;
    _labelElement = null;
    _valueElement = null;
    _errorElement = null;
    _baseId = null;
    _detailId = null;
    _labelId = null;
    _errorId = null;
    _valueId = null;

    _bindingDefined = false;
    _bindingPerformed = false;

    _labelKey = null;
    _value = null;
    _errorKey = null;

    _visible = false;
    _editable = false;
    _mandatory = false;
    _collection = false;
    _maxLength = 0;

    _entity = null;
    _session = null;
    _fieldId = null;

    _detailElementType = INodeTypes.NONE;
    _valueElementType = INodeTypes.NONE;
    _labelElementType = INodeTypes.NONE;
    _errorElementType = INodeTypes.NONE;

    _constraint = null;
    _constraintType = IConstraint.TYPE_OTHER;

    _formFileElements = null;
    
    _actionErrParams = null;
  }

  /**
   * Allows you to force the bfpr to insert a multifiles widget for fields with a file constraint
   * even if the layout does not specify multifile widget.
   * Useful for doing 'magic' file download links in listviews.
   * @param alwaysUseMultifiles if true valueElement will have multifiles widget inserted as child
   */
  public void setAlwaysUseMultifiles(boolean alwaysUseMultifiles)
  { _alwaysUseMultifiles = alwaysUseMultifiles; } //20030414AH

  public boolean isAlwaysUseMultifiles()
  { return _alwaysUseMultifiles; } //20030414AH

  /**
   * Set the option source for fields with selectable options. Only FER fields are currently
   * supported. Following an exploration of how well this works out the OptionSource feature
   * will likely be both refactored significantly and expanded to allow specification of options
   * for many other types of field. (You should therefore be sure to check the entity and field id
   * in your option source before returning options!)
   * -Changes I envisage may include automatic generation of OptionSource based on metainfo and
   * connecting an option source to only specified fields rather than requiring a hardcoded check
   * in the option source to see which field its doing. (Filters could use this change too!)
   *
   * 20030114AH
   */
  public void setOptionSource(IBFPROptionSource optionSource)
  { //20030114AH
    _optionSource = optionSource;
  }

  public IBFPROptionSource getOptionSource()
  { //20030114AH
    return _optionSource;
  }

  public void setSupportAdditionalForeignDisplayFields(boolean support)
  { //20021227AH
    _isSupportAdditionalForeignDisplayFields = support;
  }

  public boolean isSupportAdditionalForeignDisplayFields()
  { //20021227AH
    return _isSupportAdditionalForeignDisplayFields;
  }

  /**
   * Set the filter used for all rendering of selectable options for enumerated/foreign entity
   * fields. If set to null, no options will be automatically rendered. A BFPR also implements
   * IFilter to provide the convienience of a filter that allows all options through and this is
   * the default.
   * @param filter
   */
  public void setConstraintFilter(IFilter filter)
  {
    _constraintFilter = filter;
  }

  public IFilter getConstraintFilter()
  {
    return _constraintFilter;
  }

  public void setClearLinkOperationContext(boolean clearOpCon)
  {
    _clearLinkOpCons = clearOpCon;
  }

  public boolean isClearLinkOperationContext()
  {
    return _clearLinkOpCons;
  }

  public void setEditable(boolean editable)
  {
    _editable = editable;
  }

  public boolean isEditable()
  {
    return _editable;
  }

  public void setVisible(boolean visible)
  {
    _visible = visible;
  }

  public boolean isVisible()
  {
    return _visible;
  }

  public void setMandatory(boolean mandatory)
  {
    _mandatory = mandatory;
  }

  public boolean isMandatory()
  {
    return _mandatory;
  }

  public void setCollection(boolean collection)
  {
    _collection = collection;
  }

  public boolean isCollection()
  {
    return _collection;
  }

  public void setMaxLength(int maxLength)
  {
    _maxLength = maxLength;
  }

  public int getMaxLength()
  {
    return _maxLength;
  }

  public boolean isBindingDefined()
  {
    return _bindingDefined;
  }

  public boolean isBindingPerformed()
  {
    return _bindingPerformed;
  }

  public String getBaseId()
  {
    return _baseId;
  }

  public String getValueId()
  {
    return _valueId;
  }

  public String getErrorId()
  {
    return _errorId;
  }

  public String getDetailId()
  {
    return _detailId;
  }

  public Element getDetailElement()
  {
    return _detailElement;
  }

  public Element getLabelElement()
  {
    return _labelElement;
  }

  public Element getErrorElement()
  {
    return _errorElement;
  }

  public void setSeperator(String seperator)
  {
    _seperator = seperator;
  }

  public String getSeperator()
  {
    return _seperator;
  }

  public void setElementalSeperator(String elementalSeperator)
  {
    _elementalSeperator = elementalSeperator;
  }

  public String getElementalSeperator()
  {
    return _elementalSeperator;
  }

  public void setBindings(Number fieldId)
    throws RenderingException
  {
    try
    {
      IGTEntity entity = getEntity();
      ActionErrors errors = getRenderingContext().getActionErrors();
      setBindings(entity, fieldId, errors);
    }
    catch(RenderingException e)
    {
      throw e;
    }
    catch(Exception e)
    {
      throw new RenderingException("Error setting bindings for field " + fieldId,e);
    }
  }

  protected Object getValueFromForm(ActionForm form, String fieldName)
    throws RenderingException
  {
    try
    {
      return PropertyUtils.getProperty(form, fieldName);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Unable to read value for property '" + fieldName + "' from " + form,t);
    }
  }

  public void setBindings(IGTEntity entity, Number fieldId, ActionErrors errors)
    throws RenderingException
  {
    try
    {
      ActionForm form = getActionForm();
      if(entity == null)
      {
        throw new java.lang.NullPointerException(
                  "No entity to provide binding id information (entity passed is null)");
      }
      if(form == null)
      {
        throw new java.lang.NullPointerException(
                  "No action form found in operation context (form is null)");
      }
      Object value = null;
      String fieldName = entity.getFieldName(fieldId);
      IGTFieldMetaInfo fieldMetaInfo = entity.getFieldMetaInfo(fieldId);
      if(fieldMetaInfo == null)
      {
        throw new java.lang.NullPointerException("No metainfo returned for fieldId=" + fieldId);
      }
      if(fieldMetaInfo.getConstraintType() == IConstraint.TYPE_FILE)
      {
        IFileConstraint fConstraint = (IFileConstraint)fieldMetaInfo.getConstraint();
        if(fConstraint.isFileName() && (form instanceof GTActionFormBase) )
        {
          _formFileElements = ((GTActionFormBase)form).getFormFileElements(fieldName);
        }
      }

      //value = getValueFromForm(form, fieldName); // 20031009 DDJ
      //value = getValueFromForm(form, _idPrefix + fieldName); // 20031127 DDJ: Changed back to the original as it breaks viewing of certificates
      //value = getValueFromForm(form, fieldName); // 20040816 DDJ: This breaks viewing of embeded channels, thus need to behave differently according to _idPrefix
      if(StaticUtils.stringNotEmpty(_idPrefix) && _idPrefix.endsWith("_"))
      {
        // 20040816 DDJ: EmbededChannelInfoRenderer uses _idPrefix = "packagingInfo_" because 
        // it is using the same ActionForm, thus the ActionForm does contain the prefix
        value = getValueFromForm(form, _idPrefix + fieldName); 
      }
      else
      {
        // 20040816 DDJ: CertificateRenderer uses _idPrefix = "issuerDetails." because
        // it is using an embeded ActionForm, thus the ActionForm does NOT contain the prefix 
        value = getValueFromForm(form, fieldName);
      }       

      if(fieldMetaInfo.getValueClass().equals("java.lang.Boolean"))
      {
        if( (value == null) || (value instanceof String) )
        {
          if(!"".equals(value))
          { // If its "" then means not selected, otherwise do special boolean handling
            value = StaticUtils.booleanValue((String)value);
          }
        }
      }
      setBindings(entity,fieldId,value,errors);
    }
    catch(Exception e)
    {
      throw new RenderingException("Error setting bindings for field " + fieldId + " for entity: " + entity,e);
    }
  }

  public void setBindings(
                          IGTEntity entity,
                          Number fieldId,
                          Object value,
                          ActionErrors errors)
    throws RenderingException
  {
    try
    {
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(fieldId);
      String baseId = entity.getFieldName(fieldId);
      ActionError error = MessageUtils.getFirstError(errors, baseId);
      String errorKey = null;
      
      Object[] param = null;
      if(error != null) 
      {
      	errorKey = error.getKey();
      	param = error.getValues();
      }

      setBindings(entity, fieldId);
      setValue(value);
      setErrorKey(errorKey);
      
      setActionErrParams(param);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error setting bindings",t);
    }
  }

  public void setBindings(IGTEntity entity, Number fieldId)
    throws RenderingException
  {
    try
    {
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(fieldId);
      String baseId = entity.getFieldName(fieldId);
      setBindings(baseId);
      setVisible(fmi.isDisplayable(entity.isNewEntity()));
      setEditable(fmi.isEditable(entity.isNewEntity()));
      setMandatory(fmi.isMandatory(entity.isNewEntity()));
      setCollection(fmi.isCollection());
      setMaxLength(fmi.getLength());
      setLabelKey(fmi.getLabel());
      setConstraint(fmi.getConstraint());
      setBoundEntity(entity);
      setFieldId(fieldId);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error setting bindings",t);
    }
  }

  public void setBindings(String baseId)
    throws RenderingException
  {
    setBindings(baseId, false);
  }

  public void setBindings(String baseId, boolean verifyIds)
    throws RenderingException
  { //20030122AH- Mod to support id prefix
    String idPrefix = getIdPrefix(); //20030122AH
    verifyBaseId(baseId);
    setBindings(
          baseId,
          idPrefix + baseId + "_details",
          idPrefix + baseId + "_label",
          idPrefix + baseId + "_value",
          idPrefix + baseId + "_error",
          verifyIds);
  }

  public void setBindings(
                    String baseId,
                    String detailId,
                    String labelId,
                    String valueId,
                    String errorId,
                    boolean verifyIds )
    throws RenderingException
  {
    // All other setBindings() end up (eventually!) delegating to this one.
    _baseId = baseId;
    _detailId = detailId;
    _labelId = labelId;
    _valueId = valueId;
    _errorId = errorId;
    _bindingDefined = true;
    _verifyIds = verifyIds;
/*System.out.println("setBindings("
+ baseId + ","
+ detailId + ","
+ labelId + ","
+ valueId + ","
+ errorId + ","
+ verifyIds );*/
  }

  public void bindNow(Document target)
    throws RenderingException
  {
    if(target == null) throw new java.lang.NullPointerException("Null target");
    _target = target;
    bindToTarget(_verifyIds);
  }

  protected void bindToElements(Element detailElement,
                                Element labelElement,
                                Element valueElement,
                                Element errorElement)
    throws RenderingException
  { //20020909AH
    _bindingPerformed = false;
    try
    {
      _detailElement = detailElement;
      _detailElementType = getNodeType(_detailElement);


      _labelElement  = labelElement;
      _labelElementType = getNodeType(_labelElement);


      _valueElement  = valueElement;
      _valueElementType = getNodeType(_valueElement);

      _errorElement  = errorElement;
      _errorElementType = getNodeType(_errorElement);

      _bindingPerformed = true;
      _bindingDefined = true;
    }
    catch(Throwable t)
    {
      reset();
      throw new RenderingException("Error binding to specified elements",t);
    }
  }

  protected void bindToTarget(boolean checkExists) throws RenderingException
  {
    _bindingPerformed = false;
    try
    {
      if(_detailId != null)
      {
        _detailElement = getElementById(_detailId, checkExists);
        _detailElementType = getNodeType(_detailElement);
      }
      else
      {
        _detailElement = null;
        _detailElementType = INodeTypes.NONE;
      }

      if(_labelId != null)
      {
        _labelElement  = getElementById(_labelId,  checkExists);
        _labelElementType = getNodeType(_labelElement);
      }
      else
      {
        _labelElement = null;
        _labelElementType = INodeTypes.NONE;
      }

      if(_valueId != null)
      {
        _valueElement  = getElementById(_valueId,  checkExists);
        _valueElementType = getNodeType(_valueElement);
      }
      else
      {
        _valueElement = null;
        _valueElementType = INodeTypes.NONE;
      }

      if(_errorId != null)
      {
        _errorElement  = getElementById(_errorId,  checkExists);
        _errorElementType = getNodeType(_errorElement);
      }
      else
      {
        _errorElement = null;
        _errorElementType = INodeTypes.NONE;
      }

      _bindingPerformed = true;
    }
    catch(Exception e)
    {
      reset();
      if(e instanceof RenderingException) throw (RenderingException)e;
      throw new RenderingException("Error binding to DOM",e);
    }
  }

  public void setFieldId(Number fieldId)
  {
    _fieldId = fieldId;
  }

  public Number getFieldId()
  {
    return _fieldId;
  }

  public void setSession(IGTSession session)
  {
    _session = session;
  }

  public IGTSession getSession()
  {
    return _session;
  }

  public void setBoundEntity(IGTEntity entity)
  {
    _entity = entity;
    if(_session == null)
    {
      setSession(entity.getSession());
    }
  }

  public IGTEntity getBoundEntity()
  {
    return _entity;
  }

  public void setLabelKey(String key)
  {
    _labelKey = key;
  }

  public String getLabelKey()
  {
    return _labelKey;
  }

  public void setValue(Object value)
  {
    _value = value;
  }

  public Object getValue()
  {
    return _value;
  }

  public void setErrorKey(String key)
  {
    _errorKey = key;
  }

  public String getErrorKey()
  {
    return _errorKey;
  }
  
  public void setActionErrParams(Object[] param)
  {
  	_actionErrParams = param;
  }
  
  public Object[] getActionErrParams()
  {
  	return _actionErrParams;
  }
  
  public void setConstraint(IConstraint constraint)
  {
    _constraint = constraint;
    if(constraint != null)
    {
      _constraintType = constraint.getType();
    }
    else
    {
      _constraintType = IConstraint.TYPE_OTHER;
    }
  }

  public IConstraint getConstraint()
  {
    return _constraint;
  }

  public int getConstraintType()
  {
    return _constraintType;
  }

  private void verifyBaseId(String baseId) throws RenderingException
  {
    if((baseId==null) || (baseId.equals("")))
      throw new RenderingException("Invalid baseId for binding");
  }

  //End 0f initialisation and binding methods
  //...........................................................................................
  //Start of dom manipulating methods
  protected void render() throws RenderingException
  {
    if(!_bindingDefined)
      throw new RenderingException("FieldRenderer not bound");

    if(!_bindingPerformed)
      bindToTarget(_verifyIds);

    if(isVisible())
    {
      renderProperties();
      renderLabel();
      renderValue();
      renderError();
      if( isFbdEnabled() )
      {
        renderFieldDiversion(); //20030826AH
      }
    }
    else
    {
      hide();
    }
  }

  protected void renderProperties() throws RenderingException
  {
    int length = getMaxLength();
    int elementType = getNodeType(_valueElement);
    if(elementType == INodeTypes.NONE) return;
    if(elementType == INodeTypes.INSERT_SELECTION_TABLE) return; //20030717AH - We will do it all at value time
    if(elementType == INodeTypes.INSERT_MULTISELECTOR)
    {
      elementType = prepareMultiselector();
    }
    switch(_constraintType)
    {
      case IConstraint.TYPE_ENUMERATED:
        renderEnumeratedOptions(elementType);
        break;

      case IConstraint.TYPE_FOREIGN_ENTITY:
        renderForeignEntityOptions(elementType);
        break;

      case IConstraint.TYPE_FILE:
        if( isAlwaysUseMultifiles() )
        { //20030414AH
          renderToMultifiles();
          _valueRendered = true;
        }
        break;
    }
    if(!isEditable())
    {
      switch(elementType)
      {
        case INodeTypes.INPUT_TEXT:
        case INodeTypes.INPUT_PASSWORD:
        case INodeTypes.TEXTAREA:
          if(_readOnlyIsText)
          {
            _valueElement = replaceNodeWithNewElement("span",_valueElement,false);
            _valueElementType = INodeTypes.TEXT_PARENT;
          }
          else
          {
            _valueElement.setAttribute("readonly","readonly");
            _valueElement.setAttribute("class","readonly");
          }
          break;

        case INodeTypes.INPUT_CHECKBOX:
        case INodeTypes.INPUT_RADIO:
        case INodeTypes.OPTION_PARENT:
          if(_readOnlyIsText)
          {
            _valueElement = replaceNodeWithNewElement("span",_valueElement,false);
            _valueElementType = INodeTypes.TEXT_PARENT;
          }
          else
          {
            _valueElement.setAttribute("disabled","disabled");
            _valueElement.setAttribute("class","readonly");
          }
          break;
      }
    }
    else if(isMandatory())
    {
      switch(elementType)
      {
        case INodeTypes.INPUT_BUTTON:
        case INodeTypes.INPUT_CHECKBOX:
        case INodeTypes.INPUT_FILE:
        case INodeTypes.INPUT_IMAGE:
        case INodeTypes.INPUT_PASSWORD:
        case INodeTypes.INPUT_RADIO:
        case INodeTypes.INPUT_RESET:
        case INodeTypes.INPUT_SUBMIT:
        case INodeTypes.INPUT_TEXT:
        case INodeTypes.INPUT_TEXT_PARENT:
        case INodeTypes.LINK:
        case INodeTypes.OPTION_PARENT:
        case INodeTypes.TEXTAREA:
        case INodeTypes.MULTISELECTOR_VALUE:
          _valueElement.setAttribute("class","mandatory");
          break;

        default:
          
          break;
      }

    }

    switch(elementType)
    {
      case INodeTypes.INPUT_TEXT:
      case INodeTypes.INPUT_PASSWORD:
        if(length > 0)
        {
          if(length > 50) length = 40;
          _valueElement.setAttribute("size","" + length);
          _valueElement.setAttribute("maxlength","" + getMaxLength());
        }
        else
        {
          _valueElement.removeAttribute("maxlength");
        }
        _valueElement.setAttribute("name",_baseId);
        break;

      case INodeTypes.OPTION_PARENT:
        if(_value instanceof String)
        {
          _valueElement.removeAttribute("multiple");
        }
        else if(_value instanceof String[])
        {
          _valueElement.setAttribute("multiple","multiple");
        }
        _valueElement.setAttribute("name",_baseId);
        break;

      case INodeTypes.INPUT_BUTTON:
      case INodeTypes.INPUT_CHECKBOX:
      case INodeTypes.INPUT_FILE:
      case INodeTypes.INPUT_HIDDEN:
      case INodeTypes.INPUT_RADIO:
      case INodeTypes.INPUT_IMAGE:
        _valueElement.setAttribute("name",_baseId);
        break;
    }
  }

  protected void renderValue() throws RenderingException
  {
    if(_valueRendered) return; //20030414AH
    if(_valueElementType == INodeTypes.NONE)
    {
      return;
    }
    if(_valueElementType == INodeTypes.MULTIFILES)
    {
      renderToMultifiles();
      return;
    }
    if(_valueElementType == INodeTypes.INSERT_SELECTION_TABLE)
    { //20030717AH
      renderToSelectionTable();
      return;
    }
    if(_value == null)
    {
      _value = "";
    }
    if(_value instanceof String)
    {
      renderTextToSimpleElement(_valueElement, processStringValue((String)_value), _valueElementType);
    }
    else if(_value instanceof String[])
    {
      renderTextToMultipleElement(_valueElement, processStringValues((String[])_value), _valueElementType);
    }
    else if(_value instanceof Boolean)
    {
      renderBooleanToSimpleElement(_valueElement, (Boolean)_value, _valueElementType);
    }
    else
    {
      _value = StaticUtils.stringValue(_value);
      renderTextToSimpleElement(_valueElement, processStringValue((String)_value), _valueElementType);
    }
  }

  protected void renderLabel() throws RenderingException
  {
    String label = _rLookup.getMessage(_labelKey);
    if(label == null) label = "";
    if(_labelElement == null)
    {
      if(getNodeType(_valueElement) == INodeTypes.LINK)
      {
        replaceText(_valueElement, label);
        return;
      }
    }
    else
    {
      if(getNodeType(_labelElement) == INodeTypes.LINK)
      {
        replaceText(_labelElement, label);
        return;
      }
      else
      {
        String cssClass = _labelElement.getAttribute("class");
        if("".equals(cssClass) || (cssClass == null) )
        {
          _labelElement.setAttribute("class","fieldlabel");
        }
        renderTextToSimpleElement(_labelElement, label, _labelElementType);
      }
    }
  }

  protected void renderError() throws RenderingException
  {
  	//TWX: 20060410
    String error = _rLookup.getMessage(_errorKey, _actionErrParams);
    if(error == null) error = "";
    renderTextToSimpleElement(_errorElement, error, _errorElementType);
    if(_errorElement != null)
    {
      _errorElement.setAttribute("class","errortext");
    }
  }

  protected void renderTextToMultipleElement(Element element, String[] values, int elementType)
    throws RenderingException
  {
    switch(elementType)
    {
      case INodeTypes.OPTION_PARENT:
        renderSelectedOptions(element, values);
        break;

      case INodeTypes.MULTISELECTOR_VALUE:
        renderMultiselectorValues(element,values);
        break;

      case INodeTypes.LI_PARENT:
        removeAllChildren(element);
        if(values != null)
        {
          renderListItems(element, values);
        }
        break;

      case INodeTypes.TABLE:
        removeAllChildren(element);
        for(int i=0; i < values.length; i++)
        {
          Element tr = element.getOwnerDocument().createElement("tr");
          Element td = element.getOwnerDocument().createElement("td");
          replaceText(td, values[i]);
          tr.appendChild(td);
          element.appendChild(tr);
        }
        break;

      case INodeTypes.TEXT_PARENT:
      case INodeTypes.TEXTAREA:
        removeAllChildren(element);
        for(int i=0; i < values.length; i++)
        {
          Node valueNode = element.getOwnerDocument().createTextNode(values[i]);
          element.appendChild(valueNode);
          if(i != (values.length - 1))
          {
            Node seperator = getSuitableSeperator(element);
            if(seperator != null) element.appendChild(seperator);
          }
        }
        break;

      case INodeTypes.INPUT_HIDDEN:
      case INodeTypes.INPUT_TEXT:
        StringBuffer buffer = new StringBuffer();
        for(int i=0; i < values.length; i++)
        {
          buffer.append(values[i]);
          if(i != (values.length - 1))
          {
            String seperator = _seperator;
            if(seperator != null) buffer.append(seperator);
          }
        }
        //replaceText(element, buffer.toString());        // 20031112 DDJ
        element.setAttribute("value", buffer.toString()); // 20031112 DDJ
        break;

      case INodeTypes.TABLE_HEAD:
      case INodeTypes.TABLE_BODY:
      case INodeTypes.TABLE_FOOT:
        String cellType = (elementType==INodeTypes.TABLE_HEAD) ? "th" : "td";
        removeAllChildren(element);
        for(int i=0; i < values.length; i++)
        {
          Element row = element.getOwnerDocument().createElement("tr");
          Element cell = element.getOwnerDocument().createElement(cellType);
          row.appendChild(cell);
          replaceText(cell, values[i]);
          element.appendChild(row);
        }
        break;

      default:
        throw new RenderingException( "VEType " + elementType
                                      + " is not supported by renderToMultipleElement() method");
    }
  }

  protected void renderBooleanToSimpleElement(Element element, Boolean flag, int elementType)
    throws RenderingException
  {
    switch(elementType)
    {
      case INodeTypes.INPUT_CHECKBOX:
        if(flag.booleanValue())
        {
          element.setAttribute("checked","checked");
        }
        else
        {
          element.removeAttribute("checked");
        }
        break;

      default:
        RenderingContext rContext = getRenderingContext();
        String flagLabel = null;
        if(_constraintType == IConstraint.TYPE_ENUMERATED)
        {
          flagLabel = processStringValue(flag.toString());
        }
        else
        {
          if(flag.booleanValue())
          {
            flagLabel = rContext.getResourceLookup().getMessage(_trueKey);
          }
          else
          {
            flagLabel = rContext.getResourceLookup().getMessage(_falseKey);
          }
        }
        renderTextToSimpleElement(element,flagLabel,elementType);
        break;
    }
  }

  protected void renderTextToSimpleElement(Element element, String text, int elementType)
    throws RenderingException
  {
    switch(elementType)
    {
      case INodeTypes.OPTION_PARENT:
      case INodeTypes.MULTISELECTOR_VALUE:
      case INodeTypes.LI_PARENT:
        String[] values = new String[1];
        values[0] = text;
        renderTextToMultipleElement(element, values, elementType);
        break;

      case INodeTypes.LINK:
        if(_constraintType == IConstraint.TYPE_FILE)
        {
          replaceText(element,text);
        }
        else
        {
          _valueElement.setAttribute("href",getRenderingContext().getUrlRewriter().rewriteURL(
                                      text,isClearLinkOperationContext()));
        }
        break;

      case INodeTypes.TEXT_PARENT:
        replaceMultilineText(element,text);
        break;

      case INodeTypes.TEXTAREA:
        replaceText(element,text);
        break;

      case INodeTypes.INPUT_HIDDEN:
      case INodeTypes.INPUT_TEXT:
        element.setAttribute("value",text);
        break;

      case INodeTypes.INPUT_FILE:
        // Special handling for file inputs.
        // File cannot set the path, so if we want to display then we need to do elsewhere.
        // If you create an element with postfix _display will use that to display the
        // value, if _value element is a file input.
        // NORMALLY however, one would have a seperate property in the ActionForm and
        // DAO for the path or filename and only render that.(?)
        Element displayElement = getElementById(getBaseId() + "_display",false);
        if(displayElement != null)
        {
          renderTextToSimpleElement(displayElement,text, elementType);
        }
        break;

      case INodeTypes.IMAGE:
      case INodeTypes.INPUT_IMAGE:
        element.setAttribute("src",text);
        break;

      case INodeTypes.NONE:
      case INodeTypes.INPUT_PASSWORD:
        
        break;

      case INodeTypes.INPUT_CHECKBOX:
      case INodeTypes.INPUT_RADIO:
        renderCheckboxSelection(_valueElement, text);
        break;

      default:
        throw new RenderingException( "VEType " + elementType
                                      + " is not supported by renderToSimpleElement() method");
    }
  }

  protected void hide()
  {
    try
    {
      if(_detailElement != null)
      {
        removeNode(_detailElement,false);
      }
      else
      {
        removeNode(_labelElement,false);
        removeNode(_valueElement,false);
        removeNode(_errorElement,false);
      }
    }
    catch(Throwable t)
    {
      
    }
  }

  protected Node getSuitableSeperator(Element textParent)
  {
    if(textParent == null) throw new java.lang.NullPointerException("Null textParent");
    String nodeName = textParent.getNodeName();
    Node sepNode = null;

    if(nodeName.equals("textarea")) return textParent.getOwnerDocument().createTextNode(_seperator);

    return textParent.getOwnerDocument().createElement(_elementalSeperator);
  }

  protected void renderForeignEntityOptions(int elementType)
    throws RenderingException
  {
    try
    {
      if(_constraintFilter == null)
      { //If the constraintFilter is explicitly null (normally it defaults to 'this') then
        //render NO FER options at all (and dont even delete the mock options from the layout)!
        return;
      }
      switch(elementType)
      {
        case INodeTypes.OPTION_PARENT:
        case INodeTypes.MULTISELECTOR_VALUE:
          IForeignEntityConstraint foreignConstraint = (IForeignEntityConstraint)_constraint;
          IGTSession gtasSession = getSession();
          if(gtasSession == null) throw new NullPointerException("gtasSession is null"); //20030416AH
          String foreignType = foreignConstraint.getEntityType();
          int managerType = gtasSession.getManagerType(foreignType);
          IGTManager manager = gtasSession.getManager(managerType);

          Collection foreignEntities = null;
          IBFPROptionSource optionSource = getOptionSource();
          if(optionSource != null)
          { //20030114AH - Added OptionSource support for FER
            RenderingContext rContext = getRenderingContext();
            try
            {
              foreignEntities = optionSource.getOptions(rContext,this);
            }
            catch(Throwable t)
            {
              throw new RenderingException( "Error obtaining list of entities as"
                + " selection candidates using getOptions() of IBFPROptionSource:" + optionSource,t);
            }
          }
          if(foreignEntities == null)
          { //20030114AH - If either we were werent using an OptionSource or the OptionSource
            //returned null then we use the standard lookup methodology for the options list
            //-that is to getAll() entities of that type and rely on the filter to eliminate the
            //ones that are not applicable for that field (taking form state into account...)
            try
            {
              foreignEntities = manager.getAll();
            }
            catch(Throwable t)
            {
              throw new RenderingException( "Error obtaining list of entities as"
                + " selection candidates using getAll() of manager:" + manager,t);
            }
          }

          try
          {
            //We always allow the entities to be filtered even when using an OptionSource.
            //Though not particularly efficient, - it requires far less development time
            //to be $pend compared to writing a million wierd IDataFilters etc...
            //(Which would also require a ton of type conversion etc...)
            foreignEntities = StaticUtils.getFilteredCollection(  foreignEntities,
                                                                _constraintFilter,
                                                                this,
                                                                false);
          }
          catch(Throwable t)
          { //20030114AH - Added try/catch
            throw new RenderingException("Error filtering selectable options for FER field",t);
          }
          String valueFieldName = foreignConstraint.getKeyFieldName();
          Number valueFieldId = manager.getFieldId(foreignType,valueFieldName);
          String textFieldName = foreignConstraint.getDisplayFieldName();
          Number textFieldId = manager.getFieldId(foreignType,textFieldName);
          EntityOptionValueRetriever retriever = new EntityOptionValueRetriever(textFieldId,valueFieldId);
          //20021227 - Specify additional fields
          String[] additionalFields = foreignConstraint.getAdditionalDisplayFieldNames();
          retriever.setAdditionalDisplayFieldNames( additionalFields );
          //..

          if(elementType == INodeTypes.MULTISELECTOR_VALUE)
          {
            retriever.setMaxDisplayLength(64); //20021227AH (Hardcoded size for now -naughty!)
            renderSelectOptions(_baseId + "_choices",foreignEntities,retriever,true,null);
          }
          else
          {
            retriever.setMaxDisplayLength(48); //(Hardcoded size for now -naughty!)
            renderSelectOptions(_valueId,foreignEntities,retriever,true,"generic.empty");
          }
          break;

        case INodeTypes.INPUT_CHECKBOX:
        case INodeTypes.INPUT_RADIO:
          throw new java.lang.UnsupportedOperationException(
              "Foreign constraint rendering for checkboxes"
            + " and radio buttons has not been implemented yet");
          //break;
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering options for foreign entity reference field id="
        + _fieldId,t);
    }
  }

  protected void renderEnumeratedOptions(int elementType)
    throws RenderingException
  {
    switch(elementType)
    {
      case INodeTypes.OPTION_PARENT:
        renderSelectOptions(_valueElement, (IEnumeratedConstraint)_constraint,true,"generic.empty");
        sortSelectOptions(_valueElement, null); //20030625AH //@todo: enum sort types
        break;

      case INodeTypes.MULTISELECTOR_VALUE:
        // 20030929 DDJ: Fix problem with using enum constraint with Multiselector,
        //               I believe 'Choices' is only used as a element name and not element id 
        //Element choicesElement = getElementById(_baseId + "Choices",true);
        Element choicesElement = getElementById(_baseId + "_choices", true);
        renderSelectOptions(choicesElement, (IEnumeratedConstraint)_constraint,true,null);
        sortSelectOptions(choicesElement, null); //20030625AH //@todo: enum sort types
        break;
    }
  }

  protected String[] processStringValues(String[] values)
    throws RenderingException
  {
    RenderingContext rContext = getRenderingContext();
    //20030117AH - Create a new Array for the processed values!
    String[] displayValues = null;

    if(_constraintType == IConstraint.TYPE_ENUMERATED)
    {
      switch(_valueElementType)
      {
        case INodeTypes.OPTION_PARENT:
        case INodeTypes.MULTISELECTOR_VALUE: //20021204AH - untested
          displayValues = values; //20030117AH
          break;

        default:
          displayValues = new String[values.length]; //20030117AH
          for(int i=0; i < values.length; i++)
          {
            displayValues[i] = processStringValue(values[i]);
          }
          break;
      }
    }
    else if(_constraintType == IConstraint.TYPE_FOREIGN_ENTITY)
    {
      switch(_valueElementType)
      {
        case INodeTypes.OPTION_PARENT:
        case INodeTypes.MULTISELECTOR_VALUE:
          // These will render based on actual value (text/value association)
          // Additional display fields support comes from EntityOptionValueRetriever used when
          // rendering the available choices
          displayValues = values; //20030117AH
          break;

        default:
          // These need the strings to be the value to display (which differs from the reference field)
          try
          {
            if( (values == null) || (values.length == 0) )
            { //20030117AH
              displayValues = values;
            }
            else
            {
              if(_fieldId == null)
              {
                throw new java.lang.IllegalArgumentException("null fieldId");
              }
              if(_entity == null)
              {
                throw new java.lang.IllegalArgumentException("null entity reference");
              }
              //20021227AH
              IForeignEntityConstraint fConstraint = (IForeignEntityConstraint)_constraint;
              displayValues = convertForeignDisplayValues(fConstraint,values); //20030117AH
            }
          }
          catch(Throwable t)
          {
            throw new RenderingException( "Error converting display values for referenced foreign entity fieldId:"
                                          + _fieldId + " based on value array:" + values,t);
          }
          break;
      }
    }
    else if(_constraintType == IConstraint.TYPE_TIME)
    { //20030110AH
      ITimeConstraint timeConstraint = (ITimeConstraint)_constraint;
      displayValues = processTimeStrings(values,timeConstraint); //20030117AH
    }
    else
    { //20031112 DDJ: for other constraint types, use original values
      displayValues = values;
    }
    return displayValues;
  }

  protected String processStringValue(String value)
    throws RenderingException
  {
    RenderingContext rContext = getRenderingContext();
    switch(_valueElementType)
    {
      case INodeTypes.OPTION_PARENT:
      case INodeTypes.MULTISELECTOR_VALUE: //20021204AH - untested
        break;

      default:
        switch(_constraintType)
        {
          case IConstraint.TYPE_ENUMERATED:
            // Modified 20021204AH - To handle cases where data is bad (not in enum)
            String enumKey = ((IEnumeratedConstraint)_constraint).getLabel(value);
            if("".equals(enumKey) || (enumKey == null))
            { // O20021204AH - only modify value if it can be found in the enum
              
            }
            else
            {
              //value = rContext.getResourceLookup().getMessage(enumKey); // 20030915 DDJ
              value = ((IEnumeratedConstraint)_constraint).getRequiresI18n() ? rContext.getResourceLookup().getMessage(enumKey): enumKey;
            }
            break;

          case IConstraint.TYPE_FOREIGN_ENTITY:
            try
            {
              if( (value == null) || "".equals(value) ) return "";
              //20021227AH
              IForeignEntityConstraint fConstraint = (IForeignEntityConstraint)_constraint;
              _sesa[0] = value;
              value = convertForeignDisplayValues(fConstraint,_sesa)[0];
              _sesa[0] = null;
            }
            catch(Throwable t)
            {
              throw new RenderingException( "Error processing display value for referenced foreign entity fieldId:"
                                            + _fieldId + " based on value:" + value,t);
            }
            break;

          case IConstraint.TYPE_TIME:
            //20030110AH
            ITimeConstraint timeConstraint = (ITimeConstraint)_constraint;
            _sesa[0] = value;
            value = processTimeStrings(_sesa,timeConstraint)[0];
            _sesa[0] = null;
            break;
        }
    }
    return value;
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    return true;
  }

  protected int prepareMultiselector()
    throws RenderingException
  {
    MultiselectorRenderer msr = new MultiselectorRenderer(getRenderingContext());
    msr.setAllowsOrdering(true);
    msr.setFieldName(_baseId); // Requiring use of convention of naming baseId same as fieldName
    msr.setInsertId(_valueId);
    msr.initFromLayout(_valueElement);
    msr.setMandatory(isMandatory());
    msr.render(_target);
    _valueElement = msr.getValueElement();
    _valueElementType = INodeTypes.MULTISELECTOR_VALUE;
    _errorElement = msr.getErrorElement();
    _errorElementType = getNodeType(_errorElement);
    return _valueElementType;
  }

  protected void renderMultiselectorValues(Element element, String[] values)
    throws RenderingException
  {
    // need to iterate the list of values, and move those nodes from the choices box to here
    removeAllChildren(element);

    Element choicesElement = (Element)getElementById(_baseId + "_choices",true);
    if(values == null) throw new NullPointerException("values is null");
    for(int i=0; i < values.length; i++)
    {
      Element option = getOptionElement(choicesElement,values[i],false);
      if(option != null)
      {
        choicesElement.removeChild(option);
        element.appendChild(option);
        option.removeAttribute("selected");
      }
    }
  }

  protected void renderToMultifiles() throws RenderingException
  {
    try //20021205AH
    {
      //20021205AH - some assertions to help debugging!
      if(_entity == null)
      {
        throw new java.lang.UnsupportedOperationException(
                  "Rendering to multifiles with a null entity reference is not currently supported");
      }
      if(_fieldId == null)
      {
        throw new java.lang.UnsupportedOperationException(
                  "Rendering to multifiles with a null field id is not currently supported");
      }
      IGTFieldMetaInfo fmi = _entity.getFieldMetaInfo(_fieldId);
      if(IConstraint.TYPE_FILE != fmi.getConstraintType())
      {
        throw new java.lang.UnsupportedOperationException("Fields of constraint type "
                                                          + fmi.getConstraintType()
                                                          + " may not be rendered to multifiles nodes");
      }
      //...
      //20020827AH
      if(_formFileElements == null)
      {
        String[] filenames = _entity.getFieldStringArray(_fieldId);
        if(filenames == null) return; //???
        _formFileElements = new FormFileElement[filenames.length];
        for(int i=0; i < _formFileElements.length; i++)
        {
          _formFileElements[i] = new FormFileElement("" + i, filenames[i]);
        }
        /*throw new RenderingException( "The BFPR did not obtain an array of FormFileElement"
                                      + " from the ActionForm instance");*/
      }
      MultifilesRenderer mfr = _multifilesRenderer; //20030414AH
      if(mfr == null)
      { //20030414AH
        mfr = new MultifilesRenderer(getRenderingContext());
        _multifilesRenderer = mfr; //Cache it. If we in a listview next row can re-use, etc...
      }
      else
      {
        mfr.setRenderingContext( getRenderingContext() );
      }
      if(_valueId == null)
      { //20030414AH
        mfr.setInsertNode(_valueElement);
      }
      else
      {
        mfr.setInsertId(_valueId);
      }
      mfr.initFromLayout(_valueElement);
      if( !isEditable() )
      { //20030221AH - If fmi says cannot edit, force it to non editable
        //Nb: we dont just pass through the isEditable value as the layout may require
        //a non-editable one under certain circumstances while the fmi says editable...
        mfr.setViewOnly(true);
      }
      mfr.setEntity(_entity);
      mfr.setFieldId(_fieldId);
      mfr.setMandatory(isMandatory());
      mfr.setCollection(isCollection());
      mfr.setFormFileElements( _formFileElements );
      // 20030725 DDJ: GridForm Integration      
      if(_entity instanceof IGTGridDocumentEntity)
      {
        IGTGridDocumentEntity gridDoc = (IGTGridDocumentEntity)_entity;
        Long gridDocId =  gridDoc.getUidLong();
        mfr.setGridDocId(gridDocId);
      }     
      // 20061114 RZ: GDOC DETAIL Page
      else if(_entity instanceof IGTGdocDetailEntity)
      {
        IGTGdocDetailEntity gridDoc = (IGTGdocDetailEntity)_entity;
        Long gridDocId =  gridDoc.getUidLong();
        mfr.setGridDocId(gridDocId);
      }
      mfr.render(_target);
      _valueElement = mfr.getValueElement();
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering multifiles for field id=" + _fieldId,t);
    }
  }

  private String[] convertForeignDisplayValues( IForeignEntityConstraint fConstraint, String[] values )
    throws GTClientException
  { //20021227AH
    //20030117AH - Modified to return new array unless using the internal sesa
    try
    {
      if(values == null) return StaticUtils.EMPTY_STRING_ARRAY; //20030117AH
      boolean addFields = isSupportAdditionalForeignDisplayFields();
      if( ( (!addFields) || (fConstraint.getAdditionalDisplayFieldNames().length == 0) )
          && fConstraint.getKeyFieldName().equals(fConstraint.getDisplayFieldName()) ) //20030331AH - Fix logic error
      { //If no additional fields to display and link field and display field the same
        //our work is done
//System.out.println("no addFields and display is link value");
        return values;
      }
//System.out.println("addFields=" + addFields);
//System.out.println("fConstraint.getKeyFieldName()=" + fConstraint.getKeyFieldName());
//System.out.println("fConstraint.getDisplayFieldName()=" + fConstraint.getDisplayFieldName());

      String[] displayValues = (values == _sesa) ? _sesa : new String[values.length]; //20030117AH

      Collection entities = _entity.getFieldEntities(_fieldId);
      if(entities == null) entities = Collections.EMPTY_LIST;
      if( values.length != entities.size() )
      { //Sanity check!
        throw new GTClientException("Expecting "
                                    + values.length
                                    + " foreign entities but found "
                                    + entities.size());
      }
      if(values.length == 0 ) return values; //Shortcircuit - no more work to do
      String displayField = fConstraint.getDisplayFieldName();
      //We shall waste yet more resources and create an OptionValueRetriever so that we can
      //abuse its support for additional display fieldnames.
      //@todo: refactor this and the retriever to leverage a third class for this functionality
      EntityOptionValueRetriever retriever = new EntityOptionValueRetriever(displayField,
                                                                            displayField);
      if(addFields)
      {
        retriever.setAdditionalDisplayFieldNames(fConstraint.getAdditionalDisplayFieldNames());
      }
      if(fConstraint.isLocalDisplayFieldName())
      { //20030827AH
        retriever.setPrimaryDisplaySource(_entity); //nb: this really should pull from the actionForm but for now it doesnt matter.
      }
      Iterator iterator = entities.iterator();
      for(int i=0; i < values.length; i++)
      {
        IGTEntity referencedEntity = (IGTEntity)iterator.next();
        displayValues[i] = retriever.getOptionText(referencedEntity);
      }
      return displayValues; //20030117AH
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error dereferencing foreign entity display values",t);
    }
  }

  protected DateFormat getDateFormat(ITimeConstraint constraint)
    throws RenderingException
  { //20030110AH
    try
    {
      DateFormat df = null;
      
      if(constraint.hasDate() && constraint.hasTime())
      {
        if(_dateTimeFormat == null)
        {
          if (constraint.getPattern() == null)
          {
            _dateTimeFormat = DateFormat.getDateTimeInstance( DateFormat.FULL,
                                                              DateFormat.FULL,
                                                              _locale);
          }
          else
          {
            _dateTimeFormat = new SimpleDateFormat(constraint.getPattern(), _locale);                  
          }
        }
        df = _dateTimeFormat;
      }
      else if(constraint.hasDate())
      {
        if(_dateFormat == null)
        {
          if (constraint.getPattern() == null)
          {
            _dateFormat = DateFormat.getDateInstance( DateFormat.FULL, _locale);
          }
          else
          {
            _dateFormat = new SimpleDateFormat(constraint.getPattern(), _locale);
          }
        }
        df = _dateFormat;
      }
      else if(constraint.hasTime())
      {
        if(_timeFormat == null)
        {
          if (constraint.getPattern() == null)
          {
            _timeFormat = DateFormat.getTimeInstance( DateFormat.FULL, _locale);
          }
          else
          {
            _timeFormat = new SimpleDateFormat(constraint.getPattern(), _locale);
          }
        }
        df = _timeFormat;
      }
      else
      {
        df = null;
      }
      return df;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error getting DateFormat instance",t);
    }
  }

  protected long convertFromGtsDate(long gtsUtc)
  { //20030110AH
    //where utc is actually pst and we end up back with utc if we are lucky
    //if its daylight savings zone .... too bad...
    //and we lose all the innacuracy corrections inherent in the gts utcOffset as well
    //.... what a mess!
    return TimeUtil.utcToLocal(gtsUtc); //Its all quite wrong of course.... :-(
  }

  protected long convertFromGtasDate(long gtasUtc)
  { //20030110AH
    //When implemented will probably just do nothing as its likely the implementation
    //will only be a correction for inacuracies in setting of clock that generated the time
    //stamp - corrections we would want to keep.
    throw new java.lang.UnsupportedOperationException(
      "GTAS format date conversion has not been implemented yet");
  }

  protected String[] processTimeStrings(String[] values, ITimeConstraint constraint)
    throws RenderingException
  { //20030110AH
    //20030117AH - Modified to return new array if values isnt our internal sesa
    try
    {
      if(values == null) throw new NullPointerException("values is null"); //20030416AH
      String[] displayValues = (values == _sesa) ? _sesa : new String[values.length]; //20030117AH
      if(displayValues.length == 0) return displayValues; //20030117AH
      DateFormat df = getDateFormat(constraint);

      String value = null;
      for(int i=0; i < values.length; i++)
      {
        value = values[i];
        _scratchDate = StaticUtils.dateValue(value,_scratchDate);
        if(_scratchDate != null)
        {
          switch(constraint.getAdjustment())
          {
            case ITimeConstraint.ADJ_NONE:
              //No further action required
              break;

            case ITimeConstraint.ADJ_GTS:
              //Perform gts to local conversion. This is a case of cancelling a logic error
              //by applying it backwards... ;-)
              _scratchDate.setTime( convertFromGtsDate(_scratchDate.getTime()) );
              break;

            case ITimeConstraint.ADJ_GTAS:
              //Perform a more appropriate adjustment. :-) (not implemented yet)
              _scratchDate.setTime( convertFromGtasDate(_scratchDate.getTime()) );
              break;

            default:
              //Internal sanity assertion - this should never happen
              throw new java.lang.IllegalStateException("Unrecognised TimeConstraint adjustment type:"
                                                        + constraint.getAdjustment());
          }
          if(df == null)
          {
            displayValues[i] = ""; //20030117AH
          }
          else
          {
            displayValues[i] = DateUtils.formatDateInZone( _scratchDate,_timeZone,_locale,df); //20030117AH
          }
        }
        else
        {
          displayValues[i] = ""; //20030117AH
        }
      }
      return displayValues; //20030117AH
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error formatting date string for display",t);
    }
  }

  /**
   * Get ActionForm the bfpr is using. If overridden will return the set form, otherwise
   * will return action form as per AbstractRenderer.getActionForm
   */
  public ActionForm getActionForm() throws RenderingException
  { //20030122AH - Override getActionForm() in AbstractRenderer
    if(_overrideForm != null)
    {
      return _overrideForm;
    }
    else
    {
      return super.getActionForm();
    }
  }

  /**
   * Set the ActionForm for the next operation. Nb: is NOT cleared when the bfpr is reset.
   * This should be called before any of the setBindings() methods are called so that they
   * pull the value from the correct form
   */
  public void setActionForm(ActionForm form)
  { //20030122AH
    _overrideForm = form;
  }

  public void setIdPrefix(String idPrefix)
  { //20030122AH
    if(idPrefix == null)
    {
      _idPrefix = "";
    }
    else
    {
      _idPrefix = idPrefix;
    }
  }

  public String getIdPrefix()
  { //20030122AH
    return _idPrefix;
  }
  
  protected void renderToSelectionTable() throws RenderingException
  { //20030717AH
    try
    {
      IBFPROptionSource os = getOptionSource();
      if( (os == null) || !(os instanceof IComplexOptionSource) )
      {
        throw new RenderingException("A complex option source is required for"
                        + "rendering to selection tables (os="
                        + os + ")");
      }
      IComplexOptionSource cos = (IComplexOptionSource)os;
      RenderingContext rContext = getRenderingContext();
      SelectionTableRenderer str = getSelectionTableRenderer();
      if( str == null)
      {
        str = new SelectionTableRenderer(rContext);
        setSelectionTableRenderer( str );
      }
      str.reset();
      _valueElement = this.replaceNodeWithNewElement("table", _valueElement, false);
      str.setInsertElement(_valueElement);
      str.setMandatory( isMandatory() );
      str.setFilter( getConstraintFilter() );
      
      str.setTableName( _baseId );
      str.setObjects( cos.getOptions(rContext, this) );
      str.setColumnObjectAdapter( cos.getColumnObjectAdapter(rContext, this) );
      str.setValueRetriever( cos.getOptionValueRetriever(rContext, this) );
      
      String[] valueStrings = null;
      if(_value instanceof String[])
      {
        valueStrings = processStringValues((String[])_value);
        str.setMultiple( true );
      }
      else
      {
        valueStrings = new String[1];
        valueStrings[0] = processStringValue( StaticUtils.stringValue(_value) );
        str.setMultiple( false );
      }
      str.setSelectedItems( valueStrings );
      
      removeAllChildren(_valueElement);
      str.render(_target);
           
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering to selection table",t);
    }
  }
  
  public SelectionTableRenderer getSelectionTableRenderer()
  { //20030717AH
    return _selectionTableRenderer;
  }

  public void setSelectionTableRenderer(SelectionTableRenderer renderer)
  { //20030717AH
    _selectionTableRenderer = renderer;
  }
  
  protected void renderFieldDiversion()
    throws RenderingException
  { //20030826AH
    try
    {
      if(!isVisible()) return;
      if(_entity == null) return;
      if(_fieldId == null) return;
      
      if(_constraintType != IConstraint.TYPE_FOREIGN_ENTITY) return;
      IForeignEntityConstraint constraint = (IForeignEntityConstraint)_constraint;
      if(constraint == null) return;
      
      String fieldId = getIdPrefix() + getBaseId();
      Element divElement = getElementById(fieldId + "_diversion",false);
      if(divElement == null)
      {
        divElement = getElementById(fieldId + "_create",false); //todo: mk backward compat configurable
        if(divElement != null) divElement.setAttribute("id",fieldId + "_diversion");
      }
      FieldDivPath divPath = new FieldDivPath();
      divPath.setFieldName(getBaseId());
      RenderingContext rContext = getRenderingContext();
      if(divElement == null)
      {
        if( (_valueElementType == INodeTypes.TEXT_PARENT) && (_valueElement != null) )
        {
          ActionMapping mapping = rContext.getMapping();
          if (mapping == null)
            throw new NullPointerException("mapping is null");
          String fwdName = "view" + StringUtils.capitalise(constraint.getEntityType());
          ActionForward forward = mapping.findForward(fwdName);
          if(forward != null)
          {
            divPath.setMode(FieldDivPath.MODE_VIEW);
            Element anchor = makeLink(_valueElement, "javascript: divertToMapping('" + divPath.getDivPath() + "');");
          }
        }
      }
      else
      {
        removeAllChildren(divElement);
        Element icon = _target.createElement("img");
        icon.setAttribute("src", DIVERSION_ICON_URL);
        icon.setAttribute("border", "0");
        ISimpleResourceLookup rLookup = rContext.getResourceLookup();
        if (rLookup == null)
          throw new NullPointerException("rLookup is null");
        String targetType = rLookup.getMessage( constraint.getEntityType() );
        String text = rLookup.getMessage("diversion.edit", new String[]{ targetType} );
        icon.setAttribute("alt", text);
        Element anchor = null;
        if("a".equals(divElement.getNodeName()))
        {
          anchor = divElement;
        }
        else
        {
          anchor =  _target.createElement("a");
          divElement.appendChild(anchor); 
        }
        anchor.appendChild(icon);
        anchor.setAttribute("href", "javascript: divertToMapping('" + divPath.getDivPath() + "');");
        
      }

    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering field diversion features",t);
    }
  }
  
  public boolean isFbdEnabled()
  { //20030826AH - Field Based Diversions
    return _fbdEnabled;
  }

  public void setFbdEnabled(boolean fieldBasedDiversions)
  { //20030826AH - Field Based Diversions
    _fbdEnabled = fieldBasedDiversions;
  }

}