/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002    Mahesh              Created
 * Jun 13 2002    Mathew              Repackaged
 * Oct 17 2005    Neo Sok Lay         Change methods from private to protected
 *                                    to improve performance: logDebug(), logError()
 */
package com.gridnode.pdip.framework.util;


import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.*;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.xml.parsers.*;

import org.apache.xml.serialize.*;

import org.w3c.dom.*;

import org.xml.sax.*;

import org.xml.sax.helpers.*;

public class UtilXml
{

    public static String writeXmlDocument(Document document)
        throws java.io.IOException
    {
        if (document == null)
        {
            logDebug("[UtilXml.writeXmlDocument] Document was null, doing nothing");
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        writeXmlDocument(bos, document);
        String outString = bos.toString();

        if (bos != null)
            bos.close();
        return outString;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     * @param document DOCUMENT ME!
     * @throws java.io.FileNotFoundException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public static void writeXmlDocument(String filename, Document document)
        throws java.io.FileNotFoundException,
            java.io.IOException
    {
        if (document == null)
        {
            logDebug("[UtilXml.writeXmlDocument] Document was null, doing nothing");
            return;
        }
        if (filename == null)
        {
            logDebug("[UtilXml.writeXmlDocument] Filename was null, doing nothing");
            return;
        }
        File outFile = new File(filename);
        FileOutputStream fos = null;

        fos = new FileOutputStream(outFile);
        try
        {
            writeXmlDocument(fos, document);
        }
        finally
        {
            if (fos != null)
                fos.close();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param os DOCUMENT ME!
     * @param document DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public static void writeXmlDocument(OutputStream os, Document document)
        throws java.io.IOException
    {
        if (document == null)
        {
            logDebug("[UtilXml.writeXmlDocument] Document was null, doing nothing");
            return;
        }
        if (os == null)
        {
            logDebug(
                "[UtilXml.writeXmlDocument] OutputStream was null, doing nothing");
            return;
        }
        //if(document instanceof XmlDocument) {
        //Crimson writer
        //XmlDocument xdoc = (XmlDocument) document;
        //xdoc.write(os);
        //}
        //else {
        //Xerces writer
        OutputFormat format = new OutputFormat(document);

        format.setIndent(2);
        XMLSerializer serializer = new XMLSerializer(os, format);

        serializer.asDOMSerializer();
        serializer.serialize(document.getDocumentElement());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws SAXException DOCUMENT ME!
     * @throws ParserConfigurationException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public static Document readXmlDocument(String content)
        throws SAXException,
            ParserConfigurationException,
            java.io.IOException
    {
        return readXmlDocument(content, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param validate DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws SAXException DOCUMENT ME!
     * @throws ParserConfigurationException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public static Document readXmlDocument(String content, boolean validate)
        throws SAXException,
            ParserConfigurationException,
            java.io.IOException
    {
        if (content == null)
        {
            logDebug("[UtilXml.readXmlDocument] URL was null, doing nothing");
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(content.getBytes());

        return readXmlDocument(bis, validate, "Internal Content");
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws SAXException DOCUMENT ME!
     * @throws ParserConfigurationException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public static Document readXmlDocument(URL url)
        throws SAXException,
            ParserConfigurationException,
            java.io.IOException
    {
        return readXmlDocument(url, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     * @param validate DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws SAXException DOCUMENT ME!
     * @throws ParserConfigurationException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public static Document readXmlDocument(URL url, boolean validate)
        throws SAXException,
            ParserConfigurationException,
            java.io.IOException
    {
        if (url == null)
        {
            logDebug("[UtilXml.readXmlDocument] URL was null, doing nothing");
            return null;
        }
        return readXmlDocument(url.openStream(), validate, url.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param is DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws SAXException DOCUMENT ME!
     * @throws ParserConfigurationException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public static Document readXmlDocument(InputStream is)
        throws SAXException,
            ParserConfigurationException,
            java.io.IOException
    {
        return readXmlDocument(is, true, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param is DOCUMENT ME!
     * @param validate DOCUMENT ME!
     * @param docDescription DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws SAXException DOCUMENT ME!
     * @throws ParserConfigurationException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public static Document readXmlDocument(InputStream is, boolean validate,
        String docDescription)
        throws SAXException,
            ParserConfigurationException,
            java.io.IOException
    {
        if (is == null)
        {
            logDebug("[UtilXml.readXmlDocument] InputStream was null, doing nothing");
            return null;
        }
        Document document = null;
        DocumentBuilderFactory factory = new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();


        logDebug("TestClass "+factory);

        factory.setValidating(validate);
        //factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        if(validate){
            builder.setEntityResolver(
            new EntityResolver(){
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    logDebug("[UtilXml.resolveEntity] resolving DTD with publicId [" + publicId +
                                     "], systemId [" + new File(systemId).toURL().toString() + "] ");
                    try{
                        if(systemId!=null && !systemId.trim().startsWith("file:")){
                            InputStream inStream=new File(systemId).toURL().openStream();
                            if(inStream!=null)
                                return new InputSource(inStream);
                        }
                    }catch(Exception e){
                    }
                    return new DefaultHandler().resolveEntity(publicId, systemId);
                }

            });
        }
        document = builder.parse(is);
        return document;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Document makeEmptyXmlDocument()
    {
        return makeEmptyXmlDocument(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rootElementName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static Document makeEmptyXmlDocument(String rootElementName)
    {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setValidating(true);
        //factory.setNamespaceAware(true);
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();

            document = builder.newDocument();
        }
        catch (Exception e)
        {
            logError(ILogErrorCodes.XML_GENERIC,
                "[UtilXml.makeEmptyXmlDocument] error in creating new Document object",
                e);
        }
        if (rootElementName != null)
        {
            Element rootElement = document.createElement(rootElementName);

            document.appendChild(rootElement);
        }
        if (document == null)
            return null;
        return document;
    }

    /**
     * Creates a child element with the given name and appends it to the element
     * child node list.
     */
    public static Element addChildElement(Element element,
        String childElementName,
        Document document)
    {
        Element newElement = document.createElement(childElementName);

        element.appendChild(newElement);
        return newElement;
    }

    /**
     * Creates a child element with the given name and appends it to the element
     * child node list. Also creates a Text node with the given value and
     * appends it to the new elements child node list.
     */
    public static Element addChildElementValue(Element element,
        String childElementName,
        String childElementValue,
        Document document)
    {
        Element newElement = addChildElement(element, childElementName, document);

        newElement.appendChild(document.createTextNode(childElementValue));
        return newElement;
    }

    /**
     * Creates a child element with the given name and appends it to the element
     * child node list. Also creates a CDATASection node with the given value
     * and appends it to the new elements child node list.
     */
    public static Element addChildElementCDATAValue(Element element,
        String childElementName,
        String childElementValue,
        Document document)
    {
        Element newElement = addChildElement(element, childElementName, document);

        newElement.appendChild(document.createCDATASection(childElementValue));
        return newElement;
    }

    /**
     * Return a List of Element objects that have the given name and are
     * immediate children of the given element; if name is null, all child
     * elements will be included.
     */
    public static List childElementList(Element element, String childElementName)
    {
        if (element == null)
            return null;
        List elements = new LinkedList();
        Node node = element.getFirstChild();

        if (node != null)
        {
            do
            {
                if (node.getNodeType() == Node.ELEMENT_NODE &&
                    (childElementName == null ||
                        childElementName.equals(node.getNodeName())))
                {
                    Element childElement = (Element) node;

                    elements.add(childElement);
                }
            }
            while ((node = node.getNextSibling()) != null);
        }
        return elements;
    }

    /**
     * Return the first child Element with the given name; if name is null
     * returns the first element.
     */
    public static Element firstChildElement(Element element,
        String childElementName)
    {
        if (element == null)
            return null;
        //get the first element with the given name
        Node node = element.getFirstChild();

        if (node != null)
        {
            do
            {
                if (node.getNodeType() == Node.ELEMENT_NODE &&
                    (childElementName == null ||
                        childElementName.equals(node.getNodeName())))
                {
                    Element childElement = (Element) node;

                    return childElement;
                }
            }
            while ((node = node.getNextSibling()) != null);
        }
        return null;
    }

    /**
     * Return the first child Element with the given name; if name is null
     * returns the first element.
     */
    public static Element firstChildElement(Element element,
        String childElementName,
        String attrName, String attrValue)
    {
        if (element == null)
            return null;
        //get the first element with the given name
        Node node = element.getFirstChild();

        if (node != null)
        {
            do
            {
                if (node.getNodeType() == Node.ELEMENT_NODE &&
                    (childElementName == null ||
                        childElementName.equals(node.getNodeName())))
                {
                    Element childElement = (Element) node;
                    String value = childElement.getAttribute(attrName);

                    if (value != null && value.equals(attrValue))
                    {
                        return childElement;
                    }
                }
            }
            while ((node = node.getNextSibling()) != null);
        }
        return null;
    }

    /**
     * Return the text (node value) contained by the named child node
     */
    public static String childElementValue(Element element,
        String childElementName)
    {
        if (element == null)
            return null;
        //get the value of the first element with the given name
        Element childElement = firstChildElement(element, childElementName);

        return elementValue(childElement);
    }

    /**
     * Return the text (node value) of the first node under this, works best if
     * normalized
     */
    public static String elementValue(Element element)
    {
        if (element == null)
            return null;
        //make sure we get all the text there...
        element.normalize();
        Node textNode = element.getFirstChild();

        if (textNode == null)
            return null;
        //should be of type text
        return textNode.getNodeValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String checkEmpty(String string)
    {
        if (string != null && string.length() > 0)
            return string;
        else
            return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param string1 DOCUMENT ME!
     * @param string2 DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String checkEmpty(String string1, String string2)
    {
        if (string1 != null && string1.length() > 0)
            return string1;
        else if (string2 != null && string2.length() > 0)
            return string2;
        else
            return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param string1 DOCUMENT ME!
     * @param string2 DOCUMENT ME!
     * @param string3 DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String checkEmpty(String string1, String string2,
        String string3)
    {
        if (string1 != null && string1.length() > 0)
            return string1;
        else if (string2 != null && string2.length() > 0)
            return string2;
        else if (string3 != null && string3.length() > 0)
            return string3;
        else
            return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static boolean checkBoolean(String str)
    {
        return checkBoolean(str, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     * @param def DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static boolean checkBoolean(String str, boolean def)
    {
        if (str != null && str.equalsIgnoreCase("true"))
        {
            return true;
        }
        return def;
    }

    protected static void logDebug(String msg)
    {
        Log.debug(Log.FRAMEWORK, msg);
    }

    protected static void logError(String errorCode, String msg, Throwable th)
    {
        Log.error(errorCode, Log.FRAMEWORK, msg, th);
    }
}
