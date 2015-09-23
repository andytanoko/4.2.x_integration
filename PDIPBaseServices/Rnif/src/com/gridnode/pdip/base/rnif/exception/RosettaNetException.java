package com.gridnode.pdip.base.rnif.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class RosettaNetException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8600054430627285144L;
	private String _type = null;
  private String _details = null;

  public RosettaNetException(String errStr,  String details)
  {
    super(errStr + ": " + details);
    _details = details;
  }

  public RosettaNetException(String type, String errStr,  String details)
  {
    super(errStr + ": " + details);
    _type = type;
    _details = details;
  }

  public RosettaNetException(String type, String errStr,  Throwable t)
  {
    super(errStr, t);
    _type = type;
    _details = "";
  }

  public RosettaNetException(String type, String errStr, String details, Throwable t)
  {
    super(errStr + ": " + details, t);
    _type = type;
    _details = details;
  }


  public String getExType()
  {
    return _type;
  }

  public String getDetails()
  {
    return _details;
  }

  public final static String PKG_MESG_GENERR = "Error during packaging";

  public final static String PRF_ACTN_GENERR =
    "Error during action performance";

  public final static String PRF_DICT_VALERR =
    "Error during action performance - validating content against dictionary";

  public final static String UNP_MESG_GENERR = "Error during unpackaging";

  public final static String UNP_MESG_SIGNERR =
    "Error during unpackaging - verifying the signature of the message";

  public final static String UNP_PRMB_READERR =
    "Error during unpackaging - reading the Preamble";

  public final static String UNP_PRMB_VALERR =
    "Error during unpackaging - validating the Preamble";

  public final static String UNP_DHDR_READERR =
    "Error during unpackaging - reading the Delivery Header";

  public final static String UNP_DHDR_VALERR =
    "Error during unpackaging - validating the Delivery Header";

  public final static String UNP_SHDR_READERR =
    "Error during unpackaging - reading the Service Header";

  public final static String UNP_SHDR_VALERR =
    "Error during unpackaging - validating the Service Header";

  public final static String UNP_SHDR_MNFSTERR =
    "Error during unpackaging - verifying manifest";

  public final static String UNP_MESG_SEQERR =
    "Error during unpackaging - validating the message sequence";

  public final static String UNP_MESG_RESPTYPERR =
    "Unexpected response type in the HTTP header";

  public final static String UNP_MESG_DCRYPTERR =
    "Error decrypting the message";

  public final static String UNP_SCON_READERR =
    "Error during unpackaging - reading the Service Content";

  public final static String UNP_SCON_VALERR =
    "Error during unpackaging - validating the Service Content";
  
  //TWX 13 NOV 2007 Error msg for the RNIF decompression in case failed.
  public final static String UNP_SCON_UNCERR = 
	"Error during uncompress";

  // Additional exceptions for RosettaNet messages
  public final static String PKG_PREAMBLE_GDOCERR =
    "Error during packaging - missing GridDocument values for Preamble";

  public final static String PKG_DHDR_GDOCERR =
    "Error during packaging - missing GridDocument values for Delivery Header";

  public final static String PKG_SHDR_GDOCERR =
    "Error during packaging - missing GridDocument values for Service Header";

  public final static String FN_TIMEOUT =
    "Failure Notification - Timeout and exceed maximum retry times";

  public final static String ACK_RECEIPT =
    "Error during receiptAcknowledgement";

  public final static String ACK_DIGESTDIFF_EX
        = "Message digest in Acknowledgement document is not correct!";

  /*** general error**/

  public final static String FILE_PROCESS_EX = "File processing error";

  public final static String ACK_DIGEST_DIFF_CODE = "ACK.DIGESTDIFF.EX";
  
  static public RosettaNetException pkgMesgGenErr(String details)
  {
    return new RosettaNetException("PKG.MESG.GENERR", PKG_MESG_GENERR, details);
  }

  static public RosettaNetException pkgMesgGenErr(Throwable t)
  {
    return new RosettaNetException("PKG.MESG.GENERR", PKG_MESG_GENERR, t);
  }

  static public RosettaNetException prfActnGenErr(String details)
  {
    return new RosettaNetException("PRF.ACTN.GENERR", PRF_ACTN_GENERR, details);
  }

  static public RosettaNetException prfDictValErr(String details)
  {
    return new RosettaNetException("PRF.DICT.VALERR", PRF_DICT_VALERR, details);
  }

  static public RosettaNetException unpMesgGenErr(String details)
  {
    return new RosettaNetException("UNP.MESG.GENERR", UNP_MESG_GENERR, details);
  }

  static public RosettaNetException unpMesgSignErr(String details)
  {
    return new RosettaNetException("UNP.MESG.SIGNERR", UNP_MESG_SIGNERR, details);
  }

  static public RosettaNetException unpPrmbReadErr(String details)
  {
    return new RosettaNetException("UNP.PRMB.READERR", UNP_PRMB_READERR, details);
  }

  static public RosettaNetException unpPrmbValErr(String details)
  {
    return new RosettaNetException("UNP.PRMB.VALERR", UNP_PRMB_VALERR, details);
  }

  static public RosettaNetException unpDhdrReadErr(String details)
  {
    return new RosettaNetException("UNP.DHDR.READERR", UNP_DHDR_READERR, details);
  }

  static public RosettaNetException unpDhdrValErr(String details)
  {
    return new RosettaNetException("UNP.DHDR.VALERR", UNP_DHDR_VALERR, details);
  }

  static public RosettaNetException unpShdrReadErr(String details)
  {
    return new RosettaNetException("UNP.SHDR.READERR", UNP_SHDR_READERR, details);
  }

  static public RosettaNetException unpShdrValErr(String details)
  {
    return new RosettaNetException("UNP.SHDR.VALERR", UNP_SHDR_VALERR, details);
  }

  static public RosettaNetException unpShdrMnftErr(String details)
  {
    return new RosettaNetException("UNP.SHDR.MNFSTERR", UNP_SHDR_MNFSTERR, details);
  }

  static public RosettaNetException unpMesgSeqErr(String details)
  {
    return new RosettaNetException("UNP.MESG.SEQERR", UNP_MESG_SEQERR, details);
  }

  static public RosettaNetException unpMesgRespErr(String details)
  {
    return new RosettaNetException("UNP.MESG.RESPTYPERR", UNP_MESG_RESPTYPERR, details);
  }

  static public RosettaNetException unpMesgDcryptErr(String details)
  {
    return new RosettaNetException("UNP.MESG.RESPTYPERR", UNP_MESG_DCRYPTERR, details);
  }

  static public RosettaNetException unpSconReadErr(String details)
  {
    return new RosettaNetException("UNP.SCON.READERR", UNP_SCON_READERR, details);
  }

  static public RosettaNetException unpSconValErr(String details)
  {
    return new RosettaNetException("UNP.SCON.VALERR", UNP_SCON_VALERR, details);
  }
  public static RosettaNetException unpSconValErr(String details, Throwable ex)
  {
    return new RosettaNetException("UNP.SCON.VALERR", UNP_SCON_VALERR, details, ex);

  }
  
  //TWX 13 NOV 2007 For the RNIF decompress if failed
  public static RosettaNetException unpSconUnCerr(String details)
  {
	return new RosettaNetException("UNP.SCON.UNCERR", UNP_SCON_UNCERR, details);
  }

  // Additional exceptions for RosettaNet messages
  static public RosettaNetException pkgPreambleGDocErr(String details)
  {
    return new RosettaNetException("PKG.PREAMBLE.GDOCERR", PKG_PREAMBLE_GDOCERR, details);
  }

  static public RosettaNetException pkgDhdrGDocErr(String details)
  {
    return new RosettaNetException("PKG.DHDR.GDOCERR", PKG_DHDR_GDOCERR, details);
  }

  static public RosettaNetException pkgShdrGDocErr(String details)
  {
    return new RosettaNetException("PKG.SHDR.GDOCERR", PKG_SHDR_GDOCERR, details);
  }

//  static public RosettaNetException fnTimeout(String details)
//  {
//    return new RosettaNetException(FN_TIMEOUT, details);
//  }
//
//  static public RosettaNetException ackReceipt(String details)
//  {
//    return new RosettaNetException(ACK_RECEIPT, details);
//  }

  static public RosettaNetException fileProcessErr(String details)
  {
    return new RosettaNetException("FILE.PROCESS.EX", FILE_PROCESS_EX, details);
  }

  static public RosettaNetException ackDigestDiffErr(String  details)
  {
    return new RosettaNetException(ACK_DIGEST_DIFF_CODE, ACK_DIGESTDIFF_EX, details);
  }

}