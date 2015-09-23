<%	String reportTargetFileName		=  request.getParameter("ReportName");
	String reportTargetPath			=  request.getParameter("ReportTargetPath");
	String fileExtension			=  request.getParameter("FileExtension");
	String reportExportType			=  request.getParameter("ExportType");
%>

<script>
function reportSave()
{	
	document.frm.action="ReportSave.jsp";
	document.frm.submit();
}
</script>

<HTML>
<HEAD>
<TITLE> Report Generation </TITLE>
</HEAD>
<BODY onLoad="javascript:openReport();">
<form name="frm" target="_parent">

<input type="hidden" name="ReportName" value="<%=reportTargetFileName%>">
<input type="hidden" name="ReportTargetPath" value="<%=reportTargetPath%>">
<input type="hidden" name="FileExtension" value="<%=fileExtension%>">
<input type="hidden" name="ExportType" value="<%=reportExportType%>">

<table align="center" valign="center" width="40%">
<tr>
	<td align="center" colspan="2">
		<input type="button" value="Save" onClick="javascript:reportSave();">
	</td>
	<td align="center" colspan="2">
		<input type="button" value="Cancel" onClick="javascript:parent.close();">
	</td>
</tr>
</table>
</BODY>
</HTML>
