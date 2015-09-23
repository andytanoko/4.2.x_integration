/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportType.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 16, 2007   Regina Zeng           Created
 * Apr 10, 2007   Regina Zeng           Added datasourceType
 */

package com.gridnode.gridtalk.genreport.model;

import java.io.Serializable;

import com.gridnode.util.db.AbstractPersistable;

/**
 * @author i00118
 * Persistent object for a report type property
 * @hibernate.class table="`rpt_report_type`" optimistic-lock = "version"
 * @hibernate.mapping schema = "GTVAN"
 * @hibernate.query name = "getTemplate"
 *   query = "from ReportType as rt where rt.reportType = :report_type"
 * @hibernate.query name = "getDatasourceType"
 *   query = "from ReportType as rt where rt.reportType = :report_type"
 */
public class ReportType extends AbstractPersistable implements Serializable
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 7456704387262797433L;
  private String _reportType;
  private String _template;
  private String _datasourceType;
	
  private ReportType()
  {
		
  }
	
  /**
   * @hibernate.property column="`report_type`" not-null="true"
   * @return Returns the report_type.
   */
  public String getReportType() 
  {
	return _reportType;
  }
	
  public void setReportType(String type) 
  {
    _reportType = type;
  }

  /**
   * @hibernate.property column="`template_name`"
   * @return
   */
  public String getTemplate()
  {
    return _template;
  }

  public void setTemplate(String _template)
  {
    this._template = _template;
  }

  /**
   * @hibernate.property column="`datasource_type`"
   * @return
   */
  public String getDatasourceType()
  {
    return _datasourceType;
  }

  public void setDatasourceType(String type)
  {
    _datasourceType = type;
  }
}
