package com.gridnode.gtas.server.rnif.helpers;



import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerHome;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import junit.framework.TestCase;


public class RnifTestCase extends TestCase
{
  protected IRnifManagerHome _rnifMgrHome= null;
  protected IRnifManagerObj _rnifMgr= null;
  public RnifTestCase(String arg0)
  {
    super(arg0);
  }

  public static IRnifManagerHome getRnifMgrHome() throws ServiceLookupException
  {
    return (IRnifManagerHome) ServiceLookup.getInstance(
      ServiceLookup.CLIENT_CONTEXT).getHome(
      IRnifManagerHome.class);
  }


  protected void setUp()
  {
    try
    {
      super.setUp();
      _rnifMgrHome= getRnifMgrHome();
      _rnifMgr = _rnifMgrHome.create();

    }
    catch (Exception ex)
    {
      Logger.err("Error setup", ex);
    }
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

}
