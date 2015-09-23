<%@page import="java.util.Date,com.gridnode.pdip.app.reportgen.util.ReportUtility"%>

<jsp:useBean id="reportScheduler" class="com.gridnode.pdip.app.reportgen.bean.ReportScheduler" scope="page" />
<jsp:useBean id="weeklyReportParam" class="com.gridnode.pdip.app.reportgen.value.WeeklyReportScheduleParam" scope="page" /> 


<%	
	String reportName				=	request.getParameter("reportName");
	String reportTargetPath			=	request.getParameter("reportTargetPath");
	String reportTemplate			=	request.getParameter("reportTemplate");
	String reportDataSource			=	request.getParameter("reportDataSource");
	String reportFormat				=	request.getParameter("reportFormat");

	String totalOccurence			=	request.getParameter("totalOccurence");

	String weekInterval				=	request.getParameter("weekInterval");
	String dayOfWeek				=	request.getParameter("dayOfWeek");

	String hours					=	request.getParameter("hours");
	String minutes					=	request.getParameter("minutes");

	String startDate				=	request.getParameter("startDate");

	String restrict					=	request.getParameter("retrict");

	String endDate					=	request.getParameter("endDate");
	String endHours					=	request.getParameter("endHours");
	String endMinutes				=	request.getParameter("endMinutes");

	if(totalOccurence==null) 
		totalOccurence	=	"";

%>

<!-- Settings for Weekly Report -->

<%	weeklyReportParam.setReportName(reportName);  
	weeklyReportParam.setReportTargetPath(reportTargetPath);
	weeklyReportParam.setReportTemplate(reportTemplate);
	weeklyReportParam.setReportDataSource(reportDataSource); 
	weeklyReportParam.setReportFormat(reportFormat);

	if(!totalOccurence.equals(""))
		weeklyReportParam.setTotalOccurence(new Integer(totalOccurence));
	else
		weeklyReportParam.setTotalOccurence(null);
		
	
	weeklyReportParam.setStartDate(ReportUtility.instance().stringToDate(startDate)); 
	weeklyReportParam.setStartDateTime(new Integer(hours),new Integer(minutes));

	Date dt = weeklyReportParam.getStartDate();

	ReportUtility reporUtility = ReportUtility.instance();

	dt = reporUtility.getDateWithWeekDay(dt,new Integer(dayOfWeek));
	
	weeklyReportParam.setStartDate(dt);
	
	out.println("  Date Modified to " + weeklyReportParam.getStartDate());

	weeklyReportParam.setEndDate(ReportUtility.instance().stringToDate(endDate));
	weeklyReportParam.setEndDateTime(new Integer(endHours),new Integer(endMinutes));

	weeklyReportParam.setDayOfWeek(new Integer(dayOfWeek));
	weeklyReportParam.setWeekInterval(new Integer(weekInterval));
%>

<!-- END Settings for Weekly Report -->

<% reportScheduler.scheduleWeeklyReport(weeklyReportParam); %>




<HTML>
<HEAD>
<TITLE> Schedule Report Generation </TITLE>
</HEAD>
<BODY>
<input type="hidden" name="Count" value="3">
<table align="center" valign="center" width="100%" >
<tr>
	<td align="center" colspan="2">
		<h3>Schedule Report Generation</h3>
	</td>
</tr>
</table>
</BODY>
</HTML>
