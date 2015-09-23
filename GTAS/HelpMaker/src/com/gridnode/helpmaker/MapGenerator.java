package com.gridnode.helpmaker;

import java.util.*;

public class MapGenerator extends FileGenerator
{
  public static final String INDENT = "  ";
  public static final String TGT_OPENBOOK = "openbook";
  public static final String TGT_CHAPTER = "chapter";
  public static final String TGT_TOPIC = "topic";

  private TocItem _root;

  public MapGenerator(TocItem root)
  {
    this._root = root;

  }

  public void generate(String filename) throws Exception
  {
    Logger.write("Writing Map file to [" + filename + "]");
    createFile(filename);
    writeLine("<?xml version='1.0' encoding='ISO-8859-1'  ?>");
    writeLine("<!DOCTYPE map PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp Map Version 1.0//EN\" \"http://java.sun.com/products/javahelp/map_1_0.dtd\">");
    writeLine("<map version=\"1.0\">");
    writeLine();

    writeLine(INDENT + "<mapID target=\""+ TGT_OPENBOOK +"\" url=\"images/openbook.gif\" />");
    writeLine(INDENT + "<mapID target=\""+ TGT_CHAPTER +"\" url=\"images/chapTopic.gif\" />");
    writeLine(INDENT + "<mapID target=\""+ TGT_TOPIC +"\" url=\"images/topic.gif\" />");
    writeLine();

    write(_root, INDENT);

    writeLine("</map>");

    close();
  }

  private void write(TocItem item, String indent) throws Exception
  {
    StringBuffer buf = new StringBuffer(256);
    buf.append(indent).append("<mapID target=\"").append(item.getTarget())
        .append("\" url=\"").append(item.getUrl()).append("\" />");
    writeLine(buf.toString());

    Vector targetIds = item.getContentTarget();
    String trgId = null;
    if(targetIds != null)
    {
      for(int i = 0; i < targetIds.size(); i++)
      {
        trgId = (String)targetIds.get(i);
        buf = new StringBuffer(256);
        buf.append(indent).append("<mapID target=\"").append(trgId)
            .append("\" url=\"").append(item.getUrl()).append('#').append(trgId)
            .append("\" />");
        writeLine(buf.toString());
      }
    }
    else
    {
        System.out.println("No content target Ids for " + item.getText());
    }

    Enumeration children = item.children();
    while(children.hasMoreElements())
    {
      write((TocItem)children.nextElement(), indent);
    }

  }

}
