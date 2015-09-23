package com.gridnode.pdip.framework.file.access;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class BlockFileDescriptor implements java.io.Serializable {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8048064100522207150L;
	private String _fileDomain;
  private String _filePath;
  private long _fileSize;

  public BlockFileDescriptor() {
    _fileDomain=null;
    _filePath=null;
    _fileSize=0L;
  }

  public BlockFileDescriptor(String domain, String path, long size) {
    _fileDomain=domain;
    _filePath=path;
    _fileSize=size;
  }

  public String getFileDomain() {
    return _fileDomain;
  }

  public String getFilePath() {
    return _filePath;
  }

  public long getFileSize() {
    return _fileSize;
  }

  public void setFileSize(long size) {
    _fileSize=size;
  }

  public void setFilePath(String path) {
    _filePath=path;
  }

  public void setFileDomain(String domain) {
    _fileDomain=domain;
  }
}