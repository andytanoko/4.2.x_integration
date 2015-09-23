/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPublicRegistryManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Oct 17 2005    Neo Sok Lay         Temp remove implement IPublicRegistryManager
 *                                    until find a better soln for interface
 *                                    checking during compilation.
 * Dec 14 2005    Tam Wei Xiang       Extend back to IPublicRegistryManager                                   
 */
package com.gridnode.pdip.app.bizreg.facade.ejb;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager;

/**
 * EJB remote interface for PublicRegistryManagerBean
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IPublicRegistryManagerObj 
  extends EJBObject ,IPublicRegistryManager
{
  
}
