package com.gridnode.pdip.framework.exceptions.domain;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class WFXMLException extends Exception {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2141833714204446935L;
	public static String WF_INVALID_KEY="WF_INVALID_KEY";
  public static String WF_INVALID_CONTEXT_DATA="WF_INVALID_CONTEXT_DATA";
  public static String WF_OPERATION_FAILED="WF_OPERATION_FAILED";
  public static String WF_NO_AUTHORIZATION="WF_NO_AUTHORIZATION";
  public static String WF_NO_ACCESS_TO_RESOURCE="WF_NO_ACCESS_TO_RESOURCE";
  public static String WF_INVALID_STATE_TRANSITION="WF_INVALID_STATE_TRANSITION";
  public static String WF_INVALID_RESULT_DATA="WF_INVALID_RESULT_DATA";
  public static String WF_INVALID_OBSERVER_FOR_THAT_RESOURCE="WF_INVALID_OBSERVER_FOR_THAT_RESOURCE";

  String _type;

  public WFXMLException(){
  }

  public WFXMLException(String type){
    _type=type;
  }

  public String getType(){
    return _type;
  }

  public String toString() {
      String s = getClass().getName();
      return (_type != null) ? (s + ": " + _type) : s;
  }

}