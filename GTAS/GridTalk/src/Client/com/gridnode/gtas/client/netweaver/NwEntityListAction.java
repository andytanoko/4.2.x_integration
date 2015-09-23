/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-11     Andrew Hill         Created
 * 2002-10-21     Daniel D'Cotta      Added Partner Watch List
 * 2002-11-19     Andrew Hill         Correctly handle null forwards
 * 2002-12-11     Andrew Hill         Modify to support the diabolical auto-refresh feature
 * 2003-03-20     Andrew Hill         Support for Listview Paging (finally!)
 * 2003-03-21     Andrew Hill         Removed Partner Watch List
 *                                    Finalise getListItems() to force use of pager
 * 2003-03-25     Andrew Hill         Pass ActionMapping to renderer
 * 2003-04-03     Andrew Hill         Drop support for non-refreshable listviews (for now)
 * 2003-04-14     Andrew Hill         Magic downloads support
 * 2003-06-06     Andrew Hill         Improve error handling (expect errors from getListItems)
 * 2003-07-02     Andrew Hill         Support for going to first, last, nth page
 * 2003-07-08     Andrew Hill         Eliminate deprecated getColumns() method
 * 2003-08-18     Andrew Hill         New methodology for looking up the crud forwards
 */
package com.gridnode.gtas.client.netweaver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.navigation.NavigationRenderer;
import com.gridnode.gtas.client.web.renderers.BaseTagRenderer;
import com.gridnode.gtas.client.web.renderers.ColumnEntityAdapter;
import com.gridnode.gtas.client.web.renderers.IColumnEntityAdapter;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.IListViewOptions;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.ISimpleResourceLookup;
import com.gridnode.gtas.client.web.renderers.IURLRewriter;
import com.gridnode.gtas.client.web.renderers.InsertionDef;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.ListViewRenderer;
import com.gridnode.gtas.client.web.renderers.MultiNodeInsertionRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingPipelineImpl;
import com.gridnode.gtas.client.web.renderers.URLRewriterImpl;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.web.xml.IDocumentManager;
import com.gridnode.gtas.client.web.strutsbase.*;

/**
 * Abstract superclass for Entity list views.
 * Subclasses need to implement some simple methods to return information specifying
 * both the type of entity and the columns for the view. Other methods may be overridden
 * also to provide customisation of the view.
 * @todo - 20030321AH - We are now using an opCon to support paging. As such we could reduce a lot
 * of the work for refreshing by storing the refresh parameters in the opCon and just passing
 * around the opConId only - saving us having to append all the params each time etc...
 */
public abstract class NwEntityListAction extends GTActionBase
{
  //20030702AH - Made constants public:
  public static final String PAGER_ATTRIBUTE = "pager"; //20030320AH
  public static final String PAGE_NEXT = "next"; //20030320AH
  public static final String PAGE_PREV = "prev"; //20030320AH
  public static final String PAGE_FIRST = "first"; //20030702AH
  public static final String PAGE_LAST = "last"; //20030702AH
  public static final String PAGE = "page"; //20030320AH
  public static final String REFRESH = "refresh"; //20030321AH
  //..

  private static final String REFRESH_FORWARD_NAME = "refresh";

  protected abstract int getManagerType(ActionContext actionContext);
  protected abstract String getResourcePrefix(ActionContext actionContext);

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
  }

  protected abstract Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException; //20030708AH

  public final ActionForward execute(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  {
    ActionContext ac = new ActionContext(mapping, actionForm, request, response);
    return execute(ac);
  }

  protected OperationContext getListOpCon(ActionContext actionContext)
    throws GTClientException
  { //20030320AH
    try
    {
      OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
      if(opCon == null)
      {
        opCon = new OperationContext();
        OperationContext.saveOperationContext(opCon, actionContext.getRequest());
      }
      return opCon;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting listview opCon",t);
    }
  }

  protected ActionForward execute(ActionContext actionContext)
    throws IOException, ServletException, GTClientException
  {
    try
    {
     //20030605AH - Trap and store for display any errors that occur trying to get the
     //list items (usually) from gtas
      Collection listItems = null;
      try
      {
        listItems = getListItems(actionContext);
      }
      catch(Throwable t)
      {
        actionContext.getLog().error("getListItems() failed! ", t);
        listItems = Collections.EMPTY_LIST;
        saveExceptions(actionContext, t, true); //Save an exception. The ccr should pick this up and render it
      }
      IListViewOptions listOptions = getListOptions(actionContext);
      IRenderingPipeline rPipe = createRenderingPipeline(actionContext,listOptions, listItems);
      actionContext.getRequest().setAttribute(IRequestKeys.RENDERERS, rPipe);

      return actionContext.getMapping().findForward(IGlobals.VIEW_FORWARD);
    }
    catch(Exception exception)
    {
      throw new GTClientException("Error preparing "
        + getResourcePrefix(actionContext) + " list view for rendering",exception);
    }
  }

  protected IColumnEntityAdapter getColumnEntityAdapter(ActionContext actionContext)
    throws GTClientException
  {
    IGTManager manager = getManager(actionContext);
    String entityType = getResourcePrefix(actionContext);
    Object[] columns = getColumnReferences(actionContext);
    return new ColumnEntityAdapter(columns,manager,entityType);
  }

  protected IGTListPager getListPager(ActionContext actionContext) throws GTClientException
  { //20030321AH - Subclass may override to return filtered pager if necessary
    try
    {
      IGTManager manager = getManager(actionContext);
      IGTListPager pager = manager.getListPager();
      return pager;
    }
    catch(Throwable t)
    {
      throw new GTClientException("EntityListAction encountered error attempting to get ListPager",t);
    }
  }

  protected final Collection getListItems(ActionContext actionContext)
    throws GTClientException
  { //20030320AH - Support for paging
    try
    {
      OperationContext opCon = getListOpCon(actionContext);
      if(opCon == null)
      {
        throw new NullPointerException("Null OperationContext");
      }
      IGTListPager pager = (IGTListPager)opCon.getAttribute(PAGER_ATTRIBUTE);
      if(pager == null)
      {
        try
        {
          pager = getListPager(actionContext);
        }
        catch(Throwable t)
        {
          throw new GTClientException(this.getClass().getName()
                    + ".getListItems encountered error calling getListPager()",t);
        }
        if(pager == null)
        {
          throw new NullPointerException("getListpager() returned null");
        }
        opCon.setAttribute(PAGER_ATTRIBUTE, pager);
      }
      
      setPagerPage(actionContext, pager); //20030702AH
      
      String refresh = actionContext.getRequest().getParameter(REFRESH);
      if("true".equals(refresh)) freePager(actionContext, pager); //20030321AH, 20030324AH
      Collection retList = null;
      try
      {
        retList = pager.getPage();
        if(retList == null)
        {
          throw new NullPointerException("pager.getPage() returned null");
        }
      }
      catch(Throwable t)
      {
        throw new GTClientException("getListItems encountered error calling ListPager.getPage()",t);
      }
      return retList;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting list items",t);
    }
  }
  
  protected void setPagerPage(ActionContext actionContext,
                                IGTListPager pager)
    throws GTClientException
  { //20030702AH
    String page = actionContext.getRequest().getParameter(PAGE);
    try
    {
      if(StaticUtils.stringNotEmpty(page))
      {
        if(PAGE_NEXT.equals(page))
          nextPage(actionContext, pager); //20030324AH, 20030702AH - Moved into this method
        else if(PAGE_PREV.equals(page))
          prevPage(actionContext, pager); //20030324AH, 20030702AH - Moved into this method
        else if(PAGE_FIRST.equals(page))
          firstPage(actionContext, pager); //20030702AH
        else if(PAGE_LAST.equals(page))
          lastPage(actionContext, pager); //20030702AH
        else
        {
          int pageNumber = 0;
          try
          {
            pageNumber = Integer.parseInt(page);
          }
          catch(NumberFormatException nfe)
          {
            throw new GTClientException("Bad page value:" + page,nfe);
          }
          nPage(actionContext, pager, pageNumber);
        }
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error setting pager where " + PAGE + "=" + page,t);
    }
  }
  
  protected void nPage(ActionContext actionContext, IGTListPager pager, int page)
  { //20030702AH. nb: pages param start at 1 to be consistent with ui
    page = page < 1 ? 0 : page - 1; //Re-align to zero index
    int startRow = pager.getPageSize() * page;
    if(startRow > pager.getTotalItemCount())
    {
      int lastPage = calculateNumberOfPages(pager) - 1;
      nPage( actionContext, pager, lastPage );
    }
    else
    {
      pager.setPageStart(startRow);
    }
  }

  protected void nextPage(ActionContext actionContext, IGTListPager pager) throws GTClientException
  {
    pager.nextPage();
  }

  protected void prevPage(ActionContext actionContext, IGTListPager pager) throws GTClientException
  {
    pager.prevPage();
  }
  
  protected void firstPage(ActionContext actionContext, IGTListPager pager) throws GTClientException
  { //20030702AH
    nPage(actionContext, pager, 0);
  }
  
  protected void lastPage(ActionContext actionContext, IGTListPager pager) throws GTClientException
  { //20030702AH
    int lastPage = calculateNumberOfPages(pager);
    nPage( actionContext, pager, lastPage );
  }

  protected void freePager(ActionContext actionContext, IGTListPager pager) throws GTClientException
  {
    pager.free();
  }

  protected IListViewOptions getListOptions(ActionContext actionContext)
    throws GTClientException
  {
    ActionMapping mapping = actionContext.getMapping();
    String resourcePrefix = getResourcePrefix(actionContext);
    ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
    listOptions.setColumnAdapter(getColumnEntityAdapter(actionContext));
    /*20030818AH - co: ActionForward viewForward   = mapping.findForward("invokeView");
    ActionForward updateForward = mapping.findForward("invokeUpdate");
    ActionForward createForward = mapping.findForward("invokeCreate");
    ActionForward deleteForward = mapping.findForward("submit"); //HACK 20021120AH
    if(deleteForward == null)
    {
      deleteForward = mapping.findForward("invokeDelete");
    }*/

    //20030818AH - New methodology of looking up the paths...
    boolean isLvm = mapping instanceof GTListViewActionMapping;
    GTListViewActionMapping m = isLvm ? (GTListViewActionMapping)mapping : null; //For syntactical sugaring purposes ;-)
    ActionForward viewForward   = mapping.findForward( isLvm ? m.getViewForward() : GTListViewActionMapping.DEFAULT_VIEW_FORWARD );
    ActionForward updateForward = mapping.findForward( isLvm ? m.getUpdateForward() : GTListViewActionMapping.DEFAULT_UPDATE_FORWARD );
    ActionForward createForward = mapping.findForward( isLvm ? m.getCreateForward() : GTListViewActionMapping.DEFAULT_CREATE_FORWARD );
    ActionForward deleteForward = mapping.findForward("submit"); //HACK 20021120AH (20030818AH - Why?????)
    if(deleteForward == null)
    {
      deleteForward = mapping.findForward( isLvm ? m.getDeleteForward() : GTListViewActionMapping.DEFAULT_DELETE_FORWARD );
    }
    //...

    if(viewForward != null)
    {
      listOptions.setViewURL(viewForward.getPath());
    }
    else
    {
      listOptions.setViewURL(null);
    }

    if(createForward != null)
    {
      listOptions.setCreateURL(createForward.getPath());
    }
    else
    {
      listOptions.setCreateURL(null);
    }

    if(updateForward != null)
    {
      listOptions.setUpdateURL(updateForward.getPath());
    }
    else
    {
      listOptions.setUpdateURL(null);
    }

    if(deleteForward != null)
    {
      listOptions.setDeleteURL(deleteForward.getPath());
    }
    else
    {
      listOptions.setDeleteURL(null);
    }

    listOptions.setCreateLabelKey(resourcePrefix + ".listview.create");
    listOptions.setDeleteLabelKey(resourcePrefix + ".listview.delete");
    listOptions.setConfirmDeleteMsgKey(resourcePrefix + ".listview.confirmDelete");
    listOptions.setHeadingLabelKey(resourcePrefix + ".listview.heading");
    
    //TEST
    listOptions.setAllowsSorting(false);
    listOptions.setAllowsEdit(false);
    listOptions.setAllowsSelection(false);
    listOptions.setViewURL(null);
    
    return listOptions;
  }

  protected IGTManager getManager(ActionContext actionContext)
    throws GTClientException
  {
    IGTSession gtasSession = getGridTalkSession(actionContext.getRequest());
    return gtasSession.getManager(getManagerType(actionContext));
  }

  protected IRenderingPipeline createRenderingPipeline(ActionContext actionContext,
                                                       IListViewOptions listOptions,
                                                       Collection listItems)
    throws GTClientException
  {
    HttpServletRequest request = actionContext.getRequest();
    ISimpleResourceLookup rLookup = createResourceLookup(actionContext);
    IDocumentManager docMgr = getDocumentManager();
    IURLRewriter urlRewriter = new URLRewriterImpl(actionContext.getResponse(),
                                                   request.getContextPath());
    OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest()); //20030320AH
    ActionErrors errors = getActionErrors(actionContext.getRequest()); //20030606AH
    RenderingContext rContext = new RenderingContext( opCon,
                                                      docMgr,
                                                      rLookup,
                                                      urlRewriter,
                                                      errors,
                                                      actionContext.getRequest(),
                                                      getServlet().getServletContext());

    ArrayList insertions = new ArrayList(1);
    insertions.add( new InsertionDef( "listview_content",
                                      "insert_page", true, false,
                                      IDocumentKeys.TEMPLATE_LISTVIEW,
                                      false) );
    MultiNodeInsertionRenderer templateRenderer = new MultiNodeInsertionRenderer(rContext,insertions);
    //TEST
//    NavigationRenderer navRenderer = new NavigationRenderer(rContext,
//                                                            getProcessedPath(actionContext),
//                                                            actionContext.getMapping()); //20030310AH

    BaseTagRenderer baseTagRenderer = new BaseTagRenderer(request, "base_tag");
    IDocumentRenderer listViewRenderer = getListViewRenderer(actionContext,
                                                              rContext,
                                                              listOptions,
                                                              listItems);

    if(listViewRenderer instanceof ListViewRenderer)
    { //20021211AH
      prepareRefreshSettings(actionContext, (ListViewRenderer)listViewRenderer);

    }

    RenderingPipelineImpl rPipe = new RenderingPipelineImpl(IDocumentKeys.TEMPLATE, docMgr);
    rPipe.addRenderer(templateRenderer);
    rPipe.addRenderer(baseTagRenderer);
    rPipe.addRenderer(listViewRenderer);
    //TEST
//    rPipe.addRenderer(navRenderer);
    //20030321AH - no more pwl - rPipe.addRenderer(partnerWatchListRenderer);
    addCommonContentRenderer(actionContext, rContext, rPipe);

    processPipeline( actionContext, rContext, rPipe); // Hook for subclass to decorate

    return rPipe;
  }

  protected IDocumentRenderer getListViewRenderer(ActionContext actionContext,
                                                  RenderingContext rContext,
                                                  IListViewOptions listOptions,
                                                  Collection listItems)
    throws GTClientException
  {
    ListViewRenderer listViewRenderer = new ListViewRenderer( rContext,
                                                              listOptions,
                                                              listItems,
                                                              actionContext.getMapping()); //20030325AH
    listViewRenderer.setMagicDownloadsEnabled(false); //20030414AH
    return listViewRenderer;
  }

  /**
   * Returns the number of milliseconds between auto-refreshes of the listview. (Client polling)
   * Return >0 to request that the renderer adds javascript to make the list refresh itself
   * automatically.
   * Will check the refreshInterval propery of the GTListViewActionMapping for this listview. If not
   * using a GTListViewActionMapping class for your ActionMapping it returns 0.
   * If you need to control the refresh time programatically then override this method in the subclass.
   * 20021211AH
   * @param actionContext
   * @return milliseconds
   * @throws GTClientException
   */
  protected long getRefreshInterval(ActionContext actionContext) throws GTClientException
  {
    ActionMapping mapping = actionContext.getMapping();
    if(mapping instanceof GTListViewActionMapping)
    {
      return ((GTListViewActionMapping)mapping).getRefreshInterval();
    }
    else
    {
      return 0;
    }
  }

  /**
   * If extra parameters need to be appended to the form submitted in a refresh in order to
   * refresh the 'same' view you need to override this to return a map of them.
   * If none are necessary then return a null value.
   * These parameters will be appended to the url to which the listviews form is submitted on
   * a refresh thus allowing us to preserve them in the absence of an OperationContext.
   * NB: If your listview uses query parameters to specify special filtering conditions then you WILL
   * need to override this method. If you dont then you will lose those parameters on the first
   * refresh, most likely resulting in the user being unwillingly 'transported' over to the listview
   * showing everything (depending on how you structured the subclass of course).
   * Default behaviour as defined here is just to return the refreshUrl unchanged.
   * 20021211AH
   * @param actionContext
   * @return refreshUrl with parameters appended
   * @throws GTClientException
   */
  protected String appendRefreshParameters( ActionContext actionContext,
                                            String refreshUrl)
    throws GTClientException
  {
    return refreshUrl;
  }

  /**
   * Will lookup the url to which the listviews form should be submitted when a refresh is desired.
   * (This is the plain url before appending any filtering parameters etc...)
   * This is read from the refreshUrl property of the GTListViewActionMapping bean. If you arent
   * using this class for your action mapping will return null.
   * If you need programatic control over the url then override this method in the subclass.
   * 20021211AH
   * 20030403AH - There are problems with paging where refresh isnt supported so we no longer
   * support unrefreshable listviews (for now)
   * @param actionContext
   * @return refresh submit url (null to disable refresh feature)
   * @throws GTClientException
   */
  protected String getRefreshUrl(ActionContext actionContext) throws GTClientException
  {
    ActionMapping mapping = actionContext.getMapping();
    if(mapping == null) throw new NullPointerException("mapping is null"); //20030403AH
    if(mapping instanceof GTListViewActionMapping)
    {
      String refreshUrl = ((GTListViewActionMapping)mapping).getRefreshUrl(); //20030403AH
      if(refreshUrl == null)
      { //20030403AH - refreshUrl is now compulsary as we need it for paging as well
        throw new NullPointerException("GTListViewActionMapping does not have value set for refreshUrl");
      }
      return refreshUrl;
    }
    else
    { //20030403AH - No longer supported
      throw new IllegalStateException("ActionMapping is not a GTListViewActionMapping "
                                      + "and getRefreshUrl has not been overridden in ListAction");
    }
  }

  /**
   * Call appropriate setters on the ListViewRenderer to supply it with info necessary to
   * render stuff for refresh
   * @param actionContext
   * @param listViewRenderer
   * @throws GTClientException
   */
  protected void prepareRefreshSettings(ActionContext actionContext, ListViewRenderer lvr)
    throws GTClientException
  {
    try
    {
      //20021211AH - Set refresh info into LVR. We do this here to maintain compatibility with
      //code that overrides getListViewRenderer() which would have to be modified otherwise.
      //Subclass may override this if it wants to do things differently in this regard.
      String[] selectedUids = actionContext.getRequest().getParameterValues("uid");
      lvr.setSelectedUids(selectedUids); //20030319AH
      long refreshInterval = getRefreshInterval(actionContext);
      String refreshUrl = getRefreshUrl(actionContext);
      if(refreshUrl == null)
      { //20030403AH
        throw new UnsupportedOperationException("Non-refreshable listviews are not currently supported."
                                                + " getRefreshUrl() may not return null");
      }
      refreshUrl = appendRefreshParameters(actionContext, refreshUrl);
      lvr.setRefreshInterval(refreshInterval);
      lvr.setRefreshUrl(refreshUrl);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error preparing settings for listview refresh feature",t);
    }
  }

  public static int calculateNumberOfPages(IGTListPager pager)
  { //20030702AH
    int totalRows = pager.getTotalItemCount();
    int pageSize = pager.getPageSize();
    int totalPages = totalRows / pageSize + ((totalRows % pageSize)>0 ? 1 : 0);
    return totalPages;
  }
  
  public static int calculateThisPageNumber(IGTListPager pager)
  { //20030702AH (first page is 1 to be consistent with ui)
    int startRow = pager.getPageStart();
    int pageSize = pager.getPageSize();
    int currentPage = (startRow / pageSize) + 1;
    return currentPage;
  } 
}

