package com.gridnode.gtas.server.rnif.helpers.util;

import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class DocTestUtil implements IRnifTestConstants
{

  public static GridDocument createGridDocument(String uDocFileName) throws FileAccessException
  {
    GridDocument gdoc= new GridDocument();

    File udocFile= new File(uDocFileName);

    int index= uDocFileName.lastIndexOf('/');
    if (index < 0)
      index= uDocFileName.lastIndexOf('\\');
    String path= uDocFileName.substring(index + 1);

    String uDocPath;
    try
    {
      uDocPath= FileUtil.create(IDocumentPathConfig.PATH_UDOC, path, udocFile);
    }
    catch (FileAccessException ex)
    {
      Logger.err("Error in DocTestUtil.createGridDocument uDocFileName=" + uDocFileName, ex);
      throw ex;
    }
    gdoc.setUdocFilename(uDocPath);

    return gdoc;
  }

  public static GridDocument create3A4Request() throws FileAccessException
  {

    GridDocument gdoc= createGridDocument(UDOC_3A4_REQUEST);

    gdoc.setSenderBizEntityId(MY_BEID);
    gdoc.setSenderNodeId(new Long(MY_NODEID));

    gdoc.setRecipientBizEntityId(PARTNER_BE_3A4);
    gdoc.setRecipientPartnerId(PARTNER_KEY_3A4);

    return gdoc;
  }

  public static void import3A4Request() throws Exception
  {
    importBizDoc(UDOC_3A4_REQUEST, "3A4Request.xml", REQUEST_DOCTYPE_3A4);
  }
  public static void importBizDoc(String testudocFileName, String udocFileName, String docType) throws Exception
  {
    File uDoc= new File(testudocFileName);
    File newUdoc= new File("C:\\jboss\\bin\\gtas\\data\\temp\\admin\\in\\" + udocFileName);
    copyFile(uDoc, newUdoc);
    ImportDocumentActionUtil importUtil= new ImportDocumentActionUtil();
    importUtil.setDocumentType(docType);

    
    ArrayList fileList= new ArrayList();
    fileList.add(udocFileName);
    importUtil.setImportFiles(fileList);

    importUtil.importDoc();
  }

  public static void copyFile(File originalFile, File destFile) throws FileNotFoundException, IOException
  {
    //if (!destFile.exists())
    {
      FileInputStream reader= new FileInputStream(originalFile);
      FileOutputStream writer= new FileOutputStream(destFile);
      byte[] buffer= new byte[1024];
      while (true)
      {
        int len= reader.read(buffer);
        if (len >= 0)
          writer.write(buffer, 0, len);
        else
          break;
      }
      reader.close();
      writer.close();
    }
  }
 
  public static void import3A4Response() throws Exception
  {
    importBizDoc(UDOC_3A4_RESPONSE, "3A4Response.xml", RESPONSE_DOCTYPE_3A4);
  }

}
