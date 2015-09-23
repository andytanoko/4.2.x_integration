package com.gridnode.pdip.base.appinterface.data;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface IAppConstants extends java.io.Serializable {
  public static final int SERVER_APP = 1;
  public static final int CLIENT_APP = 2;
  public static final int SOAP_PROC = 3;
  public static final int JAVA_PROC = 4;

  public static final int RUNNING_STATUS = 1;
  public static final int BLOCKED_STATUS = 2;
  public static final int CREATED_STATUS = 2;
  public static final int UNKNOWN_STATUS = 0;
}