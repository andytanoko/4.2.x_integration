/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SpecificationInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12, 2003        Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.model;

/**
 * A Specification information object. This models the linkage between a 
 * ServiceBinding and one of its technical specifications that describes how 
 * to use the service using the ServiceBinding. For example, a ServiceBinding 
 * may have a Specification information object that describes how to 
 * access the service using a technical specification in the form of a WSDL 
 * document or a CORBA IDL document. 
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class SpecificationInfo extends AbstractRegistryInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3174191619087419446L;
	private String _usageDescription;
  private String _usageParams;
  //For UDDI, this is Concept, but for ebXML may be different...till then
  private ConceptInfo _specifiedObject;
 
  /**
   * Constructs a SpecificationInfo instance.
   * 
   * @param name Name of the Specification information object.
   */ 
  public SpecificationInfo(String name)
  {
    setName(name);
  }
   
  /**
   * Returns the usage description.
   *  
   * @return The usage description.
   */
  public String getUsageDescription()
  {
    return _usageDescription;
  }

  /**
   * Returns the usage parameters.
   * @return The usage parameters.
   */
  public String getUsageParams()
  {
    return _usageParams;
  }

  /**
   * Sets the usage description.
   * @param usageDescription The usage description to set
   */
  public void setUsageDescription(String usageDescription)
  {
    this._usageDescription = usageDescription;
  }

  /**
   * Sets the usage parameters.
   * @param usageParams The usage parameters to set
   */
  public void setUsageParams(String usageParams)
  {
    this._usageParams = usageParams;
  }

  /**
   * Sets the Link information object for the overview document
   * of the technical specifications.
   * 
   * @param linkInfo Link information object to set.
   * @see ILinkInfo
   */
  public void setOverviewLink(String[] linkInfo)
  {
    clearLinks();
    addLink(linkInfo);
  }
 
  /**
   * Gets the Link information object for the overview document of
   * the technical specifications.
   * 
   * @return Link information object for the overview document.
   * @see ILinkInfo
   */
  public String[] getOverviewLink()
  {
    String[][] allLinks = getLinks();
    
    if (allLinks.length > 0)
      return allLinks[0];
    else
      return null;
  }
   
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getObjectType()
   */
  public int getObjectType()
  {
    return TYPE_SPECIFICATION;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#isPublishSupported()
   */
  public boolean isPublishSupported()
  {
    return false;
  }
  /**
   * Gets the registry object specified. For UDDI, this is a Concept.
   * 
   * @return The Concept information object.
   */
  public ConceptInfo getSpecifiedObject()
  {
    return _specifiedObject;
  }

  /**
   * Sets the registry object to be specified. For UDDI, this is a Concept.
   * @param info The Concept information object.
   */
  public void setSpecifiedObject(ConceptInfo info)
  {
    _specifiedObject = info;
  }

}
