package com.gridnode.gtas.server.dbarchive.helpers;


public interface IDBArchivePathConfig
{

  public static final String CONFIG_NAME="dbarchive";
  
  public static final String PATH_ARCHIVE = "dbarchive.path.archive";
  
  public static final String MAX_FILESIZE_KEY="dbarchive.maxfilesize";
  
  public static final String ARCHIVE_DEST="dbarchive.dest";
  
  public static final String TM_ARCHIVE_DELEGATE_DEST = "dbarchive.tm.delegate";
  
  public static final String MAX_ENTRY_IN_DB_IN_OP = "dbarchive.max.entry.within.in.operator";
}
