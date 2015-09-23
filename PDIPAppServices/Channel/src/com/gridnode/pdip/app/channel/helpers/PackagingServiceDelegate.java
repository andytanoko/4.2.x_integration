/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 *
 * File: PackagingServiceDelegate.java
 *
 * **************************************************************************************
 * Date           Author            Changes
 * **************************************************************************************
 * Oct 30 2003    Ang Meng Hua      Created
 * Feb 18 2004    Jagadeesh         Modified: To use ICommonHeaders.PAYLOAD_TYPE, to indicate
 *                                  the payload type (ex: NONE,RNIF1,RNIF2).
 *
 */
package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.exceptions.PackagingException;
import com.gridnode.pdip.app.channel.model.IPackagingInfo;
import com.gridnode.pdip.base.packaging.facade.ejb.IPackagingServiceHome;
import com.gridnode.pdip.base.packaging.facade.ejb.IPackagingServiceObj;
import com.gridnode.pdip.base.packaging.helper.IPackagingConfig;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PackagingServiceDelegate
{
  private static final String CLASS_NAME = "PackagingServiceHandler";

  public PackagingServiceDelegate()
  {
  }

  public static Message pack(
    com.gridnode.pdip.app.channel.model.PackagingInfo packagingInfo,
    Message message)
    throws PackagingException
  {
    try
    {
      com.gridnode.pdip.base.packaging.helper.PackagingInfo basePackInfo =
        new com.gridnode.pdip.base.packaging.helper.PackagingInfo();

      String packageType = packagingInfo.getEnvelope();

      // set the package envelope type into common headers
      Map headers = message.getCommonHeaders();
      //headers.put(ICommonHeaders.PAYLOAD_TYPE, getPackagingConfiguration().getString(packageType));
      //Modified to include packageType.
      headers.put(ICommonHeaders.PAYLOAD_TYPE, packageType);
      message.setCommonHeaders(headers);

      // invoke base packaging service if an envelope type is specified,
      // otherwise just return the message
      if (!packageType.equals(IPackagingInfo.DEFAULT_ENVELOPE_TYPE))
      {
        basePackInfo.setEnvelopeType(packageType);
        message = getPackagingServiceFacade().pack(basePackInfo, message);
      }
      return message;
    }
    catch (Exception ex)
    {
      throw new PackagingException("Exception in packaging message: "+ex.getMessage(), ex);
    }
  }

  public static Message unpack(
    com.gridnode.pdip.app.channel.model.PackagingInfo packagingInfo,
    Message message)
    throws PackagingException
  {
    try
    {
      com.gridnode.pdip.base.packaging.helper.PackagingInfo basePackInfo =
        new com.gridnode.pdip.base.packaging.helper.PackagingInfo();

      String packageType = packagingInfo.getEnvelope();
      if (!packageType.equals(IPackagingInfo.DEFAULT_ENVELOPE_TYPE))
      {
        basePackInfo.setEnvelopeType(packageType);
        if (message != null)
          //In case if message is null do not invoke base packaging
          message = getPackagingServiceFacade().unPack(basePackInfo, message);
      }
      return message;
    }
    catch (Exception ex)
    {
      throw new PackagingException("[Exception in unpackaging message]", ex);
    }
  }

  private static IPackagingServiceObj getPackagingServiceFacade()
    throws ServiceLookupException
  {
    return (IPackagingServiceObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IPackagingServiceHome.class.getName(),
        IPackagingServiceHome.class,
        new Object[0]);
  }

  private static Configuration getPackagingConfiguration()
  {
    return ConfigurationManager.getInstance().getConfig(
      IPackagingConfig.CONFIG_NAME);
  }

  /**
   * This method returns the actual payload.type, given the packagedPayLoadType,
   * in other words, a mapping between payload.type and packagedPayLoadType exists
   * at Packaging Module.
   * Ex :
   *        PayLoad.Type      PackagedPayLoadType
   *       --------------------------------------
   *         RNIF1       =     ROSETTA_PACKAGE
   *         RNIF2       =     ROSETTA_PACKAGE
   *
   * This mapping is necessary, since RNIF and other packaging modules set Packaged-
   * PayLoadType as value for  given PayLoad type.
   *
   *
   * @param payloadType - PackagedPayLoadType value from header.
   * @return - PayLaodType mapped to PackagedPayLoadType.
   */
  public static String getPayLoadType(String payloadType)
  {
    Set pairEntries = getPackagingConfiguration().getProperties().entrySet();
    Iterator pairIterator = pairEntries.iterator();
    while (pairIterator.hasNext())
    {
      Map.Entry entry = (Map.Entry) pairIterator.next();
      if (((String) entry.getValue()).equals(payloadType))
        return (String) entry.getKey();
    }
    return null;
  }

  public static String getPackagedPayLoadType(String payLoad)
  {
    String packageType = getPackagingConfiguration().getString(payLoad);
    //Testing purpose
    System.out.println(getPackagingConfiguration().getProperties());
    ChannelLogger.debugLog(
      CLASS_NAME,
      "getPackagedPayLoadType()",
      getPackagingConfiguration().getProperties().toString());

    return (packageType == null ? payLoad : packageType);

  }

}
