/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNPackager.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 21 2003      Guo Jianyu              abstracted from the previous RNPackager
 * Aug 01 2008		Wong Yee Wah			#38  invoke RNIFConfiguration in getOwnSignCert()
 * June 29 2009   Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */
package com.gridnode.pdip.base.rnif.helper;

import java.io.File;
import java.io.Serializable;
import java.security.cert.X509Certificate;

import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.base.certificate.helpers.RNIFConfiguration;
/**
 * The abstract super class for all RNIF packagers. Currently Known
 * subclasses are RNPackager_11 and RNPackager_20, for RNIF1.1 and RNIF2.0
 * respectively.
 *
 * @author Guo Jianyu
 *
 * @version 1.0
 * @since 1.0
 */
abstract public class RNPackager implements Serializable
{
  static final int UDOC_INDEX= 1;
  static final int ATTACH_INDEX= 2;
  SecurityInfoFinder _infoFinder= null;
  protected static Configuration config = ConfigurationManager.getInstance().getConfig(
      IRnifConfig.CONFIG_NAME);

  public RNPackager()
  {
  }

  public void setSecurityInfoFinder(SecurityInfoFinder infoFinder)
  {
    _infoFinder= infoFinder;
  }

  /**
   * Returns a unique id. This ID is generated from the current timestamp
   * with the addition of a three-digit key. The result should be a 20-digit
   * string.  The initial counter will be randomized based on the time of
   * day.
   *
   * The IDs may be recycled if the system time is moved back and the sequence
   * generated comes a full cycle (over 1000 times). This should be a rare
   * enough scenario, but not fully discounted.
   */
  private static int idcounter = new java.util.Date().hashCode();
  public static synchronized String getUniqueID()
  {
      String res = null;
      if (idcounter > 999) idcounter = 0;
      java.text.NumberFormat f = new java.text.DecimalFormat();
      f.setMaximumFractionDigits(0);
      f.setMinimumIntegerDigits(3);
      f.setMaximumIntegerDigits(3);

      java.util.Date msgsent = new java.util.Date();
      java.text.DateFormat df = new java.text.SimpleDateFormat( "yyyyMMddHHmmssSSS");
      df.setTimeZone( new java.util.SimpleTimeZone( 0, "GMT"));
      res = df.format( msgsent) + f.format( idcounter++);

      return res;
  }
  //This function shall implement the actual packaging for different versions of RNIF
  abstract public File[][] packDoc(RNPackInfo packInfo, File[] payloads) throws RosettaNetException;

  //This function returns the appropriate sign cert, especially during the transition
  //period when a new cert is about to take over the expiring current cert
  public X509Certificate getOwnSignCert(RNCertInfo securityInfo)
  {
    try
    { //WYW 01082008
      //long threshold = config.getLong(IRnifConfig.SIGN_CERT_TAKEOVER, 24) * 3600 * 1000; //default 24 hours
    	long threshold = RNIFConfiguration.getSignCertTakeOverPeriod(); //default 24 hours

      return getCertManager().getOwnSignCert(securityInfo.get_ownSignCertificate(), threshold);
    }
    catch (Exception e)
    {
      Logger.warn("Can't get own sign cert", e);
      return null;
    }

  }

  /**
   * This function returns the appropriate partner encrypt cert, especially during
   * the transition period when a new cert is about to take over the expiring current cert
   */
  public X509Certificate getPartnerEncryptCert(RNCertInfo securityInfo)
  {
    try
    {
      return getCertManager().getPartnerEncryptCert(
        securityInfo.get_partnerEncryptCertificate());
    }
    catch(Exception e)
    {
      Logger.warn("Can't get partner encrypt cert", e);
      return null;
    }
  }

  private ICertificateManagerObj getCertManager()
    throws ServiceLookupException
  {
    return (ICertificateManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               ICertificateManagerHome.class.getName(),
               ICertificateManagerHome.class,
               new Object[0]);
  }

}
