package com.gridnode.pdip.framework.file;

import junit.framework.*;
import java.io.*;
import java.util.zip.*;

import com.gridnode.pdip.framework.file.access.*;
import com.gridnode.pdip.framework.file.ejb.*;
import com.gridnode.pdip.framework.j2ee.*;
import org.apache.webdav.lib.*;

public class TestFileMgrBean extends TestCase implements IConstants {
  public static FileAccess remote;

  public TestFileMgrBean(String s) {
    super(s);
  }

  protected void setUp() {
    remote = new FileAccess();
  /*    try{
      home=(IPdipFileMgrHome)ServiceLookup.getInstance(
        ServiceLookup.CLIENT_CONTEXT).getHome(IPdipFileMgrHome.class);
      assertNotNull("Session Home should not be Null ", home);
      remote = home.create();
      assertNotNull("Session Remote should not be Null ", remote);
    }catch(Exception ex){
      System.out.println(" Exception in SetUp  : "+ex.getMessage());
      ex.printStackTrace();
    }
*/  }

/*  public void testWebdavDirect() {
      String newPath = "test1/test2/test3";
      WebdavResource webdavRes = Util.initWebdavResource("localhost");
      String initialPath = webdavRes.getPath();
      assertNotNull("Webdav Resource should not be null", webdavRes);
      String path = Util.createHigherLevelFolders(webdavRes, newPath);
      try {
        int fileIndex = newPath.lastIndexOf("/");
        if (fileIndex > 0) {
          if (path.equals(initialPath+newPath.substring(0,fileIndex)))
            assertTrue("Folder should be created",webdavRes.mkcolMethod(path+newPath.substring(fileIndex)));
          else
            assertTrue(false);
        } else {
          assertTrue("Folder should be created",webdavRes.mkcolMethod(webdavRes.getPath()+newPath));
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
  }
*/
  public void testWebDAVFilingMethods() {
    if (Util.isLocal())
      return;
    File file = new File("D:/development/FrameWork/data/filetest.txt");
    String result = null;
    try {
      result = remote.createFolder("localhost", "test");
      System.out.println("CreateFolder : "+result);
      assertTrue(result.equals("/griddocs/test"));

      result = remote.createFile("localhost", "test/testfile.txt", file, false);
      System.out.println("CreateFile : "+result);
      assertTrue(result.equals("/griddocs/test/testfile.txt"));

      result = remote.copyFile("localhost", "test/testfile.txt", "test/copiedfile.txt",true);
      System.out.println("CopyFile : "+result);
      assertTrue(result.equals("/griddocs/test/copiedfile.txt"));

      result = remote.copyFolder("localhost", "test", "copiedtest", true);
      System.out.println("CopyFolder : "+result);
      assertTrue(result.equals("/griddocs/copiedtest"));

      result = remote.moveFile("localhost", "test/copiedfile.txt", "movedfile.txt", true);
      System.out.println("MoveFile : "+result);
      assertTrue(result.equals("/griddocs/movedfile.txt"));

      result = remote.moveFolder("localhost", "copiedtest", "movedtest", true);
      System.out.println("MoveFolder : "+result);
      assertTrue(result.equals("/griddocs/movedtest"));

      result = remote.deleteFile("localhost", "movedtest/testfile.txt");
      System.out.println("deleteFile : "+result);
      assertTrue(result.equals("/griddocs/movedtest/testfile.txt"));

      result = remote.deleteFolder("localhost", "movedtest");
      System.out.println("DeleteFolder : "+result);
      assertTrue(result.equals("/griddocs/movedtest"));

      result = remote.renameFolder("localhost", "test", "test2");
      System.out.println("RenameFolder : "+result);
      assertTrue(result.equals("/griddocs/test2"));

      result = remote.renameFile("localhost", "movedfile.txt", "renamedfile.txt");
      System.out.println("RenameFile : "+result);
      assertTrue(result.equals("/griddocs/renamedfile.txt"));

      result = remote.createFolder("localhost", "test1/test2/test3");
      System.out.println("Create multiple folders: "+result);
      assertTrue(result.equals("/griddocs/test1/test2/test3"));

      result = remote.createFile("localhost", "testA/testB/testC/testfile.txt", file, false);
      System.out.println("Create file in multiple folders: "+result);
      assertTrue(result.equals("/griddocs/testA/testB/testC/testfile.txt"));

      result = remote.createFile("localhost", "test folder/test file.txt", file, false);
      System.out.println("Spaces in file name : "+result);
      assertTrue(result.equals("/griddocs/test folder/test file.txt"));

      File resultFile = remote.getFile("localhost", "test folder/test file.txt");
      assertNotNull(resultFile);
      FileInputStream fShow = new FileInputStream(resultFile);
      assertNotNull(fShow);
      byte[] bShow = new byte[(int)resultFile.length()];
      int bufSize = 0;
      bufSize= fShow.read(bShow);
      System.out.print("Content of file obtained from getFile : ");
      for (int i=0; i<(int)resultFile.length(); i++)
        System.out.print((char)bShow[i]);
      System.out.println();

      { //test for raw data streams
        byte[] readShow = new byte[10];
        readShow = remote.readFromFile("localhost", "test folder/test file.txt", 2, 10);
        System.out.print("Content of file obtained from readFromFile : ");
        for (int j=0; j<10; j++) {
          System.out.print((char)readShow[j]);
        }
        System.out.println();
      }

      { //test for zipped data streams
        byte[] readShow = new byte[1000];
        byte[] readZipShow = null;
        readZipShow = remote.readFromFileZipped("localhost", "test folder/test file.txt", 5L, 10);
        ByteArrayInputStream in = new ByteArrayInputStream(readZipShow);
        GZIPInputStream zipShow = new GZIPInputStream(in);
        bufSize = zipShow.read(readShow);
        System.out.print("Content of file obtained from readFromFileZipped size "+ bufSize + " : ");
        for (int j=0; j<bufSize; j++) {
          System.out.print((char)readShow[j]);
        }
        System.out.println();
      }

      MultiFileBlock block;
      { //test for zipped data streams
        byte[] readShow = new byte[1000];
        byte[] readZipShow = null;

        String[] paths= {"test folder/test file.txt", "renamedfile.txt"};
        block = remote.readFromStreamZipped("localhost", paths, 0L, 30);
        readZipShow = block.getBlockData();
        for (int k=0; k<block.getParts(); k++) {
          BlockFileDescriptor fd =  block.getBlockFileDescriptor(k);
          System.out.println("File : "+fd.getFilePath());
          System.out.println("Size : " + fd.getFileSize());
        }
        ByteArrayInputStream in = new ByteArrayInputStream(readZipShow);
        GZIPInputStream zipShow = new GZIPInputStream(in);
        bufSize = zipShow.read(readShow);
        System.out.print("Content of file obtained from readFromStreamZipped size " + bufSize + " : ");
        String testResult = "This is a test file only.This ";
        for (int j=0; j<bufSize; j++) {
          System.out.print((char)readShow[j]);
          assertTrue((char)readShow[j]==testResult.charAt(j));
        }
        System.out.println();
      }

      {
        String[] testString = {"More to come...", "\nLine 1", "\nLine 2", "\nLast line"};

        int base = 0;
        for (int j=0; j<testString.length; j++) {
          byte[] buffer = testString[j].getBytes();
          long fileSize = remote.writeToFile("localhost", "writeNormal.txt", buffer, j==testString.length-1);
          assertTrue(buffer.length==fileSize-base);
          base = (int)fileSize;
        }
      }

      {
        String[] testString = {"More to come...", "\nLine 1", "\nLine 2", "\nLast line"};
        int base = 0;
          ByteArrayOutputStream bOut = new ByteArrayOutputStream();
          GZIPOutputStream zOut=null;

        for (int j=0; j<testString.length; j++) {
          zOut = new GZIPOutputStream(bOut);
          zOut.write(testString[j].getBytes());
          zOut.close();
          long fileSize = remote.writeToFileZipped("localhost", "writeZipped.txt", bOut.toByteArray(), j==testString.length-1);
          bOut.reset();
          assertTrue(testString[j].getBytes().length==fileSize-base);
          base = (int)fileSize;
        }
        zOut.close();
        bOut.close();
      }

      {
        String[] testFiles = {"testZip1.txt", "testZip2.txt", "testZip3.txt", "testZip4.txt"};
        for (int j=1; j<=3; j++) {
          for (int k=0; k<block.getParts(); k++) {
            BlockFileDescriptor fd = block.getBlockFileDescriptor(k);
            fd.setFilePath("testZip"+(k+j)+".txt");
          }
          long fileSize = remote.writeToStreamZipped("localhost", testFiles, block, j==3);
          System.out.println("file size : "+fileSize);
        }

        //Check the 4 files. They should contain:
        //testZip1 : This is a test file only.
        //testZip2 : This This is a test file
        //testZip3 : only.This This is a test
        //testZip4 : file only.This
        String[] testFileContent = {"This is a test file only.",
                              "This This is a test file ",
                              "only.This This is a test ",
                              "file only.This "};
        for (int j=0; j<4; j++) {
          File testFile = remote.getFile("localhost", "testZip"+(j+1)+".txt");
          assertNotNull(resultFile);
          FileInputStream fTest = new FileInputStream(testFile);
          assertNotNull(fTest);
          int data=0;
          int t=0;
          do {
            data = fTest.read();
            if (data != -1)
              assertTrue((char)data == testFileContent[j].charAt(t));
            t++;
          } while (data != -1);
        }
      }

      result = remote.copyFile("localhost", "nosuchfile.txt", "somefile.txt", false);
      System.out.println("Copy no file : "+result);
      assertNull(result);

      result = remote.moveFolder("localhost", "nosuchfolder", "somefolder", false);
      System.out.println("Move no file : "+result);
      assertNull(result);


/*      IPdipTransfer transfer = remote.openTransfer("localhost", "renamedfile.txt", READ_WRITE);
      byte[] buffer = new byte[10];
      java.util.Arrays.fill(buffer, Byte.parseByte("0"));
      result="";
      for (int j=0; j<buffer.length; j++)
        result += (char)buffer[j];
      System.out.println("fresh buffer : " + result);

      for (int i=1; i<=16; i++) {
        int bufSize = transfer.read(buffer, i);
        System.out.println("Size of buffer read in : "+bufSize);
        if (bufSize > 0) {
          result = "";
          for (int j=0; j<bufSize; j++)
            result += (char)buffer[j];
            //Byte.toString(buffer[j]);
          System.out.println("Read from file : "+ String.valueOf(i) + " : " + result);
          if (bufSize < 5)
            break;
        } else {
          break;
        }

      }

      for (int i=1; i<=5; i++) {
        Byte fill = new Byte(String.valueOf(i));
        java.util.Arrays.fill(buffer, fill.byteValue());
        assertTrue(transfer.write(buffer));
      }
*/
/*      java.util.Arrays.fill(buffer, Byte.parseByte("0"));
      for (int i=1; i<=6; i++) {
        int bufSize = transfer.read(buffer, i);
        System.out.println("Size of buffer read in : "+bufSize);
        if (bufSize > 0) {
          result = "";
          for (int j=0; j<bufSize; j++)
            result += Byte.toString(buffer[j]);
          System.out.println("Read from file : "+ String.valueOf(i) + " : " + result);
          if (bufSize < 5)
            break;
        } else {
          break;
        }
      }
      remote.closeTransfer(transfer);
*/    }
    catch (Exception ex) {
      System.out.println(" Exception occurred : "+ex.getMessage());
      ex.printStackTrace();
    }
  }

  public void tearDown() {
    try {
      //remote.deleteFolder("localhost", "test2");
      //remote.deleteFile("localhost", "renamedfile.txt");
    }
    catch (Exception ex) {
      System.out.println(" Exception occurred : "+ex.getMessage());
      ex.printStackTrace();
    }
  }

  public void testLocalFilingMethods() {
    if (!Util.isLocal())
      return;
    String projDir = "D:\\ClearCase\\i00066_GridTalk_2.0\\PDIPFramework\\FrameWork\\";
    File file = new File("D:/development/FrameWork/data/filetest.txt");
    String result = null;
    try {
      result = remote.createFolder("localhost", "test");
      System.out.println("CreateFolder : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\test"));

      result = remote.createFile("localhost", "test/testfile.txt", file, false);
      System.out.println("CreateFile : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\test\\testfile.txt"));

      result = remote.copyFile("localhost", "test/testfile.txt", "test/copiedfile.txt",true);
      System.out.println("CopyFile : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\test\\copiedfile.txt"));

      result = remote.copyFolder("localhost", "test", "copiedtest", true);
      System.out.println("CopyFolder : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\copiedtest"));

      result = remote.moveFile("localhost", "test/copiedfile.txt", "movedfile.txt", true);
      System.out.println("MoveFile : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\movedfile.txt"));

      result = remote.moveFolder("localhost", "copiedtest", "movedtest", true);
      System.out.println("MoveFolder : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\movedtest"));

      result = remote.deleteFile("localhost", "movedtest/testfile.txt");
      System.out.println("deleteFile : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\movedtest\\testfile.txt"));

      result = remote.deleteFolder("localhost", "movedtest");
      System.out.println("DeleteFolder : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\movedtest"));

      result = remote.renameFolder("localhost", "test", "test2");
      System.out.println("RenameFolder : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\test2"));

      result = remote.renameFile("localhost", "movedfile.txt", "renamedfile.txt");
      System.out.println("RenameFile : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\renamedfile.txt"));

      result = remote.createFolder("localhost", "test1/test2/test3");
      System.out.println("Create multiple folders: "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\test1\\test2\\test3"));

      result = remote.createFile("localhost", "testA/testB/testC/testfile.txt", file, false);
      System.out.println("Create file in multiple folders: "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\testA\\testB\\testC\\testfile.txt"));

      result = remote.createFile("localhost", "test folder/test file.txt", file, false);
      System.out.println("Spaces in file name : "+result);
      assertTrue(result.equals(projDir+"work\\griddocs\\test folder\\test file.txt"));

      File resultFile = remote.getFile("localhost", "test folder/test file.txt");
      assertNotNull(resultFile);
      FileInputStream fShow = new FileInputStream(resultFile);
      assertNotNull(fShow);
      byte[] bShow = new byte[(int)resultFile.length()];
      int bufSize = 0;
      bufSize= fShow.read(bShow);
      System.out.print("Content of file obtained from getFile : ");
      for (int i=0; i<(int)resultFile.length(); i++)
        System.out.print((char)bShow[i]);
      System.out.println();

      { //test for raw data streams
        byte[] readShow = new byte[10];
        readShow = remote.readFromFile("localhost", "test folder/test file.txt", 2, 10);
        System.out.print("Content of file obtained from readFromFile : ");
        for (int j=0; j<10; j++) {
          System.out.print((char)readShow[j]);
        }
        System.out.println();
      }

      { //test for zipped data streams
        byte[] readShow = new byte[1000];
        byte[] readZipShow = null;
        readZipShow = remote.readFromFileZipped("localhost", "test folder/test file.txt", 5L, 10);
        ByteArrayInputStream in = new ByteArrayInputStream(readZipShow);
        GZIPInputStream zipShow = new GZIPInputStream(in);
        bufSize = zipShow.read(readShow);
        System.out.print("Content of file obtained from readFromFileZipped size "+ bufSize + " : ");
        for (int j=0; j<bufSize; j++) {
          System.out.print((char)readShow[j]);
        }
        System.out.println();
      }

      MultiFileBlock block;
      { //test for zipped data streams
        byte[] readShow = new byte[1000];
        byte[] readZipShow = null;

        String[] paths= {"test folder/test file.txt", "renamedfile.txt"};
        block = remote.readFromStreamZipped("localhost", paths, 0L, 30);
        readZipShow = block.getBlockData();
        for (int k=0; k<block.getParts(); k++) {
          BlockFileDescriptor fd =  block.getBlockFileDescriptor(k);
          System.out.println("File : "+fd.getFilePath());
          System.out.println("Size : " + fd.getFileSize());
        }
        ByteArrayInputStream in = new ByteArrayInputStream(readZipShow);
        GZIPInputStream zipShow = new GZIPInputStream(in);
        bufSize = zipShow.read(readShow);
        System.out.print("Content of file obtained from readFromStreamZipped size " + bufSize + " : ");
        String testResult = "This is a test file only.This ";
        for (int j=0; j<bufSize; j++) {
          System.out.print((char)readShow[j]);
          assertTrue((char)readShow[j]==testResult.charAt(j));
        }
        System.out.println();
      }

      {
        String[] testString = {"More to come...", "\nLine 1", "\nLine 2", "\nLast line"};

        int base = 0;
        for (int j=0; j<testString.length; j++) {
          byte[] buffer = testString[j].getBytes();
          long fileSize = remote.writeToFile("localhost", "writeNormal.txt", buffer, j==testString.length-1);
          assertTrue(buffer.length==fileSize-base);
          base = (int)fileSize;
        }
      }

      {
        String[] testString = {"More to come...", "\nLine 1", "\nLine 2", "\nLast line"};
        int base = 0;
          ByteArrayOutputStream bOut = new ByteArrayOutputStream();
          GZIPOutputStream zOut=null;

        for (int j=0; j<testString.length; j++) {
          zOut = new GZIPOutputStream(bOut);
          zOut.write(testString[j].getBytes());
          zOut.close();
          long fileSize = remote.writeToFileZipped("localhost", "writeZipped.txt", bOut.toByteArray(), j==testString.length-1);
          bOut.reset();
          assertTrue(testString[j].getBytes().length==fileSize-base);
          base = (int)fileSize;
        }
        zOut.close();
        bOut.close();
      }

      {
        String[] testFiles = {"testZip1.txt", "testZip2.txt", "testZip3.txt", "testZip4.txt"};
        for (int j=1; j<=3; j++) {
          for (int k=0; k<block.getParts(); k++) {
            BlockFileDescriptor fd = block.getBlockFileDescriptor(k);
            fd.setFilePath("testZip"+(k+j)+".txt");
          }
          long fileSize = remote.writeToStreamZipped("localhost", testFiles, block, j==3);
          System.out.println("file size : "+fileSize);
        }

        //Check the 4 files. They should contain:
        //testZip1 : This is a test file only.
        //testZip2 : This This is a test file
        //testZip3 : only.This This is a test
        //testZip4 : file only.This
        String[] testFileContent = {"This is a test file only.",
                              "This This is a test file ",
                              "only.This This is a test ",
                              "file only.This "};
        for (int j=0; j<4; j++) {
          File testFile = remote.getFile("localhost", "testZip"+(j+1)+".txt");
          assertNotNull(resultFile);
          FileInputStream fTest = new FileInputStream(testFile);
          assertNotNull(fTest);
          int data=0;
          int t=0;
          do {
            data = fTest.read();
            if (data != -1)
              assertTrue((char)data == testFileContent[j].charAt(t));
            t++;
          } while (data != -1);
        }
      }

      result = remote.copyFile("localhost", "nosuchfile.txt", "somefile.txt", false);
      System.out.println("Copy no file : "+result);
      assertNull(result);

      result = remote.moveFolder("localhost", "nosuchfolder", "somefolder", false);
      System.out.println("Move no file : "+result);
      assertNull(result);
    }
    catch (Exception ex) {
      System.out.println(" Exception occurred : "+ex.getMessage());
      ex.printStackTrace();
    }
  }

  public static Test suite() {
     return new TestSuite(TestFileMgrBean.class);
  }

  public static void main(String args[]){
   junit.textui.TestRunner.run (suite());
  }


}
