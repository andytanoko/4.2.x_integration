package com.gridnode.pdip.base.docservice.manager.ejb;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import com.gridnode.pdip.base.docservice.filesystem.ejb.*;
import com.gridnode.pdip.base.docservice.filesystem.model.*;

import java.util.Collection;
import java.util.Iterator;

public class DocumentMgrBeanTest extends TestCase
{

    IDocumentMgrObj remote;

    public DocumentMgrBeanTest(String str)
    {
        super(str);
    }

    protected void setUp() throws Exception
    {
        //assumes that localhost domain exists, having UID 1
        remote = getRemote();
    }

    protected void tearDown() throws Exception
    {
    }

    public static Test suite()
    {
        return new TestSuite(DocumentMgrBeanTest.class);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public void testFindDomainId() throws Exception
    {
        try
        {
            long id = remote.getDomainId("localhost");
            System.out.println(id);
            assertTrue("id returned is not 1", id == 1);
        }
        catch (Exception e)
        {
            fail("Exception thrown " + e.getMessage());
        }
    }

    public void testRenameDomainUsingName() throws Exception
    {
        try
        {
            Domain domain = remote.renameDomain("localhost", "NewName");
            assertNotNull("renameDomain returned null", domain);
            domain = remote.renameDomain("NewName", "localhost");
        }
        catch (Exception e)
        {
            fail("Exception thrown " + e.getMessage());
        }
    }

    public void testRenameDomainUsingId() throws Exception
    {
        try
        {
            Domain domain = remote.renameDomain(new Long(1), "NewName");
            assertNotNull("renameDomain returned null", domain);
            domain = remote.renameDomain(new Long(1), "localhost");
        }
        catch (Exception e)
        {
            fail("Exception thrown " + e.getMessage());
        }
    }

    public void testCreateFolder() throws Exception
    {
        try
        {
            Folder folder = remote.createFolder(new Long(1), "NewFolder");
            assertNotNull("createFolder returned null", folder);
            remote.deleteFolder(new Long(folder.getUId()));
        }
        catch (Exception e)
        {
            fail("Exception thrown " + e);
        }
    }

    public void testFindFolderId() throws Exception
    {
        try
        {
            Folder folder = remote.createFolder(new Long(1), "Folder");
            try
            {
                long id = remote.getFolderId("localhost/Folder");
            }
            catch (Exception e)
            {
                fail("Exception occured while getting folder id " + e);
            }
            finally
            {
                remote.deleteFolder(new Long(folder.getUId()));
            }
        }
        catch (Exception e)
        {
            fail("Exception thrown : " + e.getMessage());
        }
    }

    public void testCreateDuplicateFolder() throws Exception
    {
        try
        {
            Folder folder1 = remote.createFolder(new Long(1), "Folder");
            try
            {
                Folder folder2 = remote.createFolder(new Long(1), "Folder");
                fail("No exception occured on creating duplicate folder");
            }
            catch (Exception e)
            {
            }
            finally
            {
                remote.deleteFolder(new Long(folder1.getUId()));
            }
        }
        catch (Exception e)
        {
            fail("Exception thrown " + e.getMessage());
        }
    }

    public void testCreateSubFolder() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        try
        {
            Folder child = remote.createSubFolder(new Long(folder.getUId()), "NewSubFolder");
            assertNotNull("createFolder returned null", child);
        }
        catch (Exception e)
        {
            fail("Exception thrown " + e);
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testRenameFolder() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        try
        {
            Folder child = remote.createSubFolder(new Long(folder.getUId()), "NewSubFolder");
            Folder renamedChild = remote.renameFolder(new Long(child.getUId()), "newName");
            assertTrue("renameFolder returned invalid id", child.getUId() == renamedChild.getUId());
        }
        catch (Exception e)
        {
            fail("Exception thrown " + e);
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testCreateDocument() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        java.io.File main = new java.io.File("data/ebBPSS.xml");
        java.io.File[] attachments = new java.io.File[1];
        attachments[0] = new java.io.File("data/BPSS1.01.dtd");

        try
        {
            Document doc = remote.createDocument("Test Document",
                "BPSS Process Specification", "Author",
                new Long(folder.getUId()), main, attachments);
            assertNotNull("create document returned null", doc);
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testCreateDuplicateDocument() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        java.io.File main = new java.io.File("data/ebBPSS.xml");
        java.io.File[] attachments = new java.io.File[1];
        attachments[0] = new java.io.File("data/BPSS1.01.dtd");
        Document doc = remote.createDocument("Test Document",
            "BPSS Process Specification", "Author",
            new Long(folder.getUId()), main, attachments);

        try
        {
            doc = remote.createDocument("Test Document",
                "BPSS Process Specification", "Author",
                new Long(folder.getUId()), main, attachments);
            fail("Exception not thrown on creating duplicate documents");
        }
        catch (Exception e)
        {
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testCreateDocumentsUsingSameFile() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        java.io.File main1 = new java.io.File("data/ebBPSS.xml");
        java.io.File main2 = new java.io.File("data/ebBPSStest.xml");
        java.io.File[] attachments = new java.io.File[1];
        attachments[0] = new java.io.File("data/BPSS1.01.dtd");
        Document doc = remote.createDocument("Test Document",
            "BPSS Process Specification", "Author",
            new Long(folder.getUId()), main1, attachments);

        try
        {
            doc = remote.createDocument("Another Document",
                "BPSS Process Specification", "Author",
                new Long(folder.getUId()), main2, attachments);
            fail("Exception not thrown on creating documents using the same file");
        }
        catch (Exception e)
        {
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testRemoveDocument() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        java.io.File main = new java.io.File("data/ebBPSS.xml");
        java.io.File[] attachments = new java.io.File[1];
        attachments[0] = new java.io.File("data/BPSS1.01.dtd");
        Document doc = remote.createDocument("Test Document",
            "BPSS Process Specification", "Author",
            new Long(folder.getUId()), main, attachments);

        try
        {
            remote.deleteDocument(new Long(doc.getUId()));
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testGetFileByPath() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        java.io.File main = new java.io.File("data/ebBPSS.xml");
        java.io.File[] attachments = new java.io.File[1];
        attachments[0] = new java.io.File("data/BPSS1.01.dtd");
        Document doc = remote.createDocument("Test Document",
            "BPSS Process Specification", "Author",
            new Long(folder.getUId()), main, attachments);

        try
        {
            java.io.File file = remote.getFile("localhost/Folder/ebBPSS.xml");
            assertNotNull("getFile method returned null", file);
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testGetDocumentFilesById() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        java.io.File main = new java.io.File("data/ebBPSS.xml");
        java.io.File[] attachments = new java.io.File[1];
        attachments[0] = new java.io.File("data/BPSS1.01.dtd");
        Document doc = remote.createDocument("Test Document",
            "BPSS Process Specification", "Author",
            new Long(folder.getUId()), main, attachments);

        try
        {
            Collection files = remote.getDocumentFiles(new Long(doc.getUId()));
            assertNotNull("getDocumentFiles method returned null", files);
            assertTrue("getDocumentFiles method returned wrong values",
                files.size() == 2);
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testGetDocumentFilesByPath() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        java.io.File main = new java.io.File("data/ebBPSS.xml");
        java.io.File[] attachments = new java.io.File[1];
        attachments[0] = new java.io.File("data/BPSS1.01.dtd");
        Document doc = remote.createDocument("Test Document",
            "BPSS Process Specification", "Author",
            new Long(folder.getUId()), main, attachments);

        try
        {
            Collection files = remote.getDocumentFiles("localhost/Folder/Test Document");
            assertNotNull("getDocumentFiles method returned null", files);
            assertTrue("getDocumentFiles method returned wrong values",
                files.size() == 2);
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testGetFileById() throws Exception
    {
        Folder folder = remote.createFolder(new Long(1), "Folder");
        java.io.File main = new java.io.File("data/ebBPSS.xml");
        java.io.File[] attachments = new java.io.File[1];
        attachments[0] = new java.io.File("data/BPSS1.01.dtd");
        Document doc = remote.createDocument("Test Document",
            "BPSS Process Specification", "Author",
            new Long(folder.getUId()), main, attachments);

        try
        {
            Collection files = remote.getDocumentFiles(new Long(doc.getUId()));
            Iterator it = files.iterator();
            java.io.File file = remote.getFile(new Long(((File) it.next()).getUId()));
            assertNotNull("getFile method returned null", file);
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    private IDocumentMgrObj getRemote() throws Exception
    {
        IDocumentMgrHome mgrHome =
            (IDocumentMgrHome) ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
            IDocumentMgrHome.class);
        assertNotNull("Home lookup returned null", mgrHome);
        return mgrHome.create();
    }
}