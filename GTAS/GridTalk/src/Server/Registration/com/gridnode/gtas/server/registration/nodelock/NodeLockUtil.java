/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseFileGenerator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26 2003    Koh Han Sing        Created
 * May 14 2003    Koh Han Sing        Disable nodelock licensing
 * May 12 2003    Koh Han Sing        Disable nodelock checking
 */
package com.gridnode.gtas.server.registration.nodelock;


public class NodeLockUtil
{ 

  public static boolean validateNode(String waxOsName, String waxOsVersion, String waxMachineName)
  {
    boolean valid = true;
// Disable nodelocking
/**
    try
    {
      String thisMachineOsName = System.getProperty("os.name");
      String thisMachineOsVersion = System.getProperty("os.version");
      String thisMachineName = InetAddress.getLocalHost().getHostName();

      //Logger.debug("[NodeLockUtil.validateNode] os.name = "+thisMachineOsName);
      //Logger.debug("[NodeLockUtil.validateNode] os.version = "+thisMachineOsVersion);
      //Logger.debug("[NodeLockUtil.validateNode] machine.name = "+thisMachineName);

      String osName = WaxEngine.waxOff(waxOsName, INodeLockConstant.KEY);
      String osVersion = WaxEngine.waxOff(waxOsVersion, INodeLockConstant.KEY);
      String machineName = WaxEngine.waxOff(waxMachineName, INodeLockConstant.KEY);

      //Logger.debug("[NodeLockUtil.validateNode] license os.name = "+osName);
      //Logger.debug("[NodeLockUtil.validateNode] license os.version = "+osVersion);
      //Logger.debug("[NodeLockUtil.validateNode] license machine.name = "+machineName);

      if ((!osName.equals(thisMachineOsName)) ||
          (!osVersion.equals(thisMachineOsVersion)) ||
          (!machineName.equals(thisMachineName)))
      {
        Logger.debug("[NodeLockUtil.validateNode] ProductRegistrationException");
        valid = false;
      }
    }
    catch (Exception ex)
    {
      Logger.err("[NodeLockUtil.validateNode] Exception", ex);
      valid = false;
    }
*/
    return valid;
  }

  public static String waxOn(String value)
  {
    return WaxEngine.waxOn(value, INodeLockConstant.KEY);
  }

  public static String waxOff(String value)
  {
    return WaxEngine.waxOff(value, INodeLockConstant.KEY);
  }

}