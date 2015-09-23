<%@page import="java.util.ArrayList,com.gridnode.pdip.app.reportgen.helpers.ReportGenFileOptions,com.gridnode.pdip.app.reportgen.value.IReportOptions"%>

<jsp:useBean id="report" class="com.gridnode.pdip.app.reportgen.bean.ReportGenerator" />

<% String reportTargetFileName=  request.getParameter("ReportName");%>
<% String reportTargetPath    =  request.getParameter("ReportTargetPath");%>
<% String reportTemplate      =  request.getParameter("ReportTemplate");%>
<% String reportDataSource    =  request.getParameter("ReportDatasource");%>
<% String reportExportType    =  request.getParameter("ExportType");%>
<% String reportOutput        =  request.getParameter("Output");
%>


<%
  boolean flag = false;
  String fileExtension = "";
 
  if(reportOutput.equals("Store and Display"))
  {
	  if(reportExportType.equals(IReportOptions.OUTPUT_PDF))
	  {
		flag = report.generatePDFReportAndSave(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
		fileExtension = ReportGenFileOptions.PDF_EXTENSION;
	  }
	  else if(reportExportType.equals(IReportOptions.OUTPUT_XML))
	  {
		flag = report.generateXMLReportAndSave(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
		fileExtension = ReportGenFileOptions.XML_EXTENSION;
	  }
	  else if(reportExportType.equals(IReportOptions.OUTPUT_HTML))
	  {
		flag = report.generateHTMLReportAndSave(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
		fileExtension = ReportGenFileOptions.HTML_EXTENSION;
	  }
  }
  else
  {
 	  if(reportExportType.equals(IReportOptions.OUTPUT_PDF))
	  {
		flag = report.generatePDFReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
		fileExtension = ReportGenFileOptions.PDF_EXTENSION;
	  }
	  else if(reportExportType.equals(IReportOptions.OUTPUT_XML))
	  {
		flag = report.generateXMLReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
		fileExtension = ReportGenFileOptions.XML_EXTENSION;
	  }
	  else if(reportExportType.equals(IReportOptions.OUTPUT_HTML))
	  {
		flag = report.generateHTMLReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
		fileExtension = ReportGenFileOptions.HTML_EXTENSION;
	  }
  }

  String fileName			  = report.getReportPathFromTempDir(reportTargetFileName+ReportGenFileOptions.PERIOD+fileExtension);


  String msg = "";
  if(flag)
    msg = "Report generated";
  else
    msg = "Report not generated";

%>

<HTML>
<HEAD>
<script>
  function openReport()
  {
    var flag=<%=flag%>;
    if(flag)
    {
		<% if(reportOutput.equals("Store and Display"))	{	%>

			window.open('<%=fileName%>','Report','status=no');

		<% } else {	%>

			window.open('ReportSaveOption.jsp?ReportName=<%=reportTargetFileName%>&FileExtension=<%=fileExtension%>&ReportTargetPath=<%=reportTargetPath%>&ExportType=<%=reportExportType%>','Report','status=no');

		<% } %>
    }
  }
</script>
<TITLE> Report Generation </TITLE>
</HEAD>
<BODY onLoad="javascript:openReport();">
<input type="hidden" name="Count" value="3">
<table align="center" valign="center" width="100%" >
<tr>
	<td align="center" colspan="2">
		<h3><%=msg%></h3>
	</td>
</tr>
</table>
</BODY>
</HTML>
