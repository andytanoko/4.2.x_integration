/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 04 2002    Koh Han Sing        Created
 * Feb 13 2003    Koh Han Sing        Add in XpathMapping
 * Feb 26 2003    Neo Sok Lay         Raise alert when there is mapping rule
 *                                    execution failure.
 * Apr 24 2003    Neo Sok Lay         Adjust raiseAlert() to new AlertRequestNotification.
 * Jun 10 2003    Koh Han Sing        Remove XpathMapping
 * Oct 31 2003    Guo Jianyu          Modified getUdocFile() to include subpath for uDoc
 * Sep 07 2005    Neo Sok Lay         Change createGridTalkMappingRule() to return key of
 *                                    created entity.
 * Nov 10 2005    Neo Sok Lay         Use local context to lookup XMLService
 * Nov 08 2006    Tam Wei Xiang       Trigger the status of the Mapping rule activity
 * Dec 26 2006    Tam Wei Xiang        modified sendMappingActivityStatus(...) to add param
 *                                     docFlowAddInfo
 * Mar 12 2007    Neo Sok Lay         Use UUID for unique filename.                                    
 * Dec 05 2007    Tam Wei Xiang       Fix GNDB00028483: XSLT transformation.
 */
package com.gridnode.gtas.server.mapper.facade.ejb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.mapper.helpers.FileHelper;
import com.gridnode.gtas.server.mapper.helpers.GridTalkMappingRuleEntityHandler;
import com.gridnode.gtas.server.mapper.helpers.Logger;
import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.gtas.server.mapper.model.IDocumentMetaInfo;
import com.gridnode.gtas.server.mapper.model.MappingData;
import com.gridnode.gtas.server.notify.AlertRequestNotification;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.pdip.app.alert.providers.ExceptionDetails;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.model.IMappingRule;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.MappingMetaData;
import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.UUIDUtil;

/**
 * This bean provides the app service layer implementation of the
 * mapper module. It serves as the facade to the business methods of
 * this module.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class GridTalkMappingManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6067388427162986021L;
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

  // ********************* Methods for GridTalkMappingRules

  /**
   * Create a new GridTalkMappingRule.
   *
   * @param gridTalkMappingRule The GridTalkMappingRule entity.
   * @return key of created entity.
   */
  public Long createGridTalkMappingRule(GridTalkMappingRule gridTalkMappingRule)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.debug("[GridTalkMappingManagerBean.createGridTalkMappingRule] Enter");

    try
    {
    	gridTalkMappingRule = (GridTalkMappingRule)getGridTalkMappingRuleEntityHandler().createEntity(gridTalkMappingRule);
    }
    catch (CreateException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.createGridTalkMappingRule] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.createGridTalkMappingRule] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.createGridTalkMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.createGridTalkMappingRule(GridTalkMappingRule) Error ",
        ex);
    }

    Logger.log("[GridTalkMappingManagerBean.createGridTalkMappingRule] Exit");
    return (Long)gridTalkMappingRule.getKey();
  }

  /**
   * Update a GridTalkMappingRule
   *
   * @param gridTalkMappingRule The GridTalkMappingRule entity with changes.
   */
  public void updateGridTalkMappingRule(GridTalkMappingRule gridTalkMappingRule)
    throws UpdateEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.updateGridTalkMappingRule] Enter");

    try
    {
      getGridTalkMappingRuleEntityHandler().update(gridTalkMappingRule);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.updateGridTalkMappingRule] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.updateGridTalkMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.updateGridTalkMappingRule(GridTalkMappingRule) Error ",
        ex);
    }

    Logger.log("[MappingManagerBean.updateGridTalkMappingRule] Exit");
  }

  /**
   * Delete a GridTalkMappingRule.
   *
   * @param gridTalkMappingRuleUId The UID of the GridTalkMappingRule to delete.
   */
  public void deleteGridTalkMappingRule(Long gridTalkMappingRuleUId)
    throws DeleteEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.deleteGridTalkMappingRule] Enter");

    try
    {
      getGridTalkMappingRuleEntityHandler().remove(gridTalkMappingRuleUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.deleteGridTalkMappingRule] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.deleteGridTalkMappingRule] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.deleteGridTalkMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.deleteGridTalkMappingRule(gridTalkMappingRuleUId) Error ",
        ex);
    }

    Logger.log("[GridTalkMappingManagerBean.deleteGridTalkMappingRule] Exit");
  }

  /**
   * Find a GridTalkMappingRule using the GridTalkMappingRule UID.
   *
   * @param mappingFileUId The UID of the GridTalkMappingRule to find.
   * @return The GridTalkMappingRule found, or <B>null</B> if none exists with that
   * UID.
   */
  public GridTalkMappingRule findGridTalkMappingRule(Long gridTalkMappingRuleUId)
    throws FindEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.findGridTalkMappingRule] UID: "+
      gridTalkMappingRuleUId);

    GridTalkMappingRule gridTalkMappingRule = null;
   
    try
    {
      gridTalkMappingRule =
        (GridTalkMappingRule)getGridTalkMappingRuleEntityHandler().
          getEntityByKeyForReadOnly(gridTalkMappingRuleUId);
      
      //added by ming qian
      MappingRule r = gridTalkMappingRule.getMappingRule();
      
      MappingFile f = r.getMappingFile();
      
      //end of added by ming qian
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRule] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRule] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findGridTalkMappingRule(gridTalkMappingRuleUId) Error ",
        ex);
    }

    return gridTalkMappingRule;
  }

  /**
   * Find a GridTalkMappingRule using the GridTalkMappingRule Name.
   *
   * @param gridTalkMappingRuleName The Name of the GridTalkMappingRule to find.
   * @return The GridTalkMappingRule found, or <B>null</B> if none exists.
   */
  public GridTalkMappingRule findGridTalkMappingRule(String gridTalkMappingRuleName)
    throws FindEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.findGridTalkMappingRule] Mapping Rule Name: "
      +gridTalkMappingRuleName);

    GridTalkMappingRule gridTalkMappingRule = null;

    try
    {
      gridTalkMappingRule =
        (GridTalkMappingRule)getGridTalkMappingRuleEntityHandler().
          findByGridTalkMappingRuleName(gridTalkMappingRuleName);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRule] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRule] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findGridTalkMappingRule(mappingFileName) Error ",
        ex);
    }

    return gridTalkMappingRule;
  }

  /**
   * Find a number of GridTalkMappingRule that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GridTalkMappingRule found, or empty collection if none
   * exists.
   */
  public Collection findGridTalkMappingRules(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.debug( "[GridTalkMappingManagerBean.findGridTalkMappingRules] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection gridTalkMappingRules = null;
    try
    {
      gridTalkMappingRules =
        getGridTalkMappingRuleEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRules] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRules] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRules] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findGridTalkMappingRules(filter) Error ",
        ex);
    }

    return gridTalkMappingRules;
  }

  /**
   * Find a number of GridTalkMappingRule that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of GridTalkMappingRule found, or empty collection if
   * none exists.
   */
  public Collection findGridTalkMappingRulesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.debug( "[GridTalkMappingManagerBean.findGridTalkMappingRulesKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection gridTalkMappingRulesKeys = null;
    try
    {
      gridTalkMappingRulesKeys =
        getGridTalkMappingRuleEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRulesKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRulesKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[GridTalkMappingManagerBean.findGridTalkMappingRulesKeys] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findGridTalkMappingRulesKeys(filter) Error ",
        ex);
    }

    return gridTalkMappingRulesKeys;
  }

  public Collection execute(Long gtMappingRuleUid, IDocumentMetaInfo gdoc)
    throws Exception
  {
    Logger.debug("[GridTalkMappingManagerBean.execute] Start");
    Logger.debug("[GridTalkMappingManagerBean.execute] gtMappingRuleUid : "+gtMappingRuleUid);
    Logger.debug("[GridTalkMappingManagerBean.execute] gdoc : "+gdoc);
    GridTalkMappingRule gtMappingRule = findGridTalkMappingRule(gtMappingRuleUid);
    return execute(gtMappingRule, gdoc);
  }

  public Collection execute(GridTalkMappingRule gtMappingRule, IDocumentMetaInfo gdoc)
    throws Exception
  {
    boolean isMappingSuccess = true;
    Exception mapEx = null;
    
    ArrayList resultGdocs = new ArrayList();
    try
    {
      Logger.debug("[GridTalkMappingManagerBean.execute] 2 Start");

      if (gtMappingRule.isHeaderTransformation())
      {
        Logger.debug("[GridTalkMappingManagerBean.execute] is headerTransformation");
        gdoc = transformHeader(gtMappingRule, gdoc);
        resultGdocs.add(gdoc);
      }
      else
      {
        resultGdocs = contentMapping(gtMappingRule, gdoc);
      }
      Logger.debug("[GridTalkMappingManagerBean.execute] 2 Returning "+resultGdocs);
    }
    catch (Exception t)
    {
      isMappingSuccess = false;
      mapEx = t;
      raiseAlert(gdoc, gtMappingRule.getMappingRule(), t);
      throw t;
    }
    finally
    {
      sendMappingActivityStatus(EDocumentFlowType.MAPPING_RULE, gdoc, isMappingSuccess, (mapEx != null ? mapEx.getMessage(): ""), new Date(), gtMappingRule.getName(), mapEx);
    }

    return resultGdocs;
  }
  
  private void raiseAlert(IDocumentMetaInfo gdoc, MappingRule mappingRule, Throwable t)
  {
    // raise alert
    AlertRequestNotification notification =
      new AlertRequestNotification(AlertRequestNotification.DOCUMENT_MAPPING_FAILURE,
                                   gdoc,
                                   createProviders(mappingRule, t));
    try
    {
      if(! JMSRetrySender.isSendViaDefMode())//TWX 23 Nov 2007 To enable the jms sending with retry
      {
        Logger.debug("[GridTalkMappingManagerBean] raiseAlert via JMSRetrySender.");
        JMSRetrySender.broadcast(notification);
      }
      else
      {
        Notifier.getInstance().broadcast(notification);
      }
    }
    catch (Exception ex) {}
  }

  private List createProviders(
    MappingRule mappingRule, Throwable t)
  {
    List providers = new ArrayList();
    providers.add(new ExceptionDetails(t));
    providers.add(new MappingData(mappingRule));
    return providers;
  }


  private ArrayList contentMapping(GridTalkMappingRule gtMappingRule,
                                    IDocumentMetaInfo gdoc)
                                    throws Exception
  {
    Logger.debug("[GridTalkMappingManagerBean.contentMapping] not headerTransformation");
    ArrayList resultGdocs = null;

    if ((gtMappingRule.getTargetDocType() != null) && !(gtMappingRule.getTargetDocType().equals("")))
    {
      gdoc.setUdocDocType(gtMappingRule.getTargetDocType());
    }
    if ((gtMappingRule.getTargetDocFileType() != null) && !(gtMappingRule.getTargetDocFileType().equals("")))
    {
      gdoc.setUdocFileType(gtMappingRule.getTargetDocFileType());
      File udocFile = FileHelper.getUdocFile(gdoc);

      if (udocFile != null)
      {
        File newFile = File.createTempFile(generateRandomFileName(), "." + gtMappingRule.getTargetDocFileType());
        //udocFile.renameTo(newFile);   //renameTo doesn't work
        String newFilename = FileHelper.copy(udocFile.getAbsolutePath(), newFile.getAbsolutePath());
        gdoc.setTempUdocFilename(newFilename);
        newFile = FileHelper.getUdocFile(gdoc);
        if (newFile != null)
        {
          gdoc.setUdocFullPath(newFile.getAbsolutePath());
        }
      }
    }
    Short mappingType = gtMappingRule.getMappingRule().getType();
    if (mappingType.equals(IMappingRule.MAPPING_CONVERT))
    {
      resultGdocs = (ArrayList)convert(gtMappingRule, gdoc);
    }
    if (mappingType.equals(IMappingRule.MAPPING_TRANSFORM))
    {
      resultGdocs = (ArrayList)transform(gtMappingRule, gdoc);
    }
    if (mappingType.equals(IMappingRule.MAPPING_SPLIT))
    {
      resultGdocs = (ArrayList)split(gtMappingRule, gdoc);
    }
    Logger.debug("[GridTalkMappingManagerBean.contentMapping] return");

    return resultGdocs;
  }

  public IDocumentMetaInfo transformHeader(GridTalkMappingRule gtMappingRule,
                                           IDocumentMetaInfo gdoc)
                                           throws Exception
  {
    AbstractEntity gdocEntity = (AbstractEntity)gdoc;
    File tempGdoc = File.createTempFile(generateRandomFileName(), ".xml");
    String gdocFullPath = tempGdoc.getAbsolutePath();
    gdocEntity.serialize(gdocFullPath);

    String tempUdocFilename = gdoc.getTempUdocFilename();

    Hashtable params = new Hashtable();
    if (gtMappingRule.isTransformWithSource())
    {
      File udocFile = FileHelper.getUdocFile(gdoc);
      if (udocFile != null)
      {
        params.put("UDOC", FileUtil.convertPath(udocFile.getAbsolutePath())); //TWX Fix GNDB00028483: XSLT transformation.
      }
    }
    MappingRule mappingRule = gtMappingRule.getMappingRule();
    File xGdoc = getMappingManager().transform(gdocFullPath, mappingRule, params);
    gdocFullPath = xGdoc.getAbsolutePath();
    gdoc = (IDocumentMetaInfo)gdocEntity.deserialize(gdocFullPath);

    if (!tempUdocFilename.equals(""))
    {
      gdoc.setTempUdocFilename(tempUdocFilename);
    }

    xGdoc.delete();
    tempGdoc.delete();
    return gdoc;
  }

  public Collection transform(GridTalkMappingRule gtMappingRule,
                              IDocumentMetaInfo gdoc)
                              throws Exception
  {
    Logger.debug("[GridTalkMappingManagerBean.transform] Enter");
    ArrayList returnGdocs = new ArrayList();

    MappingRule mappingRule = gtMappingRule.getMappingRule();
    Logger.log("[GridTalkMappingManagerBean.transform] mapping rule = "+
                gtMappingRule.getName());

    Hashtable params = new Hashtable();
    if (gtMappingRule.isTransformWithHeader())
    {
      AbstractEntity gdocEntity = (AbstractEntity)gdoc;
      File tempGdoc = File.createTempFile(generateRandomFileName(), ".xml");
      String gdocFullPath = tempGdoc.getAbsolutePath();
      gdocEntity.serialize(gdocFullPath);

      params.put("GDOC", FileUtil.convertPath(gdocFullPath)); //TWX Fix GNDB00028483: XSLT transformation.
    }

    File udocFile = FileHelper.getUdocFile(gdoc);

    File resultFile = getMappingManager().transform(
                        udocFile.getAbsolutePath(),
                        mappingRule,
                        params);

    String newUdocFilename = "x"+gdoc.getUdocFilename();
    String newTempUdocFilename = resultFile.getName();
    Logger.debug("[GridTalkMappingManagerBean.transform] newTempUdocFilename = "+newTempUdocFilename);

    gdoc.setUdocFilename(newUdocFilename);
    gdoc.setTempUdocFilename(newTempUdocFilename);
    gdoc.setUdocFullPath(resultFile.getAbsolutePath());
    //gdoc = setDocType(gtMappingRule, gdoc);

    returnGdocs.add(gdoc);
    Logger.debug("[GridTalkMappingManagerBean.transform] Exit");
    return returnGdocs;
  }

  public Collection convert(GridTalkMappingRule gtMappingRule,
                            IDocumentMetaInfo gdoc)
                            throws Exception
  {
    Logger.debug("[GridTalkMappingManagerBean.convert] Enter");
    ArrayList returnGdocs = new ArrayList();

    MappingRule mappingRule = gtMappingRule.getMappingRule();
    Logger.debug("[GridTalkMappingManagerBean.convert] mapping rule = "+
                gtMappingRule.getName());
    
    MappingMetaData metaData = new MappingMetaData(gtMappingRule.getTargetDocFileType()); //TWX 20120111 #3193
    
    File udocFile = FileHelper.getUdocFile(gdoc);
    File resultFile = getMappingManager().convert(
                        udocFile.getAbsolutePath(),
                        mappingRule, metaData);

    String newFileExt = getFileExt(resultFile.getName());
    
    String newUdocFilename = changeFileExt(gdoc.getUdocFilename(), newFileExt);
    newUdocFilename = "v"+newUdocFilename;
    String newTempUdocFilename = resultFile.getName();

    gdoc.setUdocFilename(newUdocFilename);
    gdoc.setTempUdocFilename(newTempUdocFilename);
    gdoc.setUdocFullPath(resultFile.getAbsolutePath());
    //gdoc = setDocType(gtMappingRule, gdoc);

    returnGdocs.add(gdoc);
    Logger.debug("[GridTalkMappingManagerBean.convert] Exit");
    return returnGdocs;
  }

  public Collection split(GridTalkMappingRule gtMappingRule, IDocumentMetaInfo gdoc)
    throws Exception
  {
    Logger.debug("[GridTalkMappingManagerBean.split] Enter");
    ArrayList returnGdocs = new ArrayList();

    MappingRule mappingRule = gtMappingRule.getMappingRule();
    Logger.debug("[GridTalkMappingManagerBean.split] mapping rule = "+
                gtMappingRule.getName());

    File udocFile = FileHelper.getUdocFile(gdoc);
    ArrayList splitFiles = getMappingManager().split(
                             udocFile.getAbsolutePath(),
                             mappingRule);

    for (int i = 0; i < splitFiles.size(); i++)
    {
      File aSplitFile = (File)splitFiles.get(i);
      String newUdocFilename = "s"+(i+1)+gdoc.getUdocFilename();
      String newTempUdocFilename = aSplitFile.getName();

      IDocumentMetaInfo newGridDocument = (IDocumentMetaInfo)gdoc.clone();
      newGridDocument.setUdocFilename(newUdocFilename);
      newGridDocument.setTempUdocFilename(newTempUdocFilename);
      newGridDocument.setUdocFullPath(aSplitFile.getAbsolutePath());
      //newGridDocument = setDocType(gtMappingRule, newGridDocument);
      returnGdocs.add(newGridDocument);
    }
    Logger.debug("[GridTalkMappingManagerBean.split] Exit");
    return returnGdocs;
  }

  // ********************* Methods for MappingRules

  /**
   * Create a new MappingRule.
   *
   * @param mappingRule The MappingRule entity.
   *//*
  private IEntity createMappingRule(MappingRule mappingRule)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.debug("[GridTalkMappingManagerBean.createMappingRule] Enter");

    IEntity entity = null;

    try
    {
      entity = getMappingRuleEntityHandler().createEntity(mappingRule);
    }
    catch (CreateException ex)
    {
      Logger.err("[GridTalkMappingManagerBean.createMappingRule] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.err("[GridTalkMappingManagerBean.createMappingRule] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.err("[GridTalkMappingManagerBean.createMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.createMappingRule(MappingRule) Error ",
        ex);
    }

    Logger.debug("[GridTalkMappingManagerBean.createMappingRule] Exit");

    return entity;
  }*/

  /**
   * Update a MappingRule
   *
   * @param mappingRule The MappingRule entity with changes.
   *//*
  private void updateMappingRule(MappingRule mappingRule)
    throws UpdateEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.updateMappingRule] Enter");

    try
    {
      getMappingRuleEntityHandler().update(mappingRule);
    }
    catch (EntityModifiedException ex)
    {
      Logger.err("[GridTalkMappingManagerBean.updateMappingRule] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.err("[GridTalkMappingManagerBean.updateMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.updateMappingRule(MappingRule) Error ",
        ex);
    }

    Logger.log("[GridTalkMappingManagerBean.updateMappingRule] Exit");
  }*/

  /**
   * Delete a MappingRule.
   *
   * @param mappingRuleUId The UID of the MappingRule to delete.
   *//*
  private void deleteMappingRule(Long mappingRuleUId)
    throws DeleteEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.deleteMappingRule] Enter");

    try
    {
      getMappingRuleEntityHandler().remove(mappingRuleUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.err("[GridTalkMappingManagerBean.deleteMappingRule] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.err("[GridTalkMappingManagerBean.deleteMappingRule] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.err("[GridTalkMappingManagerBean.deleteMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.deleteMappingRule(mappingRuleUId) Error ",
        ex);
    }

    Logger.log("[GridTalkMappingManagerBean.deleteMappingRule] Exit");
  }*/

  /**
   * Find a MappingRule using the MappingRule UID.
   *
   * @param mappingFileUId The UID of the MappingRule to find.
   * @return The MappingRule found, or <B>null</B> if none exists with that
   * UID.
   *//*
  private MappingRule findMappingRule(Long mappingRuleUId)
    throws FindEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.findMappingRule] UID: "+mappingRuleUId);

    MappingRule mappingRule = null;

    try
    {
      mappingRule = (MappingRule)getMappingRuleEntityHandler().getEntityByKeyForReadOnly(mappingRuleUId);
    }
    catch (ApplicationException ex)
    {
      Logger.err("[GridTalkMappingManagerBean.findMappingRule] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.err("[GridTalkMappingManagerBean.findMappingRule] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.err("[GridTalkMappingManagerBean.findMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findMappingRule(mappingRuleUId) Error ",
        ex);
    }

    return mappingRule;
  }*/

  /**
   * Find a MappingRule using the MappingRule Name.
   *
   * @param mappingRuleName The Name of the MappingRule to find.
   * @return The MappingRule found, or <B>null</B> if none exists.
   *//*
  private MappingRule findMappingRule(String mappingRuleName)
    throws FindEntityException, SystemException
  {
    Logger.debug("[GridTalkMappingManagerBean.findMappingRule] Mapping Rule Name: "
      +mappingRuleName);

    MappingRule mappingRule = null;

    try
    {
      mappingRule = (MappingRule)getMappingRuleEntityHandler().findByMappingRuleName(mappingRuleName);
    }
    catch (ApplicationException ex)
    {
      Logger.err("[GridTalkMappingManagerBean.findMappingRule] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.err("[GridTalkMappingManagerBean.findMappingRule] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.err("[GridTalkMappingManagerBean.findMappingRule] Error ", ex);
      throw new SystemException(
        "GridTalkMappingManagerBean.findMappingRule(mappingFileName) Error ",
        ex);
    }

    return mappingRule;
  }*/

  // *************************************************************************

//  private File getUdocFile(IDocumentMetaInfo gdoc)
//    throws Exception
//  {
//    File udocFile = null;
//    String udocFilename = gdoc.getUdocFilename();
//    Logger.debug("[GridTalkMappingManagerBean.getUdocFile] udocFilename = "+
//                udocFilename);
//    if (udocFilename.indexOf(":") > -1)
//    {
//      String tempUdocFilename = udocFilename.substring(udocFilename.indexOf(":")+1);
//      udocFilename = udocFilename.substring(0, udocFilename.indexOf(":"));
//      String tmpdir = System.getProperty("java.io.tmpdir");
//      Logger.debug("[GridTalkMappingManagerBean.getUdocFile] tempUdocFilename = "+tmpdir+"/"+tempUdocFilename);
//      udocFile = new File(tmpdir+"/"+tempUdocFilename);
//      if (!udocFile.exists())
//      {
//        throw new Exception("temp udoc file not found in temp directory");
//      }
//    }
//    else
//    {
//      udocFile = FileUtil.getFile(IGTMapperPathConfig.PATH_UDOC, gdoc.getFolder() + File.separator,
//                                  udocFilename);
//    }
//    return udocFile;
//  }

//  private String getOrgUdocFilename(IDocumentMetaInfo gdoc)
//  {
//    String udocFilename = gdoc.getUdocFilename();
//    Logger.debug("[GridTalkMappingManagerBean.getOrgUdocFilename] udocFilename = "+
//                udocFilename);
//    if (udocFilename.indexOf(":") > -1)
//    {
//      udocFilename = udocFilename.substring(0, udocFilename.indexOf(":"));
//    }
//    return udocFilename;
//
//  }

  // ********************* Methods for EntityHandler

  private GridTalkMappingRuleEntityHandler getGridTalkMappingRuleEntityHandler()
  {
     return GridTalkMappingRuleEntityHandler.getInstance();
  }
  /*
  private MappingRuleEntityHandler getMappingRuleEntityHandler()
  {
     return MappingRuleEntityHandler.getInstance();
  }*/
  private IMappingManagerObj getMappingManager()
    throws ServiceLookupException
  {
    return (IMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IMappingManagerHome.class.getName(),
      IMappingManagerHome.class,
      new Object[0]);
  }
  /*
  private IXMLServiceLocalObj getXMLManager()
    throws ServiceLookupException
  {
    return (IXMLServiceLocalObj)ServiceLocator.instance(
      ServiceLocator.LOCAL_CONTEXT).getObj(
      IXMLServiceLocalHome.class.getName(),
      IXMLServiceLocalHome.class,
      new Object[0]);
  }*/

  // ********************* Methods for EntityHandler
  /*
  private IDocumentMetaInfo setDocType(GridTalkMappingRule gtMappingRule,
                                  IDocumentMetaInfo gdoc)
  {
    String targetDocType = gtMappingRule.getTargetDocType();
    if ((targetDocType != null) && (!targetDocType.equals("")))
    {
      gdoc.setUdocDocType(targetDocType);
    }

    String targetFileType = gtMappingRule.getTargetDocFileType();
    if ((targetFileType != null) && (!targetFileType.equals("")))
    {
      gdoc.setUdocDocType(targetFileType);
    }

    return gdoc;
  }*/

  protected String generateRandomFileName()
  {
    //Random random = new Random();
    //return String.valueOf(random.nextInt());
    //NSL20070312 Ensure uniqueness
    return UUIDUtil.getRandomUUIDInStr();
  }

  private String getFileExt(String fullFilename)
  {
    String ext = "";
    if ((fullFilename.indexOf(".") > 0) && (fullFilename.lastIndexOf(".")+1 != fullFilename.length()))
    {
      ext = fullFilename.substring(fullFilename.lastIndexOf(".")+1);
    }

    return ext;
  }

  private String changeFileExt(String fullFilename, String newFileExt)
  {
    String newFilename = fullFilename;
    if ((fullFilename.length() > 0) && (fullFilename.indexOf(".") < 0) && (newFileExt != null && newFileExt.length() > 0))
    {
      newFilename = newFilename + "." + newFileExt;
    }
    else if ((fullFilename.indexOf(".") > 0) && (fullFilename.lastIndexOf(".")+1 != fullFilename.length())  && (newFileExt != null && newFileExt.length() > 0))
    {
      String filename = fullFilename.substring(0, fullFilename.lastIndexOf(".")+1);
      newFilename = filename + newFileExt;
    }
    return newFilename;
  }
  
  /**
   * TWX 08112006 Trigger the status of the mapping activity.
   * @param docFlowType
   * @param gdoc
   * @param isActivitySuccess
   * @param errReason
   * @param activityOccuredDate
   * @param docFlowAddInfo
   */
  private void sendMappingActivityStatus(EDocumentFlowType docFlowType, IDocumentMetaInfo gdoc, boolean isActivitySuccess, 
                                         String errReason ,Date activityOccuredDate, String docFlowAddInfo, Throwable th) throws Exception
  {
    if(gdoc == null)
    {
      throw new NullPointerException("[GridTalkMappingManagerBean.sendMappingActivityStatus] gdoc is null !!! ");
    }
    
    DocumentFlowNotifyHandler.triggerNotification(docFlowType, activityOccuredDate, gdoc.getFolder(), gdoc.getGdocId(),isActivitySuccess, 
                                                 errReason, gdoc.getTracingID(), gdoc.getUdocDocType(), gdoc.getSenderBizEntityId(),
                                                 gdoc.getRecipientBizEntityId(), docFlowAddInfo, (Long)gdoc.getKey(), gdoc.getSrcFolder(), th);
  }
}
