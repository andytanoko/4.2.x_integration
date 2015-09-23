/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SchemaManager.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Sep 22 2003      Guo Jianyu              Created
 * Oct 31 2005      Neo Sok Lay             Change to use ServiceLocator instead of ServiceLookup
 */
package com.gridnode.gtas.server.rnif.helpers;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import org.jdom.Namespace;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.rnif.helper.IRnifPathConfig;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.xml.adapters.GNDocument;
import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNNamespace;
import com.gridnode.xml.adapters.GNXMLDocumentUtility;

public class SchemaManager
{
  public static final String CONFIG_NAME = "rnif-schema";
  public final static String RNIF1_1_FILE = "RNIF1.1.file";
  public final static String RNIF2_0_FILE = "RNIF2.0.file";
  public final static String RNIF1_1_VALUES = "RNIF1.1.values";
  public final static String RNIF2_0_VALUES = "RNIF2.0.values";
  private static SchemaManager self = null;
  private IXMLServiceLocalObj xmlService = null;

  protected static Configuration config =
    ConfigurationManager.getInstance().getConfig(CONFIG_NAME);

  private SchemaManager()
  {
    try
    {
    	xmlService = (IXMLServiceLocalObj)ServiceLocator.instance().getObj(IXMLServiceLocalHome.class.getName(),
    	                                                                   IXMLServiceLocalHome.class,
    	                                                                   new Object[0]);
    	/*
      IXMLServiceLocalHome homeObj = (IXMLServiceLocalHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(IXMLServiceLocalHome.class);
      xmlService = homeObj.create(); */
    }
    catch(Exception e)
    {
      Logger.error(ILogErrorCodes.GT_RNIF_SCHEMA_MGR,"[SchemaManager.init]Error getting xml service: "+e.getMessage(), e);
    }
  }

  public synchronized static SchemaManager getInstance()
  {
    if (self == null)
    {
      self = new SchemaManager();
    }
    return self;
  }

  public GNDocument getSchemaRoot(String schemaFileName) throws Exception
  {
    String schemaPath = FileUtil.getFile(IRnifPathConfig.PATH_SCHEMA, schemaFileName).getAbsolutePath();
    return xmlService.getDocument(new File(schemaPath));
  }

/*	public synchronized void addToSchema(ProcessDef processDef)
  {
    String rnifVersion = processDef.getRNIFVersion();
    if ((rnifVersion.equalsIgnoreCase("RNIF1.1")) || (rnifVersion.equalsIgnoreCase("CIDX")))
    {
      schemaFileName = config.getString(RNIF1_1_FILE);
      prefix = RNIF1_1_VALUES;
    }
    else
    {
      schemaFileName = config.getString(RNIF2_0_FILE);
      prefix = RNIF2_0_VALUES;
    }

    GNDocument root = null;
    try
    {
      schemaPath = FileUtil.getFile(IRnifPathConfig.PATH_SCHEMA, schemaFileName).getAbsolutePath();
      root = xmlService.getDocument(new File(schemaPath));
Logger.debug("root is " + root.getRootElement().getName());
    }
    catch(Exception e)
    {
      Logger.err("Error in getting schema root, exiting...", e);
      return;
    }

    String itemList = config.getString(prefix); // the list of values to generate

    StringTokenizer tokenizer = new StringTokenizer(itemList, ",");

Logger.debug("prefix is " + prefix);
    while (tokenizer.hasMoreElements() )
    {
      String item = tokenizer.nextToken().trim();
Logger.debug("Token is " + item);
      String methodList = config.getString(prefix + "." + item + ".method");
Logger.debug("methodList is " + methodList);
      if ((methodList == null) || (methodList.equals("")))
      {
        Logger.err("Error: Can't find methodList for " + item + " for schema " + schemaFileName);
        continue;
      }
      String xpath = config.getString(prefix + "." + item + ".xpath");
Logger.debug("xpath is " + xpath);
      if ((xpath == null) || (xpath.equals("")))
      {
        Logger.err("Error: Can't find xpath for " + item + " for schema " + schemaFileName);
        continue;
      }
      GNElement itemElement;
      String namespacePrefix;
      String namespaceURI;
      try
      {
        namespacePrefix = root.getRootElement().getNamespace().getPrefix();
        Logger.debug("namespacePrefix = " + namespacePrefix);
      }
      catch(Exception e)
      {
        Logger.err("Error getting the namespace prefix for schema file " + schemaFileName
          + ", using default prefix: xsd");
        namespacePrefix = "xsd";
      }
      try
      {
        Namespace namespace = (Namespace)root.getRootElement().getNamespace().unwrap();
        namespaceURI = namespace.getURI();
        Logger.debug("namespaceURI = " + namespaceURI);
      }
      catch(Exception e)
      {
        Logger.err("Error getting the namespace URI for schema file " + schemaFileName
          + ", using default URI: http://www.w3.org/2001/XMLSchema");
        namespaceURI = "http://www.w3.org/2001/XMLSchema";
      }
      try
      {
        itemElement = (GNElement)root.getNode(xpath, namespacePrefix, namespaceURI);
//        itemElement = (GNElement)root.getNode(xpath);
      }
      catch(Exception e)
      {
        Logger.err("Error finding the element using xpath:" + xpath, e);
        continue;
      }

      if (itemElement == null)
      {
        Logger.err("Error: can't find element based on xpath:" + xpath);
        continue;
      }

      String extraXPath = xpath + "/xsd:enumeration[@value=\"" + value + "\"]";
    }

  }
*/
  public synchronized void generateSchema()
  {
    generateSchema("RNIF1.1");
    generateSchema("RNIF2.0");
  }

  public synchronized void generateSchema(String rnifVersion)
  {
    String schemaFileName;
    String schemaPath;
    String prefix;

    if ((rnifVersion.equalsIgnoreCase("RNIF1.1")) || (rnifVersion.equalsIgnoreCase("CIDX")))
    {
      schemaFileName = config.getString(RNIF1_1_FILE);
      prefix = RNIF1_1_VALUES;
    }
    else
    {
      schemaFileName = config.getString(RNIF2_0_FILE);
      prefix = RNIF2_0_VALUES;
    }

    GNDocument root = null;
    try
    {
      schemaPath = FileUtil.getFile(IRnifPathConfig.PATH_SCHEMA, schemaFileName).getAbsolutePath();
      root = xmlService.getDocument(new File(schemaPath));
    }
    catch(Exception e)
    {
      Logger.warn("Error in getting schema root, exiting...", e);
      return;
    }

    IRNProcessDefManagerObj processDefMgr;
    try
    {
    	/*
      IRNProcessDefManagerHome processDefMgrHome = (IRNProcessDefManagerHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(IRNProcessDefManagerHome.class);
      processDefMgr = processDefMgrHome.create();
      */
      processDefMgr = (IRNProcessDefManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
                          IRNProcessDefManagerHome.class.getName(),
                          IRNProcessDefManagerHome.class,
                          new Object[0]);
    }
    catch(Exception e)
    {
      Logger.warn("Error getting processDef manager, exiting...", e);
      return;
    }

    Collection processDefList = null;
    Vector processActList = null;

    String itemList = config.getString(prefix); // the list of values to generate

    StringTokenizer tokenizer = new StringTokenizer(itemList, ",");

    while (tokenizer.hasMoreTokens())
    {
      String item = tokenizer.nextToken().trim();
      String methodList = config.getString(prefix + "." + item + ".method");
      if ((methodList == null) || (methodList.equals("")))
      {
        Logger.warn("Error: Can't find methodList for " + item + " for schema " + schemaFileName);
        continue;
      }
      String xpath = config.getString(prefix + "." + item + ".xpath");
      if ((xpath == null) || (xpath.equals("")))
      {
        Logger.warn("Error: Can't find xpath for " + item + " for schema " + schemaFileName);
        continue;
      }

      GNElement itemElement;
      String namespacePrefix;
      String namespaceURI;
      try
      {
        namespacePrefix = root.getRootElement().getNamespace().getPrefix();
        Logger.debug("namespacePrefix = " + namespacePrefix);
      }
      catch(Exception e)
      {
        Logger.warn("Error getting the namespace prefix for schema file " + schemaFileName
          + ", using default prefix: xsd");
        namespacePrefix = "xsd";
      }
      try
      {
        Namespace namespace = (Namespace)root.getRootElement().getNamespace().unwrap();
        namespaceURI = namespace.getURI();
        Logger.debug("namespaceURI = " + namespaceURI);
      }
      catch(Exception e)
      {
        Logger.warn("Error getting the namespace URI for schema file " + schemaFileName
          + ", using default URI: http://www.w3.org/2001/XMLSchema");
        namespaceURI = "http://www.w3.org/2001/XMLSchema";
      }
      try
      {
        itemElement = (GNElement)root.getNode(xpath, namespacePrefix, namespaceURI);
      }
      catch(Exception e)
      {
        Logger.warn("Error finding the element using xpath:" + xpath, e);
        continue;
      }

      if (itemElement == null)
      {
        Logger.warn("Error: can't find element based on xpath:" + xpath);
        continue;
      }

      GNNamespace ns = GNXMLDocumentUtility.newNamespace(namespacePrefix,
        namespaceURI);
      if (itemElement.getChild("enumeration", ns) != null)
      {
        Logger.debug("Deleting old enumeration values...");
        itemElement.removeChildren("enumeration", ns);
      }

      Hashtable uniqueValueList = new Hashtable();

      StringTokenizer methodTokenizer = new StringTokenizer(methodList, ",");
      while (methodTokenizer.hasMoreTokens())
      {
        String method = methodTokenizer.nextToken().trim();

        if (processDefList == null)
        {
          try
          {
            processDefList = new Vector();

            //retrieve all process definitions
            Collection fullDefList = processDefMgr.findProcessDefs(null);

            //Only keep those that match with the input RNIF version
            Iterator itr = fullDefList.iterator();
            while (itr.hasNext())
            {
              ProcessDef processDef = (ProcessDef)itr.next();
              if (isSameVersion(processDef.getRNIFVersion(), rnifVersion))
                processDefList.add(processDef);
            }
          }
          catch(Exception e)
          {
            Logger.warn("Error retrieving the list of process definitions, exiting...", e);
            return;
          }
        }

        if ((method.startsWith("getRequestAct")) ||
          (method.startsWith("getResponseAct"))) //obtain the value from process actions
        {
          if (processActList == null)
          { //retrieve all process actions
            processActList = new Vector();
            Iterator itr = processDefList.iterator();
            while (itr.hasNext())
            {
              ProcessDef processDef = (ProcessDef)itr.next();
              if (processDef.getRequestAct() != null)
                processActList.add(processDef.getRequestAct());
              if (processDef.getResponseAct() != null)
                processActList.add(processDef.getResponseAct());
            }
          }
          int index = method.indexOf('.');
          if (index < 0) continue;
          String methodName = method.substring(index + 1);
          Logger.debug("Method name is " + methodName);

          for (int i=0; i<processActList.size(); i++)
          {
            ProcessAct processAct = (ProcessAct)processActList.get(i);
            Class c = processAct.getClass();
            try
            {
              Method m = c.getMethod(methodName, new Class[]{});
              String aValue = (String)m.invoke(processAct, new Object[]{});
              uniqueValueList.put(aValue.trim(), "");
            }
            catch(Exception e)
            {
              Logger.warn("Error calling " + methodName, e);
            }
          }
        }
        else //obtain the value from process definitions
        {
          Iterator defItr = processDefList.iterator();
          while (defItr.hasNext())
          {
            ProcessDef processDef = (ProcessDef)defItr.next();
            Class c = processDef.getClass();
            try
            {
              Method m = c.getMethod(method, new Class[]{});
              String aValue = (String)m.invoke(processDef, new Object[]{});
              uniqueValueList.put(aValue.trim(), "");
            }
            catch(Exception e)
            {
              Logger.warn("Error calling " + method, e);
            }
          } //while (defItr)
        } //else
      } //while (methodTokenizer)

      Enumeration enumeration = uniqueValueList.keys();
      while (enumeration.hasMoreElements())
      {
        String aValue = (String)enumeration.nextElement();
        GNElement child = GNXMLDocumentUtility.newElement("enumeration", "xsd",
          "http://www.w3.org/2001/XMLSchema");
        child.addAttribute("value", aValue);
        itemElement.addElement(child);
      }
    }//while (tokenizer)

    //Update the xml file
    try
    {
      xmlService.writeToFile(root, schemaPath, true, false);
    }
    catch(Exception e)
    {
      Logger.warn("Error writing changes to xml file:" + schemaPath, e);
    }
  }

  private boolean isSameVersion(String version1, String version2)
  {
    if ((version1 == null) || (version1.equals("")) ||
      (version2 == null) || (version2.equals("")))
      return false;
    String ver1 = version1.toUpperCase();
    String ver2 = version2.toUpperCase();

    if (ver1.equals("CIDX"))
      ver1 = "RNIF1.1";
    if (ver2.equals("CIDX"))
      ver2 = "RNIF1.1";

    return ver1.equals(ver2);

  }
}