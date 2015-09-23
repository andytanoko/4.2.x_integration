/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RDNAttributes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13 2002    Neo Sok Lay         Created
 * June 29 2009   Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */
package com.gridnode.pdip.base.certificate.rsa;

import javax.security.auth.x500.X500Principal;

/**
 * This class provides a wrapper for creating the Subject Name for a certificate.
 * The following Relative Distinguished Names are required for creating
 * the SubjectName:<p>
 * <PRE>
 * Country Name
 * Organization Name
 * Organization Unit Name
 * Common Name
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class RDNAttributes
{
//  private RDN _countryName,
//              _organizationName,
//              _orgUnitName,
//              _commonName;
  private X500Principal _subjectNameJava;

  private RDNAttributes()
  {
    
  }

  /**
   * Construct a RDNAttributes object. This will create a X500Name object for
   * the Subject Name for a Certificate.
   *
   * @param countryName Country Name for the Subject Name
   * @param organizationName Organization Name for the Subject Name
   * @param orgUnitName Organization Unit Name for the Subject Name
   * @param commonName Common Name for the Subject Name.
   */
  public RDNAttributes(
    String countryName,
    String organizationName,
    String orgUnitName,
    String commonName)
  {
    this();
    _subjectNameJava = new X500Principal("CN="+commonName+",OU="+orgUnitName+",O="+organizationName+",C="+countryName);
  }

  /**
   * Get the Relative Distinguished Name object for the Country Name.
   *
   * @param countryName The Country Name value
   * @return RDN object for Country Name.
   * @throws NameException Error in adding the Country Name to the RDN object.
   */
  /*
  protected RDN getCountryNameRDN(String countryName)
    throws NameException
  {
    RDN rdn = new RDN();

    rdn.addNameAVA(
      new AttributeValueAssertion(
      AttributeValueAssertion.COUNTRY_NAME,
      AttributeValueAssertion.COUNTRY_NAME_OID,
      ASN1.PRINT_STRING,
      countryName));

    return rdn;
  }*/

  /**
   * Get the Relative Distinguished Name object for the Organization Name.
   *
   * @param organizationName The Organization Name value
   * @return RDN object for Organization Name.
   * @throws NameException Error in adding the Organization Name to the RDN object.
   */
  /*
  protected RDN getOrganizationNameRDN(String organizationName)
    throws NameException
  {
    RDN rdn = new RDN();

    rdn.addNameAVA(
      new AttributeValueAssertion(
      AttributeValueAssertion.ORGANIZATION_NAME,
      AttributeValueAssertion.ORGANIZATION_NAME_OID,
      ASN1.PRINT_STRING,
      organizationName));

    return rdn;
  }*/

  /**
   * Get the Relative Distinguished Name object for the Organization Unit Name.
   *
   * @param orgUnitName The Organization Unit Name value
   * @return RDN object for Organization Unit Name.
   * @throws NameException Error in adding the Organization Unit Name to the RDN object.
   */
  /*
  protected RDN getOrgUnitNameRDN(String orgUnitName)
    throws NameException
  {
    RDN rdn = new RDN();

    rdn.addNameAVA(
      new AttributeValueAssertion(
      AttributeValueAssertion.ORGANIZATIONAL_UNIT_NAME,
      AttributeValueAssertion.ORGANIZATIONAL_UNIT_NAME_OID,
      ASN1.PRINT_STRING,
      orgUnitName));

    return rdn;
  }*/

  /**
   * Get the Relative Distinguished Name object for the Common Name.
   *   * @param commonName The Common Name value
   * @return RDN object for Common Name.
   * @throws NameException Error in adding the Common Name to the RDN object.
   */
  /*
  protected RDN getCommonNameRDN(String commonName)
    throws NameException
  {
    RDN rdn = new RDN();

    rdn.addNameAVA(
      new AttributeValueAssertion(
      AttributeValueAssertion.COMMON_NAME,
      AttributeValueAssertion.COMMON_NAME_OID,
      ASN1.PRINT_STRING,
      commonName));

    return rdn;
  }*/

  /**
   * Get the created Subject Name.
   * @return The X500Name object for Subject Name.
   */
  /*
  public X500Name getSubjectName()
  {
    return _subjectName;
  }*/
  
  /**
   * Get the created Subject Name.
   * @return The X500Principal object for Subject Name.
   */
  public X500Principal getSubjectNameJava()
  {
    return _subjectNameJava;
  }
  
  public static void main(String[] args) throws Exception
  {
    RDNAttributes attr = new RDNAttributes("countryName", "orgName", "orgUnit", "commonName");
    System.out.println(attr.getSubjectNameJava());
  }
}