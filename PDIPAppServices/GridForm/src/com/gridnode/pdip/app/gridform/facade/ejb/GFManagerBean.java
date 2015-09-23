/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.facade.ejb;

import com.gridnode.pdip.app.gridform.model.*;
import com.gridnode.pdip.app.gridform.exceptions.*;
import com.gridnode.pdip.app.gridform.helpers.*;
import com.gridnode.pdip.app.gridform.xml.XMLFacade;
import com.gridnode.pdip.app.gridform.entities.ejb.IGFDefinitionLocalObj;
import com.gridnode.pdip.app.gridform.entities.ejb.IGFTemplateLocalObj;

//to be taken out to helper class later
import com.gridnode.pdip.framework.file.access.*;
import java.io.*;

import com.gridnode.pdip.framework.db.DefaultEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.db.cursor.*;

import javax.ejb.*;
import java.util.*;

/**
 * This stateless session bean is the facade to all GridForm services.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GFManagerBean implements SessionBean
{
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

  // ---------------------------------------------------------------------------

  /**
   * Create a new GFDefinition entity.
   *
   * @param definition The GFDefinition entity to create.
   * @return The UID of the created definition.
   * @since 2.0
   */
  public Long createGFDefinition(GFDefinition definition) throws CreateEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.createGFDefinition] Entry");
    try
    {
      IGFDefinitionLocalObj obj = GFDefinitionEntityHandler.getInstance().createGFDefinition(definition);
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      GFLog.err("[GFManagerBean.createGFDefinition] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.createGFDefinition] Error", ex);
      throw new SystemException("GFManagerBean.createGFDefinition(GFDefinition) Error", ex);
    }
    finally
    {
      GFLog.log("[GFManagerBean.createGFDefinition] Exit");
    }
  }

  /**
   * Update a GFDefinition entity.
   *
   * @param definition The GFDefinition entity to update.
   * @since 2.0
   */
  public void updateGFDefinition(GFDefinition definition) throws UpdateEntityException, SystemException
  {
    GFLog.log("GFManagerBean.updateGFDefinition] Entry");
    try
    {
      GFDefinitionEntityHandler.getInstance().updateGFDefinition(definition);
    }
    catch (EntityModifiedException ex)
    {
      GFLog.err("[GFManagerBean.updateGFDefinition] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.updateGFDefinition] Error", ex);
      throw new SystemException("GFManagerBean.updateGFDefinition(GFDefinition) Error", ex);
    }
    GFLog.log("[GFManagerBean.updateGFDefinition] Exit");
  }

  /**
   * Delete a GFDefinition entity.
   *
   * @param uid The Unique ID of the GFDefinition to delete.
   * @since 2.0
   */
  public void deleteGFDefinition(Long uid) throws DeleteEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.deleteGFDefinition] Entry");
    try
    {
      GFDefinitionEntityHandler.getInstance().deleteGFDefinition(uid);
    }
    catch (RemoveException ex)
    {
      GFLog.err("[GFManagerBean.deleteGFDefinition] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.deleteGFDefinition] Error", ex);
      throw new SystemException("GFManagerBean.deleteGFDefinition(uid) Error", ex);
    }
    GFLog.log("[GFManagerBean.deleteGFDefinition] Exit");
  }

  /**
   * Find the GFDefinition entity that matches the UID.
   *
   * @param uid The Unique ID of the entity.
   * @return The GFDefinition entity found, or null if it does not exists.
   * @since 2.0
   */
  public GFDefinition findGFDefinition(Long uid) throws FindEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.findGFDefinition] Entry, UID: " + uid);
    GFDefinition definition = null;
    try
    {
      definition = (GFDefinition)GFDefinitionEntityHandler.getInstance().getEntityByKeyForReadOnly(uid);
    }
    catch (ApplicationException ex)
    {
      GFLog.err("[GFManagerBean.findGFDefinition] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      GFLog.err("[GFManagerBean.findGFDefinition] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.findGFDefinition] Error", ex);
      throw new SystemException("GFManagerBean.findGFDefinition(uid) Error", ex);
    }
    GFLog.log("[GFManagerBean.findGFDefinition] Exit");
    return definition;
  }

  /**
   * Find the GFDefinition entity that matches the name.
   *
   * @param name The name of the entity.
   * @return The GFDefinition entity found, or null if it does not exists.
   * @since 2.0
   */
  public GFDefinition findGFDefinition(String name) throws FindEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.findGFDefinition] Name: " + name);
    GFDefinition definition = null;
    try
    {
      definition = GFDefinitionEntityHandler.getInstance().findByName(name);
    }
    catch (ApplicationException ex)
    {
      GFLog.err("[GFManagerBean.findGFDefinition] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      GFLog.err("[GFManagerBean.findGFDefinition] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.findGFDefinition] Error ", ex);
      throw new SystemException("GFManagerBean.findGFDefinition(name) Error", ex);
    }
    return definition;
  }

  /**
   * Find a number of GFDefinition entities that satisfy the filtering
   * condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GFDefinition entities found, or a empty collection
   * if none exists.
   * @since 2.0
   */
  public Collection findGFDefinitions(IDataFilter filter) throws FindEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.findGFDefinitions] Entry, filter: " + filter.getFilterExpr());
    Collection definitions = null;
    try
    {
      definitions = GFDefinitionEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      GFLog.err("[GFManagerBean.findGFDefinitions] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      GFLog.err("[GFManagerBean.findGFDefinitions] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.findGFDefinitions] Error", ex);
      throw new SystemException("GFManagerBean.findGFDefinitions(IDataFilter) Error", ex);
    }
    GFLog.log("[GFManagerBean.findGFDefinitions] Exit");
    return definitions;
  }

  // ---------------------------------------------------------------------------

  /**
   * Create a new GFTemplate entity.
   *
   * @param template The GFTemplate entity to create.
   * @return The UID of the created template.
   * @since 2.0
   */
  public Long createGFTemplate(GFTemplate template) throws CreateEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.createGFTemplate] Entry");
    try
    {
      IGFTemplateLocalObj obj = GFTemplateEntityHandler.getInstance().createGFTemplate(template);
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      GFLog.err("[GFManagerBean.createGFTemplate] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.createGFTemplate] Error", ex);
      throw new SystemException("GFManagerBean.createGFTemplate(GFTemplate) Error", ex);
    }
    finally
    {
      GFLog.log("[GFManagerBean.createGFTemplate] Exit");
    }
  }

  /**
   * Update a GFTemplate entity.
   *
   * @param template The GFTemplate entity to update.
   * @since 2.0
   */
  public void updateGFTemplate(GFTemplate template) throws UpdateEntityException, SystemException
  {
    GFLog.log("GFManagerBean.updateGFTemplate] Entry");
    try
    {
      GFTemplateEntityHandler.getInstance().updateGFTemplate(template);
    }
    catch (EntityModifiedException ex)
    {
      GFLog.err("[GFManagerBean.updateGFTemplate] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.updateGFTemplate] Error", ex);
      throw new SystemException("GFManagerBean.updateGFTemplate(GFTemplate) Error", ex);
    }
    GFLog.log("[GFManagerBean.updateGFTemplate] Exit");
  }

  /**
   * Delete a GFTemplate entity.
   *
   * @param uid The Unique ID of the GFTemplate to delete.
   * @since 2.0
   */
  public void deleteGFTemplate(Long uid) throws DeleteEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.deleteGFTemplate] Entry");
    try
    {
      GFTemplateEntityHandler.getInstance().deleteGFTemplate(uid);
    }
    catch (RemoveException ex)
    {
      GFLog.err("[GFManagerBean.deleteGFTemplate] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.deleteGFTemplate] Error", ex);
      throw new SystemException("GFManagerBean.deleteGFTemplate(uid) Error", ex);
    }
    GFLog.log("[GFManagerBean.deleteGFTemplate] Exit");
  }

  /**
   * Find the GFTemplate entity that matches the UID.
   *
   * @param uid The Unique ID of the entity.
   * @return The GFTemplate entity found, or null if it does not exists.
   * @since 2.0
   */
  public GFTemplate findGFTemplate(Long uid) throws FindEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.findGFTemplate] Entry, UID: " + uid);
    GFTemplate template = null;
    try
    {
      template = (GFTemplate)GFTemplateEntityHandler.getInstance().getEntityByKeyForReadOnly(uid);
    }
    catch (ApplicationException ex)
    {
      GFLog.err("[GFManagerBean.findGFTemplate] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      GFLog.err("[GFManagerBean.findGFTemplate] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.findGFTemplate] Error", ex);
      throw new SystemException("GFManagerBean.findGFTemplate(uid) Error", ex);
    }
    GFLog.log("[GFManagerBean.findGFTemplate] Exit");
    return template;
  }

  /**
   * Find the GFTemplate entity that matches the name.
   *
   * @param name The name of the entity.
   * @return The GFTemplate entity found, or null if it does not exists.
   * @since 2.0
   */
  public GFTemplate findGFTemplate(String name) throws FindEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.findGFTemplate] Name: " + name);
    GFTemplate template = null;
    try
    {
      template = GFTemplateEntityHandler.getInstance().findByName(name);
    }
    catch (ApplicationException ex)
    {
      GFLog.err("[GFManagerBean.findGFTemplate] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      GFLog.err("[GFManagerBean.findGFTemplate] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.findGFTemplate] Error ", ex);
      throw new SystemException("GFManagerBean.findGFTemplate(name) Error", ex);
    }
    return template;
  }

  /**
   * Find a number of GFTemplate entities that satisfy the filtering
   * condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GFTemplate entities found, or a empty collection
   * if none exists.
   * @since 2.0
   */
  public Collection findGFTemplates(IDataFilter filter) throws FindEntityException, SystemException
  {
    GFLog.log("[GFManagerBean.findGFTemplates] Entry, filter: " + filter.getFilterExpr());
    Collection templates = null;
    try
    {
      templates = GFTemplateEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      GFLog.err("[GFManagerBean.findGFTemplates] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      GFLog.err("[GFManagerBean.findGFTemplates] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.findGFTemplates] Error", ex);
      throw new SystemException("GFManagerBean.findGFTemplates(IDataFilter) Error", ex);
    }
    GFLog.log("[GFManagerBean.findGFTemplates] Exit");
    return templates;
  }

  // ---------------------------------------------------------------------------

  /**
   * Get a <code>GFForm</code> value object that contains the template,
   * definition and depository with the given filename of a XML document.
   *
   * @param filename The filename of the XML document.
   * @return The <code>GFForm</code> value object.
   * @since 2.0
   */
  public GFForm loadForm(String xmlFile) throws LoadFormException
  {
    GFLog.log("[GFManagerBean.loadForm] Entry, filename: " + xmlFile);
    GFForm form = null;
    try
    {
      // Init.
      String PATH_GRIDFORM = "work/gridform/";
      DataDepository depository = null;
      LayoutTemplate template = null;
      LayoutDefinition definition = null;

      // Load the data depository.
      depository = XMLFacade.getInstance().loadDepository(PATH_GRIDFORM + xmlFile);

      // Get the depository root.
      String root = depository.getID().toString();
      GFLog.log("[GFManagerBean.loadForm] Root: " + root);

      // Get all definition entities from database.
      Collection result = GFDefinitionEntityHandler.getInstance().getEntityByFilter(null);

      // Iterate through all definition entities.
      Iterator iterator = result.iterator();
      boolean hasFound = false;
      while (iterator.hasNext() && !hasFound)
      {
        GFDefinition defEntity = (GFDefinition)iterator.next();

        // Load the definition file.
        definition = XMLFacade.getInstance().loadDefinition(PATH_GRIDFORM + defEntity.getFilename());

        // Compare the definition root with the depository root.
        String defRoot = definition.getRootElement();
        if (root.equals(defRoot))
        {
          // Found matching root. Stop searching.
          hasFound = true;

          // Get the template entity related to this definition entity
//          GFTemplate temEntity = (GFTemplate)GFTemplateEntityHandler.getInstance().findByPrimaryKey(defEntity.getTemplate()); // 20020813 DDJ: Changed getTemplateName() to getTemplate()
          GFTemplate temEntity = defEntity.getTemplate(); // 20020819 DDJ: Changed getTemplate() to return a GFTemplate instead of a long

          // Load the template file.
          template = XMLFacade.getInstance().loadTemplate(PATH_GRIDFORM + temEntity.getFilename());
        }
      }
      if (hasFound)
      {
        form = new GFForm();
        form.setDefinition(definition);
        form.setDepository(depository);
        form.setTemplate(template);
      }
      else throw new Exception("No matching definition found");


/** @todo: !!!!!!!!!!!!!!!  FILE ACCESS DOESN'T WORK !!!!!!!!!!!!!!!!!

      // Init.
      FileAccess access = new FileAccess();
      DataDepository depository = null;
      LayoutTemplate template = null;
      LayoutDefinition definition = null;

      // Get the depository file.
      File depFile = access.getFile(PATH_GRIDFORM + xmlFile);

      // Load the data depository.
      depository = XMLFacade.getInstance().loadDepository(depFile);

      // Get the depository root.
      String root = depository.getID().toString();
      GFLog.log("[GFManagerBean.loadForm] Root: " + root);

      // Get all definition entities from database.
      Collection result = GFDefinitionEntityHandler.getInstance().findByFilter(null);

      // Iterate through all definition entities.
      Iterator iterator = result.iterator();
      boolean hasFound = false;
      while (iterator.hasNext() && !hasFound)
      {
        // Get the definition file.
        GFDefinition defEntity = (GFDefinition)iterator.next();
        File defFile = access.getFile(PATH_GRIDFORM + defEntity.getFilename());

        // Load the definition file.
        definition = XMLFacade.getInstance().loadDefinition(defFile);

        // Compare the definition root with the depository root.
        String defRoot = definition.getRootElement();
        if (root.equals(defRoot))
        {
          // Found matching root. Stop searching.
          hasFound = true;

          // Get the template entity related to this definition entity
          GFTemplate temEntity = GFTemplateEntityHandler.getInstance().findByName(defEntity.getTemplateName());

          // Get the template file.
          File temFile = access.getFile(PATH_GRIDFORM + temEntity.getFilename());

          // Load the template file.
          template = XMLFacade.getInstance().loadTemplate(temFile);
        }
      }
      if (hasFound)
      {
        form = new GFForm();
        form.setDefinition(definition);
        form.setDepository(depository);
        form.setTemplate(template);
      }
      else throw new Exception("No matching definition found");
*/
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.loadForm] Error", ex);
      throw new LoadFormException("GFManagerBean.loadForm(filename) Error", ex);
    }
    GFLog.log("[GFManagerBean.loadForm] Exit");
    return form;
  }

  /**
   * Save the depository in the <code>GFForm</code> value object into a XML
   * document with the given filename.
   *
   * @param filename The filename of the XML document.
   * @param form The <code>GFForm</code> value object.
   * @since 2.0
   */
  public void saveForm(String xmlFile, GFForm gfForm) throws SaveFormException
  {
    GFLog.log("[GFManagerBean.saveForm] Entry");
    try
    {
      DataDepository depository = gfForm.getDepository();
      //XMLFacade.getInstance().saveDepository(depository);
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.saveForm] Error", ex);
      throw new SaveFormException("GFManagerBean.saveForm(form) Error", ex);
    }
    GFLog.log("[GFManagerBean.saveForm] Exit");
  }

  /**
   * Populate the form with data.
   *
   * Data from the depository will be set into the template based on the
   * mapping definitions.
   *
   * @param gfForm The GFForm.
   * @return The modified GFForm.
   * @since 2.0
   */
  public GFForm populateFormData(GFForm gfForm) throws InvalidAccessException
  {
    GFLog.log("[GFManagerBean.populateFormData] Entry");
    try
    {
      DataDepository depository = gfForm.getDepository();
      LayoutTemplate template = gfForm.getTemplate();
      LayoutDefinition definition = gfForm.getDefinition();

      List mapping = definition.getMapping();
      Iterator iterator = mapping.iterator();
      while (iterator.hasNext())
      {
        SourceMap map = (SourceMap)iterator.next();
        GFLog.log("[GFManagerBean.populateFormData] Mapping " + map.getName() + " to " + map.getXpath());
        String data = depository.getValue(map.getXpath()).toString();
        template.setValue(map.getName(), data);
      }
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.populateFormData] Error", ex);
      throw new InvalidAccessException("GFManagerBean.populateFormData(form) Error", ex);
    }
    GFLog.log("[GFManagerBean.populateFormData] Exit");
    return gfForm;
  }

  /**
   * Generate the HTML output from the form.
   *
   * @param gfForm The GFForm.
   * @return String The HTML output.
   * @since 2.0
   */
  public String generateHTMLOutput(GFForm gfForm) throws InvalidAccessException
  {
    GFLog.log("[GFManagerBean.generateHTMLOutput] Entry");
    String htmlOutput;
    try
    {
      LayoutTemplate template = gfForm.getTemplate();
      htmlOutput = XMLFacade.getInstance().generateTemplateString(template);
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.generateHTMLOutput] Error", ex);
      throw new InvalidAccessException("GFManagerBean.generateHTMLOutput(form) Error", ex);
    }
    GFLog.log("[GFManagerBean.generateHTMLOutput] Exit");
    return htmlOutput;
  }

  /**
   * Generate the HTML output from the form.
   *
   * @param gfForm The GFForm.
   * @return org.w3c.dom.Document The HTML output.
   * @since 2.0
   */
  // 20020610 DDJ: Added method
  public org.w3c.dom.Document generateHTMLOutputAsW3CDocument(GFForm gfForm) throws InvalidAccessException
  {
    GFLog.log("[GFManagerBean.generateHTMLOutput] Entry");
    org.w3c.dom.Document htmlOutput = null;
    try
    {
      LayoutTemplate template = gfForm.getTemplate();
      htmlOutput = XMLFacade.getInstance().generateTemplateW3CDocument(template);
    }
    catch (Throwable ex)
    {
      GFLog.err("[GFManagerBean.generateHTMLOutput] Error", ex);
      throw new InvalidAccessException("GFManagerBean.generateHTMLOutput(form) Error", ex);
    }
    GFLog.log("[GFManagerBean.generateHTMLOutput] Exit");
    return htmlOutput;
  }
}