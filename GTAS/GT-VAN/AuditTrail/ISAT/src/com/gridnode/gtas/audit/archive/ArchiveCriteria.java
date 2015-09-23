/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveCriteria.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 22, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive;

import java.util.Date;


/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveCriteria
{ 
  private Date _fromStartDate;
  private Date _toStartDate;
  private boolean _isArchiveOrphanRecord;
  private String _group;
  
  public ArchiveCriteria()
  {
  }

  public Date getFromStartDate()
  {
    return _fromStartDate;
  }

  public void setFromStartDate(Date startDate)
  {
    _fromStartDate = startDate;
  }

  public Date getToStartDate()
  {
    return _toStartDate;
  }

  public void setToStartDate(Date startDate)
  {
    _toStartDate = startDate;
  }

  public String getGroup()
  {
    return _group;
  }

  public void setGroup(String _group)
  {
    this._group = _group;
  }

  public boolean isArchiveOrphanRecord()
  {
    return _isArchiveOrphanRecord;
  }

  public void setArchiveOrphanRecord(boolean archiveOrphanRecord)
  {
    _isArchiveOrphanRecord = archiveOrphanRecord;
  }
  
  public String toString()
  {
    return ArchiveCriteria.class.getSimpleName()+"[fromStartDate: "+getFromStartDate()+" toStartDate: "+getToStartDate()+
           " isArchiveOrphanRecord: "+isArchiveOrphanRecord()+" group: "+getGroup()+"]";
  }
  
  /* The JOXWriter can't deserialize if this method is override And this instance is an instance variable of other obj.
  public String toString()
  {
    return "[ArchiveCriteria: fromStartDate: "+getFromStartDate().getTime()+" toStartDate: "+getToStartDate()+"]";
  }*/
  
  /*
  public static void main(String[] args) throws Exception
  { 
    String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    SimpleDateFormat format = new SimpleDateFormat(pattern);
    
    
    ArchiveCriteria criteria = new ArchiveCriteria();
    criteria.setFromStartDate(new Date());
    criteria.setToStartDate(new Date());
    
    String xml = XMLBeanUtil.beanToXml(criteria, ArchiveSummary.class.getSimpleName(), format);
    System.out.println(" XML is "+xml);
    
    criteria = (ArchiveCriteria)XMLBeanUtil.xmlToBean(xml, ArchiveCriteria.class);
    System.out.println(criteria);
    
    TraceEventInfo info = new TraceEventInfo();
    info.setEventOccurTime(new Date());
    info.setBizDocumentUID("biz doc id");
    info.setErrorReason("err reason");
    info.setEventName("Doc Received");
    info.setEventRemark("Remark");
    info.setGroupName("group name");
    info.setMessageID("OB-123");
    info.setTracingID("trace id");
    info.setStatus("status");
    info.setUID("UID");
    info.setVersion(0);
    
    IAuditTrailEntity ent = info;
    System.out.println("ent is "+ent.getClass());
    /*
    //String infoXML = XMLBeanUtil.encodeBean(info);
    String infoXML = XMLBeanUtil.beanToXml(info, TraceEventInfo.class.getSimpleName(), format);
    //String infoXML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><TraceEventInfo java-class=\"com.gridnode.gtas.audit.model.TraceEventInfo\"><UID>4b08e2df-12c0-48cd-b0a1-6d9b243980e2</UID><errorReason></errorReason><eventName>Channel Connectivity</eventName><eventOccurTime format=\"yyyy-MM-dd HH:mm:ss:SSSZ\" java-class=\"java.sql.Timestamp\">2007-02-28 20:01:06:582+0800</eventOccurTime><eventRemark></eventRemark><groupName>Inovis</groupName><messageID>OB-1945</messageID><status>OK</status><tracingID>be07528d-9cb8-44a4-ba3d-7dcd51c094ba</tracingID><version>0</version></TraceEventInfo>";
    System.out.println("info xml "+infoXML);
    
    info = (TraceEventInfo)XMLBeanUtil.xmlToBean(infoXML, TraceEventInfo.class);
    //info = (TraceEventInfo)XMLBeanUtil.decodeBean(infoXML);
    System.out.println(info);*/
    
    /* demo time stamp will have problem also
    Timestamp ts = new Timestamp((new Date()).getTime());
    System.out.println(ts);
    
    Google g = new Google();
    g.setFromStartTime(new Timestamp(new Date().getTime()));
    String google = XMLBeanUtil.beanToXml(g, Google.class.getName(), format);
    System.out.println(google);
    
    Google h = (Google)XMLBeanUtil.xmlToBean(google, Google.class);
    System.out.println(h.getFromStartTime()); 
    
    Google g = new Google();
    g.setFromStartTime(new Timestamp(new Date().getTime()));
    String google = XMLBeanMarshal.objToXML(g);
    System.out.println(google);
    
    Google g1 = (Google)XMLBeanMarshal.xmlToObj(google, Google.class);
    System.out.println(g1);
  }*/
}

