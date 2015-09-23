/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJndiNames.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 22, 2007        Regina Zeng          Created
 */

package com.gridnode.gridtalk.genreport.util;

/**
 * @author Regina Zeng
 * This interface defines the JNDI names for EJBs in the GenReport component.
 */
public interface IJndiNames
{
  /**
   * JNDI name for GenReportHandlerBean
   */
  static final String GENREPORT_HANDLER = "java:comp/env/ejb/GenReportHandlerBean";
  /**
   * JNDI name for GenScheduleHandlerBean
   */
  static final String GENSCHEDULE_HANDLER = "java:comp/env/ejb/GenScheduleHandlerBean";
  /**
   * JNDI name for GenScheduleHandlerBean through ScheduleReportService 
   */
  static final String SCHEDULE_HANDLER = "GenScheduleHandlerBean";
  /**
   * JNDI name for GenReportHandlerBean through ScheduleReportService
   */
  static final String REPORT_HANDLER = "GenReportHandlerBean";  
}
