/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 29 2004    Neo Sok Lay         Remove file extension before creating
 *                                    temp file.
 * Sep 29 2005    Neo Sok Lay         change of JDOM XMLOutputter syntax  
 * Jan 17 2006    Neo Sok Lay         Change sendInfoDoc)(): Factor out the writing portion to another method.                                 
 */
package com.gridnode.gtas.server.rnif.act;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.EnterpriseUtil;
import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

abstract public class SendDocumentAction
{
  static final String NNAME= "contactName";
  static final String NEMAIL= "emailAddress";
  static final String NFAX= "faxNumber";
  static final String NTEL= "telephoneNumber";

  public SendDocumentAction()
  {
  }


  protected BusinessEntity _senderBE = null;
  protected BusinessEntity _recipientBE = null;
  protected String         _defName = null;

  static boolean emptyStr(String value)
  {
    return (null == value) || ("".equals(value.trim()));
  }

  static HashMap getContactInfo(BusinessEntity be)
  {
    WhitePage wp= be.getWhitePage();
    if (null == wp)
      return null;
    HashMap result= new HashMap();
    String value= null;

    value= wp.getContactPerson();
    if (!emptyStr(value))
    {
      result.put(NNAME, value);
    }

    value= wp.getEmail();
    if (!emptyStr(value))
    {
      result.put(NEMAIL, value);
    }

    value= wp.getFax();
    if (!emptyStr(value))
    {
      result.put(NFAX, value);
    }

    value= wp.getTel();
    if (!emptyStr(value))
    {
      result.put(NTEL, value);
    }
    return result;
  }

  protected void setBE(GridDocument originalGDoc) throws RnifException
  {
    try
    {
      _senderBE= EnterpriseUtil.getBE(
          originalGDoc.getSenderNodeId(),
          originalGDoc.getSenderBizEntityId());
    }catch (Exception ex)
    {
      throw RnifException.entityNotFoundEx("in sendDocument, cannot find Sender BE of originalGDOc " +  originalGDoc.getEntityDescr(), ex);
    }

   try
   {
    _recipientBE = EnterpriseUtil.getBE(
        originalGDoc.getRecipientNodeId(),
        originalGDoc.getRecipientBizEntityId());
   }
    catch (Exception ex)
    {
     throw RnifException.entityNotFoundEx("in sendDocument, cannot find Recipient BE of originalGDOc " +  originalGDoc.getEntityDescr(), ex);
    }
  }

  public void execute(GridDocument gDoc, boolean isRequest, ProcessDef def, ProcessAct curAct, Object param)
    throws RnifException
  {
    Logger.debug("[" + getActualClassName() + "]execute() enter " + gDoc.getEntityDescr());
    _defName = def.getDefName();
    setBE(gDoc);
    setExtraParam(param, isRequest);
    sendInfoDoc(gDoc, def, curAct);
    Logger.debug("[" + getActualClassName() + "]execute() exit " + gDoc.getEntityDescr());
  }

  void setExtraParam(Object param, boolean isRequest) throws RnifException
  {
  }

  void sendInfoDoc(GridDocument originalGDoc, ProcessDef def, ProcessAct pAct) throws RnifException
  {
    String fileName= null;
    File fileObject= null;
    
    String udocName = originalGDoc.getUdocFilename();
    Logger.debug("[SendDocumentAction.sendInfoDoc] original udocName=" + udocName);
    fileName= getBaseDocName(udocName);

    //NSL20060117
    fileObject = createInfoDoc(originalGDoc, def, pAct, fileName);
    
    sendDocument(fileObject, fileName, originalGDoc);
  }

  protected File createInfoDoc(GridDocument originalGDoc, ProcessDef def, ProcessAct pAct, String fileName) throws RnifException
  {
    File fileObject= null;
    try
    {
      ByteArrayOutputStream byteOS= writeUDoc(originalGDoc, def, pAct);
      fileObject= File.createTempFile(FileUtil.removeExtension(fileName), null);
      Logger.debug("[SendDocumentAction.createInfoDoc] generated udocfileName=" +fileName + ";tempFileCreated is=" + fileObject.getAbsolutePath() );
      FileOutputStream fos= new FileOutputStream(fileObject);
      byteOS.writeTo(fos);
      fos.close();
    }
    catch (IOException e)
    {
      Logger.warn("[SendDocumentAction.createInfoDoc]", e);
      throw RnifException.fileProcessErr(getActualClassName() + " document write IOExeption", e);
    }
    catch (Throwable e)
    {
      Logger.warn("[SendDocumentAction.createInfoDoc] Throwable", e);
      throw RnifException.fileProcessErr(getActualClassName() + " document write General Error", e);
    }

    return fileObject;
  }
  
  ByteArrayOutputStream writeUDoc(GridDocument originalGDoc, ProcessDef def, ProcessAct pAct)
    throws RnifException, IOException
  {
    Document xmlDoc= writeInfo(originalGDoc, def, pAct);
    ByteArrayOutputStream byteOS= new ByteArrayOutputStream();
    //NSL20050929 change of JDOM XMLOutputter syntax
    XMLOutputter xmlout= new XMLOutputter(Format.getPrettyFormat());
    //xmlout.setNewlines(true);
    //xmlout.setIndent(true);
    xmlout.output(xmlDoc, byteOS);
    return byteOS;
  }

  abstract String getBaseDocName(String originalFileName);
  abstract String getActualClassName();
  abstract String getProcessMsgType();

  abstract Document writeInfo(GridDocument originalGDoc, ProcessDef def, ProcessAct pAct)
    throws RnifException;

  abstract String getUDocDocType();

  abstract void sendDocument(File uDoc, String udocName, GridDocument originalGDoc)
    throws RnifException;

}
