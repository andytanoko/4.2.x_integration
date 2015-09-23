# Db Script for patching the Infineon's database
# Replace those enclosed within anchor brackets (<>). 
# ******* Also replace the <> brackets.

USE userdb;

# Update Comm Profiles
UPDATE comm_info
SET Url='http://<supplier_host>:8080/httpChannel/servlet/httpReceiver'
WHERE ProtocolType='HTTP' AND RefId='2001';

UPDATE comm_info
SET Url='http://<infineon_http_host>:80/base-transport/httpReceiver'
WHERE ProtocolType='HTTP' AND RefId='2000';

# Update RFCs
UPDATE rfc
SET Host='<sap_host>'
WHERE RfcName='SAP_BE';

# Update Ports
UPDATE port
SET HostDir='\\<infineon_host>\<export_idoc_poc_dir>'
WHERE PortName='POC';

UPDATE port
SET HostDir='\\<infineon_host>\<export_idoc_pochgc_dir>'
WHERE PortName='POCHGC';

#UPDATE port
#SET HostDir='<export_ff_vir_dir>'
#WHERE PortName='VIR';

#UPDATE port
#SET HostDir='<export_idoc_asn_dir>'
#WHERE PortName='ASN';

#UPDATE port
#SET HostDir='<export_idoc_invoice_dir>'
#WHERE PortName='INVOICE';

