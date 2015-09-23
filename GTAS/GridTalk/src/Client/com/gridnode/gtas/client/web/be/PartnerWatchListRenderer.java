/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerWatchList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-21     Daniel D'Cotta      Created
 * 2002-11-01     Andrew Hill         "Performance tuning"
 * 2002-11-20     Daniel D'Cotta      Changed to use Object[] instead of Map
 * 2002-11-22     Andrew Hill         Debugged
 * 2002-11-27     Daniel D'Cotta      Gave an id to table and tbody
 * 2002-12-11     Andrew Hill         Add IFRAME & autorefresh support.
 * 2003-01-16     Andrew Hill         Fixed layout import bug when not framed
 */
package com.gridnode.gtas.client.web.be;

import java.util.ArrayList;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.model.enterprise.IServerWatchlist;

public class PartnerWatchListRenderer extends AbstractRenderer
{
  private static final String IMAGE_URL_ONLINE      = "images/green.gif";
  private static final String IMAGE_URL_OFFLINE     = "images/red.gif";
  private static final String IMAGE_URL_CONNECTING  = "images/amber.gif";
  private static final String IMAGE_URL_EXPIRED     = "images/grey.gif";
  private static final String IMAGE_URL_DETERMINING = "images/blue.gif";
  private static final String IMAGE_URL_UNKNOWN     = IMAGE_URL_DETERMINING;

  private static final String IMAGE_URL_DISCONNECTING = IMAGE_URL_CONNECTING;
  private static final String IMAGE_URL_RECONNECTING  = IMAGE_URL_CONNECTING;

  private static final String IMAGE_URL_OTHER = IMAGE_URL_DETERMINING;

  private boolean _framed; //20021211AH
  private String _watchListUrl; //20021211AH
  private long _refreshInterval; //20021211AH

  public PartnerWatchListRenderer(RenderingContext rContext, String wlUrl, long refreshInterval)
  {
    super(rContext);
    setDynamicWatchListUrl(wlUrl); //20021211AH
    setRefreshInterval(refreshInterval); //20021211AH
  }

  /**
   * Set true if being renderer onto a page of its own for use in an IFRAME
   */
  public void setFramed(boolean framed)
  { _framed = framed; }

  public boolean isFramed()
  { return _framed; }

  public void setDynamicWatchListUrl(String bob)
  { _watchListUrl = bob; }

  public String getDynamicWatchListUrl()
  { return _watchListUrl; }

  public void setRefreshInterval(long interval)
  { _refreshInterval = interval; }

  public long getRefreshInterval()
  { return _refreshInterval; }

  protected Object[] getPartnerWatchListData(RenderingContext rContext, IGTSession gtasSession)
    throws RenderingException
  {
    //20021212 - Mod to throw RenderingException and to have gtasSession passed in
    try //20021212AH - Added trycatch
    {
      IGTPartnerManager manager = (IGTPartnerManager)gtasSession.getManager(IGTManager.MANAGER_PARTNER);
      return manager.getPartnerWatchListData();
    }
    catch(Throwable t)
    {
      throw new RenderingException("Unable to get partner watch list data",t);
    }
  }

  //@todo: refactor in an ELEGANT fashion!!!!
  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();

      //20021211AH - Adapt to use iframe if presented with one!
      if(!isFramed()) //If we arent in a frame maybe we need to prepare one?
      {
        Element insertWatchList = getElementById("insert_partnerWatchList",false);
        if(insertWatchList == null)
        { // If theres nowehere for the pwl to go forget the whole deal!
          return;
        }
        else
        {
          int insertType = getNodeType(insertWatchList);
          if(insertType == INodeTypes.INLINE_FRAME)
          { // Its an iframe. Set its src appropriately!
            prepareInlineFrame(rContext, insertWatchList);
            return;
          }
          else
          { //If we arent framed we need to pull in the layout information into
            //the target
            insertWatchListLayout(rContext); //20030116AH
          }
        }
      }
      //.....

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getSession()); //20021212AH
      ISimpleResourceLookup rLookup = rContext.getResourceLookup();
      Object[] partnerWatchListData = getPartnerWatchListData(rContext,gtasSession);

//DDJ: Testing fake pwl data
/*if(true)
{
  System.out.println("DEBUG: PartnerWatchListRendered.render(): Testing fake pwl data");

  Object[] partner1Details = new Object[4];
  partner1Details[IServerWatchlist.PARTNER_INDEX_ID] = "fake p1 id";
  partner1Details[IServerWatchlist.PARTNER_INDEX_NAME] = "fake p1 name";
  partner1Details[IServerWatchlist.PARTNER_INDEX_STATUS] = IServerWatchlist.STATUS_OFFLINE;

  Object[] partner2Details = new Object[4];
  partner2Details[IServerWatchlist.PARTNER_INDEX_ID] = "fake p2 id";
  partner2Details[IServerWatchlist.PARTNER_INDEX_NAME] = "fake p2 name";
  partner2Details[IServerWatchlist.PARTNER_INDEX_STATUS] = IServerWatchlist.STATUS_OFFLINE;

  Object[] partnersDetails = new Object[2];
  partnersDetails[0] = partner1Details;
  partnersDetails[1] = partner2Details;

  partnerWatchListData = new Object[3];
  partnerWatchListData[IServerWatchlist.MAIN_INDEX_STATUS] = IServerWatchlist.STATUS_OFFLINE;
  partnerWatchListData[IServerWatchlist.MAIN_INDEX_PARTNER] = partnersDetails;
  partnerWatchListData[IServerWatchlist.MAIN_INDEX_GRIDMASTER] = new Object[1];
}*/
//DDJ: End of Testing fake pwl data

      Short connectionStatus = (Short)partnerWatchListData[IServerWatchlist.MAIN_INDEX_STATUS];
      Object[] partnersDetails = (Object[])partnerWatchListData[IServerWatchlist.MAIN_INDEX_PARTNER];
      //Object[] gridMasterDetails = (Object[])partnerWatchListData[IServerWatchlist.MAIN_INDEX_GRIDMASTER];

      String statusString = null;
      if(IServerWatchlist.STATUS_ONLINE.equals(connectionStatus))
      {
        statusString = rLookup.getMessage("gridTalk.status.connection.online");
      }
      else if(IServerWatchlist.STATUS_OFFLINE.equals(connectionStatus))
      {
        statusString = rLookup.getMessage("gridTalk.status.connection.offline");
      }
      else if(IServerWatchlist.STATUS_EXPIRED.equals(connectionStatus))
      {
        statusString = rLookup.getMessage("gridTalk.status.connection.expired");
      }
      else if(IServerWatchlist.STATUS_DETERMINING.equals(connectionStatus))
      {
        statusString = rLookup.getMessage("gridTalk.status.connection.determining");
      }
      else if(IServerWatchlist.STATUS_UNKNOWN.equals(connectionStatus))
      {
        statusString = rLookup.getMessage("gridTalk.status.connection.unknown");
      }
      else if(IServerWatchlist.STATUS_CONNECTING.equals(connectionStatus))
      {
        statusString = rLookup.getMessage("gridTalk.status.connection.connecting");
      }
      else if(IServerWatchlist.STATUS_RECONNECTING.equals(connectionStatus))
      {
        statusString = rLookup.getMessage("gridTalk.status.connection.reconnecting");
      }
      else if(IServerWatchlist.STATUS_DISCONNECTING.equals(connectionStatus))
      {
        statusString = rLookup.getMessage("gridTalk.status.connection.disconnecting");
      }
      else
      {
        Object[] statusArray = new Object[1];
        statusArray[0] = connectionStatus;
        statusString = rLookup.getMessage("gridTalk.status.connection.online",statusArray);
      }
      renderLabel("status_connection_label","gridTalk.status.connection",false);
      replaceText("status_connection_value",statusString,false);

      //20021212AH..... show gridNodeId
      renderLabel("status_gnid_label","gridTalk.status.gnid",false);
      Integer gridNodeId = gtasSession.getGridNodeId();
      String gnid = (gridNodeId == null) ? rLookup.getMessage("gridTalk.status.unregistered")
                                         : gridNodeId.toString();
      replaceText("status_gnid_value",gnid,false);
      //..

      if(isFramed())
      { //20021211AH - Render the code to make it refresh itself automatically
        long interval = getRefreshInterval();
        if(interval > 0)
        {
          appendOnloadEventMethod("setTimeout('history.go();'," + interval + ");");
        }
      }

      if( (partnersDetails==null) || (partnersDetails.length == 0) )
      {
        removeNode("pwl_row_parent", false);
      }
      else
      {
        //20021101AH - Moved binding into else clause so its only done if necessary
        Element tbody = getElementById("pwl_row_parent", true);
        Element row = getElementById("pwl_row", true);
        Element status = getElementById("pwl_status", true);
        Element partner = getElementById("pwl_partner", true);
        // Remove id attributes from the nodes. We don't need them any more as we now have references
        // and we do not want dupicate ids in our output.
        // We shall leave the tbody id in place as we only have one tbody. Those elements being used
        // as a 'stamp' will lose their ids.
        row.getAttributes().removeNamedItem("id");
        status.getAttributes().removeNamedItem("id");
        partner.getAttributes().removeNamedItem("id");
        tbody.removeChild(row);

        for(int i = 0; i < partnersDetails.length; i++)
        {
          Object[] partnerDetail = (Object[])partnersDetails[i];
          String partnerId = (String)partnerDetail[IServerWatchlist.PARTNER_INDEX_ID];
          String partnerName = (String)partnerDetail[IServerWatchlist.PARTNER_INDEX_NAME];
          Short partnerStatus = (Short)partnerDetail[IServerWatchlist.PARTNER_INDEX_STATUS];

          String image = null;
          String altText = null;

          //@todo: use a lookup table
          if(IServerWatchlist.STATUS_ONLINE.equals(partnerStatus))
          {
            image = IMAGE_URL_ONLINE;
            altText = rLookup.getMessage("gridTalk.status.connection.online");
          }
          else if(IServerWatchlist.STATUS_OFFLINE.equals(partnerStatus))
          {
            image = IMAGE_URL_OFFLINE;
            altText = rLookup.getMessage("gridTalk.status.connection.offline");
          }
          else if(IServerWatchlist.STATUS_CONNECTING.equals(partnerStatus))
          {
            image = IMAGE_URL_CONNECTING;
            altText = rLookup.getMessage("gridTalk.status.connection.connecting");
          }
          else if(IServerWatchlist.STATUS_DISCONNECTING.equals(partnerStatus))
          {
            image = IMAGE_URL_DISCONNECTING;
            altText = rLookup.getMessage("gridTalk.status.connection.connecting");
          }
          else if(IServerWatchlist.STATUS_RECONNECTING.equals(partnerStatus))
          {
            image = IMAGE_URL_RECONNECTING;
            altText = rLookup.getMessage("gridTalk.status.connection.connecting");
          }
          else if(IServerWatchlist.STATUS_EXPIRED.equals(partnerStatus))
          {
            image = IMAGE_URL_EXPIRED;
            altText = rLookup.getMessage("gridTalk.status.connection.expired");
          }
          else if(IServerWatchlist.STATUS_DETERMINING.equals(partnerStatus))
          {
            image = IMAGE_URL_DETERMINING;
            altText = rLookup.getMessage("gridTalk.status.connection.determining");
          }
          else if(IServerWatchlist.STATUS_UNKNOWN.equals(partnerStatus))
          {
            image = IMAGE_URL_UNKNOWN;
            altText = rLookup.getMessage("gridTalk.status.connection.unknown");
          }
          else
          {
            Object[] statusArray = new Object[1];
            statusArray[0] = status;
            image = IMAGE_URL_OTHER;
            altText = rLookup.getMessage("gridTalk.status.connection.other",statusArray);
          }
          altText = partnerId + " (" + partnerName + ") -" + altText;
          status.setAttribute("src", image);
          status.setAttribute("alt", altText);
          removeAllChildren(partner);
          partner.appendChild(_target.createTextNode(partnerName));
          tbody.appendChild(row.cloneNode(true));
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering partner watch list",t);
    }
  }

  protected void prepareInlineFrame(RenderingContext rContext,
                                    Element iframe) throws RenderingException
  {
    try
    {
      String wlUrl = getDynamicWatchListUrl();
      wlUrl = StaticWebUtils.addParameterToURL(wlUrl,"refreshInterval","" + getRefreshInterval());
      if(wlUrl == null) throw new NullPointerException("wlUrl is null"); //20030416AH
      wlUrl = rContext.getUrlRewriter().rewriteURL(wlUrl,false);
      iframe.setAttribute("src",wlUrl);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error preparing iframe for partner watch list",t);
    }
  }

  protected void insertWatchListLayout(RenderingContext rContext)
    throws RenderingException
  { //20030116AH
    try
    {
      ArrayList insertions = new ArrayList(1);
      insertions.add( new InsertionDef( "partnerWatchList_content",
                                        "insert_partnerWatchList",
                                        false,
                                        true,
                                        IDocumentKeys.PARTNER_WATCH_LIST,
                                        false) ); //20021101AH - Use MNIRS buildin docloading code
      MultiNodeInsertionRenderer mnir = new MultiNodeInsertionRenderer(rContext,insertions);
      mnir.setNoScream(false);
      mnir.render(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error inserting partnerWatchList layout",t);
    }
  }
}