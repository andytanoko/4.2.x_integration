/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportExportServiceBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.facade.ejb;

import java.io.File;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.exportconfig.helpers.EntityExportLogic;
import com.gridnode.pdip.base.exportconfig.helpers.EntityHandlerHelper;
import com.gridnode.pdip.base.exportconfig.helpers.EntityImportLogic;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntityList;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;

/**
 * This bean is the facade to the import/export configuration services.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class ImportExportServiceBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4094357859348423585L;
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

  /**
   * This method will retrieve from database a list of all exportable entities
   * and store them in a ConfigEntitiesContainer to be passed to the user to
   * select.
   */
  public ImportEntityList getExportableEntities() throws Exception
  {
    return EntityExportLogic.getExportableEntities();
  }

  /**
   * This method will serialize the entities in the ConfigEntitiesContainer
   * into a zip file.
   */
  public File exportConfig(ImportEntityList list) throws Exception
  {
    return EntityExportLogic.exportConfig(list);
  }

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
    throws Exception
  {
    return EntityImportLogic.prepareImport(configFile, isOverwrite);
  }

  /**
   * The registry will contain the the entities to be imported, the entities
   * that have conflicts and the directory where the import zip file is
   * unzipped.
   */
  public void importConfig(ImportRegistry registry)
    throws Exception
  {
    EntityImportLogic.importConfig(registry);
  }

  /**
   * Loads the EntityHandler for the given entity
   */
  public AbstractEntityHandler getHandler(String entityName)
    throws Exception
  {
    return EntityHandlerHelper.getHandler(entityName);
  }
}