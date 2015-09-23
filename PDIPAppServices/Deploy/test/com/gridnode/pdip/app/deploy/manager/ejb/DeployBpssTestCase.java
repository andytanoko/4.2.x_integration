package com.gridnode.pdip.app.deploy.manager.ejb;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;

public class DeployBpssTestCase extends TestCase
{
    String bpssFileNames[] = {"Test001-BpssBinaryCollaboration.xml"};
    String xpdlFileNames[] = {"Test001-XpdlCreateRequestDocument.xml","Test001-XpdlCreateResponseDocument.xml"};

    public static IGWFDeployMgrHome home;
    public static IGWFDeployMgrObj remote;

    public DeployBpssTestCase(String name)
    {
        super(name);
    }


    public static Test suite()
    {
        return new TestSuite(DeployBpssTestCase.class);
    }

    public void setUp()
    {
        try
        {
            home = (IGWFDeployMgrHome) ServiceLookup.getInstance(
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

    public void tearDown()
    {

    }

    public void setFileNames(String[] fileNames)
    {
        bpssFileNames=fileNames;
    }

    public void testBpssDeploy() throws Exception
    {
        for (int i = 0; i < bpssFileNames.length; i++)
        {
            File f = new File("./data/" + bpssFileNames[i]);

            assertTrue("Bpss File does not exist " + f.getAbsolutePath(), f.exists());
            remote.deployBpss(f);
        }
    }

    public void testXpdlDepenDeploy() throws Exception
    {
        for (int i = 0; i < xpdlFileNames.length; i++)
        {
            File f = new File("./data/" + xpdlFileNames[i]);

            assertTrue("Xpdl File does not exist " + f.getAbsolutePath(), f.exists());
            remote.deployXpdl(f);
        }
    }

    public static void main(String args[]) throws Exception
    {
        junit.textui.TestRunner.run(suite());
    }
}
