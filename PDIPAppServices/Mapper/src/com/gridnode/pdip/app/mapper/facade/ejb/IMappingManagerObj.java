/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMappingManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 * Jun 10 2003    Koh Han Sing        Add in XpathMapping
 * Oct 20 2005    Neo Sok Lay         No corresponding business method in the bean class 
 *                                    com.gridnode.pdip.app.mapper.facade.ejb.MappingManagerBean 
 *                                    was found for method createMappingFile.
 *                                    Business methods of the remote interface must throw java.rmi.RemoteException
 *                                    - The business method createMappingRule does not throw java.rmi.RemoteException
 *                                    - The business method updateMappingRule does not throw java.rmi.RemoteException
 *                                    - The business method deleteMappingRule does not throw java.rmi.RemoteException
 *                                    - The business method findMappingRule does not throw java.rmi.RemoteException
 *                                    - The business method findMappingRules does not throw java.rmi.RemoteException
 *                                    - The business method findMappingRulesKeys does not throw java.rmi.RemoteException
 *                                    - The business method createXpathMapping does not throw java.rmi.RemoteException
 *                                    - The business method updateXpathMapping does not throw java.rmi.RemoteException
 *                                    - The business method deleteXpathMapping does not throw java.rmi.RemoteException
 *                                    - The business method findXpathMapping does not throw java.rmi.RemoteException
 *                                    - The business method findXpathUid does not throw java.rmi.RemoteException
 *                                    - The business method findXpathMappings does not throw java.rmi.RemoteException
 *                                    - The business method findXpathMappingsKeys does not throw java.rmi.RemoteException
 * Mar 07 2006    Neo Sok Lay        Add importSchemas() method to handle import of schema set.        
 *                                   Use generics.                            
 */
package com.gridnode.pdip.app.mapper.facade.ejb;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.mapper.exception.BatchImportSchemasException;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.MappingMetaData;
import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.app.mapper.model.SchemaMapping;
import com.gridnode.pdip.app.mapper.model.XpathMapping;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * Remote interface for MappingManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public interface IMappingManagerObj
  extends        EJBObject
{
  /**
   * Create a new MappingFile
   *
   * @param mappingFile The MappingFile entity.
   * @return the MappingFile created.
   */
  public MappingFile createMappingFile(MappingFile mappingFile)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a MappingFile
   *
   * @param mappingFile The MappingFile entity with changes.
   */
  public void updateMappingFile(MappingFile mappingFile)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a MappingFile.
   *
   * @param mappingFileUId The UID of the MappingFile to delete.
   */
  public void deleteMappingFile(Long mappingFileUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a MappingFile using the MappingFile UID.
   *
   * @param mappingFileUId The UID of the MappingFile to find.
   * @return The MappingFile found, or <B>null</B> if none exists with that
   * UID.
   */
  public MappingFile findMappingFile(Long mappingFileUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a MappingFile using the MappingFile Name.
   *
   * @param mappingFileName The Name of the MappingFile to find.
   * @return The MappingFile found, or <B>null</B> if none exists.
   */
  public MappingFile findMappingFile(String mappingFileName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a MappingFile using the MappingFile Type.
   *
   * @param mappingFileType The Type of the MappingFile to find.
   * @return The MappingFile found, or <B>null</B> if none exists.
   */
  public Collection<MappingFile> findMappingFile(Short mappingFileType)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of MappingFile that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of MappingFile found, or empty collection if none
   * exists.
   */
  public Collection<MappingFile> findMappingFiles(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of MappingFile that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of MappingFile found, or empty collection if
   * none exists.
   */
  public Collection<Long> findMappingFilesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Create a new MappingRule.
   *
   * @param mappingRule The MappingRule entity.
   * @return the MappingRule created.
   */
  public MappingRule createMappingRule(MappingRule mappingRule)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a MappingRule
   *
   * @param mappingRule The MappingRule entity with changes.
   */
  public void updateMappingRule(MappingRule mappingRule)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a MappingRule.
   *
   * @param mappingRuleUId The UID of the MappingRule to delete.
   */
  public void deleteMappingRule(Long mappingRuleUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a MappingRule using the MappingRule UID.
   *
   * @param mappingRuleUId The UID of the MappingRule to find.
   * @return The MappingRule found, or <B>null</B> if none exists with that
   * UID.
   */
  public MappingRule findMappingRule(Long mappingRuleUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a MappingRule using the MappingRule Name.
   *
   * @param mappingRuleName The Name of the MappingRule to find.
   * @return The MappingRule found, or <B>null</B> if none exists.
   */
  public MappingRule findMappingRule(String mappingRuleName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of MappingRule with the matching MappingRule Type.
   *
   * @param mappingRuleType The Type of the MappingRule to find.
   * @return a Collection of MappingRule found, or empty collection if none
   * exists with that Type.
   */
  public Collection<MappingRule> findMappingRule(Short mappingRuleType)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of MappingRule that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of MappingRule found, or empty collection if none
   * exists.
   */
  public Collection<MappingRule> findMappingRules(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of MappingRule that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of MappingRule found, or empty collection if
   * none exists.
   */
  public Collection<Long> findMappingRulesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  // ********************* Methods for XpathMapping

  /**
   * Create a new XpathMapping.
   *
   * @param xpathMapping The XpathMapping entity.
   */
  public IEntity createXpathMapping(XpathMapping xpathMapping)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a XpathMapping
   *
   * @param xpathMapping The XpathMapping entity with changes.
   */
  public void updateXpathMapping(XpathMapping xpathMapping)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a XpathMapping.
   *
   * @param xpathMappingUId The UID of the XpathMapping to delete.
   */
  public void deleteXpathMapping(Long xpathMappingUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a XpathMapping using the XpathMapping UID.
   *
   * @param xpathMappingUId The UID of the XpathMapping to find.
   * @return The XpathMapping found, or <B>null</B> if none exists with that
   * UID.
   */
  public XpathMapping findXpathMapping(Long xpathMappingUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a XpathMapping using the Xpath Uid.
   *
   * @param xpathUid The Uid of the Xpath to find.
   * @return The XpathMapping found, or <B>null</B> if none exists with that
   * Uid.
   */
  public XpathMapping findXpathUid(Long xpathUid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a XpathMapping using the Root element name.
   *
   * @param rootName The name of the root element to find.
   * @return The XpathMapping found, or <B>null</B> if none exists with that
   * root element name.
   */
  public XpathMapping findXpathMapping(String rootName)
    throws FindEntityException, SystemException, RemoteException;
  /**
   * Find a number of XpathMapping that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of XpathMapping found, or empty collection if none
   * exists.
   */
  public Collection<XpathMapping> findXpathMappings(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of XpathMapping that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of XpathMapping found, or empty collection if
   * none exists.
   */
  public Collection<Long> findXpathMappingsKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

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
    throws Exception, RemoteException;

  /**
   * Converts a file using the conversion rule.
   *
   * @param inputFile   The full path of the file to convert.
   * @param mappingRule The mappingRule to use for the conversion.
   * @param metaData The mapping meta data that contains the transient data.
   * @return the ByteArrayOutputStream of the converted file.
   */
  public File convert(String inputFile, MappingRule mappingRule, MappingMetaData metaData)
    throws Exception, RemoteException;

   /**
   * Transform a file using the xsl provided.
   *
   * @param inputFile   The full path of the file to transform.
   * @param mappingRule The mappingRule to use for the transform.
   * @return the ByteArrayOutputStream of the transformed file.
   */
  public File transform(String inputFile, MappingRule mappingRule)
    throws Exception, RemoteException;

   /**
   * Transform a file using the xsl provided.
   *
   * @param inputFile   The full path of the file to transform.
   * @param mappingRule The mappingRule to use for the transform.
   * @param params The Hashtable containing parameters for the XSL stylesheet.
   * @return the ByteArrayOutputStream of the transformed file.
   */
  public File transform(String inputFile,
                        MappingRule mappingRule,
                        Hashtable params)
                        throws Exception, RemoteException;

   /**
   * Split a file.
   *
   * @param inputFile   The full path of the file to split.
   * @param mappingRule The mappingRule used for the splitting.
   * @return the ArrayList of File object of the splitted files.
   */
  public ArrayList<File> split(String inputFile, MappingRule mappingRule)
    throws Exception, RemoteException;
  
  
  // ***** Batch Import schemas
  /**
   * Import schemas from the specified zip file
   * 
   * @param zipFilename Full path of the zip file
   * @param schemaBaseSubPath the base subpath to place the imported schemas under the schema folder
   * @param schemaMappings List of SchemaMapping for the schema files to be created as MappingFile(s).
   * @throws BatchImportSchemasException Problem during import
   * @throws SystemException Unexpected error encountered
   */
  public void importSchemas(String zipFilename, String schemaBaseSubPath, List<SchemaMapping> schemaMappings)
  	throws BatchImportSchemasException, SystemException, RemoteException;
  
  /**
   * TWX: 15 NOV 2005
   * It will retrieve the values of the elements which specified in the xpath mapping
   * file from the user document.
   * 
   * @param udoc
   * @return Hashtable key is the uid of grid doc entity
   */
  public Hashtable getUDocElementInfo(File udoc)
		throws Exception, RemoteException;
}