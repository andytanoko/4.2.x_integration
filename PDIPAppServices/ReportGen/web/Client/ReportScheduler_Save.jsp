<%@page import="java.util.ArrayList,com.gridnode.pdip.app.reportgen.util.ReportUtility"%>

<jsp:useBean id="reportScheduler" class="com.gridnode.pdip.app.reportgen.bean.ReportScheduler" scope="page" />
<jsp:useBean id="dailyReportParam" class="com.gridnode.pdip.app.reportgen.value.DailyReportScheduleParam" scope="page" /> 
<jsp:useBean id="monthlyReportParam" class="com.gridnode.pdip.app.reportgen.value.MonthlyReportScheduleParam" scope="page" /> 
<jsp:useBean id="oneOffReportParam" class="com.gridnode.pdip.app.reportgen.value.OneOffReportScheduleParam" scope="page" /> 
<jsp:useBean id="weeklyReportParam" class="com.gridnode.pdip.app.reportgen.value.WeeklyReportScheduleParam" scope="page" /> 


<%	
	String reportName				=	request.getParameter("reportName");
	String reportTargetPath			=	request.getParameter("reportTargetPath");
	String reportTemplate			=	request.getParameter("reportTemplate");
	String reportDataSource			=	request.getParameter("reportDataSource");
	String reportFormat				=	request.getParameter("reportFormat");
	String totalOccurence			=	request.getParameter("totalOccurence");
	String Scheduled				=	request.getParameter("scheduled");

	String frequency				=	request.getParameter("frequency");

	String weekInterval				=	request.getParameter("weekInterval");
	String dayOfWeek				=	request.getParameter("dayOfWeek");

	String dayOfMonth				=	request.getParameter("dayOfMonth");
	String monthInterval			=	request.getParameter("monthInterval");

	String hours					=	request.getParameter("hours");
	String minutes					=	request.getParameter("minutes");

	String startDate				=	request.getParameter("startDate");

	String restrict					=	request.getParameter("retrict");

	String endDate					=	request.getParameter("endDate");
	String endHours					=	request.getParameter("endHours");
	String endMinutes				=	request.getParameter("endMinutes");

	String oneOffDate				=	request.getParameter("oneOffDate");
	String oneOffHours				=	request.getParameter("oneOffHours");
	String oneOffMinutes			=	request.getParameter("oneOffMinutes");


	if(totalOccurence==null) 
		totalOccurence	=	"";

	if(dayOfWeek==null) 
		dayOfWeek		=	"";	

	if(weekInterval==null)
		weekInterval	=	"";
	
	if(dayOfMonth==null) 
		dayOfMonth		=	"";	

	if(monthInterval==null)
		monthInterval	=	"";



%>

<!-- Settings for Daily Report -->
<jsp:setProperty name="dailyReportParam" property="reportName" value="<%=reportName%>" /> 
<jsp:setProperty name="dailyReportParam" property="reportTargetPath" value="<%=reportTargetPath%>" /> 
<jsp:setProperty name="dailyReportParam" property="reportTemplate" value="<%=reportTemplate%>" /> 
<jsp:setProperty name="dailyReportParam" property="reportDataSource" value="<%=reportDataSource%>" /> 
<jsp:setProperty name="dailyReportParam" property="reportFormat" value="<%=reportFormat%>" /> 


<%	if(!totalOccurence.equals(""))
		dailyReportParam.setTotalOccurence(new Integer(totalOccurence));
	else
		dailyReportParam.setTotalOccurence(null);
%>		

<%dailyReportParam.setStartDate(ReportUtility.instance().stringToDate(startDate));%> 
<%dailyReportParam.setStartDateTime(new Integer(hours),new Integer(minutes));%> 

<%dailyReportParam.setEndDate(ReportUtility.instance().stringToDate(endDate));%> <%dailyReportParam.setEndDateTime(new Integer(endHours),new Integer(endMinutes));%> 

<% reportScheduler.scheduleDailyReport(dailyReportParam); %>

<!-- END Settings for Daily Report -->


<!-- Settings for Weekly Report -->

<jsp:setProperty name="weeklyReportParam" property="reportName" value="<%=reportName%>" /> 
<jsp:setProperty name="weeklyReportParam" property="reportTargetPath" value="<%=reportTargetPath%>" /> 
<jsp:setProperty name="weeklyReportParam" property="reportTemplate" value="<%=reportTemplate%>" /> 
<jsp:setProperty name="weeklyReportParam" property="reportDataSource" value="<%=reportDataSource%>" /> 
<jsp:setProperty name="weeklyReportParam" property="reportFormat" value="<%=reportFormat%>" /> 

<%	if(!totalOccurence.equals(""))
		weeklyReportParam.setTotalOccurence(new Integer(totalOccurence));
	else
		weeklyReportParam.setTotalOccurence(null);
%>		


<%weeklyReportParam.setStartDate(ReportUtility.instance().stringToDate(startDate));%> 
<%weeklyReportParam.setStartDateTime(new Integer(hours),new Integer(minutes));%> 

<%weeklyReportParam.setEndDate(ReportUtility.instance().stringToDate(endDate));%> <%weeklyReportParam.setEndDateTime(new Integer(endHours),new Integer(endMinutes));%> 

<%
	if(!dayOfWeek.equals(""))
		weeklyReportParam.setDayOfWeek(new Integer(dayOfWeek));
	if(
		weeklyReportParam.setWeekInterval();%>

<% reportScheduler.scheduleWeeklyReport(weeklyReportParam); %>



<!-- END Settings for Weekly Report -->

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
