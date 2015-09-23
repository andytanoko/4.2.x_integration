package com.gridnode.helpmaker;

import java.io.*;
import com.sun.java.help.search.Indexer;

public class SearchIndexer extends FileGenerator
{
  private static final String SWTICH_DM = "-db";
  private static final String SWTICH_CONFIG = "-c";
  private static final String SWTICH_LOG = "-logfile";

  private File _searchFolder;
  private File _contentFolder;
  private File _logFile;

  public SearchIndexer(File contentFolder)
  {
    this._contentFolder = contentFolder;
    _searchFolder = new File(contentFolder.getParent(), contentFolder.getName() + "Search" /*"JavaHelpSearch"*/);
    _logFile = new File(contentFolder.getParent(), contentFolder.getName() + "Search.log");
    generateConfig();
  }

  public void generate()
  {
    String[] args =
        {
        SWTICH_CONFIG, "Search.config",
        SWTICH_DM, _searchFolder.getAbsolutePath(),
        SWTICH_LOG, _logFile.getAbsolutePath(),
        _contentFolder.getAbsolutePath(),
        };

    Logger.write("Generating search index to [" + _searchFolder.getAbsolutePath() + "]");

    Indexer.main(args);
  }

  private void generateConfig()
  {

    try
    {
      createFile("Search.config");
      writeLine("IndexRemove " + _contentFolder.getParent() +File.separatorChar);
//      writeLine("IndexRemove " + _contentFolder.getParent().replace('\\', '/') +'/');
      close();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

}

