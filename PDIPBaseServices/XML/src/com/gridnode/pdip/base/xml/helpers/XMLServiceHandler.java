/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLServiceHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 07 2002    Koh Han Sing        Created
 * Feb 27 2003    Neo Sok Lay         Check for returned message from stylesheet
 *                                    during transform error and throw exception
 *                                    on the returned message.
 * Jun 23 2003    Koh Han Sing        Use XML services from gn-xml, gn-convertor
 * Dec 23 2003    Koh Han Sing        Use gn-xml transformation service.
 * Dec 30 2003    Koh Han Sing        Set properties use by saxon for asian
 *                                    characters support.
 * Apr 26 2004    Neo Sok Lay         Add newNoNamespace() method.
 * Oct 27 2005    Neo Sok Lay         Load config file from SystemUtil.workingDir
 * Mar 27 2009    Tam Wei Xiang       #123: Allow specify the TransformerFactory Impl
 *                                          during runtime.
 */
package com.gridnode.pdip.base.xml.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.gridnode.convertor.ConvertorFacade;
import com.gridnode.convertor.GNConvertorException;
import com.gridnode.pdip.base.xml.exceptions.MappingException;
import com.gridnode.pdip.base.xml.exceptions.XMLException;
import com.gridnode.pdip.base.xml.extension.EnhancedXPathSAXHelper;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.util.SystemUtil;
import com.gridnode.xml.XMLDictionaryValidator;
import com.gridnode.xml.XMLSplitter;
import com.gridnode.xml.XMLTransformer;
import com.gridnode.xml.adapters.GNDocument;
import com.gridnode.xml.adapters.GNDocumentType;
import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNEntityResolver;
import com.gridnode.xml.adapters.GNNamespace;
import com.gridnode.xml.adapters.GNSchemaValidator;
import com.gridnode.xml.adapters.GNXMLDocumentUtility;
import com.gridnode.xml.helpers.FileToFileInputStream;
import com.gridnode.xml.helpers.GNURLResolver;
import com.gridnode.xml.helpers.XMLAppend;
import com.gridnode.xml.helpers.XPathExtractor;

public class XMLServiceHandler
{
  //private static final String PARSER = "org.apache.xerces.parsers.SAXParser";
  private static final String CONFIG_NAME = "xml";
  private static final String CONVERTOR_CONFIG_FILE = "xml.convertor.config.file";
  private Configuration _xmlConfig;

  private static XMLServiceHandler instance = null;
  private ConvertorFacade _convertorFacade;
  private XPathExtractor _xpathExtractor;
  private XMLSplitter _xmlSplitter;
  private Hashtable _xslTemplates = new Hashtable();

  private XMLServiceHandler() throws XMLException
  {	
	    _xmlConfig = ConfigurationManager.getInstance().getConfig(CONFIG_NAME);
	    String configFilename = _xmlConfig.getString(CONVERTOR_CONFIG_FILE);
	    File configFile = new File(SystemUtil.getWorkingDirPath(), configFilename); //NSL20051027
	    if (configFile.exists())
	    {
	      try
	      {
	        _convertorFacade = new ConvertorFacade(configFile);
	      }
	      catch (GNConvertorException ex)
	      {
	        throw new XMLException("Error creating ConvertorFacade", ex);
	      }
	    }
	    else
	    {
	      throw new XMLException("Config file : "+configFilename+" does not exist");
	    }

	    //GNXMLLog.setLogger(new Logger());
	    _xpathExtractor = new XPathExtractor();
	    _xmlSplitter = new XMLSplitter();

	    Enumeration keys = _xmlConfig.getPropertyKeys();
	    while (keys.hasMoreElements())
	    {
	      String key = keys.nextElement().toString();
	      if (key.startsWith(OutputKeys.ENCODING))
	      {
	        String value = _xmlConfig.getString(key);
	        System.setProperty(key, value);
	      }
	      
	      //#123 TWX 27 March 2009: allow to set transformer factory during runtime
	      //     The default XALAN provider seem like contain bugs, and it do not support xslt2.0
	      if(key.startsWith(TransformerFactory.class.getName()))
	      {
	        
	        String value = _xmlConfig.getString(key);
	        System.out.println("Use transformer factory IMPL: "+value);
	        System.setProperty(key, value);
	      }
	    }
  }

  public static XMLServiceHandler getInstance() throws XMLException
  {
    if(instance == null)
    {
      synchronized(XMLServiceHandler.class)
      {
        if(instance == null)
        {
          instance = new XMLServiceHandler();
        }
      }
    }
    return instance;
  }

  /**
   * This method converts a file base on the given conversion rule.
   *
   * @param input the full path of the input file to be converted
   * @param rule the full path of the conversion rule
   * @return the File object of the converted file
   */
  public File convert(String input, String rule)
    throws XMLException
  {
    try
    {
      return ConvertorHelper.convert(_convertorFacade, input, rule);
    }
    catch (Exception ex)
    {
      throw new XMLException("[XMLServiceHandler.convert] XML Conversion Failed", ex);
    }
  }
  
  public File convertJar(String inputFile, String className, String fileName, String path, String fileExt) throws XMLException
  {
    String tempOutputFile = "";
    Wrapper wrapper = new Wrapper();
    try
    {
      tempOutputFile = createTempOutputFile(fileExt); //TWX 20120111 #3193: allow specifying the file ext instead of hardcoding as .xml
      wrapper.createConversion(inputFile, inputFile, tempOutputFile, className, fileName, path);
    }
    catch (MappingException e)
    {
      Logger.warn("[XMLServiceHandler.]", "cannot Transform", e);
      throw new XMLException("[XMLServiceHandler.transform] cannot Transform", e);
    }   
    catch (Exception e)
    {
      Logger.warn("[XMLServiceHandler.]", "cannot Transform", e);
      throw new XMLException("[XMLServiceHandler.transform] cannot Transform", e);
    }   
    
    File f = new File(tempOutputFile);
    return f;
  }

  /**
   * This method extracts all the xpaths for the given root element from the
   * given DTD file.
   *
   * @param rootName    the root element where the Xpaths are to be extracted
   * @param dtdFilename the full path of the dtd file
   * @return the ArrayList of String containing all the xpaths
   */
  public ArrayList extractXPathFromDTD(String rootName, String dtdFilename)
    throws XMLException
  {
    try
    {
      return _xpathExtractor.extractXPathFromDTD(rootName, dtdFilename);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.extractXPathFromFile]",
                   "Cannot perform XpathExtractor",ex);
      throw new XMLException("[XMLServiceHandler.extractXPathFromFile] Cannot perform XpathExtractor",ex);
    }
  }

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
   * @return a ArrayList of File object of the splitted files.
   */
  public ArrayList splitXML(String styleSheet,
                            String inputFile,
                            String xPath,
                            String paraName)
                            throws XMLException
  {
    try
    {
      ArrayList list =
        _xmlSplitter.splitXML(styleSheet, inputFile, xPath, paraName);
      return UtilHelper.getFileList(list,
                                    UtilHelper.getOriginalFileExt(inputFile));
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.splitXML]", "Cannot Split", ex);
      throw new XMLException("[XMLServiceHandler.splitXML]  Cannot Split", ex);
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
    try
    {
      XMLDictionaryValidator validator = new XMLDictionaryValidator();
      ArrayList list = validator.validate(xmlFileFullPath,
                                          dictFileFullPath,
                                          dtdFileFullPath,
                                          maxErrors);
      return list;
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.validate]",
                 "Cannot Perform Validation : ",ex);
      throw new XMLException("[XMLServiceHandler.validate] Cannot Perform Validation : ",ex);
    }

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
    try
    {
      return GNXMLDocumentUtility.validateDTD(xmlFileFullPath,
                                            dtdFileFullPath);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.validateDTD]",
                 "Validation failed : ",ex);
      throw new XMLException("[XMLServiceHandler.validateDTD] Validation failed : ",ex);
    }

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
    try
    {
      return GNXMLDocumentUtility.validateDTD(xmlFileFullPath,
                                              dtdFileFullPath);

//      SAXParser parser = new SAXParser();
//
//      parser.setFeature("http://apache.org/xml/features/allow-java-encodings", true);
//      parser.setFeature("http://xml.org/sax/features/validation", true);
//      GNEntityResolver resolver = new GNEntityResolver(dtdFileFullPath);
//      parser.setEntityResolver(resolver);
//      InputSource is = new InputSource(xmlFileFullPath);
//      parser.parse(is);
//
//      return true;
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.validateDTD]",
                 "Validation failed : ",ex);
      throw new XMLException("[XMLServiceHandler.validateDTD] Validation failed : ",ex);
    }

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
    try
    {
      XMLAppend.appendXML(new File(orgFile), new File(appendFile));
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.appendXML]", "Cannot Append "
                 +appendFile+" to "+orgFile+" : ",ex);
      throw new XMLException("[XMLServiceHandler.appendXML] Cannot Append "+
                             appendFile+" to "+orgFile+" : ",ex);
    }
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
    try
    {
      return EnhancedXPathSAXHelper.getValuesAtXPathUnique(xmlFile, xpath);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getXPathValues]",
                 "Unable to retrieve XPath values from "+xmlFile,ex);
      throw new XMLException("[XMLServiceHandler.getXPathValues] " +
                            "Unable to retrieve XPath values from "+xmlFile,ex);
    }
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
//                                 String xpath,
//                                 String value)
//                                 throws XMLException
//  {
//    try
//    {
//      XPathHelper.replaceXPathValues(xmlFile, xpath, value);
//    }
//    catch (Exception ex)
//    {
//      log("[XMLServiceHandler.replaceXPathValues] " +
//          "Unable to retrieve XPath values from "+xmlFile,ex);
//      throw new XMLException("[XMLServiceHandler.replaceXPathValues] " +
//                           "Unable to retrieve XPath values from "+xmlFile,ex);
//    }
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
//    try
//    {
//      XPathHelper.replaceXPathValues(xmlFile, xpath, value, orgValue);
//    }
//    catch (Exception ex)
//    {
//      log("[XMLServiceHandler.replaceXPathValues] " +
//          "Unable to retrieve XPath values from "+xmlFile,ex);
//      throw new XMLException("[XMLServiceHandler.replaceXPathValues] " +
//                            "Unable to retrieve XPath values from "+xmlFile,ex);
//    }
//  }

  /**
   * This method transform a XML file with the given stylesheet.
   *
   * @param stylesheet the stylesheet used to transform the XML file
   * @param inputFile the full path of the XML file to be transformed
   * @return the File object of the transformed file
   */
  

  public File transform(String styleSheet, String inputFile)
    throws XMLException
  {
    return transform(styleSheet, inputFile, null);
  }
  
  public File transformJar(String inputFile, String className, String path, String fileName) throws XMLException
  {
	  String tempOutputFile = "";
	  Wrapper wrapper = new Wrapper();

	  try
	  {
		  tempOutputFile = createTempOutputFile("xml");

		  wrapper.createTransformation(inputFile, tempOutputFile, className, path, fileName);
	  }
	  catch (Exception e)
	  {
	    Logger.warn("[XMLServiceHandler.]", "cannot Transform", e);
      throw new XMLException("[XMLServiceHandler.transform] cannot Transform", e);
	  }	  
	  
	  File f = new File(tempOutputFile);
	  return f;
  }
  
  private String createTempOutputFile(String fileExt) throws IOException
  {
    fileExt = (fileExt == null || fileExt.length() == 0) ? "" : "."+fileExt; 
	  String tempFilename = UtilHelper.generateRandomFileName();
	  File tempFile = File.createTempFile(tempFilename, fileExt);
	  String outputFile = tempFile.getAbsolutePath();
	  return outputFile;
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
   * @return the File object of the transformed file
   */
  public File transform(String styleSheet,
                        String inputFile,
                        Hashtable parameters)
                        throws XMLException
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Templates pss = tryCache(styleSheet);
      XMLTransformer transformer = new XMLTransformer();
      baos = transformer.transform(pss, inputFile, parameters);
      File tempFile =
        UtilHelper.getTempFile(baos, UtilHelper.getOriginalFileExt(inputFile));
      return tempFile;
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.transform]", "cannot Transform", ex);
      throw new XMLException("[XMLServiceHandler.transform] cannot Transform", ex);
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
   * @return the File object of the transformed file
   */
/*  public File transform(String styleSheet,
                        String inputFile,
                        Hashtable parameters)
                        throws XMLException
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Templates pss = tryCache(styleSheet);
      Transformer transformer = pss.newTransformer();
      transformer.setURIResolver(new GNURLResolver());
      if(parameters != null)
      {
        insertParameters(transformer, parameters);
      }
      //FileInputStream fis = new FileInputStream(inputFile);
      //File sourceFile = XMLDocumentUtility.cleanup(fis);
      //transformer.transform(new StreamSource(sourceFile), new StreamResult(baos));
      String inputFull = "file:///" + inputFile;
      XMLReader reader = XMLReaderFactory.createXMLReader(
        "org.apache.xerces.parsers.SAXParser");
      reader.setEntityResolver(new GNEntityResolver());
      reader.setFeature(
        "http://apache.org/xml/features/continue-after-fatal-error", true);
      reader.setFeature(
        "http://apache.org/xml/features/allow-java-encodings", true);
      InputSource inputSource = new InputSource(inputFull);
      SAXSource xmlSource = new SAXSource(reader, inputSource);
      transformer.transform(xmlSource, new StreamResult(baos));
      File tempFile =
        UtilHelper.getTempFile(baos, UtilHelper.getOriginalFileExt(inputFile));
      return tempFile;
    }
    catch (Exception ex)
    {
//      if (!(ConvertorMessager.getMessage().equals("")))
//      {
//        XMLException newEx = new XMLException(ConvertorMessager.getMessage());
//        log("[XMLServiceHandler.transform] Stylesheet returns error", ex);
//        throw newEx;
//      }
//      else
//      {
//        log("[XMLServiceHandler.transform] cannot Transform", ex);
//        throw new XMLException("[XMLServiceHandler.transform] cannot Transform", ex);
//      }
      Logger.err("[XMLServiceHandler.transform]", "cannot Transform", ex);
      throw new XMLException("[XMLServiceHandler.transform] cannot Transform", ex);
    }
  }
*/
  /**
   * This method transform a XML file with the given stylesheet.
   *
   * @param stylesheet the stylesheet used to transform the XML file
   * @param inputFile the full path of the XML file to be transformed
   * @return the ByteArrayOutputStream of the transformed file
   */
  public ByteArrayOutputStream transformToBaos(String styleSheet, String inputFile)
    throws XMLException
  {
    return transformToBaos(styleSheet, inputFile, null);
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
   * @return the ByteArrayOutputStream of the transformed file
   */
  public ByteArrayOutputStream transformToBaos(String styleSheet,
                                               String inputFile,
                                               Hashtable parameters)
                                               throws XMLException
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Templates pss = tryCache(styleSheet);
      Transformer transformer = pss.newTransformer();
      transformer.setURIResolver(new GNURLResolver());
      if(parameters != null)
      {
        insertParameters(transformer, parameters);
      }
//      FileInputStream fis = new FileInputStream(inputFile);
//      File sourceFile = XMLDocumentUtility.cleanup(fis);
//      transformer.transform(new StreamSource(sourceFile), new StreamResult(baos));
      String inputFull = "file:///" + inputFile;
      XMLReader reader = XMLReaderFactory.createXMLReader(
        "org.apache.xerces.parsers.SAXParser");
      reader.setEntityResolver(new GNEntityResolver());
      reader.setFeature(
        "http://apache.org/xml/features/continue-after-fatal-error", true);
      reader.setFeature(
        "http://apache.org/xml/features/allow-java-encodings", true);
      InputSource inputSource = new InputSource(inputFull);
      SAXSource xmlSource = new SAXSource(reader, inputSource);
      transformer.transform(xmlSource, new StreamResult(baos));
      return baos;
    }
    catch (Exception ex)
    {
//      if (!(ConvertorMessager.getMessage().equals("")))
//      {
//        XMLException newEx = new XMLException(ConvertorMessager.getMessage());
//        log("[XMLServiceHandler.transformToBaos] Stylesheet returns error", ex);
//        throw newEx;
//      }
//      else
//      {
//        log("[XMLServiceHandler.transformToBaos] cannot Transform", ex);
//        throw new XMLException("[XMLServiceHandler.transformToBaos] cannot Transform", ex);
//      }
      Logger.warn("[XMLServiceHandler.transformToBaos]",
                 "cannot Transform", ex);
      throw new XMLException("[XMLServiceHandler.transformToBaos] cannot Transform", ex);
    }
  }


  public ByteArrayOutputStream transformToBaos(InputStream styleSheetIS, InputStream inputFileIS)
    throws XMLException
  {
    return transformToBaos(styleSheetIS, inputFileIS, null);
  }


  public ByteArrayOutputStream transformToBaos(InputStream styleSheetIS,
                                               InputStream inputFileIS,
                                               Hashtable parameters)
                                               throws XMLException
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      TransformerFactory factory = TransformerFactory.newInstance();
      Templates pss = factory.newTemplates(new StreamSource(styleSheetIS));
      Transformer transformer = pss.newTransformer();
      transformer.setURIResolver(new GNURLResolver());
      if(parameters != null)
      {
        insertParameters(transformer, parameters);
      }
      XMLReader reader = XMLReaderFactory.createXMLReader(
        "org.apache.xerces.parsers.SAXParser");
      reader.setEntityResolver(new GNEntityResolver());
      reader.setFeature(
        "http://apache.org/xml/features/continue-after-fatal-error", true);
      reader.setFeature(
        "http://apache.org/xml/features/allow-java-encodings", true);
      InputSource inputSource = new InputSource(inputFileIS);
      SAXSource xmlSource = new SAXSource(reader, inputSource);
      transformer.transform(xmlSource, new StreamResult(baos));
      return baos;
    }
    catch (Exception ex)
    {
//      if (!(ConvertorMessager.getMessage().equals("")))
//      {
//        XMLException newEx = new XMLException(ConvertorMessager.getMessage());
//        log("[XMLServiceHandler.transformToBaos] Stylesheet returns error", ex);
//        throw newEx;
//      }
//      else
//      {
//        log("[XMLServiceHandler.transformToBaos] cannot Transform", ex);
//        throw new XMLException("[XMLServiceHandler.transformToBaos] cannot Transform", ex);
//      }
      Logger.warn("[XMLServiceHandler.transformToBaos]",
                 "cannot Transform", ex);
      throw new XMLException("[XMLServiceHandler.transformToBaos] cannot Transform", ex);
    }
  }

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
    return validateSchema(filename, schema, null);
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
    try
    {
      GNSchemaValidator validator = new GNSchemaValidator();
      return validator.validate(filename, schema, dtdSystemID);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.validate]",
                 "Schema validation error", ex);
      throw new XMLException("[XMLServiceHandler.validate] Schema validation error", ex);
    }
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param filename the full path of the XML file.
   * @return the JDOM root element of the XML file.
   */
  public GNElement getRoot(String filename)
    throws XMLException
  {
    try
    {
      return getRoot(FileToFileInputStream.getFileInputStream(filename));
    }
    catch (FileNotFoundException ex)
    {
      Logger.warn("[XMLServiceHandler.getRoot]",
                 "Unable to find file "+filename, ex);
      throw new XMLException("[XMLServiceHandler.getRoot] Unable to find file "+filename, ex);
    }
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileObj the File object representing the XML file.
   * @return the JDOM root element of the XML file.
   */
  public GNElement getRoot(File fileObj)
    throws XMLException
  {
    try
    {
      return getRoot(FileToFileInputStream.getFileInputStream(fileObj));
    }
    catch (FileNotFoundException ex)
    {
      Logger.warn("[XMLServiceHandler.getRoot]",
                 "Unable to find file "+fileObj.getAbsolutePath(), ex);
      throw new XMLException("[XMLServiceHandler.getRoot] Unable to find file "+
                              fileObj.getAbsolutePath(), ex);
    }
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileFIS the InputStream of the XML file.
   * @return the JDOM root element of the XML file.
   */
  public GNElement getRoot(InputStream fileFIS)
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.getRoot(fileFIS);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getRoot]",
                 "Unable to get root element", ex);
      throw new XMLException("[XMLServiceHandler.getRoot] Unable to get root element", ex);
    }
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param filename the full path of the XML file.
   * @param systemId base for resolving relative URIs
   * @return the JDOM root element of the XML file.
   */
  public GNElement getRoot(String filename, String systemId)
    throws XMLException
  {
    try
    {
      return getRoot(FileToFileInputStream.getFileInputStream(filename), systemId);
    }
    catch (FileNotFoundException ex)
    {
      Logger.warn("[XMLServiceHandler.getRoot]",
                 "Unable to find file "+filename, ex);
      throw new XMLException("[XMLServiceHandler.getRoot] Unable to find file "+filename, ex);
    }
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileObj the File object representing the XML file.
   * @param systemId base for resolving relative URIs
   * @return the JDOM root element of the XML file.
   */
  public GNElement getRoot(File fileObj, String systemId)
    throws XMLException
  {
    try
    {
      return getRoot(FileToFileInputStream.getFileInputStream(fileObj), systemId);
    }
    catch (FileNotFoundException ex)
    {
      Logger.warn("[XMLServiceHandler.getRoot]",
                 "Unable to find file "+fileObj.getAbsolutePath(), ex);
      throw new XMLException("[XMLServiceHandler.getRoot] Unable to find file "+
                              fileObj.getAbsolutePath(), ex);
    }
  }

  /**
   * This method will retrieve the root element from a given XML file.
   *
   * @param fileFIS the InputStream of the XML file.
   * @param systemId base for resolving relative URIs
   * @return the JDOM root element of the XML file.
   */
  public GNElement getRoot(InputStream fileFIS, String systemId)
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.getRoot(fileFIS, systemId);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getRoot]",
                 "Unable to get root element", ex);
      throw new XMLException("[XMLServiceHandler.getRoot] Unable to get root element", ex);
    }
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
    try
    {
      GNElement rootElement = getRoot(filename);
      return rootElement.getName();
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getRootName]",
                 "Unable to get root element name", ex);
      throw new XMLException("[XMLServiceHandler.getRoot] Unable to get root element name", ex);
    }
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
    try
    {
      GNElement rootElement = getRoot(fileObj);
      return rootElement.getName();
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getRootName]",
                 "Unable to get root element name", ex);
      throw new XMLException("[XMLServiceHandler.getRoot] Unable to get root element name", ex);
    }
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
    try
    {
      return GNXMLDocumentUtility.extractDTDFilename(xmlFile);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.extractDTDFilename]",
                 "Unable to extract DTD filename", ex);
      throw new XMLException("[XMLServiceHandler.extractDTDFilename] Unable to extract DTD filename", ex);
    }
  }

  /**
   * This method will return the Document object of the XML file.
   *
   * @param xmlFile the XML file to get the Document from.
   * @return the Document object of the XML file.
   */
  public GNDocument getDocument(File xmlFile)
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.getDocument(xmlFile);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getDocument]",
                 "Unable to get document", ex);
      throw new XMLException("[XMLServiceHandler.getDocument] Unable to get document", ex);
    }
  }

  /**
   * This method will return the Document object of the XML file.
   *
   * @param xmlFile the XML file to get the Document from.
   * @return the Document object of the XML file.
   */
  public GNDocument getDocument(String xmlFile)
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.getDocument(xmlFile);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getDocument]",
                 "Unable to get document", ex);
      throw new XMLException("[XMLServiceHandler.getDocument] Unable to get document", ex);
    }
  }

  /**
   * This method will return the Document object of the XML file.
   *
   * @param xmlFile the XML file to get the Document from.
   * @return the Document object of the XML file.
   */
  public GNDocument getDocument(InputStream xmlFile)
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.getDocument(xmlFile);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getDocument]",
                 "Unable to get document", ex);
      throw new XMLException("[XMLServiceHandler.getDocument] Unable to get document", ex);
    }
  }

  /**
   * This method will return the Document object of the XML file.
   *
   * @param xmlFile the XML file to get the Document from.
   * @return the Document object of the XML file.
   */
  public GNDocument getDocument(File xmlFile, String systemId)
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.getDocument(xmlFile, systemId);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getDocument]",
                 "Unable to get document", ex);
      throw new XMLException("[XMLServiceHandler.getDocument] Unable to get document", ex);
    }
  }

  /**
   * This method will return the Document object of the XML file.
   *
   * @param xmlFile the XML file to get the Document from.
   * @return the Document object of the XML file.
   */
  public GNDocument getDocument(String xmlFile, String systemId)
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.getDocument(xmlFile, systemId);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getDocument]",
                 "Unable to get document", ex);
      throw new XMLException("[XMLServiceHandler.getDocument] Unable to get document", ex);
    }
  }

  /**
   * This method will return the Document object of the XML file.
   *
   * @param xmlFile the XML file to get the Document from.
   * @return the Document object of the XML file.
   */
  public GNDocument getDocument(InputStream xmlFile, String systemId)
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.getDocument(xmlFile, systemId);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.getDocument]",
                 "Unable to get document", ex);
      throw new XMLException("[XMLServiceHandler.getDocument] Unable to get document", ex);
    }
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
    try
    {
      GNXMLDocumentUtility.writeToFile(doc, outputFile,
                                       indent, newline);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.writeToFile]",
                 "Unable to write to XML", ex);
      throw new XMLException("[XMLServiceHandler.writeToFile] Unable to write to XML", ex);
    }
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
    try
    {
      GNXMLDocumentUtility.writeToFile(root, outputFile,
                                       indent, newline);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.writeToFile]",
                 "Unable to write to XML", ex);
      throw new XMLException("[XMLServiceHandler.writeToFile] Unable to write to XML", ex);
    }
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
    try
    {
      GNXMLDocumentUtility.writeToFile(root, dtdFile, outputFile,
                                       indent, newline);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.writeToFile]",
                 "Unable to write to XML", ex);
      throw new XMLException("[XMLServiceHandler.writeToFile] Unable to write to XML", ex);
    }
  }

  /**
   * This method will create a new GNDocument
   *
   * @return the GNDocument created.
   */
  public GNDocument newDocument()
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.newDocument();
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.newDocument]",
                 "Unable to create GNDocument", ex);
      throw new XMLException("[XMLServiceHandler.newDocument] Unable to create GNDocument", ex);
    }
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
    try
    {
      return GNXMLDocumentUtility.newDocumentType(elementName, publicID,
                                                  systemID);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.newDocumentType]",
                 "Unable to create GNDocumentType", ex);
      throw new XMLException("[XMLServiceHandler.newDocumentType] Unable to create GNDocumentType", ex);
    }
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
    try
    {
      return GNXMLDocumentUtility.newElement(name);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.newElement]",
                 "Unable to create GNElement", ex);
      throw new XMLException("[XMLServiceHandler.newElement] Unable to create GNElement", ex);
    }
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
    try
    {
      return GNXMLDocumentUtility.newElement(name, prefix, uri);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.newElement]",
                 "Unable to create GNElement", ex);
      throw new XMLException("[XMLServiceHandler.newElement] Unable to create GNElement", ex);
    }
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
    try
    {
      return GNXMLDocumentUtility.newNamespace(prefix, uri);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.newNamespace]",
                 "Unable to create namespace", ex);
      throw new XMLException("[XMLServiceHandler.newNamespace] Unable to create namespace", ex);
    }
  }

  /**
   * This method will create a GNNamespace for no namespace specification.  
   *
   * @return the namespace created.
   */
  public GNNamespace newNoNamespace()
    throws XMLException
  {
    try
    {
      return GNXMLDocumentUtility.newNoNamespace();
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLServiceHandler.newNoNamespace]",
                 "Unable to create namespace", ex);
      throw new XMLException("Unable to create namespace", ex);
    }
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
//    try
//    {
//      return GNXMLDocumentUtility.writeToStream(doc, indent, newline);
//    }
//    catch (Exception ex)
//    {
//      Logger.err("[XMLServiceHandler.writeToStream]",
//                 "Unable to write to ByteArrayOutputStream", ex);
//      throw new XMLException("[XMLServiceHandler.writeToStream] Unable to write to ByteArrayOutputStream", ex);
//    }
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
//    try
//    {
//      return GNXMLDocumentUtility.writeToStream(root, indent, newline);
//    }
//    catch (Exception ex)
//    {
//      Logger.err("[XMLServiceHandler.writeToStream]",
//                 "Unable to write to ByteArrayOutputStream", ex);
//      throw new XMLException("[XMLServiceHandler.writeToStream] Unable to write to ByteArrayOutputStream", ex);
//    }
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
//    try
//    {
//      return GNXMLDocumentUtility.writeToStream(root, dtdFile, indent, newline);
//    }
//    catch (Exception ex)
//    {
//      Logger.err("[XMLServiceHandler.writeToStream]",
//                 "Unable to write to ByteArrayOutputStream", ex);
//      throw new XMLException("[XMLServiceHandler.writeToStream] Unable to write to ByteArrayOutputStream", ex);
//    }
//  }

  private void insertParameters(Transformer transformer, Hashtable parameters)
  {
    Logger.debug("[XMLServiceHandler.insertParameters]", "Starts");
    if(parameters != null)
    {
      Enumeration parameterNames = parameters.keys();
      while(parameterNames.hasMoreElements())
      {
        String parameterName = parameterNames.nextElement().toString();
        Object parameterValue = parameters.get(parameterName);
        Logger.debug("[XMLServiceHandler.insertParameters]", "Name = "
                      +parameterName+" Value = "+parameterValue.toString());
        transformer.setParameter(parameterName, parameterValue);
      }
    }
    else
    {
      Logger.debug("[XMLServiceHandler.insertParameters]", "No Parameters");
    }
  }

  /**
  * Maintain prepared stylesheets in memory for reuse
  */
  private synchronized Templates tryCache(String path)
    throws TransformerException, IOException
  {
    Logger.debug("[XMLServiceHandler.tryCache]", "Caching " + path);
    if (path == null)
    {
      throw new TransformerException("Stylesheet " + path + " not found");
    }
    Templates template = (Templates)_xslTemplates.get(path);
    if (template == null)
    {
      TransformerFactory factory = TransformerFactory.newInstance();
      template = factory.newTemplates(new StreamSource(new File(path)));
      _xslTemplates.put(path, template);
    }
    return template;
  }
  
//  private File getTempFile(ByteArrayOutputStream baos, String ext)
//   throws FileNotFoundException, IOException
//  {
//    File tempFile = File.createTempFile(generateRandomFileName(), "."+ext);
//    FileOutputStream fos = new FileOutputStream(tempFile);
//    fos.write(baos.toByteArray());
//    fos.flush();
//    fos.close();
//    return tempFile;
//  }

//  private ArrayList getFileList(ArrayList baosList, String ext)
//    throws FileNotFoundException, IOException
//  {
//    ArrayList fileList = new ArrayList();
//    for (Iterator i = baosList.iterator(); i.hasNext();)
//    {
//      ByteArrayOutputStream baos = (ByteArrayOutputStream)i.next();
//      File tempFile = getTempFile(baos, ext);
//      fileList.add(tempFile);
//    }
//    return fileList;
//  }

//  private String generateRandomFileName()
//  {
//    Random random = new Random();
//    return String.valueOf(random.nextInt());
//  }

//  private String getOriginalFileExt(String fullFilename)
//  {
//    logD("[XMLServiceHandler.getOriginalFileExt] fullFilename = "+fullFilename);
//    String ext = "";
//    if ((fullFilename.indexOf("/") > 0) && (fullFilename.lastIndexOf("/")+1 != fullFilename.length()))
//    {
//      fullFilename = fullFilename.substring(fullFilename.lastIndexOf("/")+1);
//    }
//    if ((fullFilename.indexOf("\\") > 0) && (fullFilename.lastIndexOf("\\")+1 != fullFilename.length()))
//    {
//      fullFilename = fullFilename.substring(fullFilename.lastIndexOf("\\")+1);
//    }
//
//    // check to see if last char is not "/" or "\"
//    if ((fullFilename.lastIndexOf("/")+1 != fullFilename.length()) &&
//        (fullFilename.lastIndexOf("\\")+1 != fullFilename.length()))
//    {
//      if ((fullFilename.indexOf(".") > 0) && (fullFilename.lastIndexOf(".")+1 != fullFilename.length()))
//      {
//        logD("[XMLServiceHandler.getOriginalFileExt] found . in filename");
//        ext = fullFilename.substring(fullFilename.lastIndexOf(".")+1);
//      }
//    }
//    logD("[XMLServiceHandler.getOriginalFileExt] ext = "+ext);
//
//    return ext;
//  }

  /**
  * Clear the cache. Useful if stylesheets have been modified, or simply if space is
  * running low. We let the garbage collector do the work.
  */
  public synchronized void clearCache()
  {
    Logger.debug("[XMLServiceHandler.tryCache]", "Clearing cache");
    _xslTemplates = new Hashtable();
  }
}