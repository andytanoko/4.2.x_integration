# @Date:03/JUN/2004  @since 2.4 @Author Mahesh .

use appdb;

UPDATE fieldmetainfo
set ValueClass='java.util.Collection'
where SqlName='WebServiceUIds' and EntityObjectName = 'com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment';

use userdb;

ALTER TABLE `service_assignment`
  MODIFY COLUMN `WebServiceUIds` text;
