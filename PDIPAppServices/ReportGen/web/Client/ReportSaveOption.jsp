<%@page import="java.util.ArrayList,com.gridnode.pdip.app.reportgen.helpers.ReportGenFileOptions,com.gridnode.pdip.app.reportgen.value.IReportOptions"%>

<jsp:useBean id="report" class="com.gridnode.pdip.app.reportgen.bean.ReportGenerator" />

<%	String reportTargetFileName		=  request.getParameter("ReportName");
	String reportTargetPath			=  request.getParameter("ReportTargetPath");
	String fileExtension			=  request.getParameter("FileExtension");
	String reportExportType			=  request.getParameter("ExportType");
%>

<%  String fileName	= 		report.getReportPathFromTempDir(reportTargetFileName+ReportGenFileOptions.PERIOD+fileExtension);
%>



<frameset rows="90%,10%">
<frame name="top" src="<%=fileName%>" frameborder="0">
<frame name="bottom" src="ReportOptions.jsp?ReportName=<%=reportTargetFileName%>&ReportTargetPath=<%=reportTargetPath%>&FileExtension=<%=fileExtension%>&ExportType=<%=reportExportType%>" frameborder="0" scrollbars="no">
</frameset>