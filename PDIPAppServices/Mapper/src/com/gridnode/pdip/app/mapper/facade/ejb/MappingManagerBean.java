/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 * Jun 10 2003    Koh Han Sing        Add in XpathMapping
 * Mar 07 2006    Neo Sok Lay         Add importSchemas() method.
 *                                    Use generics.
 * Dec 05 2007    Tam Wei Xiang       Fix GNDB00028483: XSLT transformation.
 */
package com.gridnode.pdip.app.mapper.facade.ejb;

import java.io.File;
import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.mapper.exception.BatchImportSchemasException;
import com.gridnode.pdip.app.mapper.helpers.*;
import com.gridnode.pdip.app.mapper.model.*;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.base.xml.helpers.InitReader;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This bean provides the app service layer implementation of the
 * mapper module. It serves as the facade to the business methods of
 * this module.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0
 * @since 2.0
 */
public class MappingManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6063125152225312476L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ********************* Implementing methods in IMappingManagerObj

  // ********************* Methods for MappingFile

  /**
   * Create a new MappingFile.
   *
   * @param mappingFile The MappingFile entity.
   * @return the MappingFile created.
   */
  public MappingFile createMappingFile(MappingFile mappingFile)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[MappingManagerBean.createMappingFile] Enter");
    MappingFile newMappingFile = null;

    try
    {
      newMappingFile =
        (MappingFile)getMappingFileEntityHandler().createEntity(mappingFile);
      return newMappingFile;
    }
    catch (CreateException ex)
    {
      Logger.warn("[MappingManagerBean.createMappingFile] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[MappingManagerBean.createMappingFile] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.createMappingFile] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.createMappingFile(MappingFile) Error ",
        ex);
    }
    finally
    {
      Logger.log("[MappingManagerBean.createMappingFile] Exit");
    }
  }

  /**
   * Update a MappingFile
   *
   * @param mappingFile The MappingFile entity with changes.
   */
  public void updateMappingFile(MappingFile mappingFile)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.updateMappingFile] Enter");

    try
    {
      getMappingFileEntityHandler().update(mappingFile);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[MappingManagerBean.updateMappingFile] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.updateMappingFile] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.updateMappingFile(MappingFile) Error ",
        ex);
    }
    finally
    {
    	Logger.log("[MappingManagerBean.updateMappingFile] Exit");
    }
  }

  /**
   * Delete a MappingFile.
   *
   * @param mappingFileUId The UID of the MappingFile to delete.
   */
  public void deleteMappingFile(Long mappingFileUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.deleteMappingFile] Enter");

    try
    {
      MappingFile mappingFile =
        (MappingFile)getMappingFileEntityHandler().getEntityByKeyForReadOnly(mappingFileUId);
      Collection list = getMappingRuleEntityHandler().findByMappingFile(mappingFile);
      if (list.isEmpty())
      {
        getMappingFileEntityHandler().remove(mappingFileUId);
      }
      else
      {
        throw new RemoveException("MappingFile "+
                                  mappingFile.getFieldValue(MappingFile.NAME)+
                                  " is in used by some MappingRule");
      }
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[MappingManagerBean.deleteMappingFile] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[MappingManagerBean.deleteMappingFile] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.deleteMappingFile] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.deleteMappingFile(mappingFileUId) Error ",
        ex);
    }
    finally
    {
    	Logger.log("[MappingManagerBean.deleteMappingFile] Exit");
    }
  }

  /**
   * Find a MappingFile using the MappingFile UID.
   *
   * @param mappingFileUId The UID of the MappingFile to find.
   * @return The MappingFile found, or <B>null</B> if none exists with that
   * UID.
   */
  public MappingFile findMappingFile(Long mappingFileUId)
    throws FindEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.findMappingFile] UID: "+mappingFileUId);

    MappingFile mappingFile = null;

    try
    {
      mappingFile = (MappingFile)getMappingFileEntityHandler().getEntityByKeyForReadOnly(mappingFileUId);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFile] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFile] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFile] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingFile(mappingFileUId) Error ",
        ex);
    }

    return mappingFile;
  }

  /**
   * Find a MappingFile using the MappingFile Name.
   *
   * @param mappingFileName The Name of the MappingFile to find.
   * @return The MappingFile found, or <B>null</B> if none exists.
   */
  public MappingFile findMappingFile(String mappingFileName)
    throws FindEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.findMappingFile] Document Type Name: "
      +mappingFileName);

    MappingFile mappingFile = null;

    try
    {
      mappingFile = getMappingFileEntityHandler().findByMappingFileName(mappingFileName);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFile] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFile] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFile] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingFile(mappingFileName) Error ",
        ex);
    }

    return mappingFile;
  }

  /**
   * Find a number of MappingFile with the matching MappingFile Type.
   *
   * @param mappingFileType The Type of the MappingFile to find.
   * @return a Collection of MappingFile found, or empty collection if none
   * exists with that Type.
   */
  public Collection<MappingFile> findMappingFile(Short mappingFileType)
    throws FindEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.findMappingFile] UID: "+mappingFileType);

    Collection<MappingFile> mappingFiles = null;

    try
    {
      mappingFiles = getMappingFileEntityHandler().findByMappingFileType(mappingFileType);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFile] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFile] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFile] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingFile(mappingFileUId) Error ",
        ex);
    }

    return mappingFiles;
  }

  /**
   * Find a number of MappingFile that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of MappingFile found, or empty collection if none
   * exists.
   */
  public Collection<MappingFile> findMappingFiles(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[MappingManagerBean.findMappingFiles] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection<MappingFile> mappingFiles = null;
    try
    {
      mappingFiles = getMappingFileEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFiles] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFiles] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFiles] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingFiles(filter) Error ",
        ex);
    }

    return mappingFiles;
  }

  /**
   * Find a number of MappingFile that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of MappingFile found, or empty collection if
   * none exists.
   */
  public Collection<Long> findMappingFilesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[MappingManagerBean.findMappingFilesKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection<Long> mappingFilesKeys = null;
    try
    {
      mappingFilesKeys = getMappingFileEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFilesKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFilesKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingFilesKeys] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingFilesKeys(filter) Error ",
        ex);
    }

    return mappingFilesKeys;
  }

  // ********************* Methods for MappingRules

  /**
   * Create a new MappingRule.
   *
   * @param mappingRule The MappingRule entity.
   * @return the MappingRule created.
   */
  public MappingRule createMappingRule(MappingRule mappingRule)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[MappingManagerBean.createMappingRule] Enter");

    MappingRule newMappingRule = null;

    try
    {
      newMappingRule =
        (MappingRule)getMappingRuleEntityHandler().createEntity(mappingRule);
      return newMappingRule;
    }
    catch (CreateException ex)
    {
      Logger.warn("[MappingManagerBean.createMappingRule] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[MappingManagerBean.createMappingRule] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.createMappingRule] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.createMappingRule(MappingRule) Error ",
        ex);
    }
    finally
    {
    	Logger.log("[MappingManagerBean.createMappingRule] Exit");
    }
  }

  /**
   * Update a MappingRule
   *
   * @param mappingRule The MappingRule entity with changes.
   */
  public void updateMappingRule(MappingRule mappingRule)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.updateMappingRule] Enter");

    try
    {
      getMappingRuleEntityHandler().update(mappingRule);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[MappingManagerBean.updateMappingRule] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.updateMappingRule] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.updateMappingRule(MappingRule) Error ",
        ex);
    }
    finally
    {
    	Logger.log("[MappingManagerBean.updateMappingRule] Exit");
    }
  }

  /**
   * Delete a MappingRule.
   *
   * @param mappingRuleUId The UID of the MappingRule to delete.
   */
  public void deleteMappingRule(Long mappingRuleUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.deleteMappingRule] Enter");

    try
    {
      getMappingRuleEntityHandler().remove(mappingRuleUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[MappingManagerBean.deleteMappingRule] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[MappingManagerBean.deleteMappingRule] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.deleteMappingRule] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.deleteMappingRule(mappingRuleUId) Error ",
        ex);
    }
    finally
    {
    	Logger.log("[MappingManagerBean.deleteMappingRule] Exit");
    }
  }

  /**
   * Find a MappingRule using the MappingRule UID.
   *
   * @param mappingRuleUId The UID of the MappingRule to find.
   * @return The MappingRule found, or <B>null</B> if none exists with that
   * UID.
   */
  public MappingRule findMappingRule(Long mappingRuleUId)
    throws FindEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.findMappingRule] UID: "+mappingRuleUId);

    MappingRule mappingRule = null;

    try
    {
      mappingRule = (MappingRule)getMappingRuleEntityHandler().getEntityByKeyForReadOnly(mappingRuleUId);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRule] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRule] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRule] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingRule(mappingRuleUId) Error ",
        ex);
    }

    return mappingRule;
  }

  /**
   * Find a MappingRule using the MappingRule Name.
   *
   * @param mappingRuleName The Name of the MappingRule to find.
   * @return The MappingRule found, or <B>null</B> if none exists.
   */
  public MappingRule findMappingRule(String mappingRuleName)
    throws FindEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.findMappingRule] Mapping Rule Name: "
      +mappingRuleName);

    MappingRule mappingRule = null;

    try
    {
      mappingRule = getMappingRuleEntityHandler().findByMappingRuleName(mappingRuleName);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRule] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRule] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRule] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingRule(mappingFileName) Error ",
        ex);
    }

    return mappingRule;
  }

  /**
   * Find a number of MappingRule with the matching MappingRule Type.
   *
   * @param mappingRuleType The Type of the MappingRule to find.
   * @return a Collection of MappingRule found, or empty collection if none
   * exists with that Type.
   */
  public Collection<MappingRule> findMappingRule(Short mappingRuleType)
    throws FindEntityException, SystemException
  {
    Logger.log("[MappingManagerBean.findMappingRule] UID: "+mappingRuleType);

    Collection<MappingRule> mappingRules = null;

    try
    {
      mappingRules = getMappingRuleEntityHandler().findByMappingRuleType(mappingRuleType);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRule] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRule] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRule] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingRule(mappingRuleUId) Error ",
        ex);
    }

    return mappingRules;
  }

  /**
   * Find a number of MappingRule that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of MappingRule found, or empty collection if none
   * exists.
   */
  public Collection<MappingRule> findMappingRules(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[MappingManagerBean.findMappingRules] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection<MappingRule> mappingRules = null;
    try
    {
      mappingRules = getMappingRuleEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRules] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRules] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRules] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingRules(filter) Error ",
        ex);
    }

    return mappingRules;
  }

  /**
   * Find a number of MappingRule that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of MappingRule found, or empty collection if
   * none exists.
   */
  public Collection<Long> findMappingRulesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[MappingManagerBean.findMappingRulesKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection<Long> mappingRulesKeys = null;
    try
    {
      mappingRulesKeys = getMappingRuleEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRulesKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRulesKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[MappingManagerBean.findMappingRulesKeys] Error ", ex);
      throw new SystemException(
        "MappingManagerBean.findMappingRulesKeys(filter) Error ",
        ex);
    }

    return mappingRulesKeys;
  }

  // ********************* Methods for XpathMapping

  /**
   * Create a new XpathMapping.
   *
   * @param xpathMapping The XpathMapping entity.
   */
  public IEntity createXpathMapping(XpathMapping xpathMapping)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.debug("[GridTalkMappingManagerBean.createXpathMapping] Enter");

    IEntity entity = null;

    try
    {
      entity = getXpathMappingEntityHandler().createEntity(xpathMapping);
      return entity;
    }
    catch (CreateException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.createXpathMapping] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.createXpathMapping] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.createXpathMapping] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.createXpathMapping(xpathMapping) Error ",
        ex);
    }
    finally
    {
    	Logger.debug("[GridTalkMappingManagerBean.createXpathMapping] Exit");
    }
  }

  /**
   * Update a XpathMapping
   *
   * @param xpathMapping The XpathMapping entity with changes.
   */
  public void updateXpathMapping(XpathMapping xpathMapping)
    throws UpdateEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.updateXpathMapping] Enter");

    try
    {
      getXpathMappingEntityHandler().update(xpathMapping);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.updateXpathMapping] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.updateXpathMapping] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.updateXpathMapping(xpathMapping) Error ",
        ex);
    }
    finally
    {
    	Logger.log("[GridTalkMappingManagerBean.updateXpathMapping] Exit");
    }
  }

  /**
   * Delete a XpathMapping.
   *
   * @param xpathMappingUId The UID of the XpathMapping to delete.
   */
  public void deleteXpathMapping(Long xpathMappingUId)
    throws DeleteEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.deleteXpathMapping] Enter");

    try
    {
      getXpathMappingEntityHandler().remove(xpathMappingUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.deleteXpathMapping] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.deleteXpathMapping] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.deleteXpathMapping] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.deleteXpathMapping(xpathMappingUId) Error ",
        ex);
    }
    finally
    {
    	Logger.log("[GridTalkMappingManagerBean.deleteXpathMapping] Exit");
    }
  }

  /**
   * Find a XpathMapping using the XpathMapping Uid.
   *
   * @param xpathMappingUid The Uid of the XpathMapping to find.
   * @return The XpathMapping found, or <B>null</B> if none exists with that
   * Uid.
   */
  public XpathMapping findXpathMapping(Long xpathMappingUid)
    throws FindEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.findXpathMapping] Uid: "+xpathMappingUid);

    XpathMapping xpathMapping = null;

    try
    {
      xpathMapping = (XpathMapping)getXpathMappingEntityHandler().getEntityByKeyForReadOnly(xpathMappingUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMapping] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMapping] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMapping] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findXpathMapping(xpathMappingUid) Error ",
        ex);
    }

    return xpathMapping;
  }

  /**
   * Find a XpathMapping using the Xpath Uid.
   *
   * @param xpathUid The Uid of the Xpath to find.
   * @return The XpathMapping found, or <B>null</B> if none exists with that
   * Uid.
   */
  public XpathMapping findXpathUid(Long xpathUid)
    throws FindEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.findXpathMapping] Uid: "+xpathUid);

    XpathMapping xpathMapping = null;

    try
    {
      xpathMapping = getXpathMappingEntityHandler().findByXpathUid(xpathUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMapping] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMapping] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMapping] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findXpathMapping(xpathUid) Error ",
        ex);
    }

    return xpathMapping;
  }
  /**
   * Find a XpathMapping using the Root element name.
   *
   * @param rootName The name of the root element to find.
   * @return The XpathMapping found, or <B>null</B> if none exists with that
   * root element name.
   */
  public XpathMapping findXpathMapping(String rootName)
    throws FindEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.findXpathMapping] rootName: "+rootName);

    XpathMapping xpathMapping = null;

    try
    {
      xpathMapping = getXpathMappingEntityHandler().findByRootName(rootName);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMapping] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMapping] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMapping] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findXpathMapping(rootName) Error ",
        ex);
    }

    return xpathMapping;
  }

  /**
   * Find a number of XpathMapping that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of XpathMapping found, or empty collection if none
   * exists.
   */
  public Collection<XpathMapping> findXpathMappings(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.debug( "[GridTalkMappingManagerBean.findXpathMappings] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection<XpathMapping> xpathMappings = null;
    try
    {
      xpathMappings =
        getXpathMappingEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMappings] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMappings] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMappings] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findGridTalkMappingRules(filter) Error ",
        ex);
    }

    return xpathMappings;
  }

  /**
   * Find a number of XpathMapping that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of XpathMapping found, or empty collection if
   * none exists.
   */
  public Collection<Long> findXpathMappingsKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.debug( "[GridTalkMappingManagerBean.findXpathMappingsKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection<Long> xpathMappingsKeys = null;
    try
    {
      xpathMappingsKeys =
        getXpathMappingEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMappingsKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMappingsKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findXpathMappingsKeys] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findXpathMappingsKeys(filter) Error ",
        ex);
    }

    return xpathMappingsKeys;
  }

  /**
   * This method will retreive the root element name from the udoc and search
   * for the matching xpath file. It will then retreive the partner Id using
   * the xpath defined
   *
   * @param udoc The user document to extract the information from.
   * @return a list of values which includes partnerId, document type, partner
   * group and document number.
   */
  public String[] getXpathInfo(File udoc)
    throws Exception
  {
    String[] values = new String[4];
    String rootName = getXMLManager().getRootName(udoc);
    XpathMapping xpathMapping = findXpathMapping(rootName);
    if (xpathMapping != null)
    {
      Long xpathUid = xpathMapping.getXpathUid();
      MappingFile xpath = findMappingFile(xpathUid);
      File xpathFile = FileUtil.getFile(IMapperPathConfig.PATH_XPATH,
                                        xpath.getFilename());
      String xpathFullpath = xpathFile.getAbsolutePath();
      String udocFullpath = udoc.getAbsolutePath();

      values[0] = getXpathValue(udocFullpath, xpathFullpath, IXpathMappingFile.DOC_TYPE_XPATH);
      values[1] = getXpathValue(udocFullpath, xpathFullpath, IXpathMappingFile.PARTNER_ID_XPATH);
      values[2] = getXpathValue(udocFullpath, xpathFullpath, IXpathMappingFile.PARTNER_TYPE_XPATH);
      values[3] = getXpathValue(udocFullpath, xpathFullpath, IXpathMappingFile.DOC_NUM_XPATH);
    }
    else
    {
      throw new ApplicationException("XpathMapping not found for rootName : "+rootName);
    }
    return values;
  }
  
  /**
   * TWX: 15 NOV 2005
   * It will retrieve the values of the elements which specified in the xpath mapping
   * file from the user document.
   * 
   * @param udoc
   * @return Hashtable key is the uid of grid doc entity or null if no xpath mapping 
   *         has been found for the document.
   */
  public Hashtable getUDocElementInfo(File udoc)
  	throws Exception
  {
  	String rootName = getXMLManager().getRootName(udoc);
  	Logger.log("[MappingManagerBean.getUDocElementInfo ] root name is "+rootName);
  	XpathMapping xpathMapping = findXpathMapping(rootName);
  	if (xpathMapping != null)
    {
      Long xpathUid = xpathMapping.getXpathUid();
      MappingFile xpath = findMappingFile(xpathUid);
      File xpathFile = FileUtil.getFile(IMapperPathConfig.PATH_XPATH,
                                        xpath.getFilename());
      String udocFullpath = udoc.getAbsolutePath();
      
      InitReader xpathReader = new InitReader();
      xpathReader.loadProperties(xpathFile);
      
      return getXPathRepresentContent(xpathReader, udocFullpath, rootName);
 
    }
    else
    {
    	//TWX 20 Mar 2006 : Not all docs(eg non-RN doc) is required xpath extraction
      Logger.log("[MappingManagerBean.getUDocElementInfo ]XpathMapping not found for rootName : "+rootName);
      return null;
    }
  }
  
  /**
   * TWX: 15 NOV 2005
   * @param xpathReader
   * @param udocFullPath
   * @param udocRootName
   * @return Hashtable of fieldID (String) and corresponding value (String)
   */
  private Hashtable<String,String> getXPathRepresentContent(InitReader xpathReader, String udocFullPath, String udocRootName)
  	throws Exception
  {
  	Enumeration enu = xpathReader.getKeys();
  	Hashtable<String,String> xpathContent = new Hashtable<String,String>();
  	
  	List values = null;
  	while(enu.hasMoreElements())
  	{
  		String key = (String)enu.nextElement();
  		String value = xpathReader.getProperty(key);
  		
  		//user has specify to overwrite the value eg. overwrite the partnerID 
  		if (value.startsWith("\"") && value.endsWith("\""))
      {
        value = value.substring(1, value.length()-1);
      }
  		else //user want to extract value using XPATH from the udoc.
  		{
  			String fullPath = "//"+xpathReader.getProperty(key);
  			Logger.log("MappingManagerBean full path is "+fullPath);
  			values = getXMLManager().getXPathValues(udocFullPath, fullPath);
  		
  			if(!values.isEmpty())
  			{
  				value = (String)values.get(0);
  			}
  			else
  			{
  				value = null;
  			}
  		}
  		Logger.log("[MappingManagerBean.getXPathRepresentContent] **key is "+key+" **value is "+value);
  		xpathContent.put(key, value);
  	}
  	return xpathContent;
  }
  
  private String getXpathValue(String udocFullpath, String xpathFullpath, String xpath)
    throws Exception
  {
    String value = null;
    List values = getXMLManager().getXPathValues(xpathFullpath, xpath);
    if (!values.isEmpty())
    {
      value = values.get(0).toString();
      if (value.startsWith("\"") && value.endsWith("\""))
      {
        value = value.substring(1, value.length()-1);
      }
      else
      {
        values = getXMLManager().getXPathValues(udocFullpath, value);
        if (!values.isEmpty())
        {
          value = values.get(0).toString();
        }
        else
        {
          value = null;
        }
      }
    }
    return value;
  }

  // ********************* Methods for Mapping Services

  /**
   * Converts a file using the conversion rule.
   *
   * @param inputFile   The full path of the file to convert.
   * @param mappingRule The mappingRule to use for the conversion.
   * @return the File of the converted file.
   */
  public File convert(String inputFile, MappingRule mappingRule, MappingMetaData metaData)
    throws Exception
  {
    MappingFile mappingFile = mappingRule.getMappingFile();
    String conRule = mappingFile.getFilename();
    File conRuleFile = FileUtil.getFile(mappingFile.getPath(), conRule);
    conRule = conRuleFile.getAbsolutePath();

    //added by ming qian
    if (((Short)mappingFile.getFieldValue(IMappingFile.TYPE)).intValue() == IMappingFile.JAVA_BINARY.intValue())
    {
      //TWX 20120112 allow user to define the output file ext
      return getXMLManager().convertJar(inputFile, mappingRule.getMappingClass(), mappingFile.getFilename(), mappingFile.getPath(), metaData.getFileExt());
    }
    else
    {
      return getXMLManager().convert(inputFile, conRule);
    }
    //end of added by ming qian
  }

   /**
   * Transform a file using the xsl provided.
   *
   * @param inputFile   The full path of the file to transform.
   * @param mappingRule The mappingRule to use for the transform.
   * @return the File of the transformed file.
   */
  public File transform(String inputFile, MappingRule mappingRule)
    throws Exception
  {
    Logger.debug("[MappingManagerBean.transform] Start");
    Logger.debug("[MappingManagerBean.transform] inputFile = "+inputFile);
    Logger.debug("[MappingManagerBean.transform] mappingRule = "+mappingRule.getName());

    MappingFile mappingFile = mappingRule.getMappingFile();
    String conRule = mappingFile.getFilename();
    File conRuleFile = FileUtil.getFile(mappingFile.getPath(), conRule);
    conRule = conRuleFile.getAbsolutePath();
   
    return transform(inputFile, mappingRule, null);

  }

   /**
   * Transform a file using the xsl provided.
   *
   * @param inputFile   The full path of the file to transform.
   * @param mappingRule The mappingRule to use for the transform.
   * @param params The Hashtable containing parameters for the XSL stylesheet.
   * @return the File of the transformed file.
   */
  public File transform(String inputFile,
                        MappingRule mappingRule,
                        Hashtable params)
                        throws Exception
  {

    MappingFile mappingFile = mappingRule.getMappingFile();
    String xslFilename = mappingFile.getFilename();
    File xslFile = FileUtil.getFile(mappingFile.getPath(), xslFilename);
    xslFilename = xslFile.getAbsolutePath();

    if (mappingRule.isTransformRefDoc().booleanValue())
    {
      Long refDocUid = mappingRule.getRefDocUID();
      MappingFile refDoc = findMappingFile(refDocUid);
      String refDocFilename = refDoc.getFilename();
      File refDocFile = FileUtil.getFile(refDoc.getPath(), refDocFilename);
      refDocFilename = refDocFile.getAbsolutePath();
      if (params == null)
      {
        params = new Hashtable();
      }
      params.put("REFDOC", FileUtil.convertPath(refDocFilename)); //TWX:Fix GNDB00028483 XSLT transformation issue
    }
    
    //added by ming qian
    if (((Short)mappingFile.getFieldValue(IMappingFile.TYPE)).intValue() == IMappingFile.JAVA_BINARY.intValue())
    {      
      return getXMLManager().transformJar(inputFile, mappingRule.getMappingClass(), mappingFile.getPath(), mappingFile.getFilename());
    }
    else
    {
      return getXMLManager().transform(xslFilename, inputFile, params);
    }
    
    //return getXMLManager().transform(xslFilename, inputFile, params);
  }

   /**
   * Split a file.
   *
   * @param inputFile   The full path of the file to split.
   * @param mappingRule The mappingRule used for the splitting.
   * @return the ArrayList of File object of the splitted files.
   */
  public ArrayList split(String inputFile, MappingRule mappingRule)
    throws Exception
  {
    ArrayList returnFiles = new ArrayList();
    MappingFile mappingFile = mappingRule.getMappingFile();
    String xslFilename = mappingFile.getFilename();
    File xslFile = FileUtil.getFile(mappingFile.getPath(), xslFilename);
    xslFilename = xslFile.getAbsolutePath();
    String xpath = mappingRule.getXPath();
    String param = mappingRule.getParamName();

    if (mappingRule.isKeepOriginal().booleanValue())
    {
      returnFiles.add(new File(inputFile));
    }
    ArrayList splitFiles = getXMLManager().splitXML(xslFilename,
                                           inputFile,
                                           xpath,
                                           param);
    returnFiles.addAll(splitFiles);
    return returnFiles;
  }

  // ********** methods for import schemas
  
  /**
   * Import schemas from the specified zip file
   * 
   * @param zipFilename Full path of the zip file
   * @param schemaBaseSubPath the base subpath to place the imported schemas under the schema folder
   * @param schemaMappings List of SchemaMapping for the schema files to be created as MappingFile(s).
   * @throws BatchImportSchemasException Problem during import
   * @throws SystemException Unexpected error during import
   */
  public void importSchemas(String zipFilename, String schemaBaseSubPath, List<SchemaMapping> schemaMappings)
		throws BatchImportSchemasException, SystemException
	{
  	String cat = "[MappingManagerBean.importSchemas] ";
  	Logger.log(cat + "Enter");
  	
  	try
  	{
			// do import
			BatchImportSchemasHandler handler = new BatchImportSchemasHandler(zipFilename, schemaBaseSubPath, schemaMappings);
			handler.importSchemas();
  	}
  	catch (BatchImportSchemasException ex)
  	{
  		Logger.warn(cat + ex.getMessage(), ex);
  		throw ex;
  	}
  	catch (Throwable ex)
  	{
  		Logger.warn(cat+"Unexpected error encountered", ex);
  		throw new SystemException(cat+"Unexpected error encountered: "+ex.getMessage(), ex);
  	}
  	finally
  	{
  		Logger.log(cat + "Exit");
  	}
	}
  
 // ********************* Methods for EntityHandler

  private MappingFileEntityHandler getMappingFileEntityHandler()
  {
     return MappingFileEntityHandler.getInstance();
  }

  private MappingRuleEntityHandler getMappingRuleEntityHandler()
  {
     return MappingRuleEntityHandler.getInstance();
  }

  private XpathMappingEntityHandler getXpathMappingEntityHandler()
  {
     return XpathMappingEntityHandler.getInstance();
  }

  /**
   * Obtain the XMLServiceBean by doing a Jndi lookup.
   *
   * @return The proxy interface to the XMLServiceBean.
   * @exception ServiceLookupException Error in looking up the XMLServiceBean.
   *
   * @since 2.0
   */
  private IXMLServiceLocalObj getXMLManager() throws ServiceLookupException
  {
    return (IXMLServiceLocalObj)ServiceLocator.instance(
             ServiceLocator.LOCAL_CONTEXT).getObj(
             IXMLServiceLocalHome.class.getName(),
             IXMLServiceLocalHome.class,
             new Object[0]);
  }
}