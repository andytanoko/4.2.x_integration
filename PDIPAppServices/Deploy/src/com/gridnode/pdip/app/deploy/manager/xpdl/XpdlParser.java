/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002   Mahesh	      Created
 * Jun 13 2002   Mathew         Repackaged
 */


package com.gridnode.pdip.app.deploy.manager.xpdl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.gridnode.pdip.app.deploy.manager.exceptions.DefinitionParserException;
import com.gridnode.pdip.app.deploy.manager.util.Logger;
import com.gridnode.pdip.app.deploy.manager.util.XmlEntityResolver;
import com.gridnode.pdip.base.gwfbase.xpdl.helpers.XpdlConstants;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.util.UtilString;
import com.gridnode.pdip.framework.util.UtilXml;

/**
 * This class is used to parse the xpdl definition file
 */
public class XpdlParser
{

  protected List values = null;
  protected List tempParticipantList = null;
  public static final String xpdlDtdName = "xpdl.dtd";
  private static EntityResolver xpdlEntityResolver;
  /**
   * Creates a new XpdlParser object.
   */
  public XpdlParser()
  {
  }


  /**
     * Gets an XML file from the specified location and reads it into
     * Entity objects and returns them in a
     * List; does not write to the database, just gets the entities.
     */
  public static List readXpdl(URL location)
                       throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readXpdl] Enter");
    Logger.log("Beginning XPDL File Parse: " + new Date() +
            location.toString());
    XpdlParser deployer = new XpdlParser();
    try
    {

      DocumentBuilderFactory factory = new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();
      //factory.setValidating(true);
      factory.setValidating(false);
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setEntityResolver(getXpdlEntityResolver());
      builder.setErrorHandler(new ErrorHandler(){
        public void warning(SAXParseException exception) throws SAXException{
            System.out.println("Warning Validation , Line="+exception.getLineNumber()+",Column="+exception.getColumnNumber()+",exception="+exception.getMessage());
        }
        public void error(SAXParseException exception) throws SAXException{
            throw new SAXException("Error Validation , Line="+exception.getLineNumber()+",Column="+exception.getColumnNumber(),exception);
        }
        public void fatalError(SAXParseException exception) throws SAXException{
            throw new SAXException("Fatal Error Validation , Line="+exception.getLineNumber()+",Column="+exception.getColumnNumber(),exception);
        }
      });

      Document document = builder.parse(location.openStream());
      return deployer.readAll(document);
    }
    catch(FileAccessException e)
    {
      Logger.warn("[XpdlParser.readXpdl] FileAccessException", e);
      throw new DefinitionParserException("Could not get dtd resolver ", e);
    }
    catch (ParserConfigurationException e)
    {
      Logger.warn("[XpdlParser.readXpdl] ParserConfigurationException", e);
      throw new DefinitionParserException("Could not configure XML reader", e);
    }
    catch (SAXException e)
    {
      Logger.warn("[XpdlParser.readXpdl] SAXException", e);
      throw new DefinitionParserException("Could not parse XML (invalid?)", e);
    }
    catch (IOException e)
    {
      Logger.warn("[XpdlParser.readXpdl] IOException", e);
      throw new DefinitionParserException("Could not load file", e);
    }
    catch (Exception e)
    {
      Logger.warn("[XpdlParser.readXpdl] Exception location="+location, e);
      throw new DefinitionParserException("Exception while parsing file="+location, e);
    }
    finally
    {
      Logger.log(
          "[XpdlParser.readXpdl] End of XPDL File Parse: " + new Date() +
          location.toString());
    }
  }

  public static EntityResolver getXpdlEntityResolver() throws FileAccessException {
    if(xpdlEntityResolver==null){
        xpdlEntityResolver=new XmlEntityResolver(xpdlDtdName);
    }
    return xpdlEntityResolver;
  }

  /**
   * DOCUMENT ME!
   *
   * @param document DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws DefinitionParserException DOCUMENT ME!
   */
  public List readAll(Document document)
               throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readAll] Enter");
    values = new LinkedList();
    tempParticipantList = new LinkedList();
    Element docElement;
    docElement = document.getDocumentElement();
    //read the package element, and everything under it
    // puts everything in the values list for returning, etc later
    readPackage(docElement);
    return (values);
  }

  // ----------------------------------------------------------------
  // Package
  // ----------------------------------------------------------------
  protected void readPackage(Element packageElement)
                      throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readPackage] Enter");
    if (packageElement == null)
      return;
    if (!"Package".equals(packageElement.getTagName()))
      throw new DefinitionParserException("Tried to make Package from element not named Package");
    XpdlPackage packageValue = new XpdlPackage();
    values.add(packageValue);
    String packageId = packageElement.getAttribute("Id");
    packageValue.setPackageId(packageId);
    packageValue.setPackageName(packageElement.getAttribute("Name"));
    //PackageHeader
    Element packageHeaderElement = UtilXml.firstChildElement(packageElement,
                                                             "PackageHeader");
    if (packageHeaderElement != null)
    {
      packageValue.setSpecificationId("XPDL");
      packageValue.setSpecificationVersion(UtilXml.childElementValue(
                                               packageHeaderElement,
                                               "XPDLVersion"));
      packageValue.setSourceVendorInfo(UtilXml.childElementValue(
                                           packageHeaderElement, "Vendor"));
      String createdStr = UtilXml.childElementValue(packageHeaderElement,
                                                    "Created");
      if (createdStr != null)
      {
        try
        {
          packageValue.setCreationDateTime(Timestamp.valueOf(createdStr));
        }
        catch (IllegalArgumentException e)
        {
          Logger.warn("[XpdlParser.readPackage] Exception", e);
          throw new DefinitionParserException("Invalid Date-Time format in Package->Created: " +
                                              createdStr, e);
        }
      }
      packageValue.setPackageDescription(UtilXml.childElementValue(
                                             packageHeaderElement,
                                             "Description"));
      packageValue.setDocumentationUrl(UtilXml.childElementValue(
                                           packageHeaderElement,
                                           "Documentation"));
      packageValue.setPriorityUnit(UtilXml.childElementValue(
                                       packageHeaderElement, "PriorityUnit"));
      packageValue.setCostUnit(UtilXml.childElementValue(packageHeaderElement,
                                                         "CostUnit"));
    }
    //RedefinableHeader?
    Element redefinableHeaderElement = UtilXml.firstChildElement(
                                           packageElement, "RedefinableHeader");
    readRedefinableHeader(redefinableHeaderElement, packageValue);
    String pkgVersionId = packageValue.getVersionId();
    //ConformanceClass?
    Element conformanceClassElement = UtilXml.firstChildElement(packageElement,
                                                                "ConformanceClass");
    if (conformanceClassElement != null)
    {
      packageValue.setGraphConformance(conformanceClassElement.getAttribute(
                                           "GraphConformance"));
    }
    //ExternalPackages?
    Element externalPackagesElement = UtilXml.firstChildElement(packageElement,
                                                                "ExternalPackages");
    List externalPackages = UtilXml.childElementList(externalPackagesElement,
                                                     "ExternalPackage");
    readExternalPackages(externalPackages, packageId, pkgVersionId);
    //TypeDeclarations?
    Element typeDeclarationsElement = UtilXml.firstChildElement(packageElement,
                                                                "TypeDeclarations");
    List typeDeclarations = UtilXml.childElementList(typeDeclarationsElement,
                                                     "TypeDeclaration");
    readTypeDeclarations(typeDeclarations, packageId, pkgVersionId);
    //Participants?
    Element participantsElement = UtilXml.firstChildElement(packageElement,
                                                            "Participants");
    List participants = UtilXml.childElementList(participantsElement,
                                                 "Participant");
    readParticipants(participants, packageId,XpdlConstants.NA,pkgVersionId);
    //Applications?
    Element applicationsElement = UtilXml.firstChildElement(packageElement,
                                                            "Applications");
    List applications = UtilXml.childElementList(applicationsElement,
                                                 "Application");
    readApplications(applications, packageId, "_NA_", pkgVersionId);
    //DataFields?
    Element dataFieldsElement = UtilXml.firstChildElement(packageElement,
                                                          "DataFields");
    List dataFields = UtilXml.childElementList(dataFieldsElement, "DataField");
    readDataFields(dataFields, packageId, "_NA_", pkgVersionId);
    //WorkflowProcesses?
    Element workflowProcessesElement = UtilXml.firstChildElement(
                                           packageElement, "WorkflowProcesses");
    List workflowProcesses = UtilXml.childElementList(workflowProcessesElement,
                                                      "WorkflowProcess");
    readWorkflowProcesses(workflowProcesses, packageId, pkgVersionId);

    //ExtendedAttributes?
    Element extendedAttributesElement = UtilXml.firstChildElement(packageElement,
                                                              "ExtendedAttributes");
    List extendedAttributes = UtilXml.childElementList(
                              extendedAttributesElement,
                              "ExtendedAttribute");

    packageValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));

  }

  protected void readRedefinableHeader(Element redefinableHeaderElement,
                                       IEntity valueObject)
                                throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readRedefinableHeader] Enter");
    if (redefinableHeaderElement == null)
      return;
    set(valueObject, "setAuthor",
        UtilXml.childElementValue(redefinableHeaderElement, "Author"));
    set(valueObject, "setVersionId",
        UtilXml.childElementValue(redefinableHeaderElement, "Version"));
    set(valueObject, "setCodepage",
        UtilXml.childElementValue(redefinableHeaderElement, "Codepage"));
    set(valueObject, "setCountrykey",
        UtilXml.childElementValue(redefinableHeaderElement, "Countrykey"));
    set(valueObject, "setPublicationStatus",
        redefinableHeaderElement.getAttribute("PublicationStatus"));
    //Responsibles?
    Element responsiblesElement = UtilXml.firstChildElement(
                                      redefinableHeaderElement, "Responsibles");
    List responsibles = UtilXml.childElementList(responsiblesElement,
                                                 "Responsible");
    readResponsibles(responsibles, valueObject);
  }

  /**
    * @todo need to reed extendattributes for participants,
    */
  protected void readResponsibles(List responsibles, IEntity valueObject)
                           throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readResponsibles] Enter");
    if (responsibles == null || responsibles.size() == 0)
      return;
    Long responsibleListId = null;
    try
    {
      responsibleListId = KeyGen.getNextId(XpdlParticipantList.ENTITY_NAME+"_OneToMany", false);
    }
    catch (Exception e)
    {
      Logger.warn("[XpdlParser.readResponsibles] Exception", e);
      throw new DefinitionParserException("Could not get next sequence id from KeyGen",
                                          e);
    }
    if (responsibleListId == null)
      throw new DefinitionParserException("Could not get next sequence id from KeyGen");
    set(valueObject, "setResponsibleListUId", responsibleListId);
    Iterator responsibleIter = responsibles.iterator();
    int responsibleIndex = 1;
    while (responsibleIter.hasNext())
    {
      Element responsibleElement = (Element)responsibleIter.next();
      String responsibleId = UtilXml.elementValue(responsibleElement);
      XpdlParticipantList participantListValue = new XpdlParticipantList();
      values.add(participantListValue);
      participantListValue.setListUId(responsibleListId);
      participantListValue.setParticipantId(responsibleId);
      participantListValue.setParticipantIndex(new Long(responsibleIndex));
      responsibleIndex++;
    }
  }

  protected void readExternalPackages(List externalPackages, String packageId,
                                      String pkgVersionId) throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readExternalPackages] Enter");

    /**@todo need to read extended attributes */
    if (externalPackages == null || externalPackages.size() == 0)
      return;
    Iterator externalPackageIter = externalPackages.iterator();
    while (externalPackageIter.hasNext())
    {
      Element externalPackageElement = (Element)externalPackageIter.next();
      XpdlExternalPackage externalPackageValue = new XpdlExternalPackage();
      values.add(externalPackageValue);
      externalPackageValue.setPackageId(packageId);
      externalPackageValue.setPkgVersionId(pkgVersionId);
      externalPackageValue.setHref(externalPackageElement.getAttribute("href"));

      Element extendedAttributesElement = UtilXml.firstChildElement(externalPackageElement,
                                                                  "ExtendedAttributes");
      List extendedAttributes = UtilXml.childElementList(
                                  extendedAttributesElement,
                                  "ExtendedAttribute");

      externalPackageValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));
    }
  }

  protected void readTypeDeclarations(List typeDeclarations, String packageId,
                                      String pkgVersionId)
                               throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readTypeDeclarations] Enter");

    /**@todo need to read extended attributes */
    if (typeDeclarations == null || typeDeclarations.size() == 0)
      return;
    Iterator typeDeclarationsIter = typeDeclarations.iterator();
    while (typeDeclarationsIter.hasNext())
    {
      Element typeDeclarationElement = (Element)typeDeclarationsIter.next();
      XpdlTypeDeclaration typeDeclarationValue = new XpdlTypeDeclaration();
      values.add(typeDeclarationValue);
      typeDeclarationValue.setPackageId(packageId);
      typeDeclarationValue.setPkgVersionId(pkgVersionId);
      typeDeclarationValue.setTypeId(typeDeclarationElement.getAttribute("Id"));
      typeDeclarationValue.setTypeName(typeDeclarationElement.getAttribute(
                                           "Name"));
      //(%Type;)
      readType(typeDeclarationElement, typeDeclarationValue);
      //Description?
      typeDeclarationValue.setTypeDescription(UtilXml.childElementValue(
                                                  typeDeclarationElement,
                                                  "Description"));
      Element extendedAttributesElement = UtilXml.firstChildElement(typeDeclarationElement,
                                                                  "ExtendedAttributes");
      List extendedAttributes = UtilXml.childElementList(
                                  extendedAttributesElement,
                                  "ExtendedAttribute");

      typeDeclarationValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));


    }
  }

  // ----------------------------------------------------------------
  // Process
  // ----------------------------------------------------------------
  protected void readWorkflowProcesses(List workflowProcesses,
                                       String packageId, String pkgVersionId)
                                throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readWorkflowProcesses] Enter");
    if (workflowProcesses == null || workflowProcesses.size() == 0)
      return;
    Iterator workflowProcessIter = workflowProcesses.iterator();
    while (workflowProcessIter.hasNext())
    {
      Element workflowProcessElement = (Element)workflowProcessIter.next();
      readWorkflowProcess(workflowProcessElement, packageId, pkgVersionId);
    }
  }

  protected void readWorkflowProcess(Element workflowProcessElement,
                                     String packageId, String pkgVersionId)
                              throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readWorkflowProcess] Enter");
    XpdlProcess workflowProcessValue = new XpdlProcess();
    values.add(workflowProcessValue);
    String processId = workflowProcessElement.getAttribute("Id");
    workflowProcessValue.setPackageId(packageId);
    workflowProcessValue.setPkgVersionId(pkgVersionId);
    workflowProcessValue.setProcessId(processId);
    workflowProcessValue.setProcessName(workflowProcessElement.getAttribute(
                                            "Name"));
    //ProcessHeader
    Element processHeaderElement = UtilXml.firstChildElement(
                                       workflowProcessElement, "ProcessHeader");
    if (processHeaderElement != null)
    {
      workflowProcessValue.setDurationUnit(processHeaderElement.getAttribute(
                                               "DurationUnit"));
      String createdStr = UtilXml.childElementValue(processHeaderElement,
                                                    "Created");
      if (createdStr != null)
      {
        try
        {
          workflowProcessValue.setCreationDateTime(Timestamp.valueOf(
                                                       createdStr));
        }
        catch (IllegalArgumentException e)
        {
          Logger.warn("[XpdlParser.readWorkflowProcess] Exception", e);
          throw new DefinitionParserException("Invalid Date-Time format in WorkflowProcess->ProcessHeader->Created: " +
                                              createdStr, e);
        }
      }
      workflowProcessValue.setProcessDescription(UtilXml.childElementValue(
                                                     processHeaderElement,
                                                     "Description"));
      String priorityStr = UtilXml.childElementValue(processHeaderElement,
                                                     "Priority");
      if (priorityStr != null)
      {
        try
        {
          workflowProcessValue.setPriority(Long.valueOf(priorityStr));
        }
        catch (NumberFormatException e)
        {
          Logger.warn("[XpdlParser.readWorkflowProcess] Exception", e);
          throw new DefinitionParserException("Invalid whole number format in WorkflowProcess->ProcessHeader->Priority: " +
                                              priorityStr, e);
        }
      }
      String limitStr = UtilXml.childElementValue(processHeaderElement,
                                                  "Limit");
      if (limitStr != null)
      {
        try
        {
          workflowProcessValue.setProcessLimit(Double.valueOf(limitStr));
        }
        catch (NumberFormatException e)
        {
          Logger.warn("[XpdlParser.readWorkflowProcess] Exception", e);
          throw new DefinitionParserException("Invalid decimal number format in WorkflowProcess->ProcessHeader->Limit: " +
                                              limitStr, e);
        }
      }
      String validFromStr = UtilXml.childElementValue(processHeaderElement,
                                                      "ValidFrom");
      if (validFromStr != null)
      {
        try
        {
          workflowProcessValue.setValidFromDate(Timestamp.valueOf(validFromStr));
        }
        catch (IllegalArgumentException e)
        {
          Logger.warn("[XpdlParser.readWorkflowProcess] Exception", e);
          throw new DefinitionParserException("Invalid Date-Time format in WorkflowProcess->ProcessHeader->ValidFrom: " +
                                              validFromStr, e);
        }
      }
      String validToStr = UtilXml.childElementValue(processHeaderElement,
                                                    "ValidTo");
      if (validToStr != null)
      {
        try
        {
          workflowProcessValue.setValidToDate(Timestamp.valueOf(validToStr));
        }
        catch (IllegalArgumentException e)
        {
          Logger.warn("[XpdlParser.readWorkflowProcess] Exception", e);
          throw new DefinitionParserException("Invalid Date-Time format in WorkflowProcess->ProcessHeader->ValidTo: " +
                                              validToStr, e);
        }
      }
      //TimeEstimation?
      Element timeEstimationElement = UtilXml.firstChildElement(
                                          processHeaderElement,
                                          "TimeEstimation");
      if (timeEstimationElement != null)
      {
        String waitingTimeStr = UtilXml.childElementValue(
                                    timeEstimationElement, "WaitingTime");
        if (waitingTimeStr != null)
        {
          try
          {
            workflowProcessValue.setWaitingTime(Double.valueOf(waitingTimeStr));
          }
          catch (NumberFormatException e)
          {
            Logger.warn("[XpdlParser.readWorkflowProcess] Exception", e);
            throw new DefinitionParserException("Invalid decimal number format in WorkflowProcess->ProcessHeader->TimeEstimation->WaitingTime: " +
                                                waitingTimeStr, e);
          }
        }
        String workingTimeStr = UtilXml.childElementValue(
                                    timeEstimationElement, "WorkingTime");
        if (workingTimeStr != null)
        {
          try
          {
            workflowProcessValue.setWorkingTime(Double.valueOf(workingTimeStr));
          }
          catch (NumberFormatException e)
          {
            Logger.warn("[XpdlParser.readWorkflowProcess] Exception", e);
            throw new DefinitionParserException("Invalid decimal number format in WorkflowProcess->ProcessHeader->TimeEstimation->WorkingTime: " +
                                                workingTimeStr, e);
          }
        }
        String durationStr = UtilXml.childElementValue(timeEstimationElement,
                                                       "Duration");
        if (durationStr != null)
        {
          try
          {
            workflowProcessValue.setDuration(Double.valueOf(durationStr));
          }
          catch (NumberFormatException e)
          {
            Logger.warn("[XpdlParser.readWorkflowProcess] Exception", e);
            throw new DefinitionParserException("Invalid decimal number format in WorkflowProcess->ProcessHeader->TimeEstimation->Duration: " +
                                                durationStr, e);
          }
        }
      }
    }
    //RedefinableHeader?
    Element redefinableHeaderElement = UtilXml.firstChildElement(
                                           workflowProcessElement,
                                           "RedefinableHeader");
    readRedefinableHeader(redefinableHeaderElement, workflowProcessValue);
    //FormalParameters?
    Element formalParametersElement = UtilXml.firstChildElement(
                                          workflowProcessElement,
                                          "FormalParameters");
    List formalParameters = UtilXml.childElementList(formalParametersElement,
                                                     "FormalParameter");
    readFormalParameters(formalParameters, packageId, processId, "_NA_",
                         pkgVersionId);
    //(%Type;)* TODO
    //DataFields?
    Element dataFieldsElement = UtilXml.firstChildElement(
                                    workflowProcessElement, "DataFields");
    List dataFields = UtilXml.childElementList(dataFieldsElement, "DataField");
    readDataFields(dataFields, packageId, processId, pkgVersionId);
    //Participants?
    Element participantsElement = UtilXml.firstChildElement(
                                      workflowProcessElement, "Participants");
    List participants = UtilXml.childElementList(participantsElement,
                                                 "Participant");
    readParticipants(participants, packageId,processId,pkgVersionId);

    //Applications?
    Element applicationsElement = UtilXml.firstChildElement(
                                      workflowProcessElement, "Applications");
    List applications = UtilXml.childElementList(applicationsElement,
                                                 "Application");
    readApplications(applications, packageId, processId, pkgVersionId);
    //Activities
    Element activitiesElement = UtilXml.firstChildElement(
                                    workflowProcessElement, "Activities");
    List activities = UtilXml.childElementList(activitiesElement, "Activity");
    readActivities(activities, packageId, processId, workflowProcessValue);
    //Transitions
    Element transitionsElement = UtilXml.firstChildElement(
                                     workflowProcessElement, "Transitions");
    List transitions = UtilXml.childElementList(transitionsElement,
                                                "Transition");
    readTransitions(transitions, packageId, processId, pkgVersionId);

    Element extendedAttributesElement = UtilXml.firstChildElement(workflowProcessElement,
                                                              "ExtendedAttributes");
    List extendedAttributes = UtilXml.childElementList(
                              extendedAttributesElement,
                              "ExtendedAttribute");

    workflowProcessValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));


  }

  // ----------------------------------------------------------------
  // Activity
  // ----------------------------------------------------------------
  protected void readActivities(List activities, String packageId,
                                String processId,
                                XpdlProcess workflowProcessValue)
                         throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readActivities] Enter");
    if (activities == null || activities.size() == 0)
      return;
    Iterator activitiesIter = activities.iterator();
    //do the first one differently because it will be the defaultStart activity
    if (activitiesIter.hasNext())
    {
      Element activityElement = (Element)activitiesIter.next();
      String activityId = activityElement.getAttribute("Id");
      workflowProcessValue.setDefaultStartActivityId(activityId);
      readActivity(activityElement, packageId, processId,
                   workflowProcessValue.getPkgVersionId());
    }
    while (activitiesIter.hasNext())
    {
      Element activityElement = (Element)activitiesIter.next();
      readActivity(activityElement, packageId, processId,
                   workflowProcessValue.getPkgVersionId());
    }
  }

  protected void readActivity(Element activityElement, String packageId,
                              String processId, String pkgVersionId)
                       throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readActivity] Enter");
    if (activityElement == null)
      return;
    XpdlActivity activityValue = new XpdlActivity();
    values.add(activityValue);
    String activityId = activityElement.getAttribute("Id");
    activityValue.setPackageId(packageId);
    activityValue.setPkgVersionId(pkgVersionId);
    activityValue.setProcessId(processId);
    activityValue.setActivityId(activityId);
    activityValue.setActivityName(activityElement.getAttribute("Name"));
    activityValue.setActivityDescription(UtilXml.childElementValue(
                                             activityElement, "Description"));
    String limitStr = UtilXml.childElementValue(activityElement, "Limit");
    if (limitStr != null)
    {
      try
      {
        activityValue.setActivityLimit(Double.valueOf(limitStr));
      }
      catch (NumberFormatException e)
      {
        Logger.warn("[XpdlParser.readActivity] Exception", e);
        throw new DefinitionParserException("Invalid decimal number format in Activity->Limit: " +
                                            limitStr, e);
      }
    }
    //(Route | Implementation)
    Element routeElement = UtilXml.firstChildElement(activityElement, "Route");
    Element implementationElement = UtilXml.firstChildElement(activityElement,
                                                              "Implementation");
    if (routeElement != null)
    {
      activityValue.setIsRoute(Boolean.TRUE);  //"activityTypeEnumId", "WAT_ROUTE");
    }
    else if (implementationElement != null)
    {
      Element noElement = UtilXml.firstChildElement(implementationElement,
                                                    "No");
      Element subFlowElement = UtilXml.firstChildElement(implementationElement,
                                                         "SubFlow");
      Element loopElement = UtilXml.firstChildElement(implementationElement,
                                                      "Loop");
      List tools = UtilXml.childElementList(implementationElement, "Tool");
      activityValue.setIsRoute(Boolean.FALSE);
      if (noElement != null)
      {
        activityValue.setImplementationType(XpdlConstants.IT_NO);
      }
      else if (subFlowElement != null)
      {
        activityValue.setImplementationType(XpdlConstants.IT_SUBFLOW);
        readSubFlow(subFlowElement, packageId, processId, activityId,
                    pkgVersionId);
      }
      else if (loopElement != null)
      {
        activityValue.setImplementationType(XpdlConstants.IT_LOOP);
        readLoop(loopElement, packageId, processId, activityId, pkgVersionId);
      }
      else if (tools != null && tools.size() > 0)
      {
        activityValue.setImplementationType(XpdlConstants.IT_TOOL);
        readTools(tools, packageId, processId, activityId, pkgVersionId);
      }
      else
      {
        throw new DefinitionParserException("No, SubFlow, Loop or one or more Tool elements must exist under the Implementation element of Activity with ID " +
                                            activityId +
                                            " in Process with ID " +
                                            processId);
      }
    }
    else
    {
      throw new DefinitionParserException("Route or Implementation must exist for Activity with ID " +
                                          activityId +
                                          " in Process with ID " +
                                          processId);
    }
    //Performer?
    activityValue.setPerformerId(UtilXml.childElementValue(activityElement,
                                                           "Performer"));
    //StartMode?
    Element startModeElement = UtilXml.firstChildElement(activityElement,
                                                         "StartMode");
    if (startModeElement != null)
    {
      if (UtilXml.firstChildElement(startModeElement, "Automatic") != null)
        activityValue.setStartMode(XpdlConstants.AUTOMATIC_MODE);
      else if (UtilXml.firstChildElement(startModeElement, "Manual") != null)
        activityValue.setStartMode(XpdlConstants.MANUAL_MODE);
      else
        throw new DefinitionParserException("Could not find Mode under StartMode");
    }
    //FinishMode?
    Element finishModeElement = UtilXml.firstChildElement(activityElement,
                                                          "FinishMode");
    if (finishModeElement != null)
    {
      if (UtilXml.firstChildElement(finishModeElement, "Automatic") != null)
        activityValue.setFinishMode(XpdlConstants.AUTOMATIC_MODE);
      else if (UtilXml.firstChildElement(finishModeElement, "Manual") != null)
        activityValue.setFinishMode(XpdlConstants.MANUAL_MODE);
      else
        throw new DefinitionParserException("Could not find Mode under FinishMode");
    }
    //Priority?
    String priorityStr = UtilXml.childElementValue(activityElement, "Priority");
    if (priorityStr != null)
    {
      try
      {
        activityValue.setPriority(Long.valueOf(priorityStr));
      }
      catch (NumberFormatException e)
      {
        Logger.warn("[XpdlParser.readActivity] Exception", e);
        throw new DefinitionParserException("Invalid whole number format in Activity->Priority: " +
                                            priorityStr, e);
      }
    }
    //SimulationInformation?
    Element simulationInformationElement = UtilXml.firstChildElement(
                                               activityElement,
                                               "SimulationInformation");
    if (simulationInformationElement != null)
    {
      if (simulationInformationElement.getAttribute("Instantiation") != null)
        activityValue.setInstantiation(simulationInformationElement.getAttribute(
                                           "Instantiation"));
      String costStr = UtilXml.childElementValue(simulationInformationElement,
                                                 "Cost");
      if (costStr != null)
      {
        try
        {
          activityValue.setCost(Double.valueOf(costStr));
        }
        catch (NumberFormatException e)
        {
          Logger.warn("[XpdlParser.readActivity] Exception", e);
          throw new DefinitionParserException("Invalid decimal number format in Activity->SimulationInformation->Cost: " +
                                              costStr, e);
        }
      }
      //TimeEstimation
      Element timeEstimationElement = UtilXml.firstChildElement(
                                          simulationInformationElement,
                                          "TimeEstimation");
      if (timeEstimationElement != null)
      {
        String waitingTimeStr = UtilXml.childElementValue(
                                    timeEstimationElement, "WaitingTime");
        if (waitingTimeStr != null)
        {
          try
          {
            activityValue.setWaitingTime(Double.valueOf(waitingTimeStr));
          }
          catch (NumberFormatException e)
          {
            Logger.warn("[XpdlParser.readActivity] Exception", e);
            throw new DefinitionParserException("Invalid decimal number format in Activity->SimulationInformation->TimeEstimation->WaitingTime: " +
                                                waitingTimeStr, e);
          }
        }
        String workingTimeStr = UtilXml.childElementValue(
                                    timeEstimationElement, "WorkingTime");
        if (workingTimeStr != null)
        {
          try
          {
            activityValue.setWorkingTime(Double.valueOf(workingTimeStr));
          }
          catch (NumberFormatException e)
          {
            Logger.warn("[XpdlParser.readActivity] Exception", e);
            throw new DefinitionParserException("Invalid decimal number format in Activity->SimulationInformation->TimeEstimation->WorkingTime: " +
                                                workingTimeStr, e);
          }
        }
        String durationStr = UtilXml.childElementValue(timeEstimationElement,
                                                       "Duration");
        if (durationStr != null)
        {
          try
          {
            activityValue.setDuration(Double.valueOf(durationStr));
          }
          catch (NumberFormatException e)
          {
            Logger.warn("[XpdlParser.readActivity] Exception", e);
            throw new DefinitionParserException("Invalid decimal number format in Activity->SimulationInformation->TimeEstimation->Duration: " +
                                                durationStr, e);
          }
        }
      }
    }
    activityValue.setIconUrl(UtilXml.childElementValue(activityElement, "Icon"));
    activityValue.setDocumentationUrl(UtilXml.childElementValue(
                                          activityElement, "Documentation"));
    //TransitionRestrictions?
    Element transitionRestrictionsElement = UtilXml.firstChildElement(
                                                activityElement,
                                                "TransitionRestrictions");
    List transitionRestrictions = UtilXml.childElementList(
                                      transitionRestrictionsElement,
                                      "TransitionRestriction");
    readTransitionRestrictions(transitionRestrictions, activityValue);
    //ExtendedAttributes?
    Element extendedAttributesElement = UtilXml.firstChildElement(activityElement,
                                                              "ExtendedAttributes");
    List extendedAttributes = UtilXml.childElementList(
                              extendedAttributesElement,
                              "ExtendedAttribute");

    activityValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));

  }

  protected void readSubFlow(Element subFlowElement, String packageId,
                             String processId, String activityId,
                             String pkgVersionId)
                      throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readSubFlow] Enter");
    if (subFlowElement == null)
      return;
    XpdlSubFlow subFlowValue = new XpdlSubFlow();
    values.add(subFlowValue);
    subFlowValue.setPackageId(packageId);
    subFlowValue.setPkgVersionId(pkgVersionId);
    subFlowValue.setProcessId(processId);
    subFlowValue.setActivityId(activityId);
    subFlowValue.setSubFlowId(subFlowElement.getAttribute("Id"));
    if (subFlowElement.getAttribute("Execution") != null)
      subFlowValue.setSubFlowType(subFlowElement.getAttribute("Execution"));
    else
      subFlowValue.setSubFlowType(XpdlConstants.SE_ASYNCHR);
    //ActualParameters?
    Element actualParametersElement = UtilXml.firstChildElement(subFlowElement,
                                                                "ActualParameters");
    List actualParameters = UtilXml.childElementList(actualParametersElement,
                                                     "ActualParameter");
    subFlowValue.setActualParameters(readActualParameters(actualParameters));
  }

  protected void readLoop(Element loopElement, String packageId,
                          String processId, String activityId,
                          String pkgVersionId)
                   throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readLoop] Enter");
    if (loopElement == null)
      return;
    XpdlTool loopValue = new XpdlTool();
    values.add(loopValue);
    loopValue.setPackageId(packageId);
    loopValue.setPkgVersionId(pkgVersionId);
    loopValue.setProcessId(processId);
    loopValue.setActivityId(activityId);
    if (loopElement.getAttribute("Kind") != null)
      loopValue.setLoopKind(loopElement.getAttribute("Kind"));
    else
      loopValue.setLoopKind(XpdlConstants.LK_WHILE);
    //Condition?
    loopValue.setConditionExpr(UtilXml.childElementValue(loopElement,
                                                         "Condition"));
  }

  protected void readTools(List tools, String packageId, String processId,
                           String activityId, String pkgVersionId)
                    throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readTools] Enter");
    if (tools == null || tools.size() == 0)
      return;
    Iterator toolsIter = tools.iterator();
    while (toolsIter.hasNext())
    {
      Element toolElement = (Element)toolsIter.next();
      readTool(toolElement, packageId, processId, activityId, pkgVersionId);
    }
  }

  protected void readTool(Element toolElement, String packageId,
                          String processId, String activityId,
                          String pkgVersionId)
                   throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readTool] Enter");
    if (toolElement == null)
      return;
    XpdlTool toolValue = new XpdlTool();
    values.add(toolValue);
    toolValue.setPackageId(packageId);
    toolValue.setPkgVersionId(pkgVersionId);
    toolValue.setProcessId(processId);
    toolValue.setActivityId(activityId);
    toolValue.setToolId(toolElement.getAttribute("Id"));
    if (toolElement.getAttribute("Type") != null)
      toolValue.setToolType(toolElement.getAttribute("Type"));
    else
      toolValue.setToolType(XpdlConstants.TT_PROCEDURE);
    //Description?
    toolValue.setToolDescription(UtilXml.childElementValue(toolElement,
                                                           "Description"));
    //ActualParameters/ExtendedAttributes?
    Element actualParametersElement = UtilXml.firstChildElement(toolElement,
                                                                "ActualParameters");
    Element extendedAttributesElement = UtilXml.firstChildElement(toolElement,
                                                                  "ExtendedAttributes");
    List actualParameters = UtilXml.childElementList(actualParametersElement,
                                                     "ActualParameter");
    List extendedAttributes = UtilXml.childElementList(
                                  extendedAttributesElement,
                                  "ExtendedAttribute");
    toolValue.setActualParameters(readActualParameters(actualParameters));
    toolValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));
  }

  protected String readActualParameters(List actualParameters)
  {
    Logger.log("[XpdlParser.readActualParameters] Enter");
    if (actualParameters == null || actualParameters.size() == 0)
      return null;
    StringBuffer actualParametersBuf = new StringBuffer();
    Iterator actualParametersIter = actualParameters.iterator();
    while (actualParametersIter.hasNext())
    {
      Element actualParameterElement = (Element)actualParametersIter.next();
      actualParametersBuf.append(UtilXml.elementValue(actualParameterElement));
      if (actualParametersIter.hasNext())
        actualParametersBuf.append(',');
    }
    return actualParametersBuf.toString();
  }

  protected String readExtendedAttributes(List extendedAttributes) 
  {
    Logger.log("[XpdlParser.readExtendedAttributes] Enter");
    if (extendedAttributes == null || extendedAttributes.size() == 0)
      return null;
    Map ea = new HashMap();
    Iterator i = extendedAttributes.iterator();
    while (i.hasNext())
    {
      Element e = (Element)i.next();
      ea.put(e.getAttribute("Name"), e.getAttribute("Value"));
    }
  	return UtilString.mapToStr(ea);
  }

  // ----------------------------------------------------------------
  // Transition
  // ----------------------------------------------------------------
  protected void readTransitions(List transitions, String packageId,
                                 String processId, String pkgVersionId)
                          throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readTransitions] Enter");
    if (transitions == null || transitions.size() == 0)
      return;
    Iterator transitionsIter = transitions.iterator();
    while (transitionsIter.hasNext())
    {
      Element transitionElement = (Element)transitionsIter.next();
      readTransition(transitionElement, packageId, processId, pkgVersionId);
    }
  }

  protected void readTransition(Element transitionElement, String packageId,
                                String processId, String pkgVersionId)
                         throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readTransition] Enter");
    if (transitionElement == null)
      return;
    XpdlTransition transitionValue = new XpdlTransition();
    values.add(transitionValue);
    String transitionId = transitionElement.getAttribute("Id");
    transitionValue.setPackageId(packageId);
    transitionValue.setPkgVersionId(pkgVersionId);
    transitionValue.setProcessId(processId);
    transitionValue.setTransitionId(transitionId);
    transitionValue.setFromActivityId(transitionElement.getAttribute("From"));
    transitionValue.setToActivityId(transitionElement.getAttribute("To"));
    if (transitionElement.getAttribute("Loop") != null)
      transitionValue.setLoopType(transitionElement.getAttribute("Loop"));
    else
      transitionValue.setLoopType(XpdlConstants.TL_NOLOOP);
    transitionValue.setTransitionName(transitionElement.getAttribute("Name"));
    //Condition?
    Element conditionElement = UtilXml.firstChildElement(transitionElement,
                                                         "Condition");
    if (conditionElement != null)
    {
      if (conditionElement.getAttribute("Type") != null)
        transitionValue.setConditionType(conditionElement.getAttribute("Type"));
      else
        transitionValue.setConditionType(XpdlConstants.TC_CONDITION);
      //a Condition will have either a list of XPression elements, or plain PCDATA
      List xPressions = UtilXml.childElementList(conditionElement, "XPression");
      if (xPressions != null && xPressions.size() > 0)
      {
        throw new DefinitionParserException("XPression elements under Condition not yet supported, just use text inside Condition with the expression");
      }
      else
      {
        transitionValue.setConditionExpr(UtilXml.elementValue(conditionElement));
      }
    }
    //Description?
    transitionValue.setTransitionDescription(UtilXml.childElementValue(
                                                 transitionElement,
                                                 "Description"));
    Element extendedAttributesElement = UtilXml.firstChildElement(transitionElement,
                                                          "ExtendedAttributes");
    List extendedAttributes = UtilXml.childElementList(
                          extendedAttributesElement,
                          "ExtendedAttribute");

    transitionValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));

  }

  protected void readTransitionRestrictions(List transitionRestrictions,
                                            XpdlActivity activityValue)
                                     throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readTransitionRestrictions] Enter");
    if (transitionRestrictions == null ||
        transitionRestrictions.size() == 0)
      return;
    Iterator transitionRestrictionsIter = transitionRestrictions.iterator();
    if (transitionRestrictionsIter.hasNext())
    {
      Long transitionRestrictionListUId = null;
      try
      {
        transitionRestrictionListUId = KeyGen.getNextId("OneToMany", false);
      }
      catch (Exception e)
      {
        Logger.warn("[XpdlParser.readTransitionRestrictions] Exception", e);
        throw new DefinitionParserException("Could not get next sequence id from KeyGen",
                                            e);
      }
      if (transitionRestrictionListUId == null)
        throw new DefinitionParserException("Could not get next sequence id from KeyGen");
      activityValue.setTransitionRestrictionListUId(
          transitionRestrictionListUId);
      Element transitionRestrictionElement = (Element)transitionRestrictionsIter.next();
      readTransitionRestriction(transitionRestrictionElement, activityValue);
    }
    if (transitionRestrictionsIter.hasNext())
    {
      throw new DefinitionParserException("Multiple TransitionRestriction elements found, this is not currently supported. Please remove extras.");
    }
  }

  protected void readTransitionRestriction(Element transitionRestrictionElement,
                                           XpdlActivity activityValue)
                                    throws DefinitionParserException
  { 
    Logger.log("[XpdlParser.readTransitionRestriction] Enter");
    //String packageId = activityValue.getPackageId();
    //String processId = activityValue.getProcessId();
    //String activityId = activityValue.getActivityId();
    XpdlTransitionRestriction transitionRestrictionValue = new XpdlTransitionRestriction();
    values.add(transitionRestrictionValue);
    transitionRestrictionValue.setListUId(activityValue.getTransitionRestrictionListUId());
    //InlineBlock?
    Element inlineBlockElement = UtilXml.firstChildElement(
                                     transitionRestrictionElement,
                                     "InlineBlock");
    if (inlineBlockElement != null)
    {
      transitionRestrictionValue.setIsInlineBlock(Boolean.TRUE);
      transitionRestrictionValue.setBlockName(UtilXml.childElementValue(
                                                  inlineBlockElement,
                                                  "BlockName"));
      transitionRestrictionValue.setBlockDescription(UtilXml.childElementValue(
                                                         inlineBlockElement,
                                                         "Description"));
      transitionRestrictionValue.setBlockIconUrl(UtilXml.childElementValue(
                                                     inlineBlockElement,
                                                     "Icon"));
      transitionRestrictionValue.setBlockDocumentationUrl(UtilXml.childElementValue(
                                                              inlineBlockElement,
                                                              "Documentation"));
      transitionRestrictionValue.setBlockBeginActivityId(inlineBlockElement.getAttribute(
                                                             "Begin"));
      transitionRestrictionValue.setBlockEndActivityId(inlineBlockElement.getAttribute(
                                                           "End"));
    }
    else
      transitionRestrictionValue.setIsInlineBlock(Boolean.FALSE);
    //Join?
    Element joinElement = UtilXml.firstChildElement(
                              transitionRestrictionElement, "Join");
    if (joinElement != null)
    {
      String joinType = joinElement.getAttribute("Type");
      if (joinType != null && joinType.length() > 0)
      {
        transitionRestrictionValue.setJoinType(joinType);
      } else {// default join is AND
        transitionRestrictionValue.setJoinType(XpdlConstants.CT_AND);
      }
    }
    //Split?
    Element splitElement = UtilXml.firstChildElement(
                               transitionRestrictionElement, "Split");
    if (splitElement != null)
    {
      String splitType = splitElement.getAttribute("Type");
      if (splitType != null && splitType.length() > 0)
      {
        transitionRestrictionValue.setSplitType(splitType);
      } else {// default split is AND
        transitionRestrictionValue.setSplitType(XpdlConstants.CT_AND);
      }
      //TransitionRefs
      Element transitionRefsElement = UtilXml.firstChildElement(
                                          splitElement,
                                          "TransitionRefs");
      if(transitionRefsElement==null)
        throw new DefinitionParserException("TransitionRefs should exist for split "+splitElement.toString());
      List transitionRefs = UtilXml.childElementList(transitionRefsElement,
                                                     "TransitionRef");
      if(transitionRefs==null || transitionRefs.size()==0)
        throw new DefinitionParserException("TransitionRef should exist for split "+splitElement.toString());

      readTransitionRefs(transitionRefs, transitionRestrictionValue);
    }
  }

  protected void readTransitionRefs(List transitionRefs,
                                    XpdlTransitionRestriction transitionRestrictionValue)
                             throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readTransitionRestriction] Enter");
    if (transitionRefs == null || transitionRefs.size() == 0)
      return;
    Long transitionRefListUId = null;
    try
    {
      transitionRefListUId = KeyGen.getNextId("OneToMany", false);
    }
    catch (Exception e)
    {
      Logger.warn("[XpdlParser.readTransitionRestriction] Exception", e);
      throw new DefinitionParserException("Could not get next sequence id from KeyGen",
                                          e);
    }
    if (transitionRefListUId == null)
      throw new DefinitionParserException("Could not get next sequence id from KeyGen");
    transitionRestrictionValue.setTransitionRefListUId(transitionRefListUId);
    Iterator transitionRefsIter = transitionRefs.iterator();
    while (transitionRefsIter.hasNext())
    {
      Element transitionRefElement = (Element)transitionRefsIter.next();
      XpdlTransitionRef transitionRefValue = new XpdlTransitionRef();
      values.add(transitionRefValue);
      transitionRefValue.setTransitionId(transitionRefElement.getAttribute(
                                             "Id"));
      transitionRefValue.setListUId(transitionRefListUId);
    }
  }

  // ----------------------------------------------------------------
  // Others
  // ----------------------------------------------------------------
  protected void readParticipants(List participants, String packageId,
                                  String processId, String pkgVersionId)
                           throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readParticipants] Enter");

    /**@todo need to read extended attributes */
    if (participants == null || participants.size() == 0)
      return;

    Iterator participantsIter = participants.iterator();
    //long index = 1;
    while (participantsIter.hasNext())
    {
      Element participantElement = (Element)participantsIter.next();
      String participantId = participantElement.getAttribute("Id");

        XpdlParticipant participantValue = new XpdlParticipant();
        values.add(participantValue);
        //tempParticipantList.add(participantId);
        participantValue.setPackageId(packageId);
        participantValue.setPkgVersionId(pkgVersionId);
        participantValue.setProcessId(processId);

        participantValue.setParticipantId(participantId);
        participantValue.setParticipantName(participantElement.getAttribute(
                                                "Name"));


        //ParticipantType
        Element participantTypeElement = UtilXml.firstChildElement(
                                             participantElement,
                                             "ParticipantType");
        if (participantTypeElement != null)
        {
          participantValue.setParticipantTypeId(participantTypeElement.getAttribute(
                                                    "Type"));
        }
        //Description?
        participantValue.setParticipantDescription(UtilXml.childElementValue(
                                                       participantElement,
                                                       "Description"));

        //ExtendedAttributes?
        Element extendedAttributesElement = UtilXml.firstChildElement(participantElement,
                                                                  "ExtendedAttributes");
        List extendedAttributes = UtilXml.childElementList(
                                  extendedAttributesElement,
                                  "ExtendedAttribute");

        participantValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));
    }
  }

  protected void readApplications(List applications, String packageId,
                                  String processId, String pkgVersionId)
                           throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readApplications] Enter");
    if (applications == null || applications.size() == 0)
      return;
    Iterator applicationsIter = applications.iterator();
    while (applicationsIter.hasNext())
    {
      Element applicationElement = (Element)applicationsIter.next();
      XpdlApplication applicationValue = new XpdlApplication();
      values.add(applicationValue);
      String applicationId = applicationElement.getAttribute("Id");
      applicationValue.setPackageId(packageId);
      applicationValue.setPkgVersionId(pkgVersionId);
      applicationValue.setProcessId(processId);
      applicationValue.setApplicationId(applicationId);
      applicationValue.setApplicationName(applicationElement.getAttribute(
                                              "Name"));
      //Description?
      applicationValue.setApplicationDescription(UtilXml.childElementValue(
                                                     applicationElement,
                                                     "Description"));
      //FormalParameters?
      Element formalParametersElement = UtilXml.firstChildElement(
                                            applicationElement,
                                            "FormalParameters");
      List formalParameters = UtilXml.childElementList(formalParametersElement,
                                                       "FormalParameter");
      readFormalParameters(formalParameters, packageId, processId,
                           applicationId, pkgVersionId);

      Element extendedAttributesElement = UtilXml.firstChildElement(applicationElement,
                                                                  "ExtendedAttributes");
      List extendedAttributes = UtilXml.childElementList(
                                  extendedAttributesElement,
                                  "ExtendedAttribute");

      applicationValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));
    }
  }

  protected void readDataFields(List dataFields, String packageId,
                                String processId, String pkgVersionId)
                         throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readDataFields] Enter");

    /**@todo need to read extendedattributes */
    if (dataFields == null || dataFields.size() == 0)
      return;
    Iterator dataFieldsIter = dataFields.iterator();
    while (dataFieldsIter.hasNext())
    {
      Element dataFieldElement = (Element)dataFieldsIter.next();
      XpdlDataField dataFieldValue = new XpdlDataField();
      values.add(dataFieldValue);
      String dataFieldId = dataFieldElement.getAttribute("Id");
      dataFieldValue.setPackageId(packageId);
      ///////dataFieldValue.setPkgVersionId(pkgVersionId);
      dataFieldValue.setProcessId(processId);
      dataFieldValue.setDataFieldId(dataFieldId);
      dataFieldValue.setDataFieldName(dataFieldElement.getAttribute("Name"));
      //IsArray attr
      dataFieldValue.setIsArray(Boolean.valueOf(
                                    "" + ("TRUE".equals(dataFieldElement.getAttribute(
                                                            "IsArray")))));
      //DataType
      Element dataTypeElement = UtilXml.firstChildElement(dataFieldElement,
                                                          "DataType");
      if (dataTypeElement != null)
      {
        //(%Type;)
        readType(dataTypeElement, dataFieldValue);
      }
      //InitialValue?
      dataFieldValue.setInitialValue(UtilXml.childElementValue(
                                         dataFieldElement, "InitialValue"));
      //Length?
      String lengthStr = UtilXml.childElementValue(dataFieldElement, "Length");
      if (lengthStr != null && lengthStr.length() > 0)
      {
        try
        {
          dataFieldValue.setLengthBytes(Long.valueOf(lengthStr));
        }
        catch (NumberFormatException e)
        {
          Logger.warn("[XpdlParser.readDataFields] Exception", e);
          throw new DefinitionParserException("Invalid whole number format in DataField->Length: " +
                                              lengthStr, e);
        }
      }
      //Description?
      dataFieldValue.setDataFieldDescription(UtilXml.childElementValue(
                                                 dataFieldElement,
                                                 "Description"));

        Element extendedAttributesElement = UtilXml.firstChildElement(dataFieldElement,
                                                              "ExtendedAttributes");
        List extendedAttributes = UtilXml.childElementList(
                              extendedAttributesElement,
                              "ExtendedAttribute");

        dataFieldValue.setExtendedAttributes(readExtendedAttributes(extendedAttributes));

    }
  }

  protected void readFormalParameters(List formalParameters, String packageId,
                                      String processId, String applicationId,
                                      String pkgVersionId)
                               throws DefinitionParserException
  {
    Logger.log("[XpdlParser.readFormalParameters] Enter");

    /**@todo need to read datatype */
    if (formalParameters == null || formalParameters.size() == 0)
      return;
    Iterator formalParametersIter = formalParameters.iterator();
    long index = 1;
    while (formalParametersIter.hasNext())
    {
      Element formalParameterElement = (Element)formalParametersIter.next();
      XpdlFormalParam formalParameterValue = new XpdlFormalParam();
      values.add(formalParameterValue);
      String formalParamId = formalParameterElement.getAttribute("Id");
      formalParameterValue.setPackageId(packageId);
      formalParameterValue.setPkgVersionId(pkgVersionId);
      formalParameterValue.setProcessId(processId);
      formalParameterValue.setApplicationId(applicationId);
      formalParameterValue.setFormalParamId(formalParamId);
      formalParameterValue.setMode(formalParameterElement.getAttribute("Mode"));
      String indexStr = formalParameterElement.getAttribute("Index");
      if (indexStr != null && indexStr.length() > 0)
      {
        try
        {
          formalParameterValue.setIndexNumber(Integer.valueOf(indexStr));
        }
        catch (NumberFormatException e)
        {
          Logger.warn("[XpdlParser.readFormalParameters] Exception", e);
          throw new DefinitionParserException("Invalid decimal number format in FormalParameter->Index: " +
                                              indexStr, e);
        }
      }
      else
        formalParameterValue.setIndexNumber(new Integer("" + index));
      index++;
      //DataType
      Element dataTypeElement = UtilXml.firstChildElement(
                                    formalParameterElement, "DataType");
      if (dataTypeElement != null)
      {
        //(%Type;)
        readType(dataTypeElement, formalParameterValue);
      }
      //Description?
      formalParameterValue.setFormalParamDescription(UtilXml.childElementValue(
                                                         formalParameterElement,
                                                         "Description"));
    }
  }

   protected String getJavaType(String type) throws DefinitionParserException{
        String javaType=type;
        if(type.equals("STRING")){
            javaType=String.class.getName();
        } else if(type.equals("FLOAT")){
            javaType=Float.class.getName();
        } else if(type.equals("INTEGER")){
            javaType=Integer.class.getName();
        } else if(type.equals("DATETIME")){
            javaType=Timestamp.class.getName();
        } else if(type.equals("BOOLEAN")){
            javaType=String.class.getName();
        } else if(type.equals("REFERENCE") || type.equals("UNIT") || type.equals("PERFORMER")){
            throw new DefinitionParserException("[XpdlParser.getJavaType] Type not supported ,type="+type);
        }
        return javaType;
   }


  /** Reads information about "Type" entity member sub-elements; the value
    * object passed must have two fields to contain Type information:
    * <code>dataTypeEnumId</code> and <code>complexTypeInfoId</code>.
    */

   protected void readType(Element element, IEntity valueObject)  throws DefinitionParserException {
   //(%Type;) - (RecordType | UnionType | EnumerationType | ArrayType | ListType | BasicType | PlainType | DeclaredType)
       try{
           Element typeElement = null;
           if ((typeElement = UtilXml.firstChildElement(element, XpdlConstants.DT_RECORD_TYPE)) != null) {
           //TODO: write code for complex type
                Long memberListUId=getNextSeqUId("ComplexDataTypeUId");
                set(valueObject, "setDataTypeName", XpdlConstants.DT_RECORD_TYPE);
                set(valueObject, "setComplexDataTypeUId", memberListUId);
                List memberList=UtilXml.childElementList(typeElement,"Member");
                for(Iterator iterator=memberList.iterator();iterator.hasNext();){
                    XpdlComplexDataType dataType=new XpdlComplexDataType();
                    values.add(dataType);
                    dataType.setSubTypeUId(memberListUId);
                    Element memberElement=(Element)iterator.next();
                    readType(memberElement,dataType);
                }
           } else if ((typeElement = UtilXml.firstChildElement(element, XpdlConstants.DT_UNION_TYPE)) != null) {
           //TODO: write code for complex type
                Long memberListUId=getNextSeqUId("ComplexDataTypeUId");
                set(valueObject, "setDataTypeName", XpdlConstants.DT_UNION_TYPE);
                set(valueObject, "setComplexDataTypeUId", memberListUId);
                List memberList=UtilXml.childElementList(typeElement,"Member");
                for(Iterator iterator=memberList.iterator();iterator.hasNext();){
                    XpdlComplexDataType dataType=new XpdlComplexDataType();
                    values.add(dataType);
                    dataType.setSubTypeUId(memberListUId);
                    Element memberElement=(Element)iterator.next();
                    readType(memberElement,dataType);
                }
           } else if ((typeElement = UtilXml.firstChildElement(element, XpdlConstants.DT_ENUMERATION_TYPE)) != null) {
           //TODO: write code for complex type
                Long enumValueListUId=getNextSeqUId("ComplexDataTypeUId");
                set(valueObject, "setDataTypeName", XpdlConstants.DT_ENUMERATION_TYPE);
                set(valueObject, "setComplexDataTypeUId", enumValueListUId);
                List enumValueElementList=UtilXml.childElementList(typeElement,"EnumerationValue");
                List enumValueList=new ArrayList();
                for(Iterator iterator=enumValueElementList.iterator();iterator.hasNext();){
                    enumValueList.add(element.getAttribute("Name"));
                }
                XpdlComplexDataType dataType=new XpdlComplexDataType();
                values.add(dataType);
                dataType.setSubTypeUId(enumValueListUId);
                dataType.setDataTypeName(UtilString.join(enumValueList,","));

           } else if ((typeElement = UtilXml.firstChildElement(element, XpdlConstants.DT_ARRAY_TYPE)) != null) {
           //TODO: write code for complex type
                XpdlComplexDataType dataType=new XpdlComplexDataType();
                values.add(dataType);
                dataType.setArrayLowerIndex(new Integer(typeElement.getAttribute("LowerIndex")));
                dataType.setArrayUpperIndex(new Integer(typeElement.getAttribute("UpperIndex")));
                dataType.setSubTypeUId(getNextSeqUId("ComplexDataTypeUId"));
                set(valueObject, "setDataTypeName", XpdlConstants.DT_ARRAY_TYPE);
                set(valueObject, "setComplexDataTypeUId", dataType.getSubTypeUId());

                readType(typeElement,dataType);
           } else if ((typeElement = UtilXml.firstChildElement(element, XpdlConstants.DT_LIST_TYPE)) != null) {
                //TODO: write code for complex type
                XpdlComplexDataType dataType=new XpdlComplexDataType();
                values.add(dataType);
                dataType.setSubTypeUId(getNextSeqUId("ComplexDataTypeUId"));
                set(valueObject, "setDataTypeName", XpdlConstants.DT_LIST_TYPE);
                set(valueObject, "setComplexDataTypeUId", dataType.getSubTypeUId());

                readType(typeElement,dataType);
           } else if ((typeElement = UtilXml.firstChildElement(element, XpdlConstants.DT_BASIC_TYPE)) != null ||
                      (typeElement = UtilXml.firstChildElement(element, XpdlConstants.DT_PLAIN_TYPE )) != null) {
                set(valueObject, "setDataTypeName",getJavaType(typeElement.getAttribute("Type")));
           } else if ((typeElement = UtilXml.firstChildElement(element, XpdlConstants.DT_DECLARED_TYPE)) != null) {
                //For DeclaredTypes complexTypeInfoId will actually be the type id
                //set(valueObject, "setComplexDataTypeUId", typeElement.getAttribute("Id"));
                //changed **************
                //declared type is also treated as basic type,and in dtd 'Id' is changed to nmtoken insted od IDRef
                set(valueObject, "setDataTypeName", getJavaType(typeElement.getAttribute("Id")));

           }
        }catch(DefinitionParserException dpex){
            throw dpex;
        }
    }

    protected Long getNextSeqUId(String name) throws DefinitionParserException {
        try
        {
          return KeyGen.getNextId(name, false);
        }
        catch (Exception e)
        {
          Logger.warn("[XpdlParser.getNextSeqUId] Exception", e);
          throw new DefinitionParserException("Could not get next sequence id from KeyGen for "+name,e);
        }
    }


  protected String getExtendedAttributeValue(Element element, String name,
                                             String defaultValue)
  {
    Logger.log("[XpdlParser.getExtendedAttributeValue] Enter");
    if (element == null || name == null)
      return defaultValue;
    Element extendedAttributesElement = UtilXml.firstChildElement(element,
                                                                  "ExtendedAttributes");
    if (extendedAttributesElement == null)
      return defaultValue;
    List extendedAttributes = UtilXml.childElementList(
                                  extendedAttributesElement,
                                  "ExtendedAttribute");
    if (extendedAttributes == null || extendedAttributes.size() == 0)
      return defaultValue;
    Iterator iter = extendedAttributes.iterator();
    while (iter.hasNext())
    {
      Element extendedAttribute = (Element)iter.next();
      String elementName = extendedAttribute.getAttribute("Name");
      if (name.equals(elementName))
      {
        return extendedAttribute.getAttribute("Value");
      }
    }
    return defaultValue;
  }


  //----------------------------------------------------------
  // REFLICTION Methods
  //----------------------------------------------------------
  protected void set(Object obj, String method, Object param)
  {
    Logger.log("[XpdlParser.set] Enter");
    try
    {
      if (param != null)
        obj.getClass().getMethod(method, new Class[] {param.getClass()}).invoke(
            obj, new Object[] {param});
      else
      {
        Method[] methods = obj.getClass().getMethods();
        for (int loop = 0; loop < methods.length; loop++)
        {
          if (methods[loop].getName().equals(method))
          {
            methods[loop].invoke(obj, new Object[] {param});
            break;
          }
        }
      }
    }
    catch (Exception e)
    {
      Logger.warn(
          "[XpdlParser.set] Exception" + "[" + obj + "],[" + method + "],[" +
          param + "]", e);
    }
  }

 

}
