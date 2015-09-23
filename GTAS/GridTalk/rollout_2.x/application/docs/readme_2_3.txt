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
The instructions below apply only for patching an existing build of GridTalk from the same minor version (e.g. 2.3.x):
1. Unpack the release zip file to some temporary folder
2. Ensure GRIDTALK_HOME environment variable is configured to the existing GridTalk root directory
3. Execute "patch.bat" found under the unpacked temporary distribution folder, e.g. patch 2.3 <current_build_number>

----------------------------------------------------------------------------

***********************************
* Id: GridTalk2.3.4
* Foundation baseline  : 2.2.10, 2.1.21
* Version before patch : 2.3.3
* Version after patch  : 2.3.4
* Release Date : 9th June 2004
************************************

A. New Features

B. Enhancements

C. Bug-fixes 
   1. -- AS2 defects --
      GNDB00024873 - Error sending AS2 - Encrypted
      GNDB00024874 - Error sending AS2 - Encrypted and Compression
      GNDB00024875 - Error sending AS2 - Signed
      GNDB00024876 - Error sending AS2 - Signed and Compression -HTTPS
      GNDB00024879 - Error send GTAS - Signed / HTTPS/ Async

   2. GNDB00024897 - Scheduled User Procedure failed to raise Alert

   3. GNDB00024914 - Backend Receiver - BEAPIHandler: Concurrency problem when multiple threads try to 
                     create same directory simultaneously

   4. GNDB00024921 - getSendMessageHandler() not properly sychronized, causing problem when concurrent sending
                     splitted documents.

   5. GNDB00024800 - Channel Info display error

   6. GNDB00017225 - Packaging Profile UI error
      (Duplicate: GNDB00017598)

   (Below are already fixed in 2.3.2)
   7. GNDB00017296 - Navigation error, click "Search", "GridNode Search" hyperlinked instead
      GNDB00017119 - User Interface Error for "Search" feature

D. Additional
   1. Update Backend Receiver for GNDB00024914 fix
      If the running copy of backend receiver is not located under default location i.e. 
      <GRIDTALK_HOME>/backend/receiver, then it is necessary to copy the "receiver.jar" from the default
      location to the correct location.

   2. Change GridTalk Ports
      A changePorts.bat is provided under <GRIDTALK_HOME>/bin to enable changing the ports used for 
      running copy of GridTalk. changePorts.txt provides the instructions on how to use the batch program.

      In lieu with enabling port changing using changePorts.bat, the following files have been changed:

      a. <GRIDTALK_HOME>/bin/ports.map and <GRIDTALK_HOME>/bin/default.map
         ports.map provides default mapping from the GridTalk default ports to port numbers in the 20000-29999 range.
         default.map provides the reverse mapping for the above.
         If ports.map and/or default.map in existing installation have been modified earlier for port
         changes, it is recommended to backup the those existing files before applying this patch, and re-modify
         the patched files to existing port configurations. 

      b. <GRIDTALK_HOME>/bin/setenv.bat
          The following line:
              set APPSERVER_SHUTDOWN_CMD=shutdown localhost 8080
          is changed to:
              set APPSERVER_SHUTDOWN_CMD=shutdown --server=localhost:1099
          For installations where the JNDI port is not 1099, change the 1099 to your configured port number
          accordingly before starting GridTalk.

      c. <GRIDTALK_HOME>/bin/startmysql.bat and <GRIDTALK_HOME>/bin/startmysql.bat
         The commands are modified to add in specification for the mysql server port "--port=3306".
         For installations where the DB port is not 3306, change the 3306 to your configured port number
         accordingly before starting GridTalk.

E. Known Problems
   1. GNDB00024858 - Gn-convertor can not resolve the host name, must use the ip address instead.
      This is known issue with MS SQL Server 2000. For more information, see 
      <http://support.microsoft.com/default.aspx?scid=kb;en-us;306865>. Follow the workaround as suggested
      in the MS KnowledgeBase or upgrade MS SQL Server 2000 to Service Pack 3a to resolve the issue.


***********************************
* Id: GridTalk2.3.3
* Foundation baseline  : 2.2.10, 2.1.21
* Version before patch : 2.3.2
* Version after patch  : 2.3.3
* Release Date : 19th May 2004
************************************

A. New Features
   1. Support for AS2 messaging protocol
      This feature enables sending/receiving of documents using AS2 messaging over HTTP. The AS2 messaging process
      works as follows:
      - When an AS2 message is being sent, a message ID is generated for the message and stored in the 
        "User Tracking Id" field of the GridDocument record for the AS2 message. 
      - When an AS2 request message is received, a corresponding acknowledgement may be generated and sent back 
        to the sender (depending on configuration). 
      - When an AS2 acknowledgement is received, the original message ID from the message content is matched with
        the "User Tracking Id" of unacknowledged outbound documents. If found, the GridDocument will be timestamped
        for the "Date/Time Transacted" field. 
     
   Includes features from:
   2.2.10 - Internal Scheduler
   2.1.20 - Certificate Replacement


B. Enhancements

   Includes enhancements from:
   2.2.10 - GNDB00017305, GNDB00017027
   2.1.20 - GNDB00017514


C. Bug-fixes 
   1. GNDB00015561 - 1 file missing intermittently in INBOUND and EXPORT folder
      The problem boils down to numerous database queries during XPDL processing.
      This fix enhances the partner function XPDL deployment to reduce the number of database queries.
      Partner functions created or edited after applying this patch are deployed with optimized query capability.
      Existing partner function XPDL processes already running when the edit takes place will not be affected. 

   2. GNDB00021907 - No exception handling when GridMaster return negative during Keep Alive

   3. GNDB00024778 - KeepAlive interval not enabled for edit

   Includes bug fixes from:
   2.2.10 - GNDB00017315, GNDB00017295, GNDB00017214, GNDB00016828, GNDB00016182
   2.1.21 - GNDB00021906
   2.1.20 - GNDB00017554
   2.1.19 - GNDB00017273, GNDB00017253 (Dup: GNDB00016106)
   

D. Additional
   

E. Known Problems
   1. Channels sent across from another GridTalk partner (using Send Business entity/Activation feature) are not
      editable. This imposes limitation for AS2 messaging in that Security Profile settings could not be modified 
      to enable Signature and compression. In that sense, if signature and compression are required, a Non-GridTalk
      Partner AS2 messaging channel needs to be configured in order to overcome this.

   2. Domain Identifiers configured in a Business Entity are not published with the Business Entity to UDDI. Thus
      the partner that searches and configure the Business Entity from UDDI will need to manually configure the domain 
      identifiers.


***********************************
* Id: GridTalk2.3.2
* Foundation baseline  : 2.2.9
* Version before patch : 2.3.1
* Version after patch  : 2.3.2
* Release Date : 2nd April 2004
************************************

A. New Features
   1. 'Suspend' Workflow activity
      This allows workflow processing to be suspended for a configurable amount of time before continuation.
      This feature is useful under situtations of heavy load e.g. large amount of concurrent incoming documents,
      and there's need to regulate the concurrent execution of workflow processes by limiting the number of concurrent
      execution of the same workflow activity at any instant.

      An example of usage is to suspend the Export of incoming RosettaNet documents till a later dispatch time, 
      giving resources for processing the return of the RosettaNet Receipt Acknowledgements to the sender. In this
      case, in the "Exit to Export" (or Exit to Port) activity, a Dispatch Interval can be configured to force the 
      processing to proceed several seconds later. This relinguishes resources to another workflow process which 
      is started for sending RosettaNet Receipt Acknowledgements. The Dispatch Count controls the maximum number of
      suspended "Exit to Export" activities that will be dispatched at each dispatch time.  


B. Enhancements
   1. Send/Receive RosettaNet (RNIF1/2) documents between GT2s using P2P (both must be GT2.3.2). 
      A P2P RN channel can be configured using the Communication and Security profiles of the GridTalk Master Channel.
      GTs can exchange the P2P RN channel during peer activation or using the Send Business Entity feature.
      The P2P RN channel allows RosettaNet documents to be uploaded to GridMaster when the recipient partner is offline,
      and downloaded from GridMaster when the recipient partner comes online. Other configurations for enabling 
      RosettaNet processes are as per normal. The DateTimeTransacted for a PIP document is timestamped when RN_ACK
      is received, while that of RN_ACK and RN_EXCEPTION documents is timestamped on P2P-level acknowledgement received.

   2. Index Workflow tables for better performance


C. Bug-fixes 
   1. GNDB00017504 - Resource link points to deleted partner
   2. GNDB00017318 - Failure in send cause a lot of port not released.
   3. GNDB00017217 - RNIF1 packaging doesn't work after channel restructuring
   4. GNDB00017120 - unknown item in dropdown menu in Search> Field/XPath
   5. GNDB00017036 - Wrong extension for exported FF


D. Additional



***********************************
* Id: GridTalk2.3.1
* Foundation baseline  : 2.2.9
* Version before patch : 2.3.0
* Version after patch  : 2.3.1
* Release Date : 06th Feb 2004
************************************

A. New Features


B. Enhancements
   1. GNDB00017073 - Auto-connect to GridMaster upon GridTalk startup
      To enable auto-connection, modify the <jboss>/bin/gtas/data/sys/entity/conn-props.xml:
         <AutoConnectOnStartup>true</AutoConnectOnStartup>


C. Bug-fixes 
   1. GNDB00017046 - Error receiving normal docs from GT1
   2. GNDB00017043 - Problem with Backend sending
   3. GNDB00017038 - GT2-GT1 interop: Fail to receive with attachments
   4. GNDB00017037 - Approving party have problem receiving online notification immediately after activation

D. Additional


***********************************
* Id: GridTalk2.3.0
* Foundation baseline  : 2.2.9
* Version before patch : -
* Version after patch  : 2.3.0
* Release Date : 30th Jan 2004
************************************

A. New Features
   1. P2P Interoperability with GridTalk 1 series
      This consists of interoperability for:
      a. Search/Activation
      b. Send/Receive Documents (including RosettaNet docs)
      c. Upload/Download Documents (with GridMaster, including RosettaNet docs)
      d. Send/Receive Business Entity to activated GridTalk partner
      e. Auto-synchronize Business Entity creation/changes to GridMaster when online
      f. Auto-synchronize previously sent Business Entity changes to GridTalk partner (if online)
      g. Resume synchronization of Business Entity changes (when GridMaster/GridTalk partner come online)
      h. Transport level file splitting/joining for documents
      (See Also E)

   2. Support for UCCnet Transport using UtAdaptor (See Also D)
      This consists of:
      a. an user procedure ("gn-utadaptor-ext") that interface with UtAdaptor to send documents to UCCnet 
         and save response messages from UCCnet to a specific location.
      b. a program "ibtransporter" that could pick the response messages from the specific location and
         send into the GridTalk, or transform a message to its response message before sending into
         GridTalk.

   3. Document Searching
      This allows user to create persistable document search queries by specifying criteria with:
      a. GridDocument fields
      b. User document XPath

   4. Asian Characters Support for XML content
      This consists of transforming/converting/searching XML documents containing asian characters.

   5. Housekeeping
      This consists of:
      a. Scheduling of housekeeping jobs
      b. Housekeeping of Log files, Temporary files, Audit files, Documents, and Process Instances


B. Enhancements
   1. GNDB00016060 - Support authentication for email
      To enable outgoing email authentication, set the following two properties in 
      <jboss>/bin/conf/default/pdip/framework/mail.properties:
         smtp.server.username=<user name>
         smtp.server.password=<password>

   2. Process Instance List View Sorting
      Sorting on Process Instance List View is supported on all viewable columns except the "User Tracking ID". 


C. Bug-fixes 


D. Additional
   1. "gn-utadaptor-ext" is provided as a separate distribution: "gn-utadaptor-ext-v1.0".
   2. "ibtransporter" is provided in standard GridTalk distribution under "<GRIDTALK_HOME>\backend" folder.
   3. Versions of GN Libraries used:
      gn-xml       - 1.0.16
      gn-convertor - 1.0.7
      gn-log       - 1.0.4
      gn-util      - 1.0.2
      gn-jnfs      - 1.0.0


E. Known Problems
   1. P2P between GT2s do not perform security verification during decryption at the moment.
   2. Transacting RosettaNet documents over P2P between GT2s is not well supported currently.
      Although it is possible to transact RN documents using the Master communication profile (JMS),
      The following problems exist:
      a. Transaction completed timestamp for signal docs, e.g. RN_ACK will not be updated even though the
         partner has received the RN_ACK.
      b. Uploading of documents to GridMaster will not work, and thence documents can only be sent/received when both
         parties are online. 
      However, the states of the Process Instances are not affected.

