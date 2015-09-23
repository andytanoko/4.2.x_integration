package com.gridnode.pdip.app.workflow.client;

import java.util.*;

import com.gridnode.pdip.base.contextdata.entities.model.*;
import com.gridnode.pdip.framework.j2ee.*;
import junit.framework.*;

public class WorkflowTestCase extends TestCase
{
    static int pindex=0;
    String xpdlFileNames[] = {"TestXpdlProcess.xml"};

    static WorkflowClient wfClient;
    static String PROCESS_DEF[]={"http://XPDL/MappingRules/8/XpdlProcess/MappingRules",
                                 "http://XPDL/Exits/1.0/XpdlProcess/Exits",
                                 "http://XPDL/PF01/1.0/XpdlProcess/PF01"};
    static Long contextUId[]=new Long[3];
    static String keys[]={"out.gdocs","out.gdocs","main.gdocs,fork1.gdocs,fork2.gdocs"};
    public WorkflowTestCase(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(WorkflowTestCase.class);
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
        pindex=3;
        if(pindex==0 ||pindex==1){
            HashMap ctxData=new HashMap();
            ctxData.put("uid","0");
            ctxData.put("in.gdocs",new ArrayList(Arrays.asList(PROCESS_DEF)));
            contextUId[0]=wfClient.createRtProcess(PROCESS_DEF[0],null,ctxData);
            assertNotNull("Failes starting XpdlProcess ",contextUId[0]);
        }

        if(pindex==0 ||pindex==2){
            //Thread.currentThread().sleep(1000);
            HashMap ctxData=new HashMap();
            ctxData.put("exit","ExitToOutbound");
            ctxData.put("in.gdocs",new ArrayList(Arrays.asList(PROCESS_DEF)));
            contextUId[1]=wfClient.createRtProcess(PROCESS_DEF[1],null,ctxData);
            assertNotNull("Failes starting XpdlProcess ",contextUId[1]);
        }

        if(pindex==0 ||pindex==3){
            //Thread.currentThread().sleep(1000);
            HashMap ctxData=new HashMap();
            ctxData.put("main.gdocs",new ArrayList(Arrays.asList(PROCESS_DEF)));
            contextUId[2]=wfClient.createRtProcess(PROCESS_DEF[2],null,ctxData);
            assertNotNull("Failes starting XpdlProcess ",contextUId[2]);
        }

    }

    public void testSleepUntilCompletes() throws Exception
    {
        if(pindex==0)
            Thread.currentThread().sleep(60* 1000);
        else Thread.currentThread().sleep(30 * 1000);
    }

    public void testCompleteWorkflow() throws Exception
    {   boolean b=false;

        for(int i=0;i<contextUId.length;i++){
            try{
                if(contextUId[i]!=null){
                    StringTokenizer strTok=new StringTokenizer(keys[i],",");
                    while(strTok.hasMoreTokens()){
                        String key=strTok.nextToken();
                        Object output=wfClient.getDataManager().getContextData(contextUId[i],new ContextKey(key));
                        assertNotNull("Failed to get output object ,key="+key,output);
                        System.out.println("output context data :"+key+"="+output);
                    }
                }
            }catch(Exception e){
                b=true;
                e.printStackTrace();
            }
        }
        assertTrue("WorkFlow failed ",!b);
    }

    public static void main(String args[]) throws Exception
    {
        pindex=0;
        junit.textui.TestRunner.run(suite());
    }
}
