/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupContext.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.setup;

import java.io.File;
import java.util.Hashtable;

import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;
import com.gridnode.pdip.app.channel.model.ChannelInfo;

/**
 * This object represents a Context that will be passed around the Delegates
 * of the Connection Setup process. The Context allows the Delegates to share
 * information that are set and required by the other steps of the process.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionSetupContext
{
  private static final Object _lock = new Object();
  private static ConnectionSetupContext _self = null;

  private Hashtable _delegates = new Hashtable();

  // inputs
  private Integer   _gridnodeID;
  private String    _productKey;
  private String    _countryCode;
  private File      _certReqFile;
  private Long      _encryptionCert;
  private int       _responseTimeout;
  private String    _servicingRouter;
  private ConnectionSetupResult _setupResult;
  private String    _masterCertName;
  private Long      _signCert;

  // output
  private String _secureTopic;
  private String _secureUser;
  private String _securePassword;
  private Object _secureCommInfo;
  private Object _publicCommInfo;
  private ChannelInfo _sendChannel;

  private ConnectionSetupContext()
  {
  }

  /**
   * Get the instance of the ConnectionSetupContext.
   */
  public static ConnectionSetupContext getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new ConnectionSetupContext();
      }
    }

    return _self;
  }

  /**
   * Clears the context. This method should be called for each
   * new initiation of the ConnectionSetup process.
   */
  public static void clearContext()
  {
    _self = null;
  }

  /**
   * Set the Delegate to handle acknowledgement of a particular event id.
   *
   * @param id The Event ID of the acknowledgement.
   * @param delegate The Delegate to handle the acknowledgement.
   */
  public void setDelegate(String id, IConnectionSetupDelegate delegate)
  {
    _delegates.put(id, delegate);
  }

  /**
   * Get the Delegate to handle acknowledgement of a particular event id.
   *
   * @param id the Event ID of the acknowledgement
   * @return the Delegate that handles the acknowledgement, or <b>null</b> if
   * none can handle. Once returned, the Delegate will be deregistered.
   */
  public IConnectionSetupDelegate getDelegate(String id)
  {
    return (IConnectionSetupDelegate)_delegates.remove(id);
  }

  public void setGridNodeID(Integer gridnodeID)
  {
    _gridnodeID = gridnodeID;
  }

  public Integer getGridNodeID()
  {
    return _gridnodeID;
  }

  public void setProductKey(String prodKey)
  {
    _productKey = prodKey;
  }

  public String getProductKey()
  {
    return _productKey;
  }

  public void setCountryCode(String code)
  {
    _countryCode = code;
  }

  public String getCountryCode()
  {
    return _countryCode;
  }

  public void setConnectionSetupResult(ConnectionSetupResult setupResult)
  {
    _setupResult = setupResult;
  }

  public ConnectionSetupResult getConnectionSetupResult()
  {
    return _setupResult;
  }

  public void setSecureTopic(String secureTopic)
  {
    _secureTopic = secureTopic;
  }

  public String getSecureTopic()
  {
    return _secureTopic;
  }

  public void setSecureUser(String secureUser)
  {
    _secureUser = secureUser;
  }

  public String getSecureUser()
  {
    return _secureUser;
  }

  public void setSecurePassword(String securePassword)
  {
    _securePassword = securePassword;
  }

  public String getSecurePassword()
  {
    return _securePassword;
  }

  public void setSecureCommInfo(Object secureCommInfo)
  {
    _secureCommInfo = secureCommInfo;
  }

  public Object getSecureCommInfo()
  {
    return _secureCommInfo;
  }

  public void setPublicCommInfo(Object publicCommInfo)
  {
    _publicCommInfo = publicCommInfo;
  }

  public Object getPublicCommInfo()
  {
    return _publicCommInfo;
  }

  public void setSendChannel(ChannelInfo sendChannel)
  {
    _sendChannel = sendChannel;
  }

  public ChannelInfo getSendChannel()
  {
    return _sendChannel;
  }

  public void setCertRequest(File certRequest)
  {
    _certReqFile = certRequest;
  }

  public File getCertRequest()
  {
    return _certReqFile;
  }

  public void setResponseTimeout(int responseTimeout)
  {
    _responseTimeout = responseTimeout;
  }

  public int getResponseTimeout()
  {
    return _responseTimeout;
  }

  public void setServicingRouter(String router)
  {
    _servicingRouter = router;
  }

  public String getServicingRouter()
  {
    return _servicingRouter;
  }

  public void setEncryptionCert(Long certUID)
  {
    _encryptionCert = certUID;
  }

  public Long getEncryptionCert()
  {
    return _encryptionCert;
  }

  public void setMasterCertName(String certName)
  {
    _masterCertName = certName;
  }

  public String getMasterCertName()
  {
    return _masterCertName;
  }

  public void setSignCert(Long signCertUID)
  {
    _signCert = signCertUID;
  }

  public Long getSignCert()
  {
    return _signCert;
  }
}