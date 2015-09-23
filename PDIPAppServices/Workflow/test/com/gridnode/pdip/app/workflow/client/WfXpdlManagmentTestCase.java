package com.gridnode.pdip.app.workflow.client;

import java.util.*;

import com.gridnode.pdip.app.workflow.runtime.model.*;
//import com.gridnode.pdip.base.rolemap.entities.model.*;
//import com.gridnode.pdip.base.rolemap.facade.ejb.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.j2ee.*;
import junit.framework.*;


public class WfXpdlManagmentTestCase extends TestCase
{
    static int pindex=0;

    static String XPDL_PROCESS_DEF="http://XPDL/CreateReqDocPkg/Test001/XpdlProcess/CreateReqDocProcess";

    static String documentId=""+new Date().getTime();

    static WorkflowClient wfClient;

    static IDataFilter tmpFilter=null;

    static Collection processInstanceList=null;

    public WfXpdlManagmentTestCase(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(WfXpdlManagmentTestCase.class);
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
        HashMap xpdlContext=new HashMap();
        xpdlContext.put("documentId",documentId);
        xpdlContext.put("requestDocumentType","PO123");
        wfClient.createRtProcess(XPDL_PROCESS_DEF,null,xpdlContext);
    }

    public void testSleepUntilCompletes() throws Exception
    {
        Thread.currentThread().sleep(20 * 1000);
    }

    public void testGetProcessInstanceList() throws Exception {
        tmpFilter = new DataFilterImpl();
        tmpFilter.addSingleFilter(null,GWFRtProcess.STATE,tmpFilter.getEqualOperator(),new Integer(GWFRtProcess.OPEN_RUNNING),false);
        tmpFilter.addSingleFilter(tmpFilter.getAndConnector(),GWFRtProcess.PROCESS_TYPE,tmpFilter.getEqualOperator(),"XpdlProcess",false);
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
            wfClient.cancelProcessInstance((Long)rtProcess.getKey(),"Testing");
        }
        Thread.currentThread().sleep(40*1000);

        Collection newProcessInstanceList=wfClient.getProcessInstanceList(tmpFilter);
        System.out.println(" newProcessInstanceList ="+newProcessInstanceList);
        assertTrue("Not able to cancel the processInstance ",newProcessInstanceList==null || newProcessInstanceList.size()==0);
        System.out.println("Success testCancelProcessInstance");
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

    public static void main(String args[]) throws Exception
    {
        pindex=0;
        junit.textui.TestRunner.run(suite());
    }
}
