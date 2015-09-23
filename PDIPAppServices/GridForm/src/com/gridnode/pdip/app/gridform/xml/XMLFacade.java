/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLFacade.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 * May 24 2002    Jared Low           Overloaded the services.
 */
package com.gridnode.pdip.app.gridform.xml;

import com.gridnode.pdip.app.gridform.helpers.GFLog;
import com.gridnode.pdip.app.gridform.model.*;
import com.gridnode.pdip.app.gridform.exceptions.XMLException;

import java.io.File;

/**
 * Facade for all GridForm related XML services.
 *
 * All classes in this package must be in package level scope and their services
 * only to be exposed through this facade.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class XMLFacade
{
  private static XMLFacade _self;
  private LayoutTemplateReader _templateReader      = new LayoutTemplateReader();
  private LayoutDefinitionReader _definitionReader  = new LayoutDefinitionReader();
  private XMLDataDepositoryReader _depositoryReader = new XMLDataDepositoryReader();

  /**
   * Singleton.
   *
   * @since 2.0
   */
  private XMLFacade()
  {
    _self = this;
  }

  /**
   * Return the instance of this Facade.
   *
   * @return The instance of this Facade.
   * @since 2.0
   */
  public static synchronized XMLFacade getInstance()
  {
    return (_self != null ? _self : new XMLFacade());
  }

  /**
   * Loads the layout template file as an object.
   *
   * @param filename The filename of the layout template.
   * @return The <code>LayoutTemplate</code> object containing the properties in
   * the layout template file.
   * @since 2.0
   */
  public LayoutTemplate loadTemplate(String filename) throws XMLException
  {
    return loadTemplate(new File(filename));
  }

  /**
   * Loads the layout template file as an object.
   *
   * @param file The <code>File</code> object that represents the layout
   * template.
   * @return The <code>LayoutTemplate</code> object containing the properties in
   * the layout template file.
   * @since 2.0
   */
  public LayoutTemplate loadTemplate(File file) throws XMLException
  {
    GFLog.log("[XMLFacade.loadTemplate] File: " + file.getName());
    LayoutTemplate template = null;
    try
    {
      template = _templateReader.loadTemplate(file);
    }
    catch (Throwable ex)
    {
      GFLog.err("[XMLFacade.loadTemplate] XML Error", ex);
      throw new XMLException("XMLFacade.loadTemplate(filename) Error", ex);
    }
    return template;
  }

  // ---------------------------------------------------------------------------

  /**
   * Loads the layout definition file as an object.
   *
   * @param filename The filename of the layout definition.
   * @return The <code>LayoutDefinition</code> object containing the
   * associations and mappings properties in the layout definition file.
   * @since 2.0
   */
  public LayoutDefinition loadDefinition(String filename) throws XMLException
  {
    return loadDefinition(new File(filename));
  }

  /**
   * Loads the layout definition file as an object.
   *
   * @param file The <code>File</code> object that represents the layout
   * definition.
   * @return The <code>LayoutDefinition</code> object containing the
   * associations and mappings properties in the layout definition file.
   * @since 2.0
   */
  public LayoutDefinition loadDefinition(File file) throws XMLException
  {
    GFLog.log("XMLFacade.loadDefinition] File: " + file.getName());
    LayoutDefinition definition = null;
    try
    {
      definition = _definitionReader.loadDefinition(file);
    }
    catch (Throwable ex)
    {
      GFLog.err("[XMLFacade.loadDefinition] XML Error", ex);
      throw new XMLException("XMLFacade.loadDefinition(filename) Error", ex);
    }
    return definition;
  }

  // ---------------------------------------------------------------------------

  /**
   * Loads a XML document as a <code>DataDepository</code> object.
   *
   * @param filename The filename of the XML document.
   * @return The <code>DataDepository</code> object that represents the XML
   * document.
   * @since 2.0
   */
  public DataDepository loadDepository(String filename) throws XMLException
  {
    return loadDepository(new File(filename));
  }

  /**
   * Loads a XML document as a <code>DataDepository</code> object.
   *
   * @param file The <code>File</code> object that represents the XML document.
   * @return The <code>DataDepository</code> object that represents the XML
   * document.
   * @since 2.0
   */
  public DataDepository loadDepository(File file) throws XMLException
  {
    GFLog.log("[XMLFacade.loadDepository] File: " + file.getName());
    XMLDataDepository depository = null;
    try
    {
      depository = _depositoryReader.loadDepository(file);
    }
    catch (Throwable ex)
    {
      GFLog.err("[XMLFacade.loadDepository] XML Error", ex);
      throw new XMLException("XMLFacade.loadDepository(filename) Error", ex);
    }
    return depository;
  }

  // ---------------------------------------------------------------------------

  /**
   * Generate a string output of a layout template.
   *
   * @param template The <code>LayoutTemplate</code> object.
   * @return The whole layout template as a string.
   * @since 2.0
   */
  public String generateTemplateString(LayoutTemplate template) throws XMLException
  {
    GFLog.log("[XMLFacade.convertTemplateToString] Entry");
    try
    {
      return _templateReader.generateString(template);
    }
    catch (Throwable ex)
    {
      GFLog.err("[XMLFacade.convertTemplateToString] XML Error", ex);
      throw new XMLException("XMLFacade.convertTemplateToString(template) Error", ex);
    }
  }

  // ---------------------------------------------------------------------------

  /**
   * Generate a W3C Document output of a layout template.
   *
   * @param template The <code>LayoutTemplate</code> object.
   * @return The whole layout template as a W3C Document.
   * @since 2.0
   */
  // 20020610 DDJ: Added method
  public org.w3c.dom.Document generateTemplateW3CDocument(LayoutTemplate template) throws XMLException
  {
    GFLog.log("[XMLFacade.convertTemplateToString] Entry");
    try
    {
      return _templateReader.generateW3CDocument(template);
    }
    catch (Throwable ex)
    {
      GFLog.err("[XMLFacade.convertTemplateToString] XML Error", ex);
      throw new XMLException("XMLFacade.convertTemplateToString(template) Error", ex);
    }
  }
}