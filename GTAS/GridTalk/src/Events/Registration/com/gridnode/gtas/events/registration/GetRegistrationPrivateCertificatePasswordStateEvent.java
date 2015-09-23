package com.gridnode.gtas.events.registration;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * <p>Title: GTAS-UserManagement</p>
 * <p>Description: GTAS
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author unascribed
 * @version 1.0
 */

public class GetRegistrationPrivateCertificatePasswordStateEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3002505483946301544L;
	public static final String REGISTRATION_PRIVATE_PASSEWORD_STATE = "Registration Password State";
  public GetRegistrationPrivateCertificatePasswordStateEvent() throws EventException
  {
  }
  public String getEventName()
  {
    return "java:comp/env/param/event/GetRegistrationPrivateCertificatePasswordStateEvent";
  }
}