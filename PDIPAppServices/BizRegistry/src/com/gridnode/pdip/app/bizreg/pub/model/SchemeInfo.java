/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SchemeInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 08 2003    Neo Sok Lay         Add method:
 *                                    - getDunsSchemeInfo()
 * Sep 16 2003    Neo Sok Lay         Add method:
 *                                    - getUddiTypesSchemeInfo()
 */
package com.gridnode.pdip.app.bizreg.pub.model;

//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Set;

/**
 * A Scheme information object. This models a classification scheme that may
 * be used to classify other registry information objects.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class SchemeInfo extends AbstractRegistryInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3887235677387151098L;

// private Set _concepts = new HashSet();
  
  /**
   * Constructs a SchemeInfo instance.
   * 
   * @param name The name of the Scheme information object.
   */
  public SchemeInfo(String name)
  {
    setName(name);
  }
    
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getObjectType()
   */
  public int getObjectType()
  {
    return TYPE_SCHEME;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#isPublishSupported()
   */
  public boolean isPublishSupported()
  {
    return true;
  }
  
  /**
   * Add a child Concept to this Scheme.
   * 
   * @param conceptInfo The Concept information object.
   */
//  public void addConcept(ConceptInfo conceptInfo)
//  {
//    _concepts.add(conceptInfo);
//  }
  
  /**
   * Gets all child Concepts in this Scheme.
   * 
   * @return Set of ConceptInfo objects.
   */
//  public Set getConcepts()
//  {
//    return _concepts;
//  }
  
  /**
   * Sets the child Concepts in this Scheme.
   * 
   * @param conceptInfos Set of ConceptInfo objects to set.
   */
//  public void setConcepts(Set conceptInfos)
//  {
//    _concepts.clear();
//    if (conceptInfos != null)
//    {
//      for (Iterator i=conceptInfos.iterator(); i.hasNext(); )
//      {
//        Object obj = i.next();
//        if (obj instanceof ConceptInfo)
//          addConcept((ConceptInfo)obj);
//      }
//    }
//  }

  /**
   * Gets the global DUNS identification scheme information object.
   * This identifier type is provided by all UDDI registries.
   * 
   * @return A SchemeInfo object for the global DUNS identifier type.
   */
  public static final SchemeInfo getDunsSchemeInfo()
  {
    SchemeInfo schemeInfo = new SchemeInfo("D-U-N-S");
    schemeInfo.setKey("uuid:8609C81E-EE1F-4D5A-B202-3EB13AD01823");
    
    return schemeInfo;
  }
  
  /**
   * Gets the well-known uddi-org:types classification scheme information object.
   * 
   * @return A SchemeInfo object for the uddi-org:types classification scheme.
   */
  public static final SchemeInfo getUddiTypesSchemeInfo()
  {
    SchemeInfo schemeInfo = new SchemeInfo("uddi-org:types");
    schemeInfo.setKey("UUID:C1ACF26D-9672-4404-9D70-39B756E62AB4");
    
    return schemeInfo;
  }
}
