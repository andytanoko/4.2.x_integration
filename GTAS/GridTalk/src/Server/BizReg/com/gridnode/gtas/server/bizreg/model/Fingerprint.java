/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Fingerprint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 15 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         Add Taxonomy.
 * Sep 30 2003    Neo Sok Lay         Add isTransportProtocol field.
 * Oct 10 2003    Neo Sok Lay         Add isZipPayload field.
 */
package com.gridnode.gtas.server.bizreg.model;

/**
 * A Fingerprint represents a technical fingerprint adhering
 * to a certain technical specification. A Fingerprint
 * represents a protocol.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class Fingerprint
{
  private String _key;
  private String _type;
  private String _value;
  private boolean _isTransportProtocol;
  private boolean _isZipPayload;
  private String _overviewUrl;
  private String _overviewDescription;
  private Taxonomy[] _taxonomies;
  
  /**
   * Constructs a Fingerprint.
   */
  public Fingerprint()
  {
  }

  /**
   * Gets the Overview description of the Fingerprint.
   * 
   * @return Overview description
   */
  public String getOverviewDescription()
  {
    return _overviewDescription;
  }

  /**
   * Gets the Overview Url of the Fingerprint.
   * 
   * @return overview url
   */
  public String getOverviewUrl()
  {
    return _overviewUrl;
  }

  /**
   * Gets the Type code of the Fingerprint. This identifies
   * the Fingerprint.
   * 
   * @return type code for the fingerprint. 
   */
  public String getType()
  {
    return _type;
  }

  /**
   * Gets the Value of the Fingerprint.
   * 
   * @return value of the fingerprint.
   */
  public String getValue()
  {
    return _value;
  }

  /**
   * Sets the overview description for this Fingerprint.
   * 
   * @param overviewDesc Overview description to set.
   */
  public void setOverviewDescription(String overviewDesc)
  {
    _overviewDescription = overviewDesc;
  }

  /**
   * Sets the overview Url for this Fingerprint.
   * 
   * @param overviewUrl Overview url to set.
   */
  public void setOverviewUrl(String overviewUrl)
  {
    _overviewUrl = overviewUrl;
  }

  /**
   * Sets the type code for this Fingerprint.
   * 
   * @param type Type code to set.
   */
  public void setType(String type)
  {
    _type = type;
  }

  /**
   * Sets the value for this Fingerprint.
   * 
   * @param value Value to set.
   */
  public void setValue(String value)
  {
    _value = value;
  }

  /**
   * Gets the universally unique identifier for this Fingerprint in the registry.
   * 
   * @return Key of this Fingerprint. This field is not persistent. Default is null.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Sets the universally unique identifier for this Fingerprint in
   * the registry.
   *  
   * @param key The Key to set.
   */
  public void setKey(String key)
  {
    _key = key;
  }

  public static String getObjectType()
  {
    return "Fingerprint";
  }
  
  /**
   * Gets the Taxonomies for this Fingerprint.
   * @return The Taxonomy(s) of this Fingerprint. 
   */
  public Taxonomy[] getTaxonomies()
  {
    return _taxonomies;
  }

  /**
   * Sets the Taxonomies for this Fingerprint.
   * @param taxonomy The Taxonomy(s) to set.
   */
  public void setTaxonomies(Taxonomy[] taxonomies)
  {
    _taxonomies = taxonomies;
  }

  
  /**
   * Gets the isTransportProtocol value for this Fingerprint.
   * @return <b>true</b> if the Fingerprint is a transport protocol, <b>false</b> otherwise.
   */
  public boolean isTransportProtocol()
  {
    return _isTransportProtocol;
  }

  /**
   * Sets the isTransportProtocol value for this Fingerprint.
   * @param isTransportProtocol <b>true</b> to indicate this this Fingerprint is a transport protocol,
   * <b>false</b> otherwise.
   */
  public void setTransportProtocol(boolean isTransportProtocol)
  {
    _isTransportProtocol = isTransportProtocol;
  }

  /**
   * Gets the isZipPayload value for this Fingerprint. Applicable if isTransportProtocol is <b>true</b>.
   * @return <b>true</b> if the Fingerprint assumes a single payload that is zipped, <b>false</b> otherwise.
   */
  public boolean isZipPayload()
  {
    return _isZipPayload;
  }

  /**
   * Sets the isZipPayload value for this Fingerprint. Applicable if isTransportProtocol is <b>true</b>.
   * @param isZipPayload <b>true</b> to indicate that this Fingerprint assumes a single payload that 
   * is zipped, <b>false</b> otherwise.
   */
  public void setZipPayload(boolean isZipPayload)
  {
    _isZipPayload = isZipPayload;
  }

}
