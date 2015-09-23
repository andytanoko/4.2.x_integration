/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILogErrorCodes.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Mar 05 2007			Alain Ah Ming				Created
 * Mar 07 2007			Alan Ah Ming				Added error code into documentation
 */
package com.gridnode.gridtalk.genreport.exceptions;

/**
 * Error codes for GenReport
 * @author Alain Ah Ming
 * @since GT VAN 4.0
 * @version GT VAN 4.0
 *
 */
public interface ILogErrorCodes
{
	/**
	 * 005.002.: Error code prefix for GenReport module
	 */
	public static final String PREFIX = "005.002.";
	
	/**
	 * 005.002.001: Report handler error updating record
	 */
	public static final String REPORT_HANDLER_UPDATE = PREFIX+"001";

	/**
	 * 005.002.002: Report handler error creating record
	 */
	public static final String REPORT_HANDLER_CREATE = PREFIX+"002";
	
	/**
	 * 005.002.003: Schedule handler error creating record
	 */
	public static final String SCHEDULE_HANDLER_CREATE = PREFIX+"003";
	
	/**
	 * 005.002.004: Schedule handler error updating record
	 */
	public static final String SCHEDULE_HANDLER_UPDATE = PREFIX+"004";
	
	/**
	 * 005.002.005: Report Generator online generation error
	 */
	public static final String REPORT_GENERATOR_GENERATE_ONLINE = PREFIX+"005";

	/**
	 * 005.002.006: Report Generator email generation error
	 */
	public static final String REPORT_GENERATOR_GENERATE_EMAIL = PREFIX+"006";
	
	/**
	 * 005.002.007: Schedule Report Generator online generation error
	 */
	public static final String SCHEDULE_REPORT_GENERATOR_GENERATE_ONLINE = PREFIX + "007";
	
	/**
	 * 005.002.008: Schedule Report Generator email generation error
	 */
	public static final String SCHEDULE_REPORT_GENERATOR_GENERATE_EMAIL = PREFIX+"008";
	
	/**
	 * 005.002.009: Report manager online viewing error
	 */
	//public static final String REPORT_MANAGER_VIEW_ONLINE = PREFIX + "009";
	
	/**
	 * 005.002.010: Create Report Servlet Post error
	 */
	//public static final String CREATE_REPORT_SERVLET_POST = PREFIX + "010";
	
	/**
	 * 005.002.011: Create Report Servlet View Online error
	 */
	public static final String CREATE_REPORT_SERVLET_VIEW_ONLINE = PREFIX+"011";
	
	/**
	 * 005.002.012: Create Report Servlet Send Email error 
	 */
	public static final String CREATE_REPORT_SERVLET_SEND_EMAIL = PREFIX + "012";
	
	/**
	 * 005.002.013: Create Schedule Servlet Post error
	 */
	public static final String CREATE_SCHEDULE_SERVLET_POST = PREFIX + "013";
	
	/**
	 * 005.002.014: Generate Report Servlet Post error
	 */
	public static final String GENERATE_REPORT_SERVLET_POST = PREFIX + "014";
	
	/**
	 * 005.002.015: Update Schedule Servlet Post error
	 */
	public static final String UPDATE_SCHEDULE_SERVLET_POST = PREFIX + "015";

	/**
	 * 005.002.016: Generic Servlet Get error
	 */
	public static final String GENERIC_SERVLET_POST = PREFIX + "016";
	
	/**
	 * 005.002.017: View Report Servlet Post error
	 */
	public static final String VIEW_REPORT_SERVLET_POST = PREFIX + "017";
	
	/**
	 * 005.002.018: View Report Servlet Upload Report error
	 */
	//public static final String VIEW_REPORT_SERVLET_UPLOAD_REPORT = PREFIX + "018";
	
	/**
	 * 005.002.019: View Report Servlet View error
	 */
	//public static final String VIEW_REPORT_SERVLET_VIEW = PREFIX + "019";
	
	/**
	 * 005.002.020: Housekeep Report Servlet Post error
	 */
	public static final String HOUSEKEEP_REPORT_SERVLET_POST = PREFIX + "020";
  
  /**
   * 005.002.021: Report handler error getting JasperPrint
   */
  public static final String REPORT_HANDLER_GET_JASPERPRINT = PREFIX + "021";
  
  /**
   * 005.002.022: Report Manager error getting JRExporter
   */
  public static final String REPORT_MANAGER_GET_JREXPORTER = PREFIX + "022";
  
  /**
   * 005.002.023: View Report Servlet View Online error
   */
  public static final String VIEW_REPORT_SERVLET_VIEW_ONLINE = PREFIX + "023";
  
  /**
   * 005.002.024: Report Generator generation error
   */
  public static final String REPORT_GENERATOR_GENERATE = PREFIX + "024";
  
  /**
   * 005.002.025: Schedule Generator generation error
   */
  public static final String SCHEDULE_REPORT_GENERATOR_GENERATE = PREFIX + "025";
}
