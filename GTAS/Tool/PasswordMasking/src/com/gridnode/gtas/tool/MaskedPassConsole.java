/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MaskedPassConsole.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 16, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.tool;

import com.gridnode.pdip.framework.util.MaskedPass;

/**
 * @author Tam Wei Xiang
 * @since GT4.2.1
 */
public class MaskedPassConsole
{
  public static void main(String[] args)
  {
    if(args.length <=0)
    {
      System.out.println("Please enter the GridTalk account password.");
      return;
    }
    String password=MaskedPass.encode(args[0]);
    System.out.println("Masked Password: "+password);
  }
}
