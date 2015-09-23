/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendIni.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 16 2002    Koh Han Sing        GNDB00006473 : Multiple triggering of
 *                                    backend send problem
 * Apr 02 2002    Koh Han Sing        Add in fields to support attachments
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 */
package com.gridnode.gtas.backend.util;

public class BackendIni
{
  private String   portName;
  private String   hostDir;
  private String   GT_IP;
  private int      portNumber;
  private String   userName;
  private String   password;
  private String   email;
  private String   smtp;
  private String   importedDir;
  private String   exceptionDir;
  private String   lockFilename;      // Koh Han Sing 16/03/2002
  private int      timeout;           // Koh Han Sing 16/03/2002
  private String   commandFile;       // Koh Han Sing 16/03/2002
  private String   attachmentDir;     // Koh Han Sing 02/04/2002
  private String   bizEntId;          // Koh Han Sing 21/12/2002
//  private boolean  isDiffFilename;
//  private boolean  isOverWrite;
//  private boolean  isNoParameter;
//  private boolean  isExecutable;
//  private String   filename;
//  private int      userUID;
//  private String   commandFileDir;
//  private String   commandFile;
//  private String   subject;
//  private String   sender;

  public BackendIni()
  {
  }

  public void setPortName(String portName)
  {
    this.portName = portName;
  }

  public String getPortName()
  {
    return this.portName;
  }

  public void setHostDir(String hostDir)
  {
    this.hostDir = hostDir;
  }

  public String getHostDir()
  {
    return this.hostDir;
  }

  public void setGT_IP(String GT_IP)
  {
    this.GT_IP = GT_IP;
  }

  public String getGT_IP()
  {
    return this.GT_IP;
  }

  public void setPortNumber(String portNumber)
  {
    if(portNumber != null)
    {
      this.portNumber = (new Integer(portNumber)).intValue();
    }
  }

  public void setPortNumber(int portNumber)
  {
    this.portNumber = portNumber;
  }

  public int getPortNumber()
  {
    return this.portNumber;
  }

  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  public String getUserName()
  {
    return this.userName;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getPassword()
  {
    return this.password;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getEmail()
  {
    return this.email;
  }

  public void setSmtp(String smtp)
  {
    this.smtp = smtp;
  }

  public String getSmtp()
  {
    return this.smtp;
  }

  public void setImportedDir(String importedDir)
  {
    this.importedDir = importedDir;
  }

  public String getImportedDir()
  {
    return this.importedDir;
  }

  public void setExceptionDir(String exceptionDir)
  {
    this.exceptionDir = exceptionDir;
  }

  public String getExceptionDir()
  {
    return this.exceptionDir;
  }

  // Koh Han Sing 16/03/2002
  public void setLockFilename(String lockFilename)
  {
    this.lockFilename = lockFilename;
  }

  // Koh Han Sing 16/03/2002
  public String getLockFilename()
  {
    return this.lockFilename;
  }

  // Koh Han Sing 16/03/2002
  public void setTimeout(String timeout)
  {
    if (timeout != null)
    {
      this.timeout = (new Integer(timeout)).intValue();
    }
  }

  // Koh Han Sing 16/03/2002
  public int getTimeout()
  {
    return this.timeout;
  }

  public void setCommandFile(String commandFile)
  {
    this.commandFile = commandFile;
  }

  public String getCommandFile()
  {
    return this.commandFile;
  }

  // Koh Han Sing 02/04/2002
  public void setAttachmentDirectory(String attachmentDir)
  {
    this.attachmentDir = attachmentDir;
  }

  // Koh Han Sing 02/04/2002
  public String getAttachmentDirectory()
  {
    return this.attachmentDir;
  }

  public void setBizEntId(String bizEntId)
  {
    this.bizEntId = bizEntId;
  }

  public String getBizEntId()
  {
    return this.bizEntId;
  }

//  public void setOutputDirectory(String outputDirectory)
//  {
//    this.outputDirectory = outputDirectory;
//  }
//
//  public String getOutputDirectory()
//  {
//    return this.outputDirectory;
//  }

//  public void setIsDiffFilename(String isDiffFilename)
//  {
//    if(isDiffFilename != null)
//    {
//      this.isDiffFilename = (new Boolean(isDiffFilename)).booleanValue();
//    }
//  }
//
//  public void setIsDiffFilename(boolean isDiffFilename)
//  {
//    this.isDiffFilename = isDiffFilename;
//  }
//
//  public boolean isDiffFilename()
//  {
//    return this.isDiffFilename;
//  }
//
//  public void setIsOverWrite(String isOverWrite)
//  {
//    if(isOverWrite != null)
//    {
//      this.isOverWrite = (new Boolean(isOverWrite)).booleanValue();
//    }
//  }
//
//  public void setIsOverWrite(boolean isOverWrite)
//  {
//    this.isOverWrite = isOverWrite;
//  }
//
//  public boolean isOverWrite()
//  {
//    return this.isOverWrite;
//  }
//
//  public void setIsNoParameter(String isNoParameter)
//  {
//    if(isNoParameter != null)
//    {
//      this.isNoParameter = (new Boolean(isNoParameter)).booleanValue();
//    }
//  }
//
//  public void setIsNoParameter(boolean isNoParameter)
//  {
//    this.isNoParameter = isNoParameter;
//  }
//
//  public boolean isNoParameter()
//  {
//    return this.isNoParameter;
//  }
//
//  public void setIsExecutable(String isExecutable)
//  {
//    if(isExecutable != null)
//    {
//      this.isExecutable = (new Boolean(isExecutable)).booleanValue();
//    }
//  }
//
//  public void setIsExecutable(boolean isExecutable)
//  {
//    this.isExecutable = isExecutable;
//  }
//
//  public boolean isExecutable()
//  {
//    return this.isExecutable;
//  }
//
//  public void setFilename(String filename)
//  {
//    this.filename = filename;
//  }
//
//  public String getFilename()
//  {
//    return this.filename;
//  }
//
//  public void setCommandFileDir(String commandFileDir)
//  {
//    this.commandFileDir = commandFileDir;
//  }
//
//  public String getCommandFileDir()
//  {
//    return this.commandFileDir;
//  }
//
//  public void setCommandFile(String commandFile)
//  {
//    this.commandFile = commandFile;
//  }
//
//  public String getCommandFile()
//  {
//    return this.commandFile;
//  }
//
//  public void setSubject(String subject)
//  {
//    this.subject = subject;
//  }
//
//  public String getSubject()
//  {
//    return this.subject;
//  }
//
//  public void setSender(String sender)
//  {
//    this.sender = sender;
//  }
//
//  public String getSender()
//  {
//    return this.sender;
//  }
//
  /*
  public void setUserUID(String userUID)
  {
    if(userUID != null)
    {
      this.userUID = (new Integer(userUID)).intValue();
    }
  }

  public void setUserUID(int userUID)
  {
    this.userUID = userUID;
  }

  public int getUserUID()
  {
    return this.userUID;
  }

  public Object getUserUIDObj()
  {
    return new Integer(this.userUID);
  }
*/
}