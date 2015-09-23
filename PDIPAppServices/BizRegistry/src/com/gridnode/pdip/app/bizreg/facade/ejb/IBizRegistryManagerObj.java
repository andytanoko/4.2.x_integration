/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBizRegistryManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 * Sep 30 2002    Neo Sok Lay         Add method: findBusinessEntityKey().
 * Sep 03 2003    Neo Sok Lay         Extends from IBizRegManager.
 * Oct 17 2005    Neo Sok Lay         Temp remove implement IBizRegistryManager
 *                                    until find a better soln for interface
 *                                    checking during compilation.
 * Mar 01 2006    Neo Sok Lay         Re-extend from IBizRegistryManager                          
 */
package com.gridnode.pdip.app.bizreg.facade.ejb;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager;

/**
 * LocalObject for BizRegistryManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0
 * @since 2.0 I4
 */
public interface IBizRegistryManagerObj
  extends        EJBObject, IBizRegistryManager
{
}