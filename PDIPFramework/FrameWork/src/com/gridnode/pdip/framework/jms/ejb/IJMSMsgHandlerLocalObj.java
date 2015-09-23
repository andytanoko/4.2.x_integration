package com.gridnode.pdip.framework.jms.ejb;

import java.io.Serializable;
import java.util.Hashtable;

import javax.ejb.EJBLocalObject;

import com.gridnode.pdip.framework.db.jms.JMSFailedMsg;

public interface IJMSMsgHandlerLocalObj extends EJBLocalObject
{
  public void processFailedJMS(JMSFailedMsg failedMsg) throws Exception;
  
  public void updateFailedJMSMsg(JMSFailedMsg failedMsg, int maxRetry) throws Exception;
  
  public JMSFailedMsg getNextFailedMsg(int maxRetry, Long processFailedMsg) throws Exception;
  
  public void sendMessage(Hashtable<String, String> jmsSendProps, String destName, Serializable msgObj, Hashtable msgProps) throws Exception;
  
  public void sendMessage(String configName, String destName, Serializable msgObj, Hashtable msgProps) throws Exception;
}
