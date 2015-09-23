package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.File;

public class LocalFileUtil
{

  /**
   * Method makeTempDir.
   * @param dir
   */
  public static String makeTempDir(String dirName)
  {
    File dir = new File(dirName);
    if (!dir.exists())
      dir.mkdirs();
    return dir.getAbsolutePath();
  }

  /**
   * Method deleteTempDir.
   * @param TMP_DIR
   */
  public static void deleteTempDir(String dirName)
  {
    File dir = new File(dirName);
    if(dir.exists())
      recurDelete(dir);
  }

  private static void recurDelete(File dir)
  {
    if (dir.isFile())
    {
      dir.delete();
      return;
    }
    File[] files = dir.listFiles();
    if (files != null && files.length > 0)
    {
      for (int i = 0; i < files.length; i++)
        recurDelete(files[i]);
    }

    dir.delete();
  }

}
