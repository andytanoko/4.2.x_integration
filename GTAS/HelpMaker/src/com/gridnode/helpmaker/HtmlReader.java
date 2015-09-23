package com.gridnode.helpmaker;

import java.io.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.MutableAttributeSet;
import java.util.*;

public class HtmlReader
{
  public static final String IMAGES_FOLDER = "../images" + File.separatorChar;
  public static final String[] IMAGES =
      {
      "chapTopic.gif",
      "openbook.gif",
      "topic.gif"
  };
  public static final String[] TAG_ANCHOR_IGNORE =
      {
      "_top", "_Toc", "OLE_", "_Ref"
  };
  private static final byte[] A0_BYTE = {(byte)0xA0};
  private static final String A0_STRING = new String(A0_BYTE);
  public static final String CHAR_TO_CONVERT = "<>\"&" + A0_STRING;
  public static final String WHITE_SPACE = Splitter.WHITE_SPACE;
  public static final String TEMPFILE = "cover.html";
  public static final Hashtable ATTR_IGNORE = new Hashtable();
  static
  {
    ATTR_IGNORE.put("class", "class");
    ATTR_IGNORE.put("style", "style");
  }

  public static final Hashtable CHAR_REPLACE = new Hashtable();
  static
  {
    CHAR_REPLACE.put("<", "&lt;");
    CHAR_REPLACE.put(">", "&gt;");
    CHAR_REPLACE.put("\"", "&quot;;");
    CHAR_REPLACE.put("&", "&amp;");
    CHAR_REPLACE.put("\n", "&#13;&#10;");
    CHAR_REPLACE.put(A0_STRING, "");
  }

  private File _page = new File(TEMPFILE);
  private File _srcHtmlFile;
  private File _cssFile;
  private File _destFolder;
  protected String _title;
  private String _shortname;
  private int _maxLevel;
  private ParserDelegator _parDel;
  private BufferedWriter _writer = null;
  private StringBuffer _headerBuf;

  protected boolean _isHeader = false;
  protected boolean _storeTitle = false;
  private int _headerNum;
  private String _header;
  private String[] _chapTitle =
      {
      "", ""};
  private String[] _pageTitle =
      {
      "", ""};
  private int _chapIndex = 0;
  private int _pageIndex = 0;
  private int _chapterNum;
  protected TocItem[] _tocs;
  private TocItem _currentToc;
  private int _targerCount = 0;

  public HtmlReader(String srcHtml,
      String destFolder,
      String cssFile,
      String title,
      String shortname,
      int level)
  {
    this._srcHtmlFile = new File(srcHtml);
    this._cssFile = new File(cssFile);
    this._destFolder = new File(destFolder, shortname);
    this._title = title;
    this._shortname = shortname;
    this._maxLevel = level;
    this._tocs = new TocItem[_maxLevel + 1]; // plus root item

    _tocs[0] = new TocItem(_title);
    try
    {
//      _page =new File(_destFolder, "cover.htm");
      _writer = new BufferedWriter(new FileWriter(_page));
//      _tocs[0].setUrl(_shortname + "/" + _page.getName());
      _tocs[0].setTarget("home");
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void split()
  {
    File imageFolder = new File(_destFolder, "images");
    if(!imageFolder.isDirectory())
    {
      imageFolder.mkdirs();

    }
    _parDel = new ParserDelegator();

    try
    {
      FileReader reader = new FileReader(_srcHtmlFile);
      Callback cb = new Callback();
      _parDel.parse(reader, cb, true);
      close();
      Logger.write("Completed parsing " + _srcHtmlFile);

      generateSearchIndex();
      //copies the style file to the desired path
      Utilities.copy(_cssFile, new File(_destFolder, _cssFile.getName()));
      copyImages();
      generateTocFile();
      generateMapFile();
      generateHelpSet();
      Logger.write("Complete generating HelpSet");
    }
    catch(Throwable t)
    {
      t.printStackTrace();
    }
  }

  protected void close()
  {
    if(_writer == null)
    {
      return;
    }

    try
    {
      writeLine("</body>");
      writeLine("</html>");
      _writer.flush();
      _writer.close();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    _writer = null;
  }

  protected void closeFile()
  {
    close();
    File page = new File(_destFolder, _chapTitle[_chapIndex] +
        File.separatorChar + _pageTitle[_pageIndex]);
    if(!page.getParentFile().isDirectory())
    {
      page.getParentFile().mkdirs();
    }
    if(page.exists())
    {
      page.delete();
    }

    _page.renameTo(page);
  }

  protected void write(String line)
  {
    if(_writer == null)
    {
      return;
    }

    try
    {
      _writer.write(line);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  protected void write(char[] cbuf)
  {
    if(_writer == null)
    {
      return;
    }

    try
    {
      _writer.write(cbuf);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  protected void writeLine(String line)
  {
    if(_writer == null)
    {
      return;
    }

    try
    {
      _writer.write(line);
      _writer.newLine();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  protected void writeLine()
  {
    if(_writer == null)
    {
      return;
    }

    try
    {
      _writer.newLine();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  protected int isHeader(Tag t)
  {
    int h = -1;
    if(t.toString().length() == 2 && t.toString().startsWith("h"))
    {
      try
      {
        h = Integer.parseInt(t.toString().substring(1));
      }
      catch(Exception ex)
      {
      }
    }

    return h;
  }

  protected void startHeader(int level, Tag t, MutableAttributeSet a, int pos)
  {
    _isHeader = true;
    if(level <= _maxLevel)
    {
      close();
      _currentToc = new TocItem("");
    }
    _headerBuf = new StringBuffer();
    _headerBuf.append('<').append(t.toString()).append(getAttributes(a)).append(
        '>');
    _header = "";
    _headerNum = level;
  }

  protected String getAttributes(MutableAttributeSet a)
  {
    StringBuffer buf = new StringBuffer();

    Enumeration attrs = a.getAttributeNames();
    Object key = null;
    while(attrs.hasMoreElements())
    {
      key = attrs.nextElement();
      if(ATTR_IGNORE.get(key.toString()) != null)
      {
        continue; // ignore this attribute
      }

      buf.append(key).append("='").append(a.getAttribute(key)).append("' ");
    }

    String s = buf.toString().trim();

    return(s.length() == 0) ? "" : " " + s;
  }

  protected void endHeader(Tag t, int level)
  {
    _isHeader = false;

    if(level <= _maxLevel)
    {
      String[] ph = getHeaderTitle(_header);
      _pageTitle[0] = (ph[0].length() == 0) ? ph[1] : ph[0];
      _pageTitle[1] = ph[1];
      if(level == 1)
      {
        _chapTitle[0] = (ph[0].length() == 0) ? ph[1] : ph[0];
        _chapTitle[1] = ph[1];
      }
      if(_currentToc.getTarget() == null || _currentToc.getTarget().length() == 0)
        _currentToc.setTarget(_shortname + "." + (_targerCount++));
      startNewPage(level);
    }

    _headerBuf.append("</").append(t.toString()).append('>');
    writeLine(_headerBuf.toString());
  }

  protected void storeTitle(String text)
  {
    _title = _title + " " + text;
    _title = _title.trim();
  }

  protected void storeHeader(String text)
  {
    text = text.trim();
    if(text.length() != 0)
    {
      _header = _header + text + " ";
      _headerBuf.append(convertData(text)).append(' ');
    }
  }

  private void startNewPage(int level)
  {
    _page = new File(TEMPFILE);
    try
    {
      String pagename = _pageTitle[_pageIndex].trim().replace(' ', '_');
      String chaptername = _chapTitle[_chapIndex].trim().replace(' ', '_');

      _currentToc.setText(_header);
      _currentToc.setChapter(_chapterNum);
      _tocs[level] = _currentToc;
      if(_tocs[level - 1] != null && !pagename.equals(""))
      {
        _tocs[level - 1].add(_currentToc);
      }

      File page = (pagename.length() == 0)
          ? File.createTempFile("un-named", ".html")
          : new File(_destFolder,  chaptername +
          File.separatorChar + pagename + ".htm");

      if(!page.getParentFile().isDirectory())
      {
        page.getParentFile().mkdirs();
      }

      _currentToc.setUrl(_shortname + "/" + chaptername + "/" +
          page.getName());

      _writer = new BufferedWriter(new FileWriter(page));

      writeLine("<html>");
      writeLine("<head>");
      writeLine("<title>" + convertData(_header) + "</title>");
      writeLine(
          "<link href=\"../" + /*gt_help.css*/ _cssFile.getName() +
          "\" rel=\"stylesheet\" type=\"text/css\">");
      writeLine("</head>");
      writeLine("<body lang=EN-US link=blue vlink=blue>");

    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private String[] getHeaderTitle(String header)
  {
    String[] rv =
        {
        "", ""};

    StringTokenizer st = new StringTokenizer(header, WHITE_SPACE);
    StringBuffer buf = new StringBuffer(header.length());

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
          _chapterNum = Integer.parseInt(chapterNum);
          rv[0] = head;
        }
        catch(Throwable t)
        {
          buf.append(head);
          _chapterNum = 0;
        }
      }
      else
      {
        try
        {
          _chapterNum = Integer.parseInt(head);
          rv[0] = head;
        }
        catch(Throwable t)
        {
          buf.append(head);
          _chapterNum = 0;
        }
      }
    }
    while(st.hasMoreTokens())
    {
      buf.append(' ').append(st.nextToken());
    }
    rv[1] = buf.toString().trim();

    return rv;
  }

  protected void handleAnchor(MutableAttributeSet a)
  {
    String name = (String)a.getAttribute(HTML.Attribute.NAME);
    if(name != null)
    {
      boolean ignore = false;
      for(int i = 0; i < TAG_ANCHOR_IGNORE.length; i++)
      {
        if(name.startsWith(TAG_ANCHOR_IGNORE[i]))
        {
          ignore = true;
          break;
        }
      }
      if(!ignore)
      {
        if(_isHeader)
          _currentToc.setTarget(name);
        else
          _currentToc.addContentAnchor(name);
      }

    }
  }

  protected void handleImage(MutableAttributeSet a)
  {
    try
    {
      String path = a.getAttribute(HTML.Attribute.SRC).toString();
      if(path.startsWith("./") || path.startsWith(".\\"))
      {
        path = path.substring(2);
      }
      File img = new File(_srcHtmlFile.getParent(), path);

      a.addAttribute(HTML.Attribute.SRC, "../images/" + img.getName());
//      System.out.println("Image path [" + img.getAbsolutePath() +"]");
      File destImg = new File(_destFolder,
          "images" + File.separatorChar + img.getName());
      if(!destImg.exists())
      {
        Utilities.copy(img, destImg);
      }
    }
    catch(Throwable t)
    {
      t.printStackTrace();
    }

    StringBuffer buf = new StringBuffer();
    buf.append("<img").append(getAttributes(a)).append('>');

    write(buf.toString());
  }

  private String convertData(char[] data)
  {
    return convertData(new String(data));
  }

  protected String convertData(String data)
  {
    StringBuffer buf = new StringBuffer(data.length());
    StringTokenizer st = new StringTokenizer(data, CHAR_TO_CONVERT, true);
    String token = null;
    String ch = null;
    String ch2 = null;
    while(st.hasMoreElements())
    {
      token = st.nextToken();
      if(token.length() == 1)
      {
        ch = token.substring(token.length() - 1);
        ch2 = (String)CHAR_REPLACE.get(ch);
        if(ch2 != null)
        {
//          System.out.println("Data[" + data + "]");
          token = token.substring(0, token.length() - 1);
//          System.out.println("Token break[" + token + "]" + "[" + ch + "]");
          buf.append(ch2);
        }
        else
        {
          buf.append(token);
        }
      }
      else
      {
        buf.append(token);
      }
    }

    return buf.toString();
  }

  private void generateSearchIndex()
  {
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
  }

  private void copyImages()
  {
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

  }

  private void generateHelpSet()
  {
    try
    {
      String filename = _shortname + "_hs.hs";
      File hsFile = new File(_destFolder.getParentFile(), filename);
      HsGenerator hsGen = new HsGenerator(_tocs[0]);
      hsGen.generate(
          hsFile.getAbsolutePath(),
          _shortname + "_MAP.jhm",
          _shortname + "_TOC.xml",
          _shortname + "Search" /*"JavaHelpSearch"*/);
    }
    catch(Throwable t)
    {
      t.printStackTrace();
      Logger.write(t.getMessage());
    }
  }

  private void generateMapFile()
  {
    try
    {
      String filename = _shortname + "_MAP.jhm";
      File mapFile = new File(_destFolder.getParentFile(), filename);
      MapGenerator mapcGen = new MapGenerator(_tocs[0]);
      mapcGen.generate(mapFile.getAbsolutePath());
    }
    catch(Throwable t)
    {
      t.printStackTrace();
      Logger.write(t.getMessage());
    }


  }

  private void generateTocFile()
  {
    try
    {
      String filename = _shortname + "_TOC.xml";
      File tocFile = new File(_destFolder.getParentFile(), filename);
      TocGenerator tocGen = new TocGenerator(_tocs[0]);
      tocGen.generate(tocFile.getAbsolutePath());
    }
    catch(Throwable t)
    {
      t.printStackTrace();
      Logger.write(t.getMessage());
    }


  }

  protected String cleanA0(String text)
  {
    StringBuffer buf = new StringBuffer(text.length());
    StringTokenizer st = new StringTokenizer(text, A0_STRING);
    while (st.hasMoreElements())
    {
      buf.append(st.nextToken());
    }

    return buf.toString();
  }

  /** @todo start of inner class */

  class Callback
      extends HTMLEditorKit.ParserCallback
  {
    Callback()
    {
    }

    /**
     * flush
     *
     * @throws BadLocationException
     * @todo Implement this javax.swing.text.html.HTMLEditorKit.ParserCallback method
     */
    public void flush() throws BadLocationException
    {
    }

    /**
     * handleComment
     *
     * @param data char[]
     * @param pos int
     * @todo Implement this javax.swing.text.html.HTMLEditorKit.ParserCallback method
     */
    public void handleComment(char[] data, int pos)
    {
    }

    /**
     * handleText
     *
     * @param data char[]
     * @param pos int
     */
    public void handleText(char[] data, int pos)
    {
      String text = cleanA0(new String(data));
      if(_isHeader)
      {
        storeHeader(text);
        return;
      }
      if(_storeTitle)
      {
        storeTitle(text);
      }


      write(convertData(text));
    }

    /**
     * This is invoked after the stream has been parsed, but before <code>flush</code>.
     *
     * @param eol String
     */
    public void handleEndOfLineString(String eol)
    {
      close();
    }

    /**
     * handleError
     *
     * @param errorMsg String
     * @param pos int
     * @todo Implement this javax.swing.text.html.HTMLEditorKit.ParserCallback method
     */
    public void handleError(String errorMsg, int pos)
    {
    }

    /**
     * handleSimpleTag
     *
     * @param t Tag
     * @param a MutableAttributeSet
     * @param pos int
     */
    public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos)
    {
      if(t.toString().equals("span"))
      {
        return; // ignore
      }

      if(t.toString().equals("img"))
      {
        handleImage(a);
        return;
      }

      StringBuffer buf = new StringBuffer();
      buf.append('<').append(t.toString()).append(getAttributes(a)).append('>');

      write(buf.toString());
    }

    /**
     * handleStartTag
     *
     * @param t Tag
     * @param a MutableAttributeSet
     * @param pos int
     */
    public void handleStartTag(Tag t, MutableAttributeSet a, int pos)
    {
      if(t == Tag.A)
      {
        handleAnchor(a);
//        return;
      }
      if(t == Tag.DIV)
      {
        return; // ignore
      }

      int h = -1;
      if((h = isHeader(t)) != -1) // handle header
      {
        startHeader(h, t, a, pos);
        return;
      }

      if(t == Tag.TITLE)
      {
        if(_title.trim().length() == 0)
        {
          _storeTitle = true;
        }
      }
      StringBuffer buf = new StringBuffer();
      buf.append('<').append(t.toString()).append(getAttributes(a)).append('>');

      write(buf.toString());
    }

    /**
     * handleEndTag
     *
     * @param t Tag
     * @param pos int
     */
    public void handleEndTag(Tag t, int pos)
    {
      if(t == Tag.DIV)
      {
        return; // ignore
      }

      int level = isHeader(t);
      if(level != -1)
      {
        endHeader(t, level);
        return;
      }

      if(t == Tag.TITLE)
      {
        if(_storeTitle)
          _tocs[0].setText(_title);
          _storeTitle = false;
      }

      StringBuffer buf = new StringBuffer();
      buf.append("</").append(t.toString()).append('>');
      writeLine(buf.toString());

    }

  }
}
