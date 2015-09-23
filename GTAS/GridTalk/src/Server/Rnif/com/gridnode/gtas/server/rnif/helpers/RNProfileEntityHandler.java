package com.gridnode.gtas.server.rnif.helpers;



import com.gridnode.gtas.server.rnif.entities.ejb.IRNProfileLocalHome;
import com.gridnode.gtas.server.rnif.entities.ejb.IRNProfileLocalObj;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ProcessDef.
 */

public final class RNProfileEntityHandler
  extends          LocalEntityHandler
{
  private RNProfileEntityHandler()
  {
    super(RNProfile.ENTITY_NAME);
  }

  /**
   * Get an instance of a RNProfileEntityHandler.
   */
  public static RNProfileEntityHandler getInstance()
  {
    RNProfileEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(RNProfile.ENTITY_NAME, true))
    {
      handler = (RNProfileEntityHandler)EntityHandlerFactory.getHandlerFor(
                  RNProfile.ENTITY_NAME, true);
    }
    else
    {
      handler = new RNProfileEntityHandler();
      EntityHandlerFactory.putEntityHandler(RNProfile.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IRNProfileLocalHome.class.getName(),
      IRNProfileLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IRNProfileLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IRNProfileLocalObj.class;
  }

}