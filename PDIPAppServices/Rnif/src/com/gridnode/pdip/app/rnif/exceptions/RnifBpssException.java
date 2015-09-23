/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifBpssException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 30 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.rnif.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
 
public class RnifBpssException extends ApplicationException
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6848733633872915316L;

	public RnifBpssException(String type, String details)
  {
    super(type + ": " + details);
  }

  public RnifBpssException(String type, String details, Throwable t)
  {
    super(type + ": " + details, t);
  }

  static public RnifBpssException bpssDeployErr(String details, Throwable ex)
  {
    return new RnifBpssException(BPSS_DEPLOY_EX, details, ex);
  }

  static public RnifBpssException bpssUndeployErr(String details, Throwable ex)
  {
    return new RnifBpssException(BPSS_UNDEPLOY_EX, details, ex);
  }

  public final static String FILE_PROCESS_EX= "File Processing error";

  public final static String BPSS_DEPLOY_EX= "BPSS Deploy error";
  public final static String BPSS_UNDEPLOY_EX= "BPSS Undeploy error";
  public final static String BPSS_INVOKE_EX= "invoke BPSS error";

  public final static String PROFILE_CREATE_EX= "Create Profile error";
  public final static String PROFILE_NOT_FOUND_EX= "Profile not found error";
  public final static String PROFILE_UPDATE_EX= "Update Profile error";
  public final static String PROFILE_DELETE_EX= "Delete Profile error";

  public final static String PROCESSDEF_NOT_FOUND_EX= "ProcessDef not found error";

  public final static String ENTITY_NOT_FOUND_EX= "Entity not found error";
  public final static String DOCUMENT_SEND_EX= "Document Send error";

  public final static String NOF_GEN_EX= "Error in generating failure notification document!";

  public static RnifBpssException profileNotFoundEx(String details, Throwable ex)
  {
    return new RnifBpssException(PROFILE_NOT_FOUND_EX, details, ex);
  }

  public static RnifBpssException processdefNotFoundEx(String details, Throwable ex)
  {
    return new RnifBpssException(PROCESSDEF_NOT_FOUND_EX, details, ex);
  }

//  public static RnifBpssException profileCreateEx(String details, Throwable ex)
//  {
//    return new RnifBpssException(PROFILE_CREATE_EX, details, ex);
//  }
//  public static RnifBpssException profileUpdateEx(String details, Throwable ex)
//  {
//    return new RnifBpssException(PROFILE_UPDATE_EX, details, ex);
//  }


  public static RnifBpssException invokeBPSSErr(String details, Throwable ex)
  {
    return new RnifBpssException(BPSS_INVOKE_EX, details, ex);
  }

  public static RnifBpssException fileProcessErr(String details, Throwable ex)
  {
    return new RnifBpssException(FILE_PROCESS_EX, details, ex);

  }

//  public static RnifBpssException entityNotFoundEx(String details, Throwable ex)
//  {
//    return new RnifBpssException(ENTITY_NOT_FOUND_EX, details, ex);
//
//  }
//
//  public static RnifBpssException documentSendEx(String details, Throwable ex)
//  {
//    return new RnifBpssException(DOCUMENT_SEND_EX, details, ex);
//
//  }
//
//  public static RnifBpssException failureNotificationGenError(String details, Throwable ex)
//  {
//    return new RnifBpssException(NOF_GEN_EX, details, ex);
//
//  }
//
//  public static RnifBpssException failureNotificationGenError(String details)
//  {
//    return new RnifBpssException(NOF_GEN_EX, details);
//  }
//
//
//  public static RnifBpssException profileDeleteEx(String details, Throwable ex)
//  {
//    return new RnifBpssException(PROFILE_DELETE_EX, details, ex);
//  }

}
