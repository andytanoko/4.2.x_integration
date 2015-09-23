/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 25 2002    Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.gridform.helpers;

import com.gridnode.gtas.model.gridform.GFDefinitionEntityFieldID;
import com.gridnode.gtas.model.gridform.GFTemplateEntityFieldID;

import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerHome;
import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerObj;
import com.gridnode.pdip.app.gridform.helpers.IGFPathConfig;
import com.gridnode.pdip.app.gridform.model.GFDefinition;
import com.gridnode.pdip.app.gridform.model.GFTemplate;

import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.Map;

/**
 * This helper class provides helper methods for use in the Action classes
 * of the GridForm module.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public final class ActionHelper
{
  // ***************** Get Manager Helpers *****************************

  /**
   * Obtain the EJBObject for the GFManagerBean.
   *
   * @return The EJBObject to the GFManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IGFManagerObj getGFManager()
    throws ServiceLookupException
  {
    return (IGFManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGFManagerHome.class.getName(),
      IGFManagerHome.class,
      new Object[0]);
  }

  // ********************** Verification Helpers **********************

  /**
   * Verify the existence of an GFDefinition based on the specified uID.
   *
   * @param uID The UID of the GFDefinition.
   * @return The GFDefinition retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the GFManagerBean.
   * @exception Exception Bad Definition UID. No GFDefinition exists with the specified
   * uID.
   *
   * @since 2.0
   */
  public static GFDefinition verifyGFDefinition(Long uID)
    throws Exception
  {
    try
    {
      return getGFManager().findGFDefinition(uID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad Definition UID: " + uID);
    }
  }

  /**
   * Verify the existence of an GFDefinition based on the specified name.
   *
   * @param name The Name of the GFDefinition.
   * @return The GFDefinition retrieved using the specified name.
   * @exception ServiceLookupException Error in obtaining the GFManagerBean.
   * @exception Exception Bad Name. No GFDefinition exists with the specified
   * name.
   *
   * @since 2.0
   */
  public static GFDefinition verifyGFDefinition(String name)
    throws Exception
  {
    try
    {
      return getGFManager().findGFDefinition(name);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad Name: " + name);
    }
  }

  /**
   * Verify the existence in database of the definition specified by the UID or
   * name.
   *
   * @param uID The UID of the GFDefinition. If not null, verification will be
   * based on this value.
   * @param name The Name of the GFDefinition. If uID is null and this value
   * is not empty, verification will be based on this value.
   * @return The retrieved GFDefinition if the specified definition is exists in
   * database.
   * @exception Exception The specfied difinition does not exists in the database, or
   * both parameters are null/empty.
   *
   * @since 2.0
   */
  public static GFDefinition verifyGFDefinition(Long uID, String name)
    throws Exception
  {
    if (uID != null)
      return verifyGFDefinition(uID);
    else if (!isEmpty(name))
      return verifyGFDefinition(name);
    throw new Exception("No definition specified!");
  }

  /**
   * Verify the existence of an GFTemplate based on the specified uID.
   *
   * @param uID The UID of the GFTemplate.
   * @return The GFTemplate retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the GFManagerBean.
   * @exception Exception Bad Template UID. No GFTemplate exists with the specified
   * uID.
   *
   * @since 2.0
   */
  public static GFTemplate verifyGFTemplate(Long uID)
    throws Exception
  {
    try
    {
      return getGFManager().findGFTemplate(uID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad Template UID: " + uID);
    }
  }

  /**
   * Verify the existence of an GFTemplate based on the specified name.
   *
   * @param name The Name of the GFTemplate.
   * @return The GFTemplate retrieved using the specified name.
   * @exception ServiceLookupException Error in obtaining the GFManagerBean.
   * @exception Exception Bad Name. No GFTemplate exists with the specified
   * name.
   *
   * @since 2.0
   */
  public static GFTemplate verifyGFTemplate(String name)
    throws Exception
  {
    try
    {
      return getGFManager().findGFTemplate(name);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad Name: " + name);
    }
  }

  /**
   * Verify the existence in database of the template specified by the UID or
   * name.
   *
   * @param uID The UID of the GFTemplate. If not null, verification will be
   * based on this value.
   * @param name The Name of the GFTemplate. If uID is null and this value
   * is not empty, verification will be based on this value.
   * @return The retrieved GFTemplate if the specified template is exists in
   * database.
   * @exception Exception The specfied difinition does not exists in the database, or
   * both parameters are null/empty.
   *
   * @since 2.0
   */
  public static GFTemplate verifyGFTemplate(Long uID, String name)
    throws Exception
  {
    if (uID != null)
      return verifyGFTemplate(uID);
    else if (!isEmpty(name))
      return verifyGFTemplate(name);
    throw new Exception("No template specified!");
  }

  /**
   * Checks if a String value is null or its trimmed length is 0.
   *
   * @param val The String value to check.
   * @return <B>true</B> if the above condition met, <B>false</B> otherwise.
   *
   * @since 2.0
   */
  public static boolean isEmpty(String val)
  {
    return val == null || val.trim().length() == 0;
  }

  // ******************** Conversion Helpers ******************************

  /**
   * Convert a collection of GFDefinitions to Map objects.
   *
   * @param definitionList The collection of GFDefinitions to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of GFDefinitions.
   *
   * @since 2.0
   */
  public static Collection convertGFDefinitionsToMapObjects(Collection definitionList)
  {
    return GFDefinition.convertEntitiesToMap(
             (GFDefinition[])definitionList.toArray(new GFDefinition[definitionList.size()]),
             GFDefinitionEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an GFDefinition to Map object.
   *
   * @param definition The GFDefinition to convert.
   * @return A Map object converted from the specified GFDefinition.
   *
   * @since 2.0
   */
  public static Map convertGFDefinitionToMap(GFDefinition definition)
  {
    return GFDefinition.convertToMap(
             definition,
             GFDefinitionEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of GFTemplates to Map objects.
   *
   * @param templateList The collection of GFTemplates to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of GFTemplates.
   *
   * @since 2.0
   */
  public static Collection convertGFTemplatesToMapObjects(Collection templateList)
  {
    return GFTemplate.convertEntitiesToMap(
             (GFTemplate[])templateList.toArray(new GFTemplate[templateList.size()]),
             GFTemplateEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an GFTemplate to Map object.
   *
   * @param template The GFTemplate to convert.
   * @return A Map object converted from the specified GFTemplate.
   *
   * @since 2.0
   */
  public static Map convertGFTemplateToMap(GFTemplate template)
  {
    return GFTemplate.convertToMap(
             template,
             GFTemplateEntityFieldID.getEntityFieldID(),
             null);
  }

  // ******************** File Helpers ******************************

  public static void transferDefinitionFromTemp(GFDefinition definition)
    throws java.lang.Exception
  {
    String fullPath = definition.getFilename();
    String path     = FileUtil.extractPath(fullPath);
    String filename = FileUtil.extractFilename(fullPath);
    Logger.log("[ActionHelper.transferDefinitionFromTemp] Moved from " + path + filename);
    // Move from temp/user/in to ldf
    try
    {
      String subPath = FileUtil.extractRelativeFilePath(IGFPathConfig.PATH_TEMP, path, false);

      path = FileUtil.getPath(IGFPathConfig.PATH_LDF);
      filename = FileUtil.move(IGFPathConfig.PATH_TEMP, subPath, IGFPathConfig.PATH_LDF, "", filename);
    }
    catch(FileAccessException ex)
    {
      /** @todo Wrap Exception */
      throw ex;
    }
    Logger.log("[ActionHelper.transferDefinitionFromTemp] Moved to " + path + filename);
    definition.setFilename(filename);
 }

  public static void transferTemplateFromTemp(GFTemplate template)
    throws java.lang.Exception
  {
    String fullPath = template.getFilename();
    String path     = FileUtil.extractPath(fullPath);
    String filename = FileUtil.extractFilename(fullPath);
    Logger.log("[ActionHelper.transferTemplateFromTemp] Moved from " + path + filename);
    // Move from temp/user/in to ltf
    try
    {
      String subPath = FileUtil.extractRelativeFilePath(IGFPathConfig.PATH_TEMP, path, false);

      path = FileUtil.getPath(IGFPathConfig.PATH_LTF);
      filename = FileUtil.move(IGFPathConfig.PATH_TEMP, subPath, IGFPathConfig.PATH_LTF, "", filename);
    }
    catch(FileAccessException ex)
    {
      /** @todo Wrap Exception */
      throw ex;
    }
    Logger.log("[ActionHelper.transferTemplateFromTemp] Moved to " + path + filename);
    template.setFilename(filename);
 }
}