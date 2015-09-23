<%@page import="java.io.File,com.gridnode.pdip.app.reportgen.value.IReportOptions,com.gridnode.pdip.app.reportgen.util.ReportUtility,com.gridnode.pdip.app.reportgen.helpers.ReportGenFileOptions"%>

<jsp:useBean id="report" class="com.gridnode.pdip.app.reportgen.bean.ReportGenerator" />

<%	String reportTargetFileName		=  request.getParameter("ReportName");
	String reportTargetPath			=  request.getParameter("ReportTargetPath");
	String fileExtension			=  request.getParameter("FileExtension");
	String reportExportType			=  request.getParameter("ExportType");
%>

<%	String strTempDir = ReportUtility.instance().getTempFolderPath();

	if(reportExportType.equals(IReportOptions.OUTPUT_HTML))
	{ 
	
		File mainFile = new File(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.HTML_EXTENSION);

		File[] subFiles = new File[1];
		
		subFiles[0] = new File(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.ZIP_EXTENSION);

		report.copyReportToFileServer(reportTargetFileName,reportTargetPath,mainFile,subFiles);	
	}
	else if(reportExportType.equals(IReportOptions.OUTPUT_XML))
	{
		File mainFile = new File(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.XML_EXTENSION);

		report.copyReportToFileServer(reportTargetFileName,reportTargetPath,mainFile,null);	


	}
	else if(reportExportType.equals(IReportOptions.OUTPUT_PDF))
	{
		File mainFile = new File(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.PDF_EXTENSION);

		report.copyReportToFileServer(reportTargetFileName,reportTargetPath,mainFile,null);	
	}


%>
	

<h3>Report Generated</h3>

<form name="frm">
	<input type="button" onClick="window.close();" value="Close">
</form>