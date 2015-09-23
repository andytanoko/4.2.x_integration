/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveDocumentHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 11 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.document.entities.ejb.IAttachmentLocalObj;
import com.gridnode.gtas.server.document.exceptions.CreateAttachmentException;
import com.gridnode.gtas.server.document.exceptions.RegisterAttachmentException;
import com.gridnode.gtas.server.document.exceptions.UpdateAttachmentLinkException;
import com.gridnode.gtas.server.document.folder.SystemFolder;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.AttachmentRegistry;
import com.gridnode.gtas.server.document.model.GridDocument;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

public class AttachmentHelper
{
  /**
   * Use this method if you are too lazy to create the Attachment entity.
   */
  public static Long createAttachment(File attachmentFile, String orgFilename,
                                      Boolean isOutgoing, Long originalUid,
                                      String partnerId)
    throws CreateAttachmentException
  {
    try
    {
      FileInputStream fis = new FileInputStream(attachmentFile);
      String filename = FileUtil.create(IDocumentPathConfig.PATH_ATTACHMENT,
                                        orgFilename,
                                        fis);

      Attachment attachment = new Attachment();
      attachment.setFilename(filename);
      attachment.setOriginalFilename(orgFilename);
      attachment.setOriginalUid(originalUid);
      attachment.setOutgoing(isOutgoing);
      attachment.setPartnerId(partnerId);


      IAttachmentLocalObj obj =
        (IAttachmentLocalObj)AttachmentEntityHandler.getInstance().create(attachment);

      return (Long)obj.getData().getKey();

    }
    catch (Throwable ex)
    {
      throw new CreateAttachmentException(ex);
    }
  }

  public static boolean isAttachmentSent(Long attachmentUid, String partnerId) throws Exception
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      AttachmentRegistry.ATTACHMENT_UID,
      filter.getEqualOperator(),
      attachmentUid,
      false
    );
    filter.addSingleFilter(
      filter.getAndConnector(),
      AttachmentRegistry.PARTNER_ID,
      filter.getEqualOperator(),
      partnerId,
      false
    );
    Collection list = AttachmentRegistryEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    if (!list.isEmpty())
    {
      Logger.debug("[AttachmentHelper.isAttachmentSent] true");
      return true;
    }
    Logger.debug("[AttachmentHelper.isAttachmentSent] false");
    return false;
  }

  public static void register(Long attachmentUid, String partnerId)
    throws Exception
  {
    try
    {
      Logger.debug("[AttachmentHelper.register] attachmentUid : "+attachmentUid+
                   " partnerId : "+partnerId);
      AttachmentRegistry registry = new AttachmentRegistry();
      registry.setAttachmentUid(attachmentUid);
      registry.setPartnerId(partnerId);
      registry.setDateProcessed(new Date(System.currentTimeMillis()));
      AttachmentRegistryEntityHandler.getInstance().create(registry);
    }
    catch (Throwable throwable)
    {
      throw new RegisterAttachmentException(throwable);
    }
  }

  public static List findIncomingAttachments(GridDocument gdoc, String partnerId)
    throws Exception
  {
    Logger.debug("[AttachmentHelper.findIncomingAttachments] Starts");
    ArrayList newUids = new ArrayList();
    if (!gdoc.isAttachmentLinkUpdated().booleanValue())
    {
      Logger.debug("[AttachmentHelper.findIncomingAttachments] attachment link not updated");
      List attachments = gdoc.getAttachments();
      for (Iterator i = attachments.iterator(); i.hasNext(); )
      {
        Long orgUid = new Long(i.next().toString());
        Long newUid = AttachmentHelper.findIncomingAttachment(orgUid, partnerId);
        if (newUid != null)
        {
          newUids.add(newUid);
        }
        else
        {
          newUids.clear();
          break;
        }
      }
    }
    else
    {
      Logger.debug("[AttachmentHelper.findIncomingAttachments] attachment already updated");
      newUids.addAll(gdoc.getAttachments());
    }
    return newUids;
  }

  public static Long findIncomingAttachment(Long orgAttachmentUid,
                                            String partnerId)
                                            throws Exception
  {
    Logger.debug("[AttachmentHelper.register] orgAttachmentUid : "+orgAttachmentUid+
                 " partnerId : "+partnerId);
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      Attachment.ORIGINAL_UID,
      filter.getEqualOperator(),
      orgAttachmentUid,
      false
    );
    filter.addSingleFilter(
      filter.getAndConnector(),
      Attachment.PARTNER_ID,
      filter.getEqualOperator(),
      partnerId,
      false
    );
    filter.addSingleFilter(
      filter.getAndConnector(),
      Attachment.IS_OUTGOING,
      filter.getEqualOperator(),
      Boolean.FALSE,
      false
    );
    Collection list = AttachmentEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);
    Logger.debug("[AttachmentHelper.findIncomingAttachments] attachment list = "+list);
    if (!list.isEmpty())
    {
      return new Long(list.iterator().next().toString());
    }
    return null;
  }

  public static void updateAttachmentLinks(String partnerId)
    throws Exception
  {
    Logger.debug("[AttachmentHelper.updateAttachmentLinks] partnerId : "+partnerId);
    try
    {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        GridDocument.IS_ATTACHMENT_LINK_UPDATED,
        filter.getEqualOperator(),
        Boolean.FALSE,
        false
      );
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.S_PARTNER_ID,
        filter.getEqualOperator(),
        partnerId,
        false
      );
      Collection list = GridDocumentEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
      for (Iterator i = list.iterator(); i.hasNext(); )
      {
        GridDocument gdoc = (GridDocument)i.next();
        Logger.debug("[AttachmentHelper.updateAttachmentLinks] gdoc uid : "+gdoc.getUId());
        List newUids = findIncomingAttachments(gdoc, partnerId);
        gdoc.setAttachments(newUids);
        gdoc.setAttachmentLinkUpdated(Boolean.TRUE);
        SystemFolder folder = SystemFolder.getSpecificFolder(gdoc.getFolder());
        folder.updateHeader(gdoc);
      }
    }
    catch (Throwable throwable)
    {
      throw new UpdateAttachmentLinkException(throwable);
    }
  }


}