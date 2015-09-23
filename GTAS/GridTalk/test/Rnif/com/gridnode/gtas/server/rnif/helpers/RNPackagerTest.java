package com.gridnode.gtas.server.rnif.helpers;
import com.gridnode.pdip.base.rnif.helper.RNDePackager;
import com.gridnode.pdip.base.rnif.helper.RNDePackager_20;
import com.gridnode.pdip.base.rnif.helper.RNPackager;
import com.gridnode.pdip.base.rnif.helper.RNPackager_20;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;

import java.io.File;

import junit.framework.TestCase;

public class RNPackagerTest extends  RnifTestCase
{

  RNPackager_20 _packager = new RNPackager_20();
  RNDePackager_20 _depackager = new RNDePackager_20();
  public RNPackagerTest(String name)
  {
    super(name);
  }

  public void testPackage() throws Throwable
  {
    try
    {
      File packinfoFile = new File("testdata/rnif/3A4/initiator/3A4Request_Packinfo.xml").getAbsoluteFile();
      File udoc = new File("testdata/rnif/3A4/3A4Request.xml").getAbsoluteFile();
      File[]  files = new File[] { packinfoFile, udoc };
      RNPackInfo[]   packArray  = new RNPackInfo[]{null};
      
      _packager.packDoc(null, files);
      
//      _rnifMgr.invokeMethod(
//        _packager,
//        "com.gridnode.pdip.base.rnif.helper.RNPackager",
//        "packDoc",
//        new Class[] {files.getClass(), packArray.getClass() },
//        new Object[] {files, null});
 //     File[] res = _packager.packDoc(, null);
    } catch (Throwable throwable)
    {
      Logger.err(throwable);
      throw throwable;
    }
  }

  public void testDePackage() throws Throwable
  {
    try
    {
      File auditudoc = new File("testdata/rnif/3A4/3A4Request_Audit.xml").getAbsoluteFile();
     File  files =  auditudoc;
       
    File[] res  =(File[]) _rnifMgr.invokeMethod(
        _depackager,
        "com.gridnode.pdip.base.rnif.helper.RNDePackager",
        "unpackDocument",
        new Class[] { files.getClass(), RNPackInfo.class },
        new Object[] {files, null});
 //     File[] res = _depackager.unpackDocument(auditudoc , null);

   Logger.debug("file after unpackage is "+ res[0] + ";" + res[1]);
    } catch (Throwable throwable)
    {
      Logger.err(throwable);
      throw throwable;
    }
  }

}
