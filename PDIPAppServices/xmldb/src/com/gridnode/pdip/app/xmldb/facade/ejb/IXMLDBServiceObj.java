/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IXMLDBServiceObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 24 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.facade.ejb;

import com.gridnode.pdip.app.xmldb.exceptions.XMLDBException;

import javax.ejb.EJBObject;

import java.io.File;
import java.util.HashMap;
import java.util.Collection;
import java.rmi.RemoteException;

public interface IXMLDBServiceObj extends EJBObject
{

    /**
     * Populates the data from the xml file to database. The mapping file
     * specifies the mapping of xml element to entity-bean field names.
     * The mapping file should match the dtd - XML-DB-MappingFile.dtd
     * @param xmlFile The xml file to use
     * @param dtdFile The dtd of the xml file. If this is null, the xmlFile won't
     * be checked for validation
     * @param mappingFile The mapping file which maps the element to database
     */
    public Collection insertDataFromXML(String xmlFile, String dtdFile, String mappingFile)
        throws RemoteException, XMLDBException;

    /**
     * Generates an XML file from the data in the database using a mapping file
     * which maps the entity-bean field-name to xml element-name. The mapping file
     * should match the dtd DB-XML-MappingFile.dtd
     * @param mappingFile The mapping file to be used
     * @param inputs The input values. Will be referred by input names in the
     * mapping file
     * @param dtdFile The dtd of the resulting xml file.
     */
    public File generateXML(String mappingFile, HashMap inputs, String dtdFile)
        throws RemoteException, XMLDBException;
}
