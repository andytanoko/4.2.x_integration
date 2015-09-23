/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ShellExecutableAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;


public class ShellExecutableAForm extends GTActionFormBase
{
  private String _arguments;

  public String getArguments()
  { return _arguments; }

  public void setArguments(String arguments)
  { _arguments=arguments; }
}