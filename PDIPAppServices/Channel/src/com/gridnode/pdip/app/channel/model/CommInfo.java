/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 13 2002    Goh Kan Mun             Created
 * Jul 03 2002    Goh Kan Mun             Modified - Add Name, Description, IsDefaultTpt fields.
 *                                                 - Rename field names.
 * Jul 11 2002    Goh Kan Mun             Modified - Create new Hashtable at the init for protocolDetail.
 * Aug 27 2002    Neo Sok Lay             Additional methods for (de)serialization.
 * Oct 03 2002    Neo Sok Lay             Init boolean fields to false. Without
 *                                        this FMI cannot be found.
 * Oct 04 2002    Ang Meng Hua            Change partnerCategory from String to Short
 *                                        Added isPartner field
 * Nov 23 2002    Jagadeesh               Added: FieldId to represent URL of this CommInfo.
 * Jul 18 2003    Neo Sok Lay             Change EntityDescr.
 */
package com.gridnode.pdip.app.channel.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

public class CommInfo
  extends AbstractEntity
  implements ICommInfo //com.gridnode.pdip.base.transport.comminfo.ICommInfo
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 947966883431003938L;
	private String _description = null;
  //  private String _host = null;
  private boolean _isDefaultTpt = false;
  private String _name = null;
  //  private int _port = 0;
  //  private Hashtable _protocolDetail = new Hashtable();
  private String _protocolType = null;
  //  private String _protocolVersion = null;
  private String _refId = null;
  private String _tptImplVersion = null;
  private ArrayList _protocolDetailItems = new ArrayList();
  private boolean _isDisable = false;
  //private String _partnerCategory = null;
  protected Short _partnerCategory;
  protected boolean _isPartner = false;
  protected String _url = null;

  public CommInfo()
  {
  }

  public String getEntityDescr()
  {
    return new StringBuffer()
      .append(getName())
      .append('/')
      .append(getDescription())
      .toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ******************** Getters for attributes ***************************

  /*  public String getHost()
    {
      return _host;
    }
  
    public int getPort()
    {
      return _port;
    }
  */
  public String getProtocolType()
  {
    return _protocolType;
  }

  public String getTptImplVersion()
  {
    return _tptImplVersion;
  }

  /*  public String getProtocolVersion()
    {
      return _protocolVersion;
    }
  
    public Hashtable getProtocolDetail()
    {
      return _protocolDetail;
    }
  */
  public String getRefId()
  {
    return _refId;
  }

  public String getName()
  {
    return _name;
  }

  public String getDescription()
  {
    return _description;
  }

  public boolean isDefaultTpt()
  {
    return _isDefaultTpt;
  }

  public Collection getProtocolDetailItems()
  {
    return _protocolDetailItems;
  }

  public Short getPartnerCategory()
  {
    return _partnerCategory;
  }

  public boolean isDisable()
  {
    return _isDisable;
  }

  public boolean isPartner()
  {
    return _isPartner;
  }

  public String getURL()
  {
    return _url;
  }

  // ******************** Setters for attributes ***************************

  /* public void setHost(String host)
   {
     _host = host;
   }
  
   public void setPort(int port)
   {
     _port = port;
   }
  */

  public void setTptImplVersion(String tptImplVersion)
  {
    _tptImplVersion = tptImplVersion;
  }

  public void setProtocolType(String protocolType)
  {
    _protocolType = protocolType;
  }

  /*  public void setProtocolVersion(String protocolVersion)
    {
      _protocolVersion = protocolVersion;
    }
  
    public void setProtocolDetail(Hashtable protocolDetail)
    {
      _protocolDetail = protocolDetail;
    }
  */
  public void setRefId(String refId)
  {
    _refId = refId;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setIsDefaultTpt(boolean isDefaultTpt)
  {
    _isDefaultTpt = isDefaultTpt;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public void setPartnerCategory(Short partnerCategory)
  {
    _partnerCategory = partnerCategory;
  }

  public void setIsDisable(boolean isDisable)
  {
    _isDisable = isDisable;
  }

  public void setIsPartner(boolean isPartner)
  {
    _isPartner = isPartner;
  }

  public void setURL(String url)
  {
    _url = url;
  }

  public static boolean isValidURL(String url)
  {
    try
    {
      if (url != null)
      {
        return parseURL(url);
      }
      else
      {
        return false;
      }
    }
    catch (Exception ex)
    {
      return false;
    }
  }

  /*  public void preSerialize()
    {
      //convert Hashtable protocolDetail to KeyValuePair(s)
      _protocolDetailItems.clear();
      Object[] keys = _protocolDetail.keySet().toArray();
      for (int i=0; i<keys.length; i++)
      {
        KeyValuePair pair = new KeyValuePair(keys[i], _protocolDetail.get(keys[i]));
        _protocolDetailItems.add(pair);
      }
  
    }
  
    public void postSerialize()
    {
      //clear the KeyValuePair(s)
      _protocolDetailItems.clear();
    }
  
    public void postDeserialize()
    {
      //convert KeyValuePair(s) to Hashtable protocolDetail
      KeyValuePair[] pairs = (KeyValuePair[])_protocolDetailItems.toArray(
                               new KeyValuePair[_protocolDetailItems.size()]);
      _protocolDetail.clear();
      for (int i=0; i< pairs.length; i++)
      {
        _protocolDetail.put(pairs[i].getKey(), pairs[i].getValue());
      }
      _protocolDetailItems.clear();
    }
  
  */
  /**
   * Parse the URL.
   * URL:Format: <<host>>://username:password@host:port
   * @param url
   * jms://user@www.localhost.com:443/destinationtype=Topic?destination=testtopic
   */

  private static boolean parseURL(String url1) throws Exception
  {
    try
    {
      String protocol = null;
      String username = null;
      String password = null;
      String passWordHost = null;
      String host = null;
      int port = -1;

      int i = url1.indexOf("@");
      String purl = url1.substring(url1.lastIndexOf(":"));
      int pos = purl.indexOf("/");
      String url = url1.substring(0, url1.lastIndexOf(":") + pos);
      System.out.println(url);
      StringTokenizer stk = new StringTokenizer(url, ":");

      if (i > 0) //UserName:
      {
        protocol = stk.nextToken();
        String token = stk.nextToken();

        int k = token.indexOf("@");
        if (k > 0)
        {
          username = token.substring(0, k);
          passWordHost = token.substring(k + 1);
        }
        else
        {
          username = token.substring(token.indexOf("/"));
          passWordHost = stk.nextToken();
        }
        port = Integer.parseInt(stk.nextToken());
      }
      else
      {
        protocol = stk.nextToken();
        host = stk.nextToken();
        port = Integer.parseInt(stk.nextToken());
        host = host.substring(2);
      }
      //Getting Password And Host
      if (passWordHost != null)
      {
        int x = passWordHost.indexOf('@');
        password = x < 0 ? "" : passWordHost.substring(0, x);
        host = passWordHost.substring(x + 1);

        //Processing for userName
        int p = username.indexOf('/');
        username = p < 0 ? null : username.substring(p + 2);
      }
      return true;
    }
    catch (Exception ex)
    {
      throw new Exception("Invalid URL Format ");
    }
  }

}