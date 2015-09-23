package com.gridnode.pdip.base.docservice.manager.ejb;

import java.lang.reflect.UndeclaredThrowableException;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import com.gridnode.pdip.base.docservice.filesystem.ejb.*;
import com.gridnode.pdip.base.docservice.filesystem.model.*;
import com.gridnode.pdip.base.docservice.filesystem.tree.*;

public class DocumentServiceTest extends TestCase
{

    IDocumentMgrObj remote;
    long f1, f2, f11, f111, f21, d11, d111;

    public DocumentServiceTest(String str)
    {
        super(str);
    }

    protected void setUp() throws Exception
    {
        //assumes that localhost domain exists, having UID 1

        /*
        Create the following folders:
        localhost/Folder1
        localhost/Folder2
        localhost/Folder1/SubFolder11
        localhost/Folder1/SubFolder11/SubFolder111
        localhost/Folder2/SubFolder21

        Create the following documents:
        localhost/Folder1/SubFolder11/SubFolder111/Test Document 111
        localhost/Folder1/SubFolder11/Test Document 11
        */
        remote = getRemote();

        java.io.File main1 = new java.io.File("data/ebBPSS.xml");
        java.io.File main2 = new java.io.File("data/test_xpdl.xml");
        java.io.File[] attach1 = new java.io.File[1];
        attach1[0] = new java.io.File("data/BPSS1.01.dtd");
        java.io.File[] attach2 = null;


        Folder folder1 = remote.createFolder(new Long(1), "Folder1");
        Folder folder2 = remote.createFolder(new Long(1), "Folder2");
        f1 = folder1.getUId();
        f2 = folder2.getUId();
        Folder sf11 = remote.createSubFolder(new Long(folder1.getUId()),
            "SubFolder11");
        f11 = sf11.getUId();
        Folder sf21 = remote.createSubFolder(new Long(folder2.getUId()),
            "SubFolder21");
        f21 = sf21.getUId();
        Folder sf111 = remote.createSubFolder(new Long(sf11.getUId()),
            "SubFolder111");
        f111 = sf111.getUId();

        Document doc111 = remote.createDocument("Test Document 111",
            "BPSS Process Specification", "Author",
            new Long(sf111.getUId()), main1, attach1);
        d111 = doc111.getUId();
        Document doc11 = remote.createDocument("Test Document 11",
            "XPDL Process Specification", "Author",
            new Long(sf11.getUId()), main2, attach2);
        d11 = doc11.getUId();

    }

    private Document createDocument(long folderId) throws Exception
    {
        java.io.File file = new java.io.File("data/ebBPSStest.xml");
        java.io.File[] attach = new java.io.File[1];
        attach[0] = new java.io.File("data/BPSS1.01.dtd");
        return remote.createDocument("Dummy Document",
            "BPSS Process Specification", "Author",
            new Long(folderId), file, attach);
    }

    protected void tearDown() throws Exception
    {
        remote.deleteFolder(new Long(f1));
        remote.deleteFolder(new Long(f2));
    }

    public static Test suite()
    {
        return new TestSuite(DocumentServiceTest.class);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
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

    public void testMoveFolderUsingIds()
    {
        try
        {
            Folder folder = remote.moveFolder(new Long(f111), new Long(f21));
            folder = remote.moveFolder(new Long(folder.getUId()), new Long(f11));
            f111 = folder.getUId();
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
    }

    public void testMoveFolderUsingPath()
    {
        try
        {
            Folder folder = remote.moveFolder("localhost/Folder1/SubFolder11/SubFolder111",
                "localhost/Folder2/SubFolder21");
            folder = remote.moveFolder("localhost/Folder2/SubFolder21/SubFolder111",
                "localhost/Folder1/SubFolder11");
            f111 = folder.getUId();
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
    }

    public void testMoveFolderNegative1() throws Exception
    {
        try
        {
            Folder folder = remote.moveFolder(new Long(f11), new Long(f111));
            fail("No exception thrown when a folder is moved to its sub folder");
        }
        catch (Exception e)
        {
        }
    }

    public void testMoveFolderNegative2() throws Exception
    {
        Folder folder = remote.createSubFolder(new Long(f21), "SubFolder111");
        try
        {
            Folder f = remote.moveFolder(new Long(f111), new Long(f21));
            fail("No exception thrown");
        }
        catch (Exception e)
        {
        }
        finally
        {
            remote.deleteFolder(new Long(folder.getUId()));
        }
    }

    public void testMoveDocumentUsingIds() throws Exception
    {
        try
        {
            Document document = remote.moveDocument(new Long(d111), new Long(f11));
            document = remote.moveDocument(new Long(d111), new Long(f111));
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
    }

    public void testMoveDocumentsUsingPaths() throws Exception
    {
        try
        {
            Document document = remote.moveDocument("localhost/Folder1/SubFolder11/SubFolder111/Test Document 111",
                "localhost/Folder2/SubFolder21");
            document = remote.moveDocument("localhost/Folder2/SubFolder21/Test Document 111",
                "localhost/Folder1/SubFolder11/SubFolder111");
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
    }

    //Move a document to a folder having another document using a file
    //having the same name
    public void testMoveDocumentsNegative1() throws Exception
    {
        Document doc = createDocument(f21);
        try
        {
            Document document = remote.moveDocument(new Long(d111), new Long(f21));
            fail("No exception thrown");
        }
        catch (Exception e)
        {
        }
        finally
        {
            remote.deleteDocument(new Long(doc.getUId()));
        }
    }

    public void testMoveFolderToDomain() throws Exception
    {
        try
        {
            Folder folder = remote.moveFolder("localhost/Folder1/SubFolder11/SubFolder111",
                "localhost");
            folder = remote.moveFolder("localhost/SubFolder111",
                "localhost/Folder1/SubFolder11");
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
    }

    public void testCopyFolderUsingIds() throws Exception
    {
        Folder folder = null;
        try
        {
            folder = remote.copyFolder(new Long(f11), new Long(f21));
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
        finally
        {
            if (folder != null)
            {
                remote.deleteFolder(new Long(folder.getUId()));
            }
        }
    }

    public void testCopyFolderUsingPaths() throws Exception
    {
        Folder folder = null;
        try
        {
            folder = remote.copyFolder("localhost/Folder1/SubFolder11",
                "localhost/Folder2/SubFolder21");
        }
        catch (Exception e)
        {
            fail("Exception occured: " + e);
        }
        finally
        {
            if (folder != null)
            {
                remote.deleteFolder(new Long(folder.getUId()));
            }
        }
    }

    public void testCopyDocumentUsingIds() throws Exception
    {
        Document document = null;
        try
        {
            document = remote.copyDocument(new Long(d11), new Long(f21));
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
        finally
        {
            if (document != null)
            {
                remote.deleteDocument(new Long(document.getUId()));
            }
        }
    }

    public void testCopyDocumentUsingNames() throws Exception
    {
        Document document = null;
        try
        {
            document = remote.copyDocument("localhost/Folder1/SubFolder11/SubFolder111/Test Document 111",
              "localhost/Folder2");
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
        finally
        {
            if (document != null)
            {
                remote.deleteDocument("localhost/Folder2/Test Document 111");
            }
        }
    }


    public void testTreeModel() throws Exception
    {
        try
        {
            DocumentTreeModel treeModel = remote.getDocumentTree();
            assertNotNull("getDocumentTree returned null", treeModel);
        }
        catch (Exception e)
        {
            fail("Exception thrown: " + e);
        }
    }
}