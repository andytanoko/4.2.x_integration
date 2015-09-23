/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateDownloadHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTCertificateEntity;
import com.gridnode.gtas.client.ctrl.IGTCertificateManager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.download.DownloadAForm;
import com.gridnode.gtas.client.web.download.SimpleDownloadHelper;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

/**
 * DownloadHelper implementation to invoke the IGTCertificateManager.prepareExportFile() method
 * prior to certificate download
 */
public class CertificateDownloadHelper extends SimpleDownloadHelper
{
  private IGTCertificateEntity _cert;

  public CertificateDownloadHelper(IGTCertificateEntity cert)
  {
    setCert(cert);
  }

  public void setCert(IGTCertificateEntity cert)
  { _cert = cert; }

  public IGTCertificateEntity getCert()
  { return _cert; }

  public void doPreDownload(ActionContext actionContext, DownloadAForm form) throws GTClientException
  {
    try
    {
      IGTCertificateEntity cert = getCert();
      if(cert == null) throw new NullPointerException("cert is null"); //20030416AH
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession( actionContext.getRequest() );
      if(gtasSession == null) throw new NullPointerException("gtasSession is null"); //20030416AH
      IGTCertificateManager certMgr = (IGTCertificateManager)gtasSession.getManager(IGTManager.MANAGER_CERTIFICATE);
      String baseFilename = form.getFilename();
      String actualFilename = certMgr.prepareExportFile(cert, baseFilename);
      form.setActualFilename(actualFilename);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error performing doPreDownload for certificate download helper",t);
    }
  }
}