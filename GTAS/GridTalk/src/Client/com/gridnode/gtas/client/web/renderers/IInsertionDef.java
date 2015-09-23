package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.Document;

public interface IInsertionDef
{
  public String getFromNodeId();

  public String getToNodeId();

  public boolean isPreserveId();

  public boolean isReplaceNode();

  public Document getSource();

  public String getSourceKey();

  public boolean isCloneRequired();
}