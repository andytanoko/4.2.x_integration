package com.gridnode.helpmaker;

import java.util.*;

public class TocGenerator
    extends FileGenerator
{
  public static final String INDENT = "  ";
  private TocItem _root;
  public TocGenerator(TocItem root)
  {
    this._root = root;
  }

  public void generate(String filename) throws Exception
  {
    Logger.write("Writing TOC file to [" + filename + "]");
    createFile(filename);

    writeLine("<?xml version='1.0' encoding='ISO-8859-1'  ?>");
    writeLine("<!DOCTYPE toc PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp TOC Version 1.0//EN\" \"http://java.sun.com/products/javahelp/toc_1_0.dtd\">");
    writeLine("<toc version=\"1.0\">");

    write(_root, INDENT, 1);

    writeLine("</toc>");

    close();
  }

  private void write(TocItem item, String indent, int level) throws Exception
  {
    if(level == 2)
    {
      writeLine("");
      writeLine("<!-- Chapter " + item.getChapter() + " -->");
    }

    StringBuffer buf = new StringBuffer(256);
    buf.append(indent).append("<tocitem text=\"").append(item.getText())
        .append("\" image=\"").append(item.getImage()).append("\" target=\"")
        .append(item.getTarget());

    if(item.isLeaf())
    {
      buf.append("\"/>");
    }
    else
    {
      buf.append("\">");
    }
    writeLine(buf.toString());

    Enumeration children = item.children();
    while(children.hasMoreElements())
    {
      write((TocItem)children.nextElement(), indent + INDENT, level + 1);
    }

    if(!item.isLeaf())
    {
      writeLine(indent + "</tocitem>");
    }
  }
}
