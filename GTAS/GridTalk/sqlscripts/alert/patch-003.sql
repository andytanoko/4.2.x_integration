# 11 Apr 2003 I8 v2.0.27 [Koh Han Sing] Add in new alerts for License Expiry.
# 

use userdb;

#
# 'alert_type'
#
INSERT INTO alert_type VALUES("6","LICENSE_EXPIRY","License Expiry","1.0","0");

#
# 'action'
#
INSERT INTO action VALUES("-4","LICENSE_EXPIRED","License Expired Action","-4","1.0","0");
INSERT INTO action VALUES("-5","LICENSE_EXPIRING","License Expiring Action","-5","1.0","0");

#
# 'alert'
#
INSERT INTO alert VALUES("-4","LICENSE_EXPIRED","6","","","License Expired Alert","1.0","0");
INSERT INTO alert VALUES("-5","LICENSE_EXPIRING","6","","","License Expiring Alert","1.0","0");

#
# 'alert_action'
#
INSERT INTO alert_action VALUES("-4","-4","-4");
INSERT INTO alert_action VALUES("-5","-5","-5");

#
# 'message_template'
#
INSERT INTO message_template VALUES("-5","LICENSE_EXPIRING","Text","EMail","<#USER=admin#>","<#USER=admin#>","","GridNode <%GridNode.ID%> License Expiring","GridNode <%GridNode.ID%> License will be expired on <%License.END_DATE%>\r\n\r\nLicense Details:\r\n--------------\r\nProduct Key          : <%License.PRODUCT_KEY%>\r\nProduct Name       : <%License.PRODUCT_NAME%>\r\nProduct Version     : <%License.PRODUCT_VERSION%>\r\nLicense Start Date : <%License.START_DATE%>\r\nLicense End Date   : <%License.END_DATE%>","","0","1.00002","1");
INSERT INTO message_template VALUES("-4","LICENSE_EXPIRED","Text","EMail","<#USER=admin#>","<#USER=admin#>","","GridNode <%GridNode.ID%> License Expired","GridNode <%GridNode.ID%> License has expired\r\n\r\nLicense Details:\r\n--------------\r\nProduct Key          : <%License.PRODUCT_KEY%>\r\nProduct Name       : <%License.PRODUCT_NAME%>\r\nProduct Version     : <%License.PRODUCT_VERSION%>\r\nLicense Start Date : <%License.START_DATE%>\r\nLicense End Date   : <%License.END_DATE%>","","0","1.00003","1");

