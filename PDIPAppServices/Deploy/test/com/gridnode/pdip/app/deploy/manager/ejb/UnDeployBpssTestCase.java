package com.gridnode.pdip.app.deploy.manager.ejb;

import com.gridnode.pdip.app.deploy.manager.bpss.*;
import com.gridnode.pdip.framework.j2ee.*;

import fixture.*;

import java.io.*;

import junit.framework.*;



public class UnDeployBpssTestCase extends TestCase {

    String bpssFileNames[] = {"Test001-BpssBinaryCollaboration.xml"};
    String xpdlFileNames[] = {"Test001-XpdlCreateRequestDocument.xml","Test001-XpdlCreateResponseDocument.xml"};

    public static IGWFDeployMgrHome home;
    public static IGWFDeployMgrObj remote;

    public UnDeployBpssTestCase(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(UnDeployBpssTestCase.class);
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

    public void setFileNames(String[] fileNames)
    {
        bpssFileNames=fileNames;
    }

    public void testUnDeployBpss() throws Exception{
        for(int i=0;i<bpssFileNames.length;i++){
            File f = new File("./data/"+bpssFileNames[i]);
            assertTrue("File does not exist "+f.getAbsolutePath(),f.exists());
            remote.undeployBpss(f);
            //remote.undeployBpss("[1234-5678-901239]","1.14");
        }
    }

    public void testUnDeployXpdlDepn() throws Exception{
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
