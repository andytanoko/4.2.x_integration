<%@page import="java.util.Date,com.gridnode.pdip.app.reportgen.util.ReportUtility"%>

<jsp:useBean id="reportScheduler" class="com.gridnode.pdip.app.reportgen.bean.ReportScheduler" scope="page" />
<jsp:useBean id="monthlyReportParam" class="com.gridnode.pdip.app.reportgen.value.MonthlyReportScheduleParam" scope="page" /> 


<%	
	String reportName				=	request.getParameter("reportName");
	String reportTargetPath			=	request.getParameter("reportTargetPath");
	String reportTemplate			=	request.getParameter("reportTemplate");
	String reportDataSource			=	request.getParameter("reportDataSource");
	String reportFormat				=	request.getParameter("reportFormat");

	String totalOccurence			=	request.getParameter("totalOccurence");

	String dayOfMonth				=	request.getParameter("dayOfMonth");
	String monthInterval			=	request.getParameter("monthInterval");

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

<!-- Settings for Monthly Report -->

<%	monthlyReportParam.setReportName(reportName);  
	monthlyReportParam.setReportTargetPath(reportTargetPath);
	monthlyReportParam.setReportTemplate(reportTemplate);
	monthlyReportParam.setReportDataSource(reportDataSource); 
	monthlyReportParam.setReportFormat(reportFormat);

	if(!totalOccurence.equals(""))
		monthlyReportParam.setTotalOccurence(new Integer(totalOccurence));
	else
		monthlyReportParam.setTotalOccurence(null);
		
	
	monthlyReportParam.setStartDate(ReportUtility.instance().stringToDate(startDate)); 
	monthlyReportParam.setStartDateTime(new Integer(hours),new Integer(minutes));

	Date dt = monthlyReportParam.getStartDate();

	ReportUtility reporUtility = ReportUtility.instance();

	dt = reporUtility.getDateWithDayOfMonth(dt,new Integer(dayOfMonth));
	
	monthlyReportParam.setStartDate(dt);
	
	out.println("  Date Modified to " + monthlyReportParam.getStartDate());

	monthlyReportParam.setEndDate(ReportUtility.instance().stringToDate(endDate));
	monthlyReportParam.setEndDateTime(new Integer(endHours),new Integer(endMinutes));

	monthlyReportParam.setDayOfMonth(new Integer(dayOfMonth));
	monthlyReportParam.setMonthInterval(new Integer(monthInterval));
%>

<% reportScheduler.scheduleMonthlyReport(monthlyReportParam); %>


<!-- END Settings for Monthly Report -->

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
