----------------------------------------------------------------------------

Patch Instructions
******************

1. unzip the release zip file to some temporary folder.

2. ensure that the env.properties is customised according to you applications setup 
   (applicable only for 2.0 patches)

3. run "patch.bat".
   This will patch the application libraries archives, database, configuration and data files, 
   including the application server configuration files

************************************
* Id: gtas-2.1.21-patch
* Version before patch : GT 2.1.20
* Version after patch  : GT 2.1.21
* Release Date : 29th April 2004
************************************

1. New Features

2. Enhancements
   
3. Bug-fixes
   1. GNDB00021906 - Unable to read delivery headers for message with namespace
      The problem happens for RNIF2 and applies to RN_ACK (Non-repudiation enabled), preamble, 
      delivery header or service header with namespace (xmlns="http://www.rosettanet.org/RNIF/V02.00") 
      specified in the root element.

4. Additional


************************************
* Id: gtas-2.1.20-patch
* Version before patch : GT 2.1.19
* Version after patch  : GT 2.1.20
* Release Date : 07th April 2004
************************************

1. New Features
   1. Certificate Replacement
      This enables smooth transition of the certificate replacement without shutting down GridTalk.
      A new certificate could be imported as a pending certificate for an existing certificate. On the expiry
      of the existing certificate, the pending certificate will replace it automatically for processing.
      No change is required to the certificate mappings when the replacement takes place.
      Note that the pending certificate may still need to be manually exported to Trust/Key Store beforehand 
      for HTTPS.

2. Enhancements
   1. GNDB00017514 - Index Workflow tables for better performance
   
3. Bug-fixes
   1. GNDB00017554 - Wrong timestamp in deliveryheader (RNIF2.0) and preamble (RNIF1.1)

4. Additional


************************************
* Id: gtas-2.1.19-patch
* Version before patch : GT 2.1.18
* Version after patch  : GT 2.1.19
* Release Date : 26th February 2004
************************************

1. New Features

2. Enhancements
   
3. Bug-fixes
   1. GNDB00017273 - Deadlock Exception while exporting the documents
   2. GNDB00017253 - MMI GridTalk Security Issue
      Access control is now re-enabled with additional features open for control, e.g. business entity management, 
      process definition and process instance management, gridnode activations, system functions, etc.

      -- Duplicate --
      GNDB00016106 - Users with Role "User" can modify Administrator's password
 
4. Additional


************************************
* Id: gtas-2.1.18-patch
* Version before patch : GT 2.1.17
* Version after patch  : GT 2.1.18
* Release Date : 07th January 2004
************************************

1. New Features

2. Enhancements
   
3. Bug-fixes
   1. GNDB00016716 - GT2.1.17 - Error in sending files over a HTTPs channel
   2. GNDB00016720 - Inbound RosettaNet messages unintentionally modified by Transport module
 
4. Additional


************************************
* Id: gtas-2.1.17-patch
* Version before patch : GT 2.1.16
* Version after patch  : GT 2.1.17
* Release Date : 30th December 2003
************************************

1. New Features

2. Enhancements
   
3. Bug-fixes
   1. GNDB00016522 - NT service fails to load - No ClassLoaders found for: org.jboss.system.server.ServerInfo
 
4. Additional
   1. The standard re-direct port for GridTalk client is changed from 8007 to 9099.
   2. [Jboss] mysql-ds.xml settings is tweaked to enhance database connection pooling performance.
   3. [Jboss] Factory name for SSL/TLS Connector is corrected to "org.apache.catalina.net.SSLServerSocketFactory"


************************************
* Id: gtas-2.1.16-patch
* Version before patch : GT 2.1.15
* Version after patch  : GT 2.1.16
* Release Date : 15th December 2003
************************************

1. New Features

2. Enhancements
   
3. Bug-fixes
   1. GNDB00016432 - ProcessInstanceId related fields in RN Profile got truncated :- 
                     ProcessInstanceId, PIPInstanceIdentifier, ProcessTransactionId, ProcessActionId
   2. GNDB00016444 - Wrong timestamp file extension value appended during export
   3. GNDB00016467 - Login or Update of some entities fail due to JMS connection failure
   4. GNDB00014860 - Attachment export folder not unique
   5. GNDB00016509 - Concurrent Requests are not handled properly in BPSS module
 
4. Additional
   1. JBoss application server is upgraded to version 3.2.2 to resolve some of the JBossMQ 
      unstability issues. This patch assumes that the JBoss 3.2.2 is already installed under 
      folder "<GRIDTALK_HOME>/jboss-3.2.2".

************************************
* Id: gtas-2.1.15-patch
* Version before patch : GT 2.1.14
* Version after patch  : GT 2.1.15
* Release Date : 17th November 2003
************************************

1. New Features

2. Enhancements
   
3. Bug-fixes
   1. GNDB00015471 - Backend Listener log files grows too big in size
   2. GNDB00015623 - Default Partner Function SQL script contains incorrect BLOB data
   3. GNDB00016152 - GridDocument filename not updated when reached BPSS module

4. Additional
   1. Set of RosettaNet Schemas are updated to remove PIP enumeration entries. This will prevent the schema validation
      from failing due to missing PIP entry in ServiceHeader schema.
   2. Updated Backend receiver.jar (for Bug-fix GNDB00015471). 
      Please manually copy from backend\receive\receiver.jar.
      
************************************
* Id: gtas-2.1.14-patch
* Version before patch : GT 2.1.13
* Version after patch  : GT 2.1.14
* Release Date : 29th September 2003
************************************

1. New Features

2. Enhancements
   
3. Bug-fixes
   1. GNDB00015391 - Paging response slowed to a crawl when processing large result sets
   2. GNDB00015395 - Paging navigation counters spans across the screen
   3. GNDB00015381 - No limit imposed on concurrent export execution resulting possibly data loss
   4. GNDB00015270 - Sender/Senderlite encounters connection error if too many instances are run 
                     concurrently
   5. GNDB00015430 - fbstatus.bat not able to work on WinNT
   6. GNDB00015437 - Unable to acquire Database Connection for data retrieval

4. Additional
   1. Backend Listener now allows for additional optional param as follow:
      "-T#" where # denotes the number of concurrent processing allowed, optional, default is 10 if 
      not specified

************************************
* Id: gtas-2.1.13-patch
* Version before patch : GT 2.1.12
* Version after patch  : GT 2.1.13
* Release Date : 19th September 2003
************************************

1. New Features

2. Enhancements
   1.  GNDB00015064 - Enable documents to flow from INBOUND to OUTBOUND
   
3. Bug-fixes
   1.  GNDB00015196 - Date format file extension value for Port not stored correctly
   2.  GNDB00015093 - Fail to add 'InResponseToActionID' column to database, 
                      hence affecting RNIF 1.1 functionality
   3.  GNDB00014982 - FileAccessException on importing documents when using different cases of User ID
   4.  GNDB00014979 - Unable to import configurations with embedded entities
   5.  GNDB00014938 - XSL cache not cleared after create/update of XSL mapping file
   6.  GNDB00014940 - Failure in Partner Function execution when using workflow activities that was deleted 
                      and re-ceated with the same name
   7.  GNDB00014920 - Fail to handle schema with namespaces
   8.  GNDB00014912 - Rendering error when viewing PROCESS_INSTANCE_FAILURE alert
   9.  GNDB00015162 - Fail to handle UTF-16 encoded message
   10. GNDB00015009 - No prompting for security password on application startup
   11. GNDB00014860 - Non-unique attachment export folder names
   12. GNDB00014728 - Unable to send RN messages with attachment files
   13. GNDB00014712 - Error occurs randomly when deleting documents from outbound folder
   14. GNDB00015255 - Transaction rolled back due to timeout on long executing process, 
                      eg. performing splitting on large message

4. Additional


***********************************
* Id: gtas-2.1.12-patch
* Version before patch : GT 2.1.11
* Version after patch  : GT 2.1.12
* Release Date : 12th August 2003
************************************

1. New Features
   1. GridForm Editor/Viewer
   GridForm is now integrated with GridTalk to allow user to view/edit XML documents 
   in GridForm.

2. Enhancements
   1. GNDB00014880 - Enable GT to be deployed as NT service
   
3. Bug-fixes
   1. GNDB00014879 - Unable to load data files with spaces in the file paths (name inclusive)
   2. GNDB00014869 - Non-repudiation failure due to incorrect message digest generation algorithm

4. Additional


***********************************
* Id: gtas-2.1.11-patch
* Version before patch : GT 2.1.10
* Version after patch  : GT 2.1.11
* Release Date : 4th August 2003
************************************

1. New Features

2. Enhancements
         
3. Bug-fixes
   1. GNDB00014612 - Error occurs while importing document without file extension
   2. GNDB00014753 - Simultaneous login on fresh startup fails when getting registration info
   3. GNDB00014797 - CommInfo Concurrent Entity modification error after Approve Activation 
                     when update partner online

4. Additional


***********************************
* Id: gtas-2.1.10-patch
* Version before patch : GT 2.1.9
* Version after patch  : GT 2.1.10
* Release Date : 31th July 2003
************************************

1. New Features

2. Enhancements
         
3. Bug-fixes
  
4. Additional
   1. Rosettanet schema files were updated to remove all references to "tns:"
   2. Custom patch was provided to patch all existing conversion rules and xsl to be compatible 
      with new conversion engine

***********************************
* Id: gtas-2.1.9-patch
* Version before patch : GT 2.1.8
* Version after patch  : GT 2.1.9
* Release Date : 29th July 2003
************************************

1. New Features

2. Enhancements
   1. GNDB00014657 - Enable configurable destination folder in FTP UserProcedure.
   2. GNDB00014626 - Enable canonicalization for RNIF2.0
   3. GNDB00014625 - Enable support for RNIF 1.1
         
3. Bug-fixes
   1. GNDB00014732 - Login issue with IE 6.0.
   2. GNDB00014730 - Unable to perform manual send
   3. GNDB00014701 - Missing classes in backend archives sender.jar and receiver.jar

4. Additional


***********************************
* Id: gtas-2.1.8-patch
* Version before patch : GT 2.1.7
* Version after patch  : GT 2.1.8
* Release Date : 7th July 2003
************************************

1. New Features

2. Enhancements
   1. GNDB00014226 - UI Enhancements to enhance visual aspect of login page, navigation tabs 
                     and navigation trees, etc.
   2. GNDB00014372 - Upgrade conversion engine to handle XML-DB mapping
   3. GNDB00014475 - Enhance Certificate Management to handle certificates export to keystore 
                     and truststore
         
3. Bug-fixes
   1. GNDB00014171 - Document Exported alert with 2 email actions
   2. GNDB00014458 - Import of process definition did not deploy Bpss
   3. GNDB00014308 - Temp files deleted before Partner Function complete
   4. GNDB00014398 - Error on import configuration - deletion of files failed at the backend
   5. GNDB00014412 - Duplicate documents is allowed when restore archive file

4. Additional
   1. 3rd Party Libraries 
      1.1 Upgraded
	from		       to
	============================================
	struts-1.1-b1.jar    - struts-1.1.jar
	jdom-b8.jar          - jdom-b9.jar
        dom4j.jar            - dom4j-core-1.4.jar
        jaxenjar             - jaxen-full-1.0.jar      
	jaxen-core.jar       - jaxen-full-1.0.jar   
	jaxen-jdom.jar       - jaxen-full-1.0.jar   
	msv.jar		     - msv-20020414.jar
	xerces-1.4.2         - xercesImpl-2.4.0.jar
	saxon.jar            - saxon-6.5.2.jar
	bcmail-jdk13-118.jar - bcmail-jdk14-118.jar
	bcprov-jdk13-118.jar - bcprov-jdk14-118.jar

      1.2 Added
        xmlParserAPIs.jar, poi-2.0.jar

      1.3 Removed
        excelaccessor_Runtime_2.2.jar, saxpath-1.0-FCS.jar


***********************************
* Id: gtas-2.1.7-patch
* Version before patch : GT 2.1.6
* Version after patch  : GT 2.1.7
* Release Date : 27th June 2003
************************************

1. New Features

2. Enhancements
   1. GNDB00014354 - GridDocument status feedback to XB via raising a Document Status Update Alert
         
3. Bug-fixes 
   1. GNDB00014386 - Error in sql scripts causes alert entries in DB link to non-existing alert category
   2. GNDB00014335 - Wrong links still shown in Connect To GM UI
   3. GNDB00014334 - Duplicated dropdown list items in archive documents
   4. GNDB00014337 - Rendering error on viewing a user procedure

4. Additional
   1. modified embedded tomcat service descriptor to : 
      a. include AJP13 connector 
      b. use port 443 for HTTPS and
      c. reference keystore file directly under catalina folder

***********************************
* Id: gtas-2.1.6-patch
* Version before patch : GT 2.1.5
* Version after patch  : GT 2.1.6
* Release Date : 16th June 2003
************************************

1. New Features
   Export/Import Configurations
   Ease configuration by enabling exporting and importing of the following listed configurations:
   alerts, alert actions, alert triggers, message template, document type, file type, mapping rules,
   mapping files, partners, partner group, partner type, partner function, port, rfc, user procedure,
   procedure definition file, process definition, process mapping, roles, process triggers, 
   response track records
    	
2. Enhancements
   1. GNDB00014292 - To enhance UI to render two text fields for Between Operator when 
                     configuring the Procedure Return Condition.
   2. GNDB00014291 - Link to create Alert while creating Procedure return conditions.
   3. GNDB00014289 - To enable FTP URL param configuration for FTP UserProcedure.
   4. GNDB00014290 - Alert Field in Procedure Return Condition need not be mandatory.

3. Bug-fixes 
   1. GNDB00014278 - Alert trigger Connect to GridMaster is still raised after disabling
   2. GNDB00014194 - Classcast exception when restoring documents
   3. GNDB00014285 - Import from UI generate dispatich exception
   4. GNDB00014127 - Some irrelevant fields in GridDocument appear as selectable in drop-down list
 
4. Additional
   1. added HTTPS as standard connector in embedded tomcat service descriptor 
   2. added additional PIPs entries in service header schema

***********************************
* Id: gtas-2.1.5-patch
* Version before patch : GT 2.1.4
* Version after patch  : GT 2.1.5
* Release Date : 9th June 2003
************************************

1. New Features
    
    	
2. Enhancements
   

3. Bug-fixes 
   1. GNDB00014169 - Alert Trigger disabled but Alert still raised
   2. GNDB00014223 - GTAS Signed Message cannot be verified by RN Tool kit.
 
4. Additional
  


***********************************
* Id: gtas-2.1.4-patch
* Version before patch : GT 2.1.3
* Version after patch  : GT 2.1.4
* Release Date : 6th June 2003
************************************

1. New Features
    
    	
2. Enhancements
   

3. Bug-fixes 
   1. GNDB00013986 - GridTalk 2.0 is seen at the top right hand corner of browser
 
4. Additional
   1. Upgraded to use CertJ 2.1 libraries. 
   2. URL changes for the following :
      a. Login Page : http://<host>:<port>/gridtalk
      b. Rosettanet Inbound Channel : http://<host>:<port>/gridtalk/b2bi/rosettanet


***********************************
* Id: gtas-2.1.3-patch
* Version before patch : GT 2.1.2
* Version after patch  : GT 2.1.3
* Release Date : 5th June 2003
************************************

1. New Features
    
    	
2. Enhancements
   

3. Bug-fixes 
   GNDB00013996 - Login: Account with password "user1" can login using password "user2"
   GNDB00014070 - UI reflects license is valid but license has expired (from build 2.0.32)
   GNDB00014100 - Send not allowed after restart GTAS (from build 2.0.32)

 
4. Additional
   1. User Procedure for FTP Backend Integration
      A procedure definition is provided by default for FTP. 
      

***********************************
* Id: gtas-2.1.2-patch
* Version before patch : GT 2.1.1
* Version after patch  : GT 2.1.2
* Release Date : 30th May 2003
************************************

1. New Features
    
    	
2. Enhancements
   GNDB00013903 - Port Enhancement to support appending of running number for file name extension. 
                  (from build 2.0.31)
   

3. Bug-fixes 
   GNDB00013989 - Transaction rollback exceptions when sending documents between two GridTalk partners
   GNDB00013988 - File processing error occurs when receiving RN message

 
4. Additional


***********************************
* Id: gtas-2.1.1-patch
* Version before patch : GT 2.1.0
* Version after patch  : GT 2.1.1
* Release Date : 28th May 2003
************************************

1. New Features
   1. Documents Archive & Restore (also available in GT 1.x)      
      Allows user to perform documents archiving by defining archiving criteria in terms of the 
      business document meta-info in GridDocument.   
    	
2. Enhancements
   GNDB00013987 - Enhance RN processing to handle MIME boundary without blank line


3. Bug-fixes  

 
4. Additional


***********************************
* Id: gtas-2.1.0-patch
* Version before patch : -
* Version after patch  : GT 2.1.0
* Release Date : 26th May 2003
************************************

1. New Features
   1. Alert Notification Center (also available in GT 1.x)      
      Built in alert notification based on system events or user defined alerts. Alerts are associated with 
      multiple actions. Possible actions are sending emails and logging to a log file. All actions are assigned
      a configurable message template. The contents in the message template can comprise of free form text as well
      as object field reference tags.   
    	
2. Enhancements


3. Bug-fixes 

 
4. Additional


***********************************
* Id: gtas-2.0.32-patch
* Version before patch : GT 2.0.31
* Version after patch  : GT 2.0.32
* Release Date : 5th June 2003
************************************

1. New Features


2. Enhancements
   
   
3. Bug-fixes
   GNDB00014071 - Registration.properties file is missing after running patch.bat
   GNDB00014070 - UI reflects license is valid but license has expired
   GNDB00014100 - Send not allowed after restart GTAS
 
4. Additional
   1. Existing registration.properties may be overwritten after applying the patch. In order remedy the 
      "missing registration.properties" problem after applying gtas-2.0.27-patch. This patch will
      ensure that the registration.properties is copied for builds 2.0.27 onwards.
      Please backup any existing registration.properties if customization has been done on it.
      

***********************************
* Id: gtas-2.0.31-patch
* Version before patch : GT 2.0.30
* Version after patch  : GT 2.0.31
* Release Date : 30th May 2003
************************************

1. New Features


2. Enhancements
   GNDB00013903 - Port Enhancement to support appending of running number for file name extension.
   
3. Bug-fixes
 
 
4. Additional
   
      

***********************************
* Id: gtas-2.0.30-patch
* Version before patch : GT 2.0.29
* Version after patch  : GT 2.0.30
* Release Date : 16th May 2003
************************************

1. New Features


2. Enhancements

   
3. Bug-fixes

   1. GNDB00013787 - IsRequest field cannot be edited in Trigger

 
4. Additional
   
   1. GNDB00013799 - Disable NodeLock Licensing.
   2. Increase Transaction timeout in jboss-service.xml from default 300 to 900
     


***********************************
* Id: gtas-2.0.29-patch
* Version before patch : GT 2.0.28
* Version after patch  : GT 2.0.29
* Release Date : 5th May 2003
************************************

1. New Features


2. Enhancements


3. Bug-fixes

   1. GNDB00013713 - Registered GridTalk using old license scheme should not be prompted to register again

 
4. Additional


***********************************
* Id: gtas-2.0.28-patch
* Version before patch : GT 2.0.27
* Version after patch  : GT 2.0.28
* Release Date : 29th Apr 2003
************************************

1. New Features


2. Enhancements
   1. GNDB00013697 - Enhance Port configuration to allow more date/timestamp options for file name extension

          
3. Bug-fixes

   1. GNDB00013655 - Unable to approve incoming activation request
   2. GNDB00013560 - cannot send RosettaNet messages using backend senderlite.jar
   3. GNDB00013440 - Temp files in GTAS JBoss are not cleared after use
   4. GNDB00013386 - Specifying exporting without a filename will export using only the unpackage 
                  filename despite having a file extension
   5. GNDB00013377 - After creating a XPATH Mapping file, the mapping is not entered into the database

 
4. Additional
   LogFactor5 graphical log viewer is now disabled by default. To enable the usage of LogFactor5,
   uncomment the following:
   #log4j.appender.LF=org.apache.log4j.lf5.LF5Appender
   #log4j.appender.LF.MaxNumberOfRecords=1000


***********************************
* Id: gtas-2.0.27-patch
* Version before patch : GT 2.0.26
* Version after patch  : GT 2.0.27
* Release Date : 23rd Apr 2003
************************************

1. New Features

   1. GNDB00013005 - Node-Lock License for GTAS
   2. GNDB00012409 - Upload/Download Documents

2. Enhancements
          
3. Bug-fixes

   1. GNDB00013416 - Deleting of process instance does not remove the process instance in GUI
   2. GNDB00013460 - Error when viewing PartnerGroup and then clicking on edit
   3. GNDB00013480 - GUI- lb errors in Create Trigger screen
   4. GNDB00013486 - Able to create a procedure definition file with a non valid definition file
   5. GNDB00013493 - Wrong format of error message when saving a process definition with invalid name
   6. GNDB00013375 - ClassCastException when attaching files from backend and manual import

4. Additional

   The license scheme has been changed to node-locked. All existing and new users shall be required 
   to request for the node-locked license from GridNode Pte Ltd. A small utility is provided for generating
   the license request file. 

***********************************
* Id: gtas-2.0.26-patch
* Version before patch : GT 2.0.25
* Version after patch  : GT 2.0.26
* Release Date : 10th Apr 2003
************************************

1. New Features


2. Enhancements
       
   1. GNDB00013448 - Integrate LogFactor5 for use as the default graphical log viewer   
   2. GNDB00013506 - Initialise to startup backend listener service on application startup
   
3. Bug-fixes

   1. GNDB00013429 - Opening of a child window and then logging out from the parent window 
                     does not close the child window.
   2. GNDB00013428 - Clicking on viewing and then editing of certain entity types shows 
                     wrong or missing values
   3. GNDB00013430 - Closing of the GTAS window before the child window completes loading 
                     will cause the child window to continue processing
   4. GNDB00013427 - Wrong format of values shown on the GUI (shows lb in front)

   5. GNDB00013321 - No initializing for the triggering of alarms during startup

4. Additional

***********************************
* Id: gtas-2.0.25-patch
* Version before patch : GT 2.0.24
* Version after patch  : GT 2.0.25
* Release Date : 04th Apr 2003
************************************

1. New Features


2. Enhancements
       
   1. GNDB00013447 - Provide custom appender to zip up log files   
   2. GNDB00013390 - SignOn/SignOff servlets URL references configurable from configuration file
  
3. Bug-fixes

   1. GNDB00013139 - Save connection setup gives error
   2. GNDB00013325 - iCalAlarm not removed after reminder alert has been raised
   3. GNDB00013324 - date not rolled correctly across month for reminder alarms
   4. GNDB00013414 - Unable to view next page in display from GUI
   5. GNDB00013415 - _refreshUrl is undefined when clicking on next page in GUI
   6. GNDB00013367 - Viewing Java Procedure Configuration shows invalid "Execute within Application VM"

4. Additional



***********************************
* Id: gtas-2.0.24-patch
* Version before patch : GT 2.0.23
* Version after patch  : GT 2.0.24
* Release Date : 02nd Apr 2003
************************************

1. New Features


2. Enhancements
       
   Enhanced UI with new look & feel and better usability    

  
3. Bug-fixes

   1. GNDB00013312 - Temp files are not removed after sending of files, causing the temp folder to be overloaded
   

4. Additional
   

***********************************
* Id: gtas-2.0.23-patch
* Version before patch : GT 2.0.22
* Version after patch  : GT 2.0.23
* Release Date : 25th Mar 2003
************************************

1. New Features


2. Enhancements
       

  
3. Bug-fixes

   1. GNDB00013273 - Sending of files through backend without partner id causes import to fail
   

4. Additional
   This release is made compatible with jboss 3.0.5.

***********************************
* Id: gtas-2.0.22-patch
* Version before patch : GT 2.0.21
* Version after patch  : GT 2.0.22
* Release Date : 18th Mar 2003
************************************

1. New Features


2. Enhancements
   
   1. Workflow engine optimization to reduce asynchronous JMS activities
  
3. Bug-fixes

   1. GNDB00013245 - The Document type in Process Mappings could not be updated for 2 action processes
   2. GNDB00013258 - Convertor temp files causes problems if too many concurrent conversion going on
   3. GNDB00013265 - Process Instance for single action pips not completed after receiving acknowledgement
   

4. Additional


***********************************
* Id: gtas-2.0.20-patch
* Version before patch : GT 2.0.19
* Version after patch  : GT 2.0.20
* Release Date : 13th Mar 2003
************************************

1. New Features


2. Enhancements

   1. proxy with authentication    
  
3. Bug-fixes

   1. GNDB00013201 - DTD validation not switched off for reference XML documents 
                     used in XSL transformation


4. Additional



***********************************
* Id: gtas-2.0.19-patch
* Version before patch : GT 2.0.18
* Version after patch  : GT 2.0.19
* Release Date : 11th Mar 2003
************************************

1. New Features
   1. proxy support for HTTP communication

2. Enhancements
   1. one-click startup batch script 
   
  
3. Bug-fixes

   1. GNDB00013126 - JBoss hangs due to certificate size in cache increasing from repeated certs

4. Additional

***********************************
* Id: gtas-2.0.18-patch
* Version before patch : GT 2.0.17
* Version after patch  : GT 2.0.18
* Release Date : 6th Mar 2003
************************************

1. New Features
   1. Synchronous PIP(s) for both single and two actions

2. Enhancements

3. Bug-fixes

   1. GNDB00013086 - Cannot save message template the second time
   2. GNDB00013111 - Exceptions when configuring Triggers for User Procedure (Infineon Email)
   3. GNDB00013113 - Error while sending CSR document (Concurrent entity modification)
   4. GNDB00013114 - NumberFormatException: when clicking on Alert from sub-menu
   5. GNDB00013090 - Lookup of gtClientCtrl during pTier initialisation fails

4. Additional
 




