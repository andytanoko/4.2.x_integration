package com.gridnode.helpmaker;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

/**
 * <p>Title: Help Maker</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: GridNode</p>
 * @author not attributable
 * @version 1.0
 */

public class TocItem
    extends DefaultMutableTreeNode
{
  private String image = null;
  private String target = "";
  private String url = "";
  private String filename = "";
  private int chapter = 0;
  private Vector contentAnchors = new Vector();

  public TocItem()
  {
  }

  public TocItem(String text)
  {
    super(text);
  }

  public String getImage()
  {
    if(image == null || "".equals(image))
    {
      if(this.isLeaf())
      {
        return MapGenerator.TGT_TOPIC;
      }
      else
      {
        return MapGenerator.TGT_CHAPTER;
      }
    }
    return image;
  }

  public void setImage(String image)
  {
    this.image = image;
  }

  public String getTarget()
  {
    return target;
  }

  public void setTarget(String target)
  {
    if(this.target == null || this.target.length() == 0)
      this.target = target;
    else
      addContentAnchor(target);
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getFilename()
  {
    return filename;
  }

  public void setFilename(String filename)
  {
    this.filename = filename;
  }

  public String getText()
  {
    return(String)getUserObject();
  }

  public void setText(String text)
  {
    setUserObject(text);
  }

  public int getChapter()
  {
    return chapter;
  }

  public void setChapter(int chapter)
  {
    this.chapter = chapter;
  }

  public Vector getContentTarget()
  {
    return contentAnchors;
  }

  public void setContentAnchors(Vector contentAnchors)
  {
    this.contentAnchors = contentAnchors;
  }

  public void addContentAnchor(String anchor)
  {
    if(!contentAnchors.contains(anchor))
      contentAnchors.add(anchor);
  }

}
