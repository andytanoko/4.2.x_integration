package com.gridnode.pdip.app.xmldb.facade.ejb;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import java.io.File;

import org.jdom.Document;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;

//The testcase uses a mapping file an xml file and a dtd. These files
//should be placed in the appropriate directories. The mapping file
//references the entities in base.DocService module. So DocService module
//must be deployed for the testcases to run

public class XMLDBServiceTest extends TestCase
{

    IXMLDBServiceObj xmldbObj;

    public XMLDBServiceTest(String str)
    {
        super(str);
    }

    protected void setUp() throws Exception
    {
        xmldbObj = getRemote();
    }

    protected void tearDown() throws Exception
    {
    }

    public static Test suite()
    {
        return new TestSuite(XMLDBServiceTest.class);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    private IXMLDBServiceObj getRemote() throws Exception
    {
        IXMLDBServiceHome xmldbHome =
            (IXMLDBServiceHome) ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
            IXMLDBServiceHome.class);
        assertNotNull("Home lookup returned null", xmldbHome);
        return xmldbHome.create();
    }

    public void testInsertDataFromXML() throws Exception
    {
        try
        {

            xmldbObj.insertDataFromXML("data/dtds/4A3-Forecast-ODC.xml",
              //"3A4_MS_V02_02_PurchaseOrderRequest.dtd",
            null,
                "forecast_xml_db_mapping.xml");
/*
            xmldbObj.insertDataFromXML("data/dtds/filesystem.xml",
              "FileSystem.dtd",
                "filesystem_mapping.xml");

           //madhan test


            xmldbObj.insertDataFromXML("data/dtds/4A3-Forecast-ODC.xml",
              //"3A4_MS_V02_02_PurchaseOrderRequest.dtd",
            null,
                "forecast_mapping.xml");

*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Exception occured: " + e);
        }
    }
/*
    public void testGenerateXML() throws Exception
    {
        try
        {
            File file = xmldbObj.generateXML("db-xml_mapping.xml", null, "dtdfile.dtd");
            assertNotNull("generateXMLFile returned null", file);
*/
            /*File f = new File("c:/result.xml");
            if (f.exists())
            {
                f.delete();
            }
            file.renameTo(f);*/
/*
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Exception occured: " + e);
        }
    }
*/
}