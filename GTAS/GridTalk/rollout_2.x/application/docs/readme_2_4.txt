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
The instructions below apply only for patching an existing build of GridTalk from the same minor version (e.g. 2.4.x):
1. Unpack the release zip file to some temporary folder
2. Ensure GRIDTALK_HOME environment variable is configured to the existing GridTalk root directory
3. Execute "patch.bat" found under the unpacked temporary distribution folder, e.g. patch 2.4 <current_build_number>

----------------------------------------------------------------------------
***********************************
* Id: GridTalk2.4.8
* Foundation baseline  : 2.3.4
* Version before patch : 2.4.7
* Version after patch  : 2.4.8
* Release Date : 9th September 2005
************************************

Enhancements:
   1. Change GTAS ports such that they do not clash with other software such as IIS
   2. run GridTalk as windows service 
   3. assigned a root account password for MySQL
   4. Added a script (changeDatabasePwd.bat) to facilitate change of mysql database root password.

Bug Fix:
   1. to send/receive RosettaNet attachment(s), and export udoc, gdoc, and attachment(s) to a configurable folder

----------------------------------------------------------------------------
***********************************
* Id: GridTalk2.4.7
* Foundation baseline  : 2.3.4
* Version before patch : 2.4.6
* Version after patch  : 2.4.7
* Release Date : 12th May 2005
************************************

Bug-Fixes:
   1. Fixed archiving bug where documents ended in wrong folder


----------------------------------------------------------------------------
***********************************
* Id: GridTalk2.4.6
* Foundation baseline  : 2.3.4
* Version before patch : 2.4.5
* Version after patch  : 2.4.6
* Release Date : 28th March 2005
************************************

Bug-Fixes:
   1. GNDB00025457 -- GTAS doesn't return RN_EXCEPTION after failing to verify signature 
   2. GNDB00025562 -- GTAS2.4.4 sent wrong payload with P2P 
   3. GNDB........ -- RNIF1.1 exception message has an empty element
   4. GNDB........ -- Archiving error: data filter formatted wrongly
   5. GNDB00025749 -- Added 2-level access control: user and administrator

----------------------------------------------------------------------------
***********************************
* Id: GridTalk2.4.5
* Foundation baseline  : 2.3.4
* Version before patch : 2.4.4
* Version after patch  : 2.4.5
* Release Date : 7th Jan 2005
************************************
Enhancement:
   1. Use jre1.4.2_05

Bug-Fixes:
   1. GNDB00025535  --- "GlobalTransactionCode" in RNIF1.1 service header is wrong for 0A1
      
----------------------------------------------------------------------------
***********************************
* Id: GridTalk2.4.4
* Foundation baseline  : 2.3.4
* Version before patch : 2.4.3
* Version after patch  : 2.4.4
* Release Date : 3rd December 2004
************************************
Enhancement:
   1. Archive functionality enhancement.
      - runs much faster
      - able to take care of process instances created under previous versions of process definitions
   2. Integrity check program enhancement
      Now the program identifies a process instance by the combination of process instance ID and
      the process originator ID, instead of the process instance ID alone. 

Bug-Fixes:
   1. GNDB00025310 Port and URI should be optional in http URL
      Previously when configuring a http URL, a port and URI have to be supplied while they should 
      indeed be optional. This fix resolves this problem.
      
----------------------------------------------------------------------------
***********************************
* Id: GridTalk2.4.3
* Foundation baseline  : 2.3.4
* Version before patch : 2.4.2
* Version after patch  : 2.4.3
* Release Date :23rd September 2004
************************************
Enhancement:
   1. Archive functionality enhancement.
      Now archive supports document archiving by either multiple process instances or document types and 
      folders.
   2. Housekeeping enhancement
      This functionality has been simplified after removing unused fields and setting from previous
      versions.

Bug-Fixes:
   1. GNDB00016490 Exported config from GT 2.1 cannot be imported in GT 2.2
      The fix is merged from GT 2.3
 

***********************************
* Id: GridTalk2.4.2
* Foundation baseline  : 2.3.4
* Version before patch : 2.4.1
* Version after patch  : 2.4.2
* Release Date : 4th September 2004
************************************
Bug-Fixes:
   1. GNDB00025265
      The fix is not to send GridTalk proprietary headers such as "channel" when transacting
      with a Non-GridTalk partner using RosettaNet over HTTP.
   2. GNDB00025266	
      The fix is to update GD_IB_OB.xsl such that "RecipientChannelUid" will only be 
      inserted if the source document does have one. 

***********************************
* Id: GridTalk2.4.1
* Foundation baseline  : 2.3.4
* Version before patch : 2.4.0
* Version after patch  : 2.4.1
* Release Date : 17th August 2004
************************************

A. New Features
   1. Alert Email Notification Retry
      This allows email notifications to be re-delivered if they fail to be delivered when the alert is raised.
      This feature is accessible under Administration->Alert->Email Configuration. SMTP server settings are now
      configurable via this page. In addition, user may configure the maximum number of retries and the interval
      for retrying the email, and whether to save the email that has failed all delivery attempts. 
      To disable retry, enter 0 (Zero) or a negative value for Maximum Retries (There is no infinite retries).
      Note that emails that fail to deliver due to permanent errors (e.g. invalid email address, invalid smtp server),
      no retry will be done.
      After patch, existing SMTP settings in conf/default/pdip/framework/mail.properties are migrated to 
      data/alert/email/email-config.xml file.

   2. Online Help
      This allows the user to obtain help from the GridTalk UI browser. The online help consists of both the
      Administrator Guide and User Guide, accessible from Help hyperlink beside the Logout hyperlink. 
      Context-sensitive help links are also available on each data entry or record listing page.
      The online help is also accessible outside of the GridTalk UI browser using the URL:
          http://<gridtalk_host>:<port>/gridtalk/help

   Include features from:
      2.3.3  - Support for AS2 messaging protocol
      2.3.2  - Workflow suspend and resume
      2.2.10 - Internal Scheduler
      2.1.20 - Certificate Replacement   

B. Enhancements
   1. Japanization
      Support labels in Japanese locale. 

   2. GNDB00024857 - Possible enhancement for archiving in GTAS
      a. handle the 'phantom' documents more gracefully such that the archival process will not fail due to missing 
         physical GridDocument or user document files. Missing GridDocument files will be re-generated from the 
         database records. 
      b. create multiple archive files if the file size of the processes and/or documents to be archived exceed the 
         maximum quota per archive file. Maximum size (default 15MB) for each archive file is configurable in the 
         conf/default/dbarchive.properties file. 
      c. archive the processes and documents in a more coherent manner. Files from the same process and document will
         be contained in the same archive file.
      d. rewrite the 2 archive criteria fields for Process Instance as Process Instance ID and Process Instance UID.
         If Process Instance UID is selected, user will be able to select from a list of existing process instance IDs 
         (id may be truncated due to display size).
         If Process Instance ID is selected, user will need to manually enter the process instance ID.
      e. fix location to store the archive files and restore archive base on archive summary files.
      f. archive and restore in background 

     For manual archive, the user now does not need to wait for the archival process to finish before he/she
     can proceed with other activities.  Archival now works in the background and when the archival completes, 
     an email alert notification that contains a archive summary file (summary_yyyymmdd_hhmmss.xml) as an 
     attachment will be sent to the designated recipient(s) in the GTAS ARCHIVE ALERT message template.
     The user will use the archive summary file to perform restoration from the archive. After restore completed,
     an email alert notification that contains the archive summary file (used to restore) as an attachment will
     be sent to the designated recipient(s) in the GTAS RESTORE ALERT message template.
     Similarly, scheduled archival (housekeeping) also works in the background.

     All manual archive summary files are also available in the jboss-3.2.2/bin/gtas/data/archive folder.
     All scheduled archive summary files are also available in the 
     jboss-3.2.2/bin/gtas/data/housekeeping/archive folder. 

   Includes enhancements from:
      2.3.2 -  Send/Receive RosettaNet (RNIF1/2) documents between GT2s using P2P,
               Index Workflow tables for better performance 
      2.2.10 - GNDB00017305, GNDB00017027
      2.1.20 - GNDB00017514


C. Bug-fixes 
   1. GNDB00025083 - RN doc resent upon connected to GM even though process has been terminated
      The fix is to handle resume send/upload of RN docs intended for a P2P partner such that
      the system:-
      i.  resumes send/upload RN request/response messages only if the process instance
          is still in RUNNING state when the system comes online (when localpending is false)
          or partner comes online (when localpending is true).
      ii. resumes send/upload RN_ACK only if the process instance is in RUNNING or COMPLETED
          state.
      iii.resumes send/upload RN_EXCEPTION only if the process instance is in ABNORMALLY_COMPLETED
          or ABNORMALLY_TERMINATED or ABNORMALLY_ABORTED state.

   2. GNDB00025085 - Unable to find process mapping when resume upload 3A4 C ack
      The fix is to handle resume send/upload of RN_ACK for the response message in
      a Two-Action process such that the correct Channel will be used to perform the
      send or upload based on the configured process mapping.

   3. GNDB00025087 - RN_ACK send to GT1 failed due to filename longer than 80char
      The fix is to increase the UdocFilename and RefUdocFilename fields to 255 chars.
      The UdocFullPath and ExportedUdocFullPath field sizes are also enlarged to hold
      long text strings.
      Note that the existing GridDocuments that have the user document filenames truncated 
      will not be fixed with the correct filenames.

   4. GNDB00025178 - Decryption may fail if own channel and partner channel have same name
      This defect affects direct send between GT2s. The fix is to ensure only own channel is retrieved for decryption.

   5. GNDB00024957 - Persisted Certificate Serialnum column not case-sensitive

   6. GNDB00024919 - No checking for association in Service Assignments when deleting Partner or Webservices

   7. GNDB00024918 - Disabled/Deleted Partner still allowed to access webservice through wsrouter
      See E2 for known problems.

   Includes bug-fixes from:
      2.3.4    - GNDB00024921, GNDB00024914, GNDB00024800, GNDB00017225 (Dup: GNDB00017598)
      2.3.3    - GNDB00024778, GNDB00021907, GNDB00015561
      2.3.2    - GNDB00017504, GNDB00017318, GNDB00017217, GNDB00017120, GNDB00017036
      2.2.10   - GNDB00017315, GNDB00017295, GNDB00017296, GNDB00017214, GNDB00017119, GNDB00016828, GNDB00016182
      2.1.21   - GNDB00021906
      2.1.20   - GNDB00017554
      2.1.19   - GNDB00017273, GNDB00017253 (Dup: GNDB00016106)

D. Additional
   1. Linux platform support details
      - Redhat Enterprise Linux 3.x
      - JRE 1.4.2_05
      - MySQL 4.0
      - JBoss 3.2.2
      - Jakarta Ant 1.5.1 + ant-contrib-0.1.jar

   2. Refer to Readme_2_1.txt for change details included from 2.1.19-2.1.21.

   3. Refer to Readme_2.2.txt for change details included from 2.2.10.

   4. Refer to Readme_2.3.txt for change details included from 2.3.2-2.3.4.

E. Known Problems
   1. Refer to Readme_2_1.txt, Readme_2.2.txt and Readme_2.3.txt.
   2. For wswrouter, if a partner has been authenticated before he is disabled/deleted, 
      he will still be allowed to access webservice until GridTalk is restarted. This could be due to session caching
      by the embedded tomcat webserver.

   

***********************************
* Id: GridTalk2.4.0
* Foundation baseline  : 2.3.1
* Version before patch : -
* Version after patch  : 2.4.0
* Release Date : 04th Mar 2004
************************************

A. New Features
   1. GridTalk WebService Gateway for Internal WebServices
      The feature enables WebService invocation from authenticated Partners based on authorizations.
      Users will be able to manage a registry of WebServices and the Partner authorizations by:
      - configuring a service name and the WSDL and EndPoint URLs for each WebService
      - assigning authentication password (min. 6 chars) to each Partner and
      - authorizing the WebServices for invocation by each Partner
      The feature is accessible under the WebService tab in Admin view.

      The Gateway provides two URLs that will be used by Partner's WebService client to:
      - Get the WebService WSDL, using http://<gridtalk_ip_and_port>/gridtalk/wsrouter/service?service=<service_name>
      - Invoke the WebService, using http://<gridtalk_ip_and_port>/gridtalk/wsrouter/invoke?service=<service_name>
      In either case, the HTTP request must contain the authorization profile for HTTP basic authentication of
      the Partner. The authorization profile should contain the Partner ID (as the username) and the 
      authentication password assigned to the Partner.

B. Enhancements


C. Bug-fixes 


D. Additional
   1. Versions of GN Libraries used:
      gn-xml       - 1.0.16
      gn-convertor - 1.0.7
      gn-log       - 1.0.4
      gn-util      - 1.0.2
      gn-jnfs      - 1.0.0
   



