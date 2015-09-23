/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MultiNodeInsertionRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-30     Andrew Hill         Created
 * 2002-10-21     Andrew Hill         Also get cloned document
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
 
public class MultiNodeInsertionRenderer extends AbstractRenderer
{
  private Collection _insertions; // Collection of IInsertionDef objects
  private boolean _noScream = false;

  public MultiNodeInsertionRenderer(RenderingContext rContext,
                                    Collection insertions)
  {
    super(rContext);
    if(insertions == null) throw new java.lang.NullPointerException("No insertions specified");
    _insertions = insertions;
  }

  public void setNoScream(boolean noScream)
  {
    _noScream = noScream;
  }

  public boolean isNoScream()
  {
    return _noScream;
  }

  protected void render() throws RenderingException
  {
    RenderingContext rContext = getRenderingContext();
    Iterator i = _insertions.iterator();
    while(i.hasNext())
    {
      try
      {
        IInsertionDef iDef = (IInsertionDef)i.next();
        Document document = iDef.getSource();
        if(document == null)
        {
          String documentKey = iDef.getSourceKey();
          if(documentKey != null)
          {
            if("_target".equals(documentKey))
            {
              document = _target;
            }
            else
            {
              document = rContext.getDocumentManager().getDocument(documentKey,iDef.isCloneRequired());
            }
          }
          else
          {
            throw new RenderingException("No source document was specified");
          }
        }
        if(document != null)
        {
          boolean removeId = !iDef.isPreserveId();
          Node targetNode = getElementById(iDef.getToNodeId(),!isNoScream());
          Node sourceNode = getElementByAttributeValue(iDef.getFromNodeId(), "id", document);
          if(iDef.isReplaceNode())
          {
            if(sourceNode != null)
            {
              importAndSubstitute(sourceNode,targetNode,removeId);
            }
            else
            {
              this.removeNode(targetNode, false);
            }
          }
          else
          {
            if(sourceNode != null)
            {
              sourceNode = sourceNode.cloneNode(true);
              sourceNode = _target.importNode(sourceNode,true);
              targetNode.appendChild(sourceNode);
            }
            else
            {
              removeAllChildren(targetNode);
            }
          }
        }
        else
        {
          if(!isNoScream())
          {
            throw new RenderingException("No source document for MultiNodeInsertionRendering");
          }
        }
      }
      catch(Exception e)
      {
        if(!isNoScream())
        {
          if(e instanceof RenderingException)
            throw (RenderingException)e;
          else
            throw new RenderingException("Error performing MultiNodeInsertion Operation",e);
        }
      }
    }
  }


}