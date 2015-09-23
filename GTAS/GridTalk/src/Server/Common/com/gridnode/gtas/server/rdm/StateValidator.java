/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StateValidator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 16 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.rdm;

import com.gridnode.gtas.exceptions.InvalidStateException;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;

/**
 * This StateValidator contains the utilities to validate the attributes
 * contained in a StateMachine.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class StateValidator implements java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2984437615859286531L;
	private StateMachine _sm;

  /**
   * Construct a StateValidator for validating attributes in the specified
   * StateMachine.
   */
  public StateValidator(StateMachine sm)
  {
    _sm = sm;
  }

  /**
   * Check a String type attribute in the StateMachine.
   *
   * @param attrKey The Key to obtain the String attribute.
   * @exception InvalidStateException Attribute value is null, or empty, or
   * is not of String type.
   * @since 2.0
   */
  public void checkStringAttr(String attrKey)
    throws InvalidStateException
  {
    Object attrObj = _sm.getAttribute(attrKey);
    System.out.println("SM."+attrKey +"="+attrObj);
    if (attrObj instanceof String)
    {
      String attrStr = (String)attrObj;
      if (attrStr == null || attrStr.length() == 0)
        throw new InvalidStateException("No "+attrKey+" associated");
    }
    else
      throw new InvalidStateException("Associated "+attrKey+" is invalid");
  }

  /**
   * Check a String type value for non-null and not empty, and sets this value
   * to the StateMachine.
   *
   * @param attrKey Key of the String attribute
   * @param attrValue The attribute value to check.
   * @exception InvalidStateException attrValue is null or empty.
   */
  public void checkStringAttrValue(String attrKey, String attrValue)
    throws InvalidStateException
  {
    if (attrValue == null || attrValue.length() == 0)
      throw new InvalidStateException("Attribute Value is empty");

    _sm.setAttribute(attrKey, attrValue);
  }
}