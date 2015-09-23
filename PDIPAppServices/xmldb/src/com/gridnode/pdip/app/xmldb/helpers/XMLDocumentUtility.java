/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDocumentUtility.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 16 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.helpers;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jaxen.jdom.XPath;

import java.io.*;
import java.util.List;

import com.gridnode.pdip.app.xmldb.config.IXMLDBConfig;

/**
 * Handles all XML method calls
 */
public class XMLDocumentUtility
{

    private static final String PARSER = "org.apache.xerces.parsers.SAXParser";
    private static final String DOCTYPE_ELEMENT = "<!DOCTYPE";

    public static Document getDocument(String xmlFile) throws Exception
    {
        SAXBuilder builder = new SAXBuilder(PARSER, false);
        File file = cleanup(xmlFile);
        return builder.build(new FileInputStream(file));
    }

    public static Document getDocument(String xmlFile, String dtdFileName)
        throws Exception
    {
        SAXBuilder builder = new SAXBuilder(PARSER, true);
        return builder.build(new FileInputStream(xmlFile),
            XMLDBConfigUtil.getDTDsFolderPath() + '/' + dtdFileName);
    }

    public static File cleanup(String fileName) throws Exception
    {
        FileInputStream fis = new FileInputStream(fileName);
        File fileObj = File.createTempFile("xmldb", ".tmp");
        FileOutputStream fos = new FileOutputStream(fileObj);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedReader breader = new BufferedReader(new InputStreamReader(fis));
        BufferedWriter bw = new BufferedWriter(osw);

        String aLine = breader.readLine();
        while(aLine != null)
        {
            if(aLine.indexOf(DOCTYPE_ELEMENT) == -1)
            {
                bw.write(aLine);
            }
            else if(aLine.indexOf(DOCTYPE_ELEMENT) != -1)
            {
                String bLine = aLine.substring(aLine.indexOf(DOCTYPE_ELEMENT));
                aLine = aLine.substring(0, aLine.indexOf(DOCTYPE_ELEMENT));
                while (bLine.indexOf(">") == -1)
                {
                    bLine = bLine + breader.readLine();
                }
                bLine = bLine.substring(bLine.indexOf(">")+1);
                aLine = aLine + bLine;
                bw.write(aLine);
            }
            aLine = breader.readLine();
        }
        fis.close();
        bw.flush();
        osw.flush();
        fos.flush();
        bw.close();
        osw.close();
        fos.close();
        return fileObj;
    }

    public static Element getNode(String xpath, Document doc) throws Exception
    {
        XPath xpathObj = new XPath(xpath);
        return (Element) xpathObj.selectSingleNode(doc);
    }

    public static List getNodes(String xpath, String filename) throws Exception
    {
        Document doc = XMLDocumentUtility.getDocument(filename);
        return getNodes(xpath, doc);
    }

    public static List getNodes(String xpath, Document doc) throws Exception
    {
System.out.println("Inside getNodes(String xpath, Document doc) ");
        XPath xpathObj = new XPath(xpath);
        return xpathObj.selectNodes(doc);
    }

    /**
     * Returns the complete, unique xpath of the specified element
     */
    public static String getXPath(Element element)
    {
        String str = "";
        if (element.isRootElement())
        {
            return "/" + element.getName();
        }
        while (!element.isRootElement())
        {
            Element parent = element.getParent();
            List children = parent.getChildren(element.getName());
            int index = children.indexOf(element) + 1;
            String temp = element.getName() + '[' + index + ']';
            if (str.equals(""))
            {
                str = temp;
            }
            else
            {
                str = temp + '/' + str;
            }
            element = parent;
        }
        return "/" + element.getName() + '/' + str;
    }

    /**
     * Returns the value of the specified xpath
     */
    public static String stringValueOf(String xpath, Document doc)
        throws Exception
    {
        System.out.println("xpath " + xpath);
        int index = xpath.lastIndexOf("/@");
        if (index != -1)
        {
            String dd = xpath.substring(0, index);
            XPath xpathObj = new XPath(xpath.substring(0, index));
            Element element = (Element) xpathObj.selectSingleNode(doc);
            if (element == null)
            {
                return null;
            }
            String attrName = xpath.substring(index + 2);
            Attribute attr = element.getAttribute(attrName);
            if (attr == null)
            {
                return null;
            }
            else
            {
                return attr.getValue();
            }
        }
        else
        {
            XPath xpathObj = new XPath(xpath);
            Element element = (Element) xpathObj.selectSingleNode(doc);
            if (element == null)
            {
                return null;
            }
            else
            {
                return element.getText();
            }
        }
    }
}
