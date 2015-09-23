package com.gridnode.pdip.app.deploy.manager.ejb;

import com.gridnode.pdip.app.deploy.manager.bpss.*;
import com.gridnode.pdip.framework.j2ee.*;

import fixture.*;

import java.io.*;

import junit.framework.*;



public class UnDeployXpdlTestCase extends TestCase {
    String xpdlFileNames[] = {"TestXpdlWorkflow_1.xml","TestXpdlWorkflow_2.xml","TestXpdlWorkflow_3.xml"};

    public static IGWFDeployMgrHome home;
    public static IGWFDeployMgrObj remote;

    public UnDeployXpdlTestCase(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(UnDeployXpdlTestCase.class);
    }

    public void setUp() {
        try
        {
          home = (IGWFDeployMgrHome)ServiceLookup.getInstance(
                                        ServiceLookup.CLIENT_CONTEXT).getHome(
                     IGWFDeployMgrHome.class);
          remote = home.create();
          assertNotNull("Session Object Not Null ", remote);
        }
        catch (Exception ex)
        {
          System.out.println(" Exception in SetUp  : " + ex.getMessage());
          ex.printStackTrace();
        }
    }

    public void tearDown() {

    }

    public void testUnXpdlDeploy() throws Exception{
        for(int i=0;i<xpdlFileNames.length;i++){
            File f = new File("./data/"+xpdlFileNames[i]);
            assertTrue("File does not exist "+f.getAbsolutePath(),f.exists());
            remote.undeployXpdl(f);
        }
    }

    public static void main(String args[]) throws Exception {
        junit.textui.TestRunner.run(suite());
    }

}
