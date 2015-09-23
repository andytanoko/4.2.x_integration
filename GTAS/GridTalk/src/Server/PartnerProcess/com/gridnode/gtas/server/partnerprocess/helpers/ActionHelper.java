/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Koh Han Sing        Created
 * Nov 21 2002    Neo Sok Lay         Add conversion methods for ProcessMapping.
 * Jan 10 2003    Neo Sok Lay         Add conversion methods for BizCertMapping.
 */
package com.gridnode.gtas.server.partnerprocess.helpers;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.gtas.model.partner.PartnerEntityFieldID;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerHome;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerObj;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.IPartnerProcessManagerHome;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.IPartnerProcessManagerObj;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.gtas.model.partnerprocess.PartnerProcessEntityFieldID;

import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;

import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.model.Certificate;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.Map;

/**
 * This Action class provides common services used by the action classes.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ActionHelper
{
  /**
   * Obtain the EJBObject for the PartnerProcessManagerBean.
   *
   * @return The EJBObject to the PartnerProcessManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IPartnerProcessManagerObj getManager()
         throws ServiceLookupException
  {
    return (IPartnerProcessManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPartnerProcessManagerHome.class.getName(),
      IPartnerProcessManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the RNProcessDefManagerBean.
   *
   * @return The EJBObject to the RNProcessDefManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I7
   */
  public static IRNProcessDefManagerObj getProcessDefManager()
         throws ServiceLookupException
  {
    return (IRNProcessDefManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IRNProcessDefManagerHome.class.getName(),
      IRNProcessDefManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the CertificateManagerBean.
   *
   * @return The EJBObject to the CertificateManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I7
   */
  public static ICertificateManagerObj getCertManager()
         throws ServiceLookupException
  {
    return (ICertificateManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ICertificateManagerHome.class.getName(),
      ICertificateManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the PartnerManagerBean.
   *
   * @return The EJBObject to the PartnerManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I7
   */
  public static IPartnerManagerObj getPartnerManager()
         throws ServiceLookupException
  {
    return (IPartnerManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPartnerManagerHome.class.getName(),
      IPartnerManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the BizRegistryManagerBean.
   *
   * @return The EJBObject to the BizRegistryManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I7
   */
  public static IBizRegistryManagerObj getBizRegistryManager()
         throws ServiceLookupException
  {
    return (IBizRegistryManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IBizRegistryManagerHome.class.getName(),
      IBizRegistryManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the EnterpriseHierarchyManagerBean.
   *
   * @return The EJBObject to the EnterpriseHierarchyManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I7
   */
  public static IEnterpriseHierarchyManagerObj getEnterpriseManager()
         throws ServiceLookupException
  {
    return (IEnterpriseHierarchyManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IEnterpriseHierarchyManagerHome.class.getName(),
      IEnterpriseHierarchyManagerHome.class,
      new Object[0]);
  }

  /**
   * Convert an Trigger to Map object.
   *
   * @param trigger The Trigger to convert.
   * @return A Map object converted from the specified Trigger.
   *
   * @since 2.0
   */
  public static Map convertTriggerToMap(Trigger trigger)
  {
    return Trigger.convertToMap(
             trigger,
             PartnerProcessEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Trigger to Map objects.
   *
   * @param triggerList The collection of Trigger to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Triggers.
   *
   * @since 2.0
   */
  public static Collection convertTriggerToMapObjects(Collection triggerList)
  {
    return Trigger.convertEntitiesToMap(
             (Trigger[])triggerList.toArray(
             new Trigger[triggerList.size()]),
             PartnerProcessEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an ProcessMapping to Map object.
   *
   * @param mapping The ProcessMapping to convert.
   * @return A Map object converted from the specified ProcessMapping.
   *
   * @since 2.0 I7
   */
  public static Map convertProcessMappingToMap(ProcessMapping mapping)
  {
    return ProcessMapping.convertToMap(
             mapping,
             PartnerProcessEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of ProcessMapping to Map objects.
   *
   * @param mappingList The collection of ProcessMapping to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of ProcessMapping(s).
   *
   * @since 2.0 I7
   */
  public static Collection convertProcessMappingToMapObjects(Collection mappingList)
  {
    return ProcessMapping.convertEntitiesToMap(
             (ProcessMapping[])mappingList.toArray(
             new ProcessMapping[mappingList.size()]),
             PartnerProcessEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an BizCertMapping to Map object.
   *
   * @param mapping The BizCertMapping to convert.
   * @return A Map object converted from the specified BizCertMapping.
   *
   * @since 2.0 I7
   */
  public static Map convertBizCertMappingToMap(BizCertMapping mapping)
  {
    return BizCertMapping.convertToMap(
             mapping,
             PartnerProcessEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of BizCertMapping to Map objects.
   *
   * @param mappingList The collection of BizCertMapping to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of BizCertMapping(s).
   *
   * @since 2.0 I7
   */
  public static Collection convertBizCertMappingToMapObjects(Collection mappingList)
  {
    return BizCertMapping.convertEntitiesToMap(
             (BizCertMapping[])mappingList.toArray(
             new BizCertMapping[mappingList.size()]),
             PartnerProcessEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Partner to Map objects.
   *
   * @param partnerList The collection of Partner to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Partner(s).
   *
   * @since 2.0 I7
   */
  public static Collection convertPartnersToMapObjects(Collection partnerList)
  {
    return Partner.convertEntitiesToMap(
             (Partner[])partnerList.toArray(
             new Partner[partnerList.size()]),
             PartnerEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Certificate to Map objects.
   *
   * @param certList The collection of Certificate to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Certificate(s).
   *
   * @since 2.0 I7
   */
  public static Collection convertCertsToMapObjects(Collection certList)
  {
    return Certificate.convertEntitiesToMap(
             (Certificate[])certList.toArray(
             new Certificate[certList.size()]),
             PartnerProcessEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Get the Enterprise Id of the specified partner.
   *
   * @param partnerId Partner Id of the Partner
   * @return Enterprise Id taken from the Business Entity that the Partner
   * is attached to. This value may be empty if the Partner is a non-Gridtalk
   * partner.
   * @throws Exception Bad Partner Id.
   *
   * @since 2.0 I7
   */
  public static String getPartnerEnterpriseId(String partnerId)
    throws Exception
  {
    // retrieve Partner UID
    Long partnerUID = null;
    try
    {
      Partner partner = getPartnerManager().findPartnerByID(partnerId);

      if (partner != null)
        partnerUID = (Long)partner.getKey();
    }
    catch (Throwable t)
    {
      Logger.warn("[ActionHelper.getPartnerEnterpriseId] Error ", t);
    }

    if (partnerUID == null)
      throw new Exception("Bad Partner Id: "+partnerId);

    return getPartnerEnterpriseId(partnerUID);

  }

  /**
   * Get the Enterprise Id of the specified partner.
   *
   * @param partnerUID UID of the Partner.
   * @return Enterprise Id taken from the Business Entity that the Partner
   * is attached to. This value may be empty if the Partner is a non-Gridtalk
   * partner.
   * @exception Exception Partner not attached to any Business Entity.
   *
   * @since 2.0 I7
   */
  public static String getPartnerEnterpriseId(Long partnerUID) throws Exception
  {
    Long beUID = null;
    try
    {
      beUID = getEnterpriseManager().getBizEntityForPartner(partnerUID);
    }
    catch (Throwable t)
    {
      Logger.warn("[ActionHelper.getPartnerEnterpriseId] Error ", t);
    }

    if (beUID == null)
      throw new Exception("Partner not attached to any Business Entity: "+partnerUID );

    return getEnterpriseId(beUID);
  }

  /**
   * Get the Enterprise ID of the specified Business Entity.
   *
   * @param beUID UID of the Business Entity.
   * @return Enterprise Id of the Business Entity.
   * This value may be empty if the Business entity represents a
   * non-Gridtalk entity.
   * @exception Exception Bad Business Entity UID.
   *
   * @since 2.0 I7
   */
  public static String getEnterpriseId(Long beUID) throws Exception
  {
    String enterpriseId = null;
    try
    {
      BusinessEntity be = getBizRegistryManager().findBusinessEntity(beUID);
      enterpriseId = be.getEnterpriseId();
    }
    catch (Throwable t)
    {
      Logger.warn("[ActionHelper.getEnterpriseId] Error ", t);
      throw new Exception("Bad Business Entity UID: "+beUID);
    }

    if (enterpriseId == null)
      enterpriseId = "";
    return enterpriseId;
  }

  /**
   * Verify that a certificate belongs to a particular enterprise or the certificate
   * does not belong to any enterprise.
   *
   * @param certUID UID of the Certificate.
   * @param enterpriseId Enterprise Id
   * @param isPartner Also Check if the Certificate belongs to a Partner or not.
   * <b>true</b> to check that the certificate is a Partner certificate, <b>false</b>
   * to check that the certificate is this enterprise's own certificate.
   * @return The verified Certificate.
   * @exception Exception Certificate is Not Partner Certificate (isPartner=true)
   *  or Not Own Certificate (isPartner=false)
   * @throws Exception Certificate does not belong to the specified enterprise.
   * @throws Exception Bad Certificate UID.
   *
   * @since 2.0 I7
   */
  public static Certificate verifyCertificate(
    Long certUID, String enterpriseId, boolean isPartner) throws Exception
  {
    Certificate cert = verifyCertificate(certUID, enterpriseId);

    if (isPartner && !cert.isPartner())
      throw new Exception("Not Partner Certificate: "+certUID);
    if (!isPartner && cert.isPartner())
      throw new Exception("Not Own Certificate: "+certUID);

    return cert;
  }

  /**
   * Verify that a certificate belongs to a particular enterprise or the certificate
   * does not belong to any enterprise.
   *
   * @param certUID UID of the Certificate.
   * @param enterpriseId Enterprise Id
   * @return The verified Certificate.
   * @throws Exception Bad Certificate UID.
   * @throws Exception Certificate does not belong to the specified enterprise.
   *
   * @since 2.0 I7
   */
  public static Certificate verifyCertificate(
    Long certUID, String enterpriseId) throws Exception
  {
    Certificate cert = null;
    try
    {
      cert = getCertManager().findCertificateByUID(certUID);
    }
    catch (Exception t)
    {
      Logger.warn("[ActionHelper.verifyPartnerCertificate] Error ", t);
      throw new Exception("Bad Certificate UID: "+certUID);
    }

    if (cert.getID() != 0 &&
        !enterpriseId.equals(String.valueOf(cert.getID())))
      throw new Exception("Certificate "+certUID +
            " does not belong to enterprise "+enterpriseId);

    return cert;
  }

  public static Collection findCertificates(IDataFilter filter)
  {
    Collection results = null;
    try
    {
      results = getCertManager().getCertificate(filter);
    }
    catch (Exception ex)
    {
      Logger.warn("[ActionHelper.findCertificates] Error: ", ex);
    }

    return results;
  }

}