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

public class BpssRequestorTestCase extends TestCase
{
    static int pindex=0;

    static WorkflowClient wfClient;
    static String PROCESS_DEF="http://BPSS/BpssSendReceiveCollaboration/1.14/[1234-5678-901239]/BpssBinaryCollaboration/Product Fulfillment";
    static String docPropFileName="d:/testReqDocument.tmp";
    static String validateFile="d:/testValidateDoc.tmp";
    static String testReceivedDoc="d:/testReceivedDoc.tmp";
    static String testActions="d:/testActions.tmp";
    static String testNotifyReceivedSignal="d:/testNotifyReceivedSignal.tmp";

    static Long contextUId;

    public BpssRequestorTestCase(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(BpssRequestorTestCase.class);
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
        wfClient.insertDocument(null,"PO123",requestDocObject,null,IBpssConstants.PARTNER_CONSTANT,"COMPANY-B");
    }


    public void testSleepUntilReqDoc() throws Exception
    {
        //assertNotNull("Failes starting BinaryCollaboration ",contextUId);
        Properties prop=null;
        System.out.println("Waiting for reqdoc");
        String tmpDocId="";
        for(int i=0;i<5;i++){
            System.out.println("Checking count = "+i);
            Thread.currentThread().sleep(20*1000);
            prop=getProperties(docPropFileName);
            if(prop!=null){
                System.out.println("Found count = "+i);
                deleteFile(docPropFileName);
                tmpDocId=prop.getProperty(IBpssConstants.DOCUMENTID);
                processRequestDocument(prop.getProperty(IBpssConstants.DOCUMENTID),prop.getProperty(IBpssConstants.DOCUMENT_TYPE),prop.getProperty(IBpssConstants.DOCUMENT_OBJECT),prop.getProperty("partnerKey"));
                break;
            }
        }

        assertNotNull("Engine Failed to send Catalog Request Document ",prop);

        Thread.currentThread().sleep(10*1000); //time to perform

        GWFWorkListSessionHome wlHome=(GWFWorkListSessionHome)ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(GWFWorkListSessionHome.class);
        GWFWorkListSessionObject wlObj=wlHome.create();
        GWFWorkListValueEntity wlEntity=null;
        // check for workitem
        System.out.println("Waiting for workitem");
        for(int i=0;i<5;i++){
            System.out.println("Checking count = "+i);
            Thread.currentThread().sleep(10*1000);

            Collection coll=wlObj.getWorkItemsByActivity("http://XPDL/CreateReqDocPkg/Test001/XpdlProcess/CreateReqDocProcess","CREATE_REQUEST_DOCUMENT");
            if(coll!=null && coll.size()>0){
                wlEntity=(GWFWorkListValueEntity)coll.iterator().next();
                Long contextUId=wlEntity.getContextUId();
                HashMap map=new HashMap();
                map.put("requestDocumentObject","This is ASN123 Notify Document");
                wfClient.getDataManager().setContextData(contextUId,map);
                wlObj.assignWorkItem(new Long(wlEntity.getUId()),"TestUser");
                wlObj.performWorkItem(new Long(wlEntity.getUId()));
                break;
            }
        }
        assertNotNull("Did not traverse to next BusinessTransActivity, NotifyShipment ",wlEntity);

        System.out.println("Waiting for reqdoc");
        prop=null;
        for(int i=0;i<5;i++){
            System.out.println("Checking count = "+i);
            Thread.currentThread().sleep(10*1000);
            prop=getProperties(docPropFileName);
            if(prop!=null){
                deleteFile(docPropFileName);
                processRequestDocument(prop.getProperty(IBpssConstants.DOCUMENTID),prop.getProperty(IBpssConstants.DOCUMENT_TYPE),prop.getProperty(IBpssConstants.DOCUMENT_OBJECT),prop.getProperty("partnerKey"));
                break;
            }
        }

        Logger.log("Check after completion insert document <<<POA123>>> with DocumentId "+tmpDocId);
        Thread.currentThread().sleep(60 * 1000);
        wfClient.insertDocument(tmpDocId,"POA123","responseDocObject","COMPANY-B",IBpssConstants.PARTNER_CONSTANT,"COMPANY-B");

        assertNotNull("Engine Failed to send Request Notification Document ",prop);


    }


    public static void main(String args[]) throws Exception
    {
        pindex=0;
        junit.textui.TestRunner.run(suite());
    }

    private void processRequestDocument(String documentId,String documentType,Object reqDocument,String partnerKey) throws Exception{
        if(documentType.equals("PO123")){
            Logger.log("Received Request Document <<<PO123>>> with DocumentId "+documentId+", Document="+reqDocument);
            Thread.currentThread().sleep(1000);
            wfClient.insertSignal(documentId,IBpssConstants.ACK_RECEIPT_SIGNAL,"RECEIVED YOUR REQUEST DOCUMENT",partnerKey);
            Logger.log("Sent Ack Receipt Signal for document type <<<PO123>>> with DocumentId "+documentId);
            Thread.currentThread().sleep(1000);
            //Properties prop=getProperties(testNotifyReceivedSignal);
            //if(prop==null)
            //    throw new Exception("Did not recive NotifyReceivedSignal");
            wfClient.insertSignal(documentId,IBpssConstants.ACK_ACCEPTANCE_SIGNAL,"ACCEPTED YOUR REQUEST DOCUMENT",partnerKey);
            Logger.log("Sent Ack Accept Signal for document type <<<PO123>>> with DocumentId "+documentId);
            Thread.currentThread().sleep(1000);
            String responseDocObject="This POA123 Doc which is response to PO123 document";
            wfClient.insertDocument(documentId,"POA123",responseDocObject,"COMPANY-B",IBpssConstants.PARTNER_CONSTANT,"COMPANY-B");
            Logger.log("Sent Response document type <<<POA123>>> with DocumentId "+documentId);
        } else if(documentType.equals("ASN123")){
            Logger.log("Received Request Document <<<ASN123>>> with DocumentId "+documentId);
            Thread.currentThread().sleep(1000);
        }
    }

    private void deleteFile(String fileName){
        System.out.println("deleteFile "+fileName);
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

    private void saveProp(Properties prop,String fileName){
        try{
            deleteFile(fileName);
            prop.store(new FileOutputStream(fileName),null);
        }catch(Exception ex){
            ex.printStackTrace();
        }
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
