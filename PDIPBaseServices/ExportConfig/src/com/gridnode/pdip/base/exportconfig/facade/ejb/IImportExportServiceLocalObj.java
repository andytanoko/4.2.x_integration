/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IImportExportServiceLocalObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 14 2003    Koh Han Sing        Created
 * Oct 20 2005    Neo Sok Lay         No corresponding business method in the bean class 
 *                                    was found for method getHandler.
 */
package com.gridnode.pdip.base.exportconfig.facade.ejb;

import java.io.File;

import javax.ejb.EJBLocalObject;

import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntityList;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;

/**
 * Remote interface for ImportExportServiceBean.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.1 I1
 */
public interface IImportExportServiceLocalObj
  extends        EJBLocalObject
{
  /**
   * This method will retrieve from database a list of all exportable entities
   * and store them in a ConfigEntitiesContainer to be passed to the user to
   * select.
   */
  public ImportEntityList getExportableEntities() throws Exception;

  /**
   * This method will serialize the entities in the ConfigEntitiesContainer
   * into a zip file.
   */
  public File exportConfig(ImportEntityList list) throws Exception;

  /**
   * This method will deserialize the entities from the config file. It will
   * then checks for conflicting entities. If there are conflicts, it will
   * return a ConfigEntitiesContainer containing the list of conflicts and
   * store the deserialized entities in memory, waiting for the user to
   * response to the conflicts. On the other hand, if there are no conflicts it
   * will persist the import entities and return an empty
   * ConfigEntitiesContainer.
   */
  public ImportRegistry prepareImport(File configFile, boolean isOverwrite)
    throws Exception;

  /**
   * The registry will contain the the entities to be imported, the entities
   * that have conflicts and the directory where the import zip file is
   * unzipped.
   */
  public void importConfig(ImportRegistry registry)
    throws Exception;


  /**
   * Loads the EntityHandler for the given entity
   */
  public AbstractEntityHandler getHandler(String entityName) throws Exception;
}