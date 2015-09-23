/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDependencyChecker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 10 2003    Neo Sok Lay         Created
 * Dec 17 2003    Jagadeesh           Modified: Change to use ChannelServiceDelegate
 *                                    insted of ChannelUtil.
 */
package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * AppServices Channel module. <p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * SecurityInfo  - dependent on Certificate
 * ChannelInfo   - dependent on SecurityInfo
 * ChannelInfo   - dependent on CommInfo
 * ChannelInfo   - dependent on PackagingInfo
 * </PRE>
 *
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDependencyChecker
{

  /**
   * Constructor for AlertActionDependencyChecker.
   */
  public EntityDependencyChecker()
  {
  }

  /**
   * Checks whether there are dependent SecurityInfos on the specified Certificate.
   *
   * @param certUid The UID of the Certificate.
   * @return A Set of SecurityInfo entities that are dependent on the Certificate, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentSecurityInfosForCertificate(Long certUid)
  {
    Set dependents = null;
    try
    {
      dependents = getSecurityInfoListByCertificate(certUid);
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(
        "EntityDependencyChecker",
        "checkDependentSecurityInfosForCertificate",
        "Error",
        t);
    }

    return dependents;
  }

  /**
   * Checks whether there are dependent ChannelInfos on the specified CommInfo.
   *
   * @param commInfoUid The UID of the CommInfo.
   * @return A Set of ChannelInfo entities that are dependent on the CommInfo, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentChannelnfosForCommInfo(Long commInfoUid)
  {
    Set dependents = null;
    try
    {
      dependents = getChannelInfoList(ChannelInfo.TPT_COMM_INFO, commInfoUid);
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(
        "EntityDependencyChecker",
        "checkDependentChannelnfosForCommInfo",
        "Error",
        t);
    }

    return dependents;
  }

  /**
   * Checks whether there are dependent ChannelInfos on the specified SecurityInfo.
   *
   * @param secInfoUid The UID of the SecurityInfo.
   * @return A Set of ChannelInfo entities that are dependent on the SecurityInfo, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentChannelnfosForSecurityInfo(Long secInfoUid)
  {
    Set dependents = null;
    try
    {
      dependents = getChannelInfoList(ChannelInfo.SECURITY_PROFILE, secInfoUid);
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(
        "EntityDependencyChecker",
        "checkDependentChannelnfosForSecurityInfo",
        "Error",
        t);
    }

    return dependents;
  }

  /**
   * Checks whether there are dependent ChannelInfos on the specified PackagingInfo.
   *
   * @param pkgInfoUid The UID of the PackagingInfo.
   * @return A Set of ChannelInfo entities that are dependent on the PackagingInfo, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentChannelnfosForPackagingInfo(Long pkgInfoUid)
  {
    Set dependents = null;
    try
    {
      dependents =
        getChannelInfoList(ChannelInfo.PACKAGING_PROFILE, pkgInfoUid);
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(
        "EntityDependencyChecker",
        "checkDependentChannelnfosForPackagingInfo",
        "Error",
        t);
    }

    return dependents;
  }

  /**
   * Get the list of SecurityInfos that have the specified Certificate.
   *
   * @param certUid The UID of the Certificate.
   * @return A Set of SecurityInfo entities that are associated to the
   * specified Certificate.
   * @throws Throwable Error in retrieving the associations from ChannelManager.
   */
  private Set getSecurityInfoListByCertificate(Long certUid) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      SecurityInfo.ENCRYPTION_CERTIFICATE_ID,
      filter.getEqualOperator(),
      certUid,
      false);
    filter.addSingleFilter(
      filter.getOrConnector(),
      SecurityInfo.SIGNATURE_ENCRYPTION_CERTIFICATE_ID,
      filter.getEqualOperator(),
      certUid,
      false);

    Collection securityInfoList =
      ChannelServiceDelegate.getChannelServiceFacade().getSecurityInfo(filter);
    //ChannelUtil.getInstance().lookupChannelManager().getSecurityInfo(filter);

    Set set = new HashSet();
    if (securityInfoList != null)
    {
      set.addAll(securityInfoList);
    }

    return set;
  }

  /**
   * Get the list of ChannelInfos that have association with a specified entity Uid
   * on the specified field.
   *
   * @param fieldId FieldID of the field to check association.
   * @param fieldUid The UID of the entity to check against the field.
   * @return A Set of ChannelInfo entities that are associated to the
   * specified entity Uid on the specified field.
   * @throws Throwable Error in retrieving the associations from ChannelManager.
   */
  private Set getChannelInfoList(Number fieldId, Long fieldUid)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      fieldId,
      filter.getEqualOperator(),
      fieldUid,
      false);

    Collection channelInfoList =
      ChannelServiceDelegate.getChannelServiceFacade().getChannelInfo(filter);
    //ChannelUtil.getInstance().lookupChannelManager().getChannelInfo(filter);

    Set set = new HashSet();
    if (channelInfoList != null)
    {
      set.addAll(channelInfoList);
    }

    return set;
  }

}
