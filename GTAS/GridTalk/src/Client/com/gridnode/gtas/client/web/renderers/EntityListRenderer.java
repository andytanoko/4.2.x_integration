/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityListRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-04     Andrew Hill         Created
 * 2002-09-19     Andrew Hill         Fixed incorrect rendering of uid value in select checkbox
 * 2002-10-09     Andrew Hill         Support IGTEntity.canDelete() check for edit cells
 * 2002-11-13     Andrew Hill         Fix for nested foreign entity reference field bug
 * 2002-11-15     Andrew Hill         Dont render link if getViewUrl()/getEditUrl() returns null
 * 2002-12-12     Andrew Hill         View icon & new window option
 * 2003-01-17     Andrew Hill         Display non-displayable fields with nbsp in td
 * 2003-03-19     Andrew Hill         Support pre-selection of items and use br instead of nbsp
 * 2003-04-08     Andrew Hill         Use script urls from IGlobals
 * 2003-04-11     Andrew Hill         Use a checkbox for selectAll feature
 * 2003-04-14     Andrew Hill         Magic downloads support
 * 2003-04-25     Andrew Hill         Renamed js function 'selectAll' to 'selectAllRows'
 * 2003-06-17     Andrew Hill         No label for select header cell. Use of alt text (incl for e&v imgs)
 * 2003-07-16     Andrew Hill         Refactor to extend SelectionTableRenderer which factors out some code from here
 * 2003-06-28     Andrew Hill         Disable FBD in BFPR 
 */
package com.gridnode.gtas.client.web.renderers;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gridnode.gtas.client.ctrl.IConstraint;
import com.gridnode.gtas.client.ctrl.IForeignEntityConstraint;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;

public class EntityListRenderer extends SortedTableRenderer
{
  //20030425AH - Renamed js function to 'selectAllRows' to fix conflict with selectUtils.js
  protected static final String SELECT_ALL_FN_NAME = "selectAllRows"; //20030411AH, 20030425AH
  protected static final String VIEW_IMG_SRC = "images/actions/view.gif"; //20030319AH

  private boolean _isLinkNewWindow; //20021212AH

  public static final int REF_MODE_UID = 0;
  public static final int REF_MODE_FUID = 1;
  public static final int REF_MODE_INDEX = 2;

  protected BindingFieldPropertyRenderer _bfpr = null;

  protected IListViewOptions _listViewOptions;

  protected Element _prototypeEditCell;
  protected Element _prototypeEditHeaderCell;

  protected int _refMode = REF_MODE_UID;

  protected ActionMapping _mapping; //20030325AH
  protected boolean _isMagicLinksEnabled = false; //20030325AH
  protected boolean _isMagicDownloadsEnabled = false; //20030414AH

  public EntityListRenderer(RenderingContext rContext, ActionMapping mapping)
  {
    super(rContext);
    _mapping = mapping; //20030325AH
  }

  public void reset()
  {
    super.reset();
    _listViewOptions = null;
    if(_bfpr != null) _bfpr.reset();
  }

  public void setMagicLinksEnabled(boolean magicLinksEnabled)
  { //20030325AH
    _isMagicLinksEnabled = magicLinksEnabled;
  }

  public boolean isMagicLinksEnabled()
  { //20030325AH
    return _isMagicLinksEnabled;
  }

  public void setMagicDownloadsEnabled(boolean magicDownloadsEnabled)
  { //20030414AH
    _isMagicDownloadsEnabled = magicDownloadsEnabled;
  }

  public boolean isMagicDownloadsEnabled()
  { //20030414AH
    return _isMagicDownloadsEnabled;
  }

  public void setLinkNewWindow(boolean lnw) //200212AH
  { _isLinkNewWindow = lnw; }

  public boolean isLinkNewWindow() //20021212AH
  { return _isLinkNewWindow; }

  public void setReferenceMode(int refMode)
  { _refMode = refMode; }

  public int getReferenceMode()
  { return _refMode; }

  public void setBindingFieldPropertyRenderer(BindingFieldPropertyRenderer bfpr)
  {
    _bfpr = bfpr;
  }

  public BindingFieldPropertyRenderer getBindingFieldPropertyRenderer()
  { return _bfpr; }

  public void setPrototypeEditCell(Element element)
  {
    assertElementType(element,"td");
    _prototypeEditCell = element;
  }

  public Element getPrototypeEditCell()
  { return _prototypeEditCell; }

  public void setPrototypeEditHeaderCell(Element element)
  {
    assertElementType(element,"th");
    _prototypeEditHeaderCell = element;
  }

  public Element getPrototypeEditHeaderCell()
  { return _prototypeEditHeaderCell; }

  public void setListViewOptions(IListViewOptions listViewOptions)
  {
    if(listViewOptions == null) throw new NullPointerException("listViewOptions is null"); //20030416AH
    _listViewOptions = listViewOptions;
    IColumnObjectAdapter adapter = listViewOptions.getColumnAdapter();
    if(adapter == null) throw new NullPointerException("adapter is null"); //20030416AH
    setColumnObjectAdapter(adapter);
    
    // 20031019 DDJ: Added setSelectable() & setSortable()
    setSelectable(listViewOptions.isAllowsSelection()); 
    setSortable(listViewOptions.isAllowsSorting());
  }

  public IListViewOptions getListViewOptions()
  { return _listViewOptions; }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      if(_bfpr == null)
      {
        _bfpr = new BindingFieldPropertyRenderer(rContext);
      }
      if(_isLinkNewWindow)
      { //20021212AH
        includeJavaScript(IGlobals.JS_NAVIGATION_METHODS); //20030408AH
      }
      if(_selectedItems != null)
      { //20030319AH
        appendOnloadEventMethod("void highlightRows('" + _tableName + "_body');");
      }
      super.render();
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering object table",t);
    }
  }

  protected void decorateBodyCell(RenderingContext rContext,Element td,int col,int rowCount,Object object)
    throws RenderingException
  {
    if(col == 0)
    {
      Element row = (Element)td.getParentNode();

      if(_listViewOptions.isAllowsSelection())
      {
        super.decorateBodyCell(rContext, td, col, rowCount, object); //20030716AH
      }
      if(_listViewOptions.isAllowsEdit())
      {
        if(_listViewOptions.getUpdateURL() != null)
        { //20021114 - Allows edit button to be removed independent of allowsEdit()
          Element editCell = createEditCell(rContext, rowCount, object);
          if(editCell != null)
          {
            row.insertBefore(editCell,td);
          }
        }
      }
      String viewUrl = getViewUrl(rContext, rowCount, object); //20021115AH
      if(viewUrl != null)
      {
        makeContentsViewLink(rContext, td, rowCount , object, viewUrl);
      }
    }
    else if(isMagicLinksEnabled())
    { //20030325AH - Magic Link Support
      try
      {
        IColumnEntityAdapter adapter = (IColumnEntityAdapter)getColumnObjectAdapter();
        if(adapter.isColumnLinkEnabled(object,col)) //20050315AH
        {
          IGTFieldMetaInfo fmi = adapter.getColumnMetaInfo(object,col);
          if(fmi.getConstraintType() == IConstraint.TYPE_FOREIGN_ENTITY)
          {
            makeContentsMagicLink(rContext, td, rowCount, col, object, fmi);
          }
        }
      }
      catch(Throwable t)
      {
        throw new RenderingException("Error rendering cell - magicLink check",t);
      }
    }
  }

  protected Element makeContentsMagicLink(RenderingContext rContext,
                                          Element td,
                                          int rowCount,
                                          int col,
                                          Object object,
                                          IGTFieldMetaInfo fmi)
    throws RenderingException
  { //20030325AH
    if(_mapping == null)
    {
      throw new NullPointerException("Internal assertion failed: _mapping == null");
    }
    IForeignEntityConstraint constraint = (IForeignEntityConstraint)fmi.getConstraint();
    String type = StaticUtils.capitalise(constraint.getEntityType());
    String forwardName = "view" + type;
    ActionForward forward = _mapping.findForward(forwardName);
    if(forward == null)
    {
      return null;
    }
    try
    {
      IColumnEntityAdapter adapter = (IColumnEntityAdapter)getColumnObjectAdapter();
      String keyField = constraint.getKeyFieldName();
      String keyValue = StaticUtils.stringValue( adapter.getColumnValue(object, col) );

      if(StaticUtils.stringEmpty(keyValue)) return null;

      String viewUrl = StaticWebUtils.addParameterToURL(forward.getPath(),"keyField",keyField);
      viewUrl = StaticWebUtils.addParameterToURL(viewUrl,keyField,keyValue);
      Element a = _target.createElement("a");
      if(_isLinkNewWindow)
      { //20030312AH
        a.setAttribute("target",AnchorConversionRenderer.TARGET_DETAIL_VIEW);
      }

      a.setAttribute("href", rContext.getUrlRewriter().rewriteURL( viewUrl ) );
      NodeList children = td.getChildNodes();
      if(children.getLength() == 0)
      {
        Node text = _target.createTextNode( rContext.getResourceLookup().getMessage("listview.view") );
        a.appendChild(text);
      }
      else
      {
        for(int i=0; i < children.getLength(); i++)
        { //copy all the children of the td into the a
          Node child = children.item(i);
          a.appendChild(child);
        }
      }
      td.appendChild(a); //and then append the a to the td
      return a;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error making cell contents a magic link",t);
    }
  }

  protected Element makeContentsViewLink(RenderingContext rContext,
                                      Element td,
                                      int rowCount,
                                      Object object,
                                      String viewUrl)
    throws RenderingException
  {
    Element a = _target.createElement("a");
    if(_isLinkNewWindow)
    { //20030312AH
      a.setAttribute("target",AnchorConversionRenderer.TARGET_DETAIL_VIEW);
    }
    a.setAttribute("href", viewUrl);
    NodeList children = td.getChildNodes();
    if(children.getLength() == 0)
    {
      Node text = _target.createTextNode( rContext.getResourceLookup().getMessage("listview.view") );
      a.appendChild(text);
    }
    else
    {
      for(int i=0; i < children.getLength(); i++)
      { //copy all the children of the td into the a
        Node child = children.item(i);
        a.appendChild(child);
      }
    }
    td.appendChild(a); //and then append the a to the td
    insertViewIcon(rContext, a, rowCount , object, viewUrl); //20021212AH
    return a;
  }

  protected Element insertViewIcon( RenderingContext rContext,
                                    Element a,
                                    int rowCount,
                                    Object object,
                                    String viewUrl)
    throws RenderingException
  {
    Element img = _target.createElement("img");
    img.setAttribute("src",     VIEW_IMG_SRC );
    img.setAttribute("border",  "0" );
    img.setAttribute("style",   "vertical-align: text-top; margin-right: 3px;" );
    img.setAttribute("width",   "16" );
    img.setAttribute("height",  "16" );
    //20030617AH - Alt text for view icon
    String altText = rContext.getResourceLookup().getMessage("listview.viewThis");
    img.setAttribute("alt",altText);
    //...
    NodeList children = a.getChildNodes();
    if(children.getLength() == 0)
    {
      a.appendChild(img);
    }
    else
    {
      a.insertBefore(img,a.getFirstChild());
    }
    return a;
  }

  protected String getViewUrl(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    String ref = getObjectReference(rowCount,object);
    String href = rContext.getUrlRewriter().rewriteURL(_listViewOptions.getViewURL(),false);
    return StaticWebUtils.addParameterToURL(href,"uid",ref);
  }

  protected void decorateHeaderCell(RenderingContext rContext,Element td,int col)
    throws RenderingException
  {
    super.decorateHeaderCell(rContext, td, col); // 20031019 DDJ 
    if(col == 0)
    {
      Element row = (Element)td.getParentNode();
      //if(_listViewOptions.isAllowsSelection())
      //{
      //  super.decorateHeaderCell(rContext, td, col); //20030716AH
      //}
      if(_listViewOptions.isAllowsEdit())
      {
        if(_listViewOptions.getUpdateURL() != null) //20021114AH - Make edit cell confindep of allowsEdit() //20030221AH - "confindep"???? Huh?
        {
          Element editHeaderCell = createEditHeaderCell(rContext);
          if(editHeaderCell != null)
          {
            row.insertBefore(editHeaderCell,td);
          }
        }
      }
    }
  }

  protected Element createSelectCell(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    //@todo: make use of commanalities with the new superclass impl
    try
    {
      if(object == null) throw new java.lang.NullPointerException("Null entity object for rowCount=" + rowCount);
      Element selectCell = null;
      Element input = null;
      if(_prototypeSelectCell == null)
      {
        selectCell = _target.createElement("td");
      }
      else
      {
        selectCell = (Element)_prototypeSelectCell.cloneNode(true);
      }
      String checkboxValue = getSelectCheckboxValue(rContext,rowCount,object); //20030319AH
      input = getChildElement("input",selectCell);
      if(input == null)
      {
        input = _target.createElement("input");
        switch(_refMode)
        {
          case REF_MODE_UID:
            input.setAttribute("name","uid");
            break;

          case REF_MODE_FUID:
            input.setAttribute("name","fuid");
            break;

          case REF_MODE_INDEX:
            input.setAttribute("name","index");
            break;

          default:
            throw new java.lang.IllegalArgumentException("Unknown referenceMode (" + _refMode + ")");
        }
        input.setAttribute("type","checkbox");
        if(_tableName != null)
        {
          input.setAttribute("onclick","void highlightRows('" + _tableName + "_body');");
        }
        selectCell.appendChild(input);
      }
      input.setAttribute("value", checkboxValue ); //20030319AH
      if(_selectedItems != null)
      { //20030319AH
        if(StaticUtils.arrayContains(_selectedItems, checkboxValue))
        {
          input.setAttribute("checked","checked");
        }
      }
      return selectCell;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating select cell",t);
    }
  }

  protected String getSelectCheckboxValue(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    switch(_refMode)
    {
      case REF_MODE_UID:
      case REF_MODE_FUID:
        return "" + ((IGTEntity)object).getUid();

      case REF_MODE_INDEX:
        return "" + rowCount;

      default:
        throw new java.lang.IllegalArgumentException("Unknown referenceMode (" + _refMode + ")");
    }
  }

  protected Element createEditCell(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    try
    {
      Element editCell = null;
      Element a = null;
      if(_prototypeEditCell == null)
      {
        editCell = _target.createElement("td");
      }
      else
      {
        editCell = (Element)_prototypeEditCell.cloneNode(true);
      }
      String editUrl = getEditUrl(rContext, rowCount, object);
      if( (editUrl != null) && ((IGTEntity)object).canEdit() ) //20021115AH - Dont render link if url null
      {
        a = getChildElement("a",editCell);
        if(a == null)
        {
          a = _target.createElement("a");
          String label = rContext.getResourceLookup().getMessage("listview.edit");
          replaceText(a,label);
        }
        if(_isLinkNewWindow)
        { //20030312AH
          a.setAttribute("target",AnchorConversionRenderer.TARGET_DETAIL_VIEW);
        }
        a.setAttribute("href", editUrl);
        //20030617AH - Alt text for edit image
        Element editImg = findElement(a,"img",null,null);
        if(editImg != null)
        {
          String altText = rContext.getResourceLookup().getMessage("listview.editThis");
          editImg.setAttribute("alt",altText);
        }
        //
      }
      else
      {
        removeAllChildren(editCell);
        /*Node nbsp = _target.createEntityReference("nbsp");
        editCell.appendChild(nbsp);*/
        Node br = _target.createElement("br"); //20030321AH
        editCell.appendChild(br); //20030321AH
      }
      return editCell;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating edit cell",t);
    }
  }

  protected String getEditUrl(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    String updateUrl = _listViewOptions.getUpdateURL();
    if(updateUrl == null) return null;
    String href = rContext.getUrlRewriter().rewriteURL(updateUrl,false);
    String ref = getObjectReference(rowCount,object);
    return StaticWebUtils.addParameterToURL(href,"uid",ref);
  }

  protected Element createSelectHeaderCell(RenderingContext rContext)
    throws RenderingException
  { //@todo: make use of commanalities with the new superclass impl
    try
    {
      String tbodyId = _tableName + "_body"; //20030411AH
      String selectAllId = tbodyId + "_selectAll"; //20030411AH
      String selectAllName = "selectAll " + _tableName; //20030411AH
      String selectAllFunction = SELECT_ALL_FN_NAME + "('" + tbodyId + "');"; //20030411AH
      Element selectHeaderCell = null;
      Element selectAll = null;
      if(_prototypeSelectHeaderCell == null)
      {
        selectHeaderCell = _target.createElement("th");
        selectAll = _target.createElement("input"); //20030411AH
        selectAll.setAttribute("class", "checkbox"); //20030411AH
      }
      else
      {
        selectHeaderCell = (Element)_prototypeSelectHeaderCell.cloneNode(true);
        selectAll = findElement(selectHeaderCell,"input","type","checkbox");
      }
      String selectAllText = rContext.getResourceLookup().getMessage("listview.select"); //20030617AH
      if(selectAll != null)
      { //20030411AH - Use a checkbox for selectAll
        selectAll.setAttribute("type", "checkbox");
        selectAll.setAttribute("onclick", selectAllFunction);
        selectAll.setAttribute("id", selectAllId);
        selectAll.setAttribute("name",selectAllName);
        selectAll.setAttribute("value","true");
        selectAll.setAttribute("title",selectAllText); //20030617AH
      }
      //20030617AH - commentout -renderLabelCarefully(selectHeaderCell,"listview.select"); //20030411AH
      replaceTextCarefully(selectHeaderCell,""); //20030617AH
      return selectHeaderCell;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating select header cell",t);
    }
  }

  protected Element createEditHeaderCell(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      Element editHeaderCell = null;
      if(_prototypeEditHeaderCell == null)
      {
        editHeaderCell = _target.createElement("th");
      }
      else
      {
        editHeaderCell = (Element)_prototypeEditHeaderCell.cloneNode(true);
      }
      renderLabelCarefully(editHeaderCell, "listview.edit"); //20030411AH
      return editHeaderCell;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating edit header cell",t);
    }
  }

  protected void appendBodyCell(RenderingContext rContext,
                                  Element row,
                                  int col,
                                  int rowCount,
                                  Object value,
                                  Object object)
    throws RenderingException
  { //20030117AH - Modified to render non-displayable fields better
/*System.out.println("appendBodyCell value[" + value
+ "] (class=" + StaticUtils.getObjectClassName(value) + ")"
+ " object=" + object);*/
    Element td = createBodyCell(rContext,col,rowCount,object);
    IGTEntity entity = null; //20030117AH
    IGTFieldMetaInfo fmi = null; //20030117AH
    boolean isDisplayable = false;
    try
    { //20030117AH
      entity = ((IColumnEntityAdapter)_adapter).getColumnEntity(object,col); //20021113AH
      if(entity == null) throw new NullPointerException("entity is null"); //20030416AH
      fmi = ((IColumnEntityAdapter)_adapter).getColumnMetaInfo(object,col);
      if(fmi == null) throw new NullPointerException("fmi is null"); //20030416AH
      isDisplayable = fmi.isDisplayable(entity.isNewEntity());
    }
    catch(Throwable t)
    { //20030117AH
      throw new RenderingException("Error getting field metainfo for column "
          + col + " of object " + object + " in row number " + rowCount,t);
    }
    /*if( ("".equals(value)) || (null == value) || (isDisplayable == false) )  //20030117AH
    {
      Node nbsp = _target.createEntityReference("nbsp");
      td.appendChild(nbsp);
    }*/
    String strValue = StaticUtils.stringValue(value);
//System.out.println("col:" + col + " value=>" + value + "<");
    if( StaticUtils.stringEmpty(strValue) || (isDisplayable == false) )  //20030310AH
    { //20030311AH - For some reason this no longer works - the nbsp doesnt make it into the
      //serialized response. Using the DOMViewer I was able to ascertain that the nbsp node is still
      //in the DOM - but the XHTMLSerializer is no longer writing it out! It used to work, now it
      //doesnt & nothing has changed that should affect it. Spooky. I havent time to look at this
      //further right now however. :-(
//System.out.println("Empty cell in col:" + col);
//com.gridnode.gtas.client.web.xml.DomViewer._evilFlag = true;
      //EntityReference nbsp = _target.createEntityReference("nbsp");
      //td.appendChild(nbsp);
      td.appendChild( _target.createElement("br") ); //20030313AH
    }
    else
    {
//System.out.println("not empty col:" + col + " value=" + value);
      try
      {
        _bfpr.reset();
        _bfpr.setFbdEnabled(false); //20030826AH
        _bfpr.bindToElements(null,null,td,null);
        _bfpr.setFieldId(fmi.getFieldId());
        _bfpr.setBoundEntity(entity);
        if(value instanceof Boolean)
        {
          value = StaticUtils.stringValue(value);
        }
        _bfpr.setValue(value);
        _bfpr.setVisible(true); //20030117AH
        _bfpr.setEditable(false);
        _bfpr.setMandatory(false);
        _bfpr.setConstraint(fmi.getConstraint());
        if(isMagicLinksEnabled() && isMagicDownloadsEnabled() )
        { //20030414AH
          _bfpr.setAlwaysUseMultifiles(true);
        }

        _bfpr.render(_target);
      }
      catch(Throwable t)
      {
        throw new RenderingException("Error rendering column value for column "
          + col + " of object " + object + " in row number " + rowCount + " using BFPR",t);
      }
    }
    row.appendChild(td);
//System.out.println("row dump before decorate:");
//com.gridnode.gtas.client.web.xml.DomViewer.dumpNode(row);
//System.out.println("\n\n");
    decorateBodyCell(rContext,td,col,rowCount,object);
  }

  protected String getObjectReference(int rowCount, Object object)
    throws RenderingException
  {
    switch(_refMode)
    {
      case REF_MODE_UID:
      case REF_MODE_FUID:
        return "" + ((IGTEntity)object).getUid();

      case REF_MODE_INDEX:
        return "" + rowCount;

      default:
        throw new java.lang.IllegalArgumentException("Unknown referenceMode (" + _refMode + ")");
    }
  }
}