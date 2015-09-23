/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRenderingPipeline.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.Document;
 
public interface IRenderingPipeline
{
  /**
   * Will call each renderers render method, passing its output as the input to the next renderer.
   * Renderers are called in the order they were added to the pipeline.
   * @param key of target document
   * @param docMgr the IDocumentManager used to obtain the target document
   */
  public Document render() throws RenderingException;

  public void addRenderer(IDocumentRenderer renderer);

  public void removeRenderer(IDocumentRenderer renderer);
}