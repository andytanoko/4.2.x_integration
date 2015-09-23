/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceLocator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 07 2007		Alain Ah Ming				Created
 * Jan 13 2008    Tam Wei Xiang       Added code FAILED_JMS_PROCESSED_ERROR
 */
package com.gridnode.pdip.framework.exceptions;

/**
 * This interface contains the framework error codes for logging purposes 
 * @author Alain Ah Ming
 * @since GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	/**
	 * 001.001.: Prefix that MUST be prepended to all codes.&nbsp;This must not be used.
	 */
	public static final String PREFIX = "001.001.";
	
	/**
	 * 001.001.001: Error code for initializing data handler
	 */
	public static final String DATA_HANDLER_INITIALIZE			= PREFIX+"001";
	
	/**
	 * 001.001.002: Error code for creating any entity
	 */
	public static final String ENTITY_CREATE 									= PREFIX+"002";
	
	/**
	 * 001.001.003: Error code for serializing an <code>Object</code> to 
	 * an XML file
	 */
	public static final String OBJECT_SERIALIZE_TO_XML_FILE 	= PREFIX+"003";

	/**
	 * 001.001.004: Error code for serializing an <code>Object</code> to 
	 * an XML string
	 */
	public static final String OBJECT_SERIALIZE_TO_XML_STRING 	= PREFIX+"004";

	/**
	 * 001.001.005: Error code for deserializing an <code>Object</code> from 
	 * an XML file
	 */
	public static final String OBJECT_DESERIALIZE_FROM_XML_FILE	= PREFIX+"005";
	
	/**
	 * 001.001.006: Error code for loading configuration
	 */
	public static final String CONFIGURATION_LOAD									= PREFIX+"006";

	/**
	 * 001.001.007: Error code for loading object mapping
	 */
	public static final String OBJECT_MAPPING_LOAD								= PREFIX+"007";
	
	/**
	 * 001.001.008: Error code for retrieving XML entity file
	 */
	public static final String XML_ENTITY_FILE_ACCESS												= PREFIX+"008";
	
	/**
	 * 001.001.009: Error code for unmarshalling an object
	 */
	public static final String OBJECT_UNMARSHALL									= PREFIX+"009";
	
	/**
	 * 001.001.010: Error code for reading entity metainfo
	 */
	public static final String ENTITY_META_INFO_READ							= PREFIX+"010";
	
	/**
	 * 001.001.011: Error code for retrieving descriptor file
	 */
	public static final String DESCRIPTOR_FILE_RETRIEVE						= PREFIX+"011";
	
	/**
	 * 001.001.012: Error code for connecting to JMS
	 */
	public static final String JMS_CONNECT												= PREFIX+"012";
	
	/**
	 * 001.001.013: Error code for removing field meta info
	 */
	public static final String FIELD_META_INFO_REMOVE							= PREFIX+"013";
	
	/**
	 * 001.001.014: Error code for creating field meta info
	 */
	public static final String FIELD_META_INFO_CREATE							= PREFIX+"014";
	
	/**
	 * 001.001.015: Error code for retrieving field meta info
	 */
	public static final String FIELD_META_INFO_RETRIEVE						= PREFIX+"015";
	
	/**
	 * 001.001.016: Error code for generic JNDI operations
	 */
	public static final String JNDI_GENERIC												= PREFIX+"016";
	
	/**
	 * 001.001.017: Error code for generic email operations
	 */
	public static final String EMAIL_GENERIC											= PREFIX+"017";
	
	/**
	 * 001.001.018: Error code for generic notification operations
	 */
	public static final String NOTIFICATION_GENERIC								= PREFIX+"018";
	
	/**
	 * 001.001.019: Error code for generic JMS operations
	 */
	public static final String JMS_GENERIC												= PREFIX+"019";
	
	/**
	 * 001.001.020: Error code for generic class operations
	 */
	public static final String CLASS_GENERIC											= PREFIX+"020";
	
	/**
	 * 001.001.021: Error code for generic time operations
	 */
	public static final String TIME_GENERIC												= PREFIX+"021";
	
	/**
	 * 001.001.022: Error code for generic XML operations
	 */
	public static final String XML_GENERIC												= PREFIX+"022";
	
	/**
	 * 001.001.023: Error code for deserializing an <code>Object</code> from 
	 * an XML string
	 */
	public static final String OBJECT_DESERIALIZE_FROM_XML_STRING	= PREFIX+"023";
	
	/**
	 * 001.001.024: Error code for initializing the entity event sender
	 */
	public static final String ENTITY_EVENT_SENDER_INIT = PREFIX +"024";
	
	/**
	 * 001.001.025: Error code for the entity event sender sending
	 */
	public static final String ENTITY_EVENT_SENDER_SEND = PREFIX +"025";
	
	/**
	 * 001.001.026: Error code for initialisation of initialisation notifier
	 */
	public static final String INITIALISATION_NOTIFIER_INIT = PREFIX + "026";
	
	/**
	 * 001.001.027: Error code for generic ServiceLookup operations
	 */
	public static final String SERVICE_LOOKUP_GENERIC = PREFIX + "027";
	
	/**
	 * 001.001.028: Error code for Reflection Utility initialization
	 */
	public static final String REFLECTION_UTIL_INIT = PREFIX + "028";
	
  /**
   * 001.001.028: Error code for failure in processing the failed JMS msg we cache
   */
  public static final String FAILED_JMS_PROCESSED_ERROR = PREFIX +"029";
}
