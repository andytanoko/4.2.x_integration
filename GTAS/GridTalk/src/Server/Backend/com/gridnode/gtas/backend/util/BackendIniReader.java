/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendIniReader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 16 2002    Koh Han Sing        GNDB00006473 : Multiple triggering of
 *                                    backend send problem
 * Apr 02 2002    Koh Han Sing        Add in support for attachment
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 */
package com.gridnode.gtas.backend.util;

public class BackendIniReader implements IBackendIniReader
{
  public BackendIniReader()
  {
  }

  public BackendIni readIni(String iniFile)
  {
    InitManager initMgr = new InitManager();
    BackendIni backendIni = new BackendIni();
    initMgr.loadProperties(iniFile);
    backendIni.setPortName(initMgr.getProperty("PORT_NAME"));
    backendIni.setHostDir(initMgr.getProperty("HOST_DIR"));
    backendIni.setGT_IP(initMgr.getProperty("GT_IP"));
    backendIni.setPortNumber(initMgr.getProperty("GT_PORT_NUMBER"));
    backendIni.setUserName(initMgr.getProperty("USER_NAME"));
    backendIni.setPassword(initMgr.getProperty("PASSWORD"));
    backendIni.setEmail(initMgr.getProperty("EMAIL"));
    backendIni.setSmtp(initMgr.getProperty("SMTP"));
    backendIni.setImportedDir(initMgr.getProperty("IMPORTED_DIR"));
    backendIni.setExceptionDir(initMgr.getProperty("EXCEPTION_DIR"));
    // Koh Han Sing 16/03/2002
    backendIni.setLockFilename(initMgr.getProperty("LOCKFILE"));
    backendIni.setTimeout(initMgr.getProperty("TIMEOUT"));
    backendIni.setCommandFile(initMgr.getProperty("COMMAND_FILE"));
    // Koh Han Sing 02/04/2002
    backendIni.setAttachmentDirectory(initMgr.getProperty("ATTACHMENT_DIR"));
    // Koh Han Sing 21/12/2002
    backendIni.setBizEntId(initMgr.getProperty("BIZ_ENT_ID"));
//    backendIni.setOutputDirectory(initMgr.getProperty("OUTPUT_DIR"));
//    backendIni.setIsDiffFilename(initMgr.getProperty("IS_DIFF_FILE_NAME"));
//    backendIni.setIsOverWrite(initMgr.getProperty("IS_OVERWRITE"));
//    backendIni.setIsNoParameter(initMgr.getProperty("IS_NO_PARAMETER"));
//    backendIni.setIsExecutable(initMgr.getProperty("IS_EXECUTABLE"));
//    backendIni.setFilename(initMgr.getProperty("FILE_NAME"));
//    backendIni.setCommandFileDir(initMgr.getProperty("COMMAND_FILE_DIR"));
//    backendIni.setCommandFile(initMgr.getProperty("COMMAND_FILE"));
//    backendIni.setSubject(initMgr.getProperty("SUBJECT"));
//    backendIni.setSender(initMgr.getProperty("SENDER"));
//    backendIni.setUserUID(initMgr.getProperty("USER_UID"));
    return backendIni;
  }
}