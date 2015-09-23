/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataLoader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 23 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db;

import org.jdom.Document;
import org.jdom.Element;

import java.util.*;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

import com.gridnode.pdip.app.xmldb.helpers.*;
import com.gridnode.pdip.app.xmldb.db.mapping.*;
import com.gridnode.pdip.app.xmldb.exceptions.*;

import com.gridnode.pdip.framework.db.meta.*;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * Loads the data from the XML file to database. Takes the XML DOM
 * Document object and the mapping file.
 */
public class DataLoader
{
    private Document document;
    private XMLDBMappingFile mapping;
    private ArrayList temp;
    private HashMap entityMap = new HashMap();
    private HashMap xpathEntityMap = new HashMap();

    public DataLoader(Document document, XMLDBMappingFile mapping)
    {
        this.document = document;
        this.mapping = mapping;
    }

    //Load the xml data from the xml file to the entity class. Resolve the
    //relationships between entities later.
    //So two passes are required through the Mapping file. One to load the
    //data to the entities and next to resolve the relationships
    public Collection populate() throws Throwable
    {
        populateDataFromXML();
        return establishRelations();
    }

    //Store the values available in the XML to the Entity class. Loading it
    //to the ejb is abstracted in Entity class. If there are no Child_Entities
    //or Primary_Keys, the entity can be created in this pass.
    private void populateDataFromXML() throws Throwable
    {
        Collection entities = mapping.getEntities();
        Iterator iterator = entities.iterator();
        while (iterator.hasNext())
        {
            EntityElement entityElement = (EntityElement) iterator.next();
            String xpath = entityElement.getXPath();
            Logger.debug("EntityElement " + xpath);
            List elements = XMLDocumentUtility.getNodes(xpath, document);

            ArrayList list = new ArrayList();
            EntityMetaInfo metaInfo =
                EntityUtil.getEntityMetaInfo(entityElement.getName());

            FieldMetaInfo fieldMetaInfo[] = metaInfo.getFieldMetaInfo();

            Iterator it = elements.iterator();
            while (it.hasNext())
            {
                Element element = (Element) it.next();
                String eXPath = XMLDocumentUtility.getXPath(element);
                Entity entity = new Entity(entityElement, eXPath,
                    entityElement.isUnique());
                for (int i = 0; i < fieldMetaInfo.length; i++)
                {
                    String objName = fieldMetaInfo[i].getObjectName();

                    //All fields except the primary key must be declared
                    FieldElement field = entityElement.getField(objName);
                    if (field == null)
                    {
                        if (objName.equals("_uId"))
                        {
                            continue;
                        }
                        throw new MissingFieldException("Mapping file does not " +
                            "contain all the required fields. Entity: " +
                            entityElement.getName() + " Field: " + objName);
                    }

                    //If the field is a value literal of an xpath expression,
                    //populate the value object
                    FieldType type = field.getFieldType();
                    if (type.getType() == FieldType.VALUE_LITERAL ||
                        type.getType() == FieldType.XPATH_EXPR)
                    {
                        String str = getFieldValue(type, xpath, eXPath);
                        Object obj = null;
                        try
                        {
                            //Parse the String value according to the value class
                            obj = parseFieldValue(str, fieldMetaInfo[i].getValueClass(),
                                  field.getFormat(), field.getPosition());
                        }
                        catch (UnmatchingFieldTypeException uft)
                        {
                            System.out.println(" The Field Type is not matching : " + uft.getMessage());
							//throw uft;
                        }
						catch (Exception e)
                        {
                            System.out.println(" The Field Type is not matching : " + e.getMessage());
							//throw new UnmatchingFieldTypeException(e.toString());
                        }
                        entity.setFieldValue(fieldMetaInfo[i].getFieldId(),
                            obj);
                    }
                }
                entity.create();
                list.add(entity);
                xpathEntityMap.put(new EntityKey(entity.getXPath(),
                    entity.getName()), entity);
            }
            entityMap.put(entityElement, list);
        }
    }

    /**
     * Establish the relationships between entities. The entity declarations
     * in the mapping file provides the relationships. The value objects created
     * till now does not solve the relationships if any.
     */
    private Collection establishRelations() throws Throwable
    {
        temp = new ArrayList();
        Iterator iterator = mapping.getEntities().iterator();
		Collection entityCollection = new ArrayList();
        while (iterator.hasNext())
        {
            EntityElement entityElement = (EntityElement) iterator.next();
//System.out.println(" Before calling the solveEntityRelations");
            List entityList = solveEntityRelations(entityElement);
            if(entityList != null)
              entityCollection.add(entityList);
        }

        return entityCollection;
    }

    private List solveEntityRelations(EntityElement entityElement)
        throws Throwable
    {
		List tempList = checkIfEntitiesCreated(entityElement);
        if (tempList != null)
        {
            return tempList;
        }
        //Check all fields of this entity element. If any field is
        //a foreign key, load the EntityElement corresponding to that
        //foreign key, check if the entities of that type are already
        //created. If not call the recursive function
        Iterator it = entityElement.getFields().iterator();
        while (it.hasNext())
        {
            FieldElement fieldElement = (FieldElement) it.next();
            if (fieldElement.getFieldType().getType() ==
                FieldType.FOREIGN_KEY)
            {
                ForeignKey fk = (ForeignKey) fieldElement.getFieldType();
                EntityElement fkEntityElement = findFKEntityElement(fk,
                    entityElement);
                if (!entityElement.equals(fkEntityElement))
                {
                    solveEntityRelations(fkEntityElement);
                }

                //Once the relations are solved, entities are created.
                //Now set the field value
                setFieldValue(entityElement, fkEntityElement, fieldElement);
            }
            else if (fieldElement.getFieldType().getType() ==
                FieldType.CHILD_ENTITY)
            {
                ChildEntity ce = (ChildEntity) fieldElement.getFieldType();
                EntityElement ceEntityElement = findCEEntityElement(ce,
                    entityElement);
                if (!entityElement.equals(ceEntityElement) &&
                    ceEntityElement != null)
                {
                    solveEntityRelations(ceEntityElement);
                }
                Iterator iterator =
                    ((ArrayList) entityMap.get(entityElement)).iterator();
                EntityMetaInfo metaInfo =
                    EntityUtil.getEntityMetaInfo(entityElement.getName());
                FieldMetaInfo fieldMetaInfo =
                    metaInfo.findFieldMetaInfo(fieldElement.getName());
                Number destId = fieldMetaInfo.getFieldId();
                while (iterator.hasNext())
                {
                    Entity entity = (Entity) iterator.next();
                    entity.setFieldValue(destId, getCEEntity(entity, ce));
                }
            }
        }

		List entityList = createEntities(entityElement);

        if (entityList == null)
        {
            throw new UnsolvableRelationsException("Unable to solve " +
                "relations Entity: " + entityElement.getName());
        }
		else
			return entityList;
    }

    private List createEntities(EntityElement entityElement)
        throws Throwable
    {
		List entityList = new ArrayList();
        Iterator it = ((ArrayList) entityMap.get(entityElement)).iterator();
        while (it.hasNext())
        {
            Entity entity = (Entity) it.next();
            if (entity.create()==null)
            {
                return null;
            }
			else
				entityList.add(entity);
        }
        return entityList;
    }

    private void setFieldValue(EntityElement entityElement,
        EntityElement fkEntityElement, FieldElement fieldElement)
        throws Throwable
    {
        Iterator iterator =
            ((ArrayList) entityMap.get(entityElement)).iterator();
        EntityMetaInfo metaInfo =
            EntityUtil.getEntityMetaInfo(entityElement.getName());
        FieldMetaInfo fieldMetaInfo =
            metaInfo.findFieldMetaInfo(fieldElement.getName());
        Number destId = fieldMetaInfo.getFieldId();
        metaInfo = EntityUtil.getEntityMetaInfo(fkEntityElement.getName());
        ForeignKey fk = (ForeignKey) fieldElement.getFieldType();
        fieldMetaInfo = metaInfo.findFieldMetaInfo(fk.getFieldName());
        Number srcId = fieldMetaInfo.getFieldId();
        while (iterator.hasNext())
        {
            Entity entity = (Entity) iterator.next();
            Entity parent = getFKEntity(entity, fk);

            //Check if the parent is not having that value
            if (!parent.isValueAvailable(srcId))
            {
                checkForValue(parent);
            }
            Object obj = parent.getFieldValue(srcId);
            entity.setFieldValue(destId, obj);
        }
    }

    private void setFKFieldValue(Entity entity, Entity fkEntity,
        FieldElement fieldElement) throws Throwable
    {
        EntityMetaInfo metaInfo =
            EntityUtil.getEntityMetaInfo(entity.getName());
        FieldMetaInfo fieldMetaInfo =
            metaInfo.findFieldMetaInfo(fieldElement.getName());
        Number destId = fieldMetaInfo.getFieldId();
        metaInfo = EntityUtil.getEntityMetaInfo(fkEntity.getName());
        ForeignKey fk = (ForeignKey) fieldElement.getFieldType();
        fieldMetaInfo = metaInfo.findFieldMetaInfo(fk.getFieldName());
        Number srcId = fieldMetaInfo.getFieldId();
        Object obj = fkEntity.getFieldValue(srcId);
        entity.setFieldValue(destId, obj);
    }

    private void setCEFieldValue(Entity entity, Object obj,
        FieldElement fieldElement) throws Throwable
    {
        EntityMetaInfo metaInfo =
            EntityUtil.getEntityMetaInfo(entity.getName());
        FieldMetaInfo fieldMetaInfo =
            metaInfo.findFieldMetaInfo(fieldElement.getName());
        Number destId = fieldMetaInfo.getFieldId();
        if (obj == null)
        {
            entity.setFieldValue(destId, obj);
        }
        else if (obj instanceof Entity)
        {
            Entity ceEntity = (Entity) obj;
            entity.setFieldValue(destId, ceEntity.getIEntity());
        }
        else if (obj instanceof Collection)
        {
            ArrayList list = new ArrayList();
            Iterator it = ((Collection) obj).iterator();
            while (it.hasNext())
            {
                Entity ceEntity = (Entity) it.next();
                list.add(ceEntity.getIEntity());
            }
            entity.setFieldValue(destId, list);
        }
    }

    private void checkForValue(Entity entity) throws Throwable
    {
        if (entity.create() != null)
        {
            return;
        }
        Iterator it = entity.getEntityElement().getFields().iterator();
        while (it.hasNext())
        {
            FieldElement field = (FieldElement) it.next();
            EntityMetaInfo metaInfo =
                EntityUtil.getEntityMetaInfo(entity.getName());
            FieldMetaInfo fieldMetaInfo =
                metaInfo.findFieldMetaInfo(field.getName());
            if (!entity.isValueAvailable(fieldMetaInfo.getFieldId()))
            {
                if (field.getFieldType().getType() == FieldType.FOREIGN_KEY)
                {
                    ForeignKey fk = (ForeignKey) field.getFieldType();
                    Entity fkEntity = getFKEntity(entity, fk);
                    if (fkEntity.getEntityElement().equals(entity.getEntityElement()))
                    {
                        checkForValue(fkEntity);
                        setFKFieldValue(entity, fkEntity, field);
                        if (entity.create()==null)
                        {
                            checkForValue(entity);
                        }
                    }
                    else
                    {
                        setFKFieldValue(entity, fkEntity, field);
                        if (entity.create()==null)
                        {
                            checkForValue(entity);
                        }
                    }
                }
                else if (field.getFieldType().getType() == FieldType.CHILD_ENTITY)
                {
                    ChildEntity ce = (ChildEntity) field.getFieldType();
                    Object obj = getCEEntity(entity, ce);
                    if (obj == null)
                    {
                        setCEFieldValue(entity, obj, field);
                        if (entity.create()==null)
                        {
                            checkForValue(entity);
                        }
                    }
                    if (obj instanceof Entity)
                    {
                        Entity ceEntity = (Entity) obj;
                        if (ceEntity.getEntityElement().
                            equals(entity.getEntityElement()))
                        {
                            checkForValue(ceEntity);
                            setCEFieldValue(entity, ceEntity, field);
                            if (entity.create()==null)
                            {
                                checkForValue(entity);
                            }
                        }
                        else
                        {
                            setCEFieldValue(entity, ceEntity, field);
                            if (entity.create()==null)
                            {
                                checkForValue(entity);
                            }
                        }
                    }
                    if (obj instanceof Collection)
                    {
                        Iterator iterator = ((Collection) obj).iterator();
                        while (iterator.hasNext())
                        {
                            Entity ceEntity = (Entity) iterator.next();
                            checkForValue(ceEntity);
                        }
                        setCEFieldValue(entity, obj, field);
                        if (entity.create()==null)
                        {
                            checkForValue(entity);
                        }
                    }
                }
            }
        }
    }

    private List checkIfEntitiesCreated(EntityElement entityElement)
    {
		List entityList = new ArrayList();
        ArrayList list = (ArrayList) entityMap.get(entityElement);
        Iterator it = list.iterator();
        while (it.hasNext())
        {
            Entity entity = (Entity) it.next();
            if (!entity.isCreated())
            {
                return null;
            }
			entityList.add(entity);
        }
        return entityList;
    }

    private EntityElement findFKEntityElement(ForeignKey fk,
        EntityElement parent) throws Exception
    {
        Iterator it = ((ArrayList) entityMap.get(parent)).iterator();
        Entity entity = (Entity) it.next();
        return getFKEntity(entity, fk).getEntityElement();
    }

    private Entity getFKEntity(Entity entity, ForeignKey fk) throws Exception
    {
        String absXPath = entity.getXPath() + '/' + fk.getRelativeXPath();
        List elements = XMLDocumentUtility.getNodes(absXPath, document);
        if (elements.size() == 0)
        {
            throw new UnresolvedForeignKeyException("Foreign key specified " +
                "not found. Entity: " + entity.getName() +
                " FK Field: " + fk.getFieldName());
        }
        if (elements.size() > 1)
        {
            throw new DuplicateForeignKeyException("Duplicate elements " +
                "matching the foreign key found. Entity: " +
                entity.getName() + " FK Field: " + fk.getFieldName());
        }
        Object obj = elements.iterator().next();
        if (obj instanceof Element)
        {
			Element element = (Element) elements.iterator().next();
			String actualXPath = XMLDocumentUtility.getXPath(element);
			Entity fe = (Entity) xpathEntityMap.get(new EntityKey(actualXPath,
				fk.getEntityName()));
			return fe;
		}
		else
		{
			throw new InvalidMappingFileException("Invalid relative xpath " +
				"specified for ForeignKey");
		}
    }

    private EntityElement findCEEntityElement(ChildEntity ce,
        EntityElement parent) throws Exception
    {
        Iterator it = ((ArrayList) entityMap.get(parent)).iterator();
        Entity entity = (Entity) it.next();
        String absXPath = entity.getXPath() + '/' + ce.getRelativeXPath();
        List elements = XMLDocumentUtility.getNodes(absXPath, document);
        if (elements.size() == 0)
        {
            return null;
        }
        else
        {
            Element element = (Element) elements.iterator().next();
            String actualXPath = XMLDocumentUtility.getXPath(element);
            Entity ceEntity = (Entity) xpathEntityMap.get(
                new EntityKey(actualXPath, ce.getEntityName()));
            return ceEntity.getEntityElement();
        }
    }

    private Object getCEEntity(Entity entity, ChildEntity ce) throws Exception
    {
        String absXPath = entity.getXPath() + '/' + ce.getRelativeXPath();
        List elements = XMLDocumentUtility.getNodes(absXPath, document);
        if (elements.size() == 0)
        {
            if (ce.isOptional())
            {
                if (ce.isMultiplicity())
                {
                    return new ArrayList();
                }
                else
                {
                    return null;
                }
            }
            else
            {
                throw new UnresolvedForeignKeyException("Child entity " +
                    "specified not found. Entity: " + entity.getName());
            }
        }
        else
        {
            if (ce.isMultiplicity())
            {
                Iterator it = elements.iterator();
                ArrayList list = new ArrayList();
                while (it.hasNext())
                {
                    Element element = (Element) it.next();
                    String actualXPath = XMLDocumentUtility.getXPath(element);
                    list.add(xpathEntityMap.get(new EntityKey(actualXPath,
                        ce.getEntityName())));
                }
                return list;
            }
            else
            {
                if (elements.size() > 1)
                {
                    throw new DuplicateForeignKeyException("Duplicate " +
                        "elements matching the child entity found. Entity: " +
                        entity.getName());
                }
                else
                {
                    Element element = (Element) elements.iterator().next();
                    String actualXPath = XMLDocumentUtility.getXPath(element);
                    return (Entity) xpathEntityMap.get(
                        new EntityKey(actualXPath, ce.getEntityName()));
                }
            }
        }
    }

    private Object parseFieldValue(String str, String valueClass, String format, int position) throws Exception
    {
        if (str == null)
        {
            return null;
        }
        else if (str.equals(""))
        {
            if (valueClass.equals("java.lang.String"))
            {
                return "";
            }
            else
            {
				return null;
            }
        }
        if (valueClass.equals("java.lang.String"))
        {
            return str;
        }
        else if (valueClass.equals("java.lang.Integer"))
        {
			if (str.trim().equals(""))
	        {
		        return null;
			}
			try
			{
            	return new Integer(Integer.parseInt(str));
			}
			catch(Exception e)
			{
				return null;
			}
        }
        else if (valueClass.equals("java.lang.Float"))
        {
			if (str.trim().equals(""))
	        {
		        return null;
			}
			try
			{
	            return new Float(Float.parseFloat(str));
			}
			catch(Exception e)
			{
				return null;
			}
        }
        else if (valueClass.equals("java.util.Date"))
        {
          if(format != null)
          {
			try
			{
	          ParsePosition pos = new ParsePosition(position);
              SimpleDateFormat sdf = new SimpleDateFormat(format);
              Date date = sdf.parse(str, pos);
              Timestamp time = new Timestamp(date.getTime());
              return time.toString();
		    }
		    catch(Exception ex)
		    {
			   return "";
			}
          }
          else
            return "";
        }
        else if (valueClass.equals("java.lang.Long"))
        {
			try
			{
	            return new Long(Long.parseLong(str));
			}
			catch(Exception e)
			{
				return null;
			}
        }
        else if (valueClass.equals("java.lang.Boolean"))
        {
            if (str.equals("true"))
            {
                return new Boolean(true);
            }
            else
            {
                return new Boolean(false);
            }
        }
        else
        {
            throw new UnmatchingFieldTypeException("Field type mismatch. " +
                "Type: " + valueClass + " Value: " + str);
        }
    }

    private String getFieldValue(FieldType type, String xpath,
        String absXPath) throws Exception
    {
        if (type.getType() == FieldType.VALUE_LITERAL)
        {
            ValueLiteral value = (ValueLiteral) type;
            return value.getValue();
        }
        else
        {
            XPathExpr expr = (XPathExpr) type;
            String fXPath = expr.getXPath();
            if (fXPath.equals("."))
            {
                return XMLDocumentUtility.stringValueOf(absXPath, document);
            }
            else
            {
                return XMLDocumentUtility.stringValueOf(absXPath + '/' + fXPath,
                    document);
            }
            // The xpath is considered relative, so no other conditions
            /*else if (fXPath.indexOf(xpath + '/') != -1)
            {
                String str = fXPath.substring(fXPath.indexOf(xpath) +
                    xpath.length());
                return XMLDocumentUtility.stringValueOf(absXPath + str, document);
            }
            else
            {
                String pPath = getParent(fXPath, xpath);
                String absPPath = getAbsoluteParent(pPath, absXPath);
                String suffix = fXPath.substring(fXPath.indexOf(pPath) + 1);
                return XMLDocumentUtility.stringValueOf(absPPath + suffix, document);
            }*/
        }
    }

    //Method which returns the common parent of the xpaths
    //passed as arguments.
    //For e.g. The common parent of '/firm/employee/permanent_address/address'
    //and '/firm/employee/language_spoken/language' is /firm/employee
    private String getParent(String fPath, String ePath)
    {
        StringTokenizer fst = new StringTokenizer(fPath, "/");
        StringTokenizer est = new StringTokenizer(ePath, "/");
        StringTokenizer first = fst;
        StringTokenizer second = est;
        if (fst.countTokens() > est.countTokens())
        {
            first = est;
            second = fst;
        }
        String str = "/";
        while (first.hasMoreTokens())
        {
            String temp = first.nextToken();
            if (!temp.equals(second.nextToken()))
            {
                return str;
            }
            else
            {
                str = str + temp + '/';
            }
        }
        return str;
    }

    //Returns the absolue parent. The parent string passed is the xpath of
    //absPath's parent. It is the abstract xpath of parent. Indexes will be
    //obtained from the absPath to form the absolute path of the parent
    private String getAbsoluteParent(String parent, String absPath)
    {
        int count = 0;
        int startIndex = 0;
        int index = -1;
        while ((index = parent.indexOf('/', startIndex)) != -1)
        {
            count++;
            startIndex = index + 1;
        }
        startIndex = 0;
        while (count > 0)
        {
            index = absPath.indexOf('/', startIndex);
            if (index == -1)
            {
                break;
            }
            startIndex = index + 1;
            count--;
        }
        if (index == 0 || index == -1)
        {
            return "";
        }
        return absPath.substring(0, index);
    }

    public String toString()
    {
        String str = "";
        Iterator it = entityMap.values().iterator();
        while (it.hasNext())
        {
            ArrayList list = (ArrayList) it.next();
            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity) list.get(i);
                str += '\n' + entity.toString();
            }
        }
        return str;
    }

    //The key for entities
    private class EntityKey
    {

        private String xpath;
        private String entityName;

        public EntityKey(String xpath, String entityName)
        {
            this.xpath = xpath;
            this.entityName = entityName;
        }

        public boolean equals(Object obj)
        {
            if (!(obj instanceof EntityKey))
            {
                return false;
            }
            EntityKey key = (EntityKey) obj;
            if (this.xpath.equals(key.xpath) &&
                this.entityName.equals(key.entityName))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public int hashCode()
        {
            return (xpath + entityName).hashCode();
        }

        public boolean equals(String xpath, String entityName)
        {
            return (this.xpath.equals(xpath) &&
                this.entityName.equals(entityName));
        }
    }
}
