package com.gridnode.pdip.app.workflow.client;

//import com.gridnode.pdip.base.rolemap.entities.model.*;
//import com.gridnode.pdip.base.rolemap.facade.ejb.*;
import java.io.*;
import java.util.*;

import com.gridnode.pdip.app.workflow.impl.bpss.helpers.*;
import com.gridnode.pdip.framework.j2ee.*;
import junit.framework.*;

public class BpssRequestorExSignalTestCase extends TestCase
{
    static int pindex=0;

    static WorkflowClient wfClient;
    static String PROCESS_DEF="http://BPSS/BpssSendReceiveCollaboration/1.14/[1234-5678-901239]/BpssBinaryCollaboration/Product Fulfillment";
    static String docPropFileName="d:/testReqDocument.tmp";
    static String validateFile="d:/testValidateDoc.tmp";
    static String testActions="d:/testActions.tmp";

    static Long contextUId;

    public BpssRequestorExSignalTestCase(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(BpssRequestorExSignalTestCase.class);
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
        for(int i=0;i<5;i++){
            System.out.println("Checking count = "+i);
            Thread.currentThread().sleep(20*1000);
            prop=getProperties(docPropFileName);
            if(prop!=null){
                System.out.println("Found count = "+i);
                deleteFile(docPropFileName);
                wfClient.insertSignal(prop.getProperty(IBpssConstants.DOCUMENTID),IBpssConstants.EXCEPTION_VALIDATE,"Received invalid document","COMPANY-B");
                break;
            }
        }

        assertNotNull("Engine Failed to send Catalog Request Document ",prop);

    }

    public static void main(String args[]) throws Exception
    {
        pindex=0;
        junit.textui.TestRunner.run(suite());
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
