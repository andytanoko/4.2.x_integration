/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IOprationContextKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-11     Andrew Hill         Created
 * 2002-09-02     Andrew Hill         Removed some unused keys
 */
package com.gridnode.gtas.client.web;

/**
 * String keys for various objects that may get put into the operation context.
 * While the values for these keys appear to follow class naming conventions they are not always
 * named after existing classes. The nomenclature is like this to help avoid key collisions.
 */
public interface IOperationContextKeys
{
  public static final String ENTITY               = IRequestKeys.ENTITY;
  public static final String FORM_SUBMIT_URL      = "form.submit.url";
  public static final String FORM_EDIT_URL        = "form.edit.url";
}