/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertRequestNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26 2003    Neo Sok Lay         Created
 * Apr 23 2003    Neo Sok Lay         To support notification targeted to
 *                                    different handlers. Change Category to
 *                                    Handler. Use Alert Type instead of Alert
 *                                    name.
 * Jun 06 2003    Neo Sok Lay         GNDB00014171: Clone the document object 
 *                                    during construction so that any changes
 *                                    to it by its owner class do not affect
 *                                    the alert notification.
 * Jun 26 2003    Neo Sok Lay         Add CleanUp option for document object.
 * May 27 2004    Neo Sok Lay         GNDB00024897: Allow USER_DEFINED alert 
 *                                    with no Document specified.
 */
package com.gridnode.gtas.server.notify;

import com.gridnode.pdip.framework.db.entity.IEntity;

import java.util.Collection;

/**
 * Notification request to raise an alert.<p>
 * Currently the following are contained in the notification:<p>
 * <pre>
 * Handler   - Alert, DocAlert
 *           - Can be used for message selection, e.g. handler='DocAlert'
 * AlertType - Type of Alert to raise
 *           - Can be used for message selection, e.g. alertType="DOCUMENT_RECEIVED"
 *           - See {@link IAlertTypes} for available alert types.
 * AlertUID  - The specific alert to raise.
 *           - For use with USER_DEFINED alert type.
 * Document  - The document object.
 *           - For use with DocAlert handler
 * Providers - Additional Data providers that is involved for the alert notification.
 * CleanUpDocument - Whether to let the Handle cleanup the document object after
 *                   the alert has been raised. Default is <b>false</b>.
 * </pre>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.4
 * @since 2.0 I7
 */
public class AlertRequestNotification
  extends    AbstractNotification
  implements IAlertTypes
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -746604545447749470L;
	public static final String HANDLER_ALERT     = "Alert";
  public static final String HANDLER_DOCALERT  = "DocAlert";

  private String _handler;
  private String _alertType;
  private Long   _alertUID;
  private Object _document;
  private Collection _providers;
  private boolean _cleanupDoc = false;

  /**
   * Construct a AlertRequestNotification object. Use for other types of alerts
   * other than document.
   *
   * @param alertType Type of the alert to raise.
   * @param providers Data providers. These should be of type IDataProvider.
   * @see IAlertTypes
   */
  public AlertRequestNotification(
    String alertType,
    Collection providers)
  {
    _handler = HANDLER_ALERT;
    _alertType = alertType;
    _providers = providers;
    putProperty("handler", _handler);
    putProperty("alertType", _alertType);
  }

  /**
   * Construct a AlertRequestNotification object for raise document alerts.
   *
   * @param alertType Type of the alert to raise.
   * @param document The document that the alert is for.
   * @param providers Data providers. These should be of type IDataProvider.
   * @see IAlertTypes
   */
  public AlertRequestNotification(
    String alertType,
    Object document,
    Collection providers)
  {
    _handler = HANDLER_DOCALERT;
    _alertType = alertType;
    _document = (document instanceof IEntity)? 
                (((IEntity)document).clone())  :
                document;
    _providers = providers;
    putProperty("handler", _handler);
    putProperty("alertType", _alertType);
  }

  public AlertRequestNotification(
    Long   alertUID,
    Object document,
    Collection providers)
  {
    this(USER_DEFINED, document, providers);
    _alertUID = alertUID;
  }

  public AlertRequestNotification(
    Long alertUID,
    Collection providers)
  {
    this(USER_DEFINED, providers);
    _alertUID = alertUID;
  }
  
  public String getHandler()
  {
    return _handler;
  }

  public String getAlertType()
  {
    return _alertType;
  }

  public Long getAlertUID()
  {
    return _alertUID;
  }

  public Object getDocument()
  {
    return _document;
  }

  public void setCleanUpDocument(boolean cleanup)
  {
    _cleanupDoc = cleanup;
  }
  
  public boolean isCleanUpDocument()
  {
    return _cleanupDoc;
  }
  
  public Collection getProviders()
  {
    return _providers;
  }

  public String getNotificationID()
  {
    return "AlertRequest";
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer(getNotificationID());
    buff.append(" - Handler[").append(getHandler()).append("]");
    buff.append(", AlertType[").append(getAlertType()).append("]");

    return buff.toString();
  }
}