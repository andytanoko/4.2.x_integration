/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLServiceBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Koh Han Sing        Modified to confront to coding standard
 * Jul 23 2002    Koh Han Sing        Change to return File instead of
 *                                    ByteArrayOutputStream
 * Jun 23 2003    Koh Han Sing        Use XML services from gn-xml, gn-convertor
 * Dec 05 2007    Tam Wei Xiang       Fix GNDB00028483: XSLT transformation.
 */
package com.gridnode.pdip.base.xml.facade.ejb;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.xml.exceptions.XMLException;
import com.gridnode.pdip.base.xml.helpers.XMLServiceHandler;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.xml.adapters.GNDocument;
import com.gridnode.xml.adapters.GNDocumentType;
import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNNamespace;

public class XMLServiceBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3931259615773383446L;
	private SessionContext ctx;

  public XMLServiceBean()
  {
  }

  /**
   * This method converts a file base on the given conversion rule and outputs
   * to the given output filename.
   *
   * @param input the full path of the input file to be converted
   * @param output the output filename to be written to
   * @param rule the full path of the conversion rule
   * @param controller the controller that is used to retreive application info
   */
//  public void convert(String input, String output, String rule)
//                      throws XMLException
//  {
//    XMLServiceHandler handler = XMLServiceHandler.getInstance();
//    handler.convert(input, output, rule, controller);
//  }

  /**
   * This method converts a file base on the given conversion rule.
   *
   * @param input the full path of the input file to be converted
   * @param rule the full path of the conversion rule
   * @return the File of the converted file
   * @param controller the controller that is used to retreive application info
   */
  public File convert(String input, String rule)
    throws XMLException
  {
    try
    {
      XMLServiceHandler handler = XMLServiceHandler.getInstance();
      return handler.convert(input, rule);
    }
    catch (XMLException xmlex)
    {
      throw xmlex;
    }
    catch (Exception ex)
    {
      throw new XMLException("XMLException", ex);
    }
  }
  
  public File convertJar(String inputFile, String className, String fileName, String path, String fileExt) throws XMLException
  {
    try
    {
      XMLServiceHandler handler = XMLServiceHandler.getInstance();
      return handler.convertJar(inputFile, className, fileName, path, fileExt);
    }
    catch (XMLException xmlex)
    {
      throw xmlex;
    }
    catch (Exception ex)
    {
      throw new XMLException("XMLException", ex);
    }
  }

  /**
   * This method extracts all the xpaths from the given XML file.
   *
   * @param filename the full path of the XML file
   * @return the ArrayList of String containing all the xpaths
   */
//  public ArrayList extractXPathFromFile(String filename)
//    throws XMLException
//  {
//    XMLServiceHandler handler = XMLServiceHandler.getInstance();
//    ArrayList list = handler.extractXPathFromFile(filename);
//    return list;
//  }

  /**
   * This method splits an XML file with the given stylesheet, xpath and
   * the parameter used in the stylesheet.
   *
   * @param stylesheet the full path of the stylesheet used to split the XML
   *                   document
   * @param inputFile the XML file to be splitted
   * @param xPath the xpath that will contain the value used to split the XML
   *              document
   * @param paramName the name of the parameter used in the stylesheet to pass
   *                  in the values to be compared to the value in the xpath
   * @return a ArrayList of File of the splitted files.
   */
  public ArrayList splitXML(String styleSheet,
                            String inputFile,
                            String xPath,
                            String paraName)
                            throws XMLException
  {
    try
    {
      XMLServiceHandler handler = XMLServiceHandler.getInstance();
      return handler.splitXML(styleSheet, inputFile, xPath, paraName);
    }
    catch (XMLException xmlex)
    {
      throw xmlex;
    }
    catch (Exception ex)
    {
      throw new XMLException("XMLException", ex);
    }
  }

  /**
   * This method transform a XML file with the given stylesheet.
   *
   * @param stylesheet the stylesheet used to transform the XML file
   * @param inputFile the full path of the XML file to be transformed
   * @return the File of the transformed file
   */
  public File transform(String styleSheet, String inputFile)
    throws XMLException
  {
    try
    {
      //TWX:Fix GNDB00028483 XSLT transformation issue
      
      XMLServiceHandler handler = XMLServiceHandler.getInstance();
      return handler.transform(styleSheet, FileUtil.convertPath(inputFile));
    }
    catch (XMLException xmlex)
    {
      throw xmlex;
    }
    catch (Exception ex)
    {
      throw new XMLException("XMLException", ex);
    }
  }
  
  public File transformJar(String inputFile, String className, String path, String fileName) throws XMLException
  {
    try
    {
      //TWX:Fix GNDB00028483 XSLT transformation issue
      
      XMLServiceHandler handler = XMLServiceHandler.getInstance();
      return handler.transformJar(inputFile, className, path, fileName);
    }
    catch (XMLException xmlex)
    {
      throw xmlex;
    }
    catch (Exception ex)
    {
      throw new XMLException("XMLException", ex);
    }
  }

  /**
   * This method transform a XML file with the given stylesheet and parameters.
   * The parameters are stored in a Hashtable with the param name as the key
   * and the value of the param as the value.
   *
   * @param stylesheet the stylesheet used to transform the XML file
   * @param inputFile the full path of the XML file to be transformed
   * @param parameters the Hashtable containing parameters used in the
   *                   stylesheet.
   * @return the File of the transformed file
   */
  public File transform(String styleSheet,
                        String inputFile,
                        Hashtable parameters)
                        throws XMLException
  {
    try
    {
//    TWX:Fix GNDB00028483 XSLT transformation issue
      XMLServiceHandler handler = XMLServiceHandler.getInstance();
      return handler.transform(styleSheet, FileUtil.convertPath(inputFile), parameters);
    }
    catch (XMLException xmlex)
    {
      throw xmlex;
    }
    catch (Exception ex)
    {
      throw new XMLException("XMLException", ex);
    }

  }

  /**
   * This method will validate an XML file based on the DTD and dictionary file
   * provided.
   *
   * @param xmlFileFullPath the full path of the XML file to be validated
   * @param dictFileFullPath the full path of the dictionary file
   * @param dtdFileFullPath the full path of the DTD file
   * @param maxErrors the maximum number of errors that can occur before an
   *                  error is reported.
   * @return a ArrayList of element names that contains invalid values not
   *         stated in the dictionary file.
   */
  public ArrayList validate(String xmlFileFullPath,
                            String dictFileFullPath,
                            String dtdFileFullPath,
                            int maxErrors)
                            throws XMLException
  {
    XMLServiceHandler handler = XMLServiceHandler.getInstance();
    ArrayList list = handler.validate(xmlFileFullPath,
                                      dictFileFullPath,
                                      dtdFileFullPath,
                                      maxErrors);
    return list;
 }

  /**
   * This method will validate an XML file based on the DTD provided.
   *
   * @param xmlFileFullPath the full path of the XML file to be validated
   * @param dictFileFullPath the full path of the dictionary file
   * @return true if no validation errors otherwise throws XMLException
   */
  public boolean validateDTD(String xmlFileFullPath,
                             String dtdFileFullPath)
                             throws XMLException
  {
    XMLServiceHandler handler = XMLServiceHandler.getInstance();
    return handler.validateDTD(xmlFileFullPath, dtdFileFullPath);
 }

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
                          throws XMLException
  {
    XMLServiceHandler handler = XMLServiceHandler.getInstance();
    return handler.validateDTD(xmlFileFullPath,
                               dtdFileFullPath,
                               systemId);
 }

 /**
  * This method appends an XML file to another XML file.
  *
  * @param orgFile the file to be appended
  * @param appendFile the file to be appended to the orginal file.
  */
  public void appendXML(String orgFile, String appendFile)
    throws XMLException
  {
    XMLServiceHandler.getInstance().appendXML(orgFile, appendFile);
  }

  /**
   * This method will extract the values from the XML file based on the xpath
   * given.
   *
   * @param xmlFile the full path of the XML file that contain the values to
   *                be extracted
   * @param xpath the xpath to extract the values from
   * @return a List of String values extracted.
   */
  public List getXPathValues(String xmlFile, String xpath)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getXPathValues(xmlFile, xpath);
  }

  /**
   * This method will replace all the values in the XML file with the specified
   * xpath with the value given.
   *
   * @param xmlFile the XML file that contain the values to be replaced
   * @param xpath the xpath pointing to the values to be replaced
   * @param value the new value to replace the old values
   */
//  public void replaceXPathValues(String xmlFile,
//                                      String xpath,
//                                      String value)
//                                      throws XMLException
//  {
//    XMLServiceHandler.getInstance().replaceXPathValues(xmlFile, xpath, value);
//  }

  /**
   * This method will replace all the values in the XML file with the specified
   * xpath with the value given only if the orginal value is the same as the
   * value provided.
   *
   * @param xmlFile the XML file that contain the values to be replaced
   * @param xpath the xpath pointing to the values to be replaced
   * @param value the new value to replace the old values
   * @param orgValue the orginal value in the file that will be replaced by the
   *        new value
   */
//  public void replaceXPathValues(String xmlFile,
//                                 String xpath,
//                                 String value,
//                                 String orgValue)
//                                 throws XMLException
//  {
//    XMLServiceHandler.getInstance().replaceXPathValues(xmlFile,
//                                                       xpath,
//                                                       value,
//                                                       orgValue);
//  }

  /**
   * This method validates a XML file against the given schema.
   *
   * @param filename the fullpath of the xml file to be validated
   * @param schema the schema used to validate the xml file
   * @return true if validation is successful, throws exception otherwise
   */
  public boolean validateSchema(String filename, String schema)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().validateSchema(filename, schema, null);
  }

  /**
   * This method validates a XML file against the given schema.
   *
   * @param filename the fullpath of the xml file to be validated
   * @param schema the schema used to validate the xml file
   * @param dtdSystemID the url used to locate the schema
   * @return true if validation is successful, throws exception otherwise
   */
  public boolean validateSchema(String filename, String schema, String dtdSystemID)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().validateSchema(filename, schema, dtdSystemID);
  }

  /**
   * This method will retrieve the root element name from a given XML file.
   *
   * @param filename the full path of the XML file.
   * @return the name of the root element of the XML file.
   */
  public String getRootName(String filename)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getRootName(filename);
  }

  /**
   * This method will retrieve the root element name from a given XML file.
   *
   * @param fileObj the File object representing the XML file.
   * @return the name of the root element of the XML file.
   */
  public String getRootName(File fileObj)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getRootName(fileObj);
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param filename the full path of the XML file.
   * @return the root element of the XML file.
   */
  public GNElement getRoot(String filename)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getRoot(filename);
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileObj the File object representing the XML file.
   * @return the root element of the XML file.
   */
  public GNElement getRoot(File fileObj)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getRoot(fileObj);
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileFIS the InputStream of the XML file.
   * @return the root element of the XML file.
   */
  public GNElement getRoot(InputStream fileFIS)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getRoot(fileFIS);
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param filename the full path of the XML file.
   * @param systemId base for resolving relative URIs
   * @return the root element of the XML file.
   */
  public GNElement getRoot(String filename, String systemId)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getRoot(filename, systemId);
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileObj the File object representing the XML file.
   * @param systemId base for resolving relative URIs
   * @return the root element of the XML file.
   */
  public GNElement getRoot(File fileObj, String systemId)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getRoot(fileObj, systemId);
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileFIS the InputStream of the XML file.
   * @param systemId base for resolving relative URIs
   * @return the root element of the XML file.
   */
  public GNElement getRoot(InputStream fileFIS, String systemId)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getRoot(fileFIS, systemId);
  }

  /**
   * This method will retrieve the filename of the DTD used in the XML file.
   *
   * @param xmlFile the XML file to extract the DTD filename.
   * @return the name of the DTD file used.
   */
  public String extractDTDFilename(String xmlFile)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().extractDTDFilename(xmlFile);
  }

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(File xmlFile)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getDocument(xmlFile);
  }

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(String xmlFile)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getDocument(xmlFile);
  }

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(InputStream xmlFile)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getDocument(xmlFile);
  }

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(File xmlFile, String systemId)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getDocument(xmlFile, systemId);
  }

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(String xmlFile, String systemId)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getDocument(xmlFile, systemId);
  }

  /**
   * This method will return the GNDocument object of the XML file.
   *
   * @param xmlFile the XML file to get the GNDocument from.
   * @return the GNDocument object of the XML file.
   */
  public GNDocument getDocument(InputStream xmlFile, String systemId)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().getDocument(xmlFile, systemId);
  }

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
    throws XMLException
  {
    XMLServiceHandler.getInstance().writeToFile(doc, outputFile,
                                                indent, newline);
  }

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
    throws XMLException
  {
    XMLServiceHandler.getInstance().writeToFile(root, outputFile,
                                                indent, newline);
  }

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
    throws XMLException
  {
    XMLServiceHandler.getInstance().writeToFile(root, dtdFile, outputFile,
                                                indent, newline);
  }

  /**
   * This method will write the GNDocument object into a ByteArrayOutputStream
   *
   * @param doc the document to be written into a ByteArrayOutputStream.
   * @param indent whether to indent the output content.
   * @param newline whether to output newlines.
   */
//  public ByteArrayOutputStream writeToStream(GNDocument doc, boolean indent,
//                                             boolean newline)
//    throws XMLException
//  {
//    return XMLServiceHandler.getInstance().writeToStream(root, indent, newline);
//  }

  /**
   * This method will write the GNElement object into a ByteArrayOutputStream
   *
   * @param root the root element to be written into a ByteArrayOutputStream.
   * @param indent whether to indent the output content.
   * @param newline whether to output newlines.
   */
//  public ByteArrayOutputStream writeToStream(GNElement root, boolean indent,
//                                             boolean newline)
//    throws XMLException
//  {
//    return XMLServiceHandler.getInstance().writeToStream(root, indent, newline);
//  }

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
//    throws XMLException
//  {
//    return XMLServiceHandler.getInstance().writeToStream(root, dtdFile,
//                                                         indent, newline);
//  }

  /**
   * This method will create a new GNDocument
   *
   * @return the GNDocument created.
   */
  public GNDocument newDocument()
    throws XMLException
  {
    return XMLServiceHandler.getInstance().newDocument();
  }

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
    throws XMLException
  {
    return XMLServiceHandler.getInstance().newDocumentType(elementName,
                                                           publicID,
                                                           systemID);
  }

  /**
   * This method will create a new GNElement
   *
   * @param name the name of the element
   * @return the GNElement created.
   */
  public GNElement newElement(String name)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().newElement(name);
  }

  /**
   * This method will create a new GNElement
   *
   * @param name the name of the element
   * @param prefix the namespace prefix
   * @param uri the URI of the namespace
   * @return the GNElement created.
   */
  public GNElement newElement(String name, String prefix, String uri)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().newElement(name, prefix, uri);
  }

  /**
   * This method will create a namespace base on the prefix and URI provided
   *
   * @param prefix the prefix to map to the namespace.
   * @param uri URI of the namespace.
   * @return the namespace created.
   */
  public GNNamespace newNamespace(String prefix, String uri)
    throws XMLException
  {
    return XMLServiceHandler.getInstance().newNamespace(prefix, uri);
  }
  
  public void ejbCreate()
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbPassivate()
  {
  }

  public void ejbActivate()
  {
  }

  public void setSessionContext(SessionContext scx)
  {
    this.ctx = scx;
  }
}