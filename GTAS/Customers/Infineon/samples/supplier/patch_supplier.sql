# Db Script for patching the Supplier's database
# Replace those enclosed within anchor brackets (<>). 
# ******* Also replace the <> brackets.

USE userdb;

# Update Comm Profiles
UPDATE comm_info
SET Url='http://<supplier_host>:8080/httpChannel/servlet/httpReceiver'
WHERE ProtocolType='HTTP' AND RefId='2001';
UPDATE comm_info
SET Url='http://<infineon_host>:8080/httpChannel/servlet/httpReceiver'
WHERE ProtocolType='HTTP' AND RefId='2000';

# Update RFCs
UPDATE rfc
SET Host='<gridapp_host>'
WHERE RfcName='GRIDAPP_RFC';
UPDATE rfc
SET Host='<backend_ip>'
WHERE RfcName='SU_BE';

# Update Ports
UPDATE port
SET HostDir='<export_po_dir>'
WHERE PortName='POR';
UPDATE port
SET HostDir='<export_poc_dir>'
WHERE PortName='POCHG';


