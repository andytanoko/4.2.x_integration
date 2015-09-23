/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTCertificateManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-13     Andrew Hill         Created
 * 2003-01-24     Andrew Hill         prepareExportFile()
 * 2003-04-14     Andrew Hill         changePrivateCertPassword()
 * 2003-04-15     Andrew Hill         exportKeyStore(), exportTrustStore()
 * 2003-07-03     Andrew Hill         getListPager(isPartner)
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTCertificateManager extends IGTManager
{
  public String prepareExportFile(IGTCertificateEntity cert, String baseFilename)
    throws GTClientException; //20030128AH

  public void changePrivateCertPassword(String oldPassword, String newPassword)
    throws GTClientException; //20030414AH

  public void exportKeyStore(Long uid) throws GTClientException; //20030415AH

  public void exportTrustStore(Long uid) throws GTClientException; //20030415AH
  
  public IGTListPager getListPager(Boolean isPartner) throws GTClientException; //20030703AH

}