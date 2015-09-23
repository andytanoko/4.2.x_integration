/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RenderingPipelineImpl.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-31     Andrew Hill         Created
 * 2002-11-07     Andrew Hill         Added AnchorConverter as intrisic Pipeline feature!
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.*;
import org.w3c.dom.Document;

import com.gridnode.gtas.client.web.xml.IDocumentManager;

public class RenderingPipelineImpl implements IRenderingPipeline
{
  private ArrayList _renderers;
  private String _targetKey;
  private IDocumentManager _docMgr;
  private boolean _convertAnchors = true;

  public void setConvertAnchors(boolean convertAnchors)
  {
    _convertAnchors = convertAnchors;
  }

  public boolean isConvertAnchors()
  {
    return _convertAnchors;
  }

  public RenderingPipelineImpl(String targetKey, IDocumentManager docMgr)
  {
    if( (docMgr == null) && (targetKey != null))
      throw new java.lang.NullPointerException("No document manager specified");
    _renderers = new ArrayList();
    _targetKey = targetKey;
    _docMgr = docMgr;
  }

  public void addRenderer(IDocumentRenderer renderer)
  {
    _renderers.add(renderer);
  }

  public void removeRenderer(IDocumentRenderer renderer)
  {
    _renderers.remove(renderer);
  }

  public Document render() throws RenderingException
  {
    try
    {
      Document targetDocument = null;
      if(_targetKey != null)
      {
        try
        {
          targetDocument = _docMgr.getDocument(_targetKey, true);
          if(targetDocument == null)
          {
            throw new java.lang.NullPointerException("DocumentManager returned null document "
                                                     + "for key " + _targetKey);
          }
        }
        catch(Exception e)
        {
          throw new RenderingException("Unable to obtain dom of target document " + _targetKey,e);
        }
      }
      Iterator i = _renderers.iterator();
      int r = 0;
      while(i.hasNext())
      {
        IDocumentRenderer renderer = (IDocumentRenderer)i.next();
        if(renderer == null)
        {
          throw new java.lang.NullPointerException("Renderer " + r + " in pipeline is null");
        }
        targetDocument = renderer.render(targetDocument);
        if(targetDocument == null)
        {
          throw new java.lang.NullPointerException("Renderer "
                                                    + r + " in pipeline returned null Document");
        }
        r++;
      }
      if(_convertAnchors)
      { //20021107AH
        AnchorConversionRenderer anchorConverter = new AnchorConversionRenderer();
        targetDocument = anchorConverter.render(targetDocument);
      }
      return targetDocument;
    }
    catch(RenderingException re)
    {
      throw re;
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering pipeline",e);
    }
  }
}