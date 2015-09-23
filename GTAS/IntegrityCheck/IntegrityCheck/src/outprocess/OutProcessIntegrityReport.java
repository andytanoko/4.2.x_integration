import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.DocumentUtil;
import com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.model.ProcessInstance;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.log.Log;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class OutProcessIntegrityReport
{
  private final SimpleDateFormat _dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final SimpleDateFormat _fFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
  private Timestamp _start, _end;
  private String _docType;
  private Writer _successFile, _incompleteFile, _failedFile, _noDocsFile, _noObDocsFile;
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
    
    OutProcessIntegrityReport integrityCheck = new OutProcessIntegrityReport(args[0], timeStart, timeEnd);
    
    try
    {
      Log.debug("[OutProcessIntegrityReport.main]", "Start checking...");
      integrityCheck.start(); 
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.err("[OutProcessIntegrityReport.main]", "Integrity Check Error: ", e);
      System.exit(-3);
    }
    finally
    {
      Log.debug("[OutProcessIntegrityReport.main]", "Exit");
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
      Log.err("[OutProcessIntegrityReport.getTimestamp]", "Illegal Timestamp format: "+tsString + ", Expected: YYYY-MM-DD HH:mm:ss");
    }
    
    return ts;
  }
  public OutProcessIntegrityReport(String docType, Timestamp start, Timestamp end)
  {
    _docType = docType;
    _start = start;
    _end = end;
  }

  static void printUsage()
  {
    StringBuffer buff = new StringBuffer();
    buff.append("IntegrityCheck expects Parameters as follows:-\n")
      .append("Param 1: Outbound Document Type\n")
      .append("Param 2: Transaction DateTime Between [YYYY-MM-DD HH:mm:ss]\n")
      .append("Param 3: and [YYYY-MM-DD HH:mm:ss]\n");
    System.out.println(buff.toString());  
  }
  
  void start() throws Exception
  {
    loadProperties();
    String folderName = "Outgoing_".concat(_docType).concat("_").concat(formatDate(_start)).concat("_").concat(formatDate(_end));
    _reportFolder = new File(folderName);
    _reportFolder.mkdir();
    
    createNoDocsFile();
    createNoObDocsFile();
    createSuccessFile();
    createIncompleteFile();
    createFailedFile();
    
    Log.log("[OutProcessIntegrityReport.start]", "Checking process instances...");
    checkProcessInstances();
    
    _noObDocsFile.close();
    _noDocsFile.close();
    _successFile.close();
    _incompleteFile.close();
    _failedFile.close();
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
    _successFile = createWriter("complete_list.csv");
     
    StringBuffer buff = new StringBuffer();
    buff.append("ProcessInstId,ProcessStartTime,ProcessEndTime,")
      .append("RecipientPartner,RecipientBE,DocNo,OBGdocId,OBSendStartTime,OBUdocFilename,OBGdocFilename,")
      .append("IsAck,AckReceiveTime,AckExportTime")
      .append("\n");
    _successFile.write(buff.toString());
    _successFile.flush();    
  }
  
  void writeSuccessRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument obDoc = (GridDocument)record[1];
    GridDocument obSentDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument epAckDoc = (GridDocument)record[4];
    
    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",");
    
    if (obSentDoc != null)
      obDoc = obSentDoc;
        
    buff.append(obDoc.getRecipientPartnerId()).append(",").append(obDoc.getRecipientBizEntityId()).append(",")
      .append(obDoc.getUdocNum()).append(",")
      .append(obDoc.getGdocId()).append(",").append(formatDateTime(obDoc.getDateTimeSendStart())).append(",")
      .append(obDoc.getUdocFilename()).append(",").append(obDoc.getGdocFilename()).append(",");
      
    buff.append(ackDoc!=null).append(",")
    .append(ackDoc==null? "NULL" : formatDateTime(ackDoc.getDateTimeReceiveEnd()))
    .append(",");
    
    if (epAckDoc == null)
      buff.append("NULL");
    else
      buff.append(formatDateTime(epAckDoc.getDateTimeExport()));  

    buff.append("\n");        
    _successFile.write(buff.toString());
    _successFile.flush();
  }
  
  void createIncompleteFile() throws Exception
  { 
    _incompleteFile = createWriter("incomplete_list.csv");
     
    StringBuffer buff = new StringBuffer();
    buff.append("ProcessInstId,ProcessStartTime,ProcessEndTime,")
      .append("RecipientPartner,RecipientBE,DocNo,OBGdocId,OBSendStartTime,OBUdocFilename,OBGdocFilename,")
      .append("IsAck,AckReceiveTime,AckExportTime")
      .append("\n");
    _incompleteFile.write(buff.toString());
    _incompleteFile.flush();    
  }
  
  void writeIncompleteRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument obDoc = (GridDocument)record[1];
    GridDocument obSentDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument epAckDoc = (GridDocument)record[4];
    
    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",");
    
    if (obSentDoc != null)
      obDoc = obSentDoc;
        
    buff.append(obDoc.getRecipientPartnerId()).append(",").append(obDoc.getRecipientBizEntityId()).append(",")
      .append(obDoc.getUdocNum()).append(",")
      .append(obDoc.getGdocId()).append(",").append(formatDateTime(obDoc.getDateTimeSendStart())).append(",")
      .append(obDoc.getUdocFilename()).append(",").append(obDoc.getGdocFilename()).append(",");
      
    buff.append(ackDoc!=null).append(",")
    .append(ackDoc==null? "NULL" : formatDateTime(ackDoc.getDateTimeReceiveEnd()))
    .append(",");
    
    if (epAckDoc == null)
      buff.append("NULL");
    else
      buff.append(formatDateTime(epAckDoc.getDateTimeExport()));  

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
      .append("RecipientPartner,RecipientBE,DocNo,OBGdocId,OBSendStartTime,OBUdocFilename,OBGdocFilename,")
      .append("IsAck,AckReceiveTime")
      .append("\n");
    
    _failedFile.write(buff.toString());
    _failedFile.flush();
  }
  
  void writeFailedRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument obDoc = (GridDocument)record[1];
    GridDocument obSentDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument epAckDoc = (GridDocument)record[4];

    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",")
      .append(processInst.get(ProcessInstance.FAIL_REASON)).append(",");
    
    if (obSentDoc != null)
      obDoc = obSentDoc;
    if (obDoc != null)
    {  
      buff.append(obDoc.getRecipientPartnerId()).append(",").append(obDoc.getRecipientBizEntityId()).append(",")
        .append(obDoc.getUdocNum()).append(",")
        .append(obDoc.getGdocId()).append(",").append(formatDateTime(obDoc.getDateTimeSendStart())).append(",")
        .append(obDoc.getUdocFilename()).append(",").append(obDoc.getGdocFilename()).append(",");
    }
    else
    {
      buff.append("NULL,NULL,NULL,NULL,NULL,NULL,NULL,");
    }
      
    buff.append(ackDoc!=null).append(",")
    .append(ackDoc==null? "NULL" : formatDateTime(ackDoc.getDateTimeReceiveEnd()))
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
  
  void createNoObDocsFile() throws Exception
  { 
    _noObDocsFile = createWriter("no_obdocs_list.csv");
     
    StringBuffer buff = new StringBuffer();
    buff.append("ProcessInstId,ProcessStartTime,ProcessEndTime,State,Partner,")
      .append("IsAck,AckReceiveTime,AckExportTime")
      .append("\n");
    _noObDocsFile.write(buff.toString());
    _noObDocsFile.flush();    
  }
  
  void writeNoObDocsRecord(Object[] record) throws Exception
  {
    StringBuffer buff = new StringBuffer();
    Map processInst = (Map)record[0];
    GridDocument obDoc = (GridDocument)record[1];
    GridDocument obSentDoc = (GridDocument)record[2];
    GridDocument ackDoc = (GridDocument)record[3];
    GridDocument epAckDoc = (GridDocument)record[4];
    
    buff.append(processInst.get(ProcessInstance.PROCESS_INSTANCE_ID)).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.START_TIME))).append(",")
      .append(formatDateTime((Date)processInst.get(ProcessInstance.END_TIME))).append(",")
      .append(processInst.get(ProcessInstance.STATE)).append(",")
      .append(processInst.get(ProcessInstance.PARTNER)).append(",");
      
    buff.append(ackDoc!=null).append(",")
    .append(ackDoc==null? "NULL" : formatDateTime(ackDoc.getDateTimeReceiveEnd()))
    .append(",");

    if (epAckDoc != null)
    {      
      buff.append(formatDateTime(epAckDoc.getDateTimeExport()));    
    }
    else
      buff.append("NULL");

    buff.append("\n");        
    _noObDocsFile.write(buff.toString());
    _noObDocsFile.flush();
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
        if ("InitiatingRole".equals(role))
          checkProcessInstance(processInst);
      }
      else
      Log.err("[OutProcessIntegrityReport.checkProcessInstances]","Unable to locate ProcessInstance with uid="+processInstUid);
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
    
    GridDocument obDoc = findOutboundDocument(_docType, docUids);
    GridDocument obSentDoc = findSentOutboundDocument(_docType, docUids);  
    GridDocument ackDoc = findInboundDocument("RN_ACK", docUids);
    GridDocument epAckDoc = findExportDocument(docUids);
    
    Object[] record = new Object[]{processInst, obDoc, obSentDoc, ackDoc, epAckDoc };
    if (failed != null && failed.booleanValue())
    {
      // failed process instances
      writeFailedRecord(record);
    }
    else
    {
      if (completed || ackDoc != null)
      {
        // completed/acked process instances
        if (obDoc != null)
        {
          writeSuccessRecord(record);
        }
        else
        {
          writeNoObDocsRecord(record);
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
    
    // this may not be very accurate, should filter by processoriginatorid also but we
    // don't have it at this point. Anyway if it comes to this stage it means the process is initiated
    // from a partner not using gridtalk and the processinstanceid should be quite unique for the first 80 char
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RNProfile.PROCESS_INSTANCE_ID, filter.getEqualOperator(), processInstId, false);
    
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
  
}
