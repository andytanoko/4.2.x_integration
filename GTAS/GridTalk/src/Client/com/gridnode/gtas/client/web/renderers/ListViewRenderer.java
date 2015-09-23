/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ListViewRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-14     Daniel J D'Cotta    Created
 * 2002-05-14     Andrew Hill         Modified (get & post urls for create,update,delete,view)
 * 2002-05-18     Andrew Hill         Totally refactored the table rendering logic
 * 2002-06-03     Andrew Hill         Refactored (yet again) to eliminate xmlc library use
 * 2002-08-28     Andrew Hill         Added support for allowsEdit listViewOption property
 * 2002-09-09     Andrew Hill         Refactored again to use EntityListRenderer to render table
 * 2002-11-19     Andrew Hill         Check for null create,delete url and remove buttons if so
 * 2002-11-19     Andrew Hill         Allow EntityListRenderer to be switched with subclass
 * 2002-11-20     Andrew Hill         Use includeJavaScript() for submitMultipleEntities
 * 2002-12-11     Andrew Hill         Refresh support
 * 2003-03-11     Andrew Hill         Include stdFormMethod.js to get benefit of showUpdatingMsg()
 * 2003-03-19     Andrew Hill         Call ieTableHeaderStyleHack() in onload
 * 2003-03-19     Andrew Hill         Track selected items
 * 2003-03-20     Andrew Hill         Pager support
 * 2003-03-24     Andrew Hill         Support for rendering page and row numbers
 * 2003-03-25     Andrew Hill         Magic Link support
 * 2003-03-26     Andrew Hill         Change icon for paging buttons if disabled
 * 2003-04-08     Andrew Hill         Use script urls defined in IGlobals
 * 2003-04-10     Andrew Hill         Append isNotElvDivert flag here
 * 2003-04-14     Andrew Hill         Magic download links support
 * 2003-07-02     Andrew Hill         Support for first, last, and nth page support
 * 2003-07-03     Andrew Hill         No longer support next/prev text - use alttext on icon instead
 * 2003-09-25     Daniel D'Cotta      Limit the number of page quick links rendered
 */
package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.*;
import java.util.*;
import org.apache.struts.action.*;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;

public class ListViewRenderer extends AbstractRenderer
{
  private static final int NO_OF_PAGE_QUICK_LINKS = 5;  // 20030925 DDJ

  private static final String PREV_DISABLED_IMAGE_SRC = "images/arrows/prevDisabled.gif"; //20030326AH
  private static final String NEXT_DISABLED_IMAGE_SRC = "images/arrows/nextDisabled.gif"; //20030326AH

  private static final String FIRST_DISABLED_IMAGE_SRC = "images/arrows/firstDisabled.gif"; //20030702AH
  private static final String LAST_DISABLED_IMAGE_SRC = "images/arrows/lastDisabled.gif"; //20030702AH

  private String[] _selectedUids = null; //200303019AH

  private boolean _isLinkNewWindow = true; //20030312AH

  private IListViewOptions _listOptions;
  private Collection _entities;
  private EntityListRenderer _elr;

  private long _refreshInterval; //20021211AH
  private String _refreshUrl; //20021211AH

  private ActionMapping _mapping; //20030325AH

  private boolean _isMagicDownloadsEnabled; //20030414AH

  public ListViewRenderer(RenderingContext rContext,
                          IListViewOptions listOptions,
                          Collection entities,
                          ActionMapping mapping)
  {
    super(rContext);
    if(listOptions == null) throw new NullPointerException("listOptions is null"); //20030416AH
    if(entities == null) throw new NullPointerException("entities is null"); //20030416AH
    _listOptions = listOptions;
    _entities = entities;
    _mapping = mapping; //20030325AH
  }

  public void setMagicDownloadsEnabled(boolean magicDownloadsEnabled)
  { //20030414AH
    _isMagicDownloadsEnabled = magicDownloadsEnabled;
  }

  public boolean isMagicDownloadsEnabled()
  { //20030414AH
    return _isMagicDownloadsEnabled;
  }

  public void setSelectedUids(String[] selectedUids)
  { _selectedUids = selectedUids; } //20030319AH

  public String[] getSelectedUids()
  { return _selectedUids; } //20030319AH (Why do I bother with the getters here?)

  public void setRefreshInterval(long refreshInterval)
  { _refreshInterval = refreshInterval; }

  public long getRefreshInterval()
  { return _refreshInterval; }

  public boolean isRefreshable()
  { return _refreshUrl != null; }

  public void setRefreshUrl(String refreshUrl)
  { //20030403AH - We no longer support unrefreshable listviews (for now)
    if( StaticUtils.stringEmpty(refreshUrl) )
    { //20030403AH
      throw new UnsupportedOperationException("Unrefreshable listviews are no longer supported - refreshUrl must be set");
    }
    _refreshUrl = refreshUrl;
  }

  public String getRefreshUrl()
  { return _refreshUrl; }

  public void setRenderer(EntityListRenderer renderer)
  {
    _elr = renderer;
  }

  public EntityListRenderer getRenderer()
  {
    return _elr;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      ISimpleResourceLookup rLookup = getRenderingContext().getResourceLookup();
      IURLRewriter urlRewriter = getRenderingContext().getUrlRewriter();
      IColumnEntityAdapter columnAdapter = _listOptions.getColumnAdapter();

      String viewURL    = _listOptions.getViewURL();
      String editURL    = _listOptions.getUpdateURL();
      String createURL  = _listOptions.getCreateURL();
      String deleteURL  = _listOptions.getDeleteURL();

      if(rContext.isExplorer())
      {
        // 20030319AH
        // IE has a problem where it is not applying the css style to all cells in the header
        // row when the page is first shown. The method called here will force ie to redraw the
        // form, and the second time it will get it right.
        appendOnloadEventMethod("void ieTableHeaderStyleHack('listview_form');"); //20030319AH
      }

      // Lookup i18n message for delete confirmation dialog box
      String confirmDeleteMsg = rLookup.getMessage( _listOptions.getConfirmDeleteMsgKey() );
      renderLabelCarefully("listview_heading", _listOptions.getHeadingLabelKey(),false);
      renderLabelCarefully("listview_message",_listOptions.getMessageLabelKey(),false);

      String actionUrl = urlRewriter.rewriteURL(deleteURL); //20030321AH
      renderElementAttribute("listview_form","action",actionUrl); //20030321AH
      if(createURL != null)
      {
        renderLabelCarefully("create",_listOptions.getCreateLabelKey(),false);
        if(_isLinkNewWindow)
        { //200303012AH
          renderElementAttribute("create","target",AnchorConversionRenderer.TARGET_DETAIL_VIEW);
        }
        renderElementAttribute("create","href",urlRewriter.rewriteURL(createURL));
      }
      else
      {
        removeNode("create_details",false);
      }
      if(deleteURL != null)
      {
        renderLabelCarefully("delete_submit",_listOptions.getDeleteLabelKey(),false);
        String deleteActionUrl = StaticWebUtils.addParameterToURL(actionUrl,
                                                                  EntityDispatchAction.IS_NOT_ELV_DIVERT,
                                                                  "true"); //20030410AH
        String deleteSubmitURL = "javascript:submitMultipleEntities('delete','"
                                + confirmDeleteMsg + "','" + deleteActionUrl + "');"; //20030321AH
        renderElementAttribute("delete_submit","href",deleteSubmitURL);
      }
      else
      {
        removeNode("delete_details",false);
      }

      //20030320AH - paging support
      OperationContext opCon = rContext.getOperationContext();
      boolean removePagerButtons = true;
      if(opCon != null)
      {
        IGTListPager pager = (IGTListPager)opCon.getAttribute(EntityListAction.PAGER_ATTRIBUTE);
        if(pager != null)
        {
          //20030703AH - co: renderLabelCarefully("page_next","listview.nextPage",false);
          //20030703AH - co:renderLabelCarefully("page_prev","listview.prevPage",false);
          
          //20030702AH .. first last and nth page support
          int numPages = EntityListAction.calculateNumberOfPages(pager);
          int thisPage = EntityListAction.calculateThisPageNumber(pager);         
          
          Element pageFirst = getElementById("page_first",false);
          if(pageFirst != null)
          {
            replaceTextCarefully(pageFirst,""); //Kill any whitespace
            Element firstIcon = getElementById("first_icon",false);
            if (firstIcon == null)
              throw new NullPointerException("No img element with id='first_icon' found in layout");
            String text = rLookup.getMessage("listview.firstPage");
            if(StaticUtils.stringNotEmpty(text))
              firstIcon.setAttribute("alt",text);
            if(!pager.hasPrev())
            {
              disableLink("page_first",false); 
              if(firstIcon != null)
              { 
                firstIcon.setAttribute("src",FIRST_DISABLED_IMAGE_SRC);
              }
            }
            else
            {
              pageFirst.setAttribute("href", "javascript: void changePage(_refreshUrl,'" + EntityListAction.PAGE_FIRST + "');");
            }
          }
          Element pageLast = getElementById("page_last",false); 
          if(pageLast != null)
          {
            replaceTextCarefully(pageLast,""); //Kill any whitespace
            Element lastIcon = getElementById("last_icon",false);
            if(lastIcon == null)
              throw new NullPointerException("No img element with id='last_icon' found in layout");
            String text = rLookup.getMessage("listview.lastPage");
            if(StaticUtils.stringNotEmpty(text))
              lastIcon.setAttribute("alt",text);
            if(!pager.hasNext())
            {
              disableLink("page_last",false); 
               
              if(lastIcon != null)
              { 
                lastIcon.setAttribute("src",LAST_DISABLED_IMAGE_SRC);
              }
            }
            else
            {
              pageLast.setAttribute("href", "javascript: void changePage(_refreshUrl,'" + EntityListAction.PAGE_LAST + "');");
            }
          }
          Element pageNRoot = getElementById("page_n_parent",false);
          if(pageNRoot != null)
          {
            Element pageN = getElementById("page_n",false);
            if(!"a".equals(pageN.getNodeName()))
              throw new UnsupportedOperationException("Non anchor layout for pageN not supported");
            if (pageN == null)
              throw new NullPointerException("element with id='page_n' is missing");
            Node parent = pageNRoot.getParentNode(); // may be below page_n_parent though
              
            //@todo: limit number of quicklinks for really large result sets  
            //for(int i=1; i <= numPages; i++)

            // 20030925: Limit the number of page quick links rendered
            int qlFirstPage = (thisPage - NO_OF_PAGE_QUICK_LINKS) <= 1         ? 1         : (thisPage - NO_OF_PAGE_QUICK_LINKS);
            int qlLastPage  = (thisPage + NO_OF_PAGE_QUICK_LINKS) >= numPages  ? numPages  : (thisPage + NO_OF_PAGE_QUICK_LINKS);
            for(int i = qlFirstPage; i <= qlLastPage; i++)
            {   
              pageN.setAttribute("id","page_" + i);
              replaceTextCarefully(pageN,"" + i + (i == numPages ? "" : ","));
              pageN.setAttribute("href", "javascript: void changePage(_refreshUrl,'" + i + "');");
              Element pageIRoot = (Element)pageNRoot.cloneNode(true);
              parent.insertBefore(pageIRoot, pageNRoot);
              if( thisPage == i )
              {
                Element pageI = findElement(pageIRoot, "a", null, null );
                if(pageI != null)
                {
                  disableLink(pageI);
                }
              }
            }
            parent.removeChild(pageNRoot);
          }
          //...
          
          if(pager.hasNext())
          {
            Element pageNext = getElementById("page_next",false); //20030703AH
            if(pageNext != null)
            { //20030703AH
              replaceTextCarefully(pageNext,""); //Kill any whitespace
              String text = rLookup.getMessage("listview.nextPage"); //20030703AH
              String url = "javascript: void changePage(_refreshUrl,'"
                          + EntityListAction.PAGE_NEXT + "');"; //20030702AH - Use PAGE_NEXT constant
              pageNext.setAttribute("href",url); //20030703AH
              if(StaticUtils.stringNotEmpty(text))
                renderElementAttribute("next_icon","alt",text); //20030703AH
            }
          }
          else
          {
            disableLink("page_next",false); //20030321AH
            Element nextIcon = getElementById("next_icon",false); //20030326AH
            if(nextIcon != null)
            { //20030326AH
              nextIcon.setAttribute("src",NEXT_DISABLED_IMAGE_SRC);
            }
          }
          if(pager.hasPrev())
          {
            Element pagePrev = getElementById("page_prev",false); //20030703AH
            if(pagePrev != null)
            { //20030703AH
              replaceTextCarefully(pagePrev,"");
              String text = rLookup.getMessage("listview.prevPage");
              String url = "javascript: void changePage(_refreshUrl,'"
                          + EntityListAction.PAGE_PREV + "');"; //20030702AH - Use PAGE_PREV constant
              pagePrev.setAttribute("href",url);
              if(StaticUtils.stringNotEmpty(text))
                renderElementAttribute("prev_icon","alt",text); //20030703AH
            }
          }
          else
          {
            disableLink("page_prev",false); //20030321AH
            Element prevIcon = getElementById("prev_icon",false); //20030326AH
            if(prevIcon != null)
            { //20030326AH
              prevIcon.setAttribute("src",PREV_DISABLED_IMAGE_SRC);
            }
          }
          renderPageNumber(rContext, pager); //20030325AH
          removePagerButtons = (pager.getTotalItemCount() == 0);
        }
      }
      if(removePagerButtons)
      {
        removeNode("page_next_parent",false);
        removeNode("page_prev_parent",false);
        removeNode("row_number",false); //20030324AH
        removeNode("page_number",false); //20030324AH
        removeNode("page_first_parent",false); //20030702AH
        removeNode("page_last_parent",false); //20030702AH
        removeNode("page_n_parent",false); //20030702AH
      }
      //...

      includeJavaScript(IGlobals.JS_LIST_FORM_METHODS); //2002-11-20, 20030408AH
      includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS); //20030311AH, 20030408AH
      includeJavaScript(IGlobals.JS_NAVIGATION_METHODS); //20030312AH , 20030408AH ,(should be in already anyhow...)

      Element table = getElementById("listview_table", true);
      Element headerRow = getElementById("column_header_row", true);
      Element detailRow = getElementById("detail_row", true);
      Element headerCell = getElementById("column_header_cell", true);
      Element selectHeaderCell = getElementById("select_header_cell",true); //20030411AH
      Element editHeaderCell = getElementById("edit_header_cell",true); //20030411AH

      Element selectionCell = getElementById("selection_cell", true);
      Element editCell = getElementById("edit_cell", true);
      Element viewCell = getElementById("viewlink_cell", true);
      Element defaultCell = getElementById("default_cell", true);
      Element selectionInput = getElementById("selection_input", true);
      Element editLink = getElementById("edit_link", true);
      Element viewLink = getElementById("view_link", true);

      // Remove id attributes from the nodes. We don't need them any more as we now have references
      // and we do not want duplicate ids in our output.
      // We shall leave the table id in place as we only have one table. Those elements being used
      // as a 'stamp' will lose their ids.
      headerRow.removeAttribute("id"); //20030411AH
      detailRow.removeAttribute("id"); //20030411AH
      headerCell.removeAttribute("id"); //20030411AH
      selectHeaderCell.removeAttribute("id"); //20030411AH
      editHeaderCell.removeAttribute("id"); //20030411AH
      selectionCell.removeAttribute("id"); //20030411AH
      editCell.removeAttribute("id"); //20030411AH
      viewCell.removeAttribute("id"); //20030411AH
      defaultCell.removeAttribute("id"); //20030411AH
      selectionInput.removeAttribute("id"); //20030411AH
      editLink.removeAttribute("id"); //20030411AH
      viewLink.removeAttribute("id"); //20030411AH

      removeAllChildren(headerRow);
      removeAllChildren(detailRow);
      removeAllChildren(defaultCell);
      removeAllChildren(headerCell);


      EntityListRenderer elr = _elr;
      if(elr == null)
      {
        elr = new EntityListRenderer(rContext, _mapping); //20030325AH
        elr.setMagicLinksEnabled(true); //20030325AH
      }
      elr.setTableName("listview");
      elr.setPrototypeHeaderRow(headerRow);
      elr.setPrototypeBodyRow(detailRow);
      elr.setPrototypeValueCell(defaultCell);
      elr.setPrototypeHeaderCell(headerCell);
      elr.setPrototypeSelectHeaderCell(selectHeaderCell); //20030411AH
      elr.setPrototypeEditHeaderCell(editHeaderCell); //20030411AH
      elr.setPrototypeSelectCell(selectionCell);
      elr.setPrototypeEditCell(editCell);
      elr.setObjects(_entities);
      elr.setListViewOptions(_listOptions);
      elr.setInsertElement(table);
      elr.setLinkNewWindow(_isLinkNewWindow); //20030312AH
      elr.setSelectedItems(_selectedUids); //20030319AH
      elr.setMagicDownloadsEnabled( isMagicDownloadsEnabled() ); //20030414AH
      elr.render(_target);

      renderRefreshFeatures(rContext); //20021211AH
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering listview",t);
    }
  }

  protected void renderRefreshFeatures(RenderingContext rContext) throws RenderingException
  { //20021211AH
    try
    {
      if(isRefreshable())
      {
        String refreshUrl = getRefreshUrl();
        //render hidden field value (for use by delete method) without rewriting
        renderElementAttribute("listview_refresh","name", EntityDispatchAction.REFRESH_LISTVIEW_URL );
        renderElementAttribute("listview_refresh","value",refreshUrl);
        //now rewrite it for use by js
        refreshUrl = rContext.getUrlRewriter().rewriteURL(refreshUrl,false);

        // To make our life simpler we shall write the refreshUrl to a global
        // javascript variable _refreshUrl which is initialiased by a small script block
        // that we write into the head node of the page
        Element headNode = this.findElement(_target,"head",null,null);
        if(headNode == null) throw new NullPointerException("headNode is null"); //20030416AH
        Element grsNode = _target.createElement("script");
        String grs = "\n_refreshUrl='" + refreshUrl + "';\n";
        Comment grsCommentNode = _target.createComment(grs); // Script needs to be in a comment node
        grsNode.appendChild(grsCommentNode);
        headNode.appendChild(grsNode);

        Element manualRefresh = getElementById("manual_refresh");
        if(manualRefresh != null)
        {
          String refreshText = rContext.getResourceLookup().getMessage("listview.refresh");
          replaceTextCarefully(manualRefresh, refreshText);
          manualRefresh.setAttribute("href","javascript: void refreshListview(_refreshUrl);");
        }
        long interval = getRefreshInterval();
        if(interval > 0)
        {
          String script = "window.setTimeout(\"refreshListview(_refreshUrl);\"," + interval + ");";
          appendOnloadEventMethod(script);
        }
      }
      else
      { //20030403AH - Non refreshing listviews have problems with paging for now
        //removeNode("manual_refresh",false);
        throw new UnsupportedOperationException("Non-refreshable listviews are not currently supported by ListViewRenderer");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering listview refresh features",t);
    }
  }

  private void renderPageNumber(RenderingContext rContext, IGTListPager pager)
    throws RenderingException
  { //20030324AH
    try
    {
      Element rowNumberNode = getElementById("row_number",false);
      Element pageNumberNode = getElementById("page_number",false);
      if(pager == null)
      {
        removeNode(rowNumberNode,false);
        removeNode(pageNumberNode,false);
        return;
      }
      else
      {
        if(!(( rowNumberNode==null) || (pageNumberNode==null) ))
        {
          int pageSize = pager.getPageSize();
          int totalRows = pager.getTotalItemCount();
          int startRow = pager.getPageStart();
          int stopRow = startRow + pageSize;

          if( (totalRows == pager.UNKNOWN) || (totalRows == 0) )
          {
            removeNode(rowNumberNode,false);
            removeNode(pageNumberNode,false);
          }
          else
          {
            if(rowNumberNode != null)
            {
              Object[] params = new Object[]{ new Integer(startRow+1),
                                              new Integer((stopRow > totalRows) ? totalRows : stopRow),
                                              new Integer(totalRows) };
              String label = rContext.getResourceLookup().getMessage("listview.row",params);
              replaceTextCarefully(rowNumberNode,label);
            }
            if(pageNumberNode != null)
            {
              int currentPage = EntityListAction.calculateThisPageNumber(pager); //20030702AH
              int totalPages = EntityListAction.calculateNumberOfPages(pager); //20030702AH
              Object[] params = new Object[]{ new Integer(currentPage),
                                              new Integer(totalPages) };
              String label = rContext.getResourceLookup().getMessage("listview.page",params);
              replaceTextCarefully(pageNumberNode,label);
            }
          }
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering page & row numbers",t);
    }
  }
}