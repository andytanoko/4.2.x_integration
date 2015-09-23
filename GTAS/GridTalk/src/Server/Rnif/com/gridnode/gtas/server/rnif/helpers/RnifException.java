package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class RnifException extends ApplicationException
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4070110840865790359L;

	public RnifException(String type, String details)
  {
    super(type + ": " + details); 
  }

  public RnifException(String type, String details, Throwable t)
  {
    super(type + ": " + details, t);
  }

  static public RnifException bpssDeployErr(String details, Throwable ex)
  {
    return new RnifException(BPSS_DEPLOY_EX, details, ex);
  }

  static public RnifException bpssUndeployErr(String details, Throwable ex)
  {
    return new RnifException(BPSS_UNDEPLOY_EX, details, ex);
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

  public final static String ACK_RECEIPT_EX = "Error during receiptAcknowledgement";

  public static RnifException profileNotFoundEx(String details, Throwable ex)
  {
    return new RnifException(PROFILE_NOT_FOUND_EX, details, ex);
  }

  public static RnifException processdefNotFoundEx(String details, Throwable ex)
  {
    return new RnifException(PROCESSDEF_NOT_FOUND_EX, details, ex);
  }

  public static RnifException profileCreateEx(String details, Throwable ex)
  {
    return new RnifException(PROFILE_CREATE_EX, details, ex);
  }
  public static RnifException profileUpdateEx(String details, Throwable ex)
  {
    return new RnifException(PROFILE_UPDATE_EX, details, ex);
  }


  public static RnifException invokeBPSSErr(String details, Throwable ex)
  {
    return new RnifException(BPSS_INVOKE_EX, details, ex);
  }

  public static RnifException fileProcessErr(String details, Throwable ex)
  {
    return new RnifException(FILE_PROCESS_EX, details, ex);

  }

  public static RnifException entityNotFoundEx(String details, Throwable ex)
  {
    return new RnifException(ENTITY_NOT_FOUND_EX, details, ex);

  }

  public static RnifException documentSendEx(String details, Throwable ex)
  {
    return new RnifException(DOCUMENT_SEND_EX, details, ex);

  }

  public static RnifException failureNotificationGenError(String details, Throwable ex)
  {
    return new RnifException(NOF_GEN_EX, details, ex);

  }

  public static RnifException failureNotificationGenError(String details)
  {
    return new RnifException(NOF_GEN_EX, details);
  }


  public static RnifException ackReceiptEx(String details, Throwable ex)
  {
    return new RnifException(ACK_RECEIPT_EX, details, ex);
  }

  public static RnifException ackReceiptEx(String details)
  {
    return new RnifException(ACK_RECEIPT_EX, details);
  }

  public static RnifException profileDeleteEx(String details, Throwable ex)
  {
    return new RnifException(PROFILE_DELETE_EX, details, ex);
  }

}
