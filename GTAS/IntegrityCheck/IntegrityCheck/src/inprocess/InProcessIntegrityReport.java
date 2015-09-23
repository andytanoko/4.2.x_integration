/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2003(C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InProcessIntegrityReport.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ???						???									Created
 * 23 Sep 2004		Alain Ah Ming				Fix GNDB00025329:
 * 																			Add processOriginatorId to findByRnProfile
 * 22 Dec 2004		Alain Ah Ming				Remove DocNo from reports.
 */
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.BpssHandler;
import com.gridnode.gtas.server.rnif.helpers.DocumentUtil;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.model.ProcessInstance;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerHome;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.io.*;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @todo Class documentation
 * 
 *  @author 
 *  @since
 *  @version 2.4.3.3
 */
public class InProcessIntegrityReport
{
  private final SimpleDateFormat _dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final SimpleDateFormat _fFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
  private Timestamp _start, _end;
  private String _docType;
  private Writer _successFile, _successNoExportFile, _failedFile, _noDocsFile, _noIbDocsFile, _incompleteFile;
  private File _reportFolder;
  private List _processUids = new ArrayList();
  
  public static void main(String[] args)
  {
    if (args.length < 3)
    {
      printUsage();
      System.exit(-1);  
    }
    
    Timestamp timeStart = getTimestamp(args[1]);
    Timestamp timeEnd = getTimestamp(args[2]);
    if (timeStart == null || timeEnd == null)
    {
      printUsage();
      System.exit(-2);
    }
    
    InProcessIntegrityReport integrityCheck = new InProcessIntegrityReport(args[0], timeStart, timeEnd);
    
    try
    {
      Log.debug("[InProcessIntegrityReport.main]", "Start checking...");
      integrityCheck.start(); 
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.err("[InProcessIntegrityReport.main]", "Integrity Check Error: ", e);
      System.exit(-3);
    }
    finally
    {
      Log.debug("[InProcessIntegrityReport.main]", "Exit");
    }
  }
  
  static Timestamp getTimestamp(String tsString)
  {
    Timestamp ts = null;
    try
    {
      ts = Timestamp.valueOf(tsString);
    }
    catch (Exception e)
    {
      Log.err("[InProcessIntegrityReport.getTimestamp]", "Illegal Timestamp format: "+tsString + ", Expected: YYYY-MM-DD HH:mm:ss");
    }
    
    return ts;
  }
  
  public InProcessIntegrityReport(String docType, Timestamp start, Timestamp end)
  {
    _docType = docType;
    _start = start;
    _end = end;
  }

  static void printUsage()
  {
    StringBuffer buff = new StringBuffer();
    buff.append("IntegrityCheck expects Parameters as follows:-\n")
      .append("Param 1: Inbound Document Type\n")
      .append("Param 2: Transaction DateTime Between [YYYY-MM-DD HH:mm:ss]\n")
      .append("Param 3: and [YYYY-MM-DD HH:mm:ss]\n");
    System.out.println(buff.toString());  
  }
  
  void start() throws Exception
  {
    loadProperties();
    String folderName = "Incoming_".concat(_docType).concat("_").concat(formatDate(_start)).concat("_").concat(formatDate(_end));
    _reportFolder = new File(folderName);
    _reportFolder.mkdir();
    
    createNoDocsFile();
    createNoIbDocsFile();
    createSuccessFile();
    createSuccessNoExportFile();
    createFailedFile();
    createIncompleteFile();
    
    Log.log("[InProcessIntegrityReport.start]","Checking process instances...");
    checkProcessInstances();
    
    _noIbDocsFile.close();
    _noDocsFile.close();
    _successFile.close();
    _successNoExportFile.close();
    _failedFile.close();
    _incompleteFile.close();
  }
  
  void loadProperties() throws Exception
  {
    BufferedReader processFile = createReader(_docType.concat(".txt"));
    processFile.readLine(); //skip the header
    String uid;
    while ((uid=processFile.readLine())!=null)
    {
      _processUids.add(Long.valueOf(uid));        
    }
    if (_processUids.isEmpty())
      throw new Exception("No Process Instance detected!");
  }

  BufferedReader createReader(String filename) throws Exception
  {
    return new BufferedReader(new FileReader(filename));  
  }
  
  void tokenize(String str, String delim, List tokenList)
  {
    StringTokenizer st = new StringTokenizer(str, delim);
    while (st.hasMoreTokens())
    {
      tokenList.add(st.nextToken());
    }
  }
  
  Writer createWriter(String filename) throws Exception
  {
    
    BufferedWriter w = new BufferedWriter(new FileWriter(new File(_reportFolder, filename)));
    return w;
  }

  void createSuccessFile() throws Exception
  { 
    _successFile = createWriter("acked_wexport_list.csv");
     
    StringBuffer buff = new StringBuffer();
    buff.append("ProcessInstId,ProcessStartTime,ProcessEndTime,")
      .append("SenderPartner,SenderBE,IBGdocId,IBReceiveTime,IBUdocFilename,IBGdocFilename,")
      .append("IsAck,AckSentTime,")
      .append("EPGdocId,EPGdocFilename,EPUdocFilename,EPSrcFolder,EPRefGdocId,EPRefUdocFilename,EPExportedTime,EPDocFullPath")
      .append("\n");
    _successFile.write(buff.toString());
    _successFile.flush();    
  }
  
  void writeSuccessRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument ibDoc = (GridDocument)record[1];
    GridDocument epDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument sentAckDoc = (GridDocument)record[4];
    
    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",");
      
    buff.append(ibDoc.getSenderPartnerId()).append(",").append(ibDoc.getSenderBizEntityId()).append(",")
      .append(ibDoc.getGdocId()).append(",").append(formatDateTime(ibDoc.getDateTimeReceiveEnd())).append(",")
      .append(ibDoc.getUdocFilename()).append(",").append(ibDoc.getGdocFilename()).append(",");
      
    buff.append(ackDoc!=null).append(",")
    .append(sentAckDoc==null? "NULL" : formatDateTime(sentAckDoc.getDateTimeSendEnd()))
    .append(",");
      
    buff.append(epDoc.getGdocId()).append(",").append(epDoc.getGdocFilename()).append(",")
      .append(epDoc.getUdocFilename()).append(",").append(epDoc.getSrcFolder()).append(",")
      .append(epDoc.getRefGdocId()).append(",").append(epDoc.getRefUdocFilename()).append(",")
      .append(formatDateTime(epDoc.getDateTimeExport())).append(",").append(epDoc.getExportedUdocFullPath());    

    buff.append("\n");        
    _successFile.write(buff.toString());
    _successFile.flush();
  }
  
  void createSuccessNoExportFile() throws Exception
  {
    _successNoExportFile = createWriter("acked_noexport_list.csv");
     
    StringBuffer buff = new StringBuffer();
    buff.append("ProcessInstId,ProcessStartTime,ProcessEndTime,")
      .append("SenderPartner,SenderBE,IBGdocId,IBReceiveTime,IBUdocFilename,IBGdocFilename,IBUid,")
      .append("IsAck,AckSentTime")
      .append("\n");
    _successNoExportFile.write(buff.toString());
    _successNoExportFile.flush();    
  }
  
  void writeSuccessNoExportRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument ibDoc = (GridDocument)record[1];
    GridDocument epDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument sentAckDoc = (GridDocument)record[4];

    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",");
      
    buff.append(ibDoc.getSenderPartnerId()).append(",").append(ibDoc.getSenderBizEntityId()).append(",")
      .append(ibDoc.getGdocId()).append(",").append(formatDateTime(ibDoc.getDateTimeReceiveEnd())).append(",")
      .append(ibDoc.getUdocFilename()).append(",").append(ibDoc.getGdocFilename()).append(",")
      .append(ibDoc.getUId()).append(",");
      
      
    buff.append(ackDoc!=null).append(",")
    .append(sentAckDoc==null? "NULL" : formatDateTime(sentAckDoc.getDateTimeSendEnd()))
    .append(",");
      
    buff.append("\n");  
    _successNoExportFile.write(buff.toString());
    _successNoExportFile.flush();
  }
  
  void createIncompleteFile() throws Exception
  { 
    _incompleteFile = createWriter("incomplete_list.csv");
     
    StringBuffer buff = new StringBuffer();
    buff.append("ProcessInstId,ProcessStartTime,ProcessEndTime,")
      .append("SenderPartner,SenderBE,IBGdocId,IBReceiveTime,IBUdocFilename,IBGdocFilename,")
      .append("IsAck,AckSentTime,")
      .append("EPGdocId,EPGdocFilename,EPUdocFilename,EPSrcFolder,EPRefGdocId,EPRefUdocFilename,EPExportedTime,EPDocFullPath")
      .append("\n");
    _incompleteFile.write(buff.toString());
    _incompleteFile.flush();    
  }
  
  void writeIncompleteRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument ibDoc = (GridDocument)record[1];
    GridDocument epDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument sentAckDoc = (GridDocument)record[4];
    
    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",");
      
    buff.append(ibDoc.getSenderPartnerId()).append(",").append(ibDoc.getSenderBizEntityId()).append(",")
      .append(ibDoc.getGdocId()).append(",").append(formatDateTime(ibDoc.getDateTimeReceiveEnd())).append(",")
      .append(ibDoc.getUdocFilename()).append(",").append(ibDoc.getGdocFilename()).append(",");
      
    buff.append(ackDoc!=null).append(",")
    .append(sentAckDoc==null? "NULL" : formatDateTime(sentAckDoc.getDateTimeSendEnd()))
    .append(",");
      
    if (epDoc != null)
    {  
      buff.append(epDoc.getGdocId()).append(",").append(epDoc.getGdocFilename()).append(",")
        .append(epDoc.getUdocFilename()).append(",").append(epDoc.getSrcFolder()).append(",")
        .append(epDoc.getRefGdocId()).append(",").append(epDoc.getRefUdocFilename()).append(",")
        .append(formatDateTime(epDoc.getDateTimeExport())).append(",").append(epDoc.getExportedUdocFullPath());    
    }
    else
    {
      buff.append("NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL");
    }

    buff.append("\n");        
    _incompleteFile.write(buff.toString());
    _incompleteFile.flush();
  }
  
  String formatDate(Date date)
  {
    return _fFormat.format(date);
  }
  
  String formatDateTime(Date dateTime)
  {
    if (dateTime != null)
      return _dFormat.format(dateTime);
    return "NULL";
  }
  
  void createFailedFile() throws Exception
  {
    _failedFile = createWriter("abnormal_complete_list.csv");
     
    StringBuffer buff = new StringBuffer();
    buff.append("ProcessInstId,ProcessStartTime,ProcessEndTime,FailReason,")
      .append("SenderPartner,SenderBE,IBGdocId,IBReceiveTime,IBUdocFilename,IBGdocFilename,")
      .append("IsAck,AckSentTime")
      .append("\n");
    
    _failedFile.write(buff.toString());
    _failedFile.flush();
  }
  
  void writeFailedRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument ibDoc = (GridDocument)record[1];
    GridDocument epDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument sentAckDoc = (GridDocument)record[4];

    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",")
      .append(processInst.get(ProcessInstance.FAIL_REASON)).append(",");
    
    if (ibDoc != null)
    {  
      buff.append(ibDoc.getSenderPartnerId()).append(",").append(ibDoc.getSenderBizEntityId()).append(",")
        .append(ibDoc.getGdocId()).append(",").append(formatDateTime(ibDoc.getDateTimeReceiveEnd())).append(",")
        .append(ibDoc.getUdocFilename()).append(",").append(ibDoc.getGdocFilename()).append(",");
    }
    else
    {
      buff.append("NULL,NULL,NULL,NULL,NULL,NULL,NULL,");
    }
      
    buff.append(ackDoc!=null).append(",")
    .append(sentAckDoc==null? "NULL" : formatDateTime(sentAckDoc.getDateTimeSendEnd()))
    .append(",");
      
    buff.append("\n");  
    _failedFile.write(buff.toString());
    _failedFile.flush();
  }
  
  void createNoDocsFile() throws Exception
  {
    _noDocsFile = createWriter("no_assocdocs_list.csv");
     
    StringBuffer buff = new StringBuffer();
    buff.append("ProcessInstId,ProcessStartTime,ProcessEndTime,FailReason,State,Partner")
      .append("\n");
    
    _noDocsFile.write(buff.toString());
    _noDocsFile.flush();
  }

  void writeNoDocsRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument ibDoc = (GridDocument)record[1];
    GridDocument epDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument sentAckDoc = (GridDocument)record[4];

    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",")
      .append(processInst.get(ProcessInstance.FAIL_REASON)).append(",")
      .append(processInst.get(ProcessInstance.STATE)).append(",")
      .append(processInst.get(ProcessInstance.PARTNER));
      
    buff.append("\n");  
    _noDocsFile.write(buff.toString());
    _noDocsFile.flush();
  }
  
  void createNoIbDocsFile() throws Exception
  { 
    _noIbDocsFile = createWriter("no_ibdocs_list.csv");
     
    StringBuffer buff = new StringBuffer();
    buff.append("ProcessInstId,ProcessStartTime,ProcessEndTime,State,Partner,")
      .append("IsAck,AckSentTime,")
      .append("EPGdocId,EPGdocFilename,EPUdocFilename,EPSrcFolder,EPRefGdocId,EPRefUdocFilename,EPExportedTime,EPDocFullPath")
      .append("\n");
    _noIbDocsFile.write(buff.toString());
    _noIbDocsFile.flush();    
  }
  
  void writeNoIbDocsRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument ibDoc = (GridDocument)record[1];
    GridDocument epDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument sentAckDoc = (GridDocument)record[4];
    
    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",")
      .append(processInst.get(ProcessInstance.STATE)).append(",")
      .append(processInst.get(ProcessInstance.PARTNER)).append(",");
      
    buff.append(ackDoc!=null).append(",")
    .append(sentAckDoc==null? "NULL" : formatDateTime(sentAckDoc.getDateTimeSendEnd()))
    .append(",");

    if (epDoc != null)
    {      
      buff.append(epDoc.getGdocId()).append(",")
        .append(epDoc.getGdocFilename()).append(",")
        .append(epDoc.getUdocFilename()).append(",").append(epDoc.getSrcFolder()).append(",")
        .append(epDoc.getRefGdocId()).append(",").append(epDoc.getRefUdocFilename()).append(",")
        .append(formatDateTime(epDoc.getDateTimeExport())).append(",").append(epDoc.getExportedUdocFullPath());    
    }
    else
      buff.append("NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL");

    buff.append("\n");        
    _noIbDocsFile.write(buff.toString());
    _noIbDocsFile.flush();
  }
  
  void checkProcessInstances() throws Exception
  {
    Iterator processInstIter = getProcessInstances(_start, _end);
    Map processInst;
    Long processInstUid;
    while (processInstIter.hasNext())
    {
      processInstUid = (Long)processInstIter.next();
      processInst = ProcessInstanceActionHelper.getProcessInstance(processInstUid);
      if (processInst != null)
      {
        String role = (String)processInst.get(ProcessInstance.ROLE_TYPE);
        if ("RespondingRole".equals(role))
          checkProcessInstance(processInst);
      }
      else
        Log.err("[InProcessIntegrityReport.checkProcessInstances]","Unable to locate ProcessInstance with uid="+processInstUid);
    }
  }

  void checkProcessInstance(Map processInst) throws Exception
  {
    Boolean failed = (Boolean)processInst.get(ProcessInstance.IS_FAILED);
    boolean completed = (processInst.get(ProcessInstance.END_TIME) != null);
    Collection docUids = (Collection)processInst.get(ProcessInstance.ASSOC_DOCS);
    if (docUids == null || docUids.isEmpty())
    {
      findByRnprofile(processInst);
      docUids = (Collection)processInst.get(ProcessInstance.ASSOC_DOCS);
      if (docUids == null || docUids.isEmpty())
      {
        if (completed)
          writeNoDocsRecord(new Object[]{processInst,null,null,null,null});
        return;
      }
    }
      
    GridDocument ibDoc = findInboundDocument(_docType, docUids);
    GridDocument epDoc = findExportDocument(docUids);
    GridDocument ackDoc = findOutboundDocument("RN_ACK", docUids);
    GridDocument sentAckDoc = findSentOutboundDocument("RN_ACK", docUids);
    
    Object[] record = new Object[]{processInst, ibDoc, epDoc, ackDoc, sentAckDoc };
    if (failed != null && failed.booleanValue())
    {
      // failed process instances
      writeFailedRecord(record);
    }
    else
    {
      if (completed || sentAckDoc != null)
      {
        // completed/acked process instances
        if (ibDoc != null)
        {
          if (epDoc == null)
          {
            writeSuccessNoExportRecord(record);
          }
          else
            writeSuccessRecord(record);
        }
        else
        {
          writeNoIbDocsRecord(record);
        }
      }
      else
      {
        writeIncompleteRecord(record);
      }
    }
  }
  
  Iterator getProcessInstances(Timestamp start, Timestamp end)
    throws Exception
  {
    DataFilterImpl leftFilter = new DataFilterImpl();
    leftFilter.addRangeFilter(null, GWFRtProcess.START_TIME,
      start, end, false);
    leftFilter.addRangeFilter(leftFilter.getOrConnector(),
      GWFRtProcess.END_TIME, start, end, false);
    
    DataFilterImpl rightFilter = new DataFilterImpl();
    rightFilter.addSingleFilter(null, GWFRtProcess.ENGINE_TYPE,
      rightFilter.getEqualOperator(), "BPSS", false);
    rightFilter.addSingleFilter(rightFilter.getAndConnector(),
      GWFRtProcess.PROCESS_TYPE, rightFilter.getEqualOperator(),
      "BpssBinaryCollaboration", false);
    rightFilter.addDomainFilter(rightFilter.getAndConnector(), GWFRtProcess.PROCESS_UID, _processUids, false);
    
    DataFilterImpl filter = new DataFilterImpl(leftFilter, leftFilter.getAndConnector(), rightFilter);
    filter.setOrderFields(new Number[]{GWFRtProcess.UID});
    
    return ProcessInstanceActionHelper.getWorkflowMgr().getProcessInstanceKeys(filter).iterator();    
  }
    
  void findByRnprofile(Map processInst) throws Exception
  {
    String processInstId = (String)processInst.get(ProcessInstance.PROCESS_INSTANCE_ID);
    if (processInstId.length() > 80)
      processInstId = processInstId.substring(0, 80);
    
    // this is very accurate, it's filtered by processoriginatorid also as we
    // have it at this point. Sometimes when it comes to this stage it means the process is initiated
    // from a partner not using gridtalk and the processinstanceid is not quite unique for the first 80 char
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RNProfile.PROCESS_INSTANCE_ID, filter.getEqualOperator(), processInstId, false);
    
    // Add process originator Id to filter.
    String originatorId = getProcessInstanceOriginatorId((Long)processInst.get(ProcessInstance.UID));
    filter.addSingleFilter(filter.getAndConnector(), RNProfile.PROCESS_ORIGINATOR_ID, filter.getEqualOperator(), originatorId, false);
    
    Collection profileKeys = ProfileUtil.getRnifManager().findRNProfilesKeys(filter);
    if (profileKeys != null && !profileKeys.isEmpty())
    {
      DataFilterImpl gdocFilter = new DataFilterImpl();
      gdocFilter.addDomainFilter(null, GridDocument.RN_PROFILE_UID, profileKeys, false);
      Collection assocDocs = DocumentUtil.getDocumentManager().findGridDocumentsKeys(gdocFilter);
      if (assocDocs != null)
        processInst.put(ProcessInstance.ASSOC_DOCS, assocDocs);
    }
        
  }
  
  GridDocument findInboundDocument(String ibDocType, Collection uids) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.UID, uids, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.FOLDER, filter.getEqualOperator(),
      GridDocument.FOLDER_INBOUND, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.U_DOC_DOC_TYPE,
      filter.getEqualOperator(), ibDocType, false);
    
    Collection result = DocumentUtil.getDocumentManager().findGridDocuments(filter);
    return (result==null||result.isEmpty())?null:(GridDocument)result.iterator().next();    
  }

  GridDocument findOutboundDocument(String obDocType, Collection uids) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.UID, uids, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.FOLDER, filter.getEqualOperator(),
      GridDocument.FOLDER_OUTBOUND, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.U_DOC_DOC_TYPE,
    filter.getEqualOperator(), obDocType, false);
    
    Collection result = DocumentUtil.getDocumentManager().findGridDocuments(filter);
    return (result==null||result.isEmpty())?null:(GridDocument)result.iterator().next();    
  }

  GridDocument findSentOutboundDocument(String obDocType, Collection uids) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.UID, uids, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.FOLDER, filter.getEqualOperator(),
      GridDocument.FOLDER_OUTBOUND, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.U_DOC_DOC_TYPE,
      filter.getEqualOperator(), obDocType, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.DT_SEND_END, filter.getNotEqualOperator(),
      null, false);  
    
    Collection result = DocumentUtil.getDocumentManager().findGridDocuments(filter);
    return (result==null||result.isEmpty())?null:(GridDocument)result.iterator().next();    
  }
  
  GridDocument findExportDocument(Collection uids) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.UID, uids, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.FOLDER, filter.getEqualOperator(),
      GridDocument.FOLDER_EXPORT, false);
    
    Collection result = DocumentUtil.getDocumentManager().findGridDocuments(filter);
    return (result==null||result.isEmpty())?null:(GridDocument)result.iterator().next();    
  }

	// ***************** Get Manager Helpers *****************************
	private static IGWFWorkflowManagerObj getWorkflowMgr() throws ServiceLookupException
	{
		return (IGWFWorkflowManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
			IGWFWorkflowManagerHome.class.getName(),
			IGWFWorkflowManagerHome.class,
			new Object[0]);
	}
  
  private static String getProcessInstanceOriginatorId(Long processInstanceUid)
 		throws ServiceLookupException, SystemException, RemoteException, WorkflowException
  {
  	String originatorId = "";
		IGWFWorkflowManagerObj wfMgr= getWorkflowMgr();
		Collection docList= wfMgr.getRtProcessDocList(processInstanceUid);
		if (docList != null && !docList.isEmpty())
		{
			GWFRtProcessDoc processDoc= (GWFRtProcessDoc) docList.iterator().next();
			String[] separateInstId= getSeparateIdsFromBPSSDocId(processDoc.getDocumentId());
			originatorId = separateInstId[IRnifConstant.INDEX_PROCESS_INITIATOR];
		}
		Log.debug("[InProcessIntegrity.getProcessInstanceOriginatorId]", 
			"ProcessInstance UID["+processInstanceUid+"] ORIGINATOR_ID["+originatorId+"]");
		return originatorId;
  }
  
	private static String[] getSeparateIdsFromBPSSDocId(String docId)
	{
		return BpssHandler.getSeperateIdsFromBPSSDocId(docId);
	}
}
