/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BpssGenerator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 30 2003    Koh Han Sing        Move from gtas layer to app layer
 * Sep 29 2005    Neo Sok Lay         Change due to new syntax for JDOM XMLOutputter.
 *                                    Need to consider using GNXML API next time.
 * Nov 10 2005    Neo Sok Lay         Use ServiceLocator instead of ServiceLookup
 * Dec 08 2005    Tam Wei Xiang       In order to compliance with bpss1.01.dtd,
 *                                    we will always append '_' in front of processName.
 *                                    EG.The attribute 'name' of ProcessSpecification is not allowed
 *                                    start with number since its type is ID.     
 *                                    Modified method getProcessName(), getBusinessDocument(boolean),
 *                                                    getBusinessActivity(boolean)                              
 */
package com.gridnode.pdip.app.rnif.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import org.jdom.Attribute;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.gridnode.pdip.app.deploy.manager.ejb.IGWFDeployMgrHome;
import com.gridnode.pdip.app.deploy.manager.ejb.IGWFDeployMgrObj;
import com.gridnode.pdip.app.deploy.manager.exceptions.DefinitionParserException;
import com.gridnode.pdip.app.deploy.manager.exceptions.DeploymentException;
import com.gridnode.pdip.app.rnif.exceptions.RnifBpssException;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.time.entities.value.iCalDurationV;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class BpssGenerator
{

  public static boolean isRequestDocNameRefId(String docRefId)
  {
    return docRefId.endsWith("_Request");
  }

  public static String getDefNameFromDocRefId(String docRefId)
  {		
     int index = docRefId.lastIndexOf("_");
     return docRefId.substring(1, index);
  }

  static String removeSpace(String original)
  {
    int len= original.length();
    int i= -1;
    char[] val= new char[len];
    original.getChars(0, len, val, 0);
    char[] buf= new char[len];
    int resLen= 0;

    while (++i < len)
    {
      if (val[i] == ' ' || val[i] == '\t' || val[i] == '\n')
      {
        continue;
      }
      buf[resLen++]= val[i];
    }
    if (resLen < len)
      return new String(buf, 0, resLen);
    return original;
  }


  static String bool2String(Boolean value)
  {
    if (value == null)
      return "false";
    return value.booleanValue() ? "true" : "false";
  }

  static String seconds2Str(Integer time)
  {
    if (time == null)
      return null;
    iCalDurationV dur= new iCalDurationV(time);
    return dur.toString();
  }

  ProcessDef _def;
  public BpssGenerator(ProcessDef def)
  {
    _def= def;
  }

  public String getProcessName()
  {
    return "_"+_def.getDefName();
  }

  public String getUuid()
  {
 //   return _def.getGProcessIndicatorCode();
     return _def.getDefName();
  }

  public String getVersion()
  {
    return _def.getVersionIdentifier();
  }

  public String getTransactionName()
  {
    return _def.getRequestAct().getBizActivityIdentifier();
  }

  public String getTransactionNameRefId()
  {
    return removeSpace(getTransactionName()) + "_BT";
  }

  public String getCollaborationName()
  {
    return _def.getRequestAct().getBizActivityIdentifier();
  }

  public String getCollaborationNameRefId()
  {
    return removeSpace(getTransactionName()) + "_BC";
  }

  public String getBizActionName(ProcessAct act)
  {
    return act.getGBizActionCode();
  }

  public String getBizActionNameRefId(ProcessAct act)
  {
    return removeSpace(getBizActionName(act));
  }

  public String getDocName(boolean isRequest)
  {
    return isRequest ? "REQUESTDOC" : "RESPONSEDOC";
  }

  public String getDocNameRefId(boolean isRequest)
  {
    return getProcessName() + (isRequest ? "_Request" : "_Response");
  }

  public String getInitiatingRoleName()
  {
    return _def.getFromPartnerRoleClassCode();
  }

  public String getInitiatingRoleNameRefId()
  {
    return removeSpace(getInitiatingRoleName());
  }

  public String getRespondingRoleName()
  {
    return _def.getGToPartnerRoleClassCode();
  }

  public String getRespondingRoleNameRefId()
  {
    return removeSpace(getRespondingRoleName());
  }

  public String getTxActivityName()
  {
    return _def.getRequestAct().getBizActivityIdentifier();
  }

  public String getTxActivityNameRefId()
  {
    return removeSpace(getTxActivityName()) + "_BTA";
  }

  String getDocSpecLocation(boolean isRequest) throws RnifBpssException
  {
    ProcessAct act= isRequest ? _def.getRequestAct() : _def.getResponseAct();

    if (act == null)
      throw RnifBpssException.processdefNotFoundEx("ProcessAct of " + _def.getEntityDescr() + " should not be null! "+ "isRequest=" + isRequest, new Exception());
    try
    {
      return  ProcessUtil.getMappingFileName(act.getMsgType());
    }
    catch(Exception ex)
    {
      throw RnifBpssException.fileProcessErr("Error in get ProcessMsgType's actual filename", ex);
    }
  }

  Element getBusinessActivity(boolean isRequest)
  {
    String eleName= isRequest ? "RequestingBusinessActivity" : "RespondingBusinessActivity";
    Element bizAction= new Element(eleName);

    ProcessAct act= isRequest ? _def.getRequestAct() : _def.getResponseAct();

    if (act == null)
      return bizAction;
    if(ProcessDef.TYPE_SINGLE_ACTION.equalsIgnoreCase(_def.getProcessType())  && !isRequest)
      return bizAction;

    ArrayList attriList= new ArrayList();
    attriList.add(new Attribute("name", getBizActionName(act)));
    attriList.add(new Attribute("nameID", getBizActionNameRefId(act)));
    attriList.add(
      new Attribute("isAuthorizationRequired", bool2String(act.getIsAuthorizationRequired())));
    String str= bool2String(act.getIsNonRepudiationRequired());
    attriList.add(new Attribute("isNonRepudiationRequired", str));
    attriList.add(new Attribute("isNonRepudiationReceiptRequired", str));
    Integer time= act.getTimeToAcknowledge();
    if (!Boolean.TRUE.equals(_def.getIsSynchronous()))
    {//asynchronous
      if(time == null || time.intValue() <=0)
        time = new Integer(24*3600*100);
      attriList.add(new Attribute("timeToAcknowledgeReceipt", seconds2Str(time)));
    }else
    { 
      if(ProcessDef.TYPE_SINGLE_ACTION.equals( _def.getProcessType()) )
       if(time != null && time.intValue() >0)
        attriList.add(new Attribute("timeToAcknowledgeReceipt", seconds2Str(time)));
    }

    bizAction.setAttributes(attriList);

    Element docEnvelope= new Element("DocumentEnvelope");
    attriList= new ArrayList();
    attriList.add(new Attribute("businessDocument", getDocName(isRequest)));
    attriList.add(new Attribute("businessDocumentIDRef", getDocNameRefId(isRequest)));
    attriList.add(new Attribute("isPositiveResponse", "true"));
    docEnvelope.setAttributes(attriList);

    bizAction.addContent(docEnvelope);

    return bizAction;
  }

  Element getBusinessDocument(boolean isRequest) throws RnifBpssException
  {
    Element doc= new Element("BusinessDocument");
    ArrayList attriList= new ArrayList();
    attriList.add(new Attribute("name", getDocName(isRequest)));
    attriList.add(new Attribute("nameID", getDocNameRefId(isRequest)));
    String specLocation = getDocSpecLocation(isRequest);
    if(specLocation != null)
       attriList.add(new Attribute("specificationLocation", specLocation));

    doc.setAttributes(attriList);
    return doc;
  }

  public void createBPSS() throws RnifBpssException
  {
    Document doc= gen();
    deployToBPSS(doc);
  }

  //@@todo wait for merge
  public void deleteBPSS() throws RnifBpssException
  {
    try
    {
      getDeployMgr().undeployBpss(getUuid(), getVersion());
      Logger.debug("Process " + getUuid()+ "_" + getVersion() + " is successfully undeployed!" );
    }
    catch (ServiceLookupException ex)
    {
      throw RnifBpssException.bpssUndeployErr("Cannot find Deploy Manager", ex);
    }
    catch (RemoteException ex)
    {
      throw RnifBpssException.bpssUndeployErr("", ex);
    }
    catch (DeploymentException ex)
    {
      throw RnifBpssException.bpssUndeployErr("", ex);
    }
    catch (Throwable ex)
    {
      throw RnifBpssException.bpssUndeployErr("Unknow error", ex);
    }
  }

  public Document gen() throws RnifBpssException
  {
    boolean isTwoAction = (_def.getResponseAct() != null);

    Element root= new Element("ProcessSpecification");
    ArrayList attriList= new ArrayList();
    attriList.add(new Attribute("name", getProcessName()));
    attriList.add(new Attribute("uuid", getUuid()));
    attriList.add(new Attribute("version", getVersion()));
    root.setAttributes(attriList);

    String str=
      "ebXML BPSS Description generated from processdef "
        + _def.getDefName()
        + " at "
        + (new Date());
    Element documentation= new Element("Documentation");
    documentation.setText(str);
    root.addContent(documentation);

    String txName= getTransactionName();
    String txNameRefId= getTransactionNameRefId();

    Element transaction= new Element("BusinessTransaction");
    attriList= new ArrayList();
    attriList.add(new Attribute("name", txName));
    attriList.add(new Attribute("nameID", txNameRefId));
    transaction.setAttributes(attriList);
    transaction.addContent(getBusinessActivity(true));
    transaction.addContent(getBusinessActivity(false));
    root.addContent(transaction);

    Element collaboration= new Element("BinaryCollaboration");
    attriList= new ArrayList();
    attriList.add(new Attribute("name", getCollaborationName()));
    attriList.add(new Attribute("nameID", getCollaborationNameRefId()));
    collaboration.setAttributes(attriList);

    String initRoleName= getInitiatingRoleName();
    String initRoleNameRefId= getInitiatingRoleNameRefId();
    Element initRole= new Element("InitiatingRole");
    attriList= new ArrayList();
    attriList.add(new Attribute("name", initRoleName));
    attriList.add(new Attribute("nameID", initRoleNameRefId));
    initRole.setAttributes(attriList);
    collaboration.addContent(initRole);

    String respRoleName= getRespondingRoleName();
    String respRoleNameRefId= getRespondingRoleNameRefId();
    Element respRole= new Element("RespondingRole");
    attriList= new ArrayList();
    attriList.add(new Attribute("name", respRoleName));
    attriList.add(new Attribute("nameID", respRoleNameRefId));
    respRole.setAttributes(attriList);
    collaboration.addContent(respRole);

    String activityName= getTxActivityName();
    String activityNameId= getTxActivityNameRefId();
    Element txActivity= new Element("BusinessTransactionActivity");
    attriList= new ArrayList();
    attriList.add(new Attribute("name", activityName));
    attriList.add(new Attribute("nameID", activityNameId));
    attriList.add(new Attribute("businessTransaction", txName));
    attriList.add(new Attribute("businessTransactionIDRef", txNameRefId));
    attriList.add(new Attribute("fromAuthorizedRole", initRoleName));
    attriList.add(new Attribute("fromAuthorizedRoleIDRef", initRoleNameRefId));
    attriList.add(new Attribute("toAuthorizedRole", respRoleName));
    attriList.add(new Attribute("toAuthorizedRoleIDRef", respRoleNameRefId));
    Integer time= _def.getActionTimeOut();
    if (time != null)
      attriList.add(new Attribute("timeToPerform", seconds2Str(time)));
    txActivity.setAttributes(attriList);
    collaboration.addContent(txActivity);

    Element start= new Element("Start");
    attriList= new ArrayList();
    attriList.add(new Attribute("toBusinessState", activityName));
    attriList.add(new Attribute("toBusinessStateIDRef", activityNameId));
    start.setAttributes(attriList);
    collaboration.addContent(start);

    Element success= new Element("Success");
    attriList= new ArrayList();
    attriList.add(new Attribute("conditionGuard", "Success"));
    attriList.add(new Attribute("fromBusinessState", activityName));
    attriList.add(new Attribute("fromBusinessStateIDRef", activityNameId));
    success.setAttributes(attriList);
    collaboration.addContent(success);

    Element failure= new Element("Failure");
    attriList= new ArrayList();
    attriList.add(new Attribute("conditionGuard", "BusinessFailure"));
    attriList.add(new Attribute("fromBusinessState", activityName));
    attriList.add(new Attribute("fromBusinessStateIDRef", activityNameId));
    failure.setAttributes(attriList);
    collaboration.addContent(failure);

    Element tecfailure= new Element("Failure");
    attriList= new ArrayList();
    attriList.add(new Attribute("conditionGuard", "TechnicalFailure"));
    attriList.add(new Attribute("fromBusinessState", activityName));
    attriList.add(new Attribute("fromBusinessStateIDRef", activityNameId));
    tecfailure.setAttributes(attriList);
    collaboration.addContent(tecfailure);

    root.addContent(collaboration);

    root.addContent(getBusinessDocument(true));
    if (isTwoAction)
    {
      Element responseDoc= getBusinessDocument(false);
      root.addContent(responseDoc);
    }

    Document doc= new Document(root, new DocType("ProcessSpecification", IRnifConstant.BPSS_MSG_TYPE));
    return doc;
  }

  void deployToBPSS(Document xmlDoc) throws RnifBpssException
  {
    try
    {
      ByteArrayOutputStream byteOS= new ByteArrayOutputStream();
      //NSL20050929 New syntax for XMLOutputter
      Format format = Format.getPrettyFormat();
      XMLOutputter xmlout= new XMLOutputter(format);
      //xmlout.setNewlines(true);
      //xmlout.setIndent(true);
      xmlout.output(xmlDoc, byteOS);
      File bpssFile= File.createTempFile(_def.getDefName(), ".bpss");
      FileOutputStream fos= new FileOutputStream(bpssFile);
      byteOS.writeTo(fos);
      fos.close();
      Logger.debug("BPSS file " + bpssFile.getAbsolutePath() + " is gened!");
      getDeployMgr().deployBpss(bpssFile);
      Logger.debug("BPSS file " + bpssFile.getAbsolutePath() + " is successfully deployed!");
      try
      {
        bpssFile.delete();
      }catch(Throwable t)
      {
        Logger.warn("[BPSSGenerator::deployToBPSS()]Error in delete bpssFile", t);
      }
    }
    catch (IOException e)
    {
      throw RnifBpssException.bpssDeployErr("BPSS document write IOExeption", e);
    }
    catch (DefinitionParserException ex)
    {
      throw RnifBpssException.bpssDeployErr("BPSS File Parse Excpetion", ex);
    }
    catch (DeploymentException ex)
    {
      throw RnifBpssException.bpssDeployErr("", ex);
    }
    catch (ServiceLookupException ex)
    {
      throw RnifBpssException.bpssDeployErr("Cannot find Deploy Manager", ex);
    }  }

  IGWFDeployMgrObj getDeployMgr() throws ServiceLookupException
  {
    try
    {
      //return ((IGWFDeployMgrHome) ServiceLookup.getInstance(ServiceLocator.CLIENT_CONTEXT).getHome(
      //  IGWFDeployMgrHome.class)).create();
    	return (IGWFDeployMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
    	          IGWFDeployMgrHome.class.getName(),
    	          IGWFDeployMgrHome.class,
    	          new Object[0]);
    }
    catch (Exception ex)
    {
      throw new ServiceLookupException("Error in lookup IGWFDeployMgrObj!", ex);
    }
  }
}
