<%@page import="com.gridnode.pdip.app.reportgen.value.IReportOptions"%>

<HTML>
<HEAD>
<TITLE> Report Generation </TITLE>
<script language="JavaScript" src="date-picker.js"></script>
<script>
	function generateReport()
	{
		document.frmForm.action='ReportGenerator.jsp';
		document.frmForm.submit();
	}
	function setUpPath(path, type, id)
	{
		document.frmForm.elements[document.frmForm.FieldName.value].value = path;
	}
	function openFileNavigator(fieldName)
	{
		document.frmForm.FieldName.value=fieldName;
		window.open('/docservice/docservice/ServerBrowser.jsp','FileNavigator','status=no');
	}
	function openFolderNavigator(fieldName)
	{
		document.frmForm.FieldName.value=fieldName;
		window.open('/docservice/docservice/ServerBrowser.jsp?filter=folder','FileNavigator','status=no');
	}
</script>
</HEAD>
<BODY>
<form name="frmForm" method="POST">
<input type="hidden" name="FieldName">

<table align="center" valign="center" width="100%" >
	<tr>
		<td align="center" colspan="2">
			<h3>Online Report Generation</h3>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			&nbsp;
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
			<input type="text" name="ReportName">
		</td>
	</tr>
	<tr>
		<td>
			Store in Folder
		</td>
		<td>
			<input type="text" name="ReportTargetPath" value="">
			<input type="button" name="BrowseButton" onClick="javascript:openFolderNavigator('ReportTargetPath');" value="File Navigator">
		</td>
	</tr>
	<tr>
		<td>
			Report Template
		</td>
		<td>
			<input type="text" name="ReportTemplate" value="">
			<input type="button" name="BrowseButton" onClick="javascript:openFileNavigator('ReportTemplate');" value="File Navigator">
		</td>
	</tr>
	<tr>
		<tr>
		<td>
			Link to Datasource
		</td>
		<td>
			<input type="text" name="ReportDatasource" value="">
			<input type="button" name="BrowseButton" onClick="javascript:openFileNavigator('ReportDatasource');" value="File Navigator">
		</td>
	</tr>
		<tr>
		<td>
			Format
		</td>
		<td>
			<select name="ExportType">
				<option value="<%=IReportOptions.OUTPUT_HTML%>">HTML</option>
				<option value="<%=IReportOptions.OUTPUT_XML%>">XML</option>
				<option value="<%=IReportOptions.OUTPUT_PDF%>">PDF</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			Output
		</td>
		<td>
			<select name="Output">
				<option value="Store and Display">Store and Display</option>
				<option value="Display and Store">Display and Store</option>
			</select>
		</td>
	</tr>
		<tr>
		<td>
			&nbsp;
		</td>
		<td>
			<input type="button" value="Generate Report" onClick="javascript:generateReport();">
			<input type="button" value="Cancel" onClick="javascript:alert('Cancel the report generation');">
		</td>
	</tr>

</table>
</BODY>
</HTML>
