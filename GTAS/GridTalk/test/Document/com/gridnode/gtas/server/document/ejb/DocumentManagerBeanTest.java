/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 13 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.ejb;

import com.gridnode.gtas.server.document.entities.ejb.*;
import com.gridnode.gtas.server.document.facade.ejb.*;
import com.gridnode.gtas.server.document.model.*;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

public class DocumentManagerBeanTest extends TestCase
{
  static final String _DOC_TYPE = "Test";
  static final String _DOC_DESC = "Test DocumentManagerBean Desc";
  static final String _UPD_DOC_DESC = "Test Update DocumentManagerBean Desc";

  IDocumentManagerHome _home;
  IDocumentManagerObj _remote;

  public DocumentManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DocumentManagerBeanTest.class);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws Exception
  {
    Log.log("TEST", "[DocumentManagerBeanTest.setUp] starts");

    _home = (IDocumentManagerHome)ServiceLookup.getInstance(
              ServiceLookup.CLIENT_CONTEXT).getHome(
              IDocumentManagerHome.class);
    assertNotNull("IDocumentManagerHome is null", _home);
    _remote = _home.create();
    assertNotNull("IDocumentManagerObj is null", _remote);

    Log.log("TEST", "[DocumentManagerBeanTest.setUp] exits");
  }

  protected void tearDown() throws Exception
  {
    Log.log("TEST", "[DocumentManagerBeanTest.tearDown] starts");
    if (getDocumentType(_DOC_TYPE) != null)
    {
      deleteDocumentType(_DOC_TYPE);
    }
    Log.log("TEST", "[DocumentManagerBeanTest.tearDown] exits");
  }

  private void createDocumentType(String name, String desc)
  {
    Log.log("TEST", "[DocumentManagerBeanTest.createTestData] starts");

    try
    {
      DocumentType docType = new DocumentType();
      docType.setName(name);
      docType.setDescription(desc);
      _remote.createDocumentType(docType);
    }
    catch (Exception ex)
    {
      Log.err("Exception in creating documentType", ex);
      assertTrue("Error in creating documentType", false);
    }

    Log.log("TEST", "[DocumentManagerBeanTest.createTestData] exits");
  }

  private DocumentType getDocumentType(String name)
  {
    Log.log("TEST", "[DocumentManagerBeanTest.getDocumentType] starts");

    try
    {
      return _remote.findDocumentType(name);
    }
    catch (Exception ex)
    {
      Log.err("Exception in retrieving documentType", ex);
      assertTrue("Error in retrieving documentType", false);
    }
    return null;
  }

  private void updateDocumentType(String name, String updDesc)
  {
    Log.log("TEST", "[DocumentManagerBeanTest.getDocumentType] starts");

    try
    {
      DocumentType docType = getDocumentType(name);
      docType.setDescription(updDesc);
      _remote.updateDocumentType(docType);
    }
    catch (Exception ex)
    {
      Log.err("Exception in updating documentType", ex);
      assertTrue("Error in updating documentType", false);
    }
  }

  private void deleteDocumentType(String name)
  {
    Log.log("TEST", "[DocumentManagerBeanTest.updateDocumentType] starts");

    try
    {
      DocumentType docType = _remote.findDocumentType(name);
      _remote.deleteDocumentType((Long)docType.getFieldValue(IDocumentType.UID));
    }
    catch (Exception ex)
    {
      Log.err("Exception in deleting documentType", ex);
      assertTrue("Error in deleting documentType", false);
    }
    Log.log("TEST", "[DocumentManagerBeanTest.deleteTestData] exits");
  }

  public void testCreateDocumentType()
  {
    Log.log("TEST", "[DocumentManagerBeanTest.testCreateDocumentType] starts");

    createDocumentType(_DOC_TYPE, _DOC_DESC);

    DocumentType docType = getDocumentType(_DOC_TYPE);
    assertNotNull("DocumentType not created", docType);
    assertEquals("DocumentType name not equal", _DOC_TYPE, docType.getName());
    assertEquals("DocumentType desc not equal", _DOC_DESC, docType.getDescription());

    Log.log("TEST", "[DocumentManagerBeanTest.testCreateDocumentType] exits");
  }

  public void testUpdateDocumentType()
  {
    Log.log("TEST", "[DocumentManagerBeanTest.testUpdateDocumentType] starts");

    createDocumentType(_DOC_TYPE, _DOC_DESC);
    updateDocumentType(_DOC_TYPE, _UPD_DOC_DESC);

    DocumentType docType = getDocumentType(_DOC_TYPE);
    assertNotNull("DocumentType not created", docType);
    assertEquals("DocumentType name not equal", _DOC_TYPE, docType.getName());
    assertEquals("DocumentType desc not equal", _UPD_DOC_DESC, docType.getDescription());

    Log.log("TEST", "[DocumentManagerBeanTest.testUpdateDocumentType] exits");
  }

  public void testDeleteDocumentType()
  {
    Log.log("TEST", "[DocumentManagerBeanTest.testDeleteDocumentType] starts");

    createDocumentType(_DOC_TYPE, _DOC_DESC);
    DocumentType docType = getDocumentType(_DOC_TYPE);
    assertNotNull("DocumentType not created", docType);

    deleteDocumentType(_DOC_TYPE);
    docType = getDocumentType(_DOC_TYPE);
    assertNull("DocumentType not deleted", docType);

    Log.log("TEST", "[DocumentManagerBeanTest.testDeleteDocumentType] exits");
  }

}