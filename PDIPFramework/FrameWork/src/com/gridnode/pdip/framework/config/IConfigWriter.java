package com.gridnode.pdip.framework.config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

 import java.io.IOException;
 import java.util.Properties;
 /**
  *  This interface defines a Writer to write to the
  *  external data store.
  *
  *  This in further release will be deprecated since the File Services
  *  will be used for Reading/Writing to external data store.
  */

 public interface IConfigWriter
 {
    public void write(String p_path,Properties p_properties) throws IOException;
 }