package com.gridnode.helpmaker;

/**
 * Title:        Help Maker
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      GridNode
 * @author Ferry
 * @version 1.0
 */
import java.io.*;

/**
 * This class will move images to the image folder and renames them. It will preserve their links at the HTML file.
 */
public class Utilities
{
  /**
   * get the image source string inside the image tag
   *
   * @param tag String
   * @return String
   */
  public static String getSrc(String tag)
  {
    int index = tag.indexOf("src=");
    if(index == -1)
    {
      return "";
    }
    String result = tag.substring(index + 5);
    result = result.substring(0, result.indexOf("\""));

    return result;
  }

  /**
   * updates the tag with name and path of the new image source
   *
   * @param tag the tag string which source is to be updated
   * @param name name of the new file
   * @param path path of the new file
   * @return the updated tag
   */
  public static String update(String tag, String name, String path)
  {
    int index = tag.indexOf("src=");
    String t1, t2;
    //System.out.println("index:"+index);
    t1 = tag.substring(0, index + 5);
    tag = tag.substring(index + 5);
    t2 = tag.substring(tag.indexOf("\""));
    return t1 + path + name + t2;
  }

  /**
   * copies the image to the images folder
   *
   * @param src source of the old image file
   * @param name name of the new file
   * @param path path of the new file
   */
  public static void copy(String src, String name, String path)
  {
    File oldPath = new File(src);
    //System.out.println("oldpath:" +oldPath);
    File newPath = new File(path, name);
    //System.out.println("newpath:" +newPath);
    try
    {
      FileInputStream in = new FileInputStream(oldPath);
      FileOutputStream out = new FileOutputStream(newPath);
      byte[] buffer = new byte[512000];
      int len;
//      System.out.println("moving " + newPath.getName());
      while((len = in.read(buffer)) >= 0)
      {
        out.write(buffer, 0, len);

      }
      out.flush();
      in.close();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      Logger.write(ex.getMessage());
    }
  }
  /**
   * copies the image to the images folder
   *
   * @param src source of the old image file
   * @param name name of the new file
   * @param path path of the new file
   */
  public static void copy(File src, File dest)
  {
    try
    {
      FileInputStream in = new FileInputStream(src);
      FileOutputStream out = new FileOutputStream(dest);
      byte[] buffer = new byte[512000];
      int len;
//      System.out.println("moving " + newPath.getName());
      while((len = in.read(buffer)) >= 0)
      {
        out.write(buffer, 0, len);

      }
      out.flush();
      in.close();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      Logger.write(ex.getMessage());
    }
  }
}
