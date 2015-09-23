USE userdb;

#
# Table structure for table 'worklistvalue'
#

DROP TABLE IF EXISTS worklistvalue;
CREATE TABLE `worklistvalue` (
  `UID` bigint(11) NOT NULL default '0',
  `wi_description` varchar(50) default NULL,
  `wi_comments` varchar(50) default NULL,
  `wi_status` char(2) default NULL,
  `wi_cdate` date default NULL,
  `user_id` varchar(80) default NULL,
  `unassigned` char(2) default NULL,
  `processDefKey` varchar(255) default NULL,
  `activityId` varchar(255) default NULL,
  `performer` varchar(150) default NULL,
  `rtActivityUId` bigint(11) NOT NULL,
  `contextUId` bigint(11) NOT NULL,
  PRIMARY KEY  (`UID`)
) TYPE=MyISAM;


#
# Table structure for table 'worklistuser'
#

DROP TABLE IF EXISTS worklistuser;
CREATE TABLE `worklistuser` (
  `UID` bigint(20) NOT NULL default '0',
  `workitem_id` bigint(11) NOT NULL default '0',
  `user_id` varchar(80) default NULL,
  PRIMARY KEY  (`UID`)
) TYPE=MyISAM;
