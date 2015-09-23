/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.Document;

/**
 * Interface for the helper classes used to modify DOMs representing the application view.
 */
public interface IDocumentRenderer
{
  /**
   * Invoke rendering to the target document and return resulting document.
   * nb: In almost all cases the resulting document IS the (modified) target document. For efficiency
   * reasons it is not cloned by this method
   * @param targetDocument
   * @return targetDocument with modifications
   * @throws RenderingException
   */
  public Document render(Document targetDocument) throws RenderingException;
}