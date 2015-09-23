/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DomViewer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-29     Andrew Hill         Created
 * 2003-03-11     Andrew Hill         dumpNode()
 */
package com.gridnode.gtas.client.web.xml;

import javax.swing.*;
import javax.swing.tree.*;
import org.w3c.dom.*;
import java.awt.*;

/**
 * Class useful in development/debugging.
 * Provides a quick&dirty view of a dom in a jtree.
 */
public class DomViewer extends JPanel
{
  public static boolean _evilFlag = false;

  private DefaultTreeModel _model;
  GridBagLayout _gridBagLayout1 = new GridBagLayout();
  JScrollPane _scrollPane = new JScrollPane();
  JTree _tree = new JTree();


  public DomViewer()
  {
    try
    {
      jbInit();
      _tree.putClientProperty("JTree.lineStyle","Angled");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void displayInFrame(Document doc)
  {
    refresh(doc);
    JFrame frame = new JFrame(this.getClass().getName());
    frame.setSize(800,600);
    frame.getContentPane().add(this);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }

  public void refresh(Document doc)
  {
    _model = new DefaultTreeModel( buildTreeNode(doc) );
    _tree.setModel(_model);
  }

  private DefaultMutableTreeNode buildTreeNode(Node docNode)
  {
    String nodeText = docNode.toString() + " " + getNodeAttributeText(docNode);
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(nodeText);
    NodeList docNodeChildren = docNode.getChildNodes();
    for(int i=0; i < docNodeChildren.getLength(); i++)
    {
      Node childDocNode = docNodeChildren.item(i);
      DefaultMutableTreeNode childTreeNode = buildTreeNode(childDocNode);
      treeNode.add(childTreeNode);
    }
    return treeNode;
  }

  private String getNodeAttributeText(Node node)
  {
    StringBuffer b = new StringBuffer("[");
    NamedNodeMap attributeMap = node.getAttributes();
    if(attributeMap == null) return "";
    for(int i=0; i < attributeMap.getLength(); i++)
    {
      Node attribute = attributeMap.item(i);
      b.append("," + attribute.getNodeName() + "=" + attribute.getNodeValue());
    }
    b.append("]");
    return b.toString();
  }

  private void jbInit() throws Exception
  {
    this.setLayout(_gridBagLayout1);
    this.add(_scrollPane,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    _scrollPane.getViewport().add(_tree, null);
  }

  public static void dumpNode(Node node)
  { //20030311AH
    dumpNodeInternal(node, 0);
  }

  protected static void dumpNodeInternal(Node node, int indent)
  {
    for(int i=0; i < indent; i++) System.out.print(">");
    StringBuffer buffer = new StringBuffer();
    buffer.append("Type=");
    buffer.append(node.getNodeType());
    buffer.append(" Name=");
    buffer.append(node.getNodeName());
    buffer.append(" value=");
    buffer.append(node.getNodeValue());
    System.out.println(buffer.toString());

    NodeList kids = node.getChildNodes();
    for(int j=0; j < kids.getLength(); j++)
    {
      dumpNodeInternal(kids.item(j), indent+1);
    }
  }

}