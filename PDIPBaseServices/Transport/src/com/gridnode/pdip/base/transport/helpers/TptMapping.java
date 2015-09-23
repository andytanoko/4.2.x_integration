package com.gridnode.pdip.base.transport.helpers;

/**
 * <p>Title: PDIP : Peer Data Interchange Platform</p>
 * <p>Description: Transport Module - for PDIP GT(AS)</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd - Singapore</p>
 * @author unascribed
 * @version 1.0
 */

import java.util.Map;
import java.util.Set;

import java.util.Iterator;
import java.util.HashMap;

public class TptMapping
{

  private Map _messageToTptMapping = new HashMap();

  public TptMapping()
  {
  }

  public void setMappedHeader(Map header)
  {
    _messageToTptMapping = header;
  }

  public void setMessageTptHeader(String msgHeader,String tptHeader)
  {
    _messageToTptMapping.put(msgHeader,tptHeader); //set MessageHeader Key - Value TptHeader
  }

  public String getTptHeader(String messageHeader)
  {
    return (String)_messageToTptMapping.get(messageHeader);
  }

  public  String getPackgingHeader(String packHeader)
  {
    if (_messageToTptMapping.containsValue(packHeader))
    {
      Set pairEntries = _messageToTptMapping.entrySet();
      Iterator pairIterator =  pairEntries.iterator();
      while (pairIterator.hasNext())
      {
              Map.Entry entry = (Map.Entry)pairIterator.next();
              if (((String)entry.getValue()).equals(packHeader))
                return (String)entry.getKey();
      }
    }
    return null;
  }

  /**
   * This method returns a Map of TransportHeader Key to Value Mapping.
   *
   * A Mapping file defines Message to Tpt Header Key Mapping. Other headers which
   * are not a part of Mapping, are retainted as it is.
   *
   * @param header
   * @return
   */
  public Map getOutBoundMappedHeader(Map header)
  {
    if (null == header)
      return header;
    Map tptHeader = new HashMap();
    Map originalHeader = new HashMap(header);
    Iterator keyIte = header.keySet().iterator(); //Get MessageHeader Key set.
    while (keyIte.hasNext())
    {
      String messageKey = (String)keyIte.next();
      if (getTptHeader(messageKey) != null) //Get respective TptHeader Key if one exists
      {
        tptHeader.put(getTptHeader(messageKey),header.get(messageKey)); //Put TptHeader Key.
        originalHeader.remove(messageKey);
      }
    }
    tptHeader.putAll(originalHeader);
    return tptHeader;
  }


  /**
   * This method returns a Map of TransportHeader Key to Value Mapping.
   *
   * A Mapping file defines Message to Tpt Header Key Mapping. Other headers which
   * are not a part of Mapping, are retainted as it is.
   *
   * @param header
   * @return
   */

  public Map getInBoundMappedHeader(Map header)
  {
    if (null == header)
      return header;
    Map packHeader = new HashMap();
    Map originalHeader = new HashMap(header);
    Iterator keyIte = header.keySet().iterator();
    while (keyIte.hasNext())
    {
      String messageKey = (String)keyIte.next();
      if (getTptHeader(messageKey) != null)
      {
        packHeader.put(getPackgingHeader(messageKey),header.get(messageKey));
        originalHeader.remove(messageKey);
      }
    }
    packHeader.putAll(originalHeader);
    return packHeader;
  }

}