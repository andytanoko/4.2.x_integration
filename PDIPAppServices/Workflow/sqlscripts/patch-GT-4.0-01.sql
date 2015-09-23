# Tam Wei Xiang             Update the Reason value class to 'ObjectSER'

USE appdb;

UPDATE fieldmetainfo SET valueclass='ObjectSER' WHERE entityobjectname='com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc' AND fieldname='REASON';