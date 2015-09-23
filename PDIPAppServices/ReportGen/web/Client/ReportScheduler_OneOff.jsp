<%@page import="java.util.Date,com.gridnode.pdip.app.reportgen.util.ReportUtility"%>

<jsp:useBean id="reportScheduler" class="com.gridnode.pdip.app.reportgen.bean.ReportScheduler" scope="page" />
<jsp:useBean id="oneOffReportParam" class="com.gridnode.pdip.app.reportgen.value.OneOffReportScheduleParam" scope="page" /> 


<%	
	String reportName				=	request.getParameter("reportName");
	String reportTargetPath			=	request.getParameter("reportTargetPath");
	String reportTemplate			=	request.getParameter("reportTemplate");
	String reportDataSource			=	request.getParameter("reportDataSource");
	String reportFormat				=	request.getParameter("reportFormat");

	String hours					=	request.getParameter("hours");
	String minutes					=	request.getParameter("minutes");

	String startDate				=	request.getParameter("startDate");

	String endDate					=	request.getParameter("endDate");
	String endHours					=	request.getParameter("endHours");
	String endMinutes				=	request.getParameter("endMinutes");

	String oneOffDate				=	request.getParameter("oneOffDate");
	String oneOffHours				=	request.getParameter("oneOffHours");
	String oneOffMinutes			=	request.getParameter("oneOffMinutes");


	
%>

<!-- Settings for One Off Report -->

<%	
	oneOffReportParam.setReportName(reportName);  
	oneOffReportParam.setReportTargetPath(reportTargetPath);
	oneOffReportParam.setReportTemplate(reportTemplate);
	oneOffReportParam.setReportDataSource(reportDataSource); 
	oneOffReportParam.setReportFormat(reportFormat);

	oneOffReportParam.setOneOffDate(ReportUtility.instance().stringToDate(oneOffDate));
	oneOffReportParam.setOneOffDateTime(new Integer(oneOffHours),new Integer(oneOffMinutes));
%>

<!-- END Settings for One Off Report -->

<%	reportScheduler.scheduleOneOffReport(oneOffReportParam); %>




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
