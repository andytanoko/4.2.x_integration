package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final public class BackupZipFile
{
  public static final int OPEN_READ            = 1;
  public static final int OPEN_WRITE           = 2;
  public static final int STD_BUFFER_SIZE      = 512;
  public static final int MAX_ENTRY_SIZE       = 65534;

  private static final String FILE_SEPARATOR = System.getProperty("file.separator");

  private int openMode;
  private int entryCount = 0;
 
  private String fileName;
  private ZipFile zipFile;

  private ZipOutputStream zipOutput;

  /**
   * create a backup file
   * @param fileName  Name of the backup file
   */
  public BackupZipFile(String fileName)
  {
    this.fileName = fileName;
  }

  /**
   * get the backup filename
   * @return  the backup filename
   */
  public String getName()
  {
    return fileName;
  }

  public int getEntryCount()
  {
    return entryCount;
  }

  /**
   * open the backup file
   * @param mode  The file open mode(Read/Write)
   * @exception java.lang.Exception Error opening the file
   */
  public void open(int mode) throws Exception
  {
    openMode = mode;
    if (openMode == OPEN_READ)
    {
      zipFile = new ZipFile(fileName);
    }
    else if (openMode == OPEN_WRITE)
    {
      FileOutputStream fileOutput = new FileOutputStream(fileName);
      zipOutput = new ZipOutputStream(fileOutput);
//      zipOutput.setLevel(0);
    }
  }

  /**
   * close the backup file
   * @exception java.io.IOException Error closing the file
   */
  public void close() throws IOException
  {
    if (openMode == OPEN_READ)
      zipFile.close();
    else if (openMode == OPEN_WRITE)
      zipOutput.close();
  }

  /**
   * add a category to this zipped backup file
   * @param category  Name of the category
   * @exception java.lang.Exception Error adding the category
   */
  public void addCategory(String category) throws Exception
  {
    ZipEntry entry = new ZipEntry(category);
    zipOutput.putNextEntry(entry);
  }

  /**
   * add a file into the zipped backup file
   * @param delDir  The directory that should be omitted from the filename when adding
   * @param fileName  Name of the file to be added
   * @param category  Name of the category which the file belongs to, ended with "/"
   * @exception java.lang.Exception Error adding the file
   */
  public void addFile(File file2Add, String entryName, String category)
  throws Exception
  {
    entryCount++;
    
    //if(entryCount > MAX_ENTRY_SIZE)
    //  throw new Exception("Your selection has exceeded the backup quota!");

    ZipEntry entry;

    if (file2Add.isDirectory())
    {
      entryName = category + entryName+"/";
        entry = new ZipEntry(entryName);
      entry.setTime(file2Add.lastModified());
      zipOutput.putNextEntry(entry);

      File[] files = file2Add.listFiles();
      for (int i=0; i<files.length; i++)
        addFile(files[i],files[i].getName(),entryName);
    }
    else
    {
      entryName = category + entryName;
      
      entry = new ZipEntry(entryName);
      entry.setSize(file2Add.length());
      entry.setTime(file2Add.lastModified());
      zipOutput.putNextEntry(entry);

      FileInputStream fileInput = new FileInputStream(file2Add);
      byte[] byteArray = new byte[STD_BUFFER_SIZE];
      int byteRead;
      while((byteRead = fileInput.read(byteArray)) != -1)
      {
        zipOutput.write(byteArray,0,byteRead);
      }
      fileInput.close();
    }
  }

  /**
   * add all files under a directory into the zipped backup file
   * @param dir  Name of the directory
   * @param category  Name of the category which all files belong to
   * @exception java.lang.Exception  Error adding those files
   */
  public void addFiles(String dir, String category) throws Exception
  {
    File f = new File(dir);

    File[] files = f.listFiles();
    for (int i=0; i<files.length; i++)
      addFile(files[i], files[i].getName(), category);
   }

  /**
   * if the backup file contains a subitem
   * @param name  Name of the subitem
   * @return  true if contains, otherwise false
   */
  public boolean containZipItem(String name)
  {
    if (zipFile.getEntry(name) == null)
      return false;
    else
      return true;
  }

  /**
   * unzip a subitem in this backup file to a phisical file
   * @param entry  The subitem in this zipped backup file
   * @param fullFilePath  The fullfilename which the subitem would be unzipped to
   * @exception java.lang.Exception  Error unzipping the file
   */
  private void unZipFile(ZipEntry entry, String fullFilePath) throws Exception
  {
    File f = new File(fullFilePath);
    if (f.getParent() != null)
    LocalFileUtil.makeTempDir(f.getParent());
    FileOutputStream fileOutput = new FileOutputStream(f);
    InputStream entryInput = zipFile.getInputStream(entry);

    byte[] byteArray = new byte[STD_BUFFER_SIZE];
    int byteRead;
    while((byteRead = entryInput.read(byteArray,0,STD_BUFFER_SIZE)) != -1)
      fileOutput.write(byteArray,0,byteRead);
    entryInput.close();
    fileOutput.close();
    f.setLastModified(entry.getTime());
  }

  /**
   * unzip all files which belong to some categories
   * @param selItem  A hashtable contains some categories and their unzip directory
   * @return An ArrayList contains filenames of those unzipped files
   * @exception java.lang.Exception  Error unzipping those files
   */
  public ArrayList unZipFiles(Hashtable selItem) throws Exception
  {
    ArrayList filenames = new ArrayList();
    ZipEntry entry;
    Enumeration allEntry = zipFile.entries();

    int index;
    String entryName,entryCategory,categoryPath,entryPath;

    while (allEntry.hasMoreElements())
    {
      entry = (ZipEntry)allEntry.nextElement();
      entryName = entry.getName().replace('/',FILE_SEPARATOR.charAt(0));

      //if this entry is a category
      if ((index = entryName.indexOf(FILE_SEPARATOR)) == -1)
        continue;

      //if this entry is not selected
      entryCategory = entryName.substring(0,index);
      entryPath = entryName.substring(index);
      if ((categoryPath = (String)selItem.get(entryCategory)) == null)
        continue;

      LocalFileUtil.makeTempDir(categoryPath);
      if (entry.isDirectory())
        LocalFileUtil.makeTempDir(categoryPath + entryPath);
      else
        unZipFile(entry,categoryPath + entryPath);

      //add that filename to ArrayList
      filenames.add(categoryPath + entryPath);
    }

    return filenames;
  }

  /**
   * unzip all files which belong to the category
   * @param category  Name of the category
   * @param dir  The directory that files would be unzipped into
   * @return An ArrayList contains filenames of those unzipped files
   * @exception java.lang.Exception  Error unzipping the files
   */
  public ArrayList unZipFiles(String category, String dir) throws Exception
  {
    ArrayList filenames = new ArrayList();
    ZipEntry entry;
    Enumeration allEntry = zipFile.entries();

    int index;
    String entryName;

    while (allEntry.hasMoreElements())
    {
      entry = (ZipEntry)allEntry.nextElement();
      entryName = entry.getName().replace('/',FILE_SEPARATOR.charAt(0));

      //if this entry is a category
      if ((index = entryName.indexOf(FILE_SEPARATOR)) == -1)
        continue;

      //if this entry does not belong to the category
      if (!entryName.substring(0,index).equals(category))
        continue;

      LocalFileUtil.makeTempDir(dir);
      if (entry.isDirectory())
      LocalFileUtil.makeTempDir(dir + entryName.substring(index));
      else
      {
        unZipFile(entry,dir + entryName.substring(index));
        filenames.add(dir + entryName.substring(index));
      }
    }

    return filenames;
  }
}
