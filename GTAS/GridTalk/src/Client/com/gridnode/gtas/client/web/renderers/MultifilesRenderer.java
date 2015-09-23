/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MultifilesRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-24     Andrew Hill         Created
 * 2002-10-07     Daniel D'Cotta      Modified to handle single files and view mode
 * 2002-10-11     Daniel D'Cotta      Added rendering of download link
 * 2002-10-24     Andrew Hill         Corrected dodgy hardcoded layout key
 * 2002-12-31     Daniel D'Cotta      Added logging for initDomainAndFilePath()
 * 2003-01-27     Andrew Hill         Added support for using a Download Helper
 * 2003-04-02     Andrew Hill         Render update button label carefully
 * 2003-04-14     Andrew Hill         setInsertNode(), modified rendering id lookups
 * 2003-07-25     Daniel D'Cotta      GridForm integration
 * 2003-10-15     Daniel D'Cotta      Support GridDocs being split into multiple folders
 * 2006-11-14     Regina Zeng         Support GDOC DETAIL 
 * 2006-12-13     Tam Wei Xiang       modified method initDomainAndFilePath(...), remove
 *                                    the hack on GdocDetail
 */
package com.gridnode.gtas.client.web.renderers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.download.IDownloadHelper;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.web.xml.IDocumentManager;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public class MultifilesRenderer  extends AbstractRenderer
{
  private static final Log _log = LogFactory.getLog(MultifilesRenderer.class); // 20031209 DDJ
  
  private static final String TEMP_SUB_FOLDER = "tempContent";

  private static String[] _ids = new String[]
  { //20030414AH
    "multifiles_content",
    "multifiles_footer",
    "multifiles_update",
    "multifiles_filenames",
    "multifiles_file",
    "multifiles_footer",
  };

  //protected final String _dlServletPath = "/downloadServlet"; // @todo - obtain from forward mappings;
  protected final String _dlServletPath; //20030127AH
  protected final String _gfServletPath; // 20030725 DDJ

  protected String _insertId;
  protected FormFileElement[] _formFileElements;
  protected boolean _isMandatory;
  protected boolean _isCollection;
  protected boolean _isViewOnly;
  protected boolean _isDownloadable;
  protected String _layoutKey;
  protected Number _fieldId;
  protected IGTEntity _entity;
  protected Element _valueElement;
  protected String _updateLabel;

  protected String _domain;
  protected String _filePath;

  protected Element _insertNode; //20030414AH

  protected String _dlhKey; //20030127AH
  protected Long _gridDocId; // 20030725 DDJ

  public MultifilesRenderer(RenderingContext rContext)
  {
    super(rContext);

    _dlServletPath = "/downloadAction.do"; //HARDCODING (again)!!! @todo: lookup
    _gfServletPath = "/editGridDocumentAction.do"; // 20030725 DDJ

    reset();
  }

  public void reset()
  {
    _insertNode = null; //20030414AH
    _insertId = null;
    _formFileElements = null;
    _isMandatory = false;
    _isCollection = false;
    _isViewOnly = false;
    _isDownloadable = true;
    _layoutKey = IDocumentKeys.MULTIFILES;
    _fieldId = null;
    _entity = null;
    _valueElement = null;
    _updateLabel = "multifiles.update";
    _dlhKey = null; //20030127AH
    _gridDocId = null;

    _domain = "";
    _filePath = "";
  }

  public void setInsertNode(Element insertNode)
  { _insertNode = insertNode; } //20030414AH

  public Element getInsertNode()
  { return _insertNode; } //20030414AH

  public void setDlhKey(String dlhKey) //20030127AH
  { _dlhKey = dlhKey; }

  public String getDlhKey() //20030127AH
  { return _dlhKey; }

  public void setUpdateLabel(String updateLabel)
  { _updateLabel = updateLabel; }

  public String getUpdateLabel()
  { return _updateLabel; }

  public void setFieldId(Number fieldId)
  { _fieldId = fieldId; }

  public Number getFieldId()
  { return _fieldId; }

  public void setEntity(IGTEntity entity)
  { _entity = entity; }

  public void setMandatory(boolean flag)
  { _isMandatory = flag; }

  public boolean isMandatory()
  { return _isMandatory; }

  public void setCollection(boolean flag)
  { _isCollection = flag; }

  public boolean isCollection()
  { return _isCollection; }

  public void setViewOnly(boolean flag)     // 07102002 DDJ
  { _isViewOnly = flag; }

  public boolean isViewOnly()               // 07102002 DDJ
  { return _isViewOnly; }

  public void setDownloadable(boolean flag) // 14102002 DDJ
  { _isDownloadable = flag; }

  public boolean isDownloadable()           // 14102002 DDJ
  { return _isDownloadable; }

  public void setFormFileElements(FormFileElement[] formFileElements)
  { _formFileElements = formFileElements; }

  public FormFileElement[] getFormFileElements()
  { return _formFileElements; }

  public void setInsertId(String value)
  { _insertId = value; }

  public String getInsertId()
  { return _insertId; }

  public void setLayoutKey(String documentKey)
  { _layoutKey = documentKey; }

  public String getLayoutKey()
  { return _layoutKey; }
  
  public Element getValueElement()
  {
// 07102002 DDJ: _valueElement can be null, in view mode, etc
//    if(_valueElement == null)
//    {
//      throw new java.lang.IllegalStateException("Value element has not been created");
//    }
    return _valueElement;
  }

  public void setGridDocId(Long gridDocId)    // 20030725 DDJ
  { _gridDocId = gridDocId; }

  public Long getGridDocId()                  // 20030725 DDJ
  { return _gridDocId; }

  protected void render() throws RenderingException
  {
    try
    {
      if( (_insertId == null) && (_insertNode == null) ) //20030414AH
      {
        throw new java.lang.NullPointerException("insertId or insertNode not specified");
      }
      if(_fieldId == null)
      {
        throw new java.lang.NullPointerException("fieldId not specified");
      }
      String fieldName = _entity.getFieldName(_fieldId);
      initDomainAndFilePath();
      RenderingContext rContext = getRenderingContext();

      Element insertNode = insertMultifilesLayout(rContext); //20030414AH

      //Element fileRowParent = getElementById("multifiles_filenames",true);
      Element fileRowParent = findElement(insertNode,null,"id","multifiles_filenames");
      if(fileRowParent == null) throw new RenderingException("Node 'multifiles_filenames' not found");
      removeAllChildren(fileRowParent);
      //20030414AH - removeIdAttribute(fileRowParent);

      int rows = 0;
      FormFileElement[] formFileElements = getFormFileElements();
      if(formFileElements != null)
      {
        for(int i=0; i < formFileElements.length; i++)
        {
          FormFileElement ffe = formFileElements[i];
          if(!ffe.isToDelete())
          {
            createFileItemElement(ffe, fileRowParent);
            rows++;
          }
        }
      }

      if(isViewOnly())
      {
        Element footer = findElement(insertNode,null,"id","multifiles_footer");
        if(footer != null)
        {
          removeNode(footer, false);
        }
      }
      else
      {
        //20030414AH - Element mfUpdate = renderLabelCarefully("multifiles_update",getUpdateLabel(), false); //20030402AH
        Element mfUpdate = findElement(insertNode,null,"id","multifiles_update"); //20030414AH
        if(mfUpdate != null)
        { //20030130AH
          //20030414AH - mfUpdate.setAttribute("id", fieldName + "_update");
          renderLabelCarefully(mfUpdate, getUpdateLabel() ); //20030414AH

        }
        //20030414AH - Element fileInput = getElementById("multifiles_file");
        Element fileInput = findElement(insertNode,null,"id","multifiles_file"); //20030414AH
        if(fileInput == null) throw new RenderingException("Cannot find element with id 'multifiles_file'"); //20030414AH
        if(rows == 0 || isCollection())
        {
          //20030414AH - fileInput.setAttribute("id", fieldName + "_value");
          fileInput.setAttribute("name", fieldName + "Upload");
          _valueElement = fileInput;
          if(isMandatory())
          {
            fileInput.setAttribute("class", "mandatory");
          }
        }
        else
        {
          removeNode(fileInput, true);
        }
      }
      cleanupIds(rContext, insertNode); //20030130AH, 20030414AH

    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering multifiles",t);
    }
  }

  protected void createFileItemElement(FormFileElement ffe, Node parentNode)
    throws RenderingException
  {
    try
    {
      if(ffe == null)
      {
        throw new java.lang.NullPointerException("null FormFileElement");
      }
      if(parentNode == null)
      {
        throw new java.lang.NullPointerException("null parentNode");
      }
      Document doc = parentNode.getOwnerDocument();
      Element fileRow = doc.createElement("tr");
      Element fileCell = doc.createElement("td");
      fileCell.setAttribute("nowrap","nowrap"); //20030414AH

      if(!isViewOnly())
      {
        String fieldName = _entity.getFieldName(_fieldId);
        Element fileDelete = doc.createElement("input");
        fileDelete.setAttribute("type", "checkbox");
        fileDelete.setAttribute("name", fieldName + "Delete");
        fileDelete.setAttribute("value", ffe.getId());
        fileCell.appendChild(fileDelete);
      }

      Node fileLabel;
      if(ffe.isToUpload())
      {
        fileLabel = doc.createTextNode(ffe.getFileName());
      }
      else
      {
        String filename = ffe.getFileName();
        String url = null;
        try
        {
          if(_isDownloadable)
          {
            // 20030725 DDJ: GridForm integration
            url = (_gridDocId == null) ?
                  _dlServletPath :
                  StaticWebUtils.addParameterToURL(_gfServletPath, IRequestKeys.GRID_DOC_ID, _gridDocId.toString());
            //@todo: a stringbuffer version of addParameterToUrl would help efficiency...
            url = StaticWebUtils.addParameterToURL(url, IRequestKeys.DOMAIN, _domain);
            url = StaticWebUtils.addParameterToURL(url, IRequestKeys.FILE_PATH, _filePath + filename);
            //20030127AH - Dlh support.....
            String dlhKey = getDlhKey();
            if(dlhKey != null)
            { //Append the download helper session attribute name to request parameters
              url = StaticWebUtils.addParameterToURL(url, IDownloadHelper.DOWNLOAD_HELPER_ID_KEY, dlhKey);
            }
            //...
            url = getRenderingContext().getUrlRewriter().rewriteURL(url,false);
            fileLabel = doc.createElement("a");
            ((Element)fileLabel).setAttribute("href", url); //20030414AH
            ((Element)fileLabel).setAttribute("target", "_new"); //20030414AH
          }
          else
          {
            fileLabel = doc.createElement("span"); //20030414AH
          }
          fileLabel.appendChild(doc.createTextNode(filename)); //20030414AH
        }
        catch(Throwable t)
        {
          throw new RenderingException("Error rendering file link/label",t); //20030414AH
        }
     }

      fileCell.appendChild(fileLabel);
      fileRow.appendChild(fileCell);
      parentNode.appendChild(fileRow);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating row element for file",t);
    }
  }

  public void initFromLayout(Element element)
    throws RenderingException
  {
    try
    {
      if(element == null) return;
      String text = getNodeText(element);
      if( (text == null) || (text.length() == 0) ) return;
      int index = text.indexOf("viewOnly");
      setViewOnly(index != -1);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error initialising MultiselectorRenderer based on layout content of element:" + element,t);
    }
  }

  protected Element insertMultifilesLayout(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      boolean nodeIsParent = true;//20030414AH
      Node insertNode = getInsertNode(); //20030414AH
      if(insertNode == null)
      { //20030414AH
        nodeIsParent = false;
        insertNode = getElementById(_insertId,false);
      }
      if(insertNode == null)
      {
        throw new java.lang.NullPointerException("null insertNode or insertId");
      }

      IDocumentManager docManager = rContext.getDocumentManager();
      if(docManager == null)
      {
        throw new java.lang.NullPointerException("Internal Assertion Failure: null documentManager");
      }

      Document mLayout = docManager.getDocument(_layoutKey,false);
      Node multifiles = getElementByAttributeValue("multifiles_content", "id", mLayout);
      if(multifiles == null)
      {
        throw new RenderingException("Could not find node with id \"multifiles_content\" "
                                      + "in document with key \"" + _layoutKey + "\"");
      }

      if(nodeIsParent)
      { //20030414AH
        multifiles = _target.importNode(multifiles,true);
        insertNode.appendChild(multifiles);
        return (Element)multifiles;
      }
      else
      {
        // 20021126 DDJ: Clone node as Document is now cached
        return (Element)importAndSubstitute(multifiles.cloneNode(true), insertNode, true); //20030414AH
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error inserting layout xhtml for multifiles",t);
    }
  }

  protected void initDomainAndFilePath() // 14102002 DDJ
    throws RenderingException
  {
    if(_entity == null) throw new NullPointerException("entity is null"); //20030414AH
    if(_fieldId == null) throw new NullPointerException("_fieldId is null"); //20030414AH
    try
    {
      IGTFieldMetaInfo fmi = _entity.getFieldMetaInfo(_fieldId);
      if( !(fmi.getConstraintType() == IConstraint.TYPE_FILE) )
      {
        throw new java.lang.IllegalArgumentException("Not a File field - constraint type="
                                                    + fmi.getConstraintType());
      }

      IFileConstraint constraint = (IFileConstraint)fmi.getConstraint();
      if(!constraint.isFileName())
      {
        throw new java.lang.IllegalArgumentException("Not the primary field for this file field");
      }
      _isDownloadable = constraint.isDownloadable();
      if(_isDownloadable)
      {
        String pathKeyField = constraint.getPathKeyFieldName();
        String subPathField = constraint.getSubPathFieldName();
        String pathKey = null;
        if(pathKeyField == null)
        {
          pathKey = constraint.getFixedPathKey();
        }
        else
        {
          pathKey = _entity.getFieldString(pathKeyField);
        }
        if(pathKey == null)
        {
          pathKey = IPathConfig.PATH_TEMP;
        }
        String subPath = null;
        if(subPathField == null)
        {
          subPath = "";
        }
        else
        {
          subPath = _entity.getFieldString(subPathField);
        }

        _domain = FileUtil.getDomain();
        _filePath = (pathKey.length() != 0) ? FileUtil.getPath(pathKey) + subPath : ""; // 20030102 DDJ

        if(_log.isDebugEnabled())
        {
          _log.debug("_entity = " + _entity + " _fieldId = " + _fieldId);
          _log.debug("pathKeyField = " + pathKeyField + " subPathField = " + subPathField);
          _log.debug("pathKey = " + pathKey + " subPath = " + subPath);
          _log.debug("_domain = " + _domain + " _filePath = " + _filePath);
        }

        // 20031015 DDJ: Hack - Support GridDocs being split into multiple folders        
        if(_entity instanceof IGTGridDocumentEntity)
        {
          if(IGTGridDocumentEntity.U_DOC_FILENAME.equals(_fieldId)) // 20040108 DDJ: Fix for GNDB00016735
          {
            String folder = ((IGTGridDocumentEntity)_entity).getFieldString(IGTGridDocumentEntity.FOLDER);
            _filePath = _filePath + folder + "/";
          }
        }
        
        /*
        // 20061114 RZ: Support GDOC DETAIL
        else if(_entity instanceof IGTGdocDetailEntity)
        {
          if(IGTGdocDetailEntity.U_DOC_FILENAME.equals(_fieldId))
          {
            String folder = TEMP_SUB_FOLDER; 
            _filePath = _filePath + folder + "/";
          }
        }*/
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Multifiles renderer encountered error preparing"
                                    + "to initialising domain and path for field "
                                    + _fieldId + " of entity " + _entity, t); //20030414AH
    }
  }

  private void cleanupIds(RenderingContext rContext, Element insertNode) throws RenderingException
  { //20030130AH - Cleanup any leftover multifiles_xxx ids so that they wont interfere
    //with any other multifiles we render in this dom. While it would be marginally more
    //efficient to do this at the point we use these, having this method should make it
    //easier to maintain the code when we add new stuff later - otherwise its easy to forget.
    //20030414AH - Use array and find within the insertNode as we may not have it in
    //the target yet if rendering a listview cell
    for(int i=0; i < _ids.length; i++)
    {
      Element element = findElement(insertNode, null, "id", _ids[i]);
      if(element != null) element.removeAttribute("id");
    }
  }
}