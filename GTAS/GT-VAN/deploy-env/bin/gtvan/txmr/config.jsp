<%
//-------------------------------------- CHANGE SETTING WITHIN THIS LINE=

//=======================DATABASE CONNECTION ( ORACLE VERSION )=====================

db_server       = "localhost:35432/gtdb";     //db server address
dbuser          = "gtvan";                         //username
dbpass          = "gridnode";                      //password

//==================================END=================================================


//===========================EMAIL SETTING==============================================

Email_host     = "rca-relay.itlogon.com"; //SMTP SERVER
Email_username = null; //Username
Email_password = null; //Password
Email_from     = "txmr@itlogon.com"; //Email Sender

//==================================END=================================================


//=========================================TIMEZONE=====================================

timezone = "+9"; //server timezone

//==================================END=================================================

//=================================== Process URL =========================

String host = "http://gtstage.inovis.com";
cancel_url = host+"/gridtalk/van/isat/reprocess";
reprocess_url = host+"/gridtalk/van/isat/reprocess";
archive_servlet = host+"/gridtalk/van/isat/archive";

//=========================================================================

//====================================Report URL========================================

create_report   = host+"/gridtalk/van/genreport/create_report?";
create_schedule = host+"/gridtalk/van/genreport/create_schedule?";
update_schedule = host+"/gridtalk/van/genreport/update_schedule?";
delete_schedule = host+"/gridtalk/van/genreport/delete_schedule?";
generate_report = host+"/gridtalk/van/genreport/generate_report?";
view_report 	= host+"/gridtalk/van/genreport/view_report?";
housekeep_report= host+"/gridtalk/van/genreport/housekeep_report?";

//==================================END=================================================

//=========================LOG HOUSE KEEPING===================================

del_log = 30; //Age ( in days ) of log before it will be automatically deleted. 

//=========================END LOG HOUSE KEEPING===============================


//==============================Paging Limit===================================

paging_limit = 30; //Total numbers to display on paging before next button

//===================================END=======================================

//-------------------------------------- CHANGE SETTING WITHIN THIS LINE






%>
<%

subroot = ""; 

%>


