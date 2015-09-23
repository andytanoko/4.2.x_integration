<%@page import="java.util.Calendar,com.gridnode.pdip.app.reportgen.value.IReportOptions"%>

<HTML>
<HEAD>
<TITLE> Report Configuration </TITLE>
<script language="JavaScript" src="JavaScript/date-picker.js"></script>
<script>
	function submitForm()
	{

		var scheduled;
		var frequency;

		for(i=0;i<document.frmReportConfig.elements.length;i++)
		{
			if(document.frmReportConfig.elements[i].type=='radio' && document.frmReportConfig.elements[i].name=='Scheduled' && document.frmReportConfig.elements[i].checked)
			{
				scheduled = document.frmReportConfig.elements[i].value;
			}
			if(document.frmReportConfig.elements[i].type=='radio' && document.frmReportConfig.elements[i].name=='Frequency' && document.frmReportConfig.elements[i].checked)
			{
				frequency = document.frmReportConfig.elements[i].value;
			}
		}

		if(scheduled=='Periodic')
		{
		  if(frequency=='Daily')
			document.frmReportConfig.action = 'ReportScheduler_Daily.jsp';
		  else if(frequency=='Weekly')
			document.frmReportConfig.action = 'ReportScheduler_Weekly.jsp';	
		  else if(frequency=='Monthly')
			document.frmReportConfig.action = 'ReportScheduler_Monthly.jsp';
		}
		else if(scheduled=='One-Off')
			document.frmReportConfig.action = 'ReportScheduler_OneOff.jsp';

	  document.frmReportConfig.submit();
	}

	function setUpPath(path, type, id)
	{
		document.frmReportConfig.elements[document.frmReportConfig.FieldName.value].value = path;
	}

	function openFileNavigator(fieldName)
	{
		document.frmReportConfig.FieldName.value=fieldName;
		window.open('/docservice/docservice/ServerBrowser.jsp','FileNavigator','status=no');
	}

	function openFolderNavigator(fieldName)
	{
		document.frmReportConfig.FieldName.value=fieldName;
		window.open('/docservice/docservice/ServerBrowser.jsp?filter=folder','FileNavigator','status=no');
	}

</script>
</HEAD>
<BODY>
<form name="frmReportConfig" method="POST">
<input type="hidden" name="FieldName">

<table align="center" valign="center" width="100%" >
	<tr>
		<td align="center" colspan="2">
			<h3>Report Configuration</h3>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			Report Name
		</td>
		<td>
			<input type="text" name="reportName">
		</td>
	</tr>
	<tr>
		<td>
			Store in Folder
		</td>
		<td>
			<input type="text" name="reportTargetPath" value="">
			<input type="button" name="BrowseButton" onClick="javascript:openFolderNavigator('reportTargetPath');" value="File Navigator">
		</td>
	</tr>
	<tr>
		<td>
			Report Template
		</td>
		<td>
			<input type="text" name="reportTemplate" value="">
			<input type="button" name="BrowseButton" onClick="javascript:openFileNavigator('reportTemplate');" value="File Navigator">
		</td>
	</tr>
	<tr>
		<tr>
		<td>
			Link to Datasource
		</td>
		<td>
			<input type="text" name="reportDataSource" value="">
			<input type="button" name="BrowseButton" onClick="javascript:openFileNavigator('reportDataSource');" value="File Navigator">
		</td>
	</tr>
		<tr>
		<td>
			Format
		</td>
		<td>
			<select name="reportFormat">
				<option value="<%=IReportOptions.OUTPUT_HTML%>">HTML</option>
				<option value="<%=IReportOptions.OUTPUT_XML%>">XML</option>
				<option value="<%=IReportOptions.OUTPUT_PDF%>">PDF</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			Schedule Reports
		</td>
		<td>
			<table align="center" valign="center" width="100%" border="1">
				<tr>
					<td rowspan="9">
						<input type="radio" name="Scheduled" value="Periodic" checked>Periodic
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<input type="radio" name="Frequency" value="Daily" checked>Daily
					</td>
				</tr>
				<tr>
					<td>
						<input type="radio" name="Frequency" value="Weekly">Weekly
					</td>
					<td>On
						<select name="dayOfWeek">
							<option value="<%=Calendar.MONDAY%>">Monday</option>
							<option value="<%=Calendar.TUESDAY%>">Tuesday</option>
							<option value="<%=Calendar.WEDNESDAY%>">Wednesday</option>
							<option value="<%=Calendar.THURSDAY%>">Thursday</option>
							<option value="<%=Calendar.FRIDAY%>">Friday</option>
							<option value="<%=Calendar.SATURDAY%>">Saturday</option>
							<option value="<%=Calendar.SUNDAY%>">Sunday</option>
						</select>
					</td>
					<td>
						Every<input type="text" size="5" name="weekInterval" value="1">Week(s)
					</td>
				</tr>
				<tr>
					<td>
						<input type="radio" name="Frequency" value="Monthly">Monthly
					</td>
					<td>
						Day Of Month
						<select name="dayOfMonth">
						<%for(int i=1;i<=31;i++) {%>	
							<option value="<%=i%>"><%=i%></option>
						<% } %>	
						</select>
					</td>
					<td>
						Every<input type="text" name="monthInterval" size="5"  value="1">Month(s)
					</td>
				</tr>
				<tr>
					<td colspan="">At Time
					</td>
					<td colspan="2">
						HH <select name="hours">
						<%for(int i=0;i<=23;i++) {%>	
							<option value="<%=i%>"><%=i%></option>
						<% } %>	
						</select>
						MM <select name="minutes">
						<%for(int i=1;i<=59;i++) {%>	
							<option value="<%=i%>"><%=i%></option>
						<% } %>	
						</select>
					</td>
				</tr>
					<tr>
					<td rowspan="4" valign="top">
						Starting from
						<input type=text name="startDate">&nbsp;<a href="javascript:show_calendar('frmReportConfig.startDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="img/show-calendar.gif" border=0></a>

					</td>
					<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
					<td colspan="2">
						<input type="radio" name="Retrict" value="No">Do not end
					</td>
					</tr>
					<tr>
					<td colspan="2">
						<input type="radio" name="Retrict" value="Count">End After
						<input type="text" name="totalOccurence" value=""> occurences
					</td>
					</tr>
					<tr>
					<td colspan="2">
						<input type="radio" name="Retrict" value="On">End On
						<input type="text" name="endDate">&nbsp;<a href="javascript:show_calendar('frmReportConfig.endDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="img/show-calendar.gif" border=0></a>
						HH <select name="endHours">
						<%for(int i=0;i<=23;i++) {%>	
							<option value="<%=i%>"><%=i%></option>
						<% } %>	
						</select>
						MM <select name="endMinutes">
						<%for(int i=0;i<=59;i++) {%>	
							<option value="<%=i%>"><%=i%></option>
						<% } %>	
						</select>
					</td>
					</tr>
					<tr>
					<td>
						<input type="radio" name="Scheduled" value="One-Off">One-Off
					</td>
					<td colspan="3">
						On
						<input type="text" name="oneOffDate">&nbsp;<a href="javascript:show_calendar('frmReportConfig.oneOffDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="img/show-calendar.gif" border=0></a>
						HH <select name="oneOffHours">
						<% for(int i=0;i<=23;i++) {%>	
							<option value="<%=i%>"><%=i%></option>
						<% } %>	
						</select>
						MM <select name="oneOffMinutes">
						<%for(int i=0;i<=59;i++) {%>	
							<option value="<%=i%>"><%=i%></option>
						<% } %>	
						</select>
					</td>
					</tr>
			</table>
		</td>
	</tr>
		<tr>
		<td>
			&nbsp;
		</td>
		<td>
			<input type="button" value="Save Configuration" onClick="javascript:submitForm();">
			<input type="button" value="Test Configuration" onClick="javascript:alert('Generates the report and opens it in a new window');">
			<input type="button" value="Cancel" onClick="javascript:alert('Cancel the report configuration');">
		</td>
	</tr>

</table>
</form>
</BODY>
</HTML>
