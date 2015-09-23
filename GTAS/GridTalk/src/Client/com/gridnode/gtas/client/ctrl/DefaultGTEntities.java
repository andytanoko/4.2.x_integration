/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultGTEntities.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-20     Neo Sok Lay         Created
 * 2002-05-21     Andrew Hill         Made class package protected
 * 2002-05-29     Daniel D'Cotta      Added mappings for GridForm
 * 2002-??-??     Andrew Hill         Added entries
 * 2002-??-??     Daniel D'Cotta      Added entries
 * 2003-07-08     Andrew Hill         Removed GF entries
 * 2003-10-10     Neo Sok Lay         Add mapping for SearchedChannelInfo.
 * 2004-02-12     Neo Sok Lay         Add mapping for ScheduledTask.
 * 2005-01-06			SC									In initEntityMapping method, add mapping for MessageProperty.
 * 2006-03-14     Tam Wei Xiang       Added mapping for SchemaMappingFile
 * 2006-09-21     Tam Wei Xiang       Merge From estore stream
 * 2006-11-15     Regina Zeng         Add mapping for Gdoc detail
 * 2008-08-01	  Wong Yee Wah		#38    Add mapping for Certificate Swapping
 * 2008-11-12     Wong Yee Wah        added AS2DocTypeMapping
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gridnode.gtas.model.acl.IAccessRight;
import com.gridnode.gtas.model.acl.IFeature;
import com.gridnode.gtas.model.acl.IRole;
import com.gridnode.gtas.model.activation.IActivationRecord;
import com.gridnode.gtas.model.activation.IGridNodeActivation;
import com.gridnode.gtas.model.activation.ISearchGridNodeCriteria;
import com.gridnode.gtas.model.activation.ISearchGridNodeQuery;
import com.gridnode.gtas.model.activation.ISearchedGridNode;
import com.gridnode.gtas.model.alert.IAction;
import com.gridnode.gtas.model.alert.IAlert;
import com.gridnode.gtas.model.alert.IAlertCategory;
import com.gridnode.gtas.model.alert.IAlertTrigger;
import com.gridnode.gtas.model.alert.IAlertType;
import com.gridnode.gtas.model.alert.IEmailConfig;
import com.gridnode.gtas.model.alert.IJmsDestination;
import com.gridnode.gtas.model.alert.IMessageProperty;
import com.gridnode.gtas.model.alert.IMessageTemplate;
import com.gridnode.gtas.model.backend.IPort;
import com.gridnode.gtas.model.backend.IRfc;
import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.model.bizreg.IDomainIdentifier;
import com.gridnode.gtas.model.bizreg.IRegistryConnectInfo;
import com.gridnode.gtas.model.bizreg.IWhitePage;
import com.gridnode.gtas.model.certificate.ICertificate;
import com.gridnode.gtas.model.certificate.ICertificateSwapping;
import com.gridnode.gtas.model.channel.IAS2PackagingInfoExtension;
import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.gtas.model.channel.ICommInfo;
import com.gridnode.gtas.model.channel.IPackagingInfo;
import com.gridnode.gtas.model.channel.ISecurityInfo;
import com.gridnode.gtas.model.connection.IConnectionSetupParam;
import com.gridnode.gtas.model.connection.IConnectionSetupResult;
import com.gridnode.gtas.model.connection.IJmsRouter;
import com.gridnode.gtas.model.connection.INetworkSetting;
import com.gridnode.gtas.model.docalert.IReminderAlert;
import com.gridnode.gtas.model.docalert.IResponseTrackRecord;
import com.gridnode.gtas.model.document.IAttachment;
import com.gridnode.gtas.model.document.IDocumentType;
import com.gridnode.gtas.model.document.IAS2DocTypeMapping;
import com.gridnode.gtas.model.document.IFileType;
import com.gridnode.gtas.model.document.IGridDocument;
import com.gridnode.gtas.model.enterprise.ISearchRegistryCriteria;
import com.gridnode.gtas.model.enterprise.ISearchRegistryQuery;
import com.gridnode.gtas.model.enterprise.ISearchedBusinessEntity;
import com.gridnode.gtas.model.enterprise.ISearchedChannelInfo;
import com.gridnode.gtas.model.gridnode.ICompanyProfile;
import com.gridnode.gtas.model.gridnode.IGnCategory;
import com.gridnode.gtas.model.gridnode.IGridNode;
import com.gridnode.gtas.model.housekeeping.IHousekeepingInfo;
import com.gridnode.gtas.model.locale.ICountryCode;
import com.gridnode.gtas.model.locale.ILanguageCode;
import com.gridnode.gtas.model.mapper.IGridTalkMappingRule;
import com.gridnode.gtas.model.mapper.IMappingFile;
import com.gridnode.gtas.model.mapper.IMappingRule;
import com.gridnode.gtas.model.mapper.ISchemaMapping;
import com.gridnode.gtas.model.partner.IPartner;
import com.gridnode.gtas.model.partner.IPartnerGroup;
import com.gridnode.gtas.model.partner.IPartnerType;
import com.gridnode.gtas.model.partnerfunction.IPartnerFunction;
import com.gridnode.gtas.model.partnerfunction.IWorkflowActivity;
import com.gridnode.gtas.model.partnerprocess.IBizCertMapping;
import com.gridnode.gtas.model.partnerprocess.IProcessMapping;
import com.gridnode.gtas.model.partnerprocess.ITrigger;
import com.gridnode.gtas.model.registration.IRegistrationInfo;
import com.gridnode.gtas.model.rnif.IProcessAct;
import com.gridnode.gtas.model.rnif.IProcessDef;
import com.gridnode.gtas.model.rnif.IProcessInstance;
import com.gridnode.gtas.model.scheduler.ISchedule;
import com.gridnode.gtas.model.searchquery.ICondition;
import com.gridnode.gtas.model.searchquery.ISearchQuery;
import com.gridnode.gtas.model.servicemgmt.IServiceAssignment;
import com.gridnode.gtas.model.servicemgmt.IWebService;
import com.gridnode.gtas.model.user.IUserAccount;
import com.gridnode.gtas.model.user.IUserAccountState;
import com.gridnode.gtas.model.userprocedure.IJavaProcedure;
import com.gridnode.gtas.model.userprocedure.IParamDef;
import com.gridnode.gtas.model.userprocedure.IProcedureDefFile;
import com.gridnode.gtas.model.userprocedure.IReturnDef;
import com.gridnode.gtas.model.userprocedure.IShellExecutable;
import com.gridnode.gtas.model.userprocedure.ISoapProcedure;
import com.gridnode.gtas.model.userprocedure.IUserProcedure;
import com.gridnode.gtas.model.channel.IFlowControlInfo;
import com.gridnode.gtas.model.dbarchive.IProcessInstanceMetaInfo;
import com.gridnode.gtas.model.dbarchive.docforpi.IAuditFileMetaInfo;
import com.gridnode.gtas.model.dbarchive.docforpi.IDocumentMetaInfo;
import com.gridnode.gtas.model.dbarchive.searchpage.*;
import com.gridnode.gtas.server.dbarchive.model.IArchiveMetaInfo;


/**
 * This class defines the mappings between the names of the entities at
 * the client and the server sites.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
class DefaultGTEntities
{
  private Hashtable _entityMapping = new Hashtable();
  private static DefaultGTEntities _self;

  private DefaultGTEntities()
  {
    initEntityMapping();
  }

  public static DefaultGTEntities instance()
  {
    if (_self == null)
      _self = new DefaultGTEntities();

    return _self;
  }

  private void initEntityMapping()
  {
    _entityMapping.put(IGTEntity.ENTITY_USER,                       IUserAccount.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ACCOUNT_STATE,              IUserAccountState.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ACCESS_RIGHT,               IAccessRight.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_FEATURE,                    IFeature.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ROLE,                       IRole.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PARTNER_TYPE,               IPartnerType.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PARTNER_GROUP,              IPartnerGroup.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PARTNER,                    IPartner.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_DOCUMENT_TYPE,              IDocumentType.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_GRIDTALK_MAPPING_RULE,      IGridTalkMappingRule.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_MAPPING_RULE,               IMappingRule.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_MAPPING_FILE,               IMappingFile.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_CHANNEL_INFO,               IChannelInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_COMM_INFO,                  ICommInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_BUSINESS_ENTITY,            IBusinessEntity.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_WHITE_PAGE,                 IWhitePage.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_FILE_TYPE,                  IFileType.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_GRID_DOCUMENT,              IGridDocument.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_COMPANY_PROFILE,            ICompanyProfile.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_GN_CATEGORY,                IGnCategory.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_COUNTRY_CODE,               ICountryCode.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_LANGUAGE_CODE,              ILanguageCode.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SECURITY_INFO,              ISecurityInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PACKAGING_INFO,             IPackagingInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_CERTIFICATE,                ICertificate.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_CERTIFICATE_SWAPPING,       ICertificateSwapping.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PARTNER_FUNCTION,           IPartnerFunction.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_WORKFLOW_ACTIVITY,          IWorkflowActivity.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_REGISTRATION_INFO,          IRegistrationInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_TRIGGER,                    ITrigger.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PROCEDURE_DEF_FILE,         IProcedureDefFile.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_USER_PROCEDURE,             IUserProcedure.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SHELL_EXECUTABLE,           IShellExecutable.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_JAVA_PROCEDURE,             IJavaProcedure.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SOAP_PROCEDURE,             ISoapProcedure.ENTITY_NAME); //20030730AH
    _entityMapping.put(IGTEntity.ENTITY_PARAM_DEF,                  IParamDef.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_RETURN_DEF,                 IReturnDef.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SEARCHED_GRIDNODE,          ISearchedGridNode.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_GRIDNODE_ACTIVATION,        IGridNodeActivation.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SEARCH_GRIDNODE_CRITERIA,   ISearchGridNodeCriteria.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SEARCH_GRIDNODE_QUERY,      ISearchGridNodeQuery.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_NETWORK_SETTING,            INetworkSetting.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_CONNECTION_SETUP_RESULT,    IConnectionSetupResult.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_CONNECTION_SETUP_PARAM,     IConnectionSetupParam.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_JMS_ROUTER,                 IJmsRouter.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_GRIDNODE,                   IGridNode.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ACTIVATION_RECORD,          IActivationRecord.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_GRIDNODE_ACTIVATION,        IGridNodeActivation.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PROCESS_MAPPING,            IProcessMapping.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PROCESS_DEF,                IProcessDef.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PROCESS_ACT,                IProcessAct.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ATTACHMENT,                 IAttachment.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_RFC,                        IRfc.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_PORT,                       IPort.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_BIZ_CERT_MAPPING,           IBizCertMapping.ENTITY_NAME); //20030114AH
    _entityMapping.put(IGTEntity.ENTITY_PROCESS_INSTANCE,           IProcessInstance.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ALERT,                      IAlert.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ACTION,                     IAction.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_MESSAGE_TEMPLATE,           IMessageTemplate.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_RESPONSE_TRACK_RECORD,      IResponseTrackRecord.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_REMINDER_ALERT,             IReminderAlert.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ALERT_TYPE,                 IAlertType.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ALERT_CATEGORY,             IAlertCategory.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ALERT_TRIGGER,              IAlertTrigger.ENTITY_NAME); //20030506AH
    _entityMapping.put(IGTEntity.ENTITY_REGISTRY_CONNECT_INFO,      IRegistryConnectInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SEARCHED_BUSINESS_ENTITY,   ISearchedBusinessEntity.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SEARCH_REGISTRY_CRITERIA,   ISearchRegistryCriteria.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SEARCH_REGISTRY_QUERY,      ISearchRegistryQuery.ENTITY_NAME); // 20030915 DDJ
    _entityMapping.put(IGTEntity.ENTITY_SEARCHED_CHANNEL_INFO,      ISearchedChannelInfo.ENTITY_NAME); // 20031010 NSL
    _entityMapping.put(IGTEntity.ENTITY_CONDITION,                  ICondition.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SEARCH_QUERY,               ISearchQuery.ENTITY_NAME); // 20031027 DDJ
    _entityMapping.put(IGTEntity.ENTITY_AS2_PACKAGING_INFO_EXTENSION, IAS2PackagingInfoExtension.ENTITY_NAME); // 20030915 DDJ
    _entityMapping.put(IGTEntity.ENTITY_FLOW_CONTROL_INFO,          IFlowControlInfo.ENTITY_NAME); // 20031222 DDJ
    _entityMapping.put(IGTEntity.ENTITY_HOUSE_KEEPING,              IHousekeepingInfo.ENTITY_NAME); // 20040116 DDJ
    _entityMapping.put(IGTEntity.ENTITY_DOMAIN_IDENTIFIER,          IDomainIdentifier.ENTITY_NAME); // 20040102 DDJ
    _entityMapping.put(IGTEntity.ENTITY_SCHEDULED_TASK,             ISchedule.ENTITY_NAME); // 20040212NSL
    _entityMapping.put(IGTEntity.ENTITY_SCHEDULED_ARCHIVE,          ISchedule.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ARCHIVE,                    IArchiveMetaInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_WEBSERVICE,                 IWebService.ENTITY_NAME); // 20040209 MAHESH
    _entityMapping.put(IGTEntity.ENTITY_SERVICE_ASSIGNMENT,         IServiceAssignment.ENTITY_NAME); // 20040209 MAHESH
    _entityMapping.put(IGTEntity.ENTITY_EMAIL_CONFIG,               IEmailConfig.ENTITY_NAME); // 20040701 MAHESH
    _entityMapping.put(IGTEntity.ENTITY_JMS_DESTINATION,            IJmsDestination.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_MESSAGE_PROPERTY,           IMessageProperty.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_SCHEMA_MAPPING_FILE,        ISchemaMapping.ENTITY_NAME); //20060314 TWX
    // add for estore
    _entityMapping.put(IGTEntity.ENTITY_ES_PI,                      IProcessInstanceMetaInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_ES_DOC,                     IDocumentMetaInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_FIELD_VALUE_COLLECTION,     IFieldValueCollection.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_AUDIT_FILE,     						IAuditFileMetaInfo.ENTITY_NAME);
    _entityMapping.put(IGTEntity.ENTITY_GDOC_DETAIL,                IGridDocument.ENTITY_NAME); //20061115 RZ
    
    _entityMapping.put(IGTEntity.ENTITY_AS2_DOC_TYPE_MAPPING,       IAS2DocTypeMapping.ENTITY_NAME);
  }

  public static String getServerMappedName(String entityType)
  {
    //20030529AH - Renamed entityName to entityType
    String serverMappedName = (String)instance()._entityMapping.get(entityType);
    if(serverMappedName == null)
    {
      throw new java.lang.UnsupportedOperationException("Entity type:" + entityType
                                                    + " does not have a server mapped name");
    }
    return serverMappedName;
  }

  public static String getClientMappedName(String entityName)
  { //20030529AH (This is inefficient. Could use refactoring but so far only one thing uses it)
    //String clientMappedName = null;
    Set entrySet = instance()._entityMapping.entrySet();
    Iterator iterator = entrySet.iterator();
    while(iterator.hasNext())
    {
      Map.Entry entry = (Map.Entry)iterator.next();
      String serverMappedName = (String)entry.getValue();
      if(entityName.equals(serverMappedName))
      {
        String entityType = (String)entry.getKey();
        return entityType;
      }
    }
    throw new java.lang.UnsupportedOperationException("Entity name:"
                                                      + entityName
                                                      + " does not have a client mapped name");
  }
}
