/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNDePackager.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 21 2003      Guo Jianyu              abstracted from the previous RNDePackager
 * Jan 29 2004      Neo Sok Lay             The suffix for File.createTempFile()
 *                                          should contain a "." to create an extension.
 * Apr 26 2004      Neo Sok Lay             GNDB00021906: Read element with namespace.
 * Mar 12 2007      Neo Sok Lay             Use UUID for unique filename.
 * Jul 08 2009      Tam Wei Xiang           #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */
package com.gridnode.pdip.base.rnif.helper;

import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.certificate.facade.ejb.*;

import com.gridnode.pdip.framework.util.*;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import com.gridnode.xml.adapters.GNNamespace;
import com.gridnode.xml.adapters.GNElement;

import java.io.File;
import java.io.Serializable;
import java.security.cert.X509Certificate;


/**
 * The abstract super class for all RNIF Unpackagers. Currently known subclasses
 * are RNDePackager_11 and RNDePackager_20, for RNIF1.1 and RNIF2.0 respectively.
 *
 * @author Guo Jianyu
 *
 * @version GT 2.1.21
 * @since 1.0
 */
abstract public class RNDePackager implements Serializable
{
  SecurityInfoFinder _infoFinder= null;
  protected GNNamespace namespace = null;

  public RNDePackager()
  {
  }

  public void setSecurityInfoFinder(SecurityInfoFinder infoFinder)
  {
    _infoFinder= infoFinder;
  }

  /** Unpacks the RBM from the values in the UDoc. Removes the headers
   * and writes the service content into the UDoc.
   *
   * @return The path to the unpacked UDoc.
   */
  public File[][] unpackDocument(File udoc, RNPackInfo packInfo) throws RosettaNetException
  {
    if (packInfo == null)
      packInfo= new RNPackInfo();

    String udocfilename= udoc.getName();

    File[] res= null;
    File createdauditfile = null;
    try
    {
      res= unpackFile(udoc, udocfilename, packInfo);
      File tmpSerializeFile= File.createTempFile("DePackInfo"+UUIDUtil.getRandomUUIDInStr(), ".xml");
      packInfo.serialize(tmpSerializeFile.getAbsolutePath());
      res[0]= tmpSerializeFile;
    }
    catch (SecurityServiceException ex)
    {
      Logger.warn(ex);
      throw RosettaNetException.unpMesgGenErr(ex.getMessage());
    }
    catch (Exception ex)
    {
      Logger.warn(ex);
      throw RosettaNetException.unpMesgGenErr("Cannot create DePackInfo File --" + ex.getMessage());
    }
    finally
    { //create the audit file after unpack the udoc file, so audit file can have a more meaningful name
      createdauditfile = createAuditFile(udoc, udocfilename);
    }

    return new File[][]{res,{createdauditfile}};
  }

  protected File createAuditFile(File udoc, String udocfilename) throws RosettaNetException
  {
    File createdauditfile = null;
    String auditfilename= null;
    try
    {
      auditfilename= XMLUtil.createFile(IRnifPathConfig.PATH_AUDIT, udocfilename, udoc);
      Logger.debug("create from [" + udoc.getAbsolutePath() + "] to [" + auditfilename + "]");
      createdauditfile = FileUtil.getFile(IRnifPathConfig.PATH_AUDIT,auditfilename);
    }
    catch (Exception ex)
    {
      Logger.warn(ex);
      throw RosettaNetException.fileProcessErr("Cannot move file to audit: " + ex.toString());
    }
    if (null == auditfilename || createdauditfile==null)
    {
      throw RosettaNetException.fileProcessErr("Cannot move file to audit");
    }
    return createdauditfile;
  }

  protected void checkElement(Object element, String elementName, String exStr)
    throws RosettaNetException
  {
    if (null == element)
      throw new RosettaNetException(exStr, "Cannot read " + elementName + " element.");
  }

  /**
   * Method to set the field in RNPackInfo based on the element value.
   */
  protected void updatePackInfoField(RNPackInfo doc, String fieldId, GNElement parent, String elementName)
    throws ValidationException
  {
    if (null == parent)
      throw new ValidationException("null." + elementName);
    GNElement child= getChild(parent, elementName);
    if (null != child)
    {
      doc.setFieldValue(fieldId, child.getText().trim());
    }
    else
    {
      throw new ValidationException(parent.getName() + "." + elementName);
    }
  }
  /**
   * Sets an optional RNPackInfo field, uses updateDocField, but
   * ignores the exceptions thrown.
   */
  protected void updatePackInfoOptionalField(
    RNPackInfo packInfo,
    String fieldId,
    GNElement parent,
    String elementName)
  {
    try
    {
      this.updatePackInfoField(packInfo, fieldId, parent, elementName);
    }
    catch (ValidationException ex)
    {
    }
  }

  class ValidationException extends Exception
  {
    ValidationException(String name)
    {
      super(name);
    }
  }

  abstract protected File[] unpackFile(File rnfile, String udocfilename, RNPackInfo packInfo) throws RosettaNetException, SecurityServiceException;

  public X509Certificate getOwnEncryptCert(RNCertInfo securityInfo)
  {
    if (securityInfo.get_ownEncryptCertificate() == null)
      return null;
    try
    {
      Logger.debug("[RNDePackager.getOwnEncryptCert] securityInfo.get_ownEncryptCertificate() ="
        + securityInfo.get_ownEncryptCertificate().getCertName());
      return getCertManager().getX509Certificate(securityInfo.get_ownEncryptCertificate());
    }
    catch(Exception e)
    {
      Logger.warn("Error getting own encrypt cert", e);
      return null;
    }
  }

  public X509Certificate getPartnerSignCert(RNCertInfo securityInfo)
  {
    if (securityInfo.get_partnerEncryptCertificate() == null)
      return null;
    try
    {
      return getCertManager().getX509Certificate(securityInfo.get_partnerEncryptCertificate());
    }
    catch(Exception e)
    {
      Logger.warn("Error getting partner sign cert", e);
      return null;
    }
  }

  public X509Certificate getPendingCert(Certificate cert)
  {
    if (cert == null)
      return null;
    try
    {
      return getCertManager().getPendingX509Cert(cert);
    }
    catch(Exception e)
    {
      Logger.warn("Error getting pending cert for " + cert.getCertName());
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

  protected GNElement getChild(GNElement parent, String childName)
  {
    return parent.getChild(childName, namespace);
  }
}
