/**
 * 
 */
package com.etrade.sftp.bl;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.UserAuthenticator;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.auth.StaticUserAuthenticator;
import org.apache.commons.vfs.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs.impl.StandardFileSystemManager;
import org.apache.commons.vfs.provider.local.LocalFile;
import org.apache.commons.vfs.provider.sftp.SftpFileSystemConfigBuilder;

/**
 * @author EricLoh
 *
 */
public class SFTPTest
{
    // Set these variables for your testing environment:
    private String host = "172.20.31.29"; // Remote SFTP hostname
    private String user = "eric"; // Remote system login name
    private String password = "eric"; // Remote system password
    private String remoteDir = "/export/home/eric";
    // Look for a file path like "smoke20070128_wkt.txt"
    // private String filePatternString = ".*/smoke\\d{8}_wkt\\.txt";
    private String filePatternString = ".*/Test.xml";
    // Local directory to receive file
    private String localDir = "D:/Temp/sftptest/";

    private File localDirFile;
    private Pattern filePattern;
    private FileSystemManager fsManager = null;
    private FileSystemOptions opts = null;
    private FileObject sftpFile;

    private FileObject src = null; // used for cleanup in release()

    /**
     * @param args
     */
    public static void main(String args[])
    {
        System.out.println("SFTPClient started");

        SFTPTest app = new SFTPTest();

        app.initialize();

        app.uploadFile("172.20.31.29", "/export/home/eric", "D:/Temp/Test.xml", "uploadedTest.xml");

        app.release();

        System.out.println("SFTPClient done");
    }

    /**
     * Disable Strict HostKey verification
     *  
     */
    public void initialize()
    {
        try
        {
            this.opts = new FileSystemOptions();
            
            //Disable strict Hostkey verification
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(this.opts, "no");
            
            this.fsManager = VFS.getManager();
        }
        catch (FileSystemException e)
        {
            throw new RuntimeException("failed to get fsManager from VFS", e);        
        }
        
        try
        {
            //Sets the userName and password if provided
            UserAuthenticator auth = new StaticUserAuthenticator(null, this.user, this.password);
            
            //Adds the userCredentials to the fileSystem options
            DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
        }
        catch (FileSystemException e)
        {
            throw new RuntimeException("setUserAuthenticator failed", e);
        }        
    }
    
    /**
     * @param remoteHostName
     * @param remoteDir
     * @param localFilePath
     * @param remoteFileName
     */
    public void uploadFile(String remoteHostName, String remoteDir, String localFilePath, String remoteFileName)
    {
        String remotePath = "sftp://" + remoteHostName + remoteDir + "/" + remoteFileName; 
        try
        {
            this.sftpFile = this.fsManager.resolveFile(remotePath, opts);
            
            System.out.println("SFTP connected successfully established to " + remotePath);
            
            
            FileObject srcFile = this.fsManager.resolveFile(new File(localFilePath).getAbsolutePath());
            
            this.sftpFile.copyFrom(srcFile, Selectors.SELECT_SELF);
            
            System.out.println("File: " + localFilePath + " uploaded successfully." + this.fsManager);
        }
        catch (FileSystemException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Retrieves files that match the specified FileSpec from the SFTP server and stores them in the local directory.
     */
    public void process()
    {
        String startPath = "sftp://" + this.host + this.remoteDir;
        FileObject[] children;

        // Set starting path on remote SFTP server.
        try
        {
            this.sftpFile = this.fsManager.resolveFile(startPath, opts);

            System.out.println("SFTP connection successfully established to " + startPath);
        }
        catch (FileSystemException ex)
        {
            throw new RuntimeException("SFTP error parsing path " + this.remoteDir, ex);
        }

        // Get a directory listing
        try
        {
            children = this.sftpFile.getChildren();
        }
        catch (FileSystemException ex)
        {
            throw new RuntimeException("Error collecting directory listing of " + startPath, ex);
        }

        search: for (FileObject f : children)
        {
            try
            {
                String relativePath = File.separatorChar + f.getName().getBaseName();

                if (f.getType() == FileType.FILE)
                {
                    System.out.println("Examining remote file " + f.getName());

                    if (!this.filePattern.matcher(f.getName().getPath()).matches())
                    {
                        System.out.println("  Filename does not match, skipping file ." + relativePath);
                        continue search;
                    }

                    String localUrl = "file://" + this.localDir + relativePath;
                    String standardPath = this.localDir + relativePath;
                    System.out.println("  Standard local path is " + standardPath);
                    LocalFile localFile = (LocalFile) this.fsManager.resolveFile(localUrl);
                    System.out.println("    Resolved local file name: " + localFile.getName());

                    if (!localFile.getParent().exists())
                    {
                        localFile.getParent().createFolder();
                    }

                    System.out.println("  ### Retrieving file ###");
                    localFile.copyFrom(f, new AllFileSelector());
                }
                else
                {
                    System.out.println("Ignoring non-file " + f.getName());
                }
            }
            catch (FileSystemException ex)
            {
                throw new RuntimeException("Error getting file type for " + f.getName(), ex);
            }
        } // for (FileObject f : children)

        // Set src for cleanup in release()
        src = children[0];
    } // process(Object obj)

    /**
     * Release system resources, close connection to the filesystem.
     */
    public void release()
    {
        try
        {
            this.sftpFile.close();
            
            ((StandardFileSystemManager)this.fsManager).close();
        }
        catch (FileSystemException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            
        }
        
    } // release()

//    /**
//     * Copies a remote file to local filesystem.
//     */
//    public static void copyRemoteFile(String host, String user, String password, String remotePath, String localPath) throws IOException
//    {
//        // we first set strict key checking off
//        FileSystemOptions fsOptions = new FileSystemOptions();
//
//        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(fsOptions, "no");
//
//        // now we create a new filesystem manager
//        DefaultFileSystemManager fsManager = (DefaultFileSystemManager) VFS.getManager();
//
//        // the url is of form sftp://user:pass@host/remotepath/
//        String uri = "sftp://" + user + ":" + password + "@" + host + "/" + remotePath;
//
//        // get file object representing the local file
//        FileObject fo = fsManager.resolveFile(uri, fsOptions);
//
//        // open input stream from the remote file
//        BufferedInputStream is = new BufferedInputStream(fo.getContent().getInputStream());
//
//        // open output stream to local file
//        OutputStream os = new BufferedOutputStream(new FileOutputStream(localPath));
//        int c;
//
//        // do copying
//        while ((c = is.read()) != -1)
//        {
//            os.write(c);
//        }
//        os.close();
//        is.close();
//        // close the file object
//        fo.close();
//        // NOTE: if you close the file system manager, you won't be able to
//        // use VFS again in the same VM. If you wish to copy multiple files,
//        // make the fsManager static, initialize it once, and close just
//        // before exiting the process.
//        fsManager.close();
//        System.out.println("Finished copying the file");
//    }
}
