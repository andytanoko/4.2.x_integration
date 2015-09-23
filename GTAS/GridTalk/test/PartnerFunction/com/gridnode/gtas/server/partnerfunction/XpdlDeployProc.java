package com.gridnode.gtas.server.partnerfunction;

import com.gridnode.gtas.server.partnerfunction.helpers.Logger;

import com.gridnode.pdip.app.deploy.manager.ejb.IGWFDeployMgrObj;
import com.gridnode.pdip.app.deploy.manager.ejb.IGWFDeployMgrHome;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

import java.io.File;

public class XpdlDeployProc
{
  public void deployXpdl(String xpdlFile) throws Exception
  {
    File f = new File(xpdlFile);
    getDeployManager().deployXpdl(f);
  }

  public void undeployXpdl(String xpdlFile) throws Exception
  {
    File f = new File(xpdlFile);
    getDeployManager().undeployXpdl(f);
  }

  public static IGWFDeployMgrObj getDeployManager()
    throws ServiceLookupException
  {
    return (IGWFDeployMgrObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGWFDeployMgrHome.class.getName(),
      IGWFDeployMgrHome.class,
      new Object[0]);
  }

  public static void main(String[] args)
  {
    XpdlDeployProc proc = new XpdlDeployProc();

    try
    {
      proc.undeployXpdl("gtas/data/sys/workflow/xpdl/Alert.xpdl");
      proc.deployXpdl("gtas/data/sys/workflow/xpdl/Alert.xpdl");
    }
    catch (Throwable t)
    {
      Logger.err("[XpdpDeployProc.main] Error ", t);
    }

  }

}
