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
 * May 14 2002    Mahesh              Created
 * Jun 13 2002    Mathew              Repackaged
 */

package com.gridnode.pdip.base.gwfbase.xpdl.helpers;

import java.util.*;

import com.gridnode.pdip.base.gwfbase.xpdl.model.*;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.util.UtilEntity;

/**
 * This is the helper class to retrive xpdl definition. NOTE: Since
 * definitions will not change, Definitions are retrived using
 * IEntityDao.getEntityByFilterForReadOnly,
 */
public class XpdlDefinitionHelper
{


  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getPackages()
                                throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlPackage.PACKAGE_ID,
                           filter.getNotEqualOperator(), null, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlPackage.ENTITY_NAME,
                                                              true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlPackage getPackage(String packageId, String pkgVersionId)
                                throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlPackage.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(), XpdlPackage.VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlPackage.ENTITY_NAME,
                                                              true);
    if (coll == null || coll.size() == 0)
      return null;
    else
      return (XpdlPackage)coll.toArray()[0];
  }

  /**
   * DOCUMENT ME!
   *
   * @param processUId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlProcess getProcess(Long processUId)
                                throws Throwable
  {
    //return (XpdlProcess)UtilEntity.getEntityByKey(processUId,XpdlProcess.ENTITY_NAME,true);
    return (XpdlProcess)UtilEntity.getEntityByKey(processUId,XpdlProcess.ENTITY_NAME,true);
  }

  /**
   * DOCUMENT ME!
   *
   * @param processId DOCUMENT ME!
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getProcesses(String processId, String packageId,
                                        String pkgVersionId)
                                 throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlProcess.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlProcess.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    if (processId != null)
      filter.addSingleFilter(filter.getAndConnector(), XpdlProcess.PROCESS_ID,
                             filter.getEqualOperator(), processId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlProcess.ENTITY_NAME,
                                                              true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param applicationId DOCUMENT ME!
   * @param processId DOCUMENT ME!
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getApplications(String applicationId,
                                           String processId, String packageId,
                                           String pkgVersionId)
                                    throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlApplication.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlApplication.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    if (processId != null)
      filter.addSingleFilter(filter.getAndConnector(),
                             XpdlApplication.PROCESS_ID,
                             filter.getEqualOperator(), processId, false);
    if (applicationId != null)
      filter.addSingleFilter(filter.getAndConnector(),
                             XpdlApplication.APPLICATION_ID,
                             filter.getEqualOperator(), applicationId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlApplication.ENTITY_NAME,
                                                              true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param activityUId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlActivity getActivity(Long activityUId)
                                  throws Throwable
  {
    //return (XpdlActivity)UtilEntity.getEntityByKey(activityUId,XpdlActivity.ENTITY_NAME,true);
    return (XpdlActivity)UtilEntity.getEntityByKey(activityUId,XpdlActivity.ENTITY_NAME,true);
  }

  /**
   * DOCUMENT ME!
   *
   * @param activityId DOCUMENT ME!
   * @param processId DOCUMENT ME!
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlActivity getActivity(String activityId, String processId,
                                         String packageId, String pkgVersionId)
                                  throws Throwable
  {
    Collection coll = getActivities(activityId, processId, packageId,
                                    pkgVersionId);
    if (coll != null && coll.size() == 1)
      return (XpdlActivity)coll.toArray()[0];
    else
    {
      return null;
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param activityId DOCUMENT ME!
   * @param processId DOCUMENT ME!
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getActivities(String activityId, String processId,
                                         String packageId, String pkgVersionId)
                                  throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlActivity.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlActivity.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    if (processId != null)
      filter.addSingleFilter(filter.getAndConnector(), XpdlActivity.PROCESS_ID,
                             filter.getEqualOperator(), processId, false);
    if (activityId != null)
      filter.addSingleFilter(filter.getAndConnector(),
                             XpdlActivity.ACTIVITY_ID,
                             filter.getEqualOperator(), activityId, false);
    //Collection coll = UtilEntity.getEntityByFilter(filter,XpdlActivity.ENTITY_NAME,true);
    Collection coll = UtilEntity.getEntityByFilter(filter,XpdlActivity.ENTITY_NAME,true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param dataFieldId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlDataField getDataField(String dataFieldId)
                                    throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlDataField.DATA_FIELD_ID,
                           filter.getEqualOperator(), dataFieldId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlDataField.ENTITY_NAME,
                                                              true);
    if (coll == null || coll.size() == 0)
      return null;
    else
      return (XpdlDataField)coll.toArray()[0];
  }

  /**
   * DOCUMENT ME!
   *
   * @param processId DOCUMENT ME!
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getDataFields(String processId, String packageId,
                                         String pkgVersionId)
                                  throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlDataField.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlDataField.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    if (processId != null)
      filter.addSingleFilter(filter.getAndConnector(),
                             XpdlDataField.PROCESS_ID,
                             filter.getEqualOperator(), processId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlDataField.ENTITY_NAME,
                                                              true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getExternalPackages(String packageId,
                                               String pkgVersionId)
                                        throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlExternalPackage.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlExternalPackage.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlExternalPackage.ENTITY_NAME,
                                                              true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param formalParamId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlFormalParam getFormalParam(String formalParamId)
                                        throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlFormalParam.FORMAL_PARAM_ID,
                           filter.getEqualOperator(), formalParamId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlFormalParam.ENTITY_NAME,
                                                              true);
    if (coll == null || coll.size() == 0)
      return null;
    else
      return (XpdlFormalParam)coll.toArray()[0];
  }

  /**
   * DOCUMENT ME!
   *
   * @param applicationId DOCUMENT ME!
   * @param processId DOCUMENT ME!
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getFormalParams(String applicationId,
                                           String processId, String packageId,
                                           String pkgVersionId)
                                    throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlFormalParam.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlFormalParam.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    //if (processId != null)
      filter.addSingleFilter(filter.getAndConnector(),
                             XpdlFormalParam.PROCESS_ID,
                             filter.getEqualOperator(), processId, false);
    //if (applicationId != null)
      filter.addSingleFilter(filter.getAndConnector(),
                             XpdlFormalParam.APPLICATION_ID,
                             filter.getEqualOperator(), applicationId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlFormalParam.ENTITY_NAME,
                                                              true);
    return coll;
  }


  /**
   * DOCUMENT ME!
   *
   * @param subFlowId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlSubFlow getSubFlow(String subFlowId)
                                throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlSubFlow.SUB_FLOW_ID,
                           filter.getEqualOperator(), subFlowId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlSubFlow.ENTITY_NAME,
                                                              true);
    if (coll == null || coll.size() == 0)
      return null;
    else
      return (XpdlSubFlow)coll.toArray()[0];
  }

  /**
   * DOCUMENT ME!
   *
   * @param activityId DOCUMENT ME!
   * @param processId DOCUMENT ME!
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getSubFlows(String activityId, String processId,
                                       String packageId, String pkgVersionId)
                                throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlSubFlow.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlSubFlow.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    if (processId != null)
      filter.addSingleFilter(filter.getAndConnector(), XpdlSubFlow.PROCESS_ID,
                             filter.getEqualOperator(), processId, false);
    if (activityId != null)
      filter.addSingleFilter(filter.getAndConnector(), XpdlSubFlow.ACTIVITY_ID,
                             filter.getEqualOperator(), activityId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlSubFlow.ENTITY_NAME,
                                                              true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param toolId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlTool getTool(String toolId)
                          throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlTool.TOOL_ID, filter.getEqualOperator(),
                           toolId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlTool.ENTITY_NAME,
                                                              true);
    if (coll == null || coll.size() == 0)
      return null;
    else
      return (XpdlTool)coll.toArray()[0];
  }

  /**
   * DOCUMENT ME!
   *
   * @param activityId DOCUMENT ME!
   * @param processId DOCUMENT ME!
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getTools(String activityId, String processId,
                                    String packageId, String pkgVersionId)
                             throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlTool.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(), XpdlTool.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    if (processId != null)
      filter.addSingleFilter(filter.getAndConnector(), XpdlTool.PROCESS_ID,
                             filter.getEqualOperator(), processId, false);
    if (activityId != null)
      filter.addSingleFilter(filter.getAndConnector(), XpdlTool.ACTIVITY_ID,
                             filter.getEqualOperator(), activityId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlTool.ENTITY_NAME,
                                                              true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param transitionUId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlTransition getTransition(Long transitionUId)
                                      throws Throwable
  {
    return (XpdlTransition)UtilEntity.getEntityByKey(transitionUId,
                                                                XpdlTransition.ENTITY_NAME,
                                                                true);
  }

  /**
   * DOCUMENT ME!
   *
   * @param transitionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlTransition getTransition(String transitionId)
                                      throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlTransition.TRANSITION_ID,
                           filter.getEqualOperator(), transitionId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlTransition.ENTITY_NAME,
                                                              true);
    if (coll == null || coll.size() == 0)
      return null;
    else
      return (XpdlTransition)coll.toArray()[0];
  }

  /**
   * DOCUMENT ME!
   *
   * @param fromActivityId DOCUMENT ME!
   * @param toActivityId DOCUMENT ME!
   * @param processId DOCUMENT ME!
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getTransitions(String fromActivityId,
                                          String toActivityId,
                                          String processId, String packageId,
                                          String pkgVersionId)
                                   throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlTransition.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlTransition.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    filter.addSingleFilter(filter.getAndConnector(), XpdlTransition.PROCESS_ID,
                           filter.getEqualOperator(), processId, false);
    if (fromActivityId != null)
      filter.addSingleFilter(filter.getAndConnector(),
                             XpdlTransition.FROM_ACTIVITY_ID,
                             filter.getEqualOperator(), fromActivityId, false);
    if (toActivityId != null)
      filter.addSingleFilter(filter.getAndConnector(),
                             XpdlTransition.TO_ACTIVITY_ID,
                             filter.getEqualOperator(), toActivityId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlTransition.ENTITY_NAME,
                                                              true);
    return coll;
  }

  public static Collection getTransFromTransRefListUId(Long transitionRefListUId,
                                                      String processId, String packageId,
                                                      String pkgVersionId)

                                      throws Throwable {
    Collection transRefColl=getTransitionRefs(transitionRefListUId);
    Collection transUIdColl=new Vector();
    for(Iterator iterator=transRefColl.iterator();iterator.hasNext();){
        transUIdColl.add(((XpdlTransitionRef)iterator.next()).getTransitionId());
    }
    IDataFilter filter=new DataFilterImpl();
    filter.addDomainFilter(null,XpdlTransition.TRANSITION_ID,transUIdColl,false);
    filter.addSingleFilter(filter.getAndConnector(),XpdlTransition.PROCESS_ID,filter.getEqualOperator(),processId,false);
    filter.addSingleFilter(filter.getAndConnector(),XpdlTransition.PACKAGE_ID,filter.getEqualOperator(),packageId,false);
    filter.addSingleFilter(filter.getAndConnector(),XpdlTransition.PKG_VERSION_ID,filter.getEqualOperator(),pkgVersionId,false);
    return UtilEntity.getEntityByFilter(filter,XpdlTransition.ENTITY_NAME,true);
  }

  /**
   * DOCUMENT ME!
   *
   * @param transitionRefListUId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getTransitionRefs(Long transitionRefListUId)
                                      throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlTransitionRef.LIST_UID,
                           filter.getEqualOperator(), transitionRefListUId,
                           false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlTransitionRef.ENTITY_NAME,
                                                              true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param transitionRestrictionListUId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getTransitionRestrictions(Long transitionRestrictionListUId)
                                              throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlTransitionRestriction.LIST_UID,
                           filter.getEqualOperator(),
                           transitionRestrictionListUId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlTransitionRestriction.ENTITY_NAME,
                                                              true);
    return coll;
  }

  /**
   * DOCUMENT ME!
   *
   * @param typeId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static XpdlTypeDeclaration getTypeDeclaration(String typeId)
                                                throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlTypeDeclaration.TYPE_ID,
                           filter.getEqualOperator(), typeId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlTransition.ENTITY_NAME,
                                                              true);
    if (coll == null || coll.size() == 0)
      return null;
    else
      return (XpdlTypeDeclaration)coll.toArray()[0];
  }

  /**
   * DOCUMENT ME!
   *
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static Collection getTypeDeclarations(String packageId,
                                               String pkgVersionId)
                                        throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlTypeDeclaration.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlTypeDeclaration.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlTransition.ENTITY_NAME,
                                                              true);
    return coll;
  }

  public static Collection getParticipants(String processId, String packageId,
                                          String pkgVersionId)
                                        throws Throwable
  {

    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlParticipant.PACKAGE_ID,
                           filter.getEqualOperator(), packageId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlParticipant.PKG_VERSION_ID,
                           filter.getEqualOperator(), pkgVersionId, false);
    filter.addSingleFilter(filter.getAndConnector(),
                           XpdlParticipant.PROCESS_ID,
                           filter.getEqualOperator(), processId, false);

    Collection coll = UtilEntity.getEntityByFilter(filter,XpdlParticipant.ENTITY_NAME,false);
    return coll;
  }

  public static Collection getParticipantList(Long participantListUId)
                                       throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, XpdlParticipantList.LIST_UID,
                           filter.getEqualOperator(), participantListUId,
                           false);
    Collection coll = UtilEntity.getEntityByFilter(filter,
                                                              XpdlParticipantList.ENTITY_NAME,
                                                              true);
    return coll;
  }


  public static XpdlComplexDataType getComplexDataType(Long complexDataTypeUId)
                                        throws Throwable
  {
    return (XpdlComplexDataType)UtilEntity.getEntityByKey(complexDataTypeUId,XpdlComplexDataType.ENTITY_NAME,
                                                              true);
  }

  public static Collection getSubComplexDataType(Long complexDataTypeUId)
                                        throws Throwable
  {
    IDataFilter filter=new DataFilterImpl();
    filter.addSingleFilter(null,XpdlComplexDataType.SUBTYPE_UID,filter.getEqualOperator(),complexDataTypeUId,false);
    return UtilEntity.getEntityByFilter(filter,XpdlComplexDataType.ENTITY_NAME,true);
  }

  public static Collection getComplexDataTypeTree(Long complexDataTypeUId)
                                        throws Throwable
  {
    Collection complexDataTypeColl=new Vector();
    XpdlComplexDataType complexDataType=getComplexDataType(complexDataTypeUId);
    if(complexDataType!=null){
        complexDataTypeColl.add(complexDataType);
        if(complexDataType.getComplexDataTypeUId()!=null)
        {
            Collection coll=getSubComplexDataType(complexDataType.getComplexDataTypeUId());
            for(Iterator iterator=coll.iterator();iterator.hasNext();)
            {
                complexDataType=(XpdlComplexDataType)iterator.next();
                if(complexDataType.getComplexDataTypeUId()!=null)
                    complexDataTypeColl.addAll(getComplexDataTypeTree(complexDataType.getComplexDataTypeUId()));
                else complexDataTypeColl.add(complexDataType);
            }
        }
    }
    return complexDataTypeColl;
  }



  /**
   * DOCUMENT ME!
   *
   * @param packageId DOCUMENT ME!
   * @param pkgVersionId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static List getPackageTree(String packageId, String pkgVersionId)
                             throws Throwable
  {
    List entityList = new LinkedList();
    XpdlPackage xpdlPackage = getPackage(packageId, pkgVersionId);
    parsePackageTree(entityList, xpdlPackage);
    return entityList;
  }


  /**
   * DOCUMENT ME!
   *
   * @param entityList DOCUMENT ME!
   * @param entity DOCUMENT ME!
   * @throws Throwable DOCUMENT ME!
   */
  public static void parsePackageTree(List entityList, IEntity entity)
                               throws Throwable
  {
    if (entity == null)
      return;
    entityList.add(entity);
    if (entity.getEntityName().equals(XpdlPackage.ENTITY_NAME))
    {
      XpdlPackage xpdlPackage = (XpdlPackage)entity;
      Collection coll = getParticipants(XpdlConstants.NA,xpdlPackage.getPackageId(),xpdlPackage.getVersionId());
      parsePackageTree(entityList, coll);
      coll = getParticipantList(xpdlPackage.getResponsibleListUId());
      parsePackageTree(entityList, coll);
      coll = getProcesses(null, xpdlPackage.getPackageId(),
                          xpdlPackage.getVersionId());
      parsePackageTree(entityList, coll);
      coll = getApplications(null, XpdlConstants.NA,
                             xpdlPackage.getPackageId(),
                             xpdlPackage.getVersionId());
      parsePackageTree(entityList, coll);
      coll = getDataFields(XpdlConstants.NA, xpdlPackage.getPackageId(),
                           xpdlPackage.getVersionId());
      parsePackageTree(entityList, coll);
      coll = getExternalPackages(xpdlPackage.getPackageId(),
                                 xpdlPackage.getVersionId());
      parsePackageTree(entityList, coll);
      coll = getTypeDeclarations(xpdlPackage.getPackageId(),
                                 xpdlPackage.getVersionId());
      parsePackageTree(entityList, coll);
    }
    else if (entity.getEntityName().equals(XpdlProcess.ENTITY_NAME))
    {
      XpdlProcess xpdlProcess = (XpdlProcess)entity;
      Collection coll = getActivities(null, xpdlProcess.getProcessId(),
                                      xpdlProcess.getPackageId(),
                                      xpdlProcess.getPkgVersionId());
      parsePackageTree(entityList, coll);
      coll = getParticipants(xpdlProcess.getProcessId(),xpdlProcess.getPackageId(),xpdlProcess.getPkgVersionId());
      parsePackageTree(entityList, coll);
      coll = getParticipantList(xpdlProcess.getResponsibleListUId());
      parsePackageTree(entityList, coll);
      coll = getApplications(null, xpdlProcess.getProcessId(),
                             xpdlProcess.getPackageId(),
                             xpdlProcess.getPkgVersionId());
      parsePackageTree(entityList, coll);
      coll = getDataFields(xpdlProcess.getProcessId(),
                           xpdlProcess.getPackageId(),
                           xpdlProcess.getPkgVersionId());
      parsePackageTree(entityList, coll);
      coll = getFormalParams(XpdlConstants.NA, xpdlProcess.getProcessId(),
                             xpdlProcess.getPackageId(),
                             xpdlProcess.getPkgVersionId());
      parsePackageTree(entityList, coll);
      coll = getTransitions(null, null, xpdlProcess.getProcessId(),
                            xpdlProcess.getPackageId(),
                            xpdlProcess.getPkgVersionId());
      parsePackageTree(entityList, coll);
    }
    else if (entity.getEntityName().equals(XpdlActivity.ENTITY_NAME))
    {
      XpdlActivity xpdlActivity = (XpdlActivity)entity;
      Collection coll = getTransitionRestrictions(xpdlActivity.getTransitionRestrictionListUId());
      parsePackageTree(entityList, coll);
      coll = getSubFlows(xpdlActivity.getActivityId(),
                         xpdlActivity.getProcessId(),
                         xpdlActivity.getPackageId(),
                         xpdlActivity.getPkgVersionId());
      parsePackageTree(entityList, coll);
      coll = getTools(xpdlActivity.getActivityId(),
                      xpdlActivity.getProcessId(), xpdlActivity.getPackageId(),
                      xpdlActivity.getPkgVersionId());
      parsePackageTree(entityList, coll);
    }
    else if (entity.getEntityName().equals(XpdlApplication.ENTITY_NAME))
    {
      XpdlApplication xpdlApplication = (XpdlApplication)entity;
      Collection coll = getFormalParams(xpdlApplication.getApplicationId(),
                                        xpdlApplication.getProcessId(),
                                        xpdlApplication.getPackageId(),
                                        xpdlApplication.getPkgVersionId());
      parsePackageTree(entityList, coll);
    }
    else if (entity.getEntityName().equals(XpdlTransitionRestriction.ENTITY_NAME))
    {
      XpdlTransitionRestriction xpdlTransitionRestriction =
          (XpdlTransitionRestriction)entity;
      Collection coll = getTransitionRefs(xpdlTransitionRestriction.getTransitionRefListUId());
      parsePackageTree(entityList, coll);
    }
    else if (entity.getEntityName().equals(XpdlFormalParam.ENTITY_NAME))
    {
        XpdlFormalParam xpdlFormalParam = (XpdlFormalParam) entity;
        if(xpdlFormalParam.getComplexDataTypeUId()!=null)
        {
            Collection coll =  getSubComplexDataType(xpdlFormalParam.getComplexDataTypeUId());
            parsePackageTree(entityList, coll);
        }
    }
    else if (entity.getEntityName().equals(XpdlDataField.ENTITY_NAME))
    {
        XpdlDataField xpdlDataField = (XpdlDataField) entity;
        if(xpdlDataField.getComplexDataTypeUId()!=null)
        {
            Collection coll =  getSubComplexDataType(xpdlDataField.getComplexDataTypeUId());
            parsePackageTree(entityList, coll);
        }
    }
    else if (entity.getEntityName().equals(XpdlTypeDeclaration.ENTITY_NAME))
    {
        XpdlTypeDeclaration xpdlTypeDeclaration = (XpdlTypeDeclaration) entity;
        if(xpdlTypeDeclaration.getComplexDataTypeUId()!=null)
        {
            Collection coll=getSubComplexDataType(xpdlTypeDeclaration.getComplexDataTypeUId());
            parsePackageTree(entityList, coll);
        }
    }
    else if (entity.getEntityName().equals(XpdlComplexDataType.ENTITY_NAME)){
        XpdlComplexDataType xpdlComplexDataType=(XpdlComplexDataType)entity;
        if(xpdlComplexDataType.getComplexDataTypeUId()!=null)
        {
            Collection coll=getSubComplexDataType(xpdlComplexDataType.getComplexDataTypeUId());
            parsePackageTree(entityList, coll);
        }
    }
  }

  private static void parsePackageTree(List entityList, Collection coll)
                                throws Throwable
  {
    if (coll == null)
      return;
    Iterator iterator = coll.iterator();
    while (iterator.hasNext())
    {
      parsePackageTree(entityList, (IEntity)iterator.next());
    }
  }
}
