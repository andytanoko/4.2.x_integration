/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IXMLServiceObject.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Koh Han Sing        Modified to confront to coding standard
 * Jul 23 2002    Koh Han Sing        Change to return File instead of
 *                                    ByteArrayOutputStream
 * Jun 23 2003    Koh Han Sing        Use XML services from gn-xml, gn-convertor
 */
package com.gridnode.pdip.base.xml.facade.ejb;

/**
 * <p>Title: PDIP</p>
 * <p>Description: Peer Data Interchange Platform</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode</p>
 * @author unascribed
 * @version 1.0
 */

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBLocalObject;

import com.gridnode.pdip.base.xml.exceptions.XMLException;
import com.gridnode.xml.adapters.GNDocument;
import com.gridnode.xml.adapters.GNDocumentType;
import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNNamespace;

public interface IXMLServiceLocalObj extends EJBLocalObject
{
//  public void convert(String input, String output, String rules,
//                      IConvertorController controller)
//                      throws XMLException;

  public File convert(String input, String rules)
                      throws XMLException;

//  public ArrayList extractXPathFromFile(String filename)
//    throws XMLException;
  
  //added by ming qian 
  public File convertJar(String input, String className, String fileName, String path, String fileExt) throws XMLException;
  
  public File transformJar(String input, String className, String path, String fileName) throws XMLException;
  
  //end of added by ming qian
  

  public ArrayList splitXML(String styleSheet,
                            String inputFile,
                            String xPath,
                            String paraName)
                            throws XMLException;

  public File transform(String styleSheet, String inputFile)
    throws XMLException;

  public File transform(String styleSheet,
                        String inputFile,
                        Hashtable parameters)
                        throws XMLException;

  public ArrayList validate(String xmlFileFullPath,
                            String dictFileFullPath,
                            String dtdFileFullPath,
                            int maxErrors)
                            throws XMLException;

  /**
   * This method will validate an XML file based on the DTD provided.
   *
   * @param xmlFileFullPath the full path of the XML file to be validated
   * @param dictFileFullPath the full path of the dictionary file
   * @return true if no validation errors otherwise throws XMLException
   */
  public boolean validateDTD(String xmlFileFullPath,
                             String dtdFileFullPath)
                             throws XMLException;

  /**
   * This method will validate an XML file based on the DTD provided.
   *
   * @param xmlFileFullPath the full path of the XML file to be validated
   * @param dictFileFullPath the full path of the dictionary file
   * @param systemId the path used to locate the dtd if it is not found in the
   *                 path specified in the second param
   * @return true if no validation errors otherwise throws XMLException
   */
  public boolean validateDTD(String xmlFileFullPath,
                             String dtdFileFullPath,
                             String systemId)
                             throws XMLException;

  public void appendXML(String orgFile, String appendFile)
    throws XMLException;

  public List getXPathValues(String xmlFile, String xpath)
    throws XMLException;

  /**
   * This method validates a XML file against the given schema.
   *
   * @param filename the fullpath of the xml file to be validated
   * @param schema the schema used to validate the xml file
   * @return true if validation is successful, throws exception otherwise
   */
  public boolean validateSchema(String filename, String schema)
    throws XMLException;

  /**
   * This method validates a XML file against the given schema.
   *
   * @param filename the fullpath of the xml file to be validated
   * @param schema the schema used to validate the xml file
   * @param dtdSystemID the url used to locate the schema
   * @return true if validation is successful, throws exception otherwise
   */
  public boolean validateSchema(String filename, String schema, String dtdSystemID)
    throws XMLException;

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param filename the full path of the XML file.
   * @return the root element of the XML file.
   */
  public GNElement getRoot(String filename)
    throws XMLException;

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileObj the File object representing the XML file.
   * @return the root element of the XML file.
   */
  public GNElement getRoot(File fileObj)
    throws XMLException;

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileFIS the InputStream of the XML file.
   * @return the root element of the XML file.
   */
  public GNElement getRoot(InputStream fileFIS)
    throws XMLException;

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param filename the full path of the XML file.
   * @param systemId base for resolving relative URIs
   * @return the root element of the XML file.
   */
  public GNElement getRoot(String filename, String systemId)
    throws XMLException;

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileObj the File object representing the XML file.
   * @param systemId base for resolving relative URIs
   * @return the root element of the XML file.
   */
  public GNElement getRoot(File fileObj, String systemId)
    throws XMLException;

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileFIS the InputStream of the XML file.
   * @param systemId base for resolving relative URIs
   * @return the root element of the XML file.
   */
  public GNElement getRoot(InputStream fileFIS, String systemId)
    throws XMLException;

  /**
   * This method will retrieve the root element name from a given XML file.
   *
   * @param filename the full path of the XML file.
   * @return the name of the root element of the XML file.
   */
  public String getRootName(String filename)
    throws XMLException;

  /**
   * This method will retrieve the root element name from a given XML file.
   *
   * @param fileObj the File object representing the XML file.
   * @return the name of the root element of the XML file.
   */
  public String getRootName(File fileObj)
    throws XMLException;

  /**
   * This method will retrieve the filename of the DTD used in the XML file.
   *
   * @param xmlFile the XML file to extract the DTD filename.
   * @return the name of the DTD file used.
   */
  public String extractDTDFilename(String xmlFile)
    throws XMLException;

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(File xmlFile)
    throws XMLException;

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(String xmlFile)
    throws XMLException;

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(InputStream xmlFile)
    throws XMLException;

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(File xmlFile, String systemId)
    throws XMLException;

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(String xmlFile, String systemId)
    throws XMLException;

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(InputStream xmlFile, String systemId)
    throws XMLException;

  /**
   * This method will write the GNDocument object into a XML file
   *
   * @param doc the GNDocument to be written into a XML file.
   * @param outputFile the filename to write to.
   * @param indent whether to indent the output content.
   * @param newline whether to output newlines.
   */
  public void writeToFile(GNDocument doc, String outputFile,
                          boolean indent, boolean newline)
    throws XMLException;

  /**
   * This method will write the GNElement object into a XML file
   *
   * @param root the root element to be written into a XML file.
   * @param outputFile the filename to write to.
   * @param indent whether to indent the output content.
   * @param newline whether to output newlines.
   */
  public void writeToFile(GNElement root, String outputFile,
                          boolean indent, boolean newline)
    throws XMLException;

  /**
   * This method will write the GNElement object into a XML file
   *
   * @param root the root element to be written into a XML file.
   * @param dtdFile the dtd file to be set to the output XML file.
   * @param outputFile the filename to write to.
   * @param indent whether to indent the output content.
   * @param newline whether to output newlines.
   */
  public void writeToFile(GNElement root, String dtdFile, String outputFile,
                          boolean indent, boolean newline)
    throws XMLException;

  /**
   * This method will write the GNDocument object into a ByteArrayOutputStream
   *
   * @param doc the document to be written into a ByteArrayOutputStream.
   * @param indent whether to indent the output content.
   * @param newline whether to output newlines.
   */
//  public ByteArrayOutputStream writeToStream(GNDocument doc, boolean indent,
//                                             boolean newline)
//    throws XMLException;

  /**
   * This method will write the GNElement object into a ByteArrayOutputStream
   *
   * @param root the root element to be written into a ByteArrayOutputStream.
   * @param indent whether to indent the output content.
   * @param newline whether to output newlines.
   */
//  public ByteArrayOutputStream writeToStream(GNElement root, boolean indent,
//                                             boolean newline)
//    throws XMLException;

  /**
   * This method will write the GNDocument object into a ByteArrayOutputStream
   *
   * @param root the root element to be written into a ByteArrayOutputStream.
   * @param dtdFile the dtd file to be set to the output XML file.
   * @param indent whether to indent the output content.
   * @param newline whether to output newlines.
   */
//  public ByteArrayOutputStream writeToStream(GNElement root, String dtdFile,
//                                             boolean indent, boolean newline)
//    throws XMLException;

  /**
   * This method will create a new GNDocument
   *
   * @return the GNDocument created.
   */
  public GNDocument newDocument()
    throws XMLException;

  /**
   * This method will create a new GNDocumentType
   *
   * @param elementName the name of the element that the DTD belongs to
   * @param publicID publicId of the DTD
   * @param systemID systemId of the DTD
   * @return the GNDocumentType created.
   */
  public GNDocumentType newDocumentType(String elementName,
    String publicID, String systemID)
    throws XMLException;

  /**
   * This method will create a new GNElement
   *
   * @param name the name of the element
   * @return the GNElement created.
   */
  public GNElement newElement(String name)
    throws XMLException;

  /**
   * This method will create a new GNElement
   *
   * @param name the name of the element
   * @param prefix the namespace prefix
   * @param uri the URI of the namespace
   * @return the GNElement created.
   */
  public GNElement newElement(String name, String prefix, String uri)
    throws XMLException;

  /**
   * This method will create a namespace base on the prefix and URI provided
   *
   * @param prefix the prefix to map to the namespace.
   * @param uri URI of the namespace.
   * @return the namespace created.
   */
  public GNNamespace newNamespace(String prefix, String uri)
    throws XMLException;
}