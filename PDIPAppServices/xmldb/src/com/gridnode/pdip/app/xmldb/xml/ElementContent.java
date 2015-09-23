/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ElementContent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 26 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;

import java.util.*;

import com.gridnode.pdip.app.xmldb.xml.mapping.*;
import com.gridnode.pdip.app.xmldb.helpers.*;
import com.gridnode.pdip.app.xmldb.exceptions.*;

/**
 * This class models the an XML DOM element. It holds the
 * entity mappings and handles the algorithm for finding
 * the children. The root element is created by the XMLGeneratorUtil.
 * The findChildren method goes thru the mapping file and
 * finds the children.
 */
public class ElementContent extends Element
{

    private DocumentElement docElement;
    //private ArrayList children = new ArrayList();
    private HashMap entities;
    private HashMap inputs;

    private ArrayList buffer;

    public ElementContent(DocumentElement docElement, HashMap entities,
        HashMap inputs) throws Throwable
    {
        super(docElement.getName());
        this.docElement = docElement;
        if (entities == null || entities.isEmpty())
        {
            this.entities = null;
        }
        else
        {
            this.entities = entities;
        }
        this.inputs = inputs;

        //Set the attributes specified by docElement
        DBXMLMappings mappings = docElement.getMappings();
        if (mappings == null)
        {
            return;
        }
        ArrayList attributes = mappings.getAttributes();
        if (attributes != null)
        {
            //Take each attribute, find the value, set the
            //attribute to ElementContent
            Iterator it = attributes.iterator();
            while (it.hasNext())
            {
                DBXMLAttribute attr = (DBXMLAttribute) it.next();
                Object value = attr.getAttributeType().getValue(this);
                setAttribute(new Attribute(attr.getAttributeName(),
                    value.toString()));
            }
        }
        TextValue tv = mappings.getText();
        if (tv != null)
        {
            Object obj = tv.getValueType().getValue(this);
            setText(obj.toString());
        }
    }

    public void findChildren() throws Throwable
    {
        //Take each child of docElement
        //Find the entities pointed to
        //Create the elementContent
	try
	{
        ArrayList list = docElement.getChildren();
        if (list != null)
        {
            Iterator it = list.iterator();
            while (it.hasNext())
            {
                DocumentElement de = (DocumentElement) it.next();
                DBXMLMappings mappings = de.getMappings();

                boolean flag = false;
                if (mappings != null)
                {
                    Collection c = mappings.getEntityMaps(this);

                    Iterator mit = c.iterator();
                    if (c.size() > 0)
                    {
                        flag = true;
                    }
                    while (mit.hasNext())
                    {
                        HashMap eMap = (HashMap) mit.next();
                        ElementContent ec = new ElementContent(de, eMap, inputs);
                        addChild(ec);
                    }
                }

                if (!flag)
                {
                    if (!de.isMapped())
                    {
                        ElementContent ec = new ElementContent(de, null, inputs);
                        addChild(ec);
                    }
                }
            }
        }
        Iterator it = getChildren().iterator();
        while (it.hasNext())
        {
            ElementContent ec = (ElementContent) it.next();
            ec.findChildren();
        }
        if (buffer != null)
        {
            Iterator bit = buffer.iterator();
            while (bit.hasNext())
            {
                Iterator temp = ((ArrayList) bit.next()).iterator();
                while (temp.hasNext())
                {
                    ElementContent ec = (ElementContent) temp.next();
                    addChild(ec);
                }
            }
            buffer = null;
        }
        validateContentModel();
   }
   catch(Throwable th)
   {
	   System.out.println("Exception while finding the children " + th);
	   throw th;
   }

  }

    public void addChild(ElementContent element)
    {
        //children.add(element);
        this.addContent(element);
    }

    public void removeChildren(List list)
    {
        Iterator it = list.iterator();
        while (it.hasNext())
        {
            Element e = (Element) it.next();
            removeContent(e);
            //children.remove(e);
        }
    }

    public Long getEntityId(String entityName)
    {
        if (entities == null)
        {
            return null;
        }
        return (Long) entities.get(entityName);
    }

    public HashMap getInputs()
    {
        return this.inputs;
    }

    public DocumentElement getDocumentElement()
    {
        return this.docElement;
    }

    /*
     * This patch is required in the following scenario:
     *
     *      The parent element is not mapped to any entity and the
     * ContentModel of the parent is as follows:
     * <!ELEMENT A (B*, C)>
     * <!ELEMENT B (D)>
     * Consider D is mapped to entity-bean but B is not.
     * D maps to multiple entities, but only one D is possible in B.
     * This is solved by creating multiple instances of B in A and transfering
     * D's to A, which associates D's to the newly created B's
     */
    public void transferElements(ArrayList list) throws Throwable
    {
        Iterator it = list.iterator();
        while (it.hasNext())
        {
            removeContent((Element) it.next());
        }
        if (getParent() == null)
        {
            throw new InvalidMappingFileException("Mappings and ContentModel " +
                "does not match");
        }
        ((ElementContent) getParent()).createElements(this.getName(), list);
    }

    public void createElements(String name, ArrayList list) throws Throwable
    {
        DocumentElement de = docElement.getChild(name);
        if (de == null)
        {
            throw new XMLDBException("Unknown error");
        }
        if (buffer != null)
        {
            ArrayList eList = null;
            Iterator it = buffer.iterator();
            while (it.hasNext())
            {
                ArrayList temp = (ArrayList) it.next();
                ElementContent ec = (ElementContent) temp.iterator().next();
                if (ec.getName().equals(name))
                {
                    eList = temp;
                    break;
                }
            }
            if (eList == null)
            {
                addBufferedElements(de, name, list);
            }
            else
            {
                if (list.size() != eList.size())
                {
                    throw new InvalidMappingFileException("Mappings and " +
                        "ContentModel does not match");
                }
                Iterator bit = eList.iterator();
                while (it.hasNext())
                {
                    ElementContent ec = (ElementContent) bit.next();
                    ElementContent e = (ElementContent) it.next();
                    ec.addChild(e);
                }
            }
        }
        else
        {
            buffer = new ArrayList();
            addBufferedElements(de, name, list);
        }
    }

    private void addBufferedElements(DocumentElement de, String name,
        ArrayList list) throws Throwable
    {
        ArrayList nList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext())
        {
            ElementContent ec = new ElementContent(de, null, inputs);
            ElementContent e = (ElementContent) it.next();
            ec.addChild(e);
            nList.add(ec);
        }
        buffer.add(nList);
    }

    /**
     * The method validates the ContentModel. Only simple ContentModels are
     * handled. ContentModel lists the types of children possible and the
     * Cardinality of the children. Depending on the Cardinality, the elements are
     * arranged according to the order in which the ChildElements are declared.
     * Repeating ContentModels are not handled. For eg the following is not handled
     * <!ELEMENT A (B, C, D)*>
     */
    public void validateContentModel() throws Throwable
    {
        Iterator it = getChildren().iterator();
        while (it.hasNext())
        {
            ElementContent ec = (ElementContent) it.next();
            if (ec.isDefault())
            {
                it.remove();
                //removeContent(ec);
            }
        }
        DBXMLMappings mappings = docElement.getMappings();
        if (mappings != null)
        {
            ContentModel cm = mappings.getContentModel();
            if (cm != null)
            {
                ArrayList list = cm.getBody(this);
                if (getChildren().size() > 0)
                {
                    throw new InvalidMappingFileException("Mappings and " +
                        "ContentModel does not match");
                }
                else
                {
                    setChildren(list);
                    //children.addAll(list);
                }
            }
        }
    }

    /**
     * The method returns true if the element is not mapped to
     * entities and no child elements are present
     */
    public boolean isDefault()
    {
        if (entities == null || entities.isEmpty())
        {
            if (getChildren().isEmpty())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

}


