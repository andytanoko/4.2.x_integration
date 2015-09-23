/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTActionFormBase.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-04-24     Andrew Hill         Created
 * 2002-08-23     Andrew Hill         Added multiFile support
 * 2002-10-23     Daniel D'Cotta      Modified to keep track of files in B-Tier
 * 2002-12-17     Andrew Hill         Call special method for NS6 resets - doNetscape6Reset()
 * 2003-01-21     Andrew Hill         addFileToField returns boolean (true if a file actually added)
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;

public class GTActionFormBase extends ActionForm implements Serializable
{
  private HashMap _fileLists = null;
  private int _fileIdCounter = 0;

  protected void disposeFormFileElements()
  {
    if(_fileLists == null)
    {
      return;
    }
    Iterator i = _fileLists.values().iterator();
    while(i.hasNext())
    {
      ArrayList elements = (ArrayList)i.next();
      Iterator j = elements.iterator();
      while(j.hasNext())
      {
        FormFileElement ffe = (FormFileElement)j.next();
        FormFile fFile = ffe.getFormFile();             // 03102002 DDJ
        if(fFile != null) ffe.getFormFile().destroy();  // 03102002 DDJ
      }
    }
    _fileLists = null;
  }

  protected boolean addFileToField(String fieldName, FormFile file)
  {
    //20030121AH - mod to return true if file added
    if(file == null)
    {
      return false; //20030121AH
    }
    if(file.getFileSize() == 0)
    {
      file.destroy();
      return false; //20030121AH
    }
    // Get the list of files associated with this field
    if(_fileLists == null)
    {
      _fileLists = new HashMap();
    }
    ArrayList fileList = (ArrayList)_fileLists.get(fieldName);
    if(fileList == null)
    { // If a fileList has not been allocated for that field, create it now
      fileList = new ArrayList();
      _fileLists.put(fieldName,fileList);
    }
    String fileId = "" + _fileIdCounter++;
    FormFileElement fileElement = new FormFileElement(fileId,file);
    fileList.add(fileElement);
    return true; //20030121AH
  }
  
  protected FormFileElement getFormFileElement(String fieldName, FormFile wrappedFormFile)
  { //20030731AH - Nasty little method since addFileToField doesnt return the form file (duh!)
    if (wrappedFormFile == null)
      throw new NullPointerException("wrappedFormFile is null");
    if (fieldName == null)
      throw new NullPointerException("fieldName is null");
    FormFileElement[] elements = getFormFileElements(fieldName, true);
    if(elements != null)
    {
      for(int i=0; i < elements.length; i++)
      {
        if( wrappedFormFile == elements[i].getFormFile() ) return elements[i];
      }
    }
    return null;
  }

  // 03102002 DDJ: Added method to handle files in B-Tier
  protected boolean addFileToField(String fieldName, String filename)
  {
    //20030121AH - mod to return true if file was added
    if(filename == null)
    {
      return false; //20030121AH
    }
    if(filename.length() == 0)
    {
      return false; //20030121AH
    }
    // Get the list of files associated with this field
    if(_fileLists == null)
    {
      _fileLists = new HashMap();
    }
    ArrayList fileList = (ArrayList)_fileLists.get(fieldName);
    if(fileList == null)
    { // If a fileList has not been allocated for that field, create it now
      fileList = new ArrayList();
      _fileLists.put(fieldName,fileList);
    }
    String fileId = "" + _fileIdCounter++;
    FormFileElement fileElement = new FormFileElement(fileId,filename);
    fileList.add(fileElement);
    return true; //20030121AH
  }

  public FormFileElement[] getFormFileElements(String fieldName)
  {
    return getFormFileElements(fieldName, false);
  }

  public FormFileElement[] getFormFileElements(String fieldName, boolean includeToBeDeleted)
  {
    if(fieldName == null)
    {
      throw new java.lang.NullPointerException("null fieldname");
    }
    if(_fileLists == null)
    {
      return new FormFileElement[]{};
    }
    ArrayList fileList = (ArrayList)_fileLists.get(fieldName);
    if(fileList == null)
    {
      return new FormFileElement[]{};
    }

    // 07102002 DDJ: Filter off those to be deleted
    if(!includeToBeDeleted)
    {
      ArrayList tempArrayList = new ArrayList();
      for(int i = 0; i < fileList.size(); i++)
      {
        FormFileElement ffe = (FormFileElement)fileList.get(i);
        if(!ffe.isToDelete())
          tempArrayList.add(ffe);
      }
      fileList = tempArrayList;
    }

    FormFileElement[] elements = new FormFileElement[fileList.size()];
    Iterator iterator = fileList.iterator();
    for(int i = 0; i < elements.length; i++)
    {
        elements[i] = (FormFileElement)iterator.next();
    }
    return elements;
  }

  // 03102002 DDJ: Added convience method for single files
  public FormFileElement getFormFileElement(String fieldName)
  {
    return getFormFileElement(fieldName, false);
  }

  // 03102002 DDJ: Added convience method for single files
  public FormFileElement getFormFileElement(String fieldName, boolean includeToBeDeleted)
  {
    FormFileElement[] elements = getFormFileElements(fieldName, includeToBeDeleted);
    return (FormFileElement)StaticUtils.getFirst(elements);
  }

  protected String getFilename(String fieldname, boolean includeToBeDeleted)
  { //20021205AH - convienience method for single files
    FormFileElement ffe = getFormFileElement(fieldname, includeToBeDeleted);
    return (ffe != null) ? ffe.getFileName() : "";
  }

  protected String[] getFilenames(String fieldname, boolean includeToBeDeleted)
  { //20021205AH
    FormFileElement[] ffes = getFormFileElements(fieldname, includeToBeDeleted);
    if (ffes == null)
      throw new NullPointerException("ffes is null"); //20030424AH
    String[] filenames = new String[ffes.length];
    for(int i = 0; i < ffes.length; i++)
    {
      filenames[i] = (ffes[i] != null) ? ffes[i].getFileName() : "";
    }
    return filenames;
  }

  protected void removeFileFromField(String fieldName, String fileId)
  {
    ArrayList fileList = (ArrayList)_fileLists.get(fieldName);
    if(fileList == null) throw new java.lang.IllegalArgumentException("Field does not have any files");
    Iterator iterator = fileList.iterator();
    FormFileElement fileElement = null;
    while( (iterator.hasNext()) && (fileElement == null) )
    {
      FormFileElement element = (FormFileElement)iterator.next();
      if(element.getId().equals(fileId))
      {
        fileElement = element;
      }
    }
    if(fileElement != null)
    {
      FormFile ff = fileElement.getFormFile();
      if(fileElement.isToUpload())
      { // If the file has not yet been transferred to GTAS then we destroy the temp file
        ff.destroy();
        // And remove the element from the list
        fileList.remove(fileElement);
      }
      else
      {
        // Files in gtas are marked as being for deletion, but we dont take any action
        // to remove the file yet.
        fileElement.setToDelete(true);
      }
    }
    else
    {
      throw new java.lang.IllegalArgumentException("Field does not contain file with id " + fileId);
    }
  }

  //remind me again... what is this here for????
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException
  {
    ois.defaultReadObject();
  }

  //remind me again... what is this here for????
  private void writeObject(ObjectOutputStream oos) throws IOException
  {
    oos.defaultWriteObject();
  }

  /**
   * Subclass should override this instead of reset() to do reset processing
   * @param mapping
   * @param request
   */
  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    
  }

  /**
   * When it is detected that the request was submitted from Netscape6, this method will be called
   * instead of doReset(). Its default behaviour is simply to delegate to doReset() but forms
   * whose layout makes uses of certain features such as tabpanes that are affected by the
   * display:none submit bug in Netscape6 may need to override this method to provide
   * specialised handling. See also: http://bugzilla.mozilla.org/show_bug.cgi?id=34297
   * For tabpanes this means checking the appropriate request paremeter to see which tab the
   * user _was_ on and then *only* reseting the relevant fields that were shown on that tab without
   * resetting fields on tabs that were not showing. nb: this behaviour is not compatible with
   * the way non-netscape6 tabpane design works which is why a seperate method is needed and
   * branching logic that is aware of the layout needs to be implemented... Sorry. Not my fault :-(
   * 20021217AH
   */
  public void doNetscape6Reset(ActionMapping mapping, HttpServletRequest request)
  {
    doReset(mapping, request);
  }

  /**
   * Will call doReset if a parameter named reset exists with a value of "true".
   * This method has been finalised and you must override doReset to implement your
   * reset processing. This is to allow us to only call reset when it is required
   * -ie: form submission to update something, but not form submission as a result of
   * redirecting back from a diversion, listview, or view mode where reset will cause problems
   * when those fields that are reset are not being refreshed with correct values from the
   * request parameters.
   * Modified 20021217AH to call doNetscape6Reset() if NS6 detected instead of doReset().
   */
  public final void reset(ActionMapping mapping, HttpServletRequest request)
  {
    String resetRqd = request.getParameter("reset");
    if("true".equals(resetRqd))
    {
      if(StaticWebUtils.isNetscape6(request))
      { //20021217AH
        doNetscape6Reset(mapping, request);
      }
      else
      {
        doReset(mapping, request);
      }
    }
  }

  /**
   * Please perform semantic validation in the action that follows form submission.
   */
  public final ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    return super.validate(mapping, request);
  }

}