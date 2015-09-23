/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupResult.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import com.gridnode.pdip.framework.db.KeyValuePair;

/**
 * This ConnectionSetupResult entity holds the results of the latest Connection
 * setup for the GridTalk server host. <p>
 * This entity will be persistent as an Xml file instead of database table.<p>
 * The data model:<PRE>
 * Status               - The status of connection setup.
 * SetupParams          - The ConnectionSetupParam used to perform the latest
 *                        connection setup attempt.
 * FailureReason        - The reason for failure. Present if setup is unsuccessful.
 * AvailableGridMasters - Collection of GridMasters (GridNode UIDs) that are available
 *                        for connection. Present if setup is successful.
 * AvailableRouters     - Collection of JmsRouters (JmsRouter UIDs) that are available
 *                        for routing the messages. Present if setup is successful.
 * LocalRouterTopic     - Topic for Local Jms Router.
 * LocalRouterUser      - User for Local Jms Router authentication.
 * LocalRouterPassword  - Password for Local Jms Router authentication.
 * NetworkRouterTopic   - Topic for Network Jms Router.
 * NetworkRouterUser    - User for Network Jms Router authentication.
 * NetworkRouterPassword- Password for Network Jms Router authentication.
 * GridMasterTopics     - Hashtable of GridMaster NodeID vs Topic.
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionSetupResult
  extends    AbstractXmlEntity
  implements IConnectionSetupResult
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5440832125794117479L;
	private Short                 _status = new Short(STATUS_NOT_DONE);
  private ConnectionSetupParam  _setupParams;
  private String                _failureReason;
  private Collection            _availableGridMasters = new ArrayList();
  private Collection            _availableRouters     = new ArrayList();
  private Collection            _availableGridMasterUIDs = new ArrayList();
  private Collection            _availableRouterUIDs     = new ArrayList();
  private String                _localRouterTopic;
  private String                _localRouterUser;
  private String                _localRouterPassword;
  private String                _networkRouterTopic;
  private String                _networkRouterUser;
  private String                _networkRouterPassword;
  private Hashtable             _gmTopics     = new Hashtable();
  private ArrayList             _gmTopicItems = new ArrayList();

  public ConnectionSetupResult()
  {
  }

  // ************** Methods from AbstractEntity ***************************

  public String getEntityDescr()
  {
    return "Connection Setup Result - status: "+getStatus();
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

  public void setStatus(Short status)
  {
    _status = status;
  }

  public Short getStatus()
  {
    return _status;
  }

  public void setSetupParams(ConnectionSetupParam setupParams)
  {
    _setupParams = setupParams;
  }

  public ConnectionSetupParam getSetupParams()
  {
    return _setupParams;
  }

  public void setFailureReason(String failureReason)
  {
    _failureReason = failureReason;
  }

  public String getFailureReason()
  {
    return _failureReason;
  }

  public void setAvailableGridMastersUIDs(Collection availableGridMastersUIDs)
  {
    _availableGridMasterUIDs = availableGridMastersUIDs;
  }

  public Collection getAvailableGridMastersUIDs()
  {
    return _availableGridMasterUIDs;
  }

  public void setAvailableRouterUIDs(Collection availableRouterUIDs)
  {
    _availableRouterUIDs = availableRouterUIDs;
  }

  public Collection getAvailableRouterUIDs()
  {
    return _availableRouterUIDs;
  }

  public void setLocalRouterTopic(String topic)
  {
    _localRouterTopic = topic;
  }

  public String getLocalRouterTopic()
  {
    return _localRouterTopic;
  }

  public void setLocalRouterUser(String user)
  {
    _localRouterUser = user;
  }

  public String getLocalRouterUser()
  {
  return _localRouterUser;
  }

  public void setLocalRouterPassword(String password)
  {
    _localRouterPassword = password;
  }

  public String getLocalRouterPassword()
  {
  return _localRouterPassword;
  }

  public void setNetworkRouterTopic(String topic)
  {
    _networkRouterTopic = topic;
  }

  public String getNetworkRouterTopic()
  {
    return _networkRouterTopic;
  }

  public void setNetworkRouterUser(String user)
  {
    _networkRouterUser = user;
  }

  public String getNetworkRouterUser()
  {
  return _networkRouterUser;
  }

  public void setNetworkRouterPassword(String password)
  {
    _networkRouterPassword = password;
  }

  public String getNetworkRouterPassword()
  {
    return _networkRouterPassword;
  }

  public void setGridMasterTopics(Hashtable gmTopics)
  {
    _gmTopics = gmTopics;
  }

  public Hashtable getGridMasterTopics()
  {
    return _gmTopics;
  }

  public void addGridMasterTopic(String nodeID, String topic)
  {
    _gmTopics.put(nodeID, topic);
  }

  public String getGridMasterTopic(String nodeID)
  {
    return (String)_gmTopics.get(nodeID);
  }

  public Collection getGmTopicItems()
  {
    return _gmTopicItems;
  }

  public void preSerialize()
  {
    //convert Hashtable gmTopics to KeyValuePair(s)
    _gmTopicItems.clear();
    Object[] keys = _gmTopics.keySet().toArray();
    for (int i=0; i<keys.length; i++)
    {
      KeyValuePair pair = new KeyValuePair(keys[i], _gmTopics.get(keys[i]));
      _gmTopicItems.add(pair);
    }

  }

  public void postSerialize()
  {
    //clear the KeyValuePair(s)
    _gmTopicItems.clear();
  }

  public void postDeserialize()
  {
    //convert KeyValuePair(s) to Hashtable protocolDetail
    KeyValuePair[] pairs = (KeyValuePair[])_gmTopicItems.toArray(
                             new KeyValuePair[_gmTopicItems.size()]);
    _gmTopics.clear();
    for (int i=0; i< pairs.length; i++)
    {
      _gmTopics.put(pairs[i].getKey(), pairs[i].getValue());
    }
    _gmTopicItems.clear();
  }

}