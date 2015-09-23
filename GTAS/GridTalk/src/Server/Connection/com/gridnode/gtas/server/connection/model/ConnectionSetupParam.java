/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupParam.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.model;

 
/**
 * This ConnectionSetupParam entity holds the connection setup parameters for
 * the GridTalk server to contact the GmPrime host for connection setup
 * configuration.<p>
 * This entity will be persistent as an Xml file instead of database table.<p>
 *
 * The data model:<PRE>
 * CurrentLocation         - The Country (code) that the GridTalk host is now
 *                           located.
 * ServicingRouter         - The IP address for the Jms router that serves the
 *                           Connection setup request.
 * OriginalLocation        - The original CurrentLocation used during first time
 *                           connection setup. This can be used to restore if
 *                           the user ever changes the CurrentLocation and now
 *                           wants back the original.
 * OriginalServicingRouter - The original ServicingRouter used during first time
 *                           connection setup. This can be used to restore if
 *                           the user ever changes the ServicingRouter and now
 *                           wants back the original.
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionSetupParam
  extends    AbstractXmlEntity
  implements IConnectionSetupParam
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5922621430201007952L;
	private String        _currentLocation;
  private String        _servicingRouter;
  private String        _originalLocation;
  private String        _originalServicingRouter;

  public ConnectionSetupParam()
  {
  }

  // ************** Methods from AbstractEntity ***************************

  public String getEntityDescr()
  {
    return "Connection Setup Param - location: "+getCurrentLocation() +
           ", Router: "+getServicingRouter();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return null;
  }

  // ***************** Getters & Setters *********************************

  public void setCurrentLocation(String currentLocation)
  {
    _currentLocation = currentLocation;
  }

  public String getCurrentLocation()
  {
    return _currentLocation;
  }

  public void setServicingRouter(String servicingRouter)
  {
    _servicingRouter = servicingRouter;
  }

  public String getServicingRouter()
  {
    return _servicingRouter;
  }

  public void setOriginalLocation(String originalLocation)
  {
    _originalLocation = originalLocation;
  }

  public String getOriginalLocation()
  {
    return _originalLocation;
  }

  public void setOriginalServicingRouter(String originalServicingRouter)
  {
    _originalServicingRouter = originalServicingRouter;
  }

  public String getOriginalServicingRouter()
  {
    return _originalServicingRouter;
  }
}