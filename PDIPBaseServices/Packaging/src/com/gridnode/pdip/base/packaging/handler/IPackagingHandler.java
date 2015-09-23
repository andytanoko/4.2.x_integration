/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPackagingHandler
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 16-OCT-2002    Jagadeesh           Created.
 * 06-DEC-2002    Jagadeesh           Modified: Added getDefaultPackagedHeader and
 *                                    getDefaultUnPackagedHeader.
 * 25-Feb-2003    Jagadeesh           Modified: Moved getDefaultPackagingHeader and
 *                                    getDefaultUnPackagedHeader to AbstractPackagingHandler
 *
 */
package com.gridnode.pdip.base.packaging.handler;

import com.gridnode.pdip.base.packaging.exceptions.PackagingException;
import com.gridnode.pdip.base.packaging.helper.PackagingInfo;
import com.gridnode.pdip.framework.messaging.Message;

public interface IPackagingHandler
{
  public Message pack(PackagingInfo info, Message message)
    throws PackagingException;

  public Message unPack(PackagingInfo info, Message message)
    throws PackagingException;

  public static final int BUFFER = 2048;

}