package com.gridnode.pdip.app.workflow.client;

//import com.gridnode.pdip.base.rolemap.entities.model.*;
//import com.gridnode.pdip.base.rolemap.facade.ejb.*;
import java.io.*;
import java.util.*;

import com.gridnode.pdip.app.workflow.impl.bpss.helpers.*;
import com.gridnode.pdip.app.workflow.util.*;
import com.gridnode.pdip.base.worklist.entities.model.*;
import com.gridnode.pdip.base.worklist.manager.ejb.*;
import com.gridnode.pdip.framework.j2ee.*;
import junit.framework.*;

public class BpssResponderTestCase extends TestCase
{
    static int pindex=0;
    String xpdlFileNames[] = {"TestXpdlProcess.xml"};

    static WorkflowClient wfClient;
    static String docPropFileName="d:/testResDocument.tmp";
    static String validateFile="d:/testValidateDoc.tmp";
    static String receiptSignalPropFileName="d:/"+IBpssConstants.ACK_RECEIPT_SIGNAL+".tmp";
    static String acceptSignalPropFileName="d:/"+IBpssConstants.ACK_ACCEPTANCE_SIGNAL+".tmp";
    static String PROCESS_DEF="http://BPSS/BpssSendReceiveCollaboration/1.14/[1234-5678-901239]/BpssBinaryCollaboration/Product Fulfillment";
    static String testReceivedDoc="d:/testReceivedDoc.tmp";
    static String testActions="d:/testActions.tmp";
    static Long contextUId;
    static String docId=""+new Date().getTime();

    public BpssResponderTestCase(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(BpssResponderTestCase.class);
    }

    public void setUp()
    {
        try
        {
            wfClient=new WorkflowClient(ServiceLookup.CLIENT_CONTEXT);
            assertNotNull("Failed to create WorkflowManager", wfClient.getWorkflowManager());
        }
        catch (Exception ex)
        {
            System.out.println(" Exception in SetUp  : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void tearDown()
    {

    }

    public void testStartBinaryCollaboration() throws Exception {
        removeTempFiles();
        //start binary collaboration by inserting document
        String requestDocObject="This PO123 request Document";
        wfClient.insertDocument(docId,"PO123",requestDocObject,"COMPANY-A","COMPANY-A",IBpssConstants.PARTNER_CONSTANT);
    }


    public void testSleepUntilReqDoc() throws Exception
    {

        Logger.log("waiting for "+IBpssConstants.ACK_RECEIPT_SIGNAL);
        Properties prop=null;
        for(int i=0;i<5;i++){
            System.out.println("Checking count = "+i);
            Thread.currentThread().sleep(10*1000);
            prop=getProperties(receiptSignalPropFileName);
            if(prop!=null){
                Logger.log(IBpssConstants.ACK_RECEIPT_SIGNAL + "\t\t"+prop);
                assertEquals("Waiting for "+IBpssConstants.ACK_RECEIPT_SIGNAL+",  Inavlid signal "+prop,IBpssConstants.ACK_RECEIPT_SIGNAL,prop.getProperty(IBpssConstants.SIGNAL_TYPE));
                break;
            }
        }
        deleteFile(receiptSignalPropFileName);

        assertNotNull("Engine Failed to send "+IBpssConstants.ACK_RECEIPT_SIGNAL ,prop);

        Logger.log("waiting for "+IBpssConstants.ACK_ACCEPTANCE_SIGNAL);
        prop=null;
        for(int i=0;i<5;i++){
            System.out.println("Checking count = "+i);
            prop=getProperties(acceptSignalPropFileName);
            if(prop!=null){
                Logger.log(IBpssConstants.ACK_ACCEPTANCE_SIGNAL + "\t\t"+prop);
                assertEquals("Waiting for "+IBpssConstants.ACK_ACCEPTANCE_SIGNAL+",  Inavlid signal "+prop,IBpssConstants.ACK_ACCEPTANCE_SIGNAL,prop.getProperty(IBpssConstants.SIGNAL_TYPE));
                break;
            }
            Thread.currentThread().sleep(10*1000);
        }
        deleteFile(acceptSignalPropFileName);
        assertNotNull("Engine Failed to send "+IBpssConstants.ACK_ACCEPTANCE_SIGNAL,prop);

        deleteFile(docPropFileName);

        GWFWorkListSessionHome wlHome=(GWFWorkListSessionHome)ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(GWFWorkListSessionHome.class);
        GWFWorkListSessionObject wlObj=wlHome.create();
        GWFWorkListValueEntity wlEntity=null;

        // check for workitem
        for(int i=0;i<5;i++){
            System.out.println("Checking count = "+i);
            Thread.currentThread().sleep(10*1000);
            Collection coll=wlObj.getWorkItemsByActivity("http://XPDL/CreateResDocPkg/Test001/XpdlProcess/CreateResDocProcess","CREATE_RESPONSE_DOCUMENT");
            if(coll!=null && coll.size()>0){
                wlEntity=(GWFWorkListValueEntity)coll.iterator().next();
                Long contextUId=wlEntity.getContextUId();
                HashMap map=new HashMap();
                map.put("responseDocumentObject","This is POA123 Purchase Order Ack Document");
                map.put("responseDocumentType","POA123");

                wfClient.getDataManager().setContextData(contextUId,map);
                wlObj.assignWorkItem(new Long(wlEntity.getUId()),"TestUser");
                wlObj.performWorkItem(new Long(wlEntity.getUId()));
                break;
            }
        }
        assertNotNull("Did not traverse to net BusinessTransActivity, Create Order ",wlEntity);

        Logger.log("waiting for Responding Document");
        for(int i=0;i<5;i++){
            System.out.println("Checking count = "+i);
            Thread.currentThread().sleep(10*1000);
            prop=getProperties(docPropFileName);
            if(prop!=null){
                Logger.log(docPropFileName + "\t\t"+prop);
                processResponseDocument(prop.getProperty(IBpssConstants.DOCUMENTID),prop.getProperty(IBpssConstants.DOCUMENT_TYPE),prop.getProperty(IBpssConstants.DOCUMENT_OBJECT),prop.getProperty("partnerKey"));
                break;
            }
        }
        deleteFile(docPropFileName);
        assertNotNull("Engine Failed to send Response Document POA123" ,prop);

        Thread.currentThread().sleep(10 *1000);

        String requestDocObject="This ASN123 request Document";
        wfClient.insertDocument(docId,"ASN123",requestDocObject,"COMPANY-A","COMPANY-A",IBpssConstants.PARTNER_CONSTANT);
    }

    public static void main(String args[]) throws Exception
    {
        new File("./templog.log").delete();
        pindex=0;
        junit.textui.TestRunner.run(suite());
    }

    private void processResponseDocument(String documentId,String documentType,Object resDocument,String partnerKey) throws Exception{
        if(documentType.equals("POA123")){
            Logger.log("Received Response Document <<<POA123>>> with DocumentId "+documentId+", Document="+resDocument);
            Thread.currentThread().sleep(1000);
            wfClient.insertSignal(documentId,IBpssConstants.ACK_RECEIPT_SIGNAL,"RECEIVED YOUR RESPONSE DOCUMENT",partnerKey);
            Logger.log("Sent Ack Receipt Signal for document type <<<PO123>>> with DocumentId "+documentId);
        } else if(documentType.equals("ASN123")){
            Logger.log("Received Request Document <<<ASN123>>> with DocumentId "+documentId);
            Thread.currentThread().sleep(1000);
        }
    }

    private void deleteFile(String fileName){
        File f=new File(fileName);
        if(f.exists())
            f.delete();
    }

    private void removeTempFiles() throws Exception {
        File testDir=new File("d:/");
        testDir.list(new FilenameFilter(){
                            public boolean accept(File dir,String name){
                                if(name.endsWith(".tmp"))
                                    new File(dir,name).delete();
                                return false;
                            }
                          }
                     );
    }

    private Properties getProperties(String fileName){
        try{
            File f=new File(fileName);
            if(f.exists()){
                Properties prop=new Properties();
                prop.load(new FileInputStream(f));
                return prop;
            }
        }catch(Exception ex){
        }
        return null;
    }
}
