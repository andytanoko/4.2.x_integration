/**
 *
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfo
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 29-May-2002    Jagadeesh           Created.
 * 20 July 2009   Tam Wei Xiang       #560: commented out RSA JSAFE/BSafe related impl,
 *                                          no other class is referencing it.
 */


package com.gridnode.pdip.base.security.helpers;


import java.io.*;
import java.util.Date;

public class VerifyCertificate{

// public X509Certificate certificate = null;
// public JSAFE_PublicKey publicKey = null;
 //private int renewalWarnPeriod = 10;
 //private int expiredWarnPeriod = 10;

 //FUNCTION SetInGridNode: get GridNode
  /* public void SetInGridNode(GridNode gn)
   {
   try
    {
     //String gridNodeID = gn.getInfoValue(GridNode.NODEID).toString(); //privatekey password
     IDBGridNodeController.controller.loadCertificate(gn);
     byte[] certificateData = (byte[]) gn.getInfoValue(GridNode.CERTIFICATE);
     X509Certificate cert = new X509Certificate (certificateData, 0, 0);
     SetInCertificateData(cert);
    }
   catch (Exception anyException)
    {
      Tools.logSecurity(this,"[VerifyCert]  Exception caughted during set GridNode." );
      Tools.logSecurity(this,"[VerifyCert]  " + anyException.toString() );
      anyException.printStackTrace (new PrintStream (System.err));
    }
   }
  */

 // FUNCTION SetInCertificate
// public void setInCertificateData(X509Certificate inCert)
//  {
//    this.certificate=inCert;
//    try {
//       this.publicKey = certificate.getSubjectPublicKey ("Java");
//    } catch (Exception anyException) {
//      anyException.printStackTrace ();
//      //return false;
//    }
//   // return true;
//  }

 // FUNCTION SetInCertificateFile
 // return true successful
 //        false read certificate fail
// public boolean setInCertificateFile(String certificateFileName)
// {
//   X509Certificate cert=null;
//   cert=GridCertUtilities.loadX509Certificate(certificateFileName);
//   if (cert==null)
//      return false;
//   else
//     {
//      setInCertificateData(cert);
//      return true;
//     }
// }
 // FUNCTION go(): verify the following
 //    return -1: issuerName != subjectName
 //    return -2: certificate signature failed
 //    return -3: certificate not in validity period
 //    return 0: successful
//  public int go ()
//  {
//
//    /* This program will verify a self signed certificate*/
//
//
//      /* As a quick sanity check before checking the actual signature,
//       * make sure that the issuer name is the same as the subject
//       * name.  For a self-signed certificate, the two names should be
//       * the same. */
//      X500Name issuerName = certificate.getIssuerName ();
//      X500Name subjectName = certificate.getSubjectName ();
//      if (!subjectName.equals (issuerName)) {
//         return (-1);
//      //  println ("WARNING:  Subject name is not the same as " +
//      //           "the issuer name.");
//      //} else {
//      //  println ("Names match.");
//      }
//
//      /* To verify the certificate, the public key from it must be
//       * retrieved.  Because this is a self-signed certificate, we
//       * assume that the verifying key is the key contained within. */
//      try {
//        //println ("Verifing certificate signature.");
//
//        /* Once the public key that can verify the certificate
//         * signature has been obtained, the certificate signature can
//         * be verified.  In this case, we'll simply call
//         * verifyCertificateSignature() and pass in the public key
//         * needed to verify.  If this method returns true, then we can
//         * be assured that the certificate was signed using the
//         * private key corresponding to the public key.  If it returns
//         * false, then the certificate is invalid, and should not be
//         * trusted. */
//        if (!certificate.verifyCertificateSignature
//            ("Java", publicKey, null)) {
//            return (-2);
//        //  println ("Certificate signature is valid!");
//        //} else {
//        //  println ("Certificate signature is not valid!");
//        }
//      } catch (CertificateException certException) {
//        //println ("Exception caught while verifying signature.");
//
//
//        certException.printStackTrace
//           (new PrintStream (System.err));
//            return (-2);
//      }
//      /* Check the validity dates on certificates.  If notcurrently valid,
//       just issue a warning for now. */
//      if (!certificate.checkValidityDate (new Date())) {   // !!!!  UTC time stamp !!!!
//         return(-3);
//      //  println ("WARNING:  Certificate is not currently valid.");
//      //}
//      //else {
//      //  println ("WARNING:  Certificate is currently valid.");
//      }
//    return(0);
//    //println ("VerifyCert sample program finished.");
//  }
}
