/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HighlighterCreationFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.xml.sax.Attributes;

public class HighlighterCreationFactory extends AbstractObjectCreationFactory
{
  public Object createObject(Attributes attributes) throws Exception
  {
    //@todo: actually make use of the type attribute!!!
    //(Im in kinda a hurry right now!)
    String type = attributes.getValue("type");
    if("com.gridnode.gtas.client.web.navigation.Highlighter".equals(type))
    {
      return new com.gridnode.gtas.client.web.navigation.Highlighter();
    }
    else if("com.gridnode.gtas.client.web.bp.ProcessInstanceHighlighter".equals(type))
    {
      return new com.gridnode.gtas.client.web.bp.ProcessInstanceHighlighter();
    }
    else
    {
      throw new UnsupportedOperationException("Unsupported highlighter type \"" + type +"\"");
    }
  }
}