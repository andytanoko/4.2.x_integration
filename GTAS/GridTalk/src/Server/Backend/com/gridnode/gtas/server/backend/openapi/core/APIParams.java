package com.gridnode.gtas.server.backend.openapi.core;

import com.gridnode.gtas.server.backend.openapi.net.APIComm;
import com.gridnode.pdip.framework.util.SystemUtil;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

public class APIParams implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7350704336124632999L;
	private Object[] paramArray;
  private transient File[] fileArray; // NOTE! Transient member variable.
  private static File tempDirRoot = new File("C:\\");

  private static final int BUFFER_SIZE = 32768;

  public APIParams(Object[] params)
  {
    this(params, null);
  }

  public APIParams(Object[] params, File[] files)
  {
    paramArray = params;
    fileArray = files;
  }

  public File[] getFileArray()
  {
    return fileArray;
  }

  public Object[] getParamArray()
  {
    return paramArray;
  }

  public void readFiles(APIComm comm) throws Exception
  {
    System.out.println("APIParams::readFiles");
    ObjectInputStream input = comm.getInput();
    FileOutputStream fileOutput;

    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesToWrite = 0;

    String command, filename;
    boolean hasNextFile = true;
    boolean hasNextPacket = true;

    File tempDir = makeTempDir();
    String tempDirPath = tempDir.getPath();

    ArrayList filesReceived = new ArrayList();
    System.out.println("Reading files" );

    if (tempDirPath.charAt(tempDirPath.length() - 1) != File.separatorChar)
      tempDirPath = tempDirPath + File.separatorChar;

    try
    {
    while (hasNextFile)
    {
      command = input.readObject().toString();
      System.out.println(command);
      if (command.equals("SEND"))
      {
        filename = input.readObject().toString();
        System.out.println(filename);
        fileOutput = new FileOutputStream(tempDirPath + filename);
        hasNextPacket = input.readBoolean();

        while (hasNextPacket)
        {
          bytesToWrite = input.readInt();
          input.readFully(buffer, 0, bytesToWrite);
          fileOutput.write(buffer, 0, bytesToWrite);
          fileOutput.flush();
          hasNextPacket = input.readBoolean();
        }

        fileOutput.close();
        filesReceived.add(new File(tempDirPath + filename));
      }
      else
        hasNextFile = false;
    }
    }
    catch(Exception ex)
    {
     ex.printStackTrace(System.out);
     System.out.println("Fail to read file");
     }

    fileArray = new File[filesReceived.size()];

    for (int i = 0; i < fileArray.length; i++)
      fileArray[i] = (File)(filesReceived.get(i));
  }

  public void writeFiles(APIComm comm) throws Exception
  {
    if (fileArray == null)
    {
      System.out.println("File array is null");
      return;
    }
    ObjectOutputStream output = comm.getOutput();
    FileInputStream fileInput;
    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead = 0;

    //output.writeInt(fileArray.length);
   // output.flush();

    for (int i = 0; i < fileArray.length; i++)
    {
      if (fileArray[i].exists())
      {
        output.writeObject(new String("SEND"));
        output.flush();
        output.writeObject(fileArray[i].getName());
        output.flush();

        fileInput = new FileInputStream(fileArray[i]);

        while ((bytesRead = fileInput.read(buffer)) > 0)
        {
          output.writeBoolean(true);
          output.flush();
          output.writeInt(bytesRead);
          output.flush();
          output.write(buffer, 0, bytesRead);
          output.flush();
        }

        output.writeBoolean(false);
        output.flush();
        fileInput.close();
      }
    }

    output.writeObject(new String("END"));
    output.flush();
  }

  public void deleteFiles()
  {
    if (fileArray != null)
    {
      for (int i = 0; i < fileArray.length; i++)
      {
        if (!fileArray[i].isDirectory())
        {
          fileArray[i].delete();
        }
      }
    }
  }

  private File makeTempDir() throws IOException
  {
    //TWX 20090926 #1034: place the temp file in GT default temp dir
    String currentDir = SystemUtil.getSysTempDir();
    File temp = new File(currentDir);
    if(!temp.isDirectory())
    {
      temp.mkdir();
    }
    return temp;
  }
}
