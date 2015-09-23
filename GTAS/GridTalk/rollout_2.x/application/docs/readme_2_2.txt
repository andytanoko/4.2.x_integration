----------------------------------------------------------------------------
Setup Instructions
******************

For setup using Zip file distribution,
1. Unpack the release zip file to some temporary folder
2. Ensure GRIDTALK_HOME environment variable is configured to the desired GridTalk root directory
3. Ensure "jboss-3.2.2" is setup under <GRIDTALK_HOME>\jboss-3.2.2
4. Ensure "mysql-4.0" is setup under <GRIDTALK_HOME>\mysql
5. Ensure "JRE 1.4.1-02" is setup under <GRIDTALK_HOME>\jre141_02
6. Ensure "jakarta-ant-1.5.1-bin" is setup on the same directory level as <GRIDTALK_HOME>
7. Execute "setup.bat" found under the unpacked temporary distribution folder

For setup using windows installer distribution (if available), follow the 
installer setup instructions.


Patch Instructions
******************
The instructions below apply only for patching an existing build of GridTalk from the same minor version (e.g. 2.2.x):
1. Unpack the release zip file to some temporary folder
2. Ensure GRIDTALK_HOME environment variable is configured to the existing GridTalk root directory
3. Execute "patch.bat" found under the unpacked temporary distribution folder, e.g. patch 2.2 <current_build_number>
    
-----------------------------------------------------------------------------

************************************
* Id: gtas-2.2.10-patch
* Foundation baseline  : 2.1.20
* Version before patch : 2.2.9
* Version after patch  : 2.2.10
* Release Date         : 14th April 2004
************************************

1. New Features
   1. Internal Scheduler - This allows the user to schedule jobs:
      a. to run User Procedures
      b. to check GridTalk license expiry (update only)
      User is able to configure jobs to run Once, Minutely, Hourly, Daily, Weekly, or Monthly.

   Includes features from 2.1 versions:
   2.1.20 - Certificate Replacement


2. Enhancements
   1. GNDB00017305 - Support importing schema mapping files in sub-directories.

   2. GNDB00017027 - Jboss 3.2.2 server log goes to 1.2GB after sending 1000 LAT msgs
      A custom log appender is added to JBoss which will behave similar to the gts log appender.
      By default, backups of the server.log will be created once its size reaches 1MB, and when
      10 backups are created, they will be zipped. Configurable options like MaxFileSize, MaxBackupIndex, Threshold 
      can be found in jboss-3.2.2/server/default/conf/log4j.xml under "ZIPFILE" appender.
      Note that server logs that are not zipped will be wiped out on restart of GridTalk.

   Includes enhancements from 2.1 versions:
   2.1.20 - GNDB00017514


3. Bug-fixes
   1. GNDB00017315 - UI error in Edit Web serivce configuration window
      Note that the Password field will still be blank during edit for security reason. No entry is necessary
      if it is not required for modification.

   2. GNDB00017295 - Duplicate Packing Profile is possible
      The same problem applies to Channels, Communication Profiles, and Security Profiles.

   3. GNDB00017214 - Problem with attachments having long file names

   4. GNDB00016828 - GT2.2.9 - Error in importing FTP configuration file

   5. GNDB00016182 - RNIF 1.1 RN_Ack schema validation throws exception when validating against ReceiptAcknowledgementMessageGuideline.xml

   Includes bug fixes from 2.1 versions:
   2.1.19 - GNDB00017273, GNDB00017253 (Dup: GNDB00016106)
   2.1.20 - GNDB00017554

 
4. Additional


************************************
* Id: gtas-2.2.9-patch
* Foundation baseline  : 2.1.18
* Version before patch : 2.2.8
* Version after patch  : 2.2.9
* Release Date         : 12th Jan 2004
************************************

1. New Features

2. Enhancements

3. Bug-fixes
   1. GNDB00016735 - GT 2.2.8 - Error in downloading attachments with JMS
   2. GNDB00016743 - GT 2.2.8 - Error in viewing correct certificates
      -- duplicate --
      GNDB00014897 - Duplicate certificate name causes problems

   Includes bug fixes from 2.1 versions:
   2.1.18 - GNDB00016716, GNDB00016720

4. Additional

      
************************************
* Id: gtas-2.2.8-patch
* Foundation baseline  : 2.1.17
* Version before patch : 2.2.7
* Version after patch  : 2.2.8
* Release Date         : 31st Dec 2003
************************************

1. New Features

2. Enhancements
   1. Enhance UserProcedure configuration to allow byte[] DataType parameters for User document datasource.
      Note that for large files (a few MBs), it may cause significant performance impact if the 
      user procedure is a Soap WebService.
   2. Enhance Soap WebService UserProcedure:
      - add SOAP-Action into HTTP Headers
      - add HTTP authentication parameters (username and password) 

3. Bug-fixes
   1.  GNDB00016638 - Configurations exported incorrectly for Webservice user procedure, causing
                      import configuration to fail.
   2.  GNDB00016604 - Consume all exceptions occurred during user logout -- allow logout at all times
   3.  GNDB00016392 - Gridtalk can't send Biz document error due to incorrect license state
   4.  GNDB00016363 - Unable to view/edit details of imported certificates in UI
   5.  GNDB00016308 - Importing of FTP configuration throws a RunTime Exception
   6.  GNDB00016285 - Search for Partner using Whitepage as search criteria returns ArrayIndexOutOfBoundsException
   7.  GNDB00016269 - Upon filling up the maxlength available for GridNode Name field during registration,
                      a DirectoryString exception is thrown
   8.  GNDB00016125 - Missing Sender Partner Type in GridDocument detail display
   9.  GNDB00014897 - Duplicate certificate name causes problems
   10. GNDB00014745 - Reduce unnecessary log statements by GridTalk client on console
   11. GNDB00014694 - Lost timezone information if login details incorrect
   
   Includes bug fixes from 2.1 versions:
   2.1.15 - GNDB00016152
   2.1.16 - GNDB00016432, GNDB00016444, GNDB00016467, GNDB00014860, GNDB00016509

4. Additional
   1. JBoss application server is upgraded to version 3.2.2 to resolve some of the JBossMQ 
      unstability issues. This patch assumes that the JBoss 3.2.2 is already installed under 
      folder "<GRIDTALK_HOME>/jboss-3.2.2".


************************************
* Id: gtas-2.2.7-patch
* Foundation baseline  : 2.1.14
* Version before patch : 2.2.6
* Version after patch  : 2.2.7
* Release Date         : 11th Nov 2003
************************************

1. New Features
  
2. Enhancements

3. Bug-fixes
   1. GNDB00016148 - Importing of files intermittently causes a NoSuchEntityException: 
                     Record with primaryKey [xx] does not exist in database
   2. GNDB00016149 - Publishing of BE to UDDI server returns a ClassCast exception

4. Additional


************************************
* Id: gtas-2.2.6-patch
* Foundation baseline  : 2.1.14
* Version before patch : 2.2.5
* Version after patch  : 2.2.6
* Release Date         : 6th Nov 2003
************************************

1. New Features
  
2. Enhancements

3. Bug-fixes
   1.  GNDB00014726 - TransportImplVersion of GridMaster comminfo mysteriously changed to "030000" causes connection to fail
   2.  GNDB00014919 - FrameBorders visible
   3.  GNDB00015128 - PF '4C1 ' created with a space at the end got truncated when saved to DB
   4.  GNDB00015448 - Preamble elements out of sequence in GTAS RNIF1.1 messages
   5.  GNDB00015684 - GTAS -- Incorrect display label in processDef window for RNIF 1.1
   6.  GNDB00015739 - Unable to perform connection setup to GridMaster - Cannot invoke Packaging Service 
   7.  GNDB00015778 - Concurrency problem in creating KeyGen
   8.  GNDB00015932 - Registration failed when GridNode Name has invalid char eg. &
   9.  GNDB00015934 - Unable to export multiple Partner certificates to the Trust Store
   10. GNDB00015981 - Fail to invoke services in AppInterface module
   11. GNDB00015987 - Modification to Process Definition not synchronised to Process Mapping
   12. GNDB00016037 - Viewing of process instances fails when there is more than 1 process instance
   13. GNDB00016047 - Unable to delete mapping file even if dependent records have already been deleted
   14. GNDB00016048 - Unable to view documents under "All Documents" and "Outbound" folder due to process instance
   15. GNDB00016050 - Unable to startup NT service
   16. GNDB00016116 - Cancel button not showing on registration page

4. Additional
   1. The distribution now comes with "unregister.bat" script (under GRIDTALK_HOME/bin) to aid mass supplier rollout.
      This script assumes that the master copy for unregistration is a clean copy, i.e all transactional records and 
      data files are not taken into consideration for unregistering. Also, all non-P2P (GT) partner related configuration
      will be left intact. 
   2. Security password input during registration, connection setup, and connection to GridMaster is temporary 
      disabled for user entry. This is a workaround for the certificate issues arising when GridTalk is run on NT service,
      or when document transactions do not require connection to GridMaster.      


************************************
* Id: gtas-2.2.5-patch
* Foundation baseline  : 2.1.14
* Version before patch : 2.2.4
* Version after patch  : 2.2.5
* Release Date : 29th Oct 2003
************************************

1. New Features
  
2. Enhancements
   1. GNDB00015950 - To include sample registry connection URLs in default distribution

3. Bug-fixes
   1. GNDB00015471 - Backend Listener log files grow too big in size
   2. GNDB00015833 - Error sending SCAR files containing more than 1 attachment
   3. GNDB00015933 - Sending/receiving of documents (RN) use deleted Partner info
   4. GNDB00015963 - Missed Alarm triggered twice during system startup

4. Additional
   1. The standard re-direct port for GridTalk client is changed from 8007 to 9099.
   2. Installing MySQL and JBoss as NT service now works. 
      Previously the renaming of <jboss>/server/lib/xercesImpl-2.4.0.jar and xmlParserAPIs.jar
      to xerces-2.4.0.jar and xml-parser-apis.jar was not propagated to the service.bat. That's
      why JBoss could not startup properly.

***********************************
* Id: gtas-2.2.4-patch
* Foundation baseline  : 2.1.14
* Version before patch : 2.2.3
* Version after patch  : 2.2.4
* Release Date : 20th Oct 2003
************************************

1. New Features
  
2. Enhancements
   1. Enable Manual Export from Inbound system folder
   2. Re-organise documents into respective system folders
   3. Enable Sorting in Document listview   

3. Bug-fixes
   1. GNDB00015738 - SOAP-B2B Messaging Protocol not a choosable element under 
                     Com Profile and Channel  

4. Additional

***********************************
* Id: gtas-2.2.3-patch
* Foundation baseline  : 2.1.14
* Version before patch : 2.2.2
* Version after patch  : 2.2.3
* Release Date : 10th Oct 2003
************************************

1. New Features
   1. Support for Soap B2B Messaging
   
2. Enhancements

3. Bug-fixes
 
4. Additional

***********************************
* Id: gtas-2.2.2-patch
* Foundation baseline  : 2.1.14
* Version before patch : 2.2.1
* Version after patch  : 2.2.2
* Release Date : 3rd Oct 2003
************************************

1. New Features
   1. UDDI Registry Support
   The features include:
   a. Publish to UDDI registry business entitys and channels as business and service information respectively.
   b. Search from UDDI registry for published business entitys and channels
   c. Configure partner business entitys and channels from search UDDI information.
   d. Upward synchronization of registry information
   e. Downward synchronization of registry information

   2. Rosettanet PIP(s) schemas generation
   Schema will be re-generated on create/update/delete of Process Definition
   
2. Enhancements

3. Bug-fixes
   Includes all bug fixes and enhancements in 2.1.14 
 
4. Additional


***********************************
* Id: gtas-2.2.1-patch
* Foundation baseline  : 2.1.13
* Version before patch : 2.2.0
* Version after patch  : 2.2.1
* Release Date : 24th Sep 2003
************************************

1. New Features
   1. Backend Soap Interface
   This allows invocation of backend import services via WebService.
  
   2. Process Instance Tracking
   This allows for easy traceability from documents in list view to the process instances.
       	
2. Enhancements

3. Bug-fixes
   Includes all bug fixes and enhancement in 2.1.12 & 2.1.13   
 
4. Additional


***********************************
* Id: gtas-2.2.0-patch
* Foundation baseline  : 2.1.11
* Version before patch : -
* Version after patch  : 2.2.0
* Release Date : 11th Aug 2003
************************************

1. New Features
   1. Entities Dependency Checks 
   This allows for dependency checking when deleting configuration data. User shall be warned 
   and prohibited to delete configuration data that has dependencies.

   2. User Procedure Configuration Enhancement
   This provides user to select from list of classes and methods when configuring Java Procedure type.
   
   3. Soap Enabling User Procedure 
   Executing Engine has been enhanced to support invocation of web services via User Procedure.     
       	
2. Enhancements

3. Bug-fixes 
    
4. Additional



