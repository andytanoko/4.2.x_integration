/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 23 2003    Neo Sok Lay         Setup fieldIDs for ProcessInstance
 *                                    in a separate table, then combine into
 *                                    main table.
 *                                    Add method: getProcessInstaceFieldID().
 * Jan 19 2006		SC									Comment out G_DIGEST_ALG_CODE
 * Nov 07 2007    Tam Wei Xiang       Add new field IProcessAct.IS_COMPRESSED_REQUIRED
 *                                    to be viewed in UI.                                    
 */
package com.gridnode.gtas.model.rnif;

import com.gridnode.gtas.model.mapper.IMappingFile;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Rnif module.
 * The fieldIDs will be the ones that are available for the client to access.
 */
public class RnifFieldID
{
  private Hashtable _table;
  private Hashtable _processInstanceTable;
  private static final RnifFieldID _self= new RnifFieldID();

  private RnifFieldID()
  {
    _table= new Hashtable();
    _processInstanceTable = new Hashtable();

    _processInstanceTable.put(
      IProcessInstance.ENTITY_NAME,
      new Number[] {
        IProcessInstance.UID,
        IProcessInstance.PROCESS_INSTANCE_ID,
        IProcessInstance.PARTNER,
        IProcessInstance.STATE,
        IProcessInstance.START_TIME,
        IProcessInstance.END_TIME,
        IProcessInstance.RETRY_NUM,
        IProcessInstance.IS_FAILED,
        IProcessInstance.FAIL_REASON,
        IProcessInstance.DETAIL_REASON,
        IProcessInstance.ASSOC_DOCS,
        IProcessInstance.PROCESS_DEF_NAME,
        IProcessInstance.ROLE_TYPE,
        IProcessInstance.USER_TRACKING_ID
        });

    //ProcessDef
    _table
      .put(
        IProcessDef.ENTITY_NAME,
        new Number[] {
          IProcessDef.UID,
          IProcessDef.REQUEST_ACT,
          IProcessDef.RESPONSE_ACT,
          IProcessDef.DEF_NAME,
          IProcessDef.ACTION_TIME_OUT,
          IProcessDef.PROCESS_TYPE,
          IProcessDef.RNIF_VERSION,
          IProcessDef.FROM_PARTNER_ROLE_CLASS_CODE,
          IProcessDef.FROM_BIZ_SERVICE_CODE,
          IProcessDef.FROM_PARTNER_CLASS_CODE,
          IProcessDef.G_TO_PARTNER_ROLE_CLASS_CODE,
          IProcessDef.G_TO_BIZ_SERVICE_CODE,
          IProcessDef.G_TO_PARTNER_CLASS_CODE,
          IProcessDef.G_PROCESS_INDICATOR_CODE,
          IProcessDef.VERSION_IDENTIFIER,
          IProcessDef.G_USAGE_CODE,
      //        IProcessDef.EXPORT_DOC_PATH,
      //        IProcessDef.IMPORT_DOC_PATH,
      IProcessDef.REQUEST_DOC_THIS_DOC_IDENTIFIER,
        IProcessDef.RESPONSE_DOC_THIS_DOC_IDENTIFIER,
        IProcessDef.RESPONSE_DOC_REQUEST_DOC_IDENTIFIER,
        IProcessDef.USER_TRACKING_IDENTIFIER,
        IProcessDef.IS_SYNCHRONOUS
        });

    //ProcessAct
    _table.put(
      IProcessAct.ENTITY_NAME,
      new Number[] {
        IProcessAct.UID,
        IProcessAct.MSG_TYPE,
        IProcessAct.RETRIES,
        IProcessAct.TIME_TO_ACKNOWLEDGE,
        IProcessAct.IS_AUTHORIZATION_REQUIRED,
        IProcessAct.IS_NON_REPUDIATION_REQUIRED,
        IProcessAct.IS_SECURE_TRANSPORT_REQUIRED,
        IProcessAct.BIZ_ACTIVITY_IDENTIFIER,
        IProcessAct.G_BIZ_ACTION_CODE,
        IProcessAct.DICT_FILE,
        IProcessAct.XML_SCHEMA,
        
        /* 19 Jan 06 [SC] use DIGEST_ALGORITHM instead */
//        IProcessAct.G_DIGEST_ALG_CODE,
        IProcessAct.DISABLE_DTD,
        IProcessAct.DISABLE_SCHEMA,
        IProcessAct.DISABLE_ENCRYPTION,
        IProcessAct.DISABLE_SIGNATURE,
        IProcessAct.VALIDATE_AT_SENDER,
        IProcessAct.ONLY_ENCRYPT_PAYLOAD,
        IProcessAct.DIGEST_ALGORITHM,
        IProcessAct.ENCRYPTION_ALGORITHM,
        IProcessAct.ENCRYPTION_ALGORITHM_LENGTH,
        IProcessAct.IS_COMPRESS_REQUIRED
        });
    // MappingFile
    _table.put(
      IMappingFile.ENTITY_NAME,
      new Number[] { IMappingFile.DESCRIPTION, IMappingFile.NAME, IMappingFile.UID });

    /*030723NSL: set up in separate table first, then add to main table
    // 20030116 DDJ: ProcessInstance
    _table.put(
      IProcessInstance.ENTITY_NAME,
      new Number[] {
        IProcessInstance.UID,
        IProcessInstance.PROCESS_INSTANCE_ID,
        IProcessInstance.PARTNER,
        IProcessInstance.STATE,
        IProcessInstance.START_TIME,
        IProcessInstance.END_TIME,
        IProcessInstance.RETRY_NUM,
        IProcessInstance.IS_FAILED,
        IProcessInstance.FAIL_REASON,
        IProcessInstance.DETAIL_REASON,
        IProcessInstance.ASSOC_DOCS,
        IProcessInstance.PROCESS_DEF_NAME,
        IProcessInstance.ROLE_TYPE,
        });
    */
    _table.putAll(_processInstanceTable);
  }

  public static Hashtable getEntityFieldID()
  {
    return _self._table;
  }

  public static Hashtable getProcessInstanceFieldID()
  {
    return _self._processInstanceTable;
  }

}