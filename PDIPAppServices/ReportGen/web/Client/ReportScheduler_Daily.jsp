<%@page import="java.util.Date,com.gridnode.pdip.app.reportgen.util.ReportUtility"%>

<jsp:useBean id="reportScheduler" class="com.gridnode.pdip.app.reportgen.bean.ReportScheduler" scope="page" />
<jsp:useBean id="dailyReportParam" class="com.gridnode.pdip.app.reportgen.value.DailyReportScheduleParam" scope="page" /> 


<%	
	String reportName				=	request.getParameter("reportName");
	String reportTargetPath			=	request.getParameter("reportTargetPath");
	String reportTemplate			=	request.getParameter("reportTemplate");
	String reportDataSource			=	request.getParameter("reportDataSource");
	String reportFormat				=	request.getParameter("reportFormat");

	String totalOccurence			=	request.getParameter("totalOccurence");

	String hours					=	request.getParameter("hours");
	String minutes					=	request.getParameter("minutes");

	String startDate				=	request.getParameter("startDate");

	String endDate					=	request.getParameter("endDate");
	String endHours					=	request.getParameter("endHours");
	String endMinutes				=	request.getParameter("endMinutes");

	String oneOffDate				=	request.getParameter("oneOffDate");
	String oneOffHours				=	request.getParameter("oneOffHours");
	String oneOffMinutes			=	request.getParameter("oneOffMinutes");


	if(totalOccurence==null) 
		totalOccurence	=	"";


%>

<!-- Settings for Daily Report -->
<%
	dailyReportParam.setReportName(reportName);  
	dailyReportParam.setReportTargetPath(reportTargetPath);
	dailyReportParam.setReportTemplate(reportTemplate);
	dailyReportParam.setReportDataSource(reportDataSource); 
	dailyReportParam.setReportFormat(reportFormat);

	if(!totalOccurence.equals(""))
		dailyReportParam.setTotalOccurence(new Integer(totalOccurence));
	else
		dailyReportParam.setTotalOccurence(null);
	

	dailyReportParam.setStartDate(ReportUtility.instance().stringToDate(startDate));
	dailyReportParam.setStartDateTime(new Integer(hours),new Integer(minutes));

	dailyReportParam.setEndDate(ReportUtility.instance().stringToDate(endDate)); dailyReportParam.setEndDateTime(new Integer(endHours),new Integer(endMinutes));
%> 

<!-- END Settings for Daily Report -->

<% reportScheduler.scheduleDailyReport(dailyReportParam); %>




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
