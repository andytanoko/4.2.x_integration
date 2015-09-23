package com.gridnode.pdip.base.rnif.helper;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;

import java.io.File;

import junit.framework.TestCase;

public class RNPackagerTest extends TestCase
{

  RNPackager _packager = new RNPackager_20();
  RNDePackager _depackager = new RNDePackager_20();
  public RNPackagerTest(String name)
  {
    super(name);
  }

  protected void setUp()
  {}

  protected void tearDown()
  {}


  public void testPackInfoReadWrite() throws Throwable
  {
    try
    {
      File packinfoFile = new File("testdata/rnif/packinfo.xml");
      RNPackInfo packInfo = new RNPackInfo();
      packInfo = (RNPackInfo) packInfo.deserialize(packinfoFile.getAbsolutePath());
      Logger.debug("actionIdentityGlobalBusActionCode is:" + packInfo.getActionIdentityGlobalBusActionCode() );
      String oldGBizActionCode = packInfo.getActionIdentityGlobalBusActionCode();
      
      String resultFileName = "testdata/rnif/packinfo_result.xml";
      packInfo.serialize(resultFileName);
      
      packInfo = (RNPackInfo) packInfo.deserialize(new File(resultFileName).getAbsolutePath());
      Logger.debug("actionIdentityGlobalBusActionCode is:" + packInfo.getActionIdentityGlobalBusActionCode() );
      String newGBizActionCode = packInfo.getActionIdentityGlobalBusActionCode();

    } catch (Throwable throwable)
    {
      Logger.err(throwable);
      throw throwable;
    }
  }


//  public void testPackage() throws Throwable
//  {
//    try
//    {
//      File packinfoFile = new File("testdata/rnif/packinfo.xml");
//      File udoc = new File("testdata/rnif/3A4udoc/3A4Request.xml");
//      File[] res = _packager.packDoc(new File[] { packinfoFile, udoc }, null);
//    } catch (Throwable throwable)
//    {
//      Logger.err(throwable);
//      throw throwable;
//    }
//  }
//
//  public void testDePackage() throws Throwable
//  {
//    try
//    {
//      File auditudoc = new File("testdata/rnif/3A4REQAudit.xml");
//      File[] res = _depackager.unpackDocument(auditudoc, null);
//    } catch (Throwable throwable)
//    {
//      Logger.err(throwable);
//      throw throwable;
//    }
//  }

}
