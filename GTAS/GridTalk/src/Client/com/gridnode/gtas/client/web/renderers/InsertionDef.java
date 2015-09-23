package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.Document;

public class InsertionDef implements IInsertionDef
{
  private String _fromNodeId;
  private String _toNodeId;
  private boolean _preserveId;
  private boolean _replaceNode;
  private Document _document;
  private String _documentKey;
  private boolean _cloneRequired = false;

  public InsertionDef(String fromNodeId,
                      String toNodeId,
                      boolean preserveId,
                      boolean replaceNode,
                      String documentKey,
                      boolean cloneRequired)
  {
    this(fromNodeId, toNodeId, preserveId, replaceNode, (Document)null);
    _documentKey = documentKey;
    _cloneRequired = cloneRequired;
  }

  public InsertionDef(String fromNodeId,
                      String toNodeId,
                      Document document)
  {
    this(fromNodeId, toNodeId, true, false, document);
  }

  public InsertionDef(String fromNodeId,
                      String toNodeId,
                      boolean preserveId,
                      boolean replaceNode,
                      Document document)
  {
    _fromNodeId = fromNodeId;
    _toNodeId = toNodeId;
    _preserveId = preserveId;
    _replaceNode = replaceNode;
    _document = document;
    _documentKey = null;
    _cloneRequired = false;
  }

  public String toString()
  {
    return "InsertionDef[ "
            + "fromId:" + _fromNodeId
            + " toId:" + _toNodeId
            + " preserveId=" + _preserveId
            + " replaceNode=" + _replaceNode
            + " document=" + _document
            + " documentKey=" + _documentKey
            + " cloneRequired=" + _cloneRequired
            + " ]";
  }

  public String getFromNodeId()
  {
    return _fromNodeId;
  }
  public String getToNodeId()
  {
    return _toNodeId;
  }
  public boolean isPreserveId()
  {
    return _preserveId;
  }
  public boolean isReplaceNode()
  {
    return _replaceNode;
  }
  public Document getSource()
  {
    return _document;
  }

  /**
   * As an alternative to specifying document in constructor, the key may be specified.
   * It is the responsibility of the class using InsertionDef to check this and load document
   * appropriately.
   */
  public String getSourceKey()
  {
    return _documentKey;
  }

  public boolean isCloneRequired()
  {
    return _cloneRequired;
  }
}