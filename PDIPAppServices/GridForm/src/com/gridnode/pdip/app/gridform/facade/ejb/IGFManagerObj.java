/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGFManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 * May 24 2002    Jared Low           Added findByName finder methods to both
 *                                    entities.
 */
package com.gridnode.pdip.app.gridform.facade.ejb;

import com.gridnode.pdip.app.gridform.model.*;
import com.gridnode.pdip.app.gridform.exceptions.*;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.*;

import javax.ejb.EJBObject;
import javax.ejb.CreateException;
import javax.ejb.Handle;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Remote interface for GFManagerBean.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public interface IGFManagerObj extends EJBObject
{
  /**
   * Create a new GFDefinition entity.
   *
   * @param definition The GFDefinition entity to create.
   * @return The UID of the created definition.
   * @since 2.0
   */
  public Long createGFDefinition(GFDefinition definition) throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a GFDefinition entity.
   *
   * @param definition The GFDefinition entity to update.
   * @since 2.0
   */
  public void updateGFDefinition(GFDefinition definition) throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a GFDefinition entity.
   *
   * @param definition The GFDefinition entity to delete.
   * @since 2.0
   */
  public void deleteGFDefinition(Long uid) throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find the GFDefinition entity that matches the UID.
   *
   * @param uid The Unique ID of the entity.
   * @return The GFDefinition entity found, or null if it does not exists.
   * @since 2.0
   */
  public GFDefinition findGFDefinition(Long uid) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the GFDefinition entity that matches the name.
   *
   * @param name The name of the entity.
   * @return The GFDefinition entity found, or null if it does not exists.
   * @since 2.0
   */
  public GFDefinition findGFDefinition(String name) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of GFDefinition entities that satisfy the filtering
   * condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GFDefinition entities found, or a empty collection
   * if none exists.
   * @since 2.0
   */
  public Collection findGFDefinitions(IDataFilter filter) throws FindEntityException, SystemException, RemoteException;

  // ---------------------------------------------------------------------------

  /**
   * Create a new GFTemplate entity.
   *
   * @param template The GFTemplate entity to create.
   * @return The UID of the created template.
   * @since 2.0
   */
  public Long createGFTemplate(GFTemplate template) throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a GFTemplate entity.
   *
   * @param template The GFTemplate entity to update.
   * @since 2.0
   */
  public void updateGFTemplate(GFTemplate template) throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a GFTemplate entity.
   *
   * @param uid The Unique ID of the GFTemplate to delete.
   * @since 2.0
   */
  public void deleteGFTemplate(Long uid) throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find the GFTemplate entity that matches the UID.
   *
   * @param uid The Unique ID of the entity.
   * @return The GFTemplate entity found, or null if it does not exists.
   * @since 2.0
   */
  public GFTemplate findGFTemplate(Long uid) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the GFTemplate entity that matches the name.
   *
   * @param name The name of the entity.
   * @return The GFTemplate entity found, or null if it does not exists.
   * @since 2.0
   */
  public GFTemplate findGFTemplate(String name) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of GFTemplate entities that satisfy the filtering
   * condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GFTemplate entities found, or a empty collection
   * if none exists.
   * @since 2.0
   */
  public Collection findGFTemplates(IDataFilter filter) throws FindEntityException, SystemException, RemoteException;

  // ---------------------------------------------------------------------------

  /**
   * Load a form for viewing or editing.
   *
   * @param xmlFile The XML file for the form to open upon.
   * @return A GFForm that contains the Depository, Template and Definition
   * models.
   * @since 2.0
   */
  public GFForm loadForm(String xmlFile) throws LoadFormException, RemoteException;

  /**
   * Save a form that has been edited.
   *
   * @param xmlFile The XML file for the form to save to.
   * @param gfForm The GFForm.
   * @since 2.0
   */
  public void saveForm(String xmlFile, GFForm gfForm) throws SaveFormException, RemoteException;

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
  public GFForm populateFormData(GFForm gfForm) throws InvalidAccessException, RemoteException;

  /**
   * Generate the HTML output from the form.
   *
   * @param gfForm The GFForm.
   * @return String The HTML output.
   * @since 2.0
   */
  public String generateHTMLOutput(GFForm gfForm) throws InvalidAccessException, RemoteException;

  /**
   * Generate the HTML output from the form.
   *
   * @param gfForm The GFForm.
   * @return org.w3c.dom.Document The HTML output.
   * @since 2.0
   */
  // 20020610 DDJ: Added method
  public org.w3c.dom.Document generateHTMLOutputAsW3CDocument(GFForm gfForm) throws InvalidAccessException, RemoteException;
}