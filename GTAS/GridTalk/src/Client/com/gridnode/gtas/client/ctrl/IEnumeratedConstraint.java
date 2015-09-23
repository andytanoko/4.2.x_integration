/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEnumeratedConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 * 2003-09-15     Daniel D'Cotta      Added i18n support, currently only for 
 *                                    FakeEnumeratedConstraint 
 */
package com.gridnode.gtas.client.ctrl;

public interface IEnumeratedConstraint extends IConstraint
{
  public int getSize();
  public String getLabel(int i);
  public String getValue(int i);
  public String getLabel(String value);
  public String getValue(String label);
  public boolean getRequiresI18n();
}