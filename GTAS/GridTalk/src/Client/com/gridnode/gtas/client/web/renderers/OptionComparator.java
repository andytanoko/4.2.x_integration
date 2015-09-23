/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OptionComparator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-06-25     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;
import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Wrapper round a java.util.Collator that provides the collator with the strings to compare by
 * examing the child text nodes of the option elements passed to the compare method.
 */
public class OptionComparator implements Comparator
{
  /**
   * Internal reference to the Comparator that does the actual work
   */
  private Collator _collator;
  
  /**
   * Constructor. The wrapped Collator will be instantiated using the supplied locale and will
   * be of type java.text.RuleBasedComparator
   * @param locale The locale on which the comparison rules are based
   */
  public OptionComparator(Locale locale)
  {
    _collator = RuleBasedCollator.getInstance(locale);
  }
  
  /**
   * Constructor. The wrapped Collator is passed as a parameter.
   * @param collator The collator to use for the string comparison. May not be null.
   */
  public OptionComparator(Collator collator)
  {
    if (collator == null)
      throw new NullPointerException("collator is null");
    setCollator(collator);
  }
  
  /**
   * Compare the two options and return a negative, zero, or positive value depending on thier
   * relative alphabetical order. The wrapped collator will perform the comparison logic while
   * the code in this method provides it the strings to do so.
   * Both parameters MUST be org.w3c.dom.Element instances and not null or execption will be thrown.
   * @param option1 An option element
   * @param option2 An option element
   */
  public int compare(Object option1, Object option2)
  {
    return _collator.compare(getOptionText((Element)option1), getOptionText((Element)option2));
  }
  
  /**
   * Retrieve the displayed text of the option by concatenating the value of its text and entity
   * reference child nodes.
   * @param element The option element whose displayed text will be returned
   * @return text
   */
  private String getOptionText(Element option)
  {
    if (option == null)
      throw new NullPointerException("option is null");
    if(option.hasChildNodes())
    {
      String text = null;
      Node child = option.getFirstChild();
      while(child != null)
      {
        if( (child.getNodeType() == Node.TEXT_NODE)
            || (child.getNodeType() == Node.ENTITY_REFERENCE_NODE) )
        { //Not anticipating the need to do concat in most cases so not using a stringbuffer explicitly
          text = text == null ? child.getNodeValue() : text + child.getNodeValue();
        }
        child = child.getNextSibling();
      }
      return text == null ? "" : text; //20030717AH
    }
    else
    {
      return "";
    }
  }
  
  /**
   * Get a reference to the wrapped collator
   * @return collator
   */
  public Collator getCollator()
  {
    return _collator;
  }

  /**
   * Set the wrapped collator. May not be null.
   * @param collator
   */
  public void setCollator(Collator collator)
  {
    if (collator == null)
      throw new NullPointerException("collator is null");
    _collator = collator;
  }

}
