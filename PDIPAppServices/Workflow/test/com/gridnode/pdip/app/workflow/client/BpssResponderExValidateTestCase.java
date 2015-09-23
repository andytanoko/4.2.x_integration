package com.gridnode.pdip.app.workflow.client;

//import com.gridnode.pdip.base.rolemap.entities.model.*;
//import com.gridnode.pdip.base.rolemap.facade.ejb.*;
import java.io.*;
import java.util.*;

import com.gridnode.pdip.app.workflow.impl.bpss.helpers.*;
import com.gridnode.pdip.app.workflow.util.*;
import com.gridnode.pdip.framework.j2ee.*;
import junit.framework.*;

public class BpssResponderExValidateTestCase extends TestCase
{
    static int pindex=0;
    String xpdlFileNames[] = {"TestXpdlProcess.xml"};

    static WorkflowClient wfClient;
    static String docPropFileName="d:/testResDocument.tmp";
    static String validateFile="d:/testValidateDoc.tmp";
    static String receiptSignalPropFileName="d:/"+IBpssConstants.ACK_RECEIPT_SIGNAL+".tmp";
    static String acceptSignalPropFileName="d:/"+IBpssConstants.ACK_ACCEPTANCE_SIGNAL+".tmp";
    static String PROCESS_DEF="http://BPSS/BpssSendReceiveCollaboration/1.14/[1234-5678-901239]/BpssBinaryCollaboration/Product Fulfillment";
    static String exValidateSignal="d:/"+IBpssConstants.EXCEPTION_VALIDATE+".tmp";
    static String docId=""+new Date().getTime();
    static String testActions="d:/testActions.tmp";
    static Long contextUId;

    public BpssResponderExValidateTestCase(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(BpssResponderExValidateTestCase.class);
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

        Properties prop=new Properties();
        prop.setProperty("PO123","Invalid");
        saveProp(prop,validateFile);


        //start binary collaboration by inserting document
        String requestDocObject="This PO123 request Document";
        wfClient.insertDocument(docId,"PO123",requestDocObject,"COMPANY-A","COMPANY-A",IBpssConstants.PARTNER_CONSTANT);
    }


    public void testSleepUntilReqDoc() throws Exception
    {
        Properties prop=null;
        System.out.println("Waiting for Exception Signal Validate ");
        for(int i=0;i<7;i++){
            System.out.println("Checking count = "+i);
            Thread.currentThread().sleep(20*1000);
            prop=getProperties(exValidateSignal);
            if(prop!=null){
                System.out.println("Found count = "+i);
                break;
            }
        }

        assertNotNull("Engine Failed to Exception Validate Signal ",prop);
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
