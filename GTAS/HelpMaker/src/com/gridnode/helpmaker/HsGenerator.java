package com.gridnode.helpmaker;

public class HsGenerator extends FileGenerator
{
  public static final String INDENT = "  ";
  private TocItem _root;

  public HsGenerator(TocItem root)
  {
    this._root = root;
  }

  public void generate(String filename,
      String mapFilename,
      String tocFilename,
      String searchFolder) throws Exception
  {
    Logger.write("Writing HelpSet file to [" + filename + "]");
    createFile(filename);
    writeLine("<?xml version='1.0' encoding='ISO-8859-1'  ?>");
    writeLine("<!DOCTYPE helpset PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN\" \"http://java.sun.com/products/javahelp/helpset_1_0.dtd\">");
    writeLine("<helpset version=\"1.0\">");

    writeLine(INDENT + "<!-- title -->");
    writeLine(INDENT + "<title>" + _root.getText() + "</title>");

    writeLine(INDENT + "<!-- maps -->");
    writeLine(INDENT + "<maps>");
    writeLine(INDENT + INDENT + "<!-- homeID>home</homeID -->");
    writeLine(INDENT + INDENT + "<mapref location=\"" + mapFilename + "\"/>");
    writeLine(INDENT + "</maps>");

    writeLine(INDENT + "<!-- views -->");
    writeLine(INDENT + "<view>");
    writeLine(INDENT + INDENT + "<name>TOC</name>");
    writeLine(INDENT + INDENT + "<label>Table Of Contents</label>");
    writeLine(INDENT + INDENT + "<type>javax.help.TOCView</type>");
    writeLine(INDENT + INDENT + "<data>" + tocFilename + "</data>");
    writeLine(INDENT + "</view>");

    writeLine(INDENT + "<view>");
    writeLine(INDENT + INDENT + "<name>Search</name>");
    writeLine(INDENT + INDENT + "<label>Search</label>");
    writeLine(INDENT + INDENT + "<type>javax.help.SearchView</type>");
    writeLine(INDENT + INDENT + INDENT + "<data engine=\"com.sun.java.help.search.DefaultSearchEngine\">");
    writeLine(INDENT + INDENT + INDENT + searchFolder);
    writeLine(INDENT + INDENT + "</data>");
    writeLine(INDENT + "</view>");

    writeLine("</helpset>");

    close();
  }

}
