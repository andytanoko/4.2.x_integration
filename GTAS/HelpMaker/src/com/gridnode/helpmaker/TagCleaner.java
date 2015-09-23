package com.gridnode.helpmaker;

/**
 * Title:        Help Maker
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      GridNode
 * @author Ferry
 * @version 1.0
 */

/**
 * A class to clean unnecessary tags and characters
 */
public class TagCleaner
{

  public TagCleaner()
  {
  }

  /**
   * cleans tags without content
   * e.g: <a><b></b></a>
   */
  public static String cleanEmpty(String old)
  {
    String temp = old;

    //skip if it's an image tag since image tags may not have any content
    if(old.indexOf("img") != -1)
    {
      return old;
    }

    StringBuffer str = new StringBuffer(old);
    while(old.indexOf("<") != -1)
    {
//      System.out.println(old);
      str.delete(old.indexOf("<"), old.indexOf(">") + 1);
      old = str.toString();
    }
    while(old.indexOf("&nbsp;") != -1)
    {
      int tempIndex = old.indexOf("&nbsp;");
      str.delete(tempIndex, tempIndex + 6);
      old = str.toString();
    }

    // if the content is not empty, return the original string instead
    if(old.trim().equals(""))
    {
      return "";
    }
    else
    {
      return temp;
    }
  }

  /**
   * clean <span> tags since most of it would do things that can be replaced
   * using css.
   */
  public static String cleanSpanTag(String old)
  {
    String result = old;

    //skip if it's an image tag since image tags may not have any content
    if(old.indexOf("img") != -1)
    {
      return old;
    }

    StringBuffer str = new StringBuffer(old);
    int spaceCount = 0;
    while(old.indexOf("<") != -1)
    {
//      System.out.println(old);
      str.delete(old.indexOf("<"), old.indexOf(">") + 1);
      old = str.toString();
    }
    while(old.indexOf("&nbsp;") != -1)
    {
      str.delete(old.indexOf("&nbsp;"), old.indexOf("&nbsp;") + 6);
      old = str.toString();
      spaceCount++;
    }

    // if the content is empty, add back the spaces inside instead
    if(old.trim().equals(""))
    {
      result = "";
      for(int i = 0; i < spaceCount; i++)
      {
        result += "&nbsp;";
      }
    }
    return result;
  }

  /**
   * clean style attributes since most of it would do things that can be replaced
   * using css.
   */
  public static String cleanStyle(String old)
  {
    StringBuffer str = new StringBuffer(old);
    int index;

    while((index = old.indexOf("style=")) != -1)
    {
      str.delete(index, index + 6);
      old = str.toString();
      if((index = old.indexOf("'")) != -1)
      {
        str.delete(index, index + old.substring(index + 1).indexOf("'") + 2);
        old = str.toString();
      }
    }
    return old;
  }

  public static String cleanStyle2(String old)
  {
    StringBuffer str = new StringBuffer(old);
    int index, endIndex;

    while((index = old.indexOf("style=")) != -1)
    {
      endIndex = old.indexOf("'", index +7);
      if(endIndex != -1)
      {
        str.delete(index, endIndex+1);
      }
      else
      {
        str.delete(index, index + 6);
      }
      old = str.toString();
    }
    return cleanTag(old);
  }

  /**
   * clean ID attributes
   */
  public static String cleanID(String old)
  {
    StringBuffer str = new StringBuffer(old);
    int index;
    while((index = old.indexOf("id=")) != -1)
    {
      str.delete(index, index + 3);
      old = str.toString();
      if((index = old.indexOf("\"")) != -1)
      {
        str.delete(index, index + old.substring(index + 1).indexOf("\"") + 2);
        old = str.toString();
      }
    }
    return old;
  }

  /**
   * cleans anchor tags. This may need to be disabled after the bookmarking
   */
  public static String cleanAnchor(String old)
  {
    StringBuffer str = new StringBuffer(old);
    int beginIndex = 0, endIndex;
//    String temp = old;

    while((beginIndex = old.indexOf("<a name=\"_Toc", beginIndex)) != -1)
    {
      if((endIndex = old.indexOf(">", beginIndex)) != -1)
      {
        str.delete(beginIndex, endIndex+1);
        old = str.toString();
//        beginIndex += 1;
      }
      else
      {
        beginIndex += "<a name=\"_Toc".length();
        continue;
      }

      // temporary store begin index
      endIndex = beginIndex;
      if((beginIndex = old.indexOf("</a>", beginIndex)) != -1)
      {
        str.delete(beginIndex, beginIndex + "</a>".length());
        old = str.toString();
      }
      else
      {
        System.out.println("Cannot find </a> from index " + endIndex);
        //restore beginIndex for next while iteration
        beginIndex = endIndex;
      }
    }

    return old;
  }

  /**
   * cleans class attributes.
   */
  public static String cleanClass(String old)
  {
    StringBuffer str = new StringBuffer(old);
    int index;
    while((index = old.indexOf("class=")) != -1)
    {
      String temp = old.substring(index);
      int sp, gt, last;
      sp = temp.indexOf(" ");
      gt = temp.indexOf(">");
      if(sp > gt || sp == -1)
      {
        last = gt;
      }
      else
      {
        last = sp;
      }
      str.delete(index, index + last);
      old = str.toString();
    }
    return old;
  }

  /**
   * clean the spaces (&nbsp;)
   */
  public static String cleanSpace(String old)
  {
    //skip if it's an image tag since image tags may not have any content
    if(old.indexOf("img") != -1)
    {
      return old;
    }
    StringBuffer str = new StringBuffer(old);
    int index;
    while((index = old.indexOf("&nbsp;")) != -1)
    {
      str.delete(index, index + 6);
      old = str.toString();
    }
    return old;
  }

  /**
   *  clean P tag
   */
  public static String cleanPTag(String old)
  {
//    System.out.println("[TagCleaner.cleanPTag] {{" + old + "}}");
//    StringBuffer str = new StringBuffer(old);
    int index;
    old = cleanClass(old);
    old = cleanStyle2(old);
    old = cleanEmpty(old);

    //creates table if steps are involved
    if(old.indexOf("Step ") != -1 && old.indexOf(":") != -1)
    {
      old = createTable(old);
    }
    return cleanTag(old);
  }

  /**
   * Creates table so that the steps
   * can be aligned properly.
   */
  public static String createTable(String old)
  {
    String step, content, temp;
    StringBuffer str = new StringBuffer(old);
    step = old.substring(old.indexOf("Step"));
    content = step.substring(step.indexOf(":") + 1);
    step = step.substring(0, step.indexOf(":") + 1);
    content = cleanSpace(content);

    //removes any <span> tags
    if(content.startsWith("<span"))
    {
      content = content.substring(content.indexOf("</span>") + 7);

      //creates the table
    }
    old = "<table cellpadding=0 cellspacing=0 border=0>\n<tr>\n" +
        "<td valign=top width=60>" + step + "</td>" +
        "<td>" + content + "</td>" +
        "</tr></table>";
    return old;
  }

  /**
   * clean <ul> tag
   */
  public static String cleanULTag(String old)
  {
    StringBuffer str = new StringBuffer(old);
    //skip if it's an image tag since image tags may not have any content
    if(old.indexOf("img") != -1)
    {
      return old;
    }

    int index;
    old = cleanClass(old);
    old = cleanStyle(old);
    return old;
  }

  /**
   * clean <li> tags
   */
  public static String cleanListTag(String old)
  {
    StringBuffer str = new StringBuffer(old);
    int index;
    old = cleanClass(old);
    return old;
  }

  public static String cleanTag(String tag)
  {
    tag = tag.trim();
    if(tag.startsWith("<"))
    {
      int begintIndex = tag.indexOf(' ');
      if(begintIndex != -1)
      {
        int endIndex = tag.indexOf('>', begintIndex);
        String attributes = tag.substring(begintIndex, endIndex).trim();
        if(attributes.length() != 0)
          attributes = " " + attributes;
        tag = tag.substring(0, begintIndex).trim() + attributes + tag.substring(endIndex).trim();
      }
    }
    return tag;
  }

}