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
import java.util.*;

/**
 * Does the actual splitting of files and cleaning up of unnecessary tags
 */
public class Splitter
    extends Thread
{
  public static final String WHITE_SPACE = getWhiteSpace();
  public static final String TAG_ANCHOR = "<a name=\"";
  public static final String[] TAG_ANCHOR_IGNORE =
      {
      "_top", "_Toc", "OLE_", "_Ref"
  };

  public static final String IMAGES_FOLDER = "images" + File.separatorChar;
  public static final String[] IMAGES =
      {
      "chapTopic.gif",
      "openbook.gif",
      "topic.gif"
  };

  private int _trgCounter = 0;
  private Vector _imgSrc = new Vector();
  private Vector _imgDest = new Vector();

//  private String _subChapter = null;
  private int _imgNum = 1;
  private BufferedReader _buf;
  private String _srcFile;
  private File _srcFolder;
  private File _destFolder;
  private String _styleFile;
  private String _css;

//  private String _prefix = "";
  private int _chapter = 0;
  private int _max_header = 3;
  private int _max_header_body = _max_header + 4;
  private Vector _chapterName = new Vector(10);
  private TocItem _root;
  private String _name = "g";

//  private String _baseFolder = _name;
  private String _title;

  private String _tocFilename, _mapFilename, _hsFilename;

  private boolean _isFinished = false;
  private boolean _useChapterName = true;
  private String _chapterNum = "";

  /**
   * Constructor
   * @param src source file to be split
   * @param dest target directory to put split files
   * @style stylesheet file path
   */
  public Splitter(String src,
      String dest,
      String style,
      String title,
      String shortname,
      int level)
  {
    this._title = title;
    this._name = shortname;
    this._max_header = level + 1; // Java start counting from '0'
    // 1st blank chapter
    _chapterName.add("");
    // Initial size of 10
    _chapterName.setSize(10);
    _root = new TocItem(_title);
    _root.setImage(MapGenerator.TGT_OPENBOOK);
    _srcFile = src;
//    getPrefix();
    _srcFolder = (new File(src)).getParentFile();
    //System.out.println("Src:"+srcFile);
    _destFolder = new File(dest, _name);
    if(!_destFolder.isDirectory())
    {
      _destFolder.mkdirs();
    }
    //System.out.println("Dest: "+destDir);
    _styleFile = style;
    _css = new File(_styleFile).getName();
  }

  public void run()
  {
    try
    {
      preProcessing();
      int index;
      File tempFile = new File("initial.htm");
      String temp, t1, t2, fullChapterName = "", chapterNum = "";
      String title = "";
      int chapter = 0;
      boolean header = false;

      BufferedWriter dest = new BufferedWriter(new FileWriter(tempFile));
      tempFile.deleteOnExit();
      File src = new File(_srcFile);
      _buf = new BufferedReader(new FileReader(src));
//      TocItem partenItem = _root;
      TocItem currItem = null;
      TocItem[] tocs = new TocItem[_max_header + 1]; // plus root item
      tocs[0] = _root;
      Vector headerTargetId = null;
      Vector contentTargetId = new Vector();
      String chapterName = "";

      Logger.write("Start generating . .");

      while((temp = _buf.readLine()) != null)
      {
        header = false;
        for(int h = 1; h < _max_header; h++)
        {
//          for (int i = 1; i < 7; i++) {
          if((index = temp.indexOf("<h" + h)) != -1)
          {
            header = true;
            if(currItem != null)
            {
              currItem.setContentAnchors(contentTargetId);
            }

            currItem = new TocItem();
            // reset for new chapter
            contentTargetId = new Vector();
            tocs[h] = currItem;

            if(h == 1)
            {
              _imgNum = 1;
            }
//            if(tocs[h - 1] != null)
//            {
//              tocs[h - 1].add(currItem);
//            }
            t1 = temp.substring(0, index);
            dest.write(t1);
            dest.newLine();
            dest.flush();
            dest.close();
            t2 = temp.substring(index);
            temp = t2;
//            headerTargetId = new Vector();

            while(temp.indexOf("</h" + h + ">") == -1)
//              while(temp.indexOf("</h" + h + ">") == -1)
            {
              temp = _buf.readLine();
              t2 = t2 + " " + temp;
            }

            headerTargetId = findHelpID(t2);

            t2 = cleanup(t2);
            t2 = TagCleaner.cleanAnchor(t2);
            if(h == 1)
            {
              title = t2;
            }
//            if (t2.indexOf(".") != -1) {
//              chapter = t2.substring(0, t2.indexOf("."));
//            }
            String pageName = "";
            fullChapterName = t2;
            if((chapterNum = isChapter(t2)) != null)
            {
              chapter = _chapter;
              if(h == 1)
              {
//                System.out.println("[Splitter.run] Processing chapter [" +
//                    chapter + "]");
                chapterName = getHeaderName(t2);
                pageName = chapterName;
//                System.out.println("_chapterName.size()=" + _chapterName.size() + " chapter=" + chapter);
                if(_chapterName.size() <= chapter)
                {
//                  System.out.println("Increasing vector size: " + _chapterName.size());
                  _chapterName.setSize(chapter + 5);
//                  System.out.println("Increased vector size: " + _chapterName.size());
                }
                _chapterName.setElementAt(chapterName, chapter);
              }
              else
              {
                chapterName = (String)_chapterName.get(chapter);
                pageName = getHeaderName(t2);
              }
            }
            else
            {
              chapter = _chapter;
              pageName = getHeaderName(t2);
              if(h == 1)
              {
                chapterName = pageName;
              }
            }

            if(tocs[h - 1] != null && !pageName.equals(""))
            {
              tocs[h - 1].add(currItem);
            }

//            System.out.println("[Splitter.run] Processing page of chapter [" +
//                chapter + "][" + chapterName + "] {" + pageName + "}");
            String filename = pageName;
            String chFolder = chapterName;
            if(!_useChapterName && chapter != 0)
            {
              chFolder = "ch" + chapter;
              filename = _chapterNum;
            }
            // create a new folder for each chapter if it doesn't already exist
            File chapFolder = new File(_destFolder, chFolder);
            if(!chapFolder.exists())
            {
              chapFolder.mkdir();

              //create the new file for writing
            }
//            String chName = "ch" + chapter
//                + File.separator + getFile(t2) + ".htm";
            dest = new BufferedWriter(new FileWriter(new File(chapFolder,
                filename + ".htm")));
            currItem.setText(fullChapterName);
            currItem.setUrl(_name + "/" + chapFolder.getName() + "/" +
                filename + ".htm");
            if(headerTargetId.isEmpty())
            {
              currItem.setTarget(_name + "." + (_trgCounter++));
            }
            else
            {
              currItem.setTarget((String)headerTargetId.firstElement());
            }
            currItem.setChapter(chapter);
//            partenItem.add(currItem);
            Logger.write("Processing Chapter file - [" + pageName + "]");
//            if(t2.indexOf(" ") != -1)
//            {
//              _subChapter = t2.substring(0, t2.indexOf(" "));
//            }
//            else
//            {
//              _subChapter = t2.substring(0);
//            }
            //writing the header
            dest.write("<head>");
            dest.newLine();
            dest.write("<title>" + "Chapter " + title + "</title>");
            dest.newLine();
            dest.write(
                "<link href=\"../" + /*gt_help.css*/ _css +
                "\" rel=\"stylesheet\" type=\"text/css\">");
            dest.newLine();
            dest.write("</head>");
            dest.newLine();
            dest.write("<h" + h + ">" + t2 + "</h" + h + ">");
            dest.newLine();
          }
        }
        if(!header)
        {
          String t[];

          //cleaning the tags
          if((t = getTag(temp, "<p", "</p>")) != null)
          {
            t[1] = TagCleaner.cleanPTag(t[1]);
            temp = t[0] + t[1] + t[2];
          }
          if((t = getTag(temp, "<img ", ">")) != null)
          {
//            System.out.println("line [" + temp + "]\ntag [" + t[1] + "]");
            if(t[1].indexOf("src") != -1)
            {
              t[1] = fixIMGTag(t[1]);
            }
            temp = t[0] + t[1]; // + t[2];

            String t22 = t[2];
            while((t = getTag(t22, "<img ", ">")) != null)
            {
//              System.out.println("line [" + t22 + "]\ntag [" + t[1] + "]");
              if(t[1].indexOf("src") != -1)
              {
                t[1] = fixIMGTag(t[1]);
              }
              temp = t[0] + t[1]; // + t[2];
              t22 = t[2];
            }

            temp += t22;
          }

          for(int h = _max_header; h < _max_header_body; h++)
          {
            if((t = getTag(temp, "<h" + h, "</h" + h + ">")) != null)
            {
              temp = t[0] + t[1] + t[2];
            }
          }

          if((t = getTag(temp, "<ul", ">")) != null)
          {
            t[1] = TagCleaner.cleanULTag(t[1]);
            temp = t[0] + t[1] + t[2];
          }
          if((t = getTag(temp, "<li", "</li>")) != null)
          {
            t[1] = TagCleaner.cleanULTag(t[1]);
            temp = t[0] + t[1] + t[2];
          }
          if((t = getTag(temp, "<span", "</span>")) != null)
          {
            t[1] = TagCleaner.cleanSpanTag(t[1]);
            temp = t[0] + " " + t[1] + " " + t[2];
          }
          if((t = getTag(temp, "<td", ">")) != null)
          {
            t[1] = TagCleaner.cleanStyle2(t[1]);
            temp = t[0] + " " + t[1] + " " + t[2];
          }
//          if((t = getTag(temp, "<a name=\"_Toc", "></a>")) != null)
//          {
//            temp = t[0] + t[2];
//          }
          contentTargetId.addAll(findHelpID(temp));
          temp = TagCleaner.cleanAnchor(temp);

          if(!temp.trim().equals("<p></p>") && !temp.trim().equals(""))
          {
            dest.write(temp);
            dest.newLine();
          }
        }
      }
      dest.flush();
      dest.close();

      //copies the style file to the desired path
      Utilities.copy(_styleFile,
          _styleFile.substring(_styleFile.lastIndexOf(File.
          separator) + 1), _destFolder + File.separator);

      // Copies required images
      File imgs = new File(_destFolder.getParentFile(), "images");
      if(!imgs.exists())
      {
        imgs.mkdirs();
      }

      for(int i = 0; i < IMAGES.length; i++)
      {
        Utilities.copy(IMAGES_FOLDER + File.separator + IMAGES[i],
            IMAGES[i],
            imgs.getAbsolutePath());
      }

      try
      {
        _tocFilename = _name + "_TOC.xml";
        File tocFile = new File(_destFolder.getParentFile(), _tocFilename);
        TocGenerator tocGen = new TocGenerator(_root);
        tocGen.generate(tocFile.getAbsolutePath());
      }
      catch(Throwable t)
      {
        t.printStackTrace();
        Logger.write(t.getMessage());
      }

      try
      {
        _mapFilename = _name + "_MAP.jhm";
        File mapFile = new File(_destFolder.getParentFile(), _mapFilename);
        MapGenerator mapcGen = new MapGenerator(_root);
        mapcGen.generate(mapFile.getAbsolutePath());
      }
      catch(Throwable t)
      {
        t.printStackTrace();
        Logger.write(t.getMessage());
      }

      try
      {
        _hsFilename = _name + "_hs.hs";
        File hsFile = new File(_destFolder.getParentFile(), _hsFilename);
        HsGenerator hsGen = new HsGenerator(_root);
        hsGen.generate(hsFile.getAbsolutePath(), _mapFilename,
            _tocFilename, _name + "Search" /*"JavaHelpSearch"*/);
      }
      catch(Throwable t)
      {
        t.printStackTrace();
        Logger.write(t.getMessage());
      }

      Logger.write("Finished generating files");

      try
      {
        File unwanted = new File(_destFolder, ".htm");
        if(unwanted.exists())
        {
          unwanted.delete();
        }
      }
      catch(Throwable t)
      {

      }
      /*
       For a unknown reason the indexer does not work so taken out from this implementation
       */
      Logger.write("Start indexing files");
      SearchIndexer indexer = new SearchIndexer(_destFolder);
      try
      {
        indexer.generate();
        Logger.write("Finish indexing files");
      }
      catch(Throwable t)
      {
        t.printStackTrace();
      }
      /**/
      Logger.write("Help set created");

      _isFinished = true;
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      Logger.write(ex.getMessage());
    }
  }

  /**
   * @return true if the conversion is finishedse
   */
  public boolean isFinished()
  {
    return _isFinished;
  }

  /**
   * Deals with the moving and renaming of images while maintaining its link
   * to the html file
   */
  public String fixIMGTag(String imageTag)
  {
    String source = Utilities.getSrc(imageTag);
    File srcImgFile = new File(_srcFolder, source);
    try
    {
      srcImgFile = srcImgFile.getCanonicalFile();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
//    System.out.println("Source image ["+srcImgFile+"]");
    String ext, dest, result = null;
    int i;
//    if(source.indexOf("jpg")!= -1)
//      ext = ".jpg";
//    else
//      ext = ".gif";
//    dest = "ch"+ subChapter + "_" + imgNum + ext;
    dest = srcImgFile.getName();
    if(!new File(_destFolder + File.separator + "images").exists())
    {
      new File(_destFolder + File.separator + "images").mkdir();
    }

    //check for duplicate images
    for(i = 0; i < _imgSrc.size(); i++)
    {
      if(source.equals((_imgSrc.elementAt(i))))
      {
        dest = (String)_imgDest.elementAt(i);
        break;
      }
    }
    // if not duplicate, add to namelist
    if(i == _imgSrc.size())
    {
      _imgNum++;
      _imgSrc.addElement(source);
      _imgDest.addElement(dest);
    }
//    ImageMover.move(srcFile.substring(0, srcFile.lastIndexOf(File.separator) + 1) + source, dest, destDir + File.separator + "images/");
    Utilities.copy(_srcFolder.getAbsolutePath() + File.separator + source,
        dest,
        _destFolder + File.separator + "images/");
    result = Utilities.update(imageTag, dest, "../images/");

    return TagCleaner.cleanID(result);
  }

  /**
   * check the tag string for a specified start and end tag inside a string
   *
   * @param old the string to be checked
   * @param startStr the starting tag
   * @param endStr the closing tag
   * @return array of 3 strings: string before the tag, the tag, string aftre the tag.
   */
  public String[] getTag(String old, String startStr, String endStr)
  {
    int index1, index2;
    boolean oneLine = true;
    String t[] = new String[3];

    if((index1 = old.indexOf(startStr)) != -1)
    {
      t[0] = old.substring(0, index1).trim();
      t[1] = old.substring(index1).trim();
      old = t[1];
      while((index2 = old.indexOf(endStr)) == -1)
      {
        try
        {
          old = _buf.readLine();
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
          Logger.write(ex.getMessage());
        }
        if((index2 = old.indexOf(endStr)) != -1)
        {
          t[1] += " " + old.substring(0, index2 + endStr.length()).trim();
        }
        else
        {
          t[1] += " " + old.trim();
        }
        oneLine = false;
      }

      if(oneLine)
      {
        t[2] = t[1].substring(t[1].indexOf(endStr) + endStr.length()).trim();
        t[1] = t[1].substring(0, t[1].indexOf(endStr) + endStr.length()).trim();
      }
      else
      {
        t[2] = old.substring(index2 + endStr.length());
      }
      return t;
    }
    return null;
  }

  /**
   * clean up everything in the header: any tags and spaces
   */
  public String cleanup(String old)
  {
    old = old.replace('\n', ' ');
    old = old.replace('\r', ' ');

    StringBuffer str = new StringBuffer(old);
    while(old.indexOf("<") != -1)
    {
      str.delete(old.indexOf("<"), old.indexOf(">") + 1);
      old = str.toString();
    }
    while(old.indexOf("&nbsp;") != -1)
    {
      str.delete(old.indexOf("&nbsp;"), old.indexOf("&nbsp;") + 6);
      old = str.toString();
    }
    old = old.replace('/', '_');
    old = old.replace(':', ' ');
    return old.trim();
  }

  private void preProcessing()
  {

    try
    {

      int index;
//    File tempFile = File.createTempFile("htmltemp", ".tmp");
      File tempFile = new File("htmltemp.htm");

      BufferedWriter dest = new BufferedWriter(new FileWriter(tempFile));
//    tempFile.deleteOnExit();
      File src = new File(_srcFile);
      _buf = new BufferedReader(new FileReader(src));
      String temp = null;
      boolean newline = true, lookingForEndTag = false;
      while((temp = _buf.readLine()) != null)
      {
        if(lookingForEndTag)
        {
          if((index = temp.indexOf(">")) != -1)
          {
            lookingForEndTag = false;
            newline = true;

          }
        }
        else if((index = temp.toLowerCase().indexOf("<img ")) != -1)
        {
          if(index == 0)
          {
            temp = "\n\r" + temp;
          }
          else
          {
            temp = temp.substring(0, index) + "\n\r" + temp.substring(index);
          }
          if(temp.indexOf(">", index) == -1)
          {
            lookingForEndTag = true;
            newline = false;
            temp = temp + " ";
          }
        }
        else
        {
          newline = true;
        }

        dest.write(temp);
        if(newline)
        {
          dest.newLine();
        }
      }

      dest.flush();
      dest.close();
      _srcFile = tempFile.getAbsolutePath();
    }

    catch(Exception ex)
    {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  /*
    private String getFile(String title)
    {
      String s = title.trim();
      if(s.equals(""))
      {
        return "index";
      }
//    String delim = " " + 0xA0;
      StringTokenizer st = new StringTokenizer(s);

      String ch = st.nextToken();
      Logger.write("getFile token = [" + ch + "]");

      ch = ch.trim();
      char[] ar = ch.toCharArray();
      int i = 0;
      for(; i < ar.length; i++)
      {
        if(ar[i] == '.')
        {
          continue;
        }
        if(ar[i] > 'z')
        {
          break;
        }
      }

      if(i < ar.length)
      {
        ch = ch.substring(0, i);

      }
      return _prefix + ch.replace('.', '_');
    }
   */
//  private void getPrefix()
//  {
//    File f = new File(_srcFile);
//    String fn = f.getName();
//    _prefix = fn.substring(0, fn.indexOf('.'));
//    Logger.write("File prefix = [" + _prefix + "]");
//  }

  private String getHeaderName(String line)
  {
    StringTokenizer st = new StringTokenizer(line.trim(), WHITE_SPACE);
    StringBuffer buf = new StringBuffer(line.length());

    // Parse 1st token
    if(st.hasMoreTokens())
    {
      String head = st.nextToken().trim();
      String chapterNum = null;
      int index = head.indexOf(".");
      if(index != -1)
      {
        chapterNum = head.substring(0, index);
        try
        {
          Integer.parseInt(chapterNum);
        }
        catch(Throwable t)
        {
          buf.append(firstCap(head));
        }
      }
      else
      {
        try
        {
          Integer.parseInt(head);
        }
        catch(Throwable t)
        {
          buf.append(firstCap(head));
        }
      }
    }
    while(st.hasMoreTokens())
    {
      buf.append(firstCap(st.nextToken().trim()));
    }
    String header = buf.toString().trim();
//    System.out.println("[Splitter.getHeaderName] {" + line + "} ==> {" + header +
//        "}");

    return header;
  }

  private String isChapter(String line)
  {
//    String chapter = null;
    StringTokenizer st = new StringTokenizer(line, WHITE_SPACE);
    if(st.hasMoreTokens())
    {
      _chapterNum = st.nextToken();
      try
      {
        _chapter = Integer.parseInt(_chapterNum);
        return _chapterNum;
      }
      catch(Throwable t)
      {
      }

      int index = _chapterNum.indexOf(".");
      if(index != -1)
      {
        try
        {
          String cpr = _chapterNum.substring(0, index);
          _chapter = Integer.parseInt(cpr);
          return _chapterNum;
        }
        catch(Throwable t)
        {
//          _chapter = 0;
//          return null;
        }
      }
    }
    _chapter = 0;
    _chapterNum = "";
    return null;
  }

  private String firstCap(String str)
  {
    String rv = str;
    switch(str.length())
    {
      case 0:

        // nothing to do
        break;

      case 1:
        rv = str.toUpperCase();
        break;
      default:
        rv = str.substring(0, 1).toUpperCase().concat(str.substring(1));
        break;
    }

    return rv;
  }

  private static String getWhiteSpace()
  {
    byte[] byt =
        {
        (byte)0xA0};
    String ch = new String(byt);
    return " \t\r\n" + ch;
  }

  private Vector findHelpID(String line)
  {
    Vector ids = new Vector();
    boolean ignore = false;
    int fromIndex = 0, endIndex = 0;

    while((fromIndex = line.indexOf(TAG_ANCHOR, fromIndex)) != -1)
    {
      fromIndex += TAG_ANCHOR.length();
      endIndex = line.indexOf('\"', fromIndex);
      if(fromIndex == -1)
      {
        break;
      }
      String anr = line.substring(fromIndex, endIndex);
      fromIndex = endIndex;

      ignore = false;
      for(int i = 0; i < TAG_ANCHOR_IGNORE.length; i++)
      {
        if(anr.startsWith(TAG_ANCHOR_IGNORE[i]))
        {
          ignore = true;
          break;
        }
      }

      if(!ignore)
      {
        ids.add(anr);
      }
    }

    if(!ids.isEmpty())
    {
      StringBuffer buf = new StringBuffer("Found anchor: ");
      for(int i = 0; i < ids.size(); i++)
      {
        buf.append(ids.get(i)).append(';');
      }
      System.out.println(buf.toString());
    }
//    else
//    {
//        System.out.println("Cannot find any anchor....");
//    }

    return ids;
  }

}
