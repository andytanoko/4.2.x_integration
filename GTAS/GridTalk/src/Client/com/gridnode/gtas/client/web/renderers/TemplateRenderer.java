/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TemplateRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-14     Andrew Hill         Created
 * 2002-05-30     Andrew Hill         Refactored to eliminate xmlc library use
 * 2002-10-25     Daniel D'Cotta      Added Partner Watch List
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Document;
 
/**
 * Use a MultiNodeInsertionRenderer instead
 * @deprecated Use a MultiNodeInsertionRenderer
 */
public class TemplateRenderer extends AbstractRenderer
{
  private String _pageKey;
  private String _pageContentId;
  private String _navigatorKey;
  private String _navigatorContentId;
  private String _partnerWatchListKey;
  private String _partnerWatchListContentId;
  private String _headerKey;
  private String _headerContentId;
  private String _insertPageId = "insert_page";
  private String _insertNavigatorId = "insert_navigator";
  private String _insertPartnerWatchListId = "insert_partnerWatchList";
  private String _insertHeaderId = "insert_header";
  private Collection _insertions;

  public TemplateRenderer(RenderingContext rContext,
                          String pageKey,
                          String pageContentId)
  {
    this(rContext, pageKey, pageContentId, null);
  }

  public TemplateRenderer(RenderingContext rContext,
                          String pageKey,
                          String pageContentId,
                          Collection insertions)
  {
    this(rContext, pageKey, pageContentId, null, null, null, null, null, null, insertions);
  }

  public TemplateRenderer(RenderingContext rContext,
                          String pageKey,
                          String pageContentId,
                          String navigatorKey,
                          String navigatorContentId,
                          String partnerWatchListKey,
                          String partnerWatchListContentId,
                          String headerKey,
                          String headerContentId,
                          Collection insertions)
  {
    super(rContext);
    _pageKey = pageKey;
    _pageContentId = pageContentId;
    _navigatorKey = navigatorKey;
    _navigatorContentId = navigatorContentId;
    _partnerWatchListKey = partnerWatchListKey;
    _partnerWatchListContentId = partnerWatchListContentId;
    _headerKey = headerKey;
    _headerContentId = headerContentId;
    if(insertions == null)
      _insertions = new ArrayList();
    else
      _insertions = insertions;
  }

  protected void render() throws RenderingException
  {
    RenderingContext rContext = getRenderingContext();

    IInsertionDef pageIDef = getInsertionDef(_pageKey, _pageContentId, _insertPageId);
    if(pageIDef != null)
    {
      _insertions.add(pageIDef);
    }
    else
    {
      removeAllChildren(_insertPageId,false);
    }

    IInsertionDef navigatorIDef = getInsertionDef(_navigatorKey, _navigatorContentId, _insertNavigatorId);
    if(navigatorIDef != null)
    {
      _insertions.add(navigatorIDef);
    }
    else
    {
      removeAllChildren(_insertNavigatorId,false);
    }

    IInsertionDef partnerWatchListIDef = getInsertionDef(_partnerWatchListKey, _partnerWatchListContentId, _insertPartnerWatchListId);
    if(partnerWatchListIDef != null)
    {
      _insertions.add(partnerWatchListIDef);
    }
    else
    {
      removeAllChildren(_insertPartnerWatchListId,true);
    }

    IInsertionDef headerIDef = getInsertionDef(_headerKey, _headerContentId, _insertHeaderId);
    if(headerIDef != null)
    {
      _insertions.add(headerIDef);
    }
    else
    {
      removeAllChildren(_insertHeaderId, false);
    }

    if(!_insertions.isEmpty())
    {
      MultiNodeInsertionRenderer mnir = new MultiNodeInsertionRenderer(rContext, _insertions);
      mnir.setNoScream(false);
      mnir.render(_target);
    }
  }

  private IInsertionDef getInsertionDef(String documentKey, String contentId, String insertionId)
    throws RenderingException
  {
    RenderingContext rContext = getRenderingContext();
    if(contentId != null)
    {
      Document doc = null;
      if(documentKey != null)
      {
        try
        {
          doc = rContext.getDocumentManager().getDocument(documentKey, false);
        }
        catch(Exception e)
        {
          throw new RenderingException("Unable to find document using key "
                                        + documentKey,e);
        }
      }
      else
      {
        doc = _target;
      }
      return new InsertionDef(contentId, insertionId, doc);
    }
    return null;
  }

}