# This batch file is used to run gn-utadaptor-ext Sender standalone outside of GridTalk.
# This allows users to send files to UCCnet not via GridTalk UPC.

# Required jar files
export CLASSPATH=gn-utadaptor-ext.jar:jaxen-full-1.0.jar:saxpath.jar:xml-apis.jar:jdom-b9.jar

# Full file path to the file to be sent to UCCnet
export PAYLOAD=<payload_file_fullpath>

# Full path to Working directory of UCCnet Transport Adaptor, i.e. directory containing the ADAPTOR_CONF
export ADAPTOR_DIR=<utadaptor_working_directory>

# Configuration file for running the UCCnet Transport Adaptor.
export ADAPTOR_CONF=run-adaptor.properties

# Full path to Directory for saving the UCCnet response message
export RESP_DIR=<response_save_directory>

# Suffix for the saved UCCnet response message. The base filename with be after the PAYLOAD.
export RESP_SUFFIX=_resp.xml

# Run the gn-utadaptor-ext Sender
java -cp $CLASSPATH com.gridnode.utadaptor.Sender "payload.file=$PAYLOAD" "adaptor.dir=$ADAPTOR_DIR" "adaptor.conf=$ADAPTOR_CONF" "response.save.dir=$RESP_DIR" "response.file.suffix=$RESP_SUFFIX"
