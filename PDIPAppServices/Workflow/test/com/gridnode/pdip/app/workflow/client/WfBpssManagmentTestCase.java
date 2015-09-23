package com.gridnode.pdip.app.workflow.client;

import java.io.*;
import java.util.*;

import com.gridnode.pdip.app.workflow.impl.bpss.helpers.*;
import com.gridnode.pdip.app.workflow.runtime.model.*;
//import com.gridnode.pdip.base.rolemap.entities.model.*;
//import com.gridnode.pdip.base.rolemap.facade.ejb.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.j2ee.*;
import junit.framework.*;


public class WfBpssManagmentTestCase extends TestCase
{
    static int pindex=0;

    static String BPSS_PROCESS_DEF="http://BPSS/BpssSendReceiveCollaboration/1.14/[1234-5678-901239]/BpssBinaryCollaboration/Product Fulfillment";

    static String documentId=""+new Date().getTime();

    static WorkflowClient wfClient;

    static IDataFilter tmpFilter=null;

    static Collection processInstanceList=null;

    public WfBpssManagmentTestCase(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(WfBpssManagmentTestCase.class);
    }

    public void setUp()
    {
        try
        {
            wfClient=new WorkflowClient(ServiceLookup.CLIENT_CONTEXT);
            assertNotNull("Unable to create WorkflowManager", wfClient.getWorkflowManager());
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

    public void testStartWorkflow() throws Exception
    {
        removeTempFiles();
        String requestDocObject="This PO123 request Document";
        wfClient.insertDocument(documentId,"PO123",requestDocObject,null,IBpssConstants.PARTNER_CONSTANT,"COMPANY-B");
    }

    public void testSleepUntilCompletes() throws Exception
    {
        Thread.currentThread().sleep(20 * 1000);
    }

    public void testGetProcessInstanceList() throws Exception {
                                                    http://BPSS/BpssSendReceiveCollaboration/1.14/[1234-5678-901239]/BpssBinaryCollaboration/Product Fulfillment";
        System.out.println("Getting processInstanceList form definiton size="+wfClient.getBpssProcessInstanceList("Product Fulfillment","BpssBinaryCollaboration","BpssSendReceiveCollaboration","1.14","[1234-5678-901239]"));

        // test 1  get by processtype and engine type
        tmpFilter = new DataFilterImpl();
        tmpFilter.addSingleFilter(null,GWFRtProcess.PROCESS_TYPE,tmpFilter.getEqualOperator(),"BpssBinaryCollaboration",false);
        tmpFilter.addSingleFilter(tmpFilter.getAndConnector(),GWFRtProcess.ENGINE_TYPE,tmpFilter.getEqualOperator(),"BPSS",false);
        processInstanceList=wfClient.getProcessInstanceList(tmpFilter);
        assertNotNull("processInstanceList is null",processInstanceList);
        System.out.println("Test 1,processInstanceList size="+processInstanceList.size());

        //test 2
        tmpFilter = new DataFilterImpl();
        tmpFilter.addSingleFilter(null,GWFRtProcess.STATE,tmpFilter.getEqualOperator(),new Integer(GWFRtProcess.OPEN_RUNNING),false);
        tmpFilter.addSingleFilter(tmpFilter.getAndConnector(),GWFRtProcess.PROCESS_TYPE,tmpFilter.getEqualOperator(),"BpssBinaryCollaboration",false);
        tmpFilter.addSingleFilter(tmpFilter.getAndConnector(),GWFRtProcess.PARENT_RTACTIVITY_UID,tmpFilter.getEqualOperator(),null,false);
        processInstanceList=wfClient.getProcessInstanceList(tmpFilter);

        System.out.println("testGetProcessInstanceList processInstanceList ="+processInstanceList);
        assertNotNull("Unable to get processInstanceList ",processInstanceList);
        System.out.println("Success testGetProcessInstanceList");
    }

    public void testCancelProcessInstance() throws Exception {
        assertTrue("testCancelProcessInstance No active running processes ",processInstanceList!=null && processInstanceList.size()>0);
        for(Iterator iterator=processInstanceList.iterator();iterator.hasNext();){
            GWFRtProcess rtProcess=(GWFRtProcess)iterator.next();
            Collection rtProcessDocColl=wfClient.getRtProcessDocList((Long)rtProcess.getKey());
            assertTrue("Not able get rtProcessDoc  ",rtProcessDocColl!=null && rtProcessDocColl.size()>0);
            System.out.println(" rtProcessDocColl ="+rtProcessDocColl);
            wfClient.cancelProcessInstance((Long)rtProcess.getKey(),"Testing");
        }
        Thread.currentThread().sleep(40*1000);

        Collection newProcessInstanceList=wfClient.getProcessInstanceList(tmpFilter);
        System.out.println(" newProcessInstanceList ="+newProcessInstanceList);
        assertTrue("Not able to cancel the processInstance ",newProcessInstanceList==null || newProcessInstanceList.size()==0);
        System.out.println("Success testCancelProcessInstance");
    }

    public void testExceptionSignal() throws Exception {
        File exFile=new File("d:/"+IBpssConstants.EXCEPTION_CANCEL+".tmp");
        exFile.delete();
        String responseDocObject="This POA123 Doc which is response to PO123 document";
        wfClient.insertDocument(documentId,"POA123",responseDocObject,"COMPANY-B",IBpssConstants.PARTNER_CONSTANT,"COMPANY-B");

        Thread.currentThread().sleep(30*1000);
        Properties prop=null;
        try{
            prop=new Properties();
            prop.load(new FileInputStream(exFile));
        }catch(Exception ex){
            prop=null;
        }
        assertTrue("Not able send Exception signal ",prop!=null && prop.size()>0);
        System.out.println("Success testExceptionSignal prop="+prop);
    }


    public void testRemoveProcessInstance() throws Exception {

        System.out.println("testRemoveProcessInstance processInstanceList ="+processInstanceList);
        List keyList=new ArrayList();
        for(Iterator iterator=processInstanceList.iterator();iterator.hasNext();){
            GWFRtProcess rtProcess=(GWFRtProcess)iterator.next();
            keyList.add(rtProcess.getKey());
            wfClient.removeProcessInstance((Long)rtProcess.getKey());
        }

        IDataFilter filter = new DataFilterImpl();
        filter.addDomainFilter(null,GWFRtProcess.UID,keyList,false);
        Collection afterProcessInstanceList=wfClient.getProcessInstanceList(filter);
        System.out.println(" afterProcessInstanceList ="+afterProcessInstanceList);
        assertTrue("Not able to remove the processInstance ",afterProcessInstanceList==null || afterProcessInstanceList.size()==0);
        System.out.println("Success testRemoveProcessInstance");
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

    public static void main(String args[]) throws Exception
    {
        pindex=0;
        junit.textui.TestRunner.run(suite());
    }
}
