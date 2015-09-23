@echo off
REM This batch file is used to run gn-utadaptor-ext Sender standalone outside of GridTalk.
REM This allows users to send files to UCCnet not via GridTalk UPC.

REM Required jar files
SET CLASSPATH=gn-utadaptor-ext.jar;jaxen-full-1.0.jar;saxpath.jar;xml-apis.jar;jdom-b9.jar

REM Full file path to the file to be sent to UCCnet
SET PAYLOAD=<payload_file_fullpath>

REM Full path to Working directory of UCCnet Transport Adaptor, i.e. directory containing the ADAPTOR_CONF
SET ADAPTOR_DIR=<utadaptor_working_directory>

REM Configuration file for running the UCCnet Transport Adaptor.
SET ADAPTOR_CONF=run-adaptor.properties

REM Full path to Directory for saving the UCCnet response message
SET RESP_DIR=<response_save_directory>

REM Suffix for the saved UCCnet response message. The base filename with be after the PAYLOAD.
SET RESP_SUFFIX=_resp.xml

REM Run the gn-utadaptor-ext Sender
java -cp %CLASSPATH% com.gridnode.utadaptor.Sender "payload.file=%PAYLOAD%" "adaptor.dir=%ADAPTOR_DIR%" "adaptor.conf=%ADAPTOR_CONF%" "response.save.dir=%RESP_DIR%" "response.file.suffix=%RESP_SUFFIX%"
