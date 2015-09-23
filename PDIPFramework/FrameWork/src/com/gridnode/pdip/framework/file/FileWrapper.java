package com.gridnode.pdip.framework.file;

/**
 * <p>Title: PDIP</p>
 * <p>Description: Peer Data Interchange Platform</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode</p>
 * @author Jagadeesh
 * @version 1.0
 */

import java.io.*;
import java.util.*;


public class FileWrapper implements Serializable{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7360427576432179662L;
	private Object list;
  private String myString;
  public FileWrapper() {
  list = new ArrayList();
  myString = new String();
  }

  public void setArryList(Object lis){
  this.list = lis;
  }

  public Object getArrayList(){
  return list;
  }
  public void setStringValue(String inString){
  this.myString=inString;
  }
  public String getString(){
  return myString;
  }


}