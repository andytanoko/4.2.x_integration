/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveGridDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-23     Daniel D'Cotta      Created (GridForm 2.0 intergration)
 */
package com.gridnode.gtas.client.web.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTGridDocumentEntity;
import com.gridnode.gtas.client.ctrl.IGTGridDocumentManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public class SaveGridDocumentAction extends GTActionBase
{
  protected static final Log _log = LogFactory.getLog(SaveGridDocumentAction.class);

  protected static final String CLOSE_WINDOW_MAPPING = "closeWindow";

  public ActionForward execute( ActionMapping mapping,
                                ActionForm actionForm,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception
  {
    try
    {
      ActionContext actionContext = new ActionContext(mapping,actionForm,request,response);
      actionContext.setLog(_log);
      SaveGridDocumentAForm form = (SaveGridDocumentAForm)actionForm;
      validateFormState(form);

      String gridDocId = form.getGridDocId();
      String gaiaDocId = form.getGaiaDocId();

      // download the document from GAIA
      GAIAClient gaiaClient = new GAIAClient();
      File file = gaiaClient.downloadDocument(StaticUtils.primitiveIntValue(gaiaDocId));
      if(file == null || !file.exists())
      {
        throw new GTClientException("file is null or does not exist");
      }

      // get the grid document entity
      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest());
      IGTGridDocumentManager manager = (IGTGridDocumentManager)gtasSession.getManager(IGTEntity.ENTITY_GRID_DOCUMENT);
      IGTGridDocumentEntity gridDoc = (IGTGridDocumentEntity)manager.getByUid(StaticUtils.longValue(gridDocId));
      if(gridDoc == null)
      {
        throw new NullPointerException("GridDocument with uid: " + gridDocId + " not found");
      }

      // transfer the file to temp
      String pathKey = IPathConfig.PATH_TEMP;
      String subPath = gtasSession.getUserId() + "/in/";
      String oldFilename = gridDoc.getFieldString(gridDoc.U_DOC_FILENAME);

      InputStream stream = new FileInputStream(file);
      String fileName = FileUtil.create(pathKey, subPath, oldFilename, stream);
      String fullPath = FileUtil.getPath(pathKey) + subPath + fileName;
      if(_log.isInfoEnabled())
      {
        _log.info("GridDocument transfered to " + fullPath);
      }

      // call the manager to fire the update event
      gridDoc.setFieldValue(gridDoc.U_DOC_FILENAME, fullPath);
      manager.update(gridDoc);

      if(_log.isInfoEnabled())
      {
        _log.info("GridDocument succesfully updated from GAIA");
      }
      return mapping.findForward(CLOSE_WINDOW_MAPPING);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error executing SaveGridDocumentAction",t);
    }
  }

  protected void validateFormState(SaveGridDocumentAForm form)
    throws IllegalStateException
  {
//    if(StaticUtils.stringEmpty(form.getDomain()))
//      throw new IllegalStateException("domain is undefined");
//    if(StaticUtils.stringEmpty(form.getFilePath()))
//      throw new IllegalStateException("filePath is undefined");
    if(StaticUtils.stringEmpty(form.getGridDocId()))
      throw new IllegalStateException("gridDocId is undefined");
    if(StaticUtils.primitiveLongValue(form.getGridDocId()) <= 0)
      throw new IllegalStateException("gridDocId is not a valid value");
    if(StaticUtils.stringEmpty(form.getGaiaDocId()))
      throw new IllegalStateException("gaiaDocId is undefined");
    if(StaticUtils.primitiveLongValue(form.getGaiaDocId()) <= 0)
      throw new IllegalStateException("gaiaDocId is not a valid value");
  }
}
