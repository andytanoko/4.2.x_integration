package com.gridnode.ftp.facade;

/**
 * <p>Title: UserProcedure </p>
 * <p>Description: UserProcedure for customers.</p>
 * <p>Copyright: Copyright (c) 2000-2004</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author Josyula.Jagadeesh
 * @version 1.0
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.*;

import org.apache.commons.net.ftp.FTPFile;

import com.gridnode.ftp.*;
import com.gridnode.ftp.exception.FTPException;
import com.gridnode.ftp.exception.FTPServiceException;
import com.gridnode.ftp.exception.NestingException;

/**
 * This class acts as a facade, which performs a subset of ftp operations.
 *
 * FTPClient is instantiated, according to the provider specified, from the xml
 * config file. The actual FTPClient is implemented by an implementation of
 * <code>IFTPClientManager</code> interface.
 *
 * <p>Title: UserProcedure </p>
 * <p>Description: UserProcedure for customers.</p>
 * <p>Copyright: Copyright (c) 2000-2004</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author Josyula.Jagadeesh
 * @version 1.0
 */

public class FTPServiceFacade
{
  private IFTPClientManager _ftpClientManager = null;
  private static Map _ftpSites = null;
  private boolean _debug = false;
  //Class level lock used, to perform synchronize operations
  private static Map _classLockCollection = new HashMap();
  //private static Class classLock = Object.class;

  static
  {
    try
    {
      initilizeSite();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }


  public FTPServiceFacade()
  	throws Exception
  {
    if (_ftpSites == null)
    {
      initilizeSite();
    }
    _debug = true;
  }

  private static void initilizeSite()
  	throws Exception
  {
    try {
      //Initilizes Site from xml config file.
      _ftpSites = SiteInitilizer.initilizeSites();
      initilizeClassLocks();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  private static void initilizeClassLocks()
  {
    if (_ftpSites != null || !_ftpSites.isEmpty())
    {
      Set keyset = _ftpSites.keySet();
      for (Iterator ite = keyset.iterator();ite.hasNext();)
      {
        String key = (String)ite.next();
        _classLockCollection.put(key,Object.class);
      }
    }
  }


  /**
   * Puts the given file in the remote/local ftp server.
   * Authentication information is read from xml config file(ftpservice.xml).
   *
   * Supports Proxy Servers. Not tested with SOCKS
   * @param fileName The filename to upload to.
   * @return a integer, returning a FTP Code.
   * @throws java.lang.Exception, thrown when cannot perform FTP Operation.
   */

  public int pushToFTP(String fileName) throws Exception
  {
    return pushToFTP("", fileName);
  }

  public int pushAndRename(String fileName) throws Exception
  {
    return pushAndRename(ISiteConfig.DEFAULT_SITE, fileName);
  }

  public int pushAndRename(String siteName, String fileName) throws Exception
  {
    int returnCode = 1;
    Site siteLoc = getSiteBySiteName(siteName);
    int retryCount = Integer.parseInt(siteLoc.getProperty(ISiteConfig.
        RETRY_COUNT));
    if (siteLoc == null) {
      log(
          "[FtpServiceFacade][pushAndRename][Cannot push as No Site was Configured]");
      return returnCode;
    }
    try {
      log("[FtpServiceFacade][pushAndRename][Begin]");
      if (fileName == null) {
        log("[FtpServiceFacade][pushAndRename][Input File DoseNot Exists]" +
            fileName);
        return returnCode;
      }

      initilizeFtpManager(siteLoc);
      if (_ftpClientManager != null) {
        String pwd = _ftpClientManager.getDirectory();
        returnCode = pushFileToFtp(siteLoc, fileName);
        log("[FtpServiceFacade][pushAndRename()][isPositiveCompletion]" +
            _ftpClientManager.isPositiveCompletion());

        //If push completed with positive completion then check if rename is set.
        if (_ftpClientManager.isPositiveCompletion()) {
          //If need to rename then do rename, else complete the while loop.
          if (Boolean.TRUE.equals(Boolean.valueOf(siteLoc.getProperty(
              ISiteConfig.IS_RENAME)))) {
            //This block dose the rename and move operation
           Class classLock =  (Class)_classLockCollection.get(siteName);
	   System.out.println("synchronized on -- "+siteName);
            synchronized (classLock) {
              log(
                  "[FtpServiceFacade][pushAndRename()][Enter Synchronized block...]");
              String nextNum = null;
              boolean isPositiveCompletion = false;
              while (!isPositiveCompletion && retryCount > 0) {
                if (nextNum == null) {
                  System.out.println("In Getting NextNum...");
                  nextNum = getNextNumFromDB(retryCount,siteLoc); //Get Next unique no from DB
                if (nextNum == null) {
                  System.out.println("Next Num was Null breaking loop..");
                  log("[pushAndRename()][Cannont perform rename and move since DB Cannot Generate No.Exiting..]");
                  break; //Break if cannot get Unique No from DB
                }

              }
              System.out.println("Next num is .."+nextNum);
                //Rename and Move at FTP Site.
                returnCode = renameAndMove(siteLoc,
                                           new File(fileName).getName(), pwd,
                                           nextNum);
                log("[FtpServiceFacade][pushAndRename()][After Rename&Move ReturnCode=]" +
                    returnCode);
                if (!_ftpClientManager.isPositiveCompletion()) {
                  log("[FtpServiceFacade][pushAndRename()][Retrying FTP Operation @]"+retryCount);
                  initilizeFtpManager(siteLoc);
                  retryCount -= 1;
                  continue;
                }
                else {
                  updateDB(nextNum, retryCount,siteLoc);
                  isPositiveCompletion = true;
                }
              }
              log(
                  "[FtpServiceFacade][pushAndRename()][Leave Synchronized block...]");
            } //End of Synchronized Block
          } //End of if rename
        }
        else { //else of isPositiveCompletion
          log("[pushAndRename()][Cannot Upload File to FTP Server !! Either Connection lost or put operation is Not Allowed]");
        }
      }
      else { //Since initilization it self takes care of retry
        log(
            "[FtpServiceFacade][pushAndRename][Could Not Connect to FTPServer.]");
      }

      log("[FtpServiceFacade][pushAndRename][End Successful]");
    }
    catch (Exception ex) {
      log("[FtpServiceFacade][pushAndRename][Could Not Perform FTP Operation.]");
      ex.printStackTrace();
    }
    finally {
      doFTPConnectionClose();
      log("Return Code = " + returnCode);
    }
    return returnCode;

  }

  private int renameAndMove(Site site, String fileName, String rootDir,
                            String nextNum)
  {
    //Properties Keys
    int returnCode = 0;
    try {
      String renamePrefix = site.getProperty(ISiteConfig.UPLOAD_RENAME_PREFIX);
      //String isMove = site.getProperty(ISiteConfig.IS_MOVE);

      //Remote Folders
      String remoteUploadFolder = site.getProperty(ISiteConfig.
          REMOTE_UPLOAD_FOLDER);
      String remoteMoveFolder = site.getProperty(ISiteConfig.REMOTE_MOVE_FOLDER);
      String newFileName = renamePrefix + nextNum;
      log("[renameAndMove][New File Name]" + newFileName);
      _ftpClientManager.setDirectory(rootDir);
      log("[renameAndMove][current directory]" + _ftpClientManager.getDirectory());
      log("[renameAndMove][RemoteUploadfolder]" + rootDir + "/" +
          remoteUploadFolder + "/" + fileName);
      log("[renameAndMove][RemoteMoveFolderfolder]" + rootDir + "/" +
          remoteMoveFolder + "/" + newFileName);
      _ftpClientManager.fileRename(rootDir + "/" + remoteUploadFolder + "/" +
                                   fileName,
                                   rootDir + "/" + remoteMoveFolder + "/" +
                                   newFileName);
      returnCode = getFTPReplyCode();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return returnCode;
  }

  public int pushToFTP(String siteName, String fileName) throws Exception
  {
    int returnCode = 1;
    //Get site, by given site name or if no site found, get Default Site.
    Site siteLoc = getSiteBySiteName(siteName);
    if (siteLoc == null) {
      log(
          "[FtpServiceFacade][pushToFTP][Cannot push as No Site was Configured]");
      return 1;
    }
    try {
      log("[FtpServiceFacade][pushToFTP][Begin]");
      initilizeFtpManager(siteLoc);
      if (_ftpClientManager != null) {
        if (fileName == null) {
          returnCode = uploadAndMove(siteLoc);
        }
        else {
          returnCode = pushFileToFtp(siteLoc, fileName);
        }
      }
      else {
        log("[FtpServiceFacade][pushToFTP][Could Not Connect to FTPServer.]");

      }
      log("[FtpServiceFacade][pushToFTP][End Successful]");
    }
    catch (Exception ex) {
      log("[FtpServiceFacade][pushToFTP][Could Not Perform FTP Operation.]");
      ex.printStackTrace();
    }
    finally {
      doFTPConnectionClose();
      log("Return Code = " + returnCode);
    }
    return returnCode;
  }

  /**
   * Gets Site from given Site Name, if no site found,
   * get the default site, for xml config file.
   * @param siteName SiteName identifier.
   * @return Site, for given identifier.
   */

  private Site getSiteBySiteName(String siteName)
  {
    Site siteLoc = null;
    if (_ftpSites == null || _ftpSites.isEmpty()) {
      return null;
    }
    siteLoc = (Site)_ftpSites.get(siteName);
    //Get the default Site, if site is null.
    if (siteLoc == null) {
      siteLoc = (Site)_ftpSites.get(ISiteConfig.DEFAULT_SITE);
    }
    return siteLoc;
  }

  /**
   * This method essentialy responsible to initilize the FTPManager,
   * given the site, which includes, checking if Proxy is used, the
   * proxy host & port to use,which provider to use.
   *
   * We initilize the Provider with host and port informatio, which dose
   * a connect to the FTP Server. Also dose a retry, when cannot connect.
   * @param siteLoc Location identifier, to make a connection.
   * @throws java.lang.Exception, when cannot initilize ftp manager.
   */

  private void initilizeFtpManager(Site siteLoc) throws Exception
  {
    String hostName = siteLoc.getProperty(ISiteConfig.FTP_HOST);
    String port = siteLoc.getProperty(ISiteConfig.FTP_PORT);
    String provider = (String)_ftpSites.get(ISiteConfig.PROVIDER);
    String userName = siteLoc.getProperty(ISiteConfig.USERNAME);
    String password = siteLoc.getProperty(ISiteConfig.PASSWORD);
    String retryCount = siteLoc.getProperty(ISiteConfig.RETRY_COUNT);
    String isProxy = siteLoc.getProperty(ISiteConfig.IS_PROXY);
    String isSocks = siteLoc.getProperty(ISiteConfig.IS_SOCKS);
    String proxyhostName = siteLoc.getProperty(ISiteConfig.FTP_PROXY_HOST);
    String proxyPort = siteLoc.getProperty(ISiteConfig.FTP_PROXY_PORT);
    String proxyUserName = siteLoc.getProperty(ISiteConfig.PROXY_USERNAME);
    String proxyPassword = siteLoc.getProperty(ISiteConfig.PROXY_PASSWORD);
    int count = Integer.parseInt(retryCount);
    do {
      try {
        if (Boolean.valueOf(isProxy).equals(Boolean.TRUE)) {
          // Check if SOCKS Protocol is used
          //Set Porxy Authenticator.
          Authenticator.setDefault(new PorxyAuthenticator(proxyUserName.trim(),
              proxyPassword.trim()));
          if (Boolean.valueOf(isSocks).equals(Boolean.TRUE)) {
            log("Setting Socks Proxy-----------");
            System.getProperties().put("socksProxyPort", proxyPort);
            System.getProperties().put("socksProxyHost", proxyhostName);
            //Fall back settings as Authenticator will take care.
            if (proxyUserName != null) {
              System.getProperties().put("socksProxyUserName", proxyUserName);
              proxyPassword = (proxyPassword == null) ? "" : proxyPassword;
              System.getProperties().put("socksProxyPassword", proxyPassword);
            }
            log("End Setting Socks Proxy--------");
          }
          else {
            log("Setting Proxy-----------");
            Properties props = new Properties();
            props.put("ftp.proxySet", "true");
            props.put("ftp.proxyHost", proxyhostName);
            props.put("ftp.proxyPort", proxyPort);
            System.getProperties().put("ftp.proxySet", "true");
            System.getProperties().put("ftp.proxyHost", proxyhostName);
            System.getProperties().put("ftp.proxyPort", proxyPort);
            //Fall back settings as Authenticator will take care.
            if (proxyUserName != null) {
              System.getProperties().put("ftp.proxyUserName", proxyUserName);
              proxyPassword = (proxyPassword == null) ? "" : proxyPassword;
              System.getProperties().put("ftp.proxyPassword", proxyPassword);
              props.put("ftp.proxyPassword", proxyPassword);
            }
            log("[System Properties--->]" + System.getProperties());
            log("End Setting Proxy--------");
          }
        }

        _ftpClientManager = FTPClientManagerFactory.getFTPClientManager(
            hostName,
            port, provider);
        _ftpClientManager.ftpConnect(userName, password);
        //_ftpClientManager.setPassiveModeTransfer(true);
        log("[initilizeFtpManager()][Connected ...]" + getFTPReplyCode());
        break;
      }
      catch (FTPServiceException ex) {
        ex.printStackTrace();
        log("[FTPServiceFacade][initilizeFtpManager()][Retry-->]" + count);
      }
      catch (Exception ex) {
        _ftpClientManager = null;
        ex.printStackTrace();
        break;
      }
    }
    while (--count > 0);
  }

  /**
   * Dose the actual upload, by setting the destination directory, if set in
   * the config file.
   *
   * @param siteLoc Location Identifier.
   * @param fileName FileName to Upload.
   * @return returns FTP Return code.
   * @throws FTPException thrown when cannot perform FTP Operation.
   * @throws java.lang.Exception thrown when cannot perform FTP Operation.
   */

  private int pushFileToFtp(Site siteLoc, String fileName) throws FTPException,
      Exception
  {
    int returnCode = 1;
    File fileToSend = new File(fileName);
    if (fileToSend.exists()) {
      String remoteFolder = siteLoc.getProperty(ISiteConfig.
                                                REMOTE_UPLOAD_FOLDER);
      if (remoteFolder != null && !remoteFolder.trim().equals("")) {
        _ftpClientManager.setDirectory(remoteFolder);
      }
      log("[pushFileToFtp()][Directory on FtpServer]" +
          _ftpClientManager.getDirectory());
      log("[pushFileToFtp()][Reply Code after Setting Directory]" +
          _ftpClientManager.getReply());
      BufferedInputStream inStream = new BufferedInputStream(new
          FileInputStream(fileToSend));
      log("[pushFileToFtp()][Start push operation ....]");
      _ftpClientManager.putAsciiFile(inStream,
                                     fileToSend.getName());
      log("[pushFileToFtp()][End push operation ....]");
      returnCode = getFTPReplyCode();
      log("[pushFileToFtp()][Return Code after push operation ....]" +
          returnCode);
      inStream.close();
      fileToSend = null;
    }
    else {
      log("[FTPServiceFacade][pushFileToFtp()][File DoseNot Exists]" +
          fileToSend.getAbsolutePath());
    }
    return returnCode;
  }

  /**
   * Returns a FTP Reply code.
   * @return FTP Reply code.
   * @throws FTPException
   */
  private int getFTPReplyCode() throws FTPException
  {
    return Integer.parseInt(_ftpClientManager.getReply());
  }

  /**
   * Closes a FTP Connection.
   * @throws FTPException when cannot perform FTP Operation.
   * @throws IOException when cannot perform FTP Operation.
   */

  private void doFTPConnectionClose() throws FTPException, IOException
  {
    if (_ftpClientManager != null) {
      _ftpClientManager.close();
    }
  }

  /**
   * This method pulls or gets files from FTP Server. It checks for all
   * sites whose ispull is set to true. This values can be set from
   * ftpservice.xml config file.
   *
   * Support for Proxy server is enabled. SOCKS is not tested,since there
   * were no opensource software, for testing.
   *
   * @throws java.lang.Exception, thrown when cannot perform FTP Operation.
   */

  public void pullFromFTP() throws Exception
  {
    Site[] sitesToPull = getSitesByKeyValue(ISiteConfig.IS_PULL, Boolean.TRUE);
    log("Sites to Get From" + sitesToPull.length);
    //Iterate each site, and get files from each server.
    for (int i = 0; i < sitesToPull.length; i++) {
      pullFilesFromFTP(sitesToPull[i]);
    }

  }

  /**
   * Get files form the site identifier.
   * @param site Location Identifier.
   * @throws FTPException , thrown when cannot perform FTP Operation.
   * @throws java.lang.Exception, thrown when cannot perform FTP Operation.
   */

  private void pullFilesFromFTP(Site site) throws FTPException, Exception
  {
    //int returnCode = 1;
    try {
      initilizeFtpManager(site);
      if (_ftpClientManager != null) {
        String remoteDownloadFolder = site.getProperty(ISiteConfig.
            REMOTE_DOWNLOAD_FOLDER);
        String localDownloadFolder = site.getProperty(ISiteConfig.
            LOCAL_DOWNLOAD_FOLDER);
        String isMove = site.getProperty(ISiteConfig.IS_MOVE);
        String remoteMoveFolder = site.getProperty(ISiteConfig.
            REMOTE_MOVE_FOLDER);
        log("[pullFilesFromFTP()][Remote download Folder]" +
            remoteDownloadFolder);
        String rootDirectory = _ftpClientManager.getDirectory();
        log("[pullFilesFromFTP()][FTP Server Root Folder]" + rootDirectory);
        _ftpClientManager.setDirectory(remoteDownloadFolder);
        log(
            "[pullFilesFromFTP()][Set FTP Server To Remote Download Folder Response...]" +
            getFTPReplyCode());

        FTPFile[] files = _ftpClientManager.listFiles(remoteDownloadFolder);
        System.out.println("In Directory " + _ftpClientManager.getDirectory());
        if (files != null) {
          System.out.println("[pullFilesFromFTP()][No of Files at Server]" +
                             files.length);
          for (int i = 0; i < files.length; i++) {
            String remoteFileName = files[i].getName();
            if (!files[i].isDirectory()) {
              log("[pullFilesFromFTP()][Writing file..]" + remoteFileName);
              _ftpClientManager.getAsciiFile(remoteFileName,
                                             localDownloadFolder +
                                             File.separator + remoteFileName);
              log("[pullFilesFromFTP()][After writing file..]" +
                  getFTPReplyCode());
            }
          }
          if (Boolean.valueOf(isMove).equals(Boolean.TRUE)) {
            FTPFile[] filesm = _ftpClientManager.listFiles(remoteDownloadFolder);
            _ftpClientManager.setDirectory(rootDirectory);
            for (int i = 0; i < files.length; i++) {
              String remoteFileName = filesm[i].getName();
              if (!filesm[i].isDirectory()) {
                log("[pullFilesFromFTP()][Moving file]" + remoteFileName);
                _ftpClientManager.fileRename(remoteDownloadFolder + "/" +
                                             remoteFileName,
                                             remoteMoveFolder + "/" +
                                             remoteFileName);
                log("[pullFilesFromFTP()][After Moving file..]" +
                    getFTPReplyCode());
              }
            }
          }

        }
        else {
          log("[FTPServiceFacade][pullFilesFromFTP][No File Available At Remote Download Folder]");
        }
      }
      else {
        log("[FtpServiceFacade][pushToFTP][Could Not Connect to FTPServer.]");
      }
    }
    catch (Exception ex) {
      log("[FtpServiceFacade][pushToFTP][Could Not Perform FTP Operation.]");
      ex.printStackTrace();
    }
    finally {
      doFTPConnectionClose();
    }
  }

  /**
   * Gets a site by key value pair.
   * @param key A Key
   * @param value Value is true or false.
   * @return a array of Site Objects.
   */

  private Site[] getSitesByKeyValue(String key, Boolean value)
  {
    List sitesToPull = new ArrayList();
    Set keySet = _ftpSites.keySet();
    Iterator keyIterator = keySet.iterator();
    while (keyIterator.hasNext()) {
      String keyName = (String)keyIterator.next();
      Object siteObj = _ftpSites.get(keyName);
      if (siteObj instanceof Site) {
        Site site = (Site)siteObj;
        String isPull = site.getProperty(key);
        if (Boolean.valueOf(isPull).equals(value)) {
          sitesToPull.add(site);
        }
      }
    }
    return (Site[])sitesToPull.toArray(new Site[] {});
  }

  /**
   * Main method which defaults to pull from FTP servers, as configured in
   * ftpservice.xml file.
   * @param args Arguments
   * @throws java.lang.Exception.
   */

  public static void main(String[] args) throws Exception
  {
    //for (int i=0;i<args.length;i++)
    /* String methodToInvoke[] = {"pushtoftp","pullfromftp"};
     if (args.length>=1)
     {
       if (args[0] != null)
       {
         if (args[0].equalsIgnoreCase(methodToInvoke[0]))
         {
           String fileName = null;
           if (args.length>=2)
           {
             fileName = args[1];
             System.out.println("FileName="+fileName);
             FTPServiceFacade facade = new FTPServiceFacade();
             System.out.println("Return value="+facade.pushToFTP(fileName));
           }
           else
           {
             System.out.println("FileName="+fileName);
             FTPServiceFacade facade = new FTPServiceFacade();
             System.out.println("Return value="+facade.pushToFTP(fileName));
           }
         }
         else if (args[0].equalsIgnoreCase(methodToInvoke[1]))
         {
           new FTPServiceFacade().pullFromFTP();
         }
       }
       else
       {
         System.out.println("Usage : <<pushtoftp  FullpathofFileName>> or \n <<pullfromftp>>");
       }
     }
     else
     {
       System.out.println("Usage : <<pushtoftp  FullpathofFileName>> or \n <<pullfromftp>>");
     }
     */

    Thread t = new Thread(new Runnable()
     	{
               public void run()
               {
                 try
                 {
                   new FTPServiceFacade().pushAndRename("c:/winzip.log");
                 }catch(Exception ex)
                 {
                   ex.printStackTrace();
                 }
               }
        }
        );


      Thread t1 = new Thread(new Runnable()
                {
                  public void run()
                  {
                    try
                    {
                      new FTPServiceFacade().pushAndRename("SG","c:/PatchMakerlog.txt");
                    }catch(Exception ex)
                    {
                      ex.printStackTrace();
                    }
                  }
           }
           );


           Thread t2 = new Thread(new Runnable()
                     {
                       public void run()
                       {
                         try
                         {
                           new FTPServiceFacade().pushAndRename("SG","c:/convertor.log");
                         }catch(Exception ex)
                         {
                           ex.printStackTrace();
                         }
                       }
                }
                );

      t.start();
   t1.start();
   t2.start();


/*FTPServiceFacade.initilizeSite();
Site site  = (Site) _ftpSites.get(ISiteConfig.DEFAULT_SITE);
System.out.println("Select Query ---\n"+site.getProperty(ISiteConfig.DATABASE_SELECT_QUERY));
*/

// System.out.println("ClassName="+_log.getClass());
 //  _log.debug("My Message");
  }

  /**
   * Log's the message, to System out.
   * @param message Message to Log
   */
  private void log(String message)
  {
    if (_debug) {
      System.out.println(message);
    }

  }

  private String getNextNumFromDB(int retryCount,Site siteLoc)
  {
    String nextNum = null;
    while (retryCount > 0) {
      try {
        System.out.println("Calling DBUIDGenerator for getting Next Num...");
        nextNum = DBUIDGenerator.getNextRefNum(siteLoc);
        System.out.println("Next num at DB..."+nextNum);
        break;
      }
      catch (NestingException ex) {
        ex.printStackTrace();
        log("[renameAndMove][DataBase Operation Failed...Retrying @]" +
            retryCount);
        retryCount -= 1;
      }
    }
    return nextNum;
  }

  private void updateDB(String nextNum, int retryCount,Site siteLoc)
  {
    while (retryCount > 0) {
      try {
        log("[updateDB()][Updating DB with NextNum=" + nextNum + "]");
        DBUIDGenerator.updateRefNum(Long.parseLong(nextNum),siteLoc);
        break;
      }
      catch (NestingException ex) {
        ex.printStackTrace();
        log("[updateDB][DataBase Operation Failed...Retrying @]" + retryCount);
        retryCount -= 1;
      }
    }

  }

  private int uploadAndMove(Site siteLoc) throws Exception
  {
    System.out.println("In upload and move");
    int returnCode = 1;
    String localUploadFolder = siteLoc.getProperty(ISiteConfig.
        LOCAL_UPLOAD_FOLDER);
    String localMoveFolder = siteLoc.getProperty(ISiteConfig.
                                                 LOCAL_UPLOAD_MOVE_FOLDER);
    File localFolderToUpload = new File(localUploadFolder);
    if (localFolderToUpload.isDirectory()) {
      File[] files = localFolderToUpload.listFiles();
      for (int i = 0; i < files.length; i++) {
        if (!files[i].isDirectory()) {
          log("[uploadAndMove()][Upload file -- ]" + files[i].getAbsolutePath());
          returnCode = pushFileToFtp(siteLoc, files[i].getAbsolutePath());
        }
      }
      if (new File(localMoveFolder).isDirectory()) {
        File dir = new File(localMoveFolder);
        File[] filesx = localFolderToUpload.listFiles();
        for (int i = 0; i < filesx.length; i++) {
          //String name = filesx[i].getName();
          if (!filesx[i].isDirectory()) {
            log("[Moving Uploded File --]" + localMoveFolder);
            log(" Has Written " +
                files[i].renameTo(new File(dir, filesx[i].getName())));
          }
        }

      }
      else {
        log(
            "[pushToFTP()][LocalMove Folder is Not A Folder.Or Dose Not Exists]");

      }
    }
    else {
      log("[pushToFTP()][LocalMove Folder is Not A Folder..Or Dose Not Exists]");
    }
    return returnCode;
  }

}

class PorxyAuthenticator
    extends Authenticator
{
  String _userName = null;
  String _password = null;

  PorxyAuthenticator(String userName, String password)
  {
    this._userName = userName;
    this._password = password;
  }

  protected PasswordAuthentication getPasswordAuthentication()
  {
    System.out.println("Authenticator initilized");
    return new PasswordAuthentication(_userName, _password.toCharArray());
  }

}