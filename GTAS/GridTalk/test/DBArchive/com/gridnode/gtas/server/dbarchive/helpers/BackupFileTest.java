package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.File;

import junit.framework.TestCase;

public class BackupFileTest extends TestCase
{

  String backfileName = "TestBack.zip";
  public BackupFileTest(String name)
  {
    super(name);
  }

  public void testGenZipFile() throws Throwable
  {
    try
    {
      Logger.debug("testGenZipFile Enter");
      BackupZipFile zipFile = new BackupZipFile(backfileName);
      zipFile.open(BackupZipFile.OPEN_WRITE);  
      File backFile = new File("c:/my-build.properties");
      zipFile.addFile(backFile, "BACKFile", "MYBUILD/zip/");
      zipFile.close();   

    }
    catch (Throwable ex)
    {
      Logger.err("testGenZipFile", ex);
      throw ex;
    }
    Logger.debug("testGenZipFile Exit");

  }

//
// public void testRestoreGridDoc() throws Throwable
//  {
//     try
//    {
//      Logger.debug("testRestoreGridDoc Enter");
//       String fileName = "C:/GTASArchive.arc";
//        _rnifMgr.invokeMethod(
//        null,		
//        "com.gridnode.gtas.server.dbarchive.helpers.DbArchive",
//        "restoreGridDoc",
//        new Class[]{String.class},
//        new Object[] {fileName});
//      Logger.log("testRestoreGridDoc finished");
//
//    }
//    catch (Throwable ex)
//    {
//      Logger.err("testRestoreGridDoc", ex);
//      throw ex;
//    }
//    Logger.debug("testRestoreGridDoc Exit");
//  }


}
