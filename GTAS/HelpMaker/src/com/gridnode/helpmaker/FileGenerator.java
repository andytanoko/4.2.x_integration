package com.gridnode.helpmaker;

import java.io.*;

public class FileGenerator
{
  BufferedWriter _writer = null;

  protected void createFile(String filename) throws Exception
  {
    _writer = new BufferedWriter(new FileWriter(new File(filename)));

  }

  protected void close() throws Exception
  {
    _writer.flush();
    _writer.close();
  }

  protected void writeLine(String line) throws Exception
  {
    _writer.write(line);
    _writer.newLine();
  }

  protected void writeLine() throws Exception
  {
    _writer.newLine();
  }

}
